package org.abos.mizar;

import org.abos.mizar.parser.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Utils {

    private Utils() {
        // no instantiation
    }

    public static String loadFromFile(String file) throws IOException {
        return Files.readString(Path.of(Parser.class.getResource(file).getPath()));
    }
}
