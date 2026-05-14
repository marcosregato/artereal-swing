package com.artereal.swing.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes do sistema de segurança
 */
@DisplayName("Testes de Segurança")
class SecurityTest {
    
    @Test
    @DisplayName("SecurityManager - Detecção de SQL Injection")
    void testSQLInjectionDetection() {
        // Teste com SQL Injection
        String maliciousInput = "'; DROP TABLE users; --";
        SecurityManager.SecurityScanResult result = SecurityManager.scanForThreats(maliciousInput);
        
        assertThat(result.isSafe()).isFalse();
        assertThat(result.getThreats()).anyMatch(threat -> threat.contains("SQL Injection"));
    }
    
    @Test
    @DisplayName("SecurityManager - Detecção de XSS")
    void testXSSDetection() {
        // Teste com XSS
        String xssInput = "<script>alert('XSS')</script>";
        SecurityManager.SecurityScanResult result = SecurityManager.scanForThreats(xssInput);
        
        assertThat(result.isSafe()).isFalse();
        assertThat(result.getThreats()).anyMatch(threat -> threat.contains("XSS"));
    }
    
    @Test
    @DisplayName("SecurityManager - Sanitização de Input")
    void testInputSanitization() {
        // Teste de sanitização
        String maliciousInput = "<script>alert('test')</script>admin' OR '1'='1";
        String sanitized = SecurityManager.sanitizeInput(maliciousInput);
        
        assertThat(sanitized).doesNotContain("<script>");
        assertThat(sanitized).doesNotContain("alert");
        assertThat(sanitized).doesNotContain("OR");
        assertThat(sanitized).doesNotContain("'");
    }
    
    @Test
    @DisplayName("InputValidator - Validação de Nome")
    void testNomeValidation() {
        // Nome válido
        InputValidator.ValidationResult validResult = InputValidator.validateNome("João Silva");
        assertThat(validResult.isValid()).isTrue();
        
        // Nome inválido (contém números)
        InputValidator.ValidationResult invalidResult = InputValidator.validateNome("João123");
        assertThat(invalidResult.isValid()).isFalse();
        
        // Nome com palavras proibidas
        InputValidator.ValidationResult threatResult = InputValidator.validateNome("admin delete");
        assertThat(threatResult.isValid()).isFalse();
    }
    
    @Test
    @DisplayName("InputValidator - Validação de Email")
    void testEmailValidation() {
        // Email válido
        InputValidator.ValidationResult validResult = InputValidator.validateEmail("joao@exemplo.com");
        assertThat(validResult.isValid()).isTrue();
        
        // Email inválido
        InputValidator.ValidationResult invalidResult = InputValidator.validateEmail("email-invalido");
        assertThat(invalidResult.isValid()).isFalse();
        
        // Email de domínio suspeito
        InputValidator.ValidationResult suspiciousResult = InputValidator.validateEmail("test@10minutemail.com");
        assertThat(suspiciousResult.isValid()).isFalse();
    }
    
    @Test
    @DisplayName("InputValidator - Validação de CPF")
    void testCPFValidation() {
        // CPF válido
        InputValidator.ValidationResult validResult = InputValidator.validateCPF("123.456.789-09");
        assertThat(validResult.isValid()).isTrue();
        
        // CPF inválido (formato)
        InputValidator.ValidationResult invalidResult = InputValidator.validateCPF("12345678909");
        assertThat(invalidResult.isValid()).isFalse();
        
        // CPF inválido (algoritmo)
        InputValidator.ValidationResult invalidAlgorithmResult = InputValidator.validateCPF("111.111.111-11");
        assertThat(invalidAlgorithmResult.isValid()).isFalse();
    }
    
