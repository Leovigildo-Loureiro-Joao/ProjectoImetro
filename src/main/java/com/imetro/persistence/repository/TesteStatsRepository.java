package com.imetro.persistence.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.imetro.domain.dto.stats.Teste_Stat;

public class TesteStatsRepository extends JdbcBasicSqlRepository {

    public TesteStatsRepository() {
        super("stats", "id");
    }

    public int insert(Teste_Stat teste_Stat) throws SQLException  {
        try (Connection conn = JdbcBasicSqlRepository.openRequiredConnection()) {
            return insert(conn, teste_Stat);
        }
    }

    public int insert(Connection conn, Teste_Stat testeStat) throws SQLException {
        if (conn == null) {
            throw new IllegalArgumentException("conn must not be null");
        }
        if (testeStat == null) {
            throw new IllegalArgumentException("testeStat must not be null");
        }

        String sql = """
            insert into stats (
              id,
              teste_id,
              diagnostico_id,
              candidato_id,
              disciplina_id,
              disciplina_nome,
              origem,
              tempo_total_segundos,
              tempo_medio_segundos,
              total_questoes,
              total_acertos,
              total_erros,
              percentual_acerto,
              velocidade,
              precisao,
              consistencia,
              logica,
              resiliencia,
              erros_comuns,
              melhorias,
              observacoes,
              criado_em,
              atualizado_em
            ) values (
              ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, cast(? as jsonb), cast(? as jsonb), ?, ?, ?
            )
            """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, testeStat.id() == null ? UUID.randomUUID() : testeStat.id());
            stmt.setObject(2, testeStat.teste_id());
            stmt.setObject(3, testeStat.diagnostico_id());
            stmt.setObject(4, testeStat.candidato_id());
            stmt.setObject(5, testeStat.disciplina_id());
            stmt.setString(6, testeStat.disciplina_nome());
            stmt.setString(7, normalizarTexto(testeStat.origem(), "TESTE"));
            stmt.setInt(8, Math.max(0, valorOuZero(testeStat.tempo_total_segundos())));
            stmt.setObject(9, testeStat.tempo_medio_segundos());
            stmt.setInt(10, Math.max(0, valorOuZero(testeStat.total_questoes())));
            stmt.setInt(11, Math.max(0, valorOuZero(testeStat.total_acertos())));
            stmt.setInt(12, Math.max(0, valorOuZero(testeStat.total_erros())));
            stmt.setObject(13, testeStat.percentual_acerto());
            stmt.setObject(14, testeStat.velocidade());
            stmt.setObject(15, testeStat.precisao());
            stmt.setObject(16, testeStat.consistencia());
            stmt.setObject(17, testeStat.logica());
            stmt.setObject(18, testeStat.resiliencia());
            stmt.setString(19, normalizarJsonArray(testeStat.erros_comuns()));
            stmt.setString(20, normalizarJsonArray(testeStat.melhorias()));
            stmt.setString(21, testeStat.observacoes());
            stmt.setTimestamp(22, toTimestamp(testeStat.criado_em()));
            stmt.setTimestamp(23, toTimestamp(testeStat.atualizado_em()));
            return stmt.executeUpdate();
        }
    }

    public Optional<Map<String, Object>> findByTesteId(UUID testeId) throws SQLException {
        String sql = """
            select *
            from stats
            where teste_id = ?
            limit 1
            """;

        List<Map<String, Object>> rows = executeQueryList(sql, testeId);
        if (rows.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(rows.getFirst());
    }

    public List<Map<String, Object>> findByCandidatoId(UUID candidatoId) throws SQLException {
        String sql = """
            select *
            from stats
            where candidato_id = ?
            order by criado_em desc
            """;
        return executeQueryList(sql, candidatoId);
    }

    public List<Map<String, Object>> findByDisciplinaId(UUID disciplinaId) throws SQLException {
        String sql = """
            select *
            from stats
            where disciplina_id = ?
            order by criado_em desc
            """;
        return executeQueryList(sql, disciplinaId);
    }

    private List<Map<String, Object>> executeQueryList(String sql, Object value) throws SQLException {
        try (var conn = JdbcBasicSqlRepository.openRequiredConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, value);
            try (ResultSet rs = stmt.executeQuery()) {
                return readRows(rs);
            }
        }
    }

    public List<Teste_Stat> findAllDto() throws Exception{
        return findAll().stream().map(Teste_Stat::ParseDto).toList();
    }

    private List<Map<String, Object>> readRows(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int totalColunas = meta.getColumnCount();
        List<Map<String, Object>> rows = new ArrayList<>();

        while (rs.next()) {
            Map<String, Object> row = new LinkedHashMap<>();
            for (int i = 1; i <= totalColunas; i++) {
                row.put(meta.getColumnLabel(i), rs.getObject(i));
            }
            rows.add(row);
        }

        return rows;
    }

    private String normalizarJsonArray(String json) {
        return json == null || json.isBlank() ? "[]" : json;
    }

    private String normalizarTexto(String valor, String fallback) {
        return valor == null || valor.isBlank() ? fallback : valor;
    }

    private int valorOuZero(Integer valor) {
        return valor == null ? 0 : valor;
    }

    private Timestamp toTimestamp(LocalDateTime valor) {
        return valor == null ? Timestamp.valueOf(LocalDateTime.now()) : Timestamp.valueOf(valor);
    }
}
