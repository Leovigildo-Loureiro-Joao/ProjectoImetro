package com.imetro.domain.dto.perguntas;

public  record GeracaoLoteResultado(
    GeracaoLote lote,
    String jsonQuestoes,
    String erro
) {
   public boolean sucesso() {
        return jsonQuestoes != null && !jsonQuestoes.isBlank();
    }
}
