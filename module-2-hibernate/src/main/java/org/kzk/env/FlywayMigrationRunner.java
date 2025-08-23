package org.kzk.env;

import org.flywaydb.core.Flyway;

public class FlywayMigrationRunner {
    public static void main(String[] args) {
        // Настройка Flyway
        Flyway flyway = Flyway.configure()
                .dataSource(DataSourceProviderPG.INSTANCE.getDataSource())
                .defaultSchema("module_2_hibernate")
                .load();

        // Запуск миграций
        flyway.migrate();
    }
}
