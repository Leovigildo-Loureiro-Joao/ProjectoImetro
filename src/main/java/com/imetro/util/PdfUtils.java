package com.imetro.util;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PdfUtils {

    public static BufferedImage extrairCapa(File arquivo) throws IOException {

        try (PDDocument documento = Loader.loadPDF(arquivo)) {

            PDFRenderer renderer = new PDFRenderer(documento);

            return renderer.renderImageWithDPI(0, 100);
        }
    }



    public static byte[] gerarThumbnail(byte[] conteudoPdf) throws IOException {

        try (PDDocument document = Loader.loadPDF(conteudoPdf);
            ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            PDFRenderer renderer = new PDFRenderer(document);

            // Renderiza a primeira página como capa
            BufferedImage capa = renderer.renderImageWithDPI(0, 80);

            // Converte a imagem para PNG
            ImageIO.write(capa, "png", output);

            return output.toByteArray();
        }
    }
}