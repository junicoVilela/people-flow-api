package com.peopleflow.accesscontrol.inbound.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateGrupoRequest {
    
    @NotBlank(message = "Nome do grupo é obrigatório")
    private String name;
}

