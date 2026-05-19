package com.imetro.domain.dto.test;

import java.util.UUID;

public record Melhorias(
    UUID questaoId,
    String enuciado,
    String correta,
    String resposta,
    int tempoSegundos,
    String topico,
    String subtopico,
    int qtdAcerto,
    int qtdErros,
    double precisaoAnteriorPercentual,
    double precisaoAtualPercentual,
    double melhoriaPercentual
) {

    

}
