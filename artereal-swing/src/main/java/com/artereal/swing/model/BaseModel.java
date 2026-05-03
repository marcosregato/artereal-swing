package com.artereal.swing.model;

import java.util.Objects;

/**
 * Classe base abstrata para modelos com implementações padrão de equals, hashCode e toString.
 * Fornece uma abordagem consistente para todas as classes de modelo do sistema.
 */
public abstract class BaseModel {
    
    /**
     * Implementação padrão de equals baseada no ID.
     * Subclasses podem sobrescrever para incluir outros campos relevantes.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        BaseModel other = (BaseModel) obj;
        return Objects.equals(getId(), other.getId());
    }
    
    /**
     * Implementação padrão de hashCode baseada no ID.
     * Subclasses podem sobrescrever para incluir outros campos relevantes.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
    
    /**
     * Implementação padrão de toString incluindo nome da classe e ID.
     * Subclasses devem sobrescrever para incluir campos específicos.
     */
    @Override
    public String toString() {
        return String.format("%s{id=%d}", getClass().getSimpleName(), getId());
    }
    
    /**
     * Método abstrato para obter o ID do modelo.
     * Todas as subclasses devem implementar este método.
     */
    public abstract Long getId();
    
    /**
     * Método abstrato para definir o ID do modelo.
     * Todas as subclasses devem implementar este método.
     */
    public abstract void setId(Long id);
}
