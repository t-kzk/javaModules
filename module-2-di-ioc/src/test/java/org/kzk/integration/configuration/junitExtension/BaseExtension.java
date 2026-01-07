package org.kzk.integration.configuration.junitExtension;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.kzk.integration.configuration.junitExtension.preconditions.ApiRegistrationExtension;
import org.kzk.integration.configuration.junitExtension.preconditions.GenerateFilesExtension;
import org.kzk.integration.configuration.junitExtension.service.GenerateFileService;
import org.kzk.integration.configuration.junitExtension.service.GenerateUserService;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

public class BaseExtension {

    protected static ExtensionContext.Namespace BASE = ExtensionContext.Namespace.create(BaseExtension.class);

    protected static ExtensionContext.Namespace API_REGISTRATION = ExtensionContext.Namespace.create(ApiRegistrationExtension.class);

    protected static ExtensionContext.Namespace FILES = ExtensionContext.Namespace.create(GenerateFilesExtension.class);

    private static WebTestClient webTestClient;

    protected GenerateUserService generateUser = new GenerateUserService();

    protected GenerateFileService generateFile = new GenerateFileService();


    protected WebTestClient getWebTestClient(ExtensionContext context) {

        if (Objects.isNull(webTestClient)) {
            ApplicationContext applicationContext = context.getStore(ExtensionContext.Namespace.GLOBAL).get(ApplicationContext.class, ApplicationContext.class);
            webTestClient = applicationContext.getBean(WebTestClient.class);
        }

        return webTestClient;
    }

    protected String getTestId(ExtensionContext context) {
        return context.getUniqueId();
    }
}
