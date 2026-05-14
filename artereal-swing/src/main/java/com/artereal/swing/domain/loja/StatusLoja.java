package com.artereal.swing.domain.loja;

/**
 * Enum que representa o status de uma loja
 */
public enum StatusLoja {
    ATIVA("Ativa"),
    INATIVA("Inativa"),
    BLOQUEADA("Bloqueada");
    
    private final String descricao;
    
    StatusLoja(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}
