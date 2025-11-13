package com.peopleflow.pessoascontratos.core.valueobject;

import com.peopleflow.common.exception.BusinessException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@EqualsAndHashCode
@ToString
public class StatusColaborador {
    public static final StatusColaborador ATIVO = new StatusColaborador("ativo");
    public static final StatusColaborador INATIVO = new StatusColaborador("inativo");
    public static final StatusColaborador DEMITIDO = new StatusColaborador("demitido");
    public static final StatusColaborador EXCLUIDO = new StatusColaborador("excluido");

    private final String valor;

    private StatusColaborador(String status) {
        this.valor = status;
    }

    public static StatusColaborador of(String status) {
        if (status == null || status.trim().isEmpty()) {
            return ATIVO;
        }

        String statusNormalizado = status.trim().toLowerCase();
        
        return switch (statusNormalizado) {
            case "ativo" -> ATIVO;
            case "inativo" -> INATIVO;
            case "demitido" -> DEMITIDO;
            case "excluido" -> EXCLUIDO;
            default -> throw new BusinessException("STATUS_INVALIDO", 
                "Status inválido: " + status + ". Status válidos: ativo, inativo, demitido, excluido");
        };
    }

    public boolean isAtivo() {
        return this.equals(ATIVO);
    }

    public boolean isDemitido() {
        return this.equals(DEMITIDO);
    }

    public boolean isExcluido() {
        return this.equals(EXCLUIDO);
    }

}