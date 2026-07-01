package com.imetro.persistence.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public final class Database {

    private static HikariDataSource dataSource;

    private Database() {
    }

    private static HikariDataSource getDataSource() throws SQLException {
        if (dataSource == null || dataSource.isClosed()) {
            Optional<DbConfig> configOpt = DbConfig.fromEnv();
            if (configOpt.isEmpty()) {
                throw new SQLException("BD desativada/não configurada.");
            }

            DbConfig config = configOpt.get();
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl(config.getUrl());
            hikariConfig.setUsername(config.getUser());
            hikariConfig.setPassword(config.getPassword());
            hikariConfig.setMaximumPoolSize(5);
            hikariConfig.setMinimumIdle(1);
            hikariConfig.setIdleTimeout(300000);
            hikariConfig.setMaxLifetime(600000);
            hikariConfig.setConnectionTimeout(10000);
            hikariConfig.setPoolName("imetro-pool");

            dataSource = new HikariDataSource(hikariConfig);
        }
        return dataSource;
    }

    public static Optional<Connection> openConnectionFromEnv() throws SQLException {
        try {
            return Optional.of(getDataSource().getConnection());
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public static void tryWarmup() {
        try {
            getDataSource();
        } catch (Exception ignored) {
        }
    }

    public static void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
