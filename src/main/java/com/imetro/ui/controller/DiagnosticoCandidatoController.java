package com.imetro.ui.controller;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.imetro.ui.components.CircleProgress;
import com.imetro.ui.model.Questao;
import com.imetro.services.TesteMatematicaService;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.PauseTransition;
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

public class DiagnosticoCandidatoController {
    
    @FXML private StackPane circleProgressContainer;
    @FXML private Label nomeDisc, nPergunta, bloco1, bloco2;
    @FXML private Label ResA, ResB, ResC, ResD, ResE, ResF, ResG;
    @FXML private JFXToggleNode toggleA, toggleB, toggleC, toggleD, toggleE, toggleF, toggleG;
    @FXML private ToggleGroup alternativas;
    @FXML private VBox end, start;
    @FXML private ImageView imgBloco2;
    @FXML private JFXButton btnConfirmar, btnProximo;
    @FXML private StackPane loadingOverlay;
    @FXML private ProgressBar loadingProgress;
    @FXML private Label loadingMessage;
    @FXML private VBox tela;
    private int h=0,m=0,s=0,a=0,e=0;
    @FXML
    private Label corretas;
    @FXML
    private Label errada;
    @FXML
    private Label tempo;

    private JFXToggleNode selected;
    private char correta;
    private boolean diagnostico;
    private Timeline time;
    
    private CircleProgress circleProgress;
    private List<Questao> questoes;
    private int questaoAtual = 0;
    private int totalQuestoes;
    private char respostaSelecionada;
    private List<Character> respostasUsuario = new ArrayList<Character>();
    private Timeline loadingTimeline;
    
    @FXML
    public void initialize() {
        // Criar CircleProgress
        circleProgress = new CircleProgress(35, 35, 35, 0);
        circleProgressContainer.getChildren().add(circleProgress);
        
        // Carregar questões
        TesteMatematicaService service = new TesteMatematicaService();
        questoes = service.carregarQuestoes();
        totalQuestoes = questoes.size();
        
        // Iniciar com loading e depois primeira questão
        Diagnosticar(true);
        iniciarLoadingInicial();
    }

    private void TimerDiagnostic(){
        diagnostico=true;
        
        
        time=new Timeline(new KeyFrame(
            Duration.seconds(1),
            e-> {
              
                    if(s==60){
                        m++;
                        if (m==60) {
                            h++;
                            m=0;
                        }
                        s=0;
                    }   
                    tempo.setText(LocalTime.of(h, m, s).toString()+(s==0?":00":""));    
                    s++;
                    
                }
              
        ));
       time.setCycleCount(Timeline.INDEFINITE);
        time.play();
        
    }
    
    private void iniciarLoadingInicial() {
        tela.setVisible(false);
        // Mostrar overlay
        loadingOverlay.setVisible(true);
        loadingOverlay.setOpacity(1);
        
        // Animar progress bar de 0 a 100%
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
            KeyFrame kf = new KeyFrame(Duration.millis(i * 30), e -> {
                loadingProgress.setProgress(progresso / 100.0);
                
                // Mudar mensagem a cada 25%
                if (progresso == 25) loadingMessage.setText(mensagens[0]);
                if (progresso == 50) loadingMessage.setText(mensagens[1]);
                if (progresso == 75) loadingMessage.setText(mensagens[2]);
                if (progresso == 95) loadingMessage.setText(mensagens[3]);
                
                // Quando completar
                if (progresso == 100) {
                    finalizarLoading();
                }
            });
            loadingTimeline.getKeyFrames().add(kf);
        }
        
