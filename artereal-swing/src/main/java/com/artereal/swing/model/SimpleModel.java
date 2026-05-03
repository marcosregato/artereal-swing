package com.artereal.swing.model;

import java.util.Objects;

/**
 * Versão simplificada do BaseModel com implementação mínima e robusta.
 * Foco em simplicidade e redução de erros.
 */
public abstract class SimpleModel {
    
    /**
     * equals simplificado - apenas compara IDs
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SimpleModel other = (SimpleModel) obj;
        return Objects.equals(getId(), other.getId());
    }
    
    /**
     * hashCode simplificado - baseado apenas no ID
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
    
    /**
     * toString simplificado - apenas classe e ID
     */
    @Override
    public String toString() {
        return String.format("%s{id=%d}", getClass().getSimpleName(), getId());
    }
    
    public abstract Long getId();
    public abstract void setId(Long id);
}
