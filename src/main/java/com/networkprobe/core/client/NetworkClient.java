package com.networkprobe.core.client;

import com.networkprobe.core.threading.Worker;
import com.networkprobe.core.util.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetworkClient extends Worker {

    public static final Logger LOGGER = LoggerFactory.getLogger(NetworkClient.class);

    public NetworkClient() {
        super("network-client", true, false);
    }

    @Override
    public void onBegin() {
        try {
            NetworkProbeFinder finder = new NetworkProbeFinder();
            finder.start();
            synchronized (NetworkProbeFinder.LOCK) {
                LOGGER.info("Waiting for the finder response...");
                NetworkProbeFinder.LOCK.wait();
                System.out.println("Response GET: " + finder.getResponse());
            }

        } catch (Exception e) {
            Exceptions.unexpected(e, 1);
        }
    }

    @Override
    protected void onUpdate() {

    }

    @Override
    protected void onStop() {

    }

}
