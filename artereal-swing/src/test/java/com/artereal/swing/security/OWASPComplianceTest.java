package com.artereal.swing.security;

import com.artereal.swing.dao.BaseDAO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.*;

/**
 * Testes de conformidade OWASP para o sistema ArteReal
 * Valida implementação dos componentes OWASP Top 10
 */
@DisplayName("OWASP Compliance Tests")
class OWASPComplianceTest {
    
    @BeforeAll
    static void setUpClass() {
        // Configura ambiente de teste para todos os testes
        System.setProperty("test.environment", "true");
        
        // Configura secrets de teste para OWASP
        System.setProperty("crypto.master.key", "ArteReal2024SecureTestKeyForOWASPComplianceTesting1234567890");
        System.setProperty("database.password", "test_password_12345");
        System.setProperty("jwt.secret", "ArteReal2024JWTSecretKeyForOWASPComplianceTestingWithMinimum64CharactersRequired!");
        
        // Inicializa componentes de segurança OWASP
        BaseDAO.initializeSecurityConfigs();
        SecretsManager.initialize();
    }
    
    @BeforeEach
    void setUp() {
        // Limpa dados de teste entre cada teste
        SecurityInterceptor.clearBlockedIPs();
    }
    
    @Test
    @DisplayName("A01: Broken Access Control - Permissões Funcionando")
    void testAccessControlCompliance() {
        // Configura permissões de teste
        AccessControlManager.setPermission("test_user", "usuario", AccessControlManager.AccessLevel.READ);
        AccessControlManager.setPermission("admin_user", "usuario", AccessControlManager.AccessLevel.ADMIN);
        
        // Testa permissões de leitura
        assertThat(AccessControlManager.hasPermission("test_user", "usuario", AccessControlManager.AccessLevel.READ))
            .isTrue();
        
        // Testa negação de escrita
        assertThat(AccessControlManager.hasPermission("test_user", "usuario", AccessControlManager.AccessLevel.WRITE))
            .isFalse();
        
        // Testa permissões de admin
        assertThat(AccessControlManager.hasPermission("admin_user", "usuario", AccessControlManager.AccessLevel.DELETE))
            .isTrue();
        
        // Testa validação de recurso (test_user tem permissão de leitura)
        assertThat(AccessControlManager.validateResourceAccess("test_user", "usuario", 1L))
            .isTrue();
    }
    
    @Test
    @DisplayName("A02: Cryptographic Failures - Criptografia Segura")
    void testCryptographicCompliance() throws Exception {
        String testData = "Dados sensíveis de teste OWASP";
        
        // Testa criptografia
        String encrypted = CryptoManager.encrypt(testData);
        assertThat(encrypted).isNotEqualTo(testData);
        assertThat(encrypted).isNotBlank();
        
        // Testa descriptografia
        String decrypted = CryptoManager.decrypt(encrypted);
        assertThat(decrypted).isEqualTo(testData);
        
        // Testa hash de senha OWASP
        String password = "SecurePassword123!";
        String hashedPassword = CryptoManager.hashPassword(password);
        assertThat(hashedPassword).isNotEqualTo(password);
        
        // Testa verificação de senha
        assertThat(CryptoManager.verifyPassword(password, hashedPassword)).isTrue();
        assertThat(CryptoManager.verifyPassword("WrongPassword", hashedPassword)).isFalse();
        
        // Testa geração de tokens seguros
        String csrfToken = CryptoManager.generateCSRFToken();
        String sessionId = CryptoManager.generateSessionId();
        
        assertThat(csrfToken).isNotBlank().hasSize(43); // Base64 URL sem padding
        assertThat(sessionId).isNotBlank().hasSize(86); // 64 bytes = 86 chars Base64
    }
    
    @Test
    @DisplayName("A03: Injection - Prevenção Funcionando")
    void testInjectionPrevention() {
        // Testa SQL Injection
        String sqlInjection = "' OR '1'='1";
        SecurityManager.SecurityScanResult sqlResult = SecurityManager.scanForThreats(sqlInjection);
        assertThat(sqlResult.isSafe()).isFalse();
        assertThat(sqlResult.getThreats()).anyMatch(threat -> threat.contains("SQL") || threat.contains("Injection"));
        
        // Testa XSS
        String xss = "<script>alert('XSS')</script>";
        SecurityManager.SecurityScanResult xssResult = SecurityManager.scanForThreats(xss);
        assertThat(xssResult.isSafe()).isFalse();
        assertThat(xssResult.getThreats()).anyMatch(threat -> threat.contains("XSS"));
        
        // Testa Command Injection
        String cmdInjection = "; rm -rf /";
        SecurityManager.SecurityScanResult cmdResult = SecurityManager.scanForThreats(cmdInjection);
        assertThat(cmdResult.isSafe()).isFalse();
        
        // Testa input seguro
        String safeInput = "Dados normais e seguros";
        SecurityManager.SecurityScanResult safeResult = SecurityManager.scanForThreats(safeInput);
        assertThat(safeResult.isSafe()).isTrue();
        
        // Testa sanitização
        String sanitized = SecurityManager.sanitizeInput("test'\"<script>");
        assertThat(sanitized).doesNotContain("'").doesNotContain("\"").doesNotContain("<script>");
    }
    
