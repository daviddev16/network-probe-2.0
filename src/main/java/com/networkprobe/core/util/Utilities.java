package com.networkprobe.core.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import static com.networkprobe.core.util.Validator.validate;

public class Utilities {

    public static final Random RANDOM = new Random();

    public static void write(File file, String data) throws IOException {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter( validate(file, "file") ))) {
            writer.write( validate(data, "data") );
        } catch (Exception e) {
            throw new IOException(String.format("Não foi possível salvar o dados no arquivo \"%s\".", file.getPath()), e);
        }
    }

}
