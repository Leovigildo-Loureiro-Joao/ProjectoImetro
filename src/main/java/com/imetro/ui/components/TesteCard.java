package com.imetro.ui.components;

import java.util.ArrayList;
import java.util.List;

import com.imetro.domain.dto.Topico;
import com.imetro.domain.dto.test.TesteDto;
import com.jfoenix.controls.JFXButton;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class TesteCard extends FlowPane{

   

    private CircleProgress progresso;
    private ProgressBar melhoria=new ProgressBar();
    private ProgressBar erros_comum=new ProgressBar();
    private ProgressBar ritmo_estudo=new ProgressBar();
    private VBox passos;
    private FlowPane topicoBadge;

    public TesteCard(TesteDto teste,Runnable run) {
    
        progresso=new CircleProgress(75,75);
        progresso.setValue(teste.percent());
        StackPane cir=new StackPane(progresso);
        cir.setPrefSize(200, 200);
        JFXButton button=new JFXButton("Iniciar Teste adaptativo");
        button.setPrefSize(199, 39);
        button.setOnAction(event -> run.run());
        button.getStyleClass().add("btn-primary-two");
        button.setTranslateY(35);
        Label disciplina=new Label(teste.disciplina());
        disciplina.getStyleClass().add("h2-thin");
        disciplina.setPadding(new Insets(10));

        VBox vBox0 = new VBox(
            disciplina,
            cir
        );
        vBox0.setPrefSize(200, 200);
        vBox0.setAlignment(Pos.TOP_CENTER);
        VBox vBox = new VBox(10,
            Percent("Melhorias",teste.melhoria()),
            Percent("Erros comuns",teste.errosComuns()),
            Percent("Ritmo evolutivo",teste.ritmoEvolutivo()),
            button
        );
        vBox.setPadding(new Insets(10));
        vBox.setPrefHeight(139);

        topicoBadge=new FlowPane(10, 10);
        topicoBadge.setPadding(new Insets(10, 0, 10, 0));
        Label title=new Label("Dominio dos topicos diagnosticados");
        title.getStyleClass().add("h3-thin-big");
        ScrollPane pane=new ScrollPane(topicoBadge);
        pane.setFitToHeight(true);
        pane.setFitToWidth(true);
        pane.setHbarPolicy(ScrollBarPolicy.NEVER);
        pane.setPrefSize(401, 121);
        JFXButton button2=new JFXButton("Fazer diagnostico inteligente");
        button2.setPrefSize(230, 39);
        //button.setOnAction(null);
        button2.getStyleClass().add("btn-primary");
        VBox vBox2 = new VBox(10,title,pane,button2);
        vBox2.setPadding(new Insets(10));
        vBox2.setPrefSize(369, 200);

        Label title2=new Label("Proximos passos");
        title2.getStyleClass().add("h3-thin-big");
        passos=new VBox(10);
        VBox vBox3 = new VBox(10,title2,passos);
        vBox3.setPadding(new Insets(10));
        vBox3.setPrefSize(200, 200);
        this.setPadding(new Insets(10));
        this.setVgap(10);
        this.setHgap(10);
        this.getChildren().addAll(vBox0,vBox,vBox2,vBox3);
        this.getStyleClass().add("card");
        TopicosAdd(teste.topicos());

    }

    public void TopicosAdd(List<com.imetro.domain.dto.test.Percent> o){
        for (com.imetro.domain.dto.test.Percent topico : o) {
            
            Label label=new Label(topico.topico());
            Label percent=new Label(topico.evolucao()+"%");
            label.getStyleClass().add("h3-thin");
            percent.getStyleClass().add("percent-value");
            HBox topicBox=new HBox(label,percent);
            topicBox.getStyleClass().add("badge-test");
            topicoBadge.getChildren().add(topicBox);
        }
    }

    public VBox Percent(String text,float value){
        Label p = new Label(text);
        p.getStyleClass().add("h3-thin");
        VBox ps = new VBox(5,p);
        switch (text) {
            case "Melhorias":
                ps.getChildren().add(melhoria);
                melhoria.setPrefWidth(200);
                melhoria.setProgress(value);
                break;
            case "Erros comuns":
                ps.getChildren().add(erros_comum);
                erros_comum.setPrefWidth(200);
                erros_comum.setProgress(value);
                break;
            default:
                 ps.getChildren().add(ritmo_estudo);
                 ritmo_estudo.setPrefWidth(200);
                 ritmo_estudo.setProgress(value);

                break;
        }
        return ps;
    }
}
