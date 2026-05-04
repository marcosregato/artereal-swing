package com.artereal.swing.integration.dao;

import com.artereal.swing.database.DatabaseManager;
import com.artereal.swing.dao.DespesaDAO;
import com.artereal.swing.model.Despesa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Teste de debug para DespesaDAO
 */
@DisplayName("Debug Test - DespesaDAO")
class DespesaDebugTest {

    private DatabaseManager databaseManager;
    private DespesaDAO despesaDAO;

    @BeforeEach
    void setUp() throws SQLException {
        // Configurar ambiente de testes para OWASP
        System.setProperty("test.environment", "true");
        
        databaseManager = DatabaseManager.getInstance();
        despesaDAO = new DespesaDAO();
        
        // Forçar recriação do banco para cada teste
        databaseManager.initializeDatabase();
    }

    @Test
    void testFluxoCompletoDespesaDebug() throws SQLException {
        // Arrange
        Despesa despesa = new Despesa();
        despesa.setDescricao("Despesa de Teste Debug");
        despesa.setValor(1500.0);
        despesa.setData(new Date());
        despesa.setCategoria("MANUTENCAO");
        despesa.setFornecedor("Tesoureiro Teste");
        despesa.setNumeroDocumento("DOC-001");

        System.out.println("=== DEBUG: Iniciando teste de fluxo completo ===");
        System.out.println("Despesa inicial: " + despesa);
        System.out.println("Despesa ID inicial: " + despesa.getId());

        // Act - Insert
        System.out.println("=== DEBUG: Executando INSERT ===");
        despesaDAO.save(despesa);
        System.out.println("Despesa após INSERT: " + despesa);
        System.out.println("Despesa ID após INSERT: " + despesa.getId());
        
        assertThat(despesa.getId()).isNotNull();
        
        Long id = despesa.getId();
        System.out.println("ID salvo: " + id);
        
        // Verificar se está no banco
        System.out.println("=== DEBUG: Verificando se está no banco ===");
        System.out.println("Procurando ID: " + id);
        System.out.println("SQL esperado: SELECT * FROM despesas WHERE id = ?");
        
        // Verificar se existe no banco com SQL direto
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM despesas WHERE id = ?")) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Registros encontrados no banco: " + rs.getInt(1));
                }
            }
        } catch (Exception e) {
            System.out.println("Erro na consulta direta: " + e.getMessage());
        }
        
        Despesa salva = despesaDAO.findById(id);
        System.out.println("Despesa recuperada do banco: " + salva);
        assertThat(salva).isNotNull();
        
        // Act - Update
        System.out.println("=== DEBUG: Executando UPDATE ===");
        System.out.println("Valor antes: " + despesa.getValor());
        despesa.setValor(2500.0);
        System.out.println("Valor depois: " + despesa.getValor());
        System.out.println("Despesa antes do UPDATE: " + despesa);
        System.out.println("Despesa ID antes do UPDATE: " + despesa.getId());
        
        despesaDAO.save(despesa);
        System.out.println("Despesa após UPDATE: " + despesa);
        
        // Verificar se foi atualizado
        System.out.println("=== DEBUG: Verificando se foi atualizado ===");
        Despesa atualizada = despesaDAO.findById(id);
        System.out.println("Despesa atualizada do banco: " + atualizada);
        assertThat(atualizada).isNotNull();
        assertThat(atualizada.getValor()).isEqualTo(2500.0);
        
        System.out.println("=== DEBUG: Teste concluído com sucesso ===");
    }
}
