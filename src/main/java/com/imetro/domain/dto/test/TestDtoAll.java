package com.imetro.domain.dto.test;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import com.imetro.util.ParseTimeStampLocalDate;

public record TestDtoAll(
    UUID id ,
    UUID candidato_id,
    UUID orientador_id,
    UUID disciplina_id,
    UUID diagnostico_id,
    UUID relatorio_id,
    String disciplina_nome,
    LocalDateTime data_teste,
    float resultado,
    String nivel_inicial,
    String nivel_final,
    int limite_questoes ,
    double limite_inferior,
    double limite_superior,
    Object[] topicos,
    Object[] subtopicos,
    int duracao_segundos,
    int total_questoes,
    int total_acertos,
    int total_erros,
    double percentual_acerto ,
    float velocidade,
    float precisao ,
    float consistencia ,
    float logica ,
    float resiliencia ,
    String observacoes,
    LocalDateTime criado_em,
    LocalDateTime atualizado_em
) {
     public static TestDtoAll ParseMapDto(Map<String,Object> link){
        UUID id = (UUID)link.get("id");
        UUID candidato_id = (UUID)link.get("candidato_id");
        UUID orientador_id = (UUID)link.get("orientador_id");
        UUID disciplina_id = (UUID)link.get("disciplina_id");
        UUID relatorio_id = (UUID)link.get("relatorio_id");
        UUID diagnostico_id = (UUID)link.get("diagnostico_id");
        String nome = link.get("disciplina_nome").toString();
        String nivel_inicial=link.get("nivel_inicial").toString();
        String nivel_final=link.get("nivel_final").toString();
        int duracao_seg=(int)link.get("duracao_segundos");
        int total_questoes=(int)link.get("total_questoes");
        int total_acertos=(int)link.get("total_acertos");
        int total_erros=(int)link.get("total_erros");
        double percentual_acerto=(double)link.get("percentual_acerto");
         int limite_questoes=(int)link.get("limite_questoes");
        double limite_inferior=(double)link.get("limite_inferior");
        double limite_superior=(double)link.get("limite_superior");
        float velocidade=(float)link.get("velocidade");
        float precisao=(float)link.get("precisao");
        float consistencia=(float)link.get("consistencia");
        float logica=(float)link.get("logica");
        float resiliencia=(float)link.get("resiliencia");
        float resultado=(float)link.get("resultado");
        String observacoes = link.get("observacoes").toString();
        LocalDateTime criado_em = ParseTimeStampLocalDate.mapearDataHora(link.get("criado_em"));
        LocalDateTime data_teste = ParseTimeStampLocalDate.mapearDataHora(link.get("data_teste"));
        LocalDateTime atualizado_em = ParseTimeStampLocalDate.mapearDataHora(link.get("atualizado_em"));
        return new TestDtoAll(id, candidato_id, orientador_id,disciplina_id, diagnostico_id,relatorio_id, nome, data_teste,resultado, nivel_inicial,nivel_final,limite_questoes,limite_inferior,limite_superior,null,null, duracao_seg,total_questoes, total_acertos, total_erros, percentual_acerto, velocidade, precisao, consistencia, logica, resiliencia, observacoes, criado_em, atualizado_em);
   }
}
