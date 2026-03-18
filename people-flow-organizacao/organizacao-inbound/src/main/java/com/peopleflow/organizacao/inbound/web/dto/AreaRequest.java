package com.peopleflow.organizacao.inbound.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AreaRequest {

    @NotBlank(message = "Código é obrigatório")
    @Size(min = 2, max = 50, message = "Código deve ter entre 2 e 50 caracteres")
    private String codigo;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 150, message = "Nome deve ter entre 3 e 150 caracteres")
    private String nome;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String descricao;
}
