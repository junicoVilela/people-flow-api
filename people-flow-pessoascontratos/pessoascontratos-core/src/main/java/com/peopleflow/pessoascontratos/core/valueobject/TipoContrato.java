package com.peopleflow.pessoascontratos.core.valueobject;

import com.peopleflow.common.exception.BusinessException;

/**
 * Valores persistidos em {@code CONTRATO.TIPO} (alinhado ao {@code CHK_CONTRATO_TIPO}).
 */
public enum TipoContrato {

    CLT("CLT"),
    PJ("PJ"),
    ESTAGIO("estagio"),
    TEMPORARIO("temporario"),
    INTERMITENTE("intermitente");

    private final String valor;

    TipoContrato(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static TipoContrato of(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new BusinessException("TIPO_CONTRATO_OBRIGATORIO", "Tipo de contrato não pode ser nulo ou vazio");
        }
        String t = valor.trim();
        for (TipoContrato tipo : values()) {
            if (tipo.valor.equalsIgnoreCase(t)) {
                return tipo;
            }
        }
        throw new BusinessException(
                "TIPO_CONTRATO_INVALIDO",
                "Tipo de contrato inválido: '" + valor + "'. Valores aceitos: CLT, PJ, estagio, temporario, intermitente");
    }

    /**
     * Interpreta texto da API ou null; string em branco resulta em {@code null} (campo opcional no contrato).
     */
    public static TipoContrato ofNullable(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return of(valor);
    }
}
