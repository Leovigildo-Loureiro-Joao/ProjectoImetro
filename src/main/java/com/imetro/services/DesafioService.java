package com.imetro.services;

import java.sql.SQLException;
import java.util.List;

import com.imetro.domain.dto.Desafio;
import com.imetro.domain.dto.planejamento.PlaneamentoEstudoResumo;
import com.imetro.domain.dto.progresso.ProgressaoRigorDto;
import com.imetro.domain.dto.progresso.ProgressoDisciplinaTeste;
import com.imetro.domain.dto.stats.Teste_Stat;
import com.imetro.domain.enums.TipoDesafio;
import com.imetro.persistence.repository.ProgressaoRigorRepository;
import com.imetro.persistence.repository.TesteStatsRepository;
import com.imetro.util.Authentication;

public class DesafioService {

    private TesteStatsRepository statsRepository;
    private ProgressaoRigorRepository progressoRepository;


    public Desafio gerarDesafio(PlaneamentoEstudoResumo resumo) throws SQLException{
        var candidato=Authentication.getCurrentUserId();
        List<ProgressaoRigorDto> progresso = progressoRepository.findByCandidato(candidato).stream().map(ProgressaoRigorDto::fromMap).toList();
        List<Teste_Stat> testStats = statsRepository.findByCandidatoId(candidato).stream().map(Teste_Stat::ParseDto).toList();

        if(progresso.isEmpty() || testStats.isEmpty()){
            String foco=resumo.focoAtual().split("-")[1];
            String disciplina=resumo.focoAtual().split("-")[0];
            return new Desafio(TipoDesafio.NOVO_DIAGNOSTICO, "Inicie logo diagnoticando suas habilidades", "Analise ate que ponto dominas "+foco, disciplina, foco, 0, 0, DesafioStatus., null);
        }



        return null;
    }

}