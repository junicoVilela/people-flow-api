package com.peopleflow.pessoascontratos.inbound.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaixaSalarialResponse {
    private Long id;
    private Long cargoId;
    private BigDecimal faixaMin;
    private BigDecimal faixaMax;
    private String moeda;
}
