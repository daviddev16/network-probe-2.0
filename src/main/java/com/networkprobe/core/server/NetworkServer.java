package com.networkprobe.core.server;

import com.networkprobe.core.threading.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;

public class NetworkServer extends Worker {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkServer.class);

    public static final int LISTEN_PORT = 14477;

    private ServerSocket serverSocket;

    public NetworkServer() {
        super("network-server", true, false);
    }

    @Override
    public void onBegin() {
        try {
            serverSocket = new ServerSocket(LISTEN_PORT);
            LOGGER.info("Listening on port {} for connections.", LISTEN_PORT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdate() {
        try {
            synchronized (serverSocket) {
                Socket clientSocket = serverSocket.accept();
                ClientSocketWorker clientSocketWorker = new ClientSocketWorker(clientSocket);
                clientSocketWorker.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
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
