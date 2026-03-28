package com.peopleflow.pessoascontratos.inbound.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ContaBancariaRequest {

    @NotBlank
    private String banco;

    @NotBlank
    private String agencia;

    @NotBlank
    private String conta;

    /** corrente, poupanca, salario (opcional) */
    private String tipo;

    private String pix;
}
