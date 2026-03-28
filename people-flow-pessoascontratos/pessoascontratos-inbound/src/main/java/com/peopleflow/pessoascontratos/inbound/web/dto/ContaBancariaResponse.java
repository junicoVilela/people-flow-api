package com.peopleflow.pessoascontratos.inbound.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContaBancariaResponse {
    private Long id;
    private Long colaboradorId;
    private String banco;
    private String agencia;
    private String conta;
    private String tipo;
    private String pix;
}
