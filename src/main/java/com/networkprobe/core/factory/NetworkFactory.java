package com.networkprobe.core.factory;

import com.networkprobe.core.*;
import com.networkprobe.core.enums.AddressFamily;
import com.networkprobe.core.enums.RuleType;
import com.networkprobe.core.model.Route;
import com.networkprobe.core.model.RouterBuilder;
import com.networkprobe.core.model.Rule;
import com.networkprobe.core.model.RuleBuilder;
import com.networkprobe.core.persistence.Yaml;

import javax.management.InstanceAlreadyExistsException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.networkprobe.core.util.Exceptions.*;
import static com.networkprobe.core.util.Names.*;
import static com.networkprobe.core.util.Validator.validate;


public class NetworkFactory {

    private static NetworkFactory instance;

    public NetworkFactory() throws InstanceAlreadyExistsException {

        if (instance != null)
            throw instanceAlreadyExistsException(NetworkFactory.class);

        instance = this;

    }

    public void createDefaultNetworkConfigFile(String fileName) throws IOException {

        File currentDirectory = new File(Environment.getString(Environment.CURRENT_DIRECTORY_KEY));
        File configFile = new File(currentDirectory, validate(fileName, "fileName"));
        NetworkConfig networkConfig = createDefaultNetworkConfig();

        if (configFile.exists())
            return; /*retornar um log de debug dizendo que já existe este arquivo */

        configFile.createNewFile();
        Yaml.save(configFile, networkConfig);
        System.out.println("Arquivo salvo.");

    }

    public NetworkConfig createDefaultNetworkConfig() {

        NetworkConfig config = new NetworkConfig();

        final String immobileRouteName = createName("immobile-route");
        final String packRouteName = createName("pack0-route");

        List<Route> routes = Route.createRoutes(
                /* route for immobile database connection */
                RouterBuilder.create()
                        .name(immobileRouteName)
                        .family(AddressFamily.LOCAL, Route.LOCAL_FAMILY_VALUE)
                        .database("ALTERDATA_IMMOBILE")
                        .get(),
                /* route for pack database connection */
                RouterBuilder.create()
                        .name(packRouteName)
                        .family(AddressFamily.REMOTE, "172.16.100.254")
                        .database("ALTERDATA_PACK")
                        .get()
        );

        List<Rule> rules = Rule.createRules(
                RuleBuilder.create()
                        .order(Rule.DEFAULT_ORDER)
                        //.command("cmd:immobile") -> para quando receber a comunicação TCP
                        .route(immobileRouteName)
                        .range("192.168.1.0", "192.168.1.254")
                        .type(RuleType.ACCEPT)
                        .get(),
                RuleBuilder.create()
                        .order(Rule.DEFAULT_ORDER)
                        //.command("cmd:pack0") -> para quando receber a comunicação TCP
                        .route(immobileRouteName)
                        .range("192.168.1.0", "192.168.1.100")
                        .type(RuleType.ACCEPT)
                        .get()
        );

        config.setRoutes(routes);
        config.setRules(rules);
        return config;
    }

    public static void init() throws InstanceAlreadyExistsException {
        new NetworkFactory();
    }

    public static NetworkFactory getFactory() {
        return instance;
    }

}
