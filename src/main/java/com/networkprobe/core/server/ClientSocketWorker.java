package com.networkprobe.core.server;

import com.networkprobe.core.threading.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientSocketWorker extends Worker {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientSocketWorker.class);

    private static final AtomicInteger ID = new AtomicInteger(-1);

    private final Socket clientSocket;

    public ClientSocketWorker(Socket clientSocket) {
        super("client-worker" + ID.incrementAndGet(), true, false);
        this.clientSocket = clientSocket;
    }

    @Override
    public void onBegin() {
        LOGGER.info("O servidor começou uma conexão com \"{}\".", clientSocket.getInetAddress().getHostAddress());
    }

    @Override
    public void onUpdate() {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            Scanner in = new Scanner(clientSocket.getInputStream());
            while (in.hasNextLine()) {
                String input = in.nextLine();
                if (input.equalsIgnoreCase("close")) {
                    break;
                }
                out.println("Server get: " + input.trim());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onStop() {
        LOGGER.info("Conexão encerrada.");
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

}
