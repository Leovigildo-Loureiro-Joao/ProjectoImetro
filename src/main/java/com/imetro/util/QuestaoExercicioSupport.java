package com.imetro.util;

public final class QuestaoExercicioSupport {

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
        StringBuilder resultado = new StringBuilder(texto.length() + 16);
        int indice = 0;

        while (indice < texto.length()) {
            RaizParse raiz = consumirRaiz(texto, indice);
            if (raiz != null) {
                resultado.append(raiz.latex());
                indice = raiz.proximoIndice();
                continue;
            }

            resultado.append(texto.charAt(indice));
            indice++;
        }

        return resultado.toString();
    }

    private static RaizParse consumirRaiz(String texto, int indiceInicial) {
        if (texto == null || indiceInicial < 0 || indiceInicial >= texto.length()) {
            return null;
        }

        int cursor = indiceInicial;
        String indiceRaiz = null;
        char atual = texto.charAt(cursor);

        if (atual == '∛') {
            indiceRaiz = "3";
            cursor++;
        } else if (atual == '∜') {
            indiceRaiz = "4";
            cursor++;
        } else if (atual == '√') {
            cursor++;
        } else if (comecaComSqrt(texto, cursor)) {
            cursor += 4;
        } else {
            return null;
        }

        cursor = pularEspacos(texto, cursor);

        if (cursor < texto.length() && texto.charAt(cursor) == '[') {
            ParseResult indiceExplicito = lerGrupoBalanceado(texto, cursor, '[', ']');
            if (indiceExplicito == null) {
                return null;
            }

            indiceRaiz = indiceExplicito.valor().trim();
            cursor = pularEspacos(texto, indiceExplicito.proximoIndice());
        }

        if (cursor >= texto.length()) {
            return null;
        }

        ParseResult radicando = lerRadicando(texto, cursor);
        if (radicando == null) {
            return null;
        }

        String valorRadicando = radicando.valor().trim();
        if (valorRadicando.isEmpty()) {
            return null;
        }

        StringBuilder latex = new StringBuilder("\\sqrt");
        if (indiceRaiz != null && !indiceRaiz.isBlank()) {
            latex.append('[').append(indiceRaiz).append(']');
        }
        latex.append('{').append(valorRadicando).append('}');
        return new RaizParse(latex.toString(), radicando.proximoIndice());
    }

    private static boolean comecaComSqrt(String texto, int indice) {
        if (texto == null || indice < 0 || indice + 4 > texto.length()) {
            return false;
        }

        if (!texto.regionMatches(true, indice, "sqrt", 0, 4)) {
            return false;
        }

        if (indice > 0) {
            char anterior = texto.charAt(indice - 1);
            if (Character.isLetterOrDigit(anterior) || anterior == '\\' || anterior == '_') {
                return false;
            }
        }

        return true;
    }

    private static ParseResult lerRadicando(String texto, int indiceInicial) {
        char atual = texto.charAt(indiceInicial);
        return switch (atual) {
            case '{' -> lerGrupoBalanceado(texto, indiceInicial, '{', '}');
            case '(' -> lerGrupoBalanceado(texto, indiceInicial, '(', ')');
            case '[' -> lerGrupoBalanceado(texto, indiceInicial, '[', ']');
            default -> lerAtomo(texto, indiceInicial);
        };
    }

    private static ParseResult lerGrupoBalanceado(String texto, int indiceInicial, char abertura, char fechamento) {
        if (texto == null || indiceInicial < 0 || indiceInicial >= texto.length() || texto.charAt(indiceInicial) != abertura) {
            return null;
        }

        StringBuilder conteudo = new StringBuilder();
        int profundidade = 0;
        int indice = indiceInicial;

        while (indice < texto.length()) {
            char atual = texto.charAt(indice);

            if (atual == '\\') {
                if (indice + 1 >= texto.length()) {
                    return null;
                }

                conteudo.append(atual).append(texto.charAt(indice + 1));
                indice += 2;
                continue;
            }

            if (atual == abertura) {
                if (profundidade > 0) {
                    conteudo.append(atual);
                }
                profundidade++;
                indice++;
                continue;
            }

            if (atual == fechamento) {
                profundidade--;
                if (profundidade < 0) {
                    return null;
                }
                if (profundidade == 0) {
                    return new ParseResult(conteudo.toString(), indice + 1);
                }

                conteudo.append(atual);
                indice++;
                continue;
            }

            conteudo.append(atual);
            indice++;
        }

        return null;
    }

    private static ParseResult lerAtomo(String texto, int indiceInicial) {
        StringBuilder conteudo = new StringBuilder();
        int profundidadeChaves = 0;
        int profundidadeParenteses = 0;
        int profundidadeColchetes = 0;
        int indice = indiceInicial;

        while (indice < texto.length()) {
            char atual = texto.charAt(indice);

            if (atual == '\\') {
                conteudo.append(atual);
                indice++;
                if (indice >= texto.length()) {
                    break;
                }

                atual = texto.charAt(indice);
                conteudo.append(atual);
                indice++;
                if (Character.isLetter(atual)) {
                    while (indice < texto.length() && Character.isLetter(texto.charAt(indice))) {
                        conteudo.append(texto.charAt(indice));
                        indice++;
                    }
                }
                continue;
            }

            if (atual == '{') {
                profundidadeChaves++;
                conteudo.append(atual);
                indice++;
                continue;
            }
            if (atual == '}') {
                if (profundidadeChaves == 0) {
                    break;
                }
                profundidadeChaves--;
                conteudo.append(atual);
                indice++;
                continue;
            }

            if (atual == '(') {
                profundidadeParenteses++;
                conteudo.append(atual);
                indice++;
                continue;
            }
            if (atual == ')') {
                if (profundidadeParenteses == 0) {
                    break;
                }
                profundidadeParenteses--;
                conteudo.append(atual);
                indice++;
                continue;
            }

            if (atual == '[') {
                profundidadeColchetes++;
                conteudo.append(atual);
                indice++;
                continue;
            }
            if (atual == ']') {
                if (profundidadeColchetes == 0) {
                    break;
                }
                profundidadeColchetes--;
                conteudo.append(atual);
                indice++;
                continue;
            }

            if (profundidadeChaves == 0
                && profundidadeParenteses == 0
                && profundidadeColchetes == 0
                && (Character.isWhitespace(atual) || isSeparadorDeRaiz(atual))) {
                break;
            }

            conteudo.append(atual);
            indice++;
        }

        String valor = conteudo.toString().trim();
        return valor.isEmpty() ? null : new ParseResult(valor, indice);
    }

    private static int pularEspacos(String texto, int indice) {
        int cursor = indice;
        while (cursor < texto.length() && Character.isWhitespace(texto.charAt(cursor))) {
            cursor++;
        }
        return cursor;
    }

    private static boolean isSeparadorDeRaiz(char valor) {
        return valor == ',' || valor == ';' || valor == ':';
    }

    private record ParseResult(String valor, int proximoIndice) {
    }

    private record RaizParse(String latex, int proximoIndice) {
    }
}
