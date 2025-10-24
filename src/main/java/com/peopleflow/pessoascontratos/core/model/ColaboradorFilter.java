package com.peopleflow.pessoascontratos.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColaboradorFilter {
    private String nome;
    private String cpf;
    private String email;
    private String matricula;
    private String status;
    private Long clienteId;
    private Long empresaId;
    private Long departamentoId;
    private Long centroCustoId;
    private LocalDate dataAdmissaoInicio;
    private LocalDate dataAdmissaoFim;
    private LocalDate dataDemissaoInicio;
    private LocalDate dataDemissaoFim;
}
