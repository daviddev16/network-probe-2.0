package com.networkprobe.core.init;

import org.apache.commons.cli.*;

public class CmdOptions {

    public static Options createDefaultOptions() {

        Options options = new Options();

        Option loadAsOption = Option.builder("la")
                .longOpt("load-as")
                .hasArg(true)
                .required(true)
                .desc("Tipo de inicialização do serviço.")
                .build();

        Option verboseOption = Option.builder("v")
                .longOpt("verbose")
                .hasArg(false)
                .required(false)
                .desc("Informar logs da aplicação")
                .build();

        options.addOption(loadAsOption);
        options.addOption(verboseOption);

        return options;
    }

    public static CommandLine createCommandLine(Options options, String[] args) throws ParseException {
        DefaultParser parser = new DefaultParser();
        return parser.parse(options, args);
    }

}

