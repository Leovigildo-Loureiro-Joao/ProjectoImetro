// com/imetro/ui/animation/AuthBackgroundAnimator.java
package com.imetro.ui.animation;

import javafx.animation.*;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class AuthBackgroundAnimator {
    
    private static final String[] GRADIENTS = {
        "-fx-background-color: linear-gradient(135deg, #667eea 0%, #764ba2 100%);",
        "-fx-background-color: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);",
        "-fx-background-color: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);",
        "-fx-background-color: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);",
        "-fx-background-color: linear-gradient(135deg, #fa709a 0%, #fee140 100%);"
    };
    
    public static void animateBackground(Pane rootPane) {
        Timeline timeline = new Timeline();
        
        for (int i = 0; i < GRADIENTS.length; i++) {
            KeyFrame keyFrame = new KeyFrame(
                Duration.seconds(i * 8),
                new KeyValue(rootPane.styleProperty(), GRADIENTS[i], Interpolator.LINEAR)
            );
            timeline.getKeyFrames().add(keyFrame);
        }
        
        // Voltar ao primeiro gradiente
        KeyFrame lastFrame = new KeyFrame(
            Duration.seconds(GRADIENTS.length * 8),
            new KeyValue(rootPane.styleProperty(), GRADIENTS[0], Interpolator.LINEAR)
        );
        timeline.getKeyFrames().add(lastFrame);
        
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}