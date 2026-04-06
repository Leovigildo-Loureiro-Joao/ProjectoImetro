package com.imetro.persistence.repository;

public class PerguntasRepository extends JdbcBasicSqlRepository{

    public PerguntasRepository() {
        super("perguntas", "id");
    }
    
}
