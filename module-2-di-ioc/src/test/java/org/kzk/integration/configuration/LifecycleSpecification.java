package org.kzk.integration.configuration;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kzk.Module2DiIocApplication;
import org.kzk.data.AuthRepository;
import org.kzk.data.UserRepository;
import org.kzk.integration.configuration.env.Containers;
import org.kzk.integration.configuration.junitExtension.preconditions.SpringContextExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

//@ExtendWith(TestContainerExtension.class) TODO не работает, так как контейнеры слишком поздно запускаются,
// а контекст уже начал создаваться
//@Testcontainers //TODO бесполезен, так как запуск контейнеров реализован кастомно руками
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {Module2DiIocApplication.class
                //    , TestSupportConfig.class тут, например можно руками сконфигурировать свой клиент
        }
)
@ExtendWith(SpringContextExtension.class)
@ActiveProfiles("local")
@Slf4j
public class LifecycleSpecification {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected AuthRepository authRepository;

    @Autowired
    protected WebTestClient webTestClient;

    @Autowired
    ApplicationContext context;

    static {
        Containers.runContainers();
    }

    @BeforeEach
    protected void setUp() {
        authRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DynamicPropertySource // перетирает application.yaml
    static void registerProps(DynamicPropertyRegistry r) {

        // Flyway
        r.add("spring.flyway.url", Containers.mysql::getJdbcUrl);
        r.add("spring.flyway.username", Containers.mysql::getUsername);
        r.add("spring.flyway.password", Containers.mysql::getPassword);

        // R2DBC
        r.add("spring.r2dbc.url", () ->
                "r2dbc:mysql://" +
                        Containers.mysql.getHost() + ":" +
                        Containers.mysql.getMappedPort(3306) + "/" +
                        Containers.mysql.getDatabaseName()
        );
        r.add("spring.r2dbc.username", Containers.mysql::getUsername);
        r.add("spring.r2dbc.password", Containers.mysql::getPassword);
    }

}
