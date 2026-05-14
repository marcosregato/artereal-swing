package com.artereal.swing.domain.caixa;

/**
 * Enum que representa o status de um caixa
 */
public enum StatusCaixa {
    ABERTO("Aberto"),
    FECHADO("Fechado");
    
    private final String descricao;
    
    StatusCaixa(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}
