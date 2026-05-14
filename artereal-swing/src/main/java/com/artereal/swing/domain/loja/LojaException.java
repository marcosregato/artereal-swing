package com.artereal.swing.domain.loja;

/**
 * Exceção de domínio para erros relacionados à Loja
 */
public class LojaException extends RuntimeException {
    
    public LojaException(String message) {
        super(message);
    }
    
    public LojaException(String message, Throwable cause) {
        super(message, cause);
    }
}
