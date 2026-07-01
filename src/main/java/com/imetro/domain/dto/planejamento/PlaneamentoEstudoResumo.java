package com.imetro.domain.dto.planejamento;

import java.rmi.server.UID;
import java.util.List;
import java.util.UUID;

public record PlaneamentoEstudoResumo(
    double pontuacaoHero,
    String resumoHero,
    String acertoMedio,
    String ritmoMedio,
    String consistenciaMedia,
    String focoPrincipal,
    String focoSecundario,
    String focoAtual,
    String focoAtual2,
    UUID mapaLivroEstudo1,
    UUID mapaLivroEstudo2,
    List<PlaneamentoEstudoInsight> insights,
    List<PlaneamentoEstudoEtapa> etapas,
    List<PlaneamentoEstudoRegistro> registros,
    List<PlaneamentoEstudoDisciplina> disciplinas,
    List<PlaneamentoEstudoPonto> evolucao
) {
}
