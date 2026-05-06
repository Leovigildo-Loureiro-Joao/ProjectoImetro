package com.imetro.persistence.repository;

import static com.imetro.persistence.repository.JdbcBasicSqlRepository.readAllRows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import com.imetro.domain.dto.diagnostico.DiagnosticoDto;
import com.imetro.domain.dto.disciplina.DisciplinaDto;
import com.imetro.domain.enums.NivelDisciplina;
import com.imetro.domain.model.Candidato;
import com.imetro.services.DisciplinaService;
import com.imetro.util.ParseTimeStampLocalDate;

public class DiagnosticoRepository extends JdbcBasicSqlRepository{

    private final DisciplinaService disciplinaService;

    public DiagnosticoRepository() {
        super("diagnosticos", "id");
        disciplinaService=new DisciplinaService();

    }

    public List<DiagnosticoDto> findAllDto(){
        try {
            List<DiagnosticoDto> lista=new ArrayList();
            for (Map<String, Object> link : findAll()) {
                lista.add(DiagnosticoDto.ParseMapDto(link));
            }
            return lista;
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        return null;
    }

    public List<DiagnosticoDto> CandidatoDiagnostico(UUID caUuid){
        String sql="SELECT * FROM diagnosticos where candidato_id =?";
        try (var conn = JdbcBasicSqlRepository.openRequiredConnection();
            var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, caUuid.toString());
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    List<Map<String, Object>> value=JdbcBasicSqlRepository.readAllRows(rs);
                    if (value instanceof  List<Map<String, Object>> list) {
                        List<DiagnosticoDto> lista=new ArrayList();
                        for (Map<String, Object> link : list) {
                            lista.add(DiagnosticoDto.ParseMapDto(link));
                        }
                        return lista;
                    }
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<DisciplinaDto> carregarDisciplinasAtivasDoCandidato(UUID candidatoId) {
        if (candidatoId == null) {
            return List.of();
        }

        String sql = """
            select d.id, d.nome, d.peso, d.nivel, d.objectivo
            from progresso_aluno_disciplina p
            join disciplinas d on d.id = p.disciplina_id
            where p.aluno_id = ?
            order by d.nome
            """;

        ArrayList<DisciplinaDto> disciplinas = new ArrayList<>();
        try (Connection conn = JdbcBasicSqlRepository.openRequiredConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, candidatoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    disciplinas.add(
                        new DisciplinaDto(
                            rs.getObject("id", UUID.class),
                            rs.getString("nome"),
                            rs.getObject("peso") instanceof Number number ? number.floatValue() : 1.0f,
                            NivelDisciplina.fromDescricao(rs.getString("nivel")),
                            rs.getString("objectivo")
                        )
                    );
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar disciplinas ativas do candidato: " + e.getMessage());
        }

        return List.copyOf(disciplinas);
    }


    public UUID inserir(
        Connection conn,
        UUID candidatoId,
        UUID disciplinaId,
        String disciplinaNome,
        LocalDateTime iniciadoEm,
        LocalDateTime concluidoEm,
        int duracaoSegundos,
        int totalQuestoes,
        int totalAcertos,
        int totalErros,
        Double percentualAcerto,
        Double evolucao,
        String nivel,
        double velocidade,
        double precisao,
        double consistencia,
        String respostasJson,
        String observacoes
    ) throws SQLException {
        UUID diagnosticoId = UUID.randomUUID();
        String sql = """
            insert into diagnosticos (
              id,
              candidato_id,
              disciplina_id,
              disciplina_nome,
              iniciado_em,
              concluido_em,
              duracao_segundos,
              total_questoes,
              total_acertos,
              total_erros,
              percentual_acerto,
              evolucao_percentual,
              nivel,
              velocidade,
              precisao,
              consistencia,
              respostas,
              observacoes,
              criado_em,
              atualizado_em
            ) values (
              ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, cast(? as jsonb), ?, now(), now()
            )
            """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, diagnosticoId);
            stmt.setObject(2, candidatoId);
            stmt.setObject(3, disciplinaId);
            stmt.setString(4, disciplinaNome);
            stmt.setTimestamp(5, Timestamp.valueOf(iniciadoEm));
            stmt.setTimestamp(6, Timestamp.valueOf(concluidoEm));
            stmt.setInt(7, duracaoSegundos);
            stmt.setInt(8, totalQuestoes);
            stmt.setInt(9, totalAcertos);
            stmt.setInt(10, totalErros);
            stmt.setObject(11, percentualAcerto);
            stmt.setObject(12, evolucao);
            stmt.setString(13, nivel);
            stmt.setFloat(14, (float) velocidade);
            stmt.setFloat(15, (float) precisao);
            stmt.setFloat(16, (float) consistencia);
            stmt.setString(17, respostasJson);
            stmt.setString(18, observacoes);
            stmt.executeUpdate();
        }
        return diagnosticoId;
    }


