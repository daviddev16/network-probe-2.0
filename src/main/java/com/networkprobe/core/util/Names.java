package com.networkprobe.core.util;

import java.util.UUID;

public class Names {

    public static String createRouteName() {
        return createName("route");
    }

    public static String createRuleName() {
        return createName("rule");
    }

    public static String createName(String prefix) {
        return prefix + "-" + UUID.randomUUID();
    }
}
