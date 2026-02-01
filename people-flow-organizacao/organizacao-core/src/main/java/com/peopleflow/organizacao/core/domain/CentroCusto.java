package com.peopleflow.organizacao.core.domain;

import com.peopleflow.common.exception.BusinessException;
import com.peopleflow.organizacao.core.valueobjects.StatusOrganizacao;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CentroCusto {

    private Long id;
    private String nome;
    private String codigo;
    private StatusOrganizacao status;

    public static class CentroCustoBuilder {
        public CentroCusto build() {
            if (status == null) {
                status = StatusOrganizacao.ATIVO;
            }
            CentroCusto centroCusto = new CentroCusto(
                    id,
                    nome,
                    codigo,
                    status
            );

            if (centroCusto.nome != null) {
                centroCusto.validarInvariantes();
            }

            return centroCusto;
        }
    }

    private void validarInvariantes() {
        if (nome == null || nome.trim().isEmpty()) {
            throw new BusinessException("NOME_OBRIGATORIO", "Nome é obrigatório");
        }

        if (codigo == null || codigo.trim().isEmpty()) {
            throw new BusinessException("CODIGO_OBRIGATORIO", "Código é obrigatório");
        }
    }

    public static CentroCusto nova(String nome, String codigo) {
        return CentroCusto.builder()
                .nome(nome)
                .codigo(codigo)
                .build();
    }

    public CentroCusto atualizar(String nome, String codigo) {
        return this.toBuilder()
                .nome(nome)
                .codigo(codigo)
                .build();
    }

    public CentroCusto ativar() {
        if (isExcluido()) {
            throw new BusinessException("CENTRO_CUSTO_EXCLUIDO", "Não é possível ativar centro de custo excluído");
        }
        return this.toBuilder()
                .status(StatusOrganizacao.ATIVO)
                .build();
    }

    public CentroCusto inativar() {
        return this.toBuilder()
                .status(StatusOrganizacao.INATIVO)
                .build();
    }

    public CentroCusto excluir() {
        if (isExcluido()) {
            throw new BusinessException("CENTRO_CUSTO_JA_EXCLUIDO", "Centro de custo já está excluído");
        }
        return this.toBuilder()
                .status(StatusOrganizacao.EXCLUIDO)
                .build();
    }

    public boolean isExcluido() {
        return status != null && status.isExcluido();
    }
}
