package com.artereal.swing.patterns.decorator;

import com.artereal.swing.dao.BaseDAO;
import com.artereal.swing.security.AccessControlManager;
import com.artereal.swing.security.CryptoManager;
import com.artereal.swing.security.IntegrityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Decorator para adicionar segurança a DAOs existentes
 * Implementa o padrão Decorator para adicionar funcionalidades de segurança
 */
public class SecureDAO<T> extends BaseDAO<T> {
    
    private static final Logger logger = LoggerFactory.getLogger(SecureDAO.class);
    
    private final BaseDAO<T> wrappedDAO;
    private final boolean enableEncryption;
    private final boolean enableAccessControl;
    private final boolean enableIntegrityCheck;
    
    // Implementação dos métodos abstratos de BaseDAO
    @Override
    public String getFindAllSQL() {
        return wrappedDAO.getFindAllSQL();
    }
    
    @Override
    public String getFindByIdSQL() {
        return wrappedDAO.getFindByIdSQL();
    }
    
    @Override
    public String getInsertSQL() {
        return wrappedDAO.getInsertSQL();
    }
    
    @Override
    public String getUpdateSQL() {
        return wrappedDAO.getUpdateSQL();
    }
    
    @Override
    public String getDeleteSQL() {
        return wrappedDAO.getDeleteSQL();
    }
    
    @Override
    public void setInsertParameters(PreparedStatement stmt, T entity) throws SQLException {
        wrappedDAO.setInsertParameters(stmt, entity);
    }
    
    @Override
    public void setUpdateParameters(PreparedStatement stmt, T entity) throws SQLException {
        wrappedDAO.setUpdateParameters(stmt, entity);
    }
    
    @Override
    public void setGeneratedId(T entity, long id) {
        wrappedDAO.setGeneratedId(entity, id);
    }
    
    @Override
    public boolean isNew(T entity) {
        return wrappedDAO.isNew(entity);
    }
    
    @Override
    public T mapResultSetToEntity(java.sql.ResultSet rs) throws SQLException {
        return wrappedDAO.mapResultSetToEntity(rs);
    }
    
    /**
     * Construtor
     * 
     * @param wrappedDAO DAO a ser decorado
     * @param enableEncryption Habilitar criptografia
     * @param enableAccessControl Habilitar controle de acesso
     * @param enableIntegrityCheck Habilitar verificação de integridade
     */
    public SecureDAO(BaseDAO<T> wrappedDAO, boolean enableEncryption, 
                   boolean enableAccessControl, boolean enableIntegrityCheck) {
        this.wrappedDAO = wrappedDAO;
        this.enableEncryption = enableEncryption;
        this.enableAccessControl = enableAccessControl;
        this.enableIntegrityCheck = enableIntegrityCheck;
        
        logger.info("SecureDAO criado - Encryption: {}, AccessControl: {}, Integrity: {}",
                  enableEncryption, enableAccessControl, enableIntegrityCheck);
    }
    
    @Override
    public void save(T entity) throws SQLException {
        logger.debug("Iniciando save seguro para entidade: {}", entity.getClass().getSimpleName());
        
        try {
            // 1. Verificação de integridade
            if (enableIntegrityCheck) {
                if (!IntegrityManager.verifyDataIntegrity(entity.getClass().getSimpleName(), entity.toString().getBytes())) {
                    logger.error("Falha na verificação de integridade da entidade");
                    throw new SQLException("Integridade dos dados comprometida");
                }
            }
            
            // 2. Criptografia
            T entityToSave = entity;
            if (enableEncryption) {
                try {
                    entityToSave = (T) CryptoManager.encryptData(entity);
                    logger.debug("Entidade criptografada para salvamento");
                } catch (CryptoManager.CryptoException e) {
                    logger.error("Erro na criptografia da entidade", e);
                    throw new SQLException("Falha na criptografia dos dados", e);
                }
            }
            
            // 3. Controle de acesso
            if (enableAccessControl) {
                String currentUser = AccessControlManager.getCurrentUser();
                String tableName = entity.getClass().getSimpleName().toLowerCase();
                
                if (!AccessControlManager.hasPermission(currentUser, tableName, 
                        AccessControlManager.AccessLevel.WRITE)) {
                    logger.warn("Acesso negado para usuário {} na tabela {}", currentUser, tableName);
                    throw new SQLException("Acesso negado: permissão insuficiente");
                }
            }
            
            // 4. Salvamento executivo
            wrappedDAO.save(entityToSave);
            
            logger.info("Entidade salva com segurança: {}", entity.getClass().getSimpleName());
            
        } catch (SQLException e) {
            logger.error("Erro ao salvar entidade com segurança", e);
            throw e;
        }
    }
    
