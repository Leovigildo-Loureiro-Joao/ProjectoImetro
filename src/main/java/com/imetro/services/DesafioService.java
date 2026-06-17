package com.imetro.services;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.imetro.domain.dto.Desafio;
import com.imetro.domain.dto.planejamento.PlaneamentoEstudoResumo;
import com.imetro.domain.dto.progresso.ProgressaoRigorDto;
import com.imetro.domain.dto.stats.Teste_Stat;
import com.imetro.domain.dto.test.ErrosComuns;
import com.imetro.domain.enums.TipoDesafio;
import com.imetro.persistence.repository.ProgressaoRigorRepository;
import com.imetro.persistence.repository.TesteStatsRepository;
import com.imetro.util.Authentication;

public class DesafioService {

    private TesteStatsRepository statsRepository;
    private TesteService testeService;
    private ProgressaoRigorRepository progressoRepository;

    public DesafioService(){
        statsRepository=new TesteStatsRepository();
        progressoRepository=new ProgressaoRigorRepository();
        testeService=new TesteService();
    }

    public Desafio gerarDesafio(PlaneamentoEstudoResumo resumo) throws SQLException{
        var candidato=Authentication.getCurrentUserId();
        List<ProgressaoRigorDto> progresso = progressoRepository.findByCandidato(candidato).stream().map(ProgressaoRigorDto::fromMap).toList();
        List<Teste_Stat> testStats = statsRepository.findByCandidatoId(candidato).stream().map(Teste_Stat::ParseDto).toList();
        System.out.println(resumo.focoAtual().split("-").length);
        String foco=resumo.focoAtual().split("-")[2].trim();
        String disciplina=resumo.focoAtual().split("-")[0].trim();
        if(progresso.isEmpty() || testStats.isEmpty()){
            return new Desafio(TipoDesafio.NOVO_DIAGNOSTICO, "Inicie logo diagnoticando suas habilidades", "Analise ate que ponto dominas "+foco, disciplina, foco);
        }

        if(resumo.pontuacaoHero()<45){
            return DesafioErros(testStats,disciplina);
        }
        if(resumo.pontuacaoHero()>90){
            return new Desafio(TipoDesafio.DESAFIO_AVANCADO, "Voce esta indo muito bem", "Vamos tentar subir um pouco mais o nivel de dificuldade em "+foco, disciplina, foco);
        }
        if(resumo.evolucao().size()>1 && resumo.evolucao().get(resumo.evolucao().size()-1).valor()>resumo.evolucao().get(resumo.evolucao().size()-2).valor()){
            return DesafioEvolucao(testStats,disciplina,foco);
        }
        if(resumo.evolucao().size()>3 && resumo.evolucao().get(resumo.evolucao().size()-1).valor()<resumo.evolucao().get(resumo.evolucao().size()-2).valor() && resumo.evolucao().get(resumo.evolucao().size()-2).valor()<resumo.evolucao().get(resumo.evolucao().size()-3).valor()){
            return DesafioEstagnacao(testStats,disciplina,foco);
        }


        return new Desafio(TipoDesafio.REVISAO_GERAL, "Voce esta indo muito bem", "Revise seus estudo de "+foco, disciplina, foco);
    }

    private Desafio DesafioErros(List<Teste_Stat> testStats,String disciplina){
        List<Evolucao> list= new ArrayList<>();
        Set<String> set = new HashSet<>();
        for (Teste_Stat stats : testStats.stream().filter(filt -> filt.disciplina_nome().toLowerCase().contains(disciplina)).toList()) {
            for (ErrosComuns erro : testeService.parseErrosComunsJson(stats.erros_comuns())) {
                list.add(new Evolucao(disciplina, erro.subtopico(),erro.percentualDificuldade() ,stats.criado_em().toLocalDate()));
                set.add(erro.subtopico());
            }

        }
        int maior = 0;
        int aux = 0;
        String subtop="";
        for (String subtopico : set) {
            aux=list.stream().filter(ev -> ev.subtopico.equalsIgnoreCase(subtopico)).toList().size();
            if(aux>maior){
                subtop=subtopico;
                maior=aux;
            }
        }

        return new Desafio(TipoDesafio.RECUPERACAO, "Vejo que estas com dificuldades", "Revise e exercite sua precisao em "+subtop, disciplina, subtop);
    }


    private Desafio DesafioEvolucao(List<Teste_Stat> testStats,String disciplina,String subtop){

        return new Desafio(TipoDesafio.VALIDACAO_PRECISAO, "Vejo que estas com dificuldades", "Revise e exercite sua precisao em "+subtop, disciplina, subtop);
    }

    private Desafio DesafioEstagnacao(List<Teste_Stat> testStats,String disciplina,String subtop){

        return new Desafio(TipoDesafio.RECUPERACAO, "Vejo que estas com dificuldades", "Revise e exercite sua precisao em "+subtop, disciplina, subtop);
    }

    private record  Evolucao(String disciplina, String subtopico,double dificuldade,LocalDate data) {
    }

}