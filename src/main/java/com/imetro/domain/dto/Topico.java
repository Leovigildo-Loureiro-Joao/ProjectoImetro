package com.imetro.domain.dto;

import java.util.UUID;

public record Topico(UUID idDisciplina,String topicos,UUID recomendacoes,String[] subTopicos) {
    
}
