package org.kzk.env.db;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;

public enum DataSourceProviderMySql {
    INSTANCE;

    private final DataSource dataSource;

    DataSourceProviderMySql() {
        // убрать в проперти
        MysqlDataSource mySqlDataSource = new MysqlDataSource();
        mySqlDataSource.setUrl("jdbc:mysql://localhost:3306/module-2-jdbc?serverTimezone=UTC&createDatabaseIfNotExist=true");
        mySqlDataSource.setUser("root");
        mySqlDataSource.setPassword("secret");
        this.dataSource = mySqlDataSource;
    }


    public DataSource getDataSource() {
        return dataSource;
    }
}
