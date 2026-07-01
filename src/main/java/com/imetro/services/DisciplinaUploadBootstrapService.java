package com.imetro.services;

import com.imetro.domain.dto.biblioteca.BibliotecaLivroDto;
import com.imetro.domain.dto.disciplina.DisciplinaDto;
import com.imetro.domain.dto.gemini.ExtracaoTopicosRequest;
import com.imetro.domain.dto.perguntas.TopicoSubtopico;
import com.imetro.domain.enums.TopicoExame;
import com.imetro.persistence.repository.BibliotecaLivroRepository;
import com.imetro.util.AppLogger;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class DisciplinaUploadBootstrapService {

    private static final String DEFAULT_OUTPUT_FILE = "topicos-extraidos.json";
    private static final String PROCESSED_STATE_FILE = ".livros-processados.snapshot";
    private static final Logger LOGGER = AppLogger.getLogger(DisciplinaUploadBootstrapService.class);
    private static final Pattern JSON_STRING_PATTERN = Pattern.compile("\"((?:\\\\.|[^\"\\\\])*)\"");
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

    private String extrairTopicosComMapaPorPdf(
        DisciplinaDto disciplina,
        List<Path> pdfs,
        String instrucoesCanonicas
    ) throws IOException, InterruptedException {
        ArrayList<String> resultados = new ArrayList<>();

        for (Path pdf : pdfs) {
            String nomeArquivo = pdf.getFileName().toString();
            LOGGER.info("A extrair topicos do PDF: " + nomeArquivo);

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

        return agregarTopicosJson(resultados);
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
        String topicosArray = extrairCampoArrayJson(jsonTopicos, "topicos");
        if (topicosArray == null || topicosArray.isBlank()) {
            return pares;
        }

        LinkedHashSet<String> chaves = new LinkedHashSet<>();
        for (String objetoTopico : extrairObjetosJsonArray(topicosArray)) {
            String nomeTopico = extrairCampoStringJson(objetoTopico, "nome");
            if (nomeTopico == null || nomeTopico.isBlank()) {
                nomeTopico = "Geral";
            }
            nomeTopico = nomeTopico.trim();

            int topicoPagInicio = extrairCampoInteiroJson(objetoTopico, "pagina_inicio", 0);
            int topicoPagFim = extrairCampoInteiroJson(objetoTopico, "pagina_fim", 0);

            String subtopicosArray = extrairCampoArrayJson(objetoTopico, "subtopicos");
            if (subtopicosArray == null || subtopicosArray.isBlank()) {
                continue;
            }

            for (String objetoSubtopico : extrairObjetosJsonArray(subtopicosArray)) {
                String nomeSubtopico = extrairCampoStringJson(objetoSubtopico, "nome");
                if (nomeSubtopico == null || nomeSubtopico.isBlank()) {
                    continue;
                }
                nomeSubtopico = nomeSubtopico.trim();

                int pagInicio = extrairCampoInteiroJson(objetoSubtopico, "pagina_inicio", topicoPagInicio);
                int pagFim = extrairCampoInteiroJson(objetoSubtopico, "pagina_fim", topicoPagFim);

                String chave = (nomeTopico + "::" + nomeSubtopico).toLowerCase();
                if (!chaves.add(chave)) {
                    continue;
                }
                pares.add(new TopicoSubtopico(nomeTopico, nomeSubtopico, pagInicio, pagFim));
            }
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

        StringBuilder agregado = new StringBuilder();
        agregado.append("{\"topicos\":[");
        boolean primeiro = true;
        for (String json : resultados) {
            String topicosArray = extrairCampoArrayJson(json, "topicos");
            if (topicosArray == null || topicosArray.isBlank()) {
                continue;
            }
            String conteudo = topicosArray.substring(1, topicosArray.length() - 1).trim();
            if (conteudo.isBlank()) {
                continue;
            }
            if (!primeiro) {
                agregado.append(',');
            }
            agregado.append(conteudo);
            primeiro = false;
        }
        agregado.append("],\"fonteResumo\":\"Agregado de ")
            .append(resultados.size()).append(" PDF(s).\",");
        agregado.append("\"observacoes\":\"Mapa de topicos agregado de varios PDFs.\"}");
        return agregado.toString();
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

    private String extrairCampoStringJson(String json, String campo) {
        if (json == null || json.isBlank() || campo == null || campo.isBlank()) {
            return null;
        }

        Pattern pattern = Pattern.compile("\"" + Pattern.quote(campo) + "\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"");
        Matcher matcher = pattern.matcher(json);
        if (!matcher.find()) {
            return null;
        }
        return matcher.group(1);
    }

    private int extrairCampoInteiroJson(String json, String campo, int padrao) {
        if (json == null || json.isBlank() || campo == null || campo.isBlank()) {
            return padrao;
        }

        Pattern pattern = Pattern.compile("\"" + Pattern.quote(campo) + "\"\\s*:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(json);
        if (!matcher.find()) {
            return padrao;
        }

        try {
            return Integer.parseInt(matcher.group(1));
        } catch (NumberFormatException e) {
            return padrao;
        }
    }

    private String extrairCampoArrayJson(String json, String campo) {
        if (json == null || json.isBlank() || campo == null || campo.isBlank()) {
            return null;
        }

        Pattern pattern = Pattern.compile("\"" + Pattern.quote(campo) + "\"\\s*:\\s*\\[", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(json);
        if (!matcher.find()) {
            return null;
        }

        int inicioArray = matcher.end() - 1;
        int fimArray = localizarFechoJson(json, inicioArray, '[', ']');
        if (fimArray < 0) {
            return null;
        }
        return json.substring(inicioArray, fimArray + 1);
    }

    private ArrayList<String> extrairObjetosJsonArray(String arrayJson) {
        ArrayList<String> objetos = new ArrayList<>();
        if (arrayJson == null || arrayJson.isBlank()) {
            return objetos;
        }

        int cursor = 0;
        while (cursor < arrayJson.length()) {
            char atual = arrayJson.charAt(cursor);
            if (atual != '{') {
                cursor++;
                continue;
            }

            int fimObjeto = localizarFechoJson(arrayJson, cursor, '{', '}');
            if (fimObjeto < 0) {
                break;
            }

            objetos.add(arrayJson.substring(cursor, fimObjeto + 1));
            cursor = fimObjeto + 1;
        }

        return objetos;
    }

    private int localizarFechoJson(String json, int inicio, char aberto, char fechado) {
        if (json == null || inicio < 0 || inicio >= json.length()) {
            return -1;
        }

        int profundidade = 0;
        boolean emString = false;
        for (int i = inicio; i < json.length(); i++) {
            char c = json.charAt(i);
            if (emString) {
                if (c == '\\') {
                    i++;
                } else if (c == '"') {
                    emString = false;
                }
                continue;
            }

            if (c == '"') {
                emString = true;
                continue;
            }

            if (c == aberto) {
                profundidade++;
            } else if (c == fechado) {
                profundidade--;
                if (profundidade == 0) {
                    return i;
                }
            }
        }

        return -1;
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
