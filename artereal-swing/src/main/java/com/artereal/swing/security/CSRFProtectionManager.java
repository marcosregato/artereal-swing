package com.artereal.swing.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gerenciador de CSRF Protection para formulários
 * Implementa OWASP A01: Broken Access Control
 * 
 * Previne ataques Cross-Site Request Forgery em formulários web
 */
public class CSRFProtectionManager {
    
    private static final Logger logger = LoggerFactory.getLogger(CSRFProtectionManager.class);
    private static volatile CSRFProtectionManager instance;
    
    private static final int TOKEN_LENGTH = 32;
    private static final int TOKEN_EXPIRY_MINUTES = 60;
    private static final int MAX_TOKENS_PER_SESSION = 10;
    
    private final SecureRandom secureRandom;
    private final ConcurrentHashMap<String, CSRFToken> tokenStore;
    
    private CSRFProtectionManager() {
        this.secureRandom = new SecureRandom();
        this.tokenStore = new ConcurrentHashMap<>();
        logger.info("CSRF Protection Manager inicializado");
    }
    
    public static CSRFProtectionManager getInstance() {
        if (instance == null) {
            synchronized (CSRFProtectionManager.class) {
                if (instance == null) {
                    instance = new CSRFProtectionManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * Token CSRF com metadados
     */
    private static class CSRFToken {
        @SuppressWarnings("unused")
        final String token;
        final LocalDateTime createdAt;
        final LocalDateTime expiresAt;
        final String sessionId;
        final String userAgent;
        final String ipAddress;
        int usageCount;
        
        CSRFToken(String token, String sessionId, String userAgent, String ipAddress) {
            this.token = token;
            this.sessionId = sessionId;
            this.userAgent = userAgent;
            this.ipAddress = ipAddress;
            this.createdAt = LocalDateTime.now();
            this.expiresAt = createdAt.plusMinutes(TOKEN_EXPIRY_MINUTES);
            this.usageCount = 0;
        }
        
        boolean isValid() {
            return LocalDateTime.now().isBefore(expiresAt) && usageCount < 2; // Token pode ser usado 2 vezes
        }
    }
    
    /**
     * Gera um novo token CSRF
     */
    public String generateToken(String sessionId, String userAgent, String ipAddress) {
        // Limpa tokens expirados
        cleanupExpiredTokens();
        
        // Verifica limite de tokens por sessão
        long sessionTokenCount = tokenStore.values().stream()
            .filter(token -> token.sessionId.equals(sessionId) && token.isValid())
            .count();
        
        if (sessionTokenCount >= MAX_TOKENS_PER_SESSION) {
            logger.warn("Limite de tokens CSRF excedido para sessão: {}", sessionId);
            throw new SecurityException("Limite de tokens CSRF excedido");
        }
        
        // Gera token seguro
        byte[] randomBytes = new byte[TOKEN_LENGTH];
        secureRandom.nextBytes(randomBytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        
        // Armazena token
        CSRFToken csrfToken = new CSRFToken(token, sessionId, userAgent, ipAddress);
        tokenStore.put(token, csrfToken);
        
        logger.debug("Token CSRF gerado para sessão: {}", sessionId);
        return token;
    }
    
    /**
     * Valida um token CSRF
     */
    public CSRFValidationResult validateToken(String token, String sessionId, String userAgent, String ipAddress) {
        if (token == null || token.trim().isEmpty()) {
            return CSRFValidationResult.invalid("Token CSRF ausente");
        }
        
        CSRFToken storedToken = tokenStore.get(token);
        if (storedToken == null) {
            logger.warn("Token CSRF não encontrado: {}", token.substring(0, Math.min(10, token.length())) + "...");
            return CSRFValidationResult.invalid("Token CSRF inválido ou não encontrado");
        }
        
        // Verifica expiração
        if (!storedToken.isValid()) {
            logger.warn("Token CSRF expirado para sessão: {}", sessionId);
            tokenStore.remove(token);
            return CSRFValidationResult.invalid("Token CSRF expirado");
        }
        
        // Verifica correspondência de sessão
        if (!storedToken.sessionId.equals(sessionId)) {
            logger.error("Tentativa de uso de token CSRF de outra sessão! Esperado: {}, Recebido: {}", 
                        storedToken.sessionId, sessionId);
            return CSRFValidationResult.invalid("Token CSRF inválido para esta sessão");
        }
        
        // Verifica User-Agent (proteção adicional)
        if (storedToken.userAgent != null && userAgent != null && 
            !storedToken.userAgent.equals(userAgent)) {
            logger.warn("User-Agent diferente do token CSRF. Esperado: {}, Recebido: {}", 
                       storedToken.userAgent, userAgent);
            return CSRFValidationResult.invalid("Token CSRF inválido - User-Agent mismatch");
        }
        
        // Verifica IP (proteção adicional - pode ser configurável)
        if (storedToken.ipAddress != null && ipAddress != null && 
            !storedToken.ipAddress.equals(ipAddress) && 
            !isPrivateNetwork(storedToken.ipAddress) && 
            !isPrivateNetwork(ipAddress)) {
            logger.warn("IP diferente do token CSRF. Esperado: {}, Recebido: {}", 
                       storedToken.ipAddress, ipAddress);
            return CSRFValidationResult.invalid("Token CSRF inválido - IP mismatch");
        }
        
        // Incrementa uso
        storedToken.usageCount++;
        
        // Remove token após segundo uso
        if (storedToken.usageCount >= 2) {
            tokenStore.remove(token);
            logger.debug("Token CSRF removido após uso: {}", token.substring(0, Math.min(10, token.length())) + "...");
        }
        
        logger.debug("Token CSRF validado para sessão: {}", sessionId);
        return CSRFValidationResult.valid();
    }
    
    /**
     * Verifica se IP é de rede privada (para não bloquear usuários de IP dinâmico)
     */
    private boolean isPrivateNetwork(String ip) {
        if (ip == null) return false;
        
        return ip.startsWith("192.168.") || 
               ip.startsWith("10.") || 
               ip.startsWith("172.") ||
               ip.equals("127.0.0.1") ||
               ip.startsWith("169.254.") ||
               ip.startsWith("::1") ||
               ip.startsWith("fc00:") ||
               ip.startsWith("fe80:");
    }
    
    /**
     * Revoga todos os tokens de uma sessão
     */
    public void revokeSessionTokens(String sessionId) {
        final int[] removedCount = {0};
        tokenStore.entrySet().removeIf(entry -> {
            boolean shouldRemove = entry.getValue().sessionId.equals(sessionId);
            if (shouldRemove) removedCount[0]++;
            return shouldRemove;
        });
        
        logger.info("Revogados {} tokens CSRF da sessão: {}", removedCount[0], sessionId);
    }
    
    /**
     * Revoga um token específico
     */
    public void revokeToken(String token) {
        CSRFToken removed = tokenStore.remove(token);
        if (removed != null) {
            logger.info("Token CSRF revogado: {}", token.substring(0, Math.min(10, token.length())) + "...");
        }
    }
    
    /**
     * Limpa tokens expirados
     */
    public void cleanupExpiredTokens() {
        final int[] removedCount = {0};
        
        tokenStore.entrySet().removeIf(entry -> {
            boolean shouldRemove = !entry.getValue().isValid();
            if (shouldRemove) removedCount[0]++;
            return shouldRemove;
        });
        
        if (removedCount[0] > 0) {
            logger.debug("Removidos {} tokens CSRF expirados", removedCount[0]);
        }
    }
    
    /**
     * Obtém estatísticas de tokens
     */
    public CSRFStats getStats() {
        cleanupExpiredTokens();
        
        int totalTokens = tokenStore.size();
        int expiredTokens = (int) tokenStore.values().stream()
            .filter(token -> !token.isValid())
            .count();
        int validTokens = totalTokens - expiredTokens;
        
        return new CSRFStats(totalTokens, validTokens, expiredTokens);
    }
    
    /**
     * Gera header CSRF para resposta HTTP
     */
    public String generateCSRFHeader(String token) {
        return token;
    }
    
    /**
     * Verifica se requisição precisa de proteção CSRF
     */
    public boolean requiresCSRFProtection(String method, String path) {
        // Métodos seguros não precisam de CSRF
        if ("GET".equalsIgnoreCase(method) || "HEAD".equalsIgnoreCase(method) || 
            "OPTIONS".equalsIgnoreCase(method) || "TRACE".equalsIgnoreCase(method)) {
            return false;
        }
        
        // Endpoints públicos podem ser configurados para não precisar de CSRF
        if (path != null && (path.startsWith("/api/public/") || 
                           path.startsWith("/api/info/") ||
                           path.startsWith("/static/"))) {
            return false;
        }
        
        // Endpoints de API geralmente precisam de CSRF
        return path != null && (path.startsWith("/api/") || path.startsWith("/admin/"));
    }
    
    /**
     * Resultado da validação CSRF
     */
    public static class CSRFValidationResult {
        private final boolean valid;
        private final String errorMessage;
        
        private CSRFValidationResult(boolean valid, String errorMessage) {
            this.valid = valid;
            this.errorMessage = errorMessage;
        }
        
        public static CSRFValidationResult valid() {
            return new CSRFValidationResult(true, null);
        }
        
        public static CSRFValidationResult invalid(String errorMessage) {
            return new CSRFValidationResult(false, errorMessage);
        }
        
        public boolean isValid() { return valid; }
        public String getErrorMessage() { return errorMessage; }
    }
    
    /**
     * Estatísticas de CSRF
     */
    public static class CSRFStats {
        private final int totalTokens;
        private final int validTokens;
        private final int expiredTokens;
        
        public CSRFStats(int total, int valid, int expired) {
            this.totalTokens = total;
            this.validTokens = valid;
            this.expiredTokens = expired;
        }
        
        public int getTotalTokens() { return totalTokens; }
        public int getValidTokens() { return validTokens; }
        public int getExpiredTokens() { return expiredTokens; }
    }
    
    /**
     * Configuração de proteção CSRF para formulários específicos
     */
    public static class CSRFConfig {
        private final boolean enabled;
        private final int tokenExpiryMinutes;
        private final int maxTokensPerSession;
        private final boolean validateUserAgent;
        private final boolean validateIPAddress;
        
        public CSRFConfig(boolean enabled, int expiryMinutes, int maxTokens, boolean validateUA, boolean validateIP) {
            this.enabled = enabled;
            this.tokenExpiryMinutes = expiryMinutes;
            this.maxTokensPerSession = maxTokens;
            this.validateUserAgent = validateUA;
            this.validateIPAddress = validateIP;
        }
        
        public boolean isEnabled() { return enabled; }
        public int getTokenExpiryMinutes() { return tokenExpiryMinutes; }
        public int getMaxTokensPerSession() { return maxTokensPerSession; }
        public boolean shouldValidateUserAgent() { return validateUserAgent; }
        public boolean shouldValidateIPAddress() { return validateIPAddress; }
    }
}
