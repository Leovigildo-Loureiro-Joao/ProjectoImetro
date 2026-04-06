package com.imetro.persistence.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Repositório SQL genérico para operações básicas em uma tabela.
 *
 * <p>Observação: para evitar SQL injection, o nome da tabela/colunas deve ser validado
 * antes de ser interpolado no SQL (valores usam {@code PreparedStatement}).</p>
 */
public interface BasicSqlRepository {

    List<Map<String, Object>> findAll() throws SQLException;

    Optional<Map<String, Object>> findById(Object id) throws SQLException;

    int deleteById(Object id) throws SQLException;

    int deleteAll() throws SQLException;

    int insert(Map<String, ?> fields) throws SQLException;

    int updateById(Object id, Map<String, ?> fields) throws SQLException;
}

