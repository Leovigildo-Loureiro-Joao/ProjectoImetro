package com.imetro.util;

import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;


import javafx.scene.image.Image;

public class ImageUtils {

    public static Image converter(BufferedImage buffered) {
        return SwingFXUtils.toFXImage(buffered, null);
    }
}