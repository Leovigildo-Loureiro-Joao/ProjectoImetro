package com.imetro.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Relatorio {

    private final UUID id;

    private UUID candidatoId;
    private UUID orientadorId;

    private LocalDateTime geradoEm;
    private String titulo;
    private String resumo;

    private Double tempoMedioSegundos;
    private String dificuldadeAtingida;

    private Map<String, Double> taxaAcertoPorTopico;
    private List<EvolucaoSemanalItem> evolucaoSemanal;
    private List<ErroRecorrente> errosRecorrentes;

    private List<String> skillsBoas;
    private List<String> skillsFracas;

    private List<String> recomendacoesSugeridas;
    private List<String> recomendacoesValidadas;

    /**
     * Campo opcional para observações do orientador.
     * O candidato pode ver o relatório sem este campo, caso a UI decida ocultar.
     */
    private String notaOrientador;

    public Relatorio() {
        this.id = UUID.randomUUID();
        this.geradoEm = LocalDateTime.now();
        this.taxaAcertoPorTopico = new LinkedHashMap<>();
        this.evolucaoSemanal = new ArrayList<>();
        this.errosRecorrentes = new ArrayList<>();
        this.skillsBoas = new ArrayList<>();
        this.skillsFracas = new ArrayList<>();
        this.recomendacoesSugeridas = new ArrayList<>();
        this.recomendacoesValidadas = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public UUID getCandidatoId() {
        return candidatoId;
    }

    public void setCandidatoId(UUID candidatoId) {
        this.candidatoId = candidatoId;
    }

    public UUID getOrientadorId() {
        return orientadorId;
    }

    public void setOrientadorId(UUID orientadorId) {
        this.orientadorId = orientadorId;
    }

    public LocalDateTime getGeradoEm() {
        return geradoEm;
    }

    public void setGeradoEm(LocalDateTime geradoEm) {
        this.geradoEm = geradoEm;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public Double getTempoMedioSegundos() {
        return tempoMedioSegundos;
    }

    public void setTempoMedioSegundos(Double tempoMedioSegundos) {
        this.tempoMedioSegundos = tempoMedioSegundos;
    }

    public String getDificuldadeAtingida() {
        return dificuldadeAtingida;
    }

    public void setDificuldadeAtingida(String dificuldadeAtingida) {
        this.dificuldadeAtingida = dificuldadeAtingida;
    }

    public Map<String, Double> getTaxaAcertoPorTopico() {
        return taxaAcertoPorTopico;
    }

    public void setTaxaAcertoPorTopico(Map<String, Double> taxaAcertoPorTopico) {
        this.taxaAcertoPorTopico = taxaAcertoPorTopico;
    }

    public List<EvolucaoSemanalItem> getEvolucaoSemanal() {
        return evolucaoSemanal;
    }

    public void setEvolucaoSemanal(List<EvolucaoSemanalItem> evolucaoSemanal) {
        this.evolucaoSemanal = evolucaoSemanal;
    }

    public List<ErroRecorrente> getErrosRecorrentes() {
        return errosRecorrentes;
    }

    public void setErrosRecorrentes(List<ErroRecorrente> errosRecorrentes) {
        this.errosRecorrentes = errosRecorrentes;
    }

    public List<String> getSkillsBoas() {
        return skillsBoas;
    }

    public void setSkillsBoas(List<String> skillsBoas) {
        this.skillsBoas = skillsBoas;
    }

    public List<String> getSkillsFracas() {
        return skillsFracas;
    }

    public void setSkillsFracas(List<String> skillsFracas) {
        this.skillsFracas = skillsFracas;
    }

    public List<String> getRecomendacoesSugeridas() {
        return recomendacoesSugeridas;
    }

    public void setRecomendacoesSugeridas(List<String> recomendacoesSugeridas) {
        this.recomendacoesSugeridas = recomendacoesSugeridas;
    }

    public List<String> getRecomendacoesValidadas() {
        return recomendacoesValidadas;
    }

    public void setRecomendacoesValidadas(List<String> recomendacoesValidadas) {
        this.recomendacoesValidadas = recomendacoesValidadas;
    }

    public String getNotaOrientador() {
        return notaOrientador;
    }

    public void setNotaOrientador(String notaOrientador) {
        this.notaOrientador = notaOrientador;
    }

    public static class EvolucaoSemanalItem {
        private LocalDate semanaInicio;
        private Double taxaAcerto;
        private Double tempoMedioSegundos;

        public EvolucaoSemanalItem() {
        }

        public EvolucaoSemanalItem(LocalDate semanaInicio, Double taxaAcerto, Double tempoMedioSegundos) {
            this.semanaInicio = semanaInicio;
            this.taxaAcerto = taxaAcerto;
            this.tempoMedioSegundos = tempoMedioSegundos;
        }

        public LocalDate getSemanaInicio() {
            return semanaInicio;
        }

        public void setSemanaInicio(LocalDate semanaInicio) {
            this.semanaInicio = semanaInicio;
        }

        public Double getTaxaAcerto() {
            return taxaAcerto;
        }

        public void setTaxaAcerto(Double taxaAcerto) {
            this.taxaAcerto = taxaAcerto;
        }

        public Double getTempoMedioSegundos() {
            return tempoMedioSegundos;
        }

        public void setTempoMedioSegundos(Double tempoMedioSegundos) {
            this.tempoMedioSegundos = tempoMedioSegundos;
        }
    }

    public static class ErroRecorrente {
        private String topico;
        private String descricao;
        private Integer ocorrencias;

        public ErroRecorrente() {
        }

        public ErroRecorrente(String topico, String descricao, Integer ocorrencias) {
            this.topico = topico;
            this.descricao = descricao;
            this.ocorrencias = ocorrencias;
        }

        public String getTopico() {
            return topico;
        }

        public void setTopico(String topico) {
            this.topico = topico;
        }

        public String getDescricao() {
            return descricao;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }

        public Integer getOcorrencias() {
            return ocorrencias;
        }

        public void setOcorrencias(Integer ocorrencias) {
            this.ocorrencias = ocorrencias;
        }
    }
}
