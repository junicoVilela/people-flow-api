package com.peopleflow.organizacao.inbound.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmpresaRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotBlank(message = "CNPJ é obrigatório")
    @Pattern(
            regexp = "^\\d{2}\\.?\\d{3}\\.?\\d{3}/?\\d{4}-?\\d{2}$",
            message = "CNPJ deve estar no formato XX.XXX.XXX/XXXX-XX ou apenas números (14 dígitos)"
    )
    private String cnpj;

    private String inscricaoEstadual;

    @Pattern(
            regexp = "^(ativo|inativo|excluido)?$",
            message = "Status deve ser: ativo, inativo ou excluido"
    )
    private String status;

    @NotNull(message = "Cliente ID é obrigatório")
    private Long clienteId;
}