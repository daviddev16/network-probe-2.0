package com.networkprobe.core;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public final class Messages {

    public static final String HELLO                = "HELLO";
    public static final String NONE                 = "NONE";
    public static final long ATTEMPT_TIMEOUT        = TimeUnit.SECONDS.toMillis(2);
    public static final int MAX_ATTEMPTS            = 3;

    public static boolean checkMessage(@NotNull String message) {

        if (message.equals(HELLO) || message.equals(NONE))
            return true;

        return false;

    }


}
