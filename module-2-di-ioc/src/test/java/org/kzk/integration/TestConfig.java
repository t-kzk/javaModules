package org.kzk.integration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@TestConfiguration
@Testcontainers
public class TestConfig {

    @Bean
    @ServiceConnection
    public MySQLContainer<?> mysql() {
        return new MySQLContainer<>("mysql:8.0.43")
                .withDatabaseName("module_2_di_ioc")
                .withUsername("root")
                .withPassword("secret");
    }

    @Bean
    public DynamicPropertyRegistrar mysqlProperties(MySQLContainer<?> container) {
        return registry -> {
            // Flyway
            registry.add("spring.flyway.url", container::getJdbcUrl);
            registry.add("spring.flyway.username", container::getUsername);
            registry.add("spring.flyway.password", container::getPassword);

            // R2DBC
            registry.add("spring.r2dbc.url", () ->
                    "r2dbc:mysql://" +
                            container.getHost() + ":" +
                            container.getMappedPort(3306) + "/" +
                            container.getDatabaseName()
            );
            registry.add("spring.r2dbc.username", container::getUsername);
            registry.add("spring.r2dbc.password", container::getPassword);
        };
    }
}
