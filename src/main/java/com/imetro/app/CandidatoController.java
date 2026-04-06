package com.imetro.app;

import com.imetro.domain.dto.candidato.CandidatoRegister;
import com.imetro.services.CandidatoService;

public class CandidatoController {
    private CandidatoService candidatoService;

    public CandidatoController() {
        candidatoService=new CandidatoService();
    }

    public void RegistrarCandidato(CandidatoRegister candidatoRegister){
        
        candidatoService.CriarConta(candidatoRegister);
        System.out.println("Conta criada");
    }

}
