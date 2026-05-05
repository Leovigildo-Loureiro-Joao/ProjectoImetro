package com.imetro.services;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.imetro.domain.dto.Stats;
import com.imetro.domain.dto.StatsProgress;
import com.imetro.domain.dto.diagnostico.DiagnosticoDto;
import com.imetro.domain.dto.test.TestDtoAll;
import com.imetro.persistence.repository.DiagnosticoRepository;
import com.imetro.persistence.repository.TesteRepository;
import com.imetro.persistence.repository.TesteStatsRepository;
import com.imetro.util.Authentication;

public class TesteService {
    private final TesteRepository testeRepository;
    private final TesteStatsRepository testeStatsRepository;
    private final DiagnosticoRepository diagnosticoRepository;

    public TesteService() {
        this.testeRepository = new TesteRepository();
        this.testeStatsRepository = new TesteStatsRepository();
        this.diagnosticoRepository=new DiagnosticoRepository();
    }

    public Optional<Map<String, Object>> getTeste(UUID testeId) {
        try {
            return testeRepository.findById(testeId);
        } catch (SQLException e) {
            System.err.println("Erro ao carregar teste: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Map<String, Object>> getTestesDisciplina(UUID disciplinaId) {
        try {
            return testeRepository.findByDisciplinaId(disciplinaId);
        } catch (SQLException e) {
            System.err.println("Erro ao carregar testes por disciplina: " + e.getMessage());
            return List.of();
        }
    }

    public List<Map<String, Object>> getTestesCandidato(UUID candidatoId) {
        try {
            return testeRepository.findByCandidatoId(candidatoId);
        } catch (SQLException e) {
            System.err.println("Erro ao carregar testes do candidato: " + e.getMessage());
            return List.of();
        }
    }

    public Optional<Map<String, Object>> getStatsDoTeste(UUID testeId) {
        try {
            return testeStatsRepository.findByTesteId(testeId);
        } catch (SQLException e) {
            System.err.println("Erro ao carregar stats do teste: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Map<String, Object>> getStatsDaDisciplina(UUID disciplinaId) {
        try {
            return testeStatsRepository.findByDisciplinaId(disciplinaId);
        } catch (SQLException e) {
            System.err.println("Erro ao carregar stats da disciplina: " + e.getMessage());
            return List.of();
        }
    }

    public List<Map<String, Object>> getStatsDoCandidato(UUID candidatoId) {
        try {
            return testeStatsRepository.findByCandidatoId(candidatoId);
        } catch (SQLException e) {
            System.err.println("Erro ao carregar stats do candidato: " + e.getMessage());
            return List.of();
        }
    }

    public StatsProgress Stats(){
        float velocidade=0,precisao=0,consistencia=0,resiliencia=0,logica=0,progresso=0;
        try {
            for (Map<String,Object> map : testeRepository.findByCandidatoId(Authentication.getCurrentUserId())) {
                TestDtoAll test=TestDtoAll.ParseMapDto(map);    
                Map<String, Object> value=diagnosticoRepository.findById(test.diagnostico_id()).orElseThrow();
                DiagnosticoDto diagnosticoDto=DiagnosticoDto.ParseMapDto(value);
                velocidade+=test.velocidade()-diagnosticoDto.velocidade();
                precisao+=test.precisao()-diagnosticoDto.precisao();
                consistencia+=test.consistencia()-diagnosticoDto.consistencia();
                logica+=test.logica()-diagnosticoDto.logica();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new StatsProgress(velocidade,precisao,consistencia,resiliencia,logica,progresso);
    }
}
