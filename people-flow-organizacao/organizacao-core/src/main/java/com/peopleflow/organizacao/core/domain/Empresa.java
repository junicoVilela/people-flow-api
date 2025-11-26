package com.peopleflow.organizacao.core.domain;

import com.peopleflow.common.exception.BusinessException;
import com.peopleflow.common.valueobject.Cnpj;
import com.peopleflow.common.valueobject.InscricaoEstadual;
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
    private Cnpj cnpj;
    private InscricaoEstadual inscricaoEstadual;
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

    public Empresa atualizar(String nome, Cnpj cnpj, InscricaoEstadual inscricaoEstadual, Long clienteId) {
        return this.toBuilder()
                .nome(nome)
                .cnpj(cnpj)
                .inscricaoEstadual(inscricaoEstadual)
                .clienteId(clienteId)
                .build();
    }

    public Empresa ativar() {
        if (isExcluido()) {
            throw new BusinessException("EMPRESA_EXCLUIDO", "Não é possível ativar empresa excluído");
        }

        return this.toBuilder()
                .status(StatusEmpresa.ATIVO)
                .build();
    }

    public Empresa inativar() {
        return this.toBuilder()
                .status(StatusEmpresa.INATIVO)
                .build();
    }

    public Empresa excluir() {
        if (isExcluido()) {
            throw new BusinessException("EMPRESA_JA_EXCLUIDA", "Empresa já está excluída");
        }

        return this.toBuilder()
                .status(StatusEmpresa.EXCLUIDO)
                .build();
    }

    public boolean isExcluido() {
        return status.isExcluido();
    }

    public static Empresa nova(
            String nome,
            String cnpjString,
            String inscricaoEstadualString,
            Long clienteId) {
        
        // Validar e criar Value Objects (lança exceção se inválido)
        Cnpj cnpj = new Cnpj(cnpjString);
        InscricaoEstadual inscricaoEstadual = InscricaoEstadual.of(inscricaoEstadualString);
        
        // Construir empresa com status inicial ATIVO
        return Empresa.builder()
            .nome(nome)
            .cnpj(cnpj)
            .inscricaoEstadual(inscricaoEstadual)
            .status(StatusEmpresa.ATIVO)
            .clienteId(clienteId)
            .build();
    }
}
