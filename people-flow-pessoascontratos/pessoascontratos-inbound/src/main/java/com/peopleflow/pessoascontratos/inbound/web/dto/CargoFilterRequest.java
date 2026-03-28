package com.peopleflow.pessoascontratos.inbound.web.dto;

import lombok.Data;

@Data
public class CargoFilterRequest {

    private String nome;
    private String codigo;
    private Long nivelHierarquicoId;
    private Long familiaCargoId;
    private Long departamentoId;

    public boolean hasAnyCriteria() {
        return (nome != null && !nome.isBlank())
                || (codigo != null && !codigo.isBlank())
                || nivelHierarquicoId != null
                || familiaCargoId != null
                || departamentoId != null;
    }
}
