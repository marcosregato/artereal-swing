package com.artereal.swing.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes para os componentes avançados de segurança OWASP
 * CSP, Rate Limiting, CSRF, Logging Estruturado, Upload Integrity, Config Monitor
 */
@DisplayName("Testes de Segurança Avançada OWASP")
class AdvancedSecurityTest {

    private CSPManager cspManager;
    private RateLimitManager rateLimitManager;
    private CSRFProtectionManager csrfManager;
    private StructuredLogger structuredLogger;
    private UploadIntegrityManager uploadManager;
    private ConfigurationMonitor configMonitor;

    @BeforeEach
    void setUp() {
        System.setProperty("test.environment", "true");
        
        cspManager = CSPManager.getInstance();
        rateLimitManager = RateLimitManager.getInstance();
        csrfManager = CSRFProtectionManager.getInstance();
        structuredLogger = StructuredLogger.getInstance();
        uploadManager = UploadIntegrityManager.getInstance();
        configMonitor = ConfigurationMonitor.getInstance();
    }

    @AfterEach
    void tearDown() {
        // Cleanup dos managers
        if (configMonitor != null) {
            configMonitor.shutdown();
        }
    }

    // ========== CSP MANAGER TESTS ==========
    
    @Test
    @DisplayName("CSP Manager - Headers Básicos")
    void testCSPManagerBasicHeaders() {
        Map<String, String> headers = cspManager.getCSPHeaders();
        
        assertThat(headers).isNotEmpty();
        assertThat(headers).containsKey("Content-Security-Policy");
        assertThat(headers).containsKey("X-Content-Type-Options");
        assertThat(headers).containsKey("X-Frame-Options");
        assertThat(headers).containsKey("Referrer-Policy");
        
        String cspHeader = headers.get("Content-Security-Policy");
        assertThat(cspHeader).contains("default-src 'self'");
        assertThat(cspHeader).contains("frame-ancestors 'none'");
        assertThat(cspHeader).contains("upgrade-insecure-requests");
    }

    @Test
    @DisplayName("CSP Manager - Validação de URL")
    void testCSPURLValidation() {
        // URLs permitidas
        assertThat(cspManager.validateURL("/api/users")).isTrue();
        assertThat(cspManager.validateURL("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8/5+hHgAHggJ/PchI7wAAAABJRU5ErkJggg==")).isTrue();
        assertThat(cspManager.validateURL("https://trusted-domain.com")).isTrue();
        
        // URLs não permitidas
        assertThat(cspManager.validateURL("http://untrusted.com")).isFalse();
        assertThat(cspManager.validateURL(null)).isFalse();
        assertThat(cspManager.validateURL("")).isFalse();
    }

    @Test
    @DisplayName("CSP Manager - Validação de Script")
    void testCSPScriptValidation() {
        // Scripts seguros
        assertThat(cspManager.validateScriptContent("console.log('hello')")).isTrue();
        assertThat(cspManager.validateScriptContent("document.getElementById('test')")).isTrue();
        
        // Scripts perigosos
        assertThat(cspManager.validateScriptContent("eval('malicious code')")).isFalse();
        assertThat(cspManager.validateScriptContent("Function('dangerous')()")).isFalse();
        assertThat(cspManager.validateScriptContent("document.write('xss')")).isFalse();
    }

    // ========== RATE LIMIT MANAGER TESTS ==========

    @Test
    @DisplayName("Rate Limit Manager - Limites Básicos")
    void testRateLimitBasicLimits() {
        String clientId = "test-client-1";
        String endpoint = "/api/login"; // Endpoint crítico com limite baixo
        
        // Configura endpoint customizado com limite baixo e burst ajustado
        rateLimitManager.configureEndpoint("/api/login", 3, 10, 30, 2, true);
        
        // Primeiras requisições devem ser permitidas
        RateLimitManager.RateLimitResult result1 = rateLimitManager.checkRateLimit(clientId, endpoint);
        RateLimitManager.RateLimitResult result2 = rateLimitManager.checkRateLimit(clientId, endpoint);
        RateLimitManager.RateLimitResult result3 = rateLimitManager.checkRateLimit(clientId, endpoint);
        
        assertThat(result1.isAllowed()).isTrue();
        assertThat(result2.isAllowed()).isTrue();
        assertThat(result3.isAllowed()).isTrue();
        
        // Próxima requisição deve ser bloqueada
        RateLimitManager.RateLimitResult result4 = rateLimitManager.checkRateLimit(clientId, endpoint);
        assertThat(result4.isAllowed()).isFalse();
        assertThat(result4.getReason()).contains("Limite de requisições");
    }

