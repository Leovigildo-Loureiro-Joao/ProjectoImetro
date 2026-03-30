package com.imetro.model;

import java.util.ArrayList;
import java.util.UUID;

public class Orientador {
    private UUID idOrientador;
    private String nome;
    private String email;
    private String senha;
    private ArrayList<Candidato> candidatos;
    private ArrayList<Relatorio> relatoriosGeral;
    private ArrayList<Teste> testes;

    

   
    public Orientador() {
        candidatos=new ArrayList<>();
        relatoriosGeral=new ArrayList<>();
        testes=new ArrayList<>();
        idOrientador=UUID.randomUUID();
    }


    public ArrayList<Candidato> getCandidatos() {
        return candidatos;
    }
    public void setCandidatos(ArrayList<Candidato> candidatos) {
        this.candidatos = candidatos;
    }
    public ArrayList<Relatorio> getRelatoriosGeral() {
        return relatoriosGeral;
    }
    public void setRelatoriosGeral(ArrayList<Relatorio> relatoriosGeral) {
        this.relatoriosGeral = relatoriosGeral;
    }
    public ArrayList<Teste> getTestes() {
        return testes;
    }
    public void setTestes(ArrayList<Teste> testes) {
        this.testes = testes;
    }
    public UUID getIdOrientador() {
        return idOrientador;
    }
    public void setIdOrientador(UUID idOrientador) {
        this.idOrientador = idOrientador;
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
   

    

}
