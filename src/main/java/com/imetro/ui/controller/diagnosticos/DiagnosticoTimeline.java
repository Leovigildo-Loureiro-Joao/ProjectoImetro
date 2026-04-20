package com.imetro.ui.controller.diagnosticos;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

import com.imetro.domain.dto.Stats;
import com.imetro.domain.dto.diagnostico.TimelineDTO;
import com.imetro.ui.components.TimelineCard;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DiagnosticoTimeline implements Initializable {

    @FXML
    private VBox timelineContent;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        if (timelineContent == null) {
            return;
        }

        timelineContent.getChildren().clear();
        timelineContent.getChildren().addAll(
            new TimelineCard( new TimelineDTO(
                LocalDate.of(2025, 1, 19),
                new LocalTime[]{LocalTime.of(15, 0), LocalTime.of(16, 30)},
                new String[]{"MATEMATICA", "PORTUGUES"},
                new String[]{"2min", "3min"},
                new float[]{15f, 12f},
                new float[]{20f, 8f},
                new float[]{50f, 65f},
                new Stats[]{
                    new Stats(0.75f, 0.60f, 0.85f, 0.45f, 0.90f),
                    new Stats(0.80f, 0.70f, 0.75f, 0.60f, 0.85f)
                }
            )).getRoot(),
            new TimelineCard(new TimelineDTO(
                LocalDate.of(2025, 1, 20),
                new LocalTime[]{LocalTime.of(14, 0), LocalTime.of(17, 0)},
                new String[]{"CIENCIAS", "HISTORIA"},
                new String[]{"2min 30s", "3min 15s"},
                new float[]{18f, 14f},
                new float[]{12f, 6f},
                new float[]{75f, 80f},
                new Stats[]{
                    new Stats(0.70f, 0.85f, 0.80f, 0.75f, 0.70f),
                    new Stats(0.85f, 0.75f, 0.90f, 0.80f, 0.85f)
                }
            )
          
        ).getRoot() );
    }
}
