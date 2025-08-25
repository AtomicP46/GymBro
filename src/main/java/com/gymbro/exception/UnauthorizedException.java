package com.gymbro.exception;

/**
 * Exception lançada quando um usuário tenta acessar um recurso sem autorização adequada.
 * Utilizada em operações que requerem permissões específicas.
 */
public class UnauthorizedException extends RuntimeException {
    
    public UnauthorizedException(String message) {
        super(message);
    }
    
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
