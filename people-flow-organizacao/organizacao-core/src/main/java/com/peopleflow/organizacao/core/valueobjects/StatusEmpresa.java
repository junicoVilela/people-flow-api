package com.peopleflow.organizacao.core.valueobjects;

import com.peopleflow.common.exception.BusinessException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class StatusEmpresa {
    public static final StatusEmpresa ATIVO = new StatusEmpresa("ativo");
    public static final StatusEmpresa INATIVO = new StatusEmpresa("inativo");
    public static final StatusEmpresa EXCLUIDO = new StatusEmpresa("excluido");

    private final String valor;

    private StatusEmpresa(String status) {
        this.valor = status;
    }

    public static StatusEmpresa of(String status) {
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
