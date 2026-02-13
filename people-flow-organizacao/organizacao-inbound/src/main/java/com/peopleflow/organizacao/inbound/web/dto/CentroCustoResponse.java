package com.peopleflow.organizacao.inbound.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CentroCustoResponse {
    private Long id;
    private String nome;
    private String codigo;
    private Long empresaId;
    private String status;
}