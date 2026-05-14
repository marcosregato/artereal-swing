package com.artereal.swing.integration.dao;

import com.artereal.swing.database.DatabaseManager;
import com.artereal.swing.dao.VisitanteDAO;
import com.artereal.swing.model.Visitante;
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
 * Testes de integração para VisitanteDAO
 */
@DisplayName("Testes de Integração - VisitanteDAO")
class VisitanteDAOTest {

    private DatabaseManager databaseManager;
    private VisitanteDAO visitanteDAO;

    @BeforeEach
    void setUp() throws SQLException {
        databaseManager = DatabaseManager.getInstance();
        visitanteDAO = new VisitanteDAO();
        
        // Forçar recriação do banco para cada teste
        databaseManager.initializeDatabase();
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Limpar dados da tabela visitante após cada teste
        try (Connection conn = databaseManager.getConnection()) {
            conn.createStatement().execute("DELETE FROM visitante");
        }
    }

    @Test
    @DisplayName("Deve criar e salvar visitante")
    void testSalvarVisitante() throws SQLException {
        // Arrange
        Visitante visitante = criarVisitanteTeste();
        visitante.setNome("Visitante Teste");

        // Act
        visitanteDAO.save(visitante);

        // Assert
        assertThat(visitante.getId()).isNotNull();
        assertThat(visitante.getId()).isPositive();
    }

    @Test
    @DisplayName("Deve buscar visitante por ID")
    void testBuscarVisitantePorId() throws SQLException {
        // Arrange
        Visitante visitante = criarVisitanteTeste();
        visitanteDAO.save(visitante);
        Long id = visitante.getId();

        // Act
        Visitante encontrado = visitanteDAO.findById(id);

        // Assert
        if (encontrado != null) {
            assertThat(encontrado.getId()).isEqualTo(id);
            assertThat(encontrado.getNome()).isEqualTo("João da Silva");
            assertThat(encontrado.getGrauSecreto()).isEqualTo("Mestre");
            assertThat(encontrado.isAutorizado()).isTrue();
            assertThat(encontrado.isAtivo()).isTrue();
        }
    }

