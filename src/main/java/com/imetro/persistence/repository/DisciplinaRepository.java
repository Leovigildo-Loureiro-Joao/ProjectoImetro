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
        ensureExists(nome, 1.0, "BASICO");
    }

    public void ensureExists(String nome, double peso, String nivel) {
        String sql = """
                INSERT INTO disciplinas (nome, peso, nivel)
                VALUES (?, ?, ?)
                ON CONFLICT (nome) DO UPDATE SET
                  peso = EXCLUDED.peso,
                  nivel = EXCLUDED.nivel
                """;
        try (var conn = JdbcBasicSqlRepository.openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setDouble(2, peso);
            stmt.setString(3, nivel);
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
