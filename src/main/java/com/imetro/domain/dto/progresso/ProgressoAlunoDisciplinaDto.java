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
    NivelDisciplina nivelAtual,
    NivelDisciplina nivelAnterior,
    LocalDate dataMudancaNivel,
    Double pesoAtual,
    Integer totalQuestoesResolvidas,
    Integer totalAcertos,
    Integer totalErros,
    Double taxaAcertoGeral,
    Integer ultimos3DiagnosticosAcertos,
    Integer ultimos3DiagnosticosTotal,
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
        if (ultimos3DiagnosticosAcertos == null) {
            ultimos3DiagnosticosAcertos = 0;
        }
        if (ultimos3DiagnosticosTotal == null) {
            ultimos3DiagnosticosTotal = 0;
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
        map.put("ultimos_3_diagnosticos_acertos", this.ultimos3DiagnosticosAcertos);
        map.put("ultimos_3_diagnosticos_total", this.ultimos3DiagnosticosTotal);
        map.put("ultimo_estudo", this.ultimoEstudo);
        map.put("dias_sem_estudo", this.diasSemEstudo);
        map.put("streak_dias_consecutivos", this.streakDiasConsecutivos);
        map.put("criado_em", this.criadoEm);
        map.put("atualizado_em", this.atualizadoEm);
        
        return map;
    }

    
} 
