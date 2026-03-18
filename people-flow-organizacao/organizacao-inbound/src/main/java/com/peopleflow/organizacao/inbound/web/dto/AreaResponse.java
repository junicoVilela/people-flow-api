package com.peopleflow.organizacao.inbound.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AreaResponse {
    private Long id;
    private String codigo;
    private String nome;
    private String descricao;
    private String status;
}
