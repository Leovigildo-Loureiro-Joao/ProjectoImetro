package com.imetro.ui.controller.candidato.resultados;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.imetro.App;
import com.imetro.ui.controller.candidato.BibliotecaController;
import com.imetro.ui.controller.lifecycle.DisposableController;
import com.imetro.util.QuestaoResultado;
import com.imetro.util.ResultadoPayload;
import com.imetro.util.TextoUtil;
import com.jfoenix.controls.JFXButton;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ResultadoRecomendacao implements Initializable, DisposableController {

    @FXML
    private JFXButton btnToggleRecomendacao;

    @FXML
    private JFXButton abrirBibliotecaButton;

    @FXML
    private Label bibliotecaHintValue;

    @FXML
    private Label leituraDetalheValue;
    

    @FXML
    private Label observacoesDetalheValue;

    @FXML
    private VBox painelRecomendacao;

    @FXML
    private Label recomendacaoValue;

    @FXML
    private Accordion recommendationAccordion;

    @FXML
    private VBox trilhaDetalheBox;

    private boolean painelRecomendacaoVisivel = true;
    private ResultadoPayload resultado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resultado = carregarResultado();

        if (recomendacaoValue != null) {
            recomendacaoValue.setText(construirResumoRecomendacao(resultado));
        }
        if (observacoesDetalheValue != null) {
            observacoesDetalheValue.setText(construirObservacoes(resultado));
        }
        if (leituraDetalheValue != null) {
            leituraDetalheValue.setText(construirLeitura(resultado));
        }
        if (bibliotecaHintValue != null) {
            bibliotecaHintValue.setText(construirHintBiblioteca(resultado));
        }
        if (abrirBibliotecaButton != null) {
            abrirBibliotecaButton.setText(construirTextoBotaoBiblioteca(resultado));
        }

        aplicarEstadoPainel(true);
        renderizarTrilha(resultado);
        Platform.runLater(this::expandirPrimeiroTitledPane);
    }

    @FXML
    private void togglePainelRecomendacao() {
        aplicarEstadoPainel(!painelRecomendacaoVisivel);
    }

    @FXML
    private void abrirBibliotecaDisciplina() {
        if (resultado != null && textoReal(resultado.getDisciplina())) {
            BibliotecaController.definirDisciplinaPreferida(resultado.getDisciplina());
        }

        if (abrirBibliotecaButton == null || abrirBibliotecaButton.getScene() == null) {
            return;
        }

        Node contentHostNode = abrirBibliotecaButton.getScene().lookup("#contentHost");
        if (!(contentHostNode instanceof StackPane contentHost)) {
            return;
        }

        App.swapContent(contentHost, "views/pages/candidato/livro");
    }

    @Override
    public void dispose() {
    }

    private ResultadoPayload carregarResultado() {
        ResultadoPayload atual = ResultadoAvaliacaoController.ultimoResultado;
        if (atual != null) {
            return atual;
        }

        return new ResultadoPayload(
            "Avaliacao",
            "-",
            0,
            0,
            0,
            0d,
            "00:00",
            "-",
            "-",
            "Conclua uma avaliacao para ver recomendacoes mais precisas.",
            "views/pages/candidato/dashboard",
            List.of()
        );
    }

    private void aplicarEstadoPainel(boolean visivel) {
        painelRecomendacaoVisivel = visivel;

        if (painelRecomendacao != null) {
            painelRecomendacao.setVisible(visivel);
            painelRecomendacao.setManaged(visivel);
        }
        if (btnToggleRecomendacao != null) {
            btnToggleRecomendacao.setText(visivel ? "Ocultar recomendacao" : "Ver recomendacao");
        }

        if (visivel) {
            expandirPrimeiroTitledPane();
        }
    }

    private void expandirPrimeiroTitledPane() {
        if (!painelRecomendacaoVisivel || recommendationAccordion == null) {
            return;
        }
        if (!recommendationAccordion.getPanes().isEmpty()) {
            recommendationAccordion.setExpandedPane(recommendationAccordion.getPanes().get(0));
        }
    }

    private void renderizarTrilha(ResultadoPayload resultado) {
        if (trilhaDetalheBox == null) {
            return;
        }

        trilhaDetalheBox.getChildren().clear();

        List<GrupoTrilha> grupos = agruparQuestoesErradas(resultado);
        if (grupos.isEmpty()) {
            trilhaDetalheBox.getChildren().add(criarCardConsolidacao(resultado));
            return;
        }

        for (GrupoTrilha grupo : grupos) {
            trilhaDetalheBox.getChildren().add(criarCardTrilha(grupo, resultado));
        }
    }

    private List<GrupoTrilha> agruparQuestoesErradas(ResultadoPayload resultado) {
        LinkedHashMap<String, List<QuestaoResultado>> grupos = new LinkedHashMap<>();
        if (resultado == null || resultado.getQuestoesResultado() == null) {
            return List.of();
        }

        for (QuestaoResultado questao : resultado.getQuestoesResultado()) {
            if (questao == null || questao.isAcertou()) {
                continue;
            }

            String chave = textoOuFallback(
                questao.getSubtopico(),
                textoOuFallback(questao.getTopico(), textoOuFallback(questao.getDisciplina(), "Tema principal"))
            );
            grupos.computeIfAbsent(chave, key -> new ArrayList<>()).add(questao);
        }

        return grupos.entrySet().stream()
            .sorted((left, right) -> {
                int comparacao = Integer.compare(right.getValue().size(), left.getValue().size());
                if (comparacao != 0) {
                    return comparacao;
                }
                return left.getKey().compareToIgnoreCase(right.getKey());
            })
            .limit(4)
            .map(entry -> new GrupoTrilha(entry.getKey(), List.copyOf(entry.getValue())))
            .toList();
    }

    private VBox criarCardTrilha(GrupoTrilha grupo, ResultadoPayload resultado) {
        String tituloTexto = textoOuFallback(grupo.titulo(), "Tema a rever");

        Label titulo = new Label(tituloTexto);
        titulo.getStyleClass().add("question-side-title");
        titulo.setMaxWidth(Double.MAX_VALUE);

        Label badge = new Label(grupo.questoes().size() == 1 ? "1 erro" : grupo.questoes().size() + " erros");
        badge.getStyleClass().add("result-trail-badge");
        badge.getStyleClass().add("result-trail-badge-warn");

        HBox header = new HBox(10.0, titulo, badge);
        HBox.setHgrow(titulo, Priority.ALWAYS);

        int totalErros = grupo.questoes().size();
        Label resumo = new Label(totalErros == 1
            ? "1 questao errada nesta zona."
            : totalErros + " questoes erradas nesta zona.");
        resumo.getStyleClass().add("question-side-copy");
        resumo.setWrapText(true);

        Label topicos = new Label(construirTopicosAssociados(grupo.questoes()));
        topicos.getStyleClass().add("question-side-caption");
        topicos.setWrapText(true);

        Label caminho = new Label(String.format(
            Locale.ROOT,
            "Abre a biblioteca de %s para rever este bloco com mais exemplos.",
            textoOuFallback(resultado == null ? null : resultado.getDisciplina(), "a disciplina")
        ));
        caminho.getStyleClass().add("question-side-caption");
        caminho.setWrapText(true);

        VBox card = new VBox(8.0, header, resumo, topicos, caminho);
        card.getStyleClass().add("result-trail-card");
        card.setMaxWidth(Double.MAX_VALUE);
        return card;
    }

    private VBox criarCardConsolidacao(ResultadoPayload resultado) {
        Label titulo = new Label("Sem erros nesta tentativa");
        titulo.getStyleClass().add("question-side-title");

        Label badge = new Label("Consolidacao");
        badge.getStyleClass().add("result-trail-badge");
        badge.getStyleClass().add("result-trail-badge-good");

        HBox header = new HBox(10.0, titulo, badge);
        HBox.setHgrow(titulo, Priority.ALWAYS);

        Label resumo = new Label(String.format(
            Locale.ROOT,
            "Usa a biblioteca de %s para consolidar a base e explorar materiais mais avancados.",
            textoOuFallback(resultado == null ? null : resultado.getDisciplina(), "a disciplina")
        ));
        resumo.getStyleClass().add("question-side-copy");
        resumo.setWrapText(true);

        Label caminho = new Label("A recomendacao abaixo liga-te a area certa para continuares a aprender.");
        caminho.getStyleClass().add("question-side-caption");
        caminho.setWrapText(true);

        VBox card = new VBox(8.0, header, resumo, caminho);
        card.getStyleClass().add("result-trail-card");
        card.setMaxWidth(Double.MAX_VALUE);
        return card;
    }

    private String construirTopicosAssociados(List<QuestaoResultado> questoes) {
        LinkedHashSet<String> topicos = new LinkedHashSet<>();
        if (questoes != null) {
            for (QuestaoResultado questao : questoes) {
                if (questao == null) {
                    continue;
                }

                String topico = textoReal(questao.getTopico()) ? questao.getTopico().trim() : "";
                if (!topico.isBlank()) {
                    topicos.add(topico);
                    continue;
                }

                String subtopico = textoReal(questao.getSubtopico()) ? questao.getSubtopico().trim() : "";
                if (!subtopico.isBlank()) {
                    topicos.add(subtopico);
                }
            }
        }

        if (topicos.isEmpty()) {
            return "Topicos associados: nao identificados.";
        }
        return "Topicos associados: " + topicos.stream().limit(3).collect(Collectors.joining(", "));
    }

    private String construirResumoRecomendacao(ResultadoPayload resultado) {
        if (resultado == null) {
            return "Conclui uma avaliacao para receber recomendacoes mais precisas.";
        }

        String texto = textoOuFallback(resultado.getRecomendacao(), "");
        if (texto.isBlank()) {
            texto = resultado.getErros() > 0
                ? "Vale consolidar a base antes da proxima tentativa."
                : "Boa! Mantem o ritmo para aprofundar ainda mais a area.";
        }

        if (resultado.getErros() > 0) {
            String pontos = resultado.getErros() == 1 ? "1 ponto" : resultado.getErros() + " pontos";
            return texto + " Tens " + pontos + " para rever em "
                + textoOuFallback(resultado.getDisciplina(), "esta area") + ".";
        }

        return texto + " Usa a biblioteca abaixo para aprofundar "
            + textoOuFallback(resultado.getDisciplina(), "a area") + ".";
    }

    private String construirObservacoes(ResultadoPayload resultado) {
        if (resultado == null || resultado.getTotalQuestoes() <= 0) {
            return "Conclui uma avaliacao para veres observacoes detalhadas e um trilho de estudo mais preciso.";
        }

        String disciplina = textoOuFallback(resultado.getDisciplina(), "esta area");
        String resumoBase = String.format(
            Locale.ROOT,
            "Na %s, tiveste %d acertos em %d questoes.",
            disciplina,
            resultado.getAcertos(),
            resultado.getTotalQuestoes()
        );

        List<String> focos = topicosPrioritarios(resultado);
        if (resultado.getErros() > 0) {
            String focosTexto = focos.isEmpty() ? "os fundamentos" : String.join(", ", focos);
            return resumoBase
                + " Os erros concentraram-se em " + focosTexto + "."
                + " Rever esses pontos na biblioteca vai acelerar a melhoria.";
        }

        return resumoBase
            + " Nao houve erros, por isso o proximo passo e consolidar e explorar materiais mais avancados.";
    }

    private String construirLeitura(ResultadoPayload resultado) {
        if (resultado == null) {
            return "Abre a biblioteca da disciplina para encontrar livros e PDFs que te ajudem a aprender mais sobre a area.";
        }

        String disciplina = textoOuFallback(resultado.getDisciplina(), "a disciplina");
        List<String> focos = topicosPrioritarios(resultado);

        if (focos.isEmpty()) {
            return "Abre a biblioteca de " + disciplina
                + " para rever os materiais essenciais e continuar a aprofundar a area.";
        }

        return "Abre a biblioteca de " + disciplina
            + " e procura conteudos sobre " + String.join(", ", focos)
            + ". Comeca pelos materiais base e passa depois para exercicios mais exigentes.";
    }

    private String construirHintBiblioteca(ResultadoPayload resultado) {
        if (resultado == null || !textoReal(resultado.getDisciplina())) {
            return "A biblioteca abre com os materiais disponiveis para continuares a aprender mais sobre a area.";
        }

        return "A biblioteca vai abrir ja filtrada para " + resultado.getDisciplina()
            + ", para rever os conteudos desta area com mais calma.";
    }

    private String construirTextoBotaoBiblioteca(ResultadoPayload resultado) {
        if (resultado == null || !textoReal(resultado.getDisciplina())) {
            return "Abrir biblioteca";
        }
        return "Abrir biblioteca de " + resultado.getDisciplina();
    }

    private List<String> topicosPrioritarios(ResultadoPayload resultado) {
        LinkedHashSet<String> topicos = new LinkedHashSet<>();
        if (resultado == null || resultado.getQuestoesResultado() == null) {
            return List.of();
        }

        for (QuestaoResultado questao : resultado.getQuestoesResultado()) {
            if (questao == null || questao.isAcertou()) {
                continue;
            }

            String subtopico = textoReal(questao.getSubtopico()) ? questao.getSubtopico().trim() : "";
            if (!subtopico.isBlank()) {
                topicos.add(subtopico);
                continue;
            }

            String topico = textoReal(questao.getTopico()) ? questao.getTopico().trim() : "";
            if (!topico.isBlank()) {
                topicos.add(topico);
            }
        }

        return topicos.stream().limit(3).toList();
    }

    private boolean textoReal(String valor) {
        String texto = TextoUtil.safeText(valor, "");
        return !texto.isBlank() && !"-".equals(texto);
    }

    private String textoOuFallback(String valor, String fallback) {
        String texto = TextoUtil.safeText(valor, fallback);
        if ("-".equals(texto)) {
            return fallback;
        }
        return texto;
    }

    private record GrupoTrilha(String titulo, List<QuestaoResultado> questoes) {
    }
}
