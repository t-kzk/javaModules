package org.kzk.integration.configuration.env;

import io.github.cdimascio.dotenv.Dotenv;

public class TestEnvLoader {
    public static void init() {
        Dotenv dotenv = Dotenv.configure()
                .directory("./module-2-di-ioc/src/main/resources")
                .filename("application.env")
                .ignoreIfMissing()
                .load();
        System.out.println("WORKING DIR = " + System.getProperty("user.dir"));
        dotenv.entries().forEach(e ->
                System.setProperty(e.getKey(), e.getValue()));
    }
}
