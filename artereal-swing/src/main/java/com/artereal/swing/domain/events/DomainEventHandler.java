package com.artereal.swing.domain.events;

/**
 * Interface para handlers de eventos de domínio
 */
@FunctionalInterface
public interface DomainEventHandler {
    
    /**
     * Processa um evento de domínio
     */
    void handle(DomainEvent event);
}
