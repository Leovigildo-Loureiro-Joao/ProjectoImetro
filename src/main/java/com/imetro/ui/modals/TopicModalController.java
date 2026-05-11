package com.imetro.ui.modals;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.imetro.domain.dto.Topico;
import com.imetro.services.DiagnosticoService;
import com.imetro.ui.controller.candidato.diagnosticos.DiagnosticoCoordinator;
import com.imetro.ui.controller.candidato.testes.TesteAdaptativoCoordinator;
import com.imetro.util.Authentication;
import com.imetro.util.TextoUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TopicModalController extends ModalController {

    @FXML
    private Label tituloLabel;

    @FXML
    private VBox topicosContainer;

    @FXML
    private Label resumoLabel;

    @FXML
    private Label contadorLabel;

    @FXML
    private JFXButton iniciarButton;

    @FXML
    private JFXButton usarTodosButton;

    private final Map<String, List<JFXCheckBox>> checkboxesPorTopico = new LinkedHashMap<>();
    private final DiagnosticoService diagnosticoService = new DiagnosticoService();
    private Map<String, Double> progressoPorSubtopico = Map.of();

    @Override
    public void init() {
        if (FluxoModalContext.isTesteAdaptativo()) {
            tituloLabel.setText("Escolher subtopicos do teste");
            iniciarButton.setText("Iniciar teste com os selecionados");
            usarTodosButton.setText("Usar todos os subtopicos do teste");
        }
        montarTopicos();
        atualizarResumo();
        super.init();
    }

    @FXML
    private void InteligentDiagnostic(ActionEvent event) {
        Map<String, List<String>> selecionados = coletarSubtopicosSelecionados(false);
        if (selecionados.isEmpty()) {
            contadorLabel.setText("Selecione pelo menos um subtopico para continuar.");
            return;
        }

        if (FluxoModalContext.isTesteAdaptativo()) {
            TesteAdaptativoCoordinator.updateSubtopicosSelecionados(selecionados);
        } else {
            DiagnosticoCoordinator.updateSubtopicosSelecionados(selecionados);
        }
        closeModal();
        requestStartSoRun();
    }

    @FXML
    private void SoRun(ActionEvent event) {
        Map<String, List<String>> selecionados = coletarSubtopicosSelecionados(true);
        if (FluxoModalContext.isTesteAdaptativo()) {
            TesteAdaptativoCoordinator.updateSubtopicosSelecionados(selecionados);
        } else {
            DiagnosticoCoordinator.updateSubtopicosSelecionados(selecionados);
        }
        closeModal();
        requestStartSoRun();
    }

    private void requestStartSoRun() {
        if (FluxoModalContext.isTesteAdaptativo()) {
            TesteAdaptativoCoordinator.requestStartSoRun();
            return;
        }
        DiagnosticoCoordinator.requestStartSoRun();
    }

    private void montarTopicos() {
        topicosContainer.getChildren().clear();
        checkboxesPorTopico.clear();

        List<Topico> topicos = FluxoModalContext.isTesteAdaptativo()
            ? TesteAdaptativoCoordinator.getTopicosSelecionados()
            : DiagnosticoCoordinator.getTopicosSelecionados();
        progressoPorSubtopico = diagnosticoService.carregarProgressoSubtopicos(
            Authentication.getCurrentUserId(),
            topicos
        );

        for (Topico topico : topicos) {
            VBox grupo = new VBox(10);
            Label titulo = new Label(topico.topicos());
            titulo.getStyleClass().add("h3-thin");
            grupo.getChildren().add(titulo);

            List<JFXCheckBox> checkboxes = new ArrayList<>();
            String[] subtopicos = topico.subTopicos() == null ? new String[0] : topico.subTopicos();
            for (String subtopico : subtopicos) {
                HBox linha = new HBox(16);
                linha.setPadding(new Insets(0, 0, 0, 10));

                JFXCheckBox checkBox = new JFXCheckBox(subtopico);
                checkBox.selectedProperty().addListener((obs, oldValue, newValue) -> atualizarResumo());

                ProgressBar progresso = new ProgressBar(calcularProgresso(topico, subtopico));
                progresso.setPrefWidth(212);

                linha.getChildren().addAll(checkBox, progresso);
                grupo.getChildren().add(linha);
                checkboxes.add(checkBox);
            }

            checkboxesPorTopico.put(topico.topicos(), checkboxes);
            topicosContainer.getChildren().add(grupo);
        }
    }

    private void atualizarResumo() {
        int totalSelecionado = 0;
        int totalDisponivel = 0;

        for (List<JFXCheckBox> checkboxes : checkboxesPorTopico.values()) {
            totalDisponivel += checkboxes.size();
            for (JFXCheckBox checkBox : checkboxes) {
                if (checkBox.isSelected()) {
                    totalSelecionado++;
                }
            }
        }

        String resumo = FluxoModalContext.isTesteAdaptativo()
            ? TesteAdaptativoCoordinator.buildResumoSelecao()
            : DiagnosticoCoordinator.buildResumoSelecao();

        resumoLabel.setText("Topicos: " + resumo);
        contadorLabel.setText(totalSelecionado + " de " + totalDisponivel + " subtopicos selecionados");
        iniciarButton.setDisable(totalSelecionado == 0);
    }

    private Map<String, List<String>> coletarSubtopicosSelecionados(boolean selecionarTodos) {
        Map<String, List<String>> selecionados = new LinkedHashMap<>();

        for (Map.Entry<String, List<JFXCheckBox>> entry : checkboxesPorTopico.entrySet()) {
            List<String> subtopicos = new ArrayList<>();
            for (JFXCheckBox checkBox : entry.getValue()) {
                if (selecionarTodos || checkBox.isSelected()) {
                    subtopicos.add(checkBox.getText());
                }
            }
            if (!subtopicos.isEmpty()) {
                selecionados.put(entry.getKey(), subtopicos);
            }
        }

        return selecionados;
    }

    private double calcularProgresso(Topico topico, String subtopico) {
        if (topico == null) {
            return 0d;
        }
        String chave = TextoUtil.normalizarMinusculo(topico.disciplina()) + "::" + TextoUtil.normalizarMinusculo(subtopico);
        return progressoPorSubtopico.getOrDefault(chave, 0d);
    }
}
