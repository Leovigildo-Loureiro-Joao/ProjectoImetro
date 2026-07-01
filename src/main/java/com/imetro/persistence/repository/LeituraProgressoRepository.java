package com.imetro.persistence.repository;

import com.imetro.domain.dto.leitura.LeituraProgresso;

import java.sql.SQLException;
import java.util.List;
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
}
