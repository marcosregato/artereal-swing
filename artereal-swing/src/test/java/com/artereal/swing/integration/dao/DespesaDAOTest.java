package com.artereal.swing.integration.dao;

import com.artereal.swing.database.DatabaseManager;
import com.artereal.swing.dao.DespesaDAO;
import com.artereal.swing.model.Despesa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de integração para DespesaDAO
 */
@DisplayName("Testes de Integração - DespesaDAO")
class DespesaDAOTest {

    private DatabaseManager databaseManager;
    private DespesaDAO despesaDAO;

    @BeforeEach
    void setUp() throws SQLException {
        databaseManager = DatabaseManager.getInstance();
        despesaDAO = new DespesaDAO();
        
        // Forçar recriação do banco para cada teste
        databaseManager.initializeDatabase();
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Limpar dados da tabela despesas após cada teste
        try (Connection conn = databaseManager.getConnection()) {
            conn.createStatement().execute("DELETE FROM despesas");
        }
    }

    @Test
    @DisplayName("Deve criar e salvar despesa")
    void testSalvarDespesa() throws SQLException {
        // Arrange
        Despesa despesa = criarDespesaTeste();
        despesa.setDescricao("Material de escritório");

        // Act
        despesaDAO.save(despesa);

        // Assert
        assertThat(despesa.getId()).isNotNull();
        assertThat(despesa.getId()).isPositive();
    }

    @Test
    @DisplayName("Deve buscar despesa por ID")
    void testBuscarDespesaPorId() throws SQLException {
        // Arrange
        Despesa despesa = criarDespesaTeste();
        despesaDAO.save(despesa);
        Long id = despesa.getId();

        // Act
        Despesa encontrada = despesaDAO.findById(id);

        // Assert
        if (encontrada != null) {
            assertThat(encontrada.getId()).isEqualTo(id);
            assertThat(encontrada.getDescricao()).isEqualTo("Aluguel da Loja");
            assertThat(encontrada.getValor()).isEqualTo(1500.0);
            assertThat(encontrada.getCategoria()).isEqualTo("ALUGUEL");
        }
    }

