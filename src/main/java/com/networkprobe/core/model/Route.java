package com.networkprobe.core.model;

import com.networkprobe.core.enums.AddressFamily;
import com.networkprobe.core.util.Names;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.networkprobe.core.util.Validator.validate;

public final class Route {

	public static final String LOCAL_FAMILY_VALUE = "-";
	public static final String UNDEFINED_ROUTE_VALUE = "[?undefined?]";

	private String routeName;
	private Set<String> blockedAddresses;
	private AddressFamily addressFamily;
	private String databaseName;
	private String remoteAddress;

	public Route(String routeName) {
		this(routeName, new HashSet<>(),
				AddressFamily.LOCAL, "ALTERDATA_DATABASE_NAME", LOCAL_FAMILY_VALUE);
	}

	public Route(String routeName, Set<String> blockedAddresses, AddressFamily addressFamily,
				 String databaseName, String remoteAddress) {
		this.routeName = routeName;
		this.blockedAddresses = blockedAddresses;
		this.addressFamily = addressFamily;
		this.databaseName = databaseName;
		this.remoteAddress = remoteAddress;
	}

	public Route() {
		this(Names.createRouteName());
	}

	public String getRouteName() {
		return routeName;
	}

	protected void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public Set<String> getBlockedAddresses() {
		return blockedAddresses;
	}

	protected void setBlockedAddresses(Set<String> blockedAddresses) {
		this.blockedAddresses = blockedAddresses;
	}

	public AddressFamily getAddressFamily() {
		return addressFamily;
	}

	protected void setAddressFamily(AddressFamily addressFamily) {
		this.addressFamily = addressFamily;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	protected void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	protected void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	public static final List<Route> createRoutes(Route route0, Route... route1) {
		List<Route> routes = new ArrayList<>();
		routes.add(validate(route0, "Route"));
		for (Route route : validate(route1, "Route[]")) {
			routes.add(validate(route, "Route em Route[]"));
		}
		return routes;
	}

}
