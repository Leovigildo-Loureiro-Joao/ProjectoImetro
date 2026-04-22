package com.imetro.ui.components;

import com.imetro.domain.dto.disciplina.DisciplinaDto;
import com.imetro.domain.enums.NivelDisciplina;
import com.jfoenix.controls.JFXRadioButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DisciplinaCard extends HBox {
    
    private final DisciplinaDto disciplina;
    private JFXRadioButton radioSelecionado;
    
    public DisciplinaCard(DisciplinaDto disciplina) {
        this.disciplina = disciplina;
        
        // ✅ Mostra nome E peso/objetivo (opcional)
        Label nomeLabel = new Label(disciplina.nome());
        nomeLabel.getStyleClass().add("muted");
        nomeLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        Label objetivoLabel = new Label(disciplina.objectivo());
        objetivoLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #666;");
        
        VBox left = new VBox(4, nomeLabel, objetivoLabel);
        
        ToggleGroup toggleGroup = new ToggleGroup();
        
        for (NivelDisciplina nivel : NivelDisciplina.values()) {
            JFXRadioButton rb = new JFXRadioButton(nivel.getDescricao());
            rb.setPadding(new Insets(8, 20, 8, 20));
            rb.setToggleGroup(toggleGroup);
            rb.setUserData(nivel);
            

            if (disciplina.nivel() == nivel) {
                rb.setSelected(true);
                radioSelecionado = rb;
            }
            
            left.getChildren().add(rb);
        }
        
        if (radioSelecionado == null && left.getChildren().size() > 1) {
            for (var node : left.getChildren()) {
                if (node instanceof JFXRadioButton) {
                    ((JFXRadioButton) node).setSelected(true);
                    radioSelecionado = (JFXRadioButton) node;
                    break;
                }
            }
        }
        
        left.setPrefWidth(300);
        left.setSpacing(5);
        
        
        this.getChildren().add(left);
        this.setSpacing(10);
        this.setAlignment(Pos.TOP_LEFT);
        this.setStyle("-fx-padding: 10 10; -fx-border-color: #ddd; -fx-border-radius: 5;");
    }
    
    public NivelDisciplina getNivelSelecionado() {
        if (radioSelecionado != null && radioSelecionado.getUserData() instanceof NivelDisciplina) {
            return (NivelDisciplina) radioSelecionado.getUserData();
        }
        return disciplina.nivel(); // Retorna o nível original se nada foi selecionado
    }
    
    public DisciplinaDto getDisciplina() {
        return disciplina;
    }
}