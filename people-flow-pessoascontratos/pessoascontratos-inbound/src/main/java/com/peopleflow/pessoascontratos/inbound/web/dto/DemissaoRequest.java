package com.peopleflow.pessoascontratos.inbound.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DemissaoRequest {
    
    @NotNull(message = "Data de demissão é obrigatória")
    @PastOrPresent(message = "Data de demissão não pode ser futura")
    private LocalDate dataDemissao;
}
