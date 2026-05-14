package com.artereal.swing.domain.loja.events;

import com.artereal.swing.domain.events.AbstractDomainEvent;

/**
 * Evento disparado quando uma loja é inativada
 */
public class LojaInativadaEvent extends AbstractDomainEvent {
    
    public LojaInativadaEvent(Long lojaId) {
        super(lojaId);
    }
    
    @Override
    public String getEventType() {
        return "LojaInativada";
    }
    
    public Long getLojaId() {
        return (Long) getAggregateId();
    }
}
