package com.imetro.ui.controller.candidato.diagnosticos;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import com.imetro.domain.dto.Topico;
import com.imetro.ui.components.DiagnosticoCard;
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

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        if (diagnosticosPane == null) {
            return;
        }

        diagnosticosPane.getChildren().clear();
        diagnosticosPane.getChildren().addAll(
            criarCard("Matematica", "-6%", 0.50, topicosMatematica()),
            criarCard("Fisica", "+4%", 0.62, topicosFisica()),
            criarCard("Quimica", "+1%", 0.58, topicosQuimica()),
            criarCard("Biologia", "-2%", 0.46, topicosBiologia()),
            criarCard("Portugues", "+7%", 0.71, topicosPortugues())
        );
        atualizarEstadoBotoes();
    }

    private DiagnosticoCard criarCard(String disciplina, String variacao, double progresso, Topico[] topicos) {
        return new DiagnosticoCard(
            disciplina,
            topicos,
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

    private Topico[] topicosMatematica() {
        return new Topico[] {
            new Topico(UUID.randomUUID(), "Algebra", UUID.randomUUID(), new String[] {"Equacoes", "Potenciacao", "Fracoes"}),
            new Topico(UUID.randomUUID(), "Geometria", UUID.randomUUID(), new String[] {"Areas", "Perimetros", "Circulos"})
        };
    }

    private Topico[] topicosFisica() {
        return new Topico[] {
            new Topico(UUID.randomUUID(), "Mecanica", UUID.randomUUID(), new String[] {"MRU", "Forca", "Energia"}),
            new Topico(UUID.randomUUID(), "Termologia", UUID.randomUUID(), new String[] {"Calor", "Temperatura"})
        };
    }

    private Topico[] topicosQuimica() {
        return new Topico[] {
            new Topico(UUID.randomUUID(), "Quimica Geral", UUID.randomUUID(), new String[] {"Atomos", "Ligacoes", "Tabela periodica"}),
            new Topico(UUID.randomUUID(), "Estequiometria", UUID.randomUUID(), new String[] {"Mol", "Balanceamento"})
        };
    }

    private Topico[] topicosBiologia() {
        return new Topico[] {
            new Topico(UUID.randomUUID(), "Citologia", UUID.randomUUID(), new String[] {"Celula animal", "Celula vegetal"}),
            new Topico(UUID.randomUUID(), "Genetica", UUID.randomUUID(), new String[] {"Hereditariedade", "DNA"})
        };
    }

    private Topico[] topicosPortugues() {
        return new Topico[] {
            new Topico(UUID.randomUUID(), "Gramatica", UUID.randomUUID(), new String[] {"Concordancia", "Regencia", "Pontuacao"}),
            new Topico(UUID.randomUUID(), "Interpretacao", UUID.randomUUID(), new String[] {"Texto", "Genero textual"})
        };
    }
}
