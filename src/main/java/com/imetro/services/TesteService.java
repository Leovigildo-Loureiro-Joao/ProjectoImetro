package com.imetro.services;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.imetro.persistence.repository.TesteRepository;
import com.imetro.persistence.repository.TesteStatsRepository;

public class TesteService {
    private final TesteRepository testeRepository;
    private final TesteStatsRepository testeStatsRepository;

    public TesteService() {
        this.testeRepository = new TesteRepository();
        this.testeStatsRepository = new TesteStatsRepository();
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
}
