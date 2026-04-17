package com.imetro.ui.controller;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.imetro.ui.components.CircleProgress;
import com.imetro.ui.model.Questao;
import com.imetro.services.TesteAdaptativoService;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleNode;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class TesteAdaptativoController {
    
    // Componentes FXML
    @FXML private StackPane circleProgressContainer;
    @FXML private Label nomeDisc, nPergunta, bloco1, bloco2;
    @FXML private Label ResA, ResB, ResC, ResD, ResE, ResF, ResG;
    @FXML private JFXToggleNode toggleA, toggleB, toggleC, toggleD, toggleE, toggleF, toggleG;
    @FXML private ToggleGroup alternativas;
    @FXML private VBox testeContainer, start, tela;
    @FXML private VBox disciplinasContainer;
    @FXML private ImageView imgBloco2;
    @FXML private JFXButton btnConfirmar, btnProximo;
    @FXML private StackPane loadingOverlay;
    @FXML private ProgressBar loadingProgress;
    @FXML private Label loadingMessage;
    
    // Indicadores adaptativos
    @FXML private Label nivelAtual;
    @FXML private Label dificuldadeAtual;
    @FXML private Label tempo;
    @FXML private Label acertosLabel;
    @FXML private Label errosLabel;
    @FXML private VBox feedbackContainer;
    @FXML private Label feedbackIcon;
    @FXML private Label feedbackMessage;
    
    // Componentes visuais
    private CircleProgress circleProgress;
    
    // Dados do teste
    private List<Questao> questoes;
    private int questaoAtual = 0;
    private int totalQuestoes = 10;
    private char respostaSelecionada;
    private List<Character> respostasUsuario = new ArrayList<>();
    private List<Long> temposResposta = new ArrayList<>();
    
    // Metricas adaptativas
    private int nivelAtualInt = 2; // 1=FACIL, 2=MEDIO, 3=DIFICIL, 4=EXPERT
    private int acertos = 0;
    private int erros = 0;
    private int sequenciaAcertos = 0;
    private int sequenciaErros = 0;
    private long tempoInicioQuestao;
    
    // Cronometro
    private Timeline cronometro;
    private int segundos = 0;
    private int minutos = 0;
    
    // Service
    private TesteAdaptativoService service;
    
    @FXML
    public void initialize() {
        // Criar CircleProgress
        circleProgress = new CircleProgress(35, 35, 35, 0);
        circleProgressContainer.getChildren().add(circleProgress);
        
        // Inicializar service
        service = new TesteAdaptativoService();
        
        // Carregar disciplinas disponiveis
        carregarDisciplinas();
        
        // Start visivel, teste invisivel
        testeContainer.setVisible(false);
        start.setVisible(true);
    }
    
    private void carregarDisciplinas() {
        // Botoes para cada disciplina disponivel
        JFXButton btnMatematica = new JFXButton("MATEMATICA");
        btnMatematica.getStyleClass().add("btn-primary");
        btnMatematica.setPrefWidth(200);
        btnMatematica.setOnAction(e -> iniciarTesteAdaptativo("MATEMATICA"));
        
        JFXButton btnPortugues = new JFXButton("PORTUGUES (Em breve)");
        btnPortugues.getStyleClass().add("btn-secondary");
        btnPortugues.setPrefWidth(200);
        btnPortugues.setDisable(true);
        
        disciplinasContainer.getChildren().addAll(btnMatematica, btnPortugues);
    }
    
    private void iniciarTesteAdaptativo(String disciplina) {
        this.nomeDisc.setText(disciplina);
        
        // Carregar questoes adaptativas (inicia no nivel medio)
        questoes = service.carregarQuestoesAdaptativas(disciplina, nivelAtualInt);
        totalQuestoes = questoes.size();
        
        // Resetar metricas
        acertos = 0;
        erros = 0;
        sequenciaAcertos = 0;
        sequenciaErros = 0;
        respostasUsuario.clear();
        temposResposta.clear();
        questaoAtual = 0;
        
        // Resetar UI
        acertosLabel.setText("0");
        errosLabel.setText("0");
        atualizarIndicadoresNivel();
        
        // Iniciar com loading
        start.setVisible(false);
        testeContainer.setVisible(true);
        iniciarLoading();
    }
    
    private void iniciarLoading() {
        tela.setVisible(false);
        loadingOverlay.setVisible(true);
        loadingOverlay.setOpacity(1);
        loadingProgress.setProgress(0);
        
        String[] mensagens = {
            "Analisando seu perfil...",
            "Ajustando nivel de dificuldade...",
            "Preparando questoes personalizadas...",
            "Teste adaptativo pronto!"
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
        // Animacao fade
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
        
        // Registrar tempo de inicio
        tempoInicioQuestao = System.currentTimeMillis();
    }
    
    private void atualizarConteudoQuestao(int index) {
        Questao q = questoes.get(index);
        
        // Header
        nPergunta.setText("Questao " + (index + 1));
        
        // Enunciado
        bloco1.setText(q.getEnunciado());
        bloco2.setText(q.getBloco2() != null ? q.getBloco2() : "");
        
        // Alternativas
        ResA.setText(q.getOpcaoA());
        ResB.setText(q.getOpcaoB());
        ResC.setText(q.getOpcaoC());
        ResD.setText(q.getOpcaoD());
        ResE.setText(q.getOpcaoE());
        ResF.setText(q.getOpcaoF());
        ResG.setText(q.getOpcaoG());
        
        // Imagem
        if (q.getImagem() != null) {
            imgBloco2.setImage(q.getImagem());
            imgBloco2.setVisible(true);
        } else {
            imgBloco2.setVisible(false);
        }
        
        // Limpar selecao
        alternativas.selectToggle(null);
        respostaSelecionada = '\0';
        
        // Esconder feedback anterior
        feedbackContainer.setVisible(false);
        
        // Atualizar CircleProgress
        double progresso = (double) (index + 1) / totalQuestoes;
        circleProgress.setValue(progresso);
        
        // Botoes
        btnConfirmar.setDisable(false);
        btnProximo.setDisable(true);
    }
    
    @FXML
    private void confirmarResposta() {
        if (alternativas.getSelectedToggle() == null) {
            mostrarAlerta("Atencao", "Selecione uma alternativa antes de confirmar.");
            return;
        }
        
        JFXToggleNode selected = (JFXToggleNode) alternativas.getSelectedToggle();
        respostaSelecionada = selected.getText().charAt(0);
        
        // Calcular tempo de resposta
        long tempoResposta = System.currentTimeMillis() - tempoInicioQuestao;
        temposResposta.add(tempoResposta);
        
        // Salvar resposta
        respostasUsuario.add(respostaSelecionada);
        
        // Verificar acerto
        Questao q = questoes.get(questaoAtual);
        boolean acertou = (respostaSelecionada == q.getRespostaCorreta());
        
        // Feedback visual
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
            
            // Destacar resposta correta
            destacarRespostaCorreta(q.getRespostaCorreta());
        }
        
        // AJUSTAR NIVEL ADAPTATIVO
        ajustarNivelAdaptativo(acertou, tempoResposta);
        
        // Atualizar indicadores na UI
        atualizarIndicadoresNivel();
        
        // Habilitar proximo
        btnProximo.setDisable(false);
        btnConfirmar.setDisable(true);
    }
    
    private void mostrarFeedbackAdaptativo(boolean acertou, long tempoResposta) {
        boolean foiRapido = tempoResposta < 30000; // menos de 30 segundos
        
        feedbackContainer.setVisible(true);
        
        if (acertou && foiRapido) {
            feedbackIcon.setText("OK");
            feedbackMessage.setText("Excelente! Rapido e preciso. Subindo de nivel...");
            feedbackContainer.setStyle("-fx-background-color: #ecfdf5; -fx-border-color: #10b981;");
        } else if (acertou) {
            feedbackIcon.setText("OK");
            feedbackMessage.setText("Correta! Bom trabalho.");
            feedbackContainer.setStyle("-fx-background-color: #ecfdf5; -fx-border-color: #10b981;");
        } else if (!acertou && tempoResposta > 60000) {
            feedbackIcon.setText("!"); // ALERTA
            feedbackMessage.setText("Demorou muito e errou. Vamos revisar conceitos basicos...");
            feedbackContainer.setStyle("-fx-background-color: #fef2f2; -fx-border-color: #ef4444;");
        } else {
            feedbackIcon.setText("X");
            feedbackMessage.setText("Errada! A resposta correta eh " + questoes.get(questaoAtual).getRespostaCorreta());
            feedbackContainer.setStyle("-fx-background-color: #fef2f2; -fx-border-color: #ef4444;");
        }
        
        // Auto-esconder apos 3 segundos
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
            // Acertou rapido: sobe mais rapido
            if (sequenciaAcertos >= 2 && nivelAtualInt < 4) {
                nivelAtualInt++;
            } else if (sequenciaAcertos >= 1 && nivelAtualInt < 4 && Math.random() > 0.5) {
                nivelAtualInt++;
            }
        } else if (acertou) {
            // Acertou devagar: sobe devagar
            if (sequenciaAcertos >= 3 && nivelAtualInt < 4) {
                nivelAtualInt++;
            }
        } else if (!acertou && tempoResposta > 60000) {
            // Errou muito lento: desce forte
            if (sequenciaErros >= 1 && nivelAtualInt > 1) {
                nivelAtualInt--;
                sequenciaErros = 0;
            }
        } else if (!acertou) {
            // Errou rapido: desce devagar
            if (sequenciaErros >= 2 && nivelAtualInt > 1) {
                nivelAtualInt--;
                sequenciaErros = 0;
            }
        }
        
        // Limitar niveis
        nivelAtualInt = Math.max(1, Math.min(nivelAtualInt, 4));
        
        // Se mudou de nivel, recarregar proximas questoes
        if (nivelAtualInt != nivelAnterior) {
            System.out.println("Nivel alterado: " + nivelAnterior + " -> " + nivelAtualInt);
            // As proximas questoes virao do novo nivel
        }
    }
    
    private void atualizarIndicadoresNivel() {
        switch(nivelAtualInt) {
            case 1:
                nivelAtual.setText("FACIL");
                dificuldadeAtual.setText("*");
                nivelAtual.setStyle("-fx-text-fill: #10b981;");
                break;
            case 2:
                nivelAtual.setText("MEDIO");
                dificuldadeAtual.setText("**");
                nivelAtual.setStyle("-fx-text-fill: #f59e0b;");
                break;
            case 3:
                nivelAtual.setText("DIFICIL");
                dificuldadeAtual.setText("***");
                nivelAtual.setStyle("-fx-text-fill: #ef4444;");
                break;
            case 4:
                nivelAtual.setText("EXPERT");
                dificuldadeAtual.setText("****");
                nivelAtual.setStyle("-fx-text-fill: #8b5cf6;");
                break;
        }
    }
    
    private void destacarRespostaCorreta(char letra) {
        JFXToggleNode correta = null;
        switch(letra) {
            case 'A': correta = toggleA; break;
            case 'B': correta = toggleB; break;
            case 'C': correta = toggleC; break;
            case 'D': correta = toggleD; break;
            case 'E': correta = toggleE; break;
            case 'F': correta = toggleF; break;
            case 'G': correta = toggleG; break;
        }
        if (correta != null) {
            correta.setStyle("-fx-background-color: #10b981; -fx-border-color: #10b981; -fx-text-fill: white;");
        }
    }
    
    @FXML
    private void proximaQuestao() {
        // Limpar estilos dos toggles
        limparEstilosToggles();
        
        if (questaoAtual + 1 < totalQuestoes) {
            questaoAtual++;
            // Buscar proxima questao no nivel atual
            Questao proxima = service.getProximaQuestaoAdaptativa(nivelAtualInt);
            if (proxima != null && questoes.size() > questaoAtual) {
                questoes.set(questaoAtual, proxima);
            }
            carregarQuestao(questaoAtual);
        } else {
            finalizarTesteAdaptativo();
        }
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
        
        // Calcular metricas finais
        double mediaTempo = temposResposta.stream().mapToLong(Long::longValue).average().orElse(0);
        double porcentagemAcertos = (acertos * 100.0) / totalQuestoes;
        
        // Determinar perfil do usuario
        String perfil = determinarPerfil(porcentagemAcertos, mediaTempo);
        
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Teste Adaptativo Concluido");
        alert.setHeaderText("Resultado Final Adaptativo");
        alert.setContentText(String.format(
            "RESULTADOS:\n" +
            "- Acertos: %d/%d (%.1f%%)\n" +
            "- Nivel alcanado: %s\n" +
            "- Tempo medio por questao: %.1f segundos\n" +
            "- Perfil: %s\n\n" +
            "Recomendacao: %s",
            acertos, totalQuestoes, porcentagemAcertos,
            getNomeNivel(nivelAtualInt),
            mediaTempo / 1000,
            perfil,
            getRecomendacao(porcentagemAcertos, nivelAtualInt)
        ));
        alert.showAndWait();
        
        // Voltar para tela de selecao
        respostasUsuario.clear();
        questaoAtual = 0;
        testeContainer.setVisible(false);
        start.setVisible(true);
    }
    
    private String determinarPerfil(double porcentagem, double tempoMedio) {
        if (porcentagem >= 80 && tempoMedio < 30000) return "Rapido e Preciso";
        if (porcentagem >= 80) return "Preciso mas Lento";
        if (porcentagem >= 60 && tempoMedio < 30000) return "Agressivo (erra pouco, acerta rapido)";
        if (porcentagem >= 60) return "Cauteloso";
        if (porcentagem >= 40) return "Intermediario";
        return "Iniciante";
    }
    
    private String getNomeNivel(int nivel) {
        switch(nivel) {
            case 1: return "FACIL";
            case 2: return "MEDIO";
            case 3: return "DIFICIL";
            case 4: return "EXPERT";
            default: return "MEDIO";
        }
    }
    
    private String getRecomendacao(double porcentagem, int nivel) {
        if (porcentagem >= 80) {
            return "Parabens! Voce esta pronto para desafios avancados.";
        } else if (porcentagem >= 60) {
            return "Bom trabalho! Continue praticando para alcancar o proximo nivel.";
        } else if (porcentagem >= 40) {
            return "Vamos melhorar! Foque nos topicos que errou.";
        } else {
            return "Que tal revisar os fundamentos antes de tentar novamente?";
        }
    }
    
    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}