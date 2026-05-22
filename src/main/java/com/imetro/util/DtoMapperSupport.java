package com.imetro.util;

import java.util.UUID;

public class DtoMapperSupport {
     public static String parseText(Object value) {
        if (value == null) {
            return null;
        }
        String text = value.toString();
        return text.isBlank() ? null : text;
    }

    public static UUID parseUuid(Object value) {
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

    public static Integer parseInteger(Object value) {
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

    public static Double parseDouble(Object value) {
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

    public static Boolean parseBoolean(Object value) {
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

    public static java.time.LocalDateTime parseDateTime(Object value) {
        return ParseTimeStampLocalDate.mapearDataHora(value);
    }

    public static boolean valueOrDefault(Boolean value, boolean fallback) {
        return value == null ? fallback : value;
    }

    public static int valueOrDefault(Integer value, int fallback) {
        return value == null ? fallback : value;
    }

    public static double valueOrDefault(Double value, double fallback) {
        return value == null ? fallback : value;
    }
}
