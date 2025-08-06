package org.kzk.helper;

import org.kzk.env.db.InitDbLiquibaseImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class JdbcHelper {

    private static Connection connection;

    static {
        new InitDbLiquibaseImpl().initDb();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down... Closing database connection");
            if (Objects.nonNull(connection)) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }));
    }

    private static Connection getConnection() {
        if (Objects.isNull(connection)) {
            try {
                connection = DataSourceProviderMySql.INSTANCE.getDataSource().getConnection();
            } catch (SQLException e) {

                System.out.println("oi");
                System.exit(1);
            }
        }
        return connection;
    }

    public static PreparedStatement getPreparedStatement(String sql) throws SQLException {
        return getConnection().prepareStatement(sql);
    }

    public static PreparedStatement getPreparedStatementWithKeys(String sql) throws SQLException {
        return getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    }

    public static void beginTransaction() throws SQLException {
        if (Objects.nonNull(connection)) {
            connection.setAutoCommit(false);
        } else {
            throw new RuntimeException("Connection not exist for beginTransaction");
        }
    }

    public static void commitTransaction() throws SQLException {
        if (Objects.nonNull(connection)) {
            connection.commit();
            connection.setAutoCommit(true);
        } else {
            throw new RuntimeException("Connection not exist for commitTransaction");
        }

    }

    public static void rollbackTransaction() {
        if (Objects.nonNull(connection)) {
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to rollback transaction", e);
            }
        } else {
            throw new RuntimeException("Connection not exist for rollbackTransaction");
        }

    }
}
