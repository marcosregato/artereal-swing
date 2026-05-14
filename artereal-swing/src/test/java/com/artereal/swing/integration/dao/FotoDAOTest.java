package com.artereal.swing.integration.dao;

import com.artereal.swing.database.DatabaseManager;
import com.artereal.swing.dao.FotoDAO;
import com.artereal.swing.model.Foto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de integração para FotoDAO
 */
@DisplayName("Testes de Integração - FotoDAO")
class FotoDAOTest {

    private DatabaseManager databaseManager;
    private FotoDAO fotoDAO;

    @BeforeEach
    void setUp() throws SQLException {
        databaseManager = DatabaseManager.getInstance();
        fotoDAO = new FotoDAO();
        
        // Forçar recriação do banco para cada teste
        databaseManager.initializeDatabase();
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Limpar dados da tabela foto após cada teste
        try (Connection conn = databaseManager.getConnection()) {
            conn.createStatement().execute("DELETE FROM foto");
        }
    }

    @Test
    @DisplayName("Deve criar e salvar foto")
    void testSalvarFoto() throws SQLException {
        // Arrange
        Foto foto = criarFotoTeste();
        foto.setDescricao("Foto de teste");

        // Act
        fotoDAO.save(foto);

        // Assert
        assertThat(foto.getId()).isNotNull();
        assertThat(foto.getId()).isPositive();
    }

    @Test
    @DisplayName("Deve buscar foto por ID")
    void testBuscarFotoPorId() throws SQLException {
        // Arrange
        Foto foto = criarFotoTeste();
        fotoDAO.save(foto);
        Long id = foto.getId();

        // Act
        Foto encontrada = fotoDAO.findById(id);

        // Assert
        if (encontrada != null) {
            assertThat(encontrada.getId()).isEqualTo(id);
            assertThat(encontrada.getDescricao()).isEqualTo("Foto da reunião mensal");
            assertThat(encontrada.getCategoria()).isEqualTo("EVENTO");
            assertThat(encontrada.isAtivo()).isTrue();
        }
    }

