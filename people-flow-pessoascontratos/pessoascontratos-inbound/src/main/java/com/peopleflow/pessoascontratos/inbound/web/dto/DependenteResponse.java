package com.peopleflow.pessoascontratos.inbound.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DependenteResponse {
    private Long id;
    private Long colaboradorId;
    private String nome;
    private String parentesco;
    private LocalDate dataNascimento;
    private String cpf;
}
