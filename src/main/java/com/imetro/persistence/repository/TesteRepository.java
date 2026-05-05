package com.imetro.persistence.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TesteRepository extends JdbcBasicSqlRepository {

    public TesteRepository() {
        super("testes", "id");
    }

    public List<Map<String, Object>> findByDisciplinaId(UUID disciplinaId) throws SQLException {
        String sql = """
            select *
            from testes
            where disciplina_id = ?
            order by coalesce(data_teste, criado_em) desc, criado_em desc
            """;
        return executeQueryList(sql, disciplinaId);
    }

    public List<Map<String, Object>> findByCandidatoId(UUID candidatoId) throws SQLException {
        String sql = """
            select *
            from testes
            where candidato_id = ?
            order by coalesce(data_teste, criado_em) desc, criado_em desc
            """;
        return executeQueryList(sql, candidatoId);
    }

     public List<Map<String, Object>> findByCandidatoIdDisciplina(UUID candidatoId,UUID disciplinaId) throws SQLException {
        String sql = """
            select *
            from testes
            where candidato_id = ? and disciplina_id = ?
            order by coalesce(data_teste, criado_em) desc, criado_em desc
            """;
        return executeQueryList(sql, candidatoId,disciplinaId);
    }

    public List<Map<String, Object>> findByDiagnosticoId(UUID diagnosticoId) throws SQLException {
        String sql = """
            select *
            from testes
            where diagnostico_id = ?
            order by coalesce(data_teste, criado_em) desc, criado_em desc
            """;
        return executeQueryList(sql, diagnosticoId);
    }

    private List<Map<String, Object>> executeQueryList(String sql, Object ...value) throws SQLException {
        try (var conn = JdbcBasicSqlRepository.openRequiredConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                for (int i = 0; i < value.length; i++) {
                    stmt.setObject(i+1, value[i]);        
                }
            
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
