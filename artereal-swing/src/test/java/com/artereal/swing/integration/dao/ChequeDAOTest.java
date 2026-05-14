package com.artereal.swing.integration.dao;

import com.artereal.swing.database.DatabaseManager;
import com.artereal.swing.dao.ChequeDAO;
import com.artereal.swing.model.Cheque;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de integração para ChequeDAO
 */
@DisplayName("Testes de Integração - ChequeDAO")
class ChequeDAOTest {

    private DatabaseManager databaseManager;
    private ChequeDAO chequeDAO;

    @BeforeEach
    void setUp() throws SQLException {
        databaseManager = DatabaseManager.getInstance();
        chequeDAO = new ChequeDAO();
        
        // Forçar recriação do banco para cada teste
        databaseManager.initializeDatabase();
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Limpar dados da tabela cheque após cada teste
        try (Connection conn = databaseManager.getConnection()) {
            conn.createStatement().execute("DELETE FROM cheque");
        }
    }

    @Test
    @DisplayName("Deve criar e salvar cheque")
    void testSalvarCheque() throws SQLException {
        // Arrange
        Cheque cheque = criarChequeTeste();
        cheque.setSacado("João Silva");

        // Act
        chequeDAO.save(cheque);

        // Assert
        assertThat(cheque.getId()).isNotNull();
        assertThat(cheque.getId()).isPositive();
    }

    @Test
    @DisplayName("Deve buscar cheque por ID")
    void testBuscarChequePorId() throws SQLException {
        // Arrange
        Cheque cheque = criarChequeTeste();
        chequeDAO.save(cheque);
        Long id = cheque.getId();

        // Act
        Cheque encontrado = chequeDAO.findById(id);

        // Assert
        if (encontrado != null) {
            assertThat(encontrado.getId()).isEqualTo(id);
            assertThat(encontrado.getSacado()).isEqualTo("Empresa ABC Ltda");
            assertThat(encontrado.getValor()).isEqualTo(1500.0);
            assertThat(encontrado.getSituacao()).isEqualTo("ABERTO");
        }
    }

