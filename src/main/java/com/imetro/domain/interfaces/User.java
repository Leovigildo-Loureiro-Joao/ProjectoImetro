package com.imetro.domain.interfaces;

import com.imetro.domain.dto.candidato.UserRegister;

public interface User {
    
    public void Login();

    public boolean CriarConta(UserRegister conta);

    public void Logout();

    public void RemoverConta();

    public void VerRelatorios();

    public void VerPerfil();

}
