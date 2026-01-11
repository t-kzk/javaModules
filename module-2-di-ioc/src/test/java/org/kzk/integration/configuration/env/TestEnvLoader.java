package org.kzk.integration.configuration.env;

import io.github.cdimascio.dotenv.Dotenv;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TestEnvLoader {
    public static void init() {

        Path root = Paths.get(System.getProperty("user.dir"));
        Path envFile = root.resolve("module-2-di-ioc/test.env");

        Dotenv dotenv = Dotenv.configure()
                .directory(envFile.getParent().toString())
                .filename("test.env")
                .ignoreIfMissing()
                .load();
        System.out.println("WORKING DIR = " + System.getProperty("user.dir"));
        dotenv.entries().forEach(e ->
                System.setProperty(e.getKey(), e.getValue()));
    }
}
