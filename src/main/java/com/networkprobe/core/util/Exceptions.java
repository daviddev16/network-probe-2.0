package com.networkprobe.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.InstanceAlreadyExistsException;
import java.util.Objects;

public class Exceptions {

    private static final Logger LOGGER = LoggerFactory.getLogger(Exceptions.class);

    public static final InstanceAlreadyExistsException instanceAlreadyExistsException(Class<?> clazz) {
        return new InstanceAlreadyExistsException(String.format("Uma instância para \"%s\" já existe.", clazz
                .getName()));
    }

    public static void traceAndQuit(Exception exception, int exitCode) {
        trace(exception);
        Runtime.getRuntime().exit(exitCode);
    }

    public static void trace(Exception exception) {
        Objects.requireNonNull(exception, "A exceção não pode ser nula.");
        LOGGER.error("Tipo: " + exception.getClass().getSimpleName());
        LOGGER.error("Mensagem: " + exception.getMessage());
        LOGGER.error("Stacktrace:");
        for (StackTraceElement element : exception.getStackTrace()) {
            LOGGER.error("  {}", element.toString());
        }
    }
}
