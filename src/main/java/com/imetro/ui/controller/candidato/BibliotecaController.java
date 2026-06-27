package com.imetro.ui.controller.candidato;

import com.imetro.App;
import com.imetro.domain.dto.MenuEntry;
import com.imetro.domain.dto.biblioteca.BibliotecaLivroDto;
import com.imetro.domain.dto.progresso.ProgressoAlunoDisciplinaDto;
import com.imetro.services.BibliotecaLivroService;
import com.imetro.services.DisciplinaService;
import com.imetro.ui.components.Item_Cell;
import com.imetro.ui.components.biblioteca.LivroCard;
import com.imetro.ui.modals.AddLivroModalController;
import com.imetro.ui.modals.ModalController;
import com.imetro.ui.modals.AddLivroModalController.DisciplinaOption;
import com.imetro.util.TextoUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import java.awt.image.BufferedImage;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;

public class BibliotecaController implements Initializable  {

    private final ExecutorService executor =
            Executors.newFixedThreadPool(2);
    private PDDocument document;
    private PDFRenderer renderer;
    private static String disciplinaPreferida;

    private FXMLLoader modFxml;
    private Node mod;
    private Node modTop;
    private ModalController cont;
    private BibliotecaLivroDto livro;
    private final Map<Integer, Image> cache =
            Collections.synchronizedMap(
                    new LinkedHashMap<>(50, 0.75f, true) {
                        @Override
                        protected boolean removeEldestEntry(Map.Entry<Integer, Image> eldest) {
                            return size() > 50; // cache LRU
                        }
                    }
            );

    private double zoom = 1.5;
    private int currentPage = 0;

    @FXML private ScrollPane scrollPane;

    @FXML
    private JFXButton btnAnterior;
    @FXML
    private VBox pageContainer;

    @FXML
    private JFXComboBox<AddLivroModalController.DisciplinaOption> disciplinaCombo;

    @FXML
    private JFXButton btnAnterior1;

    @FXML
    private JFXButton btnProxima;

    @FXML
    private JFXButton btnProxima1;

    @FXML
    private JFXButton btnVoltar;


    @FXML
    private ImageView imgPagina;

    @FXML
    private Label lblInfo;

    @FXML
    private Label lblPaginaAtual;

    @FXML
    private Label lblTitulo;

    @FXML
    private FlowPane pdfList;

    @FXML
    private ProgressBar questionProgressBar;


    @FXML
    private VBox biblioteca;


    @FXML
    private TextField search;

    @FXML
    private ListView<MenuEntry> sublist;

    @FXML
    private StackPane pdfViewer;


    @FXML
    private Label textTitle;

    @FXML
    private StackPane modalPai;
    private BibliotecaLivroService servoce;
    private byte[] dados;



