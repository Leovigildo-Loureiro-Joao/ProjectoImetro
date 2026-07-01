package com.imetro.persistence.repository;

import com.imetro.domain.dto.leitura.TrilhoLeitura;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class TrilhoLeituraRepository extends JdbcBasicSqlRepository {

    public TrilhoLeituraRepository() {
        super("trilho_leitura", "id");
    }

    public TrilhoLeitura getDto(UUID id) throws SQLException {
        return findById(id)
            .map(TrilhoLeitura::fromMap)
            .orElse(null);
    }

    public List<TrilhoLeitura> findAllDto() throws SQLException {
        return findAll().stream()
            .map(TrilhoLeitura::fromMap)
            .toList();
    }

    public void insertDto(TrilhoLeitura dto) throws SQLException {
        insert(dto.toMap());
    }

    public void updateDto(UUID id, TrilhoLeitura dto) throws SQLException {
        updateById(id, dto.toMap());
    }
}
