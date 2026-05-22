package com.imetro.ui.controller.candidato;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

import com.imetro.domain.dto.configuracao.ConfiguracaoDto;
import com.imetro.persistence.repository.ConfiguracoesRepository;
import com.imetro.util.Authentication;
import com.imetro.util.ParseTimeStampLocalDate;
import com.imetro.util.QuestaoUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXToggleButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class ConfiguracaoController implements Initializable{
    @FXML
    private TextField consisDiag;

    @FXML
    private TextField desafTest;

    @FXML
    private TextField extraTest;

    @FXML
    private TextField logDiag;

    @FXML
    private TextField longTest;

    @FXML
    private TextField normTest;

    @FXML
    private TextField precisDiag;

    @FXML
    private TextField resiliDiag;

    @FXML
    private TextField speedTemp;

    @FXML
    private TextField tempAdapt;

    @FXML
    private JFXToggleButton togDiagTest;

    @FXML
    private JFXToggleButton togVarDiag;
    @FXML
    private JFXComboBox<String> varSpeedTemp;

    @FXML
    private JFXComboBox<String> varTempAdapt;

    @FXML
    private TextField velociDiag;

    private ConfiguracoesRepository configRepository;
    private ConfiguracaoDto config;
    List<String>tempoStatus=List.of("SEGUNDOS","MINUTOS","MILISSEGUNDOS");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configRepository=new ConfiguracoesRepository();
        config=configRepository.selectConfiguracao(Authentication.getCurrentUserId());
        varSpeedTemp.getItems().addAll(tempoStatus);
        varTempAdapt.getItems().addAll(tempoStatus);
        config = configRepository.selectConfiguracao(Authentication.getCurrentUserId());
        DisableTog(true);
        InitConfigs();
    }

    public void InitConfigs(){
        consisDiag.setText(config.consistencia_percentual_min()+"");
        desafTest.setText(config.desaf_test_q()+"");
        extraTest.setText(config.extra_test_q()+"");
        logDiag.setText(config.logica_qtd_desafiante_extra()+"");
        longTest.setText(config.long_test_q()+"");
        normTest.setText(config.norm_test_q()+"");
        precisDiag.setText(config.precisao_consecutivas()+"");
        resiliDiag.setText(config.resiliencia_repeticoes_por_dia()+"");
        speedTemp.setText(config.speed_temp_val()+"");
        tempAdapt.setText(config.temp_adapt_val()+"");
        velociDiag.setText(config.velocidade_segundos_por_percent()+"");
        varSpeedTemp.getSelectionModel().select(tempoStatus.indexOf(config.speed_temp_unit()));
        varTempAdapt.getSelectionModel().select(tempoStatus.indexOf(config.temp_adapt_unit()));
    }




    public void DisableTog( boolean p ){
        togDiagTest.setDisable(p);
        togVarDiag.setDisable(p);
        if (p) {
            DisableAll();
        }
    }

    public void DisableAll(  ){
        boolean p = true;
        togDiagTest.setSelected(false);
        togVarDiag.setSelected(false);
        setSectionDisabled(togDiagTest, p);
        setSectionDisabled(togVarDiag, p);
    }

    private void setSectionDisabled(JFXToggleButton sectionToggle, boolean p) {
        if (sectionToggle == togDiagTest) {

            tempAdapt.setDisable(p);
            varTempAdapt.setDisable(p);
            speedTemp.setDisable(p);
            varSpeedTemp.setDisable(p);
            longTest.setDisable(p);
            extraTest.setDisable(p);
            desafTest.setDisable(p);
            normTest.setDisable(p);
        }

        if (sectionToggle == togVarDiag) {

            velociDiag.setDisable(p);
            resiliDiag.setDisable(p);
            precisDiag.setDisable(p);
            logDiag.setDisable(p);
            consisDiag.setDisable(p);
        }
    }

    @FXML
    private void Alterar(ActionEvent event) throws NumberFormatException, SQLException {
        JFXButton bt = (JFXButton) event.getSource();
        boolean entrarModoEdicao = bt.getText().equals("Editar alterações");
        bt.setText(entrarModoEdicao ? "Salvar alterações" : "Editar alterações");
        DisableTog(!entrarModoEdicao);
        configRepository.updateById(config.id(),new ConfiguracaoDto(config.id(), config.user_id(), Integer.parseInt(tempAdapt.getText()), varTempAdapt.getSelectionModel().getSelectedItem(), Integer.parseInt(speedTemp.getText()), varSpeedTemp.getSelectionModel().getSelectedItem(), Integer.parseInt(longTest.getText()), Integer.parseInt(normTest.getText()), Integer.parseInt(desafTest.getText()), Integer.parseInt(extraTest.getText()), "MEDIO", "DIAGNOSTICAS", Integer.parseInt(velociDiag.getText()),  Integer.parseInt(resiliDiag.getText()),  Integer.parseInt(precisDiag.getText()),  Integer.parseInt(logDiag.getText()),  Double.parseDouble(consisDiag.getText()), config.criado_em() , LocalDateTime.now()).toMapUpdate());

    }

    @FXML
    private void Reiniciar(ActionEvent event) throws NumberFormatException, SQLException {
        configRepository.updateById(config.id(),new ConfiguracaoDto(config.id(), config.user_id(),20,"MINUTOS", 60, "SEGUNDOS", 10,7,7, 5, "MEDIO", "DIAGNOSTICAS", 120,  2,  3,  2, 70, config.criado_em() , LocalDateTime.now()).toMap());
        InitConfigs();
    }

    @FXML
    private void alterarConfTestDiag(ActionEvent event) {
        JFXToggleButton bt = (JFXToggleButton) event.getSource();
        setSectionDisabled(bt, !bt.isSelected());
    }

    @FXML
    private void alterarNivelDif(ActionEvent event) {
        JFXToggleButton bt = (JFXToggleButton) event.getSource();
        setSectionDisabled(bt, !bt.isSelected());
    }

    @FXML
    private void alterarVarDiag(ActionEvent event) {
        JFXToggleButton bt = (JFXToggleButton) event.getSource();
        setSectionDisabled(bt, !bt.isSelected());
    }
}
