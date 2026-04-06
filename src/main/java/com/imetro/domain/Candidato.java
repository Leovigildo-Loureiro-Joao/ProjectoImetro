package com.imetro.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class Candidato {
    
    private UUID idCandidato;
    private String nome;
    private String email;
    private String senha;
    private LocalDateTime criado_em;
    private ArrayList<Disciplina> disciplinas;
    private ArrayList<Teste> testes;
    private ArrayList<Relatorio> relatorios;

  
    public Candidato() {
        disciplinas=new ArrayList<>();
        testes=new ArrayList<>();
        relatorios=new ArrayList<>();
        idCandidato=UUID.randomUUID();
        criado_em = LocalDateTime.now();
    }


    public UUID getIdCandidato() {
        return idCandidato;
    }
    public void setIdCandidato(UUID idCandidato) {
        this.idCandidato = idCandidato;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public ArrayList<Disciplina> getDisciplinas() {
        return disciplinas;
    }
    public void setDisciplinas(ArrayList<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }
    public ArrayList<Teste> getTestes() {
        return testes;
    }
    public void setTestes(ArrayList<Teste> testes) {
        this.testes = testes;
    }
    public ArrayList<Relatorio> getRelatorios() {
        return relatorios;
    }
    public void setRelatorios(ArrayList<Relatorio> relatorios) {
        this.relatorios = relatorios;
    }
    
    public Map<String,?> toMap(){
        return Map.of("id",idCandidato,"nome",nome,"email",email,"senha_hash",senha,"criado_em",criado_em);
    }


    @Override
    public String toString() {
        return "Candidato [idCandidato=" + idCandidato + ", nome=" + nome + ", email=" + email + ", senha=" + senha
                + ", disciplinas=" + disciplinas + ", testes=" + testes + ", relatorios=" + relatorios + "]";
    }

    

    
}
