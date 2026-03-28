package com.peopleflow.pessoascontratos.core.valueobject;

public enum ParentescoDependente {

    CONJUGE("conjuge"),
    FILHO("filho"),
    FILHA("filha"),
    PAI("pai"),
    MAE("mae"),
    OUTRO("outro");

    private final String valor;

    ParentescoDependente(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static ParentescoDependente of(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("Parentesco é obrigatório");
        }
        String v = valor.trim().toLowerCase();
        for (ParentescoDependente p : values()) {
            if (p.valor.equals(v)) {
                return p;
            }
        }
        throw new IllegalArgumentException(
                "Parentesco inválido. Valores: conjuge, filho, filha, pai, mae, outro");
    }
}
