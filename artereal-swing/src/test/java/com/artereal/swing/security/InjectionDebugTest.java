package com.artereal.swing.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import static org.assertj.core.api.Assertions.*;

/**
 * Teste para debug de Injection Prevention
 */
class InjectionDebugTest {
    
    @BeforeAll
    static void setup() {
        System.setProperty("test.environment", "true");
    }
    
    @Test
    void testSQLInjectionDetection() {
        String sqlInjection = "' OR '1'='1";
        SecurityManager.SecurityScanResult result = SecurityManager.scanForThreats(sqlInjection);
        
        System.out.println("SQL Injection: " + sqlInjection);
        System.out.println("Is Safe: " + result.isSafe());
        System.out.println("Threats: " + result.getThreats());
        System.out.println("Message: " + result.getMessage());
        
        // Verificar se alguma ameaça detectada
        assertThat(result.getThreats()).isNotEmpty();
    }
    
    @Test
    void testXSSDetection() {
        String xss = "<script>alert('XSS')</script>";
        SecurityManager.SecurityScanResult result = SecurityManager.scanForThreats(xss);
        
        System.out.println("XSS: " + xss);
        System.out.println("Is Safe: " + result.isSafe());
        System.out.println("Threats: " + result.getThreats());
        System.out.println("Message: " + result.getMessage());
        
        // Verificar se alguma ameaça detectada
        assertThat(result.getThreats()).isNotEmpty();
    }
}
