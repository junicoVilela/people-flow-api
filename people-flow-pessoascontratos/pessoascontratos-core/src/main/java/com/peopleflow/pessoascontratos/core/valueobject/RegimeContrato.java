package com.peopleflow.pessoascontratos.core.valueobject;

import com.peopleflow.common.exception.BusinessException;

/**
 * Valores persistidos em {@code CONTRATO.REGIME} (alinhado ao {@code CHK_CONTRATO_REGIME}).
 */
public enum RegimeContrato {

    INTEGRAL("integral"),
    PARCIAL("parcial"),
    HORISTA("horista");

    private final String valor;

    RegimeContrato(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static RegimeContrato of(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new BusinessException("REGIME_CONTRATO_OBRIGATORIO", "Regime de contrato não pode ser nulo ou vazio");
        }
        String r = valor.trim().toLowerCase();
        for (RegimeContrato regime : values()) {
            if (regime.valor.equals(r)) {
                return regime;
            }
        }
        throw new BusinessException(
                "REGIME_CONTRATO_INVALIDO",
                "Regime inválido: '" + valor + "'. Valores aceitos: integral, parcial, horista");
    }

    public static RegimeContrato ofNullable(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return of(valor);
    }
}
