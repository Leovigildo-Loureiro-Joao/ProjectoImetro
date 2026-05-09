package com.imetro.domain.dto.stats;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public record Teste_Stat(
    UUID id,
    UUID teste_id,
    UUID diagnostico_id,
    UUID candidato_id,
    UUID disciplina_id,
    String  disciplina_nome,
    String origem,
    Integer tempo_total_segundos,
    Double tempo_medio_segundos,
    Integer total_questoes,
    Integer total_acertos,
    Integer total_erros,
    Double  percentual_acerto,
    Double velocidade,
    Double precisao,
    Double consistencia,
    Double logica,
    Double resiliencia,
    String erros_comuns,
    String melhorias,
    String  observacoes,
    LocalDateTime criado_em,
    LocalDateTime atualizado_em
) {

    public Map<String,Object> toMap(){
        Map<String,Object> map = new LinkedHashMap<>();

        map.put( "id",id);
        map.put(  "teste_id",teste_id);
        map.put( "diagnostico_id",diagnostico_id);
        map.put( "candidato_id",candidato_id);
        map.put( "disciplina_id",disciplina_id);
        map.put( "disciplina_nome",disciplina_nome);
        map.put(  "origem",origem);
        map.put( "tempo_total_segundos",tempo_total_segundos);
        map.put( "tempo_medio_segundos",tempo_medio_segundos);
        map.put(  "total_questoes",total_questoes);
        map.put(  "total_acertos",total_acertos);
        map.put(  "total_erros",total_erros);
        map.put( "percentual_acerto",percentual_acerto);
        map.put( "velocidade",velocidade);
        map.put( "precisao",precisao);
        map.put(  "consistencia",consistencia);
        map.put(  "logica",logica);
        map.put(  "resiliencia",resiliencia);
        map.put(   "erros_comuns",erros_comuns);
        map.put(    "melhorias",melhorias);
        map.put(   "observacoes",observacoes);
        map.put(  "criado_em",criado_em);
        map.put(   "atualizado_em",atualizado_em);
        return map;
    }

    public static Teste_Stat ParseDto(Map<String,Object> map) {
        UUID id= (UUID)map.get( "id");
        UUID teste_id= (UUID) map.get(  "teste_id");
        UUID diagnostico_id= (UUID)  map.get( "diagnostico_id");
        UUID candidato_id= (UUID) map.get( "candidato_id");
        UUID disciplina_id= (UUID)map.get( "disciplina_id");
        String  disciplina_nome= (String) map.get(  "disciplina_nome");
        String origem= (String)  map.get(  "origem");
        Integer tempo_total_segundos= (Integer)  map.get( "tempo_total_segundos");
        Double tempo_medio_segundos= (Double)  map.get( "tempo_medio_segundos");
        Integer total_questoes= (Integer)  map.get(  "total_questoes");
        Integer total_acertos= (Integer) map.get(  "total_acertos");
        Integer total_erros= (Integer) map.get(  "total_erros");
        Double  percentual_acerto= (Double) map.get( "percentual_acerto");
        Double velocidade= (Double)map.get( "velocidade");
        Double precisao= (Double)map.get( "precisao");
        Double consistencia= (Double)map.get( "consistencia");
        Double logica= (Double)map.get( "logica");
        Double resiliencia= (Double)map.get( "resiliencia");
        String erros_comuns= (String)   map.get(   "erros_comuns");
        String melhorias= (String) map.get(    "melhorias");
        String  observacoes= (String)   map.get(   "observacoes");
        LocalDateTime criado_em= (LocalDateTime) map.get(  "criado_em");
        LocalDateTime atualizado_em= (LocalDateTime)map.get(   "atualizado_em");
        return new Teste_Stat(id, teste_id, diagnostico_id, candidato_id, disciplina_id, disciplina_nome, origem, tempo_total_segundos, tempo_medio_segundos, total_questoes, total_acertos, total_erros, percentual_acerto, velocidade, precisao, consistencia, logica, resiliencia, erros_comuns, melhorias, observacoes, criado_em, atualizado_em)
        ;
    }

}
