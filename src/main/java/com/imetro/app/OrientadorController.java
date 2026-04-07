package com.imetro.app;

import com.imetro.domain.dto.candidato.UserRegister;
import com.imetro.services.OrientadorService;

public class OrientadorController {

    private final OrientadorService orientadorService;

    public OrientadorController() {
        this.orientadorService = new OrientadorService();
    }

    public boolean RegistrarOrientador(UserRegister orientadorRegister) {
        return orientadorService.CriarConta(orientadorRegister);
    }
}

