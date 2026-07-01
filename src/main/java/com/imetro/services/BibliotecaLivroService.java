package com.imetro.services;

import com.imetro.domain.dto.biblioteca.BibliotecaLivroDto;
import com.imetro.domain.dto.biblioteca.BibliotecaLivroPaginaDto;
import com.imetro.domain.dto.gemini.ExtracaoTopicosRequest;
import com.imetro.domain.dto.perguntas.TopicoSubtopico;
import com.imetro.domain.enums.TopicoExame;
import com.imetro.persistence.repository.BibliotecaLivroRepository;
import com.imetro.persistence.repository.JdbcBasicSqlRepository;
import com.imetro.util.AppLogger;
import com.imetro.util.PdfUtils;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class BibliotecaLivroService {

    private static final Logger LOGGER = AppLogger.getLogger(BibliotecaLivroService.class);
    private static final long MAX_PDF_BYTES = 50L * 1024L * 1024L;

    private final BibliotecaLivroRepository repository;

    public BibliotecaLivroService() {
        this(new BibliotecaLivroRepository());
    }

    BibliotecaLivroService(BibliotecaLivroRepository repository) {
        this.repository = repository;
    }

    public List<BibliotecaLivroDto> sincronizarArquivos(UUID disciplinaId, List<Path> arquivosOrigem)
        {
      try {
          if (arquivosOrigem == null || arquivosOrigem.isEmpty()) {
            return List.of();
        }

        ArrayList<BibliotecaLivroDto> livros = new ArrayList<>();
        for (Path arquivo : arquivosOrigem) {
            livros.add(sincronizarArquivo(disciplinaId, arquivo));
        }
        return List.copyOf(livros);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return List.of();
    }

    public BibliotecaLivroDto sincronizarArquivo(UUID disciplinaId, Path arquivoPdf) throws IOException {
        if (disciplinaId == null) {
            throw new IllegalArgumentException("disciplinaId nao pode ser nulo.");
        }
        if (arquivoPdf == null) {
            throw new IllegalArgumentException("arquivoPdf nao pode ser nulo.");
        }

        Path normalizado = arquivoPdf.toAbsolutePath().normalize();
        if (!Files.exists(normalizado) || !Files.isRegularFile(normalizado)) {
            throw new IOException("PDF nao encontrado: " + normalizado);
        }

        String nomeArquivo = normalizado.getFileName() == null ? normalizado.toString() : normalizado.getFileName().toString();
        if (!nomeArquivo.toLowerCase(Locale.ROOT).endsWith(".pdf")) {
            throw new IllegalArgumentException("Apenas ficheiros PDF sao suportados: " + normalizado);
        }

        long tamanhoBytes = Files.size(normalizado);
        if (tamanhoBytes <= 0) {
            throw new IOException("O PDF esta vazio: " + normalizado);
        }
        if (tamanhoBytes > MAX_PDF_BYTES) {
            throw new IOException("O PDF excede 50MB e nao pode ser guardado na biblioteca: " + normalizado);
        }

        byte[] conteudoPdf = Files.readAllBytes(normalizado);
        byte[] capaThumbnail = PdfUtils.gerarThumbnail(conteudoPdf);
        String checksum = sha256Hex(conteudoPdf);
        String titulo = derivarTitulo(nomeArquivo);

        Optional<BibliotecaLivroDto> existente;
        try {
            existente = repository.findByDisciplinaAndChecksum(disciplinaId, checksum);
        } catch (SQLException e) {
            throw new IOException("Falha ao verificar se o PDF ja existe na biblioteca.", e);
        }

        boolean precisaExtrairPaginas = existente.isEmpty() || existente.get().totalPaginas() <= 0;
        List<BibliotecaLivroPaginaDto> paginas = precisaExtrairPaginas
            ? extrairPaginas(normalizado)
            : List.of();

        try (Connection conn = JdbcBasicSqlRepository.openRequiredConnection()) {
            boolean autoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try {
                UUID livroId = repository.upsertLivro(
                    conn,
                    disciplinaId,
                    titulo,
                    nomeArquivo,
                    "application/pdf",
                    tamanhoBytes,
                    checksum,
                    normalizado.toString(),
                    conteudoPdf,
                    capaThumbnail
                );

                if (precisaExtrairPaginas) {
                    repository.substituirPaginas(conn, livroId, paginas);
                }

                conn.commit();
                BibliotecaLivroDto livro = repository.findById(conn, livroId)
                    .orElseThrow(() -> new IOException("Nao foi possivel recarregar o livro guardado."));
                LOGGER.info(() -> "PDF sincronizado na biblioteca: " + livro.nomeArquivo());
                return livro;
            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackError) {
                    LOGGER.warning("Falha ao desfazer a transacao da biblioteca: " + rollbackError.getMessage());
                }
                if (e instanceof IOException ioException) {
                    throw ioException;
                }
                if (e instanceof SQLException sqlException) {
                    throw new IOException("Falha ao guardar o PDF na biblioteca.", sqlException);
                }
                throw new IOException("Falha ao guardar o PDF na biblioteca.", e);
            } finally {
                conn.setAutoCommit(autoCommit);
            }
        } catch (SQLException e) {
            throw new IOException("Nao foi possivel ligar a base de dados para guardar o PDF.", e);
        }
    }

    public Optional<BibliotecaLivroDto> encontrarLivro(UUID livroId) throws IOException {
        try {
            return repository.findById(livroId);
        } catch (SQLException e) {
            throw new IOException("Falha ao carregar o livro da biblioteca.", e);
        }
    }

    public Optional<BibliotecaLivroDto> encontrarLivroPorNome(UUID disciplinaId, String nomeLivro) throws IOException {
        if (nomeLivro == null || nomeLivro.isBlank()) return Optional.empty();
        try {
            return repository.listarPorDisciplina(disciplinaId).stream()
                .filter(l -> l.titulo() != null && l.titulo().toLowerCase().contains(nomeLivro.toLowerCase()))
                .findFirst();
        } catch (SQLException e) {
            throw new IOException("Falha ao procurar livro por nome.", e);
        }
    }

    public Optional<BibliotecaLivroDto> encontrarLivroPorNome(String nomeLivro) throws IOException {
        if (nomeLivro == null || nomeLivro.isBlank()) return Optional.empty();
        try {
            return repository.listarTodos().stream()
                .filter(l -> l.titulo() != null && l.titulo().toLowerCase().contains(nomeLivro.toLowerCase()))
                .findFirst();
        } catch (SQLException e) {
            throw new IOException("Falha ao procurar livro por nome.", e);
        }
    }

    public Optional<BibliotecaLivroDto> encontrarLivroPorNomeArquivoETamanho(String nomeArquivo, long tamanhoBytes) throws IOException {
        if (nomeArquivo == null || nomeArquivo.isBlank()) return Optional.empty();
        try {
            return repository.findByNomeArquivoETamanho(nomeArquivo, tamanhoBytes);
        } catch (SQLException e) {
            throw new IOException("Falha ao procurar livro por nome de arquivo.", e);
        }
    }

    public void substituirMapaTopicos(UUID livroId, List<TopicoSubtopico> topicos) throws IOException {
        try {
            repository.substituirMapaTopicos(livroId, topicos);
        } catch (SQLException e) {
            throw new IOException("Falha ao guardar o mapa de topicos do livro.", e);
        }
    }

    public List<TopicoSubtopico> extrairTopicosDoLivro(UUID livroId, GeminiService geminiService) throws IOException {
        BibliotecaLivroDto livro = encontrarLivro(livroId)
            .orElseThrow(() -> new IOException("Livro nao encontrado: " + livroId));
        String disciplinaNome = livro.disciplinaNome();
        TopicoExame.Disciplina disciplinaEnum = TopicoExame.resolverDisciplina(disciplinaNome).orElse(null);
        String instrucoesCanonicas = TopicoExame.instrucoesModoInteligente(disciplinaEnum);
        String instrucoesExtras = "Organiza os topicos com foco no conteudo programatico da disciplina " + disciplinaNome + "."
            + (instrucoesCanonicas.isBlank() ? "" : "\n" + instrucoesCanonicas);
        ExtracaoTopicosRequest request = new ExtracaoTopicosRequest(
            disciplinaNome,
            "pt-AO",
            instrucoesExtras
        );
        Path tempPdf = exportarParaArquivoTemporario(livroId)
            .orElseThrow(() -> new IOException("Nao foi possivel extrair o PDF do livro."));
        try {
            String jsonTopicos = geminiService.extrairTopicosJson(List.of(tempPdf), request);
            ArrayList<TopicoSubtopico> topicos = parseTopicos(jsonTopicos);
            if (!topicos.isEmpty()) {
                try {
                    repository.substituirMapaTopicos(livroId, topicos);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return topicos;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("A extracao de topicos foi interrompida.", e);
        } finally {
            Files.deleteIfExists(tempPdf);
        }
    }

    private ArrayList<TopicoSubtopico> parseTopicos(String jsonTopicos) {
        ArrayList<TopicoSubtopico> pares = new ArrayList<>();
        String topicosArray = extrairCampoArrayJson(jsonTopicos, "topicos");
        if (topicosArray == null || topicosArray.isBlank()) return pares;

        LinkedHashSet<String> chaves = new LinkedHashSet<>();
        for (String objetoTopico : extrairObjetosJsonArray(topicosArray)) {
            String nomeTopico = extrairCampoStringJson(objetoTopico, "nome");
            if (nomeTopico == null || nomeTopico.isBlank()) nomeTopico = "Geral";
            nomeTopico = nomeTopico.trim();

            int topicoPagInicio = extrairCampoInteiroJson(objetoTopico, "pagina_inicio", 0);
            int topicoPagFim = extrairCampoInteiroJson(objetoTopico, "pagina_fim", 0);

            String subtopicosArray = extrairCampoArrayJson(objetoTopico, "subtopicos");
            if (subtopicosArray == null || subtopicosArray.isBlank()) continue;

            for (String objetoSubtopico : extrairObjetosJsonArray(subtopicosArray)) {
                String nomeSubtopico = extrairCampoStringJson(objetoSubtopico, "nome");
                if (nomeSubtopico == null || nomeSubtopico.isBlank()) continue;
                nomeSubtopico = nomeSubtopico.trim();

                int pagInicio = extrairCampoInteiroJson(objetoSubtopico, "pagina_inicio", topicoPagInicio);
                int pagFim = extrairCampoInteiroJson(objetoSubtopico, "pagina_fim", topicoPagFim);

                String chave = (nomeTopico + "::" + nomeSubtopico).toLowerCase();
                if (!chaves.add(chave)) continue;
                pares.add(new TopicoSubtopico(nomeTopico, nomeSubtopico, pagInicio, pagFim));
            }
        }
        return pares;
    }

    private String extrairCampoStringJson(String json, String campo) {
        if (json == null || campo == null) return null;
        String procurado = "\"" + campo + "\":\"";
        int inicio = json.indexOf(procurado);
        if (inicio < 0) return null;
        inicio += procurado.length();
        int fim = json.indexOf("\"", inicio);
        return fim < 0 ? null : json.substring(inicio, fim);
    }

    private int extrairCampoInteiroJson(String json, String campo, int padrao) {
        if (json == null || campo == null) return padrao;
        String procurado = "\"" + campo + "\":";
        int inicio = json.indexOf(procurado);
        if (inicio < 0) return padrao;
        inicio += procurado.length();
        StringBuilder num = new StringBuilder();
        for (int i = inicio; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c >= '0' && c <= '9') {
                num.append(c);
            } else if (num.isEmpty()) {
                continue;
            } else {
                break;
            }
        }
        return num.isEmpty() ? padrao : Integer.parseInt(num.toString());
    }

    private String extrairCampoArrayJson(String json, String campo) {
        if (json == null || campo == null) return null;
        String procurado = "\"" + campo + "\":[";
        int inicio = json.indexOf(procurado);
        if (inicio < 0) {
            String procurado2 = "\"" + campo + "\": [";
            inicio = json.indexOf(procurado2);
            if (inicio < 0) return null;
            inicio += procurado2.length();
        } else {
            inicio += procurado.length();
        }
        int fim = localizarFechoJson(json, inicio);
        return fim < 0 ? null : json.substring(inicio - 1, fim + 1);
    }

    private List<String> extrairObjetosJsonArray(String jsonArray) {
        ArrayList<String> objetos = new ArrayList<>();
        if (jsonArray == null || jsonArray.isBlank()) return objetos;
        jsonArray = jsonArray.trim();
        if (!jsonArray.startsWith("[") || !jsonArray.endsWith("]")) return objetos;
        jsonArray = jsonArray.substring(1, jsonArray.length() - 1).trim();
        if (jsonArray.isBlank()) return objetos;

        int i = 0;
        while (i < jsonArray.length()) {
            while (i < jsonArray.length() && Character.isWhitespace(jsonArray.charAt(i)) || jsonArray.charAt(i) == ',') i++;
            if (i >= jsonArray.length()) break;
            if (jsonArray.charAt(i) == '{') {
                int fim = localizarFechoJson(jsonArray, i);
                if (fim < 0) break;
                objetos.add(jsonArray.substring(i, fim + 1));
                i = fim + 1;
            } else {
                i++;
            }
        }
        return objetos;
    }

    private int localizarFechoJson(String json, int inicio) {
        if (inicio < 0 || inicio >= json.length()) return -1;
        char aberto = json.charAt(inicio);
        char fechado;
        if (aberto == '{') fechado = '}';
        else if (aberto == '[') fechado = ']';
        else return -1;

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
            } else {
                if (c == '"') {
                    emString = true;
                } else if (c == aberto) {
                    profundidade++;
                } else if (c == fechado) {
                    profundidade--;
                    if (profundidade == 0) return i;
                }
            }
        }
        return -1;
    }

    public List<BibliotecaLivroDto> listarLivros(UUID disciplinaId) throws IOException {
        try {
            return repository.listarPorDisciplina(disciplinaId);
        } catch (SQLException e) {
            throw new IOException("Falha ao listar os livros da biblioteca.", e);
        }
    }

    

    public List<BibliotecaLivroPaginaDto> listarPaginas(UUID livroId) throws IOException {
        try {
            return repository.listarPaginas(livroId);
        } catch (SQLException e) {
            throw new IOException("Falha ao listar as paginas do livro.", e);
        }
    }

    public Optional<byte[]> carregarPdf(UUID livroId) throws IOException {
        try {
            return repository.carregarPdfBytes(livroId);
        } catch (SQLException e) {
            throw new IOException("Falha ao carregar o PDF da biblioteca.", e);
        }
    }

    public Optional<Path> exportarParaArquivoTemporario(UUID livroId) throws IOException {
        Optional<byte[]> pdfOpt = carregarPdf(livroId);
        if (pdfOpt.isEmpty() || pdfOpt.get().length == 0) {
            return Optional.empty();
        }

        BibliotecaLivroDto livro = encontrarLivro(livroId)
            .orElseThrow(() -> new IOException("O livro nao foi encontrado na biblioteca."));

        String nomeBase = livro.nomeArquivo();
        String extensao = nomeBase.toLowerCase(Locale.ROOT).endsWith(".pdf") ? "" : ".pdf";
        Path tempFile = Files.createTempFile("imetro-biblioteca-", "-" + nomeBase.replaceAll("[^a-zA-Z0-9._-]", "_") + extensao);
        Files.write(tempFile, pdfOpt.get());
        tempFile.toFile().deleteOnExit();
        return Optional.of(tempFile);
    }

    private List<BibliotecaLivroPaginaDto> extrairPaginas(Path arquivoPdf) throws IOException {
        ArrayList<BibliotecaLivroPaginaDto> paginas = new ArrayList<>();
        try (PDDocument document = Loader.loadPDF(arquivoPdf.toFile())) {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            int totalPaginas = document.getNumberOfPages();
            for (int pagina = 1; pagina <= totalPaginas; pagina++) {
                stripper.setStartPage(pagina);
                stripper.setEndPage(pagina);
                String texto = sanitizarTextoPdf(stripper.getText(document));
                if (texto == null) {
                    texto = "";
                }

                // 🚨 REMOVE NULL BYTES E CARACTERES INVÁLIDOS
                texto = texto
                    .replace("\u0000", "")                // CRÍTICO
                    .replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "") // controla chars perigosos
                    .strip();
                paginas.add(new BibliotecaLivroPaginaDto(null, null, pagina, texto, null));
            }
        }
        return List.copyOf(paginas);
    }

    private String sanitizarTextoPdf(String texto) {
        if (texto == null) return "";

        return texto
            .replace("\u0000", "")
            .replaceAll("[\\p{Cc}&&[^\r\n\t]]", "")
            .replaceAll("\\s{3,}", " ")
            .trim();
    }

    private String derivarTitulo(String nomeArquivo) {
        if (nomeArquivo == null || nomeArquivo.isBlank()) {
            return "Livro";
        }

        String titulo = nomeArquivo;
        if (titulo.toLowerCase(Locale.ROOT).endsWith(".pdf")) {
            titulo = titulo.substring(0, titulo.length() - 4);
        }
        titulo = titulo.replace('_', ' ').replace('-', ' ').replaceAll("\\s+", " ").trim();
        return titulo.isBlank() ? nomeArquivo : titulo;
    }

    private String sha256Hex(byte[] conteudo) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(conteudo));
        } catch (NoSuchAlgorithmException e) {
            throw new IOException("Nao foi possivel calcular o checksum do PDF.", e);
        }
    }
}
