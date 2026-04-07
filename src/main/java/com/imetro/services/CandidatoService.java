package com.imetro.services;

import java.sql.SQLException;

import com.imetro.domain.dto.candidato.UserRegister;
import com.imetro.domain.interfaces.User;
import com.imetro.persistence.repository.UserRepository;

public class CandidatoService implements User{

    private final UserRepository userRepository;
    public CandidatoService(){
        userRepository = new UserRepository();
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
    public boolean CriarConta(UserRegister conta) {
        try {
            if (conta == null || !conta.ValidateData()) {
                return false;
            }
            if (!"CANDIDATO".equalsIgnoreCase(conta.role())) {
                return false;
            }

            userRepository.insert(conta.toMap());
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    
}

