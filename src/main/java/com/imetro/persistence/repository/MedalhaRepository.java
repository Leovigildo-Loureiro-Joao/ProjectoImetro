package com.imetro.persistence.repository;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.imetro.util.MedalSupport;

public final class MedalhaRepository {

    public List<MedalSupport.MedalAward> findAwardsByUserId(UUID userId) {
        if (userId == null) {
            return List.of();
        }

        String sql = """
            SELECT medalha_codigo, progresso_atual, recorde_valor, conquistada_em
            FROM user_medalhas
            WHERE user_id = ?
            ORDER BY conquistada_em ASC
            """;

        List<MedalSupport.MedalAward> awards = new ArrayList<>();
        try (var conn = JdbcBasicSqlRepository.openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, userId);
            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Timestamp earnedAt = rs.getTimestamp("conquistada_em");
                    awards.add(new MedalSupport.MedalAward(
                        rs.getString("medalha_codigo"),
                        rs.getInt("progresso_atual"),
                        (Integer) rs.getObject("recorde_valor"),
                        earnedAt == null ? null : earnedAt.toLocalDateTime()
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        return awards;
    }

    public boolean upsertAward(UUID userId, String medalCode, int progressValue, Integer recordValue) {
        if (userId == null || medalCode == null || medalCode.isBlank()) {
            return false;
        }

        String sql = """
            INSERT INTO user_medalhas (user_id, medalha_codigo, progresso_atual, recorde_valor, conquistada_em, atualizado_em)
            VALUES (?, ?, ?, ?, now(), now())
            ON CONFLICT (user_id, medalha_codigo)
            DO UPDATE SET
                progresso_atual = EXCLUDED.progresso_atual,
                recorde_valor = EXCLUDED.recorde_valor,
                atualizado_em = now()
            """;

        try (var conn = JdbcBasicSqlRepository.openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, userId);
            stmt.setString(2, medalCode.trim().toUpperCase());
            stmt.setInt(3, Math.max(0, progressValue));
            if (recordValue == null) {
                stmt.setObject(4, null);
            } else {
                stmt.setInt(4, Math.max(0, recordValue));
            }
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        return false;
    }
}
