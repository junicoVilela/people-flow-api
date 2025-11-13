package com.peopleflow.common.exception;

public class DuplicateResourceException extends BusinessException {
    
    public DuplicateResourceException(String field, String value) {
        super("DUPLICATE_" + field.toUpperCase(), 
              String.format("%s '%s' já está cadastrado no sistema", field, value));
    }
}

