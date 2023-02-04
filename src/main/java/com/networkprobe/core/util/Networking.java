package com.networkprobe.core.util;

import java.util.regex.Pattern;

public class Networking {

	public static int[] toIntegers(String address) {

		String[] octets = address.replaceAll("\\s+", "")
				.split(Pattern.quote("."));

		if (address == null || address.isEmpty())
			throw new RuntimeException("Parâmetro \"address\" nulo ou em branco.");

		else if (octets.length != 4)
			throw new RuntimeException("Formato de endereço IPv4 inválido.");

		int[] values = new int[4];

		for (int i = 0; i < 4; i++) {
			values[i] = validateOctet(Integer.parseInt(octets[i]));
		}

		return values;
	}

	private static int validateOctet(int octetValue) {
		if (octetValue < 0 || octetValue > 255)
			throw new RuntimeException(String.format("Octeto inválido (0 > \"%s\" > 256)", octetValue));
		return octetValue;
	}

	public static String toString(long addressValue) {
		return  ((addressValue >> 24 ) & 0xFF ) + "." + 
				((addressValue >> 16 ) & 0xFF ) + "." + 
				(( addressValue >> 8 ) & 0xFF ) + "." + 
				(addressValue & 0xFF);
	}

	public static long toLong(String address) {
		int[] octets = toIntegers(address);
		int addressValue = ((octets[0] << 24));
		addressValue |= ((octets[1] << 16));
		addressValue |= ((octets[2] << 8));
		addressValue |= ((octets[3]));
		return addressValue;
	}
}
