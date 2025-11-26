package com.peopleflow.organizacao.core.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaFilter {
    
    private String nome;
    private String status;

    public boolean hasAnyCriteria() {
        return nome != null || status != null;
    }
}
