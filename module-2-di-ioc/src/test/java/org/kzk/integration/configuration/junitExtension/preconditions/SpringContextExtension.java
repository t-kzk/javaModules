package org.kzk.integration.configuration.junitExtension.preconditions;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class SpringContextExtension implements BeforeAllCallback {

    @Override
    public void beforeAll(ExtensionContext context) {
        ApplicationContext appContext =
                SpringExtension.getApplicationContext(context);

        context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL)
                .put(ApplicationContext.class, appContext);
    }
}
