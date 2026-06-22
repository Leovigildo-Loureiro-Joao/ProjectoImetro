package com.imetro.services;

import com.imetro.domain.dto.disciplina.DisciplinaDto;
import com.imetro.domain.dto.gemini.ExtracaoTopicosRequest;
import com.imetro.domain.enums.TopicoExame;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Stream;

public class DisciplinaUploadBootstrapService {

    private static final String DEFAULT_OUTPUT_FILE = "topicos-extraidos.json";
    private static final String PROCESSED_STATE_FILE = ".livros-processados.snapshot";
    private final GeminiService geminiService;
    private final BibliotecaLivroService bibliotecaLivroService;
    private final Path uploadRoot;

    public DisciplinaUploadBootstrapService() {
        this(
            new DisciplinaService(),
            new GeminiService(),
            new BibliotecaLivroService(),
            Paths.get("uploads", "disciplinas")
        );
    }

    DisciplinaUploadBootstrapService(
        DisciplinaService disciplinaService,
        GeminiService geminiService,
        BibliotecaLivroService bibliotecaLivroService,
        Path uploadRoot
    ) {
        this.geminiService = geminiService;
        this.bibliotecaLivroService = bibliotecaLivroService;
        this.uploadRoot = uploadRoot.toAbsolutePath().normalize();
    }

    public Path getUploadRoot() {
        return uploadRoot;
    }

    public List<DisciplinaUploadFolder> prepararPastasUploads() throws IOException {
        Files.createDirectories(uploadRoot);
        List<DisciplinaDto> disciplinas = carregarDisciplinas();

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
        Path arquivoTopicos = arquivoTopicos(disciplina.id());

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

        TopicoExame.Disciplina disciplinaEnum = TopicoExame.resolverDisciplina(disciplina.nome()).orElse(null);
        String instrucoesCanonicas = TopicoExame.instrucoesModoInteligente(disciplinaEnum);
        String jsonTopicos = geminiService.extrairTopicosJson(
            pdfs,
            new ExtracaoTopicosRequest(
                disciplina.nome(),
                "pt-AO",
                "Organiza os topicos com foco no conteudo programatico da disciplina " + disciplina.nome() + "."
                    + (instrucoesCanonicas.isBlank() ? "" : "\n" + instrucoesCanonicas)
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
            "Conteudo preparado com sucesso."
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

    public Path arquivoTopicos(UUID disciplinaId) {
        return pastaDisciplina(disciplinaId).resolve(DEFAULT_OUTPUT_FILE);
    }

    public Path arquivoEstadoProcessado(UUID disciplinaId) {
        return pastaDisciplina(disciplinaId).resolve(PROCESSED_STATE_FILE);
    }

    public boolean possuiPdfsPendentes(UUID disciplinaId) throws IOException {
        List<String> estadoAtual = construirEstadoPdfs(listarPdfs(disciplinaId));
        if (estadoAtual.isEmpty()) {
            return false;
        }

        Path arquivoEstado = arquivoEstadoProcessado(disciplinaId);
        if (Files.notExists(arquivoEstado)) {
            return true;
        }

        List<String> estadoProcessado = Files.readAllLines(arquivoEstado, StandardCharsets.UTF_8).stream()
            .map(String::trim)
            .filter(linha -> !linha.isBlank())
            .toList();
        return !estadoAtual.equals(estadoProcessado);
    }

    public void registrarEstadoProcessado(UUID disciplinaId, List<Path> pdfs) throws IOException {
        Path arquivoEstado = arquivoEstadoProcessado(disciplinaId);
        Files.createDirectories(arquivoEstado.getParent());
        Files.write(
            arquivoEstado,
            construirEstadoPdfs(pdfs),
            StandardCharsets.UTF_8,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING,
            StandardOpenOption.WRITE
        );
    }

    public List<Path> adicionarPdfs(UUID disciplinaId, List<Path> arquivosOrigem) throws IOException {
        DisciplinaDto disciplina = localizarDisciplina(disciplinaId);
        if (arquivosOrigem == null || arquivosOrigem.isEmpty()) {
            throw new IllegalArgumentException("Indica pelo menos um PDF para carregar.");
        }

        Path pasta = pastaDisciplina(disciplina.id());
        Files.createDirectories(pasta);

        ArrayList<Path> copiados = new ArrayList<>();
        for (Path origem : arquivosOrigem) {
            if (origem == null) {
                continue;
            }

            Path normalizado = origem.toAbsolutePath().normalize();
            if (!Files.exists(normalizado) || !Files.isRegularFile(normalizado)) {
                throw new IOException("PDF nao encontrado: " + normalizado);
            }

            String nomeArquivo = normalizado.getFileName().toString();
            if (!nomeArquivo.toLowerCase(Locale.ROOT).endsWith(".pdf")) {
                throw new IllegalArgumentException("Apenas ficheiros PDF sao suportados: " + normalizado);
            }

            Path destino = resolverDestinoPdf(pasta, nomeArquivo);
            Files.copy(normalizado, destino, StandardCopyOption.COPY_ATTRIBUTES);
            copiados.add(destino);
        }

        if (copiados.isEmpty()) {
            throw new IllegalArgumentException("Nenhum PDF valido foi selecionado.");
        }
        System.out.println("Cheguei");
        bibliotecaLivroService.sincronizarArquivos(disciplina.id(), copiados);
        return List.copyOf(copiados);
    }

    public Path pastaDisciplina(UUID disciplinaId) {
        if (disciplinaId == null) {
            throw new IllegalArgumentException("disciplinaId nao pode ser nulo.");
        }
        return uploadRoot.resolve(disciplinaId.toString());
    }

    private List<String> construirEstadoPdfs(List<Path> pdfs) throws IOException {
        if (pdfs == null || pdfs.isEmpty()) {
            return List.of();
        }

        ArrayList<String> estado = new ArrayList<>();
        for (Path pdf : pdfs) {
            if (pdf == null || Files.notExists(pdf) || !Files.isRegularFile(pdf) || pdf.getFileName() == null) {
                continue;
            }

            String nomeCodificado = Base64.getEncoder()
                .encodeToString(pdf.getFileName().toString().getBytes(StandardCharsets.UTF_8));
            estado.add(
                nomeCodificado
                    + "|"
                    + Files.size(pdf)
                    + "|"
                    + Files.getLastModifiedTime(pdf).toMillis()
            );
        }
        return List.copyOf(estado);
    }

    private Path resolverDestinoPdf(Path pasta, String nomeOriginal) {
        int ponto = nomeOriginal.lastIndexOf('.');
        String base = ponto > 0 ? nomeOriginal.substring(0, ponto) : nomeOriginal;
        String extensao = ponto > 0 ? nomeOriginal.substring(ponto) : ".pdf";

        Path destino = pasta.resolve(nomeOriginal);
        if (Files.notExists(destino)) {
            return destino;
        }

        int sequencia = 2;
        while (true) {
            Path candidato = pasta.resolve(base + "-" + sequencia + extensao);
            if (Files.notExists(candidato)) {
                return candidato;
            }
            sequencia++;
        }
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
        List<DisciplinaDto> disciplinas = DisciplinaService.discCategoria().stream()
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
