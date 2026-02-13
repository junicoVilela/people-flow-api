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
public class Departamento {

    private Long id;
    private String nome;
    private String codigo;
    private Long empresaId;
    private Long unidadeId;
    private StatusOrganizacao status;

    public static class DepartamentoBuilder {
        public Departamento build() {
            if (status == null) {
                status = StatusOrganizacao.ATIVO;
            }
            Departamento departamento = new Departamento(
                    id,
                    nome,
                    codigo,
                    empresaId,
                    unidadeId,
                    status
            );

            if (departamento.nome != null) {
                departamento.validarInvariantes();
            }

            return departamento;
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

        if (unidadeId == null) {
            throw new BusinessException("UNIDADE_ID_OBRIGATORIO",
                    "Unidade ID é obrigatória");
        }
    }

    public static Departamento nova(
            String nome,
            String codigo,
            Long empresaId,
            Long unidadeId,
            StatusOrganizacao status) {

        return Departamento.builder()
                .nome(nome)
                .codigo(codigo)
                .empresaId(empresaId)
                .unidadeId(unidadeId)
                .status(status != null ? status : StatusOrganizacao.ATIVO)
                .build();
    }

    public Departamento atualizar(
            String nome,
            String codigo,
            Long empresaId,
            Long unidadeId,
            StatusOrganizacao status) {

        return this.toBuilder()
                .nome(nome)
                .codigo(codigo)
                .empresaId(empresaId)
                .unidadeId(unidadeId)
                .status(status)
                .build();
    }

    public Departamento ativar() {
        if (isExcluido()) {
            throw new BusinessException("DEPARTAMENTO_EXCLUIDO", "Não é possível ativar departamento excluído");
        }
        return this.toBuilder()
                .status(StatusOrganizacao.ATIVO)
                .build();
    }

    public Departamento inativar() {
        return this.toBuilder()
                .status(StatusOrganizacao.INATIVO)
                .build();
    }

    public Departamento excluir() {
        if (isExcluido()) {
            throw new BusinessException("DEPARTAMENTO_JA_EXCLUIDO", "Departamento já está excluído");
        }
        return this.toBuilder()
                .status(StatusOrganizacao.EXCLUIDO)
                .build();
    }

    public boolean isExcluido() {
        return status != null && status.isExcluido();
    }
}
