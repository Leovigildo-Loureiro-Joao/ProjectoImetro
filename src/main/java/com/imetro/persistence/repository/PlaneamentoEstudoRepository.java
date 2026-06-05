package com.imetro.persistence.repository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class PlaneamentoEstudoRepository extends JdbcBasicSqlRepository {

    public PlaneamentoEstudoRepository() {
        super("planeamentos_estudo", "id");
    }

    public void upsertSnapshot(
        UUID candidatoId,
        LocalDate semanaInicio,
        LocalDate semanaFim,
        String assinaturaFonte,
        double pontuacaoHero,
        String resumoHero,
        String acertoMedio,
        String ritmoMedio,
        String consistenciaMedia,
        String focoAtual,
        String resumoJson
    ) throws SQLException {
        String sql = """
            insert into planeamentos_estudo (
              id,
              candidato_id,
              semana_inicio,
              semana_fim,
              assinatura_fonte,
              pontuacao_hero,
              resumo_hero,
              acerto_medio,
              ritmo_medio,
              consistencia_media,
              foco_atual,
              resumo_json,
              criado_em,
              atualizado_em
            ) values (
              ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, cast(? as jsonb), now(), now()
            )
            on conflict (candidato_id, semana_inicio)
            do update set
              semana_fim = excluded.semana_fim,
              assinatura_fonte = excluded.assinatura_fonte,
              pontuacao_hero = excluded.pontuacao_hero,
              resumo_hero = excluded.resumo_hero,
              acerto_medio = excluded.acerto_medio,
              ritmo_medio = excluded.ritmo_medio,
              consistencia_media = excluded.consistencia_media,
              foco_atual = excluded.foco_atual,
              resumo_json = excluded.resumo_json,
              atualizado_em = now()
            where planeamentos_estudo.assinatura_fonte is distinct from excluded.assinatura_fonte
            """;

        try (var conn = openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, UUID.randomUUID());
            stmt.setObject(2, candidatoId);
            stmt.setObject(3, semanaInicio);
            stmt.setObject(4, semanaFim);
            stmt.setString(5, assinaturaFonte == null ? "" : assinaturaFonte);
            stmt.setDouble(6, clamp(pontuacaoHero, 0d, 100d));
            stmt.setString(7, textoOuVazio(resumoHero));
            stmt.setString(8, textoOuVazio(acertoMedio));
            stmt.setString(9, textoOuVazio(ritmoMedio));
            stmt.setString(10, textoOuVazio(consistenciaMedia));
            stmt.setString(11, textoOuVazio(focoAtual));
            stmt.setString(12, resumoJson == null || resumoJson.isBlank() ? "{}" : resumoJson);
            stmt.executeUpdate();
        }
    }

    public Optional<Map<String, Object>> findByCandidatoIdESemana(UUID candidatoId, LocalDate semanaInicio) throws SQLException {
        String sql = """
            select *
            from planeamentos_estudo
            where candidato_id = ?
              and semana_inicio = ?
            limit 1
            """;

        try (var conn = openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, candidatoId);
            stmt.setObject(2, semanaInicio);
            try (var rs = stmt.executeQuery()) {
                var rows = readAllRows(rs);
                if (rows.isEmpty()) {
                    return Optional.empty();
                }
                return Optional.of(rows.getFirst());
            }
        }
    }

    public Optional<Map<String, Object>> findLatestByCandidatoId(UUID candidatoId) throws SQLException {
        String sql = """
            select *
            from planeamentos_estudo
            where candidato_id = ?
            order by semana_inicio desc, atualizado_em desc
            limit 1
            """;

        try (var conn = openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, candidatoId);
            try (var rs = stmt.executeQuery()) {
                var rows = readAllRows(rs);
                if (rows.isEmpty()) {
                    return Optional.empty();
                }
                return Optional.of(rows.getFirst());
            }
        }
    }

    public int prolongarUltimoPlanejamento(UUID candidatoId, LocalDate novaSemanaFim) throws SQLException {
        if (candidatoId == null || novaSemanaFim == null) {
            return 0;
        }

        String sql = """
            update planeamentos_estudo
            set semana_fim = ?,
                atualizado_em = now()
            where id = (
              select id
              from planeamentos_estudo
              where candidato_id = ?
              order by semana_inicio desc, atualizado_em desc
              limit 1
            )
            """;

        try (var conn = openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, novaSemanaFim);
            stmt.setObject(2, candidatoId);
            return stmt.executeUpdate();
        }
    }

    public int deleteByCandidatoId(UUID candidatoId) throws SQLException {
        if (candidatoId == null) {
            return 0;
        }

        String sql = """
            delete from planeamentos_estudo
            where candidato_id = ?
            """;

        try (var conn = openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, candidatoId);
            return stmt.executeUpdate();
        }
    }

    private String textoOuVazio(String valor) {
        return valor == null ? "" : valor;
    }

    private double clamp(double valor, double min, double max) {
        return Math.max(min, Math.min(max, valor));
    }
}
