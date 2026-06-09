package com.imetro.ui.modals;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class ModalController implements Initializable{

    @FXML
    public JFXButton close;

    @FXML
    protected VBox modal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public void init(){
        if (!(modal.getParent() instanceof StackPane pai)) {
            return;
        }

        StackPane.setAlignment(modal, Pos.CENTER);
        pai.setVisible(true);
        modal.setVisible(true);
        modal.toFront();
        pai.toFront();
        FadeTransition dTransition=new FadeTransition(Duration.seconds(0.3),pai);
        dTransition.setByValue(0);
        dTransition.setToValue(1);
        dTransition.play();

    }

    protected void closeModal(){
        if (!(modal.getParent() instanceof StackPane pai)) {
            modal.setVisible(false);
            return;
        }

        FadeTransition dTransition=new FadeTransition(Duration.seconds(0.3),modal.getParent());
        dTransition.setByValue(1);
        dTransition.setToValue(0);
        dTransition.play();
        dTransition.setOnFinished(event -> {
            modal.setVisible(false);
            pai.setVisible(false);
        });
    }

    @FXML
    public void Close(ActionEvent ev){
        closeModal();
    }

}
