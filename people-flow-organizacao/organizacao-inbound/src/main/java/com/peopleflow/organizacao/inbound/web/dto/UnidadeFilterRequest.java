package com.peopleflow.organizacao.inbound.web.dto;

import lombok.Data;

@Data
public class UnidadeFilterRequest {

    private String nome;
    private String codigo;
    private String status;

    public boolean hasFilters() {
        return nome != null || codigo != null || status != null;
    }
}