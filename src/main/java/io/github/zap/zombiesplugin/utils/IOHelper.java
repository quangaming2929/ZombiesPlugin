package io.github.zap.zombiesplugin.utils;

import io.github.zap.zombiesplugin.ZombiesPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;

public class IOHelper {

    /**
     * Java 7-11 version of Files.readString()
     * @param path the file path to read
     * @return the string representation of this file content
     */
    public static String readFile(Path path) {
        try{
            byte[] encoded = Files.readAllBytes(path);
            return new String(encoded, Charset.forName("UTF-16"));
        } catch (IOException e) {
            ZombiesPlugin.instance.getLogger().log(Level.WARNING, "Failed to read file: " + path);
            return null;
        }
    }

    public static void writeFile(Path path, String content) {
        try {
            Files.write(path, content.getBytes("UTF-16"), StandardOpenOption.WRITE);
        } catch (Exception e) {
            ZombiesPlugin.instance.getLogger().log(Level.WARNING, "Failed to write file: " + path);
        }
    }
}
