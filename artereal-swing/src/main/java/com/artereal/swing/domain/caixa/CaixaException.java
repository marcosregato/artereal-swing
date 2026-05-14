package com.artereal.swing.domain.caixa;

/**
 * Exceção de domínio para erros relacionados a Caixas
 */
public class CaixaException extends RuntimeException {
    
    public CaixaException(String message) {
        super(message);
    }
    
    public CaixaException(String message, Throwable cause) {
        super(message, cause);
    }
}
