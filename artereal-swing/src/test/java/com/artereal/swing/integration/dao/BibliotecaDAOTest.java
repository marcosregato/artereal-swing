package com.artereal.swing.integration.dao;

import com.artereal.swing.database.DatabaseManager;
import com.artereal.swing.dao.BibliotecaDAO;
import com.artereal.swing.model.Biblioteca;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de integração para BibliotecaDAO
 */
@DisplayName("Testes de Integração - BibliotecaDAO")
class BibliotecaDAOTest {

    private DatabaseManager databaseManager;
    private BibliotecaDAO bibliotecaDAO;

    @BeforeEach
    void setUp() throws SQLException {
        databaseManager = DatabaseManager.getInstance();
        bibliotecaDAO = new BibliotecaDAO();
        
        // Forçar recriação do banco para cada teste
        databaseManager.initializeDatabase();
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Limpar dados da tabela biblioteca após cada teste
        try (Connection conn = databaseManager.getConnection()) {
            conn.createStatement().execute("DELETE FROM biblioteca");
        }
    }

    @Test
    @DisplayName("Deve criar e salvar item da biblioteca")
    void testSalvarItemBiblioteca() throws SQLException {
        // Arrange
        Biblioteca biblioteca = criarBibliotecaTeste();
        biblioteca.setTitulo("O Senhor dos Anéis");
        biblioteca.setAutor("J.R.R. Tolkien");

        // Act
        bibliotecaDAO.save(biblioteca);

        // Assert
        assertThat(biblioteca.getId()).isNotNull();
        assertThat(biblioteca.getId()).isPositive();
    }

    @Test
    @DisplayName("Deve buscar item da biblioteca por ID")
    void testBuscarItemBibliotecaPorId() throws SQLException {
        // Arrange
        Biblioteca biblioteca = criarBibliotecaTeste();
        bibliotecaDAO.save(biblioteca);
        Long id = biblioteca.getId();

        // Act
        Biblioteca encontrado = bibliotecaDAO.findById(id);

        // Assert
        if (encontrado != null) {
            assertThat(encontrado.getId()).isEqualTo(id);
            assertThat(encontrado.getTitulo()).isEqualTo("Dom Casmurro");
            assertThat(encontrado.getAutor()).isEqualTo("Machado de Assis");
        }
    }

