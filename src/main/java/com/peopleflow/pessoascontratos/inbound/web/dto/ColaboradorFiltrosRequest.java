package com.peopleflow.pessoascontratos.inbound.web.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class ColaboradorFiltrosRequest {
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