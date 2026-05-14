package com.artereal.swing.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Logger Estruturado JSON para eventos de segurança
 * Implementa OWASP A09: Security Logging and Monitoring Failures
 * 
 * Fornece logs estruturados em formato JSON para análise e monitoramento
 */
public class StructuredLogger {
    
    private static final Logger logger = LoggerFactory.getLogger(StructuredLogger.class);
    private static volatile StructuredLogger instance;
    
    private final ObjectMapper objectMapper;
    private final Map<String, Object> globalContext;
    private final DateTimeFormatter timestampFormatter;
    
    private StructuredLogger() {
        this.objectMapper = new ObjectMapper();
        this.globalContext = new ConcurrentHashMap<>();
        this.timestampFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        
        // Contexto global padrão
        globalContext.put("service", "artereal-swing");
        globalContext.put("version", "2.1.0");
        globalContext.put("environment", System.getProperty("environment", "development"));
        
        logger.info("Structured Logger JSON inicializado");
    }
    
    public static StructuredLogger getInstance() {
        if (instance == null) {
            synchronized (StructuredLogger.class) {
                if (instance == null) {
                    instance = new StructuredLogger();
                }
            }
        }
        return instance;
    }
    
    /**
     * Evento de segurança estruturado
     */
    public static class SecurityEvent {
        private final String eventId;
        private final String eventType;
        private final String severity;
        private final LocalDateTime timestamp;
        private final String userId;
        private final String sessionId;
        private final String ipAddress;
        private final String userAgent;
        private final String resource;
        private final String action;
        private final String result;
        private final String description;
        private final Map<String, Object> details;
        
        public SecurityEvent(String eventType, String severity, String userId, String action, String result, String description) {
            this.eventId = UUID.randomUUID().toString();
            this.eventType = eventType;
            this.severity = severity;
            this.timestamp = LocalDateTime.now();
            this.userId = userId;
            this.sessionId = null;
            this.ipAddress = null;
            this.userAgent = null;
            this.resource = null;
            this.action = action;
            this.result = result;
            this.description = description;
            this.details = new ConcurrentHashMap<>();
        }
        
        // Builders com encadeamento
        public SecurityEvent withSessionId(String sessionId) {
            return new SecurityEvent(eventId, eventType, severity, timestamp, userId, sessionId, ipAddress, userAgent, resource, action, result, description, details);
        }
        
        public SecurityEvent withIpAddress(String ipAddress) {
            return new SecurityEvent(eventId, eventType, severity, timestamp, userId, sessionId, ipAddress, userAgent, resource, action, result, description, details);
        }
        
        public SecurityEvent withUserAgent(String userAgent) {
            return new SecurityEvent(eventId, eventType, severity, timestamp, userId, sessionId, ipAddress, userAgent, resource, action, result, description, details);
        }
        
        public SecurityEvent withResource(String resource) {
            return new SecurityEvent(eventId, eventType, severity, timestamp, userId, sessionId, ipAddress, userAgent, resource, action, result, description, details);
        }
        
        public SecurityEvent withDetail(String key, Object value) {
            details.put(key, value);
            return this;
        }
        
        // Construtor privado para cópia
        private SecurityEvent(String eventId, String eventType, String severity, LocalDateTime timestamp, String userId, String sessionId, String ipAddress, String userAgent, String resource, String action, String result, String description, Map<String, Object> details) {
            this.eventId = eventId;
            this.eventType = eventType;
            this.severity = severity;
            this.timestamp = timestamp;
            this.userId = userId;
            this.sessionId = sessionId;
            this.ipAddress = ipAddress;
            this.userAgent = userAgent;
            this.resource = resource;
            this.action = action;
            this.result = result;
            this.description = description;
            this.details = new ConcurrentHashMap<>(details);
        }
        
        // Getters
        public String getEventId() { return eventId; }
        public String getEventType() { return eventType; }
        public String getSeverity() { return severity; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public String getUserId() { return userId; }
        public String getSessionId() { return sessionId; }
        public String getIpAddress() { return ipAddress; }
        public String getUserAgent() { return userAgent; }
        public String getResource() { return resource; }
        public String getAction() { return action; }
        public String getResult() { return result; }
        public String getDescription() { return description; }
        public Map<String, Object> getDetails() { return details; }
    }
    
    /**
     * Registra evento de segurança
     */
    public void logSecurityEvent(SecurityEvent event) {
        try {
            ObjectNode logEntry = objectMapper.createObjectNode();
            
            // Contexto global
            for (Map.Entry<String, Object> entry : globalContext.entrySet()) {
                logEntry.put(entry.getKey(), entry.getValue().toString());
            }
            
            // Metadados do evento
            logEntry.put("event_id", event.getEventId());
            logEntry.put("event_type", event.getEventType());
            logEntry.put("severity", event.getSeverity());
            logEntry.put("timestamp", event.getTimestamp().format(timestampFormatter));
            
            // Contexto do usuário
            if (event.getUserId() != null) logEntry.put("user_id", event.getUserId());
            if (event.getSessionId() != null) logEntry.put("session_id", event.getSessionId());
            if (event.getIpAddress() != null) logEntry.put("ip_address", event.getIpAddress());
            if (event.getUserAgent() != null) logEntry.put("user_agent", event.getUserAgent());
            
            // Detalhes do evento
            if (event.getResource() != null) logEntry.put("resource", event.getResource());
            if (event.getAction() != null) logEntry.put("action", event.getAction());
            if (event.getResult() != null) logEntry.put("result", event.getResult());
            if (event.getDescription() != null) logEntry.put("description", event.getDescription());
            
            // Detalhes adicionais
            ObjectNode detailsNode = objectMapper.createObjectNode();
            for (Map.Entry<String, Object> entry : event.getDetails().entrySet()) {
                detailsNode.put(entry.getKey(), entry.getValue().toString());
            }
            if (detailsNode.size() > 0) {
                logEntry.set("details", detailsNode);
            }
            
            String jsonLog = objectMapper.writeValueAsString(logEntry);
            
            // Log baseado na severidade
            switch (event.getSeverity().toLowerCase()) {
                case "critical":
                    logger.error(jsonLog);
                    break;
                case "high":
                    logger.warn(jsonLog);
                    break;
                case "medium":
                    logger.info(jsonLog);
                    break;
                case "low":
                    logger.debug(jsonLog);
                    break;
                default:
                    logger.info(jsonLog);
            }
            
        } catch (JsonProcessingException e) {
            logger.error("Erro ao gerar log estruturado JSON", e);
        }
    }
    
