package com.networkprobe.core.server;

import com.networkprobe.core.Environment;
import com.networkprobe.core.Messages;
import com.networkprobe.core.config.NetworkConfig;
import com.networkprobe.core.server.audit.Monitor;
import com.networkprobe.core.server.policies.DoSPolicy;
import com.networkprobe.core.threading.Worker;
import com.networkprobe.core.util.Exceptions;
import com.networkprobe.core.util.Networking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;

public final class BroadcastListener extends Worker {

    private static final Logger LOGGER          = LoggerFactory.getLogger(BroadcastListener.class);
    public static final int DEFAULT_LISTEN_PORT = 14476;

    private NetworkConfig networkConfig = Environment.get(Environment.NETWORK_CONFIG);
    private DatagramSocket datagramSocket;

    public BroadcastListener() {
        super("broadcast-listener", true, false);
    }

    @Override
    public void onBegin() {
        try {
            String bindAddress = Environment.get(Environment.BIND_ADDRESS);
            InetAddress inetAddress = InetAddress.getByName(bindAddress);
            SocketAddress socketAddress = new InetSocketAddress(inetAddress, DEFAULT_LISTEN_PORT);
            datagramSocket = new DatagramSocket(socketAddress);
            LOGGER.info("Listening on port {} for broadcast signals.", DEFAULT_LISTEN_PORT);
        } catch (Exception e) {
            Exceptions.unexpected(e, 1);
        }
    }

    private void process(DatagramPacket packet) throws IOException {

        String address = packet.getAddress().getHostAddress();
        Monitor.getMonitor().registerOrUpdate(address);

        if (!DoSPolicy.accept(networkConfig, address))
            return;

        String dataMessage = Networking.getBufferedData(packet);

        if (!Messages.checkMessage(dataMessage)) {
            LOGGER.warn("Unknown message data.");
        }
        else if (dataMessage.equals(Messages.HELLO)) {
            datagramSocket.send(Networking.createMessagePacket(packet.getAddress(),
                    packet.getPort(), Messages.HELLO));
        }
    }

    @Override
    public void onUpdate() {
        try {
            final DatagramPacket packet = Networking.createABufferedPacket(0);
            datagramSocket.receive(packet);
            process(packet);
        } catch (Exception e) {
            Exceptions.unexpected(e, 1);
        }
    }

    @Override
    public void onStop() {
        try {
            if (datagramSocket != null)
                datagramSocket.close();
        } catch (Exception e) { /* ignore */ }
    }

}
