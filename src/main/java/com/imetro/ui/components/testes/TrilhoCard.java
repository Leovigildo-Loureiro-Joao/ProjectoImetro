package com.imetro.ui.components.testes;

import com.imetro.domain.dto.test.TrilhoDTO;
import com.imetro.domain.enums.TrilhoStatus;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class TrilhoCard extends HBox {

    private final TrilhoDTO dto;

    public TrilhoCard(TrilhoDTO dto) {
        this.dto = dto;

        definirStatus();
        configurarCard();
        construirLayout();
    }


    private void definirStatus() {

        if (!dto.getTrilho().existis()) {
            dto.setStatus(TrilhoStatus.SEM_DADOS);
            return;
        }

        double progresso = dto.getTrilho().progressoPercentual();

        if (progresso >= 100) {
            dto.setStatus(TrilhoStatus.CONCLUIDO);
        }
        else if (progresso <= 0) {
            dto.setStatus(TrilhoStatus.BLOQUEADO);
        }
        else {
            dto.setStatus(TrilhoStatus.EM_PROGRESSO);
        }
    }


    private void configurarCard() {

        setSpacing(18);
        setAlignment(Pos.CENTER_LEFT);

        setPadding(new Insets(16));

        setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 14;
            -fx-border-radius: 14;
            -fx-border-color: #E8E8E8;
        """);
    }


    private void construirLayout() {

        getChildren().addAll(
                criarNumeroEtapa(), 
                criarConteudo(),
                criarStatus()
        );
    }


    private Label criarNumeroEtapa() {

        Label numero = new Label(
                String.valueOf(dto.getEtapa())
        );

        numero.setMinSize(28, 28);
        numero.setMaxSize(28, 28);

        numero.setAlignment(Pos.CENTER);

        numero.setStyle("""
            -fx-background-color: #FA7602;
            -fx-background-radius: 100;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-font-size: 12;
        """);

        return numero;
    }


    private StackPane criarIcone() {

        StackPane caixa = new StackPane();

        caixa.setPrefSize(42, 42);

        String cor = switch (dto.getStatus()) {

            case CONCLUIDO -> "#FA7602";

            case EM_PROGRESSO -> "#2962FF";

            case BLOQUEADO -> "#9E9E9E";

            case SEM_DADOS -> "#E0E0E0";
        };


        caixa.setStyle("""
            -fx-background-color: %s;
            -fx-background-radius: 10;
        """.formatted(cor));


        ImageView icone = new ImageView(
                new Image(
                    getClass()
                    .getResourceAsStream(
                        "/com/imetro/assets/imgs/marker_48px.png"
                    )
                )
        );


        icone.setFitWidth(22);
        icone.setFitHeight(22);


        if (dto.getStatus() == TrilhoStatus.SEM_DADOS) {
            icone.setOpacity(0.45);
        }


        caixa.getChildren().add(icone);

        return caixa;
    }


    private VBox criarConteudo() {

        if (!dto.getTrilho().existis()) {
            return criarConteudoSemDados();
        }

        return criarConteudoCompleto();
    }


    private VBox criarConteudoSemDados() {

        VBox box = new VBox(6);

        Label titulo = new Label(
                dto.getTrilho().subtopico()
        );


        titulo.setStyle("""
            -fx-font-size: 15;
            -fx-font-weight: bold;
            -fx-text-fill: #555;
        """);


        Label mensagem = new Label(
            "Faça um teste adaptativo para que o KBols "
          + "consiga criar o seu trilho personalizado."
        );


        mensagem.setWrapText(true);


        mensagem.setStyle("""
            -fx-text-fill: #888;
            -fx-font-style: italic;
            -fx-max-width:250;
            -fx-max-height: 80;
        """);


        box.getChildren().addAll(
                titulo,
                mensagem
        );


        HBox.setHgrow(
                box,
                Priority.ALWAYS
        );


        return box;
    }

    private VBox criarConteudoCompleto() {

        VBox principal = new VBox(10);

        // Cabeçalho
        HBox cabecalho = new HBox(16);

        VBox detalhes = new VBox(5);


        Label subtopico = new Label(
                dto.getTrilho().subtopico()
        );

        subtopico.setStyle("""
            -fx-font-size: 15;
            -fx-font-weight: bold;
            -fx-text-fill: #212121;
        """);


        Label observacao = new Label(
                dto.getTrilho().observacao()
        );

        observacao.setWrapText(true);

        observacao.setStyle("""
            -fx-text-fill: #666;
            -fx-font-size: 12;
        """);


        detalhes.getChildren().addAll(
                subtopico,
                observacao
        );


        Region espaco = new Region();

        HBox.setHgrow(
                espaco,
                Priority.ALWAYS
        );


        VBox livroBox = new VBox(4);


        Label livro = new Label(
                dto.getTrilho().recomendacaoLivro()
        );


        Label paginas = new Label(
                "Páginas: "
                + dto.getTrilho().recomendacaoPaginas()
        );


        livro.setStyle("-fx-text-fill:#555;");
        paginas.setStyle("-fx-text-fill:#555;");


        livroBox.getChildren().addAll(
                livro,
                paginas
        );


        cabecalho.getChildren().addAll(
                detalhes,
                espaco,
                livroBox
        );


        // Estatísticas
        HBox estatisticas = new HBox(18);


        Label avancos = new Label(
                "Avanços: "
                + dto.getTrilho().avancosRecentes()
        );


        Label quedas = new Label(
                "Quedas: "
                + dto.getTrilho().quedasRecentes()
        );


        Label dificuldade = new Label(
                "Dificuldade: "
                + dto.getTrilho().dificuldadeMediaPercentual()
                + "%"
        );


        avancos.setStyle("-fx-text-fill:#444;");
        quedas.setStyle("-fx-text-fill:#444;");
        dificuldade.setStyle("-fx-text-fill:#444;");


        estatisticas.getChildren().addAll(
                avancos,
                quedas,
                dificuldade
        );


        // Barra de progresso
        ProgressBar progresso = new ProgressBar(
                dto.getTrilho().progressoPercentual() / 100.0
        );


        progresso.setPrefHeight(8);
        progresso.setMaxWidth(Double.MAX_VALUE);


        VBox.setVgrow(
                progresso,
                Priority.NEVER
        );


        Label progressoTexto = new Label(
                "Progresso "
                + String.format(
                    "%.0f",
                    dto.getTrilho().progressoPercentual()
                )
                + "% | Rigor atual "
                + String.format(
                    "%.0f",
                    dto.getTrilho().rigorAtualPercentual()
                )
                + "% de "
                + String.format(
                    "%.0f",
                    dto.getTrilho().rigorAlvoPercentual()
                )
                + "%"
        );


        progressoTexto.setStyle("""
            -fx-text-fill:#666;
            -fx-font-size:11;
        """);


        principal.getChildren().addAll(
                cabecalho,
                estatisticas,
                progresso,
                progressoTexto
        );


        HBox.setHgrow(
                principal,
                Priority.ALWAYS
        );


        return principal;
    }



    private StackPane criarStatus() {

        StackPane caixa = new StackPane();


        Label status = new Label();


        switch (dto.getStatus()) {


            case CONCLUIDO -> {

                status.setText("Concluído");

                status.setStyle("""
                    -fx-background-color:#FFF0E4;
                    -fx-text-fill:#FA7602;
                    -fx-font-weight:bold;
                    -fx-background-radius:8;
                    -fx-padding:8 12;
                """);
            }


            case EM_PROGRESSO -> {

                status.setText("Estudando");

                status.setStyle("""
                    -fx-background-color:#EAF1FF;
                    -fx-text-fill:#2962FF;
                    -fx-font-weight:bold;
                    -fx-background-radius:8;
                    -fx-padding:8 12;
                """);
            }


            case BLOQUEADO -> {

                status.setText("Bloqueado");

                status.setStyle("""
                    -fx-background-color:#EEEEEE;
                    -fx-text-fill:#757575;
                    -fx-font-weight:bold;
                    -fx-background-radius:8;
                    -fx-padding:8 12;
                """);
            }


            case SEM_DADOS -> {

                status.setText("Diagnóstico");

                status.setStyle("""
                    -fx-background-color:#F5F5F5;
                    -fx-text-fill:#9E9E9E;
                    -fx-font-weight:bold;
                    -fx-background-radius:8;
                    -fx-padding:8 12;
                """);
            }
        }


        caixa.getChildren().add(status);

        return caixa;
    }

}