package com.imetro.util;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PdfUtils {

    public static BufferedImage extrairCapa(File arquivo) throws IOException {

        try (PDDocument documento = Loader.loadPDF(arquivo)) {

            PDFRenderer renderer = new PDFRenderer(documento);

            return renderer.renderImageWithDPI(0, 100);
        }
    }
}