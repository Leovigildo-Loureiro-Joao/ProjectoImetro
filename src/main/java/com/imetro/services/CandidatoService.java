package com.imetro.services;

import java.sql.SQLException;

import com.imetro.domain.Candidato;
import com.imetro.domain.dto.candidato.CandidatoRegister;
import com.imetro.domain.interfaces.User;
import com.imetro.persistence.repository.CandidatoRepository;

public class CandidatoService implements User{

    private CandidatoRepository candidatoRepository;
    public CandidatoService(){
        candidatoRepository= new CandidatoRepository(); 
    }

    @Override
    public void Login() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Login'");
    }

    @Override
    public void Logout() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Logout'");
    }

    @Override
    public void RemoverConta() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'RemoverConta'");
    }

    @Override
    public void VerRelatorios() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'VerRelatorios'");
    }

    @Override
    public void VerPerfil() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'VerPerfil'");
    }

    @Override
    public void CriarConta(Object conta) {
        CandidatoRegister candidato= (CandidatoRegister)conta;
        try {
            candidatoRepository.insert(candidato.toMap());

        } catch (SQLException e) {
        }
    }
    
}
