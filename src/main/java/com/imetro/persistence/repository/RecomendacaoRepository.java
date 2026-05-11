package com.imetro.persistence.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import com.imetro.util.CalculoStats;

public class RecomendacaoRepository extends JdbcBasicSqlRepository{

    public RecomendacaoRepository() {
        super("recomendacoes_rigor", "id");
    }

    public void inserirRecomendacaoRigor(
        UUID diagnosticoId,
        String subtopico,
        double rigorRecomendado,
        double nivelAtual,
        Double progressoAtingido,
        String recomendacaoLivro,
        String recomendacaoPaginas,
        String exerciciosSugeridosJson,
        boolean precisaNovoDiagnostico
    ) throws SQLException {
        String sql = """
            insert into recomendacoes_rigor (
              id,
              diagnostico_id,
              subtopico,
              rigor_recomendado,
              nivel_atual,
              progresso_atingido,
              recomendacao_livro,
              recomendacao_paginas,
              exercicios_sugeridos,
              precisa_novo_diagnostico,
              criado_em
            ) values (
              ?, ?, ?, ?, ?, ?, ?, ?, cast(? as jsonb), ?, now()
            )
            """;

        try (PreparedStatement stmt = openRequiredConnection().prepareStatement(sql)) {
            stmt.setObject(1, UUID.randomUUID());
            stmt.setObject(2, diagnosticoId);
            stmt.setString(3, subtopico);
            stmt.setDouble(4, CalculoStats.limitarRigor(rigorRecomendado));
            stmt.setDouble(5, CalculoStats.limitarRigor(nivelAtual));
            stmt.setObject(6, progressoAtingido == null ? null : Math.max(0d, Math.min(1d, progressoAtingido)));
            stmt.setString(7, recomendacaoLivro);
            stmt.setString(8, recomendacaoPaginas);
            stmt.setString(9, exerciciosSugeridosJson);
            stmt.setBoolean(10, precisaNovoDiagnostico);
            stmt.executeUpdate();
        }
    }

}
