package com.artereal.swing.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gerenciador de Configuração de Segurança baseado em OWASP
 * Implementa A05:2021 - Security Misconfiguration
 */
public class SecurityConfigManager {
    
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfigManager.class);
    
    // Configurações de segurança OWASP
    private static final Map<String, SecurityConfig> securityConfigs = new ConcurrentHashMap<>();
    
    public static class SecurityConfig {
        private final String key;
        private final String value;
        private final boolean isSensitive;
        private final String description;
        
        public SecurityConfig(String key, String value, boolean isSensitive, String description) {
            this.key = key;
            this.value = value;
            this.isSensitive = isSensitive;
            this.description = description;
        }
        
        // Getters
        public String getKey() { return key; }
        public String getValue() { return value; }
        public boolean isSensitive() { return isSensitive; }
        public String description() { return description; }
    }
    
    /**
     * Inicializa configurações de segurança recomendadas OWASP
     */
    public static void initializeSecurityConfigs() {
        logger.info("Inicializando configurações de segurança OWASP");
        
        // Headers de segurança HTTP
        addConfig("security.headers.x-frame-options", "DENY", false, "Prevenção contra clickjacking");
        addConfig("security.headers.x-content-type-options", "nosniff", false, "Prevenção contra MIME-sniffing");
        addConfig("security.headers.x-xss-protection", "1; mode=block", false, "Proteção XSS do browser");
        addConfig("security.headers.strict-transport-security", "max-age=31536000; includeSubDomains", false, "HSTS para HTTPS");
        addConfig("security.headers.content-security-policy", "default-src 'self'", false, "Política de conteúdo seguro");
        addConfig("security.headers.referrer-policy", "strict-origin-when-cross-origin", false, "Política de referer");
        
        // Configurações de sessão
        addConfig("security.session.timeout", "1800", false, "Timeout da sessão em segundos (30 min)");
        addConfig("security.session.cookie-secure", "true", false, "Cookie apenas em HTTPS");
        addConfig("security.session.cookie-http-only", "true", false, "Cookie inacessível via JavaScript");
        addConfig("security.session.cookie-same-site", "Strict", false, "Proteção CSRF");
        
        // Configurações de validação
        addConfig("security.validation.max-login-attempts", "5", false, "Máximo de tentativas de login");
        addConfig("security.validation.lockout-duration", "900", false, "Duração do bloqueio em segundos (15 min)");
        addConfig("security.validation.password-min-length", "8", false, "Comprimento mínimo da senha");
        addConfig("security.validation.password-require-special", "true", false, "Exigir caracteres especiais");
        
        // Configurações de criptografia
        addConfig("security.crypto.algorithm", "AES-GCM", false, "Algoritmo de criptografia");
        addConfig("security.crypto.key-length", "256", false, "Comprimento da chave em bits");
        addConfig("security.crypto.pbkdf2-iterations", "100000", false, "Iterações PBKDF2");
        
        // Configurações de logging
        addConfig("security.logging.level", "INFO", false, "Nível de logging de segurança");
        addConfig("security.logging.include-sensitive", "false", true, "Incluir dados sensíveis no log");
        addConfig("security.logging.max-log-size", "10485760", false, "Tamanho máximo do log em bytes (10MB)");
        
        // Configurações de rate limiting
        addConfig("security.rate-limit.requests-per-minute", "60", false, "Requisições por minuto por IP");
        addConfig("security.rate-limit.burst-size", "10", false, "Tamanho do pico de requisições");
        
        logger.info("Configurações de segurança inicializadas: {} configurações", securityConfigs.size());
    }
    
    /**
     * Adiciona configuração de segurança
     */
    private static void addConfig(String key, String value, boolean isSensitive, String description) {
        securityConfigs.put(key, new SecurityConfig(key, value, isSensitive, description));
    }
    
    /**
     * Obtém configuração de segurança
     */
    public static String getConfig(String key) {
        SecurityConfig config = securityConfigs.get(key);
        if (config == null) {
            logger.warn("Configuração de segurança não encontrada: {}", key);
            return null;
        }
        return config.getValue();
    }
    
    /**
     * Obtém configuração com valor padrão
     */
    public static String getConfig(String key, String defaultValue) {
        String value = getConfig(key);
        return value != null ? value : defaultValue;
    }
    
    /**
     * Obtém configuração como inteiro
     */
    public static int getIntConfig(String key) {
        String value = getConfig(key);
        if (value == null) {
            throw new IllegalArgumentException("Configuração não encontrada: " + key);
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.error("Configuração inválida como inteiro: {} = {}", key, value);
            throw new IllegalArgumentException("Configuração inválida: " + key);
        }
    }
    
    /**
     * Obtém configuração como booleano
     */
    public static boolean getBooleanConfig(String key) {
        String value = getConfig(key);
        if (value == null) {
            throw new IllegalArgumentException("Configuração não encontrada: " + key);
        }
        return Boolean.parseBoolean(value);
    }
    
    /**
     * Valida configurações de segurança OWASP
     */
    public static List<String> validateSecurityConfigs() {
        List<String> issues = new ArrayList<>();
        
        // Validações OWASP recomendadas
        
        // Headers de segurança
        if (!"DENY".equals(getConfig("security.headers.x-frame-options"))) {
            issues.add("X-Frame-Options não está configurado como DENY");
        }
        
        if (!"nosniff".equals(getConfig("security.headers.x-content-type-options"))) {
            issues.add("X-Content-Type-Options não está configurado como nosniff");
        }
        
        if (getConfig("security.headers.strict-transport-security") == null) {
            issues.add("HSTS não está configurado");
        }
        
        // Configurações de sessão
        int sessionTimeout = getIntConfig("security.session.timeout");
        if (sessionTimeout > 3600) { // Mais de 1 hora
            issues.add("Timeout de sessão muito longo: " + sessionTimeout + " segundos");
        }
        
        if (!getBooleanConfig("security.session.cookie-secure")) {
            issues.add("Cookie seguro não está habilitado");
        }
        
        if (!getBooleanConfig("security.session.cookie-http-only")) {
            issues.add("Cookie HTTP-only não está habilitado");
        }
        
        // Configurações de senha
        int passwordMinLength = getIntConfig("security.validation.password-min-length");
        if (passwordMinLength < 8) {
            issues.add("Comprimento mínimo de senha muito curto: " + passwordMinLength);
        }
        
        // Configurações de criptografia
        int keyLength = getIntConfig("security.crypto.key-length");
        if (keyLength < 256) {
            issues.add("Comprimento de chave criptográfica muito curto: " + keyLength);
        }
        
        int pbkdf2Iterations = getIntConfig("security.crypto.pbkdf2-iterations");
        if (pbkdf2Iterations < 100000) {
            issues.add("Iterações PBKDF2 muito baixas: " + pbkdf2Iterations);
        }
        
        // Rate limiting
        int requestsPerMinute = getIntConfig("security.rate-limit.requests-per-minute");
        if (requestsPerMinute > 1000) {
            issues.add("Rate limiting muito permissivo: " + requestsPerMinute + " req/min");
        }
        
        logger.info("Validação de configurações concluída: {} problemas encontrados", issues.size());
        return issues;
    }
    
    /**
     * Gera headers de segurança HTTP
     */
    public static Map<String, String> getSecurityHeaders() {
        Map<String, String> headers = new HashMap<>();
        
        headers.put("X-Frame-Options", getConfig("security.headers.x-frame-options"));
        headers.put("X-Content-Type-Options", getConfig("security.headers.x-content-type-options"));
        headers.put("X-XSS-Protection", getConfig("security.headers.x-xss-protection"));
        headers.put("Strict-Transport-Security", getConfig("security.headers.strict-transport-security"));
        headers.put("Content-Security-Policy", getConfig("security.headers.content-security-policy"));
        headers.put("Referrer-Policy", getConfig("security.headers.referrer-policy"));
        
        return headers;
    }
    
    /**
     * Lista todas as configurações (sem valores sensíveis)
     */
    public static List<SecurityConfig> listConfigs() {
        List<SecurityConfig> configs = new ArrayList<>();
        for (SecurityConfig config : securityConfigs.values()) {
            if (!config.isSensitive()) {
                configs.add(config);
            }
        }
        return configs;
    }
    
    /**
     * Verifica se configuração existe
     */
    public static boolean hasConfig(String key) {
        return securityConfigs.containsKey(key);
    }
    
    /**
     * Atualiza configuração (apenas em modo desenvolvimento)
     */
    public static void updateConfig(String key, String value) {
        SecurityConfig existing = securityConfigs.get(key);
        if (existing != null) {
            securityConfigs.put(key, new SecurityConfig(key, value, existing.isSensitive(), existing.description()));
            logger.info("Configuração atualizada: {} = {}", key, value);
        } else {
            logger.warn("Tentativa de atualizar configuração inexistente: {}", key);
        }
    }
}
