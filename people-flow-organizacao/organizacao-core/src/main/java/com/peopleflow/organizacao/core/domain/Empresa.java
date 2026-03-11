package com.peopleflow.organizacao.core.domain;

import com.peopleflow.common.exception.BusinessException;
import com.peopleflow.common.valueobject.Cnpj;
import com.peopleflow.common.valueobject.InscricaoEstadual;
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
public class Empresa {

    private Long id;
    private String nome;
    private Cnpj cnpj;
    private InscricaoEstadual inscricaoEstadual;
    private StatusOrganizacao status;

    public static class EmpresaBuilder {
        public Empresa build() {
            if (status == null) {
                status = StatusOrganizacao.ATIVO;
            }

            Empresa empresa = new Empresa(
                    id,
                    nome,
                    cnpj,
                    inscricaoEstadual,
                    status
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
            throw new BusinessException("CNPJ_OBRIGATORIO", "CNPJ é obrigatório");
        }
    }

    public static Empresa nova(
            String nome,
            String cnpjString,
            String inscricaoEstadualString) {

        Cnpj cnpj = new Cnpj(cnpjString);
        InscricaoEstadual inscricaoEstadual = InscricaoEstadual.of(inscricaoEstadualString);

        return Empresa.builder()
                .nome(nome)
                .cnpj(cnpj)
                .inscricaoEstadual(inscricaoEstadual)
                .build();
    }

    public Empresa atualizar(String nome, Cnpj cnpj, InscricaoEstadual inscricaoEstadual) {
        return this.toBuilder()
                .nome(nome)
                .cnpj(cnpj)
                .inscricaoEstadual(inscricaoEstadual)
                .build();
    }

    public Empresa ativar() {
        if (isExcluido()) {
            throw new BusinessException("EMPRESA_EXCLUIDA", "Não é possível ativar empresa excluída");
        }

        return this.toBuilder()
                .status(StatusOrganizacao.ATIVO)
                .build();
    }

    public Empresa inativar() {
        return this.toBuilder()
                .status(StatusOrganizacao.INATIVO)
                .build();
    }

    public Empresa excluir() {
        if (isExcluido()) {
            throw new BusinessException("EMPRESA_JA_EXCLUIDA", "Empresa já está excluída");
        }

        return this.toBuilder()
                .status(StatusOrganizacao.EXCLUIDO)
                .build();
    }

    public boolean isExcluido() {
        return status.isExcluido();
    }
}
