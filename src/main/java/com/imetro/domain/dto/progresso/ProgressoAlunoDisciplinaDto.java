package com.imetro.domain.dto.progresso;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.imetro.domain.enums.NivelDisciplina;

public record ProgressoAlunoDisciplinaDto(
    UUID id,
    UUID alunoId,
    UUID disciplinaId,
    String disciplina,
    float progresso,
    NivelDisciplina nivelAtual,
    NivelDisciplina nivelAnterior,
    LocalDate dataMudancaNivel,
    Double pesoAtual,
    Integer totalQuestoesResolvidas,
    Integer totalAcertos,
    Integer totalErros,
    Double taxaAcertoGeral,
    Integer[] ultimos3DiagnosticosAcertos,
    Integer[] ultimos3DiagnosticosTotal,
    LocalDateTime ultimoEstudo,
    Integer diasSemEstudo,
    Integer streakDiasConsecutivos,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm
) {

    public ProgressoAlunoDisciplinaDto {
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (pesoAtual == null) {
            pesoAtual = 1.0;
        }
        if (totalQuestoesResolvidas == null) {
            totalQuestoesResolvidas = 0;
        }
        if (totalAcertos == null) {
            totalAcertos = 0;
        }
        if (totalErros == null) {
            totalErros = 0;
        }
        if (taxaAcertoGeral == null) {
            taxaAcertoGeral = 0.0;
        }
        if (diasSemEstudo == null) {
            diasSemEstudo = 0;
        }
        if (streakDiasConsecutivos == null) {
            streakDiasConsecutivos = 0;
        }
        if (criadoEm == null) {
            criadoEm = LocalDateTime.now();
        }
        if (atualizadoEm == null) {
            atualizadoEm = LocalDateTime.now();
        }
    }

    // Método para calcular taxa de acerto automaticamente
    public double calcularTaxaAcerto() {
        if (totalQuestoesResolvidas == null || totalQuestoesResolvidas == 0) {
            return 0.0;
        }
        int acertos = totalAcertos != null ? totalAcertos : 0;
        return Math.round((double) acertos / totalQuestoesResolvidas * 100.0) / 100.0;
    }

    public double getPercentualAcerto() {
        return calcularTaxaAcerto() * 100;
    }

    public boolean precisaRevisao() {
        return diasSemEstudo != null && diasSemEstudo > 7;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("aluno_id", this.alunoId);
        map.put("disciplina_id", this.disciplinaId);
        map.put("nivel_atual", this.nivelAtual != null ? this.nivelAtual.name() : null);
        map.put("nivel_anterior", this.nivelAnterior != null ? this.nivelAnterior.name() : null);
        map.put("data_mudanca_nivel", this.dataMudancaNivel);
        map.put("peso_atual", this.pesoAtual);
        map.put("total_questoes_resolvidas", this.totalQuestoesResolvidas);
        map.put("total_acertos", this.totalAcertos);
        map.put("total_erros", this.totalErros);
        map.put("taxa_acerto_geral", this.taxaAcertoGeral);
        map.put("ultimo_estudo", this.ultimoEstudo);
        map.put("dias_sem_estudo", this.diasSemEstudo);
        map.put("streak_dias_consecutivos", this.streakDiasConsecutivos);
        map.put("criado_em", this.criadoEm);
        map.put("atualizado_em", this.atualizadoEm);

        if (this.ultimos3DiagnosticosAcertos != null) {
            map.put("ultimos_3_diagnosticos_acertos", this.ultimos3DiagnosticosAcertos);
        }
        if (this.ultimos3DiagnosticosTotal != null) {
            map.put("ultimos_3_diagnosticos_total", this.ultimos3DiagnosticosTotal);
        }

        return map;
    }

    public static ProgressoAlunoDisciplinaDto fromMap(Map<String, Object> map) {
        return new ProgressoAlunoDisciplinaDto(
            UUID.randomUUID(), // Gerar um novo ID para o progresso
            (UUID) map.get("aluno_id"),
            (UUID) map.get("disciplina_id"),
            (String) map.get("disciplina"),
            map.get("progresso") != null ? ((Number) map.get("progresso")).floatValue() : 0f,
            map.get("nivel_atual") != null ? NivelDisciplina.valueOf((String) map.get("nivel_atual")) : null,
            map.get("nivel_anterior") != null ? NivelDisciplina.valueOf((String) map.get("nivel_anterior")) : null,
            (LocalDate) map.get("data_mudanca_nivel"),
            map.get("peso_atual") != null ? ((Number) map.get("peso_atual")).doubleValue() : 1.0,
            map.get("total_questoes_resolvidas") != null ? ((Number) map.get("total_questoes_resolvidas")).intValue() : 0,
            map.get("total_acertos") != null ? ((Number) map.get("total_acertos")).intValue() : 0,
            map.get("total_erros") != null ? ((Number) map.get("total_erros")).intValue() : 0,
            map.get("taxa_acerto_geral") != null ? ((Number) map.get("taxa_acerto_geral")).doubleValue() : 0.0,
            (Integer[]) map.get("ultimos_3_diagnosticos_acertos"),
            (Integer[]) map.get("ultimos_3_diagnosticos_total"),
            (LocalDateTime) map.get("ultimo_estudo"),
            map.get("dias_sem_estudo") != null ? ((Number) map.get("dias_sem_estudo")).intValue() : 0,
            map.get("streak_dias_consecutivos") != null ? ((Number) map.get("streak_dias_consecutivos")).intValue() : 0,
            (LocalDateTime) map.getOrDefault("criado_em", LocalDateTime.now()),
            (LocalDateTime) map.getOrDefault("atualizado_em", LocalDateTime.now())
        );
    }

}
