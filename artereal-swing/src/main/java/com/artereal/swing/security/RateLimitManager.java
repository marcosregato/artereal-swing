package com.artereal.swing.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Gerenciador de Rate Limiting avançado para endpoints críticos
 * Implementa OWASP A07: Identification and Authentication Failures
 * 
 * Protege contra ataques de força bruta, DoS e abuso de API
 */
public class RateLimitManager {
    
    private static final Logger logger = LoggerFactory.getLogger(RateLimitManager.class);
    private static volatile RateLimitManager instance;
    
    // Configurações padrão
    private static final int DEFAULT_REQUESTS_PER_MINUTE = 60;
    private static final int DEFAULT_REQUESTS_PER_HOUR = 1000;
    private static final int DEFAULT_REQUESTS_PER_DAY = 10000;
    private static final int DEFAULT_BURST_CAPACITY = 10;
    
    // Estruturas de dados para rate limiting
    private final ConcurrentHashMap<String, RateLimitInfo> rateLimitMap;
    private final ConcurrentHashMap<String, EndpointConfig> endpointConfigs;
    
    private RateLimitManager() {
        this.rateLimitMap = new ConcurrentHashMap<>();
        this.endpointConfigs = new ConcurrentHashMap<>();
        initializeDefaultEndpoints();
    }
    
