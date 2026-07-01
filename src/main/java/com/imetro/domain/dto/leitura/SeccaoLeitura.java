package com.imetro.domain.dto.leitura;

import java.time.LocalDateTime;
import java.util.UUID;

public record SeccaoLeitura( 
        int ordem ,
        UUID livroId,
        String tituloLivro,
        int paginasInicio ,
        int paginasFim ,
        String topico ,
        String subtopico,
        String estado,//"PENDENTE" | "A_LER" | "LIDO"
        LocalDateTime dataConclusao
    ) {
    
}
