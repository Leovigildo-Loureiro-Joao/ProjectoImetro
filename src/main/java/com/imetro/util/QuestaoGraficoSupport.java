package com.imetro.util;

import java.util.Optional;
import java.util.Set;

import com.imetro.ui.model.Questao;

public final class QuestaoGraficoSupport {

    private static final Set<String> PALAVRAS_MATEMATICA_GRAFICA = Set.of(
        "funcao",
        "equacao",
        "equacoes",
        "grafico",
        "grafica",
        "coordenada",
        "coordenadas",
        "reta",
        "retas",
        "cartesiano",
        "cartesiana",
        "algebra",
        "afim",
        "sistema"
    );

    private static final Set<String> PALAVRAS_MATEMATICA_PARABOLA = Set.of(
        "parabola",
        "parabolas",
        "quadratica",
        "vertice",
        "concavidade"
    );

    private static final Set<String> PALAVRAS_FISICA_MOVIMENTO = Set.of(
        "movimento",
        "cinematica",
        "velocidade",
        "tempo",
        "posicao",
        "deslocamento",
        "trajetoria"
    );

    private static final Set<String> PALAVRAS_FISICA_PARABOLA = Set.of(
        "queda livre",
        "lancado",
        "altura maxima",
        "altura"
    );

    private static final Set<String> PALAVRAS_FISICA_PROPORCAO = Set.of(
        "forca",
        "aceleracao",
        "newton"
    );

    private QuestaoGraficoSupport() {
    }

    public static Optional<PlanoCartesianoConfig> resolver(Questao questao) {
        if (questao == null) {
            return Optional.empty();
        }

        Optional<PlanoCartesianoConfig> configExplicita = resolverConfigExplicita(questao);
        if (configExplicita.isPresent()) {
            return configExplicita;
        }

        String disciplina = TextoUtil.normalizarMaiusculo(questao.getDisciplina());
        String contexto = construirContexto(questao);
        String topico = TextoUtil.safeText(questao.getTopico(), "questao");

        if ("MATEMATICA".equals(disciplina) && contemQualquer(contexto, PALAVRAS_MATEMATICA_PARABOLA)) {
            return Optional.of(new PlanoCartesianoConfig(
                "Parabola para " + topico,
                "Usa a curva para testar concavidade, pontos de corte e regiao crescente ou decrescente.",
                "eixo x",
                "eixo y",
                "Neste tipo de questao, olha para o vertice antes de decidir onde a parabola sobe ou desce.",
                "#2563eb",
                TipoCurva.PARABOLA,
                1d,
                0d,
                -4d,
                -4d,
                4d,
                1d
            ));
        }

        if ("MATEMATICA".equals(disciplina) && contemQualquer(contexto, PALAVRAS_MATEMATICA_GRAFICA)) {
            return Optional.of(new PlanoCartesianoConfig(
                "Reta para " + topico,
                "O grafico ajuda a confirmar se a resposta combina com uma reta crescente ou decrescente.",
                "eixo x",
                "eixo y",
                "Se a taxa e positiva, a reta sobe da esquerda para a direita; se for negativa, ela desce.",
                "#2563eb",
                TipoCurva.RETA,
                1d,
                0d,
                0d,
                -4d,
                4d,
                1d
            ));
        }

        if ("FISICA".equals(disciplina) && contemQualquer(contexto, PALAVRAS_FISICA_PARABOLA)) {
            return Optional.of(new PlanoCartesianoConfig(
                "Curva de movimento para " + topico,
                "Boa para visualizar subida, pico e descida sem precisar de vetores nem setas extra.",
                "tempo (s)",
                "altura / posicao",
                "Em lancamentos, a curva sobe ate um ponto maximo e depois desce.",
                "#f97316",
                TipoCurva.PARABOLA,
                -1d,
                4d,
                0d,
                -1d,
                5d,
                1d
            ));
        }

        if ("FISICA".equals(disciplina) && contemQualquer(contexto, PALAVRAS_FISICA_MOVIMENTO)) {
            return Optional.of(new PlanoCartesianoConfig(
                "Reta de movimento para " + topico,
                "Ajuda a verificar se a grandeza cresce de forma uniforme ao longo do tempo.",
                "tempo (s)",
                "posicao / velocidade",
                "Se a inclinacao aumenta de forma constante, a curva deve manter a mesma direcao.",
                "#f97316",
                TipoCurva.RETA,
                2d,
                0d,
                0d,
                -1d,
                5d,
                1d
            ));
        }

        if ("FISICA".equals(disciplina) && contemQualquer(contexto, PALAVRAS_FISICA_PROPORCAO)) {
            return Optional.of(new PlanoCartesianoConfig(
                "Reta proporcional para " + topico,
                "Neste caso a linha corta o zero e ajuda a validar relacoes diretas como F = m.a.",
                "aceleracao / massa",
                "forca (N)",
                "Quando a relacao e diretamente proporcional, a reta passa pela origem.",
                "#f97316",
                TipoCurva.RETA,
                2d,
                0d,
                0d,
                -1d,
                5d,
                1d
            ));
        }

        return Optional.empty();
    }

