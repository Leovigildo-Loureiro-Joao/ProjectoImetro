package com.imetro.domain.dto.test;

import com.imetro.domain.enums.TrilhoStatus;

public class TrilhoDTO {

    private int etapa;
    private TrilhaAdaptacaoSubtopico trilho;
    private TrilhoStatus status;




    public TrilhoDTO(int etapa, TrilhaAdaptacaoSubtopico trilho) {
        this.etapa = etapa;

        this.trilho = trilho;
    }
    public int getEtapa() {
        return etapa;
    }
    public void setEtapa(int etapa) {
        this.etapa = etapa;
    }

    public TrilhaAdaptacaoSubtopico getTrilho() {
        return trilho;
    }
    public void setTrilho(TrilhaAdaptacaoSubtopico trilho) {
        this.trilho = trilho;
    }
    public TrilhoStatus getStatus() {
        return status;
    }
    public void setStatus(TrilhoStatus status) {
        this.status = status;
    }




}
