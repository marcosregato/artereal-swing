package com.artereal.swing.integration.database;

import com.artereal.swing.database.DatabaseManager;
import com.artereal.swing.dao.IrmaoDAO;
import com.artereal.swing.model.Irmao;
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
 * Testes de integração simplificados para o banco de dados PostgreSQL
 */
@DisplayName("Testes de Integração do Banco de Dados (Simplificado)")
class DatabaseIntegrationTestSimplificado {

    private DatabaseManager databaseManager;
    private IrmaoDAO irmaoDAO;

    @BeforeEach
    void setUp() throws SQLException {
        // Usar instância padrão do DatabaseManager
        databaseManager = DatabaseManager.getInstance();
        irmaoDAO = new IrmaoDAO();
        
        // Inicializar banco de dados
        databaseManager.initializeDatabase();
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Limpar apenas a tabela irmao para evitar interferência entre testes
        try (Connection conn = databaseManager.getConnection()) {
            try {
                conn.createStatement().execute("DELETE FROM irmao");
            } catch (SQLException e) {
                // Se a tabela não existir, não é problema
                System.err.println("Erro ao limpar tabela irmao: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Deve conectar ao banco de dados")
    void testDatabaseConnection() throws SQLException {
        // Act
        Connection conn = databaseManager.getConnection();

        // Assert
        assertThat(conn).isNotNull();
        assertThat(conn.isClosed()).isFalse();
        
        conn.close();
        assertThat(conn.isClosed()).isTrue();
    }

    @Test
    @DisplayName("Deve criar e salvar irmão no banco")
    void testSalvarIrmao() throws SQLException {
        // Arrange
        Irmao irmao = criarIrmaoTeste();

        // Act
        irmaoDAO.save(irmao);

        // Assert
        assertThat(irmao.getId()).isNotNull();
        assertThat(irmao.getId()).isPositive();
    }

    @Test
    @DisplayName("Deve buscar irmão por ID")
    void testBuscarIrmaoPorId() throws SQLException {
        // Arrange
        Irmao irmao = criarIrmaoTeste();
        irmaoDAO.save(irmao);
        Long id = irmao.getId();

        // Act
        Irmao encontrado = irmaoDAO.findById(id);

        // Assert
        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getId()).isEqualTo(id);
        assertThat(encontrado.getNome()).isEqualTo("Teste Busca");
    }

    @Test
    @DisplayName("Deve buscar todos os irmãos")
    void testBuscarTodosIrmaos() throws SQLException {
        // Arrange
        Irmao irmao1 = criarIrmaoTeste();
        irmao1.setNome("Irmão 1");
        irmaoDAO.save(irmao1);

        Irmao irmao2 = criarIrmaoTeste();
        irmao2.setNome("Irmão 2");
        irmaoDAO.save(irmao2);

        // Act
        List<Irmao> irmaos = irmaoDAO.findAll();

        // Assert
        assertThat(irmaos).hasSize(2);
        assertThat(irmaos).extracting("nome")
            .containsExactlyInAnyOrder("Irmão 1", "Irmão 2");
    }

    @Test
    @DisplayName("Deve buscar irmãos por nome")
    void testBuscarIrmaosPorNome() throws SQLException {
        // Arrange
        Irmao irmao1 = criarIrmaoTeste();
        irmao1.setNome("João Silva");
        irmaoDAO.save(irmao1);

        Irmao irmao2 = criarIrmaoTeste();
        irmao2.setNome("João Santos");
        irmaoDAO.save(irmao2);

        Irmao irmao3 = criarIrmaoTeste();
        irmao3.setNome("Pedro Souza");
        irmaoDAO.save(irmao3);

        // Act
        List<Irmao> irmaos = irmaoDAO.findByNome("João");

        // Assert
        assertThat(irmaos).hasSize(2);
        assertThat(irmaos).extracting("nome")
            .allMatch(nome -> nome.toString().contains("João"));
    }

    @Test
    @DisplayName("Deve atualizar irmão existente")
    void testAtualizarIrmao() throws SQLException {
        // Arrange
        Irmao irmao = criarIrmaoTeste();
        irmaoDAO.save(irmao);
        Long id = irmao.getId();

        // Act
        irmao.setNome("Nome Atualizado");
        irmao.setTelefone("999999999");
        irmaoDAO.save(irmao);

        // Assert
        Irmao atualizado = irmaoDAO.findById(id);
        assertThat(atualizado.getNome()).isEqualTo("Nome Atualizado");
        assertThat(atualizado.getTelefone()).isEqualTo("999999999");
    }

    @Test
    @DisplayName("Deve excluir irmão")
    void testExcluirIrmao() throws SQLException {
        // Arrange
        Irmao irmao = criarIrmaoTeste();
        irmaoDAO.save(irmao);
        Long id = irmao.getId();

        // Act
        irmaoDAO.delete(id);

        // Assert
        Irmao excluido = irmaoDAO.findById(id);
        assertThat(excluido).isNull();
    }

    @Test
    @DisplayName("Deve retornar null para ID inexistente")
    void testBuscarIdInexistente() throws SQLException {
        // Act
        Irmao resultado = irmaoDAO.findById(999L);

        // Assert
        assertThat(resultado).isNull();
    }

    @Test
    @DisplayName("Deve lidar com campos nulos opcionalmente")
    void testCamposNulosOpcionais() throws SQLException {
        // Arrange
        Irmao irmao = new Irmao();
        irmao.setNome("Teste Nulos");
        irmao.setTelefone("123456789");
        // Campos opcionais como null
        irmao.setEmpresa(null);
        irmao.setTelefoneEmpresa(null);
        irmao.setEnderecoEmpresa(null);
        irmao.setCargoGrandeLoja(null);
        irmao.setRegistroGrandeLoja(null);

        // Act
        irmaoDAO.save(irmao);
        Irmao salvo = irmaoDAO.findById(irmao.getId());

        // Assert
        assertThat(salvo).isNotNull();
        assertThat(salvo.getNome()).isEqualTo("Teste Nulos");
        assertThat(salvo.getEmpresa()).isNull();
        assertThat(salvo.getTelefoneEmpresa()).isNull();
    }

    @Test
    @DisplayName("Deve validar persistência de dados")
    void testPersistenciaDados() throws SQLException {
        // Arrange
        Irmao irmao = criarIrmaoTeste();
        irmao.setNome("Teste Persistência");
        irmaoDAO.save(irmao);
        Long id = irmao.getId();

        // Act - Buscar em outra instância do DAO
        IrmaoDAO outroDAO = new IrmaoDAO();
        Irmao recuperado = outroDAO.findById(id);

        // Assert
        assertThat(recuperado).isNotNull();
        assertThat(recuperado.getNome()).isEqualTo("Teste Persistência");
        assertThat(recuperado.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Deve lidar com múltiplas operações")
    void testMultiplasOperacoes() throws SQLException {
        // Arrange & Act
        // Criar vários irmãos
        for (int i = 1; i <= 5; i++) {
            Irmao irmao = criarIrmaoTeste();
            irmao.setNome("Irmão " + i);
            irmao.setTelefone("12345678" + i);
            irmaoDAO.save(irmao);
        }

        // Assert
        List<Irmao> todos = irmaoDAO.findAll();
        assertThat(todos).hasSize(5);

        // Verificar que todos foram salvos corretamente
        for (int i = 1; i <= 5; i++) {
            List<Irmao> encontrados = irmaoDAO.findByNome("Irmão " + i);
            assertThat(encontrados).hasSize(1);
            assertThat(encontrados.get(0).getTelefone()).isEqualTo("12345678" + i);
        }
    }

    /**
     * Método auxiliar para criar um irmão de teste
     */
    private Irmao criarIrmaoTeste() {
        Irmao irmao = new Irmao();
        irmao.setNome("Teste Busca");
        irmao.setTelefone("123456789");
        irmao.setNascimento(LocalDate.of(1980, 5, 15));
        irmao.setEstadoCivil("Solteiro");
        irmao.setNatural("São Paulo");
        irmao.setIdentidade("123456789");
        irmao.setTipoSanguineo("O+");
        irmao.setCargoLoja("Aprendiz");
        irmao.setGrau("1");
        irmao.setEndereco("Rua Teste, 123");
        irmao.setBairro("Centro");
        irmao.setCidade("São Paulo");
        irmao.setEstado("SP");
        irmao.setEmpresa("Empresa Teste");
        irmao.setTelefoneEmpresa("987654321");
        irmao.setEnderecoEmpresa("Av. Empresarial, 456");
        irmao.setRegistroGrandeLoja("12345/SP");
        irmao.setAtivo(true);
        return irmao;
    }
}
