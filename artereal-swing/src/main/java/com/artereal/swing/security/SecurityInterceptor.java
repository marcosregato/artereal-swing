package com.artereal.swing.security;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interceptador de segurança para monitorar e bloquear tentativas de ataque
 */
public class SecurityInterceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(SecurityInterceptor.class);
    
    // Contadores para detecção de ataques
    private static final ConcurrentHashMap<String, AtomicInteger> ipCounter = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, AtomicInteger> threatCounter = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Long> lastAttemptTime = new ConcurrentHashMap<>();
    
    // Limites de segurança
    private static final int MAX_ATTEMPTS_PER_MINUTE = 10;
    private static final int MAX_THREATS_PER_HOUR = 3; // Reduzido para testar mais rapidamente
    private static final int BLOCK_DURATION_MINUTES = 5;
    
    // Lista de IPs bloqueados
    private static final ConcurrentHashMap<String, Long> blockedIPs = new ConcurrentHashMap<>();
    
    /**
     * Intercepta e valida uma requisição
     */
    public static InterceptionResult interceptRequest(String clientIP, String endpoint, String inputData) {
        // Verifica se IP está bloqueado
        if (isIPBlocked(clientIP)) {
            logger.warn("IP {} bloqueado tentando acessar {}", clientIP, endpoint);
            return new InterceptionResult(false, "IP bloqueado", "BLOCKED_IP");
        }
        
        // Verifica rate limiting
        if (exceedsRateLimit(clientIP)) {
            logger.warn("IP {} excedeu rate limit em {}", clientIP, endpoint);
            blockIP(clientIP);
            return new InterceptionResult(false, "Rate limit excedido", "RATE_LIMIT");
        }
        
        // Escaneia dados de entrada
        SecurityManager.SecurityScanResult scanResult = SecurityManager.scanForThreats(inputData);
        
        if (!scanResult.isSafe()) {
            // Registra tentativa de ataque
            registerThreatAttempt(clientIP);
            
            // Verifica se deve bloquear o IP
            if (shouldBlockIP(clientIP)) {
                blockIP(clientIP);
                logger.warn("IP {} bloqueado por múltiplas tentativas de ataque", clientIP);
                return new InterceptionResult(false, "IP bloqueado por tentativas de ataque", "THREAT_BLOCKED");
            }
            
            return new InterceptionResult(false, "Dados maliciosos detectados", "MALICIOUS_DATA");
        }
        
        // Registra requisição válida
        registerValidRequest(clientIP);
        
        return new InterceptionResult(true, "Requisição segura", "SAFE");
    }
    
    /**
     * Intercepta e valida dados específicos do ArteReal
     */
    public static InterceptionResult interceptArteRealData(String clientIP, String dataType, String data) {
        // Verifica se IP está bloqueado
        if (isIPBlocked(clientIP)) {
            return new InterceptionResult(false, "IP bloqueado", "BLOCKED_IP");
        }
        
        // Validação específica por tipo de dado
        InputValidator.ValidationResult validationResult = validateByType(dataType, data);
        
        if (!validationResult.isValid()) {
            registerThreatAttempt(clientIP);
            return new InterceptionResult(false, validationResult.getMessage(), "INVALID_DATA");
        }
        
        // Escaneamento adicional de segurança
        SecurityManager.SecurityScanResult scanResult = SecurityManager.scanForThreats(data);
        
        if (!scanResult.isSafe()) {
            registerThreatAttempt(clientIP);
            return new InterceptionResult(false, "Dados maliciosos detectados", "MALICIOUS_DATA");
        }
        
        return new InterceptionResult(true, "Dados válidos e seguros", "SAFE");
    }
    
    /**
     * Valida dados por tipo específico
     */
    private static InputValidator.ValidationResult validateByType(String dataType, String data) {
        switch (dataType.toUpperCase()) {
            case "NOME":
                return InputValidator.validateNome(data);
            case "EMAIL":
                return InputValidator.validateEmail(data);
            case "TELEFONE":
                return InputValidator.validateTelefone(data);
            case "CEP":
                return InputValidator.validateCEP(data);
            case "CPF":
                return InputValidator.validateCPF(data);
            case "RG":
                return InputValidator.validateRG(data);
            case "DATA":
                return InputValidator.validateData(data);
            case "VALOR":
                return InputValidator.validateValor(data);
            case "GRAU":
                return InputValidator.validateGrau(data);
            case "CARGO":
                return InputValidator.validateCargo(data);
            case "DESCRICAO":
                return InputValidator.validateDescricao(data);
            case "TIPO_SANGUINEO":
                return InputValidator.validateTipoSanguineo(data);
            default:
                return new InputValidator.ValidationResult(true, "Tipo de dado não especificado, usando validação padrão");
        }
    }
    
    /**
     * Verifica se IP está bloqueado
     */
    public static boolean isIPBlocked(String ip) {
        Long blockTime = blockedIPs.get(ip);
        if (blockTime == null) {
            return false;
        }
        
        long currentTime = System.currentTimeMillis();
        long blockDuration = BLOCK_DURATION_MINUTES * 60 * 1000;
        
        if (currentTime - blockTime > blockDuration) {
            // Bloqueio expirou
            blockedIPs.remove(ip);
            return false;
        }
        
        return true;
    }
    
    /**
     * Verifica se IP excedeu rate limit
     */
    private static boolean exceedsRateLimit(String ip) {
        AtomicInteger counter = ipCounter.computeIfAbsent(ip, k -> new AtomicInteger(0));
        Long lastTime = lastAttemptTime.get(ip);
        
        long currentTime = System.currentTimeMillis();
        long oneMinute = 60 * 1000;
        
        if (lastTime != null && currentTime - lastTime > oneMinute) {
            // Reset counter após um minuto
            counter.set(0);
        }
        
        lastAttemptTime.put(ip, currentTime);
        
        return counter.incrementAndGet() > MAX_ATTEMPTS_PER_MINUTE;
    }
    
    /**
     * Registra tentativa de ameaça
     */
    private static void registerThreatAttempt(String ip) {
        AtomicInteger counter = threatCounter.computeIfAbsent(ip, k -> new AtomicInteger(0));
        counter.incrementAndGet();
    }
    
    /**
     * Verifica se deve bloquear IP por tentativas de ameaça
     */
    private static boolean shouldBlockIP(String ip) {
        AtomicInteger counter = threatCounter.get(ip);
        return counter != null && counter.get() >= MAX_THREATS_PER_HOUR;
    }
    
    /**
     * Bloqueia IP
     */
    private static void blockIP(String ip) {
        blockedIPs.put(ip, System.currentTimeMillis());
        logger.warn("IP {} bloqueado por violação de segurança", ip);
    }
    
    /**
     * Registra requisição válida
     */
    private static void registerValidRequest(String ip) {
        // Reset contadores de ameaça para requisições válidas
        threatCounter.remove(ip);
    }
    
    /**
     * Limpa contadores antigos
     */
    public static void cleanup() {
        long currentTime = System.currentTimeMillis();
        long oneHour = 60 * 60 * 1000;
        
        // Limpa contadores de IP
        ipCounter.entrySet().removeIf(entry -> {
            Long lastTime = lastAttemptTime.get(entry.getKey());
            return lastTime != null && currentTime - lastTime > oneHour;
        });
        
        // Limpa contadores de ameaça
        threatCounter.entrySet().removeIf(entry -> {
            Long lastTime = lastAttemptTime.get(entry.getKey());
            return lastTime != null && currentTime - lastTime > oneHour;
        });
        
        // Limpa bloqueios expirados
        blockedIPs.entrySet().removeIf(entry -> {
            long blockDuration = BLOCK_DURATION_MINUTES * 60 * 1000;
            return currentTime - entry.getValue() > blockDuration;
        });
    }
    
    /**
     * Registra ameaça para testes OWASP
     */
    public static void recordThreat(String ip) {
        AtomicInteger threats = threatCounter.computeIfAbsent(ip, k -> new AtomicInteger(0));
        int count = threats.incrementAndGet();
        
        if (count >= MAX_THREATS_PER_HOUR) {
            blockIP(ip);
        }
        
        logger.warn("Ameaça registrada para IP {}: {}/{}", ip, count, MAX_THREATS_PER_HOUR);
    }
    
    /**
     * Limpa IPs bloqueados para testes
     */
    public static void clearBlockedIPs() {
        blockedIPs.clear();
        logger.info("IPs bloqueados limpos para testes");
    }
    
    public static class InterceptionResult {
        private final boolean allowed;
        private final String message;
        private final String reason;
        
        public InterceptionResult(boolean allowed, String message, String reason) {
            this.allowed = allowed;
            this.message = message;
            this.reason = reason;
        }
        
        public boolean isAllowed() {
            return allowed;
        }
        
        public String getMessage() {
            return message;
        }
        
        public String getReason() {
            return reason;
        }
        
        @Override
        public String toString() {
            return allowed ? "ALLOWED: " + message : "BLOCKED: " + message + " (" + reason + ")";
        }
    }
    
    /**
     * Estatísticas de segurança
     */
    public static class SecurityStats {
        private final int totalIPs;
        private final int blockedIPs;
        private final int threatAttempts;
        
        public SecurityStats(int totalIPs, int blockedIPs, int threatAttempts) {
            this.totalIPs = totalIPs;
            this.blockedIPs = blockedIPs;
            this.threatAttempts = threatAttempts;
        }
        
        public int getTotalIPs() {
            return totalIPs;
        }
        
        public int getBlockedIPs() {
            return blockedIPs;
        }
        
        public int getThreatAttempts() {
            return threatAttempts;
        }
        
        @Override
        public String toString() {
            return String.format("SecurityStats{IPs: %d, Blocked: %d, Threats: %d}", 
                               totalIPs, blockedIPs, threatAttempts);
        }
    }
    
    /**
     * Obtém estatísticas de segurança para testes
     */
    public static SecurityStats getSecurityStats() {
        return new SecurityStats(
            ipCounter.size(),
            blockedIPs.size(),
            threatCounter.values().stream().mapToInt(AtomicInteger::get).sum()
        );
    }
}
