package com.peopleflow.pessoascontratos.inbound.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ColaboradorFilterRequest {
    
    @Size(min = 2, max = 200, message = "Nome deve ter entre 2 e 200 caracteres")
    private String nome;
    
    @Pattern(regexp = "^[0-9]{3}\\.?[0-9]{3}\\.?[0-9]{3}-?[0-9]{2}$", 
             message = "CPF deve estar no formato válido")
    private String cpf;
    
    @jakarta.validation.constraints.Email(message = "Email deve ter formato válido")
    @Size(max = 200, message = "Email deve ter no máximo 200 caracteres")
    private String email;
    
    @Size(max = 50, message = "Matrícula deve ter no máximo 50 caracteres")
    private String matricula;
    
    @Pattern(regexp = "^(ativo|inativo|demitido|excluido)$", 
             message = "Status deve ser: ativo, inativo, demitido ou excluido",
             flags = Pattern.Flag.CASE_INSENSITIVE)
    private String status;
    
    @Min(value = 1, message = "Cliente ID deve ser positivo")
    private Long clienteId;
    
    @Min(value = 1, message = "Empresa ID deve ser positivo")
    private Long empresaId;
    
    @Min(value = 1, message = "Departamento ID deve ser positivo")
    private Long departamentoId;
    
    @Min(value = 1, message = "Centro de Custo ID deve ser positivo")
    private Long centroCustoId;
    
    private LocalDate dataAdmissaoInicio;
    private LocalDate dataAdmissaoFim;
    private LocalDate dataDemissaoInicio;
    private LocalDate dataDemissaoFim;

}