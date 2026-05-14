package com.artereal.swing.domain.events;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Publisher para eventos de domínio
 * 
 * Esta classe gerencia a publicação e subscrição de eventos
 * de domínio seguindo o padrão Observer.
 */
public class DomainEventPublisher {
    
    private static DomainEventPublisher instance;
    private final List<DomainEventHandler> handlers;
    
    private DomainEventPublisher() {
        this.handlers = new CopyOnWriteArrayList<>();
    }
    
    public static synchronized DomainEventPublisher getInstance() {
        if (instance == null) {
            instance = new DomainEventPublisher();
        }
        return instance;
    }
    
    /**
     * Registra um handler para eventos de domínio
     */
    public void registerHandler(DomainEventHandler handler) {
        handlers.add(handler);
    }
    
    /**
     * Remove um handler de eventos de domínio
     */
    public void unregisterHandler(DomainEventHandler handler) {
        handlers.remove(handler);
    }
    
    /**
     * Publica um evento de domínio para todos os handlers registrados
     */
    public void publish(DomainEvent event) {
        for (DomainEventHandler handler : handlers) {
            try {
                handler.handle(event);
            } catch (Exception e) {
                // Log error but continue with other handlers
                System.err.println("Erro ao processar evento: " + e.getMessage());
            }
        }
    }
    
    /**
     * Publica múltiplos eventos de domínio
     */
    public void publishAll(List<DomainEvent> events) {
        for (DomainEvent event : events) {
            publish(event);
        }
    }
    
    /**
     * Limpa todos os handlers registrados
     */
    public void clearHandlers() {
        handlers.clear();
    }
}
