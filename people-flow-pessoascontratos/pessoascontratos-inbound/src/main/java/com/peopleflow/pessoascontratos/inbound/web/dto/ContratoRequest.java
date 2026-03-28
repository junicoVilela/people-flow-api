package com.peopleflow.pessoascontratos.inbound.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "Dados do contrato de trabalho")
public class ContratoRequest {

    @NotNull(message = "jornadaId é obrigatório")
    private Long jornadaId;

    @NotNull(message = "cargoId é obrigatório")
    private Long cargoId;

    @Schema(description = "Tipo: CLT, PJ, estagio, temporario, intermitente", example = "CLT")
    private String tipo;

    @Schema(description = "Regime: integral, parcial, horista", example = "integral")
    private String regime;

    @Schema(description = "Salário base (opcional)")
    private BigDecimal salarioBase;

    @NotNull(message = "inicio é obrigatório")
    private LocalDate inicio;

    private LocalDate fim;
}
