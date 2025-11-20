package com.peopleflow.common.valueobject;

import com.peopleflow.common.exception.BusinessException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Value Object para CNPJ (Cadastro Nacional de Pessoa Jurídica)
 * 
 * Encapsula validação e formatação de CNPJ brasileiro.
 * Este é um value object genérico que pode ser reutilizado em qualquer módulo.
 */
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

    public String getValorNumerico() {
        return valor.replaceAll("[^0-9]", "");
    }

    private boolean isValidCnpj(String cnpj) {
        if (cnpj.length() != 14) {
            return false;
        }

        // Verifica se todos os dígitos são iguais (ex: 00000000000000)
        if (cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        // Valida primeiro dígito verificador
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

        // Valida segundo dígito verificador
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
        return cnpj.substring(0, 2) + "." +
               cnpj.substring(2, 5) + "." +
               cnpj.substring(5, 8) + "/" +
               cnpj.substring(8, 12) + "-" +
               cnpj.substring(12, 14);
    }
}

