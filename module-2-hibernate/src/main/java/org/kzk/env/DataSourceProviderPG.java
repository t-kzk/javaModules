package org.kzk.env;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

public enum DataSourceProviderPG {
    INSTANCE;

    private final DataSource dataSource;

    DataSourceProviderPG() {
        // убрать в проперти
        PGSimpleDataSource pgDataSource = new PGSimpleDataSource();
        pgDataSource.setUrl("jdbc:postgresql://localhost:5432/postgres");
        pgDataSource.setUser("postgres");
        pgDataSource.setPassword("secret");
        dataSource = pgDataSource;
    }


    public DataSource getDataSource() {
        return dataSource;
    }
}
