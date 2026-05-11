package com.artereal.swing.logging;

import com.artereal.swing.security.AccessControlManager;
import com.artereal.swing.security.CryptoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Logger estruturado e centralizado para o sistema ArteReal
 * Implementa logging JSON com segurança e auditoria
 */
public class ArteRealLogger {
    
    private static final Logger logger = LoggerFactory.getLogger(ArteRealLogger.class);
    private static final Map<String, ArteRealLogger> instances = new ConcurrentHashMap<>();
    
    // Configurações de logging
    private final String className;
    private final boolean enableEncryption;
    private final boolean enableAccessControl;
    
    /**
     * Construtor privado para Singleton
     */
    private ArteRealLogger(String className, boolean enableEncryption, boolean enableAccessControl) {
        this.className = className;
        this.enableEncryption = enableEncryption;
        this.enableAccessControl = enableAccessControl;
    }
    
    /**
     * Obtém instância do logger para classe específica
     * 
     * @param className Nome da classe
     * @param enableEncryption Habilitar criptografia nos logs
     * @param enableAccessControl Habilitar controle de acesso nos logs
     * @return Logger configurado
     */
    public static synchronized ArteRealLogger getLogger(String className, boolean enableEncryption, boolean enableAccessControl) {
        return instances.computeIfAbsent(className, k -> 
            new ArteRealLogger(className, enableEncryption, enableAccessControl));
    }
    
    /**
     * Obtém logger padrão (sem criptografia)
     */
    public static ArteRealLogger getDefaultLogger(String className) {
        return getLogger(className, false, false);
    }
    
    /**
     * Obtém logger com segurança máxima
     */
    public static ArteRealLogger getSecureLogger(String className) {
        return getLogger(className, true, true);
    }
    
    /**
     * Obtém logger com criptografia apenas
     */
    public static ArteRealLogger getEncryptedLogger(String className) {
        return getLogger(className, true, false);
    }
    
    /**
     * Obtém logger com controle de acesso apenas
     */
    public static ArteRealLogger getAccessControlLogger(String className) {
        return getLogger(className, false, true);
    }
    
    /**
     * Registra informação de auditoria
     * 
     * @param level Nível do log
     * @param action Ação executada
     * @param entity Entidade envolvida
     * @param details Detalhes adicionais
     */
    public void audit(String level, String action, Object entity, Map<String, Object> details) {
        if (!enableAccessControl && !enableEncryption) {
            // Log simples se não houver segurança
            logger.info("[AUDITORIA] {} - {} - {} - {}", level, action, entity, details);
            return;
        }
        
        try {
            // Prepara dados de auditoria
            Map<String, Object> auditData = new ConcurrentHashMap<>();
            auditData.put("timestamp", LocalDateTime.now());
            auditData.put("level", level);
            auditData.put("action", action);
            auditData.put("entity", entity != null ? entity.getClass().getSimpleName() : "null");
            auditData.put("className", className);
            auditData.put("user", getCurrentUser());
            
            // Adiciona detalhes personalizados
            if (details != null) {
                auditData.putAll(details);
            }
            
            // Aplica criptografia se habilitado
            if (enableEncryption) {
                auditData = CryptoManager.encryptData(auditData);
            }
            
            // Verifica permissão se habilitado
            if (enableAccessControl) {
                String currentUser = getCurrentUser();
                if (currentUser != null) {
                    auditData.put("hasPermission", true);
                } else {
                    auditData.put("hasPermission", false);
                }
            }
            
            // Log estruturado em formato JSON
            logger.info("[AUDITORIA] {}", auditData);
            
        } catch (Exception e) {
            logger.error("Erro ao gerar log de auditoria", e);
        }
    }
    
    /**
     * Registra operação CRUD
     * 
     * @param operation Tipo de operação
     * @param entity Entidade envolvida
     * @param result Resultado da operação
     */
    public void logCRUD(String operation, Object entity, boolean result) {
        String level = result ? "INFO" : "ERROR";
        String action = operation.toUpperCase() + "_" + (result ? "SUCCESS" : "FAILURE");
        
        Map<String, Object> details = new ConcurrentHashMap<>();
        details.put("result", result);
        details.put("operation", operation);
        
        audit(level, action, entity, details);
    }
    
