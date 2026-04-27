package com.imetro.persistence.repository;

import java.sql.SQLException;
import java.util.UUID;

public class ProgressoALunoDisciplinaRepository extends JdbcBasicSqlRepository{

    public ProgressoALunoDisciplinaRepository() {
        super("progresso_aluno_disciplina", "id");
    }

    public boolean hasAny(UUID candidatoId) {
        String sql = "SELECT 1 FROM progresso_aluno_disciplina WHERE aluno_id = ? LIMIT 1";
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

   
}

