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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;

public class BibliotecaController implements Initializable  {

    private PDDocument document;
    private PDFRenderer renderer;
    private static String disciplinaPreferida;
    private static UUID livroParaAbrir;
    private static int paginaParaAbrir = -1;

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

    private double zoom = 1;
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
    private List<BibliotecaLivroDto> listaLivrosAtual;
    private String filtroAtual = "mybooks";



    public static void definirDisciplinaPreferida(String disciplina) {
        disciplinaPreferida = disciplina;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        pdfViewer.setVisible(false);
        biblioteca.setVisible(true);
        modalPai.setVisible(false);
        
        pdfList.setVisible(false);
        questionProgressBar.setVisible(true);

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

        disciplinaCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                carregarLivrosDaDisciplina(newVal.id());
            }
        });

        Platform.runLater(() -> {
            UUID pendingId = livroParaAbrir;
            int pendingPagina = paginaParaAbrir;
            if (pendingId != null) {
                livroParaAbrir = null;
                paginaParaAbrir = -1;
                try {
                    servoce.encontrarLivro(pendingId).ifPresent(livro -> abrirLivro(livro, pendingPagina));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

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

        search.textProperty().addListener((obs, oldVal, newVal) -> aplicarFiltro());

        scrollPane.vvalueProperty().addListener((obs, oldVal, newVal) -> {

                for (Node node : pageContainer.getChildren()) {
                    Bounds bounds = node.localToScene(node.getBoundsInLocal());

                    if (bounds.getMinY() >= 0 && bounds.getMinY() < 300) {
                        int page = (int) node.getUserData();

                        if (page != currentPage) {
                            currentPage = page;
                            lblPaginaAtual.setText("Página " + (currentPage + 1));
                            preloadPages(page);
                        }
                        break;
                    }
                }
            });

    }


    private void navigate(String key) {
        filtroAtual = key;
        switch (key) {
            case "mybooks":
                textTitle.setText("Meus Livros");
                break;
            case "download":
                textTitle.setText("Baixados");
                break;
            case "recomendados":
                textTitle.setText("Recomendados");
                break;
        }
        aplicarFiltro();
    }

    private void carregarLivrosDaDisciplina(UUID disciplinaId) {
        pdfList.setVisible(false);
        questionProgressBar.setVisible(true);

        Task<List<BibliotecaLivroDto>> task = new Task<>() {
        @Override
        protected List<BibliotecaLivroDto> call() {
            try {
                return servoce.listarLivros(disciplinaId);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return List.of();
        }

        @Override
        protected void succeeded() {
            questionProgressBar.setVisible(false); // Esconder aqui também
            carregarLivros(getValue());
        }

        @Override
        protected void failed() {
            questionProgressBar.setVisible(false); // Esconder aqui também
            getException().printStackTrace();              // TODO: Mostrar feedback de erro para o usuário
        }
    };
        App.getExecutorService().execute(task);
    }
    private void carregarLivros(List<BibliotecaLivroDto> livros) {
        this.listaLivrosAtual = livros;
        aplicarFiltro();
    }

    private void aplicarFiltro() {
        if (listaLivrosAtual == null) return;

        pdfList.getChildren().clear();

        List<BibliotecaLivroDto> filtrados = listaLivrosAtual.stream()
            .filter(livro -> passarFiltro(livro))
            .filter(livro -> passarBusca(livro))
            .toList();

        for (BibliotecaLivroDto livro : filtrados) {
            Image capa;
            byte[] thumb = livro.capaThumbnail();
            if (thumb != null) {
                capa = new Image(new ByteArrayInputStream(thumb));
            } else {
                capa = null;
            }
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

        if (filtrados.isEmpty()) {
            Label vazio = new Label("Nenhum livro encontrado.");
            vazio.getStyleClass().add("muted");
            pdfList.getChildren().add(vazio);
        }

        pdfList.setVisible(true);
    }

    private boolean passarFiltro(BibliotecaLivroDto livro) {
        if ("mybooks".equals(filtroAtual)) return true;
        if ("recomendados".equals(filtroAtual)) return true;
        if ("download".equals(filtroAtual)) return true;
        return true;
    }

    private boolean passarBusca(BibliotecaLivroDto livro) {
        String termo = search.getText();
        if (termo == null || termo.isBlank()) return true;
        String q = termo.toLowerCase();
        if (livro.titulo() != null && livro.titulo().toLowerCase().contains(q)) return true;
        if (livro.nomeArquivo() != null && livro.nomeArquivo().toLowerCase().contains(q)) return true;
        return false;
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
        abrirLivro(livro, -1);
    }

    private void abrirLivro(BibliotecaLivroDto livro, int pagina) {

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
                abrirDocumento(dados, pagina);
            }

            @Override
            protected void failed() {
                getException().printStackTrace();
                voltarBiblioteca();
            }
        };

        App.getExecutorService().execute(task);
    }

    public static void abrirLivroNaPagina(UUID disciplinaId, String nomeLivro, int pagina) {
        try {
            BibliotecaLivroService svc = new BibliotecaLivroService();
            Optional<BibliotecaLivroDto> opt = disciplinaId != null
                ? svc.encontrarLivroPorNome(disciplinaId, nomeLivro)
                : svc.encontrarLivroPorNome(nomeLivro);
            opt.ifPresent(livro -> {
                definirDisciplinaPreferida(livro.disciplinaNome());
                livroParaAbrir = livro.id();
                paginaParaAbrir = pagina < 0 ? 0 : pagina - 1;
                Platform.runLater(() -> {
                    StackPane contentHost = (StackPane) com.imetro.App.scene.lookup("#contentHost");
                    if (contentHost != null) {
                        try {
                            com.imetro.App.swapContent(contentHost, "views/pages/candidato/livro");
                        } catch (Exception ignored) {}
                    }
                });
            });
        } catch (Exception ignored) {}
    }

    public void abrirDocumento(byte[] pdfBytes) {
        abrirDocumento(pdfBytes, -1);
    }

    public void abrirDocumento(byte[] pdfBytes, int paginaParaIr) {
        CompletableFuture.runAsync(() -> {
            try {
                if (this.document != null) {
                    this.document.close();
                }
                this.document = Loader.loadPDF(pdfBytes);
                this.renderer = new PDFRenderer(document);
            } catch (IOException e) {
                throw new RuntimeException("Falha ao carregar o documento PDF", e);
            }
        }, App.getExecutorService()).thenRun(() -> {
            Platform.runLater(() -> {
                if (document == null) return;
                int total = document.getNumberOfPages();
                pageContainer.getChildren().clear();
                for (int i = 0; i < total; i++) {
                    StackPane wrapper = new StackPane();
                    wrapper.setAlignment(javafx.geometry.Pos.CENTER);
                    wrapper.getStyleClass().add("page-area");
                    wrapper.setUserData(i);
                    ImageView view = createPageView(i);
                    wrapper.getChildren().add(view);
                    pageContainer.getChildren().add(wrapper);
                }
                int paginaDestino = paginaParaIr >= 0 && paginaParaIr < total ? paginaParaIr : 0;
                preloadPages(paginaDestino);
                if (paginaDestino > 0 && total > 1) {
                    scrollPane.setVvalue((double) paginaDestino / (total - 1));
                }
                lblPaginaAtual.setText("Página " + (paginaDestino + 1));
                lblInfo.setText("Total de " + total + " páginas");
            });
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });
    }

    private ImageView createPageView(int pageIndex) {

        ImageView view = new ImageView();

        view.setPreserveRatio(true);
        view.setFitWidth(500 * zoom);

        view.setUserData(pageIndex);

        view.imageProperty().set(null);

        view.viewportProperty();

        // Renderiza a página quando a view entra na cena
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

        CompletableFuture.runAsync(() -> {
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
        }, App.getExecutorService());
    }

    private void preloadPages(int page) {

        int[] targets = { page - 1, page + 1, page + 2 };

        for (int p : targets) {
            if (p < 0 || p >= document.getNumberOfPages()) continue;
            if (cache.containsKey(p)) continue;

            CompletableFuture.runAsync(() -> {
                try {
                    BufferedImage buffered =
                            renderer.renderImageWithDPI(p, (int)(150 * zoom));

                    Image img = SwingFXUtils.toFXImage(buffered, null);
                    cache.put(p, img);

                } catch (Exception ignored) {}
            }, App.getExecutorService());
        }
    }

    private void navegarPagina(int pag, BibliotecaLivroDto livro) {

        Task<Image> task = new Task<>() {
            @Override
            protected Image call() throws IOException {
                if (renderer == null) {
                    throw new IOException("PDF Renderer não foi inicializado.");
                }
                BufferedImage bufferedImage = renderer.renderImageWithDPI(pag, 150);
                return SwingFXUtils.toFXImage(bufferedImage, null);
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

        App.getExecutorService().execute(task);
    }

    

    @FXML
    private void voltarBiblioteca() {
        pdfViewer.setVisible(false);
        biblioteca.setVisible(true);
        if (document != null) {
            try {
                document.close();
            } catch (IOException ignored) {}
            document = null;
            renderer = null;
        }
        cache.clear();
    }

    @FXML
    private void Procurar(ActionEvent event) {
        aplicarFiltro();
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
            questionProgressBar.setVisible(false);
            pdfList.getChildren().setAll(new Label("Nenhuma disciplina disponível."));
            pdfList.setVisible(true);
            return;
        }

        disciplinaCombo.getSelectionModel().selectFirst();
        aplicarDisciplinaPreferidaSeExistir();

        // Garante o carregamento inicial dos livros para a disciplina selecionada.
        DisciplinaOption disciplinaInicial = disciplinaCombo.getSelectionModel().getSelectedItem();
        if (disciplinaInicial != null) {
            carregarLivrosDaDisciplina(disciplinaInicial.id());
        }
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
        if (document != null) {
            scrollPane.setVvalue(0);
        }
    }

    @FXML
    private void LastPag(ActionEvent event) {
        if (document != null) {
            scrollPane.setVvalue(1.0);
        }
    }

    @FXML
    private void PagAnterior(ActionEvent event) {
        if (document != null && currentPage > 0) {
            double targetV = (double) (currentPage - 1) / (document.getNumberOfPages() - 1);
            scrollPane.setVvalue(targetV);
        }
    }

    @FXML
    private void PagSeguinte(ActionEvent event) {
        if (document != null && currentPage < document.getNumberOfPages() - 1) {
            // Calcula a posição aproximada da próxima página
            double pageHeight = pageContainer.getHeight() / document.getNumberOfPages();
            double currentPixel = scrollPane.getVvalue() * (pageContainer.getHeight() - scrollPane.getViewportBounds().getHeight());
            double nextPixel = currentPixel + pageHeight;
            scrollPane.setVvalue(nextPixel / (pageContainer.getHeight() - scrollPane.getViewportBounds().getHeight()));
        }
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
