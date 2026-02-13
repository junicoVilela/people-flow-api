package com.peopleflow.organizacao.inbound.web.dto;

import lombok.Data;

@Data
public class EmpresaFilterRequest {

    private String nome;
    private String status;
}