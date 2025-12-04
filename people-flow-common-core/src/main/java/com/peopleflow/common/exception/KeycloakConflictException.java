package com.peopleflow.common.exception;

/**
 * Exceção lançada quando há conflito no Keycloak (409) - recurso já existe
 */
public class KeycloakConflictException extends KeycloakException {
    
    public KeycloakConflictException(String message) {
        super(message);
    }
    
    public KeycloakConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}

