package com.networkprobe.core;

import com.networkprobe.core.annotation.LikellyStaticMethods;
import com.networkprobe.core.config.NetworkConfig;

import javax.management.InstanceAlreadyExistsException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.networkprobe.core.util.Exceptions.*;

@LikellyStaticMethods
public final class Environment {

    public static final String NETWORK_CONFIG       = "networkConfigObject";

    private static Environment instance;

    private volatile Map<String, Object> envMap;

    private Environment() throws InstanceAlreadyExistsException {

        if (instance != null)
            throw instanceAlreadyExistsException(Environment.class);

        instance = this;
        envMap = Collections.synchronizedMap(new HashMap<>());
    }

    public static void put(String key, Object value) {
        instance.envMap.put(key, value);
    }

    public static <E> E get(String key) {
        return (E) instance.envMap.getOrDefault(key, null);
    }

    public static String getString(String key) {
        return (String) instance.envMap.getOrDefault(key, "");
    }

    public static NetworkConfig getConfig() {
        return (NetworkConfig) get(NETWORK_CONFIG);
    }

    public static boolean exists(String key) {
        return instance.envMap.containsKey(key);
    }

    public static void setup() throws InstanceAlreadyExistsException {
        new Environment();
    }

}
