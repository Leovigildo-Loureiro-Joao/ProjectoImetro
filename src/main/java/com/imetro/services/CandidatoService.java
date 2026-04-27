package com.imetro.services;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.imetro.domain.dto.candidato.UserRegister;
import com.imetro.domain.dto.progresso.ProgressoAlunoDisciplinaDto;
import com.imetro.domain.enums.NivelDisciplina;
import com.imetro.domain.interfaces.User;
import com.imetro.persistence.repository.ProgressoALunoDisciplinaRepository;
import com.imetro.persistence.repository.ProgressoALunoDisciplinaRepository;
import com.imetro.persistence.repository.UserRepository;

public class CandidatoService implements User{

    private final UserRepository userRepository;
    private  ProgressoALunoDisciplinaRepository progresso;
    public CandidatoService(){
        userRepository = new UserRepository();
        progresso=new ProgressoALunoDisciplinaRepository();
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

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public ProgressoALunoDisciplinaRepository getProgresso() {
        return progresso;
    }

    public void setProgresso(ProgressoALunoDisciplinaRepository progresso) {
        this.progresso = progresso;
    }

    public void AddFirstProgressoDisciplina(UUID candidato,UUID disicplina,NivelDisciplina actual,double peso){
        try {
            progresso.insert(new ProgressoAlunoDisciplinaDto(UUID.randomUUID(), candidato, disicplina, actual, actual, LocalDate.now(), peso,0, 0, 0, 0.0, null, null, LocalDateTime.now(), 0, 0, LocalDateTime.now(), LocalDateTime.now()).toMap());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    
    
}

