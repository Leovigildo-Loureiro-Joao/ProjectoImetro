package com.imetro.persistence.repository;

import com.imetro.domain.dto.test.MiniTeste;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class MiniTesteRepository extends JdbcBasicSqlRepository {

    public MiniTesteRepository() {
        super("mini_teste", "id");
    }

    public MiniTeste getDto(UUID id) throws SQLException {
        return findById(id)
            .map(MiniTeste::fromMap)
            .orElse(null);
    }

    public List<MiniTeste> findAllDto() throws SQLException {
        return findAll().stream()
            .map(MiniTeste::fromMap)
            .toList();
    }

    public void insertDto(MiniTeste dto) throws SQLException {
        insert(dto.toMap());
    }

    public void updateDto(UUID id, MiniTeste dto) throws SQLException {
        updateById(id, dto.toMap());
    }
}
