package com.peopleflow.pessoascontratos.core.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContratoFilter {

    private String tipo;
    private String regime;

    public boolean hasAnyCriteria() {
        return (tipo != null && !tipo.isBlank()) || (regime != null && !regime.isBlank());
    }
}