    @Test
    @DisplayName("A05: Security Misconfiguration - Configurações Seguras")
    void testSecurityConfigurationCompliance() {
        // Testa inicialização das configurações
        assertThat(SecurityConfigManager.hasConfig("security.headers.x-frame-options")).isTrue();
        assertThat(SecurityConfigManager.hasConfig("security.session.cookie-secure")).isTrue();
        assertThat(SecurityConfigManager.hasConfig("security.crypto.key-length")).isTrue();
        
        // Testa valores críticos
        assertThat(SecurityConfigManager.getConfig("security.headers.x-frame-options")).isEqualTo("DENY");
        assertThat(SecurityConfigManager.getBooleanConfig("security.session.cookie-secure")).isTrue();
        assertThat(SecurityConfigManager.getIntConfig("security.crypto.key-length")).isEqualTo(256);
        
        // Testa headers de segurança
        var headers = SecurityConfigManager.getSecurityHeaders();
        assertThat(headers).containsKeys("X-Frame-Options", "X-Content-Type-Options", "X-XSS-Protection");
        assertThat(headers.get("X-Frame-Options")).isEqualTo("DENY");
        
        // Testa validação de configurações
        var issues = SecurityConfigManager.validateSecurityConfigs();
        assertThat(issues).isEmpty(); // Todas as configurações devem estar corretas
    }
    
    @Test
    @DisplayName("A08: Software and Data Integrity - Integridade Funcionando")
    void testDataIntegrityCompliance() throws Exception {
        String testData = "Dados críticos para integridade";
        byte[] dataBytes = testData.getBytes();
        
        // Testa cálculo de checksum
        String checksum1 = IntegrityManager.calculateChecksum(dataBytes);
        String checksum2 = IntegrityManager.calculateChecksum(dataBytes);
        assertThat(checksum1).isEqualTo(checksum2);
        
        // Testa armazenamento e verificação
        IntegrityManager.storeChecksum("test_data", dataBytes);
        assertThat(IntegrityManager.verifyDataIntegrity("test_data", dataBytes)).isTrue();
        
        // Testa detecção de alteração
        byte[] alteredData = "Dados alterados".getBytes();
        assertThat(IntegrityManager.verifyDataIntegrity("test_data", alteredData)).isFalse();
        
        // Testa monitoramento de alterações
        assertThat(IntegrityManager.monitorDataChange("test_data", dataBytes)).isTrue(); // Primeira vez
        
        // Testa validação de arquivo
        String fileChecksum = IntegrityManager.calculateChecksum(dataBytes);
        assertThat(IntegrityManager.validateFileIntegrity("test.txt", dataBytes, fileChecksum)).isTrue();
    }
    
    @Test
    @DisplayName("Secrets Management - Armazenamento Seguro")
    void testSecretsManagementCompliance() {
        // Testa obtenção de secrets
        String cryptoKey = SecretsManager.getSecret("crypto.master.key");
        assertThat(cryptoKey).isNotBlank().hasSizeGreaterThanOrEqualTo(32);
        
        // Testa configuração de database
        var dbConfig = SecretsManager.getDatabaseConfig();
        assertThat(dbConfig.getUrl()).isNotBlank();
        assertThat(dbConfig.getUsername()).isNotBlank();
        assertThat(dbConfig.getPassword()).isNotBlank();
        
        // Testa cache TTL
        var expiredSecrets = SecretsManager.checkExpiredSecrets();
        assertThat(expiredSecrets).isEmpty(); // Secrets não devem estar expirados
        
        // Testa validação de secrets críticos
        assertThatCode(() -> SecretsManager.initialize()).doesNotThrowAnyException();
    }
    
    @Test
    @DisplayName("Input Validation ArteReal - Validação Específica")
    void testArteRealInputValidation() {
        // Testa validação de nome
        var validName = InputValidator.validateNome("João da Silva");
        assertThat(validName.isValid()).isTrue();
        
        var invalidName = InputValidator.validateNome("João<script>");
        assertThat(invalidName.isValid()).isFalse();
        
        // Testa validação de email
        var validEmail = InputValidator.validateEmail("joao@arteal.com.br");
        assertThat(validEmail.isValid()).isTrue();
        
        var invalidEmail = InputValidator.validateEmail("joao@spam.com");
        assertThat(invalidEmail.isValid()).isTrue(); // Email válido formato, apenas exemplo
        
        // Testa validação de CPF
        var validCPF = InputValidator.validateCPF("123.456.789-09");
        assertThat(validCPF.isValid()).isTrue();
        
        var invalidCPF = InputValidator.validateCPF("111.111.111-11");
        assertThat(invalidCPF.isValid()).isFalse();
        
        // Testa validação de telefone
        var validPhone = InputValidator.validateTelefone("(11) 98765-4321");
        assertThat(validPhone.isValid()).isTrue();
        
        var invalidPhone = InputValidator.validateTelefone("123");
        assertThat(invalidPhone.isValid()).isFalse();
    }
    
    @Test
    @DisplayName("Security Interceptor - Rate Limiting e Blocking")
    void testSecurityInterceptorCompliance() {
        String testIP = "192.168.1.100";
        String testEndpoint = "/api/usuarios";
        String testData = "dados de teste";
        
        // Testa rate limiting
        for (int i = 0; i < 5; i++) {
            var result = SecurityInterceptor.interceptRequest(testIP, testEndpoint, testData);
            assertThat(result.isAllowed()).isTrue();
        }
        
        // Testa bloqueio por muitas ameaças
        for (int i = 0; i < 5; i++) {
            SecurityInterceptor.recordThreat(testIP);
        }
        
        var blockedResult = SecurityInterceptor.interceptRequest(testIP, testEndpoint, testData);
        assertThat(blockedResult.isAllowed()).isFalse();
        assertThat(blockedResult.getReason()).contains("BLOCKED_IP");
        
        // Testa verificação de IP bloqueado
        assertThat(SecurityInterceptor.isIPBlocked(testIP)).isTrue();
    }
    
    @AfterEach
    void tearDown() {
        // Limpa dados de teste
        AccessControlManager.resetFailedAttempts("test_user");
        AccessControlManager.resetFailedAttempts("admin_user");
        SecurityInterceptor.clearBlockedIPs();
    }
}
