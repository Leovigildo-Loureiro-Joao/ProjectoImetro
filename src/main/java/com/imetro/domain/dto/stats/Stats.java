package com.imetro.domain.dto.stats;

import java.time.LocalDateTime;
import java.util.UUID;

public record  Stats(double velocidade, double precisao, double consistencia, double logica, double resiliencia) {

}
