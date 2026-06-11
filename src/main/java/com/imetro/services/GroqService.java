package com.imetro.services;

import com.imetro.config.Env;
import com.imetro.config.RuntimeConfig;
import com.imetro.domain.dto.gemini.GeracaoSimuladoRequest;
import com.imetro.domain.dto.gemini.ParsedJsonString;
import com.imetro.util.AppLogger;
import com.imetro.util.TextoUtil;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GroqService {

    private static final String API_BASE_URL = "https://api.groq.com/openai/v1";
    private static final URI CHAT_COMPLETIONS_URI = URI.create(API_BASE_URL + "/chat/completions");
    private static final String DEFAULT_MODEL = "openai/gpt-oss-120b";
    private static final String FALLBACK_MODEL = "openai/gpt-oss-20b";
    private static final Duration REQUEST_TIMEOUT = Duration.ofMinutes(5);
    private static final Duration RETRY_BASE_DELAY = Duration.ofSeconds(2);
    private static final int MAX_ATTEMPTS = 3;
    public static final int DEFAULT_SIMULADO_QUESTOES = 48;
    private static final int MINIMO_QUESTOES_BASE_TOPICOS = 72;
    private static final long MAX_DOCUMENT_BYTES = 50L * 1024L * 1024L;
    private static final int MAX_DOCUMENTO_CHARS = 90_000;
    private static final int MAX_PROMPT_CHARS = 180_000;
    private static final Logger LOGGER = AppLogger.getLogger(GroqService.class);

    private final HttpClient httpClient;
    private final String apiKey;
    private final String defaultModel;

    public GroqService() {
        this(
            HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .version(HttpClient.Version.HTTP_1_1)
                .build(),
            firstNonBlank(Env.get("GROQ_API_KEY")),
            firstNonBlank(Env.get("GROQ_MODEL"), DEFAULT_MODEL)
        );
    }

    GroqService(HttpClient httpClient, String apiKey, String defaultModel) {
        this.httpClient = Objects.requireNonNull(httpClient, "httpClient");
        this.apiKey = apiKey == null ? null : apiKey.trim();
        this.defaultModel = firstNonBlank(defaultModel, DEFAULT_MODEL);
    }

    public boolean isConfigured() {
        return apiKey != null && !apiKey.isBlank() && !RuntimeConfig.isBlockedIA();
    }

    public String getDefaultModel() {
        return defaultModel;
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
        List<DocumentoExtraido> documentos = extrairDocumentos(pdfPaths);
        String prompt = montarPromptSimulado(requestFinal);
        String userPrompt = construirPromptComDocumentos(prompt, documentos);

        LOGGER.info(
            "A gerar perguntas com o Groq para a disciplina " + requestFinal.disciplina()
                + " usando " + documentos.size() + " PDF(s)."
        );

        return chamarGroq(
            userPrompt,
            "simulado",
            SIMULADO_JSON_SCHEMA
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
            "A gerar base de questoes com o Groq a partir do JSON de topicos para a disciplina "
                + requestFinal.disciplina()
                + " com minimo de "
                + requestFinal.quantidadeQuestoes()
                + " questoes."
        );

        String prompt = montarPromptSimuladoPorTopicos(contexto, requestFinal);
        return chamarGroq(prompt, "simulado", SIMULADO_JSON_SCHEMA);
    }

    private List<DocumentoExtraido> extrairDocumentos(List<Path> pdfPaths) throws IOException {
        List<Path> documentos = validarDocumentos(pdfPaths);
        ArrayList<DocumentoExtraido> extraidos = new ArrayList<>();

        for (Path documento : documentos) {
            DocumentoExtraido extraido = extrairDocumento(documento);
            if (extraido.texto().isBlank()) {
                LOGGER.warning("O PDF " + extraido.nome() + " nao tem texto extraivel.");
                continue;
            }
            extraidos.add(extraido);
        }

        if (extraidos.isEmpty()) {
            throw new IOException(
                "Nenhum PDF tem texto extraivel. Se forem digitalizacoes, faz OCR antes de usar o Groq."
            );
        }

        return List.copyOf(extraidos);
    }

    private DocumentoExtraido extrairDocumento(Path pdfPath) throws IOException {
        Path normalizado = pdfPath.toAbsolutePath().normalize();
        long tamanho = Files.size(normalizado);
        if (tamanho <= 0) {
            throw new IllegalArgumentException("O PDF esta vazio: " + normalizado);
        }
        if (tamanho > MAX_DOCUMENT_BYTES) {
            throw new IllegalArgumentException("O PDF excede 50MB e nao pode ser processado: " + normalizado);
        }

        String nome = normalizado.getFileName() == null ? normalizado.toString() : normalizado.getFileName().toString();
        StringBuilder texto = new StringBuilder();
        int paginasTotais = 0;
        int paginasComTexto = 0;
        boolean truncado = false;

        try (PDDocument document = Loader.loadPDF(normalizado.toFile())) {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            paginasTotais = document.getNumberOfPages();

            for (int pagina = 1; pagina <= paginasTotais; pagina++) {
                stripper.setStartPage(pagina);
                stripper.setEndPage(pagina);
                String paginaTexto = stripper.getText(document);
                if (paginaTexto != null) {
                    paginaTexto = paginaTexto.strip();
                }
                if (paginaTexto == null || paginaTexto.isBlank()) {
                    continue;
                }

                paginasComTexto++;
                String bloco = "[Pagina " + pagina + "]\n" + paginaTexto.strip() + "\n";
                if (texto.length() + bloco.length() > MAX_DOCUMENTO_CHARS) {
                    int restante = MAX_DOCUMENTO_CHARS - texto.length();
                    if (restante > 0) {
                        texto.append(bloco, 0, Math.min(restante, bloco.length()));
                    }
                    truncado = true;
                    break;
                }
                texto.append(bloco);
            }
        }

        return new DocumentoExtraido(normalizado, nome, texto.toString(), paginasTotais, paginasComTexto, truncado);
    }

    private List<Path> validarDocumentos(List<Path> pdfPaths) throws IOException {
        if (!isConfigured()) {
            LOGGER.warning("Tentativa de usar o Groq sem API key configurada.");
            throw new IllegalStateException(
                "Groq nao configurado. Define GROQ_API_KEY no ambiente."
            );
        }
        if (pdfPaths == null || pdfPaths.isEmpty()) {
            throw new IllegalArgumentException("Indica pelo menos um PDF para enviar ao Groq.");
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

            documentos.add(normalizado);
        }

        if (documentos.isEmpty()) {
            throw new IllegalArgumentException("Nenhum PDF valido foi informado.");
        }
        return List.copyOf(documentos);
    }

    private String construirPromptComDocumentos(String promptBase, List<DocumentoExtraido> documentos) {
        StringBuilder prompt = new StringBuilder(requireNonBlank(promptBase, "prompt"));
        prompt.append("\n\nContexto extraido dos livros:\n");

        boolean primeiro = true;
        for (DocumentoExtraido documento : documentos) {
            if (documento == null || documento.texto().isBlank()) {
                continue;
            }

            String bloco = formatarDocumento(documento);
            if (!primeiro) {
                prompt.append("\n\n");
            }

            if (prompt.length() + bloco.length() > MAX_PROMPT_CHARS) {
                int restante = MAX_PROMPT_CHARS - prompt.length();
                if (restante > 0) {
                    prompt.append(bloco, 0, Math.min(restante, bloco.length()));
                }
                prompt.append("\n[Contexto truncado por limite de tamanho.]");
                break;
            }

            prompt.append(bloco);
            primeiro = false;
        }

        return prompt.toString();
    }

    private String formatarDocumento(DocumentoExtraido documento) {
        StringBuilder bloco = new StringBuilder();
        bloco.append("[Livro: ").append(documento.nome()).append("]\n");
        bloco.append("[Paginas totais: ").append(documento.paginasTotais()).append("]\n");
        bloco.append("[Paginas com texto: ").append(documento.paginasComTexto()).append("]\n");
        if (documento.truncado()) {
            bloco.append("[Texto truncado: sim]\n");
        }
        bloco.append(documento.texto().strip());
        return bloco.toString();
    }

    private String chamarGroq(String userPrompt, String schemaName, String schemaJson)
        throws IOException, InterruptedException {
        IOException lastError = null;

        for (String model : resolveModelAttempts()) {
            for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
                try {
                    LOGGER.info(
                        "A chamar o Groq no modelo " + model + " (tentativa "
                            + attempt + "/" + MAX_ATTEMPTS + ")."
                    );
                    String requestBody = montarRequestBody(model, userPrompt, schemaName, schemaJson);
                    String responseBody = postChatCompletion(requestBody);
                    String content = extrairConteudoResposta(responseBody);
                    if (content == null || content.isBlank()) {
                        throw new IOException("Groq respondeu sem conteudo util. Corpo: " + resumir(responseBody));
                    }
                    return content.trim();
                } catch (IOException e) {
                    lastError = e;
                    boolean retryable = isRetryableGenerateContentError(e);
                    boolean fallback = shouldTryModelFallback(model, e);
                    boolean hasMoreAttempts = attempt < MAX_ATTEMPTS;

                    LOGGER.log(
                        retryable || fallback ? Level.WARNING : Level.SEVERE,
                        "Falha ao chamar o Groq no modelo " + model + ": " + e.getMessage(),
                        retryable || fallback ? null : e
                    );

                    if (retryable && hasMoreAttempts) {
                        long delayMillis = RETRY_BASE_DELAY.toMillis() * (1L << (attempt - 1));
                        LOGGER.info("Nova tentativa do Groq em " + delayMillis + "ms para o modelo " + model + ".");
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
        throw new IOException("Nao foi possivel chamar o Groq.");
    }

    private String montarRequestBody(String model, String prompt, String schemaName, String schemaJson) {
        StringBuilder body = new StringBuilder();
        body.append("{");
        body.append("\"model\":\"").append(escapeJson(model)).append("\",");
        body.append("\"messages\":[");
        body.append("{\"role\":\"system\",\"content\":\"")
            .append(escapeJson(montarMensagemSistema(schemaJson != null && !schemaJson.isBlank())))
            .append("\"},");
        body.append("{\"role\":\"user\",\"content\":\"").append(escapeJson(prompt)).append("\"}");
        body.append("],");
        body.append("\"temperature\":0.2");

        if (schemaJson != null && !schemaJson.isBlank()) {
            if (suportaStructuredOutputsEstritos(model)) {
                body.append(",\"response_format\":{");
                body.append("\"type\":\"json_schema\",");
                body.append("\"json_schema\":{");
                body.append("\"name\":\"").append(escapeJson(schemaName)).append("\",");
                body.append("\"strict\":true,");
                body.append("\"schema\":").append(schemaJson);
                body.append("}}");
            } else {
                body.append(",\"response_format\":{\"type\":\"json_object\"}");
            }
        }

        body.append("}");
        return body.toString();
    }

    private String montarMensagemSistema(boolean estruturado) {
        if (estruturado) {
            return """
                Responde apenas com JSON valido.
                Segue rigorosamente o schema pedido e nao acrescentes markdown, texto extra ou blocos de codigo.
                Se o schema exigir campos obrigatorios, preenche todos.
                Se um valor nao se aplicar, usa valores neutros validos em vez de omitir o campo.
                """;
        }

        return """
            Responde com base apenas no contexto fornecido.
            Nao inventes factos fora dos livros e respeita a lingua pedida pelo utilizador.
            """;
    }

    private String postChatCompletion(String jsonBody) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(CHAT_COMPLETIONS_URI)
            .timeout(REQUEST_TIMEOUT)
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + apiKey)
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        int statusCode = response.statusCode();
        if (statusCode < 200 || statusCode >= 300) {
            throw new IOException("Falha ao chamar o Groq (HTTP " + statusCode + "): " + resumir(response.body()));
        }

        return response.body();
    }

    private String extrairConteudoResposta(String responseBody) {
        return extractFirstJsonStringValue(responseBody, "content");
    }

    private List<String> resolveModelAttempts() {
        LinkedHashSet<String> models = new LinkedHashSet<>();
        models.add(defaultModel);
        models.add(DEFAULT_MODEL);
        models.add(FALLBACK_MODEL);
        return List.copyOf(models);
    }

    private boolean shouldTryModelFallback(String model, IOException error) {
        if (model == null || error == null) {
            return false;
        }

        String normalized = firstNonBlank(error.getMessage(), "").toLowerCase(Locale.ROOT);
        if (normalized.contains("prompt too long")
            || normalized.contains("context length")
            || normalized.contains("maximum context")
            || normalized.contains("too long")) {
            return false;
        }

        if (normalized.contains("http 429")
            || normalized.contains("http 500")
            || normalized.contains("http 502")
            || normalized.contains("http 503")
            || normalized.contains("http 504")
            || normalized.contains("quota")
            || normalized.contains("rate limit")
            || normalized.contains("temporar")
            || normalized.contains("service unavailable")
            || normalized.contains("timeout")
            || normalized.contains("timed out")
            || normalized.contains("connection")
            || normalized.contains("network")
            || normalized.contains("model")
            || normalized.contains("not found")
            || normalized.contains("unsupported")) {
            return !fallbackModelMatches(model);
        }

        return !fallbackModelMatches(model);
    }

    private boolean fallbackModelMatches(String model) {
        return FALLBACK_MODEL.equalsIgnoreCase(model);
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
            || normalized.contains("service unavailable")
            || normalized.contains("timeout")
            || normalized.contains("timed out")
            || normalized.contains("connection")
            || normalized.contains("network");
    }

    private boolean suportaStructuredOutputsEstritos(String model) {
        if (model == null || model.isBlank()) {
            return false;
        }

        String normalizado = model.trim().toLowerCase(Locale.ROOT);
        return "openai/gpt-oss-120b".equals(normalizado) || "openai/gpt-oss-20b".equals(normalizado);
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

    private String montarPromptSimulado(GeracaoSimuladoRequest request) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Gera um simulado em ").append(request.idioma()).append(".\n");
        prompt.append("Disciplina alvo: ").append(request.disciplina()).append(".\n");
        prompt.append("Quantidade minima de questoes: ").append(request.quantidadeQuestoes()).append(".\n");
        prompt.append("Nivel desejado: ").append(request.nivel()).append(".\n");
        prompt.append("Regras obrigatorias:\n");
        prompt.append("- Usa o nome da disciplina exatamente como foi recebido, incluindo acentos.\n");
        prompt.append("- Cria exatamente 4 alternativas objetivas e apenas uma resposta correta.\n");
        prompt.append("- No campo respostaCorreta, devolve o texto exato da alternativa correta, nunca apenas a letra.\n");
        prompt.append("- No campo pesosAlternativas, devolve 4 numeros entre 0.0 e 1.0 alinhados com as 4 alternativas.\n");
        prompt.append("- A alternativa correta deve ter peso 1.0 e as outras devem ter pesos menores que 1.0.\n");
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
            prompt.append("- Quando nao precisares de grafico, devolve grafico.usar=false, grafico.tipoCurva='NENHUM' e preenche a=0, b=0, c=0, eixoX='', eixoY='', xMin=0, xMax=0 e xTickUnit=0.\n");
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
            prompt.append("- Para disciplinas fora de MATEMATICA e FISICA, devolve sempre grafico.usar=false, grafico.tipoCurva='NENHUM' e preenche a=0, b=0, c=0, eixoX='', eixoY='', xMin=0, xMax=0 e xTickUnit=0.\n");
        }
    }

    private boolean disciplinaSuportaGrafico(String disciplina) {
        String normalizada = TextoUtil.normalizarMaiusculo(disciplina);
        return "MATEMATICA".equals(normalizada) || "FISICA".equals(normalizada);
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
        if (value == null) {
            return "";
        }

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

    private String extractFirstJsonStringValue(String json, String fieldName) {
        List<String> values = extractJsonStringValues(json, fieldName);
        return values.isEmpty() ? null : values.getFirst();
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

    private static final String SIMULADO_JSON_SCHEMA = """
        {
          "type": "object",
          "additionalProperties": false,
          "properties": {
            "titulo": { "type": "string" },
            "disciplina": { "type": "string" },
            "idioma": { "type": "string" },
            "fonteResumo": { "type": "string" },
            "questoes": {
              "type": "array",
              "items": {
                "type": "object",
                "additionalProperties": false,
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
                    "additionalProperties": false,
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

    private record DocumentoExtraido(
        Path arquivo,
        String nome,
        String texto,
        int paginasTotais,
        int paginasComTexto,
        boolean truncado
    ) {
    }
}
