package com.imetro.domain.dto.progresso;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.imetro.util.ParseTimeStampLocalDate;

public record ProgressaoRigorDto(
    UUID id,
    UUID alunoId,
    UUID disciplinaId,
    String subtopico,
    Double rigorAtual,
    Double rigorAlvo,
    Double ultimoAcertoEmRigor,
    Double ultimoErroEmRigor,
    Integer tentativasNoNivel,
    Integer acertosConsecutivos,
    Integer errosConsecutivos,
    Boolean precisaRevisao,
    String recomendacaoLivro,
    String recomendacaoPaginas,
    LocalDateTime atualizadoEm
) {

    public ProgressaoRigorDto {

        if (id == null) {
            id = UUID.randomUUID();
        }

        if (rigorAtual == null) {
            rigorAtual = 0.12;
        }

        if (rigorAlvo == null) {
            rigorAlvo = 0.70;
        }

        if (tentativasNoNivel == null) {
            tentativasNoNivel = 0;
        }

        if (acertosConsecutivos == null) {
            acertosConsecutivos = 0;
        }

        if (errosConsecutivos == null) {
            errosConsecutivos = 0;
        }

        if (precisaRevisao == null) {
            precisaRevisao = false;
        }

        if (recomendacaoLivro == null) {
            recomendacaoLivro = "";
        }

        if (recomendacaoPaginas == null) {
            recomendacaoPaginas = "";
        }

        if (atualizadoEm == null) {
            atualizadoEm = LocalDateTime.now();
        }
    }


    public boolean atingiuRigorAlvo() {
        return rigorAtual >= rigorAlvo;
    }


    public double percentualDominio() {
        if (rigorAlvo == 0) {
            return 0;
        }

        return (rigorAtual / rigorAlvo) * 100;
    }


    public boolean estaEmDificuldade() {
        return errosConsecutivos >= 3;
    }


    public Map<String, Object> toMap() {

        Map<String, Object> map = new HashMap<>();

        map.put("aluno_id", alunoId);
        map.put("disciplina_id", disciplinaId);
        map.put("subtopico", subtopico);
        map.put("rigor_atual", rigorAtual);
        map.put("rigor_alvo", rigorAlvo);
        map.put("ultimo_acerto_em_rigor", ultimoAcertoEmRigor);
        map.put("ultimo_erro_em_rigor", ultimoErroEmRigor);
        map.put("tentativas_no_nivel", tentativasNoNivel);
        map.put("acertos_consecutivos", acertosConsecutivos);
        map.put("erros_consecutivos", errosConsecutivos);
        map.put("precisa_revisao", precisaRevisao);
        map.put("recomendacao_livro", recomendacaoLivro);
        map.put("recomendacao_paginas", recomendacaoPaginas);
        map.put("atualizado_em", atualizadoEm);

        return map;
    }


    public static ProgressaoRigorDto fromMap(Map<String, Object> map) {

        return new ProgressaoRigorDto(
            UUID.randomUUID(),
            (UUID) map.get("aluno_id"),
            (UUID) map.get("disciplina_id"),
            (String) map.get("subtopico"),
            map.get("rigor_atual") != null
                ? ((Number) map.get("rigor_atual")).doubleValue()
                : 0.12,

            map.get("rigor_alvo") != null
                ? ((Number) map.get("rigor_alvo")).doubleValue()
                : 0.70,

            map.get("ultimo_acerto_em_rigor") != null
                ? ((Number) map.get("ultimo_acerto_em_rigor")).doubleValue()
                : null,

            map.get("ultimo_erro_em_rigor") != null
                ? ((Number) map.get("ultimo_erro_em_rigor")).doubleValue()
                : null,

            map.get("tentativas_no_nivel") != null
                ? ((Number) map.get("tentativas_no_nivel")).intValue()
                : 0,

            map.get("acertos_consecutivos") != null
                ? ((Number) map.get("acertos_consecutivos")).intValue()
                : 0,

            map.get("erros_consecutivos") != null
                ? ((Number) map.get("erros_consecutivos")).intValue()
                : 0,

            map.get("precisa_revisao") != null
                ? (Boolean) map.get("precisa_revisao")
                : false,

            (String) map.get("recomendacao_livro"),
            (String) map.get("recomendacao_paginas"),

            ParseTimeStampLocalDate.mapearDataHora(
                map.get("atualizado_em")
            )
        );
    }
}