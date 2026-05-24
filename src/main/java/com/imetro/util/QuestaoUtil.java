package com.imetro.util;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import com.imetro.domain.dto.disciplina.DisciplinaDto;
import com.imetro.services.DisciplinaService;
import com.imetro.ui.model.Questao;

public class QuestaoUtil {

    public static String formatarDisciplina(String valor) {
        return switch (normalizar(valor)) {
            case "matematica" -> "Matematica";
            case "fisica" -> "Fisica";
            default -> toTitleCase(valor);
        };
    }

    public static String toTitleCase(String valor) {
        String[] partes = safeText(valor, "").trim().split("\\s+");
        StringBuilder texto = new StringBuilder();
        for (String parte : partes) {
            if (parte.isBlank()) {
                continue;
            }
            if (texto.length() > 0) {
                texto.append(' ');
            }
            texto.append(parte.substring(0, 1).toUpperCase(Locale.ROOT));
            if (parte.length() > 1) {
                texto.append(parte.substring(1).toLowerCase(Locale.ROOT));
            }
        }
        return texto.isEmpty() ? valor : texto.toString();
    }

    public static String formatarPercentual(double percentual) {
        return Math.round(percentual) + "%";
    }

    public static String formatarDelta(double valor) {
        long arredondado = Math.round(valor);
        return (arredondado > 0 ? "+" : "") + arredondado + "%";
    }

    public static double limitarPercentual(double percentual) {
        return Math.max(0d, Math.min(1d, percentual / 100d));
    }

    public static double limitarPercentualUnitario(double valor) {
        return Math.max(0d, Math.min(1d, valor));
    }

    public static double limitarPercentualFaixaCem(double valor) {
        return Math.max(0d, Math.min(100d, valor));
    }

    public static double calcularPercentualDificuldade(Questao questao) {
        if (questao == null) {
            return 0d;
        }
        return calcularPercentualDificuldade(questao.getNivelDificuldade(), questao.getRigor());
    }

    public static double calcularPercentualDificuldade(Integer nivelDificuldade, Double rigor) {
        if (rigor != null && Double.isFinite(rigor) && rigor > 0d) {
            return limitarPercentualFaixaCem(rigor * 100d);
        }
        if (nivelDificuldade != null && nivelDificuldade > 0) {
            return limitarPercentualFaixaCem((nivelDificuldade / 5d) * 100d);
        }
        return 0d;
    }

    public static String safeText(Object value, String defaultValue) {
        return TextoUtil.safeText(value, defaultValue);
    }

    public static String normalizar(String valor) {
        return TextoUtil.normalizarMinusculo(valor);
    }

    public static String normalizarTextoLivre(String valor) {
        return TextoUtil.normalizarMinusculo(valor);
    }

