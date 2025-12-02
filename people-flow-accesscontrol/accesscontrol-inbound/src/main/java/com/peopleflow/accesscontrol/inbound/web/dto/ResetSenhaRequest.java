package com.peopleflow.accesscontrol.inbound.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetSenhaRequest {
    
    @NotBlank(message = "Nova senha é obrigatória")
    private String novaSenha;
    
    private boolean temporaria = false;
}

