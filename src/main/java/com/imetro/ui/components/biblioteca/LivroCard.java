package com.imetro.ui.components.biblioteca;

import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

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

        capaView.setFitWidth(180);
        capaView.setFitHeight(240);
        capaView.setPreserveRatio(false);

        StackPane capaContainer = new StackPane(capaView);

        capaContainer.getStyleClass()
                .add("livro-capa");


        //-------------------------
        // TÍTULO
        //-------------------------

        Label lblTitulo = new Label(titulo);

        lblTitulo.getStyleClass()
                .add("livro-titulo");


        Label lblAutor = new Label(autor);

        lblAutor.getStyleClass()
                .add("livro-autor");


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


        //-------------------------
        // CARD
        //-------------------------

        VBox info = new VBox(
                5,
                lblTitulo,
                lblAutor,
                disciplinaBox,
                paginasBox,
                progressoTexto,
                barra,
                btnLer
        );

        info.setPadding(
                new Insets(10, 5, 5, 5)
        );


        getChildren().addAll(
                capaContainer,
                info
        );


        setSpacing(10);
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
                        8,
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