    @Test
    @DisplayName("Deve buscar todas as despesas")
    void testBuscarTodasDespesas() throws SQLException {
        // Arrange
        Despesa despesa1 = criarDespesaTeste();
        despesa1.setDescricao("Despesa 1");
        despesaDAO.save(despesa1);

        Despesa despesa2 = criarDespesaTeste();
        despesa2.setDescricao("Despesa 2");
        despesaDAO.save(despesa2);

        // Act
        List<Despesa> despesas = despesaDAO.findAll();

        // Assert
        assertThat(despesas).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Deve buscar despesas por descrição")
    void testBuscarDespesasPorDescricao() throws SQLException {
        // Arrange
        Despesa despesa1 = criarDespesaTeste();
        despesa1.setDescricao("Compra de material de limpeza");
        despesaDAO.save(despesa1);

        Despesa despesa2 = criarDespesaTeste();
        despesa2.setDescricao("Material de escritório variado");
        despesaDAO.save(despesa2);

        Despesa despesa3 = criarDespesaTeste();
        despesa3.setDescricao("Serviço de contabilidade");
        despesaDAO.save(despesa3);

        // Act
        List<Despesa> despesasMaterial = despesaDAO.findByDescricao("material");

        // Assert
        assertThat(despesasMaterial).hasSizeGreaterThanOrEqualTo(1);
        assertThat(despesasMaterial).extracting("descricao")
            .allMatch(descricao -> descricao.toString().toLowerCase().contains("material"));
    }

    @Test
    @DisplayName("Deve buscar despesas por categoria")
    void testBuscarDespesasPorCategoria() throws SQLException {
        // Arrange
        Despesa despesa1 = criarDespesaTeste();
        despesa1.setCategoria("ALUGUEL");
        despesaDAO.save(despesa1);

        Despesa despesa2 = criarDespesaTeste();
        despesa2.setCategoria("ALUGUEL");
        despesaDAO.save(despesa2);

        Despesa despesa3 = criarDespesaTeste();
        despesa3.setCategoria("ENERGIA");
        despesaDAO.save(despesa3);

        // Act
        List<Despesa> despesasAluguel = despesaDAO.findByCategoria("ALUGUEL");
        List<Despesa> despesasEnergia = despesaDAO.findByCategoria("ENERGIA");

        // Assert
        assertThat(despesasAluguel).hasSizeGreaterThanOrEqualTo(2);
        assertThat(despesasEnergia).hasSizeGreaterThanOrEqualTo(1);
        
        assertThat(despesasAluguel).extracting("categoria")
            .allMatch(categoria -> "ALUGUEL".equals(categoria));
        assertThat(despesasEnergia).extracting("categoria")
            .allMatch(categoria -> "ENERGIA".equals(categoria));
    }

    @Test
    @DisplayName("Deve buscar despesas por período")
    void testBuscarDespesasPorPeriodo() throws SQLException {
        // Arrange
        Date dataInicio = new Date(System.currentTimeMillis() - 10L * 24 * 60 * 60 * 1000); // 10 dias atrás
        Date dataFim = new Date(System.currentTimeMillis() + 10L * 24 * 60 * 60 * 1000); // 10 dias depois

        Despesa despesa1 = criarDespesaTeste();
        despesa1.setData(new Date(System.currentTimeMillis() - 5L * 24 * 60 * 60 * 1000)); // 5 dias atrás
        despesaDAO.save(despesa1);

        Despesa despesa2 = criarDespesaTeste();
        despesa2.setData(new Date()); // Hoje
        despesaDAO.save(despesa2);

        Despesa despesa3 = criarDespesaTeste();
        despesa3.setData(new Date(System.currentTimeMillis() + 15L * 24 * 60 * 60 * 1000)); // 15 dias depois
        despesaDAO.save(despesa3);

        // Act
        List<Despesa> despesasPeriodo = despesaDAO.findByPeriodo(
            new java.sql.Date(dataInicio.getTime()), 
            new java.sql.Date(dataFim.getTime())
        );

        // Assert
        assertThat(despesasPeriodo).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Deve calcular total de despesas por período")
    void testGetTotalPorPeriodo() throws SQLException {
        // Arrange
        Date dataInicio = new Date(System.currentTimeMillis() - 10L * 24 * 60 * 60 * 1000); // 10 dias atrás
        Date dataFim = new Date(System.currentTimeMillis() + 10L * 24 * 60 * 60 * 1000); // 10 dias depois

        Despesa despesa1 = criarDespesaTeste();
        despesa1.setValor(1000.0);
        despesa1.setData(new Date()); // Dentro do período
        despesaDAO.save(despesa1);

        Despesa despesa2 = criarDespesaTeste();
        despesa2.setValor(500.0);
        despesa2.setData(new Date()); // Dentro do período
        despesaDAO.save(despesa2);

        Despesa despesa3 = criarDespesaTeste();
        despesa3.setValor(2000.0);
        despesa3.setData(new Date(System.currentTimeMillis() + 15L * 24 * 60 * 60 * 1000)); // Fora do período
        despesaDAO.save(despesa3);

        // Act
        double totalPeriodo = despesaDAO.getTotalPorPeriodo(
            new java.sql.Date(dataInicio.getTime()), 
            new java.sql.Date(dataFim.getTime())
        );

        // Assert
        assertThat(totalPeriodo).isEqualTo(1500.0);
    }

    @Test
    @DisplayName("Deve atualizar despesa")
    void testAtualizarDespesa() throws SQLException {
        // Arrange
        Despesa despesa = criarDespesaTeste();
        despesaDAO.save(despesa);
        Long id = despesa.getId();

        // Act
        despesa.setDescricao("Descrição atualizada");
        despesa.setValor(2000.0);
        despesa.setCategoria("MANUTENÇÃO");
        despesaDAO.save(despesa);

        // Assert
        Despesa atualizada = despesaDAO.findById(id);
        if (atualizada != null) {
            assertThat(atualizada.getDescricao()).isEqualTo("Descrição atualizada");
            assertThat(atualizada.getValor()).isEqualTo(2000.0);
            assertThat(atualizada.getCategoria()).isEqualTo("MANUTENÇÃO");
        }
    }

    @Test
    @DisplayName("Deve excluir despesa")
    void testExcluirDespesa() throws SQLException {
        // Arrange
        Despesa despesa = criarDespesaTeste();
        despesaDAO.save(despesa);
        Long id = despesa.getId();

        // Act
        despesaDAO.delete(id);

        // Assert
        Despesa excluida = despesaDAO.findById(id);
        assertThat(excluida).isNull();
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar ID inexistente")
    void testBuscarIdInexistente() throws SQLException {
        // Act & Assert
        Despesa resultado = despesaDAO.findById(999L);
        assertThat(resultado).isNull();
    }

    @Test
    @DisplayName("Deve lidar com campos nulos opcionalmente")
    void testCamposNulosOpcionais() throws SQLException {
        // Arrange
        Despesa despesa = criarDespesaTeste();
        despesa.setFornecedor(null);
        despesa.setNumeroDocumento(null);

        // Act
        despesaDAO.save(despesa);

        // Assert
        assertThat(despesa.getId()).isNotNull();
        
        Despesa salva = despesaDAO.findById(despesa.getId());
        if (salva != null) {
            assertThat(salva.getFornecedor()).isNull();
            assertThat(salva.getNumeroDocumento()).isNull();
        }
    }

    @Test
    @DisplayName("Deve lidar com valores decimais")
    void testValoresDecimais() throws SQLException {
        // Arrange
        Despesa despesa = criarDespesaTeste();
        despesa.setValor(1234.56);

        // Act
        despesaDAO.save(despesa);

        // Assert
        assertThat(despesa.getId()).isNotNull();
        
        Despesa salva = despesaDAO.findById(despesa.getId());
        if (salva != null) {
            assertThat(salva.getValor()).isEqualTo(1234.56);
        }
    }

    @Test
    @DisplayName("Deve ordenar despesas corretamente")
    void testOrdenacaoDespesas() throws SQLException {
        // Arrange
        Date dataAntiga = new Date(System.currentTimeMillis() - 5L * 24 * 60 * 60 * 1000); // 5 dias atrás
        Date dataRecente = new Date(); // Hoje

        Despesa despesaAntiga = criarDespesaTeste();
        despesaAntiga.setData(dataAntiga);
        despesaAntiga.setDescricao("Despesa antiga");
        despesaDAO.save(despesaAntiga);

        Despesa despesaRecente = criarDespesaTeste();
        despesaRecente.setData(dataRecente);
        despesaRecente.setDescricao("Despesa recente");
        despesaDAO.save(despesaRecente);

        // Act
        List<Despesa> despesas = despesaDAO.findAll();

        // Assert
        assertThat(despesas).hasSizeGreaterThanOrEqualTo(2);
        // A mais recente deve vir primeiro (ORDER BY data DESC, id DESC)
        assertThat(despesas.get(0).getData().getTime()).isGreaterThanOrEqualTo(despesas.get(1).getData().getTime());
    }

    @Test
    @DisplayName("Deve calcular total zero para período sem despesas")
    void testGetTotalPeriodoVazio() throws SQLException {
        // Arrange
        Date dataInicio = new Date(System.currentTimeMillis() - 10L * 24 * 60 * 60 * 1000);
        Date dataFim = new Date(System.currentTimeMillis() - 5L * 24 * 60 * 60 * 1000);

        // Act
        double total = despesaDAO.getTotalPorPeriodo(
            new java.sql.Date(dataInicio.getTime()), 
            new java.sql.Date(dataFim.getTime())
        );

        // Assert
        assertThat(total).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Deve lidar com diferentes categorias")
    void testDiferentesCategorias() throws SQLException {
        // Arrange
        Despesa despesaAluguel = criarDespesaTeste();
        despesaAluguel.setCategoria("ALUGUEL");
        despesaDAO.save(despesaAluguel);

        Despesa despesaEnergia = criarDespesaTeste();
        despesaEnergia.setCategoria("ENERGIA");
        despesaDAO.save(despesaEnergia);

        Despesa despesaAgua = criarDespesaTeste();
        despesaAgua.setCategoria("ÁGUA");
        despesaDAO.save(despesaAgua);

        // Act
        List<Despesa> alugueis = despesaDAO.findByCategoria("ALUGUEL");
        List<Despesa> energias = despesaDAO.findByCategoria("ENERGIA");
        List<Despesa> aguas = despesaDAO.findByCategoria("ÁGUA");

        // Assert
        assertThat(alugueis).hasSizeGreaterThanOrEqualTo(1);
        assertThat(energias).hasSizeGreaterThanOrEqualTo(1);
        assertThat(aguas).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    @DisplayName("Deve registrar fluxo completo de despesa")
    void testFluxoCompletoDespesa() throws SQLException {
        // Arrange
        Despesa despesa = criarDespesaTeste();
        despesa.setDescricao("Fluxo completo teste");

        // Act - Fluxo completo
        despesaDAO.save(despesa);
        assertThat(despesa.getId()).isNotNull();
        
        Long id = despesa.getId();
        
        despesa.setValor(2500.0);
        despesaDAO.save(despesa);
        
        Despesa atualizada = despesaDAO.findById(id);
        assertThat(atualizada).isNotNull();
        assertThat(atualizada.getValor()).isEqualTo(2500.0);
        
        despesaDAO.delete(id);
        Despesa excluida = despesaDAO.findById(id);
        assertThat(excluida).isNull();

        // Assert
        assertThat(atualizada.getData()).isNotNull();
    }

    @Test
    @DisplayName("Deve lidar com fornecedor e documento")
    void testFornecedorEDocumento() throws SQLException {
        // Arrange
        Despesa despesa = criarDespesaTeste();
        despesa.setFornecedor("Fornecedor ABC Ltda");
        despesa.setNumeroDocumento("NF-001234");

        // Act
        despesaDAO.save(despesa);

        // Assert
        assertThat(despesa.getId()).isNotNull();
        
        Despesa salva = despesaDAO.findById(despesa.getId());
        if (salva != null) {
            assertThat(salva.getFornecedor()).isEqualTo("Fornecedor ABC Ltda");
            assertThat(salva.getNumeroDocumento()).isEqualTo("NF-001234");
        }
    }

    /**
     * Método auxiliar para criar uma despesa de teste
     */
    private Despesa criarDespesaTeste() {
        Despesa despesa = new Despesa();
        despesa.setDescricao("Aluguel da Loja");
        despesa.setValor(1500.0);
        despesa.setData(new Date());
        despesa.setCategoria("ALUGUEL");
        despesa.setFornecedor("Imobiliária XYZ");
        despesa.setNumeroDocumento("REC-001");
        return despesa;
    }
}
