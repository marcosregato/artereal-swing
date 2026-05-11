package com.artereal.swing.patterns.observer;

import com.artereal.swing.dao.IrmaoDAO;
import com.artereal.swing.model.Irmao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Gerenciador de dados em tempo real usando Observer Pattern
 * Notifica observers sobre alterações nos dados dos irmãos
 */
public class RealTimeDataManager {
    
    private static final Logger logger = LoggerFactory.getLogger(RealTimeDataManager.class);
    private static RealTimeDataManager instance;
    
    private final DataObservable observable;
    private final IrmaoDAO irmaoDAO;
    private Timer updateTimer;
    private long lastUpdateCount = -1;
    
    /**
     * Construtor privado para Singleton
     */
    private RealTimeDataManager() {
        this.observable = new DataObservable(this);
        this.irmaoDAO = new IrmaoDAO();
    }
    
    /**
     * Obtém instância única (Singleton)
     */
    public static synchronized RealTimeDataManager getInstance() {
        if (instance == null) {
            instance = new RealTimeDataManager();
        }
        return instance;
    }
    
    /**
     * Inicia monitoramento em tempo real
     * 
     * @param intervalMs Intervalo de atualização em milissegundos
     */
    public void startRealTimeUpdates(long intervalMs) {
        stopRealTimeUpdates();
        
        updateTimer = new Timer("RealTimeDataUpdater", true);
        updateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkForUpdates();
            }
        }, 0, intervalMs);
        
        logger.info("Monitoramento em tempo real iniciado com intervalo de {}ms", intervalMs);
    }
    
    /**
     * Para monitoramento em tempo real
     */
    public void stopRealTimeUpdates() {
        if (updateTimer != null) {
            updateTimer.cancel();
            updateTimer = null;
            logger.info("Monitoramento em tempo real parado");
        }
    }
    
    /**
     * Adiciona observer para receber atualizações
     * 
     * @param observer Observer a ser adicionado
     */
    public void addObserver(DataObserver observer) {
        observable.addObserver(observer);
        logger.debug("Observer adicionado: {}", observer.getClass().getSimpleName());
    }
    
    /**
     * Remove observer das atualizações
     * 
     * @param observer Observer a ser removido
     */
    public void removeObserver(DataObserver observer) {
        observable.removeObserver(observer);
        logger.debug("Observer removido: {}", observer.getClass().getSimpleName());
    }
    
    /**
     * Força atualização imediata dos dados
     */
    public void forceUpdate() {
        checkForUpdates();
    }
    
    /**
     * Verifica se houve alterações nos dados
     */
    private void checkForUpdates() {
        try {
            int currentCount = irmaoDAO.countAtivos();
            
            if (currentCount != lastUpdateCount) {
                List<Irmao> irmaos = irmaoDAO.findAll();
                
                // Notifica observers sobre novos dados
                observable.notifyObservers(irmaos);
                
                lastUpdateCount = currentCount;
                logger.debug("Dados atualizados: {} irmãos", irmaos.size());
                
                // Notifica conclusão bem-sucedida
                observable.notifyComplete(true);
            }
            
        } catch (Exception e) {
            logger.error("Erro ao verificar atualizações", e);
            observable.notifyError(e);
            observable.notifyComplete(false);
        }
    }
    
    /**
     * Obtém contagem atual de registros
     * 
     * @return Número de irmãos no banco
     */
    public int getCurrentCount() {
        try {
            return irmaoDAO.countAtivos();
        } catch (Exception e) {
            logger.error("Erro ao obter contagem atual", e);
            return -1;
        }
    }
    
    /**
     * Limpa todos os observers
     */
    public void clearObservers() {
        observable.clearObservers();
        logger.debug("Todos os observers removidos");
    }
    
    /**
     * Retorna número de observers ativos
     * 
     * @return Quantidade de observers
     */
    public int getObserverCount() {
        return observable.countObservers();
    }
}
