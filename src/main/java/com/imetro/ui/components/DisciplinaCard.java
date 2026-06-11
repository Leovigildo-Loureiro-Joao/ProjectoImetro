package com.imetro.ui.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.imetro.domain.dto.disciplina.DisciplinaDto;
import com.imetro.domain.enums.TopicoExame;
import com.imetro.util.TextoUtil;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DisciplinaCard extends HBox {

    private final DisciplinaDto disciplina;
    private final List<CheckBox> topicosChecks = new ArrayList<>();
    private final VBox topicosBox = new VBox(4);

    public DisciplinaCard(DisciplinaDto disciplina) {
        this(disciplina, List.of());
    }

    public DisciplinaCard(DisciplinaDto disciplina, Collection<String> topicosSelecionados) {
        super();
        this.disciplina = Objects.requireNonNull(disciplina, "disciplina");

        Label nomeLabel = new Label(disciplina.nome());
        nomeLabel.getStyleClass().add("muted");
        nomeLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Label objetivoLabel = new Label(normalizarObjetivo(disciplina.objectivo()));
        objetivoLabel.setWrapText(true);
        objetivoLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #666;");

        Label topicosLabel = new Label("Topicos que vais estudar");
        topicosLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #888;");

        carregarTopicosDisponiveis();
        aplicarSelecaoInicial(topicosSelecionados);

        ScrollPane scrollPane = new ScrollPane(topicosBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(152);
        scrollPane.setMinHeight(152);

        VBox left = new VBox(
            6,
            nomeLabel,
            objetivoLabel,
            topicosLabel,
            scrollPane
        );

        this.getChildren().add(left);
        this.setSpacing(10);
        this.setAlignment(Pos.TOP_LEFT);
        this.setStyle("-fx-padding: 10 10; -fx-border-color: #ddd; -fx-border-radius: 5;");
    }

    public DisciplinaDto getDisciplina() {
        return disciplina;
    }

    public List<String> getTopicosSelecionados() {

        return topicosChecks.stream()
            .filter(CheckBox::isSelected)
            .map(cb -> (String) cb.getUserData())
            .toList();
    }

    public String getTopicosFoco() {
        return String.join(", ", getTopicosSelecionados());
    }

    public String getTopicosFocoPersistencia() {
        return String.join("\n", getTopicosSelecionados());
    }

    public String getSubtopicosFoco() {
        return getTopicosFocoPersistencia();
    }

    private String normalizarObjetivo(String objetivo) {
        if (objetivo == null || objetivo.isBlank()) {
            return "Escolhe apenas os topicos que queres consolidar nesta disciplina.";
        }
        return objetivo;
    }

    private void carregarTopicosDisponiveis() {

        TopicoExame.Disciplina disciplinaEnum = resolverDisciplinaEnum();

        if (disciplinaEnum == null) {
            topicosBox.getChildren().add(
                new Label("Sem topicos disponiveis")
            );
            return;
        }

        List<TopicoExame> topicos =
            TopicoExame.topicosModoInteligente(disciplinaEnum);

        for (TopicoExame topico : topicos) {

            CheckBox checkBox =
                new CheckBox(formatarTopico(topico));

            checkBox.setUserData(topico.getLabel());

            topicosChecks.add(checkBox);
            topicosBox.getChildren().add(checkBox);
        }
    }

    private void aplicarSelecaoInicial(
        Collection<String> topicosSelecionados) {

        if (topicosSelecionados == null
                || topicosSelecionados.isEmpty()) {
            return;
        }

        Set<String> selecionadosNormalizados =
            topicosSelecionados.stream()
                .filter(Objects::nonNull)
                .map(TextoUtil::normalizarMinusculo)
                .collect(Collectors.toSet());

        for (CheckBox checkBox : topicosChecks) {

            String label =
                TextoUtil.normalizarMinusculo(
                    (String) checkBox.getUserData()
                );

            if (selecionadosNormalizados.contains(label)) {
                checkBox.setSelected(true);
            }
        }
    }

    private TopicoExame.Disciplina resolverDisciplinaEnum() {
        if (disciplina == null || disciplina.nome() == null) {
            return null;
        }

        return TopicoExame.resolverDisciplina(disciplina.nome()).orElse(null);
    }

    private String formatarTopico(TopicoExame topico) {
        return "[" + topico.getArea() + "] " + topico.getLabel();
    }

}
