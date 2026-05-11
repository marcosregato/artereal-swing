package com.artereal.swing.patterns.observer;

/**
 * Interface Observer para padrão Observer
 * Permite que objetos sejam notificados sobre mudanças em dados
 */
@FunctionalInterface
public interface DataObserver {
    
    /**
     * Método chamado quando os dados observados sofrem alterações
     * 
     * @param source Objeto que originou a notificação
     * @param data Dados alterados (pode ser null para apenas notificação de evento)
     */
    void update(Object source, Object data);
    
    /**
     * Método chamado quando ocorre erro na operação observada
     * 
     * @param source Objeto que originou o erro
     * @param error Exceção ocorrida
     */
    default void onError(Object source, Throwable error) {
        // Implementação padrão vazia - opcional para observers
    }
    
    /**
     * Método chamado quando a operação observada é concluída
     * 
     * @param source Objeto que concluiu a operação
     * @param success true se operação foi bem-sucedida
     */
    default void onComplete(Object source, boolean success) {
        // Implementação padrão vazia - opcional para observers
    }
}
