package com.imetro.persistence.repository;

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
        Map<String, Object> map = teste_Pergunta.toMap();
        map.put("teste_id",teste_id);
        return insert(map);
    }


}
