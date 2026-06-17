package com.imetro.persistence.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.imetro.util.CalculoStats;

public class ProgressaoRigorRepository extends JdbcBasicSqlRepository{

    public ProgressaoRigorRepository() {
        super("progressao_rigor", "id");
    }

    public void upsertProgressaoRigor(
        UUID idAtual,
        UUID candidatoId,
        UUID disciplinaId,
        String subtopico,
        double rigorAtual,
        double rigorAlvo,
        Double ultimoAcertoEmRigor,
        Double ultimoErroEmRigor,
        int tentativasNoNivel,
        int acertosConsecutivos,
        int errosConsecutivos,
        boolean precisaRevisao,
        String recomendacaoLivro,
        String recomendacaoPaginas
    ) throws SQLException {
        String sql = """
            insert into progressao_rigor (
              id,
              aluno_id,
              disciplina_id,
              subtopico,
              rigor_atual,
              rigor_alvo,
              ultimo_acerto_em_rigor,
              ultimo_erro_em_rigor,
              tentativas_no_nivel,
              acertos_consecutivos,
              erros_consecutivos,
              precisa_revisao,
              recomendacao_livro,
              recomendacao_paginas,
              atualizado_em
            ) values (
              ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now()
            )
            on conflict (aluno_id, disciplina_id, subtopico) do update
            set rigor_atual = excluded.rigor_atual,
                rigor_alvo = excluded.rigor_alvo,
                ultimo_acerto_em_rigor = excluded.ultimo_acerto_em_rigor,
                ultimo_erro_em_rigor = excluded.ultimo_erro_em_rigor,
                tentativas_no_nivel = excluded.tentativas_no_nivel,
                acertos_consecutivos = excluded.acertos_consecutivos,
                erros_consecutivos = excluded.erros_consecutivos,
                precisa_revisao = excluded.precisa_revisao,
                recomendacao_livro = excluded.recomendacao_livro,
                recomendacao_paginas = excluded.recomendacao_paginas,
                atualizado_em = now()
            """;

        try (PreparedStatement stmt = openRequiredConnection().prepareStatement(sql)) {
            stmt.setObject(1, idAtual == null ? UUID.randomUUID() : idAtual);
            stmt.setObject(2, candidatoId);
            stmt.setObject(3, disciplinaId);
            stmt.setString(4, subtopico);
            stmt.setDouble(5,  CalculoStats.limitarRigor(rigorAtual));
            stmt.setDouble(6, CalculoStats.limitarRigor(rigorAlvo));
            stmt.setObject(7, ultimoAcertoEmRigor == null ? null : CalculoStats.limitarRigor(ultimoAcertoEmRigor));
            stmt.setObject(8, ultimoErroEmRigor == null ? null : CalculoStats.limitarRigor(ultimoErroEmRigor));
            stmt.setInt(9, Math.max(0, tentativasNoNivel));
            stmt.setInt(10, Math.max(0, acertosConsecutivos));
            stmt.setInt(11, Math.max(0, errosConsecutivos));
            stmt.setBoolean(12, precisaRevisao);
            stmt.setString(13, recomendacaoLivro);
            stmt.setString(14, recomendacaoPaginas);
            stmt.executeUpdate();
        }
    }

    public int deleteByCandidatoId(UUID candidatoId) throws SQLException {
        if (candidatoId == null) {
            return 0;
        }

        String sql = """
            delete from progressao_rigor
            where aluno_id = ?
            """;

        try (var conn = openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, candidatoId);
            return stmt.executeUpdate();
        }
    }

    public List<Map<String,Object>> findByCandidato(UUID candidato) throws SQLException{
         String sql = "select * from progressao_rigor where aluno_id=?";
        try (Connection conn = openRequiredConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1,candidato);
            ResultSet rs = stmt.executeQuery();
            return readAllRows(rs);
        }
    }
}
