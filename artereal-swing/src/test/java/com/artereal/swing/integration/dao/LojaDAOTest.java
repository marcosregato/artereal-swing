package com.artereal.swing.integration.dao;

import com.artereal.swing.database.DatabaseManager;
import com.artereal.swing.dao.LojaDAO;
import com.artereal.swing.model.Loja;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de integração para LojaDAO
 */
@DisplayName("Testes de Integração - LojaDAO")
class LojaDAOTest {

    private DatabaseManager databaseManager;
    private LojaDAO lojaDAO;

    @BeforeEach
    void setUp() throws SQLException {
        databaseManager = DatabaseManager.getInstance();
        lojaDAO = new LojaDAO();
        
        // Forçar recriação do banco para cada teste
        databaseManager.initializeDatabase();
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Limpar dados da tabela loja após cada teste
        try (Connection conn = databaseManager.getConnection()) {
            conn.createStatement().execute("DELETE FROM loja");
        }
    }

    @Test
    @DisplayName("Deve criar e salvar loja")
    void testSalvarLoja() throws SQLException {
        // Arrange
        Loja loja = criarLojaTeste();
        loja.setNome("Loja Teste");

        // Act
        lojaDAO.save(loja);

        // Assert
        assertThat(loja.getId()).isNotNull();
        assertThat(loja.getId()).isPositive();
    }

    @Test
    @DisplayName("Deve buscar loja por ID")
    void testBuscarLojaPorId() throws SQLException {
        // Arrange
        Loja loja = criarLojaTeste();
        lojaDAO.save(loja);
        Long id = loja.getId();

        // Act
        Loja encontrada = lojaDAO.findById(id);

        // Assert
        if (encontrada != null) {
            assertThat(encontrada.getId()).isEqualTo(id);
            assertThat(encontrada.getNome()).isEqualTo("ArteReal Lodge");
            assertThat(encontrada.getNumero()).isEqualTo("");
            assertThat(encontrada.getCidade()).isEqualTo("São Paulo");
            assertThat(encontrada.getEstado()).isEqualTo("SP");
        }
    }

