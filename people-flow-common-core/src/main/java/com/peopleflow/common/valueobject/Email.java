package com.peopleflow.common.valueobject;

import com.peopleflow.common.exception.BusinessException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.regex.Pattern;

/**
 * Value Object para Email
 * 
 * Encapsula validação e formatação de email.
 * Este é um value object genérico que pode ser reutilizado em qualquer módulo.
 */
@Getter
@EqualsAndHashCode
@ToString
public class Email {
    private final String valor;
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

    public Email(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new BusinessException("EMAIL_OBRIGATORIO", "Email é obrigatório");
        }
        
        String emailLimpo = email.trim().toLowerCase();
        
        if (!isValidEmail(emailLimpo)) {
            throw new BusinessException("EMAIL_INVALIDO", "Email inválido: " + email);
        }
        
        this.valor = emailLimpo;
    }

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}

