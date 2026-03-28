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
public class FamiliaCargo {

    private Long id;
    private String nome;
    private String descricao;

    public static FamiliaCargo nova(String nome, String descricao) {
        FamiliaCargo f = FamiliaCargo.builder()
                .nome(trimToNull(nome))
                .descricao(trimEmptyToNull(descricao))
                .build();
        f.validar();
        return f;
    }

    public FamiliaCargo atualizar(String nome, String descricao) {
        FamiliaCargo f = this.toBuilder()
                .nome(trimToNull(nome))
                .descricao(trimEmptyToNull(descricao))
                .build();
        f.validar();
        return f;
    }

    public void validar() {
        if (nome == null || nome.isBlank()) {
            throw new BusinessException("FAMILIA_CARGO_NOME_OBRIGATORIO", "Nome é obrigatório");
        }
    }

    private static String trimToNull(String s) {
        if (s == null) {
            return null;
        }
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private static String trimEmptyToNull(String s) {
        if (s == null) {
            return null;
        }
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
