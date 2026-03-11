package com.peopleflow.pessoascontratos.inbound.web.dto;

import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReativacaoRequest {

    @PastOrPresent(message = "Nova data de admissão não pode ser futura")
    private LocalDate novaDataAdmissao;
}
