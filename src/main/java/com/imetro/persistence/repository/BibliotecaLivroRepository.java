package com.imetro.persistence.repository;

import com.imetro.domain.dto.biblioteca.BibliotecaLivroDto;
import com.imetro.domain.dto.biblioteca.BibliotecaLivroPaginaDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class BibliotecaLivroRepository extends JdbcBasicSqlRepository {

    private static final String LIVRO_QUERY = """
        select
          l.id,
          l.disciplina_id,
          d.nome as disciplina_nome,
          l.titulo,
          l.nome_arquivo,
          l.mime_type,
          l.tamanho_bytes,
          l.checksum_sha256,
          l.source_path,
          l.ativo,
          l.criado_em,
          l.atualizado_em,
          l.capa_thumbnail,
          coalesce(pg.total_paginas, 0) as total_paginas,
          coalesce(pg.paginas_com_texto, 0) as paginas_com_texto
        from biblioteca_livros l
        join disciplinas d on d.id = l.disciplina_id
        left join (
          select
            livro_id,
            count(*) as total_paginas,
            count(*) filter (where coalesce(btrim(texto_pagina), '') <> '') as paginas_com_texto
          from biblioteca_livro_paginas
          group by livro_id
        ) pg on pg.livro_id = l.id
        """;

    public BibliotecaLivroRepository() {
        super("biblioteca_livros", "id");
    }

    public Optional<BibliotecaLivroDto> findById(UUID livroId) throws SQLException {
        try (Connection conn = openRequiredConnection()) {
            return findById(conn, livroId);
        }
    }

    public Optional<BibliotecaLivroDto> findById(Connection conn, UUID livroId) throws SQLException {
        return queryLivro(conn, LIVRO_QUERY + " where l.id = ?", livroId);
    }

    public Optional<BibliotecaLivroDto> findByDisciplinaAndChecksum(UUID disciplinaId, String checksumSha256)
        throws SQLException {
        try (Connection conn = openRequiredConnection()) {
            return findByDisciplinaAndChecksum(conn, disciplinaId, checksumSha256);
        }
    }

    public Optional<BibliotecaLivroDto> findByDisciplinaAndChecksum(
        Connection conn,
        UUID disciplinaId,
        String checksumSha256
    ) throws SQLException {
        return queryLivro(
            conn,
            LIVRO_QUERY + " where l.disciplina_id = ? and l.checksum_sha256 = ?",
            disciplinaId,
            checksumSha256
        );
    }

    public List<BibliotecaLivroDto> listarPorDisciplina(UUID disciplinaId) throws SQLException {
        try (Connection conn = openRequiredConnection()) {
            return listarPorDisciplina(conn, disciplinaId);
        }
    }

    public List<BibliotecaLivroDto> listarPorDisciplina(Connection conn, UUID disciplinaId) throws SQLException {
        String sql = LIVRO_QUERY + " where l.disciplina_id = ? order by l.atualizado_em desc, l.criado_em desc, l.titulo asc, l.nome_arquivo asc";
        try (PreparedStatement stmt = preparar(conn, sql, disciplinaId);
             ResultSet rs = stmt.executeQuery()) {
            ArrayList<BibliotecaLivroDto> livros = new ArrayList<>();
            while (rs.next()) {
                livros.add(mapLivro(rs));
            }
            return List.copyOf(livros);
        }
    }

    public List<BibliotecaLivroPaginaDto> listarPaginas(UUID livroId) throws SQLException {
        try (Connection conn = openRequiredConnection()) {
            return listarPaginas(conn, livroId);
        }
    }

    public List<BibliotecaLivroPaginaDto> listarPaginas(Connection conn, UUID livroId) throws SQLException {
        String sql = """
            select id, livro_id, pagina_numero, texto_pagina, criado_em
            from biblioteca_livro_paginas
            where livro_id = ?
            order by pagina_numero asc
            """;
        try (PreparedStatement stmt = preparar(conn, sql, livroId);
             ResultSet rs = stmt.executeQuery()) {
            ArrayList<BibliotecaLivroPaginaDto> paginas = new ArrayList<>();
            while (rs.next()) {
                paginas.add(mapPagina(rs));
            }
            return List.copyOf(paginas);
        }
    }

    public Optional<byte[]> carregarPdfBytes(UUID livroId) throws SQLException {
        try (Connection conn = openRequiredConnection()) {
            return carregarPdfBytes(conn, livroId);
        }
    }

    public Optional<byte[]> carregarPdfBytes(Connection conn, UUID livroId) throws SQLException {
        String sql = "select conteudo_pdf from biblioteca_livros where id = ?";
        try (PreparedStatement stmt = preparar(conn, sql, livroId);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                byte[] bytes = rs.getBytes("conteudo_pdf");
                return Optional.ofNullable(bytes);
            }
            return Optional.empty();
        }
    }

    public UUID upsertLivro(
        Connection conn,
        UUID disciplinaId,
        String titulo,
        String nomeArquivo,
        String mimeType,
        long tamanhoBytes,
        String checksumSha256,
        String sourcePath,
        byte[] conteudoPdf,
        byte[] capaThumbnail
    ) {
        String sql = """
            insert into biblioteca_livros (
              disciplina_id,
              titulo,
              nome_arquivo,
              mime_type,
              tamanho_bytes,
              checksum_sha256,
              source_path,
              conteudo_pdf,
              ativo,
              criado_em,
              atualizado_em,
              capa_thumbnail
            ) values (?, ?, ?, ?, ?, ?, ?, ?, true, now(), now(),?)
            on conflict (disciplina_id, checksum_sha256) do update set
              titulo = excluded.titulo,
              nome_arquivo = excluded.nome_arquivo,
              mime_type = excluded.mime_type,
              tamanho_bytes = excluded.tamanho_bytes,
              checksum_sha256 = excluded.checksum_sha256,
              source_path = excluded.source_path,
              conteudo_pdf = excluded.conteudo_pdf,
              ativo = true,
              atualizado_em = now()
              capa_thumbnail = excluded.capa_thumbnail,  
            returning id
            """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, disciplinaId);
            stmt.setString(2, titulo);
            stmt.setString(3, nomeArquivo);
            stmt.setString(4, mimeType);
            stmt.setLong(5, tamanhoBytes);
            stmt.setString(6, checksumSha256);
            stmt.setString(7, sourcePath);
            stmt.setBytes(8, conteudoPdf);
            stmt.setBytes(9, capaThumbnail);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("Nao foi possivel guardar o livro na biblioteca.");
                }
                Object value = rs.getObject("id");
                if (value instanceof UUID uuid) {
                    return uuid;
                }
                return UUID.fromString(String.valueOf(value));
            }
             catch (Exception e) {
             e.printStackTrace();
            }
        }  catch (Exception e) {
             e.printStackTrace();
            }
 
        return null;   }

    public int substituirPaginas(Connection conn, UUID livroId, List<BibliotecaLivroPaginaDto> paginas)
        throws SQLException {
        if (livroId == null) {
            throw new IllegalArgumentException("livroId nao pode ser nulo.");
        }

        try (PreparedStatement delete = conn.prepareStatement("delete from biblioteca_livro_paginas where livro_id = ?")) {
            delete.setObject(1, livroId);
            delete.executeUpdate();
        }

        if (paginas == null || paginas.isEmpty()) {
            return 0;
        }

        String insertSql = """
            insert into biblioteca_livro_paginas (
              livro_id,
              pagina_numero,
              texto_pagina,
              criado_em
            ) values (?, ?, ?, now())
            """;
        try (PreparedStatement insert = conn.prepareStatement(insertSql)) {
            int total = 0;
            for (BibliotecaLivroPaginaDto pagina : paginas) {
                if (pagina == null) {
                    continue;
                }
                insert.setObject(1, livroId);
                insert.setInt(2, pagina.paginaNumero());
                insert.setString(3, pagina.textoPagina() == null ? "" : pagina.textoPagina());
                insert.addBatch();
                total++;
            }

            if (total == 0) {
                return 0;
            }

            insert.executeBatch();
            return total;
        }
    }

    private Optional<BibliotecaLivroDto> queryLivro(Connection conn, String sql, Object... params)
        throws SQLException {
        try (PreparedStatement stmt = preparar(conn, sql, params);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return Optional.of(mapLivro(rs));
            }
            return Optional.empty();
        }
    }

    private PreparedStatement preparar(Connection conn, String sql, Object... params) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
        return stmt;
    }

    private BibliotecaLivroDto mapLivro(ResultSet rs) throws SQLException {
        return new BibliotecaLivroDto(
            toUuid(rs.getObject("id")),
            toUuid(rs.getObject("disciplina_id")),
            rs.getString("disciplina_nome"),
            rs.getString("titulo"),
            rs.getString("nome_arquivo"),
            rs.getString("mime_type"),
            rs.getLong("tamanho_bytes"),
            rs.getBytes("capa_thumbnail"),
            rs.getString("checksum_sha256"),
            rs.getString("source_path"),
            rs.getBoolean("ativo"),
            toInstant(rs.getTimestamp("criado_em")),
            toInstant(rs.getTimestamp("atualizado_em")),
            rs.getInt("total_paginas"),
            rs.getInt("paginas_com_texto")
        );
    }

    private BibliotecaLivroPaginaDto mapPagina(ResultSet rs) throws SQLException {
        return new BibliotecaLivroPaginaDto(
            toUuid(rs.getObject("id")),
            toUuid(rs.getObject("livro_id")),
            rs.getInt("pagina_numero"),
            rs.getString("texto_pagina"),
            toInstant(rs.getTimestamp("criado_em"))
        );
    }

    private UUID toUuid(Object value) {
        if (value instanceof UUID uuid) {
            return uuid;
        }
        if (value == null) {
            return null;
        }
        return UUID.fromString(value.toString());
    }

    private java.time.Instant toInstant(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toInstant();
    }
}
