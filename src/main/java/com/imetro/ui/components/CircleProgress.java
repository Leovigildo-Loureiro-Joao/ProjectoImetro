package com.imetro.ui.components;

import static javafx.scene.control.ProgressIndicator.INDETERMINATE_PROGRESS;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class CircleProgress extends Group {

    private Arc backgroundArc;
    private Arc progressArc;
    private Label percentLabel;
    private Label subtitleLabel;
    private double progressValue;
    private DoubleProperty progress;
    private ReadOnlyBooleanWrapper indeterminate;
    private Timeline animationTimeline;

    // Propriedades para customização
    private ObjectProperty<Color> progressColor = new SimpleObjectProperty<>(Color.web("#3b82f6"));
    private ObjectProperty<Color> backgroundColor = new SimpleObjectProperty<>(Color.web("#e5e7eb"));
    private ObjectProperty<Color> textColor = new SimpleObjectProperty<>(Color.web("#1d4ed8"));

    private static final PseudoClass PSEUDO_CLASS_DETERMINATE =
          PseudoClass.getPseudoClass("determinate");
    private static final PseudoClass PSEUDO_CLASS_INDETERMINATE =
            PseudoClass.getPseudoClass("indeterminate");

    // Construtores
    public CircleProgress(int radius, int size) {
        this(radius, size, radius, 0f);
    }

    public CircleProgress(int radius, int size, double translate, float values) {
        init(radius, size, translate, values);
    }

    private void init(int radius, int size, double translate, float initialValue) {


        // Arco de fundo (cinza claro)
        backgroundArc = createArc(radius, 360, backgroundColor.get());
        backgroundArc.getStyleClass().add("arc-background");

        // Arco de progresso (colorido)
        progressArc = createArc(radius, 0, progressColor.get());
        progressArc.getStyleClass().add("arc-progress");
        progressArc.setEffect(createGlowEffect());

        // Label da porcentagem
        percentLabel = new Label("0%");
        percentLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, size * 0.22));
        percentLabel.setTextFill(textColor.get());
        percentLabel.setAlignment(Pos.CENTER);
        percentLabel.getStyleClass().add("percent-label");

        percentLabel.setLayoutX(0);
        percentLabel.setLayoutY(0);

        // Label do subtítulo (opcional)
        subtitleLabel = new Label("");
        subtitleLabel.setFont(Font.font("Roboto", FontWeight.MEDIUM, size * 0.08));
        subtitleLabel.setTextFill(Color.web("#6b7280"));
        subtitleLabel.setAlignment(Pos.CENTER);
        subtitleLabel.getStyleClass().add("subtitle-label");

        subtitleLabel.setLayoutY(((size)/2)*-1);
        subtitleLabel.setLayoutX(((size)/2));
        //subtitleLabel.setLayoutY(radius + size * 0.1);
        getChildren().addAll(backgroundArc, progressArc, percentLabel, subtitleLabel);
        percentLabel.setPrefSize(size*2, size*2);
        // Configurar gradientes
        setupGradients();

        // Bind de cores
        progressColor.addListener((obs, oldVal, newVal) -> updateProgressColor());
        backgroundColor.addListener((obs, oldVal, newVal) -> updateBackgroundColor());
        textColor.addListener((obs, oldVal, newVal) -> percentLabel.setTextFill(newVal));

        setValue(initialValue);
    }

    private Arc createArc(int radius, int length, Color color) {
        Arc arc = new Arc();
        arc.setCenterX(radius);
        arc.setCenterY(radius);
        arc.setRadiusX(radius - 4);
        arc.setRadiusY(radius - 4);
        arc.setStartAngle(90);
        arc.setLength(length);
        arc.setType(ArcType.OPEN);
        arc.setStrokeWidth(radius * 0.15);
        arc.setStroke(color);
        arc.setFill(null);
        arc.setStrokeLineCap(javafx.scene.shape.StrokeLineCap.ROUND);
        return arc;
    }

    private void setupGradients() {
        // Gradiente para o arco de progresso
        LinearGradient gradient = new LinearGradient(
            0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#93c5fd")),
            new Stop(0.5, Color.web("#3b82f6")),
            new Stop(1, Color.web("#2563eb"))
        );
        progressArc.setStroke(gradient);
    }

    private void updateProgressColor() {
        if (progressValue > 0.8) {
            // Azul forte para >80%
            LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#60a5fa")),
                new Stop(1, Color.web("#2563eb"))
            );
            progressArc.setStroke(gradient);
        } else if (progressValue > 0.5) {
            // Azul medio para 50-80%
            LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#93c5fd")),
                new Stop(1, Color.web("#3b82f6"))
            );
            progressArc.setStroke(gradient);
        } else {
            // Âmbar para <50%
            LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#bfdbfe")),
                new Stop(1, Color.web("#60a5fa"))
            );
            progressArc.setStroke(gradient);
        }
    }

    private void updateBackgroundColor() {
        backgroundArc.setStroke(backgroundColor.get());
    }

    private Glow createGlowEffect() {
        Glow glow = new Glow();
        glow.setLevel(0.3);
        return glow;
    }

    private DropShadow createShadowEffect() {
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(37, 99, 235, 0.26));
        shadow.setRadius(10);
        shadow.setOffsetX(0);
        shadow.setOffsetY(4);
        return shadow;
    }

    public void setValue(double perc) {
        this.progressValue = Math.min(1.0, Math.max(0.0, perc));

        // Cancelar animação anterior
        if (animationTimeline != null && animationTimeline.getStatus() == Timeline.Status.RUNNING) {
            animationTimeline.stop();
        }

        // Animar o progresso
        animationTimeline = new Timeline(
            new KeyFrame(Duration.millis(0), new KeyValue(progressProperty(), 0)),
            new KeyFrame(Duration.millis(800), new KeyValue(progressProperty(), progressValue * 360))
        );
        animationTimeline.setAutoReverse(false);
        animationTimeline.play();

        // Atualizar cor baseada no valor
        updateProgressColor();
    }

    public void setValueWithText(double perc, String subtitle) {
        setValue(perc);
        if (subtitleLabel != null) {
            subtitleLabel.setText(subtitle);
        }
    }

    public void setSubtitle(String text) {
        subtitleLabel.setText(text);
    }

    public final DoubleProperty progressProperty() {
        if (progress == null) {
            progress = new DoublePropertyBase(-1.0) {
                @Override
                protected void invalidated() {
                    setIndeterminate(getProgress() < 0.0);
                    updateArcLength();
                }

                @Override
                public Object getBean() {
                    return CircleProgress.this;
                }

                @Override
                public String getName() {
                    return "progress";
                }
            };
        }
        return progress;
    }

    private void updateArcLength() {
        if (progress != null && progress.get() > 0) {
            double angle = progress.get();
            progressArc.setLength(-angle); // Negativo para sentido horário
            int percentValue = (int) Math.round((angle / 360.0) * 100);
            percentLabel.setText(percentValue + "%");

            // Atualizar cor baseada no percentual
            if (percentValue >= 80) {
                percentLabel.setStyle("-fx-text-fill: #2563eb;");
            } else if (percentValue >= 50) {
                percentLabel.setStyle("-fx-text-fill: #3b82f6;");
            } else {
                percentLabel.setStyle("-fx-text-fill: #60a5fa;");
            }
        }
    }

    public final double getProgress() {
        return progress == null ? INDETERMINATE_PROGRESS : progress.get();
    }

    private void setIndeterminate(boolean value) {
        indeterminatePropertyImpl().set(value);
    }

    private ReadOnlyBooleanWrapper indeterminatePropertyImpl() {
        if (indeterminate == null) {
            indeterminate = new ReadOnlyBooleanWrapper(true) {
                @Override
                protected void invalidated() {
                    final boolean active = get();
                    pseudoClassStateChanged(PSEUDO_CLASS_INDETERMINATE, active);
                    pseudoClassStateChanged(PSEUDO_CLASS_DETERMINATE, !active);
                }

                @Override
                public Object getBean() {
                    return CircleProgress.this;
                }

                @Override
                public String getName() {
                    return "indeterminate";
                }
            };
        }
        return indeterminate;
    }

    // Getters e Setters para customização
    public void setProgressColor(Color color) {
        this.progressColor.set(color);
    }

    public void setBackgroundColor(Color color) {
        this.backgroundColor.set(color);
    }

    public void setTextColor(Color color) {
        this.textColor.set(color);
    }

    public void setArcStrokeWidth(double width) {
        progressArc.setStrokeWidth(width);
        backgroundArc.setStrokeWidth(width);
    }

    public void setPercentFontSize(double size) {
        percentLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, size));
    }
}
