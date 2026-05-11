package com.artereal.swing.patterns.template;

import com.artereal.swing.dao.BaseDAO;
import com.artereal.swing.security.AccessControlManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * Template Method para operações DAO
 * Implementa o padrão Template Method para operações padronizadas
 */
public abstract class DAOTemplate<T> extends BaseDAO<T> {
    
    private static final Logger logger = LoggerFactory.getLogger(DAOTemplate.class);
    
    /**
     * Template method para operação de criação
     * 
     * @param entity Entidade a ser criada
     * @throws SQLException Em caso de erro
     */
    public final void createWithTemplate(T entity) throws SQLException {
        logger.debug("Iniciando criação com template para: {}", entity.getClass().getSimpleName());
        
        // 1. Pré-processamento
        preProcess(entity);
        
        // 2. Validação
        validateForCreate(entity);
        
        // 3. Autorização
        checkCreatePermission(entity);
        
        // 4. Processamento principal
        processCreate(entity);
        
        // 5. Pós-processamento
        postProcess(entity);
        
        // 6. Auditoria
        auditCreate(entity);
        
        logger.info("Entidade criada com template: {}", entity.getClass().getSimpleName());
    }
    
    /**
     * Template method para operação de atualização
     * 
     * @param entity Entidade a ser atualizada
     * @throws SQLException Em caso de erro
     */
    public final void updateWithTemplate(T entity) throws SQLException {
        logger.debug("Iniciando atualização com template para: {}", entity.getClass().getSimpleName());
        
        // 1. Pré-processamento
        preProcess(entity);
        
        // 2. Validação
        validateForUpdate(entity);
        
        // 3. Autorização
        checkUpdatePermission(entity);
        
        // 4. Processamento principal
        processUpdate(entity);
        
        // 5. Pós-processamento
        postProcess(entity);
        
        // 6. Auditoria
        auditUpdate(entity);
        
        logger.info("Entidade atualizada com template: {}", entity.getClass().getSimpleName());
    }
    
    /**
     * Template method para operação de exclusão
     * 
     * @param id ID da entidade a ser excluída
     * @throws SQLException Em caso de erro
     */
    public final void deleteWithTemplate(Long id) throws SQLException {
        logger.debug("Iniciando exclusão com template para ID: {}", id);
        
        // 1. Pré-processamento
        preDelete(id);
        
        // 2. Autorização
        checkDeletePermission();
        
        // 3. Processamento principal
        processDelete(id);
        
        // 4. Pós-processamento
        postDelete(id);
        
        // 5. Auditoria
        auditDelete(id);
        
        logger.info("Entidade excluída com template: ID {}", id);
    }
    
    // Métodos Hook para serem sobrescritos pelas subclasses
    
    /**
     * Pré-processamento antes da operação
     * 
     * @param entity Entidade a ser processada
     */
    protected void preProcess(T entity) {
        // Implementação padrão vazia - pode ser sobrescrita
        logger.debug("Pré-processamento padrão para: {}", entity.getClass().getSimpleName());
    }
    
    /**
     * Validação específica para criação
     * 
     * @param entity Entidade a ser validada
     */
    protected void validateForCreate(T entity) throws SQLException {
        // Implementação padrão vazia - pode ser sobrescrita
        logger.debug("Validação padrão para criação de: {}", entity.getClass().getSimpleName());
    }
    
    /**
     * Validação específica para atualização
     * 
     * @param entity Entidade a ser validada
     */
    protected void validateForUpdate(T entity) throws SQLException {
        // Implementação padrão vazia - pode ser sobrescrita
        logger.debug("Validação padrão para atualização de: {}", entity.getClass().getSimpleName());
    }
    
    /**
     * Verificação de permissão para criação
     * 
     * @param entity Entidade a ser verificada
     */
    protected void checkCreatePermission(T entity) throws SQLException {
        if (!System.getProperty("test.environment", "false").equals("true")) {
            String currentUser = getCurrentUser();
            String tableName = entity.getClass().getSimpleName().toLowerCase();
            
            if (!AccessControlManager.hasPermission(currentUser, tableName, 
                    AccessControlManager.AccessLevel.WRITE)) {
                logger.warn("Acesso negado para criação - Usuário: {}, Tabela: {}", 
                          currentUser, tableName);
                throw new SQLException("Acesso negado: permissão insuficiente para criar");
            }
        }
    }
    
