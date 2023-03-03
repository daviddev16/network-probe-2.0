package com.networkprobe.core.util;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Networking {

	public static final int MIN_BUFFER_SIZE = 8;
	public static final String ALL_INTERFACES_BROADCAST_ADDRESS = "255.255.255.255";

	public static NetworkInterface getNetworkInterfaceByAddress(String addressString)
			throws InvalidPropertiesFormatException, UnknownHostException, SocketException {
		Validator.validateAddress(addressString);

		InetAddress address = InetAddress.getByName(addressString);
		return NetworkInterface.getByInetAddress(address);
	}

	public static Set<String> getBroadcastAddresses(NetworkInterface networkInterface) {

		Validator.validate(networkInterface, "networkInterface");
		Set<String> broadcastAddresses = new LinkedHashSet<>();
		for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
			if (interfaceAddress.getBroadcast() != null) {
				broadcastAddresses.add(interfaceAddress.getBroadcast().getHostAddress());
			}
		}

		if (broadcastAddresses.isEmpty())
			broadcastAddresses.add(ALL_INTERFACES_BROADCAST_ADDRESS);

		return broadcastAddresses;
	}

	public static String getFirstBroadcast(Set<String> broadcastAddresses) {
		Validator.validate(broadcastAddresses, "broadcastAddresses");

		return broadcastAddresses.stream()
				.findFirst()
				.orElse(null);
	}

	public static DatagramPacket createMessagePacket(InetAddress inetAddress, int port, String message) {
		Validator.validate(inetAddress, "inetAddress");
		Validator.validate(message, "message");

		byte[] buffer = message.getBytes(StandardCharsets.UTF_8);
		return new DatagramPacket(buffer, 0, buffer.length, inetAddress, port);
	}

	public static DatagramPacket createABufferedPacket(int length) {
		byte[] buffer = new byte[ (length < MIN_BUFFER_SIZE) ? MIN_BUFFER_SIZE : length ];
		return new DatagramPacket(buffer, 0, buffer.length);
	}

	public static String getBufferedData(DatagramPacket packet) {
		Validator.validate(packet, "packet");
		return new String(packet.getData(), StandardCharsets.UTF_8).trim();
	}


}
