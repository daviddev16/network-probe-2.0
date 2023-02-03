package com.networkprobe.core.server;

import com.networkprobe.core.threading.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;

public final class BroadcastServerListener extends Worker {

    public static final int LISTEN_PORT = 14476;

    private static final Logger LOGGER = LoggerFactory.getLogger(BroadcastServerListener.class);

    private DatagramSocket socket;

    public BroadcastServerListener() {
        super("bc-worker", true, false);
    }

    @Override
    public void onBegin() {
        LOGGER.info("Starting broadcast listener...");
        try {
            LOGGER.info("iniciando socket...");

            InetAddress inetAddress = InetAddress.getByName("0.0.0.0");
            SocketAddress socketAddress = new InetSocketAddress(inetAddress, LISTEN_PORT);
            socket = new DatagramSocket(null);
            socket.bind(socketAddress);
            LOGGER.info("Escutando na porta {}.", LISTEN_PORT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdate() {
        byte[] buffer = new byte[1024];
        final DatagramPacket packet = new DatagramPacket(buffer, 0, buffer.length);
        try {
            socket.receive(packet);
            LOGGER.info("Recebido: " + new String(packet.getData()).trim());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onStop() {
        LOGGER.info("broadcast listener stopped.");
    }
}
