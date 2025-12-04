package com.peopleflow.common.exception;

/**
 * Exceção lançada quando um recurso não é encontrado no Keycloak (404)
 */
public class KeycloakNotFoundException extends KeycloakException {
    
    public KeycloakNotFoundException(String message) {
        super(message);
    }
    
    public KeycloakNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

