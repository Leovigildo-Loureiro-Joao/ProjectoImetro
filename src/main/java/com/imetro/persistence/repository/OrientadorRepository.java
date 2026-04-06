package com.imetro.persistence.repository;

public class OrientadorRepository extends JdbcBasicSqlRepository {

    public OrientadorRepository() {
        super("orientadores", "id");
    }
    
}
