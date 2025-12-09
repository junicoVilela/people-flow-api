package com.peopleflow.common.validation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção lançada quando há violação de regras de acesso
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccessViolationException extends RuntimeException {

    public AccessViolationException(String message) {
        super(message);
    }

    public AccessViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}

