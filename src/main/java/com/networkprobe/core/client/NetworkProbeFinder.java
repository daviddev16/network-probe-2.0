package com.networkprobe.core.client;

import com.networkprobe.core.init.Environment;
import com.networkprobe.core.server.BroadcastListener;
import com.networkprobe.core.threading.Worker;
import com.networkprobe.core.util.Exceptions;
import com.networkprobe.core.util.Networking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class NetworkProbeFinder extends Worker {

    public static final Logger LOGGER               = LoggerFactory.getLogger(NetworkProbeFinder.class);
    public static final Object LOCK                 = new Object();

    public static final String HELLO_MESSAGE        = "hello";
    public static final String FAILED_MESSAGE       = "failed";

    public static final long ATTEMPT_TIMEOUT        = TimeUnit.SECONDS.toMillis(2);
    public static final int MAX_ATTEMPTS            = 3;

    private AtomicReference<String> reponse = new AtomicReference<>();

    private volatile DatagramSocket datagramSocket;
    private volatile String broadcastAddress;
    private int attempts = 0;

    public NetworkProbeFinder() {
        super("network-probe-finder", true, false);
    }

    @Override
    protected void onBegin() {

        final String bindAddress = Environment.get(Environment.BIND_ADDRESS);

        try {

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

            Thread.sleep(ATTEMPT_TIMEOUT);

            LOGGER.info("ATTEMPT: Sending hello packet to the network [{}/{}]", attempts + 1, MAX_ATTEMPTS);

            InetAddress broadcastInetAddress = InetAddress.getByName(broadcastAddress);

            DatagramPacket helloPacket = Networking.createMessagePacket(broadcastInetAddress,
                    BroadcastListener.DEFAULT_LISTEN_PORT, HELLO_MESSAGE);

            datagramSocket.send(helloPacket);

            attempts++;

            if (attempts == MAX_ATTEMPTS) {
                stop();
                synchronized (LOCK) {
                    LOCK.notify();
                }
            }

        } catch (Exception e) {
            Exceptions.unexpected(e, 1);
        }
    }

    @Override
    protected void onStop() {}

    public String getResponse() {
        return reponse.get();
    }

}


