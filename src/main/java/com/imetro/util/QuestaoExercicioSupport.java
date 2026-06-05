package com.imetro.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Optional;

import javax.swing.JLabel;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

public final class QuestaoExercicioSupport {

    private static final float TEX_SIZE = 50f;
    private static final int PADDING_VERTICAL = 12;
    private static final int PADDING_HORIZONTAL = 18;

    private QuestaoExercicioSupport() {
    }

    public static Optional<Image> render(String exercicio) {
        String normalizado = normalizar(exercicio);
        if (normalizado.isBlank()) {
            return Optional.empty();
        }

        try {
            TeXFormula formula = new TeXFormula(normalizado);
            TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, TEX_SIZE);
            icon.setInsets(new java.awt.Insets(PADDING_VERTICAL, PADDING_HORIZONTAL, PADDING_VERTICAL, PADDING_HORIZONTAL));

            BufferedImage imagem = new BufferedImage(
                Math.max(1, icon.getIconWidth()),
                Math.max(1, icon.getIconHeight()),
                BufferedImage.TYPE_INT_ARGB
            );

            Graphics2D graphics = imagem.createGraphics();
            try {
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                icon.paintIcon(new JLabel(), graphics, 0, 0);
            } finally {
                graphics.dispose();
            }

            return Optional.of(SwingFXUtils.toFXImage(imagem, null));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    private static String normalizar(String exercicio) {
        if (exercicio == null) {
            return "";
        }

        String texto = exercicio.trim();
        if (texto.isBlank()) {
            return "";
        }

        if ((texto.startsWith("$$") && texto.endsWith("$$")) || (texto.startsWith("$") && texto.endsWith("$"))) {
            texto = texto.substring(texto.startsWith("$$") ? 2 : 1, texto.length() - (texto.startsWith("$$") ? 2 : 1));
        } else if (texto.startsWith("\\(") && texto.endsWith("\\)")) {
            texto = texto.substring(2, texto.length() - 2);
        } else if (texto.startsWith("\\[") && texto.endsWith("\\]")) {
            texto = texto.substring(2, texto.length() - 2);
        }

        texto = texto.replace('\r', ' ').replace('\n', ' ');
        texto = texto.replace("\u00D7", "\\times");
        texto = texto.replace("\u00B7", "\\cdot");
        texto = texto.replace("\u00F7", "\\div");
        texto = texto.replace("\u2212", "-");
        texto = texto.replace("\u2013", "-");
        texto = texto.replace("\u2264", "\\leq");
        texto = texto.replace("\u2265", "\\geq");
        texto = texto.replace("\u03C0", "\\pi");
        texto = texto.replace("\u221E", "\\infty");
        texto = texto.replace("\u00B0", "^\\circ");
        texto = texto.replaceAll("\\s+", " ").trim();

        return texto;
    }
}
