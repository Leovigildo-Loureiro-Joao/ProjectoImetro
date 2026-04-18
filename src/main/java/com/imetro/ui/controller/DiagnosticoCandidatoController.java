package com.imetro.ui.controller;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.imetro.services.TesteMatematicaService;
import com.imetro.ui.components.CircleProgress;
import com.imetro.ui.components.DiagnosticoCard;
import com.imetro.ui.model.Questao;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleNode;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class DiagnosticoCandidatoController {

    @FXML
    private Label ResA;

    @FXML
    private Label ResB;

    @FXML
    private Label ResC;

    @FXML
    private Label ResD;

    @FXML
    private Label ResE;

    @FXML
    private Label ResF;

    @FXML
    private Label ResG;

    @FXML
    private ToggleGroup alternativas;

    @FXML
    private Label bloco1;

    @FXML
    private Label bloco2;

    @FXML
    private JFXButton btnConfirmar;

    @FXML
    private JFXButton btnDiagnosticos;

    @FXML
    private JFXButton btnEstatisticas;

    @FXML
    private JFXButton btnProximo;

    @FXML
    private JFXButton btnTimeline;

    @FXML
    private StackPane circleProgressContainer;

    @FXML
    private HBox containerPrincipal;

    @FXML
    private Label corretas;

    @FXML
    private VBox detalhamentoDisciplinas;

    @FXML
    private FlowPane diagnosticosPane;

    @FXML
    private VBox end;

    @FXML
    private Label errada;

    @FXML
    private VBox estatisticasPane;

    @FXML
    private ImageView imgBloco2;

    @FXML
    private Label lblDisciplinaLenta;

    @FXML
    private Label lblDisciplinaRapida;

    @FXML
    private Label lblMediaGeral;

    @FXML
    private Label lblMelhorDisciplina;

    @FXML
    private Label lblMelhorPontuacao;

    @FXML
    private Label lblPiorDisciplina;

    @FXML
    private Label lblPiorPontuacao;

    @FXML
    private Label lblTaxaAcerto;

    @FXML
    private Label lblTempoMaisLento;

    @FXML
    private Label lblTempoMaisRapido;

    @FXML
    private Label lblTempoMedio;

    @FXML
    private Label lblTotalAcertos;

    @FXML
    private Label lblTotalErros;

    @FXML
    private Label lblTotalQuestoes;

    @FXML
    private Label lblTotalTestes;

    @FXML
    private Label loadingMessage;

    @FXML
    private StackPane loadingOverlay;

    @FXML
    private ProgressBar loadingProgress;

    @FXML
    private Label nPergunta;

    @FXML
    private Label nomeDisc;

    @FXML
    private ProgressBar progressMediaGeral;

    @FXML
    private ProgressBar progressTaxaAcerto;

    @FXML
    private ScrollPane scroll;

    @FXML
    private HBox start;

    @FXML
    private VBox tela;

    @FXML
    private Label tempo;

    @FXML
    private VBox timelineContent;

    @FXML
    private VBox timelinePane;

    @FXML
    private JFXToggleNode toggleA;

    @FXML
    private JFXToggleNode toggleB;

    @FXML
    private JFXToggleNode toggleC;

    @FXML
    private JFXToggleNode toggleD;

    @FXML
    private JFXToggleNode toggleE;

    @FXML
    private JFXToggleNode toggleF;

    @FXML
    private JFXToggleNode toggleG;

    private int h = 0, m = 0, s = 0, a = 0, e = 0;

    private JFXToggleNode selected;
    private char corretaLetra;
    private Timeline time;

    private CircleProgress circleProgress;
    private List<Questao> questoes;
    private int questaoAtual = 0;
    private int totalQuestoes;
    private char respostaSelecionada;
    private List<Character> respostasUsuario = new ArrayList<>();
    private Timeline loadingTimeline;

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            // Criar CircleProgress
            circleProgress = new CircleProgress(35, 35, 35, 0);
            circleProgressContainer.getChildren().add(circleProgress);

            // Carregar questões
            TesteMatematicaService service = new TesteMatematicaService();
            questoes = service.carregarQuestoes();
            totalQuestoes = questoes.size();

            // Configurar visibilidade inicial
            end.setVisible(false);
            start.setVisible(true);
            tela.setVisible(true);
            
            // Carregar diagnósticos
            BuscarDiagnosticos();
            
            // Configurar scroll horizontal
            scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        });
    }

    private void TimerDiagnostic() {
        time = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
            s++;
            if (s == 60) {
                s = 0;
                m++;
                if (m == 60) {
                    m = 0;
                    h++;
                }
            }
            tempo.setText(String.format("%02d:%02d:%02d", h, m, s));
        }));
        time.setCycleCount(Timeline.INDEFINITE);
        time.play();
    }

    private void iniciarLoadingInicial() {
        tela.setVisible(false);
        loadingOverlay.setVisible(true);
        loadingOverlay.setOpacity(1);
        loadingProgress.setProgress(0);

        String[] mensagens = {
            "Analisando seu perfil...",
            "Preparando questões personalizadas...",
            "Configurando nível de dificuldade...",
            "Quase lá..."
        };

        loadingTimeline = new Timeline();
        
        for (int i = 0; i <= 100; i++) {
            final int progresso = i;
            KeyFrame kf = new KeyFrame(Duration.millis(i * 25), ev -> {
                loadingProgress.setProgress(progresso / 100.0);
                
                if (progresso == 25) loadingMessage.setText(mensagens[0]);
                if (progresso == 50) loadingMessage.setText(mensagens[1]);
                if (progresso == 75) loadingMessage.setText(mensagens[2]);
                if (progresso == 95) loadingMessage.setText(mensagens[3]);
                
                if (progresso == 100) {
                    finalizarLoading();
                }
            });
            loadingTimeline.getKeyFrames().add(kf);
        }
        
        loadingTimeline.play();
    }

    private void finalizarLoading() {
        PauseTransition pause = new PauseTransition(Duration.millis(300));
        pause.setOnFinished(ev -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(200), loadingOverlay);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(e -> {
                loadingOverlay.setVisible(false);
                tela.setVisible(true);
                TimerDiagnostic();
                carregarQuestao(0);
            });
            fadeOut.play();
        });
        pause.play();
    }

    private void carregarQuestao(int index) {
        atualizarConteudoQuestao(index);
    }

    private void atualizarConteudoQuestao(int index) {
        if (index >= questoes.size()) return;
        
        Questao q = questoes.get(index);

        nomeDisc.setText(q.getDisciplina());
        nPergunta.setText("Questão " + (index + 1) + " / " + totalQuestoes);

        bloco1.setText(q.getEnunciado());
        bloco2.setText(q.getBloco2() != null ? q.getBloco2() : "");

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

        double progresso = (double) (index + 1) / totalQuestoes;
        circleProgress.setValue(progresso);

        btnConfirmar.setDisable(false);
        btnProximo.setDisable(true);
    }

    @FXML
    void confirmarResposta(ActionEvent event) {
        if (alternativas.getSelectedToggle() == null) {
            mostrarAlerta("Atenção", "Selecione uma alternativa antes de confirmar.");
            return;
        }

        selected = (JFXToggleNode) alternativas.getSelectedToggle();
        respostaSelecionada = selected.getText().charAt(0);
        respostasUsuario.add(respostaSelecionada);

        Questao q = questoes.get(questaoAtual);
        boolean acertou = (respostaSelecionada == q.getRespostaCorreta());

        if (acertou) {
            a++;
            selected.getStyleClass().add("sucess");
        } else {
            e++;
            selected.getStyleClass().add("error");
            destacarRespostaCorreta(q.getRespostaCorreta());
        }

        btnProximo.setDisable(false);
        corretas.setText(String.valueOf(a));
        errada.setText(String.valueOf(e));
        btnConfirmar.setDisable(true);
    }

    private void destacarRespostaCorreta(char letra) {
        this.corretaLetra = letra;
        JFXToggleNode corretaNode = getToggleByLetra(letra);
        if (corretaNode != null) {
            corretaNode.getStyleClass().add("sucess");
        }
    }

    private void removerDestaqueRespostaCorreta() {
        JFXToggleNode corretaNode = getToggleByLetra(corretaLetra);
        if (corretaNode != null) {
            corretaNode.getStyleClass().remove("sucess");
        }
    }
    
    private JFXToggleNode getToggleByLetra(char letra) {
        switch (letra) {
            case 'A': return toggleA;
            case 'B': return toggleB;
            case 'C': return toggleC;
            case 'D': return toggleD;
            case 'E': return toggleE;
            case 'F': return toggleF;
            case 'G': return toggleG;
            default: return null;
        }
    }

    @FXML
    void proximaQuestao(ActionEvent event) {
        if (selected != null) {
            selected.getStyleClass().removeAll("error", "sucess");
        }
        removerDestaqueRespostaCorreta();
        
        if (questaoAtual + 1 < totalQuestoes) {
            questaoAtual++;
            carregarQuestao(questaoAtual);
        } else {
            finalizarDiagnostico();
        }
    }

    private void finalizarDiagnostico() {
        if (time != null) {
            time.stop();
        }
        
        int acertos = 0;
        for (int i = 0; i < questoes.size(); i++) {
            if (respostasUsuario.get(i) == questoes.get(i).getRespostaCorreta()) {
                acertos++;
            }
        }

        double porcentagem = (acertos * 100.0) / totalQuestoes;

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Diagnóstico Concluído");
        alert.setHeaderText("Resultado Final");
        alert.setContentText(String.format(
            "Você acertou %d de %d questões\n\nPorcentagem: %.1f%%\n\n🎓 Nível: %s\n\n%s",
            acertos, totalQuestoes, porcentagem,
            getNivelPorPorcentagem(porcentagem),
            getMensagemMotivacional(porcentagem)
        ));
        alert.showAndWait();

        // Reset
        respostasUsuario.clear();
        questaoAtual = 0;
        a = 0;
        e = 0;
        h = 0;
        m = 0;
        s = 0;
        corretas.setText("0");
        errada.setText("0");
        tempo.setText("00:00:00");
        
        end.setVisible(false);
        start.setVisible(true);
    }

    private String getNivelPorPorcentagem(double pct) {
        if (pct >= 80) return "Scholarship Ready";
        if (pct >= 60) return "Avançado";
        if (pct >= 40) return "Intermediário";
        if (pct >= 20) return "ISAF";
        return "INAF";
    }

    private String getMensagemMotivacional(double pct) {
        if (pct >= 80) return "Parabéns! Você está pronto para bolsas de estudo!";
        if (pct >= 60) return "Bom trabalho! Continue praticando para alcançar o próximo nível.";
        if (pct >= 40) return "Vamos melhorar! Foque nos pontos fracos identificados.";
        if (pct >= 20) return "Você precisa de mais prática. Não desista!";
        return "Vamos recomeçar? O diagnóstico identificou áreas para melhoria.";
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void BuscarDiagnosticos() {
        diagnosticosPane.getChildren().clear();
        for (int i = 0; i < 5; i++) {
            final int index = i;
            diagnosticosPane.getChildren().add(new DiagnosticoCard(
                "Matemática", 
                "-6%", 
                50, 
                () -> Diagnosticar(true)
            ));
        }
    }

    private void Diagnosticar(boolean iniciar) {
        end.setVisible(iniciar);
        start.setVisible(!iniciar);
        if (iniciar) {
            iniciarLoadingInicial();
        }
    }

    private void SwitchPane(int i) {
        diagnosticosPane.setVisible(i == 0);
        timelinePane.setVisible(i == 1);
        estatisticasPane.setVisible(i == 2);
        
        btnDiagnosticos.getStyleClass().clear();
        btnTimeline.getStyleClass().clear();
        btnEstatisticas.getStyleClass().clear();
        
        btnDiagnosticos.getStyleClass().add(i == 0 ? "nav-btn-active" : "nav-btn");
        btnTimeline.getStyleClass().add(i == 1 ? "nav-btn-active" : "nav-btn");
        btnEstatisticas.getStyleClass().add(i == 2 ? "nav-btn-active" : "nav-btn");
        
        // Scroll horizontal
        double target = 0.0;
        if (i == 0) target = 0.0;
        else if (i == 1) target = 0.5;
        else target = 1.0;
        
        Timeline l = new Timeline(
            new KeyFrame(Duration.seconds(0.3), new KeyValue(scroll.hvalueProperty(), target))
        );
        l.play();
    }

    @FXML
    void switchToDiagnosticos(ActionEvent event) {
        SwitchPane(0);
    }

    @FXML
    void switchToEstatisticas(ActionEvent event) {
        SwitchPane(2);
    }

    @FXML
    void switchToTimeline(ActionEvent event) {
        SwitchPane(1);
    }
}