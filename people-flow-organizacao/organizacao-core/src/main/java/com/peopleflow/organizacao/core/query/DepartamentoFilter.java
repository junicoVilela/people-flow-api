package com.peopleflow.organizacao.core.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartamentoFilter {
    
    private String nome;
    private String codigo;

    public boolean hasAnyCriteria() {
        return nome != null || codigo != null;
    }
}
