package com.imetro.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class QuestaoExercicioSupport {

    private static final Pattern SQRT_PAREN_PATTERN = Pattern.compile("(?i)(?<!\\\\)sqrt\\s*\\(\\s*([^\\)]+?)\\s*\\)");
    private static final Pattern SQRT_BRACE_PATTERN = Pattern.compile("(?i)(?<!\\\\)sqrt\\s*\\{\\s*([^\\}]+?)\\s*\\}");
    private static final Pattern SQRT_TOKEN_PATTERN = Pattern.compile("(?i)(?<!\\\\)sqrt\\s+([A-Za-z0-9\\\\^_+-]+)");

    private QuestaoExercicioSupport() {
    }

    public static String normalizar(String exercicio) {
        if (exercicio == null) {
            return null;
        }

        String texto = exercicio.trim();
        if (texto.isBlank()) {
            return null;
        }

        texto = removerDelimitadores(texto);
        texto = texto.replace('\r', ' ').replace('\n', ' ');
        texto = texto.replace("\u00D7", "\\times");
        texto = texto.replace("\u00B7", "\\cdot");
        texto = texto.replace("\u00F7", "\\div");
        texto = texto.replace("\u2212", "-");
        texto = texto.replace("\u2013", "-");
        texto = texto.replace("\u2264", "\\leq");
        texto = texto.replace("\u2265", "\\geq");
        texto = texto.replace("\u03C0", "\\pi");
        texto = texto.replace("\u221E", "\\infty");
        texto = texto.replace("\u00B0", "^\\circ");
        texto = normalizarRaizes(texto);
        texto = texto.replaceAll("\\s+", " ").trim();

        return texto.isBlank() ? null : texto;
    }

    private static String removerDelimitadores(String texto) {
        String atual = texto;
        if ((atual.startsWith("$$") && atual.endsWith("$$")) || (atual.startsWith("$") && atual.endsWith("$"))) {
            atual = atual.substring(atual.startsWith("$$") ? 2 : 1, atual.length() - (atual.startsWith("$$") ? 2 : 1));
        } else if (atual.startsWith("\\(") && atual.endsWith("\\)")) {
            atual = atual.substring(2, atual.length() - 2);
        } else if (atual.startsWith("\\[") && atual.endsWith("\\]")) {
            atual = atual.substring(2, atual.length() - 2);
        }
        return atual.trim();
    }

    private static String normalizarRaizes(String texto) {
        String atual = texto;
        atual = substituirPattern(atual, SQRT_PAREN_PATTERN, "\\\\sqrt{$1}");
        atual = substituirPattern(atual, SQRT_BRACE_PATTERN, "\\\\sqrt{$1}");
        atual = substituirPattern(atual, SQRT_TOKEN_PATTERN, "\\\\sqrt{$1}");
        return atual;
    }

    private static String substituirPattern(String texto, Pattern pattern, String replacement) {
        Matcher matcher = pattern.matcher(texto);
        return matcher.replaceAll(replacement);
    }
}
