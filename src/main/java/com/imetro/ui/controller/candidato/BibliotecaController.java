package com.imetro.ui.controller.candidato;

import com.imetro.App;
import com.imetro.domain.dto.MenuEntry;
import com.imetro.domain.dto.Topico;
import com.imetro.domain.dto.biblioteca.BibliotecaLivroDto;
import com.imetro.domain.dto.biblioteca.LivroMapaTopicos;
import com.imetro.domain.dto.planejamento.LeituraRecomendada;
import com.imetro.domain.dto.planejamento.PlaneamentoEstudoResumo;
import com.imetro.domain.dto.leitura.LeituraProgresso;
import com.imetro.domain.dto.perguntas.TopicoSubtopico;
import com.imetro.domain.dto.progresso.ProgressoAlunoDisciplinaDto;
import com.imetro.persistence.repository.BibliotecaLivroRepository;
import com.imetro.persistence.repository.LeituraProgressoRepository;
import com.imetro.persistence.repository.LivroMapaTopicosRepository;
import com.imetro.services.BibliotecaLivroService;
import com.imetro.services.DisciplinaService;
import com.imetro.services.GeminiService;
import com.imetro.services.PlaneamentoEstudoService;
import com.imetro.ui.components.Item_Cell;
import com.imetro.ui.components.biblioteca.LivroCard;
import com.imetro.ui.controller.candidato.diagnosticos.DiagnosticoCoordinator;
import com.imetro.ui.controller.candidato.testes.TesteAdaptativoCoordinator;
import com.imetro.ui.modals.AddLivroModalController;
import com.imetro.ui.modals.AddLivroModalController.DisciplinaOption;
import com.imetro.ui.modals.ModalController;
import com.imetro.util.Authentication;
import com.imetro.util.TextoUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.concurrent.CompletableFuture;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.concurrent.CompletableFuture;

public class BibliotecaController implements Initializable {

    private PDDocument document;
    private PDFRenderer renderer;
    private static String disciplinaPreferida;
    private static UUID livroParaAbrir;
    private static int paginaParaAbrir = -1;

    private FXMLLoader modFxml;
    private Node mod;
    private ModalController cont;
    private BibliotecaLivroDto livro;
    private List<LivroMapaTopicos> topicos;
    private int topicoSelecionado = -1;
    private boolean mostrandoTopicos = true;
    private int currentPage = 0;
    private final Map<Integer, Image> cache =
            Collections.synchronizedMap(
                    new LinkedHashMap<>(20, 0.75f, true) {
                        @Override
                        protected boolean removeEldestEntry(Map.Entry<Integer, Image> eldest) {
                            return size() > 20;
                        }
                    }
            );

    private BibliotecaLivroService servoce;
    private GeminiService geminiService;
    private LeituraProgressoRepository leituraProgressoRepository = new LeituraProgressoRepository();
    private final PlaneamentoEstudoService planeamentoService = new PlaneamentoEstudoService();
    private static Boolean seguindoPlano = null;
    private byte[] dados;
    private List<BibliotecaLivroDto> listaLivrosAtual;
    private String filtroAtual = "mybooks";

    @FXML private ScrollPane scrollPane;
    @FXML private VBox pageContainer;
    @FXML private JFXComboBox<DisciplinaOption> disciplinaCombo;
    @FXML private JFXButton btnAnterior;
    @FXML private JFXButton btnProxima;
    @FXML private JFXButton btnVoltar;
    @FXML private Label lblInfo;
    @FXML private Label lblPaginaAtual;
    @FXML private Label lblTitulo;
    @FXML private FlowPane pdfList;
    @FXML private ProgressBar questionProgressBar;
    @FXML private VBox biblioteca;
    @FXML private TextField search;
    @FXML private ProgressBar progressoLeitura;
    @FXML private ListView<MenuEntry> sublist;
    @FXML private StackPane pdfViewer;
    @FXML private Label textTitle;
    @FXML private StackPane modalPai;
    @FXML private JFXButton btnFazerTeste;

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

