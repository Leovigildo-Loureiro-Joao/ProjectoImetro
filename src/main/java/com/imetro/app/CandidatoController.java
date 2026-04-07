package com.imetro.app;

import com.imetro.domain.dto.candidato.CandidatoRegister;
import com.imetro.domain.dto.candidato.UserRegister;
import com.imetro.services.CandidatoService;

public class CandidatoController {
    private CandidatoService candidatoService;

    public CandidatoController() {
        candidatoService=new CandidatoService();
    }

    public boolean RegistrarCandidato(UserRegister candidatoRegister){
        
        return candidatoService.CriarConta(candidatoRegister);
    }

}
