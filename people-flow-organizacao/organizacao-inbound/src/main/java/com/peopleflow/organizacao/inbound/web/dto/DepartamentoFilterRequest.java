package com.peopleflow.organizacao.inbound.web.dto;

import lombok.Data;

@Data
public class DepartamentoFilterRequest {

    private Long empresaId;
    private Long unidadeId;
    private String nome;
    private String codigo;
    private String status;

    public boolean hasAnyCriteria() {
        return empresaId != null
                || unidadeId != null
                || (nome != null && !nome.isBlank())
                || (codigo != null && !codigo.isBlank())
                || (status != null && !status.isBlank());
    }
}