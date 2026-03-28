package com.peopleflow.organizacao.inbound.web.dto;

import lombok.Data;

@Data
public class AreaFilterRequest {

    private String nome;
    private String codigo;
    private String status;

    public boolean hasAnyCriteria() {
        return (nome != null && !nome.isBlank())
                || (codigo != null && !codigo.isBlank())
                || (status != null && !status.isBlank());
    }
}
