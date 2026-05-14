package com.artereal.swing.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import static org.assertj.core.api.Assertions.*;

/**
 * Teste simples para debug do CryptoManager
 */
class CryptoDebugTest {
    
    @BeforeAll
    static void setup() {
        System.setProperty("test.environment", "true");
        System.setProperty("crypto.master.key", "ArteReal2024SecureTestKeyForOWASPComplianceTesting1234567890");
        SecretsManager.initialize();
    }
    
    @Test
    void testCryptoKeyDebug() {
        System.err.println("DEBUG: Iniciando teste de criptografia");
        
        try {
            // Testar obter a chave diretamente
            String masterKey = "ArteReal2024SecureTestKeyForOWASPComplianceTesting1234567890";
            System.err.println("DEBUG: Master key length: " + masterKey.length());
            System.err.println("DEBUG: Master key: '" + masterKey + "'");
            
            String plaintext = "test message";
            System.err.println("DEBUG: Plaintext: " + plaintext);
            
            String encrypted = CryptoManager.encrypt(plaintext);
            System.err.println("DEBUG: Encrypted: " + encrypted);
            
            String decrypted = CryptoManager.decrypt(encrypted);
            System.err.println("DEBUG: Decrypted: " + decrypted);
            
            assertThat(decrypted).isEqualTo(plaintext);
        } catch (Exception e) {
            System.err.println("DEBUG: Erro: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
