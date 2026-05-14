package com.artereal.swing.application.caixa;

import java.math.BigDecimal;

/**
 * DTO para abertura de um novo caixa
 */
public record AbrirCaixaRequest(
    Long lojaId,
    BigDecimal valor,
    String responsavel
) {
    public AbrirCaixaRequest {
        validate();
    }
    
    private void validate() {
        if (lojaId == null) {
            throw new IllegalArgumentException("ID da loja é obrigatório");
        }
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do caixa deve ser positivo");
        }
        if (responsavel == null || responsavel.trim().isEmpty()) {
            throw new IllegalArgumentException("Responsável pelo caixa é obrigatório");
        }
    }
}
