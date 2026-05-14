package com.artereal.swing.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Classe base abstrata para eventos de domínio
 */
public abstract class AbstractDomainEvent implements DomainEvent {
    
    private final UUID eventId;
    private final LocalDateTime occurredOn;
    private final Object aggregateId;
    
    protected AbstractDomainEvent(Object aggregateId) {
        this.eventId = UUID.randomUUID();
        this.occurredOn = LocalDateTime.now();
        this.aggregateId = aggregateId;
    }
    
    @Override
    public UUID getEventId() {
        return eventId;
    }
    
    @Override
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
    
    @Override
    public Object getAggregateId() {
        return aggregateId;
    }
}
