package com.networkprobe.core.model;

import com.networkprobe.core.enums.AddressFamily;

import java.util.InvalidPropertiesFormatException;
import java.util.Objects;

import static com.networkprobe.core.util.Validator.*;

public class RouterBuilder {

    private final Route route;

    protected RouterBuilder(String routeName) {
        route = new Route( validate(routeName, "routeName") );
    }

    protected RouterBuilder() {
        route = new Route();
    }

    public RouterBuilder name(String routeName) {
        route.setRouteName( validate(routeName, "routeName") );
        return this;
    }

    public RouterBuilder blockAddress(String address) throws InvalidPropertiesFormatException {
        route.getBlockedAddresses().add( validateAddress(address) );
        return this;
    }

    public RouterBuilder family(AddressFamily addressFamily, String remoteAddress) {
        Objects.requireNonNull(addressFamily, "Familia de endereço não pode ser nulo.");
        route.setAddressFamily( addressFamily );
        route.setRemoteAddress( validate(remoteAddress, "remoteAddress") );
        return this;
    }

    public RouterBuilder database(String databaseName) {
        route.setDatabaseName( validate(databaseName, "databaseName") );
        return this;
    }

    public Route get() {
        return route;
    }

    public static RouterBuilder create(String routeName) {
        return new RouterBuilder(routeName);
    }

    public static RouterBuilder create() {
        return new RouterBuilder();
    }

}
