package com.artereal.swing.integration.dao;

import com.artereal.swing.database.DatabaseManager;
import com.artereal.swing.dao.DespesaDAO;
import com.artereal.swing.model.Despesa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Teste de debug para DespesaDAO
 */
@DisplayName("Debug Test - DespesaDAO")
class DespesaDebugTest {

    private static final Logger logger = LoggerFactory.getLogger(DespesaDebugTest.class);
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
        despesa.setData(java.sql.Date.valueOf(LocalDate.now()));
        despesa.setCategoria("MANUTENCAO");
        despesa.setFornecedor("Tesoureiro Teste");
        despesa.setNumeroDocumento("DOC-001");

        logger.info("=== DEBUG: Iniciando teste de fluxo completo ===");
        logger.info("Despesa inicial: " + despesa);
        logger.info("Despesa ID inicial: " + despesa.getId());

        // Act - Insert
        logger.info("=== DEBUG: Executando INSERT ===");
        despesaDAO.save(despesa);
        logger.info("Despesa após INSERT: " + despesa);
        logger.info("Despesa ID após INSERT: " + despesa.getId());
        
        assertThat(despesa.getId()).isNotNull();
        
        Long id = despesa.getId();
        logger.info("ID salvo: " + id);
        
        // Verificar se está no banco
        logger.info("=== DEBUG: Verificando se está no banco ===");
        logger.info("Procurando ID: " + id);
        logger.info("SQL esperado: SELECT * FROM despesas WHERE id = ?");
        
        // Verificar se existe no banco com SQL direto
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM despesas WHERE id = ?")) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    logger.info("Registros encontrados no banco: " + rs.getInt(1));
                }
            }
        } catch (Exception e) {
            logger.error("Erro na consulta direta: " + e.getMessage());
        }
        
        Despesa salva = despesaDAO.findById(id);
        logger.info("Despesa recuperada do banco: " + salva);
        assertThat(salva).isNotNull();
        
        // Act - Update
        logger.info("=== DEBUG: Executando UPDATE ===");
        logger.info("Valor antes: " + despesa.getValor());
        despesa.setValor(2500.0);
        logger.info("Valor depois: " + despesa.getValor());
        despesaDAO.update(despesa);
        
        logger.info("=== DEBUG: Verificando UPDATE ===");
        logger.info("Valor no banco após UPDATE: " + despesaDAO.findById(id).getValor());
        
        despesaDAO.save(despesa);
        logger.info("Despesa após UPDATE: " + despesa);
        
        // Verificar se foi atualizado
        logger.info("=== DEBUG: Verificando se foi atualizado ===");
        Despesa atualizada = despesaDAO.findById(id);
        logger.info("Despesa atualizada do banco: " + atualizada);
        assertThat(atualizada).isNotNull();
        assertThat(atualizada.getValor()).isEqualTo(2500.0);
        
        logger.info("=== DEBUG: Teste concluído com sucesso ===");
    }
}
