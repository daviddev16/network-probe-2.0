package com.networkprobe.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.InstanceAlreadyExistsException;
import java.util.Objects;

public class Exceptions {

    private static final Logger LOGGER = LoggerFactory.getLogger(Exceptions.class);

    public static final InstanceAlreadyExistsException instanceAlreadyExistsException(Class<?> clazz) {
        return new InstanceAlreadyExistsException(String.format("An instance of \"%s\" already exist.", clazz
                .getName()));
    }

    public static void unexpected(Exception exception, int exitCode) {
        Objects.requireNonNull(exception, "Exception can not be null.");
        LOGGER.error("An unexpected exception was thrown, quiting.", exception);
        Runtime.getRuntime().exit(exitCode);
    }

}
