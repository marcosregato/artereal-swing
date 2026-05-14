package com.artereal.swing.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Gerenciador de Content Security Policy (CSP) Headers
 * Implementa OWASP A05: Security Misconfiguration
 * 
 * Previne ataques XSS, injection e outros ataques baseados em conteúdo
 */
public class CSPManager {
    
    private static final Logger logger = LoggerFactory.getLogger(CSPManager.class);
    private static volatile CSPManager instance;
    
    // Configurações CSP padrão OWASP recomendadas
    private static final String DEFAULT_CSP_POLICY = 
        "default-src 'self'; " +
        "script-src 'self' 'unsafe-inline' 'unsafe-eval'; " +  // Necessário para Swing/JavaFX
        "style-src 'self' 'unsafe-inline'; " +
        "img-src 'self' data: https:; " +
        "font-src 'self' data:; " +
        "connect-src 'self'; " +
        "frame-ancestors 'none'; " +
        "base-uri 'self'; " +
        "form-action 'self'; " +
        "upgrade-insecure-requests;";
    
    private static final String STRICT_CSP_POLICY = 
        "default-src 'self'; " +
        "script-src 'self'; " +
        "style-src 'self'; " +
        "img-src 'self' data:; " +
        "font-src 'self'; " +
        "connect-src 'self'; " +
        "frame-ancestors 'none'; " +
        "base-uri 'self'; " +
        "form-action 'self'; " +
        "upgrade-insecure-requests;";
    
    private final Map<String, String> cspHeaders;
    private final boolean strictMode;
    
    private CSPManager() {
        this.strictMode = Boolean.parseBoolean(System.getProperty("csp.strict.mode", "false"));
        this.cspHeaders = new HashMap<>();
        initializeCSPHeaders();
    }
    
    public static CSPManager getInstance() {
        if (instance == null) {
            synchronized (CSPManager.class) {
                if (instance == null) {
                    instance = new CSPManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * Inicializa os headers CSP
     */
    private void initializeCSPHeaders() {
        String policy = strictMode ? STRICT_CSP_POLICY : DEFAULT_CSP_POLICY;
        
        cspHeaders.put("Content-Security-Policy", policy);
        cspHeaders.put("X-Content-Security-Policy", policy); // Para browsers mais antigos
        cspHeaders.put("X-WebKit-CSP", policy); // Para Safari/iOS
        
        // Headers adicionais de segurança
        cspHeaders.put("X-Content-Type-Options", "nosniff");
        cspHeaders.put("X-Frame-Options", "DENY");
        cspHeaders.put("Referrer-Policy", "strict-origin-when-cross-origin");
        cspHeaders.put("Permissions-Policy", 
            "geolocation=(), microphone=(), camera=(), payment=(), usb=(), " +
            "magnetometer=(), gyroscope=(), accelerometer=(), autoplay=()");
        
        logger.info("CSP Headers inicializados - Modo: {}", strictMode ? "STRICT" : "DEFAULT");
    }
    
    /**
     * Obtém todos os headers CSP
     */
    public Map<String, String> getCSPHeaders() {
        return new HashMap<>(cspHeaders);
    }
    
    /**
     * Obtém o header CSP principal
     */
    public String getCSPHeader() {
        return cspHeaders.get("Content-Security-Policy");
    }
    
    /**
     * Valida se uma URL está em conformidade com a política CSP
     */
    public boolean validateURL(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }
        
        try {
            // Remove parâmetros e fragmentos
            String cleanUrl = url.split("[?]")[0].split("#")[0];
            
            // Verifica se é URL relativa ou mesmo domínio
            if (cleanUrl.startsWith("/") || cleanUrl.startsWith("data:")) {
                return true;
            }
            
            // Verifica se é HTTPS (se não for data:)
            if (cleanUrl.startsWith("http://") || cleanUrl.startsWith("https://")) {
                return cleanUrl.startsWith("https://");
            }
            
            return false;
        } catch (Exception e) {
            logger.warn("Erro ao validar URL CSP: {} - {}", url, e.getMessage());
            return false;
        }
    }
    
    /**
     * Gera nonce para scripts inline (se necessário)
     */
    public String generateNonce() {
        if (strictMode) {
            return java.util.Base64.getEncoder()
                .encodeToString(java.security.SecureRandom.getSeed(16));
        }
        return null;
    }
    
    /**
     * Adiciona nonce à política CSP
     */
    public String getCSPHeaderWithNonce(String nonce) {
        if (nonce != null && !nonce.isEmpty()) {
            String basePolicy = getCSPHeader();
            return basePolicy.replace("script-src 'self'", "script-src 'self' 'nonce-" + nonce + "'");
        }
        return getCSPHeader();
    }
    
    /**
     * Verifica se o modo strict está ativado
     */
    public boolean isStrictMode() {
        return strictMode;
    }
    
    /**
     * Valida conteúdo de script
     */
    public boolean validateScriptContent(String content) {
        if (content == null) return false;
        
        // Padrões perigosos que devem ser bloqueados
        String[] dangerousPatterns = {
            "eval\\(",
            "Function\\(",
            "setTimeout\\(",
            "setInterval\\(",
            "document\\.write",
            "innerHTML\\s*=",
            "outerHTML\\s*=",
            "insertAdjacentHTML"
        };
        
        for (String pattern : dangerousPatterns) {
            if (content.matches("(?i).*" + pattern + ".*")) {
                logger.warn("Conteúdo de script perigoso detectado: {}", pattern);
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Configura CSP para ambiente de desenvolvimento
     */
    public void configureForDevelopment() {
        if (System.getProperty("development.mode", "false").equals("true")) {
            logger.info("Configurando CSP para modo desenvolvimento");
            // Em desenvolvimento, permite mais flexibilidade
            String devPolicy = DEFAULT_CSP_POLICY.replace("'unsafe-inline'", "'unsafe-inline' 'unsafe-eval'");
            cspHeaders.put("Content-Security-Policy", devPolicy);
        }
    }
    
    /**
     * Relatório de violação CSP (simulado)
     */
    public void reportViolation(String violation) {
        logger.warn("Violação CSP detectada: {}", violation);
        
        // Em produção, enviar para serviço de monitoramento
        if (!System.getProperty("development.mode", "false").equals("true")) {
            // TODO: Integrar com serviço de monitoramento
            logger.info("Violação CSP registrada para monitoramento");
        }
    }
}
