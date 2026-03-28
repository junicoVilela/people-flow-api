package com.peopleflow.pessoascontratos.inbound.web.dto;

import lombok.Data;

@Data
public class ContaBancariaFilterRequest {

    private String banco;
    private String agencia;
    private String conta;

    public boolean hasAnyCriteria() {
        return (banco != null && !banco.isBlank())
                || (agencia != null && !agencia.isBlank())
                || (conta != null && !conta.isBlank());
    }
}
