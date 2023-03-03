package com.networkprobe.core.server.audit;

import com.networkprobe.core.threading.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.InstanceAlreadyExistsException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.networkprobe.core.util.Exceptions.*;

public class Monitor extends Worker {

    private static Monitor instance;

    private static final Logger LOGGER = LoggerFactory.getLogger(Monitor.class);

    public static final long CACHE_TTL = TimeUnit.SECONDS.toMillis(1L);

    public static final AtomicBoolean DEBUG = new AtomicBoolean(false);

    private final Map<String, RequestMetadata> metadata;

    private Monitor() throws InstanceAlreadyExistsException {
        super("monitor", true, false);

        if (instance != null)
            throw instanceAlreadyExistsException(Monitor.class);

        metadata = Collections.synchronizedMap(new HashMap<>());
        instance = this;
    }

    @Override
    public void onBegin() {
        LOGGER.info("Starting monitor");
    }

    @Override
    public void onUpdate() {
        try {
            Thread.sleep(CACHE_TTL);
            clearMetadata();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onStop() {
        LOGGER.info("Stopping monitor");
        clearMetadata();
    }

    public void registerOrUpdate(String address) {

        if (!metadata.containsKey(address)) {
            RequestMetadata clientMetadata = new RequestMetadata();
            metadata.put(address, clientMetadata);
            debug("\"{}\" has been added to the motinor mapper.", address);
        }

        metadata.get(address).count();
        debug("\"{}\" requested \"{}\" times in 1 minute cycle.", address, (""+metadata
                .get(address).getRequestTimes()));
    }

    public void clearMetadata() {
        metadata.clear();
    }

    public RequestMetadata getMetadata(String address) {
        return metadata.get(address);
    }

    public static Monitor getMonitor() {
        return instance;
    }

    private void debug(String message, String... args) {
        if (DEBUG.get()) {
            LOGGER.debug(message, args);
        }
    }

    public static void setup() throws InstanceAlreadyExistsException {
        Monitor monitor = new Monitor();
        monitor.start();
    }

    public static final class RequestMetadata {

        private AtomicInteger requestTimes;

        public void count() {
            if (requestTimes == null)
                requestTimes = new AtomicInteger(0);

            requestTimes.incrementAndGet();
        }

        public int getRequestTimes() {
            return requestTimes.get();
        }

    }

}
