package com.imetro.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

public final class Database {

    private Database() {
    }

    /**
     * Conexão simples via JDBC puro.
     * Requer o driver do Postgres no classpath quando a persistência for ativada.
     */
    public static Optional<Connection> openConnectionFromEnv() throws SQLException {
        Optional<DbConfig> configOpt = DbConfig.fromEnv();
        if (configOpt.isEmpty()) {
            return Optional.empty();
        }

        DbConfig config = configOpt.get();
        return Optional.of(DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword()));
    }

    /**
     * MVP: tenta conectar sem derrubar a aplicação (se DB não estiver configurado/ligado).
     */
    public static void tryWarmup() {
        try {
            Optional<Connection> connOpt = openConnectionFromEnv();
            if (connOpt.isPresent()) {
                connOpt.get().close();
            }
        } catch (Exception ignored) {
        }
    }
}
