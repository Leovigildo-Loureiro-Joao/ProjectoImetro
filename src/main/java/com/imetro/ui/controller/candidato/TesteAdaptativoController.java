package com.imetro.ui.controller.candidato;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.imetro.App;
import com.imetro.domain.dto.Topico;
import com.imetro.services.TesteAdaptativoService;
import com.imetro.ui.components.CircleProgress;
import com.imetro.ui.model.Questao;
import com.imetro.util.QuestaoResultado;
import com.imetro.util.ResultadoPayload;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXToggleNode;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class TesteAdaptativoController {

    @FXML private StackPane circleProgressContainer;
    @FXML private Label nomeDisc;
    @FXML private Label nPergunta;
    @FXML private Label bloco1;
    @FXML private Label bloco2;
    @FXML private Label ResA;
    @FXML private Label ResB;
    @FXML private Label ResC;
    @FXML private Label ResD;
    @FXML private Label ResE;
    @FXML private Label ResF;
    @FXML private Label ResG;
    @FXML private JFXToggleNode toggleA;
    @FXML private JFXToggleNode toggleB;
    @FXML private JFXToggleNode toggleC;
    @FXML private JFXToggleNode toggleD;
    @FXML private JFXToggleNode toggleE;
    @FXML private JFXToggleNode toggleF;
    @FXML private JFXToggleNode toggleG;
    @FXML private ToggleGroup alternativas;
    @FXML private VBox testeContainer;
    @FXML private VBox start;
    @FXML private VBox tela;
    @FXML private VBox disciplinasContainer;
    @FXML private ImageView imgBloco2;
    @FXML private JFXButton btnConfirmar;
    @FXML private JFXButton btnProximo;
    @FXML private StackPane loadingOverlay;
    @FXML private ProgressBar loadingProgress;
    @FXML private Label loadingMessage;
    @FXML private Label nivelAtual;
    @FXML private Label dificuldadeAtual;
    @FXML private Label tempo;
    @FXML private Label acertosLabel;
    @FXML private Label errosLabel;
    @FXML private VBox feedbackContainer;
    @FXML private Label feedbackIcon;
    @FXML private Label feedbackMessage;

    private final VBox botoesDisciplinasBox = new VBox(12);
    private final VBox focoTopicosBox = new VBox(12);
    private final Map<String, List<JFXCheckBox>> checkboxesPorTopico = new LinkedHashMap<>();
    private final List<Character> respostasUsuario = new ArrayList<>();
    private final List<Long> temposResposta = new ArrayList<>();
    private final List<String> topicosSelecionados = new ArrayList<>();
    private final List<String> subtopicosSelecionados = new ArrayList<>();

    private CircleProgress circleProgress;
    private List<Questao> questoes = new ArrayList<>();
    private List<Questao> focoQuestoes = new ArrayList<>();
    private int questaoAtual = 0;
    private int totalQuestoes = 0;
    private char respostaSelecionada;
    private int nivelAtualInt = 2;
    private int acertos = 0;
    private int erros = 0;
    private int sequenciaAcertos = 0;
    private int sequenciaErros = 0;
    private long tempoInicioQuestao;
    private Timeline cronometro;
    private int segundos = 0;
    private int minutos = 0;
    private TesteAdaptativoService service;
    private String disciplinaSelecionada;

    @FXML
    public void initialize() {
        circleProgress = new CircleProgress(35, 35, 35, 0);
        circleProgressContainer.getChildren().add(circleProgress);

        service = new TesteAdaptativoService();

        disciplinasContainer.getChildren().clear();
        focoTopicosBox.setPadding(new Insets(12, 0, 0, 0));
        disciplinasContainer.getChildren().addAll(botoesDisciplinasBox, focoTopicosBox);

        carregarDisciplinas();

        feedbackContainer.setVisible(false);
        testeContainer.setVisible(false);
        start.setVisible(true);
    }

    private void carregarDisciplinas() {
        botoesDisciplinasBox.getChildren().clear();

        Label titulo = new Label("Disciplinas com questoes organizadas por topico e subtopico");
        titulo.getStyleClass().add("h3-thin");
        botoesDisciplinasBox.getChildren().add(titulo);

        for (String disciplina : service.carregarDisciplinasDisponiveis()) {
            JFXButton botao = new JFXButton(formatarDisciplina(disciplina));
            botao.getStyleClass().add("btn-primary");
            botao.setPrefWidth(260);
            botao.setOnAction(event -> selecionarDisciplina(disciplina));
            botoesDisciplinasBox.getChildren().add(botao);
        }
    }

    private void selecionarDisciplina(String disciplina) {
        disciplinaSelecionada = disciplina;
        renderizarFocoTopicos(service.carregarTopicosPorDisciplina(disciplina));
    }

    private void renderizarFocoTopicos(List<Topico> topicos) {
        focoTopicosBox.getChildren().clear();
        checkboxesPorTopico.clear();

        Label disciplinaLabel = new Label("Foco atual: " + formatarDisciplina(disciplinaSelecionada));
        disciplinaLabel.getStyleClass().add("h3-thin-big");

        Label resumoLabel = new Label("Escolha os subtopicos que devem guiar o exame.");
        resumoLabel.getStyleClass().add("muted");
        resumoLabel.setWrapText(true);

        focoTopicosBox.getChildren().addAll(disciplinaLabel, resumoLabel);

        for (Topico topico : topicos) {
            VBox grupo = new VBox(10);
            Label tituloTopico = new Label(topico.topicos());
            tituloTopico.getStyleClass().add("h3-thin");
            grupo.getChildren().add(tituloTopico);

            List<JFXCheckBox> checkboxes = new ArrayList<>();
            for (String subtopico : topico.subTopicos()) {
                JFXCheckBox checkBox = new JFXCheckBox(subtopico);
                checkBox.setWrapText(true);
                grupo.getChildren().add(checkBox);
                checkboxes.add(checkBox);
            }

            checkboxesPorTopico.put(topico.topicos(), checkboxes);
            focoTopicosBox.getChildren().add(grupo);
        }

        HBox acoes = new HBox(12);

        JFXButton iniciarSelecionados = new JFXButton("Iniciar com selecionados");
        iniciarSelecionados.getStyleClass().add("btn-primary");
        iniciarSelecionados.setOnAction(event -> iniciarTesteAdaptativo(false));

        JFXButton iniciarTodos = new JFXButton("Usar todos os subtopicos");
        iniciarTodos.getStyleClass().add("btn-primary-two");
        iniciarTodos.setOnAction(event -> iniciarTesteAdaptativo(true));

        acoes.getChildren().addAll(iniciarSelecionados, iniciarTodos);
        focoTopicosBox.getChildren().add(acoes);
    }

    private void iniciarTesteAdaptativo(boolean usarTodosSubtopicos) {
        if (disciplinaSelecionada == null || disciplinaSelecionada.isBlank()) {
            mostrarAlerta("Atencao", "Selecione uma disciplina para continuar.");
            return;
        }

        atualizarFocoSelecionado(usarTodosSubtopicos);
        if (!usarTodosSubtopicos && subtopicosSelecionados.isEmpty()) {
            mostrarAlerta("Atencao", "Selecione ao menos um subtopico antes de iniciar.");
            return;
        }

        focoQuestoes = service.carregarQuestoesDisponiveis(disciplinaSelecionada, topicosSelecionados, subtopicosSelecionados);
        if (focoQuestoes.isEmpty()) {
            mostrarAlerta("Atencao", "Nao encontramos questoes para esse foco. Tente outro recorte.");
            return;
        }

        nivelAtualInt = 2;
        questoes.clear();
        totalQuestoes = Math.min(10, focoQuestoes.size());
        resetarMetricas();

        Questao primeiraQuestao = obterProximaQuestao();
        if (primeiraQuestao == null) {
            mostrarAlerta("Atencao", "Nao foi possivel preparar o exame.");
            return;
        }

        questoes.add(primeiraQuestao);
        nomeDisc.setText(formatarDisciplina(disciplinaSelecionada));

        start.setVisible(false);
        testeContainer.setVisible(true);
        iniciarLoading();
    }

    private void atualizarFocoSelecionado(boolean usarTodosSubtopicos) {
        topicosSelecionados.clear();
        subtopicosSelecionados.clear();

        for (Map.Entry<String, List<JFXCheckBox>> entry : checkboxesPorTopico.entrySet()) {
            List<String> escolhidos = new ArrayList<>();
            for (JFXCheckBox checkBox : entry.getValue()) {
                if (usarTodosSubtopicos || checkBox.isSelected()) {
                    escolhidos.add(checkBox.getText());
                }
            }

            if (!escolhidos.isEmpty()) {
                topicosSelecionados.add(entry.getKey());
                subtopicosSelecionados.addAll(escolhidos);
            }
        }
    }

    private void iniciarLoading() {
        tela.setVisible(false);
        loadingOverlay.setVisible(true);
        loadingOverlay.setOpacity(1);
        loadingProgress.setProgress(0);

        String[] mensagens = {
            "Organizando foco em " + String.join(", ", topicosSelecionados),
            "Separando questoes para " + buildResumoSubtopicos(),
            "Ajustando nivel inicial adaptativo...",
            "Exame adaptativo pronto!"
        };

        Timeline loadingTimeline = new Timeline();
        for (int i = 0; i <= 100; i++) {
            final int progresso = i;
            KeyFrame kf = new KeyFrame(Duration.millis(i * 25), e -> {
                loadingProgress.setProgress(progresso / 100.0);
                if (progresso == 25) loadingMessage.setText(mensagens[0]);
                if (progresso == 50) loadingMessage.setText(mensagens[1]);
                if (progresso == 75) loadingMessage.setText(mensagens[2]);
                if (progresso == 100) {
                    loadingMessage.setText(mensagens[3]);
                    finalizarLoading();
                }
            });
            loadingTimeline.getKeyFrames().add(kf);
        }
        loadingTimeline.play();
    }

    private String buildResumoSubtopicos() {
        if (subtopicosSelecionados.isEmpty()) {
            return "todos os subtopicos";
        }
        return subtopicosSelecionados.stream().limit(4).collect(Collectors.joining(", "));
    }

    private void finalizarLoading() {
        PauseTransition pause = new PauseTransition(Duration.millis(300));
        pause.setOnFinished(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(200), loadingOverlay);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(ev -> {
                loadingOverlay.setVisible(false);
                tela.setVisible(true);
                iniciarCronometro();
                carregarQuestao(0);
            });
            fadeOut.play();
        });
        pause.play();
    }

    private void iniciarCronometro() {
        segundos = 0;
        minutos = 0;
        cronometro = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            segundos++;
            if (segundos == 60) {
                minutos++;
                segundos = 0;
            }
            tempo.setText(String.format("%02d:%02d", minutos, segundos));
        }));
        cronometro.setCycleCount(Timeline.INDEFINITE);
        cronometro.play();
    }

    private void carregarQuestao(int index) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(150), tela);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        fadeOut.setOnFinished(e -> {
            atualizarConteudoQuestao(index);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(200), tela);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
        });

        fadeOut.play();
        tempoInicioQuestao = System.currentTimeMillis();
    }

    private void atualizarConteudoQuestao(int index) {
        Questao q = questoes.get(index);

        nPergunta.setText("Questao " + (index + 1) + " / " + totalQuestoes);
        bloco1.setText(q.getEnunciado());
        bloco2.setText(montarBlocoSecundario(q));

        ResA.setText(q.getOpcaoA());
        ResB.setText(q.getOpcaoB());
        ResC.setText(q.getOpcaoC());
        ResD.setText(q.getOpcaoD());
        ResE.setText(q.getOpcaoE());
        ResF.setText(q.getOpcaoF());
        ResG.setText(q.getOpcaoG());

        if (q.getImagem() != null) {
            imgBloco2.setImage(q.getImagem());
            imgBloco2.setVisible(true);
        } else {
            imgBloco2.setVisible(false);
        }

        alternativas.selectToggle(null);
        respostaSelecionada = '\0';
        feedbackContainer.setVisible(false);
        feedbackContainer.setOpacity(1);

        double progresso = totalQuestoes == 0 ? 0 : (double) (index + 1) / totalQuestoes;
        circleProgress.setValue(progresso);

        btnConfirmar.setDisable(false);
        btnProximo.setDisable(true);
    }

    private String montarBlocoSecundario(Questao questao) {
        String foco = "Topico: " + questao.getTopico() + " | Subtopico: " + questao.getSubtopico();
        if (questao.getBloco2() == null || questao.getBloco2().isBlank()) {
            return foco;
        }
        return questao.getBloco2() + "\n" + foco;
    }

    @FXML
    private void confirmarResposta() {
        if (alternativas.getSelectedToggle() == null) {
            mostrarAlerta("Atencao", "Selecione uma alternativa antes de confirmar.");
            return;
        }

        JFXToggleNode selected = (JFXToggleNode) alternativas.getSelectedToggle();
        respostaSelecionada = selected.getText().charAt(0);

        long tempoResposta = System.currentTimeMillis() - tempoInicioQuestao;
        temposResposta.add(tempoResposta);
        respostasUsuario.add(respostaSelecionada);

        Questao q = questoes.get(questaoAtual);
        boolean acertou = respostaSelecionada == q.getRespostaCorreta();

        if (acertou) {
            acertos++;
            sequenciaAcertos++;
            sequenciaErros = 0;
            acertosLabel.setText(String.valueOf(acertos));
            selected.setStyle("-fx-background-color: #10b981; -fx-border-color: #10b981; -fx-text-fill: white;");
            mostrarFeedbackAdaptativo(true, tempoResposta);
        } else {
            erros++;
            sequenciaErros++;
            sequenciaAcertos = 0;
            errosLabel.setText(String.valueOf(erros));
            selected.setStyle("-fx-background-color: #ef4444; -fx-border-color: #ef4444; -fx-text-fill: white;");
            mostrarFeedbackAdaptativo(false, tempoResposta);
            destacarRespostaCorreta(q.getRespostaCorreta());
        }

        ajustarNivelAdaptativo(acertou, tempoResposta);
        atualizarIndicadoresNivel();

        btnProximo.setDisable(false);
        btnConfirmar.setDisable(true);
    }

    private void mostrarFeedbackAdaptativo(boolean acertou, long tempoResposta) {
        boolean foiRapido = tempoResposta < 30000;
        feedbackContainer.setVisible(true);
        feedbackContainer.setOpacity(1);

        if (acertou && foiRapido) {
            feedbackIcon.setText("OK");
            feedbackMessage.setText("Excelente! Rapido e preciso. Vamos subir a exigencia.");
            feedbackContainer.setStyle("-fx-background-color: #ecfdf5; -fx-border-color: #10b981;");
        } else if (acertou) {
            feedbackIcon.setText("OK");
            feedbackMessage.setText("Correta! O foco continua nos mesmos subtopicos.");
            feedbackContainer.setStyle("-fx-background-color: #ecfdf5; -fx-border-color: #10b981;");
        } else if (tempoResposta > 60000) {
            feedbackIcon.setText("!");
            feedbackMessage.setText("Demorou bastante. Vou manter o foco e reduzir a pressao.");
            feedbackContainer.setStyle("-fx-background-color: #fef2f2; -fx-border-color: #ef4444;");
        } else {
            feedbackIcon.setText("X");
            feedbackMessage.setText("Errada. A resposta correta e " + questoes.get(questaoAtual).getRespostaCorreta() + ".");
            feedbackContainer.setStyle("-fx-background-color: #fef2f2; -fx-border-color: #ef4444;");
        }

        Timeline hideFeedback = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
            FadeTransition fade = new FadeTransition(Duration.millis(300), feedbackContainer);
            fade.setFromValue(1);
            fade.setToValue(0);
            fade.setOnFinished(ev -> feedbackContainer.setVisible(false));
            fade.play();
        }));
        hideFeedback.play();
    }

    private void ajustarNivelAdaptativo(boolean acertou, long tempoResposta) {
        int nivelAnterior = nivelAtualInt;
        boolean foiRapido = tempoResposta < 30000;

        if (acertou && foiRapido) {
            if (sequenciaAcertos >= 2 && nivelAtualInt < 4) {
                nivelAtualInt++;
            }
        } else if (acertou) {
            if (sequenciaAcertos >= 3 && nivelAtualInt < 4) {
                nivelAtualInt++;
            }
        } else if (tempoResposta > 60000) {
            if (nivelAtualInt > 1) {
                nivelAtualInt--;
            }
            sequenciaErros = 0;
        } else if (sequenciaErros >= 2 && nivelAtualInt > 1) {
            nivelAtualInt--;
            sequenciaErros = 0;
        }

        nivelAtualInt = Math.max(1, Math.min(nivelAtualInt, 4));
        if (nivelAtualInt != nivelAnterior) {
            System.out.println("Nivel alterado: " + nivelAnterior + " -> " + nivelAtualInt);
        }
    }

    private void atualizarIndicadoresNivel() {
        switch (nivelAtualInt) {
            case 1 -> {
                nivelAtual.setText("FACIL");
                dificuldadeAtual.setText("*");
                nivelAtual.setStyle("-fx-text-fill: #10b981;");
            }
            case 2 -> {
                nivelAtual.setText("MEDIO");
                dificuldadeAtual.setText("**");
                nivelAtual.setStyle("-fx-text-fill: #f59e0b;");
            }
            case 3 -> {
                nivelAtual.setText("DIFICIL");
                dificuldadeAtual.setText("***");
                nivelAtual.setStyle("-fx-text-fill: #ef4444;");
            }
            case 4 -> {
                nivelAtual.setText("EXPERT");
                dificuldadeAtual.setText("****");
                nivelAtual.setStyle("-fx-text-fill: #8b5cf6;");
            }
            default -> {
            }
        }
    }

    private void destacarRespostaCorreta(char letra) {
        JFXToggleNode correta = switch (letra) {
            case 'A' -> toggleA;
            case 'B' -> toggleB;
            case 'C' -> toggleC;
            case 'D' -> toggleD;
            case 'E' -> toggleE;
            case 'F' -> toggleF;
            case 'G' -> toggleG;
            default -> null;
        };
        if (correta != null) {
            correta.setStyle("-fx-background-color: #10b981; -fx-border-color: #10b981; -fx-text-fill: white;");
        }
    }

    @FXML
    private void proximaQuestao() {
        limparEstilosToggles();

        if (questaoAtual + 1 < totalQuestoes) {
            Questao proxima = obterProximaQuestao();
            if (proxima == null) {
                finalizarTesteAdaptativo();
                return;
            }
            questoes.add(proxima);
            questaoAtual++;
            carregarQuestao(questaoAtual);
        } else {
            finalizarTesteAdaptativo();
        }
    }

    private Questao obterProximaQuestao() {
        Set<String> idsUsados = questoes.stream()
            .map(Questao::getId)
            .collect(Collectors.toCollection(LinkedHashSet::new));

        return service.getProximaQuestaoAdaptativa(
            disciplinaSelecionada,
            nivelAtualInt,
            topicosSelecionados,
            subtopicosSelecionados,
            idsUsados
        );
    }

    private void limparEstilosToggles() {
        toggleA.setStyle("");
        toggleB.setStyle("");
        toggleC.setStyle("");
        toggleD.setStyle("");
        toggleE.setStyle("");
        toggleF.setStyle("");
        toggleG.setStyle("");
    }

    private void finalizarTesteAdaptativo() {
        if (cronometro != null) {
            cronometro.stop();
        }

        double mediaTempo = temposResposta.stream().mapToLong(Long::longValue).average().orElse(0);
        double porcentagemAcertos = totalQuestoes == 0 ? 0 : (acertos * 100.0) / totalQuestoes;
        String perfil = determinarPerfil(porcentagemAcertos, mediaTempo);
        String recomendacao = getRecomendacao(porcentagemAcertos, nivelAtualInt);
        List<QuestaoResultado> questoesResultado = construirQuestoesResultado();

        ResultadoAvaliacaoController.setResultado(
            new ResultadoPayload(
                "Exame Adaptativo",
                formatarDisciplina(disciplinaSelecionada),
                acertos,
                erros,
                totalQuestoes,
                porcentagemAcertos,
                tempo.getText(),
                getNomeNivel(nivelAtualInt),
                perfil,
                recomendacao,
                "views/pages/candidato/testes",
                questoesResultado
            )
        );

        StackPane contentHost = nomeDisc == null || nomeDisc.getScene() == null
            ? null
            : (StackPane) nomeDisc.getScene().lookup("#contentHost");
        if (contentHost != null) {
            App.swapContent(contentHost, "views/pages/candidato/resultado-avaliacao");
            return;
        }

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Teste Adaptativo Concluido");
        alert.setHeaderText("Resultado Final Adaptativo");
        alert.setContentText(String.format(
            "RESULTADOS:\n- Acertos: %d/%d (%.1f%%)\n- Nivel alcancado: %s\n- Tempo medio por questao: %.1f segundos\n- Perfil: %s\n\nRecomendacao: %s",
            acertos,
            totalQuestoes,
            porcentagemAcertos,
            getNomeNivel(nivelAtualInt),
            mediaTempo / 1000,
            perfil,
            recomendacao
        ));
        alert.showAndWait();
    }

    private String determinarPerfil(double porcentagem, double tempoMedio) {
        if (porcentagem >= 80 && tempoMedio < 30000) return "Rapido e preciso";
        if (porcentagem >= 80) return "Preciso mas lento";
        if (porcentagem >= 60 && tempoMedio < 30000) return "Seguro e agil";
        if (porcentagem >= 60) return "Cauteloso";
        if (porcentagem >= 40) return "Intermediario";
        return "Em consolidacao";
    }

    private String getNomeNivel(int nivel) {
        return switch (nivel) {
            case 1 -> "FACIL";
            case 2 -> "MEDIO";
            case 3 -> "DIFICIL";
            case 4 -> "EXPERT";
            default -> "MEDIO";
        };
    }

    private String getRecomendacao(double porcentagem, int nivel) {
        String foco = subtopicosSelecionados.isEmpty()
            ? "os subtopicos escolhidos"
            : subtopicosSelecionados.stream().limit(3).collect(Collectors.joining(", "));

        if (porcentagem >= 80) {
            return "Parabens! Voce esta pronto para desafios mais avancados em " + foco + ".";
        } else if (porcentagem >= 60) {
            return "Bom trabalho! Continue praticando " + foco + " para subir de nivel.";
        } else if (porcentagem >= 40) {
            return "Vamos melhorar! Foque nos subtopicos " + foco + ".";
        } else {
            return "Vale revisar os fundamentos de " + foco + " antes da proxima tentativa.";
        }
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private List<QuestaoResultado> construirQuestoesResultado() {
        List<QuestaoResultado> itens = new ArrayList<>();
        int limite = Math.min(questoes == null ? 0 : questoes.size(), respostasUsuario.size());
        for (int i = 0; i < limite; i++) {
            itens.add(QuestaoResultado.fromQuestao(i + 1, questoes.get(i), respostasUsuario.get(i)));
        }
        return itens;
    }

    private void resetarMetricas() {
        acertos = 0;
        erros = 0;
        sequenciaAcertos = 0;
        sequenciaErros = 0;
        respostasUsuario.clear();
        temposResposta.clear();
        questaoAtual = 0;
        respostaSelecionada = '\0';
        segundos = 0;
        minutos = 0;

        acertosLabel.setText("0");
        errosLabel.setText("0");
        tempo.setText("00:00");
        atualizarIndicadoresNivel();
        limparEstilosToggles();
        alternativas.selectToggle(null);
        btnConfirmar.setDisable(false);
        btnProximo.setDisable(true);
        if (circleProgress != null) {
            circleProgress.setValue(0);
        }
    }

    private String formatarDisciplina(String disciplina) {
        if (disciplina == null) {
            return "-";
        }

        return switch (disciplina) {
            case "MATEMATICA" -> "Matematica";
            case "FISICA" -> "Fisica";
            case "QUIMICA" -> "Quimica";
            case "BIOLOGIA" -> "Biologia";
            case "PORTUGUES" -> "Portugues";
            default -> disciplina;
        };
    }
}
