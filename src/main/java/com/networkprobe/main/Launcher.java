package com.networkprobe.main;

import com.networkprobe.core.Environment;
import com.networkprobe.core.factory.NetworkFactory;
import com.networkprobe.core.config.NetworkConfig;
import com.networkprobe.core.persistence.Yaml;
import com.networkprobe.core.server.BroadcastListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static com.networkprobe.core.Environment.*;

public class Launcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(Launcher.class);

    public static void main(String[] args) {

        try {

            LOGGER.info("Iniciando NetworkProbe");

            Environment.setup();
            NetworkFactory.setup();

            String currentDirectory = System.getProperty("user.dir");
            File networkConfigFile = new File(currentDirectory, NetworkConfig.DEFAULT_CONFIG_FILENAME);

            if (!networkConfigFile.exists())
                NetworkFactory.getFactory().createDefaultNetworkConfigFile(networkConfigFile);

            NetworkConfig config = Yaml.load(networkConfigFile, NetworkConfig.class);

            put(NETWORK_CONFIG, config);

            LOGGER.info("Carregando serviços");

            BroadcastListener broadcastServerListener = new BroadcastListener();
            broadcastServerListener.start();

        } catch (Exception e) {
            LOGGER.error("Houve um erro ao iniciar ou processar os dados.");
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }


    }

}
