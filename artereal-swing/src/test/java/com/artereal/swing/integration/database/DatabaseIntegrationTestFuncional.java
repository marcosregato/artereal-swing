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
 * Testes de integração funcionais para o banco de dados
 * Foco em testar as operações básicas do DAO
 */
@DisplayName("Testes de Integração do Banco de Dados (Funcional)")
class DatabaseIntegrationTestFuncional {

    private DatabaseManager databaseManager;
    private IrmaoDAO irmaoDAO;

    @BeforeEach
    void setUp() throws SQLException {
        // Usar instância padrão do DatabaseManager
        databaseManager = DatabaseManager.getInstance();
        irmaoDAO = new IrmaoDAO();
        
        // Inicializar banco de dados se necessário
        try {
            databaseManager.initializeDatabase();
        } catch (Exception e) {
            // Banco já inicializado, continuar
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Não limpar o banco para evitar erros
        // Em produção, os testes devem ser independentes
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
        Irmao irmao = criarIrmaoTesteUnico();

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
        Irmao irmao = criarIrmaoTesteUnico();
        irmao.setNome("Teste Busca ID " + System.currentTimeMillis());
        irmaoDAO.save(irmao);
        Long id = irmao.getId();

        // Act
        Irmao encontrado = irmaoDAO.findById(id);

        // Assert
        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getId()).isEqualTo(id);
        assertThat(encontrado.getNome()).contains("Teste Busca ID");
    }

    @Test
    @DisplayName("Deve buscar todos os irmãos")
    void testBuscarTodosIrmaos() throws SQLException {
        // Arrange
        List<Irmao> irmaosAntes = irmaoDAO.findAll();
        int quantidadeAntes = irmaosAntes.size();

        Irmao irmao1 = criarIrmaoTesteUnico();
        irmao1.setNome("Irmão Teste 1 " + System.currentTimeMillis());
        irmaoDAO.save(irmao1);

        Irmao irmao2 = criarIrmaoTesteUnico();
        irmao2.setNome("Irmão Teste 2 " + System.currentTimeMillis());
        irmaoDAO.save(irmao2);

        // Act
        List<Irmao> irmaosDepois = irmaoDAO.findAll();

        // Assert
        assertThat(irmaosDepois.size()).isEqualTo(quantidadeAntes + 2);
        
        // Verificar que nossos irmãos estão na lista
        boolean encontrou1 = irmaosDepois.stream()
            .anyMatch(i -> i.getNome().contains("Irmão Teste 1"));
        boolean encontrou2 = irmaosDepois.stream()
            .anyMatch(i -> i.getNome().contains("Irmão Teste 2"));
        
        assertThat(encontrou1).isTrue();
        assertThat(encontrou2).isTrue();
    }

    @Test
    @DisplayName("Deve buscar irmãos por nome")
    void testBuscarIrmaosPorNome() throws SQLException {
        // Arrange
        String nomeUnico = "João Teste " + System.currentTimeMillis();
        
        Irmao irmao1 = criarIrmaoTesteUnico();
        irmao1.setNome(nomeUnico + " Silva");
        irmaoDAO.save(irmao1);

        Irmao irmao2 = criarIrmaoTesteUnico();
        irmao2.setNome(nomeUnico + " Santos");
        irmaoDAO.save(irmao2);

        // Act
        List<Irmao> irmaos = irmaoDAO.findByNome(nomeUnico);

        // Assert
        assertThat(irmaos).hasSize(2);
        assertThat(irmaos).extracting("nome")
            .allMatch(nome -> nome.toString().contains(nomeUnico));
    }

    @Test
    @DisplayName("Deve atualizar irmão existente")
    void testAtualizarIrmao() throws SQLException {
        // Arrange
        Irmao irmao = criarIrmaoTesteUnico();
        irmao.setNome("Teste Atualizar " + System.currentTimeMillis());
        irmaoDAO.save(irmao);
        Long id = irmao.getId();

        // Act
        irmao.setNome("Nome Atualizado " + System.currentTimeMillis());
        irmao.setTelefone("999999999");
        irmaoDAO.save(irmao);

        // Assert
        Irmao atualizado = irmaoDAO.findById(id);
        assertThat(atualizado).isNotNull();
        assertThat(atualizado.getNome()).contains("Nome Atualizado");
        assertThat(atualizado.getTelefone()).isEqualTo("999999999");
    }

