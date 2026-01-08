package org.kzk.integration.configuration.env;

import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.containers.MySQLContainer;

public final class Containers {

    public static final MySQLContainer<?> mysql = MySqlTestContainer.selfMySQLContainer;

    public static final MinIOContainer minio = MinIoTestContainer.minio;

    private Containers() {}

    public static void runContainers() {
        TestEnvLoader.init();

        if (!mysql.isRunning()) {
            mysql.start();
        }

        if (!minio.isRunning()) {
            minio.start();
        }
    }

    public static void stopContainers() {
        if (mysql.isRunning()) {
            mysql.stop();
        }

        if (minio.isRunning()) {
            minio.stop();
        }
    }
}
