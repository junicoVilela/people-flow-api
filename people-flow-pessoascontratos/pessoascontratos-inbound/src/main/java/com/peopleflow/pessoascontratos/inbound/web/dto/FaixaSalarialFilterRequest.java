package com.peopleflow.pessoascontratos.inbound.web.dto;

import lombok.Data;

@Data
public class FaixaSalarialFilterRequest {

    private String moeda;

    public boolean hasAnyCriteria() {
        return moeda != null && !moeda.isBlank();
    }
}
