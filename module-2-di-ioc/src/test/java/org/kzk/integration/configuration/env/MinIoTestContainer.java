package org.kzk.integration.configuration.env;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class MinIoTestContainer {

    public static final GenericContainer minIo;

    static {
        minIo = new GenericContainer<>(
                DockerImageName.parse("quay.io/minio/minio:latest"))
                       // .withExposedPorts()
                .withEnv("MINIO_ROOT_USER", "minioadmin")
                .withEnv("MINIO_ROOT_PASSWORD", "minioadmin")
                .withCommand("server /data --console-address \":9001\"");
    }
}
