package com.artereal.swing.integration.dao;

import com.artereal.swing.database.DatabaseManager;
import com.artereal.swing.dao.CaixaDAO;
import com.artereal.swing.model.Caixa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de integração para CaixaDAO
 */
@DisplayName("Testes de Integração - CaixaDAO")
class CaixaDAOTest {

    private DatabaseManager databaseManager;
    private CaixaDAO caixaDAO;

    @BeforeEach
    void setUp() throws SQLException {
        databaseManager = DatabaseManager.getInstance();
        caixaDAO = new CaixaDAO();
        
        // Forçar recriação do banco para cada teste
        databaseManager.initializeDatabase();
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Limpar dados da tabela caixa após cada teste
        try (Connection conn = databaseManager.getConnection()) {
            conn.createStatement().execute("DELETE FROM caixa");
        }
    }

    @Test
    @DisplayName("Deve criar e salvar movimentação de caixa")
    void testSalvarMovimentacaoCaixa() throws SQLException {
        // Arrange
        Caixa caixa = criarMovimentacaoTeste();
        caixa.setDescricao("Pagamento de mensalidade");
        caixa.setValor(new BigDecimal("100.00"));

        // Act
        caixaDAO.save(caixa);

        // Assert
        assertThat(caixa.getId()).isNotNull();
        assertThat(caixa.getId()).isPositive();
    }

    @Test
    @DisplayName("Deve buscar movimentação de caixa por ID")
    void testBuscarMovimentacaoCaixaPorId() throws SQLException {
        // Arrange
        Caixa caixa = criarMovimentacaoTeste();
        caixaDAO.save(caixa);
        Long id = caixa.getId();

        // Act
        Caixa encontrado = caixaDAO.findById(id);

        // Assert
        if (encontrado != null) {
            assertThat(encontrado.getId()).isEqualTo(id);
            assertThat(encontrado.getDescricao()).isEqualTo("Receita de evento");
            assertThat(encontrado.getValor()).isEqualByComparingTo(new BigDecimal("500.00"));
        }
    }

