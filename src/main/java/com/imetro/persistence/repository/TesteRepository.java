package com.imetro.persistence.repository;

public class TesteRepository extends JdbcBasicSqlRepository{

    public TesteRepository() {
        super("testes", "id");
    }
    
}
