package com.peopleflow.pessoascontratos.inbound.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

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

    @Pattern(
        regexp = "^(ativo|inativo|demitido|excluido)?$",
        message = "Status deve ser: ativo, inativo, demitido ou excluido"
    )
    private String status;

    @NotNull(message = "Cliente ID é obrigatório")
    private Long clienteId;
    
    @NotNull(message = "Empresa ID é obrigatória")
    private Long empresaId;
    
    @Positive(message = "Cargo ID deve ser um número positivo")
    private Long cargoId;
    
    @Positive(message = "Departamento ID deve ser um número positivo")
    private Long departamentoId;
    
    @Positive(message = "Centro de Custo ID deve ser um número positivo")
    private Long centroCustoId;
    
    /**
     * Indica se o colaborador requer acesso ao sistema (criação de usuário no Keycloak)
     * Default: false (não cria usuário automaticamente)
     */
    private Boolean requerAcessoSistema = false;
}