package org.kzk.integration.configuration.env;

import org.testcontainers.containers.MinIOContainer;

public class MinIoTestContainer {

    protected static final MinIOContainer minio;

    static {
        minio = new MinIOContainer("minio/minio:RELEASE.2025-09-07T16-13-09Z")
                .withUserName("minioadmin")
                .withPassword("minioadmin")
                .withExposedPorts(9000, 9001);
             //   .withCommand("server /data --console-address \":9001\"");
    }
}
