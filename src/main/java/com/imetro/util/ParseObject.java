package com.imetro.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.imetro.domain.dto.test.Melhorias;

public class ParseObject {

    public static List<Map<String, String>> parseJsonObjectArray(String json) {
        if (json == null || json.isBlank()) {
            return List.of();
        }

        ArrayList<Map<String, String>> itens = new ArrayList<>();
        int cursor = skipWhitespace(json, 0);
        if (cursor >= json.length() || json.charAt(cursor) != '[') {
            return List.of();
        }

        cursor++;
        while (cursor < json.length()) {
            cursor = skipWhitespace(json, cursor);
            if (cursor >= json.length() || json.charAt(cursor) == ']') {
                break;
            }
            if (json.charAt(cursor) == ',') {
                cursor++;
                continue;
            }
            if (json.charAt(cursor) != '{') {
                cursor++;
                continue;
            }

            ParsedJsonObject parsed = parseJsonObject(json, cursor);
            itens.add(parsed.values());
            cursor = parsed.nextIndex();
        }

        return itens;
    }

    public static ParsedJsonObject parseJsonObject(String json, int startIndex) {
        LinkedHashMap<String, String> valores = new LinkedHashMap<>();
        int cursor = startIndex + 1;

        while (cursor < json.length()) {
            cursor = skipWhitespace(json, cursor);
            if (cursor >= json.length()) {
                break;
            }

            char atual = json.charAt(cursor);
            if (atual == '}') {
                return new ParsedJsonObject(valores, cursor + 1);
            }
            if (atual == ',') {
                cursor++;
                continue;
            }
            if (atual != '"') {
                cursor++;
                continue;
            }

            ParsedJsonToken chave = parseJsonStringToken(json, cursor);
            cursor = skipWhitespace(json, chave.nextIndex());
            if (cursor < json.length() && json.charAt(cursor) == ':') {
                cursor++;
            }
            cursor = skipWhitespace(json, cursor);

            ParsedJsonToken valor = cursor < json.length() && json.charAt(cursor) == '"'
                ? parseJsonStringToken(json, cursor)
                : parseJsonLiteralToken(json, cursor);

            valores.put(chave.value(), valor.value());
            cursor = skipWhitespace(json, valor.nextIndex());
            if (cursor < json.length() && json.charAt(cursor) == ',') {
                cursor++;
            }
        }

        return new ParsedJsonObject(valores, cursor);
    }

    public static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }

     public static List<Melhorias> parseMelhoriasJson(String json) {
        ArrayList<Melhorias> itens = new ArrayList<>();
        for (Map<String, String> valores : parseJsonObjectArray(json)) {
            itens.add(
                new Melhorias(
                    parseUuid(valores.get("questaoId")),
                    firstNonBlank(valores.get("enuciado"), valores.get("enunciado")),
                    valores.get("correta"),
                    firstNonBlank(valores.get("resposta"), valores.get("marcada")),
                    parseInteger(valores.get("tempoSegundos")),
                    valores.get("topico"),
                    valores.get("subtopico"),
                    parseInteger(valores.get("qtdAcerto")),
                    parseInteger(valores.get("qtdErros")),
                    parseDouble(valores.get("precisaoAnteriorPercentual")),
                    parseDouble(valores.get("precisaoAtualPercentual")),
                    parseDouble(valores.get("melhoriaPercentual"))
                )
            );
        }
        return itens;
    }

    public static UUID parseUuid(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof UUID uuid) {
            return uuid;
        }

        String text = value.toString().trim();
        if (text.isBlank()) {
            return null;
        }

        try {
            return UUID.fromString(text);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    public static Boolean parseBoolean(Object value) {
        if (value instanceof Boolean bool) {
            return bool;
        }
        if (value == null) {
            return null;
        }

        String text = value.toString().trim();
        if (text.isBlank()) {
            return null;
        }

        return Boolean.parseBoolean(text);
    }

    public static int parseInteger(String value) {
        if (value == null || value.isBlank()) {
            return 0;
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }


    public static ParsedJsonToken parseJsonStringToken(String json, int startIndex) {
        StringBuilder out = new StringBuilder();
        int cursor = startIndex + 1;

        while (cursor < json.length()) {
            char atual = json.charAt(cursor);
            if (atual == '"') {
                return new ParsedJsonToken(out.toString(), cursor + 1);
            }
            if (atual == '\\' && cursor + 1 < json.length()) {
                char proximo = json.charAt(++cursor);
                switch (proximo) {
                    case '"' -> out.append('"');
                    case '\\' -> out.append('\\');
                    case '/' -> out.append('/');
                    case 'b' -> out.append('\b');
                    case 'f' -> out.append('\f');
                    case 'n' -> out.append('\n');
                    case 'r' -> out.append('\r');
                    case 't' -> out.append('\t');
                    case 'u' -> {
                        if (cursor + 4 < json.length()) {
                            String hex = json.substring(cursor + 1, cursor + 5);
                            out.append((char) Integer.parseInt(hex, 16));
                            cursor += 4;
                        }
                    }
                    default -> out.append(proximo);
                }
            } else {
                out.append(atual);
            }
            cursor++;
        }

        return new ParsedJsonToken(out.toString(), json.length());
    }

    public static ParsedJsonToken parseJsonLiteralToken(String json, int startIndex) {
        int cursor = startIndex;
        while (cursor < json.length()) {
            char atual = json.charAt(cursor);
            if (atual == ',' || atual == '}') {
                break;
            }
            cursor++;
        }

        String literal = json.substring(startIndex, cursor).trim();
        if (literal.isBlank() || "null".equalsIgnoreCase(literal)) {
            return new ParsedJsonToken(null, cursor);
        }
        return new ParsedJsonToken(literal, cursor);
    }


    public static double parseDouble(Object value) {
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        if (value == null) {
            return 0d;
        }

        String text = value.toString().trim();
        if (text.isBlank()) {
            return 0d;
        }

        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException ignored) {
            return 0d;
        }
    }

    public static int skipWhitespace(String value, int startIndex) {
        int cursor = startIndex;
        while (cursor < value.length() && Character.isWhitespace(value.charAt(cursor))) {
            cursor++;
        }
        return cursor;
    }


    public record ParsedJsonObject(Map<String, String> values, int nextIndex) {
    }

    public record ParsedJsonToken(String value, int nextIndex) {
    }

}
