package com.networkprobe.core.init;

import com.networkprobe.core.annotation.LikellyStaticMethods;

import javax.management.InstanceAlreadyExistsException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.networkprobe.core.util.Exceptions.*;

@LikellyStaticMethods
public class Environment {

    public static final String NETWORK_CONFIG       = "networkConfigObject";

    public static final String DEFAULT_VALUE        = "default";

    private static Environment instance;

    private static volatile Map<String, Object> mapper;

    private Environment() throws InstanceAlreadyExistsException {

        if (instance != null)
            throw instanceAlreadyExistsException(Environment.class);

        mapper = Collections.synchronizedMap(new HashMap<>());
        instance = this;
    }

    public static void setup() throws InstanceAlreadyExistsException {
        new Environment();
    }

    public static void set(String key, Object value, boolean replace) {
        Objects.requireNonNull(key, "environment keys can not be null.");

        if (replace)
            getMapper().put(key, value);
        else
            getMapper().putIfAbsent(key, value);
    }

    public static void set(String key) {
        Objects.requireNonNull(key, "environment keys can not be null.");

        if (!getMapper().containsKey(key))
            getMapper().put(key, DEFAULT_VALUE);
    }

    @SuppressWarnings("unchecked")
    public static <E> E get(String key) {
        Objects.requireNonNull(key, "environment keys can not be null.");
        return (E) getMapper().getOrDefault(key, null);
    }

    public static boolean contains(String key) {
        Objects.requireNonNull(key, "environment keys can not be null.");
        return getMapper().containsKey(key);
    }

    private static Map<String, Object> getMapper() {
        return mapper;
    }

}
