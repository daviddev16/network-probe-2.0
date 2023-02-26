package com.networkprobe.core.factory;

import com.networkprobe.core.config.NetworkConfig;
import com.networkprobe.core.enums.AddressFamily;
import com.networkprobe.core.model.Route;
import com.networkprobe.core.model.RouterBuilder;
import com.networkprobe.core.model.Rule;
import com.networkprobe.core.model.RuleBuilder;
import com.networkprobe.core.persistence.Yaml;
import com.networkprobe.core.server.BroadcastListener;
import com.networkprobe.core.server.NetworkServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.InstanceAlreadyExistsException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.networkprobe.core.util.Exceptions.*;
import static com.networkprobe.core.util.Validator.validate;

public class NetworkConfigFactory {

    private static NetworkConfigFactory instance;

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkConfigFactory.class);

    private NetworkConfigFactory() throws InstanceAlreadyExistsException {

        if (instance != null)
            throw instanceAlreadyExistsException(NetworkConfigFactory.class);

        instance = this;
    }

    public void createDefaultNetworkConfigFile(File configFile) throws IOException {

        validate(configFile, "configFile");

        NetworkConfig networkConfig = createDefaultNetworkConfig();

        if (configFile.exists()) {
            LOGGER.info("Não foi necessário gerar um arquivo de configuração padrão.");
            return;
        }

        configFile.createNewFile();
        Yaml.save(configFile, networkConfig);

        LOGGER.info("Configuração básica de NetworkConfig foi salva em \"{}\".", configFile.getPath());
    }

    public NetworkConfig createDefaultNetworkConfig() {

        NetworkConfig config = new NetworkConfig();

        List<Route> routes = Route.createRoutes(
                RouterBuilder.create()
                        .name("alterdata_pack_route")
                        .family(AddressFamily.LOCAL, Route.LOCAL_FAMILY_VALUE)
                        .database("ALTERDATA_PACK")
                        .get()
        );

        List<Rule> rules = Rule.createRules(
                RuleBuilder.create()
                        .order(Rule.DEFAULT_ORDER)
                        .name("alterdata_pack_rule")
                        //.command("cmd:pack0") -> para quando receber a comunicação TCP
                        .route("alterdata_pack_route")
                        .range("192.168.1.0", "192.168.1.100")
                        .get()
        );

        config.getServer().setRequestThreshold(20);
        config.getServer().setTcpPort(NetworkServer.DEFAULT_LISTEN_PORT);
        config.getServer().setUdpPort(BroadcastListener.DEFAULT_LISTEN_PORT);
        config.setRoutes(routes);
        config.setRules(rules);

        return config;
    }

    public static void setup() throws InstanceAlreadyExistsException {
        new NetworkConfigFactory();
    }

    public static NetworkConfigFactory getFactory() {
        return instance;
    }

}
