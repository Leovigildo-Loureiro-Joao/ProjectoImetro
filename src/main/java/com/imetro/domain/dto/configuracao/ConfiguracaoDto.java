package com.imetro.domain.dto.configuracao;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.imetro.util.DtoMapperSupport;

public record ConfiguracaoDto(UUID id,
    UUID user_id,
    Integer temp_adapt_val ,
    String temp_adapt_unit,
    Integer speed_temp_val ,
    String speed_temp_unit,
    Integer long_test_q,
    Integer norm_test_q,
    Integer desaf_test_q ,
    Integer extra_test_q ,
    String nivel_dificuldade_padrao,
    String modo_escolhas,
    Integer velocidade_segundos_por_percent ,
    Integer resiliencia_repeticoes_por_dia ,
    Integer precisao_consecutivas ,
    Integer logica_qtd_desafiante_extra ,
    double consistencia_percentual_min ,
    LocalDateTime criado_em,
    LocalDateTime atualizado_em)
    {



    public Map<String, ?> toMap() {
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("id", id());
        values.put("user_id", user_id());
        values.put("temp_adapt_val", temp_adapt_val());
        values.put("temp_adapt_unit", temp_adapt_unit());
        values.put("speed_temp_val", speed_temp_val());
        values.put("speed_temp_unit", speed_temp_unit());
        values.put("long_test_q", long_test_q());
        values.put("norm_test_q", norm_test_q());
        values.put("desaf_test_q", desaf_test_q());
        values.put("extra_test_q", extra_test_q());
        values.put("nivel_dificuldade_padrao", nivel_dificuldade_padrao());
        values.put("modo_escolhas", modo_escolhas());
        values.put("velocidade_segundos_por_percent", velocidade_segundos_por_percent());
        values.put("resiliencia_repeticoes_por_dia", resiliencia_repeticoes_por_dia());
        values.put("precisao_consecutivas", precisao_consecutivas());
        values.put("logica_qtd_desafiante_extra", logica_qtd_desafiante_extra());
        values.put("consistencia_percentual_min", consistencia_percentual_min());
        values.put("criado_em", criado_em());
        values.put("atualizado_em", atualizado_em());
        return values;
    }

    public Map<String, ?> toMapUpdate() {
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("user_id", user_id());
        values.put("temp_adapt_val", temp_adapt_val());
        values.put("temp_adapt_unit", temp_adapt_unit());
        values.put("speed_temp_val", speed_temp_val());
        values.put("speed_temp_unit", speed_temp_unit());
        values.put("long_test_q", long_test_q());
        values.put("norm_test_q", norm_test_q());
        values.put("desaf_test_q", desaf_test_q());
        values.put("extra_test_q", extra_test_q());
        values.put("nivel_dificuldade_padrao", nivel_dificuldade_padrao());
        values.put("modo_escolhas", modo_escolhas());
        values.put("velocidade_segundos_por_percent", velocidade_segundos_por_percent());
        values.put("resiliencia_repeticoes_por_dia", resiliencia_repeticoes_por_dia());
        values.put("precisao_consecutivas", precisao_consecutivas());
        values.put("logica_qtd_desafiante_extra", logica_qtd_desafiante_extra());
        values.put("consistencia_percentual_min", consistencia_percentual_min());
        values.put("criado_em", criado_em());
        values.put("atualizado_em", atualizado_em());
        return values;
    }


     public static ConfiguracaoDto fromMap(Map<String, ?> map) {
        Objects.requireNonNull(map, "map");

        return new ConfiguracaoDto(
            DtoMapperSupport.parseUuid(map.get("id")),
            DtoMapperSupport.parseUuid(map.get("user_id")),
            DtoMapperSupport.parseInteger(map.get("temp_adapt_val")),
            DtoMapperSupport.parseText(map.get("temp_adapt_unit")),
            DtoMapperSupport.parseInteger(map.get("speed_temp_val")),
            DtoMapperSupport.parseText(map.get("speed_temp_unit")),
            DtoMapperSupport.parseInteger(map.get("long_test_q")),
            DtoMapperSupport.parseInteger(map.get("norm_test_q")),
            DtoMapperSupport.parseInteger(map.get("desaf_test_q")),
            DtoMapperSupport.parseInteger(map.get("extra_test_q")),
            DtoMapperSupport.parseText(map.get("nivel_dificuldade_padrao")),
            DtoMapperSupport.parseText(map.get("modo_escolhas")),
            DtoMapperSupport.parseInteger(map.get("velocidade_segundos_por_percent")),
            DtoMapperSupport.parseInteger(map.get("resiliencia_repeticoes_por_dia")),
            DtoMapperSupport.parseInteger(map.get("precisao_consecutivas")),
            DtoMapperSupport.parseInteger(map.get("logica_qtd_desafiante_extra")),
            DtoMapperSupport.parseDouble(map.get("consistencia_percentual_min")),
            DtoMapperSupport.parseDateTime(map.get("criado_em")),
            DtoMapperSupport.parseDateTime(map.get("atualizado_em"))

        );
    }



}
