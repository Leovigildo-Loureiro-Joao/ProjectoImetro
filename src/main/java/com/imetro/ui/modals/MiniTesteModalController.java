package com.imetro.ui.modals;

import java.util.ArrayList;
import java.util.List;

import com.imetro.ui.model.Questao;
import com.imetro.util.QuestaoUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleNode;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MiniTesteModalController extends ModalController {

    @FXML private Label tituloLabel;
    @FXML private Label progressoLabel;
    @FXML private ProgressBar barraProgresso;
    @FXML private Label questaoNumeroLabel;
    @FXML private Label enunciadoLabel;
    @FXML private Label topicoLabel;
    @FXML private Label subtopicoLabel;
    @FXML private JFXToggleNode toggleA;
    @FXML private JFXToggleNode toggleB;
    @FXML private JFXToggleNode toggleC;
    @FXML private JFXToggleNode toggleD;
    @FXML private Label ResA;
    @FXML private Label ResB;
    @FXML private Label ResC;
    @FXML private Label ResD;
    @FXML private JFXButton btnConfirmar;
    @FXML private JFXButton btnProximo;
    @FXML private VBox questaoBox;
    @FXML private VBox resultadoBox;
    @FXML private Label resultadoTituloLabel;
    @FXML private Label resultadoDetalheLabel;
    @FXML private JFXButton btnFechar;

    private final ToggleGroup alternativas = new ToggleGroup();
    private List<Questao> questoes;
    private int questaoAtual;
    private final List<Character> respostasUsuario = new ArrayList<>();
    private int acertos;
    private String tituloLivro;
    private String topico;
    private String subtopico;
    private StackPane modalPaiRef;

    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
    }

    public void configurar(List<Questao> questoes, String tituloLivro, String topico, String subtopico, StackPane modalPai) {
        this.questoes = questoes;
        this.tituloLivro = tituloLivro;
        this.topico = topico;
        this.subtopico = subtopico;
        this.modalPaiRef = modalPai;
    }

    @Override
    public void init() {
        tituloLabel.setText("Mini teste: " + tituloLivro);
        questaoAtual = 0;
        acertos = 0;
        respostasUsuario.clear();
        resultadoBox.setVisible(false);
        resultadoBox.setManaged(false);
        questaoBox.setVisible(true);
        questaoBox.setManaged(true);
        carregarQuestao();
        super.init();
    }

    private void carregarQuestao() {
        if (questaoAtual >= questoes.size()) {
            mostrarResultado();
            return;
        }

        Questao q = questoes.get(questaoAtual);
        questaoNumeroLabel.setText("Questao " + (questaoAtual + 1) + " / " + questoes.size());
        barraProgresso.setProgress((double) (questaoAtual + 1) / questoes.size());
        progressoLabel.setText((questaoAtual + 1) + " de " + questoes.size());
        topicoLabel.setText("Topico: " + (q.getTopico() != null ? q.getTopico() : topico));
        subtopicoLabel.setText("Subtopico: " + (q.getSubtopico() != null ? q.getSubtopico() : subtopico));
        enunciadoLabel.setText(q.getEnunciado() != null ? q.getEnunciado() : "Sem enunciado");

        ResA.setText(q.getOpcaoA() != null ? q.getOpcaoA() : "-");
        ResB.setText(q.getOpcaoB() != null ? q.getOpcaoB() : "-");
        ResC.setText(q.getOpcaoC() != null ? q.getOpcaoC() : "-");
        ResD.setText(q.getOpcaoD() != null ? q.getOpcaoD() : "-");

        alternativas.selectToggle(null);
        limparEstilos();
        btnConfirmar.setDisable(false);
        btnProximo.setDisable(true);
    }

    @FXML
    private void confirmarResposta(ActionEvent event) {
        JFXToggleNode selected = (JFXToggleNode) alternativas.getSelectedToggle();
        if (selected == null) return;

        char resposta = selected.getText().charAt(0);
        respostasUsuario.add(resposta);
        Questao questao = questoes.get(questaoAtual);

        boolean correto = QuestaoUtil.respostaEstaCorreta(questao, resposta);
        if (correto) {
            acertos++;
            selected.getStyleClass().add("success");
        } else {
            selected.getStyleClass().add("error");
            destacarRespostaCorreta(questao);
        }

        btnConfirmar.setDisable(true);
        btnProximo.setDisable(false);
    }

    @FXML
    private void proximaQuestao(ActionEvent event) {
        questaoAtual++;
        if (questaoAtual >= questoes.size()) {
            mostrarResultado();
        } else {
            carregarQuestao();
        }
    }

    @FXML
    private void fecharResultado(ActionEvent event) {
        closeModal();
    }

    private void mostrarResultado() {
        questaoBox.setVisible(false);
        questaoBox.setManaged(false);
        resultadoBox.setVisible(true);
        resultadoBox.setManaged(true);

        int total = questoes.size();
        double percentual = total > 0 ? (acertos * 100.0 / total) : 0;
        String status = percentual >= 70.0 ? "Aprovado" : "Tente novamente";

        resultadoTituloLabel.setText("Mini teste concluido - " + status);
        resultadoDetalheLabel.setText(
            "Acertaste " + acertos + " de " + total + " questoes (" + String.format("%.0f", percentual) + "%).\n"
            + (percentual >= 70.0
                ? "Bom trabalho! O conhecimento foi validado."
                : "Reve as paginas " + questoes.get(0).getPaginaInicio() + "-"
                    + questoes.get(questoes.size() - 1).getPaginaFim() + " e tenta novamente.")
        );
    }

    private void limparEstilos() {
        toggleA.getStyleClass().removeAll("success", "error");
        toggleB.getStyleClass().removeAll("success", "error");
        toggleC.getStyleClass().removeAll("success", "error");
        toggleD.getStyleClass().removeAll("success", "error");
    }

    private void destacarRespostaCorreta(Questao questao) {
        char correta = QuestaoUtil.resolverAlternativaCorreta(questao);
        for (JFXToggleNode toggle : List.of(toggleA, toggleB, toggleC, toggleD)) {
            if (toggle.getText().charAt(0) == correta) {
                toggle.getStyleClass().add("success");
            }
        }
    }

    @Override
    protected void closeModal() {
        if (modalPaiRef != null) {
            modalPaiRef.getChildren().clear();
            modalPaiRef.setVisible(false);
        }
    }
}
