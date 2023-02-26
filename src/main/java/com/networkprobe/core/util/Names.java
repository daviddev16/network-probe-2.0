package com.networkprobe.core.util;

import java.util.Random;

public class Names {

    private static final Random RAND = new Random();

    public static String createRouteName() {
        return createName("route");
    }

    public static String createRuleName() {
        return createName("rule");
    }

    public static String createName(String prefix) {
        return String.format("%s-%04d", prefix, RAND.nextInt(65536));
    }
}
