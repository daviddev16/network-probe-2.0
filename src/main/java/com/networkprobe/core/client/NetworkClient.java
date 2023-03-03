package com.networkprobe.core.client;

import com.networkprobe.core.threading.Worker;
import com.networkprobe.core.util.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetworkClient extends Worker {

    public static final Logger LOGGER = LoggerFactory.getLogger(NetworkClient.class);

    private final NetworkProbeFinder finder = new NetworkProbeFinder();

    public NetworkClient() {
        super("network-client", true, false);
    }

    @Override
    public void onBegin() {
        try {

            LOGGER.info("Starting finder...");
            finder.start();

            synchronized (NetworkProbeFinder.LOCK) {
                NetworkProbeFinder.LOCK.wait();
            }

            String serverAddress = finder.getResponse();
            LOGGER.info("Network-probe server service is hosted in \"{}\" address.", serverAddress);

        } catch (Exception e) {
            Exceptions.unexpected(e, 1);
        }
    }

    @Override
    protected void onUpdate() {}

    @Override
    protected void onStop() {}

}