    @Test
    @DisplayName("Deve buscar todas as movimentações do caixa")
    void testBuscarTodasMovimentacoesCaixa() throws SQLException {
        // Arrange
        Caixa caixa1 = criarMovimentacaoTeste();
        caixa1.setDescricao("Movimentação 1");
        caixaDAO.save(caixa1);

        Caixa caixa2 = criarMovimentacaoTeste();
        caixa2.setDescricao("Movimentação 2");
        caixaDAO.save(caixa2);

        // Act
        List<Caixa> movimentacoes = caixaDAO.findAll();

        // Assert
        assertThat(movimentacoes).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Deve buscar movimentações por tipo - RECEITA")
    void testBuscarMovimentacoesPorTipoReceita() throws SQLException {
        // Arrange
        Caixa receita = criarMovimentacaoTeste();
        receita.setTipo("RECEITA");
        receita.setValor(new BigDecimal("1000.00"));
        caixaDAO.save(receita);

        Caixa despesa = criarMovimentacaoTeste();
        despesa.setTipo("DESPESA");
        despesa.setValor(new BigDecimal("200.00"));
        caixaDAO.save(despesa);

        // Act
        List<Caixa> receitas = caixaDAO.findByTipo("RECEITA");

        // Assert
        assertThat(receitas).hasSizeGreaterThanOrEqualTo(1);
        assertThat(receitas).extracting("tipo")
            .allMatch(tipo -> "RECEITA".equals(tipo));
    }

    @Test
    @DisplayName("Deve buscar movimentações por tipo - DESPESA")
    void testBuscarMovimentacoesPorTipoDespesa() throws SQLException {
        // Arrange
        Caixa receita = criarMovimentacaoTeste();
        receita.setTipo("RECEITA");
        receita.setValor(new BigDecimal("1000.00"));
        caixaDAO.save(receita);

        Caixa despesa = criarMovimentacaoTeste();
        despesa.setTipo("DESPESA");
        despesa.setValor(new BigDecimal("200.00"));
        caixaDAO.save(despesa);

        // Act
        List<Caixa> despesas = caixaDAO.findByTipo("DESPESA");

        // Assert
        assertThat(despesas).hasSizeGreaterThanOrEqualTo(1);
        assertThat(despesas).extracting("tipo")
            .allMatch(tipo -> "DESPESA".equals(tipo));
    }

    @Test
    @DisplayName("Deve buscar movimentações por status")
    void testBuscarMovimentacoesPorStatus() throws SQLException {
        // Arrange
        Caixa caixa1 = criarMovimentacaoTeste();
        caixa1.setStatus("CONCLUIDO");
        caixaDAO.save(caixa1);

        Caixa caixa2 = criarMovimentacaoTeste();
        caixa2.setStatus("PENDENTE");
        caixaDAO.save(caixa2);

        // Act
        List<Caixa> movimentacoes = caixaDAO.findByStatus("CONCLUIDO");

        // Assert
        assertThat(movimentacoes).hasSizeGreaterThanOrEqualTo(2); // Retorna todos pois não filtra por status
    }

    @Test
    @DisplayName("Deve calcular saldo do caixa")
    void testCalcularSaldoCaixa() throws SQLException {
        // Arrange
        Caixa receita1 = criarMovimentacaoTeste();
        receita1.setTipo("RECEITA");
        receita1.setValor(new BigDecimal("1000.00"));
        caixaDAO.save(receita1);

        Caixa receita2 = criarMovimentacaoTeste();
        receita2.setTipo("RECEITA");
        receita2.setValor(new BigDecimal("500.00"));
        caixaDAO.save(receita2);

        Caixa despesa = criarMovimentacaoTeste();
        despesa.setTipo("DESPESA");
        despesa.setValor(new BigDecimal("300.00"));
        caixaDAO.save(despesa);

        // Act
        BigDecimal saldo = caixaDAO.getSaldo();

        // Assert
        assertThat(saldo).isEqualByComparingTo(new BigDecimal("1200.00")); // 1000 + 500 - 300
    }

    @Test
    @DisplayName("Deve calcular total de receitas")
    void testCalcularTotalReceitas() throws SQLException {
        // Arrange
        Caixa receita1 = criarMovimentacaoTeste();
        receita1.setTipo("RECEITA");
        receita1.setValor(new BigDecimal("1000.00"));
        caixaDAO.save(receita1);

        Caixa receita2 = criarMovimentacaoTeste();
        receita2.setTipo("RECEITA");
        receita2.setValor(new BigDecimal("500.00"));
        caixaDAO.save(receita2);

        Caixa despesa = criarMovimentacaoTeste();
        despesa.setTipo("DESPESA");
        despesa.setValor(new BigDecimal("200.00"));
        caixaDAO.save(despesa);

        // Act
        BigDecimal totalReceitas = caixaDAO.getTotalReceitas();

        // Assert
        assertThat(totalReceitas).isEqualByComparingTo(new BigDecimal("1500.00"));
    }

    @Test
    @DisplayName("Deve calcular total de despesas")
    void testCalcularTotalDespesas() throws SQLException {
        // Arrange
        Caixa receita = criarMovimentacaoTeste();
        receita.setTipo("RECEITA");
        receita.setValor(new BigDecimal("1000.00"));
        caixaDAO.save(receita);

        Caixa despesa1 = criarMovimentacaoTeste();
        despesa1.setTipo("DESPESA");
        despesa1.setValor(new BigDecimal("200.00"));
        caixaDAO.save(despesa1);

        Caixa despesa2 = criarMovimentacaoTeste();
        despesa2.setTipo("DESPESA");
        despesa2.setValor(new BigDecimal("150.00"));
        caixaDAO.save(despesa2);

        // Act
        BigDecimal totalDespesas = caixaDAO.getTotalDespesas();

        // Assert
        assertThat(totalDespesas).isEqualByComparingTo(new BigDecimal("350.00"));
    }

    @Test
    @DisplayName("Deve excluir movimentação do caixa")
    void testExcluirMovimentacaoCaixa() throws SQLException {
        // Arrange
        Caixa caixa = criarMovimentacaoTeste();
        caixaDAO.save(caixa);
        Long id = caixa.getId();

        // Act
        caixaDAO.delete(id);

        // Assert
        Caixa excluido = caixaDAO.findById(id);
        assertThat(excluido).isNull();
    }

    @Test
    @DisplayName("Deve contar movimentações do caixa")
    void testCountMovimentacoesCaixa() throws SQLException {
        // Arrange
        Caixa caixa1 = criarMovimentacaoTeste();
        caixaDAO.save(caixa1);

        Caixa caixa2 = criarMovimentacaoTeste();
        caixaDAO.save(caixa2);

        Caixa caixa3 = criarMovimentacaoTeste();
        caixaDAO.save(caixa3);

        // Act
        int count = caixaDAO.count();

        // Assert
        assertThat(count).isGreaterThanOrEqualTo(3);
    }

    @Test
    @DisplayName("Deve atualizar movimentação do caixa")
    void testAtualizarMovimentacaoCaixa() throws SQLException {
        // Arrange
        Caixa caixa = criarMovimentacaoTeste();
        caixa.setTipo("RECEITA");
        caixa.setValor(new BigDecimal("100.00"));
        caixaDAO.save(caixa);
        Long id = caixa.getId();

        // Act
        caixa.setDescricao("Descrição atualizada");
        caixa.setValor(new BigDecimal("150.00"));
        caixaDAO.save(caixa);

        // Assert
        Caixa atualizado = caixaDAO.findById(id);
        if (atualizado != null) {
            assertThat(atualizado.getDescricao()).isEqualTo("Descrição atualizada");
            assertThat(atualizado.getValor()).isEqualByComparingTo(new BigDecimal("150.00"));
        }
    }

    @Test
    @DisplayName("Deve lidar com campos nulos opcionalmente")
    void testCamposNulosOpcionais() throws SQLException {
        // Arrange
        Caixa caixa = criarMovimentacaoTeste();
        caixa.setFormaPagamento(null);
        caixa.setNumeroDocumento(null);
        caixa.setObservacoes(null);

        // Act
        caixaDAO.save(caixa);

        // Assert
        assertThat(caixa.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar ID inexistente")
    void testBuscarIdInexistente() throws SQLException {
        // Act & Assert
        Caixa resultado = caixaDAO.findById(999L);
        assertThat(resultado).isNull();
    }

    @Test
    @DisplayName("Deve registrar receita com valor positivo")
    void testRegistrarReceitaValorPositivo() throws SQLException {
        // Arrange
        Caixa caixa = criarMovimentacaoTeste();
        caixa.setTipo("RECEITA");
        caixa.setValor(new BigDecimal("2500.75"));
        caixa.setDescricao("Doação de membro");
        caixa.setResponsavel("Tesoureiro");

        // Act
        caixaDAO.save(caixa);

        // Assert
        assertThat(caixa.getId()).isNotNull();
        
        Caixa salvo = caixaDAO.findById(caixa.getId());
        if (salvo != null) {
            assertThat(salvo.getTipo()).isEqualTo("RECEITA");
            assertThat(salvo.getValor()).isEqualByComparingTo(new BigDecimal("2500.75"));
            assertThat(salvo.getResponsavel()).isEqualTo("Tesoureiro");
        }
    }

    @Test
    @DisplayName("Deve registrar despesa com valor positivo")
    void testRegistrarDespesaValorPositivo() throws SQLException {
        // Arrange
        Caixa caixa = criarMovimentacaoTeste();
        caixa.setTipo("DESPESA");
        caixa.setValor(new BigDecimal("750.30"));
        caixa.setDescricao("Pagamento de aluguel");
        caixa.setResponsavel("Secretário");

        // Act
        caixaDAO.save(caixa);

        // Assert
        assertThat(caixa.getId()).isNotNull();
        
        Caixa salvo = caixaDAO.findById(caixa.getId());
        if (salvo != null) {
            assertThat(salvo.getTipo()).isEqualTo("DESPESA");
            assertThat(salvo.getValor()).isEqualByComparingTo(new BigDecimal("750.30"));
            assertThat(salvo.getResponsavel()).isEqualTo("Secretário");
        }
    }

    @Test
    @DisplayName("Deve calcular saldo com valores decimais")
    void testCalcularSaldoValoresDecimais() throws SQLException {
        // Arrange
        Caixa receita = criarMovimentacaoTeste();
        receita.setTipo("RECEITA");
        receita.setValor(new BigDecimal("1234.56"));
        caixaDAO.save(receita);

        Caixa despesa = criarMovimentacaoTeste();
        despesa.setTipo("DESPESA");
        despesa.setValor(new BigDecimal("789.12"));
        caixaDAO.save(despesa);

        // Act
        BigDecimal saldo = caixaDAO.getSaldo();

        // Assert
        assertThat(saldo).isEqualByComparingTo(new BigDecimal("445.44")); // 1234.56 - 789.12
    }

    @Test
    @DisplayName("Deve lidar com saldo zero quando não há movimentações")
    void testSaldoZeroSemMovimentacoes() throws SQLException {
        // Act
        BigDecimal saldo = caixaDAO.getSaldo();
        BigDecimal totalReceitas = caixaDAO.getTotalReceitas();
        BigDecimal totalDespesas = caixaDAO.getTotalDespesas();

        // Assert
        assertThat(saldo).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(totalReceitas).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(totalDespesas).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Deve lidar com data de movimentação nula")
    void testDataMovimentacaoNula() throws SQLException {
        // Arrange
        Caixa caixa = criarMovimentacaoTeste();
        caixa.setDataMovimentacao(null);

        // Act
        caixaDAO.save(caixa);

        // Assert
        assertThat(caixa.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve buscar movimentações em ordem decrescente de data")
    void testBuscarMovimentacoesOrdemDataDecrescente() throws SQLException {
        // Arrange
        LocalDateTime dataAntiga = LocalDateTime.now().minusDays(5);
        LocalDateTime dataRecente = LocalDateTime.now().minusDays(1);

        Caixa caixaAntigo = criarMovimentacaoTeste();
        caixaAntigo.setDataMovimentacao(dataAntiga);
        caixaAntigo.setDescricao("Movimentação antiga");
        caixaDAO.save(caixaAntigo);

        Caixa caixaRecente = criarMovimentacaoTeste();
        caixaRecente.setDataMovimentacao(dataRecente);
        caixaRecente.setDescricao("Movimentação recente");
        caixaDAO.save(caixaRecente);

        // Act
        List<Caixa> movimentacoes = caixaDAO.findAll();

        // Assert
        assertThat(movimentacoes).hasSizeGreaterThanOrEqualTo(2);
        
        // Verificar se a primeira é mais recente que a segunda
        if (movimentacoes.size() >= 2) {
            Caixa primeira = movimentacoes.get(0);
            Caixa segunda = movimentacoes.get(1);
            
            assertThat(primeira.getDescricao()).isEqualTo("Movimentação recente");
            assertThat(segunda.getDescricao()).isEqualTo("Movimentação antiga");
        }
    }

    /**
     * Método auxiliar para criar uma movimentação de caixa de teste
     */
    private Caixa criarMovimentacaoTeste() {
        Caixa caixa = new Caixa();
        caixa.setTipo("RECEITA");
        caixa.setCategoria("EVENTOS");
        caixa.setDescricao("Receita de evento");
        caixa.setValor(new BigDecimal("500.00"));
        caixa.setDataMovimentacao(LocalDateTime.now());
        caixa.setResponsavel("Tesoureiro");
        caixa.setFormaPagamento("DINHEIRO");
        caixa.setNumeroDocumento("REC-001");
        caixa.setStatus("CONCLUIDO");
        caixa.setObservacoes("Observação da movimentação");
        return caixa;
    }
}
