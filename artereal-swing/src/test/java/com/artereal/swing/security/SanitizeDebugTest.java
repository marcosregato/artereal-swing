package com.artereal.swing.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

/**
 * Teste para debug de sanitização
 */
class SanitizeDebugTest {
    
    @BeforeAll
    static void setup() {
        System.setProperty("test.environment", "true");
    }
    
    @Test
    void testSanitizeInput() {
        String input = "test'\"<script>";
        String sanitized = SecurityManager.sanitizeInput(input);
        
        System.out.println("Original: " + input);
        System.out.println("Sanitized: " + sanitized);
        System.out.println("Contains '<script>': " + sanitized.contains("<script>"));
        System.out.println("Contains \"': " + sanitized.contains("'"));
        System.out.println("Contains '\"': " + sanitized.contains("\""));
    }
}