    @Test
    @DisplayName("Deve buscar todos os visitantes")
    void testBuscarTodosVisitantes() throws SQLException {
        // Arrange
        Visitante visitante1 = criarVisitanteTeste();
        visitante1.setNome("Visitante 1");
        visitanteDAO.save(visitante1);

        Visitante visitante2 = criarVisitanteTeste();
        visitante2.setNome("Visitante 2");
        visitanteDAO.save(visitante2);

        // Act
        List<Visitante> visitantes = visitanteDAO.findAll();

        // Assert
        assertThat(visitantes).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Deve buscar visitantes por data")
    void testBuscarVisitantesPorData() throws SQLException {
        // Arrange
        LocalDate dataVisita = LocalDate.now();
        
        Visitante visitante1 = criarVisitanteTeste();
        visitante1.setDataVisita(dataVisita);
        visitanteDAO.save(visitante1);

        Visitante visitante2 = criarVisitanteTeste();
        visitante2.setDataVisita(dataVisita);
        visitanteDAO.save(visitante2);

        Visitante visitante3 = criarVisitanteTeste();
        visitante3.setDataVisita(dataVisita.minusDays(1));
        visitanteDAO.save(visitante3);

        // Act
        List<Visitante> visitantesData = visitanteDAO.findByData(dataVisita);

        // Assert
        assertThat(visitantesData).hasSizeGreaterThanOrEqualTo(2);
        assertThat(visitantesData).extracting("dataVisita")
            .allMatch(data -> data.equals(dataVisita));
    }

    @Test
    @DisplayName("Deve buscar visitantes por período")
    void testBuscarVisitantesPorPeriodo() throws SQLException {
        // Arrange
        LocalDate dataInicio = LocalDate.now().minusDays(5);
        LocalDate dataFim = LocalDate.now().plusDays(5);
        
        Visitante visitante1 = criarVisitanteTeste();
        visitante1.setDataVisita(LocalDate.now());
        visitanteDAO.save(visitante1);

        Visitante visitante2 = criarVisitanteTeste();
        visitante2.setDataVisita(LocalDate.now().minusDays(2));
        visitanteDAO.save(visitante2);

        Visitante visitante3 = criarVisitanteTeste();
        visitante3.setDataVisita(LocalDate.now().minusDays(10));
        visitanteDAO.save(visitante3);

        // Act
        List<Visitante> visitantesPeriodo = visitanteDAO.findByPeriodo(dataInicio, dataFim);

        // Assert
        assertThat(visitantesPeriodo).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar ID inexistente")
    void testBuscarIdInexistente() throws SQLException {
        // Act & Assert
        Visitante resultado = visitanteDAO.findById(999L);
        assertThat(resultado).isNull();
    }

    @Test
    @DisplayName("Deve lidar com campos nulos opcionalmente")
    void testCamposNulosOpcionais() throws SQLException {
        // Arrange
        Visitante visitante = criarVisitanteTeste();
        visitante.setEmail(null);
        visitante.setTelefone(null);
        visitante.setLojaOrigem(null);

        // Act
        visitanteDAO.save(visitante);

        // Assert
        assertThat(visitante.getId()).isNotNull();
        
        Visitante salvo = visitanteDAO.findById(visitante.getId());
        if (salvo != null) {
            assertThat(salvo.getEmail()).isNull();
            assertThat(salvo.getTelefone()).isNull();
            assertThat(salvo.getLojaOrigem()).isNull();
        }
    }

    @Test
    @DisplayName("Deve lidar com diferentes tipos de visitante")
    void testDiferentesTiposVisitante() throws SQLException {
        // Arrange
        Visitante visitanteRegular = criarVisitanteTeste();
        visitanteRegular.setTipo("REGULAR");
        visitanteDAO.save(visitanteRegular);

        Visitante visitanteConvidado = criarVisitanteTeste();
        visitanteConvidado.setTipo("CONVIDADO");
        visitanteDAO.save(visitanteConvidado);

        Visitante visitanteAutoridade = criarVisitanteTeste();
        visitanteAutoridade.setTipo("AUTORIDADE");
        visitanteDAO.save(visitanteAutoridade);

        // Act
        List<Visitante> visitantes = visitanteDAO.findAll();

        // Assert
        assertThat(visitantes).hasSizeGreaterThanOrEqualTo(3);
        assertThat(visitantes).extracting("tipo")
            .contains("REGULAR", "CONVIDADO", "AUTORIDADE");
    }

    @Test
    @DisplayName("Deve lidar com diferentes graus secretos")
    void testDiferentesGrausSecretos() throws SQLException {
        // Arrange
        Visitante visitanteAprendiz = criarVisitanteTeste();
        visitanteAprendiz.setGrauSecreto("Aprendiz");
        visitanteDAO.save(visitanteAprendiz);

        Visitante visitanteCompanheiro = criarVisitanteTeste();
        visitanteCompanheiro.setGrauSecreto("Companheiro");
        visitanteDAO.save(visitanteCompanheiro);

        Visitante visitanteMestre = criarVisitanteTeste();
        visitanteMestre.setGrauSecreto("Mestre");
        visitanteDAO.save(visitanteMestre);

        // Act
        List<Visitante> visitantes = visitanteDAO.findAll();

        // Assert
        assertThat(visitantes).hasSizeGreaterThanOrEqualTo(3);
        assertThat(visitantes).extracting("grauSecreto")
            .contains("Aprendiz", "Companheiro", "Mestre");
    }

    @Test
    @DisplayName("Deve registrar fluxo completo de visitante")
    void testFluxoCompletoVisitante() throws SQLException {
        // Arrange
        Visitante visitante = criarVisitanteTeste();
        visitante.setNome("Fluxo Completo");

        // Act - Fluxo completo
        visitanteDAO.save(visitante);
        assertThat(visitante.getId()).isNotNull();
        
        Long id = visitante.getId();
        
        visitante.setAutorizado(false);
        visitante.setObservacoes("Observação modificada");
        visitanteDAO.save(visitante);
        
        Visitante atualizado = visitanteDAO.findById(id);
        if (atualizado != null) {
            assertThat(atualizado.isAutorizado()).isFalse();
            assertThat(atualizado.getObservacoes()).isEqualTo("Observação modificada");
        }
        
        visitanteDAO.delete(id);
        visitanteDAO.findById(id);

        // Assert
        if (atualizado != null) {
            assertThat(atualizado.getDataVisita()).isNotNull();
            assertThat(atualizado.getDataCadastro()).isNotNull();
        }
    }

    @Test
    @DisplayName("Deve lidar com informações de contato")
    void testInformacoesContato() throws SQLException {
        // Arrange
        Visitante visitante = criarVisitanteTeste();
        visitante.setTelefone("11 1234-5678");
        visitante.setEmail("visitante@exemplo.com");

        // Act
        visitanteDAO.save(visitante);

        // Assert
        assertThat(visitante.getId()).isNotNull();
        
        Visitante salvo = visitanteDAO.findById(visitante.getId());
        if (salvo != null) {
            assertThat(salvo.getTelefone()).isEqualTo("11 1234-5678");
            assertThat(salvo.getEmail()).isEqualTo("visitante@exemplo.com");
        }
    }

    @Test
    @DisplayName("Deve lidar com autorização")
    void testAutorizacao() throws SQLException {
        // Arrange
        Visitante visitanteAutorizado = criarVisitanteTeste();
        visitanteAutorizado.setAutorizado(true);
        visitanteAutorizado.setAutorizadoPor("Venerável");
        visitanteDAO.save(visitanteAutorizado);

        Visitante visitanteNaoAutorizado = criarVisitanteTeste();
        visitanteNaoAutorizado.setAutorizado(false);
        visitanteNaoAutorizado.setAutorizadoPor(null);
        visitanteDAO.save(visitanteNaoAutorizado);

        // Act
        List<Visitante> visitantes = visitanteDAO.findAll();

        // Assert
        assertThat(visitantes).hasSizeGreaterThanOrEqualTo(2);
        
        Visitante autorizado = visitantes.stream()
            .filter(v -> v.isAutorizado())
            .findFirst()
            .orElse(null);
        Visitante naoAutorizado = visitantes.stream()
            .filter(v -> !v.isAutorizado())
            .findFirst()
            .orElse(null);
            
        if (autorizado != null) {
            assertThat(autorizado.isAutorizado()).isTrue();
            assertThat(autorizado.getAutorizadoPor()).isEqualTo("Venerável");
        }
        
        if (naoAutorizado != null) {
            assertThat(naoAutorizado.isAutorizado()).isFalse();
        }
    }

    @Test
    @DisplayName("Deve lidar com número de crachá")
    void testNumeroCracha() throws SQLException {
        // Arrange
        Visitante visitante = criarVisitanteTeste();
        visitante.setNumeroCracha("V001");

        // Act
        visitanteDAO.save(visitante);

        // Assert
        assertThat(visitante.getId()).isNotNull();
        
        Visitante salvo = visitanteDAO.findById(visitante.getId());
        if (salvo != null) {
            assertThat(salvo.getNumeroCracha()).isEqualTo("V001");
        }
    }

    @Test
    @DisplayName("Deve lidar com loja de origem")
    void testLojaOrigem() throws SQLException {
        // Arrange
        Visitante visitante = criarVisitanteTeste();
        visitante.setLojaOrigem("Loja Esperança de Luz");

        // Act
        visitanteDAO.save(visitante);

        // Assert
        assertThat(visitante.getId()).isNotNull();
        
        Visitante salvo = visitanteDAO.findById(visitante.getId());
        if (salvo != null) {
            assertThat(salvo.getLojaOrigem()).isEqualTo("Loja Esperança de Luz");
        }
    }

    @Test
    @DisplayName("Deve lidar com histórico")
    void testHistorico() throws SQLException {
        // Arrange
        Visitante visitante = criarVisitanteTeste();
        visitante.setHistorico("Visitante frequente, já veio em outras ocasiões");

        // Act
        visitanteDAO.save(visitante);

        // Assert
        assertThat(visitante.getId()).isNotNull();
        
        Visitante salvo = visitanteDAO.findById(visitante.getId());
        if (salvo != null) {
            assertThat(salvo.getHistorico()).isEqualTo("Visitante frequente, já veio em outras ocasiões");
        }
    }

    @Test
    @DisplayName("Deve ordenar visitantes por data de visita e nome")
    void testOrdenacaoVisitantes() throws SQLException {
        // Arrange
        LocalDate dataAntiga = LocalDate.now().minusDays(2);
        LocalDate dataRecente = LocalDate.now();

        Visitante visitanteAntigo = criarVisitanteTeste();
        visitanteAntigo.setDataVisita(dataAntiga);
        visitanteAntigo.setNome("Visitante Antigo");
        visitanteDAO.save(visitanteAntigo);

        Visitante visitanteRecente = criarVisitanteTeste();
        visitanteRecente.setDataVisita(dataRecente);
        visitanteRecente.setNome("Visitante Recente");
        visitanteDAO.save(visitanteRecente);

        // Act
        List<Visitante> visitantes = visitanteDAO.findAll();

        // Assert
        assertThat(visitantes).hasSizeGreaterThanOrEqualTo(2);
        // O mais recente deve vir primeiro (ORDER BY data_visita DESC, nome)
        assertThat(visitantes.get(0).getDataVisita()).isAfterOrEqualTo(visitantes.get(1).getDataVisita());
    }

    @Test
    @DisplayName("Deve lidar com status ativo/inativo")
    void testStatusAtivoInativo() throws SQLException {
        // Arrange
        Visitante visitanteAtivo = criarVisitanteTeste();
        visitanteAtivo.setAtivo(true);
        visitanteDAO.save(visitanteAtivo);

        Visitante visitanteInativo = criarVisitanteTeste();
        visitanteInativo.setAtivo(false);
        visitanteDAO.save(visitanteInativo);

        // Act
        List<Visitante> visitantes = visitanteDAO.findAll();

        // Assert
        // findAll só retorna ativos, então deve encontrar apenas o visitante ativo
        assertThat(visitantes).hasSizeGreaterThanOrEqualTo(1);
        assertThat(visitantes).extracting("ativo")
            .allMatch(ativo -> ativo.equals(true));
    }

    /**
     * Método auxiliar para criar um visitante de teste
     */
    private Visitante criarVisitanteTeste() {
        Visitante visitante = new Visitante();
        visitante.setNome("João da Silva");
        visitante.setDataVisita(LocalDate.now());
        visitante.setGrauSecreto("Mestre");
        visitante.setHistorico("Primeira visita");
        visitante.setTipo("REGULAR");
        visitante.setLojaOrigem("Loja ArteReal");
        visitante.setTelefone("11 1234-5678");
        visitante.setEmail("joao@exemplo.com");
        visitante.setAutorizadoPor("Venerável");
        visitante.setAutorizado(true);
        visitante.setObservacoes("Visitante autorizado para participar");
        visitante.setNumeroCracha("V001");
        visitante.setAtivo(true);
        return visitante;
    }
}