    /**
     * Métodos de conveniência para eventos comuns
     */
    public void logAuthenticationEvent(String userId, String action, String result, String description) {
        SecurityEvent event = new SecurityEvent("AUTHENTICATION", "MEDIUM", userId, action, result, description);
        logSecurityEvent(event);
    }
    
    public void logAuthorizationEvent(String userId, String resource, String action, String result, String description) {
        SecurityEvent event = new SecurityEvent("AUTHORIZATION", "MEDIUM", userId, action, result, description)
            .withResource(resource);
        logSecurityEvent(event);
    }
    
    public void logSecurityThreat(String threatType, String severity, String description, Map<String, Object> details) {
        SecurityEvent event = new SecurityEvent(threatType, severity, null, "THREAT_DETECTED", "BLOCKED", description);
        details.forEach(event::withDetail);
        logSecurityEvent(event);
    }
    
    public void logDataAccess(String userId, String resource, String action, String result) {
        SecurityEvent event = new SecurityEvent("DATA_ACCESS", "LOW", userId, action, result, "Data access operation")
            .withResource(resource);
        logSecurityEvent(event);
    }
    
    public void logSystemEvent(String eventType, String severity, String description) {
        SecurityEvent event = new SecurityEvent(eventType, severity, null, "SYSTEM", "INFO", description);
        logSecurityEvent(event);
    }
    
    public void logPerformanceEvent(String operation, long durationMs, boolean success) {
        SecurityEvent event = new SecurityEvent("PERFORMANCE", "LOW", null, operation, success ? "SUCCESS" : "FAILED", 
                                                "Performance metric")
            .withDetail("duration_ms", durationMs)
            .withDetail("success", success);
        logSecurityEvent(event);
    }
    
    /**
     * Adiciona contexto global
     */
    public void addGlobalContext(String key, Object value) {
        globalContext.put(key, value);
    }
    
    /**
     * Remove contexto global
     */
    public void removeGlobalContext(String key) {
        globalContext.remove(key);
    }
    
    /**
     * Obtém contexto global
     */
    public Map<String, Object> getGlobalContext() {
        return new ConcurrentHashMap<>(globalContext);
    }
    
    /**
     * Configuração do logger
     */
    public static class LoggerConfig {
        private final boolean includeStackTrace;
        private final boolean maskSensitiveData;
        private final int maxDetailsSize;
        private final String[] sensitiveFields;
        
        public LoggerConfig(boolean includeStackTrace, boolean maskSensitiveData, int maxDetailsSize, String[] sensitiveFields) {
            this.includeStackTrace = includeStackTrace;
            this.maskSensitiveData = maskSensitiveData;
            this.maxDetailsSize = maxDetailsSize;
            this.sensitiveFields = sensitiveFields;
        }
        
        public boolean shouldIncludeStackTrace() { return includeStackTrace; }
        public boolean shouldMaskSensitiveData() { return maskSensitiveData; }
        public int getMaxDetailsSize() { return maxDetailsSize; }
        public String[] getSensitiveFields() { return sensitiveFields; }
    }
    
    /**
     * Mascara dados sensíveis
     */
    @SuppressWarnings("unused")
    private static String maskSensitiveData(String data) {
        if (data == null || data.length() <= 4) {
            return "****";
        }
        return data.substring(0, 2) + "****" + data.substring(data.length() - 2);
    }
    
    /**
     * Formata para SIEM (Security Information and Event Management)
     */
    public String formatForSIEM(SecurityEvent event) {
        try {
            ObjectNode siemEntry = objectMapper.createObjectNode();
            
            // Formato padrão SIEM
            siemEntry.put("timestamp", event.getTimestamp().format(timestampFormatter));
            siemEntry.put("source", globalContext.get("service").toString());
            siemEntry.put("event_type", event.getEventType());
            siemEntry.put("severity", event.getSeverity());
            siemEntry.put("description", event.getDescription());
            siemEntry.put("user", event.getUserId() != null ? event.getUserId() : "anonymous");
            siemEntry.put("source_ip", event.getIpAddress() != null ? event.getIpAddress() : "unknown");
            siemEntry.put("destination", event.getResource() != null ? event.getResource() : "system");
            siemEntry.put("action", event.getAction());
            siemEntry.put("result", event.getResult());
            
            return objectMapper.writeValueAsString(siemEntry);
        } catch (JsonProcessingException e) {
            logger.error("Erro ao formatar evento para SIEM", e);
            return "{}";
        }
    }
}
