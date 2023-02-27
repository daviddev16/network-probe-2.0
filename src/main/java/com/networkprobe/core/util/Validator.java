package com.networkprobe.core.util;

import java.util.InvalidPropertiesFormatException;
import java.util.regex.Pattern;

public class Validator {

    private static final String IPV4_REGEX_PATTERN =
            "^((25[0-5]|(2[0-4]|1[0-9]|[1-9]|)[0-9])(\\.(?!$)|$)){4}$";

    public static String validateAddress(String address) throws InvalidPropertiesFormatException {

        if (!Pattern.matches(IPV4_REGEX_PATTERN, validate(address, "address")))
            throw new InvalidPropertiesFormatException(String.format("Invalid IPv4 format. [address=%s]", address));

        return address;
    }

    public static String validate(String string, String valueIdentifier) {

        if (string == null || string.isEmpty())
            throw new NullPointerException(String.format("Null or empty values are not allowed on this field. " +
                    "[field=\"%s\"].", valueIdentifier));

        return string;
    }

    public static <E> E validate(E data, String valueIdentifier) {

        if (data == null)
            throw new NullPointerException(String.format("This object can not be null. " +
                    "[object=\"%s\"].", valueIdentifier));

        return data;
    }

}
