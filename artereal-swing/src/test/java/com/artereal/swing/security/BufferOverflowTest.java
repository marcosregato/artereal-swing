package com.artereal.swing.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

/**
 * Teste específico para debug de buffer overflow
 */
@DisplayName("Debug Buffer Overflow")
class BufferOverflowTest {
    
    @Test
    @DisplayName("Debug - Teste de Buffer Overflow com diferentes tamanhos")
    void debugBufferOverflow() {
        // Testa diferentes tamanhos para encontrar o threshold correto
        for (int size = 1000; size <= 3000; size += 100) {
            String bufferOverflow = "A".repeat(size);
            SecurityManager.SecurityScanResult result = SecurityManager.scanForThreats(bufferOverflow);
            
            System.out.println("Tamanho: " + size + " - Seguro: " + result.isSafe() + 
                             (result.isSafe() ? "" : " - Threats: " + result.getThreats()));
            
            if (!result.isSafe()) {
                System.out.println("Encontrado buffer overflow em tamanho: " + size);
                break;
            }
        }
    }
    
    @Test
    @DisplayName("Debug - Verificar padrão específico")
    void debugPatternMatching() {
        // Testa o padrão específico de 'A' repetidos
        String test1000 = "A".repeat(1000);
        String test1500 = "A".repeat(1500);
        String test2000 = "A".repeat(2000);
        
        System.out.println("Testando 1000 A's: " + SecurityManager.scanForThreats(test1000));
        System.out.println("Testando 1500 A's: " + SecurityManager.scanForThreats(test1500));
        System.out.println("Testando 2000 A's: " + SecurityManager.scanForThreats(test2000));
    }
}
