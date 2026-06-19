package com.imetro.util;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PdfRenderService {

    public BufferedImage renderPagina(
            File arquivo,
            int numeroPagina,
            float dpi
    ) throws IOException {

        try (PDDocument document = Loader.loadPDF(arquivo)) {

            PDFRenderer renderer = new PDFRenderer(document);

            return renderer.renderImageWithDPI(
                    numeroPagina,
                    dpi
            );
        }
    }

}