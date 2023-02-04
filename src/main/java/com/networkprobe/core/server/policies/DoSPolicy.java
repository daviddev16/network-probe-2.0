package com.networkprobe.core.server.policies;

import com.networkprobe.core.Environment;
import com.networkprobe.core.config.NetworkConfig;
import com.networkprobe.core.server.audit.Monitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.DatagramPacket;

public class DoSPolicy {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoSPolicy.class);

    public static boolean accept(DatagramPacket packet) {

        String address = packet.getAddress().getHostAddress();

        int requestThreshold = ((NetworkConfig)Environment.access(Environment.NETWORK_CONFIG))
                .getRequestThreshold();

        int requestTimes = Monitor.getMonitor()
                .getMetadata(address).getRequestTimes();

        if (requestTimes > requestThreshold) {
            LOGGER.warn("{} ultrapassou o limite de requisições por segundo.", address);
            return false;
        }

        return true;
    }
}
