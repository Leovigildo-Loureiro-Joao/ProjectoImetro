package com.imetro.ui.controller.candidato.resultados;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import com.imetro.config.RuntimeConfig;
import com.imetro.persistence.repository.MedalhaRepository;
import com.imetro.ui.components.bolsas.BolsaCard;
import com.imetro.ui.components.perfil.MedalCard;
import com.imetro.ui.controller.lifecycle.DisposableController;
import com.imetro.util.Authentication;
import com.imetro.util.MedalSupport;
import com.imetro.util.ResultadoPayload;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;

public class ResultadoStats implements Initializable, DisposableController{

    @FXML
    private Label acertosValue;

    @FXML
    private Label disciplinaValue;

    @FXML
    private Label errosValue;

    @FXML
    private FlowPane medalhasFlow;

    @FXML
    private Label nivelValue;

    @FXML
    private Label percentualAcerto;

    @FXML
    private Label percentualDificuldade;

    @FXML
    private Label percentualMelhoria;

    @FXML
    private ProgressBar percentualProgressMelhotis;

    @FXML
    private Label perfilValue;

    @FXML
    private ProgressBar progressAcerto;

    @FXML
    private ProgressBar progressDificuldade;

    @FXML
    private Label tempoValue;
    MedalhaRepository medalhaRepository = new MedalhaRepository();

    @Override
    public void dispose() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ResultadoPayload payload = ResultadoAvaliacaoController.ultimoResultado;
       if (payload == null) {
            payload = new ResultadoPayload(
                "Avaliacao",
                "-",
                0,
                0,
                0,
                0,
                "00:00",
                "-",
                "-",
                "Conclua uma avaliacao para ver os resultados aqui.",
                "views/pages/candidato/dashboard",
                List.of()
            );
        }
            disciplinaValue.setText(payload.getDisciplina());
            acertosValue.setText(payload.getAcertos() + " / " + payload.getTotalQuestoes());
            errosValue.setText(String.valueOf(payload.getErros()));
            percentualAcerto.setText(String.format("%.1f%%", payload.getPercentual()));
            progressAcerto.setProgress(Math.max(0, Math.min(payload.getPercentual() / 100.0, 1)));
            tempoValue.setText(payload.getTempo());
            nivelValue.setText(payload.getNivel());
            perfilValue.setText(payload.getPerfil());

            List<MedalSupport.MedalViewModel> list= AwardsGoal();
            for (MedalSupport.MedalViewModel medal : list) {
                medalhasFlow.getChildren().add(new MedalCard(medal));
            }


    }

    private List<MedalSupport.MedalViewModel> AwardsGoal(){
        List<MedalSupport.MedalAward> awards=medalhaRepository.findAwardsByUserIdUpdates(Authentication.getCurrentUserId());
        Map<MedalSupport.MedalSkill, Integer> previewProgress = RuntimeConfig.isDbEnabled()
        ? Map.of()
        : MedalSupport.navigationPreviewProgress();
        return awards.stream().map(award -> {
            int progressValue = 0;
            boolean unlocked = false;
            Integer recordValue = null;
            java.time.LocalDateTime earnedAt = null;
            MedalSupport.MedalDefinition definition=MedalSupport.catalog().stream().filter(def -> def.code().equals(award.medalCode())).toList().get(0);
            if (award != null) {
                progressValue = Math.max(award.progressValue(), definition.targetValue());
                unlocked = true;
                recordValue = award.recordValue();
                earnedAt = award.earnedAt();
            } else if (!RuntimeConfig.isDbEnabled()) {
                progressValue = previewProgress.getOrDefault(definition.skill(), 0);
                unlocked = progressValue >= definition.targetValue();
            }

            return new MedalSupport.MedalViewModel(definition, unlocked, progressValue, recordValue, earnedAt);
        }).toList();
    }

}