        loadingTimeline.play();
    }
    
    private void finalizarLoading() {
        // Pequena pausa antes de esconder
        PauseTransition pause = new PauseTransition(Duration.millis(500));
        pause.setOnFinished(e -> {
            // Fade out do overlay
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), loadingOverlay);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(ev -> {
                loadingOverlay.setVisible(false);
                // Iniciar primeira questão
                tela.setVisible(true);
                TimerDiagnostic();
                carregarQuestao(0);
            });
            fadeOut.play();
        });
        pause.play();
    }
     
    private void carregarQuestao(int index) {
        // Animação de fade out/in
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), end);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        
        fadeOut.setOnFinished(e -> {
            // Atualizar conteúdo
            atualizarConteudoQuestao(index);
            
            // Fade in
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), end);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
        });
        
        fadeOut.play();
    }

    private void atualizarConteudoQuestao(int index) {
        Questao q = questoes.get(index);
        
        // Atualizar header
        nomeDisc.setText(q.getDisciplina());
        nPergunta.setText("Questão " + (index + 1) + " / " + totalQuestoes);
        
        // Atualizar enunciado
        bloco1.setText(q.getEnunciado());
        bloco2.setText(q.getBloco2());
        
        // Atualizar alternativas
        ResA.setText(q.getOpcaoA());
        ResB.setText(q.getOpcaoB());
        ResC.setText(q.getOpcaoC());
        ResD.setText(q.getOpcaoD());
        ResE.setText(q.getOpcaoE());
        ResF.setText(q.getOpcaoF());
        ResG.setText(q.getOpcaoG());
        
        // Atualizar texto dos toggles
        toggleA.setText("A");
        toggleB.setText("B");
        toggleC.setText("C");
        toggleD.setText("D");
        toggleE.setText("E");
        toggleF.setText("F");
        toggleG.setText("G");
        
        // Carregar imagem se existir
        if (q.getImagem() != null) {
            imgBloco2.setImage(q.getImagem());
            imgBloco2.setVisible(true);
        } else {
            imgBloco2.setVisible(false);
        }
        
        // Limpar seleção anterior
        alternativas.selectToggle(null);
        respostaSelecionada = '\0';
        
        // Atualizar CircleProgress
        double progresso = (double) (index + 1) / totalQuestoes;
        circleProgress.setValue(progresso);
        
        // Reabilitar botões
        btnConfirmar.setDisable(false);
        btnProximo.setDisable(true);
    }
    
    @FXML
    private void confirmarResposta() {
        if (alternativas.getSelectedToggle() == null) {
            mostrarAlerta("Atenção", "Selecione uma alternativa antes de confirmar.");
            return;
        }
        
        JFXToggleNode selected = (JFXToggleNode) alternativas.getSelectedToggle();
        respostaSelecionada = selected.getText().charAt(0);
        
        // Salvar resposta
        respostasUsuario.add(respostaSelecionada);
        
        // Verificar se acertou
        Questao q = questoes.get(questaoAtual);
        boolean acertou = (respostaSelecionada == q.getRespostaCorreta());
        this.selected=selected;
        // Feedback visual no toggle
        if (acertou) {
            a++;
            selected.setStyle("-fx-background-color: #10b981; -fx-border-color: #10b981; -fx-text-fill: white;");
            mostrarMensagemTemporaria("Correta!");
        } else {
            e++;
            selected.setStyle("-fx-background-color: #ef4444; -fx-border-color: #ef4444; -fx-text-fill: white;");
            mostrarMensagemTemporaria("Errada! Resposta correta: " + q.getRespostaCorreta());
            
            // Destacar resposta correta (opcional)
            destacarRespostaCorreta(q.getRespostaCorreta());
        }
        
        // Habilitar botão próximo
        btnProximo.setDisable(false);
        corretas.setText(a+"");
        errada.setText(e+"");
        btnConfirmar.setDisable(true);
    }
    
    private void destacarRespostaCorreta(char letra) {
        this.correta=letra;
        JFXToggleNode correta = null;
        switch(letra) {
            case 'A': correta = toggleA; break;
            case 'B': correta = toggleB; break;
            case 'C': correta = toggleC; break;
            case 'D': correta = toggleD; break;
        }
        if (correta != null) {
            correta.setStyle("-fx-background-color: #10b981; -fx-border-color: #10b981; -fx-text-fill: white;");
        }
    }

    private void removerDestaqueRespostaCorreta(char letra) {
        JFXToggleNode correta = null;
        switch(letra) {
            case 'A': correta = toggleA; break;
            case 'B': correta = toggleB; break;
            case 'C': correta = toggleC; break;
            case 'D': correta = toggleD; break;
        }
        if (correta != null) {
            correta.setStyle("");
        }
    }
    
    @FXML
    private void proximaQuestao() {
        this.selected.setStyle("");
        removerDestaqueRespostaCorreta(correta);
        if (questaoAtual + 1 < totalQuestoes) {
            questaoAtual++;
            carregarQuestao(questaoAtual);
        } else {
            finalizarDiagnostico();
        }
    }

    private void finalizarDiagnostico() {
        // Calcular pontuação
       time.stop();
        int acertos = 0;
        for (int i = 0; i < questoes.size(); i++) {
            if (respostasUsuario.get(i) == questoes.get(i).getRespostaCorreta()) {
                acertos++;
            }
        }
        
        double porcentagem = (acertos * 100.0) / totalQuestoes;
        
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(" Diagnóstico Concluído");
        alert.setHeaderText("Resultado Final");
        alert.setContentText(String.format(
            "Você acertou %d de %d questões\n\nPorcentagem: %.1f%%\n\n🎓 Nível: %s\n\n%s",
            acertos, totalQuestoes, porcentagem,
            getNivelPorPorcentagem(porcentagem),
            getMensagemMotivacional(porcentagem)
        ));
        alert.showAndWait();
        
        // Limpar e voltar
        respostasUsuario.clear();
        questaoAtual = 0;
        Diagnosticar(false);
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
    
    private void mostrarMensagemTemporaria(String mensagem) {
        System.out.println(mensagem);
        // TODO: Implementar Toast notification
    }
    
    private void Diagnosticar(boolean iniciar) {
        end.setVisible(iniciar);
        start.setVisible(!iniciar);
    }
}