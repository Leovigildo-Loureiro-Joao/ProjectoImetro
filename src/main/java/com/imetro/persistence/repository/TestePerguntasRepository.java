package com.imetro.persistence.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

import com.imetro.domain.dto.test.Teste_Pergunta;


public class TestePerguntasRepository extends JdbcBasicSqlRepository{

   public TestePerguntasRepository(){
        super("teste_perguntas", "teste_id", "pergunta_id");
   }

   public int inserir(
       Teste_Pergunta teste_Pergunta,UUID teste_id) throws SQLException {
        try (Connection conn = JdbcBasicSqlRepository.openRequiredConnection()) {
            return inserir(conn, teste_Pergunta, teste_id);
        }
   }

   public int inserir(Connection conn, Teste_Pergunta teste_Pergunta, UUID teste_id) throws SQLException {
        if (conn == null) {
            throw new IllegalArgumentException("conn must not be null");
        }
        if (teste_Pergunta == null) {
            throw new IllegalArgumentException("teste_Pergunta must not be null");
        }

        Map<String, Object> map = teste_Pergunta.toMap();
        map.put("teste_id",teste_id);
        return insert(conn, map);
    }


}
