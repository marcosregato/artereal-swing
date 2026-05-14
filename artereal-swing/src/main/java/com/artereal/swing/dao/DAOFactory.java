package com.artereal.swing.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory Singleton para gerenciamento centralizado de DAOs.
 * Implementa o padrão Singleton para garantir uma única instância
 * e otimizar o uso de memória e performance.
 */
public class DAOFactory {
    
    private static final Logger logger = LoggerFactory.getLogger(DAOFactory.class);
    private static volatile DAOFactory instance;
    
    // Cache de instâncias de DAOs
    private final Map<Class<?>, Object> daoCache;
    
    /**
     * Construtor privado para implementar Singleton
     */
    private DAOFactory() {
        this.daoCache = new HashMap<>();
        logger.debug("DAOFactory Singleton criado");
    }
    
    /**
     * Obtém a instância única do DAOFactory (Double-Checked Locking)
     */
    public static DAOFactory getInstance() {
        if (instance == null) {
            synchronized (DAOFactory.class) {
                if (instance == null) {
                    instance = new DAOFactory();
                }
            }
        }
        return instance;
    }
    
    /**
     * Obtém uma instância de DAO com cache (thread-safe)
     */
    @SuppressWarnings("unchecked")
    public <T> T getDAO(Class<T> daoClass) {
        // Validações de entrada
        if (daoClass == null) {
            logger.warn("Tentativa de obter DAO com classe nula - retornando null");
            return null;
        }
        
        // Verifica se é classe interna não estática (apenas classes membro)
        if (daoClass.isMemberClass() && !java.lang.reflect.Modifier.isStatic(daoClass.getModifiers())) {
            logger.warn("Classe interna não estática não suportada: {} - retornando null", daoClass.getSimpleName());
            return null;
        }
        
        // Verifica se é classe interna local - precisa tratamento especial
        if (daoClass.isLocalClass()) {
            logger.warn("Classe interna local não suportada: {} - retornando null", daoClass.getSimpleName());
            return null;
        }
        
                
        if (daoClass.isAnonymousClass()) {
            logger.warn("Classe anônima não suportada: {} - retornando null", daoClass.getSimpleName());
            return null;
        }
        
        // Usa sincronização para evitar problemas com múltiplas threads
        synchronized (daoCache) {
            if (daoCache.containsKey(daoClass)) {
                return (T) daoCache.get(daoClass);
            }
            
            try {
                T daoInstance = daoClass.getDeclaredConstructor().newInstance();
                daoCache.put(daoClass, daoInstance);
                return daoInstance;
            } catch (Exception e) {
                logger.error("Erro ao criar instância de DAO: {}", daoClass.getSimpleName(), e);
                throw new RuntimeException("Falha ao criar DAO: " + daoClass.getSimpleName(), e);
            }
        }
    }
    
        
    /**
     * Limpa o cache de DAOs (útil para testes ou reinicialização)
     */
    public void clearCache() {
        synchronized (daoCache) {
            int size = daoCache.size();
            daoCache.clear();
            logger.debug("Cache de DAOs limpo. {} instâncias removidas.", size);
        }
    }
    
    /**
     * Obtém estatísticas do cache
     */
    public int getCacheSize() {
        synchronized (daoCache) {
            return daoCache.size();
        }
    }
    
    /**
     * Verifica se um DAO está em cache
     */
    public boolean isCached(Class<?> daoClass) {
        synchronized (daoCache) {
            return daoCache.containsKey(daoClass);
        }
    }
    
    // Métodos de conveniência para os DAOs mais usados
    
    /**
     * Obtém IrmaoDAO
     */
    public IrmaoDAO getIrmaoDAO() {
        return getDAO(IrmaoDAO.class);
    }
    
    /**
     * Obtém SessaoDAO
     */
    public SessaoDAO getSessaoDAO() {
        return getDAO(SessaoDAO.class);
    }
    
    /**
     * Obtém UsuarioDAO
     */
    public UsuarioDAO getUsuarioDAO() {
        return getDAO(UsuarioDAO.class);
    }
    
    /**
     * Obtém LojaDAO
     */
    public LojaDAO getLojaDAO() {
        return getDAO(LojaDAO.class);
    }
    
    /**
     * Obtém CaixaDAO
     */
    public CaixaDAO getCaixaDAO() {
        return getDAO(CaixaDAO.class);
    }
    
    /**
     * Obtém BibliotecaDAO
     */
    public BibliotecaDAO getBibliotecaDAO() {
        return getDAO(BibliotecaDAO.class);
    }
    
    /**
     * Obtém CandidatoDAO
     */
    public CandidatoDAO getCandidatoDAO() {
        return getDAO(CandidatoDAO.class);
    }
    
    /**
     * Obtém FrequenciaDAO
     */
    public FrequenciaDAO getFrequenciaDAO() {
        return getDAO(FrequenciaDAO.class);
    }
    
    /**
     * Obtém AfastamentoDAO
     */
    public AfastamentoDAO getAfastamentoDAO() {
        return getDAO(AfastamentoDAO.class);
    }
    
    /**
     * Obtém ConfiguracaoDAO
     */
    public ConfiguracaoDAO getConfiguracaoDAO() {
        return getDAO(ConfiguracaoDAO.class);
    }
    
    /**
     * Obtém DocumentoDAO
     */
    public DocumentoDAO getDocumentoDAO() {
        return getDAO(DocumentoDAO.class);
    }
    
        
    /**
     * Obtém ChequeDAO
     */
    public ChequeDAO getChequeDAO() {
        return getDAO(ChequeDAO.class);
    }
    
    /**
     * Obtém VisitanteDAO
     */
    public VisitanteDAO getVisitanteDAO() {
        return getDAO(VisitanteDAO.class);
    }
    
    /**
     * Obtém CalendarioDAO
     */
    public CalendarioDAO getCalendarioDAO() {
        return getDAO(CalendarioDAO.class);
    }
    
    /**
     * Obtém FotoDAO
     */
    public FotoDAO getFotoDAO() {
        return getDAO(FotoDAO.class);
    }
}
