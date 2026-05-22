package com.imetro.domain.dto.configuracao;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public record ConfiguracaoTesteAdaptativoDto(
    UUID id,
    String codigo,
    String descricao,
    boolean ativo,
    double tempoLentoFator,
    double tempoRecuperacaoFator,
    int acertosSubirRapido,
    int acertosSubirLento,
    int errosDescer,
    int janelaConsistencia,
    int janelaRecuperacao,
    double pesoConsistenciaAcerto,
    double pesoConsistenciaRitmo,
    double pesoResilienciaRecuperacao,
    double pesoResilienciaEstabilidade,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm,
    List<ConfiguracaoTesteAdaptativoNivelDto> niveis,
    List<ConfiguracaoTesteAdaptativoDuracaoDto> duracoes
) {

    public ConfiguracaoTesteAdaptativoDto {
        niveis = niveis == null ? List.of() : List.copyOf(niveis);
        duracoes = duracoes == null ? List.of() : List.copyOf(duracoes);
    }

    public static ConfiguracaoTesteAdaptativoDto fromMap(Map<String, ?> map) {
        Objects.requireNonNull(map, "map");

        return new ConfiguracaoTesteAdaptativoDto(
            ConfiguracaoDtoMapperSupport.parseUuid(map.get("id")),
            ConfiguracaoDtoMapperSupport.parseText(map.get("codigo")),
            ConfiguracaoDtoMapperSupport.parseText(map.get("descricao")),
            valueOrDefault(ConfiguracaoDtoMapperSupport.parseBoolean(map.get("ativo")), false),
            valueOrDefault(ConfiguracaoDtoMapperSupport.parseDouble(map.get("tempo_lento_fator")), 1.25d),
            valueOrDefault(ConfiguracaoDtoMapperSupport.parseDouble(map.get("tempo_recuperacao_fator")), 1.10d),
            valueOrDefault(ConfiguracaoDtoMapperSupport.parseInteger(map.get("acertos_subir_rapido")), 2),
            valueOrDefault(ConfiguracaoDtoMapperSupport.parseInteger(map.get("acertos_subir_lento")), 3),
            valueOrDefault(ConfiguracaoDtoMapperSupport.parseInteger(map.get("erros_descer")), 2),
            valueOrDefault(ConfiguracaoDtoMapperSupport.parseInteger(map.get("janela_consistencia")), 3),
            valueOrDefault(ConfiguracaoDtoMapperSupport.parseInteger(map.get("janela_recuperacao")), 2),
            valueOrDefault(ConfiguracaoDtoMapperSupport.parseDouble(map.get("peso_consistencia_acerto")), 0.70d),
            valueOrDefault(ConfiguracaoDtoMapperSupport.parseDouble(map.get("peso_consistencia_ritmo")), 0.30d),
            valueOrDefault(ConfiguracaoDtoMapperSupport.parseDouble(map.get("peso_resiliencia_recuperacao")), 0.70d),
            valueOrDefault(ConfiguracaoDtoMapperSupport.parseDouble(map.get("peso_resiliencia_estabilidade")), 0.30d),
            ConfiguracaoDtoMapperSupport.parseDateTime(map.get("criado_em")),
            ConfiguracaoDtoMapperSupport.parseDateTime(map.get("atualizado_em")),
            List.of(),
            List.of()
        );
    }

    public ConfiguracaoTesteAdaptativoDto withRelacionamentos(
        List<ConfiguracaoTesteAdaptativoNivelDto> novosNiveis,
        List<ConfiguracaoTesteAdaptativoDuracaoDto> novasDuracoes
    ) {
        return new ConfiguracaoTesteAdaptativoDto(
            id,
            codigo,
            descricao,
            ativo,
            tempoLentoFator,
            tempoRecuperacaoFator,
            acertosSubirRapido,
            acertosSubirLento,
            errosDescer,
            janelaConsistencia,
            janelaRecuperacao,
            pesoConsistenciaAcerto,
            pesoConsistenciaRitmo,
            pesoResilienciaRecuperacao,
            pesoResilienciaEstabilidade,
            criadoEm,
            atualizadoEm,
            novosNiveis,
            novasDuracoes
        );
    }

    public ConfiguracaoTesteAdaptativoNivelDto encontrarNivel(int valorNivel) {
        return niveis.stream()
            .filter(item -> item.nivel() == valorNivel)
            .findFirst()
            .orElse(null);
    }

    public ConfiguracaoTesteAdaptativoDuracaoDto encontrarDuracao(String valorCodigo) {
        if (valorCodigo == null || valorCodigo.isBlank()) {
            return null;
        }

        return duracoes.stream()
            .filter(item -> item.codigo() != null && item.codigo().equalsIgnoreCase(valorCodigo))
            .findFirst()
            .orElse(null);
    }

    public Map<String, ?> toMap() {
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("id", id());
        values.put("codigo", codigo());
        values.put("descricao", descricao());
        values.put("ativo", ativo());
        values.put("tempo_lento_fator", tempoLentoFator());
        values.put("tempo_recuperacao_fator", tempoRecuperacaoFator());
        values.put("acertos_subir_rapido", acertosSubirRapido());
        values.put("acertos_subir_lento", acertosSubirLento());
        values.put("erros_descer", errosDescer());
        values.put("janela_consistencia", janelaConsistencia());
        values.put("janela_recuperacao", janelaRecuperacao());
        values.put("peso_consistencia_acerto", pesoConsistenciaAcerto());
        values.put("peso_consistencia_ritmo", pesoConsistenciaRitmo());
        values.put("peso_resiliencia_recuperacao", pesoResilienciaRecuperacao());
        values.put("peso_resiliencia_estabilidade", pesoResilienciaEstabilidade());
        values.put("criado_em", criadoEm());
        values.put("atualizado_em", atualizadoEm());
        return values;
    }

    public static boolean valueOrDefault(Boolean value, boolean fallback) {
        return value == null ? fallback : value;
    }

    public static int valueOrDefault(Integer value, int fallback) {
        return value == null ? fallback : value;
    }

    public static double valueOrDefault(Double value, double fallback) {
        return value == null ? fallback : value;
    }
}
