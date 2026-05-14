package com.artereal.swing.domain.irmao.events;

import com.artereal.swing.domain.events.AbstractDomainEvent;

/**
 * Evento disparado quando um irmão é associado a uma loja
 */
public class IrmaoAssociadoEvent extends AbstractDomainEvent {
    
    private final Long lojaId;
    private final String nomeIrmao;
    
    public IrmaoAssociadoEvent(Long irmaoId, Long lojaId, String nomeIrmao) {
        super(irmaoId);
        this.lojaId = lojaId;
        this.nomeIrmao = nomeIrmao;
    }
    
    @Override
    public String getEventType() {
        return "IrmaoAssociado";
    }
    
    public Long getLojaId() {
        return lojaId;
    }
    
    public String getNomeIrmao() {
        return nomeIrmao;
    }
    
    public Long getIrmaoId() {
        return (Long) getAggregateId();
    }
}