    @Test
    @DisplayName("SecurityInterceptor - Rate Limiting")
    void testRateLimiting() {
        String testIP = "192.168.1.100";
        
        // Simula múltiplas requisições
        for (int i = 0; i < 5; i++) {
            SecurityInterceptor.InterceptionResult result = SecurityInterceptor.interceptRequest(
                testIP, "/test", "dados seguros");
            assertThat(result.isAllowed()).isTrue();
        }
        
        // Não deve bloquear ainda (abaixo do limite)
        assertThat(SecurityInterceptor.isIPBlocked(testIP)).isFalse();
    }
    
    @Test
    @DisplayName("SecurityInterceptor - Bloqueio por Ameaças")
    void testThreatBlocking() {
        String testIP = "192.168.1.101";
        
        // Simula tentativas de ataque
        String maliciousData = "'; DROP TABLE users; --";
        for (int i = 0; i < 3; i++) {
            SecurityInterceptor.InterceptionResult result = SecurityInterceptor.interceptRequest(
                testIP, "/test", maliciousData);
            assertThat(result.isAllowed()).isFalse();
        }
        
        // Deve bloquear após múltiplas tentativas
        assertThat(SecurityInterceptor.isIPBlocked(testIP)).isTrue();
    }
    
    @Test
    @DisplayName("InputValidator - Validação de Dados ArteReal")
    void testArteRealDataValidation() {
        // Teste de grau maçônico
        InputValidator.ValidationResult grauResult = InputValidator.validateGrau("APRENDIZ");
        assertThat(grauResult.isValid()).isTrue();
        
        InputValidator.ValidationResult grauInvalidResult = InputValidator.validateGrau("INVALIDO");
        assertThat(grauInvalidResult.isValid()).isFalse();
        
        // Teste de tipo sanguíneo
        InputValidator.ValidationResult sangueResult = InputValidator.validateTipoSanguineo("A+");
        assertThat(sangueResult.isValid()).isTrue();
        
        InputValidator.ValidationResult sangueInvalidResult = InputValidator.validateTipoSanguineo("XYZ");
        assertThat(sangueInvalidResult.isValid()).isFalse();
    }
    
    @Test
    @DisplayName("SecurityManager - Detecção de Path Traversal")
    void testPathTraversalDetection() {
        String pathTraversal = "../../../etc/passwd";
        SecurityManager.SecurityScanResult result = SecurityManager.scanForThreats(pathTraversal);
        
        assertThat(result.isSafe()).isFalse();
        assertThat(result.getThreats()).anyMatch(threat -> threat.contains("Path Traversal"));
    }
    
    @Test
    @DisplayName("SecurityManager - Detecção de Buffer Overflow")
    void testBufferOverflowDetection() {
        String bufferOverflow = "A".repeat(1500);
        SecurityManager.SecurityScanResult result = SecurityManager.scanForThreats(bufferOverflow);
        
        assertThat(result.isSafe()).isFalse();
        assertThat(result.getThreats()).anyMatch(threat -> threat.contains("Buffer Overflow"));
    }
    
    @Test
    @DisplayName("SecurityManager - Input Seguro")
    void testSafeInput() {
        String safeInput = "Dados normais e seguros";
        SecurityManager.SecurityScanResult result = SecurityManager.scanForThreats(safeInput);
        
        assertThat(result.isSafe()).isTrue();
        assertThat(result.getThreats()).isEmpty();
    }
    
    @Test
    @DisplayName("SecurityInterceptor - Estatísticas")
    void testSecurityStats() {
        // Limpa dados anteriores
        SecurityInterceptor.cleanup();
        
        // Simula algumas requisições
        SecurityInterceptor.interceptRequest("192.168.1.1", "/test1", "dados seguros");
        SecurityInterceptor.interceptRequest("192.168.1.2", "/test2", "dados seguros");
        
        SecurityInterceptor.SecurityStats stats = SecurityInterceptor.getSecurityStats();
        
        assertThat(stats.getTotalIPs()).isGreaterThanOrEqualTo(2);
        assertThat(stats.getBlockedIPs()).isGreaterThanOrEqualTo(0);
        assertThat(stats.getThreatAttempts()).isGreaterThanOrEqualTo(0);
    }
}
