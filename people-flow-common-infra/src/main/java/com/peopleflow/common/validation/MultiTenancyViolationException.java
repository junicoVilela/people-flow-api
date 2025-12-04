package com.peopleflow.common.validation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção lançada quando há violação de regras de multi-tenancy
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class MultiTenancyViolationException extends RuntimeException {

    public MultiTenancyViolationException(String message) {
        super(message);
    }

    public MultiTenancyViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}

