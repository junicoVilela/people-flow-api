package com.peopleflow.pessoascontratos.inbound.web.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DemissaoRequest {
    private LocalDate dataDemissao;
}
