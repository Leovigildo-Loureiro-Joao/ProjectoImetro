package com.imetro.persistence.repository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.imetro.domain.dto.progresso.ProgressoAlunoDisciplinaDto;
import com.imetro.domain.enums.NivelDisciplina;

public class ProgressoALunoDisciplinaRepository extends JdbcBasicSqlRepository{

    public ProgressoALunoDisciplinaRepository() {
        super("progresso_aluno_disciplina", "id");
    }

    public boolean hasAny(UUID candidatoId) {
        String sql = "SELECT 1 FROM progresso_aluno_disciplina WHERE aluno_id = ? LIMIT 1";
        try (var conn = JdbcBasicSqlRepository.openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, candidatoId);
            try (var rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void associarDisciplinaCandidato(UUID candidatoId, UUID disciplinaId) throws SQLException {
        String sql = "INSERT INTO progresso_aluno_disciplina (aluno_id, disciplina_id) VALUES (?, ?)";
        try (var conn = JdbcBasicSqlRepository.openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, candidatoId);
            stmt.setObject(2, disciplinaId);
            stmt.executeUpdate();
        }
    }

    public int deleteByAlunoId(UUID candidatoId) throws SQLException {
        if (candidatoId == null) {
            return 0;
        }

        String sql = """
            delete from progresso_aluno_disciplina
            where aluno_id = ?
            """;

        try (var conn = JdbcBasicSqlRepository.openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, candidatoId);
            return stmt.executeUpdate();
        }
    }

    public void atualizarProgresso(UUID candidatoId, UUID disciplinaId, float progresso) throws SQLException {
        String sql = "UPDATE progresso_aluno_disciplina SET progresso = ? WHERE aluno_id = ? AND disciplina_id = ?";
        try (var conn = JdbcBasicSqlRepository.openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setFloat(1, progresso);
            stmt.setObject(2, candidatoId);
            stmt.setObject(3, disciplinaId);
            stmt.executeUpdate();
        }
    }

