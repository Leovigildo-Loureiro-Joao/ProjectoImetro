package com.imetro.domain.dto.diagnostico;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import com.imetro.util.ParseTimeStampLocalDate;

public record DiagnosticoDto(
    UUID id ,
    UUID candidato_id,
    UUID disciplina_id,
    UUID relatorio_id,
    String disciplina_nome,
    LocalDateTime iniciado_em,
    LocalDateTime concluido_em,
    int duracao_segundos,
    int total_questoes,
    int total_acertos,
    int total_erros,
    double percentual_acerto ,
    double evolucao_percentual,
    String nivel,
    float velocidade,
    float precisao ,
    float consistencia ,
    float logica ,
    float resiliencia ,
    String observacoes,
    LocalDateTime criado_em,
    LocalDateTime atualizado_em
) {
   public static DiagnosticoDto ParseMapDto(Map<String,Object> link){
        UUID id = (UUID)link.get("id");
        UUID candidato_id = (UUID)link.get("candidato_id");
        UUID disciplina_id = (UUID)link.get("disciplina_id");
        UUID relatorio_id = (UUID)link.get("relatorio_id");
        String nome = link.get("nome").toString();
        LocalDateTime iniciado = ParseTimeStampLocalDate.mapearDataHora(link.get("iniciado_em"));
        LocalDateTime concluido = ParseTimeStampLocalDate.mapearDataHora(link.get("concluido_em"));
        int duracao_seg=(int)link.get("duracao_segundos");
        int total_questoes=(int)link.get("total_questoes");
        int total_acertos=(int)link.get("total_acertos");
        int total_erros=(int)link.get("total_erros");
        double percentual_acerto=(int)link.get("percentual_acerto");
        double evolucao_percentual=(int)link.get("evolucao_percentual");
        float velocidade=(float)link.get("velocidade");
        float precisao=(float)link.get("precisao");
        float consistencia=(float)link.get("consistencia");
        float logica=(float)link.get("logica");
        float resiliencia=(float)link.get("resiliencia");
        String observacoes = link.get("observacoes").toString();
        String nivel = link.get("nivel").toString();
        LocalDateTime criado_em = ParseTimeStampLocalDate.mapearDataHora(link.get("criado_em"));
        LocalDateTime atualizado_em = ParseTimeStampLocalDate.mapearDataHora(link.get("atualizado_em"));
        return new DiagnosticoDto(id, candidato_id, disciplina_id, relatorio_id, nome, iniciado,concluido, duracao_seg, total_questoes, total_acertos, total_erros, percentual_acerto, evolucao_percentual, nivel, velocidade, precisao, consistencia, logica, resiliencia, observacoes, criado_em, atualizado_em);
   }
}
