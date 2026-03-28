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
public class NivelHierarquico {

    private Long id;
    private String nome;
    private String descricao;
    private Integer ordem;

    public static class NivelHierarquicoBuilder {
        public NivelHierarquico build() {
            NivelHierarquico nivelHierarquico = new NivelHierarquico(
                    id,
                    nome,
                    descricao,
                    ordem
            );

            if (nivelHierarquico.nome != null ||  nivelHierarquico.ordem != null) {
                nivelHierarquico.validarInvariantes();
            }

            return nivelHierarquico;
        }
    }

    public void validarInvariantes() {
        if (nome == null || nome.isBlank()) {
            throw new BusinessException("NIVEL_HIERARQUICO_NOME_OBRIGATORIO", "Nome é obrigatório");
        }

        if (ordem == null) {
            throw new BusinessException("NIVEL_HIERARQUICO_ORDEM_OBRIGATORIO", "Ordem é obrigatória");
        }

    }

    public static NivelHierarquico nova(String nome, String descricao, Integer ordem) {
        return NivelHierarquico.builder()
                .nome(nome)
                .descricao(descricao)
                .ordem(ordem)
                .build();
    }

    public NivelHierarquico atualizar(String nome, String descricao, Integer ordem) {
        return this.toBuilder()
                .nome(nome)
                .descricao(descricao)
                .ordem(ordem)
                .build();
    }
}
