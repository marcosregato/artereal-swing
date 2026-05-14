package com.artereal.swing.application.caixa;

import java.math.BigDecimal;

/**
 * DTO para resposta de operações com caixas
 */
public record CaixaResponse(
    Long id,
    Long lojaId,
    BigDecimal valor,
    String responsavel,
    String status,
    BigDecimal saldo,
    String dataAbertura,
    String dataFechamento
) {
    public boolean isAberto() {
        return "ABERTO".equals(status);
    }
    
    public boolean isFechado() {
        return "FECHADO".equals(status);
    }
}
