package com.imetro.domain.enums;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import com.imetro.util.TextoUtil;

public enum NivelDificuldadeAdaptativa {
    FACIL(
        1,
        "FACIL",
        "Facil",
        "*",
        "#10b981",
        0.18d,
        40d,
        "fácil",
        "easy",
        "basico",
        "básico",
        "leve"
    ),
    MEDIO(
        2,
        "MEDIO",
        "Medio",
        "**",
        "#f59e0b",
        0.35d,
        55d,
        "médio",
        "normal",
        "padrao",
        "padrão",
        "medium",
        "intermediario",
        "intermediário"
    ),
    DIFICIL(
        3,
        "DIFICIL",
        "Dificil",
        "***",
        "#ef4444",
        0.58d,
        70d,
        "difícil",
        "desafiante",
        "hard",
        "avancado",
        "avançado"
    ),
    EXPERT(
        4,
        "EXPERT",
        "Expert",
        "****",
        "#8b5cf6",
        0.78d,
        85d,
        "extra",
        "extra dificil",
        "extra difícil",
        "muito dificil",
        "muito difícil"
    );

    private static final NivelDificuldadeAdaptativa PADRAO = MEDIO;

    private final int nivel;
    private final String codigo;
    private final String rotulo;
    private final String estrelas;
    private final String corHex;
    private final double rigorBase;
    private final double tempoSugeridoSegundos;
    private final Set<String> aliases;

    NivelDificuldadeAdaptativa(
        int nivel,
        String codigo,
        String rotulo,
        String estrelas,
        String corHex,
        double rigorBase,
        double tempoSugeridoSegundos,
        String... aliases
    ) {
        this.nivel = nivel;
        this.codigo = codigo;
        this.rotulo = rotulo;
        this.estrelas = estrelas;
        this.corHex = corHex;
        this.rigorBase = rigorBase;
        this.tempoSugeridoSegundos = tempoSugeridoSegundos;
        this.aliases = normalizarAliases(codigo, rotulo, aliases);
    }

    public int nivel() {
        return nivel;
    }

    public String codigo() {
        return codigo;
    }

    public String rotulo() {
        return rotulo;
    }

    public String estrelas() {
        return estrelas;
    }

    public String corHex() {
        return corHex;
    }

    public double rigorBase() {
        return rigorBase;
    }

    public double tempoSugeridoSegundos() {
        return tempoSugeridoSegundos;
    }

    public NivelDificuldadeAdaptativa subir() {
        return fromNivel(nivel + 1);
    }

    public NivelDificuldadeAdaptativa descer() {
        return fromNivel(nivel - 1);
    }

    public boolean incluiQuestaoNoFiltroDiagnostico(int nivelQuestao) {
        return switch (this) {
            case FACIL -> nivelQuestao <= FACIL.nivel;
            case MEDIO -> true;
            case DIFICIL -> nivelQuestao >= MEDIO.nivel;
            case EXPERT -> nivelQuestao >= DIFICIL.nivel;
        };
    }

    public boolean correspondeAoTexto(String valor) {
        return aliases.contains(normalizar(valor));
    }

    public static NivelDificuldadeAdaptativa padrao() {
        return PADRAO;
    }

    public static NivelDificuldadeAdaptativa fromNivel(Integer nivel) {
        if (nivel == null) {
            return PADRAO;
        }

        for (NivelDificuldadeAdaptativa value : values()) {
            if (value.nivel == nivel) {
                return value;
            }
        }

        if (nivel < FACIL.nivel) {
            return FACIL;
        }
        if (nivel > EXPERT.nivel) {
            return EXPERT;
        }
        return PADRAO;
    }

    public static NivelDificuldadeAdaptativa fromTexto(String valor) {
        if (valor == null || valor.isBlank()) {
            return PADRAO;
        }

        for (NivelDificuldadeAdaptativa value : values()) {
            if (value.correspondeAoTexto(valor)) {
                return value;
            }
        }

        return PADRAO;
    }

    private static Set<String> normalizarAliases(String codigo, String rotulo, String[] aliases) {
        LinkedHashSet<String> valores = new LinkedHashSet<>();
        valores.add(normalizar(codigo));
        valores.add(normalizar(rotulo));
        Arrays.stream(aliases)
            .map(NivelDificuldadeAdaptativa::normalizar)
            .forEach(valores::add);
        return Set.copyOf(valores);
    }

    private static String normalizar(String valor) {
        return TextoUtil.normalizarMinusculo(valor);
    }
}
