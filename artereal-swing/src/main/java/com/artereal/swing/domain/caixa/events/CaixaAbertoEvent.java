package com.artereal.swing.domain.caixa.events;

import com.artereal.swing.domain.events.AbstractDomainEvent;
import java.math.BigDecimal;

/**
 * Evento disparado quando um caixa é aberto
 */
public class CaixaAbertoEvent extends AbstractDomainEvent {
    
    private final Long lojaId;
    private final BigDecimal valor;
    private final String responsavel;
    
    public CaixaAbertoEvent(Long caixaId, Long lojaId, BigDecimal valor, String responsavel) {
        super(caixaId);
        this.lojaId = lojaId;
        this.valor = valor;
        this.responsavel = responsavel;
    }
    
    @Override
    public String getEventType() {
        return "CaixaAberto";
    }
    
    public Long getLojaId() {
        return lojaId;
    }
    
    public BigDecimal getValor() {
        return valor;
    }
    
    public String getResponsavel() {
        return responsavel;
    }
    
    public Long getCaixaId() {
        return (Long) getAggregateId();
    }
}
