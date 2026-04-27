package com.imetro.ui.components;

import com.imetro.domain.dto.disciplina.DisciplinaDto;
import com.imetro.domain.enums.NivelDisciplina;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DisciplinaCard extends HBox {
    
    private final DisciplinaDto disciplina;
    private JFXRadioButton radioSelecionado;
    private JFXCheckBox nomeLabel;
    private VBox left;
    private VBox radiBox;
    private boolean isExpanded;
    public final ToggleGroup toggleGroup = new ToggleGroup();
    
    public DisciplinaCard(DisciplinaDto disciplina) {
        super();
        this.disciplina = disciplina;
        nomeLabel = new JFXCheckBox(disciplina.nome());
        nomeLabel.getStyleClass().add("muted");
        nomeLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        Label objetivoLabel = new Label(disciplina.objectivo());
        objetivoLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #666;");
        
        
        radiBox=new VBox(5);
        for (NivelDisciplina nivel : NivelDisciplina.values()) {
            JFXRadioButton rb = new JFXRadioButton(nivel.getDescricao());
            rb.setPadding(new Insets(8, 20, 8, 20));
            rb.getStyleClass().add("muted");
            rb.setToggleGroup(toggleGroup);
            rb.setUserData(nivel);
            

            if (disciplina.nivel() == nivel) {
                rb.setSelected(true);
                radioSelecionado = rb;
            }
            
            radiBox.getChildren().add(rb);
        }
        
        if (radioSelecionado == null && left.getChildren().size() > 1) {
            for (var node : radiBox.getChildren()) {
                if (node instanceof JFXRadioButton) {
                    ((JFXRadioButton) node).setSelected(true);
                    radioSelecionado = (JFXRadioButton) node;
                    break;
                }
            }
        }
        left = new VBox(4, nomeLabel, objetivoLabel,radiBox);
        left.setPrefWidth(300);
        left.setSpacing(5);
        
        
        this.getChildren().add(left);
        this.setSpacing(10);
        this.setAlignment(Pos.TOP_LEFT);
        this.setStyle("-fx-padding: 10 10; -fx-border-color: #ddd; -fx-border-radius: 5;");
        nomeLabel.setOnAction(arg0 -> toggleExpand());
        this.setMaxHeight(BASELINE_OFFSET_SAME_AS_HEIGHT);
        toggleExpand() ;
    }
    
    
    private void toggleExpand() {
        isExpanded = nomeLabel.isSelected();

        for (var node : radiBox.getChildren()) {
            if (node instanceof JFXRadioButton) {
                ((JFXRadioButton) node).setToggleGroup(isExpanded?toggleGroup:null);
                ((JFXRadioButton) node).setSelected(((JFXRadioButton) node).isSelected()?false:true);
                break;
            }
        }
        // Expansão/colapso
        if (isExpanded) {
            radiBox.setVisible(true);
            radiBox.setManaged(true);
            radiBox.setOpacity(0);
            
            javafx.animation.Timeline timeline = new javafx.animation.Timeline();
            javafx.animation.KeyValue kv = new javafx.animation.KeyValue(
                radiBox.opacityProperty(), 1
            );
            javafx.animation.KeyFrame kf = new javafx.animation.KeyFrame(
                javafx.util.Duration.millis(300), kv
            );
            timeline.getKeyFrames().add(kf);
            timeline.play();
        } else {
            javafx.animation.Timeline timeline = new javafx.animation.Timeline();
            javafx.animation.KeyValue kv = new javafx.animation.KeyValue(
                radiBox.opacityProperty(), 0
            );
            javafx.animation.KeyFrame kf = new javafx.animation.KeyFrame(
                javafx.util.Duration.millis(200), 
                e -> {
                    radiBox.setVisible(false);
                    radiBox.setManaged(false);
                }, 
                kv
            );
            timeline.getKeyFrames().add(kf);
            timeline.play();
        }
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


    public JFXRadioButton getRadioSelecionado() {
        return radioSelecionado;
    }


    public void setRadioSelecionado(JFXRadioButton radioSelecionado) {
        this.radioSelecionado = radioSelecionado;
    }


    public JFXCheckBox getNomeLabel() {
        return nomeLabel;
    }


    public void setNomeLabel(JFXCheckBox nomeLabel) {
        this.nomeLabel = nomeLabel;
    }


    public VBox getLeft() {
        return left;
    }


    public void setLeft(VBox left) {
        this.left = left;
    }


    public VBox getRadiBox() {
        return radiBox;
    }


    public void setRadiBox(VBox radiBox) {
        this.radiBox = radiBox;
    }


    public boolean isExpanded() {
        return isExpanded;
    }


    public void setExpanded(boolean isExpanded) {
        this.isExpanded = isExpanded;
    }
    
}