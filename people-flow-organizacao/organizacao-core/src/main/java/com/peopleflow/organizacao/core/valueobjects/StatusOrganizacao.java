package com.peopleflow.organizacao.core.valueobjects;

import com.peopleflow.common.exception.BusinessException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Status de entidades da organização (Empresa, Unidade, Departamento, CentroCusto).
 * Valores: ativo, inativo, excluido.
 */
@Getter
@EqualsAndHashCode
@ToString
public class StatusOrganizacao {
    public static final StatusOrganizacao ATIVO = new StatusOrganizacao("ativo");
    public static final StatusOrganizacao INATIVO = new StatusOrganizacao("inativo");
    public static final StatusOrganizacao EXCLUIDO = new StatusOrganizacao("excluido");

    private final String valor;

    private StatusOrganizacao(String status) {
        this.valor = status;
    }

    public static StatusOrganizacao of(String status) {
        if (status == null || status.trim().isEmpty()) {
            return ATIVO;
        }

        String statusNormalizado = status.trim().toLowerCase();

        return switch (statusNormalizado) {
            case "ativo" -> ATIVO;
            case "inativo" -> INATIVO;
            case "excluido" -> EXCLUIDO;
            default -> throw new BusinessException("STATUS_INVALIDO",
                    "Status inválido: " + status + ". Status válidos: ativo, inativo, excluido");
        };
    }

    public boolean isAtivo() {
        return this.equals(ATIVO);
    }

    public boolean isExcluido() {
        return this.equals(EXCLUIDO);
    }
}
