package com.peopleflow.pessoascontratos.inbound.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContratoResponse {

    private Long id;
    private Long colaboradorId;
    private Long jornadaId;
    private Long cargoId;
    private String tipo;
    private String regime;
    private BigDecimal salarioBase;
    private LocalDate inicio;
    private LocalDate fim;
}
