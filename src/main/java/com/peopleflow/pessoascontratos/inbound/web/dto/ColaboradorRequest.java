package com.peopleflow.pessoascontratos.inbound.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ColaboradorRequest {
    
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    @NotBlank(message = "CPF é obrigatório")
    private String cpf;
    
    private String matricula;
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;
    
    private LocalDate dataAdmissao;
    
    // Status é opcional - se não informado, será definido como ATIVO pelo mapper
    private String status;
}