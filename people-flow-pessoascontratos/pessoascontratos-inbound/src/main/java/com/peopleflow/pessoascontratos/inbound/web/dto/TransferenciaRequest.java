package com.peopleflow.pessoascontratos.inbound.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TransferenciaRequest {

    @NotNull(message = "Empresa de destino é obrigatória")
    @Positive(message = "Empresa ID deve ser um número positivo")
    private Long novaEmpresaId;

    @Positive(message = "Departamento ID deve ser um número positivo")
    private Long novoDepartamentoId;

    @Positive(message = "Centro de Custo ID deve ser um número positivo")
    private Long novoCentroCustoId;

    @PastOrPresent(message = "Data de transferência não pode ser futura")
    private LocalDate dataTransferencia;
}
