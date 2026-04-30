package com.imetro.ui.controller.candidato.diagnosticos;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.imetro.domain.dto.Topico;
import com.imetro.services.CatalogoQuestoesService;
import com.imetro.ui.components.diagnostico.DiagnosticoCard;
import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;

public class DiagnosticoListController implements Initializable {

    @FXML
    private FlowPane diagnosticosPane;


    @FXML
    private JFXButton massButton;

    @FXML
    private JFXButton resetButton;

    private final ArrayList<Topico> topicosSelecionados = new ArrayList<>();
    private final CatalogoQuestoesService catalogoQuestoesService = new CatalogoQuestoesService();

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        if (diagnosticosPane == null) {
            return;
        }

        diagnosticosPane.getChildren().clear();
        diagnosticosPane.getChildren().addAll(
            criarCard("Matematica", "-26%","-6%","-56%","-16%", 0.50),
            criarCard("Fisica", "+24%", "+4%", "+14%", "+40%", 0.62),
            criarCard("Quimica", "+8%","+1%","+4%","+5%", 0.58),
            criarCard("Biologia", "5%","-2%","-8%","2%", 0.46),
            criarCard("Portugues", "+12%","+9%","+14%","+17%", 0.71)
        );
        atualizarEstadoBotoes();
    }

    private DiagnosticoCard criarCard(String disciplina, String variacao,String variacaoTime, String variacaoAcer, String variacaoErr,double progresso) {
        Topico[] topicos = catalogoQuestoesService
            .carregarTopicosPorDisciplina(disciplina)
            .toArray(Topico[]::new);

        return new DiagnosticoCard(
            disciplina,
            topicos,
            variacaoAcer,
            variacaoErr,
            variacaoTime,
            variacao,
            progresso,
            param -> {
                DiagnosticoCoordinator.requestStart(new ArrayList<>(param));
                return null;
            },
            this::atualizarEstadoBotoes
        );
    }

    public void atualizarEstadoBotoes() {
        int selecionados = 0;
        topicosSelecionados.clear();

        for (Node node : diagnosticosPane.getChildren()) {
            DiagnosticoCard card = (DiagnosticoCard) node;
            if (card.getDiciplina().isSelected()) {
                selecionados++;
                topicosSelecionados.addAll(card.getTopicos());
            }
        }

        massButton.setDisable(selecionados <= 1);
        resetButton.setDisable(selecionados == 0);
    }

    @FXML
    private void DiagnosticoMassa(ActionEvent event) {
        if (!topicosSelecionados.isEmpty()) {
            DiagnosticoCoordinator.requestStart(new ArrayList<>(topicosSelecionados));
        }
    }

    @FXML
    private void ResetData(ActionEvent event) {
        DiagnosticoCoordinator.requestAlert(
            "Resetar diagnosticos",
            "Deseja limpar a selecao atual e recomecar o fluxo?",
            this::limparSelecaoAtual
        );
    }

    private void limparSelecaoAtual() {
        for (Node node : diagnosticosPane.getChildren()) {
            DiagnosticoCard card = (DiagnosticoCard) node;
            card.setSelecionado(false);
        }
        topicosSelecionados.clear();
        atualizarEstadoBotoes();
    }
}
