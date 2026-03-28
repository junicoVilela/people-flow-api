package com.peopleflow.pessoascontratos.core.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaixaSalarialFilter {

    private String moeda;

    public boolean hasAnyCriteria() {
        return moeda != null && !moeda.isBlank();
    }
}
