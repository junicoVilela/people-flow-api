package com.peopleflow.pessoascontratos.inbound.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO de entrada para criação/atualização de Colaborador
 * 
 * Validações nesta camada (Apresentação):
 * - Formato básico dos campos (@Pattern, @Size)
 * - Presença de campos obrigatórios (@NotBlank)
 * - Validações simples e rápidas (feedback imediato ao usuário)
 * 
 * Validações no Domínio (Value Objects):
 * - Algoritmo de validação de CPF
 * - Regras complexas de negócio
 * - Garantia de invariantes
 */
@Data
public class ColaboradorRequest {
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;
    
    @NotBlank(message = "CPF é obrigatório")
    @Pattern(
        regexp = "^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$",
        message = "CPF deve estar no formato XXX.XXX.XXX-XX ou apenas números (11 dígitos)"
    )
    private String cpf;
    
    @Size(max = 20, message = "Matrícula deve ter no máximo 20 caracteres")
    private String matricula;
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ter formato válido (exemplo@dominio.com)")
    @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
    private String email;
    
    @PastOrPresent(message = "Data de admissão não pode ser futura")
    private LocalDate dataAdmissao;
    
    // Status é opcional - se não informado, será definido como ATIVO pelo domínio
    @Pattern(
        regexp = "^(ativo|inativo|demitido|excluido)?$",
        message = "Status deve ser: ativo, inativo, demitido ou excluido"
    )
    private String status;
}