        servoce = new BibliotecaLivroService();
        geminiService = new GeminiService();
        if (geminiService.isConfigured()) {
            geminiService.setBibliotecaLivroRepository(new BibliotecaLivroRepository());
        }

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
                    servoce.encontrarLivro(pendingId).ifPresent(l -> abrirLivro(l, pendingPagina));
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
            if (newValue != null) navigate(newValue.key());
        });

        sublist.getSelectionModel().selectFirst();
        search.textProperty().addListener((obs, oldVal, newVal) -> aplicarFiltro());
    }

    private void navigate(String key) {
        filtroAtual = key;
        switch (key) {
            case "mybooks": textTitle.setText("Meus Livros"); break;
            case "download": textTitle.setText("Baixados"); break;
            case "recomendados": textTitle.setText("Recomendados"); break;
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
                questionProgressBar.setVisible(false);
                carregarLivros(getValue());
            }
            @Override
            protected void failed() {
                questionProgressBar.setVisible(false);
                getException().printStackTrace();
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
            .filter(l -> passarFiltro(l))
            .filter(l -> passarBusca(l))
            .toList();
        for (BibliotecaLivroDto l : filtrados) {
            Image capa = l.capaThumbnail() != null ? new Image(new ByteArrayInputStream(l.capaThumbnail())) : null;
            LivroCard card = new LivroCard(capa, l.titulo(), "Autor desconhecido", l.disciplinaNome(), l.totalPaginas(), 0.0);
            card.getBtnLer().setOnAction(e -> abrirLivro(l));
            pdfList.getChildren().add(card);
        }
        if (filtrados.isEmpty()) {
            Label vazio = new Label("Nenhum livro encontrado.");
            vazio.getStyleClass().add("muted");
            pdfList.getChildren().add(vazio);
        }
        pdfList.setVisible(true);
    }

    private boolean passarFiltro(BibliotecaLivroDto l) { return true; }

    private boolean passarBusca(BibliotecaLivroDto l) {
        String termo = search.getText();
        if (termo == null || termo.isBlank()) return true;
        String q = termo.toLowerCase();
        if (l.titulo() != null && l.titulo().toLowerCase().contains(q)) return true;
        if (l.nomeArquivo() != null && l.nomeArquivo().toLowerCase().contains(q)) return true;
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

    // ===================== ABRIR LIVRO =====================

    private void abrirLivro(BibliotecaLivroDto l) { abrirLivro(l, -1); }

    private void abrirLivro(BibliotecaLivroDto l, int pagina) {
        biblioteca.setVisible(false);
        pdfViewer.setVisible(true);
        this.livro = l;
        lblTitulo.setText(l.titulo());
        pageContainer.getChildren().clear();
        lblPaginaAtual.setText("");
        lblInfo.setText("A carregar livro…");

        Task<byte[]> task = new Task<>() {
            @Override
            protected byte[] call() throws Exception {
                return servoce.carregarPdf(l.id()).orElseThrow(() -> new IOException("PDF vazio"));
            }
            @Override
            protected void succeeded() {
                dados = getValue();
                App.getExecutorService().execute(() -> {
                    try {
                        if (document != null) document.close();
                        document = Loader.loadPDF(dados);
                        renderer = new PDFRenderer(document);
                        LivroMapaTopicosRepository repo = new LivroMapaTopicosRepository();
                        List<LivroMapaTopicos> tops = repo.findAllByField("livro_id", l.id()).stream()
                            .map(LivroMapaTopicos::fromMap)
                            .toList();
                        Platform.runLater(() -> {
                            topicos = tops;
                            iniciarLeitura();
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        Platform.runLater(() -> voltarBiblioteca());
                    }
                });
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
            opt.ifPresent(l -> {
                definirDisciplinaPreferida(l.disciplinaNome());
                livroParaAbrir = l.id();
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

    // ===================== TÓPICOS =====================

    private void iniciarLeitura() {
        if (topicos == null || topicos.isEmpty()) {
            mostrarTopicos();
            return;
        }

        UUID candidatoId = Authentication.getCurrentUserId();
        if (candidatoId == null) {
            mostrarTopicos();
            return;
        }

        PlaneamentoEstudoResumo resumo = planeamentoService.gerarResumo(candidatoId);
        boolean temLeituras = resumo != null && resumo.leituras() != null && !resumo.leituras().isEmpty();

        if (!temLeituras) {
            if (seguindoPlano == null) seguindoPlano = false;
            mostrarTopicos();
            return;
        }

        if (Boolean.TRUE.equals(seguindoPlano)) {
            topicos = filtrarTopicosDoPlano(resumo);
            if (!topicos.isEmpty()) {
                //registrarLeituraHoje(candidatoId);
            }
            if (topicos.isEmpty()) {
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setTitle("Fora do Plano");
                info.setHeaderText(null);
                info.setContentText("Este livro não tem tópicos planeados para hoje.");
                info.showAndWait();
                voltarBiblioteca();
            } else {
                mostrarTopicos();
            }
            return;
        }

        if (Boolean.FALSE.equals(seguindoPlano)) {
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Leitura bloqueada");
            info.setHeaderText(null);
            info.setContentText("Segue o planeamento de estudo para aproveitares melhor o teu tempo. Vai ao Dashboard e vê o plano de hoje.");
            info.showAndWait();
            voltarBiblioteca();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Plano de Estudo");
        alert.setHeaderText(null);
        alert.setContentText("Desejas seguir o planeamento de leitura de hoje?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                seguindoPlano = true;
                topicos = filtrarTopicosDoPlano(resumo);
                if (!topicos.isEmpty()) {
                   // registrarLeituraHoje(candidatoId);
                }
                if (topicos.isEmpty()) {
                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setTitle("Fora do Plano");
                    info.setHeaderText(null);
                    info.setContentText("Este livro não faz parte do teu plano de hoje. Segue as recomendações do plano para estudar o prioritário.");
                    info.showAndWait();
                    voltarBiblioteca();
                } else {
                    mostrarTopicos();
                }
            } else {
                seguindoPlano = false;
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setTitle("Leitura bloqueada");
                info.setHeaderText(null);
                info.setContentText("Segue o planeamento de estudo para aproveitares melhor o teu tempo. Vai ao Dashboard e vê o plano de hoje.");
                info.showAndWait();
                voltarBiblioteca();
            }
        });
    }

    private List<LivroMapaTopicos> filtrarTopicosDoPlano(PlaneamentoEstudoResumo resumo) {
        return topicos.stream()
            .filter(t -> resumo.leituras().stream().anyMatch(l ->
                TextoUtil.normalizarMinusculo(l.topico()).equals(TextoUtil.normalizarMinusculo(t.topico()))
            ))
            .collect(Collectors.toList());
    }

    private void registrarLeituraHoje(UUID candidatoId) {
        if (livro == null) return;
        try {
            leituraProgressoRepository.registrarAtividadeHoje(candidatoId, livro.id());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void mostrarTopicos() {
        mostrandoTopicos = true;
        topicoSelecionado = -1;
        if (btnFazerTeste != null) btnFazerTeste.setVisible(false);
        pageContainer.getChildren().clear();
        cache.clear();
        pageContainer.setFillWidth(true);
        pageContainer.setAlignment(Pos.TOP_CENTER);

        if (topicos.isEmpty()) {
            lblInfo.setText("A extrair tópicos do livro…");
            Label carregando = new Label("A processar livro com IA. Aguarde…");
            carregando.getStyleClass().add("muted");
            pageContainer.getChildren().add(carregando);
            extrairTopicosEmBackground();
            return;
        }

        List<LivroMapaTopicos> filtrados = filtrarTopicosPorSelecao(topicos);

        lblInfo.setText("Seleccione um tópico para estudar");
        for (int i = 0; i < filtrados.size(); i++) {
            LivroMapaTopicos t = filtrados.get(i);
            VBox card = criarCardTopicoComProgresso(i, t);
            pageContainer.getChildren().add(card);
        }

        if (filtrados.isEmpty()) {
            Label vazio = new Label("Nenhum tópico corresponde à seleção atual.");
            vazio.getStyleClass().add("muted");
            pageContainer.getChildren().add(vazio);
        }
    }

    private List<LivroMapaTopicos> filtrarTopicosPorSelecao(List<LivroMapaTopicos> topicos) {
        Map<String, List<String>> subtopicosSelecionados = DiagnosticoCoordinator.getSubtopicosSelecionados();
        if (subtopicosSelecionados == null || subtopicosSelecionados.isEmpty()) return topicos;

        return topicos.stream()
            .filter(t -> {
                String topico = t.topico() != null ? t.topico().toLowerCase().trim() : "";
                String subtopico = t.subtopico() != null ? t.subtopico().toLowerCase().trim() : "";
                return subtopicosSelecionados.entrySet().stream().anyMatch(entry -> {
                    String topicoSel = entry.getKey().toLowerCase().trim();
                    if (!topico.contains(topicoSel) && !topicoSel.contains(topico)) return false;
                    if (entry.getValue() == null || entry.getValue().isEmpty()) return true;
                    return entry.getValue().stream().anyMatch(s ->
                        subtopico.contains(s.toLowerCase().trim()) || s.toLowerCase().trim().contains(subtopico)
                    );
                });
            })
            .toList();
    }

    private void extrairTopicosEmBackground() {
        if (!geminiService.isConfigured()) {
            lblInfo.setText("Gemini não configurado. Não é possível extrair tópicos.");
            return;
        }
        UUID livroId = livro.id();
        BibliotecaLivroService svc = servoce;
        App.getExecutorService().execute(() -> {
            try {
                List<TopicoSubtopico> topicosExtraidos = svc.extrairTopicosDoLivro(livroId, geminiService);
                Platform.runLater(() -> {
                    if (!topicosExtraidos.isEmpty()) {
                        carregarTopicosDoLivro(livroId);
                        mostrarTopicos();
                    } else {
                        lblInfo.setText("Não foram encontrados tópicos neste livro.");
                        pageContainer.getChildren().clear();
                        Label vazio = new Label("Nenhum tópico detectado. O PDF pode não ter conteúdo estruturado.");
                        vazio.getStyleClass().add("muted");
                        pageContainer.getChildren().add(vazio);
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    lblInfo.setText("Erro ao extrair tópicos: " + e.getMessage());
                    e.printStackTrace();
                });
            }
        });
    }

    private void carregarTopicosDoLivro(UUID livroId) {
        try {
            LivroMapaTopicosRepository repo = new LivroMapaTopicosRepository();
            topicos = repo.findAllByField("livro_id", livroId).stream()
                .map(LivroMapaTopicos::fromMap)
                .toList();
        } catch (SQLException e) {
            e.printStackTrace();
            topicos = List.of();
        }
    }

    private VBox criarCardTopicoComProgresso(int index, LivroMapaTopicos t) {
        VBox card = new VBox(6);
        card.getStyleClass().add("topico-card");
        card.setPadding(new Insets(14));
        card.setUserData(index);
        card.setCursor(javafx.scene.Cursor.HAND);

        Label lblTopico = new Label(t.topico());
        lblTopico.getStyleClass().add("h2");

        Label lblSubtopico = new Label(t.subtopico());
        lblSubtopico.getStyleClass().add("muted");

        Label lblPaginas = new Label("Páginas " + t.paginaInicio() + " — " + t.paginaFim());
        lblPaginas.setStyle("-fx-text-fill: -color-warning; -fx-font-size: 12px; -fx-font-weight: 700;");

        double progresso = carregarProgressoTopico(t);
        ProgressBar bar = new ProgressBar(Math.max(0, Math.min(1, progresso)));
        bar.setPrefWidth(200);
        bar.getStyleClass().add("progresso-bar");
        Label percentLabel = new Label(String.format("%.0f%%", progresso * 100));
        percentLabel.getStyleClass().add("progresso-texto");
        HBox progressoBox = new HBox(8, bar, percentLabel);
        progressoBox.setAlignment(Pos.CENTER_LEFT);

        card.getChildren().addAll(lblTopico, lblSubtopico, lblPaginas, progressoBox);
        card.setOnMouseClicked(e -> selecionarTopico(index));
        return card;
    }

    private double carregarProgressoTopico(LivroMapaTopicos t) {
        if (livro == null) return 0.0;
        UUID alunoId = Authentication.getCurrentUserId();
        if (alunoId == null) return 0.0;
        try {
            LeituraProgresso lp = leituraProgressoRepository.findByAlunoAndLivro(alunoId, livro.id());
            if (lp != null && lp.totalPaginas() != null && lp.totalPaginas() > 0) {
                int lidas = lp.paginaAtual() != null ? lp.paginaAtual() : 0;
                return Math.min(1.0, (lidas * 1.0) / lp.totalPaginas());
            }
        } catch (SQLException ignored) {}
        return 0.0;
    }

    private void selecionarTopico(int index) {
        if (index < 0 || index >= topicos.size()) return;
        topicoSelecionado = index;
        mostrandoTopicos = false;
        if (btnFazerTeste != null) btnFazerTeste.setVisible(true);
        LivroMapaTopicos t = topicos.get(index);

        lblTitulo.setText(t.topico() + " — " + t.subtopico());
        lblInfo.setText("Tópico " + (index + 1) + " de " + topicos.size());

        pageContainer.getChildren().clear();
        cache.clear();
        pageContainer.setFillWidth(false);
        pageContainer.setAlignment(Pos.TOP_CENTER);

        int start = Math.max(0, t.paginaInicio() - 1);
        int end = Math.min(document.getNumberOfPages(), t.paginaFim());

        for (int page = start; page < end; page++) {
            StackPane wrapper = new StackPane();
            wrapper.setAlignment(Pos.CENTER);
            wrapper.getStyleClass().add("page-area");
            ImageView view = criarViewPagina(page);
            wrapper.getChildren().add(view);
            pageContainer.getChildren().add(wrapper);
        }

        currentPage = start;
        lblPaginaAtual.setText("Página " + (start + 1));
        scrollPane.setVvalue(0);
    }

    @FXML
    private void fazerTesteDoTopico(ActionEvent event) {
        if (topicoSelecionado < 0 || topicoSelecionado >= topicos.size()) return;
        if (livro == null) return;

        LivroMapaTopicos t = topicos.get(topicoSelecionado);
        String disciplinaNome = livro.disciplinaNome();
        UUID disciplinaId = livro.disciplinaId();

        Topico topico = new Topico(disciplinaId, disciplinaNome, t.topico(), null,
            t.subtopico() != null ? new String[]{t.subtopico()} : new String[0]);

        ArrayList<Topico> listaTopicos = new ArrayList<>();
        listaTopicos.add(topico);

        TesteAdaptativoCoordinator.definirContextoTeste(disciplinaNome, listaTopicos);
        CandidatoLayoutController.navegar("exame_adaptativo");
    }

    // ===================== PÁGINAS =====================

    private ImageView criarViewPagina(int pageIndex) {
        ImageView view = new ImageView();
        view.setPreserveRatio(true);
        view.setFitWidth(500);
        view.setUserData(pageIndex);

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
                BufferedImage buffered = renderer.renderImageWithDPI(page, 150);
                Image fxImage = SwingFXUtils.toFXImage(buffered, null);
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

    private void atualizarPaginaScroll(int paginaAbsoluta) {
        currentPage = paginaAbsoluta;
        lblPaginaAtual.setText("Página " + (paginaAbsoluta + 1));
    }

    // ===================== NAVEGAÇÃO =====================

    @FXML
    private void voltarBiblioteca() {
        if (!mostrandoTopicos && topicoSelecionado >= 0) {
            mostrarTopicos();
            lblTitulo.setText(livro.titulo());
            return;
        }
        pdfViewer.setVisible(false);
        biblioteca.setVisible(true);
        if (document != null) {
            try { document.close(); } catch (IOException ignored) {}
            document = null;
            renderer = null;
        }
        cache.clear();
    }

    @FXML
    private void InitPag(ActionEvent event) {
        if (document == null) return;
        if (mostrandoTopicos || topicoSelecionado < 0) return;
        LivroMapaTopicos t = topicos.get(topicoSelecionado);
        int first = Math.max(0, t.paginaInicio() - 1);
        scrollPane.setVvalue(0);
        atualizarPaginaScroll(first);
    }

    @FXML
    private void LastPag(ActionEvent event) {
        if (document == null) return;
        if (mostrandoTopicos || topicoSelecionado < 0) return;
        LivroMapaTopicos t = topicos.get(topicoSelecionado);
        int last = Math.min(document.getNumberOfPages(), t.paginaFim()) - 1;
        int total = last - Math.max(0, t.paginaInicio() - 1);
        if (total > 0) {
            scrollPane.setVvalue(1.0);
            atualizarPaginaScroll(last);
        }
    }

    @FXML
    private void PagAnterior(ActionEvent event) {
        if (document == null) return;
        if (mostrandoTopicos || topicoSelecionado < 0) return;
        if (currentPage > 0) {
            LivroMapaTopicos t = topicos.get(topicoSelecionado);
            int first = Math.max(0, t.paginaInicio() - 1);
            if (currentPage - 1 >= first) {
                double targetV = (double) (currentPage - 1 - first) / (Math.min(document.getNumberOfPages(), t.paginaFim()) - 1 - first);
                scrollPane.setVvalue(Math.max(0, targetV));
                atualizarPaginaScroll(currentPage - 1);
            }
        }
    }

    @FXML
    private void PagSeguinte(ActionEvent event) {
        if (document == null) return;
        if (mostrandoTopicos || topicoSelecionado < 0) return;
        LivroMapaTopicos t = topicos.get(topicoSelecionado);
        int last = Math.min(document.getNumberOfPages(), t.paginaFim()) - 1;
        if (currentPage < last) {
            int first = Math.max(0, t.paginaInicio() - 1);
            double targetV = (double) (currentPage + 1 - first) / (last - first);
            scrollPane.setVvalue(Math.min(1.0, targetV));
            atualizarPaginaScroll(currentPage + 1);
        }
    }

    @FXML
    private void Procurar(ActionEvent event) { aplicarFiltro(); }

    @FXML
    private void ZoomIn(ActionEvent event) {}
    @FXML
    private void ZoomOut(ActionEvent event) {}

    // ===================== DISCIPLINAS =====================

    private void carregarDisciplinas() {
        LinkedHashMap<UUID, DisciplinaOption> opcoes = new LinkedHashMap<>();
        for (ProgressoAlunoDisciplinaDto p : DisciplinaService.getProgressoDisciplinasCandidatoSafe()) {
            if (p == null || p.disciplinaId() == null) continue;
            String nome = firstNonBlank(p.disciplina(), DisciplinaService.findByNomeIdSearch(p.disciplinaId()));
            if (!DisciplinaService.isDisciplinaSuportada(nome)) continue;
            opcoes.putIfAbsent(p.disciplinaId(), new DisciplinaOption(p.disciplinaId(), nome));
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
        DisciplinaOption inicial = disciplinaCombo.getSelectionModel().getSelectedItem();
        if (inicial != null) carregarLivrosDaDisciplina(inicial.id());
    }

    private String firstNonBlank(String first, String second) {
        if (first != null && !first.isBlank()) return first.trim();
        if (second != null && !second.isBlank()) return second.trim();
        return "";
    }

    private void aplicarDisciplinaPreferidaSeExistir() {
        String preferida = disciplinaPreferida;
        disciplinaPreferida = null;
        if (preferida == null || preferida.isBlank() || disciplinaCombo.getItems().isEmpty()) return;
        String alvo = TextoUtil.normalizarMinusculo(preferida);
        disciplinaCombo.getItems().stream()
            .filter(i -> TextoUtil.normalizarMinusculo(i.nome()).equals(alvo))
            .findFirst()
            .ifPresent(i -> disciplinaCombo.getSelectionModel().select(i));
    }

   
}
