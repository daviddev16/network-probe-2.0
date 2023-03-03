package com.networkprobe.core;

import java.util.Objects;

public enum ServiceType {

    CLIENT, SERVER;

    public static boolean isValid(String value) {
        Objects.requireNonNull(value, "ServiceType string value is null.");
        return value.equalsIgnoreCase("client") ||
                value.equalsIgnoreCase("server");
    }

    public static ServiceType parse(String value) {
        Objects.requireNonNull(value, "ServiceType string value is null.");
        return valueOf(value.trim().toUpperCase());
    }

}
