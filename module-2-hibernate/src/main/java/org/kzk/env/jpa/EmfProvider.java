package org.kzk.env.jpa;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public enum EmfProvider {
    INSTANCE;

    private final EntityManagerFactory emf;

    EmfProvider() {
        // убрать в проперти

        Map<String, String> settings = new HashMap<>();
        settings.put("hibernate.connection.url", "jdbc:postgresql://localhost:5432/postgres");
        settings.put("hibernate.connection.user", "postgres");
        settings.put("hibernate.connection.password", "secret");
        settings.put("hibernate.connection.driver_class", "org.postgresql.Driver");
        settings.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(
                "kzk-persistence-unit-name", settings);

        emf = new ThreadSafeEmf(factory);
    }


    public EntityManagerFactory getEmf() {
        return emf;
    }
}
