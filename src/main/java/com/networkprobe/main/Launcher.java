package com.networkprobe.main;

import com.networkprobe.core.Environment;
import com.networkprobe.core.factory.NetworkFactory;
import com.networkprobe.core.config.NetworkConfig;
import com.networkprobe.core.persistence.Yaml;
import com.networkprobe.core.server.BroadcastListener;
import com.networkprobe.core.server.NetworkServer;
import com.networkprobe.core.server.audit.Monitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

import static com.networkprobe.core.Environment.*;

public class Launcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(Launcher.class);
    public static void main(String[] args)
            throws IOException {

        String currentDirectory = System.getProperty("user.dir");

        try {

            Thread.currentThread().setName("network-probe-main");
            LOGGER.info("Iniciando NetworkProbe");

            Environment.setup();
            NetworkFactory.setup();
            Monitor.setup();

            File networkConfigFile = new File(currentDirectory, NetworkConfig.DEFAULT_CONFIG_FILENAME);

            if (!networkConfigFile.exists())
                NetworkFactory.getFactory().createDefaultNetworkConfigFile(networkConfigFile);

            NetworkConfig config = Yaml.load(networkConfigFile, NetworkConfig.class);

            put(NETWORK_CONFIG, config);

            LOGGER.info("Carregando serviços");

            BroadcastListener broadcastServerListener = new BroadcastListener();
            broadcastServerListener.start();

            NetworkServer networkServer = new NetworkServer();
            networkServer.start();

        } catch (Exception e) {

            LOGGER.error("Houve um erro ao iniciar ou processar os dados.");
            LOGGER.error(e.getClass().getSimpleName() + ": " + e.getMessage());


            StringBuilder buffer = new StringBuilder()
                    .append(e.getClass().getSimpleName())
                    .append(':')
                    .append(e.getMessage())
                    .append('\n');

            for (StackTraceElement element : e.getStackTrace())
                buffer.append(element.toString())
                        .append('\n');

            byte[] exceptionTrace = buffer.toString().getBytes(StandardCharsets.UTF_8);

            File logFile = new File(currentDirectory + "/.log_trace");

            if (!logFile.exists())
                logFile.createNewFile();

            URI logUri = logFile.toURI();

            Files.write(Paths.get(logUri), exceptionTrace, StandardOpenOption.WRITE);

            System.exit(0);

        }

    }

}
