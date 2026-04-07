package com.imetro.persistence.repository;

import java.sql.SQLException;
import java.util.UUID;

public final class CandidatoDisciplinaRepository {

    public boolean hasAny(UUID candidatoId) {
        String sql = "SELECT 1 FROM candidato_disciplinas WHERE candidato_id = ? LIMIT 1";
        try (var conn = JdbcBasicSqlRepository.openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, candidatoId);
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

    public void add(UUID candidatoId, UUID disciplinaId) {
        String sql = "INSERT INTO candidato_disciplinas (candidato_id, disciplina_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (var conn = JdbcBasicSqlRepository.openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, candidatoId);
            stmt.setObject(2, disciplinaId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}