    private static Optional<PlanoCartesianoConfig> resolverConfigExplicita(Questao questao) {
        if (questao == null || !questao.isUsaGrafico()) {
            return Optional.empty();
        }

        String disciplina = TextoUtil.normalizarMaiusculo(questao.getDisciplina());
        if (!"MATEMATICA".equals(disciplina) && !"FISICA".equals(disciplina)) {
            return Optional.empty();
        }

        TipoCurva tipoCurva = TipoCurva.fromTexto(questao.getGraficoTipoCurva());
        if (tipoCurva == TipoCurva.NENHUM) {
            return Optional.empty();
        }

        String topico = TextoUtil.safeText(questao.getTopico(), "questao");
        String titulo = switch (tipoCurva) {
            case RETA -> "Reta para " + topico;
            case PARABOLA -> "Parabola para " + topico;
            case NENHUM -> "Grafico para " + topico;
        };

        String subtitulo = "Grafico gerado a partir da configuracao da propria questao.";
        String dica = switch (tipoCurva) {
            case RETA -> "Confirma se a inclinacao, o corte no eixo y e o sentido da reta batem com o exercicio.";
            case PARABOLA -> "Confirma concavidade, vertice e pontos de corte antes de responder.";
            case NENHUM -> "Usa o grafico como apoio visual para validar a resposta.";
        };

        return Optional.of(new PlanoCartesianoConfig(
            titulo,
            subtitulo,
            TextoUtil.safeText(questao.getGraficoEixoX(), "eixo x"),
            TextoUtil.safeText(questao.getGraficoEixoY(), "eixo y"),
            dica,
            "FISICA".equals(disciplina) ? "#f97316" : "#2563eb",
            tipoCurva,
            valueOrDefault(questao.getGraficoA(), 1d),
            valueOrDefault(questao.getGraficoB(), 0d),
            valueOrDefault(questao.getGraficoC(), 0d),
            valueOrDefault(questao.getGraficoXMin(), -4d),
            valueOrDefault(questao.getGraficoXMax(), 4d),
            garantirTickUnit(questao.getGraficoXTickUnit())
        ));
    }

    private static String construirContexto(Questao questao) {
        return TextoUtil.normalizarMinusculo(String.join(" ",
            TextoUtil.safeText(questao.getDisciplina(), ""),
            TextoUtil.safeText(questao.getEnunciado(), ""),
            TextoUtil.safeText(questao.getBloco2(), ""),
            TextoUtil.safeText(questao.getTopico(), ""),
            TextoUtil.safeText(questao.getSubtopico(), ""),
            TextoUtil.safeText(questao.getTopicoPrincipal(), "")
        ));
    }

    private static boolean contemQualquer(String texto, Set<String> palavras) {
        return palavras.stream().anyMatch(texto::contains);
    }

    public enum TipoCurva {
        RETA,
        PARABOLA,
        NENHUM;

        public static TipoCurva fromTexto(String valor) {
            String normalizado = TextoUtil.normalizarMaiusculo(valor);
            return switch (normalizado) {
                case "RETA" -> RETA;
                case "PARABOLA" -> PARABOLA;
                default -> NENHUM;
            };
        }
    }

    public record PlanoCartesianoConfig(
        String titulo,
        String subtitulo,
        String eixoX,
        String eixoY,
        String dica,
        String accentHex,
        TipoCurva tipoCurva,
        double a,
        double b,
        double c,
        double xMin,
        double xMax,
        double xTickUnit
    ) {
        public double calcularY(double x) {
            return switch (tipoCurva) {
                case RETA -> (a * x) + b;
                case PARABOLA -> (a * x * x) + (b * x) + c;
                case NENHUM -> 0d;
            };
        }
    }

    private static double valueOrDefault(Double value, double defaultValue) {
        return value == null || !Double.isFinite(value) ? defaultValue : value;
    }

    private static double garantirTickUnit(Double value) {
        if (value == null || !Double.isFinite(value) || value <= 0d) {
            return 1d;
        }
        return value;
    }
}
