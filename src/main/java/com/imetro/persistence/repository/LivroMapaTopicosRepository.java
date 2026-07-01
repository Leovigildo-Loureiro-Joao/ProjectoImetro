package com.imetro.persistence.repository;

import com.imetro.domain.dto.biblioteca.LivroMapaTopicos;
import com.imetro.domain.dto.disciplina.DisciplinaDto;
import com.imetro.util.DtoMapperSupport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LivroMapaTopicosRepository extends JdbcBasicSqlRepository {

    public LivroMapaTopicosRepository() {
        super("livro_mapa_topicos", "id");
    }

    public LivroMapaTopicos getDto(UUID id) throws SQLException {
        return findById(id)
            .map(LivroMapaTopicos::fromMap)
            .orElse(null);
    }

    public List<LivroMapaTopicos> findAllDto() throws SQLException {
        return findAll().stream()
            .map(LivroMapaTopicos::fromMap)
            .toList();
    }

    public List<LivroMapaTopicos> findTopicos(String topico){
        String sql = """
           SELECT id,livro_id,topico,subtopico,pagina_inicio,pagina_fim,criado_em 
           FROM livro_mapa_topicos WHERE topico = ?
        """;
        ArrayList<LivroMapaTopicos> lista = new ArrayList<>();

         try (Connection conn = JdbcBasicSqlRepository.openRequiredConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, topico);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new LivroMapaTopicos(
                        rs.getObject("id",UUID.class), 
                        rs.getObject("livro_id",UUID.class), 
                        rs.getString("topico"),
                        rs.getString("subtopico"),
                        DtoMapperSupport.parseInteger(rs.getObject("pagina_inicio")),
                        DtoMapperSupport.parseInteger(rs.getObject("pagina_fim")),
                        DtoMapperSupport.parseDateTime(rs.getObject("criado_em"))
                    ));
                }
                return lista;
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar disciplinas ativas do candidato: " + e.getMessage());
        }
        return List.of();
    }

      public List<LivroMapaTopicos> findSubTopicos(String topico){
        String sql = """
           SELECT id,livro_id,topico,subtopico,pagina_inicio,pagina_fim,criado_em 
           FROM livro_mapa_topicos WHERE subtopico = ?
        """;
        ArrayList<LivroMapaTopicos> lista = new ArrayList<>();

         try (Connection conn = JdbcBasicSqlRepository.openRequiredConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, topico);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new LivroMapaTopicos(
                        rs.getObject("id",UUID.class), 
                        rs.getObject("livro_id",UUID.class), 
                        rs.getString("topico"),
                        rs.getString("subtopico"),
                        DtoMapperSupport.parseInteger(rs.getObject("pagina_inicio")),
                        DtoMapperSupport.parseInteger(rs.getObject("pagina_fim")),
                        DtoMapperSupport.parseDateTime(rs.getObject("criado_em"))
                    ));
                }
                return lista;
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar disciplinas ativas do candidato: " + e.getMessage());
        }
        return List.of();
    }

    public void insertDto(LivroMapaTopicos dto) throws SQLException {
        insert(dto.toMap());
    }

    public void updateDto(UUID id, LivroMapaTopicos dto) throws SQLException {
        updateById(id, dto.toMap());
    }
}
