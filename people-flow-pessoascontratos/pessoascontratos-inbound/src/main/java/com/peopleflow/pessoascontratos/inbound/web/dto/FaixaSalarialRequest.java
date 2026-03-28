package com.peopleflow.pessoascontratos.inbound.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FaixaSalarialRequest {

    @NotNull
    private BigDecimal faixaMin;

    @NotNull
    private BigDecimal faixaMax;

    /** BRL (padrão), USD ou EUR */
    private String moeda;
}