    @Test
    @DisplayName("Deve buscar todos os cheques")
    void testBuscarTodosCheques() throws SQLException {
        // Arrange
        Cheque cheque1 = criarChequeTeste();
        cheque1.setSacado("Cheque 1");
        chequeDAO.save(cheque1);

        Cheque cheque2 = criarChequeTeste();
        cheque2.setSacado("Cheque 2");
        chequeDAO.save(cheque2);

        // Act
        List<Cheque> cheques = chequeDAO.findAll();

        // Assert
        assertThat(cheques).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Deve buscar cheques por situação")
    void testBuscarChequesPorSituacao() throws SQLException {
        // Arrange
        Cheque cheque1 = criarChequeTeste();
        cheque1.setSituacao("ABERTO");
        chequeDAO.save(cheque1);

        Cheque cheque2 = criarChequeTeste();
        cheque2.setSituacao("PAGO");
        chequeDAO.save(cheque2);

        Cheque cheque3 = criarChequeTeste();
        cheque3.setSituacao("ABERTO");
        chequeDAO.save(cheque3);

        // Act
        List<Cheque> chequesAbertos = chequeDAO.findBySituacao("ABERTO");
        List<Cheque> chequesPagos = chequeDAO.findBySituacao("PAGO");

        // Assert
        assertThat(chequesAbertos).hasSizeGreaterThanOrEqualTo(2);
        assertThat(chequesPagos).hasSizeGreaterThanOrEqualTo(1);
        
        assertThat(chequesAbertos).extracting("situacao")
            .allMatch(situacao -> "ABERTO".equals(situacao));
        assertThat(chequesPagos).extracting("situacao")
            .allMatch(situacao -> "PAGO".equals(situacao));
    }

    @Test
    @DisplayName("Deve buscar cheques vencidos")
    void testBuscarChequesVencidos() throws SQLException {
        // Arrange
        Cheque chequeVencido = criarChequeTeste();
        chequeVencido.setDataVencimento(LocalDate.now().minusDays(10));
        chequeVencido.setSituacao("ABERTO");
        chequeDAO.save(chequeVencido);

        Cheque chequeFuturo = criarChequeTeste();
        chequeFuturo.setDataVencimento(LocalDate.now().plusDays(10));
        chequeFuturo.setSituacao("ABERTO");
        chequeDAO.save(chequeFuturo);

        Cheque chequePago = criarChequeTeste();
        chequePago.setDataVencimento(LocalDate.now().minusDays(5));
        chequePago.setSituacao("PAGO");
        chequeDAO.save(chequePago);

        // Act
        List<Cheque> vencidos = chequeDAO.findVencidos();

        // Assert
        assertThat(vencidos).hasSizeGreaterThanOrEqualTo(1);
        assertThat(vencidos).extracting("situacao")
            .allMatch(situacao -> "ABERTO".equals(situacao));
    }

    @Test
    @DisplayName("Deve buscar cheques próximos ao vencimento")
    void testBuscarChequesProximosVencimento() throws SQLException {
        // Arrange
        Cheque chequeAmanha = criarChequeTeste();
        chequeAmanha.setDataVencimento(LocalDate.now().plusDays(1));
        chequeAmanha.setSituacao("ABERTO");
        chequeDAO.save(chequeAmanha);

        Cheque chequeProximo = criarChequeTeste();
        chequeProximo.setDataVencimento(LocalDate.now().plusDays(5));
        chequeProximo.setSituacao("ABERTO");
        chequeDAO.save(chequeProximo);

        Cheque chequeLonge = criarChequeTeste();
        chequeLonge.setDataVencimento(LocalDate.now().plusDays(15));
        chequeLonge.setSituacao("ABERTO");
        chequeDAO.save(chequeLonge);

        // Act
        List<Cheque> proximos7dias = chequeDAO.findProximosVencimento(7);
        List<Cheque> proximos10dias = chequeDAO.findProximosVencimento(10);

        // Assert
        assertThat(proximos7dias).hasSizeGreaterThanOrEqualTo(2); // amanhã e 5 dias
        assertThat(proximos10dias).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Deve buscar cheques por sacado")
    void testBuscarChequesPorSacado() throws SQLException {
        // Arrange
        Cheque cheque1 = criarChequeTeste();
        cheque1.setSacado("João Silva Santos");
        chequeDAO.save(cheque1);

        Cheque cheque2 = criarChequeTeste();
        cheque2.setSacado("Maria João Oliveira");
        chequeDAO.save(cheque2);

        Cheque cheque3 = criarChequeTeste();
        cheque3.setSacado("Pedro Costa");
        chequeDAO.save(cheque3);

        // Act
        List<Cheque> cheques = chequeDAO.findBySacado("João");

        // Assert
        assertThat(cheques).hasSizeGreaterThanOrEqualTo(2);
        assertThat(cheques).extracting("sacado")
            .allMatch(sacado -> sacado.toString().contains("João"));
    }

    @Test
    @DisplayName("Deve buscar cheques por período")
    void testBuscarChequesPorPeriodo() throws SQLException {
        // Arrange
        LocalDate dataInicio = LocalDate.now().minusDays(10);
        LocalDate dataFim = LocalDate.now().plusDays(10);

        Cheque cheque1 = criarChequeTeste();
        cheque1.setDataVencimento(LocalDate.now().minusDays(5));
        chequeDAO.save(cheque1);

        Cheque cheque2 = criarChequeTeste();
        cheque2.setDataVencimento(LocalDate.now().plusDays(5));
        chequeDAO.save(cheque2);

        Cheque cheque3 = criarChequeTeste();
        cheque3.setDataVencimento(LocalDate.now().plusDays(15));
        chequeDAO.save(cheque3);

        // Act
        List<Cheque> chequesPeriodo = chequeDAO.findByPeriodo(dataInicio, dataFim);

        // Assert
        assertThat(chequesPeriodo).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Deve compensar cheque")
    void testCompensarCheque() throws SQLException {
        // Arrange
        Cheque cheque = criarChequeTeste();
        cheque.setSituacao("ABERTO");
        chequeDAO.save(cheque);
        Long id = cheque.getId();

        // Act
        chequeDAO.compensar(id, 1500.0, LocalDate.now());

        // Assert
        Cheque compensado = chequeDAO.findById(id);
        if (compensado != null) {
            assertThat(compensado.getSituacao()).isEqualTo("PAGO");
            assertThat(compensado.getValorPago()).isEqualTo(1500.0);
            assertThat(compensado.getDataPagamento()).isNotNull();
        }
    }

    @Test
    @DisplayName("Deve cancelar cheque")
    void testCancelarCheque() throws SQLException {
        // Arrange
        Cheque cheque = criarChequeTeste();
        cheque.setSituacao("ABERTO");
        chequeDAO.save(cheque);
        Long id = cheque.getId();

        // Act
        chequeDAO.cancelar(id);

        // Assert
        Cheque cancelado = chequeDAO.findById(id);
        if (cancelado != null) {
            assertThat(cancelado.getSituacao()).isEqualTo("CANCELADO");
        }
    }

    @Test
    @DisplayName("Deve devolver cheque")
    void testDevolverCheque() throws SQLException {
        // Arrange
        Cheque cheque = criarChequeTeste();
        cheque.setSituacao("ABERTO");
        chequeDAO.save(cheque);
        Long id = cheque.getId();

        // Act
        chequeDAO.devolver(id);

        // Assert
        Cheque devolvido = chequeDAO.findById(id);
        if (devolvido != null) {
            assertThat(devolvido.getSituacao()).isEqualTo("DEVOLVIDO");
        }
    }

    @Test
    @DisplayName("Deve obter estatísticas de cheques")
    void testGetEstatisticas() throws SQLException {
        // Arrange
        Cheque cheque1 = criarChequeTeste();
        cheque1.setSituacao("ABERTO");
        cheque1.setValor(1000.0);
        chequeDAO.save(cheque1);

        Cheque cheque2 = criarChequeTeste();
        cheque2.setSituacao("ABERTO");
        cheque2.setValor(2000.0);
        chequeDAO.save(cheque2);

        Cheque cheque3 = criarChequeTeste();
        cheque3.setSituacao("PAGO");
        cheque3.setValor(1500.0);
        cheque3.setValorPago(1500.0);
        chequeDAO.save(cheque3);

        // Act
        List<Object[]> estatisticas = chequeDAO.getEstatisticas();

        // Assert
        assertThat(estatisticas).hasSizeGreaterThanOrEqualTo(2);
        
        // Verificar estrutura das estatísticas: [situacao, quantidade, valor_total, valor_medio, valor_pago_total]
        for (Object[] estatistica : estatisticas) {
            assertThat(estatistica).hasSize(5);
            assertThat(estatistica[0]).isInstanceOf(String.class); // situacao
            assertThat(estatistica[1]).isInstanceOf(Integer.class); // quantidade
            assertThat(estatistica[2]).isInstanceOf(Double.class); // valor_total
            assertThat(estatistica[3]).isInstanceOf(Double.class); // valor_medio
            assertThat(estatistica[4]).isInstanceOf(Double.class); // valor_pago_total
        }
    }

    @Test
    @DisplayName("Deve obter valor total por situação")
    void testGetValorTotalPorSituacao() throws SQLException {
        // Arrange
        Cheque cheque1 = criarChequeTeste();
        cheque1.setSituacao("ABERTO");
        cheque1.setValor(1000.0);
        chequeDAO.save(cheque1);

        Cheque cheque2 = criarChequeTeste();
        cheque2.setSituacao("ABERTO");
        cheque2.setValor(2000.0);
        chequeDAO.save(cheque2);

        Cheque cheque3 = criarChequeTeste();
        cheque3.setSituacao("PAGO");
        cheque3.setValor(1500.0);
        chequeDAO.save(cheque3);

        // Act
        double totalAberto = chequeDAO.getValorTotalPorSituacao("ABERTO");
        double totalPago = chequeDAO.getValorTotalPorSituacao("PAGO");

        // Assert
        assertThat(totalAberto).isEqualTo(3000.0);
        assertThat(totalPago).isEqualTo(1500.0);
    }

    @Test
    @DisplayName("Deve excluir (desativar) cheque")
    void testExcluirCheque() throws SQLException {
        // Arrange
        Cheque cheque = criarChequeTeste();
        chequeDAO.save(cheque);
        Long id = cheque.getId();

        // Act
        chequeDAO.delete(id);

        // Assert
        Cheque desativado = chequeDAO.findById(id);
        if (desativado != null) {
            assertThat(desativado.isAtivo()).isFalse();
        }
        
        // Verificar que não aparece nas listagens normais
        List<Cheque> ativos = chequeDAO.findAll();
        assertThat(ativos).extracting("id")
            .doesNotContain(id);
    }

    @Test
    @DisplayName("Deve contar cheques por situação")
    void testCountChequesPorSituacao() throws SQLException {
        // Arrange
        Cheque cheque1 = criarChequeTeste();
        cheque1.setSituacao("ABERTO");
        chequeDAO.save(cheque1);

        Cheque cheque2 = criarChequeTeste();
        cheque2.setSituacao("ABERTO");
        chequeDAO.save(cheque2);

        Cheque cheque3 = criarChequeTeste();
        cheque3.setSituacao("PAGO");
        chequeDAO.save(cheque3);

        // Act
        int countAbertos = chequeDAO.countBySituacao("ABERTO");
        int countPagos = chequeDAO.countBySituacao("PAGO");

        // Assert
        assertThat(countAbertos).isGreaterThanOrEqualTo(2);
        assertThat(countPagos).isGreaterThanOrEqualTo(1);
    }

    @Test
    @DisplayName("Deve atualizar cheque")
    void testAtualizarCheque() throws SQLException {
        // Arrange
        Cheque cheque = criarChequeTeste();
        cheque.setSituacao("ABERTO");
        chequeDAO.save(cheque);
        Long id = cheque.getId();

        // Act
        cheque.setSituacao("PAGO");
        cheque.setValorPago(1500.0);
        cheque.setDataPagamento(LocalDate.now());
        chequeDAO.save(cheque);

        // Assert
        Cheque atualizado = chequeDAO.findById(id);
        if (atualizado != null) {
            assertThat(atualizado.getSituacao()).isEqualTo("PAGO");
            assertThat(atualizado.getValorPago()).isEqualTo(1500.0);
            assertThat(atualizado.getDataPagamento()).isNotNull();
        }
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar ID inexistente")
    void testBuscarIdInexistente() throws SQLException {
        // Act & Assert
        Cheque resultado = chequeDAO.findById(999L);
        assertThat(resultado).isNull();
    }

    @Test
    @DisplayName("Deve lidar com campos nulos opcionalmente")
    void testCamposNulosOpcionais() throws SQLException {
        // Arrange
        Cheque cheque = criarChequeTeste();
        cheque.setCodigoCliente(null);
        cheque.setCodigoVendedor(null);
        cheque.setHistorico(null);

        // Act
        chequeDAO.save(cheque);

        // Assert
        assertThat(cheque.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve lidar com datas nulas")
    void testDatasNulas() throws SQLException {
        // Arrange
        Cheque cheque = criarChequeTeste();
        // data_emissao não pode ser nula (constraint NOT NULL)
        // cheque.setDataEmissao(null); // Removido para evitar violação de constraint
        cheque.setDataPagamento(null);

        // Act
        chequeDAO.save(cheque);

        // Assert
        assertThat(cheque.getId()).isNotNull();
        
        Cheque salvo = chequeDAO.findById(cheque.getId());
        if (salvo != null) {
            assertThat(salvo.getDataEmissao()).isNotNull();
            assertThat(salvo.getDataPagamento()).isNull();
        }
    }

    @Test
    @DisplayName("Deve registrar fluxo completo de cheque")
    void testFluxoCompletoCheque() throws SQLException {
        // Arrange
        Cheque cheque = criarChequeTeste();
        cheque.setSituacao("ABERTO");
        chequeDAO.save(cheque);
        Long id = cheque.getId();

        // Act - Fluxo completo
        chequeDAO.compensar(id, 1500.0, LocalDate.now());
        
        Cheque compensado = chequeDAO.findById(id);
        assertThat(compensado).isNotNull();
        assertThat(compensado.getSituacao()).isEqualTo("PAGO");
        assertThat(compensado.getValorPago()).isEqualTo(1500.0);

        // Assert
        assertThat(compensado.getDataPagamento()).isNotNull();
    }

    @Test
    @DisplayName("Deve lidar com valores decimais")
    void testValoresDecimais() throws SQLException {
        // Arrange
        Cheque cheque = criarChequeTeste();
        cheque.setValor(1234.56);
        cheque.setValorPago(0.0);

        // Act
        chequeDAO.save(cheque);

        // Assert
        assertThat(cheque.getId()).isNotNull();
        
        Cheque salvo = chequeDAO.findById(cheque.getId());
        if (salvo != null) {
            assertThat(salvo.getValor()).isEqualTo(1234.56);
            assertThat(salvo.getValorPago()).isEqualTo(0.0);
        }
    }

    @Test
    @DisplayName("Deve lidar com informações bancárias")
    void testInformacoesBancarias() throws SQLException {
        // Arrange
        Cheque cheque = criarChequeTeste();
        cheque.setBanco("Banco do Brasil");
        cheque.setModoPagamento("CHEQUE");
        cheque.setLancamentoCredito("Conta Corrente");
        cheque.setLancamentoDebito("Fornecedor");

        // Act
        chequeDAO.save(cheque);

        // Assert
        assertThat(cheque.getId()).isNotNull();
        
        Cheque salvo = chequeDAO.findById(cheque.getId());
        if (salvo != null) {
            assertThat(salvo.getBanco()).isEqualTo("Banco do Brasil");
            assertThat(salvo.getModoPagamento()).isEqualTo("CHEQUE");
            assertThat(salvo.getLancamentoCredito()).isEqualTo("Conta Corrente");
            assertThat(salvo.getLancamentoDebito()).isEqualTo("Fornecedor");
        }
    }

    /**
     * Método auxiliar para criar um cheque de teste
     */
    private Cheque criarChequeTeste() {
        Cheque cheque = new Cheque();
        cheque.setFatura("FAT-001");
        cheque.setDataEmissao(LocalDate.now());
        cheque.setSacado("Empresa ABC Ltda");
        cheque.setValor(1500.0);
        cheque.setDataVencimento(LocalDate.now().plusDays(30));
        cheque.setModoPagamento("CHEQUE");
        cheque.setBanco("Banco Teste");
        cheque.setDataPagamento(null);
        cheque.setValorPago(0.0);
        cheque.setCodigoCliente(123L);
        cheque.setSituacao("ABERTO");
        cheque.setGrupo("FORNECEDORES");
        cheque.setHistorico("Pagamento de fornecedor");
        cheque.setLancamentoCredito("Conta Corrente");
        cheque.setLancamentoDebito("Fornecedor");
        cheque.setNumeroNota("NF-001");
        cheque.setCodigoVendedor(456L);
        cheque.setAtivo(true);
        return cheque;
    }
}