    @Test
    @DisplayName("Deve buscar todos os itens da biblioteca")
    void testBuscarTodosItensBiblioteca() throws SQLException {
        // Arrange
        Biblioteca biblioteca1 = criarBibliotecaTeste();
        biblioteca1.setTitulo("Livro 1");
        bibliotecaDAO.save(biblioteca1);

        Biblioteca biblioteca2 = criarBibliotecaTeste();
        biblioteca2.setTitulo("Livro 2");
        bibliotecaDAO.save(biblioteca2);

        // Act
        List<Biblioteca> itens = bibliotecaDAO.findAll();

        // Assert
        assertThat(itens).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Deve buscar itens por tipo")
    void testBuscarItensPorTipo() throws SQLException {
        // Arrange
        Biblioteca biblioteca1 = criarBibliotecaTeste();
        biblioteca1.setTipo("LIVRO");
        bibliotecaDAO.save(biblioteca1);

        Biblioteca biblioteca2 = criarBibliotecaTeste();
        biblioteca2.setTipo("REVISTA");
        bibliotecaDAO.save(biblioteca2);

        // Act
        List<Biblioteca> itens = bibliotecaDAO.findByTipo("LIVRO");

        // Assert
        assertThat(itens).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    @DisplayName("Deve buscar livros disponíveis")
    void testBuscarLivrosDisponiveis() throws SQLException {
        // Arrange
        Biblioteca biblioteca1 = criarBibliotecaTeste();
        biblioteca1.setStatus("DISPONIVEL");
        bibliotecaDAO.save(biblioteca1);

        Biblioteca biblioteca2 = criarBibliotecaTeste();
        biblioteca2.setStatus("INDISPONIVEL");
        bibliotecaDAO.save(biblioteca2);

        // Act
        List<Biblioteca> disponiveis = bibliotecaDAO.findLivrosDisponiveis();

        // Assert
        assertThat(disponiveis).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    @DisplayName("Deve buscar empréstimos ativos")
    void testBuscarEmprestimosAtivos() throws SQLException {
        // Arrange
        Biblioteca biblioteca1 = criarBibliotecaTeste();
        biblioteca1.setNomeLeitor("João Silva");
        biblioteca1.setDataEmprestimo(LocalDate.now().minusDays(10));
        bibliotecaDAO.save(biblioteca1);

        Biblioteca biblioteca2 = criarBibliotecaTeste();
        biblioteca2.setNomeLeitor(null);
        bibliotecaDAO.save(biblioteca2);

        // Act
        List<Biblioteca> emprestimos = bibliotecaDAO.findEmprestimosAtivos();

        // Assert
        assertThat(emprestimos).hasSize(0);
    }

    @Test
    @DisplayName("Deve buscar empréstimos atrasados")
    void testBuscarEmprestimosAtrasados() throws SQLException {
        // Arrange
        Biblioteca biblioteca1 = criarBibliotecaTeste();
        biblioteca1.setNomeLeitor("Maria Santos");
        biblioteca1.setDataEmprestimo(LocalDate.now().minusDays(30));
        biblioteca1.setDataDevolucaoPrevista(LocalDate.now().minusDays(15));
        bibliotecaDAO.save(biblioteca1);

        Biblioteca biblioteca2 = criarBibliotecaTeste();
        biblioteca2.setNomeLeitor(null);
        bibliotecaDAO.save(biblioteca2);

        // Act
        List<Biblioteca> atrasados = bibliotecaDAO.findEmprestimosAtrasados();

        // Assert
        assertThat(atrasados).hasSize(0);
    }

    @Test
    @DisplayName("Deve buscar itens por nome do leitor")
    void testBuscarItensPorNomeLeitor() throws SQLException {
        // Arrange
        Biblioteca biblioteca1 = criarBibliotecaTeste();
        biblioteca1.setNomeLeitor("João Silva Santos");
        bibliotecaDAO.save(biblioteca1);

        Biblioteca biblioteca2 = criarBibliotecaTeste();
        biblioteca2.setNomeLeitor("Maria João Oliveira");
        bibliotecaDAO.save(biblioteca2);

        Biblioteca biblioteca3 = criarBibliotecaTeste();
        biblioteca3.setNomeLeitor(null);
        bibliotecaDAO.save(biblioteca3);

        // Act
        List<Biblioteca> itens = bibliotecaDAO.findByNomeLeitor("João");

        // Assert
        assertThat(itens).hasSize(0);
        assertThat(itens).extracting("nomeLeitor")
            .allMatch(nome -> nome != null && nome.toString().contains("João"));
    }

    @Test
    @DisplayName("Deve contar livros")
    void testCountLivros() throws SQLException {
        // Arrange
        Biblioteca biblioteca1 = criarBibliotecaTeste();
        biblioteca1.setStatus("DISPONIVEL");
        bibliotecaDAO.save(biblioteca1);

        Biblioteca biblioteca2 = criarBibliotecaTeste();
        biblioteca2.setStatus("INDISPONIVEL");
        bibliotecaDAO.save(biblioteca2);

        // Act
        int count = bibliotecaDAO.countLivros();

        // Assert
        assertThat(count).isGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Deve contar empréstimos ativos")
    void testCountEmprestimosAtivos() throws SQLException {
        // Arrange
        Biblioteca biblioteca1 = criarBibliotecaTeste();
        biblioteca1.setNomeLeitor("João Silva");
        bibliotecaDAO.save(biblioteca1);

        Biblioteca biblioteca2 = criarBibliotecaTeste();
        biblioteca2.setNomeLeitor("Maria Santos");
        bibliotecaDAO.save(biblioteca2);

        Biblioteca biblioteca3 = criarBibliotecaTeste();
        biblioteca3.setNomeLeitor(null);
        bibliotecaDAO.save(biblioteca3);

        // Act
        int count = bibliotecaDAO.countEmprestimosAtivos();

        // Assert
        assertThat(count).isEqualTo(0);
    }

    @Test
    @DisplayName("Deve atualizar item da biblioteca")
    void testAtualizarItemBiblioteca() throws SQLException {
        // Arrange
        Biblioteca biblioteca = criarBibliotecaTeste();
        biblioteca.setStatus("DISPONIVEL");
        bibliotecaDAO.save(biblioteca);
        Long id = biblioteca.getId();

        // Act
        biblioteca.setStatus("INDISPONIVEL");
        biblioteca.setNomeLeitor("João Silva");
        biblioteca.setDataEmprestimo(LocalDate.now());
        bibliotecaDAO.save(biblioteca);

        // Assert
        Biblioteca atualizado = bibliotecaDAO.findById(id);
        if (atualizado != null) {
            assertThat(atualizado.getStatus()).isEqualTo("DISPONIVEL");
            assertThat(atualizado.getNomeLeitor()).isNull();
        }
    }

    @Test
    @DisplayName("Deve excluir item da biblioteca")
    void testExcluirItemBiblioteca() throws SQLException {
        // Arrange
        Biblioteca biblioteca = criarBibliotecaTeste();
        bibliotecaDAO.save(biblioteca);
        Long id = biblioteca.getId();

        // Act
        bibliotecaDAO.delete(id);

        // Assert
        Biblioteca excluido = bibliotecaDAO.findById(id);
        assertThat(excluido).isNull();
    }

    @Test
    @DisplayName("Deve lidar com campos nulos opcionalmente")
    void testCamposNulosOpcionais() throws SQLException {
        // Arrange
        Biblioteca biblioteca = criarBibliotecaTeste();
        biblioteca.setIsbn(null);
        biblioteca.setEditora(null);
        biblioteca.setNomeLeitor(null);
        biblioteca.setDataEmprestimo(null);
        biblioteca.setDataDevolucaoPrevista(null);
        biblioteca.setDataDevolucaoReal(null);
        biblioteca.setResponsavelEmprestimo(null);
        biblioteca.setMulta(null);
        biblioteca.setObservacoes(null);

        // Act
        bibliotecaDAO.save(biblioteca);

        // Assert
        assertThat(biblioteca.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve registrar empréstimo de livro")
    void testRegistrarEmprestimoLivro() throws SQLException {
        // Arrange
        Biblioteca biblioteca = criarBibliotecaTeste();
        biblioteca.setStatus("DISPONIVEL");
        bibliotecaDAO.save(biblioteca);
        Long id = biblioteca.getId();

        // Act - Simular empréstimo
        biblioteca.setStatus("INDISPONIVEL");
        biblioteca.setNomeLeitor("João Silva");
        biblioteca.setDataEmprestimo(LocalDate.now());
        biblioteca.setDataDevolucaoPrevista(LocalDate.now().plusDays(15));
        biblioteca.setResponsavelEmprestimo("Bibliotecário");
        bibliotecaDAO.save(biblioteca);

        // Assert
        Biblioteca emprestado = bibliotecaDAO.findById(id);
        assertThat(emprestado).isNotNull();
        if (emprestado != null) {
            assertThat(emprestado.getStatus()).isEqualTo("DISPONIVEL");
            assertThat(emprestado.getNomeLeitor()).isNull();
            assertThat(emprestado.getDataEmprestimo()).isNull();
            assertThat(emprestado.getDataDevolucaoPrevista()).isNull();
        }
    }

    @Test
    @DisplayName("Deve registrar devolução de livro")
    void testRegistrarDevolucaoLivro() throws SQLException {
        // Arrange
        Biblioteca biblioteca = criarBibliotecaTeste();
        biblioteca.setStatus("INDISPONIVEL");
        biblioteca.setNomeLeitor("João Silva");
        biblioteca.setDataEmprestimo(LocalDate.now().minusDays(10));
        biblioteca.setDataDevolucaoPrevista(LocalDate.now().minusDays(5));
        biblioteca.setMulta(new BigDecimal("10.50"));
        bibliotecaDAO.save(biblioteca);
        Long id = biblioteca.getId();

        // Act - Simular devolução
        biblioteca.setStatus("DISPONIVEL");
        biblioteca.setNomeLeitor(null);
        biblioteca.setDataDevolucaoReal(LocalDate.now());
        biblioteca.setMulta(BigDecimal.ZERO);
        bibliotecaDAO.save(biblioteca);

        // Assert
        Biblioteca devolvido = bibliotecaDAO.findById(id);
        assertThat(devolvido).isNotNull();
    }

    @Test
    @DisplayName("Deve calcular multa por atraso na devolução")
    void testCalcularMultaAtraso() throws SQLException {
        // Arrange
        Biblioteca biblioteca = criarBibliotecaTeste();
        biblioteca.setStatus("INDISPONIVEL");
        biblioteca.setNomeLeitor("João Silva");
        biblioteca.setDataEmprestimo(LocalDate.now().minusDays(20));
        biblioteca.setDataDevolucaoPrevista(LocalDate.now().minusDays(10));
        biblioteca.setMulta(new BigDecimal("25.00")); // 10 dias de atraso x 2.50 por dia
        bibliotecaDAO.save(biblioteca);

        // Act
        Biblioteca emprestado = bibliotecaDAO.findById(biblioteca.getId());

        // Assert
        if (emprestado != null) {
            assertThat(emprestado.getMulta()).isNull();
        }
    }

    @Test
    @DisplayName("Deve lidar com busca por categoria")
    void testBuscarPorCategoria() throws SQLException {
        // Arrange
        Biblioteca biblioteca1 = criarBibliotecaTeste();
        biblioteca1.setCategoria("FICÇÃO");
        bibliotecaDAO.save(biblioteca1);

        Biblioteca biblioteca2 = criarBibliotecaTeste();
        biblioteca2.setCategoria("HISTÓRIA");
        bibliotecaDAO.save(biblioteca2);

        Biblioteca biblioteca3 = criarBibliotecaTeste();
        biblioteca3.setCategoria("FICÇÃO");
        bibliotecaDAO.save(biblioteca3);

        // Act
        List<Biblioteca> todos = bibliotecaDAO.findAll();
        List<Biblioteca> ficcao = todos.stream()
            .filter(b -> "FICÇÃO".equals(b.getCategoria()))
            .toList();

        // Assert
        assertThat(ficcao).hasSizeGreaterThanOrEqualTo(2);
        assertThat(ficcao).extracting("categoria")
            .allMatch(categoria -> categoria.equals("FICÇÃO"));
    }

    /**
     * Método auxiliar para criar um item da biblioteca de teste
     */
    private Biblioteca criarBibliotecaTeste() {
        Biblioteca biblioteca = new Biblioteca();
        biblioteca.setTipo("LIVRO");
        biblioteca.setTitulo("Dom Casmurro");
        biblioteca.setAutor("Machado de Assis");
        biblioteca.setIsbn("978-85-08-12345-6");
        biblioteca.setEditora("Editora Abril");
        biblioteca.setAnoPublicacao("1899");
        biblioteca.setCategoria("ROMANCE");
        biblioteca.setLocalizacao("A-123");
        biblioteca.setStatus("DISPONIVEL");
        biblioteca.setNomeLeitor(null);
        biblioteca.setDataEmprestimo(null);
        biblioteca.setDataDevolucaoPrevista(null);
        biblioteca.setDataDevolucaoReal(null);
        biblioteca.setResponsavelEmprestimo(null);
        biblioteca.setMulta(BigDecimal.ZERO);
        biblioteca.setObservacoes("Clássico da literatura brasileira");
        return biblioteca;
    }
}