    /**
     * Verificação de permissão para atualização
     * 
     * @param entity Entidade a ser verificada
     */
    protected void checkUpdatePermission(T entity) throws SQLException {
        if (!System.getProperty("test.environment", "false").equals("true")) {
            String currentUser = getCurrentUser();
            String tableName = entity.getClass().getSimpleName().toLowerCase();
            
            if (!AccessControlManager.hasPermission(currentUser, tableName, 
                    AccessControlManager.AccessLevel.WRITE)) {
                logger.warn("Acesso negado para atualização - Usuário: {}, Tabela: {}", 
                          currentUser, tableName);
                throw new SQLException("Acesso negado: permissão insuficiente para atualizar");
            }
        }
    }
    
    /**
     * Verificação de permissão para exclusão
     */
    protected void checkDeletePermission() throws SQLException {
        if (!System.getProperty("test.environment", "false").equals("true")) {
            String currentUser = getCurrentUser();
            String tableName = getTableName();
            
            if (!AccessControlManager.hasPermission(currentUser, tableName, 
                    AccessControlManager.AccessLevel.DELETE)) {
                logger.warn("Acesso negado para exclusão - Usuário: {}, Tabela: {}", 
                          currentUser, tableName);
                throw new SQLException("Acesso negado: permissão insuficiente para excluir");
            }
        }
    }
    
    /**
     * Processamento principal de criação
     * 
     * @param entity Entidade a ser criada
     * @throws SQLException Em caso de erro
     */
    protected void processCreate(T entity) throws SQLException {
        // Define timestamp de criação
        setCreationTimestamp(entity);
        
        // Salva entidade
        save(entity);
    }
    
    /**
     * Processamento principal de atualização
     * 
     * @param entity Entidade a ser atualizada
     * @throws SQLException Em caso de erro
     */
    protected void processUpdate(T entity) throws SQLException {
        // Define timestamp de atualização
        setUpdateTimestamp(entity);
        
        // Atualiza entidade
        update(entity);
    }
    
    /**
     * Processamento principal de exclusão
     * 
     * @param id ID da entidade a ser excluída
     * @throws SQLException Em caso de erro
     */
    protected void processDelete(Long id) throws SQLException {
        // Exclui entidade
        delete(id);
    }
    
    /**
     * Pós-processamento após operação
     * 
     * @param entity Entidade processada
     */
    protected void postProcess(T entity) {
        // Implementação padrão vazia - pode ser sobrescrita
        logger.debug("Pós-processamento padrão para: {}", entity.getClass().getSimpleName());
    }
    
    /**
     * Pós-processamento após exclusão
     * 
     * @param id ID da entidade excluída
     */
    protected void postDelete(Long id) {
        // Implementação padrão vazia - pode ser sobrescrita
        logger.debug("Pós-processamento padrão para exclusão: ID {}", id);
    }
    
    /**
     * Pré-processamento antes da exclusão
     * 
     * @param id ID da entidade a ser excluída
     */
    protected void preDelete(Long id) {
        // Implementação padrão vazia - pode ser sobrescrita
        logger.debug("Pré-processamento padrão para exclusão: ID {}", id);
    }
    
    /**
     * Auditoria de criação
     * 
     * @param entity Entidade criada
     */
    protected void auditCreate(T entity) {
        logger.info("AUDITORIA: Entidade {} criada por {} em {}", 
                  entity.getClass().getSimpleName(), getCurrentUser(), LocalDateTime.now());
    }
    
    /**
     * Auditoria de atualização
     * 
     * @param entity Entidade atualizada
     */
    protected void auditUpdate(T entity) {
        logger.info("AUDITORIA: Entidade {} atualizada por {} em {}", 
                  entity.getClass().getSimpleName(), getCurrentUser(), LocalDateTime.now());
    }
    
    /**
     * Auditoria de exclusão
     * 
     * @param id ID da entidade excluída
     */
    protected void auditDelete(Long id) {
        logger.info("AUDITORIA: Entidade {} com ID {} excluída por {} em {}", 
                  getTableName(), id, getCurrentUser(), LocalDateTime.now());
    }
    
    // Métodos auxiliares para serem sobrescritos
    
    /**
     * Define timestamp de criação na entidade
     * 
     * @param entity Entidade a receber timestamp
     */
    protected abstract void setCreationTimestamp(T entity);
    
    /**
     * Define timestamp de atualização na entidade
     * 
     * @param entity Entidade a receber timestamp
     */
    protected abstract void setUpdateTimestamp(T entity);
    
    /**
     * Obtém nome da tabela para auditoria
     * 
     * @return Nome da tabela
     */
    protected abstract String getTableName();
}
