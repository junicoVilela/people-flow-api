package com.peopleflow.organizacao.core.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnidadeFilter {

    private String nome;
    private String codigo;
    private Long empresaId;

    public boolean hasAnyCriteria() {
        return empresaId != null || nome != null || codigo != null;
    }
}
