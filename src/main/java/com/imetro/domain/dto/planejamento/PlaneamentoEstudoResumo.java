package com.imetro.domain.dto.planejamento;

import java.util.List;

public record PlaneamentoEstudoResumo(
    double pontuacaoHero,
    String resumoHero,
    String acertoMedio,
    String ritmoMedio,
    String consistenciaMedia,
    String focoAtual,
    String focoAtual2,
    List<PlaneamentoEstudoInsight> insights,
    List<PlaneamentoEstudoEtapa> etapas,
    List<PlaneamentoEstudoRegistro> registros,
    List<PlaneamentoEstudoDisciplina> disciplinas,
    List<PlaneamentoEstudoPonto> evolucao
) {
}
