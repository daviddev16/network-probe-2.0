package com.networkprobe.core.client;

import com.networkprobe.core.Messages;
import com.networkprobe.core.Environment;
import com.networkprobe.core.server.BroadcastListener;
import com.networkprobe.core.threading.Worker;
import com.networkprobe.core.util.Exceptions;
import com.networkprobe.core.util.Networking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class NetworkProbeFinder extends Worker {

    public static final Logger LOGGER               = LoggerFactory.getLogger(NetworkProbeFinder.class);
    public static final Object LOCK                 = new Object();

    private AtomicReference<String> response = new AtomicReference<>(Messages.NONE);

    private volatile DatagramSocket datagramSocket;
    private volatile String broadcastAddress;
    private int attempts = 0;

    public NetworkProbeFinder() {
        super("network-probe-finder", true, false);
    }

    @Override
    protected void onBegin() {
        try {
            final String bindAddress = Environment.get(Environment.BIND_ADDRESS);
            NetworkInterface networkInterface = Networking.getNetworkInterfaceByAddress(bindAddress);
            Set<String> broadcastAddresses = Networking.getBroadcastAddresses(networkInterface);
            broadcastAddress = Networking.getFirstBroadcast(broadcastAddresses);

            if (broadcastAddress == null || broadcastAddress.isEmpty())
                throw new NullPointerException("Could not find a broadcast address for the specified interface");

            LOGGER.info("The broadcast address is \"{}\"", broadcastAddress);

            LOGGER.info("Starting network-probe finder socket...");

            datagramSocket = new DatagramSocket(null);
            datagramSocket.bind(new InetSocketAddress(bindAddress, 0));

            LOGGER.info("Socket bind to \"{}\" on port \"{}\"", bindAddress, datagramSocket.getLocalPort());
            LOGGER.info("Starting sequence to find a network-probe server on \"{}\" network", bindAddress);
        } catch (Exception e) {
            Exceptions.unexpected(e, 1);
        }
    }

    @Override
    protected void onUpdate() {
        try {
            attempts++;
            Thread.sleep(Messages.ATTEMPT_TIMEOUT);
            LOGGER.info("ATTEMPT: Sending hello packet to the network [{}/{}]", attempts, Messages.MAX_ATTEMPTS);

            DatagramPacket helloPacket = Networking.createMessagePacket(InetAddress.getByName(broadcastAddress),
                        BroadcastListener.DEFAULT_LISTEN_PORT, Messages.HELLO);

            datagramSocket.send(helloPacket);

            Thread socketListenerThread = createListenerThread();
            socketListenerThread.setName("socket-receiver-thread-attempt#" + attempts);
            socketListenerThread.start();

            if (attempts == Messages.MAX_ATTEMPTS) {
                stop();
            }

        } catch (InterruptedException e) {
            /* ignore */
        } catch (Exception e) {
            Exceptions.unexpected(e, 1);
        }
    }

    /* it doesn't need to be a Worker in this case */
    private Thread createListenerThread() {
        Thread listenerThread = new Thread(() ->
        {
            DatagramPacket comingPacket = Networking.createABufferedPacket(0);
            try {
                synchronized (datagramSocket) {
                    datagramSocket.receive(comingPacket);
                    String message = Networking.getBufferedData(comingPacket);

                    if (!Messages.checkMessage(message)) {
                        LOGGER.warn("Unknown message data.");
                        return;
                    }

                    if (message.equals(Messages.HELLO)) {
                        response.set(comingPacket.getAddress().getHostAddress());
                        NetworkProbeFinder.this.stop();
                    }
                }
            } catch (Exception e) {
                Exceptions.unexpected(e, 10);
            }
        });
        return listenerThread;
    }

    @Override
    protected void onStop() {
        synchronized (LOCK) {
            LOCK.notify();
        }
    }

    public String getResponse() {
        return response.get();
    }



}


