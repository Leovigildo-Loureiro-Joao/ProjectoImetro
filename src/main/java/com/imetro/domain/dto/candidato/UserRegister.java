package com.imetro.domain.dto.candidato;

import java.time.LocalDateTime;
import java.util.Map;


public record UserRegister(String nome, String email, String senha_hash, String role, LocalDateTime criado_em) {
    public boolean ValidateData(){

      return nome != null
              && !nome.isBlank()
              && email != null
              && !email.isBlank()
              && email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
              && senha_hash != null
              && !senha_hash.isBlank()
              && role != null
              && ("CANDIDATO".equalsIgnoreCase(role) || "ORIENTADOR".equalsIgnoreCase(role));
    }
    public Map<String,?> toMap(){
        return Map.of(
                "nome", nome,
                "email", email,
                "senha_hash", senha_hash,
                "role", role.toUpperCase(),
                "criado_em", criado_em
        );
    }
}
