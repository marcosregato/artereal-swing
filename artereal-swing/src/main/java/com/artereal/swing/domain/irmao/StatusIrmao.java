package com.artereal.swing.domain.irmao;

/**
 * Enum que representa o status de um irmão
 */
public enum StatusIrmao {
    ATIVO("Ativo"),
    INATIVO("Inativo"),
    SUSPENSO("Suspenso");
    
    private final String descricao;
    
    StatusIrmao(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}
