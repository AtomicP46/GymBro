package com.gymbro.exception;

/**
 * Exception lançada quando um recurso solicitado não é encontrado no sistema.
 * Utilizada principalmente em operações de busca por ID ou outros identificadores únicos.
 */
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s não encontrado com %s: '%s'", resourceName, fieldName, fieldValue));
    }
}
