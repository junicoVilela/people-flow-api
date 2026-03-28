package com.peopleflow.pessoascontratos.inbound.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class JornadaTrabalhoRequest {

    @NotBlank(message = "descrição é obrigatória")
    private String descricao;

    /** Carga horária semanal (0 a 60). Opcional. */
    private BigDecimal cargaSemanalHoras;
}
