package org.kzk.integration.configuration.env;

import org.testcontainers.containers.MySQLContainer;

public final class Containers {

    public static MySQLContainer<?> mysql = MySqlTestContainer.selfMySQLContainer;

    private Containers() {}

    public static void runContainers() {
        if (!mysql.isRunning()) {
            mysql.start();
        }
    }

    public static void stopContainers() {
        if (mysql.isRunning()) {
            mysql.stop();
        }
    }
}
