package com.imetro.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

import com.imetro.domain.Disciplina;
import com.imetro.domain.dto.disciplina.DisciplinaDto;
import com.imetro.domain.enums.NivelDisciplina;
import com.imetro.persistence.repository.DisciplinaRepository;

public class DisciplinaService {
    private final DisciplinaRepository disciplinaRepository=new DisciplinaRepository();

    public ArrayList<DisciplinaDto> discCategoria(){
        ArrayList<DisciplinaDto> disc=new ArrayList<>();
        try {
            for (Object elObject : disciplinaRepository.findAll()) {
                if (elObject instanceof LinkedHashMap) {
                    @SuppressWarnings("unchecked")
                    LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) elObject;
                    UUID id = (UUID) map.get("id");
                    String nome = (String) map.get("nome");
                    Float peso = ((Number) map.get("peso")).floatValue();
                    String nivelStr = (String) map.get("nivel");
                    String objectivo = (String) map.get("objectivo");
                    NivelDisciplina nivelDisciplina=NivelDisciplina.valueOf(nivelStr);
                    disc.add(new DisciplinaDto(id,nome,peso,nivelDisciplina,objectivo));
                }
            }
        } catch (Exception e) {
             System.err.println("Erro ao buscar disciplinas: " + e.getMessage());
        };
        return disc;
    }

}
