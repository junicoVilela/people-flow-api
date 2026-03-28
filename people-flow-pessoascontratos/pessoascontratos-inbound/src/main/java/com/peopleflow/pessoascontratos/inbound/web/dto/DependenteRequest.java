package com.peopleflow.pessoascontratos.inbound.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DependenteRequest {

    @NotBlank
    @Size(max = 500)
    private String nome;

    /** conjuge, filho, filha, pai, mae, outro */
    @NotNull
    private String parentesco;

    private LocalDate dataNascimento;

    /** Formato 999.999.999-99 (14 caracteres) ou omitir */
    @Size(max = 14)
    private String cpf;
}