    @Test
    @DisplayName("Deve excluir irmão")
    void testExcluirIrmao() throws SQLException {
        // Arrange
        Irmao irmao = criarIrmaoTesteUnico();
        irmao.setNome("Teste Excluir " + System.currentTimeMillis());
        irmaoDAO.save(irmao);
        Long id = irmao.getId();

        // Verificar que o irmão existe
        Irmao antesDeExcluir = irmaoDAO.findById(id);
        assertThat(antesDeExcluir).isNotNull();

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
        Irmao resultado = irmaoDAO.findById(999999L);

        // Assert
        assertThat(resultado).isNull();
    }

    @Test
    @DisplayName("Deve lidar com campos nulos opcionalmente")
    void testCamposNulosOpcionais() throws SQLException {
        // Arrange
        Irmao irmao = new Irmao();
        irmao.setNome("Teste Nulos " + System.currentTimeMillis());
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
        assertThat(salvo.getNome()).contains("Teste Nulos");
        assertThat(salvo.getEmpresa()).isNull();
        assertThat(salvo.getTelefoneEmpresa()).isNull();
    }

    @Test
    @DisplayName("Deve validar persistência de dados")
    void testPersistenciaDados() throws SQLException {
        // Arrange
        Irmao irmao = criarIrmaoTesteUnico();
        irmao.setNome("Teste Persistência " + System.currentTimeMillis());
        irmaoDAO.save(irmao);
        Long id = irmao.getId();

        // Act - Buscar em outra instância do DAO
        IrmaoDAO outroDAO = new IrmaoDAO();
        Irmao recuperado = outroDAO.findById(id);

        // Assert
        assertThat(recuperado).isNotNull();
        assertThat(recuperado.getNome()).contains("Teste Persistência");
        assertThat(recuperado.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Deve lidar com múltiplas operações")
    void testMultiplasOperacoes() throws SQLException {
        // Arrange
        String timestamp = String.valueOf(System.currentTimeMillis());
        List<Long> ids = new java.util.ArrayList<>();

        // Act & Assert - Criar vários irmãos
        for (int i = 1; i <= 3; i++) {
            Irmao irmao = criarIrmaoTesteUnico();
            irmao.setNome("Irmão " + i + " " + timestamp);
            irmao.setTelefone("12345678" + i);
            irmaoDAO.save(irmao);
            ids.add(irmao.getId());
            
            // Verificar que foi salvo
            assertThat(irmao.getId()).isNotNull();
        }

        // Verificar que todos foram encontrados
        for (Long id : ids) {
            Irmao encontrado = irmaoDAO.findById(id);
            assertThat(encontrado).isNotNull();
            assertThat(encontrado.getNome()).contains(timestamp);
        }

        // Verificar busca por nome
        for (int i = 1; i <= 3; i++) {
            List<Irmao> encontrados = irmaoDAO.findByNome("Irmão " + i + " " + timestamp);
            assertThat(encontrados).hasSize(1);
            assertThat(encontrados.get(0).getTelefone()).isEqualTo("12345678" + i);
        }
    }

    /**
     * Método auxiliar para criar um irmão de teste com dados únicos
     */
    private Irmao criarIrmaoTesteUnico() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        
        Irmao irmao = new Irmao();
        irmao.setNome("Teste " + timestamp);
        irmao.setTelefone("1234567" + timestamp.substring(timestamp.length() - 3));
        irmao.setNascimento(LocalDate.of(1980, 5, 15));
        irmao.setEstadoCivil("Solteiro");
        irmao.setNatural("São Paulo");
        irmao.setIdentidade("12345678" + timestamp.substring(timestamp.length() - 3));
        irmao.setTipoSanguineo("O+");
        irmao.setCargoLoja("Aprendiz");
        irmao.setGrau("1");
        irmao.setEndereco("Rua Teste " + timestamp + ", 123");
        irmao.setBairro("Centro");
        irmao.setCidade("São Paulo");
        irmao.setEstado("SP");
        irmao.setEmpresa("Empresa Teste " + timestamp);
        irmao.setTelefoneEmpresa("9876543" + timestamp.substring(timestamp.length() - 3));
        irmao.setEnderecoEmpresa("Av. Empresarial " + timestamp + ", 456");
        irmao.setRegistroGrandeLoja("12345/SP-" + timestamp.substring(timestamp.length() - 3));
        irmao.setAtivo(true);
        return irmao;
    }
}
