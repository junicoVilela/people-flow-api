package com.peopleflow.pessoascontratos.inbound.web.dto;

import lombok.Data;

@Data
public class DependenteFilterRequest {

    private String nome;
    private String parentesco;
    private String cpf;

    public boolean hasAnyCriteria() {
        return (nome != null && !nome.isBlank())
                || (parentesco != null && !parentesco.isBlank())
                || (cpf != null && !cpf.isBlank());
    }
}