    @Test
    @DisplayName("Rate Limit Manager - Estatísticas")
    void testRateLimitStats() {
        String clientId = "test-client-stats";
        String endpoint = "/api/users"; // Endpoint com configuração padrão
        
        // Faz algumas requisições
        for (int i = 0; i < 3; i++) {
            rateLimitManager.checkRateLimit(clientId, endpoint);
        }
        
        RateLimitManager.RateLimitStats stats = rateLimitManager.getStats(clientId, endpoint);
        assertThat(stats.getRequestsPerMinute()).isEqualTo(3);
        assertThat(stats.isBlocked()).isFalse();
        assertThat(stats.getViolationCount()).isEqualTo(0);
    }

    // ========== CSRF PROTECTION TESTS ==========

    @Test
    @DisplayName("CSRF Protection - Geração e Validação")
    void testCSRFTokenGenerationAndValidation() {
        String sessionId = "test-session-1";
        String userAgent = "Mozilla/5.0 Test Browser";
        String ipAddress = "127.0.0.1";
        
        // Gera token
        String token = csrfManager.generateToken(sessionId, userAgent, ipAddress);
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        
        // Valida token correto
        CSRFProtectionManager.CSRFValidationResult validResult = csrfManager.validateToken(token, sessionId, userAgent, ipAddress);
        assertThat(validResult.isValid()).isTrue();
        
        // Valida token incorreto
        CSRFProtectionManager.CSRFValidationResult invalidResult = csrfManager.validateToken("invalid-token", sessionId, userAgent, ipAddress);
        assertThat(invalidResult.isValid()).isFalse();
        assertThat(invalidResult.getErrorMessage()).contains("inválido");
    }

