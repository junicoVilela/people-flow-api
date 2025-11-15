package com.peopleflow.organizacao.core.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Empresa {

    private Long id;
    private String nome;
    private String cnpj;
    private String inscricaoEstadual;
    private Long clienteId;
}
