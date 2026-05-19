package com.imetro.domain.dto.test;

import java.util.UUID;

public record ErrosComuns(
    UUID questaoId,
    String enuciado,
    String marcada,
    String topico,
    String subtopico,
    String resposta,
    double percentualDificuldade
) {

}
