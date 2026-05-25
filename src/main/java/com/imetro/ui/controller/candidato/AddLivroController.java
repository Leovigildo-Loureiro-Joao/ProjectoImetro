package com.imetro.ui.controller.candidato;

import com.imetro.domain.dto.progresso.ProgressoAlunoDisciplinaDto;
import com.imetro.services.DisciplinaService;
import com.imetro.services.DisciplinaUploadBootstrapService;
import com.imetro.services.PerguntasBootstrapAsyncService;
import com.imetro.util.Authentication;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import java.awt.Desktop;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Window;

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

public class AddLivroController implements Initializable {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    private JFXComboBox<DisciplinaOption> disciplinaCombo;

    @FXML
    private TextField selectedFilesField;

    @FXML
    private Label selectionSummaryLabel;

    @FXML
    private Label bibliotecaResumoLabel;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Label launchHintLabel;

    @FXML
    private TableView<LivroTabelaRow> livrosTable;

    @FXML
    private TableColumn<LivroTabelaRow, String> nomeColumn;

    @FXML
    private TableColumn<LivroTabelaRow, String> tamanhoColumn;

    @FXML
    private TableColumn<LivroTabelaRow, String> atualizadoColumn;

    @FXML
    private TableColumn<LivroTabelaRow, String> estadoColumn;

    @FXML
    private TableColumn<LivroTabelaRow, LivroTabelaRow> abrirColumn;

    @FXML
    private JFXButton selecionarArquivosButton;

    @FXML
    private JFXButton limparSelecaoButton;

    @FXML
    private JFXButton lancarButton;

    private final DisciplinaUploadBootstrapService uploadService = new DisciplinaUploadBootstrapService();
    private final PerguntasBootstrapAsyncService bootstrapAsyncService = PerguntasBootstrapAsyncService.getInstance();
    private final List<Path> arquivosSelecionados = new ArrayList<>();
    private final ObservableList<LivroTabelaRow> livros = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarTabela();
        livrosTable.setItems(livros);
        selectedFilesField.setText("Nenhum PDF selecionado");
        selectionSummaryLabel.setText("Seleciona uma disciplina e escolhe um ou mais livros em PDF.");
        launchHintLabel.setText("Ao lancar, o sistema recalcula os topicos e atualiza a tabela perguntas.");

        disciplinaCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            arquivosSelecionados.clear();
            atualizarSelecaoArquivos();
            carregarBibliotecaAtual();
        });

        bootstrapAsyncService.runningProperty().addListener((obs, oldValue, running) -> {
            atualizarEstadoAcoes();
            if (!running) {
                refletirResumoBootstrap();
            }
        });
        bootstrapAsyncService.stateProperty().addListener((obs, oldValue, newValue) -> {
            if (!bootstrapAsyncService.runningProperty().get()) {
                refletirResumoBootstrap();
            }
        });

        carregarDisciplinas();
        aplicarFeedback(
            "Esta area aceita livros de Matematica e Fisica. Depois do envio, a geracao corre em segundo plano.",
            "info"
        );
        atualizarEstadoAcoes();
    }

    @FXML
    private void selecionarArquivos() {
        DisciplinaOption disciplina = disciplinaCombo.getValue();
        if (disciplina == null) {
            aplicarFeedback("Seleciona primeiro a disciplina para sabermos onde guardar os PDFs.", "error");
            return;
        }

        Window window = selecionarArquivosButton != null && selecionarArquivosButton.getScene() != null
            ? selecionarArquivosButton.getScene().getWindow()
            : null;

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Seleciona os livros em PDF");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));

        List<File> files = chooser.showOpenMultipleDialog(window);
        if (files == null || files.isEmpty()) {
            return;
        }

        LinkedHashMap<String, Path> unicos = new LinkedHashMap<>();
        for (File file : files) {
            if (file == null) {
                continue;
            }
            Path path = file.toPath().toAbsolutePath().normalize();
            unicos.put(path.toString(), path);
        }

        arquivosSelecionados.clear();
        arquivosSelecionados.addAll(unicos.values());
        atualizarSelecaoArquivos();
        aplicarFeedback(
            arquivosSelecionados.size() + " PDF(s) preparados para " + disciplina.nome() + ". Clica em Lancar para enviar.",
            "info"
        );
    }

    @FXML
    private void limparSelecao() {
        arquivosSelecionados.clear();
        atualizarSelecaoArquivos();
        aplicarFeedback("A selecao local foi limpa. A biblioteca da disciplina continua intacta.", "info");
    }

    @FXML
    private void lancarGeracao() {
        DisciplinaOption disciplina = disciplinaCombo.getValue();
        if (disciplina == null) {
            aplicarFeedback("Seleciona a disciplina antes de lancar a leitura do livro.", "error");
            return;
        }

        UUID candidatoId = Authentication.getCurrentUserId();
        if (candidatoId == null) {
            aplicarFeedback("Nao foi possivel identificar o candidato atual nesta sessao.", "error");
            return;
        }

        if (bootstrapAsyncService.runningProperty().get()) {
            aplicarFeedback(
                "Ja existe um processamento em curso. Acompanha a barra no topo e tenta novamente quando terminar.",
                "info"
            );
            return;
        }

        try {
            int copiados = 0;
            if (!arquivosSelecionados.isEmpty()) {
                copiados = uploadService.adicionarPdfs(disciplina.id(), List.copyOf(arquivosSelecionados)).size();
                arquivosSelecionados.clear();
                atualizarSelecaoArquivos();
                carregarBibliotecaAtual();
            }

            if (livros.isEmpty()) {
                aplicarFeedback("Ainda nao existe nenhum PDF nesta disciplina. Seleciona ao menos um livro para continuar.", "error");
                return;
            }

            boolean iniciado = bootstrapAsyncService.startDisciplina(candidatoId, disciplina.id(), true, true);
            if (!iniciado) {
                aplicarFeedback(
                    "Nao foi possivel iniciar agora porque ja existe outro processamento ativo no momento.",
                    "info"
                );
                return;
            }

            String prefixo = copiados > 0
                ? copiados + " PDF(s) enviados para a biblioteca. "
                : "A biblioteca atual sera relida sem enviar novos PDFs. ";
            aplicarFeedback(
                prefixo + "O Gemini ja esta a recalcular topicos e a atualizar a tabela perguntas em segundo plano.",
                "success"
            );
        } catch (Exception e) {
            aplicarFeedback(
                "Falha ao enviar ou processar os livros: " + firstNonBlank(e.getMessage(), "erro inesperado"),
                "error"
            );
        } finally {
            atualizarEstadoAcoes();
        }
    }

    private void configurarTabela() {
        nomeColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().nome()));
        tamanhoColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().tamanho()));
        atualizadoColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().atualizadoEm()));
        estadoColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().estado()));
        if (abrirColumn != null) {
            abrirColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue()));
            abrirColumn.setCellFactory(column -> new TableCell<>() {
                private final JFXButton abrirButton = new JFXButton("Abrir");

                {
                    abrirButton.getStyleClass().add("btn-secondary");
                    abrirButton.setOnAction(event -> {
                        LivroTabelaRow row = getItem();
                        if (row != null) {
                            abrirLivro(row);
                        }
                    });
                }

                @Override
                protected void updateItem(LivroTabelaRow item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(null);
                    setGraphic(empty || item == null ? null : abrirButton);
                }
            });
        }
    }

    private void carregarDisciplinas() {
        LinkedHashMap<UUID, DisciplinaOption> opcoes = new LinkedHashMap<>();
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

            opcoes.putIfAbsent(progresso.disciplinaId(), new DisciplinaOption(progresso.disciplinaId(), nomeDisciplina));
        }

        disciplinaCombo.setItems(FXCollections.observableArrayList(opcoes.values()));
        if (disciplinaCombo.getItems().isEmpty()) {
            livros.clear();
            bibliotecaResumoLabel.setText("Ainda nao ha disciplinas ativas para esta sessao.");
            aplicarFeedback(
                "Nao encontramos disciplinas ativas para este candidato. Confirma o onboarding ou o progresso inicial.",
                "error"
            );
            return;
        }

        disciplinaCombo.getSelectionModel().selectFirst();
        carregarBibliotecaAtual();
    }

    private void carregarBibliotecaAtual() {
        DisciplinaOption disciplina = disciplinaCombo.getValue();
        if (disciplina == null) {
            livros.clear();
            bibliotecaResumoLabel.setText("Seleciona uma disciplina para ver a biblioteca.");
            atualizarEstadoAcoes();
            return;
        }

        try {
            List<LivroTabelaRow> rows = uploadService.listarPdfs(disciplina.id()).stream()
                .map(this::toRow)
                .toList();
            livros.setAll(rows);
            bibliotecaResumoLabel.setText(construirResumoBiblioteca(disciplina.nome(), rows.size()));
        } catch (IOException e) {
            livros.clear();
            bibliotecaResumoLabel.setText("Nao foi possivel ler a biblioteca desta disciplina.");
            aplicarFeedback("Falha ao listar os PDFs da disciplina: " + firstNonBlank(e.getMessage(), "erro de leitura"), "error");
        }

        atualizarEstadoAcoes();
    }

    private LivroTabelaRow toRow(Path path) {
        try {
            long size = Files.size(path);
            FileTime lastModified = Files.getLastModifiedTime(path);
            LocalDateTime atualizadoEm = LocalDateTime.ofInstant(lastModified.toInstant(), ZoneId.systemDefault());
            return new LivroTabelaRow(
                path.getFileName().toString(),
                formatBytes(size),
                DATE_FORMATTER.format(atualizadoEm),
                "Na biblioteca",
                path
            );
        } catch (IOException e) {
            return new LivroTabelaRow(
                path.getFileName().toString(),
                "-",
                "-",
                "Com detalhe indisponivel",
                path
            );
        }
    }

    private void abrirLivro(LivroTabelaRow row) {
        if (row == null || row.caminho() == null) {
            aplicarFeedback("Nao foi possivel localizar o PDF selecionado.", "error");
            return;
        }

        Path pdf = row.caminho().toAbsolutePath().normalize();
        if (Files.notExists(pdf) || !Files.isRegularFile(pdf)) {
            aplicarFeedback("O PDF ja nao existe nesta pasta: " + row.nome(), "error");
            return;
        }

        try {
            if (!Desktop.isDesktopSupported()) {
                throw new IOException("Abertura externa de ficheiros nao esta disponivel nesta sessao.");
            }

            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(pdf.toUri());
            } else if (desktop.isSupported(Desktop.Action.OPEN)) {
                desktop.open(pdf.toFile());
            } else {
                throw new IOException("O sistema nao suporta abrir este PDF a partir da tabela.");
            }

            aplicarFeedback("Livro aberto: " + row.nome(), "success");
        } catch (Exception e) {
            aplicarFeedback(
                "Nao foi possivel abrir o PDF agora: " + firstNonBlank(e.getMessage(), "erro inesperado"),
                "error"
            );
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

    private void atualizarSelecaoArquivos() {
        if (arquivosSelecionados.isEmpty()) {
            selectedFilesField.setText("Nenhum PDF selecionado");
            selectionSummaryLabel.setText("Seleciona um ou mais PDFs. Eles entram na biblioteca ao clicar em Lancar.");
        } else if (arquivosSelecionados.size() == 1) {
            Path unico = arquivosSelecionados.getFirst();
            selectedFilesField.setText(unico.getFileName().toString());
            selectionSummaryLabel.setText("1 PDF pronto para envio.");
        } else {
            selectedFilesField.setText(arquivosSelecionados.size() + " PDFs selecionados");
            selectionSummaryLabel.setText("Os PDFs serao enviados juntos para a disciplina selecionada.");
        }
        atualizarEstadoAcoes();
    }

    private void atualizarEstadoAcoes() {
        boolean disciplinaSelecionada = disciplinaCombo.getValue() != null;
        boolean running = bootstrapAsyncService.runningProperty().get();
        boolean temSelecao = !arquivosSelecionados.isEmpty();
        boolean temBiblioteca = !livros.isEmpty();

        selecionarArquivosButton.setDisable(!disciplinaSelecionada || running);
        limparSelecaoButton.setDisable(!temSelecao || running);
        lancarButton.setDisable(!disciplinaSelecionada || (!temSelecao && !temBiblioteca) || running);

        if (running) {
            launchHintLabel.setText("Existe um processamento ativo. Podes acompanhar a barra de progresso no topo.");
        } else if (temSelecao) {
            launchHintLabel.setText("Ao lancar, os PDFs selecionados entram na biblioteca e a geracao arranca.");
        } else if (temBiblioteca) {
            launchHintLabel.setText("Podes relancar a disciplina para atualizar topicos e tentar gerar novas perguntas.");
        } else {
            launchHintLabel.setText("A biblioteca ainda esta vazia. Seleciona pelo menos um PDF para comecar.");
        }
    }

    private void refletirResumoBootstrap() {
        String resumo = bootstrapAsyncService.summaryProperty().get();
        if (resumo == null || resumo.isBlank()) {
            return;
        }

        switch (bootstrapAsyncService.getState()) {
            case SUCCESS -> aplicarFeedback(resumo, "success");
            case WARNING -> aplicarFeedback(resumo, "info");
            case ERROR -> aplicarFeedback(resumo, "error");
            default -> {
            }
        }
        carregarBibliotecaAtual();
    }

    private void aplicarFeedback(String mensagem, String tipo) {
        feedbackLabel.setText(mensagem == null ? "" : mensagem);
        feedbackLabel.getStyleClass().removeAll(
            "profile-feedback-info",
            "profile-feedback-success",
            "profile-feedback-error"
        );
        if (!feedbackLabel.getStyleClass().contains("profile-feedback")) {
            feedbackLabel.getStyleClass().add("profile-feedback");
        }

        String estilo = switch (tipo) {
            case "success" -> "profile-feedback-success";
            case "error" -> "profile-feedback-error";
            default -> "profile-feedback-info";
        };
        feedbackLabel.getStyleClass().add(estilo);
    }

    private String formatBytes(long bytes) {
        if (bytes >= 1024L * 1024L) {
            return String.format("%.1f MB", bytes / (1024d * 1024d));
        }
        if (bytes >= 1024L) {
            return String.format("%.1f KB", bytes / 1024d);
        }
        return bytes + " B";
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

    private record DisciplinaOption(UUID id, String nome) {
        @Override
        public String toString() {
            return nome;
        }
    }

    private record LivroTabelaRow(
        String nome,
        String tamanho,
        String atualizadoEm,
        String estado,
        Path caminho
    ) {
    }
}
