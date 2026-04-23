package com.imetro.domain.enums;

public enum NivelDisciplina {
    INICIANTE("Iniciante"),
    INTERMEDIARIO("Intermediário"), 
    AVANCADO("Avançado");
    
    private final String descricao;
    
    NivelDisciplina(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
   public static NivelDisciplina fromDescricao(String descricao) {
        if (descricao == null) return INICIANTE;
        
        String normalized = descricao.trim()
            .toLowerCase();
        
        for (NivelDisciplina nivel : values()) {
            String nivelNormalized = nivel.descricao
                .toLowerCase();
            
            if (nivelNormalized.equalsIgnoreCase(normalized)) {
                return nivel;
            }
        }
        
        // Tenta pelo nome do enum
        try {
            return NivelDisciplina.valueOf(descricao.toUpperCase());
        } catch (IllegalArgumentException e) {
            return INICIANTE; // padrão
        }
    }
    
    public static NivelDisciplina fromString(String texto) {
        return fromDescricao(texto);
    }
}