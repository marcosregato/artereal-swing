package com.artereal.swing.security;

import com.artereal.swing.model.Despesa;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

/**
 * Teste para debug de validação de Despesa
 */
@DisplayName("Debug Despesa Security")
class DespesaDebugTest {
    
    @Test
    @DisplayName("Debug - Validar Despesa")
    void debugDespesaValidation() {
        // Cria uma despesa de teste
        Despesa despesa = new Despesa("Aluguel", 1500.00, new Date(), "Moradia", "Imobiliária", "NF-001");
        
        System.out.println("Despesa criada: " + despesa.toString());
        
        // Testa validação de segurança
        SecurityManager.SecurityScanResult result = SecurityManager.scanForThreats(despesa.toString());
        System.out.println("Resultado da validação: " + result);
        System.out.println("É seguro? " + result.isSafe());
        if (!result.isSafe()) {
            System.out.println("Ameaças: " + result.getThreats());
        }
    }
    
    @Test
    @DisplayName("Debug - Testar campos individuais")
    void debugCamposIndividuais() {
        // Testa cada campo individualmente
        String descricao = "Aluguel mensal";
        String categoria = "Moradia";
        String fornecedor = "Imobiliária XYZ";
        String numeroDocumento = "NF-001";
        
        System.out.println("Descrição: " + SecurityManager.scanForThreats(descricao));
        System.out.println("Categoria: " + SecurityManager.scanForThreats(categoria));
        System.out.println("Fornecedor: " + SecurityManager.scanForThreats(fornecedor));
        System.out.println("Documento: " + SecurityManager.scanForThreats(numeroDocumento));
    }
    
    @Test
    @DisplayName("Debug - Testar SQL queries")
    void debugSQLQueries() {
        // Testa as queries SQL
        String insertSQL = "INSERT INTO despesas (descricao, valor, data, categoria, fornecedor, numero_documento) VALUES (?, ?, ?, ?, ?, ?)";
        String selectSQL = "SELECT * FROM despesas WHERE id = ?";
        String selectAllSQL = "SELECT * FROM despesas ORDER BY data DESC, id DESC";
        
        System.out.println("INSERT SQL: " + SecurityManager.scanForThreats(insertSQL));
        System.out.println("SELECT BY ID: " + SecurityManager.scanForThreats(selectSQL));
        System.out.println("SELECT ALL: " + SecurityManager.scanForThreats(selectAllSQL));
    }
}