    public static RateLimitManager getInstance() {
        if (instance == null) {
            synchronized (RateLimitManager.class) {
                if (instance == null) {
                    instance = new RateLimitManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * Configurações de rate limit por endpoint
     */
    private static class EndpointConfig {
        final int requestsPerMinute;
        final int requestsPerHour;
        final int requestsPerDay;
        final int burstCapacity;
        @SuppressWarnings("unused")
        final boolean critical;
        
        EndpointConfig(int rpm, int rph, int rpd, int burst, boolean critical) {
            this.requestsPerMinute = rpm;
            this.requestsPerHour = rph;
            this.requestsPerDay = rpd;
            this.burstCapacity = burst;
            this.critical = critical;
        }
    }
    
    /**
     * Informações de rate limit por cliente
     */
    private static class RateLimitInfo {
        final AtomicInteger minuteCounter = new AtomicInteger(0);
        final AtomicInteger hourCounter = new AtomicInteger(0);
        final AtomicInteger dayCounter = new AtomicInteger(0);
        final AtomicInteger burstCounter = new AtomicInteger(0);
        
        final AtomicLong lastMinuteReset = new AtomicLong(System.currentTimeMillis());
        final AtomicLong lastHourReset = new AtomicLong(System.currentTimeMillis());
        final AtomicLong lastDayReset = new AtomicLong(System.currentTimeMillis());
        final AtomicLong lastBurstReset = new AtomicLong(System.currentTimeMillis());
        
        volatile LocalDateTime blockedUntil = null;
        volatile int violationCount = 0;
        
        void resetCountersIfNeeded() {
            long now = System.currentTimeMillis();
            
            // Reset minute counter
            if (now - lastMinuteReset.get() > 60000) {
                minuteCounter.set(0);
                lastMinuteReset.set(now);
            }
            
            // Reset hour counter
            if (now - lastHourReset.get() > 3600000) {
                hourCounter.set(0);
                lastHourReset.set(now);
            }
            
            // Reset day counter
            if (now - lastDayReset.get() > 86400000) {
                dayCounter.set(0);
                lastDayReset.set(now);
            }
            
            // Reset burst counter (10 segundos)
            if (now - lastBurstReset.get() > 10000) {
                burstCounter.set(0);
                lastBurstReset.set(now);
            }
        }
        
        boolean isBlocked() {
            return blockedUntil != null && LocalDateTime.now().isBefore(blockedUntil);
        }
    }
    
    /**
     * Inicializa configurações padrão para endpoints críticos
     */
    private void initializeDefaultEndpoints() {
        // Endpoints de autenticação - mais restritivos
        endpointConfigs.put("/auth/login", new EndpointConfig(5, 20, 100, 3, true));
        endpointConfigs.put("/auth/logout", new EndpointConfig(10, 50, 200, 5, true));
        endpointConfigs.put("/auth/reset-password", new EndpointConfig(3, 10, 50, 2, true));
        
        // Endpoints de dados críticos
        endpointConfigs.put("/api/users", new EndpointConfig(20, 200, 1000, 10, true));
        endpointConfigs.put("/api/financial", new EndpointConfig(15, 150, 800, 8, true));
        endpointConfigs.put("/api/admin", new EndpointConfig(10, 100, 500, 5, true));
        
        // Endpoints de uploads - muito restritivos
        endpointConfigs.put("/api/upload", new EndpointConfig(5, 25, 100, 2, true));
        endpointConfigs.put("/api/export", new EndpointConfig(3, 15, 50, 2, true));
        
        // Endpoints públicos - menos restritivos
        endpointConfigs.put("/api/public", new EndpointConfig(100, 1000, 10000, 20, false));
        endpointConfigs.put("/api/info", new EndpointConfig(50, 500, 5000, 15, false));
        
        logger.info("Configurações de Rate Limit inicializadas para {} endpoints", endpointConfigs.size());
    }
    
    /**
     * Verifica se uma requisição deve ser permitida
     */
    public RateLimitResult checkRateLimit(String clientId, String endpoint) {
        EndpointConfig config = endpointConfigs.getOrDefault(endpoint, getDefaultConfig());
        RateLimitInfo info = rateLimitMap.computeIfAbsent(clientId + ":" + endpoint, k -> new RateLimitInfo());
        
        // Verifica se está bloqueado
        if (info.isBlocked()) {
            logger.warn("Cliente {} bloqueado para endpoint {} até {}", clientId, endpoint, info.blockedUntil);
            return RateLimitResult.blocked("Cliente temporariamente bloqueado devido a múltiplas violações");
        }
        
        // Reset contadores se necessário
        info.resetCountersIfNeeded();
        
        // Verifica limites
        if (info.minuteCounter.get() >= config.requestsPerMinute) {
            return handleViolation(info, clientId, endpoint, "Limite de requisições por minuto excedido");
        }
        
        if (info.hourCounter.get() >= config.requestsPerHour) {
            return handleViolation(info, clientId, endpoint, "Limite de requisições por hora excedido");
        }
        
        if (info.dayCounter.get() >= config.requestsPerDay) {
            return handleViolation(info, clientId, endpoint, "Limite de requisições por dia excedido");
        }
        
        if (info.burstCounter.get() >= config.burstCapacity) {
            return handleViolation(info, clientId, endpoint, "Capacidade de burst excedida");
        }
        
        // Incrementa contadores
        info.minuteCounter.incrementAndGet();
        info.hourCounter.incrementAndGet();
        info.dayCounter.incrementAndGet();
        info.burstCounter.incrementAndGet();
        
        return RateLimitResult.allowed();
    }
    
    /**
     * Trata violação de rate limit
     */
    private RateLimitResult handleViolation(RateLimitInfo info, String clientId, String endpoint, String reason) {
        info.violationCount++;
        
        // Calcula tempo de bloqueio baseado no número de violações
        int blockMinutes = Math.min(60, info.violationCount * 5); // 5 min por violação, máximo 1 hora
        info.blockedUntil = LocalDateTime.now().plusMinutes(blockMinutes);
        
        // Registra violação
        logger.warn("Violação de Rate Limit - Cliente: {}, Endpoint: {}, Razão: {}, Bloqueado por {} minutos", 
                   clientId, endpoint, reason, blockMinutes);
        
        // Em produção, enviar alerta para equipe de segurança
        if (info.violationCount >= 5) {
            logger.error("ALERTA DE SEGURANÇA: Múltiplas violações detectadas - Cliente: {}, Endpoint: {}", clientId, endpoint);
        }
        
        return RateLimitResult.blocked(reason + ". Cliente bloqueado por " + blockMinutes + " minutos");
    }
    
    /**
     * Configuração padrão para endpoints não configurados
     */
    private EndpointConfig getDefaultConfig() {
        return new EndpointConfig(DEFAULT_REQUESTS_PER_MINUTE, DEFAULT_REQUESTS_PER_HOUR, 
                               DEFAULT_REQUESTS_PER_DAY, DEFAULT_BURST_CAPACITY, false);
    }
    
    /**
     * Adiciona configuração personalizada para endpoint
     */
    public void configureEndpoint(String endpoint, int requestsPerMinute, int requestsPerHour, 
                                int requestsPerDay, int burstCapacity, boolean critical) {
        endpointConfigs.put(endpoint, new EndpointConfig(requestsPerMinute, requestsPerHour, 
                                                       requestsPerDay, burstCapacity, critical));
        logger.info("Endpoint {} configurado com rate limit customizado", endpoint);
    }
    
    /**
     * Remove bloqueio de cliente (uso administrativo)
     */
    public void unblockClient(String clientId, String endpoint) {
        String key = clientId + ":" + endpoint;
        RateLimitInfo info = rateLimitMap.get(key);
        if (info != null) {
            info.blockedUntil = null;
            info.violationCount = 0;
            logger.info("Cliente {} desbloqueado para endpoint {}", clientId, endpoint);
        }
    }
    
    /**
     * Obtém estatísticas de rate limit
     */
    public RateLimitStats getStats(String clientId, String endpoint) {
        String key = clientId + ":" + endpoint;
        RateLimitInfo info = rateLimitMap.get(key);
        EndpointConfig config = endpointConfigs.get(endpoint);
        
        if (info == null || config == null) {
            return new RateLimitStats(0, 0, 0, 0, false, 0);
        }
        
        info.resetCountersIfNeeded();
        
        return new RateLimitStats(
            info.minuteCounter.get(),
            info.hourCounter.get(),
            info.dayCounter.get(),
            info.burstCounter.get(),
            info.isBlocked(),
            info.violationCount
        );
    }
    
    /**
     * Limpa dados antigos de rate limit
     */
    public void cleanup() {
        long now = System.currentTimeMillis();
        rateLimitMap.entrySet().removeIf(entry -> {
            RateLimitInfo info = entry.getValue();
            return now - info.lastDayReset.get() > 172800000; // Remove após 2 dias
        });
        logger.debug("Cleanup de Rate Limit executado");
    }
    
    /**
     * Resultado da verificação de rate limit
     */
    public static class RateLimitResult {
        private final boolean allowed;
        private final String reason;
        
        private RateLimitResult(boolean allowed, String reason) {
            this.allowed = allowed;
            this.reason = reason;
        }
        
        public static RateLimitResult allowed() {
            return new RateLimitResult(true, null);
        }
        
        public static RateLimitResult blocked(String reason) {
            return new RateLimitResult(false, reason);
        }
        
        public boolean isAllowed() { return allowed; }
        public String getReason() { return reason; }
    }
    
    /**
     * Estatísticas de rate limit
     */
    public static class RateLimitStats {
        private final int requestsPerMinute;
        private final int requestsPerHour;
        private final int requestsPerDay;
        private final int burstRequests;
        private final boolean blocked;
        private final int violationCount;
        
        public RateLimitStats(int rpm, int rph, int rpd, int burst, boolean blocked, int violations) {
            this.requestsPerMinute = rpm;
            this.requestsPerHour = rph;
            this.requestsPerDay = rpd;
            this.burstRequests = burst;
            this.blocked = blocked;
            this.violationCount = violations;
        }
        
        // Getters
        public int getRequestsPerMinute() { return requestsPerMinute; }
        public int getRequestsPerHour() { return requestsPerHour; }
        public int getRequestsPerDay() { return requestsPerDay; }
        public int getBurstRequests() { return burstRequests; }
        public boolean isBlocked() { return blocked; }
        public int getViolationCount() { return violationCount; }
    }
}
