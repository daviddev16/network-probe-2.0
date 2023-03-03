package com.networkprobe.core.server.policies;

import com.networkprobe.core.config.NetworkConfig;
import com.networkprobe.core.server.audit.Monitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DoSPolicy {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoSPolicy.class);

    public static boolean accept(final NetworkConfig config, String address) {
        if (Monitor.getMonitor().getMetadata(address)
                .getRequestTimes() > config.getServer().getRequestThreshold()) {
            LOGGER.warn("{} exceed the limit of request per second.", address);
            return false;
        }
        return true;
    }
}
