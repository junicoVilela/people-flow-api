package com.peopleflow.pessoascontratos.inbound.web.dto;

import lombok.Data;

@Data
public class ContratoFilterRequest {

    private String tipo;
    private String regime;

    public boolean hasAnyCriteria() {
        return (tipo != null && !tipo.isBlank()) || (regime != null && !regime.isBlank());
    }
}
