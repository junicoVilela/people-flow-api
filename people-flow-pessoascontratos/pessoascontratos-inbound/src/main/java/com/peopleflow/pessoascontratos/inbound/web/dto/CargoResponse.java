package com.peopleflow.pessoascontratos.inbound.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CargoResponse {
    private Long id;
    private String codigo;
    private String nome;
    private String descricao;
    private Long nivelHierarquicoId;
    private Long familiaCargoId;
    private Long departamentoId;
}
