package com.imetro.services;

import com.imetro.config.Env;
import com.imetro.domain.dto.biblioteca.BibliotecaLivroDto;
import com.imetro.domain.dto.gemini.ExtracaoTopicosRequest;
import com.imetro.domain.dto.gemini.GeracaoSimuladoRequest;
import com.imetro.domain.dto.gemini.ParsedJsonString;
import com.imetro.domain.dto.gemini.UploadedPdf;
import com.imetro.persistence.repository.BibliotecaLivroRepository;
import com.imetro.util.TextoUtil;
import com.imetro.util.AppLogger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GeminiService {

    private static final String API_BASE_URL = "https://generativelanguage.googleapis.com/v1beta";
    private static final String DEFAULT_MODEL = "gemini-2.5-flash";
    private static final String MIME_TYPE_PDF = "application/pdf";
    private static final String GENERATE_CONTENT_URL_TEMPLATE = API_BASE_URL + "/models/%s:generateContent";
    private static final String FILES_START_URL_TEMPLATE =
        "https://generativelanguage.googleapis.com/upload/v1beta/files?key=%s";
    private static final Duration FILE_UPLOAD_TIMEOUT = Duration.ofMinutes(5);
    private static final Duration FILE_PROCESSING_TIMEOUT = Duration.ofMinutes(3);
    private static final Duration FILE_PROCESSING_POLL_INTERVAL = Duration.ofSeconds(2);
    private static final Duration GENERATE_CONTENT_RETRY_BASE_DELAY = Duration.ofSeconds(2);
    public static final int DEFAULT_SIMULADO_QUESTOES = 48;
    private static final int MINIMO_QUESTOES_BASE_TOPICOS = 72;
    private static final int GENERATE_CONTENT_MAX_ATTEMPTS = 3;

    private static final long MAX_DOCUMENT_BYTES = 50L * 1024L * 1024L;
    private static final long INLINE_BYTES_LIMIT = 15L * 1024L * 1024L;
    private static final Logger LOGGER = AppLogger.getLogger(GeminiService.class);

    private final HttpClient httpClient;
    private final String apiKey;
    private final String defaultModel;
    private final Map<Path, UploadedPdf> uploadCache = new ConcurrentHashMap<>();
    private BibliotecaLivroRepository bibliotecaLivroRepository;

    public GeminiService() {
        this(
            HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .version(HttpClient.Version.HTTP_1_1)
                .build(),
            firstNonBlank(Env.get("GEMINI_API_KEY"), Env.get("GEMENI_API_KEY")),
            firstNonBlank(Env.get("GEMINI_MODEL"), Env.get("GEMENI_MODEL"), DEFAULT_MODEL)
        );
    }

    GeminiService(HttpClient httpClient, String apiKey, String defaultModel) {
        this.httpClient = Objects.requireNonNull(httpClient, "httpClient");
        this.apiKey = apiKey == null ? null : apiKey.trim();
        this.defaultModel = firstNonBlank(defaultModel, DEFAULT_MODEL);
    }

    public void setBibliotecaLivroRepository(BibliotecaLivroRepository bibliotecaLivroRepository) {
        this.bibliotecaLivroRepository = bibliotecaLivroRepository;
    }

    public boolean isConfigured() {
        return apiKey != null && !apiKey.isBlank();
    }

    public String getDefaultModel() {
        return defaultModel;
    }

    public String enviarPromptComPdf(Path pdfPath, String prompt) throws IOException, InterruptedException {
        return enviarPromptComPdfs(List.of(pdfPath), prompt);
    }

    public String enviarPromptComPdfs(List<Path> pdfPaths, String prompt) throws IOException, InterruptedException {
        List<Path> documentos = validarDocumentos(pdfPaths);
        String promptFinal = requireNonBlank(prompt, "prompt");
        LOGGER.info("A enviar prompt com " + documentos.size() + " PDF(s) ao Gemini.");

        List<String> documentParts = deveUsarFilesApi(documentos)
            ? criarPartesPorUpload(documentos)
            : criarPartesInline(documentos);

        String requestBody = montarRequestGenerateContent(
            promptFinal,
            documentParts,
            "text/plain",
            null
        );

        String responseBody = postGenerateContent(requestBody);

        String text = extrairTextoResposta(responseBody);
        if (text == null || text.isBlank()) {
            throw new IOException("Gemini respondeu sem texto util. Corpo: " + resumir(responseBody));
        }
        return text;
    }

    public String gerarSimuladoJson(Path pdfPath) throws IOException, InterruptedException {
        return gerarSimuladoJson(List.of(pdfPath), GeracaoSimuladoRequest.padrao());
    }

    public String gerarSimuladoJson(Path pdfPath, GeracaoSimuladoRequest request)
        throws IOException, InterruptedException {
        return gerarSimuladoJson(List.of(pdfPath), request);
    }

    public String gerarSimuladoJson(List<Path> pdfPaths, GeracaoSimuladoRequest request)
        throws IOException, InterruptedException {
        GeracaoSimuladoRequest requestFinal = request == null ? GeracaoSimuladoRequest.padrao() : request;
        return gerarJsonEstruturado(
            pdfPaths,
            montarPromptSimulado(requestFinal),
            SIMULADO_JSON_SCHEMA,
            "Gemini nao devolveu o JSON do simulado."
        );
    }

    public String gerarSimuladoJsonAPartirDeTopicos(String topicosJson, GeracaoSimuladoRequest request)
        throws IOException, InterruptedException {
        return gerarSimuladoJsonAPartirDeTopicos(topicosJson, request, true);
    }

    public String gerarSimuladoJsonAPartirDeTopicosEmLote(String topicosJson, GeracaoSimuladoRequest request)
        throws IOException, InterruptedException {
        return gerarSimuladoJsonAPartirDeTopicos(topicosJson, request, false);
    }

    private String gerarSimuladoJsonAPartirDeTopicos(
        String topicosJson,
        GeracaoSimuladoRequest request,
        boolean expandirQuantidadeMinima
    ) throws IOException, InterruptedException {
        GeracaoSimuladoRequest baseRequest = request == null ? GeracaoSimuladoRequest.padrao() : request;
        GeracaoSimuladoRequest requestFinal = expandirQuantidadeMinima
            ? expandirRequestParaBaseDeQuestoes(baseRequest)
            : baseRequest;
        String contexto = requireNonBlank(topicosJson, "topicosJson");
        LOGGER.info(
            "A gerar base alargada de questoes a partir do JSON de topicos para a disciplina "
                + requestFinal.disciplina()
                + " com minimo de "
                + requestFinal.quantidadeQuestoes()
                + " questoes."
        );

        return gerarJsonEstruturadoSemDocumentos(
            montarPromptSimuladoPorTopicos(contexto, requestFinal),
            SIMULADO_JSON_SCHEMA,
            "Gemini nao devolveu o JSON do simulado."
        );
    }

    public String extrairTopicosJson(Path pdfPath) throws IOException, InterruptedException {
        return extrairTopicosJson(List.of(pdfPath), ExtracaoTopicosRequest.padrao());
    }

    public String extrairTopicosJson(Path pdfPath, ExtracaoTopicosRequest request)
        throws IOException, InterruptedException {
        return extrairTopicosJson(List.of(pdfPath), request);
    }

    public String extrairTopicosJson(List<Path> pdfPaths, ExtracaoTopicosRequest request)
        throws IOException, InterruptedException {
        ExtracaoTopicosRequest requestFinal = request == null ? ExtracaoTopicosRequest.padrao() : request;
        LOGGER.info(
            "A extrair topicos com o Gemini para a disciplina " + requestFinal.disciplina()
                + " usando " + (pdfPaths == null ? 0 : pdfPaths.size()) + " PDF(s)."
        );
        return gerarJsonEstruturado(
            pdfPaths,
            montarPromptExtracaoTopicos(requestFinal),
            TOPICOS_JSON_SCHEMA,
            "Gemini nao devolveu o JSON de topicos."
        );
    }

    private List<Path> validarDocumentos(List<Path> pdfPaths) throws IOException {
        if (!isConfigured()) {
            LOGGER.warning("Tentativa de usar o Gemini sem API key configurada.");
            throw new IllegalStateException(
                "Gemini nao configurado. Define GEMINI_API_KEY ou GEMENI_API_KEY no ambiente."
            );
        }
        if (pdfPaths == null || pdfPaths.isEmpty()) {
            throw new IllegalArgumentException("Indica pelo menos um PDF para enviar ao Gemini.");
        }

        ArrayList<Path> documentos = new ArrayList<>();
        for (Path pdfPath : pdfPaths) {
            if (pdfPath == null) {
                continue;
            }

            Path normalizado = pdfPath.toAbsolutePath().normalize();
            if (!Files.exists(normalizado) || !Files.isRegularFile(normalizado)) {
                throw new IOException("PDF nao encontrado: " + normalizado);
            }

            String nome = normalizado.getFileName().toString().toLowerCase(Locale.ROOT);
            if (!nome.endsWith(".pdf")) {
                throw new IllegalArgumentException("Apenas ficheiros PDF sao suportados: " + normalizado);
            }

            long size = Files.size(normalizado);
            if (size <= 0) {
                throw new IllegalArgumentException("O PDF esta vazio: " + normalizado);
            }
            if (size > MAX_DOCUMENT_BYTES) {
                throw new IllegalArgumentException(
                    "O PDF excede 50MB e nao pode ser enviado ao Gemini: " + normalizado
                );
            }

            documentos.add(normalizado);
        }

        if (documentos.isEmpty()) {
            throw new IllegalArgumentException("Nenhum PDF valido foi informado.");
        }
        return List.copyOf(documentos);
    }

    private boolean deveUsarFilesApi(List<Path> documentos) throws IOException {
        if (documentos.size() > 1) {
            return true;
        }

        long totalBytes = 0L;
        for (Path documento : documentos) {
            totalBytes += Files.size(documento);
        }
        return totalBytes > INLINE_BYTES_LIMIT;
    }

    private List<String> criarPartesInline(List<Path> documentos) throws IOException {
        ArrayList<String> partes = new ArrayList<>();
        for (Path documento : documentos) {
            byte[] pdfBytes = Files.readAllBytes(documento);
            String base64 = Base64.getEncoder().encodeToString(pdfBytes);
            partes.add(
                "{\"inline_data\":{\"mime_type\":\"" + MIME_TYPE_PDF + "\",\"data\":\"" + base64 + "\"}}"
            );
        }
        return partes;
    }

    private List<String> criarPartesPorUpload(List<Path> documentos) throws IOException, InterruptedException {
        ArrayList<String> partes = new ArrayList<>();
        for (Path documento : documentos) {
            LOGGER.info("A preparar upload do PDF " + documento.getFileName() + ".");
            UploadedPdf uploadedPdf = uploadPdf(documento);
            partes.add(
                "{\"file_data\":{\"mime_type\":\"" + uploadedPdf.mimeType() + "\",\"file_uri\":\""
                    + escapeJson(uploadedPdf.uri()) + "\"}}"
            );
        }
        return partes;
    }

    private UploadedPdf uploadPdf(Path pdfPath) throws IOException, InterruptedException {
        Path caminhoNormalizado = pdfPath.toAbsolutePath().normalize();
        UploadedPdf cached = uploadCache.get(caminhoNormalizado);
        if (cached != null) {
            LOGGER.info("PDF ja enviado anteriormente: " + pdfPath.getFileName() + " (usa cache de memoria).");
            return cached;
        }

        UploadedPdf dbCached = buscarUploadNaBaseDeDados(caminhoNormalizado);
        if (dbCached != null) {
            LOGGER.info("PDF ja enviado anteriormente: " + pdfPath.getFileName() + " (usa cache da base de dados).");
            uploadCache.put(caminhoNormalizado, dbCached);
            return dbCached;
        }

        long fileSize = Files.size(pdfPath);
        LOGGER.info("A iniciar upload do PDF " + pdfPath.getFileName() + " (" + fileSize + " bytes).");
        String startBody = "{\"file\":{\"display_name\":\"" + escapeJson(pdfPath.getFileName().toString()) + "\"}}";

        HttpRequest startRequest = HttpRequest.newBuilder()
            .uri(URI.create(FILES_START_URL_TEMPLATE.formatted(apiKey)))
            .timeout(Duration.ofSeconds(60))
            .header("Content-Type", "application/json")
            .header("X-Goog-Upload-Protocol", "resumable")
            .header("X-Goog-Upload-Command", "start")
            .header("X-Goog-Upload-Header-Content-Length", Long.toString(fileSize))
            .header("X-Goog-Upload-Header-Content-Type", MIME_TYPE_PDF)
            .POST(HttpRequest.BodyPublishers.ofString(startBody))
            .build();

        HttpResponse<String> startResponse = httpClient.send(
            startRequest,
            HttpResponse.BodyHandlers.ofString()
        );
        ensureSuccess("iniciar upload do PDF", startResponse.statusCode(), startResponse.body());

        String uploadUrl = firstHeader(startResponse, "x-goog-upload-url")
            .orElseThrow(() -> new IOException("Gemini nao devolveu a URL do upload resumable."));
        LOGGER.info("Upload resumable iniciado para " + pdfPath.getFileName() + ".");

        HttpRequest uploadRequest = HttpRequest.newBuilder()
            .uri(URI.create(uploadUrl))
            .timeout(FILE_UPLOAD_TIMEOUT)
            .header("Content-Type", MIME_TYPE_PDF)
            .header("X-Goog-Upload-Offset", "0")
            .header("X-Goog-Upload-Command", "upload, finalize")
            .POST(HttpRequest.BodyPublishers.ofFile(pdfPath))
            .build();

        HttpResponse<String> uploadResponse = httpClient.send(
            uploadRequest,
            HttpResponse.BodyHandlers.ofString()
        );
        ensureSuccess("finalizar upload do PDF", uploadResponse.statusCode(), uploadResponse.body());
        LOGGER.info("Upload finalizado para " + pdfPath.getFileName() + ".");

        String fileUri = extractFirstJsonStringValue(uploadResponse.body(), "uri");
        if (fileUri == null || fileUri.isBlank()) {
            throw new IOException("Gemini nao devolveu file_uri para o PDF. Corpo: " + resumir(uploadResponse.body()));
        }

        String fileName = extractFirstJsonStringValue(uploadResponse.body(), "name");
        String fileState = extractFirstJsonStringValue(uploadResponse.body(), "state");
        String mimeType = firstNonBlank(
            extractFirstJsonStringValue(uploadResponse.body(), "mimeType"),
            MIME_TYPE_PDF
        );

        UploadedPdf uploadedPdf = aguardarArquivoAtivo(new UploadedPdf(fileUri, mimeType, fileName, fileState));
        uploadCache.put(caminhoNormalizado, uploadedPdf);
        salvarUploadNaBaseDeDados(caminhoNormalizado, uploadedPdf);
        return uploadedPdf;
    }

    private UploadedPdf buscarUploadNaBaseDeDados(Path caminhoNormalizado) {
        if (bibliotecaLivroRepository == null) {
            return null;
        }

        try {
            String nomeArquivo = caminhoNormalizado.getFileName().toString();
            long tamanhoBytes = Files.size(caminhoNormalizado);
            Optional<BibliotecaLivroDto> livro = bibliotecaLivroRepository.findByNomeArquivoETamanho(nomeArquivo, tamanhoBytes);
            if (livro.isPresent() && livro.get().possuiGeminiUpload()) {
                BibliotecaLivroDto dto = livro.get();
                UploadedPdf uploaded = new UploadedPdf(
                    dto.geminiFileUri(),
                    dto.mimeType(),
                    dto.geminiFileName(),
                    null
                );
                try {
                    UploadedPdf atual = consultarArquivo(uploaded);
                    if ("ACTIVE".equals(atual.state())) {
                        return atual;
                    }
                    LOGGER.warning("Upload em cache no BD ja nao esta ACTIVE: " + nomeArquivo + " (" + atual.state() + "). Vai re-enviar.");
                } catch (IOException e) {
                    LOGGER.warning("Nao foi possivel verificar upload em cache no BD: " + e.getMessage() + ". Vai re-enviar.");
                }
            }
        } catch (Exception e) {
            LOGGER.warning("Falha ao consultar cache de upload no BD: " + e.getMessage());
        }
        return null;
    }

    private void salvarUploadNaBaseDeDados(Path caminhoNormalizado, UploadedPdf uploadedPdf) {
        if (bibliotecaLivroRepository == null) {
            return;
        }

        try {
            String nomeArquivo = caminhoNormalizado.getFileName().toString();
            long tamanhoBytes = Files.size(caminhoNormalizado);
            Optional<BibliotecaLivroDto> livro = bibliotecaLivroRepository.findByNomeArquivoETamanho(nomeArquivo, tamanhoBytes);
            if (livro.isPresent()) {
                bibliotecaLivroRepository.atualizarGeminiUpload(
                    livro.get().id(),
                    uploadedPdf.uri(),
                    uploadedPdf.name()
                );
                LOGGER.info("Upload do PDF salvo no cache da base de dados: " + nomeArquivo);
            }
        } catch (Exception e) {
            LOGGER.warning("Falha ao salvar upload no cache do BD: " + e.getMessage());
        }
    }

    private String gerarJsonEstruturado(
        List<Path> pdfPaths,
        String prompt,
        String jsonSchema,
        String emptyResponseMessage
    ) throws IOException, InterruptedException {
        List<Path> documentos = validarDocumentos(pdfPaths);

        List<String> documentParts = deveUsarFilesApi(documentos)
            ? criarPartesPorUpload(documentos)
            : criarPartesInline(documentos);

        String requestBody = montarRequestGenerateContent(
            prompt,
            documentParts,
            "application/json",
            jsonSchema
        );

        String responseBody = postGenerateContent(requestBody);

        String json = extrairTextoResposta(responseBody);
        if (json == null || json.isBlank()) {
            throw new IOException(emptyResponseMessage + " Corpo: " + resumir(responseBody));
        }
        return json;
    }

    private String gerarJsonEstruturadoSemDocumentos(
        String prompt,
        String jsonSchema,
        String emptyResponseMessage
    ) throws IOException, InterruptedException {
        String requestBody = montarRequestGenerateContent(
            prompt,
            List.of(),
            "application/json",
            jsonSchema
        );

        String responseBody = postGenerateContent(requestBody);
        String json = extrairTextoResposta(responseBody);
        if (json == null || json.isBlank()) {
            throw new IOException(emptyResponseMessage + " Corpo: " + resumir(responseBody));
        }
        return json;
    }

    private String montarRequestGenerateContent(
        String prompt,
        List<String> documentParts,
        String responseMimeType,
        String responseJsonSchema
    ) {
        StringBuilder body = new StringBuilder();
        body.append("{\"contents\":[{\"parts\":[");
        body.append("{\"text\":\"").append(escapeJson(prompt)).append("\"}");

        for (String documentPart : documentParts) {
            body.append(",").append(documentPart);
        }

        body.append("]}],\"generationConfig\":{");
        body.append("\"temperature\":0.2,");
        body.append("\"responseMimeType\":\"").append(escapeJson(responseMimeType)).append("\"");

        if (responseJsonSchema != null && !responseJsonSchema.isBlank()) {
            body.append(",\"responseJsonSchema\":").append(responseJsonSchema);
        }

        body.append("}}");
        return body.toString();
    }

    private String postJson(URI uri, String jsonBody) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(uri)
            .timeout(Duration.ofMinutes(5))
            .header("Content-Type", "application/json")
            .header("x-goog-api-key", apiKey)
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build();

        HttpResponse<String> response = httpClient.send(
            request,
            HttpResponse.BodyHandlers.ofString()
        );
        ensureSuccess("chamar generateContent", response.statusCode(), response.body());
        return response.body();
    }

    private String postGenerateContent(String jsonBody) throws IOException, InterruptedException {
        IOException lastError = null;

        for (String model : resolveModelAttempts()) {
            for (int attempt = 1; attempt <= GENERATE_CONTENT_MAX_ATTEMPTS; attempt++) {
                try {
                    LOGGER.info(
                        "A chamar generateContent no modelo " + model + " (tentativa "
                            + attempt + "/" + GENERATE_CONTENT_MAX_ATTEMPTS + ")."
                    );
                    return postJson(
                        URI.create(GENERATE_CONTENT_URL_TEMPLATE.formatted(model)),
                        jsonBody
                    );
                } catch (IOException e) {
                    lastError = e;
                    boolean retryable = isRetryableGenerateContentError(e);
                    boolean fallback = shouldTryModelFallback(model, e);
                    boolean hasMoreAttempts = attempt < GENERATE_CONTENT_MAX_ATTEMPTS;

                    LOGGER.log(
                        retryable || fallback ? Level.WARNING : Level.SEVERE,
                        "Falha ao chamar o Gemini no modelo " + model + ": " + e.getMessage(),
                        retryable || fallback ? null : e
                    );

                    if (retryable && hasMoreAttempts) {
                        long delayMillis = GENERATE_CONTENT_RETRY_BASE_DELAY.toMillis() * (1L << (attempt - 1));
                        LOGGER.info(
                            "Nova tentativa do Gemini em " + delayMillis + "ms para o modelo " + model + "."
                        );
                        Thread.sleep(delayMillis);
                        continue;
                    }

                    if (!fallback) {
                        throw e;
                    }
                    break;
                }
            }
        }

        if (lastError != null) {
            throw lastError;
        }
        throw new IOException("Nao foi possivel chamar o Gemini.");
    }

    private void ensureSuccess(String operacao, int statusCode, String body) throws IOException {
        if (statusCode >= 200 && statusCode < 300) {
            return;
        }

        String apiMessage = extractFirstJsonStringValue(body, "message");
        String detalhe = apiMessage == null || apiMessage.isBlank() ? resumir(body) : apiMessage;
        throw new IOException("Falha ao " + operacao + " no Gemini (HTTP " + statusCode + "): " + detalhe);
    }

    private Optional<String> firstHeader(HttpResponse<?> response, String headerName) {
        return response.headers().firstValue(headerName);
    }

    private String extrairTextoResposta(String responseBody) {
        List<String> textos = extractJsonStringValues(responseBody, "text");
        StringBuilder text = new StringBuilder();
        for (String trecho : textos) {
            if (text.length() > 0) {
                text.append('\n');
            }
            text.append(trecho);
        }
        return text.toString();
    }

    private String extractFirstJsonStringValue(String json, String fieldName) {
        List<String> values = extractJsonStringValues(json, fieldName);
        if (values.isEmpty()) {
            return null;
        }
        return values.getFirst();
    }

    private String montarPromptSimulado(GeracaoSimuladoRequest request) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Analisa apenas os PDFs anexados.\n");
        prompt.append("Gera um simulado em ").append(request.idioma()).append(".\n");
        prompt.append("Disciplina alvo: ").append(request.disciplina()).append(".\n");
        prompt.append("Quantidade minima de questoes: ").append(request.quantidadeQuestoes()).append(".\n");
        prompt.append("Nivel desejado: ").append(request.nivel()).append(".\n");
        prompt.append("Regras obrigatorias:\n");
        prompt.append("- Usa o nome da disciplina exatamente como foi recebido, incluindo acentos.\n");
        prompt.append("- Usa somente conteudo suportado pelos documentos.\n");
        prompt.append("- Cria exatamente 4 alternativas objetivas e apenas uma resposta correta.\n");
        prompt.append("- No campo respostaCorreta, devolve o texto exato da alternativa correta, nunca apenas a letra.\n");
        prompt.append("- No campo pesosAlternativas, devolve 4 numeros entre 0.0 e 1.0 alinhados com as 4 alternativas.\n");
        prompt.append("- A alternativa correta deve ter peso 1.0 e as outras devem ter pesos menores que 1.0.\n");
        prompt.append("- Usa pesos baixos para erros graves, pesos medios para distratores proximos e nunca deixes duas alternativas corretas.\n");
        prompt.append("- Nao repitas alternativas e nao uses alternativas genericas como 'todas as anteriores'.\n");
        prompt.append("- Mantem a dificuldade coerente com o material.\n");
        prompt.append("- No campo exercicio, devolve um bloco LaTeX curto e renderizavel pelo JLaTeXMath, sem markdown, sem delimitadores $ e sem texto explicativo extra.\n");
        prompt.append("- O exercicio deve mostrar a expressao, igualdade ou dados essenciais de forma elegante e compacta.\n");
        prompt.append("- Quando precisares de raiz quadrada, usa sempre \\\\sqrt{...} e nunca a palavra literal sqrt.\n");
        prompt.append("- Quando precisares de raiz n-esima, usa sempre \\\\sqrt[n]{...}.\n");
        prompt.append("- Nao uses o simbolo Unicode de raiz nem a palavra sqrt sem barra; escreve sempre \\\\sqrt{radicando} ou \\\\sqrt[n]{radicando}.\n");
        prompt.append("- Se o radicando tiver soma, fracao, potencia ou parenteses, inclui a expressao completa dentro das chavetas.\n");
        prompt.append("- Se a questao nao beneficiar de exercicio visual, devolve string vazia.\n");
        prompt.append("- Nao devolvas menos do que a quantidade minima pedida.\n");
        prompt.append("- Gera base suficiente para testes curtos, medios e longos.\n");
        prompt.append("- Para cada questao, define um campo rigor entre 0.0 e 1.0 conforme a profundidade exigida.\n");
        prompt.append("- Usa topicoPrincipal para resumir o eixo principal do conhecimento cobrado.\n");
        prompt.append("- Se houver multiplos PDFs, consolida os topicos em um unico simulado.\n");
        prompt.append("- No campo fonteResumo, resume em poucas linhas os assuntos-base dos PDFs.\n");
        prompt.append("- No campo explicacao, justifica a resposta correta de forma curta e objetiva.\n");
        prompt.append("- Antes de responder, verifica que respostaCorreta coincide exatamente com a alternativa cujo peso e 1.0.\n");
        appendContratoLeituraAncorada(prompt);
        appendContratoGrafico(prompt, request.disciplina());
        prompt.append("- Responde estritamente no JSON definido pelo schema, sem markdown.\n");

        if (request.instrucoesExtras() != null && !request.instrucoesExtras().isBlank()) {
            prompt.append("Instrucoes extra:\n");
            prompt.append(request.instrucoesExtras().trim()).append('\n');
        }

        return prompt.toString();
    }

    private String montarPromptSimuladoPorTopicos(String topicosJson, GeracaoSimuladoRequest request) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Analisa apenas o JSON de topicos abaixo, extraido previamente dos livros da disciplina.\n");
        prompt.append("Nao assumas conteudo fora do que esta presente nessa estrutura.\n");
        prompt.append("Gera um simulado em ").append(request.idioma()).append(".\n");
        prompt.append("Disciplina alvo: ").append(request.disciplina()).append(".\n");
        prompt.append("Quantidade minima de questoes: ").append(request.quantidadeQuestoes()).append(".\n");
        prompt.append("Nivel desejado: ").append(request.nivel()).append(".\n");
        prompt.append("Regras obrigatorias:\n");
        prompt.append("- Usa o nome da disciplina exatamente como foi recebido, incluindo acentos.\n");
        prompt.append("- Usa somente os topicos e subtopicos presentes no JSON informado.\n");
        prompt.append("- Cria exatamente 4 alternativas objetivas e apenas uma resposta correta.\n");
        prompt.append("- No campo respostaCorreta, devolve o texto exato da alternativa correta, nunca apenas a letra.\n");
        prompt.append("- No campo pesosAlternativas, devolve 4 numeros entre 0.0 e 1.0 alinhados com as 4 alternativas.\n");
        prompt.append("- A alternativa correta deve ter peso 1.0 e as outras devem ter pesos menores que 1.0.\n");
        prompt.append("- Usa pesos baixos para erros graves, pesos medios para distratores proximos e nunca deixes duas alternativas corretas.\n");
        prompt.append("- Nao repitas alternativas e nao uses alternativas genericas como 'todas as anteriores'.\n");
        prompt.append("- Equilibra a distribuicao das questoes pelos topicos principais.\n");
        prompt.append("- No campo exercicio, devolve um bloco LaTeX curto e renderizavel pelo JLaTeXMath, sem markdown, sem delimitadores $ e sem texto explicativo extra.\n");
        prompt.append("- O exercicio deve mostrar a expressao, igualdade ou dados essenciais de forma elegante e compacta.\n");
        prompt.append("- Quando precisares de raiz quadrada, usa sempre \\\\sqrt{...} e nunca a palavra literal sqrt.\n");
        prompt.append("- Quando precisares de raiz n-esima, usa sempre \\\\sqrt[n]{...}.\n");
        prompt.append("- Nao uses o simbolo Unicode de raiz nem a palavra sqrt sem barra; escreve sempre \\\\sqrt{radicando} ou \\\\sqrt[n]{radicando}.\n");
        prompt.append("- Se o radicando tiver soma, fracao, potencia ou parenteses, inclui a expressao completa dentro das chavetas.\n");
        prompt.append("- Se a questao nao beneficiar de exercicio visual, devolve string vazia.\n");
        prompt.append("- Nao devolvas menos do que a quantidade minima pedida.\n");
        prompt.append("- Gera uma base ampla o suficiente para testes curtos, medios e longos.\n");
        prompt.append("- Sempre que houver cobertura suficiente, distribui varias questoes por subtopico em niveis FACIL, MEDIO, DESAFIANTE e EXTRA.\n");
        prompt.append("- Para cada questao, define um campo rigor entre 0.0 e 1.0 conforme a profundidade exigida.\n");
        prompt.append("- Usa topicoPrincipal para resumir o eixo principal do conhecimento cobrado.\n");
        prompt.append("- No campo fonteResumo, resume a cobertura indicada no JSON.\n");
        prompt.append("- No campo explicacao, justifica a resposta correta de forma curta e objetiva.\n");
        prompt.append("- Antes de responder, verifica que respostaCorreta coincide exatamente com a alternativa cujo peso e 1.0.\n");
        appendContratoLeituraAncorada(prompt);
        appendContratoGrafico(prompt, request.disciplina());
        prompt.append("- Responde estritamente no JSON definido pelo schema, sem markdown.\n");
        prompt.append("JSON de topicos:\n");
        prompt.append(topicosJson).append('\n');

        if (request.instrucoesExtras() != null && !request.instrucoesExtras().isBlank()) {
            prompt.append("Instrucoes extra:\n");
            prompt.append(request.instrucoesExtras().trim()).append('\n');
        }

        return prompt.toString();
    }

    private void appendContratoLeituraAncorada(StringBuilder prompt) {
        prompt.append("- Gera apenas questoes que o aluno consegue revisar diretamente no material fornecido.\n");
        prompt.append("- Toda questao deve ter referenciaLivro, paginaInicio e paginaFim preenchidos.\n");
        prompt.append("- paginaInicio e paginaFim devem apontar para paginas reais onde o conceito aparece de forma suficiente para revisao.\n");
        prompt.append("- Se existir CATALOGO_LIVROS_DA_DISCIPLINA nas instrucoes extra, usa referenciaLivro exatamente com um dos nomes listados.\n");
        prompt.append("- Se nao conseguires provar a questao com um livro real e paginas reais do material, nao geres essa questao.\n");
        prompt.append("- Nunca inventes livro, pagina, capitulo ou detalhe fora do material.\n");
    }

    private void appendContratoGrafico(StringBuilder prompt, String disciplina) {
        if (disciplinaSuportaGrafico(disciplina)) {
            prompt.append("- Nesta fase do projeto, podes gerar questoes com grafico apenas para MATEMATICA e FISICA.\n");
            prompt.append("- Quando a leitura da questao melhorar com grafico, preenche o objeto grafico com usar=true.\n");
            prompt.append("- Quando nao precisares de grafico, devolve grafico.usar=false e grafico.tipoCurva='NENHUM'.\n");
            prompt.append("- Usa apenas os tipos de curva RETA ou PARABOLA.\n");
            prompt.append("- Para RETA, usa a formula y = a*x + b.\n");
            prompt.append("- Para PARABOLA, usa a formula y = a*x^2 + b*x + c.\n");
            prompt.append("- Faz o grafico bater exatamente com o enunciado e com a resposta correta.\n");
            prompt.append("- Se a questao depender de energia, vetores, setas, diagrama de forcas ou muitos elementos visuais, nao uses grafico nesta versao.\n");
            prompt.append("- Em Fisica, prefere graficos simples de tempo-posicao, tempo-velocidade, forca-aceleracao ou trajetoria parabolica.\n");
            prompt.append("- Em Matematica, prefere reta, funcao afim e funcao quadratica.\n");
            prompt.append("- Quando grafico.usar=true, preenche obrigatoriamente: tipoCurva, a, b, c, eixoX, eixoY, xMin, xMax e xTickUnit.\n");
            prompt.append("- Usa janelas simples e estaveis como xMin/xMax pequenos e xTickUnit positivo.\n");
        } else {
            prompt.append("- Para disciplinas fora de MATEMATICA e FISICA, devolve sempre grafico.usar=false e grafico.tipoCurva='NENHUM'.\n");
        }
    }

    private boolean disciplinaSuportaGrafico(String disciplina) {
        String normalizada = TextoUtil.normalizarMaiusculo(disciplina);
        return "MATEMATICA".equals(normalizada) || "FISICA".equals(normalizada);
    }

    private GeracaoSimuladoRequest expandirRequestParaBaseDeQuestoes(GeracaoSimuladoRequest request) {
        if (request.quantidadeQuestoes() >= MINIMO_QUESTOES_BASE_TOPICOS) {
            return request;
        }
        return new GeracaoSimuladoRequest(
            request.disciplina(),
            request.idioma(),
            MINIMO_QUESTOES_BASE_TOPICOS,
            request.nivel(),
            request.instrucoesExtras()
        );
    }

    private String montarPromptExtracaoTopicos(ExtracaoTopicosRequest request) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Analisa apenas os PDFs anexados.\n");
        prompt.append("Extrai somente os topicos e subtopicos realmente abordados no material.\n");
        prompt.append("Disciplina alvo: ").append(request.disciplina()).append(".\n");
        prompt.append("Idioma de resposta: ").append(request.idioma()).append(".\n");
        prompt.append("Regras obrigatorias:\n");
        prompt.append("- Mantem o nome oficial da disciplina exatamente como recebido, incluindo acentos.\n");
        prompt.append("- Nao geres perguntas, exercicios ou respostas.\n");
        prompt.append("- Nao inventes topicos que nao estejam sustentados pelos documentos.\n");
        prompt.append("- Junta topicos repetidos e organiza os subtopicos sem duplicacao.\n");
        prompt.append("- Para cada topico e subtopico, indica obrigatoriamente as paginas (pagina_inicio e pagina_fim) onde o conteudo aparece no PDF.\n");
        prompt.append("- pagina_inicio e pagina_fim sao numeros inteiros baseados na numeracao original do PDF.\n");
        prompt.append("- Os intervalos de paginas nao podem sobrepor-se entre subtopicos do mesmo topico.\n");
        prompt.append("- O campo fonteResumo deve resumir em poucas linhas o escopo dos livros.\n");
        prompt.append("- O campo observacoes deve citar lacunas, ambiguidades ou mistura de areas, se existirem.\n");
        prompt.append("- Responde estritamente no JSON definido pelo schema, sem markdown.\n");

        if (request.instrucoesExtras() != null && !request.instrucoesExtras().isBlank()) {
            prompt.append("Instrucoes extra:\n");
            prompt.append(request.instrucoesExtras().trim()).append('\n');
        }

        return prompt.toString();
    }

    private UploadedPdf aguardarArquivoAtivo(UploadedPdf uploadedPdf) throws IOException, InterruptedException {
        if (uploadedPdf.name() == null || uploadedPdf.name().isBlank()) {
            return uploadedPdf;
        }

        String estadoAtual = normalizeFileState(uploadedPdf.state());
        if (!"PROCESSING".equals(estadoAtual)) {
            LOGGER.info("Ficheiro " + uploadedPdf.name() + " pronto com estado " + estadoAtual + ".");
            return uploadedPdf;
        }

        LOGGER.info("A aguardar que o Gemini processe o ficheiro " + uploadedPdf.name() + ".");
        long deadlineNanos = System.nanoTime() + FILE_PROCESSING_TIMEOUT.toNanos();
        UploadedPdf atual = uploadedPdf;
        while (System.nanoTime() < deadlineNanos) {
            Thread.sleep(FILE_PROCESSING_POLL_INTERVAL.toMillis());
            atual = consultarArquivo(atual);
            estadoAtual = normalizeFileState(atual.state());

            if (estadoAtual == null || estadoAtual.isBlank() || "ACTIVE".equals(estadoAtual)) {
                LOGGER.info("Ficheiro " + atual.name() + " ficou ativo no Gemini.");
                return atual;
            }
            if ("FAILED".equals(estadoAtual) || "ERROR".equals(estadoAtual)) {
                LOGGER.severe("O Gemini falhou ao processar o ficheiro " + atual.name() + ".");
                throw new IOException("O Gemini falhou ao processar o PDF enviado: " + atual.name());
            }
        }

        LOGGER.warning("O Gemini demorou demasiado tempo a processar o ficheiro " + uploadedPdf.name() + ".");
        throw new IOException("O Gemini demorou demasiado tempo a processar o PDF enviado: " + uploadedPdf.name());
    }

    private UploadedPdf consultarArquivo(UploadedPdf uploadedPdf) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_BASE_URL + "/" + uploadedPdf.name()))
            .timeout(Duration.ofSeconds(30))
            .header("x-goog-api-key", apiKey)
            .GET()
            .build();

        HttpResponse<String> response = httpClient.send(
            request,
            HttpResponse.BodyHandlers.ofString()
        );
        ensureSuccess("consultar o estado do ficheiro", response.statusCode(), response.body());

        return new UploadedPdf(
            firstNonBlank(extractFirstJsonStringValue(response.body(), "uri"), uploadedPdf.uri()),
            firstNonBlank(extractFirstJsonStringValue(response.body(), "mimeType"), uploadedPdf.mimeType()),
            firstNonBlank(extractFirstJsonStringValue(response.body(), "name"), uploadedPdf.name()),
            firstNonBlank(extractFirstJsonStringValue(response.body(), "state"), uploadedPdf.state())
        );
    }

    private List<String> resolveModelAttempts() {
        LinkedHashSet<String> models = new LinkedHashSet<>();
        models.add(defaultModel);
        models.add(DEFAULT_MODEL);
        return List.copyOf(models);
    }

    private boolean shouldTryModelFallback(String model, IOException error) {
        if (DEFAULT_MODEL.equalsIgnoreCase(model)) {
            return false;
        }

        String message = error.getMessage();
        if (message == null) {
            return false;
        }

        String normalized = message.toLowerCase(Locale.ROOT);
        return normalized.contains("http 429")
            || normalized.contains("http 503")
            || normalized.contains("quota")
            || normalized.contains("free tier")
            || normalized.contains("high demand")
            || normalized.contains("temporar")
            || normalized.contains("model")
            || normalized.contains("not found")
            || normalized.contains("unsupported");
    }

    private boolean isRetryableGenerateContentError(IOException error) {
        String message = error == null ? null : error.getMessage();
        if (message == null || message.isBlank()) {
            return false;
        }

        String normalized = message.toLowerCase(Locale.ROOT);
        return normalized.contains("http 500")
            || normalized.contains("http 502")
            || normalized.contains("http 503")
            || normalized.contains("http 504")
            || normalized.contains("high demand")
            || normalized.contains("temporar")
            || normalized.contains("service unavailable");
    }

    private String normalizeFileState(String state) {
        if (state == null || state.isBlank()) {
            return null;
        }
        return state.trim().toUpperCase(Locale.ROOT);
    }

    private static String firstNonBlank(String... values) {
        if (values == null) {
            return null;
        }
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }
        return null;
    }

    private static String requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " nao pode estar vazio.");
        }
        return value.trim();
    }

    private static String resumir(String body) {
        if (body == null) {
            return "";
        }

        String sanitized = body.replaceAll("\\s+", " ").trim();
        if (sanitized.length() <= 400) {
            return sanitized;
        }
        return sanitized.substring(0, 400) + "...";
    }

    private static String escapeJson(String value) {
        StringBuilder out = new StringBuilder(value.length() + 16);
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            switch (ch) {
                case '\\' -> out.append("\\\\");
                case '"' -> out.append("\\\"");
                case '\b' -> out.append("\\b");
                case '\f' -> out.append("\\f");
                case '\n' -> out.append("\\n");
                case '\r' -> out.append("\\r");
                case '\t' -> out.append("\\t");
                default -> {
                    if (ch < 0x20) {
                        out.append(String.format("\\u%04x", (int) ch));
                    } else {
                        out.append(ch);
                    }
                }
            }
        }
        return out.toString();
    }

    private List<String> extractJsonStringValues(String json, String fieldName) {
        ArrayList<String> values = new ArrayList<>();
        if (json == null || json.isBlank() || fieldName == null || fieldName.isBlank()) {
            return values;
        }

        int index = 0;
        while (index < json.length()) {
            if (json.charAt(index) != '"') {
                index++;
                continue;
            }

            ParsedJsonString key = parseJsonString(json, index);
            if (key == null) {
                break;
            }

            int cursor = skipWhitespace(json, key.nextIndex());
            if (cursor >= json.length() || json.charAt(cursor) != ':') {
                index = key.nextIndex();
                continue;
            }

            cursor = skipWhitespace(json, cursor + 1);
            if (!fieldName.equals(key.value()) || cursor >= json.length() || json.charAt(cursor) != '"') {
                index = cursor;
                continue;
            }

            ParsedJsonString value = parseJsonString(json, cursor);
            if (value == null) {
                break;
            }

            values.add(value.value());
            index = value.nextIndex();
        }

        return values;
    }

    private ParsedJsonString parseJsonString(String json, int startIndex) {
        if (json == null || startIndex < 0 || startIndex >= json.length() || json.charAt(startIndex) != '"') {
            return null;
        }

        StringBuilder out = new StringBuilder();
        for (int i = startIndex + 1; i < json.length(); i++) {
            char ch = json.charAt(i);
            if (ch == '"') {
                return new ParsedJsonString(out.toString(), i + 1);
            }

            if (ch != '\\') {
                out.append(ch);
                continue;
            }

            if (i + 1 >= json.length()) {
                out.append('\\');
                return new ParsedJsonString(out.toString(), json.length());
            }

            char next = json.charAt(++i);
            switch (next) {
                case '"' -> out.append('"');
                case '\\' -> out.append('\\');
                case '/' -> out.append('/');
                case 'b' -> out.append('\b');
                case 'f' -> out.append('\f');
                case 'n' -> out.append('\n');
                case 'r' -> out.append('\r');
                case 't' -> out.append('\t');
                case 'u' -> {
                    if (i + 4 >= json.length()) {
                        out.append("\\u");
                        break;
                    }
                    String hex = json.substring(i + 1, i + 5);
                    try {
                        out.append((char) Integer.parseInt(hex, 16));
                        i += 4;
                    } catch (NumberFormatException ignored) {
                        out.append("\\u").append(hex);
                        i += 4;
                    }
                }
                default -> out.append(next);
            }
        }

        return new ParsedJsonString(out.toString(), json.length());
    }

    private int skipWhitespace(String json, int index) {
        int cursor = index;
        while (cursor < json.length() && Character.isWhitespace(json.charAt(cursor))) {
            cursor++;
        }
        return cursor;
    }



    private static final String TOPICOS_JSON_SCHEMA = """
        {
          "type": "object",
          "properties": {
            "disciplina": { "type": "string" },
            "idioma": { "type": "string" },
            "fonteResumo": { "type": "string" },
            "topicos": {
              "type": "array",
              "items": {
                "type": "object",
                "properties": {
                  "nome": { "type": "string" },
                  "pagina_inicio": { "type": "integer" },
                  "pagina_fim": { "type": "integer" },
                  "subtopicos": {
                    "type": "array",
                    "items": {
                      "type": "object",
                      "properties": {
                        "nome": { "type": "string" },
                        "pagina_inicio": { "type": "integer" },
                        "pagina_fim": { "type": "integer" }
                      },
                      "required": ["nome", "pagina_inicio", "pagina_fim"]
                    }
                  }
                },
                "required": ["nome", "subtopicos"]
              }
            },
            "observacoes": { "type": "string" }
          },
          "required": ["disciplina", "idioma", "fonteResumo", "topicos", "observacoes"]
        }
        """;

    private static final String SIMULADO_JSON_SCHEMA = """
        {
          "type": "object",
          "properties": {
            "titulo": { "type": "string" },
            "disciplina": { "type": "string" },
            "idioma": { "type": "string" },
            "fonteResumo": { "type": "string" },
            "questoes": {
              "type": "array",
              "items": {
                "type": "object",
                "properties": {
                  "numero": { "type": "integer" },
                  "enunciado": { "type": "string" },
                  "topicoPrincipal": { "type": "string" },
                  "topico": { "type": "string" },
                  "subtopico": { "type": "string" },
                  "dificuldade": { "type": "string" },
                  "rigor": { "type": "number" },
                  "referenciaLivro": { "type": "string" },
                  "paginaInicio": { "type": "integer" },
                  "paginaFim": { "type": "integer" },
                  "exercicio": { "type": "string" },
                  "alternativas": {
                    "type": "array",
                    "items": { "type": "string" },
                    "minItems": 4,
                    "maxItems": 4
                  },
                  "pesosAlternativas": {
                    "type": "array",
                    "items": { "type": "number" },
                    "minItems": 4,
                    "maxItems": 4
                  },
                  "grafico": {
                    "type": "object",
                    "properties": {
                      "usar": { "type": "boolean" },
                      "tipoCurva": { "type": "string" },
                      "a": { "type": "number" },
                      "b": { "type": "number" },
                      "c": { "type": "number" },
                      "eixoX": { "type": "string" },
                      "eixoY": { "type": "string" },
                      "xMin": { "type": "number" },
                      "xMax": { "type": "number" },
                      "xTickUnit": { "type": "number" }
                    },
                    "required": [
                      "usar",
                      "tipoCurva",
                      "a",
                      "b",
                      "c",
                      "eixoX",
                      "eixoY",
                      "xMin",
                      "xMax",
                      "xTickUnit"
                    ]
                  },
                  "respostaCorreta": { "type": "string" },
                  "explicacao": { "type": "string" }
                },
                "required": [
                  "numero",
                  "enunciado",
                  "topicoPrincipal",
                  "topico",
                  "subtopico",
                  "dificuldade",
                  "rigor",
                  "referenciaLivro",
                  "paginaInicio",
                  "paginaFim",
                  "exercicio",
                  "alternativas",
                  "pesosAlternativas",
                  "grafico",
                  "respostaCorreta",
                  "explicacao"
                ]
              }
            }
          },
          "required": ["titulo", "disciplina", "idioma", "fonteResumo", "questoes"]
        }
        """;
}
