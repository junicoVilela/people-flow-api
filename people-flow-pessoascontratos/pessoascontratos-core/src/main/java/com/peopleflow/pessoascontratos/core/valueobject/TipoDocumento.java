package com.peopleflow.pessoascontratos.core.valueobject;

public enum TipoDocumento {

    CONTRATO("contrato"),
    ATESTADO("atestado"),
    EXAME("exame"),
    CERTIDAO("certidao"),
    COMPROVANTE("comprovante"),
    DOCUMENTO("documento"),
    OUTRO("outro");

    private final String valor;

    TipoDocumento(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static TipoDocumento of(String valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Tipo de documento não pode ser nulo");
        }
        for (TipoDocumento tipo : values()) {
            if (tipo.valor.equalsIgnoreCase(valor.trim())) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de documento inválido: '" + valor + "'. " +
                "Valores aceitos: contrato, atestado, exame, certidao, comprovante, documento, outro");
    }
}
