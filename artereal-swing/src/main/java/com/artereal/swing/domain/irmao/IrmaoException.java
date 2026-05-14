package com.artereal.swing.domain.irmao;

/**
 * Exceção de domínio para erros relacionados a Irmãos
 */
public class IrmaoException extends RuntimeException {
    
    public IrmaoException(String message) {
        super(message);
    }
    
    public IrmaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
