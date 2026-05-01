package com.imetro.persistence.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class TesteStatsRepository extends JdbcBasicSqlRepository {

    public TesteStatsRepository() {
        super("stats", "id");
    }

    public Optional<Map<String, Object>> findByTesteId(UUID testeId) throws SQLException {
        String sql = """
            select *
            from stats
            where teste_id = ?
            limit 1
            """;

        List<Map<String, Object>> rows = executeQueryList(sql, testeId);
        if (rows.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(rows.getFirst());
    }

    public List<Map<String, Object>> findByCandidatoId(UUID candidatoId) throws SQLException {
        String sql = """
            select *
            from stats
            where candidato_id = ?
            order by criado_em desc
            """;
        return executeQueryList(sql, candidatoId);
    }

    public List<Map<String, Object>> findByDisciplinaId(UUID disciplinaId) throws SQLException {
        String sql = """
            select *
            from stats
            where disciplina_id = ?
            order by criado_em desc
            """;
        return executeQueryList(sql, disciplinaId);
    }

    private List<Map<String, Object>> executeQueryList(String sql, Object value) throws SQLException {
        try (var conn = JdbcBasicSqlRepository.openRequiredConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, value);
            try (ResultSet rs = stmt.executeQuery()) {
                return readRows(rs);
            }
        }
    }

    private List<Map<String, Object>> readRows(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int totalColunas = meta.getColumnCount();
        List<Map<String, Object>> rows = new ArrayList<>();

        while (rs.next()) {
            Map<String, Object> row = new LinkedHashMap<>();
            for (int i = 1; i <= totalColunas; i++) {
                row.put(meta.getColumnLabel(i), rs.getObject(i));
            }
            rows.add(row);
        }

        return rows;
    }
}
