package com.imetro.persistence.seed;

import com.imetro.persistence.connection.Database;
import com.imetro.util.AppLogger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ConfiguracoesSeedService {

    private static final Logger LOGGER = AppLogger.getLogger(ConfiguracoesSeedService.class);
    private static final String CONFIG_ADAPTATIVA_RESOURCE = "/seeds_configuracoes_teste_adaptativo_niveis.sql";
    private static final String CONFIG_UTILIZADOR_RESOURCE = "/seeds_configuracoes_por_utilizador.sql";
    private static final Path CONFIG_ADAPTATIVA_FILE = Path.of("src", "main", "resources", "seeds_configuracoes_teste_adaptativo_niveis.sql");
    private static final Path CONFIG_UTILIZADOR_FILE = Path.of("src", "main", "resources", "seeds_configuracoes_por_utilizador.sql");

    private ConfiguracoesSeedService() {
    }

    public static void trySeedIfNeeded() {
        Optional<Connection> connOpt;
        try {
            connOpt = Database.openConnectionFromEnv();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Falha ao abrir conexao para verificar os seeds de configuracoes.", e);
            return;
        }

        if (connOpt.isEmpty()) {
            return;
        }

        try (Connection conn = connOpt.get()) {
            boolean previousAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);

            try {
                int blocosExecutados = 0;
                blocosExecutados += executarSeedSeDisponivel(conn, CONFIG_ADAPTATIVA_RESOURCE, CONFIG_ADAPTATIVA_FILE);
                blocosExecutados += executarSeedSeDisponivel(conn, CONFIG_UTILIZADOR_RESOURCE, CONFIG_UTILIZADOR_FILE);
                conn.commit();
                LOGGER.info("Seed de configuracoes concluido com sucesso em " + blocosExecutados + " bloco(s).");
            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackException) {
                    LOGGER.log(Level.WARNING, "Falha ao fazer rollback do seed de configuracoes.", rollbackException);
                }
                throw e;
            } finally {
                try {
                    conn.setAutoCommit(previousAutoCommit);
                } catch (SQLException ignored) {
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Falha ao executar os seeds iniciais de configuracoes.", e);
        }
    }

    private static int executarSeedSeDisponivel(Connection conn, String resourcePath, Path fallbackFile) throws IOException, SQLException {
        Optional<String> seedSqlOpt = loadSeedSql(resourcePath, fallbackFile);
        if (seedSqlOpt.isEmpty() || seedSqlOpt.get().isBlank()) {
            LOGGER.warning("Seed de configuracoes nao encontrado: " + resourcePath);
            return 0;
        }

        int statementsExecutadas = executarSeed(conn, seedSqlOpt.get());
        LOGGER.info("Seed executado para " + resourcePath + " em " + statementsExecutadas + " bloco(s).");
        return statementsExecutadas;
    }

    private static Optional<String> loadSeedSql(String resourcePath, Path fallbackFile) throws IOException {
        try (InputStream in = ConfiguracoesSeedService.class.getResourceAsStream(resourcePath)) {
            if (in != null) {
                return Optional.of(readSql(in));
            }
        }

        if (Files.exists(fallbackFile)) {
            return Optional.of(Files.readString(fallbackFile, StandardCharsets.UTF_8));
        }

        return Optional.empty();
    }

    private static String readSql(InputStream in) throws IOException {
        String sql = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        if (!sql.isEmpty() && sql.charAt(0) == '\uFEFF') {
            return sql.substring(1);
        }
        return sql;
    }

    private static int executarSeed(Connection conn, String seedSql) throws SQLException {
        List<String> statements = splitSqlStatements(seedSql);
        if (statements.isEmpty()) {
            return 0;
        }

        try (Statement stmt = conn.createStatement()) {
            int executadas = 0;
            for (int i = 0; i < statements.size(); i++) {
                String statement = statements.get(i).trim();
                if (statement.isEmpty()) {
                    continue;
                }

                try {
                    stmt.execute(statement);
                    executadas++;
                } catch (SQLException e) {
                    throw new SQLException(
                        "Falha ao executar o bloco " + (i + 1) + " do seed de configuracoes: " + resumirStatement(statement),
                        e
                    );
                }
            }
            return executadas;
        }
    }

    private static List<String> splitSqlStatements(String sql) {
        if (sql == null || sql.isBlank()) {
            return List.of();
        }

        List<String> statements = new java.util.ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inSingleQuote = false;
        boolean inLineComment = false;
        boolean inBlockComment = false;

        for (int i = 0; i < sql.length(); i++) {
            char ch = sql.charAt(i);
            char next = i + 1 < sql.length() ? sql.charAt(i + 1) : '\0';

            if (inLineComment) {
                current.append(ch);
                if (ch == '\n') {
                    inLineComment = false;
                }
                continue;
            }

            if (inBlockComment) {
                current.append(ch);
                if (ch == '*' && next == '/') {
                    current.append(next);
                    i++;
                    inBlockComment = false;
                }
                continue;
            }

            if (!inSingleQuote && ch == '-' && next == '-') {
                current.append(ch).append(next);
                i++;
                inLineComment = true;
                continue;
            }

            if (!inSingleQuote && ch == '/' && next == '*') {
                current.append(ch).append(next);
                i++;
                inBlockComment = true;
                continue;
            }

            if (ch == '\'') {
                current.append(ch);
                if (inSingleQuote && next == '\'') {
                    current.append(next);
                    i++;
                    continue;
                }
                inSingleQuote = !inSingleQuote;
                continue;
            }

            if (ch == ';' && !inSingleQuote) {
                String statement = current.toString().trim();
                if (!statement.isEmpty()) {
                    statements.add(statement);
                }
                current.setLength(0);
                continue;
            }

            current.append(ch);
        }

        String tail = current.toString().trim();
        if (!tail.isEmpty()) {
            statements.add(tail);
        }

        return statements;
    }

    private static String resumirStatement(String statement) {
        String normalized = statement.replaceAll("\\s+", " ").trim();
        if (normalized.length() <= 180) {
            return normalized;
        }
        return normalized.substring(0, 180) + "...";
    }
}
