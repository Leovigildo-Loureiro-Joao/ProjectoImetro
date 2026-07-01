package com.imetro.domain.dto.configuracao;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.imetro.util.DtoMapperSupport;

public record ConfiguracaoTesteAdaptativoDto(
    UUID id,
    UUID user_id,
    String descricao,
    double tempoLentoFator,
    double tempoRecuperacaoFator,
    int acertosSubirRapido,
    int acertosSubirLento,
    int errosDescer,
    int tempAdapt,
    int janelaConsistencia,
    int janelaRecuperacao,
    double pesoConsistenciaAcerto,
    double pesoConsistenciaRitmo,
    double pesoResilienciaRecuperacao,
    double pesoResilienciaEstabilidade,
    double resilienciaQuestaoBase,
    double resilienciaQuestaoBonusAcerto,
    double resilienciaQuestaoBonusRitmo,
    double resilienciaQuestaoBonusRecuperacao,
    double scoreNeutro,
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
            DtoMapperSupport.parseUuid(map.get("id")),
            DtoMapperSupport.parseUuid(map.get("user_id")),
            DtoMapperSupport.parseText(map.get("descricao")),
            DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseDouble(map.get("tempo_lento_fator")), 1.25d),
            DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseDouble(map.get("tempo_recuperacao_fator")), 1.10d),
            DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseInteger(map.get("acertos_subir_rapido")), 2),
            DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseInteger(map.get("acertos_subir_lento")), 3),
            DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseInteger(map.get("erros_descer")), 2),
            DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseInteger(map.get("tempo_adapt")), 30),
            DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseInteger(map.get("janela_consistencia")), 3),
            DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseInteger(map.get("janela_recuperacao")), 2),
            DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseDouble(map.get("peso_consistencia_acerto")), 0.70d),
            DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseDouble(map.get("peso_consistencia_ritmo")), 0.30d),
            DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseDouble(map.get("peso_resiliencia_recuperacao")), 0.70d),
            DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseDouble(map.get("peso_resiliencia_estabilidade")), 0.30d),
            DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseDouble(map.get("resiliencia_questao_base")), 0.10d),
            DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseDouble(map.get("resiliencia_questao_bonus_acerto")), 0.65d),
            DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseDouble(map.get("resiliencia_questao_bonus_ritmo")), 0.15d),
            DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseDouble(map.get("resiliencia_questao_bonus_recuperacao")), 0.10d),
            DtoMapperSupport.valueOrDefault(DtoMapperSupport.parseDouble(map.get("score_neutro")), 0.50d),
            DtoMapperSupport.parseDateTime(map.get("criado_em")),
            DtoMapperSupport.parseDateTime(map.get("atualizado_em")),
            List.of(),
            List.of()
        );
    }

    public static ConfiguracaoTesteAdaptativoDto padrao(UUID userId) {
        return new ConfiguracaoTesteAdaptativoDto(
            null,
            userId,
            "Perfil adaptativo padrao do candidato.",
            1.25d,
            1.10d,
            2,
            3,
            2,
            30,
            3,
            2,
            0.70d,
            0.30d,
            0.70d,
            0.30d,
            0.10d,
            0.65d,
            0.15d,
            0.10d,
            0.50d,
            null,
            null,
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
            user_id,
            descricao,
            tempoLentoFator,
            tempoRecuperacaoFator,
            acertosSubirRapido,
            acertosSubirLento,
            errosDescer,
            tempAdapt,
            janelaConsistencia,
            janelaRecuperacao,
            pesoConsistenciaAcerto,
            pesoConsistenciaRitmo,
            pesoResilienciaRecuperacao,
            pesoResilienciaEstabilidade,
            resilienciaQuestaoBase,
            resilienciaQuestaoBonusAcerto,
            resilienciaQuestaoBonusRitmo,
            resilienciaQuestaoBonusRecuperacao,
            scoreNeutro,
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
        values.put("user_id", user_id());
        values.put("descricao", descricao());
        values.put("tempo_lento_fator", tempoLentoFator());
        values.put("tempo_recuperacao_fator", tempoRecuperacaoFator());
        values.put("acertos_subir_rapido", acertosSubirRapido());
        values.put("acertos_subir_lento", acertosSubirLento());
        values.put("erros_descer", errosDescer());
        values.put("tempo_adapt", tempAdapt());
        values.put("janela_consistencia", janelaConsistencia());
        values.put("janela_recuperacao", janelaRecuperacao());
        values.put("peso_consistencia_acerto", pesoConsistenciaAcerto());
        values.put("peso_consistencia_ritmo", pesoConsistenciaRitmo());
        values.put("peso_resiliencia_recuperacao", pesoResilienciaRecuperacao());
        values.put("peso_resiliencia_estabilidade", pesoResilienciaEstabilidade());
        values.put("resiliencia_questao_base", resilienciaQuestaoBase());
        values.put("resiliencia_questao_bonus_acerto", resilienciaQuestaoBonusAcerto());
        values.put("resiliencia_questao_bonus_ritmo", resilienciaQuestaoBonusRitmo());
        values.put("resiliencia_questao_bonus_recuperacao", resilienciaQuestaoBonusRecuperacao());
        values.put("score_neutro", scoreNeutro());
        values.put("criado_em", criadoEm());
        values.put("atualizado_em", atualizadoEm());
        return values;
    }


}
