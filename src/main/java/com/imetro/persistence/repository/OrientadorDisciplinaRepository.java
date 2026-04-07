package com.imetro.persistence.repository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class OrientadorDisciplinaRepository {

    public boolean hasAny(UUID orientadorId) {
        String sql = "SELECT 1 FROM orientador_disciplinas WHERE orientador_id = ? LIMIT 1";
        try (var conn = JdbcBasicSqlRepository.openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, orientadorId);
            try (var rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void add(UUID orientadorId, UUID disciplinaId) {
        String sql = "INSERT INTO orientador_disciplinas (orientador_id, disciplina_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (var conn = JdbcBasicSqlRepository.openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, orientadorId);
            stmt.setObject(2, disciplinaId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Integer> countOrientadoresByDisciplinaNames(List<String> nomes) {
        Map<String, Integer> counts = new HashMap<>();
        if (nomes == null || nomes.isEmpty()) {
            return counts;
        }

        String sql = """
                SELECT d.nome as nome, COUNT(od.orientador_id) as total
                FROM disciplinas d
                LEFT JOIN orientador_disciplinas od ON od.disciplina_id = d.id
                WHERE d.nome = ANY (?)
                GROUP BY d.nome
                """;

        try (var conn = JdbcBasicSqlRepository.openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            var array = conn.createArrayOf("text", nomes.toArray(new String[0]));
            stmt.setArray(1, array);
            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    counts.put(rs.getString("nome"), rs.getInt("total"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        return counts;
    }
}

