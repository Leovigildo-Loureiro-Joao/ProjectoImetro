package com.imetro.persistence.repository;

import java.sql.SQLException;

import com.imetro.domain.dto.configuracao.ConfiguracaoTesteAdaptativoNivelDto;

public class ConfiguracaoTesteAdaptativoNivelRepositorty extends JdbcBasicSqlRepository {

    public ConfiguracaoTesteAdaptativoNivelRepositorty() {
        super("configuracoes_teste_adaptativo_niveis", "id");
     }

     public int inserirNivel(ConfiguracaoTesteAdaptativoNivelDto nivel) {
        try {
         return super.insert(nivel.toMap());
        } catch (SQLException e) {
         e.printStackTrace();
         return -1;
        }
     }

      public int updateById(ConfiguracaoTesteAdaptativoNivelDto nivel) throws SQLException   {
         return super.updateById(nivel.id(), nivel.toMap());
      }

      public ConfiguracaoTesteAdaptativoNivelDto findById(String id) throws SQLException {
         return super.findById(id).map(ConfiguracaoTesteAdaptativoNivelDto::fromMap).orElse(null);
      }

      public ConfiguracaoTesteAdaptativoNivelDto findByCodigo(String codigo) throws SQLException {
         return super.findOneByField("codigo", codigo).map(ConfiguracaoTesteAdaptativoNivelDto::fromMap).orElse(null);
      }

}
