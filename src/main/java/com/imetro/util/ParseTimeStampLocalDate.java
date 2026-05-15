package com.imetro.util;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public  class ParseTimeStampLocalDate {
    public  static LocalDateTime mapearDataHora(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof LocalDateTime dateTime) {
            return dateTime;
        }
        if (value instanceof OffsetDateTime offsetDateTime) {
            return offsetDateTime.toLocalDateTime();
        }
        if (value instanceof Timestamp timestamp) {
            return timestamp.toLocalDateTime();
        }

        String text = asText(value);
        if (text == null) {
            return null;
        }

        try {
            return LocalDateTime.parse(text);
        } catch (RuntimeException ignored) {
        }

        try {
            return OffsetDateTime.parse(text).toLocalDateTime();
        } catch (RuntimeException ignored) {
        }

        return null;
    }


    public  static LocalDate mapearData(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof LocalDate date) {
            return date;
        }
        if (value instanceof OffsetDateTime offsetDateTime) {
            return offsetDateTime.toLocalDateTime().toLocalDate();
        }
        if (value instanceof Timestamp timestamp) {
            return timestamp.toLocalDateTime().toLocalDate();
        }

        String text = asText(value);
        if (text == null) {
            return null;
        }

        try {
            return LocalDate.parse(text);
        } catch (RuntimeException ignored) {
        }

        try {
            return OffsetDateTime.parse(text).toLocalDateTime().toLocalDate();
        } catch (RuntimeException ignored) {
        }

        return null;
    }

    private static String asText(Object value) {
        if (value == null) {
            return null;
        }
        String text = value.toString();
        return text.isBlank() ? null : text;
    }
}
