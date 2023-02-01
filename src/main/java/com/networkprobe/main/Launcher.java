package com.networkprobe.main;

import com.networkprobe.core.Environment;
import com.networkprobe.core.factory.NetworkFactory;
import com.networkprobe.core.NetworkConfig;

import javax.management.InstanceAlreadyExistsException;
import java.io.File;
import java.io.IOException;

import static com.networkprobe.core.Environment.*;

public class Launcher {

    public static void main(String[] args) throws InstanceAlreadyExistsException, IOException {
        System.out.println("Carregando ambiente...");
        Environment.init();
        NetworkFactory.init();
        System.out.println("Carregando variáveis...");
        setupAllVariables();
    }

    public static void setupAllVariables() throws IOException {

        put(CURRENT_DIRECTORY_KEY, System.getProperty("user.dir"));

        File networkConfigFile = new File(getString(CURRENT_DIRECTORY_KEY), NetworkConfig.DEFAULT_CONFIG_FILENAME);

        if (!networkConfigFile.exists())
            NetworkFactory.getFactory().createDefaultNetworkConfigFile(NetworkConfig.DEFAULT_CONFIG_FILENAME);

        put(CONFIG_FILE_KEY, networkConfigFile);
        System.out.println("Ok.");

    }

}
