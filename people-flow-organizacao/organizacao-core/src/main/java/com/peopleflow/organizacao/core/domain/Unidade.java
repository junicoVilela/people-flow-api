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
public class Unidade {

    private Long id;
    private String nome;
    private String codigo;
    private Long empresaId;
    private StatusOrganizacao status;

    public static class UnidadeBuilder {
        public Unidade build() {
            if (status == null) {
                status = StatusOrganizacao.ATIVO;
            }
            Unidade unidade = new Unidade(
                    id,
                    nome,
                    codigo,
                    empresaId,
                    status
            );

            if (unidade.nome != null) {
                unidade.validarInvariantes();
            }

            return unidade;
        }
    }

    private void validarInvariantes() {
        if (nome == null || nome.trim().isEmpty()) {
            throw new BusinessException("NOME_OBRIGATORIO", "Nome é obrigatório");
        }

        if (codigo == null || codigo.trim().isEmpty()) {
            throw new BusinessException("CODIGO_OBRIGATORIO", "Código é obrigatório");
        }

        if (empresaId == null) {
            throw new BusinessException("EMPRESA_ID_OBRIGATORIO",
                    "Empresa ID é obrigatória");
        }
    }

    public static Unidade nova(
            String nome,
            String codigo,
            Long empresaId,
            StatusOrganizacao status) {

        return Unidade.builder()
                .nome(nome)
                .codigo(codigo)
                .empresaId(empresaId)
                .status(status != null ? status : StatusOrganizacao.ATIVO)
                .build();
    }

    public Unidade atualizar(String nome, String codigo, Long empresaId) {
        return this.toBuilder()
                .nome(nome)
                .codigo(codigo)
                .empresaId(empresaId)
                .build();
    }

    public Unidade ativar() {
        if (isExcluido()) {
            throw new BusinessException("UNIDADE_EXCLUIDA", "Não é possível ativar unidade excluída");
        }
        return this.toBuilder()
                .status(StatusOrganizacao.ATIVO)
                .build();
    }

    public Unidade inativar() {
        return this.toBuilder()
                .status(StatusOrganizacao.INATIVO)
                .build();
    }

    public Unidade excluir() {
        if (isExcluido()) {
            throw new BusinessException("UNIDADE_JA_EXCLUIDA", "Unidade já está excluída");
        }
        return this.toBuilder()
                .status(StatusOrganizacao.EXCLUIDO)
                .build();
    }

    public boolean isExcluido() {
        return status.isExcluido();
    }
}
