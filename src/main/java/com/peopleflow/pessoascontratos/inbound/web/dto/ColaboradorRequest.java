package com.peopleflow.pessoascontratos.inbound.web.dto;

import com.peopleflow.pessoascontratos.core.valueobject.Cpf;
import com.peopleflow.pessoascontratos.core.valueobject.StatusColaborador;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ColaboradorRequest {
    
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF deve estar no formato 000.000.000-00")
    private Cpf cpf;
    
    private String matricula;
    
    @Email(message = "Email deve ter um formato válido")
    private com.peopleflow.pessoascontratos.core.valueobject.Email email;
    
    private LocalDate dataAdmissao;
    
    private StatusColaborador status;
}