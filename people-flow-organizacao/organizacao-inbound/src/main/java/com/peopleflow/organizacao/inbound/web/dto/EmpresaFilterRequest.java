package com.peopleflow.organizacao.inbound.web.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmpresaFilterRequest {

    private String nome;
    private String status;
}