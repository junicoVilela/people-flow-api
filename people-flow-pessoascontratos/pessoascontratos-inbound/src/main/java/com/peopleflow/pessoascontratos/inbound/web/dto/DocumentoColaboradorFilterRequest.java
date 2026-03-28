package com.peopleflow.pessoascontratos.inbound.web.dto;

import lombok.Data;

@Data
public class DocumentoColaboradorFilterRequest {

    private String tipo;
    private String nomeArquivo;

    public boolean hasAnyCriteria() {
        return (tipo != null && !tipo.isBlank()) || (nomeArquivo != null && !nomeArquivo.isBlank());
    }
}
