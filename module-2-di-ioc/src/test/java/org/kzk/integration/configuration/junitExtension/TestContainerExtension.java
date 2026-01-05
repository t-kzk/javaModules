package org.kzk.integration.configuration.junitExtension;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.kzk.integration.configuration.env.Containers;

//TODO не работает, так как контейнеры поднимаются слишком поздно, контекст быстрее
public class TestContainerExtension implements BeforeAllCallback, AfterAllCallback {
    @Override
    public void beforeAll(ExtensionContext context) {
        Containers.runContainers();
    }

    @Override
    public void afterAll(ExtensionContext context) {
        Containers.stopContainers();
    }
}
