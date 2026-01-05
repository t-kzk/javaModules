package org.kzk.integration.demo;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.containers.MySQLContainer;

//TODO Я так и не понимаю, почему этот вариант так плох?
// Чем концептуально отличается от враппера?
@TestConfiguration
//@Testcontainers
public class TestConfig {

    @Bean()
    @ServiceConnection
  //  @Scope("prototype")
    public MySQLContainer<?> mysql() {
        return new MySQLContainer<>("mysql:8.0.43")
                .withDatabaseName("module_2_di_ioc")
                .withUsername("root")
                .withPassword("secret");
    }

    @Bean
   // @Scope("prototype")
    public DynamicPropertyRegistrar mysqlProperties(MySQLContainer<?> container) {
        return r -> {
            // Flyway
            r.add("spring.flyway.url", container::getJdbcUrl);
            r.add("spring.flyway.username", container::getUsername);
            r.add("spring.flyway.password", container::getPassword);

            // R2DBC
            r.add("spring.r2dbc.url", () ->
                    "r2dbc:mysql://" +
                            container.getHost() + ":" +
                            container.getMappedPort(3306) + "/" +
                            container.getDatabaseName()
            );
            r.add("spring.r2dbc.username", container::getUsername);
            r.add("spring.r2dbc.password", container::getPassword);
        };
    }
}
