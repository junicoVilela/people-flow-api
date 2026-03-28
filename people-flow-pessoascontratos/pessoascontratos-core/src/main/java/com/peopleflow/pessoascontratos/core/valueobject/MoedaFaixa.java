package com.peopleflow.pessoascontratos.core.valueobject;

import com.peopleflow.common.exception.BusinessException;

public enum MoedaFaixa {

    BRL("BRL"),
    USD("USD"),
    EUR("EUR");

    private final String valor;

    MoedaFaixa(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static MoedaFaixa of(String valor) {
        if (valor == null || valor.isBlank()) {
            return BRL;
        }
        String v = valor.trim().toUpperCase();
        for (MoedaFaixa m : values()) {
            if (m.valor.equals(v)) {
                return m;
            }
        }
        throw new BusinessException("MOEDA_INVALIDA", "Moeda inválida. Valores: BRL, USD, EUR");
    }
}
