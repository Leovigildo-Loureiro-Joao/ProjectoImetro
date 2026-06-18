package com.imetro.ui.components.testes;

import org.kordamp.ikonli.javafx.FontIcon;

import com.imetro.domain.dto.test.TrilhoDTO;
import com.imetro.domain.enums.TrilhoStatus;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class TrilhoCard extends VBox {

    private final VBox detalhes = new VBox(12);
    private HBox metricas;
    private boolean expandido;
    private FontIcon arrow;
    private VBox livro;

    public TrilhoCard(TrilhoDTO dto) {
        this.expandido = false;
        metricas=criarMetricas(dto);
        livro=criarLivro(dto);
        Expanded(false);

        getStyleClass().add("trilho-card");

        setSpacing(16);
        setPadding(new Insets(20));

        getChildren().addAll(
                criarHeader(dto),
                criarProgresso(dto),
                metricas,
                livro,
                detalhes
        );

        detalhes.getChildren().add(
                criarDetalhes(dto)
        );
    }

    private void Expanded(boolean expa){
        this.detalhes.setVisible(expa);
        this.detalhes.setManaged(expa);
        this.metricas.setVisible(expa);
        this.metricas.setManaged(expa);
        this.livro.setVisible(expa);
        this.livro.setManaged(expa);
    }

    private HBox criarHeader(TrilhoDTO dto) {
        arrow = new FontIcon("fas-chevron-down");
        arrow.getStyleClass().add("trilho-arrow");
        arrow.setRotate(0);

        HBox root = new HBox(12);

        Label etapa = new Label(
                String.valueOf(dto.getEtapa())
        );

        etapa.getStyleClass().add(
                "trilho-etapa"
        );

        StackPane iconBox = new StackPane();

        iconBox.getStyleClass().add(
                "trilho-icon-box"
        );

        FontIcon icon;

        if (
            !dto.getTrilho().existis()
            || dto.getTrilho().progressoPercentual() <= 0
        ) {

            icon = new FontIcon(
                "fas-lock"
            );

            iconBox.getStyleClass()
                   .add("trilho-icon-locked");

        }
        else {

            icon = new FontIcon(
                "fas-chart-line"
            );
        }

        icon.getStyleClass().add(
                "trilho-icon"
        );

        iconBox.getChildren().add(icon);

        VBox textos = new VBox(4);

        Label titulo = new Label(
                dto.getTrilho().subtopico()
        );

        titulo.getStyleClass().add(
                "trilho-title"
        );

        Label desc = new Label(
                dto.getTrilho().observacao()
        );

        desc.getStyleClass().add(
                "trilho-description"
        );

        textos.getChildren().addAll(
                titulo,
                desc
        );

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        Label percent = new Label(
                String.format(
                        "%.0f%%",
                        dto.getTrilho()
                           .progressoPercentual()
                )
        );

        percent.getStyleClass().add(
                "trilho-percent"
        );

        root.setOnMouseClicked(e -> {
                expandido = !expandido;
                Expanded(expandido);
                arrow.setRotate(expandido ? 180 : 0);
        });

        root.getChildren().addAll(
                etapa,
                iconBox,
                textos,
                spacer,
                percent,
                arrow

        );

        return root;
    }

    private HBox criarMetricas(TrilhoDTO dto) {

        HBox box = new HBox(10);

        box.getChildren().addAll(

            miniCard(
                    "+" +
                    dto.getTrilho()
                       .avancosRecentes(),
                    "Avanços"
            ),

            miniCard(
                    String.format(
                            "%.0f%%",
                            dto.getTrilho()
                               .dificuldadeMediaPercentual()
                    ),
                    "Domínio"
            ),

            miniCard(
                    String.format(
                            "%.0f%%",
                            dto.getTrilho()
                               .rigorAtualPercentual()
                    ),
                    "Rigor"
            )
    );

    return box;
}

    private VBox criarProgresso(TrilhoDTO dto) {

        ProgressBar progresso =
        new ProgressBar(
                dto.getTrilho()
                   .progressoPercentual()
                        / 100.0
        );

        progresso.getStyleClass().add(
                "trilho-progress"
        );

        Label texto = new Label(
                String.format(
                        "%.0f%% concluído",
                        dto.getTrilho()
                        .progressoPercentual()
                )
        );

        texto.getStyleClass().add(
                "trilho-progress-text"
        );

        return new VBox(
                6,
                progresso,
                texto
        );
        }

private VBox miniCard(
    String valor,
    String label
) {

Label v = new Label(valor);

v.getStyleClass().add(
        "trilho-stat-value"
);

Label l = new Label(label);

l.getStyleClass().add(
        "trilho-stat-label"
);

VBox box = new VBox(
        4,
        v,
        l
);

box.getStyleClass().add("trilho-stat-card");

return box;
}

        private VBox criarLivro(TrilhoDTO dto) {

                VBox box = new VBox(4);

                String livroNome =dto.getTrilho().recomendacaoLivro();

                if(livroNome == null|| livroNome.isBlank()){
                        livroNome = "Sem referência";
                }
                HBox livroRow = new HBox(8);

                FontIcon livroIcon =new FontIcon("fas-book-open");

                livroIcon.getStyleClass().add("trilho-book-icon");

                Label livro =new Label(livroNome);

                livro.getStyleClass().add("trilho-book-title");

                livroRow.getChildren().addAll(
                    livroIcon,
                    livro
                );

                String paginasTexto =dto.getTrilho().recomendacaoPaginas();

                if(paginasTexto == null|| paginasTexto.isBlank()){
                        paginasTexto = "Sem páginas recomendadas";
                }

                livro.getStyleClass().add("trilho-book-title");

                HBox paginaRow = new HBox(8);

                FontIcon paginaIcon =new FontIcon("fas-file");
                paginaIcon.getStyleClass().add("trilho-book-icon");

                Label paginas =new Label(paginasTexto);

                paginas.getStyleClass().add("trilho-book-pages");

                paginaRow.getChildren().addAll(paginaIcon,paginas);

                box.getChildren().addAll(livroRow,paginaRow);

                return box;
        }

        private VBox criarDetalhes(TrilhoDTO dto) {

        VBox box = new VBox(8);

        Label titulo =
                new Label("Próximo objetivo");

        titulo.getStyleClass().add("trilho-detail-title");

        Label conteudo =new Label(dto.getTrilho().observacao());

        conteudo.setWrapText(true);

        box.getChildren().addAll(
                titulo,
                conteudo
        );

        return box;
        }
}