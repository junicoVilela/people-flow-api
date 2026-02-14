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
    private String status;
    private Long empresaId;
    private Long unidadeId;

    public boolean hasAnyCriteria() {
        return empresaId != null || unidadeId != null || nome != null || codigo != null || status != null;
    }
}
