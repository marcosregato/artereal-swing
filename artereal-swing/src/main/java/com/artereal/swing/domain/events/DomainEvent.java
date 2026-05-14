package com.artereal.swing.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Interface base para eventos de domínio
 */
public interface DomainEvent {
    
    /**
     * Retorna o ID único do evento
     */
    UUID getEventId();
    
    /**
     * Retorna o timestamp de quando o evento ocorreu
     */
    LocalDateTime getOccurredOn();
    
    /**
     * Retorna o tipo do evento
     */
    String getEventType();
    
    /**
     * Retorna o aggregate root ID relacionado ao evento
     */
    Object getAggregateId();
}