    public static String unescapeJson(String value) {
        StringBuilder out = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            if (ch != '\\' || i + 1 >= value.length()) {
                out.append(ch);
                continue;
            }

            char next = value.charAt(++i);
            switch (next) {
                case '"' -> out.append('"');
                case '\\' -> out.append('\\');
                case '/' -> out.append('/');
                case 'b' -> out.append('\b');
                case 'f' -> out.append('\f');
                case 'n' -> out.append('\n');
                case 'r' -> out.append('\r');
                case 't' -> out.append('\t');
                case 'u' -> {
                    if (i + 4 >= value.length()) {
                        out.append("\\u");
                        break;
                    }
                    String hex = value.substring(i + 1, i + 5);
                    out.append((char) Integer.parseInt(hex, 16));
                    i += 4;
                }
                default -> out.append(next);
            }
        }
        return out.toString();
    }

    public static UUID resolverDisciplinaId(String disciplina) {
        String disciplinaNormalizada = normalizar(disciplina);
        for (DisciplinaDto disciplinaDto : DisciplinaService.discCategoria()) {
            if (disciplinaDto.id() != null && normalizar(disciplinaDto.nome()).equals(disciplinaNormalizada)) {
                return disciplinaDto.id();
            }
        }

        return UUID.nameUUIDFromBytes(("disciplina:" + disciplinaNormalizada).getBytes(StandardCharsets.UTF_8));
    }

    public static String resolverNivelDiagnostico(double percentualAcerto) {
        if (percentualAcerto >= 85d) {
            return "EXPERT";
        }
        if (percentualAcerto >= 65d) {
            return "AVANCADO";
        }
        if (percentualAcerto >= 40d) {
            return "INTERMEDIARIO";
        }
        return "INICIANTE";
    }

    public static String construirJsonRespostas(
        List<Integer> indices,
        List<Questao> questoes,
        List<Character> respostasUsuario
    ) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < indices.size(); i++) {
            int indice = indices.get(i);
            Questao questao = questoes.get(indice);
            char marcada = respostasUsuario.get(indice);
            boolean acertou = marcada == questao.getRespostaCorreta();
            double precisaoResposta = CalculoStats.calcularPrecisaoResposta(questao, marcada);

            if (i > 0) {
                json.append(", ");
            }

            json.append("{")
                .append("\"questaoId\":\"").append(escapeJson( QuestaoUtil.safeText(questao.getId(), ""))).append("\",")
                .append("\"topico\":\"").append(escapeJson( QuestaoUtil.safeText(questao.getTopico(), ""))).append("\",")
                .append("\"subtopico\":\"").append(escapeJson( QuestaoUtil.safeText(questao.getSubtopico(), ""))).append("\",")
                .append("\"marcada\":\"").append(marcada).append("\",")
                .append("\"correta\":\"").append(questao.getRespostaCorreta()).append("\",")
                .append("\"precisao\":").append(String.format(Locale.ROOT, "%.4f", precisaoResposta)).append(",")
                .append("\"percentualAcerto\":").append(String.format(Locale.ROOT, "%.2f", precisaoResposta * 100d)).append(",")
                .append("\"acertou\":").append(acertou)
                .append("}");
        }
        json.append("]");
        return json.toString();
    }


    public static String construirJsonErrosComuns(
        List<Integer> indices,
        List<Questao> questoes,
        List<Character> respostasUsuario
    ) {
        StringBuilder json = new StringBuilder("[");
        boolean primeiroItem = true;

        for (int i = 0; i < indices.size(); i++) {
            int indice = indices.get(i);
            Questao questao = questoes.get(indice);
            char marcada = respostasUsuario.get(indice);
            boolean errou = marcada != questao.getRespostaCorreta();
            double rigor = Double.isFinite(questao.getRigor())
                ? limitarPercentualUnitario(questao.getRigor())
                : 0d;
            double percentualDificuldade = calcularPercentualDificuldade(questao);

            if (!errou) {
                continue;
            }

            if (!primeiroItem) {
                json.append(", ");
            }

            json.append("{")
                .append("\"questaoId\":\"").append(escapeJson(QuestaoUtil.safeText(questao.getId(), ""))).append("\",")
                .append("\"topico\":\"").append(escapeJson(QuestaoUtil.safeText(questao.getTopico(), ""))).append("\",")
                .append("\"subtopico\":\"").append(escapeJson(QuestaoUtil.safeText(questao.getSubtopico(), ""))).append("\",")
                .append("\"marcada\":\"").append(marcada).append("\",")
                .append("\"nivelDificuldade\":").append(Math.max(0, questao.getNivelDificuldade())).append(",")
                .append("\"rigor\":").append(String.format(Locale.ROOT, "%.4f", rigor)).append(",")
                .append("\"percentualDificuldade\":").append(String.format(Locale.ROOT, "%.2f", percentualDificuldade)).append(",")
                .append("\"enuciado\":\"").append(escapeJson(QuestaoUtil.safeText(questao.getEnunciado(), ""))).append("\",")
                .append("\"resposta\":\"").append(questao.getRespostaCorreta()).append("\"")
                .append("}");

            primeiroItem = false;

        }
        json.append("]");
        return json.toString();
    }

    public static String escapeJson(String valor) {
        return valor
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\r", "\\r")
            .replace("\n", "\\n")
            .replace("\t", "\\t");
    }

    public static String resolverTextoRespostaCorreta(String respostaCorreta, List<String> respostas) {
        if (respostas == null || respostas.isEmpty()) {
            return "";
        }

        char letraCorreta = resolverRespostaCorreta(respostaCorreta, respostas);
        return resolverTextoOpcao(respostas, letraCorreta);
    }

    public static String resolverTextoOpcao(List<String> respostas, char letra) {
        int indice = Character.toUpperCase(letra) - 'A';
        if (indice < 0 || indice >= respostas.size()) {
            return respostas.getFirst();
        }
        return respostas.get(indice);
    }

    public static String resolverTextoOpcao(Questao resposta, char letra) {
       switch (letra) {
        case 'A':
            return resposta.getOpcaoA();
        case 'B':
            return resposta.getOpcaoB();
        case 'C':
            return resposta.getOpcaoC();
        case 'D':
            return resposta.getOpcaoD();
        case 'E':
            return resposta.getOpcaoE();
        case 'F':
            return resposta.getOpcaoF();
        default:
            return resposta.getOpcaoG();

       }
    }

    public static char resolverRespostaCorreta(String respostaCorreta, List<String> respostas) {
        if (respostaCorreta != null && !respostaCorreta.isBlank()) {
            char primeiraLetra = Character.toUpperCase(respostaCorreta.trim().charAt(0));
            if (primeiraLetra >= 'A' && primeiraLetra <= 'G') {
                return primeiraLetra;
            }

            String normalizada =  QuestaoUtil.normalizarTextoLivre(respostaCorreta);
            for (int i = 0; i < respostas.size(); i++) {
                if ( QuestaoUtil.normalizarTextoLivre(respostas.get(i)).equals(normalizada)) {
                    return (char) ('A' + i);
                }
            }
        }
        return 'A';
    }



    public static record Evolucao(int qtdErros,int qtdAcertos,int tempoSegundos) {
    }

}
