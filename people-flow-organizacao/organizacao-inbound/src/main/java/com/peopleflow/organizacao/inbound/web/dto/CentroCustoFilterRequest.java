package com.peopleflow.organizacao.inbound.web.dto;

import lombok.Data;

@Data
public class CentroCustoFilterRequest {

    private String nome;
    private String codigo;
    private String status;
    private Long empresaId;

    public boolean hasFilters() {
        return empresaId != null || nome != null || codigo != null || status != null;
    }
}