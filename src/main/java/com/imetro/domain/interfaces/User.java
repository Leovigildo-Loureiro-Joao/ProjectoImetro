package com.imetro.domain.interfaces;

public interface User {
    
    public void Login();

    public void CriarConta(Object conta);

    public void Logout();

    public void RemoverConta();

    public void VerRelatorios();

    public void VerPerfil();

}