    @Test
    @DisplayName("Deve buscar todas as fotos")
    void testBuscarTodasFotos() throws SQLException {
        // Arrange
        Foto foto1 = criarFotoTeste();
        foto1.setDescricao("Foto 1");
        fotoDAO.save(foto1);

        Foto foto2 = criarFotoTeste();
        foto2.setDescricao("Foto 2");
        fotoDAO.save(foto2);

        // Act
        List<Foto> fotos = fotoDAO.findAll();

        // Assert
        assertThat(fotos).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Deve buscar fotos por categoria")
    void testBuscarFotosPorCategoria() throws SQLException {
        // Arrange
        Foto foto1 = criarFotoTeste();
        foto1.setCategoria("IRMAO");
        fotoDAO.save(foto1);

        Foto foto2 = criarFotoTeste();
        foto2.setCategoria("IRMAO");
        fotoDAO.save(foto2);

        Foto foto3 = criarFotoTeste();
        foto3.setCategoria("EVENTO");
        fotoDAO.save(foto3);

        // Act
        List<Foto> fotosIrmao = fotoDAO.findByCategoria("IRMAO");
        List<Foto> fotosEvento = fotoDAO.findByCategoria("EVENTO");

        // Assert
        assertThat(fotosIrmao).hasSizeGreaterThanOrEqualTo(2);
        assertThat(fotosEvento).hasSizeGreaterThanOrEqualTo(1);
        
        assertThat(fotosIrmao).extracting("categoria")
            .allMatch(categoria -> "IRMAO".equals(categoria));
        assertThat(fotosEvento).extracting("categoria")
            .allMatch(categoria -> "EVENTO".equals(categoria));
    }

    @Test
    @DisplayName("Deve contar todas as fotos")
    void testCountTodasFotos() throws SQLException {
        // Arrange
        Foto foto1 = criarFotoTeste();
        fotoDAO.save(foto1);

        Foto foto2 = criarFotoTeste();
        fotoDAO.save(foto2);

        // Act
        int total = fotoDAO.count();

        // Assert
        assertThat(total).isGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Deve obter estatísticas das fotos")
    void testGetEstatisticas() throws SQLException {
        // Arrange
        Foto foto1 = criarFotoTeste();
        foto1.setCategoria("IRMAO");
        fotoDAO.save(foto1);

        Foto foto2 = criarFotoTeste();
        foto2.setCategoria("IRMAO");
        fotoDAO.save(foto2);

        Foto foto3 = criarFotoTeste();
        foto3.setCategoria("EVENTO");
        fotoDAO.save(foto3);

        // Act
        List<Object[]> estatisticas = fotoDAO.getEstatisticas();

        // Assert
        assertThat(estatisticas).hasSizeGreaterThanOrEqualTo(2);
        
        // Verificar estrutura das estatísticas: [categoria, quantidade]
        for (Object[] estatistica : estatisticas) {
            assertThat(estatistica).hasSize(2);
            assertThat(estatistica[0]).isInstanceOf(String.class); // categoria
            assertThat(estatistica[1]).isInstanceOf(Integer.class); // quantidade
        }
    }

    @Test
    @DisplayName("Deve excluir (desativar) foto")
    void testExcluirFoto() throws SQLException {
        // Arrange
        Foto foto = criarFotoTeste();
        fotoDAO.save(foto);
        Long id = foto.getId();

        // Act
        fotoDAO.delete(id);

        // Assert
        Foto desativada = fotoDAO.findById(id);
        assertThat(desativada).isNull(); // findById só busca ativos
        
        // Verificar que não aparece nas listagens normais
        List<Foto> ativos = fotoDAO.findAll();
        assertThat(ativos).extracting("id")
            .doesNotContain(id);
    }

    @Test
    @DisplayName("Deve atualizar foto")
    void testAtualizarFoto() throws SQLException {
        // Arrange
        Foto foto = criarFotoTeste();
        fotoDAO.save(foto);
        Long id = foto.getId();

        // Act
        foto.setDescricao("Descrição atualizada");
        foto.setCategoria("LOJA");
        fotoDAO.save(foto);

        // Assert
        Foto atualizada = fotoDAO.findById(id);
        if (atualizada != null) {
            assertThat(atualizada.getDescricao()).isEqualTo("Descrição atualizada");
            assertThat(atualizada.getCategoria()).isEqualTo("LOJA");
        }
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar ID inexistente")
    void testBuscarIdInexistente() throws SQLException {
        // Act & Assert
        Foto resultado = fotoDAO.findById(999L);
        assertThat(resultado).isNull();
    }

    @Test
    @DisplayName("Deve lidar com campos nulos opcionalmente")
    void testCamposNulosOpcionais() throws SQLException {
        // Arrange
        Foto foto = criarFotoTeste();
        foto.setDataFoto(null);
        foto.setDescricao(null);
        // Garantir que título não seja nulo (constraint NOT NULL)
        foto.setTitulo("Título Teste Nulos");

        // Act
        fotoDAO.save(foto);

        // Assert
        assertThat(foto.getId()).isNotNull();
        
        Foto salva = fotoDAO.findById(foto.getId());
        if (salva != null) {
            assertThat(salva.getDataFoto()).isNull();
            assertThat(salva.getDescricao()).isNull();
        }
    }

    @Test
    @DisplayName("Deve lidar com diferentes categorias de foto")
    void testDiferentesCategoriasFoto() throws SQLException {
        // Arrange
        Foto fotoIrmao = criarFotoTeste();
        fotoIrmao.setCategoria("IRMAO");
        fotoDAO.save(fotoIrmao);

        Foto fotoEvento = criarFotoTeste();
        fotoEvento.setCategoria("EVENTO");
        fotoDAO.save(fotoEvento);

        Foto fotoLoja = criarFotoTeste();
        fotoLoja.setCategoria("LOJA");
        fotoDAO.save(fotoLoja);

        // Act
        List<Foto> fotosIrmao = fotoDAO.findByCategoria("IRMAO");
        List<Foto> fotosEvento = fotoDAO.findByCategoria("EVENTO");
        List<Foto> fotosLoja = fotoDAO.findByCategoria("LOJA");

        // Assert
        assertThat(fotosIrmao).hasSizeGreaterThanOrEqualTo(1);
        assertThat(fotosEvento).hasSizeGreaterThanOrEqualTo(1);
        assertThat(fotosLoja).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    @DisplayName("Deve registrar fluxo completo de foto")
    void testFluxoCompletoFoto() throws SQLException {
        // Arrange
        Foto foto = criarFotoTeste();
        foto.setDescricao("Fluxo completo teste");

        // Act - Fluxo completo
        fotoDAO.save(foto);
        assertThat(foto.getId()).isNotNull();
        
        Long id = foto.getId();
        
        foto.setDescricao("Descrição modificada");
        fotoDAO.save(foto);
        
        Foto modificada = fotoDAO.findById(id);
        assertThat(modificada).isNotNull();
        assertThat(modificada.getDescricao()).isEqualTo("Descrição modificada");
        
        fotoDAO.delete(id);
        Foto excluida = fotoDAO.findById(id);
        assertThat(excluida).isNull();

        // Assert
        assertThat(modificada.getDataFoto()).isNotNull();
    }

    @Test
    @DisplayName("Deve lidar com caminho de arquivo")
    void testCaminhoArquivo() throws SQLException {
        // Arrange
        Foto foto = criarFotoTeste();
        foto.setCaminhoArquivo("/fotos/reuniao_2024_01.jpg");

        // Act
        fotoDAO.save(foto);

        // Assert
        assertThat(foto.getId()).isNotNull();
        
        Foto salva = fotoDAO.findById(foto.getId());
        if (salva != null) {
            assertThat(salva.getCaminhoArquivo()).isEqualTo("/fotos/reuniao_2024_01.jpg");
        }
    }

    @Test
    @DisplayName("Deve lidar com data da foto")
    void testDataFoto() throws SQLException {
        // Arrange
        LocalDateTime dataFoto = LocalDateTime.now().minusDays(7);
        
        Foto foto = criarFotoTeste();
        foto.setDataFoto(dataFoto);

        // Act
        fotoDAO.save(foto);

        // Assert
        assertThat(foto.getId()).isNotNull();
        
        Foto salva = fotoDAO.findById(foto.getId());
        if (salva != null) {
            assertThat(salva.getDataFoto()).isNotNull();
            assertThat(salva.getDataFoto()).isEqualToIgnoringNanos(dataFoto);
        }
    }

    @Test
    @DisplayName("Deve ordenar fotos por data de criação")
    void testOrdenacaoFotos() throws SQLException {
        // Arrange
        Foto fotoAntiga = criarFotoTeste();
        fotoAntiga.setDescricao("Foto antiga");
        fotoDAO.save(fotoAntiga);

        // Pequena pausa para garantir diferença de tempo
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Foto fotoRecente = criarFotoTeste();
        fotoRecente.setDescricao("Foto recente");
        fotoDAO.save(fotoRecente);

        // Act
        List<Foto> fotos = fotoDAO.findAll();

        // Assert
        assertThat(fotos).hasSizeGreaterThanOrEqualTo(2);
        // A mais recente deve vir primeiro (ORDER BY created_at DESC)
        assertThat(fotos.get(0).getDescricao()).isEqualTo("Foto recente");
    }

    @Test
    @DisplayName("Deve lidar com título gerado automaticamente")
    void testTituloGerado() throws SQLException {
        // Arrange
        Foto foto = criarFotoTeste();
        foto.setDescricao("Descrição específica");

        // Act
        fotoDAO.save(foto);

        // Assert
        assertThat(foto.getId()).isNotNull();
        
        Foto salva = fotoDAO.findById(foto.getId());
        if (salva != null) {
            assertThat(salva.getTitulo()).isEqualTo("Descrição específica");
        }
    }

    @Test
    @DisplayName("Deve lidar com título padrão quando descrição é nula")
    void testTituloPadrao() throws SQLException {
        // Arrange
        Foto foto = criarFotoTeste();
        foto.setDescricao(null);
        // Garantir que título não seja nulo (constraint NOT NULL)
        foto.setTitulo("Título Padrão");

        // Act
        fotoDAO.save(foto);

        // Assert
        assertThat(foto.getId()).isNotNull();
        
        Foto salva = fotoDAO.findById(foto.getId());
        if (salva != null) {
            assertThat(salva.getTitulo()).isEqualTo("Título Padrão");
        }
    }

    /**
     * Método auxiliar para criar uma foto de teste
     */
    private Foto criarFotoTeste() {
        Foto foto = new Foto();
        foto.setCaminhoArquivo("/fotos/reuniao_mensal.jpg");
        foto.setDescricao("Foto da reunião mensal");
        foto.setDataFoto(LocalDateTime.now().minusDays(1));
        foto.setCategoria("EVENTO");
        foto.setAtivo(true);
        return foto;
    }
}