    @Test
    @DisplayName("CSRF Protection - Validação de Sessão")
    void testCSRFSessionValidation() {
        String sessionId1 = "session-1";
        String sessionId2 = "session-2";
        String userAgent = "Test Browser";
        String ipAddress = "127.0.0.1";
        
        // Gera token para sessão 1
        String token = csrfManager.generateToken(sessionId1, userAgent, ipAddress);
        
        // Tenta usar token na sessão 2 (deve falhar)
        CSRFProtectionManager.CSRFValidationResult result = csrfManager.validateToken(token, sessionId2, userAgent, ipAddress);
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrorMessage()).contains("sessão");
    }

    @Test
    @DisplayName("CSRF Protection - Requerimento de Proteção")
    void testCSRFProtectionRequirement() {
        // Métodos seguros não precisam de CSRF
        assertThat(csrfManager.requiresCSRFProtection("GET", "/api/data")).isFalse();
        assertThat(csrfManager.requiresCSRFProtection("HEAD", "/api/data")).isFalse();
        assertThat(csrfManager.requiresCSRFProtection("OPTIONS", "/api/data")).isFalse();
        
        // Métodos inseguros precisam de CSRF
        assertThat(csrfManager.requiresCSRFProtection("POST", "/api/data")).isTrue();
        assertThat(csrfManager.requiresCSRFProtection("PUT", "/api/data")).isTrue();
        assertThat(csrfManager.requiresCSRFProtection("DELETE", "/api/data")).isTrue();
        
        // Endpoints públicos não precisam de CSRF
        assertThat(csrfManager.requiresCSRFProtection("POST", "/api/public/info")).isFalse();
    }

    // ========== STRUCTURED LOGGER TESTS ==========

    @Test
    @DisplayName("Structured Logger - Eventos de Segurança")
    void testStructuredSecurityEvents() {
        // Testa eventos de autenticação
        structuredLogger.logAuthenticationEvent("user123", "LOGIN", "SUCCESS", "Login successful");
        structuredLogger.logAuthenticationEvent("user123", "LOGIN", "FAILED", "Invalid password");
        
        // Testa eventos de autorização
        structuredLogger.logAuthorizationEvent("user123", "/api/admin", "READ", "DENIED", "Insufficient permissions");
        
        // Testa eventos de ameaça
        Map<String, Object> threatDetails = Map.of("threat_type", "SQL_INJECTION", "pattern", "' OR '1'='1");
        structuredLogger.logSecurityThreat("SQL_INJECTION", "HIGH", "SQL injection attempt detected", threatDetails);
        
        // Testa eventos de sistema
        structuredLogger.logSystemEvent("CONFIGURATION_CHANGE", "MEDIUM", "Security configuration updated");
        
        // Testa eventos de performance
        structuredLogger.logPerformanceEvent("database_query", 150, true);
        
        // Verifica contexto global
        Map<String, Object> context = structuredLogger.getGlobalContext();
        assertThat(context).containsKey("service");
        assertThat(context).containsKey("version");
        assertThat(context).containsKey("environment");
    }

    @Test
    @DisplayName("Structured Logger - Formatação SIEM")
    void testStructuredLoggerSIEMFormat() {
        StructuredLogger.SecurityEvent event = new StructuredLogger.SecurityEvent(
            "AUTHENTICATION", "MEDIUM", "user123", "LOGIN", "SUCCESS", "Login successful"
        ).withIpAddress("192.168.1.100").withResource("/api/login");
        
        String siemFormat = structuredLogger.formatForSIEM(event);
        assertThat(siemFormat).isNotNull();
        assertThat(siemFormat).contains("\"event_type\":\"AUTHENTICATION\"");
        assertThat(siemFormat).contains("\"user\":\"user123\"");
        assertThat(siemFormat).contains("\"source_ip\":\"192.168.1.100\"");
    }

    // ========== UPLOAD INTEGRITY TESTS ==========

    @Test
    @DisplayName("Upload Integrity - Validação Básica")
    void testUploadBasicValidation() {
        String fileName = "test.png";
        byte[] validPNGContent = {(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};
        String mimeType = "image/png";
        String uploadedBy = "user123";
        String ipAddress = "127.0.0.1";
        
        // Upload válido
        UploadIntegrityManager.UploadValidationResult validResult = uploadManager.validateUpload(
            fileName, validPNGContent, mimeType, uploadedBy, ipAddress
        );
        
        assertThat(validResult.isValid()).isTrue();
        assertThat(validResult.getMetadata()).isNotNull();
        assertThat(validResult.getMetadata()).containsKey("sha256");
        assertThat(validResult.getMetadata()).containsKey("md5");
        assertThat(validResult.getMetadata()).containsKey("file_size");
        
        // Verificar valor do file_size
        Object fileSize = validResult.getMetadata().get("file_size");
        assertThat(fileSize).isNotNull();
        assertThat(fileSize).isInstanceOf(Integer.class);
        assertThat((Integer) fileSize).isEqualTo(validPNGContent.length);
    }

    @Test
    @DisplayName("Upload Integrity - Validação de Extensões Perigosas")
    void testUploadDangerousExtensions() {
        String dangerousFileName = "malware.exe";
        byte[] content = "fake content".getBytes();
        String mimeType = "application/octet-stream";
        
        UploadIntegrityManager.UploadValidationResult result = uploadManager.validateUpload(
            dangerousFileName, content, mimeType, "user123", "127.0.0.1"
        );
        
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrorMessage()).contains("não permitida");
    }

    @Test
    @DisplayName("Upload Integrity - Validação de MIME Type")
    void testUploadMimeTypeValidation() {
        String fileName = "test.jpg";
        byte[] content = "fake image content".getBytes();
        String invalidMimeType = "application/x-msdownload";
        
        UploadIntegrityManager.UploadValidationResult result = uploadManager.validateUpload(
            fileName, content, invalidMimeType, "user123", "127.0.0.1"
        );
        
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrorMessage()).contains("não permitido");
    }

    @Test
    @DisplayName("Upload Integrity - Validação de Conteúdo")
    void testUploadContentValidation() {
        String fileName = "test.pdf";
        byte[] invalidPDFContent = "not a pdf".getBytes();
        String mimeType = "application/pdf";
        
        UploadIntegrityManager.UploadValidationResult result = uploadManager.validateUpload(
            fileName, invalidPDFContent, mimeType, "user123", "127.0.0.1"
        );
        
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrorMessage()).contains("não corresponde");
    }

    // ========== CONFIGURATION MONITOR TESTS ==========

    @Test
    @DisplayName("Configuration Monitor - Estatísticas")
    void testConfigurationMonitorStats() {
        Map<String, Object> stats = configMonitor.getMonitoringStats();
        
        assertThat(stats).isNotNull();
        assertThat(stats).containsKey("monitored_files");
        assertThat(stats).containsKey("total_changes");
        assertThat(stats).containsKey("suspicious_changes");
        assertThat(stats).containsKey("last_check");
        
        // Verifica se os valores são numéricos
        assertThat(stats.get("monitored_files")).isInstanceOf(Integer.class);
        assertThat(stats.get("total_changes")).isInstanceOf(Integer.class);
        assertThat(stats.get("suspicious_changes")).isInstanceOf(Long.class);
    }

    @Test
    @DisplayName("Configuration Monitor - Histórico de Alterações")
    void testConfigurationChangeHistory() {
        // Obtém histórico (deve estar vazio no início)
        var history = configMonitor.getChangeHistory(null, 10);
        assertThat(history).isNotNull();
        
        // Verifica se o monitoramento está funcionando (deve ter arquivos críticos)
        Map<String, Object> statsBefore = configMonitor.getMonitoringStats();
        int initialFiles = (Integer) statsBefore.get("monitored_files");
        
        // Adiciona arquivo ao monitoramento (não deve falhar mesmo se não existir)
        configMonitor.addFileToMonitoring("test-config.properties");
        
        // Verifica se o monitoramento ainda está funcionando
        Map<String, Object> statsAfter = configMonitor.getMonitoringStats();
        assertThat(statsAfter).isNotNull();
        assertThat(statsAfter.get("monitored_files")).isInstanceOf(Integer.class);
    }

    @Test
    @DisplayName("Configuration Monitor - Verificação de Integridade")
    void testConfigurationIntegrityVerification() {
        // Adiciona arquivo de teste ao monitoramento
        configMonitor.addFileToMonitoring("test-integrity.properties");
        
        // Verifica integridade (deve funcionar para arquivos monitorados)
        boolean integrity = configMonitor.verifyFileIntegrity("test-integrity.properties");
        // Pode ser false se o arquivo não existir, mas não deve lançar exceção
        
        // Tenta verificar arquivo não monitorado
        boolean nonMonitoredIntegrity = configMonitor.verifyFileIntegrity("non-existent-file.properties");
        assertThat(nonMonitoredIntegrity).isFalse();
    }

    // ========== INTEGRAÇÃO ENTRE COMPONENTES ==========

    @Test
    @DisplayName("Integração - CSP + Rate Limiting")
    void testCSPRateLimitIntegration() {
        // Simula requisição com verificação CSP e rate limiting
        String clientId = "integration-test-client";
        String endpoint = "/api/login"; // Endpoint crítico configurado
        
        // Configura rate limit baixo
        rateLimitManager.configureEndpoint(endpoint, 2, 10, 50, 1, true);
        
        // Primeira requisição - deve passar
        RateLimitManager.RateLimitResult result1 = rateLimitManager.checkRateLimit(clientId, endpoint);
        assertThat(result1.isAllowed()).isTrue();
        
        // Verifica CSP para URL da requisição
        boolean cspValid = cspManager.validateURL(endpoint);
        assertThat(cspValid).isTrue();
        
        // Segunda requisição - deve passar
        RateLimitManager.RateLimitResult result2 = rateLimitManager.checkRateLimit(clientId, endpoint);
        assertThat(result2.isAllowed()).isTrue();
        
        // Terceira requisição - deve ser bloqueada por rate limit
        RateLimitManager.RateLimitResult result3 = rateLimitManager.checkRateLimit(clientId, endpoint);
        assertThat(result3.isAllowed()).isFalse();
        
        // Log do evento de rate limiting
        structuredLogger.logSecurityThreat("RATE_LIMIT_EXCEEDED", "MEDIUM", 
            "Rate limit exceeded for client: " + clientId, 
            Map.of("endpoint", endpoint, "client_id", clientId));
    }

    @Test
    @DisplayName("Integração - CSRF + Upload Integrity")
    void testCSRFUploadIntegration() {
        String sessionId = "upload-session";
        String userAgent = "Test Browser";
        String ipAddress = "127.0.0.1";
        
        // Gera token CSRF
        String csrfToken = csrfManager.generateToken(sessionId, userAgent, ipAddress);
        assertThat(csrfToken).isNotNull();
        
        // Valida token CSRF
        CSRFProtectionManager.CSRFValidationResult csrfResult = csrfManager.validateToken(csrfToken, sessionId, userAgent, ipAddress);
        assertThat(csrfResult.isValid()).isTrue();
        
        // Simula upload com validação de integridade
        String fileName = "secure-upload.png";
        byte[] validPNGContent = {(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A, 0x00, 0x00, 0x00, 0x0D};
        String mimeType = "image/png";
        
        UploadIntegrityManager.UploadValidationResult uploadResult = uploadManager.validateUpload(
            fileName, validPNGContent, mimeType, "user123", ipAddress
        );
        
        assertThat(uploadResult.isValid()).isTrue();
        
        // Log da operação de upload seguro
        structuredLogger.logDataAccess("user123", "file_upload", "UPLOAD", "SUCCESS");
    }

    @Test
    @DisplayName("Integração Completa - Fluxo de Segurança")
    void testCompleteSecurityIntegration() {
        // 1. Configuração inicial
        structuredLogger.addGlobalContext("test_suite", "AdvancedSecurityTest");
        
        // 2. Rate limiting para endpoint crítico
        String clientId = "integration-client";
        String endpoint = "/api/critical-operation";
        rateLimitManager.configureEndpoint(endpoint, 1, 5, 20, 1, true);
        
        // 3. Geração de token CSRF para formulário
        String sessionId = "secure-session";
        String csrfToken = csrfManager.generateToken(sessionId, "Test Browser", "127.0.0.1");
        
        // 4. Validação de todos os componentes
        assertThat(csrfManager.validateToken(csrfToken, sessionId, "Test Browser", "127.0.0.1").isValid()).isTrue();
        assertThat(rateLimitManager.checkRateLimit(clientId, endpoint).isAllowed()).isTrue();
        assertThat(cspManager.validateURL(endpoint)).isTrue();
        
        // 5. Simula upload seguro
        byte[] fileContent = {(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};
        UploadIntegrityManager.UploadValidationResult uploadResult = uploadManager.validateUpload(
            "integration-test.png", fileContent, "image/png", "integration-user", "127.0.0.1"
        );
        assertThat(uploadResult.isValid()).isTrue();
        
        // 6. Log completo da operação
        structuredLogger.logSecurityEvent(
            new StructuredLogger.SecurityEvent("SECURITY_INTEGRATION", "MEDIUM", "integration-user", 
                                             "COMPLETE_FLOW", "SUCCESS", "Fluxo de segurança integrado concluído")
                .withResource(endpoint)
                .withDetail("csrf_token_valid", true)
                .withDetail("rate_limit_allowed", true)
                .withDetail("csp_valid", true)
                .withDetail("upload_valid", true)
        );
        
        // 7. Verificação final de configurações
        Map<String, Object> finalStats = configMonitor.getMonitoringStats();
        assertThat(finalStats).isNotNull();
        
        // Cleanup
        structuredLogger.removeGlobalContext("test_suite");
    }
}
