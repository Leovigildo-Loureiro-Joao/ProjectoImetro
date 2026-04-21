package com.imetro.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;

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
    private JFXToggleButton nivDifTogAEsc;

    @FXML
    private JFXToggleButton nivDifTogDEsc;

    @FXML
    private JFXToggleButton nivDifTogNEsc;

    @FXML
    private TextField normTest;

    @FXML
    private TextField precisDiag;

    @FXML
    private JFXRadioButton radDesa;

    @FXML
    private JFXRadioButton radExtra;

    @FXML
    private JFXRadioButton radFac;

    @FXML
    private JFXRadioButton radMed;

    @FXML
    private TextField resiliDiag;

    @FXML
    private TextField speedTemp;

    @FXML
    private TextField tempAdapt;

    @FXML
    private JFXToggleButton togDiagTest;

    @FXML
    private JFXToggleButton togNivelDif;

    @FXML
    private JFXToggleButton togVarDiag;

    @FXML
    private JFXComboBox<?> varSpeedTemp;

    @FXML
    private JFXComboBox<?> varTempAdapt;

    @FXML
    private TextField velociDiag;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DisableTog(true);
    }

    public void DisableTog( boolean p ){
        togDiagTest.setDisable(p);
        togNivelDif.setDisable(p);      
        togVarDiag.setDisable(p);
        if (p) {
            DisableAll();
        }
    }

    public void DisableAll(  ){
        boolean p = true;
        DisableNode(p,togDiagTest.getText());
        DisableNode(p,togNivelDif.getText());
        DisableNode(p,togVarDiag.getText());
    }

     public void DisableNode(boolean p, String section){
        if (section.equals(togDiagTest.getText())) {
            tempAdapt.setDisable(p);
            varTempAdapt.setDisable(p);
            speedTemp.setDisable(p);
            varSpeedTemp.setDisable(p);
            longTest.setDisable(p);
            extraTest.setDisable(p);
            desafTest.setDisable(p);
            normTest.setDisable(p);
        }
        if (section.equals(togNivelDif.getText())) {
            radDesa.setDisable(p);
            radExtra.setDisable(p);
            radFac.setDisable(p);
            radMed.setDisable(p);
            resiliDiag.setDisable(p);
            nivDifTogAEsc.setDisable(p);
            nivDifTogDEsc.setDisable(p);
            nivDifTogNEsc.setDisable(p);
        } 
        if (section.equals(togNivelDif.getText())) {
            radDesa.setDisable(p);
            radExtra.setDisable(p);
            radFac.setDisable(p);
            radMed.setDisable(p);
            resiliDiag.setDisable(p);
            nivDifTogAEsc.setDisable(p);
            nivDifTogDEsc.setDisable(p);
            nivDifTogNEsc.setDisable(p);
        }if (section.equals(togVarDiag.getText())) {
            velociDiag.setDisable(p);
            resiliDiag.setDisable(p);
            precisDiag.setDisable(p);
            logDiag.setDisable(p);
            consisDiag.setDisable(p);
        }
    }
    
    @FXML
    private void Alterar(ActionEvent event) {
        JFXButton bt=(JFXButton)event.getTarget();
        boolean p = !bt.getText().equals("Editar alterações");
        bt.setText(p?"Editar alterações":"Salvar alterações");
        DisableTog(p);
    }

    @FXML
    private void Reiniciar(ActionEvent event) {

    }

    @FXML
    private void alterarConfTestDiag(ActionEvent event) {
        JFXToggleButton bt=(JFXToggleButton)event.getTarget();
        DisableNode(!bt.isSelected(),bt.getText());
    }

    @FXML
    private void alterarNivelDif(ActionEvent event) {
        JFXToggleButton bt=(JFXToggleButton)event.getTarget();
       DisableNode(!bt.isSelected(),bt.getText());
    }

    @FXML
    private void alterarVarDiag(ActionEvent event) {
        JFXToggleButton bt=(JFXToggleButton)event.getTarget();
        DisableNode(!bt.isSelected(),bt.getText());
    }
}
