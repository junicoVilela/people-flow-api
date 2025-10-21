package com.peopleflow.pessoascontratos.inbound.web.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ColaboradorResponse {
    private Long id;
    private Long clienteId;
    private Long empresaId;
    private Long departamentoId;
    private Long centroCustoId;
    private String nome;
    private String cpf;
    private String matricula;
    private String email;
    private LocalDate dataAdmissao;
    private LocalDate dataDemissao;
    private String status;
}