package com.peopleflow.organizacao.inbound.web.dto;

import lombok.Data;

@Data
public class EmpresaFilterRequest {

    private String nome;
    private String status;

    public boolean hasAnyCriteria() {
        return (nome != null && !nome.isBlank()) || (status != null && !status.isBlank());
    }
}