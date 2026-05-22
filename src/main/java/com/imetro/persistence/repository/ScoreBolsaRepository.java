package com.imetro.persistence.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import com.imetro.util.DtoMapperSupport;

public class ScoreBolsaRepository extends JdbcBasicSqlRepository {

    public ScoreBolsaRepository() {
        super("score_bolsas", "id");
    }

    public void upsertWeeklyScore(
        UUID candidatoId,
        UUID bolsaId,
        UUID testeId,
        double score,
        String destaque,
        int totalQuestoes,
        int totalAcertos,
        double percentualAcerto,
        int tempoTotalSegundos,
        boolean elegivel,
        String criteriosJson,
        LocalDate semanaRef
    ) throws SQLException {
        try (var conn = openRequiredConnection()) {
            upsertWeeklyScore(
                conn,
                candidatoId,
                bolsaId,
                testeId,
                score,
                destaque,
                totalQuestoes,
                totalAcertos,
                percentualAcerto,
                tempoTotalSegundos,
                elegivel,
                criteriosJson,
                semanaRef
            );
        }
    }

    public void upsertWeeklyScore(
        Connection conn,
        UUID candidatoId,
        UUID bolsaId,
        UUID testeId,
        double score,
        String destaque,
        int totalQuestoes,
        int totalAcertos,
        double percentualAcerto,
        int tempoTotalSegundos,
        boolean elegivel,
        String criteriosJson,
        LocalDate semanaRef
    ) throws SQLException {
        String sql = """
            insert into score_bolsas (
              id,
              candidato_id,
              bolsa_id,
              teste_id,
              score,
              destaque,
              total_questoes,
              total_acertos,
              percentual_acerto,
              tempo_total_segundos,
              elegivel,
              criterios_json,
              semana_ref,
              criado_em,
              atualizado_em
            ) values (
              ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, cast(? as jsonb), ?, now(), now()
            )
            on conflict (candidato_id, bolsa_id, semana_ref)
            do update set
              teste_id = excluded.teste_id,
              score = excluded.score,
              destaque = excluded.destaque,
              total_questoes = excluded.total_questoes,
              total_acertos = excluded.total_acertos,
              percentual_acerto = excluded.percentual_acerto,
              tempo_total_segundos = excluded.tempo_total_segundos,
              elegivel = excluded.elegivel,
              criterios_json = excluded.criterios_json,
              atualizado_em = now()
            """;

        try (var stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, UUID.randomUUID());
            stmt.setObject(2, candidatoId);
            stmt.setObject(3, bolsaId);
            stmt.setObject(4, testeId);
            stmt.setDouble(5, Math.max(0d, Math.min(100d, score)));
            stmt.setString(6, destaque == null ? "" : destaque);
            stmt.setInt(7, Math.max(0, totalQuestoes));
            stmt.setInt(8, Math.max(0, totalAcertos));
            stmt.setDouble(9, Math.max(0d, Math.min(100d, percentualAcerto)));
            stmt.setInt(10, Math.max(0, tempoTotalSegundos));
            stmt.setBoolean(11, elegivel);
            stmt.setString(12, criteriosJson == null || criteriosJson.isBlank() ? "{}" : criteriosJson);
            stmt.setObject(13, semanaRef == null ? LocalDate.now() : semanaRef);
            stmt.executeUpdate();
        }
    }

    public Map<String, Object> criarResumoCriterios(
        String disciplinaFoco,
        int medalhasMin,
        int desempenhoMin,
        int evolucaoMin,
        int precisaoMin,
        int velocidadeMin,
        String modoResposta
    ) {
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("disciplinaFoco", disciplinaFoco);
        values.put("medalhasMin", medalhasMin);
        values.put("desempenhoMin", desempenhoMin);
        values.put("evolucaoMin", evolucaoMin);
        values.put("precisaoMin", precisaoMin);
        values.put("velocidadeMin", velocidadeMin);
        values.put("modoResposta", modoResposta);
        return values;
    }

    public List<WeeklyLeaderboardEntry> findWeeklyLeaderboard(LocalDate inicioSemana, LocalDate fimSemana) throws SQLException {
        if (inicioSemana == null || fimSemana == null) {
            return List.of();
        }

        String sql = """
            with tentativas_semana as (
              select
                sb.candidato_id,
                coalesce(nullif(trim(u.nome), ''), 'Candidato') as candidato_nome,
                sb.bolsa_id,
                coalesce(nullif(trim(b.nome), ''), 'Bolsa semanal') as bolsa_nome,
                sb.score,
                sb.percentual_acerto,
                sb.tempo_total_segundos,
                sb.total_acertos,
                sb.total_questoes,
                sb.destaque,
                sb.semana_ref,
                coalesce(sb.atualizado_em, sb.criado_em) as ordenacao_ref,
                row_number() over (
                  partition by sb.candidato_id
                  order by
                    sb.score desc,
                    sb.percentual_acerto desc,
                    sb.tempo_total_segundos asc,
                    coalesce(sb.atualizado_em, sb.criado_em) asc
                ) as melhor_do_candidato
              from score_bolsas sb
              left join users u on u.id = sb.candidato_id
              left join bolsas b on b.id = sb.bolsa_id
              where sb.elegivel = true
                and sb.semana_ref between ? and ?
            ),
            ranking as (
              select
                candidato_id,
                candidato_nome,
                bolsa_id,
                bolsa_nome,
                score,
                percentual_acerto,
                tempo_total_segundos,
                total_acertos,
                total_questoes,
                destaque,
                semana_ref,
                rank() over (
                  order by
                    score desc,
                    percentual_acerto desc,
                    tempo_total_segundos asc,
                    candidato_nome asc
                ) as posicao
              from tentativas_semana
              where melhor_do_candidato = 1
            )
            select
              candidato_id,
              candidato_nome,
              bolsa_id,
              bolsa_nome,
              score,
              percentual_acerto,
              tempo_total_segundos,
              total_acertos,
              total_questoes,
              destaque,
              semana_ref,
              posicao
            from ranking
            order by posicao asc, candidato_nome asc
            """;

        try (var conn = openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, inicioSemana);
            stmt.setObject(2, fimSemana);
            try (var rs = stmt.executeQuery()) {
                return readAllRows(rs).stream()
                    .map(WeeklyLeaderboardEntry::fromMap)
                    .toList();
            }
        }
    }

    public record WeeklyLeaderboardEntry(
        UUID candidatoId,
        String candidatoNome,
        UUID bolsaId,
        String bolsaNome,
        double score,
        double percentualAcerto,
        int tempoTotalSegundos,
        int totalAcertos,
        int totalQuestoes,
        String destaque,
        LocalDate semanaRef,
        int posicao
    ) {
        public static WeeklyLeaderboardEntry fromMap(Map<String, ?> map) {
            return new WeeklyLeaderboardEntry(
                DtoMapperSupport.parseUuid(map.get("candidato_id")),
                fallbackText(DtoMapperSupport.parseText(map.get("candidato_nome")), "Candidato"),
                DtoMapperSupport.parseUuid(map.get("bolsa_id")),
                fallbackText(DtoMapperSupport.parseText(map.get("bolsa_nome")), "Bolsa semanal"),
                DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseDouble(map.get("score")), 0d),
                DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseDouble(map.get("percentual_acerto")), 0d),
                DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseInteger(map.get("tempo_total_segundos")), 0),
                DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseInteger(map.get("total_acertos")), 0),
                DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseInteger(map.get("total_questoes")), 0),
                DtoMapperSupport.parseText(map.get("destaque")),
                parseLocalDate(map.get("semana_ref")),
                Math.max(0, DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseInteger(map.get("posicao")), 0))
            );
        }

        private static String fallbackText(String value, String fallback) {
            return value == null || value.isBlank() ? fallback : value;
        }

        private static LocalDate parseLocalDate(Object value) {
            if (value instanceof LocalDate localDate) {
                return localDate;
            }
            if (value instanceof Date date) {
                return date.toLocalDate();
            }
            if (value == null) {
                return null;
            }
            try {
                return LocalDate.parse(value.toString());
            } catch (RuntimeException ignored) {
                return null;
            }
        }
    }
}
