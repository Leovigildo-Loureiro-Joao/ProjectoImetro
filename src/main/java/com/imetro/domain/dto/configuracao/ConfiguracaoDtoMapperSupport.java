package com.imetro.domain.dto.configuracao;

import java.util.UUID;

import com.imetro.util.ParseTimeStampLocalDate;

final class ConfiguracaoDtoMapperSupport {

    private ConfiguracaoDtoMapperSupport() {
    }

    static String parseText(Object value) {
        if (value == null) {
            return null;
        }
        String text = value.toString();
        return text.isBlank() ? null : text;
    }

    static UUID parseUuid(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof UUID uuid) {
            return uuid;
        }

        String text = parseText(value);
        if (text == null) {
            return null;
        }

        try {
            return UUID.fromString(text);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    static Integer parseInteger(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }

        String text = parseText(value);
        if (text == null) {
            return null;
        }

        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    static Double parseDouble(Object value) {
        if (value instanceof Number number) {
            return number.doubleValue();
        }

        String text = parseText(value);
        if (text == null) {
            return null;
        }

        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    static Boolean parseBoolean(Object value) {
        if (value instanceof Boolean bool) {
            return bool;
        }
        if (value instanceof Number number) {
            return number.intValue() != 0;
        }

        String text = parseText(value);
        if (text == null) {
            return null;
        }

        return Boolean.parseBoolean(text);
    }

    static java.time.LocalDateTime parseDateTime(Object value) {
        return ParseTimeStampLocalDate.mapearDataHora(value);
    }
}
