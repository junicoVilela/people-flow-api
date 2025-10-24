package com.peopleflow.pessoascontratos.inbound.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ColaboradorRequest {
    
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}", 
             message = "CPF deve estar no formato 000.000.000-00 ou 00000000000")
    private String cpf;
    
    private String matricula;
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;
    
    private LocalDate dataAdmissao;
    
    @Pattern(regexp = "ativo|inativo|demitido|excluido", 
             message = "Status deve ser: ativo, inativo, demitido ou excluido")
    private String status;
}