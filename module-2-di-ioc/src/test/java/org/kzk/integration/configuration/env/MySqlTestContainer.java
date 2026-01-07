package org.kzk.integration.configuration.env;

import org.testcontainers.containers.MySQLContainer;

public class MySqlTestContainer {

    protected static final MySQLContainer<?> selfMySQLContainer;

    static {
        selfMySQLContainer =
                new MySQLContainer<>("mysql:8.0.43")
                        .withDatabaseName("module_2_di_ioc")
                        .withUsername("root")
                        .withPassword("secret");
    }
}
