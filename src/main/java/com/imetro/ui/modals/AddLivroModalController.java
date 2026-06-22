package com.imetro.ui.modals;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import com.imetro.domain.dto.progresso.ProgressoAlunoDisciplinaDto;
import com.imetro.services.BibliotecaLivroService;
import com.imetro.services.DisciplinaService;
import com.imetro.services.DisciplinaUploadBootstrapService;
import com.imetro.services.PerguntasBootstrapAsyncService;
import com.imetro.util.Authentication;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class AddLivroModalController extends ModalController{
    @FXML
    private JFXComboBox<DisciplinaOption> disciplinaCombo;

    @FXML
    private Label feedbackLabel;

    @FXML
    private JFXButton lancarButton;

    @FXML
    private Label launchHintLabel;

    @FXML
    private JFXButton limparSelecaoButton;

    @FXML
    private JFXButton selecionarArquivosButton;

    @FXML
    private TextField selectedFilesField;

    @FXML
    private Label selectionSummaryLabel;
    private BibliotecaLivroService service;

    private final DisciplinaUploadBootstrapService uploadService = new DisciplinaUploadBootstrapService();
    private final PerguntasBootstrapAsyncService bootstrapAsyncService = PerguntasBootstrapAsyncService.getInstance();
    private final List<Path> arquivosSelecionados = new ArrayList<>();


    @Override
    public void init() {
        service=new BibliotecaLivroService();
        // TODO Auto-generated method stub
        super.init();
        selectedFilesField.setText("Nenhum PDF selecionado");
        selectionSummaryLabel.setText("Seleciona uma disciplina e escolhe um ou mais livros em PDF.");
        launchHintLabel.setText("Ao atualizar, o sistema recalcula os topicos e atualiza a biblioteca.");
        carregarDisciplinas();
        disciplinaCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            arquivosSelecionados.clear();
            atualizarSelecaoArquivos();
        });

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
            arquivosSelecionados.size() + " PDF(s) prontos para atualizar a biblioteca de " + disciplina.nome() + ". Clica em Atualizar.",
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
            }


          /*   boolean iniciado = bootstrapAsyncService.startDisciplina(candidatoId, disciplina.id(), true, true);
            if (!iniciado) {
                aplicarFeedback(
                    "Nao foi possivel iniciar agora porque ja existe outro processamento ativo no momento.",
                    "info"
                );
                return;
            }

            String prefixo = copiados > 0
                ? copiados + " PDF(s) guardados na biblioteca e sincronizados no Supabase. "
                : "A biblioteca atual sera relida sem enviar novos PDFs. ";
            aplicarFeedback(
                prefixo + "A leitura dos livros e a geracao das perguntas ja estao a correr em segundo plano.",
                "success"
            );
            */
        } catch (Exception e) {
            aplicarFeedback(
                "Falha ao enviar ou processar os livros: " + firstNonBlank(e.getMessage(), "erro inesperado"),
                "error"
            );
        } finally {
            atualizarEstadoAcoes();
        }
    }

    private void atualizarSelecaoArquivos() {
        if (arquivosSelecionados.isEmpty()) {
            selectedFilesField.setText("Nenhum PDF selecionado");
            selectionSummaryLabel.setText("Seleciona um ou mais PDFs. Eles entram na biblioteca ao clicar em Atualizar.");
        } else if (arquivosSelecionados.size() == 1) {
            Path unico = arquivosSelecionados.getFirst();
            selectedFilesField.setText(unico.getFileName().toString());
            selectionSummaryLabel.setText("1 PDF pronto para atualizar a biblioteca.");
        } else {
            selectedFilesField.setText(arquivosSelecionados.size() + " PDFs selecionados");
            selectionSummaryLabel.setText("Os PDFs serao enviados juntos para a disciplina selecionada.");
        }
        atualizarEstadoAcoes();
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


    private void atualizarEstadoAcoes() {
        boolean disciplinaSelecionada = disciplinaCombo.getValue() != null;
        boolean running = bootstrapAsyncService.runningProperty().get();
        boolean temSelecao = !arquivosSelecionados.isEmpty();

        selecionarArquivosButton.setDisable(!disciplinaSelecionada || running);
        limparSelecaoButton.setDisable(!temSelecao || running);
        lancarButton.setDisable(!disciplinaSelecionada || (!temSelecao) || running);

        if (running) {
            launchHintLabel.setText("Existe um processamento ativo. Podes acompanhar a barra de progresso no topo.");
        } else if (temSelecao) {
            launchHintLabel.setText("Ao atualizar, os PDFs selecionados entram na biblioteca e a geracao arranca.");
        } else {
            launchHintLabel.setText("A biblioteca ainda esta vazia. Seleciona pelo menos um PDF para comecar.");
        }
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
    }

    public static record DisciplinaOption(UUID id, String nome) {
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
