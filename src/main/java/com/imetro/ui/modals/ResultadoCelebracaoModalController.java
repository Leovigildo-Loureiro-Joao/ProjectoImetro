package com.imetro.ui.modals;

import com.imetro.ui.components.CircleProgress;
import com.imetro.util.ResultadoCelebracaoSupport.CelebrationSummary;
import com.jfoenix.controls.JFXButton;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class ResultadoCelebracaoModalController extends ModalController {

    @FXML
    private Label badgeLabel;

    @FXML
    private Label headlineLabel;

    @FXML
    private Label summaryLabel;

    @FXML
    private Label encouragementLabel;

    @FXML
    private StackPane performanceRingHost;

    @FXML
    private Label performanceValueLabel;

    @FXML
    private Label performanceDetailLabel;

    @FXML
    private Label medalTitleLabel;

    @FXML
    private Label medalMessageLabel;

    @FXML
    private ProgressBar medalProgressBar;

    @FXML
    private Label medalProgressDetailLabel;

    @FXML
    private Label scholarshipTitleLabel;

    @FXML
    private Label scholarshipMessageLabel;

    @FXML
    private ProgressBar scholarshipProgressBar;

    @FXML
    private Label scholarshipProgressDetailLabel;

    @FXML
    private JFXButton actionButton;

    @Override
    public void init() {
        ResultadoCelebracaoContext.CelebrationRequest request = ResultadoCelebracaoContext.obterAtual();
        CelebrationSummary summary = request == null ? resumoPadrao() : request.summary();

        badgeLabel.setText(summary.badgeLabel());
        headlineLabel.setText(summary.headline());
        summaryLabel.setText(summary.summary());
        encouragementLabel.setText(summary.encouragement());
        performanceValueLabel.setText(Math.round(summary.performancePercent()) + "% de desempenho");
        performanceDetailLabel.setText(summary.performanceDetail());
        medalTitleLabel.setText(summary.medalTitle());
        medalMessageLabel.setText(summary.medalMessage());
        medalProgressBar.setProgress(summary.medalProgress());
        medalProgressDetailLabel.setText(summary.medalProgressDetail());
        scholarshipTitleLabel.setText(summary.scholarshipTitle());
        scholarshipMessageLabel.setText(summary.scholarshipMessage());
        scholarshipProgressBar.setProgress(summary.scholarshipProgress());
        scholarshipProgressDetailLabel.setText(summary.scholarshipProgressDetail());
        actionButton.setText(summary.actionLabel());

        CircleProgress progress = new CircleProgress(62, 62, 62, 0);
        progress.setValue(summary.performancePercent() / 100d);
        performanceRingHost.getChildren().setAll(progress);

        super.init();
    }

    @FXML
    private void Continuar(ActionEvent event) {
        ResultadoCelebracaoContext.CelebrationRequest request = ResultadoCelebracaoContext.consumirAtual();
        Runnable onContinue = request == null ? null : request.onContinue();
        fecharEContinuar(onContinue);
    }

    private void fecharEContinuar(Runnable onContinue) {
        StackPane pai = modal == null ? null : (StackPane) modal.getParent();
        if (pai == null) {
            if (onContinue != null) {
                onContinue.run();
            }
            return;
        }

        FadeTransition fade = new FadeTransition(Duration.seconds(0.3), pai);
        fade.setFromValue(Math.max(0, pai.getOpacity()));
        fade.setToValue(0);
        fade.setOnFinished(event -> {
            modal.setVisible(false);
            pai.setVisible(false);
            if (onContinue != null) {
                onContinue.run();
            }
        });
        fade.play();
    }

    private CelebrationSummary resumoPadrao() {
        return new CelebrationSummary(
            "RESULTADO",
            "Terminaste esta etapa.",
            "O sistema ja preparou o teu resultado completo.",
            "Cada sessao concluida ajuda a afinar medalhas, progresso e recomendacoes.",
            0,
            "0/0 respostas certas",
            "Rumo a nova medalha",
            "A tua proxima sessao vai empurrar ainda mais esta habilidade.",
            0,
            "0/5 progresso inicial",
            "Bolsa ficticia em mira",
            "O teu perfil interno vai ganhar mais forca conforme fores concluindo novas rodadas.",
            0,
            "0% de match interno | meta 60%",
            "Ver meu resultado"
        );
    }
}
