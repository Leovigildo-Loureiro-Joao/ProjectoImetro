package com.imetro.domain.dto.gemini;

import java.util.Locale;

import com.imetro.services.GeminiService;

public record GeracaoSimuladoRequest(
    String disciplina,
    String idioma,
    int quantidadeQuestoes,
    String nivel,
    String instrucoesExtras
) {
    public GeracaoSimuladoRequest {
        disciplina = disciplina == null || disciplina.isBlank() ? "GERAL" : disciplina.trim();
        idioma = idioma == null || idioma.isBlank() ? "pt-AO" : idioma.trim();
        quantidadeQuestoes = quantidadeQuestoes <= 0 ? GeminiService.DEFAULT_SIMULADO_QUESTOES : quantidadeQuestoes;
        nivel = nivel == null || nivel.isBlank() ? "MISTO" : nivel.trim().toUpperCase(Locale.ROOT);
        instrucoesExtras = instrucoesExtras == null ? "" : instrucoesExtras.trim();
    }

    public static GeracaoSimuladoRequest padrao() {
        return new GeracaoSimuladoRequest("GERAL", "pt-AO", GeminiService.DEFAULT_SIMULADO_QUESTOES, "MISTO", "");
    }
}
