package com.peopleflow.pessoascontratos.core.domain;

import com.peopleflow.common.exception.BusinessException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Cargo {

    private Long id;
    private String codigo;
    private String nome;
    private String descricao;
    private Long nivelHierarquicoId;
    private Long familiaCargoId;
    private Long departamentoId;

    public static Cargo novo(
            String codigo,
            String nome,
            String descricao,
            Long nivelHierarquicoId,
            Long familiaCargoId,
            Long departamentoId) {
        Cargo c = Cargo.builder()
                .codigo(trimReq(codigo))
                .nome(trimReq(nome))
                .descricao(trimNull(descricao))
                .nivelHierarquicoId(nivelHierarquicoId)
                .familiaCargoId(familiaCargoId)
                .departamentoId(departamentoId)
                .build();
        c.validar();
        return c;
    }

    public Cargo atualizar(
            String codigo,
            String nome,
            String descricao,
            Long nivelHierarquicoId,
            Long familiaCargoId,
            Long departamentoId) {
        Cargo c = this.toBuilder()
                .codigo(trimReq(codigo))
                .nome(trimReq(nome))
                .descricao(trimNull(descricao))
                .nivelHierarquicoId(nivelHierarquicoId)
                .familiaCargoId(familiaCargoId)
                .departamentoId(departamentoId)
                .build();
        c.validar();
        return c;
    }

    public void validar() {
        if (codigo == null || codigo.isBlank()) {
            throw new BusinessException("CARGO_CODIGO_OBRIGATORIO", "Código é obrigatório");
        }
        if (nome == null || nome.isBlank()) {
            throw new BusinessException("CARGO_NOME_OBRIGATORIO", "Nome é obrigatório");
        }
        if (nivelHierarquicoId == null) {
            throw new BusinessException("CARGO_NIVEL_OBRIGATORIO", "Nível hierárquico é obrigatório");
        }
        if (familiaCargoId == null) {
            throw new BusinessException("CARGO_FAMILIA_OBRIGATORIA", "Família de cargo é obrigatória");
        }
    }

    private static String trimReq(String s) {
        return s == null ? null : s.trim();
    }

    private static String trimNull(String s) {
        if (s == null) {
            return null;
        }
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
