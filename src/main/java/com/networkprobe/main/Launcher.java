package com.networkprobe.main;

import com.networkprobe.core.ServiceType;
import com.networkprobe.core.client.NetworkClient;
import com.networkprobe.core.Environment;
import com.networkprobe.core.factory.NetworkConfigFactory;
import com.networkprobe.core.config.NetworkConfig;
import com.networkprobe.core.CmdOptions;
import com.networkprobe.core.persistence.Yaml;
import com.networkprobe.core.server.BroadcastListener;
import com.networkprobe.core.server.NetworkServer;
import com.networkprobe.core.server.audit.Monitor;
import com.networkprobe.core.util.Validator;
import org.apache.commons.cli.*;
import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.InstanceAlreadyExistsException;
import java.io.File;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;

import static com.networkprobe.core.Environment.*;

public class Launcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(Launcher.class);

    public static void main(String[] args) {

        try {

            Thread.currentThread().setName("network-probe-main");

            Options options = CmdOptions.createDefaultOptions();
            CommandLine commandLine = processCommandLine(options, args);

            String serviceTypeValue = commandLine.getOptionValue("load-as")
                    .toUpperCase().trim();

            if (serviceTypeValue == null || !ServiceType.isValid(serviceTypeValue)) {
                LOGGER.error("Missing or invalid service type.");
                Runtime.getRuntime().exit(0);
            }

            if (!commandLine.hasOption("verbose"))
                disableLogging();

            String bindAddressValue = Validator.validateAddress(
                    commandLine.getOptionValue("bind")
            );

            LOGGER.info("Starting NetworkProbe");
            Environment.setup();
            Environment.set(BIND_ADDRESS, bindAddressValue, true);

            switch (ServiceType.parse(serviceTypeValue)) {
                case CLIENT:
                    launchClient(commandLine);
                    break;
                case SERVER:
                    launchServer();
                    break;
            }

            /* experimental */
            Monitor.DEBUG.set(true);

        } catch (Exception e) {
            LOGGER.error("Error while starting NetworkProbe.", e);
            System.exit(0);
        }
    }

    public static void launchServer()
            throws InstanceAlreadyExistsException, IOException {

        final String currentDirectory = System.getProperty("user.dir");

        NetworkConfigFactory.setup();
        Monitor.setup();

        File networkConfigFile = new File(currentDirectory, NetworkConfig.DEFAULT_CONFIG_FILENAME);

        if (!networkConfigFile.exists())
            NetworkConfigFactory.getFactory().createDefaultNetworkConfigFile(networkConfigFile);

        NetworkConfig config = Yaml.load(networkConfigFile, NetworkConfig.class);

        set(NETWORK_CONFIG, config, true);

        LOGGER.info("Loading network services...");

        BroadcastListener broadcastServerListener = new BroadcastListener();
        broadcastServerListener.start();

        NetworkServer networkServer = new NetworkServer();
        networkServer.start();
    }

    public static void launchClient(final CommandLine commandLine)
            throws InvalidPropertiesFormatException {

        NetworkClient client = new NetworkClient();
        client.start();
    }

    public static CommandLine processCommandLine(Options options, String[] args) {
        try {
            return CmdOptions.createCommandLine(options, args);
        } catch (ParseException e) {
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("network-probe", options);
            Runtime.getRuntime().exit(1);
        }
        return null;
    }

    public static void disableLogging() {
        org.apache.log4j.Logger.getRootLogger().setLevel(Level.OFF);
    }
}
