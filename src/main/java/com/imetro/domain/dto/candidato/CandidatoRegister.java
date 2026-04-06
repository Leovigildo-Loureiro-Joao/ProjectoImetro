package com.imetro.domain.dto.candidato;

import java.time.LocalDateTime;
import java.util.Map;


public record CandidatoRegister(String nome,String email,String senha_hash,LocalDateTime criado_em) {
    public Map<String,?> toMap(){
        return Map.of("nome",nome,"email",email,"senha_hash",senha_hash,"criado_em",criado_em);
    }
}
