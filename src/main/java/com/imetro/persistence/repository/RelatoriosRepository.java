package com.imetro.persistence.repository;

public class RelatoriosRepository extends JdbcBasicSqlRepository{

    public RelatoriosRepository() {
        super("relatorios", "id");
    }
    
}
