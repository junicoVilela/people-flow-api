package com.peopleflow.pessoascontratos.core.valueobject;

import com.peopleflow.common.exception.BusinessException;

public enum TipoContaBancaria {

    CORRENTE("corrente"),
    POUPANCA("poupanca"),
    SALARIO("salario");

    private final String valor;

    TipoContaBancaria(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static TipoContaBancaria of(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new BusinessException("TIPO_CONTA_OBRIGATORIO", "Tipo de conta é obrigatório");
        }
        String v = valor.trim().toLowerCase();
        for (TipoContaBancaria t : values()) {
            if (t.valor.equals(v)) {
                return t;
            }
        }
        throw new BusinessException("TIPO_CONTA_INVALIDO", "Tipo inválido. Valores: corrente, poupanca, salario");
    }

    public static TipoContaBancaria ofNullable(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return of(valor);
    }
}
