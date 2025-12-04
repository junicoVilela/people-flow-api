package com.peopleflow.common.exception;

/**
 * Exceção base para erros relacionados ao Keycloak
 */
public class KeycloakException extends RuntimeException {
    
    public KeycloakException(String message) {
        super(message);
    }
    
    public KeycloakException(String message, Throwable cause) {
        super(message, cause);
    }
}

