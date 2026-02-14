package com.peopleflow.organizacao.inbound.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CentroCustoRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Código é obrigatório")
    @Size(min = 3, max = 100, message = "Código deve ter entre 3 e 100 caracteres")
    private String codigo;

    @Pattern(
            regexp = "^(ativo|inativo|excluido)?$",
            message = "Status deve ser: ativo, inativo ou excluido"
    )
    private String status;

    @NotNull(message = "Empresa ID é obrigatório")
    private Long empresaId;
}