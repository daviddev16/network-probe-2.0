package com.networkprobe.core.server;

import com.networkprobe.core.Environment;
import com.networkprobe.core.config.NetworkConfig;
import com.networkprobe.core.threading.Worker;
import com.networkprobe.core.util.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;

public class NetworkServer extends Worker {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkServer.class);

    public static final int DEFAULT_LISTEN_PORT = 14477;

    private ServerSocket serverSocket;

    public NetworkServer() {
        super("network-server", true, false);
    }

    @Override
    public void onBegin() {
        try {
            int listenPort = Environment.getConfig().getServer().getTcpPort();
            serverSocket = new ServerSocket(listenPort);
            LOGGER.info("Escutando na porta {} por conexões", listenPort);
        } catch (Exception e) {
            Exceptions.traceAndQuit(e, 0);
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
            Exceptions.traceAndQuit(e, 0);
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