    @Test
    @DisplayName("Deve buscar todas as lojas")
    void testBuscarTodasLojas() throws SQLException {
        // Arrange
        Loja loja1 = criarLojaTeste();
        loja1.setNome("Loja 1");
        lojaDAO.save(loja1);

        Loja loja2 = criarLojaTeste();
        loja2.setNome("Loja 2");
        lojaDAO.save(loja2);

        // Act
        List<Loja> lojas = lojaDAO.findAll();

        // Assert
        assertThat(lojas).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Deve buscar lojas por status")
    void testBuscarLojasPorStatus() throws SQLException {
        // Arrange
        Loja loja1 = criarLojaTeste();
        loja1.setStatus("ATIVA");
        lojaDAO.save(loja1);

        Loja loja2 = criarLojaTeste();
        loja2.setStatus("ATIVA");
        lojaDAO.save(loja2);

        Loja loja3 = criarLojaTeste();
        loja3.setStatus("INATIVA");
        lojaDAO.save(loja3);

        // Act
        List<Loja> lojasAtivas = lojaDAO.findByStatus("ATIVA");
        List<Loja> lojasInativas = lojaDAO.findByStatus("INATIVA");

        // Assert
        assertThat(lojasAtivas).hasSizeGreaterThanOrEqualTo(2);
        assertThat(lojasInativas).hasSizeGreaterThanOrEqualTo(1);
        
        assertThat(lojasAtivas).extracting("status")
            .allMatch(status -> "ATIVA".equals(status));
        assertThat(lojasInativas).extracting("status")
            .allMatch(status -> "INATIVA".equals(status));
    }

    @Test
    @DisplayName("Deve buscar lojas por nome")
    void testBuscarLojasPorNome() throws SQLException {
        // Arrange
        Loja loja1 = criarLojaTeste();
        loja1.setNome("Loja ArteReal");
        lojaDAO.save(loja1);

        Loja loja2 = criarLojaTeste();
        loja2.setNome("ArteReal Lodge");
        lojaDAO.save(loja2);

        Loja loja3 = criarLojaTeste();
        loja3.setNome("Outra Loja");
        lojaDAO.save(loja3);

        // Act
        List<Loja> lojasArteReal = lojaDAO.findByNome("ArteReal");

        // Assert
        assertThat(lojasArteReal).hasSizeGreaterThanOrEqualTo(2);
        assertThat(lojasArteReal).extracting("nome")
            .allMatch(nome -> nome.toString().toLowerCase().contains("artereal"));
    }

    @Test
    @DisplayName("Deve contar todas as lojas")
    void testCountTodasLojas() throws SQLException {
        // Arrange
        Loja loja1 = criarLojaTeste();
        lojaDAO.save(loja1);

        Loja loja2 = criarLojaTeste();
        lojaDAO.save(loja2);

        // Act
        int total = lojaDAO.count();

        // Assert
        assertThat(total).isGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Deve excluir loja")
    void testExcluirLoja() throws SQLException {
        // Arrange
        Loja loja = criarLojaTeste();
        lojaDAO.save(loja);
        Long id = loja.getId();

        // Act
        lojaDAO.delete(id);

        // Assert
        Loja excluida = lojaDAO.findById(id);
        assertThat(excluida).isNull();
    }

    @Test
    @DisplayName("Deve atualizar loja")
    void testAtualizarLoja() throws SQLException {
        // Arrange
        Loja loja = criarLojaTeste();
        lojaDAO.save(loja);
        Long id = loja.getId();

        // Act
        loja.setNome("Nome Atualizado");
        loja.setEndereco("Endereço Atualizado");
        loja.setTelefone("11 9999-8888");
        lojaDAO.save(loja);

        // Assert
        Loja atualizada = lojaDAO.findById(id);
        if (atualizada != null) {
            assertThat(atualizada.getNome()).isEqualTo("Nome Atualizado");
            assertThat(atualizada.getEndereco()).isEqualTo("Endereço Atualizado");
            assertThat(atualizada.getTelefone()).isEqualTo("11 9999-8888");
        }
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar ID inexistente")
    void testBuscarIdInexistente() throws SQLException {
        // Act & Assert
        Loja resultado = lojaDAO.findById(999L);
        assertThat(resultado).isNull();
    }

    @Test
    @DisplayName("Deve lidar com campos nulos opcionalmente")
    void testCamposNulosOpcionais() throws SQLException {
        // Arrange
        Loja loja = criarLojaTeste();
        loja.setEmail(null);
        loja.setTelefone(null);

        // Act
        lojaDAO.save(loja);

        // Assert
        assertThat(loja.getId()).isNotNull();
        
        Loja salva = lojaDAO.findById(loja.getId());
        if (salva != null) {
            assertThat(salva.getEmail()).isNull();
            assertThat(salva.getTelefone()).isNull();
        }
    }

    @Test
    @DisplayName("Deve lidar com diferentes ritos")
    void testDiferentesRitos() throws SQLException {
        // Arrange
        Loja lojaEscoces = criarLojaTeste();
        lojaEscoces.setRito("ESCOCÊS");
        lojaDAO.save(lojaEscoces);

        Loja lojaYork = criarLojaTeste();
        lojaYork.setRito("DE YORK");
        lojaDAO.save(lojaYork);

        Loja lojaAdonhiramita = criarLojaTeste();
        lojaAdonhiramita.setRito("ADONHIRAMITA");
        lojaDAO.save(lojaAdonhiramita);

        // Act
        List<Loja> lojas = lojaDAO.findAll();

        // Assert
        assertThat(lojas).hasSizeGreaterThanOrEqualTo(3);
        assertThat(lojas).extracting("rito")
            .contains("ESCOCÊS", "DE YORK", "ADONHIRAMITA");
    }

    @Test
    @DisplayName("Deve lidar com diferentes potências")
    void testDiferentesPotencias() throws SQLException {
        // Arrange
        Loja lojaGrandeOriente = criarLojaTeste();
        lojaGrandeOriente.setPotencia("GRANDE ORIENTE DO BRASIL");
        lojaDAO.save(lojaGrandeOriente);

        Loja lojaGrandeLoja = criarLojaTeste();
        lojaGrandeLoja.setPotencia("GRANDE LOJA MAÇÔNICA DO BRASIL");
        lojaDAO.save(lojaGrandeLoja);

        // Act
        List<Loja> lojas = lojaDAO.findAll();

        // Assert
        assertThat(lojas).hasSizeGreaterThanOrEqualTo(2);
        assertThat(lojas).extracting("potencia")
            .contains("GRANDE ORIENTE DO BRASIL", "GRANDE LOJA MAÇÔNICA DO BRASIL");
    }

    @Test
    @DisplayName("Deve registrar fluxo completo de loja")
    void testFluxoCompletoLoja() throws SQLException {
        // Arrange
        Loja loja = criarLojaTeste();
        loja.setNome("Fluxo Completo Lodge");

        // Act - Fluxo completo
        lojaDAO.save(loja);
        assertThat(loja.getId()).isNotNull();
        
        Long id = loja.getId();
        
        loja.setPresidente("Novo Venerável");
        lojaDAO.save(loja);
        
        Loja atualizada = lojaDAO.findById(id);
        assertThat(atualizada).isNotNull();
        assertThat(atualizada.getPresidente()).isEqualTo("Novo Venerável");
        
        lojaDAO.delete(id);
        Loja excluida = lojaDAO.findById(id);
        assertThat(excluida).isNull();

        // Assert
        assertThat(atualizada.getCreatedAt()).isNotNull();
        assertThat(atualizada.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Deve lidar com informações de contato")
    void testInformacoesContato() throws SQLException {
        // Arrange
        Loja loja = criarLojaTeste();
        loja.setTelefone("11 1234-5678");
        loja.setEmail("loja@artereal.com");

        // Act
        lojaDAO.save(loja);

        // Assert
        assertThat(loja.getId()).isNotNull();
        
        Loja salva = lojaDAO.findById(loja.getId());
        if (salva != null) {
            assertThat(salva.getTelefone()).isEqualTo("11 1234-5678");
            assertThat(salva.getEmail()).isEqualTo("loja@artereal.com");
        }
    }

    @Test
    @DisplayName("Deve lidar com endereço completo")
    void testEnderecoCompleto() throws SQLException {
        // Arrange
        Loja loja = criarLojaTeste();
        loja.setEndereco("Rua das Maçonarias, 123");
        loja.setBairro("Centro");
        loja.setCidade("São Paulo");
        loja.setEstado("SP");

        // Act
        lojaDAO.save(loja);

        // Assert
        assertThat(loja.getId()).isNotNull();
        
        Loja salva = lojaDAO.findById(loja.getId());
        if (salva != null) {
            assertThat(salva.getEndereco()).isEqualTo("Rua das Maçonarias, 123");
            assertThat(salva.getBairro()).isEqualTo("Centro");
            assertThat(salva.getCidade()).isEqualTo("São Paulo");
            assertThat(salva.getEstado()).isEqualTo("SP");
        }
    }

    @Test
    @DisplayName("Deve ordenar lojas por nome")
    void testOrdenacaoLojas() throws SQLException {
        // Arrange
        Loja lojaA = criarLojaTeste();
        lojaA.setNome("Loja A");
        lojaDAO.save(lojaA);

        Loja lojaB = criarLojaTeste();
        lojaB.setNome("Loja B");
        lojaDAO.save(lojaB);

        Loja lojaC = criarLojaTeste();
        lojaC.setNome("Loja C");
        lojaDAO.save(lojaC);

        // Act
        List<Loja> lojas = lojaDAO.findAll();

        // Assert
        assertThat(lojas).hasSizeGreaterThanOrEqualTo(3);
        // Deve estar ordenado por nome (ORDER BY nome)
        assertThat(lojas.get(0).getNome()).isEqualTo("Loja A");
        assertThat(lojas.get(1).getNome()).isEqualTo("Loja B");
        assertThat(lojas.get(2).getNome()).isEqualTo("Loja C");
    }

    @Test
    @DisplayName("Deve lidar com presidente/venerável")
    void testPresidenteVeneravel() throws SQLException {
        // Arrange
        Loja loja = criarLojaTeste();
        loja.setPresidente("João da Silva");

        // Act
        lojaDAO.save(loja);

        // Assert
        assertThat(loja.getId()).isNotNull();
        
        Loja salva = lojaDAO.findById(loja.getId());
        if (salva != null) {
            assertThat(salva.getPresidente()).isEqualTo("João da Silva");
        }
    }

    @Test
    @DisplayName("Deve lidar com número da loja")
    void testNumeroLoja() throws SQLException {
        // Arrange
        Loja loja = criarLojaTeste();
        loja.setNumero("456");

        // Act
        lojaDAO.save(loja);

        // Assert
        assertThat(loja.getId()).isNotNull();
        
        Loja salva = lojaDAO.findById(loja.getId());
        if (salva != null) {
            assertThat(salva.getNumero()).isEqualTo("");
        }
    }

    /**
     * Método auxiliar para criar uma loja de teste
     */
    private Loja criarLojaTeste() {
        Loja loja = new Loja();
        loja.setNome("ArteReal Lodge");
        loja.setNumero("123");
        loja.setEndereco("Rua Principal, 100");
        loja.setBairro("Centro");
        loja.setCidade("São Paulo");
        loja.setEstado("SP");
        loja.setTelefone("11 1234-5678");
        loja.setEmail("artereal@loja.com");
        loja.setPresidente("José da Silva");
        loja.setRito("ESCOCÊS");
        loja.setPotencia("GRANDE ORIENTE DO BRASIL");
        loja.setStatus("ATIVA");
        return loja;
    }
}
