package com.artereal.swing.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes unitários para a classe Biblioteca
 */
@DisplayName("Testes Unitários - Biblioteca")
class BibliotecaTest {

    private Biblioteca biblioteca;

    @BeforeEach
    void setUp() {
        biblioteca = new Biblioteca();
    }

    @Test
    @DisplayName("Deve criar biblioteca com construtor padrão")
    void testConstrutorPadrao() {
        // Act
        Biblioteca novaBiblioteca = new Biblioteca();

        // Assert
        assertThat(novaBiblioteca).isNotNull();
        assertThat(novaBiblioteca.getId()).isNull();
        assertThat(novaBiblioteca.getTipo()).isNull();
        assertThat(novaBiblioteca.getTitulo()).isNull();
        assertThat(novaBiblioteca.getAutor()).isNull();
        assertThat(novaBiblioteca.getIsbn()).isNull();
        assertThat(novaBiblioteca.getStatus()).isNull();
    }

    @Test
    @DisplayName("Deve criar biblioteca do tipo LIVRO com construtor parametrizado")
    void testConstrutorParametrizadoLivro() {
        // Arrange
        String tipo = "LIVRO";
        String titulo = "Ritual do Aprendiz";

        // Act
        Biblioteca novaBiblioteca = new Biblioteca(tipo, titulo);

        // Assert
        assertThat(novaBiblioteca).isNotNull();
        assertThat(novaBiblioteca.getTipo()).isEqualTo(tipo);
        assertThat(novaBiblioteca.getTitulo()).isEqualTo(titulo);
        assertThat(novaBiblioteca.getStatus()).isEqualTo("DISPONIVEL");
    }

    @Test
    @DisplayName("Deve criar biblioteca do tipo EMPRESTIMO com construtor parametrizado")
    void testConstrutorParametrizadoEmprestimo() {
        // Arrange
        String tipo = "EMPRESTIMO";
        String titulo = "Empréstimo para João";

        // Act
        Biblioteca novaBiblioteca = new Biblioteca(tipo, titulo);

        // Assert
        assertThat(novaBiblioteca).isNotNull();
        assertThat(novaBiblioteca.getTipo()).isEqualTo(tipo);
        assertThat(novaBiblioteca.getTitulo()).isEqualTo(titulo);
        assertThat(novaBiblioteca.getStatus()).isEqualTo("EMPRESTADO");
    }

