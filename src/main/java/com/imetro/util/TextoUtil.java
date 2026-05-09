package com.imetro.util;

import java.text.Normalizer;
import java.util.Locale;

public final class TextoUtil {

    private TextoUtil() {
    }

    public static String safeText(Object value, String defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        String text = value.toString().trim();
        return text.isEmpty() ? defaultValue : text;
    }

    public static String removerAcentos(String valor) {
        return Normalizer.normalize(safeText(valor, ""), Normalizer.Form.NFD)
            .replaceAll("\\p{M}+", "");
    }

    public static String normalizarMinusculo(String valor) {
        return removerAcentos(valor).trim().toLowerCase(Locale.ROOT);
    }

    public static String normalizarMaiusculo(String valor) {
        return removerAcentos(valor).trim().toUpperCase(Locale.ROOT);
    }
}