    @Override
    public T findById(Long id) throws SQLException {
        logger.debug("Buscando entidade segura com ID: {}", id);
        
        T entity = wrappedDAO.findById(id);
        
        if (entity != null && enableEncryption) {
            try {
                entity = (T) CryptoManager.decryptData(entity);
                logger.debug("Entidade decriptografada: {}", entity.getClass().getSimpleName());
            } catch (Exception e) {
                logger.error("Erro ao decriptografar entidade", e);
                throw new SQLException("Erro ao decriptografar dados", e);
            }
        }
        
        return entity;
    }
    
    @Override
    public java.util.List<T> findAll() throws SQLException {
        logger.debug("Buscando todas as entidades com segurança");
        
        java.util.List<T> entities = wrappedDAO.findAll();
        
        if (enableEncryption) {
            java.util.List<T> decryptedEntities = new java.util.ArrayList<>();
            
            for (T entity : entities) {
                try {
                    T decrypted = (T) CryptoManager.decryptData(entity);
                    decryptedEntities.add(decrypted);
                } catch (Exception e) {
                    logger.error("Erro ao decriptografar entidade durante findAll", e);
                    // Continua com entidade criptografada em caso de erro
                    decryptedEntities.add(entity);
                }
            }
            
            entities = decryptedEntities;
        }
        
        return entities;
    }
    
    @Override
    public void delete(Long id) throws SQLException {
        logger.debug("Excluindo entidade segura com ID: {}", id);
        
        // Verificação de acesso antes de excluir
        if (enableAccessControl) {
            String currentUser = getCurrentUser();
            String tableName = wrappedDAO.getClass().getSimpleName().toLowerCase();
            
            if (!AccessControlManager.hasPermission(currentUser, tableName, 
                    AccessControlManager.AccessLevel.DELETE)) {
                logger.warn("Acesso negado para exclusão - Usuário: {}, Tabela: {}", 
                          currentUser, tableName);
                throw new SQLException("Acesso negado: permissão insuficiente para excluir");
            }
        }
        
        wrappedDAO.delete(id);
        logger.info("Entidade excluída com segurança: ID {}", id);
    }
    
    /**
     * Obtém DAO decorado
     * 
     * @return DAO original
     */
    public BaseDAO<T> getWrappedDAO() {
        return wrappedDAO;
    }
    
    /**
     * Verifica se criptografia está habilitada
     * 
     * @return true se criptografia está ativa
     */
    public boolean isEncryptionEnabled() {
        return enableEncryption;
    }
    
    /**
     * Verifica se controle de acesso está habilitado
     * 
     * @return true se controle de acesso está ativo
     */
    public boolean isAccessControlEnabled() {
        return enableAccessControl;
    }
    
    /**
     * Verifica se verificação de integridade está habilitada
     * 
     * @return true se verificação de integridade está ativa
     */
    public boolean isIntegrityCheckEnabled() {
        return enableIntegrityCheck;
    }
    
    /**
     * Aplica configurações de segurança ao DAO existente
     * 
     * @param dao DAO a ser protegido
     * @param enableEncryption Habilitar criptografia
     * @param enableAccessControl Habilitar controle de acesso
     * @return DAO decorado com segurança
     */
    public static <T> SecureDAO<T> secure(BaseDAO<T> dao, boolean enableEncryption, 
                                           boolean enableAccessControl, boolean enableIntegrityCheck) {
        return new SecureDAO<>(dao, enableEncryption, enableAccessControl, enableIntegrityCheck);
    }
    
    /**
     * Cria DAO com todas as seguranças habilitadas
     * 
     * @param dao DAO a ser protegido
     * @return DAO com segurança máxima
     */
    public static <T> SecureDAO<T> maxSecurity(BaseDAO<T> dao) {
        return new SecureDAO<>(dao, true, true, true);
    }
    
    /**
     * Cria DAO apenas com criptografia
     * 
     * @param dao DAO a ser protegido
     * @return DAO com criptografia
     */
    public static <T> SecureDAO<T> withEncryption(BaseDAO<T> dao) {
        return new SecureDAO<>(dao, true, false, false);
    }
    
    /**
     * Cria DAO apenas com controle de acesso
     * 
     * @param dao DAO a ser protegido
     * @return DAO com controle de acesso
     */
    public static <T> SecureDAO<T> withAccessControl(BaseDAO<T> dao) {
        return new SecureDAO<>(dao, false, true, false);
    }
    
    /**
     * Cria DAO apenas com verificação de integridade
     * 
     * @param dao DAO a ser protegido
     * @return DAO com verificação de integridade
     */
    public static <T> SecureDAO<T> withIntegrityCheck(BaseDAO<T> dao) {
        return new SecureDAO<>(dao, false, false, true);
    }
}
