package com.networkprobe.main;

import com.networkprobe.core.Environment;
import com.networkprobe.core.factory.NetworkFactory;
import com.networkprobe.core.config.NetworkConfig;
import com.networkprobe.core.persistence.Yaml;
import com.networkprobe.core.server.BroadcastServerListener;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.InstanceAlreadyExistsException;
import java.io.File;
import java.io.IOException;

import static com.networkprobe.core.Environment.*;

public class Launcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(Launcher.class);

    public static void main(String[] args) throws InstanceAlreadyExistsException, IOException {
        LOGGER.info("Iniciando");
        LOGGER.info("Carregando ambiente...");
        Environment.init();
        NetworkFactory.init();
        LOGGER.info("Carregando variáveis...");
        setupAllVariables();
        enableWorkers();
        Runtime.getRuntime().addShutdownHook(new Thread(()-> {}));/* later */
    }

    public static void enableWorkers() {
        LOGGER.info("Carregando serviços...");
        BroadcastServerListener broadcastServerListener = new BroadcastServerListener();
        broadcastServerListener.start();
    }


    public static void setupAllVariables() throws IOException {

        put(CURRENT_DIRECTORY, System.getProperty("user.dir"));

        File networkConfigFile = new File(getString(CURRENT_DIRECTORY), NetworkConfig.DEFAULT_CONFIG_FILENAME);

        if (!networkConfigFile.exists())
            NetworkFactory.getFactory().createDefaultNetworkConfigFile(NetworkConfig.DEFAULT_CONFIG_FILENAME);

        put(NETWORK_CONFIG_FILE, networkConfigFile);

        NetworkConfig config = Yaml.load(networkConfigFile, NetworkConfig.class);
        Environment.put(NETWORK_CONFIG, config);
    }

}
