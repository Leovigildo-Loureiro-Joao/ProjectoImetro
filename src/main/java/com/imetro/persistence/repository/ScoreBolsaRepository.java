package com.imetro.persistence.repository;

public class ScoreBolsaRepository extends JdbcBasicSqlRepository{

    protected ScoreBolsaRepository() {
        super("score_bolsas", "id");
    }

}
