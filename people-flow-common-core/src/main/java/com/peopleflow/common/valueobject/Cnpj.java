package com.peopleflow.common.valueobject;

import com.peopleflow.common.exception.BusinessException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class Cnpj {
    private final String valor;

    public Cnpj(String cnpj) {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            throw new BusinessException("CNPJ_OBRIGATORIO", "CNPJ é obrigatório");
        }
        String cnpjLimpo = cnpj.replaceAll("[^0-9]", "");
        if (!isValidCnpj(cnpjLimpo)) {
            throw new BusinessException("CNPJ_INVALIDO", "CNPJ inválido: " + cnpj);
        }
        this.valor = formatarCnpj(cnpjLimpo);
    }

    private static final Object STORAGE = new Object();

    /**
     * Cria CNPJ a partir de valor já persistido (ex.: leitura do banco, listagem).
     * Não valida dígitos verificadores — use apenas em fluxos de leitura (GET/list).
     */
    public static Cnpj fromStorage(String cnpj) {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            return null;
        }
        String cnpjLimpo = cnpj.replaceAll("[^0-9]", "");
        if (cnpjLimpo.length() == 14) {
            return new Cnpj(formatarCnpjStatic(cnpjLimpo), STORAGE);
        }
        return new Cnpj(cnpj.trim(), STORAGE);
    }

    private Cnpj(String valorJaFormatadoOuBruto, Object storageMarker) {
        this.valor = valorJaFormatadoOuBruto;
    }

    public String getValorNumerico() {
        return valor.replaceAll("[^0-9]", "");
    }

    private boolean isValidCnpj(String cnpj) {
        if (cnpj.length() != 14) {
            return false;
        }

        if (cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        int soma = 0;
        int peso = 5;
        for (int i = 0; i < 12; i++) {
            soma += Character.getNumericValue(cnpj.charAt(i)) * peso;
            peso = (peso == 2) ? 9 : peso - 1;
        }
        int primeiroDigito = 11 - (soma % 11);
        if (primeiroDigito >= 10) {
            primeiroDigito = 0;
        }

        if (Character.getNumericValue(cnpj.charAt(12)) != primeiroDigito) {
            return false;
        }

        soma = 0;
        peso = 6;
        for (int i = 0; i < 13; i++) {
            soma += Character.getNumericValue(cnpj.charAt(i)) * peso;
            peso = (peso == 2) ? 9 : peso - 1;
        }
        int segundoDigito = 11 - (soma % 11);
        if (segundoDigito >= 10) {
            segundoDigito = 0;
        }

        return Character.getNumericValue(cnpj.charAt(13)) == segundoDigito;
    }

    private String formatarCnpj(String cnpj) {
        return formatarCnpjStatic(cnpj);
    }

    private static String formatarCnpjStatic(String cnpj) {
        return cnpj.substring(0, 2) + "." +
               cnpj.substring(2, 5) + "." +
               cnpj.substring(5, 8) + "/" +
               cnpj.substring(8, 12) + "-" +
               cnpj.substring(12, 14);
    }
}

