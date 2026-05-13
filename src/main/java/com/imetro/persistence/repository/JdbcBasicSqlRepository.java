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

    private final String id2Column;
    private final String id3Column;
    private final String id4Column;
    private final List<String> idsColumn=new ArrayList<>();

    public String getId2Column() {
        return id2Column;
    }

    public String getId3Column() {
        return id3Column;
    }

    public String getId4Column() {
        return id4Column;
    }

    public List<String> getIdsColumn() {
        return idsColumn;
    }

    protected JdbcBasicSqlRepository(String tableName, String idColumn) {
        this.tableName = SqlIdentifiers.requireSafeQualifiedName(Objects.requireNonNull(tableName, "tableName"));
        this.idColumn = SqlIdentifiers.requireSafeQualifiedName(Objects.requireNonNull(idColumn, "idColumn"));
        id2Column=null;
        id3Column=null;
        id4Column=null;
        idsColumn.add(this.idColumn);

    }

    protected JdbcBasicSqlRepository(String tableName, String idColumn, String id2Column) {
        this.tableName = SqlIdentifiers.requireSafeQualifiedName(Objects.requireNonNull(tableName, "tableName"));
        this.idColumn = SqlIdentifiers.requireSafeQualifiedName(Objects.requireNonNull(idColumn, "idColumn"));
        this.id2Column=SqlIdentifiers.requireSafeQualifiedName(Objects.requireNonNull(idColumn, "id2Column"));
        this.id3Column=null;
        this.id4Column=null;
        idsColumn.add(this.idColumn);
        idsColumn.add(this.id2Column);

    }

    protected JdbcBasicSqlRepository(String tableName, String idColumn, String id2Column, String id3Column) {
        this.tableName = SqlIdentifiers.requireSafeQualifiedName(Objects.requireNonNull(tableName, "tableName"));
        this.idColumn = SqlIdentifiers.requireSafeQualifiedName(Objects.requireNonNull(idColumn, "idColumn"));
        this.id2Column=SqlIdentifiers.requireSafeQualifiedName(Objects.requireNonNull(idColumn, "id3Column"));
        this.id3Column=SqlIdentifiers.requireSafeQualifiedName(Objects.requireNonNull(idColumn, "id3Column"));
        this.id4Column=null;
        idsColumn.add(this.idColumn);
        idsColumn.add(this.id2Column);
        idsColumn.add(this.id3Column);

    }

    protected JdbcBasicSqlRepository(String tableName, String idColumn, String id2Column, String id3Column,String id4Column) {
        this.tableName = SqlIdentifiers.requireSafeQualifiedName(Objects.requireNonNull(tableName, "tableName"));
        this.idColumn = SqlIdentifiers.requireSafeQualifiedName(Objects.requireNonNull(idColumn, "idColumn"));
        this.id2Column=SqlIdentifiers.requireSafeQualifiedName(Objects.requireNonNull(idColumn, "id3Column"));
        this.id3Column=SqlIdentifiers.requireSafeQualifiedName(Objects.requireNonNull(idColumn, "id3Column"));
        this.id4Column=null;
        idsColumn.add(this.idColumn);
        idsColumn.add(this.id2Column);
        idsColumn.add(this.id3Column);
        idsColumn.add(this.id4Column);
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

    public Optional<Map<String, Object>> findOneByField(String field, Object value) throws SQLException {
        String sql = "select * from " + tableName + " where " + field + " = ?";
        try (Connection conn = openRequiredConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, value);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Map<String, Object>> rows = readAllRows(rs);
                if (rows.isEmpty()) {
                    return Optional.empty();
                }
                return Optional.of(rows.getFirst());
            }
        }
    }

        public int updateByField(String field, Object value, Map<String, ?> fields) throws SQLException {
            if (fields == null || fields.isEmpty()) {
                throw new IllegalArgumentException("fields must not be null/empty");
            }

            List<String> columns = fields.keySet().stream()
                    .map(SqlIdentifiers::requireSafeQualifiedName)
                    .sorted(Comparator.naturalOrder())
                    .toList();

            String setClause = String.join(", ", columns.stream().map(c -> c + " = ?").toList());
            String sql = "update " + tableName + " set " + setClause + " where " + field + " = ?";

            try (Connection conn = openRequiredConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
                int index = 1;
                for (String column : columns) {
                    stmt.setObject(index++, fields.get(column));
                }
                stmt.setObject(index, value);
                return stmt.executeUpdate();
            }
        }


        public List<Map<String, Object>> findAllByField(String field, Object value) throws SQLException {
            String sql = "select * from " + tableName + " where " + field + " = ?";
            try (Connection conn = openRequiredConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setObject(1, value);
                try (ResultSet rs = stmt.executeQuery()) {
                    List<Map<String, Object>> rows = readAllRows(rs);
                    if (rows.isEmpty()) {
                        return List.of();
                    }
                    return rows;
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

    public static List<Map<String, Object>> readAllRows(ResultSet rs) throws SQLException {
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

    public static Connection openRequiredConnection() throws SQLException {
        Optional<Connection> connOpt = Database.openConnectionFromEnv();
        if (connOpt.isEmpty()) {
            throw new IllegalStateException("BD desativada/não configurada. Defina TESTE=true (ou DB_ENABLED=true) e DB_URL, DB_USER, DB_PASSWORD.");
        }
        return connOpt.get();
    }

    //Tables with many primary keys

    public Optional<Map<String, Object>> findById(Object ...id) throws SQLException {
        String idSolution=AllIds(id);

        String sql = "select * from " + tableName + " where " + idSolution;
        try (Connection conn = openRequiredConnection()){
            try (ResultSet rs = (ResultSet) PreparedStatement(conn,sql,id).executeQuery()) {
                List<Map<String, Object>> rows = readAllRows(rs);
                if (rows.isEmpty()) {
                    return Optional.empty();
                }
                return Optional.of(rows.getFirst());
            }
        }
    }

    public int deleteById(Object ...id) throws SQLException {
        String idSolution=AllIds(id);
        String sql = "delete from " + tableName + " where " +idSolution;
        try (Connection conn = openRequiredConnection()){
            PreparedStatement stmt= PreparedStatement(conn, sql, id);
            return stmt.executeUpdate();
        }
    }



    private String AllIds(Object ...id){
        String idSolution="";
        for (int i = 0; i < id.length; i++) {
            idSolution+=" "+idsColumn.get(i) + "= ?";
        }
        return idSolution;
    }

    private PreparedStatement PreparedStatement(Connection conn,String sql,Object[] id){
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < id.length; i++) {
                Object object=id[i];
                stmt.setObject(i+1, object);
            }
            return stmt;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public int updateById(Object[] id, Map<String, ?> fields) throws SQLException {
        if (fields == null || fields.isEmpty()) {
            throw new IllegalArgumentException("fields must not be null/empty");
        }

        List<String> columns = fields.keySet().stream()
                .map(SqlIdentifiers::requireSafeQualifiedName)
                .sorted(Comparator.naturalOrder())
                .toList();

        String setClause = String.join(", ", columns.stream().map(c -> c + " = ?").toList());
        String sql = "update " + tableName + " set " + setClause + " where " + AllIds(id);

        try (Connection conn = openRequiredConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            int index = 1;
            for (String column : columns) {
                stmt.setObject(index++, fields.get(column));
            }
            for (int i = 0; i < id.length; i++) {
                Object object=id[i];
                stmt.setObject(index++, object);
            }
            return stmt.executeUpdate();
        }
    }


}
