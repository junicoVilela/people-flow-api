package com.peopleflow.pessoascontratos.core.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Objeto de consulta/filtro para buscar Colaboradores
 * 
 * Este não é um objeto de domínio, mas sim um DTO para queries.
 * Usado para construir filtros dinâmicos em pesquisas.
 */
@Data
@Builder(toBuilder = true)
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

