package com.networkprobe.core;

import com.networkprobe.core.annotation.LikellyStaticMethods;

import javax.management.InstanceAlreadyExistsException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.networkprobe.core.util.Exceptions.instanceAlreadyExistsException;

@LikellyStaticMethods
public final class Environment {

    public static final String CURRENT_DIRECTORY = "currentDirectory";
    public static final String NETWORK_CONFIG_FILE = "networkConfigFile";
    public static final String NETWORK_CONFIG = "networkConfigObject";

    private static Environment instance;

    private volatile Map<String, Object> envMap;

    public Environment() throws InstanceAlreadyExistsException {

        if (instance != null)
            throw instanceAlreadyExistsException(Environment.class);

        instance = this;
        envMap = Collections.synchronizedMap(new HashMap<>());
    }

    public static void init() throws InstanceAlreadyExistsException {
        new Environment();
    }

    public static void put(String key, Object value) {
        getEnvMap().put(key, value);
    }

    public static <E> E get(String key) {
        return (E) getEnvMap().getOrDefault(key, null);
    }

    public static String getString(String key) {
        return (String) getEnvMap().getOrDefault(key, null);
    }

    public static boolean exists(String key) {
        return getEnvMap().containsKey(key);
    }

    public static Map<String, Object> getEnvMap() {
        return instance.envMap;
    }


}
