package com.imetro.persistence.repository;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.UUID;

import com.imetro.util.MedalSupport;
import com.imetro.util.MedalSupport.MedalDefinition;
import com.imetro.util.MedalSupport.MedalSkill;

public final class MedalhaRepository {

    private static final double LIMIAR_TIME = 0.70d;
    private static final double LIMIAR_PONTARIA = 0.75d;
    private static final double LIMIAR_LOGICA = 0.70d;
    private static final double LIMIAR_RESILIENCIA = 0.65d;
    private static final double LIMIAR_CONSISTENCIA = 0.70d;

    public List<MedalSupport.MedalAward> findAwardsByUserId(UUID userId) {
        if (userId == null) {
            return List.of();
        }

        String sql = """
            SELECT medalha_codigo, progresso_atual, recorde_valor, conquistada_em, atualizado_em
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
                    Timestamp actualizada = rs.getTimestamp("atualizado_em");
                    awards.add(new MedalSupport.MedalAward(
                        rs.getString("medalha_codigo"),
                        rs.getInt("progresso_atual"),
                        (Integer) rs.getObject("recorde_valor"),
                        earnedAt == null ? null : earnedAt.toLocalDateTime(),
                        actualizada == null ? null : actualizada.toLocalDateTime()
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

    public List<MedalSupport.MedalAward> findAwardsByUserIdUpdates(UUID userId){
        List<MedalSupport.MedalAward> awards = findAwardsByUserId(userId);
        return awards.stream().filter(award -> award.actualizadaAt().toLocalDate().equals(LocalDate.now())).toList();
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

    public void sincronizarMedalhasPorUserId(UUID userId) {
        if (userId == null) {
            return;
        }

        EnumMap<MedalSkill, SkillSnapshot> progressoPorSkill = carregarProgressoPorSkill(userId);
        if (progressoPorSkill.isEmpty()) {
            return;
        }

        for (MedalDefinition definition : MedalSupport.catalog()) {
            SkillSnapshot snapshot = progressoPorSkill.getOrDefault(definition.skill(), SkillSnapshot.vazio());
            if (snapshot.progresso() < definition.targetValue()) {
                continue;
            }

            upsertAward(userId, definition.code(), snapshot.progresso(), snapshot.recorde());
        }
    }

    private EnumMap<MedalSkill, SkillSnapshot> carregarProgressoPorSkill(UUID userId) {
        EnumMap<MedalSkill, SkillSnapshot> progresso = inicializarSnapshots();
        String sql = """
            select velocidade, precisao, logica, resiliencia, consistencia
            from testes
            where candidato_id = ?
            """;

        try (var conn = JdbcBasicSqlRepository.openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, userId);
            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    acumularSkill(progresso, MedalSkill.TIME, rs.getObject("velocidade"), LIMIAR_TIME);
                    acumularSkill(progresso, MedalSkill.PONTARIA, rs.getObject("precisao"), LIMIAR_PONTARIA);
                    acumularSkill(progresso, MedalSkill.LOGICA, rs.getObject("logica"), LIMIAR_LOGICA);
                    acumularSkill(progresso, MedalSkill.RESILIENCIA, rs.getObject("resiliencia"), LIMIAR_RESILIENCIA);
                    acumularSkill(progresso, MedalSkill.CONSISTENCIA, rs.getObject("consistencia"), LIMIAR_CONSISTENCIA);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        return progresso;
    }

    private EnumMap<MedalSkill, SkillSnapshot> inicializarSnapshots() {
        EnumMap<MedalSkill, SkillSnapshot> progresso = new EnumMap<>(MedalSkill.class);
        for (MedalSkill skill : MedalSkill.values()) {
            progresso.put(skill, SkillSnapshot.vazio());
        }
        return progresso;
    }

    private void acumularSkill(
        EnumMap<MedalSkill, SkillSnapshot> progresso,
        MedalSkill skill,
        Object valorRaw,
        double limiar
    ) {
        double valor = limitar01(parseDouble(valorRaw));
        SkillSnapshot atual = progresso.getOrDefault(skill, SkillSnapshot.vazio());

        int novoProgresso = atual.progresso() + (valor >= limiar ? 1 : 0);
        Integer novoRecorde = Math.max(atual.recorde() == null ? 0 : atual.recorde(), (int) Math.round(valor * 100d));

        progresso.put(skill, new SkillSnapshot(novoProgresso, novoRecorde));
    }

    private double parseDouble(Object value) {
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        if (value == null) {
            return 0d;
        }

        String text = value.toString().trim();
        if (text.isBlank()) {
            return 0d;
        }

        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException ignored) {
            return 0d;
        }
    }

    private double limitar01(double value) {
        return Math.max(0d, Math.min(1d, value));
    }

    private record SkillSnapshot(int progresso, Integer recorde) {
        private static SkillSnapshot vazio() {
            return new SkillSnapshot(0, 0);
        }
    }
}
