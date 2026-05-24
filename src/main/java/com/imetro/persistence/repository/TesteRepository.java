package com.imetro.persistence.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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



    public UUID inserir(
        UUID candidatoId,
        UUID relatorioId,
        LocalDateTime dataTeste,
        Double resultado,
        LocalDateTime criadoEm,
        UUID diagnosticoId,
        UUID disciplinaId,
        String disciplinaNome,
        String nivelInicial,
        String nivelFinal,
        Integer limiteQuestoes,
        Double limiteInferior,
        Double limiteSuperior,
        String topicosJson,
        String subtopicosJson,
        int duracaoSegundos,
        int totalQuestoes,
        int totalAcertos,
        int totalErros,
        Double percentualAcerto,
        Double velocidade,
        Double precisao,
        Double consistencia,
        Double logica,
        Double resiliencia,
        String observacoes,
        LocalDateTime atualizadoEm,
        UUID configuracaoID
    ) throws SQLException {
        try (Connection conn = openRequiredConnection()) {
            return inserir(
                conn,
                candidatoId,
                relatorioId,
                dataTeste,
                resultado,
                criadoEm,
                diagnosticoId,
                disciplinaId,
                disciplinaNome,
                nivelInicial,
                nivelFinal,
                limiteQuestoes,
                limiteInferior,
                limiteSuperior,
                topicosJson,
                subtopicosJson,
                duracaoSegundos,
                totalQuestoes,
                totalAcertos,
                totalErros,
                percentualAcerto,
                velocidade,
                precisao,
                consistencia,
                logica,
                resiliencia,
                observacoes,
                atualizadoEm,
                configuracaoID
            );
        }
    }

    public UUID inserir(
        Connection conn,
        UUID candidatoId,
        UUID relatorioId,
        LocalDateTime dataTeste,
        Double resultado,
        LocalDateTime criadoEm,
        UUID diagnosticoId,
        UUID disciplinaId,
        String disciplinaNome,
        String nivelInicial,
        String nivelFinal,
        Integer limiteQuestoes,
        Double limiteInferior,
        Double limiteSuperior,
        String topicosJson,
        String subtopicosJson,
        int duracaoSegundos,
        int totalQuestoes,
        int totalAcertos,
        int totalErros,
        Double percentualAcerto,
        Double velocidade,
        Double precisao,
        Double consistencia,
        Double logica,
        Double resiliencia,
        String observacoes,
        LocalDateTime atualizadoEm,
        UUID configuracaoID
    ) throws SQLException {
        UUID id = UUID.randomUUID();
        int totalQuestoesPersistido = Math.max(0, totalQuestoes);
        int totalAcertosPersistido = Math.max(0, Math.min(totalAcertos, totalQuestoesPersistido));
        int totalErrosPersistido = Math.max(0, Math.min(totalErros, totalQuestoesPersistido - totalAcertosPersistido));
        Double resultadoPersistido = limitarPercentual(resultado);
        Double percentualPersistido = limitarPercentual(percentualAcerto);
        Double limiteInferiorPersistido = limitarUnitario(limiteInferior);
        Double limiteSuperiorPersistido = limitarUnitario(limiteSuperior);
        LocalDateTime criadoEmPersistido = criadoEm == null ? LocalDateTime.now() : criadoEm;
        LocalDateTime atualizadoEmPersistido = atualizadoEm == null ? criadoEmPersistido : atualizadoEm;
        String topicosPersistidos = normalizarJsonArray(topicosJson);
        String subtopicosPersistidos = normalizarJsonArray(subtopicosJson);

        if (limiteInferiorPersistido != null && limiteSuperiorPersistido != null
            && limiteInferiorPersistido > limiteSuperiorPersistido) {
            double ajusteInferior = limiteSuperiorPersistido;
            limiteSuperiorPersistido = limiteInferiorPersistido;
            limiteInferiorPersistido = ajusteInferior;
        }

        String sql = """
            insert into testes (
              id,
              candidato_id,
              relatorio_id,
              data_teste,
              resultado,
              criado_em,
              diagnostico_id,
              disciplina_id,
              disciplina_nome,
              nivel_inicial,
              nivel_final,
              limite_questoes,
              limite_inferior,
              limite_superior,
              topicos,
              subtopicos,
              duracao_segundos,
              total_questoes,
              total_acertos,
              total_erros,
              percentual_acerto,
              velocidade,
              precisao,
              consistencia,
              logica,
              resiliencia,
              observacoes,
              atualizado_em,
              configuracao_teste_adaptativo_id
            ) values (
              ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, cast(? as jsonb), cast(? as jsonb), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?
            )
            """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, id);
            stmt.setObject(2, candidatoId);
            stmt.setObject(3, relatorioId);
            stmt.setTimestamp(4, toTimestamp(dataTeste));
            stmt.setObject(5, toNullableFloat(resultadoPersistido));
            stmt.setTimestamp(6, toTimestamp(criadoEmPersistido));
            stmt.setObject(7, diagnosticoId);
            stmt.setObject(8, disciplinaId);
            stmt.setString(9, disciplinaNome);
            stmt.setString(10, nivelInicial);
            stmt.setString(11, nivelFinal);
            stmt.setObject(12, limiteQuestoes);
            stmt.setObject(13, limiteInferiorPersistido);
            stmt.setObject(14, limiteSuperiorPersistido);
            stmt.setString(15, topicosPersistidos);
            stmt.setString(16, subtopicosPersistidos);
            stmt.setInt(17, Math.max(0, duracaoSegundos));
            stmt.setInt(18, totalQuestoesPersistido);
            stmt.setInt(19, totalAcertosPersistido);
            stmt.setInt(20, totalErrosPersistido);
            stmt.setObject(21, percentualPersistido);
            stmt.setObject(22, toNullableFloat(limitarUnitario(velocidade)));
            stmt.setObject(23, toNullableFloat(limitarUnitario(precisao)));
            stmt.setObject(24, toNullableFloat(limitarUnitario(consistencia)));
            stmt.setObject(25, toNullableFloat(limitarUnitario(logica)));
            stmt.setObject(26, toNullableFloat(limitarUnitario(resiliencia)));
            stmt.setString(27, observacoes);
            stmt.setTimestamp(28, toTimestamp(atualizadoEmPersistido));
            stmt.setObject(29, configuracaoID);
            stmt.executeUpdate();
        }
        return id;
    }

    private List<Map<String, Object>> readRows(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int totalColunas = meta.getColumnCount();
        List<Map<String, Object>> rows =  new ArrayList<>();

        while (rs.next()) {
            Map<String, Object> row = new LinkedHashMap<>();
            for (int i = 1; i <= totalColunas; i++) {
                row.put(meta.getColumnLabel(i), rs.getObject(i));
            }
            rows.add(row);
        }

        return rows;
    }

    private Double limitarPercentual(Double valor) {
        if (valor == null) {
            return null;
        }
        return Math.max(0d, Math.min(100d, valor));
    }

    private Double limitarUnitario(Double valor) {
        if (valor == null) {
            return null;
        }
        return Math.max(0d, Math.min(1d, valor));
    }

    private String normalizarJsonArray(String json) {
        return json == null || json.isBlank() ? "[]" : json;
    }

    private Float toNullableFloat(Double valor) {
        return valor == null ? null : valor.floatValue();
    }

    private Timestamp toTimestamp(LocalDateTime valor) {
        return valor == null ? null : Timestamp.valueOf(valor);
    }


}
