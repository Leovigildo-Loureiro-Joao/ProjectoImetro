package com.imetro.domain.dto.bolsa;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.imetro.util.DtoMapperSupport;

public record BolsaDto(
    UUID id,
    String nome,
    String tipo,
    Integer match,
    Integer vagas,
    String cobertura,
    String destaque,
    String risco,
    LocalDateTime abertura,
    LocalDateTime fechamento,
    String disciplinaFoco,
    Integer duracaoMinutos,
    Integer criterioMedalhasMin,
    Integer criterioDesempenhoMin,
    Integer criterioEvolucaoMin,
    Integer criterioPrecisaoMin,
    Integer criterioVelocidadeMin,
    Integer aberturaDiaSemana,
    Integer fechamentoDiaSemana,
    String modoResposta,
    Boolean ativa
) {

    public static BolsaDto fromMap(Map<String, ?> map) {
        Objects.requireNonNull(map, "map");

        return new BolsaDto(
            DtoMapperSupport.parseUuid(map.get("id")),
            DtoMapperSupport.parseText(map.get("nome")),
            DtoMapperSupport.parseText(map.get("tipo")),
            DtoMapperSupport.valueOrDefault(
                DtoMapperSupport.parseInteger(map.get("matches")),
                DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseInteger(map.get("match")), 0)
            ),
            DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseInteger(map.get("vagas")), 0),
            DtoMapperSupport.parseText(map.get("cobertura")),
            DtoMapperSupport.parseText(
                map.containsKey("descricao") ? map.get("descricao") : map.get("destaque")
            ),
            DtoMapperSupport.parseText(map.get("risco")),
            DtoMapperSupport.parseDateTime(map.get("abertura")),
            DtoMapperSupport.parseDateTime(map.get("fechamento")),
            DtoMapperSupport.parseText(map.get("disciplina_foco")),
            DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseInteger(map.get("duracao_minutos")), 45),
            DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseInteger(map.get("criterio_medalhas_min")), 1),
            DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseInteger(map.get("criterio_desempenho_min")), 60),
            DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseInteger(map.get("criterio_evolucao_min")), 55),
            DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseInteger(map.get("criterio_precisao_min")), 60),
            DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseInteger(map.get("criterio_velocidade_min")), 50),
            DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseInteger(map.get("abertura_dia_semana")), 1),
            DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseInteger(map.get("fechamento_dia_semana")), 2),
            DtoMapperSupport.parseText(map.get("modo_resposta")),
            parseBoolean(map.get("ativa"), true)
        );
    }

    public Map<String, ?> toMap() {
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("id", id());
        values.put("nome", nome());
        values.put("tipo", tipo());
        values.put("matches", match());
        values.put("vagas", vagas());
        values.put("cobertura", cobertura());
        values.put("descricao", destaque());
        values.put("risco", risco());
        values.put("abertura", abertura());
        values.put("fechamento", fechamento());
        values.put("disciplina_foco", disciplinaFoco());
        values.put("duracao_minutos", duracaoMinutos());
        values.put("criterio_medalhas_min", criterioMedalhasMin());
        values.put("criterio_desempenho_min", criterioDesempenhoMin());
        values.put("criterio_evolucao_min", criterioEvolucaoMin());
        values.put("criterio_precisao_min", criterioPrecisaoMin());
        values.put("criterio_velocidade_min", criterioVelocidadeMin());
        values.put("abertura_dia_semana", aberturaDiaSemana());
        values.put("fechamento_dia_semana", fechamentoDiaSemana());
        values.put("modo_resposta", modoResposta());
        values.put("ativa", ativa());
        return values;
    }

    private static Boolean parseBoolean(Object value, boolean fallback) {
        if (value instanceof Boolean bool) {
            return bool;
        }
        if (value == null) {
            return fallback;
        }
        String text = value.toString().trim();
        if (text.isBlank()) {
            return fallback;
        }
        return Boolean.parseBoolean(text);
    }
}