    public static void definirDisciplinaPreferida(String disciplina) {
        disciplinaPreferida = disciplina;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        pdfViewer.setVisible(false);
        biblioteca.setVisible(true);
        modalPai.setVisible(false);

        servoce=new BibliotecaLivroService();
        sublist.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(MenuEntry item, boolean empty) {
                super.updateItem(item, empty);
                setText(null);
                setGraphic(empty || item == null ? null : new Item_Cell(item.title(), item.icon()));
            }
        });
        carregarDisciplinas();
        try {
            DisciplinaOption dis=disciplinaCombo.getSelectionModel().getSelectedItem();
            if (dis!=null) {
                servoce.listarLivros(disciplinaCombo.getSelectionModel().getSelectedItem().id());
                carregarLivros(servoce.listarLivros(disciplinaCombo.getSelectionModel().getSelectedItem().id()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        sublist.getItems().setAll(
            new MenuEntry("mybooks", "Meus Livros", FontAwesomeSolid.BOOK),
            new MenuEntry("download", "Baixados", FontAwesomeSolid.DOWNLOAD),
            new MenuEntry("recomendados", "Recomendados", FontAwesomeSolid.RECEIPT)
        );

        sublist.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                navigate(newValue.key());
            }
        });

        sublist.getSelectionModel().selectFirst();
        scrollPane.vvalueProperty().addListener((obs, oldVal, newVal) -> {

                for (Node node : pageContainer.getChildren()) {
                    Bounds bounds = node.localToScene(node.getBoundsInLocal());

                    if (bounds.getMinY() >= 0 && bounds.getMinY() < 300) {
                        int page = (int) node.getUserData();

                        if (page != currentPage) {
                            currentPage = page;
                            preloadPages(page);
                        }
                        break;
                    }
                }
            });

    }


    private void navigate(String key) {
        switch (key) {
            case "mybooks" :
                textTitle.setText("Meus Livros");
                break;

            case "download":
                 textTitle.setText("Baixados");
            break;

            case "recomendados" :
                textTitle.setText("Recomendados");
            break;
            }
    }

    private void carregarLivros(List<BibliotecaLivroDto> livros) {

        pdfList.getChildren().clear();

        for (BibliotecaLivroDto livro : livros) {
            Image capa = new Image(
                new ByteArrayInputStream(
                    livro.capaThumbnail()
                )
            );
            LivroCard card = new LivroCard(
                capa,
                livro.titulo(),
                "Autor desconhecido",
                livro.disciplinaNome(),
                livro.totalPaginas(),
                0.0
            );

            card.getBtnLer().setOnAction(e -> {
               abrirLivro(livro);
            });

            pdfList.getChildren().add(card);
        }
    }

    @FXML
    private void AdicionarLivro(ActionEvent event) {
        try {
            modalPai.getChildren().clear();
            modFxml = App.loadFXMLModal("AddLivro");
            mod = modFxml.load();
            modalPai.getChildren().add(mod);
            cont = modFxml.getController();
            cont.init();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void abrirLivro(BibliotecaLivroDto livro) {

        biblioteca.setVisible(false);
        pdfViewer.setVisible(true);

        Task<byte[]> task = new Task<>() {
            @Override
            protected byte[] call() throws Exception {
                return servoce.carregarPdf(livro.id())
                        .orElseThrow(() -> new IOException("PDF vazio"));
            }

            @Override
            protected void succeeded() {
                dados = getValue();
                open(dados);
            }

            @Override
            protected void failed() {
                getException().printStackTrace();
            }
        };

        new Thread(task).start();
    }

   public void open(byte[] pdfBytes) {

        executor.submit(() -> {
            try {

                this.document = Loader.loadPDF(pdfBytes);
                this.renderer = new PDFRenderer(document);

                int pages = document.getNumberOfPages();

                Platform.runLater(() -> {
                    pageContainer.getChildren().clear();

                    for (int i = 0; i < pages; i++) {
                        ImageView view = createPageView(i);
                        pageContainer.getChildren().add(view);
                    }

                    preloadPages(0);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private ImageView createPageView(int pageIndex) {

        ImageView view = new ImageView();

        view.setPreserveRatio(true);
        view.setFitWidth(500 * zoom);

        view.setUserData(pageIndex);

        view.imageProperty().set(null);

        view.viewportProperty();

        // render assíncrono ao aparecer
        view.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                renderPageAsync(pageIndex, view);
            }
        });

        return view;
    }


    private void renderPageAsync(int page, ImageView view) {

        if (cache.containsKey(page)) {
            view.setImage(cache.get(page));
            return;
        }

        executor.submit(() -> {
            try {

                BufferedImage buffered =
                        renderer.renderImageWithDPI(page, (int)(150 * zoom));

                Image fxImage =
                        SwingFXUtils.toFXImage(buffered, null);

                cache.put(page, fxImage);

                Platform.runLater(() -> {
                    if ((int) view.getUserData() == page) {
                        view.setImage(fxImage);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void preloadPages(int page) {

        int[] targets = { page - 1, page + 1, page + 2 };

        for (int p : targets) {
            if (p < 0 || p >= document.getNumberOfPages()) continue;
            if (cache.containsKey(p)) continue;

            executor.submit(() -> {
                try {
                    BufferedImage buffered =
                            renderer.renderImageWithDPI(p, (int)(150 * zoom));

                    Image img = SwingFXUtils.toFXImage(buffered, null);
                    cache.put(p, img);

                } catch (Exception ignored) {}
            });
        }
    }

    private void navegarPagina(int pag, BibliotecaLivroDto livro) {

        Task<Image> task = new Task<>() {
            @Override
            protected Image call() throws Exception {

                try (PDDocument document = Loader.loadPDF(dados)) {

                    PDFRenderer renderer = new PDFRenderer(document);

                    BufferedImage bufferedImage =
                            renderer.renderImageWithDPI(pag, 150);

                    return SwingFXUtils.toFXImage(bufferedImage, null);
                }
            }

            @Override
            protected void succeeded() {
                imgPagina.setImage(getValue());
            }

            @Override
            protected void failed() {
                getException().printStackTrace();
            }
        };

        new Thread(task).start();
    }

    

    @FXML
    private void Procurar(ActionEvent event) {

    }

    private void carregarDisciplinas() {
        LinkedHashMap<UUID, AddLivroModalController.DisciplinaOption> opcoes = new LinkedHashMap<>();
        for (ProgressoAlunoDisciplinaDto progresso : DisciplinaService.getProgressoDisciplinasCandidatoSafe()) {
            if (progresso == null || progresso.disciplinaId() == null) {
                continue;
            }

            String nomeDisciplina = firstNonBlank(
                progresso.disciplina(),
                DisciplinaService.findByNomeIdSearch(progresso.disciplinaId())
            );
            if (!DisciplinaService.isDisciplinaSuportada(nomeDisciplina)) {
                continue;
            }

            opcoes.putIfAbsent(progresso.disciplinaId(), new AddLivroModalController.DisciplinaOption(progresso.disciplinaId(), nomeDisciplina));
        }

        disciplinaCombo.setItems(FXCollections.observableArrayList(opcoes.values()));
        if (disciplinaCombo.getItems().isEmpty()) {
            return;
        }

        disciplinaCombo.getSelectionModel().selectFirst();
        aplicarDisciplinaPreferidaSeExistir();
    }

    private String firstNonBlank(String first, String second) {
        if (first != null && !first.isBlank()) {
            return first.trim();
        }
        if (second != null && !second.isBlank()) {
            return second.trim();
        }
        return "";
    }

    private void aplicarDisciplinaPreferidaSeExistir() {
        String preferida = disciplinaPreferida;
        disciplinaPreferida = null;

        if (preferida == null || preferida.isBlank() || disciplinaCombo.getItems().isEmpty()) {
            return;
        }

        String alvo = TextoUtil.normalizarMinusculo(preferida);
        disciplinaCombo.getItems().stream()
            .filter(item -> TextoUtil.normalizarMinusculo(item.nome()).equals(alvo))
            .findFirst()
            .ifPresent(item -> disciplinaCombo.getSelectionModel().select(item));
    }


    @FXML
    private void InitPag(ActionEvent event) {
        if(livro!=null)
            navegarPagina(0, livro);
    }

    @FXML
    private void LastPag(ActionEvent event) {
      
            
    }

    @FXML
    private void PagAnterior(ActionEvent event) {
      
    }

    @FXML
    private void PagSeguinte(ActionEvent event) {
      
    }


    private String construirResumoBiblioteca(String disciplinaNome, int totalLivros) {
        if (totalLivros <= 0) {
            return "A biblioteca de " + disciplinaNome + " ainda esta vazia. Envia o primeiro PDF para gerar perguntas.";
        }
        return totalLivros == 1
            ? "1 livro carregado em " + disciplinaNome + ". Podes relancar a geracao sempre que adicionares novo material."
            : totalLivros + " livros carregados em " + disciplinaNome + ". A disciplina ja esta pronta para releituras.";
    }





}
