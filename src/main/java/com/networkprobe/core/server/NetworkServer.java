package com.networkprobe.core.server;

import com.networkprobe.core.config.NetworkConfig;
import com.networkprobe.core.init.Environment;
import com.networkprobe.core.threading.Worker;
import com.networkprobe.core.util.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;

public class NetworkServer extends Worker {

    private static final Logger LOGGER          = LoggerFactory.getLogger(NetworkServer.class);
    public static final int DEFAULT_LISTEN_PORT = 14477;

    private ServerSocket serverSocket;

    public NetworkServer() {
        super("network-server", true, false);
    }

    @Override
    public void onBegin() {
        try {
            NetworkConfig networkConfig = Environment.get(Environment.NETWORK_CONFIG);
            String bindAddress = Environment.get(Environment.BIND_ADDRESS);
            InetAddress inetAddress = InetAddress.getByName(bindAddress);
            serverSocket = new ServerSocket(DEFAULT_LISTEN_PORT, 1024, inetAddress);
            LOGGER.info("Listen on port {} for TCP connections.", DEFAULT_LISTEN_PORT);
        } catch (Exception e) {
            Exceptions.unexpected(e, 1);
        }
    }

    @Override
    public void onUpdate() {
        try {
            synchronized (serverSocket) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();
            }
        } catch (Exception e) {
            Exceptions.unexpected(e, 1);
        }
    }

    @Override
    public void onStop() {
        try {
            if (serverSocket != null)
                serverSocket.close();
        } catch (Exception e) { /* ignore */ }
    }
}
