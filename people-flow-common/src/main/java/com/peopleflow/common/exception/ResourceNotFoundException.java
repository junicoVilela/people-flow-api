package com.peopleflow.common.exception;

public class ResourceNotFoundException extends BusinessException {
    
    public ResourceNotFoundException(String resource, Long id) {
        super("RESOURCE_NOT_FOUND", String.format("%s com ID %s não encontrado", resource, id));
    }
    
    public ResourceNotFoundException(String resource, String identifier) {
        super("RESOURCE_NOT_FOUND", String.format("%s com identificador %s não encontrado", resource, identifier));
    }
}