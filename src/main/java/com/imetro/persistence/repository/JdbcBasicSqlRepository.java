package com.imetro.persistence.repository;

import com.imetro.persistence.connection.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementação JDBC de um repositório genérico baseado em nome de tabela.
 *
 * <p>O {@code tableName} e o {@code idColumn} são interpolados no SQL; por isso, são validados
 * como identificadores seguros (sem espaços/aspas/etc).</p>
 */
public abstract class JdbcBasicSqlRepository implements BasicSqlRepository {

    private final String tableName;
    private final String idColumn;

    protected JdbcBasicSqlRepository(String tableName, String idColumn) {
        this.tableName = SqlIdentifiers.requireSafeQualifiedName(Objects.requireNonNull(tableName, "tableName"));
        this.idColumn = SqlIdentifiers.requireSafeQualifiedName(Objects.requireNonNull(idColumn, "idColumn"));
    }

    @Override
    public List<Map<String, Object>> findAll() throws SQLException {
        String sql = "select * from " + tableName;
        try (Connection conn = openRequiredConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return readAllRows(rs);
        }
    }

    @Override
    public Optional<Map<String, Object>> findById(Object id) throws SQLException {
        String sql = "select * from " + tableName + " where " + idColumn + " = ?";
        try (Connection conn = openRequiredConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Map<String, Object>> rows = readAllRows(rs);
                if (rows.isEmpty()) {
                    return Optional.empty();
                }
                return Optional.of(rows.getFirst());
            }
        }
    }

    @Override
    public int deleteById(Object id) throws SQLException {
        String sql = "delete from " + tableName + " where " + idColumn + " = ?";
        try (Connection conn = openRequiredConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, id);
            return stmt.executeUpdate();
        }
    }

    @Override
    public int deleteAll() throws SQLException {
        String sql = "delete from " + tableName;
        try (Connection conn = openRequiredConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            return stmt.executeUpdate();
        }
    }

    @Override
    public int insert(Map<String, ?> fields) throws SQLException {
        if (fields == null || fields.isEmpty()) {
            throw new IllegalArgumentException("fields must not be null/empty");
        }

        List<String> columns = fields.keySet().stream()
                .map(SqlIdentifiers::requireSafeQualifiedName)
                .sorted(Comparator.naturalOrder())
                .toList();

        String columnList = String.join(", ", columns);
        String placeholders = String.join(", ", columns.stream().map(c -> "?").toList());
        String sql = "insert into " + tableName + " (" + columnList + ") values (" + placeholders + ")";

        try (Connection conn = openRequiredConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < columns.size(); i++) {
                Object value = fields.get(columns.get(i));
                stmt.setObject(i + 1, value);
            }
            return stmt.executeUpdate();
        }
    }

    @Override
    public int updateById(Object id, Map<String, ?> fields) throws SQLException {
        if (fields == null || fields.isEmpty()) {
            throw new IllegalArgumentException("fields must not be null/empty");
        }

        List<String> columns = fields.keySet().stream()
                .map(SqlIdentifiers::requireSafeQualifiedName)
                .sorted(Comparator.naturalOrder())
                .toList();

        String setClause = String.join(", ", columns.stream().map(c -> c + " = ?").toList());
        String sql = "update " + tableName + " set " + setClause + " where " + idColumn + " = ?";

        try (Connection conn = openRequiredConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            int index = 1;
            for (String column : columns) {
                stmt.setObject(index++, fields.get(column));
            }
            stmt.setObject(index, id);
            return stmt.executeUpdate();
        }
    }

    protected final String getTableName() {
        return tableName;
    }

    protected final String getIdColumn() {
        return idColumn;
    }

    private static List<Map<String, Object>> readAllRows(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();

        List<Map<String, Object>> rows = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> row = new LinkedHashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                String key = meta.getColumnLabel(i);
                Object value = rs.getObject(i);
                row.put(key, value);
            }
            rows.add(row);
        }
        return rows;
    }

    private static Connection openRequiredConnection() throws SQLException {
        Optional<Connection> connOpt = Database.openConnectionFromEnv();
        if (connOpt.isEmpty()) {
            throw new IllegalStateException("DB não configurada. Defina DB_ENABLED=true, DB_URL, DB_USER e DB_PASSWORD.");
        }
        return connOpt.get();
    }
}

