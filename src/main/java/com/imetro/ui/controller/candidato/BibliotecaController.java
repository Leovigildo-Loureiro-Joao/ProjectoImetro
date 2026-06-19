package com.imetro.ui.controller.candidato;

import com.imetro.App;
import com.imetro.domain.dto.MenuEntry;
import com.imetro.domain.dto.biblioteca.BibliotecaLivroDto;
import com.imetro.domain.dto.progresso.ProgressoAlunoDisciplinaDto;
import com.imetro.services.BibliotecaLivroService;
import com.imetro.services.DisciplinaService;
import com.imetro.services.DisciplinaUploadBootstrapService;
import com.imetro.services.PerguntasBootstrapAsyncService;
import com.imetro.ui.components.Item_Cell;
import com.imetro.ui.components.biblioteca.LivroCard;
import com.imetro.ui.controller.lifecycle.DisposableController;
import com.imetro.ui.modals.AddLivroModalController;
import com.imetro.ui.modals.ModalAlert;
import com.imetro.ui.modals.ModalController;
import com.imetro.util.Authentication;
import com.imetro.util.TextoUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import java.awt.Desktop;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;

public class BibliotecaController implements Initializable  {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static String disciplinaPreferida;

    private FXMLLoader modFxml;
    private Node mod;
    private Node modTop;
    private ModalController cont;

    @FXML
    private JFXButton btnAnterior;

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
    private ScrollPane scrollPdf;


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
            servoce.listarLivros(disciplinaCombo.getSelectionModel().getSelectedItem().id());
            carregarLivros(servoce.listarLivros(disciplinaCombo.getSelectionModel().getSelectedItem().id()));
        } catch (IOException e) {
            // TODO Auto-generated catch block
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
               // abrirLivro(livro);
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





    private String construirResumoBiblioteca(String disciplinaNome, int totalLivros) {
        if (totalLivros <= 0) {
            return "A biblioteca de " + disciplinaNome + " ainda esta vazia. Envia o primeiro PDF para gerar perguntas.";
        }
        return totalLivros == 1
            ? "1 livro carregado em " + disciplinaNome + ". Podes relancar a geracao sempre que adicionares novo material."
            : totalLivros + " livros carregados em " + disciplinaNome + ". A disciplina ja esta pronta para releituras.";
    }





}
