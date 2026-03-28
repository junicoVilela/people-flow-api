package com.peopleflow.pessoascontratos.inbound.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CargoRequest {

    @NotBlank
    @Size(max = 50)
    private String codigo;

    @NotBlank
    @Size(max = 150)
    private String nome;

    @Size(max = 500)
    private String descricao;

    @NotNull
    private Long nivelHierarquicoId;

    @NotNull
    private Long familiaCargoId;

    private Long departamentoId;
}
