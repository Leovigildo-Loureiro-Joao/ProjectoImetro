package com.imetro.services;

import com.imetro.domain.dto.disciplina.DisciplinaDto;
import com.imetro.domain.dto.gemini.ExtracaoTopicosRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class DisciplinaUploadBootstrapService {

    private static final String DEFAULT_OUTPUT_FILE = "topicos-extraidos.json";

    private final DisciplinaService disciplinaService;
    private final GeminiService geminiService;
    private final Path uploadRoot;

    public DisciplinaUploadBootstrapService() {
        this(new DisciplinaService(), new GeminiService(), Paths.get("uploads", "disciplinas"));
    }

    DisciplinaUploadBootstrapService(
        DisciplinaService disciplinaService,
        GeminiService geminiService,
        Path uploadRoot
    ) {
        this.disciplinaService = disciplinaService;
        this.geminiService = geminiService;
        this.uploadRoot = uploadRoot.toAbsolutePath().normalize();
    }

    public Path getUploadRoot() {
        return uploadRoot;
    }

    public List<DisciplinaUploadFolder> prepararPastasUploads() throws IOException {
        List<DisciplinaDto> disciplinas = carregarDisciplinas();
        Files.createDirectories(uploadRoot);

        ArrayList<DisciplinaUploadFolder> folders = new ArrayList<>();
        for (DisciplinaDto disciplina : disciplinas) {
            Path pasta = pastaDisciplina(disciplina.id());
            boolean criadaAgora = Files.notExists(pasta);
            Files.createDirectories(pasta);
            folders.add(new DisciplinaUploadFolder(disciplina.id(), disciplina.nome(), pasta, criadaAgora));
        }

        return List.copyOf(folders);
    }

    public List<DisciplinaTopicosBootstrapResult> processarCargaInicial()
        throws IOException, InterruptedException {
        return processarCargaInicial(false);
    }

    public List<DisciplinaTopicosBootstrapResult> processarCargaInicial(boolean sobrescrever)
        throws IOException, InterruptedException {
        List<DisciplinaUploadFolder> folders = prepararPastasUploads();
        ArrayList<DisciplinaTopicosBootstrapResult> results = new ArrayList<>();
        for (DisciplinaUploadFolder folder : folders) {
            results.add(processarCargaInicial(folder.disciplinaId(), sobrescrever));
        }
        return List.copyOf(results);
    }

    public DisciplinaTopicosBootstrapResult processarCargaInicial(UUID disciplinaId)
        throws IOException, InterruptedException {
        return processarCargaInicial(disciplinaId, false);
    }

    public DisciplinaTopicosBootstrapResult processarCargaInicial(UUID disciplinaId, boolean sobrescrever)
        throws IOException, InterruptedException {
        DisciplinaDto disciplina = localizarDisciplina(disciplinaId);
        Path pasta = pastaDisciplina(disciplina.id());
        Path arquivoTopicos = pasta.resolve(DEFAULT_OUTPUT_FILE);

        Files.createDirectories(pasta);

        List<Path> pdfs = listarPdfs(disciplina.id());
        if (pdfs.isEmpty()) {
            return new DisciplinaTopicosBootstrapResult(
                disciplina.id(),
                disciplina.nome(),
                pasta,
                arquivoTopicos,
                0,
                BootstrapStatus.SEM_PDFS,
                "Nenhum PDF encontrado para esta disciplina."
            );
        }

        if (Files.exists(arquivoTopicos) && !sobrescrever) {
            return new DisciplinaTopicosBootstrapResult(
                disciplina.id(),
                disciplina.nome(),
                pasta,
                arquivoTopicos,
                pdfs.size(),
                BootstrapStatus.JA_PROCESSADO,
                "O arquivo de topicos ja existe. Usa sobrescrever=true para recalcular."
            );
        }

        String jsonTopicos = geminiService.extrairTopicosJson(
            pdfs,
            new ExtracaoTopicosRequest(
                disciplina.nome(),
                "pt-AO",
                "Organiza os topicos com foco no conteudo programatico da disciplina " + disciplina.nome() + "."
            )
        );

        Files.writeString(
            arquivoTopicos,
            jsonTopicos,
            StandardCharsets.UTF_8,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING,
            StandardOpenOption.WRITE
        );

        return new DisciplinaTopicosBootstrapResult(
            disciplina.id(),
            disciplina.nome(),
            pasta,
            arquivoTopicos,
            pdfs.size(),
            BootstrapStatus.PROCESSADO,
            "Topicos extraidos com sucesso."
        );
    }

    public List<Path> listarPdfs(UUID disciplinaId) throws IOException {
        Path pasta = pastaDisciplina(disciplinaId);
        if (Files.notExists(pasta) || !Files.isDirectory(pasta)) {
            return List.of();
        }

        try (Stream<Path> stream = Files.list(pasta)) {
            return stream
                .filter(Files::isRegularFile)
                .filter(path -> path.getFileName().toString().toLowerCase().endsWith(".pdf"))
                .sorted(Comparator.comparing(path -> path.getFileName().toString().toLowerCase()))
                .toList();
        }
    }

    public Path pastaDisciplina(UUID disciplinaId) {
        if (disciplinaId == null) {
            throw new IllegalArgumentException("disciplinaId nao pode ser nulo.");
        }
        return uploadRoot.resolve(disciplinaId.toString());
    }

    private DisciplinaDto localizarDisciplina(UUID disciplinaId) {
        if (disciplinaId == null) {
            throw new IllegalArgumentException("disciplinaId nao pode ser nulo.");
        }

        return carregarDisciplinas().stream()
            .filter(disciplina -> disciplinaId.equals(disciplina.id()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Disciplina nao encontrada: " + disciplinaId));
    }

    private List<DisciplinaDto> carregarDisciplinas() {
        List<DisciplinaDto> disciplinas = disciplinaService.discCategoria().stream()
            .filter(disciplina -> disciplina.id() != null)
            .toList();

        if (disciplinas.isEmpty()) {
            throw new IllegalStateException(
                "Nenhuma disciplina cadastrada foi encontrada. Verifica a BD e as migrations antes de preparar os uploads."
            );
        }

        return disciplinas;
    }

    public enum BootstrapStatus {
        PROCESSADO,
        JA_PROCESSADO,
        SEM_PDFS
    }

    public record DisciplinaUploadFolder(
        UUID disciplinaId,
        String nomeDisciplina,
        Path pasta,
        boolean criadaAgora
    ) {
    }

    public record DisciplinaTopicosBootstrapResult(
        UUID disciplinaId,
        String nomeDisciplina,
        Path pasta,
        Path arquivoTopicos,
        int totalPdfs,
        BootstrapStatus status,
        String detalhe
    ) {
    }
}
