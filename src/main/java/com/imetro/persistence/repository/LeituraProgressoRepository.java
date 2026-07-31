package com.imetro.persistence.repository;

import com.imetro.domain.dto.leitura.LeituraProgresso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LeituraProgressoRepository extends JdbcBasicSqlRepository {

    public LeituraProgressoRepository() {
        super("leitura_progresso", "id");
    }

    public LeituraProgresso getDto(UUID id) throws SQLException {
        return findById(id)
            .map(LeituraProgresso::fromMap)
            .orElse(null);
    }

    public List<LeituraProgresso> findAllDto() throws SQLException {
        return findAll().stream()
            .map(LeituraProgresso::fromMap)
            .toList();
    }

    public void insertDto(LeituraProgresso dto) throws SQLException {
        insert(dto.toMap());
    }

    public void updateDto(UUID id, LeituraProgresso dto) throws SQLException {
        updateById(id, dto.toMap());
    }

    public boolean existeLeituraHoje(UUID alunoId) throws SQLException {
        String sql = "select count(*) from leitura_progresso where aluno_id = ? and atualizado_em >= ?";
        try (Connection conn = JdbcBasicSqlRepository.openRequiredConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, alunoId);
            stmt.setObject(2, LocalDate.now().atStartOfDay());
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public void registrarAtividadeHoje(UUID alunoId, UUID livroId) throws SQLException {
        LeituraProgresso existente = findByAlunoAndLivro(alunoId, livroId);
        LocalDateTime agora = LocalDateTime.now();
        if (existente != null) {
            updateByField("aluno_id", alunoId, Map.of("atualizado_em", agora));
        } else {
            LeituraProgresso novo = new LeituraProgresso(
                UUID.randomUUID(), alunoId, livroId,
                0, 0, "", "ABERTO", "", agora, agora
            );
            insertDto(novo);
        }
    }

    public LeituraProgresso findByAlunoAndLivro(UUID alunoId, UUID livroId) throws SQLException {
        String sql = "select * from leitura_progresso where aluno_id = ? and livro_id = ?";
        try (Connection conn = JdbcBasicSqlRepository.openRequiredConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, alunoId);
            stmt.setObject(2, livroId);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Map<String, Object>> rows = readAllRows(rs);
                if (rows.isEmpty()) {
                    return null;
                }
                return LeituraProgresso.fromMap(rows.getFirst());
            }
        }
    }
}
