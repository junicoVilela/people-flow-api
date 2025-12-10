package com.peopleflow.common.validation;

/**
 * Exceção lançada quando há violação de regras de acesso
 */
public class AccessViolationException extends RuntimeException {

    public AccessViolationException(String message) {
        super(message);
    }

    public AccessViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
