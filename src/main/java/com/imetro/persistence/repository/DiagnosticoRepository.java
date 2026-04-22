package com.imetro.persistence.repository;

public class DiagnosticoRepository extends JdbcBasicSqlRepository{

    protected DiagnosticoRepository() {
        super("diagnostico", "id");
    }
     
}