    @Test
    @DisplayName("Deve definir e obter ID")
    void testSetGetId() {
        // Arrange
        Long id = 123L;

        // Act
        biblioteca.setId(id);

        // Assert
        assertThat(biblioteca.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Deve definir e obter tipo")
    void testSetGetTipo() {
        // Arrange
        String tipo = "LIVRO";

        // Act
        biblioteca.setTipo(tipo);

        // Assert
        assertThat(biblioteca.getTipo()).isEqualTo(tipo);
    }

    @Test
    @DisplayName("Deve definir e obter título")
    void testSetGetTitulo() {
        // Arrange
        String titulo = "História da Maçonaria";

        // Act
        biblioteca.setTitulo(titulo);

        // Assert
        assertThat(biblioteca.getTitulo()).isEqualTo(titulo);
    }

    @Test
    @DisplayName("Deve definir e obter autor")
    void testSetGetAutor() {
        // Arrange
        String autor = "John Doe";

        // Act
        biblioteca.setAutor(autor);

        // Assert
        assertThat(biblioteca.getAutor()).isEqualTo(autor);
    }

    @Test
    @DisplayName("Deve definir e obter ISBN")
    void testSetGetIsbn() {
        // Arrange
        String isbn = "978-3-16-148410-0";

        // Act
        biblioteca.setIsbn(isbn);

        // Assert
        assertThat(biblioteca.getIsbn()).isEqualTo(isbn);
    }

    @Test
    @DisplayName("Deve definir e obter editora")
    void testSetGetEditora() {
        // Arrange
        String editora = "Editora Maçônica";

        // Act
        biblioteca.setEditora(editora);

        // Assert
        assertThat(biblioteca.getEditora()).isEqualTo(editora);
    }

    @Test
    @DisplayName("Deve definir e obter ano de publicação")
    void testSetGetAnoPublicacao() {
        // Arrange
        String anoPublicacao = "2023";

        // Act
        biblioteca.setAnoPublicacao(anoPublicacao);

        // Assert
        assertThat(biblioteca.getAnoPublicacao()).isEqualTo(anoPublicacao);
    }

    @Test
    @DisplayName("Deve definir e obter categoria")
    void testSetGetCategoria() {
        // Arrange
        String categoria = "RITUAL";

        // Act
        biblioteca.setCategoria(categoria);

        // Assert
        assertThat(biblioteca.getCategoria()).isEqualTo(categoria);
    }

    @Test
    @DisplayName("Deve definir e obter localização")
    void testSetGetLocalizacao() {
        // Arrange
        String localizacao = "Estante A, Prateleira 1";

        // Act
        biblioteca.setLocalizacao(localizacao);

        // Assert
        assertThat(biblioteca.getLocalizacao()).isEqualTo(localizacao);
    }

    @Test
    @DisplayName("Deve definir e obter status")
    void testSetGetStatus() {
        // Arrange
        String status = "EMPRESTADO";

        // Act
        biblioteca.setStatus(status);

        // Assert
        assertThat(biblioteca.getStatus()).isEqualTo(status);
    }

    @Test
    @DisplayName("Deve definir e obter nome do leitor")
    void testSetGetNomeLeitor() {
        // Arrange
        String nomeLeitor = "João da Silva";

        // Act
        biblioteca.setNomeLeitor(nomeLeitor);

        // Assert
        assertThat(biblioteca.getNomeLeitor()).isEqualTo(nomeLeitor);
    }

    @Test
    @DisplayName("Deve definir e obter data de empréstimo")
    void testSetGetDataEmprestimo() {
        // Arrange
        LocalDate dataEmprestimo = LocalDate.of(2024, 1, 1);

        // Act
        biblioteca.setDataEmprestimo(dataEmprestimo);

        // Assert
        assertThat(biblioteca.getDataEmprestimo()).isEqualTo(dataEmprestimo);
    }

    @Test
    @DisplayName("Deve definir e obter data de devolução prevista")
    void testSetGetDataDevolucaoPrevista() {
        // Arrange
        LocalDate dataDevolucaoPrevista = LocalDate.of(2024, 1, 15);

        // Act
        biblioteca.setDataDevolucaoPrevista(dataDevolucaoPrevista);

        // Assert
        assertThat(biblioteca.getDataDevolucaoPrevista()).isEqualTo(dataDevolucaoPrevista);
    }

    @Test
    @DisplayName("Deve definir e obter data de devolução real")
    void testSetGetDataDevolucaoReal() {
        // Arrange
        LocalDate dataDevolucaoReal = LocalDate.of(2024, 1, 14);

        // Act
        biblioteca.setDataDevolucaoReal(dataDevolucaoReal);

        // Assert
        assertThat(biblioteca.getDataDevolucaoReal()).isEqualTo(dataDevolucaoReal);
    }

    @Test
    @DisplayName("Deve definir e obter responsável pelo empréstimo")
    void testSetGetResponsavelEmprestimo() {
        // Arrange
        String responsavel = "Bibliotecário";

        // Act
        biblioteca.setResponsavelEmprestimo(responsavel);

        // Assert
        assertThat(biblioteca.getResponsavelEmprestimo()).isEqualTo(responsavel);
    }

    @Test
    @DisplayName("Deve definir e obter multa")
    void testSetGetMulta() {
        // Arrange
        BigDecimal multa = new BigDecimal("10.50");

        // Act
        biblioteca.setMulta(multa);

        // Assert
        assertThat(biblioteca.getMulta()).isEqualTo(multa);
    }

    @Test
    @DisplayName("Deve definir e obter observações")
    void testSetGetObservacoes() {
        // Arrange
        String observacoes = "Livro em bom estado";

        // Act
        biblioteca.setObservacoes(observacoes);

        // Assert
        assertThat(biblioteca.getObservacoes()).isEqualTo(observacoes);
    }

    @Test
    @DisplayName("Deve definir e obter created at")
    void testSetGetCreatedAt() {
        // Arrange
        String createdAt = "2024-01-01 10:00:00";

        // Act
        biblioteca.setCreatedAt(createdAt);

        // Assert
        assertThat(biblioteca.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Deve definir e obter updated at")
    void testSetGetUpdatedAt() {
        // Arrange
        String updatedAt = "2024-01-15 10:00:00";

        // Act
        biblioteca.setUpdatedAt(updatedAt);

        // Assert
        assertThat(biblioteca.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("Deve representar objeto como string")
    void testToString() {
        // Arrange
        biblioteca.setId(123L);
        biblioteca.setTipo("LIVRO");
        biblioteca.setTitulo("Ritual do Aprendiz");
        biblioteca.setAutor("Autor Maçônico");
        biblioteca.setStatus("DISPONIVEL");

        // Act
        String resultado = biblioteca.toString();

        // Assert - Simplificado para usar SimpleModel
        assertThat(resultado).contains("Biblioteca");
        assertThat(resultado).contains("id=123");
    }

    @Test
    @DisplayName("Deve comparar objetos iguais")
    void testEquals() {
        // Arrange
        Biblioteca biblioteca1 = new Biblioteca();
        biblioteca1.setId(123L);

        Biblioteca biblioteca2 = new Biblioteca();
        biblioteca2.setId(123L);

        Biblioteca biblioteca3 = new Biblioteca();
        biblioteca3.setId(456L);

        // Act & Assert
        assertThat(biblioteca1).isEqualTo(biblioteca2);
        assertThat(biblioteca1).isNotEqualTo(biblioteca3);
        assertThat(biblioteca1).isNotEqualTo(null);
        assertThat(biblioteca1).isNotEqualTo("string");
    }

    @Test
    @DisplayName("Deve ter mesmo hashCode para IDs iguais")
    void testHashCode() {
        // Arrange
        Biblioteca biblioteca1 = new Biblioteca();
        biblioteca1.setId(123L);

        Biblioteca biblioteca2 = new Biblioteca();
        biblioteca2.setId(123L);

        Biblioteca biblioteca3 = new Biblioteca();
        biblioteca3.setId(456L);

        // Act & Assert
        assertThat(biblioteca1.hashCode()).isEqualTo(biblioteca2.hashCode());
        assertThat(biblioteca1.hashCode()).isNotEqualTo(biblioteca3.hashCode());
    }

    @Test
    @DisplayName("Deve lidar com valores nulos em multa")
    void testMultaNula() {
        // Act
        biblioteca.setMulta(null);

        // Assert
        assertThat(biblioteca.getMulta()).isNull();
    }

    @Test
    @DisplayName("Deve lidar com multa zero")
    void testMultaZero() {
        // Arrange
        BigDecimal multaZero = BigDecimal.ZERO;

        // Act
        biblioteca.setMulta(multaZero);

        // Assert
        assertThat(biblioteca.getMulta()).isEqualTo(multaZero);
        assertThat(biblioteca.getMulta()).isEqualByComparingTo("0.00");
    }

    @Test
    @DisplayName("Deve lidar com datas de empréstimo futuras")
    void testDatasEmprestimoFuturas() {
        // Arrange
        LocalDate hoje = LocalDate.now();
        LocalDate dataFutura = hoje.plusDays(30);

        // Act
        biblioteca.setDataEmprestimo(hoje);
        biblioteca.setDataDevolucaoPrevista(dataFutura);

        // Assert
        assertThat(biblioteca.getDataEmprestimo()).isEqualTo(hoje);
        assertThat(biblioteca.getDataDevolucaoPrevista()).isEqualTo(dataFutura);
        assertThat(biblioteca.getDataDevolucaoPrevista()).isAfter(biblioteca.getDataEmprestimo());
    }

    @Test
    @DisplayName("Deve lidar com devolução antes da prevista")
    void testDevolucaoAntesPrevista() {
        // Arrange
        LocalDate dataEmprestimo = LocalDate.of(2024, 1, 1);
        LocalDate dataPrevista = LocalDate.of(2024, 1, 15);
        LocalDate dataReal = LocalDate.of(2024, 1, 10);

        // Act
        biblioteca.setDataEmprestimo(dataEmprestimo);
        biblioteca.setDataDevolucaoPrevista(dataPrevista);
        biblioteca.setDataDevolucaoReal(dataReal);

        // Assert
        assertThat(biblioteca.getDataDevolucaoReal()).isBefore(biblioteca.getDataDevolucaoPrevista());
    }

    @Test
    @DisplayName("Deve lidar com devolução após a prevista")
    void testDevolucaoAposPrevista() {
        // Arrange
        LocalDate dataEmprestimo = LocalDate.of(2024, 1, 1);
        LocalDate dataPrevista = LocalDate.of(2024, 1, 15);
        LocalDate dataReal = LocalDate.of(2024, 1, 20);

        // Act
        biblioteca.setDataEmprestimo(dataEmprestimo);
        biblioteca.setDataDevolucaoPrevista(dataPrevista);
        biblioteca.setDataDevolucaoReal(dataReal);

        // Assert
        assertThat(biblioteca.getDataDevolucaoReal()).isAfter(biblioteca.getDataDevolucaoPrevista());
    }

    @Test
    @DisplayName("Deve lidar com diferentes categorias de livros")
    void testDiferentesCategorias() {
        // Arrange & Act
        biblioteca.setCategoria("RITUAL");
        assertThat(biblioteca.getCategoria()).isEqualTo("RITUAL");

        biblioteca.setCategoria("HISTORIA");
        assertThat(biblioteca.getCategoria()).isEqualTo("HISTORIA");

        biblioteca.setCategoria("FILOSOFIA");
        assertThat(biblioteca.getCategoria()).isEqualTo("FILOSOFIA");

        biblioteca.setCategoria("SIMBOLISMO");
        assertThat(biblioteca.getCategoria()).isEqualTo("SIMBOLISMO");
    }

    @Test
    @DisplayName("Deve lidar com diferentes status")
    void testDiferentesStatus() {
        // Arrange & Act
        biblioteca.setStatus("DISPONIVEL");
        assertThat(biblioteca.getStatus()).isEqualTo("DISPONIVEL");

        biblioteca.setStatus("EMPRESTADO");
        assertThat(biblioteca.getStatus()).isEqualTo("EMPRESTADO");

        biblioteca.setStatus("EM_MANUTENCAO");
        assertThat(biblioteca.getStatus()).isEqualTo("EM_MANUTENCAO");

        biblioteca.setStatus("BAIXADO");
        assertThat(biblioteca.getStatus()).isEqualTo("BAIXADO");
    }
}
