package com.peopleflow.pessoascontratos.core.query;

import lombok.Data;

import java.time.LocalDate;

@Data
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

