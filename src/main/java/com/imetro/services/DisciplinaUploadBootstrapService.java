package com.imetro.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.imetro.domain.dto.biblioteca.BibliotecaLivroDto;
import com.imetro.domain.dto.disciplina.DisciplinaDto;
import com.imetro.domain.dto.gemini.ExtracaoTopicosRequest;
import com.imetro.domain.dto.perguntas.TopicoSubtopico;
import com.imetro.domain.enums.TopicoExame;
import com.imetro.persistence.repository.BibliotecaLivroRepository;
import com.imetro.util.AppLogger;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class DisciplinaUploadBootstrapService {

    private static final String DEFAULT_OUTPUT_FILE = "topicos-extraidos.json";
    private static final String PROCESSED_STATE_FILE = ".livros-processados.snapshot";
    private static final Logger LOGGER = AppLogger.getLogger(DisciplinaUploadBootstrapService.class);

    private static final int CHUNK_PAGE_SIZE = 30;
    private static final int MAX_PAGES_BEFORE_CHUNK = 50;
    private static final int MIN_TEXT_LENGTH = 100;
    private final ObjectMapper objectMapper = new ObjectMapper();
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
        this.geminiService.setBibliotecaLivroRepository(new BibliotecaLivroRepository());
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

        String jsonTopicosAgregado = extrairTopicosComMapaPorPdf(disciplina, pdfs, instrucoesCanonicas);

        Files.writeString(
            arquivoTopicos,
            jsonTopicosAgregado,
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

    private boolean pdfTemTextoExtraivel(Path pdfPath) {
        try (PDDocument doc = Loader.loadPDF(pdfPath.toFile())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String texto = stripper.getText(doc);
            if (texto == null || texto.trim().length() < MIN_TEXT_LENGTH) {
                LOGGER.warning("PDF sem texto extraivel suficiente: " + pdfPath.getFileName()
                    + " (" + (texto == null ? 0 : texto.trim().length()) + " caracteres)");
                return false;
            }
            return true;
        } catch (Exception e) {
            LOGGER.warning("Nao foi possivel extrair texto do PDF " + pdfPath.getFileName() + ": " + e.getMessage());
            return false;
        }
    }

    private List<Path> criarChunksPdf(Path pdfPath, int totalPaginas) throws IOException {
        ArrayList<Path> chunks = new ArrayList<>();
        String nomeBase = pdfPath.getFileName().toString();
        int ponto = nomeBase.lastIndexOf('.');
        String nomeSemExt = ponto > 0 ? nomeBase.substring(0, ponto) : nomeBase;

        for (int inicio = 1; inicio <= totalPaginas; inicio += CHUNK_PAGE_SIZE) {
            int fim = Math.min(inicio + CHUNK_PAGE_SIZE - 1, totalPaginas);

            try (PDDocument original = Loader.loadPDF(pdfPath.toFile())) {
                PDDocument chunk = new PDDocument();
                for (int i = inicio; i <= fim; i++) {
                    chunk.addPage(original.getPage(i - 1));
                }
                Path tempFile = Files.createTempFile("imetro-chunk-", "-" + nomeSemExt + "-paginas-" + inicio + "-" + fim + ".pdf");
                chunk.save(tempFile.toFile());
                chunk.close();
                tempFile.toFile().deleteOnExit();
                chunks.add(tempFile);
            }
        }
        LOGGER.info("PDF " + pdfPath.getFileName() + " dividido em " + chunks.size() + " chunks de ate " + CHUNK_PAGE_SIZE + " paginas cada.");
        return chunks;
    }

    private String extrairTopicosComMapaPorPdf(
        DisciplinaDto disciplina,
        List<Path> pdfs,
        String instrucoesCanonicas
    ) throws IOException, InterruptedException {
        ArrayList<String> resultados = new ArrayList<>();

        for (Path pdf : pdfs) {
            String nomeArquivo = pdf.getFileName().toString();
            LOGGER.info("A extrair topicos do PDF: " + nomeArquivo);

            if (!pdfTemTextoExtraivel(pdf)) {
                LOGGER.warning("PDF ignorado por nao ter texto extraivel: " + nomeArquivo);
                continue;
            }

            int totalPaginas = contarPaginas(pdf);

            if (totalPaginas > MAX_PAGES_BEFORE_CHUNK) {
                LOGGER.info("PDF " + nomeArquivo + " tem " + totalPaginas
                    + " paginas. A dividir em chunks para melhor processamento.");
                String jsonAgregado = extrairTopicosPorChunks(disciplina, pdf, totalPaginas, instrucoesCanonicas);
                resultados.add(jsonAgregado);
                salvarMapaTopicosDoPdf(pdf, jsonAgregado);
            } else {
                String jsonTopicos = geminiService.extrairTopicosJson(
                    List.of(pdf),
                    new ExtracaoTopicosRequest(
                        disciplina.nome(),
                        "pt-AO",
                        "Organiza os topicos com foco no conteudo programatico da disciplina " + disciplina.nome() + "."
                            + (instrucoesCanonicas.isBlank() ? "" : "\n" + instrucoesCanonicas)
                    )
                );
                resultados.add(jsonTopicos);
                salvarMapaTopicosDoPdf(pdf, jsonTopicos);
            }
        }

        return agregarTopicosJson(resultados);
    }

    private String extrairTopicosPorChunks(
        DisciplinaDto disciplina,
        Path pdf,
        int totalPaginas,
        String instrucoesCanonicas
    ) throws IOException, InterruptedException {
        List<Path> chunks = criarChunksPdf(pdf, totalPaginas);
        ArrayList<String> resultadosChunks = new ArrayList<>();

        try {
            for (Path chunk : chunks) {
                LOGGER.info("A processar chunk: " + chunk.getFileName());
                String jsonTopicos = geminiService.extrairTopicosJson(
                    List.of(chunk),
                    new ExtracaoTopicosRequest(
                        disciplina.nome(),
                        "pt-AO",
                        "Extrai os topicos deste fragmento do livro (paginas " + extrairFaixaPaginas(chunk) + ")."
                            + (instrucoesCanonicas.isBlank() ? "" : "\n" + instrucoesCanonicas)
                    )
                );
                resultadosChunks.add(jsonTopicos);
            }
        } finally {
            for (Path chunk : chunks) {
                try {
                    Files.deleteIfExists(chunk);
                } catch (IOException e) {
                    LOGGER.warning("Nao foi possivel apagar chunk temporario: " + chunk);
                }
            }
        }

        return agregarTopicosJson(resultadosChunks);
    }

    private String extrairFaixaPaginas(Path chunkPath) {
        String nome = chunkPath.getFileName().toString();
        int idx = nome.lastIndexOf("-paginas-");
        if (idx > 0) {
            String rest = nome.substring(idx + "-paginas-".length());
            int ponto = rest.lastIndexOf('.');
            if (ponto > 0) return rest.substring(0, ponto);
            return rest;
        }
        return "desconhecida";
    }

    private int contarPaginas(Path pdfPath) throws IOException {
        try (PDDocument doc = Loader.loadPDF(pdfPath.toFile())) {
            return doc.getNumberOfPages();
        }
    }

    private void salvarMapaTopicosDoPdf(Path pdf, String jsonTopicos) {
        try {
            Optional<BibliotecaLivroDto> livro = bibliotecaLivroService.encontrarLivroPorNomeArquivoETamanho(
                pdf.getFileName().toString(),
                Files.size(pdf)
            );
            if (livro.isEmpty()) {
                LOGGER.warning("Livro nao encontrado na biblioteca para o PDF: " + pdf.getFileName());
                return;
            }

            ArrayList<TopicoSubtopico> topicos = parseTopicosDoJson(jsonTopicos);
            if (topicos.isEmpty()) {
                LOGGER.warning("Nenhum topico extraido do PDF: " + pdf.getFileName());
                return;
            }

            bibliotecaLivroService.substituirMapaTopicos(livro.get().id(), topicos);
            LOGGER.info("Mapa de topicos guardado para o livro: " + livro.get().titulo()
                + " (" + topicos.size() + " subtopicos)");
        } catch (Exception e) {
            LOGGER.warning("Falha ao guardar mapa de topicos do PDF " + pdf.getFileName() + ": " + e.getMessage());
        }
    }

    private ArrayList<TopicoSubtopico> parseTopicosDoJson(String jsonTopicos) {
        ArrayList<TopicoSubtopico> pares = new ArrayList<>();
        if (jsonTopicos == null || jsonTopicos.isBlank()) {
            return pares;
        }
        try {
            JsonNode root = objectMapper.readTree(jsonTopicos);
            JsonNode topicosNode = root.get("topicos");
            if (topicosNode == null || !topicosNode.isArray()) {
                return pares;
            }

            LinkedHashSet<String> chaves = new LinkedHashSet<>();
            for (JsonNode topicoNode : topicosNode) {
                String nomeTopico = topicoNode.has("nome") && !topicoNode.get("nome").isNull()
                    ? topicoNode.get("nome").asText().trim()
                    : "Geral";
                if (nomeTopico.isBlank()) {
                    nomeTopico = "Geral";
                }

                int topicoPagInicio = topicoNode.has("pagina_inicio") ? topicoNode.get("pagina_inicio").asInt(0) : 0;
                int topicoPagFim = topicoNode.has("pagina_fim") ? topicoNode.get("pagina_fim").asInt(0) : 0;

                JsonNode subtopicosNode = topicoNode.get("subtopicos");
                if (subtopicosNode == null || !subtopicosNode.isArray()) {
                    continue;
                }

                for (JsonNode subtopicoNode : subtopicosNode) {
                    String nomeSubtopico = subtopicoNode.has("nome") && !subtopicoNode.get("nome").isNull()
                        ? subtopicoNode.get("nome").asText().trim()
                        : "";
                    if (nomeSubtopico.isBlank()) {
                        continue;
                    }

                    int pagInicio = subtopicoNode.has("pagina_inicio") ? subtopicoNode.get("pagina_inicio").asInt(topicoPagInicio) : topicoPagInicio;
                    int pagFim = subtopicoNode.has("pagina_fim") ? subtopicoNode.get("pagina_fim").asInt(topicoPagFim) : topicoPagFim;

                    String chave = (nomeTopico + "::" + nomeSubtopico).toLowerCase();
                    if (!chaves.add(chave)) {
                        continue;
                    }
                    pares.add(new TopicoSubtopico(nomeTopico, nomeSubtopico, pagInicio, pagFim));
                }
            }
        } catch (Exception e) {
            LOGGER.warning("Falha ao fazer parse do JSON de topicos com Jackson: " + e.getMessage());
        }
        return pares;
    }

    private String agregarTopicosJson(ArrayList<String> resultados) {
        if (resultados.isEmpty()) {
            return "{\"topicos\":[],\"observacoes\":\"Nenhum PDF processado.\"}";
        }
        if (resultados.size() == 1) {
            return resultados.getFirst();
        }

        try {
            ObjectNode agregado = objectMapper.createObjectNode();
            ArrayNode todosTopicos = agregado.putArray("topicos");

            for (String json : resultados) {
                JsonNode root = objectMapper.readTree(json);
                JsonNode topicosNode = root.get("topicos");
                if (topicosNode != null && topicosNode.isArray()) {
                    for (JsonNode topico : topicosNode) {
                        todosTopicos.add(topico);
                    }
                }
            }

            agregado.put("fonteResumo", "Agregado de " + resultados.size() + " PDF(s).");
            agregado.put("observacoes", "Mapa de topicos agregado de varios PDFs.");
            return objectMapper.writeValueAsString(agregado);
        } catch (Exception e) {
            LOGGER.warning("Falha ao agregar JSONs de topicos: " + e.getMessage());
            return "{\"topicos\":[],\"observacoes\":\"Erro ao agregar topicos: " + e.getMessage() + "\"}";
        }
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
