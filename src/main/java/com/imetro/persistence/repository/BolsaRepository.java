package com.imetro.persistence.repository;

public class BolsaRepository extends JdbcBasicSqlRepository {

    protected BolsaRepository() {
        super("bolsas", "id");
    }

}
