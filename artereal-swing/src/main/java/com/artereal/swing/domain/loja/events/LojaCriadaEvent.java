package com.artereal.swing.domain.loja.events;

import com.artereal.swing.domain.events.AbstractDomainEvent;

/**
 * Evento disparado quando uma loja é criada
 */
public class LojaCriadaEvent extends AbstractDomainEvent {
    
    private final String nome;
    private final String cnpj;
    
    public LojaCriadaEvent(Long lojaId, String nome, String cnpj) {
        super(lojaId);
        this.nome = nome;
        this.cnpj = cnpj;
    }
    
    @Override
    public String getEventType() {
        return "LojaCriada";
    }
    
    public String getNome() {
        return nome;
    }
    
    public String getCnpj() {
        return cnpj;
    }
    
    public Long getLojaId() {
        return (Long) getAggregateId();
    }
}
