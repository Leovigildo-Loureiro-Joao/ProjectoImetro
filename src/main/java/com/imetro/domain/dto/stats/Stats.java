package com.imetro.domain.dto.stats;

import java.time.LocalDateTime;
import java.util.UUID;

public record  Stats(float velocidade, float precisao, float consistencia, float logica, float resiliencia) {

}
