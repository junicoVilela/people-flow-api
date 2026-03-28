package com.peopleflow.pessoascontratos.core.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContaBancariaFilter {

    private String banco;
    private String agencia;
    private String conta;

    public boolean hasAnyCriteria() {
        return (banco != null && !banco.isBlank())
                || (agencia != null && !agencia.isBlank())
                || (conta != null && !conta.isBlank());
    }
}
