package com.imetro.util;

import javafx.scene.control.ProgressIndicator;

public class Loading {
    public static ProgressIndicator load() {
         ProgressIndicator progress = new ProgressIndicator();
            progress.setMaxSize(30, 30);
            progress.getStyleClass().add("loading-progress");
        return progress;
    }
}
