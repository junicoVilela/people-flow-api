package com.peopleflow.pessoascontratos.inbound.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ColaboradorFilterRequest {

    private String nome;
    private String cpf;
    private String email;
    private String matricula;
    private String status;

    @Schema(description = "Filtra por empresa. Administradores podem informar qualquer ID; " +
                          "usuários comuns têm este campo sobrescrito pelo ID da empresa do token JWT.")
    private Long empresaId;

    private Long departamentoId;
    private Long centroCustoId;
    private LocalDate dataAdmissaoInicio;
    private LocalDate dataAdmissaoFim;
    private LocalDate dataDemissaoInicio;
    private LocalDate dataDemissaoFim;
}