package com.imetro.persistence.repository;

import static com.imetro.persistence.repository.JdbcBasicSqlRepository.readAllRows;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.imetro.domain.dto.diagnostico.DiagnosticoDto;
import com.imetro.domain.model.Candidato;
import com.imetro.util.ParseTimeStampLocalDate;

public class DiagnosticoRepository extends JdbcBasicSqlRepository{

    public DiagnosticoRepository() {
        super("diagnostico", "id");
    }

    public List<DiagnosticoDto> findAllDto(){
        try {
            List<DiagnosticoDto> lista=new ArrayList();
            for (Map<String, Object> link : findAll()) {
                lista.add(DiagnosticoDto.ParseMapDto(link));
            }
            return lista;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            
        }
        return null;
    }

    public List<DiagnosticoDto> CandidatoDiagnostico(UUID caUuid){
        String sql="SELECT * FROM diagnosticos where candidato_id =?";
        try (var conn = JdbcBasicSqlRepository.openRequiredConnection();
            var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, caUuid.toString());
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    List<Map<String, Object>> value=JdbcBasicSqlRepository.readAllRows(rs);
                    if (value instanceof  List<Map<String, Object>> list) {
                        List<DiagnosticoDto> lista=new ArrayList();
                        for (Map<String, Object> link : list) {
                            lista.add(DiagnosticoDto.ParseMapDto(link));
                        }
                        return lista;
                    }
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return null;
    }
   
   
}
