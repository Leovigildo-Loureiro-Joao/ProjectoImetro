package com.imetro.persistence.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.imetro.domain.dto.configuracao.ConfiguracaoTesteAdaptativoDto;

public class ConfiguracoesTesteAdaptativoRespository extends  JdbcBasicSqlRepository {

    public ConfiguracoesTesteAdaptativoRespository() {
        super("configuracoes_teste_adaptativo", "id");
    }

    public int inserirConfiguracao(ConfiguracaoTesteAdaptativoDto configuracao) {
       try {
        return super.insert(configuracao.toMap());
       } catch (SQLException e) {
        e.printStackTrace();
        return -1;
       }
    }

    public List<ConfiguracaoTesteAdaptativoDto> listarTodas() throws SQLException {
        return super.findAll().stream()
            .map(ConfiguracaoTesteAdaptativoDto::fromMap)
            .toList();
    }

    public int updateById(UUID id, ConfiguracaoTesteAdaptativoDto configuracao) throws SQLException   {
        return super.updateById(id, configuracao.toMap());
    }

    public ConfiguracaoTesteAdaptativoDto findById(UUID id) throws SQLException {
        Map<String, Object> result = super.findById(id).orElse(null);
        if (result != null) {
            return ConfiguracaoTesteAdaptativoDto.fromMap(result);
        }
        return null;
    }

    public ConfiguracaoTesteAdaptativoDto findByCodigo(String codigo) throws SQLException {
        Map<String, Object> result = super.findOneByField("codigo", codigo).orElse(null);
        if (result != null) {
            return ConfiguracaoTesteAdaptativoDto.fromMap(result);
        }
        return null;
     }

     public ConfiguracaoTesteAdaptativoDto findAtiva() throws SQLException {
        Map<String, Object> result = super.findOneByField("ativo", true).orElse(null);
        if (result != null) {
            return ConfiguracaoTesteAdaptativoDto.fromMap(result);
        }
        return null;
     }

     public int desativarTodas() throws SQLException {
        return super.updateByField("ativo", true, Map.of("ativo", false));
     }

     public int ativarPorId(UUID id) throws SQLException {
        return super.updateById(id, Map.of("ativo", true));
     }

     public int ativarPorCodigo(String codigo) throws SQLException {
        return super.updateByField("codigo", codigo, Map.of("ativo", true));
    }

}
