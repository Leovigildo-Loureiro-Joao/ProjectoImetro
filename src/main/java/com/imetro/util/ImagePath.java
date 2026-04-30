package com.imetro.util;

import com.imetro.App;

import javafx.scene.image.Image;

public class ImagePath {
     public static Image load(String p) {
        try {
            return new Image(App.class.getResource(p).toExternalForm(), true);
        } catch (Exception e) {
            return null;
        }
    }
}
