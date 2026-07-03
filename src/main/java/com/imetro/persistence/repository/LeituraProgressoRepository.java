package com.imetro.persistence.repository;

import com.imetro.domain.dto.leitura.LeituraProgresso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
