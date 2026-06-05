package com.imetro.persistence.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.imetro.domain.dto.configuracao.AdaptacaoDto;

public class AdaptacaoRepository extends  JdbcBasicSqlRepository {

    public AdaptacaoRepository() {
        super("adaptacao", "id");
    }

    public int inserirConfiguracao(AdaptacaoDto configuracao) {
       try {
        return super.insert(configuracao.toMap());
       } catch (SQLException e) {
        e.printStackTrace();
        return -1;
       }
    }

    public List<AdaptacaoDto> listarTodas() throws SQLException {
        return super.findAll().stream()
            .map(AdaptacaoDto::fromMap)
            .toList();
    }

    public int updateById(UUID id, AdaptacaoDto configuracao) throws SQLException   {
        return super.updateById(id, configuracao.toMap());
    }

    public AdaptacaoDto findById(UUID id) throws SQLException {
        Map<String, Object> result = super.findById(id).orElse(null);
        if (result != null) {
            return AdaptacaoDto.fromMap(result);
        }
        return null;
    }

    public AdaptacaoDto findByIdCandidato(UUID id) throws SQLException {
        Map<String, Object> result = super.findOneByField("user_id",id).orElse(null);
        if (result != null) {
            return AdaptacaoDto.fromMap(result);
        }
        return null;
    }

    public AdaptacaoDto findOrCreateByUserId(UUID userId) throws SQLException {
        if (userId == null) {
            return AdaptacaoDto.padrao(null);
        }

        ensureDefaultsForUserId(userId);
        AdaptacaoDto configuracao = findByIdCandidato(userId);
        return configuracao != null ? configuracao : AdaptacaoDto.padrao(userId);
    }

    public int ensureDefaultsForUserId(UUID userId) throws SQLException {
        if (userId == null) {
            return 0;
        }

        try (Connection conn = openRequiredConnection()) {
            return ensureDefaultsForUser(conn, userId);
        }
    }

    public int ensureDefaultsForUser(Connection conn, UUID userId) throws SQLException {
        if (conn == null || userId == null) {
            return 0;
        }

        AdaptacaoDto padrao = AdaptacaoDto.padrao(userId);
        String sql = """
            insert into adaptacao (
              user_id,
              tempo_lento_fator,
              tempo_recuperacao_fator,
              tempo_adapt,
              acertos_subir_rapido,
              acertos_subir_lento,
              erros_descer,
              janela_consistencia,
              janela_recuperacao,
              peso_consistencia_acerto,
              peso_consistencia_ritmo,
              peso_resiliencia_recuperacao,
              peso_resiliencia_estabilidade
            ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            on conflict (user_id) do nothing
            """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, userId);
            stmt.setDouble(3, padrao.tempoLentoFator());
            stmt.setDouble(4, padrao.tempoRecuperacaoFator());
            stmt.setInt(5, padrao.tempAdapt());
            stmt.setInt(6, padrao.acertosSubirRapido());
            stmt.setInt(7, padrao.acertosSubirLento());
            stmt.setInt(8, padrao.errosDescer());
            stmt.setInt(9, padrao.janelaConsistencia());
            stmt.setInt(10, padrao.janelaRecuperacao());
            stmt.setDouble(11, padrao.pesoConsistenciaAcerto());
            stmt.setDouble(12, padrao.pesoConsistenciaRitmo());
            stmt.setDouble(13, padrao.pesoResilienciaRecuperacao());
            stmt.setDouble(14, padrao.pesoResilienciaEstabilidade());
            return stmt.executeUpdate();
        }
    }
}
