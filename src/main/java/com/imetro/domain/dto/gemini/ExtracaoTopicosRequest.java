package com.imetro.domain.dto.gemini;

public record ExtracaoTopicosRequest(
    String disciplina,
    String idioma,
    String instrucoesExtras
) {
    public ExtracaoTopicosRequest {
        disciplina = disciplina == null || disciplina.isBlank() ? "GERAL" : disciplina.trim();
        idioma = idioma == null || idioma.isBlank() ? "pt-AO" : idioma.trim();
        instrucoesExtras = instrucoesExtras == null ? "" : instrucoesExtras.trim();
    }

    public static ExtracaoTopicosRequest padrao() {
        return new ExtracaoTopicosRequest("GERAL", "pt-AO", "");
    }
}
