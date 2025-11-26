package com.peopleflow.common.valueobject;

import com.peopleflow.common.exception.BusinessException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class Cpf {
    private final String valor;

    public Cpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new BusinessException("CPF_OBRIGATORIO", "CPF é obrigatório");
        }
        
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        
        if (!isValidCpf(cpfLimpo)) {
            throw new BusinessException("CPF_INVALIDO", "CPF inválido: " + cpf);
        }
        
        this.valor = formatarCpf(cpfLimpo);
    }

    public String getValorNumerico() {
        return valor.replaceAll("[^0-9]", "");
    }

    private boolean isValidCpf(String cpf) {
        if (cpf.length() != 11) {
            return false;
        }

        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int primeiroDigito = 11 - (soma % 11);
        if (primeiroDigito >= 10) {
            primeiroDigito = 0;
        }

        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        int segundoDigito = 11 - (soma % 11);
        if (segundoDigito >= 10) {
            segundoDigito = 0;
        }

        return Character.getNumericValue(cpf.charAt(9)) == primeiroDigito &&
               Character.getNumericValue(cpf.charAt(10)) == segundoDigito;
    }

    private String formatarCpf(String cpf) {
        return cpf.substring(0, 3) + "." +
               cpf.substring(3, 6) + "." +
               cpf.substring(6, 9) + "-" +
               cpf.substring(9, 11);
    }
}

