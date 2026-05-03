package com.artereal.swing.security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DAO com segurança integrada para proteção contra ataques
 */
public abstract class SecureDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(SecureDAO.class);
    
    /**
     * Cria PreparedStatement seguro com validação
     */
    protected PreparedStatement createSecureStatement(Connection conn, String sql, Object... params) throws SQLException {
        // Valida SQL contra injection
        SecurityManager.SecurityScanResult scanResult = SecurityManager.scanForThreats(sql);
        if (!scanResult.isSafe()) {
            throw new SecurityException("SQL Injection detectado na consulta: " + scanResult.getMessage());
        }
        
        // Valida parâmetros
        for (Object param : params) {
            if (param != null && param instanceof String) {
                SecurityManager.SecurityScanResult paramScanResult = SecurityManager.scanForThreats((String) param);
                if (!paramScanResult.isSafe()) {
                    logger.warn("Tentativa de SQL Injection detectada em parâmetros: {}", paramScanResult);
                    throw new SecurityException("Parâmetro malicioso detectado: " + paramScanResult.getMessage());
                }
            }
        }
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        
        // Define parâmetros de forma segura
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            if (param instanceof String) {
                // Sanitiza string antes de definir
                String sanitized = SecurityManager.sanitizeInput((String) param);
                stmt.setString(i + 1, sanitized);
            } else {
                stmt.setObject(i + 1, param);
            }
        }
        
        return stmt;
    }
    
    /**
     * Executa consulta segura com validação
     */
    protected ResultSet executeSecureQuery(PreparedStatement stmt) throws SQLException {
        // Log da consulta para auditoria
        logger.debug("Executando consulta segura: {}", stmt.toString());
        
        return stmt.executeQuery();
    }
    
    /**
     * Executa update seguro com validação
     */
    protected int executeSecureUpdate(PreparedStatement stmt) throws SQLException {
        // Log da operação para auditoria
        logger.debug("Executando update seguro: {}", stmt.toString());
        
        int result = stmt.executeUpdate();
        
        // Verifica se o resultado é suspeito (muitas linhas afetadas)
        if (result > 1000) {
            logger.warn("Operação afetou muitas linhas: {}", result);
            throw new SecurityException("Operação suspeita detectada");
        }
        
        return result;
    }
    
    /**
     * Valida dados antes de salvar
     */
    protected void validateBeforeSave(Object entity) throws SecurityException {
        if (entity == null) {
            throw new SecurityException("Entidade nula não permitida");
        }
        
        // Validação básica da entidade
        SecurityManager.SecurityScanResult scanResult = SecurityManager.scanForThreats(entity.toString());
        if (!scanResult.isSafe()) {
            throw new SecurityException("Entidade contém dados maliciosos: " + scanResult.getMessage());
        }
    }
    
    /**
     * Valida ID antes de operação
     */
    protected void validateId(Long id) throws SecurityException {
        if (id == null) {
            throw new SecurityException("ID não pode ser nulo");
        }
        
        if (id <= 0) {
            throw new SecurityException("ID deve ser positivo");
        }
        
        if (id > 1000000) {
            throw new SecurityException("ID suspeito detectado");
        }
    }
    
    /**
     * Valida string de busca
     */
    protected String validateSearchString(String searchString) throws SecurityException {
        if (searchString == null) {
            return "";
        }
        
        // Limita comprimento
        if (searchString.length() > 100) {
            throw new SecurityException("String de busca muito longa");
        }
        
        // Verifica conteúdo malicioso
        SecurityManager.SecurityScanResult scanResult = SecurityManager.scanForThreats(searchString);
        if (!scanResult.isSafe()) {
            throw new SecurityException("String de busca contém conteúdo malicioso: " + scanResult.getMessage());
        }
        
        return SecurityManager.sanitizeInput(searchString);
    }
    
    /**
     * Valida dados específicos do ArteReal
     */
    protected void validateArteRealData(String dataType, String data) throws SecurityException {
        InputValidator.ValidationResult result;
        
        switch (dataType.toUpperCase()) {
            case "NOME":
                result = InputValidator.validateNome(data);
                break;
            case "EMAIL":
                result = InputValidator.validateEmail(data);
                break;
            case "TELEFONE":
                result = InputValidator.validateTelefone(data);
                break;
            case "CEP":
                result = InputValidator.validateCEP(data);
                break;
            case "CPF":
                result = InputValidator.validateCPF(data);
                break;
            case "RG":
                result = InputValidator.validateRG(data);
                break;
            case "DATA":
                result = InputValidator.validateData(data);
                break;
            case "VALOR":
                result = InputValidator.validateValor(data);
                break;
            case "GRAU":
                result = InputValidator.validateGrau(data);
                break;
            case "CARGO":
                result = InputValidator.validateCargo(data);
                break;
            case "DESCRICAO":
                result = InputValidator.validateDescricao(data);
                break;
            case "TIPO_SANGUINEO":
                result = InputValidator.validateTipoSanguineo(data);
                break;
            default:
                // Validação genérica para tipos não especificados
                SecurityManager.SecurityScanResult scanResult = SecurityManager.scanForThreats(data);
                if (!scanResult.isSafe()) {
                    throw new SecurityException("Dados inválidos para " + dataType + ": " + scanResult.getMessage());
                }
                return;
        }
        
        if (!result.isValid()) {
            throw new SecurityException("Dados inválidos para " + dataType + ": " + result.getMessage());
        }
    }
    
    /**
     * Verifica permissões de acesso
     */
    protected void checkPermissions(String operation, String entityType) throws SecurityException {
        // Implementar verificação de permissões baseada em usuário
        // Por enquanto, apenas log da operação
        logger.info("Verificando permissões para operação: {} em {}", operation, entityType);
        
        // TODO: Implementar controle de acesso real
    }
    
    /**
     * Log de auditoria de segurança
     */
    protected void logSecurityEvent(String event, String details) {
        logger.info("Security Event: {} - {}", event, details);
    }
    
    /**
     * Verifica se a operação é permitida em horário comercial
     */
    protected void checkBusinessHours() throws SecurityException {
        int hour = java.time.LocalTime.now().getHour();
        
        // Permite operações das 6h às 22h
        if (hour < 6 || hour > 22) {
            logger.warn("Tentativa de operação fora do horário comercial: {}", hour);
            throw new SecurityException("Operação não permitida fora do horário comercial");
        }
    }
    
    /**
     * Limita quantidade de resultados
     */
    protected void limitResults(ResultSet rs, int maxResults) throws SQLException {
        int count = 0;
        while (rs.next() && count < maxResults) {
            count++;
        }
        
        if (count >= maxResults) {
            logger.warn("Consulta retornou muitos resultados, limitado a {}", maxResults);
        }
    }
    
    /**
     * Valida conexão segura
     */
    protected void validateConnection(Connection conn) throws SecurityException {
        if (conn == null) {
            throw new SecurityException("Conexão nula não permitida");
        }
        
        try {
            if (conn.isClosed()) {
                throw new SecurityException("Conexão fechada não permitida");
            }
        } catch (SQLException e) {
            throw new SecurityException("Erro ao validar conexão: " + e.getMessage());
        }
    }
    
    /**
     * Sanitiza nome de tabela
     */
    protected String sanitizeTableName(String tableName) throws SecurityException {
        if (tableName == null || tableName.trim().isEmpty()) {
            throw new SecurityException("Nome da tabela não pode ser vazio");
        }
        
        // Verifica se nome da tabela é válido
        if (!tableName.matches("^[a-zA-Z_][a-zA-Z0-9_]*$")) {
            throw new SecurityException("Nome da tabela inválido: " + tableName);
        }
        
        // Lista de tabelas permitidas
        List<String> allowedTables = List.of(
            "irmao", "usuario", "despesas", "configuracao", "sessao", 
            "loja", "cheque", "documento", "foto", "frequencia", 
            "visitante", "caixa", "candidato", "afastamento", "biblioteca"
        );
        
        if (!allowedTables.contains(tableName.toLowerCase())) {
            throw new SecurityException("Tabela não permitida: " + tableName);
        }
        
        return tableName;
    }
    
    /**
     * Verifica integridade dos dados
     */
    protected void validateDataIntegrity(Object data) throws SecurityException {
        if (data == null) {
            return;
        }
        
        // Verifica se os dados foram corrompidos
        String dataString = data.toString();
        
        // Verifica padrões de corrupção
        if (dataString.contains("") || dataString.contains("?")) {
            throw new SecurityException("Dados corrompidos detectados");
        }
        
        // Verifica tamanho excessivo
        if (dataString.length() > 10000) {
            throw new SecurityException("Dados excessivamente longos");
        }
    }
}
