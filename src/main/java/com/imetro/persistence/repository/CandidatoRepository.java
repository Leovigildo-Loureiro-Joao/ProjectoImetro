package com.imetro.persistence.repository;

public final class CandidatoRepository extends JdbcBasicSqlRepository {

    public CandidatoRepository() {
        super("candidatos", "id");
    }
}