    public  ProgressoAlunoDisciplinaDto getDto(UUID candidatoId, UUID disciplinaId) throws SQLException {
        String sql = """
            select p.*, d.nome as disciplina_nome
            from progresso_aluno_disciplina p
            left join disciplinas d on d.id = p.disciplina_id
            where p.aluno_id = ? and p.disciplina_id = ?
            """;
        try (var conn = JdbcBasicSqlRepository.openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, candidatoId);
            stmt.setObject(2, disciplinaId);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    UUID id = (UUID) rs.getObject("id");
                    String disciplina = rs.getString("disciplina_nome");
                    NivelDisciplina nivelAtual=NivelDisciplina.fromDescricao(rs.getString("nivel_atual"));
                    NivelDisciplina nivelAnterior=NivelDisciplina.fromDescricao(rs.getString("nivel_anterior"));
                    LocalDate dataMudancaNivel = rs.getDate("data_mudanca_nivel") != null ? rs.getDate("data_mudanca_nivel").toLocalDate() : null;
                    Double pesoAtual = rs.getDouble("peso_atual");
                    Integer totalQuestoesResolvidas = rs.getInt("total_questoes_resolvidas");
                    Integer totalAcertos = rs.getInt("total_acertos");
                    Integer totalErros = rs.getInt("total_erros");
                    Double taxaAcertoGeral = rs.getDouble("taxa_acerto_geral");
                    float progresso = taxaAcertoGeral == null ? 0f : taxaAcertoGeral.floatValue();
                    Integer[] ultimos3DiagnosticosAcertos =
                        ProgressoAlunoDisciplinaDto.ConvertIntegerVector(rs.getArray("ultimos_3_diagnosticos_acertos"));
                    Integer[] ultimos3DiagnosticosTotal =
                        ProgressoAlunoDisciplinaDto.ConvertIntegerVector(rs.getArray("ultimos_3_diagnosticos_total"));
                    LocalDateTime ultimoEstudo = rs.getTimestamp("ultimo_estudo") != null ? rs.getTimestamp("ultimo_estudo").toLocalDateTime() : null;
                    Integer diasSemEstudo = rs.getInt("dias_sem_estudo");
                    Integer streakDiasConsecutivos = rs.getInt("streak_dias_consecutivos");
                    LocalDateTime criadoEm = rs.getTimestamp("criado_em") != null ? rs.getTimestamp("criado_em").toLocalDateTime() : null;
                    LocalDateTime atualizadoEm = rs.getTimestamp("atualizado_em") != null ? rs.getTimestamp("atualizado_em").toLocalDateTime() : null;
                    return new ProgressoAlunoDisciplinaDto(
                        id,
                        candidatoId,
                        disciplinaId,
                        disciplina,
                        progresso,
                        nivelAtual,
                        nivelAnterior,
                        dataMudancaNivel,
                        pesoAtual,
                        rs.getString("foco_subtopicos"),
                        totalQuestoesResolvidas,
                        totalAcertos,
                        totalErros,
                        taxaAcertoGeral,
                        ultimos3DiagnosticosAcertos,
                        ultimos3DiagnosticosTotal,
                        ultimoEstudo,
                        diasSemEstudo,
                        streakDiasConsecutivos,
                        criadoEm,
                        atualizadoEm
                    );
                }
            }
        }
        return null;
    }


    public  ProgressoAlunoDisciplinaDto getDto(UUID candidatoId, String disciplinaNome) throws SQLException {
        String sql = """
            select p.*, d.nome as disciplina_nome
            from progresso_aluno_disciplina p
            left join disciplinas d on d.id = p.disciplina_id
            where p.aluno_id = ? and d.nome = ?
            """;
            System.out.println(disciplinaNome);
            System.out.println(sql);
        try (var conn = JdbcBasicSqlRepository.openRequiredConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, candidatoId);
            stmt.setString(2, disciplinaNome);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    UUID id = (UUID) rs.getObject("id");
                    String disciplina = rs.getString("disciplina_nome");
                    UUID disciplinaId =(UUID) rs.getObject("disciplina_id");
                    NivelDisciplina nivelAtual=NivelDisciplina.fromDescricao(rs.getString("nivel_atual"));
                    NivelDisciplina nivelAnterior=NivelDisciplina.fromDescricao(rs.getString("nivel_anterior"));
                    LocalDate dataMudancaNivel = rs.getDate("data_mudanca_nivel") != null ? rs.getDate("data_mudanca_nivel").toLocalDate() : null;
                    Double pesoAtual = rs.getDouble("peso_atual");
                    Integer totalQuestoesResolvidas = rs.getInt("total_questoes_resolvidas");
                    Integer totalAcertos = rs.getInt("total_acertos");
                    Integer totalErros = rs.getInt("total_erros");
                    Double taxaAcertoGeral = rs.getDouble("taxa_acerto_geral");
                    float progresso = taxaAcertoGeral == null ? 0f : taxaAcertoGeral.floatValue();
                    Integer[] ultimos3DiagnosticosAcertos =
                        ProgressoAlunoDisciplinaDto.ConvertIntegerVector(rs.getArray("ultimos_3_diagnosticos_acertos"));
                    Integer[] ultimos3DiagnosticosTotal =
                        ProgressoAlunoDisciplinaDto.ConvertIntegerVector(rs.getArray("ultimos_3_diagnosticos_total"));
                    LocalDateTime ultimoEstudo = rs.getTimestamp("ultimo_estudo") != null ? rs.getTimestamp("ultimo_estudo").toLocalDateTime() : null;
                    Integer diasSemEstudo = rs.getInt("dias_sem_estudo");
                    Integer streakDiasConsecutivos = rs.getInt("streak_dias_consecutivos");
                    LocalDateTime criadoEm = rs.getTimestamp("criado_em") != null ? rs.getTimestamp("criado_em").toLocalDateTime() : null;
                    LocalDateTime atualizadoEm = rs.getTimestamp("atualizado_em") != null ? rs.getTimestamp("atualizado_em").toLocalDateTime() : null;
                    return new ProgressoAlunoDisciplinaDto(
                        id,
                        candidatoId,
                        disciplinaId,
                        disciplina,
                        progresso,
                        nivelAtual,
                        nivelAnterior,
                        dataMudancaNivel,
                        pesoAtual,
                        rs.getString("foco_subtopicos"),
                        totalQuestoesResolvidas,
                        totalAcertos,
                        totalErros,
                        taxaAcertoGeral,
                        ultimos3DiagnosticosAcertos,
                        ultimos3DiagnosticosTotal,
                        ultimoEstudo,
                        diasSemEstudo,
                        streakDiasConsecutivos,
                        criadoEm,
                        atualizadoEm
                    );
                }
            }
        }
        return null;
    }

}