    public Double buscarUltimoPercentualDiagnostico(
        Connection conn,
        UUID candidatoId,
        UUID disciplinaId,
        String disciplinaNome
    ) throws SQLException {
        String sql = """
            select percentual_acerto
            from diagnosticos
            where candidato_id = ?
              and (
                (disciplina_id is not distinct from ?)
                or lower(coalesce(disciplina_nome, '')) = lower(coalesce(?, ''))
              )
            order by coalesce(concluido_em, iniciado_em) desc, criado_em desc
            limit 1
            """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, candidatoId);
            stmt.setObject(2, disciplinaId);
            stmt.setString(3, disciplinaNome);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return rs.getObject("percentual_acerto") instanceof Number number
                    ? number.doubleValue()
                    : null;
            }
        }
    }

    public void atualizarProgressoAposDiagnostico(
        Connection conn,
        UUID candidatoId,
        UUID disciplinaId,
        String disciplinaNome,
        int totalQuestoes,
        int totalAcertos,
        int totalErros,
        String nivelAtual,
        LocalDateTime concluidoEm
    ) throws SQLException {
        String selectSql = """
            select id,
              nivel_atual,
              total_questoes_resolvidas,
              total_acertos,
              total_erros,
              peso_atual,
              ultimos_3_diagnosticos_acertos,
              ultimos_3_diagnosticos_total,
              streak_dias_consecutivos
            from progresso_aluno_disciplina
            where aluno_id = ? and disciplina_id = ?
            limit 1
            """;

        UUID progressoId = null;
        String nivelAnterior = null;
        int totalQuestoesResolvidas = 0;
        int acertosAcumulados = 0;
        int errosAcumulados = 0;
        double pesoAtual = 1.0d;
        Integer[] ultimosAcertos = new Integer[0];
        Integer[] ultimosTotais = new Integer[0];
        int streak = 0;

        try (PreparedStatement stmt = conn.prepareStatement(selectSql)) {
            stmt.setObject(1, candidatoId);
            stmt.setObject(2, disciplinaId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    progressoId = rs.getObject("id", UUID.class);
                    nivelAnterior = rs.getString("nivel_atual");
                    totalQuestoesResolvidas = rs.getObject("total_questoes_resolvidas") instanceof Number number
                        ? number.intValue()
                        : 0;
                    acertosAcumulados = rs.getObject("total_acertos") instanceof Number number
                        ? number.intValue()
                        : 0;
                    errosAcumulados = rs.getObject("total_erros") instanceof Number number
                        ? number.intValue()
                        : 0;
                    pesoAtual = rs.getObject("peso_atual") instanceof Number number
                        ? number.doubleValue()
                        : 1.0d;
                    ultimosAcertos = parseIntegerArray(rs.getArray("ultimos_3_diagnosticos_acertos"));
                    ultimosTotais = parseIntegerArray(rs.getArray("ultimos_3_diagnosticos_total"));
                    streak = rs.getObject("streak_dias_consecutivos") instanceof Number number
                        ? number.intValue()
                        : 0;
                }
            }
        }

        totalQuestoesResolvidas += totalQuestoes;
        acertosAcumulados += totalAcertos;
        errosAcumulados += totalErros;
        double taxaAcertoGeral = totalQuestoesResolvidas == 0
            ? 0d
            : (double) acertosAcumulados / totalQuestoesResolvidas;
        Integer[] novosUltimosAcertos = appendUltimoValor(ultimosAcertos, totalAcertos);
        Integer[] novosUltimosTotais = appendUltimoValor(ultimosTotais, totalQuestoes);
        String nivelBanco = nivelAtual == null || nivelAtual.isBlank() ? "INICIANTE" : nivelAtual;
        String nivelAnteriorBanco = nivelAnterior == null || nivelAnterior.isBlank() ? nivelBanco : nivelAnterior;

        if (progressoId == null) {
            String insertSql = """
                insert into progresso_aluno_disciplina (
                  id,
                  aluno_id,
                  disciplina_id,
                  nivel_atual,
                  nivel_anterior,
                  data_mudanca_nivel,
                  peso_atual,
                  total_questoes_resolvidas,
                  total_acertos,
                  total_erros,
                  taxa_acerto_geral,
                  ultimos_3_diagnosticos_acertos,
                  ultimos_3_diagnosticos_total,
                  ultimo_estudo,
                  dias_sem_estudo,
                  streak_dias_consecutivos,
                  criado_em,
                  atualizado_em
                ) values (
                  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), now()
                )
                """;

            try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                stmt.setObject(1, UUID.randomUUID());
                stmt.setObject(2, candidatoId);
                stmt.setObject(3, disciplinaId);
                stmt.setString(4, nivelBanco);
                stmt.setString(5, nivelAnteriorBanco);
                stmt.setTimestamp(6, Timestamp.valueOf(concluidoEm));
                stmt.setDouble(7, pesoDisciplina(disciplinaNome));
                stmt.setInt(8, totalQuestoesResolvidas);
                stmt.setInt(9, acertosAcumulados);
                stmt.setInt(10, errosAcumulados);
                stmt.setDouble(11, taxaAcertoGeral);
                stmt.setArray(12, toSqlArray(conn, novosUltimosAcertos));
                stmt.setArray(13, toSqlArray(conn, novosUltimosTotais));
                stmt.setTimestamp(14, Timestamp.valueOf(concluidoEm));
                stmt.setInt(15, 0);
                stmt.setInt(16, Math.max(1, streak));
                stmt.executeUpdate();
            }
            return;
        }

        String updateSql = """
            update progresso_aluno_disciplina
            set nivel_anterior = ?,
                nivel_atual = ?,
                data_mudanca_nivel = ?,
                peso_atual = ?,
                total_questoes_resolvidas = ?,
                total_acertos = ?,
                total_erros = ?,
                taxa_acerto_geral = ?,
                ultimos_3_diagnosticos_acertos = ?,
                ultimos_3_diagnosticos_total = ?,
                ultimo_estudo = ?,
                dias_sem_estudo = 0,
                streak_dias_consecutivos = ?,
                atualizado_em = now()
            where id = ?
            """;

        try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
            stmt.setString(1, nivelAnteriorBanco);
            stmt.setString(2, nivelBanco);
            stmt.setTimestamp(3, Timestamp.valueOf(concluidoEm));
            stmt.setDouble(4, pesoAtual > 0 ? pesoAtual : pesoDisciplina(disciplinaNome));
            stmt.setInt(5, totalQuestoesResolvidas);
            stmt.setInt(6, acertosAcumulados);
            stmt.setInt(7, errosAcumulados);
            stmt.setDouble(8, taxaAcertoGeral);
            stmt.setArray(9, toSqlArray(conn, novosUltimosAcertos));
            stmt.setArray(10, toSqlArray(conn, novosUltimosTotais));
            stmt.setTimestamp(11, Timestamp.valueOf(concluidoEm));
            stmt.setInt(12, Math.max(1, streak));
            stmt.setObject(13, progressoId);
            stmt.executeUpdate();
        }
    }


    private Integer[] parseIntegerArray(java.sql.Array array) {
        if (array == null) {
            return new Integer[0];
        }
        try {
            Object raw = array.getArray();
            if (raw instanceof Object[] values) {
                return Arrays.stream(values)
                    .filter(value -> value instanceof Number)
                    .map(value -> ((Number) value).intValue())
                    .toArray(Integer[]::new);
            }
        } catch (Exception ignored) {
        }
        return new Integer[0];
    }

    private double pesoDisciplina(String disciplinaNome) {
        return disciplinaService.discCategoria().stream()
            .filter(disciplina -> normalizar(disciplina.nome()).equals(normalizar(disciplinaNome)))
            .map(DisciplinaDto::peso)
            .filter(java.util.Objects::nonNull)
            .mapToDouble(Float::doubleValue)
            .findFirst()
            .orElse(1.0d);
    }


    private Integer[] appendUltimoValor(Integer[] origem, int valor) {
        ArrayList<Integer> valores = new ArrayList<>();
        if (origem != null) {
            valores.addAll(Arrays.asList(origem));
        }
        valores.add(valor);
        while (valores.size() > 3) {
            valores.removeFirst();
        }
        return valores.toArray(Integer[]::new);
    }

    private java.sql.Array toSqlArray(Connection conn, Integer[] valores) throws SQLException {
        return conn.createArrayOf("integer", valores == null ? new Integer[0] : valores);
    }


    private String normalizar(String valor) {
        return normalizarTextoLivre(safeText(valor, ""));
    }

    private String safeText(Object value, String defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        String text = value.toString().trim();
        return text.isEmpty() ? defaultValue : text;
    }


    private String normalizarTextoLivre(String valor) {
        String semAcento = Normalizer.normalize(valor == null ? "" : valor, Normalizer.Form.NFD)
            .replaceAll("\\p{M}+", "");
        return semAcento.trim().toLowerCase(Locale.ROOT);
    }

}
