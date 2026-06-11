package com.imetro.persistence.seed;

import com.imetro.persistence.connection.Database;
import com.imetro.util.AppLogger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class PerguntasSeedService {

    private static final Logger LOGGER = AppLogger.getLogger(PerguntasSeedService.class);
    private static final String SEED_RESOURCE = "/seeds_perguntas.sql";
    private static final Path SEED_FILE = Path.of("scripts", "db", "seeds_perguntas.sql");

    private PerguntasSeedService() {
    }

    public static void trySeedIfEmpty() {
        Optional<Connection> connOpt;
        try {
            connOpt = Database.openConnectionFromEnv();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Falha ao abrir conexao para verificar o seed de perguntas.", e);
            return;
        }

        if (connOpt.isEmpty()) {
            return;
        }

        try (Connection conn = connOpt.get()) {
            if (!isPerguntasTableEmpty(conn)) {
                LOGGER.info("Seed de perguntas ignorado: a tabela perguntas ja contem dados.");
                return;
            }

            Optional<String> seedSqlOpt = loadSeedSql();
            if (seedSqlOpt.isEmpty() || seedSqlOpt.get().isBlank()) {
                LOGGER.warning("Seed de perguntas nao encontrado. A tabela perguntas continua vazia.");
                return;
            }

            String seedSql = seedSqlOpt.get();
            int totalPerguntas = 0;
            boolean previousAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);

            try {
                int statementsExecutadas = executarSeed(conn, seedSql);

                totalPerguntas = countPerguntas(conn);
                conn.commit();
                LOGGER.info("Seed de perguntas executado em " + statementsExecutadas + " bloco(s).");
            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackException) {
                    LOGGER.log(Level.WARNING, "Falha ao fazer rollback do seed de perguntas.", rollbackException);
                }
                throw e;
            } finally {
                try {
                    conn.setAutoCommit(previousAutoCommit);
                } catch (SQLException ignored) {
                }
            }

            LOGGER.info("Seed de perguntas concluido com sucesso. Registos em perguntas: " + totalPerguntas + ".");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Falha ao executar o seed inicial de perguntas.", e);
        }
    }

    private static boolean isPerguntasTableEmpty(Connection conn) throws SQLException {
        String sql = "select 1 from perguntas limit 1";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return !rs.next();
        }
    }

    private static int countPerguntas(Connection conn) throws SQLException {
        String sql = "select count(*) from perguntas";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (!rs.next()) {
                return 0;
            }
            return rs.getInt(1);
        }
    }

    private static Optional<String> loadSeedSql() throws IOException {
        try (InputStream in = PerguntasSeedService.class.getResourceAsStream(SEED_RESOURCE)) {
            if (in != null) {
                return Optional.of(readSql(in));
            }
        }

        if (Files.exists(SEED_FILE)) {
            return Optional.of(Files.readString(SEED_FILE, StandardCharsets.UTF_8));
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
            for (int i = 0; i < statements.size(); i++) {
                String statement = statements.get(i).trim();
                if (statement.isEmpty()) {
                    continue;
                }

                try {
                    stmt.execute(statement);
                } catch (SQLException e) {
                    throw new SQLException(
                        "Falha ao executar o bloco " + (i + 1) + " do seed de perguntas: " + resumirStatement(statement),
                        e
                    );
                }
            }
        }

        return statements.size();
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
