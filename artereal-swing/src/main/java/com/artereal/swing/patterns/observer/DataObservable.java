package com.artereal.swing.patterns.observer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Classe base para implementação do padrão Observer
 * Gerencia notificações para múltiplos observers de forma thread-safe
 */
public class DataObservable {
    
    private final List<DataObserver> observers;
    private final Object source;
    
    /**
     * Construtor
     * 
     * @param source Objeto que será a fonte das notificações
     */
    public DataObservable(Object source) {
        this.source = source;
        this.observers = new CopyOnWriteArrayList<>();
    }
    
    /**
     * Adiciona um observer para receber notificações
     * 
     * @param observer Observer a ser adicionado
     */
    public void addObserver(DataObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    /**
     * Remove um observer das notificações
     * 
     * @param observer Observer a ser removido
     */
    public void removeObserver(DataObserver observer) {
        observers.remove(observer);
    }
    
    /**
     * Notifica todos os observers sobre alteração de dados
     * 
     * @param data Dados alterados
     */
    public void notifyObservers(Object data) {
        notifyObservers(data, null);
    }
    
    /**
     * Notifica todos os observers sobre erro
     * 
     * @param error Exceção ocorrida
     */
    public void notifyError(Throwable error) {
        notifyObservers(null, error);
    }
    
    /**
     * Notifica todos os observers sobre conclusão de operação
     * 
     * @param success true se operação foi bem-sucedida
     */
    public void notifyComplete(boolean success) {
        for (DataObserver observer : observers) {
            try {
                observer.onComplete(source, success);
            } catch (Exception e) {
                // Log erro mas não interrompe notificação para outros observers
                System.err.println("Erro em observer onComplete: " + e.getMessage());
            }
        }
    }
    
    /**
     * Limpa todos os observers
     */
    public void clearObservers() {
        observers.clear();
    }
    
    /**
     * Retorna número de observers registrados
     * 
     * @return Quantidade de observers
     */
    public int countObservers() {
        return observers.size();
    }
    
    /**
     * Método privado para notificar observers com dados e/ou erro
     */
    private void notifyObservers(Object data, Throwable error) {
        for (DataObserver observer : observers) {
            try {
                if (error != null) {
                    observer.onError(source, error);
                } else {
                    observer.update(source, data);
                }
            } catch (Exception e) {
                // Log erro mas não interrompe notificação para outros observers
                System.err.println("Erro em observer: " + e.getMessage());
            }
        }
    }
}
