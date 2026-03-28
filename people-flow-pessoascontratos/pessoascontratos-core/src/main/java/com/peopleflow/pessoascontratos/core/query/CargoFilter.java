package com.peopleflow.pessoascontratos.core.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CargoFilter {

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
