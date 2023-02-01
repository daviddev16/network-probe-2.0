package com.networkprobe.core.util;

import javax.management.InstanceAlreadyExistsException;

public class Exceptions {

    public static final InstanceAlreadyExistsException instanceAlreadyExistsException(Class<?> clazz) {
        return new InstanceAlreadyExistsException(String.format("Uma instância para \"%s\" já existe.", clazz
                .getName()));
    }
}
