package com.peopleflow.accesscontrol.inbound.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateRoleRequest {
    
    @NotBlank(message = "Nome da role é obrigatório")
    @Pattern(
        regexp = "^[a-z]+:[a-z]+$",
        message = "Role deve seguir padrão 'resource:action' (ex: colaborador:criar)"
    )
    private String name;
    
    private String description;
}

