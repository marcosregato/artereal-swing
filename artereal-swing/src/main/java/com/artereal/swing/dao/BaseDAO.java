package com.artereal.swing.dao;

import com.artereal.swing.database.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe base para todos os DAOs do sistema.
 * Elimina código duplicado e fornece métodos comuns para operações CRUD.
 * 
 * @param <T> Tipo da entidade
 */
public abstract class BaseDAO<T> {
    
    protected static final Logger logger = LoggerFactory.getLogger(BaseDAO.class);
    
    /**
     * Salva ou atualiza uma entidade no banco de dados
     * 
     * @param entity Entidade a ser salva
     * @throws SQLException Em caso de erro no banco
     */
    public void save(T entity) throws SQLException {
        if (isNew(entity)) {
            insert(entity);
        } else {
            update(entity);
        }
    }
    
    /**
     * Insere uma nova entidade no banco de dados
     * 
     * @param entity Entidade a ser inserida
     * @throws SQLException Em caso de erro no banco
     */
    protected void insert(T entity) throws SQLException {
        String sql = getInsertSQL();
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            setInsertParameters(stmt, entity);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Falha ao inserir registro, nenhuma linha afetada.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    setGeneratedId(entity, generatedKeys.getLong(1));
                }
            }
            
            logger.debug("Registro inserido com sucesso: {}", entity.getClass().getSimpleName());
        }
    }
    
    /**
     * Atualiza uma entidade existente no banco de dados
     * 
     * @param entity Entidade a ser atualizada
     * @throws SQLException Em caso de erro no banco
     */
    protected void update(T entity) throws SQLException {
        String sql = getUpdateSQL();
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            setUpdateParameters(stmt, entity);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Falha ao atualizar registro, nenhuma linha afetada.");
            }
            
            logger.debug("Registro atualizado com sucesso: {}", entity.getClass().getSimpleName());
        }
    }
    
    /**
     * Busca uma entidade pelo ID
     * 
     * @param id ID da entidade
     * @return Entidade encontrada ou null
     * @throws SQLException Em caso de erro no banco
     */
    public T findById(Long id) throws SQLException {
        String sql = getSelectByIdSQL();
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Busca todas as entidades
     * 
     * @return Lista de todas as entidades
     * @throws SQLException Em caso de erro no banco
     */
    public List<T> findAll() throws SQLException {
        // Usa sincronização para evitar problemas com múltiplas threads
        synchronized (this) {
            String sql = getSelectAllSQL();
            List<T> entities = new ArrayList<>();
            
            logger.debug("Iniciando findAll() - SQL: {}", sql);
            
            // Garante que cada thread tenha sua própria conexão
            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;
            
            try {
                logger.debug("Obtendo conexão com banco de dados");
                conn = DatabaseManager.getInstance().getConnection();
                logger.debug("Conexão obtida, preparando statement");
                stmt = conn.prepareStatement(sql);
                logger.debug("Statement preparado, executando query");
                rs = stmt.executeQuery();
                logger.debug("Query executada, processando resultados");
                
                int rowCount = 0;
                while (rs.next()) {
                    entities.add(mapResultSetToEntity(rs));
                    rowCount++;
                }
                logger.info("findAll() concluído - {} entidades encontradas", rowCount);
                
            } catch (SQLException e) {
                logger.error("Erro SQL em findAll(): {}", e.getMessage(), e);
                logger.error("SQL executado: {}", sql);
                logger.error("Status da conexão: {}", conn != null ? "aberta" : "nula");
                logger.error("Status do statement: {}", stmt != null ? "criado" : "nulo");
                throw e;
            } finally {
                // Fecha recursos na ordem inversa
                if (rs != null) {
                    try { 
                        rs.close(); 
                        logger.debug("ResultSet fechado com sucesso");
                    } catch (SQLException e) { 
                        logger.warn("Erro ao fechar ResultSet: {}", e.getMessage());
                    }
                }
                if (stmt != null) {
                    try { 
                        stmt.close(); 
                        logger.debug("PreparedStatement fechado com sucesso");
                    } catch (SQLException e) { 
                        logger.warn("Erro ao fechar PreparedStatement: {}", e.getMessage());
                    }
                }
                if (conn != null) {
                    try { 
                        conn.close(); 
                        logger.debug("Connection fechada com sucesso");
                    } catch (SQLException e) { 
                        logger.warn("Erro ao fechar Connection: {}", e.getMessage());
                    }
                }
            }
            
            return entities;
        }
    }
    
    /**
     * Exclui uma entidade pelo ID
     * 
     * @param id ID da entidade
     * @throws SQLException Em caso de erro no banco
     */
    public void delete(Long id) throws SQLException {
        String sql = getDeleteSQL();
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Falha ao excluir registro, nenhuma linha afetada.");
            }
            
            logger.debug("Registro excluído com sucesso, ID: {}", id);
        }
    }
    
    /**
     * Busca entidades com filtro personalizado
     * 
     * @param sql SQL personalizado
     * @param parameters Parâmetros da consulta
     * @return Lista de entidades encontradas
     * @throws SQLException Em caso de erro no banco
     */
    protected List<T> findByFilter(String sql, Object... parameters) throws SQLException {
        List<T> entities = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            for (int i = 0; i < parameters.length; i++) {
                stmt.setObject(i + 1, parameters[i]);
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    entities.add(mapResultSetToEntity(rs));
                }
            }
        }
        
        return entities;
    }
    
    /**
     * Executa contagem de registros
     * 
     * @param sql SQL de contagem
     * @param parameters Parâmetros da consulta
     * @return Número de registros
     * @throws SQLException Em caso de erro no banco
     */
    protected int count(String sql, Object... parameters) throws SQLException {
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            for (int i = 0; i < parameters.length; i++) {
                stmt.setObject(i + 1, parameters[i]);
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        
        return 0;
    }
    
    /**
     * Formata data para o banco de dados
     * 
     * @param date Data a ser formatada
     * @return String formatada ou null
     */
    protected String formatDate(LocalDateTime date) {
        return date != null ? date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
    }
    
    /**
     * Formata data para o banco de dados
     * 
     * @param date Data a ser formatada
     * @return String formatada ou null
     */
    protected String formatDate(java.time.LocalDate date) {
        return date != null ? date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null;
    }
    
    // Métodos abstratos que devem ser implementados pelas classes filhas
    
    /**
     * Verifica se a entidade é nova (não tem ID)
     * 
     * @param entity Entidade a verificar
     * @return true se for nova, false caso contrário
     */
    protected abstract boolean isNew(T entity);
    
    /**
     * Obtém SQL para inserção
     * 
     * @return SQL de inserção
     */
    protected abstract String getInsertSQL();
    
    /**
     * Obtém SQL para atualização
     * 
     * @return SQL de atualização
     */
    protected abstract String getUpdateSQL();
    
    /**
     * Obtém SQL para busca por ID
     * 
     * @return SQL de busca por ID
     */
    protected abstract String getSelectByIdSQL();
    
    /**
     * Obtém SQL para busca de todos os registros
     * 
     * @return SQL de busca de todos
     */
    protected abstract String getSelectAllSQL();
    
    /**
     * Obtém SQL para exclusão
     * 
     * @return SQL de exclusão
     */
    protected abstract String getDeleteSQL();
    
    /**
     * Define parâmetros para inserção
     * 
     * @param stmt PreparedStatement
     * @param entity Entidade
     * @throws SQLException Em caso de erro
     */
    protected abstract void setInsertParameters(PreparedStatement stmt, T entity) throws SQLException;
    
    /**
     * Define parâmetros para atualização
     * 
     * @param stmt PreparedStatement
     * @param entity Entidade
     * @throws SQLException Em caso de erro
     */
    protected abstract void setUpdateParameters(PreparedStatement stmt, T entity) throws SQLException;
    
    /**
     * Mapeia ResultSet para entidade
     * 
     * @param rs ResultSet
     * @return Entidade mapeada
     * @throws SQLException Em caso de erro
     */
    protected abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;
    
    /**
     * Define ID gerado na entidade
     * 
     * @param entity Entidade
     * @param id ID gerado
     */
    protected abstract void setGeneratedId(T entity, Long id);
}
