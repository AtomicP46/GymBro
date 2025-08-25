package com.gymbro.exception;

/**
 * Exception lançada quando uma regra de negócio é violada.
 * Utilizada para validações específicas do domínio da aplicação.
 */
public class BusinessException extends RuntimeException {
    
    public BusinessException(String message) {
        super(message);
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
