package com.networkprobe.core.server;

import com.networkprobe.core.Environment;
import com.networkprobe.core.config.NetworkConfig;
import com.networkprobe.core.server.audit.Monitor;
import com.networkprobe.core.server.policies.DoSPolicy;
import com.networkprobe.core.threading.Worker;
import com.networkprobe.core.util.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.nio.charset.Charset;

public final class BroadcastListener extends Worker {

    private static final Logger LOGGER = LoggerFactory.getLogger(BroadcastListener.class);

    public static final int MESSAGE_MAX_LENGTH = 8;
    public static final int DEFAULT_LISTEN_PORT = 14476;

    private DatagramSocket socket;

    public BroadcastListener() {
        super("bc-listener", true, false);
    }

    @Override
    public void onBegin() {
        try {
            InetAddress inetAddress = InetAddress.getByName("0.0.0.0");
            int listenPort = Environment.getConfig().getServer().getUdpPort();
            SocketAddress socketAddress = new InetSocketAddress(inetAddress, listenPort);
            socket = new DatagramSocket(socketAddress);
            LOGGER.info("Escutando na porta {} por broadcasts", listenPort);
        } catch (Exception e) {
            Exceptions.traceAndQuit(e, 0);
        }
    }

    @Override
    public void onUpdate() {
        try {
            final DatagramPacket packet = createDatagramPacket();
            socket.receive(packet);
            process(packet);
        } catch (Exception e) {
            Exceptions.traceAndQuit(e, 0);
        }
    }

    @Override
    public void onStop() {
        if (socket != null)
            socket.close();
    }

    private void process(DatagramPacket packet) {

        if (!DoSPolicy.accept(packet))
            return;

        String data = new String(packet.getData(), Charset.forName("UTF-8"));
        System.out.println(data);

        Monitor.getMonitor().registerOrUpdate(packet);
    }

    private DatagramPacket createDatagramPacket() {
        byte[] buffer = new byte[MESSAGE_MAX_LENGTH];
        return new DatagramPacket(buffer, 0, buffer.length);
    }


}
