package com.peopleflow.common.exception;

import java.util.UUID;

public class ResourceNotFoundException extends BusinessException {
    
    public ResourceNotFoundException(String resource, UUID id) {
        super("RESOURCE_NOT_FOUND", String.format("%s com ID %s não encontrado", resource, id));
    }
    
    public ResourceNotFoundException(String resource, String identifier) {
        super("RESOURCE_NOT_FOUND", String.format("%s com identificador %s não encontrado", resource, identifier));
    }
}