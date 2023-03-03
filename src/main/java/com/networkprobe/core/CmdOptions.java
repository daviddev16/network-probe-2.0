package com.networkprobe.core;

import org.apache.commons.cli.*;

public class CmdOptions {

    public static Options createDefaultOptions() {

        Options options = new Options();

        Option loadAsOption = Option.builder("la")
                .longOpt("load-as")
                .hasArg(true)
                .required(true)
                .desc("Start as a client or server")
                .build();

        Option bindAddressOption = Option.builder("bind")
                .longOpt("bind")
                .hasArg(true)
                .required(true)
                .desc("Bind socket to a defined interface")
                .build();

        Option verboseOption = Option.builder("v")
                .longOpt("verbose")
                .hasArg(false)
                .required(false)
                .desc("verbose mode")
                .build();

        options.addOption(loadAsOption);
        options.addOption(bindAddressOption);
        options.addOption(verboseOption);

        return options;
    }

    public static CommandLine createCommandLine(Options options, String[] args) throws ParseException {
        DefaultParser parser = new DefaultParser();
        return parser.parse(options, args);
    }

}