    /**
     * Registra erro de segurança
     * 
     * @param errorType Tipo do erro
     * @param entity Entidade envolvida
     * @param error Exceção ocorrida
     */
    public void logSecurity(String errorType, Object entity, Exception error) {
        Map<String, Object> details = new ConcurrentHashMap<>();
        details.put("errorType", errorType);
        details.put("errorMessage", error.getMessage());
        details.put("stackTrace", getStackTrace(error));
        
        audit("SECURITY", errorType + "_ERROR", entity, details);
    }
    
    /**
     * Registra evento de performance
     * 
     * @param operation Operação medida
     * @param duration Duração em ms
     * @param entity Entidade envolvida
     */
    public void logPerformance(String operation, long duration, Object entity) {
        Map<String, Object> details = new ConcurrentHashMap<>();
        details.put("duration", duration);
        details.put("performance", duration < 100 ? "GOOD" : "POOR");
        
        audit("PERFORMANCE", operation, entity, details);
    }
    
    /**
     * Registra evento de negócio
     * 
     * @param event Tipo de evento
     * @param entity Entidade envolvida
     * @param data Dados adicionais
     */
    public void logBusiness(String event, Object entity, Map<String, Object> data) {
        Map<String, Object> details = new ConcurrentHashMap<>();
        if (data != null) {
            details.putAll(data);
        }
        
        audit("BUSINESS", event, entity, details);
    }
    
    /**
     * Registra login de usuário
     * 
     * @param username Nome do usuário
     * @param result Resultado do login
     * @param details Detalhes adicionais
     */
    public void logLogin(String username, boolean result, Map<String, Object> details) {
        Map<String, Object> auditData = new ConcurrentHashMap<>();
        auditData.put("username", username);
        auditData.put("loginResult", result);
        if (details != null) {
            auditData.putAll(details);
        }
        
        audit("AUTH", result ? "LOGIN_SUCCESS" : "LOGIN_FAILURE", "User", auditData);
    }
    
    /**
     * Obtém usuário atual logado
     */
    private String getCurrentUser() {
        try {
            // Tenta obter usuário do contexto de segurança
            return AccessControlManager.getCurrentUser();
        } catch (Exception e) {
            return "unknown";
        }
    }
    
    /**
     * Extrai stack trace formatado
     */
    private String getStackTrace(Exception e) {
        java.io.StringWriter sw = new java.io.StringWriter();
        java.io.PrintWriter pw = new java.io.PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
    
    /**
     * Limpa todas as instâncias de logger
     */
    public static void clearAllLoggers() {
        instances.clear();
        logger.info("Todas as instâncias de logger foram limpas");
    }
    
    /**
     * Obtém estatísticas dos loggers
     */
    public static Map<String, Integer> getLoggerStatistics() {
        Map<String, Integer> stats = new ConcurrentHashMap<>();
        for (Map.Entry<String, ArteRealLogger> entry : instances.entrySet()) {
            stats.put(entry.getKey(), 1);
        }
        return stats;
    }
    
    /**
     * Configura nível de log global
     */
    public static void setGlobalLevel(String level) {
        // Implementação para configurar nível global de log
        logger.info("Nível global de log alterado para: {}", level);
    }
    
    /**
     * Habilita/Desabilita modo debug
     */
    public static void setDebugMode(boolean enabled) {
        System.setProperty("artereal.debug", String.valueOf(enabled));
        logger.info("Modo debug: {}", enabled ? "ATIVADO" : "DESATIVADO");
    }
    
    /**
     * Obtém informações do logger atual
     */
    public String getLoggerInfo() {
        return String.format("Logger: %s | Encryption: %s | AccessControl: %s", 
                          className, enableEncryption, enableAccessControl);
    }
}
