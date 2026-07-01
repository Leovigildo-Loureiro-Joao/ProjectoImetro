package com.imetro.ui.components.biblioteca;

import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

import org.kordamp.ikonli.javafx.FontIcon;


public class LivroCard extends VBox {

    private final JFXButton btnLer;

    public LivroCard(
            Image capa,
            String titulo,
            String autor,
            String disciplina,
            int paginas,
            double progresso
    ) {

        //-------------------------
        // CAPA
        //-------------------------

        ImageView capaView = new ImageView(capa);

        capaView.setFitWidth(200);
        capaView.setFitHeight(260);
        capaView.setPreserveRatio(false);

        StackPane capaContainer = new StackPane(capaView);

        capaContainer.getStyleClass()
                .add("livro-capa");

        Rectangle clip = new Rectangle(200, 260);
        clip.setArcWidth(16);
        clip.setArcHeight(16);
        capaContainer.setClip(clip);


        //-------------------------
        // TÍTULO
        //-------------------------

        Label lblTitulo = new Label(titulo);

        lblTitulo.getStyleClass()
                .add("livro-titulo");
        lblTitulo.setMaxWidth(190);


        Label lblAutor = new Label(autor);

        lblAutor.getStyleClass()
                .add("livro-autor");
        lblAutor.setMaxWidth(190);


        //-------------------------
        // INFORMAÇÕES
        //-------------------------

        HBox disciplinaBox = criarInfo(
                "fas-book-open",
                disciplina
        );

        HBox paginasBox = criarInfo(
                "fas-file-alt",
                paginas + " páginas"
        );

        //-------------------------
        // PROGRESSO
        //-------------------------

        Label progressoTexto =
                new Label(
                        (int)(progresso * 100)
                        + "% concluído"
                );

        progressoTexto
                .getStyleClass()
                .add("progresso-texto");


        ProgressBar barra =
                new ProgressBar(progresso);

        barra.getStyleClass()
                .add("progresso-bar");


        //-------------------------
        // BOTÃO
        //-------------------------

        FontIcon playIcon =
                new FontIcon(
                        "fas-play"
                );


        btnLer = new JFXButton(
                "Continuar lendo"
        );

        btnLer.setGraphic(playIcon);

        btnLer.getStyleClass()
                .add("btn-ler");
        btnLer.setMaxWidth(Double.MAX_VALUE);


        //-------------------------
        // CARD
        //-------------------------

        VBox info = new VBox(
                4,
                lblTitulo,
                lblAutor,
                disciplinaBox,
                paginasBox,
                progressoTexto,
                barra,
                btnLer
        );

        info.setPadding(
                new Insets(12, 10, 12, 10)
        );


        getChildren().addAll(
                capaContainer,
                info
        );


        setSpacing(0);
        setAlignment(Pos.TOP_CENTER);

        setPrefWidth(220);
        setMaxWidth(220);


        getStyleClass()
                .add("livro-card");
    }


    private HBox criarInfo(
            String icon,
            String texto
    ) {

        FontIcon fontIcon =
                new FontIcon(icon);

        fontIcon.getStyleClass()
                .add("info-icon");


        Label label =
                new Label(texto);

        label.getStyleClass()
                .add("info-label");


        HBox box =
                new HBox(
                        6,
                        fontIcon,
                        label
                );

        box.setAlignment(Pos.CENTER_LEFT);

        return box;
    }


    public JFXButton getBtnLer() {
        return btnLer;
    }
}
