package com.networkprobe.core.persistence;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.sun.media.sound.InvalidFormatException;

import java.io.File;
import java.io.IOException;

import static com.networkprobe.core.util.Validator.validate;

public class Yaml {

    public static final ObjectMapper MAPPER = new ObjectMapper(new YAMLFactory())
            .enable(SerializationFeature.INDENT_OUTPUT);

    private static final DefaultPrettyPrinter PRINTER = new DefaultPrettyPrinter()
            .withObjectIndenter(new DefaultIndenter("    ", "\n"));

    public static void save(File file, Object object) throws IOException {

        if (!file.getName().endsWith(".yaml"))
            throw new InvalidFormatException("O arquivo não é do tipo \".yaml\"");

        if ( !validate(file, "file").exists() )
            file.createNewFile();

        MAPPER.writer(PRINTER).writeValue(file, validate(object, "object"));
    }

    public static <E> E load(File file, Class<E> clazz) throws IOException {
        return (E) MAPPER.readValue(file, clazz);
    }


}
