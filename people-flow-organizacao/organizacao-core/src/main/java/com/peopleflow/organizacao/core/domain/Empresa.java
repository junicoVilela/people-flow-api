package com.peopleflow.organizacao.core.domain;

import com.peopleflow.common.exception.BusinessException;
import com.peopleflow.organizacao.core.valueobjects.StatusEmpresa;
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
    private StatusEmpresa status;
    private Long clienteId;

    public static class EmpresaBuilder {
        public Empresa build() {
            if (status == null) {
                status = StatusEmpresa.ATIVO;
            }

            Empresa empresa = new Empresa(
                    id,
                    nome,
                    cnpj,
                    inscricaoEstadual,
                    status,
                    clienteId
            );

            if (empresa.nome != null) {
                empresa.validarInvariantes();
            }

            return empresa;
        }
    }

    private void validarInvariantes() {
        if (nome == null || nome.trim().isEmpty()) {
            throw new BusinessException("NOME_OBRIGATORIO", "Nome é obrigatório");
        }

        if (cnpj == null) {
            throw new BusinessException("CNPJ_OBRIGATORIO", "CPF é obrigatório");
        }

        if (clienteId == null) {
            throw new BusinessException("CLIENTE_ID_OBRIGATORIO",
                    "Cliente ID é obrigatório");
        }

    }

    public Empresa ativar() {
        if (isExcluido()) {
            throw new BusinessException("EMPRESA_EXCLUIDA",
                    "Não é possível ativar empresa excluída");
        }

        return this.toBuilder()
                .status(StatusEmpresa.ATIVO)
                .build();
    }

    public Empresa inativar() {
        if (isExcluido()) {
            throw new BusinessException("EMPRESA_EXCLUIDA",
                    "Não é possível inativar empresa excluída");
        }

        return this.toBuilder()
                .status(StatusEmpresa.INATIVO)
                .build();
    }

    public Empresa excluir() {
        if (isExcluido()) {
            throw new BusinessException("EMPRESA_JA_EXCLUIDA",
                    "Empresa já está excluída");
        }

        return this.toBuilder()
                .status(StatusEmpresa.EXCLUIDO)
                .build();
    }

    public boolean isExcluido() {
        return status.isExcluido();
    }
}
