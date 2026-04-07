package com.imetro.persistence.repository;

import java.sql.SQLException;
import java.util.UUID;

public final class DisciplinaRepository {

    public UUID ensureAndGetIdByName(String nome) {
        if (nome == null || nome.isBlank()) {
            return null;
        }
        ensureExists(nome);
        return getIdByName(nome);
    }

    public void ensureExists(String nome) {
        String sql = "INSERT INTO disciplinas (nome) VALUES (?) ON CONFLICT (nome) DO NOTHING";
        try (var conn = JdbcBasicSqlRepository.openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public UUID getIdByName(String nome) {
        String sql = "SELECT id FROM disciplinas WHERE nome = ?";
        try (var conn = JdbcBasicSqlRepository.openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Object value = rs.getObject("id");
                    if (value instanceof UUID uuid) {
                        return uuid;
                    }
                    if (value != null) {
                        return UUID.fromString(value.toString());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return null;
    }
}

