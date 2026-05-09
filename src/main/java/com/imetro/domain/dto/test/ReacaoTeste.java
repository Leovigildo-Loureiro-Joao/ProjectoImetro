package com.imetro.domain.dto.test;

import java.time.LocalDateTime;

import com.imetro.ui.model.Questao;

public record ReacaoTeste(
        Questao questao,
        int ordem,
        char respostaDada,
        long tempoSegundos,
        double consistencia,
        double resiliencia,
        LocalDateTime respondidoEm
) {



}
