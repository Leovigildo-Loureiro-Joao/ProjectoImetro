package com.imetro.services;

import com.imetro.domain.dto.biblioteca.BibliotecaLivroDto;
import com.imetro.domain.dto.biblioteca.BibliotecaLivroPaginaDto;
import com.imetro.persistence.repository.BibliotecaLivroRepository;
import com.imetro.persistence.repository.JdbcBasicSqlRepository;
import com.imetro.util.AppLogger;
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
        throws IOException {
        if (arquivosOrigem == null || arquivosOrigem.isEmpty()) {
            return List.of();
        }

        ArrayList<BibliotecaLivroDto> livros = new ArrayList<>();
        for (Path arquivo : arquivosOrigem) {
            livros.add(sincronizarArquivo(disciplinaId, arquivo));
        }
        return List.copyOf(livros);
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
                    conteudoPdf
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
                String texto = stripper.getText(document);
                texto = texto == null ? "" : texto.strip();
                paginas.add(new BibliotecaLivroPaginaDto(null, null, pagina, texto, null));
            }
        }
        return List.copyOf(paginas);
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
