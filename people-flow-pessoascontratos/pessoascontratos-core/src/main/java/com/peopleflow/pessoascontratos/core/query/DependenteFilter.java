package com.peopleflow.pessoascontratos.core.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DependenteFilter {

    private String nome;
    private String parentesco;
    private String cpf;

    public boolean hasAnyCriteria() {
        return (nome != null && !nome.isBlank())
                || (parentesco != null && !parentesco.isBlank())
                || (cpf != null && !cpf.isBlank());
    }
}
