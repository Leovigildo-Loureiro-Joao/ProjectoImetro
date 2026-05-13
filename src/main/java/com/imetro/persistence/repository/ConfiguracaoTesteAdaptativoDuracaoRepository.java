package com.imetro.persistence.repository;

import java.sql.SQLException;

import com.imetro.domain.dto.configuracao.ConfiguracaoTesteAdaptativoDuracaoDto;

public class ConfiguracaoTesteAdaptativoDuracaoRepository extends JdbcBasicSqlRepository {

    public ConfiguracaoTesteAdaptativoDuracaoRepository() {
        super("configuracoes_teste_adaptativo_duracao", "id");
    }

    public int inserirDuracao(ConfiguracaoTesteAdaptativoDuracaoDto duracao) {
       try {
        return super.insert(duracao.toMap());
       } catch (SQLException e) {
        e.printStackTrace();
        return -1;
       }
    }

     public int updateById(ConfiguracaoTesteAdaptativoDuracaoDto duracao) throws SQLException   {
        return super.updateById(duracao.id(), duracao.toMap());
     }

     public ConfiguracaoTesteAdaptativoDuracaoDto findById(String id) throws SQLException {
        return super.findById(id).map(ConfiguracaoTesteAdaptativoDuracaoDto::fromMap).orElse(null);
     }

     public ConfiguracaoTesteAdaptativoDuracaoDto findByCodigo(String codigo) throws SQLException {
        return super.findOneByField("codigo", codigo).map(ConfiguracaoTesteAdaptativoDuracaoDto::fromMap).orElse(null);
     }

     
}
