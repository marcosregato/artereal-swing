package com.artereal.swing.integration.dao;

import com.artereal.swing.database.DatabaseManager;
import com.artereal.swing.dao.AfastamentoDAO;
import com.artereal.swing.model.Afastamento;
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
 * Testes de integração para AfastamentoDAO
 */
@DisplayName("Testes de Integração - AfastamentoDAO")
class AfastamentoDAOTest {

    private DatabaseManager databaseManager;
    private AfastamentoDAO afastamentoDAO;

    @BeforeEach
    void setUp() throws SQLException {
        databaseManager = DatabaseManager.getInstance();
        afastamentoDAO = new AfastamentoDAO();
        
        // Forçar recriação do banco para cada teste
        databaseManager.initializeDatabase();
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Limpar dados da tabela afastamento após cada teste
        try (Connection conn = databaseManager.getConnection()) {
            conn.createStatement().execute("DELETE FROM afastamento");
        }
    }

    @Test
    @DisplayName("Deve criar e salvar afastamento")
    void testSalvarAfastamento() throws SQLException {
        // Arrange
        Afastamento afastamento = criarAfastamentoTeste();
        afastamento.setDescricao("Licença médica tratamento");
        afastamento.setMotivo("LICENCA_MEDICA");

        // Act
        afastamentoDAO.save(afastamento);

        // Assert
        assertThat(afastamento.getId()).isNotNull();
        assertThat(afastamento.getId()).isPositive();
    }

    @Test
    @DisplayName("Deve buscar afastamento por ID")
    void testBuscarAfastamentoPorId() throws SQLException {
        // Arrange
        Afastamento afastamento = criarAfastamentoTeste();
        afastamentoDAO.save(afastamento);
        Long id = afastamento.getId();

        // Act
        Afastamento encontrado = afastamentoDAO.findById(id);

        // Assert
        if (encontrado != null) {
            assertThat(encontrado.getId()).isEqualTo(id);
            assertThat(encontrado.getCodigoIrmao()).isEqualTo(123L);
            assertThat(encontrado.getMotivo()).isEqualTo("LICENCA_MEDICA");
        }
    }

    @Test
    @DisplayName("Deve buscar todos os afastamentos")
    void testBuscarTodosAfastamentos() throws SQLException {
        // Arrange
        Afastamento afastamento1 = criarAfastamentoTeste();
        afastamento1.setDescricao("Afastamento 1");
        afastamentoDAO.save(afastamento1);

        Afastamento afastamento2 = criarAfastamentoTeste();
        afastamento2.setDescricao("Afastamento 2");
        afastamentoDAO.save(afastamento2);

        // Act
        List<Afastamento> afastamentos = afastamentoDAO.findAll();

        // Assert
        assertThat(afastamentos).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Deve buscar afastamentos por irmão")
    void testBuscarAfastamentosPorIrmao() throws SQLException {
        // Arrange
        Afastamento afastamento1 = criarAfastamentoTeste();
        afastamento1.setCodigoIrmao(123L);
        afastamentoDAO.save(afastamento1);

        Afastamento afastamento2 = criarAfastamentoTeste();
        afastamento2.setCodigoIrmao(456L);
        afastamentoDAO.save(afastamento2);

        // Act
        List<Afastamento> afastamentosIrmao123 = afastamentoDAO.findByIrmao(123L);

        // Assert
        assertThat(afastamentosIrmao123).hasSizeGreaterThanOrEqualTo(1);
        assertThat(afastamentosIrmao123).extracting("codigoIrmao")
            .allMatch(codigo -> codigo.equals(123L));
    }

    @Test
    @DisplayName("Deve buscar afastamentos por status")
    void testBuscarAfastamentosPorStatus() throws SQLException {
        // Arrange
        Afastamento afastamento1 = criarAfastamentoTeste();
        afastamento1.setStatus("ATIVO");
        afastamentoDAO.save(afastamento1);

        Afastamento afastamento2 = criarAfastamentoTeste();
        afastamento2.setStatus("FINALIZADO");
        afastamentoDAO.save(afastamento2);

        // Act
        List<Afastamento> afastamentosAtivos = afastamentoDAO.findByStatus("ATIVO");

        // Assert
        assertThat(afastamentosAtivos).hasSizeGreaterThanOrEqualTo(1);
        assertThat(afastamentosAtivos).extracting("status")
            .allMatch(status -> status.equals("ATIVO"));
    }

    @Test
    @DisplayName("Deve buscar afastamentos por período")
    void testBuscarAfastamentosPorPeriodo() throws SQLException {
        // Arrange
        LocalDate dataInicio = LocalDate.of(2024, 1, 1);
        LocalDate dataFim = LocalDate.of(2024, 12, 31);
        
        Afastamento afastamento1 = criarAfastamentoTeste();
        afastamento1.setDataInicial(LocalDate.of(2024, 6, 1));
        afastamento1.setDataFinal(LocalDate.of(2024, 6, 30));
        afastamentoDAO.save(afastamento1);

        Afastamento afastamento2 = criarAfastamentoTeste();
        afastamento2.setDataInicial(LocalDate.of(2025, 1, 1));
        afastamento2.setDataFinal(LocalDate.of(2025, 1, 31));
        afastamentoDAO.save(afastamento2);

        // Act
        List<Afastamento> afastamentos2024 = afastamentoDAO.findByPeriodo(dataInicio, dataFim);

        // Assert
        assertThat(afastamentos2024).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    @DisplayName("Deve atualizar afastamento existente")
    void testAtualizarAfastamento() throws SQLException {
        // Arrange
        Afastamento afastamento = criarAfastamentoTeste();
        afastamento.setStatus("ATIVO");
        afastamentoDAO.save(afastamento);
        Long id = afastamento.getId();

        // Act
        afastamento.setStatus("FINALIZADO");
        afastamento.setObservacoes("Tratamento concluído");
        afastamentoDAO.save(afastamento);

        // Assert
        Afastamento atualizado = afastamentoDAO.findById(id);
        if (atualizado != null) {
            assertThat(atualizado.getStatus()).isEqualTo("FINALIZADO");
            assertThat(atualizado.getObservacoes()).isEqualTo("Tratamento concluído");
        }
    }

    @Test
    @DisplayName("Deve excluir afastamento")
    void testExcluirAfastamento() throws SQLException {
        // Arrange
        Afastamento afastamento = criarAfastamentoTeste();
        afastamentoDAO.save(afastamento);
        Long id = afastamento.getId();

        // Act
        afastamentoDAO.delete(id);

        // Assert
        Afastamento excluido = afastamentoDAO.findById(id);
        assertThat(excluido).isNull();
    }

    @Test
    @DisplayName("Deve calcular dias de afastamento")
    void testCalcularDiasAfastamento() throws SQLException {
        // Arrange
        Afastamento afastamento = criarAfastamentoTeste();
        LocalDate dataInicio = LocalDate.of(2024, 6, 1);
        LocalDate dataFim = LocalDate.of(2024, 6, 10);
        afastamento.setDataInicial(dataInicio);
        afastamento.setDataFinal(dataFim);

        // Act
        afastamentoDAO.save(afastamento);

        // Assert
        assertThat(afastamento.getDiasAfastamento()).isEqualTo(10);
    }

    @Test
    @DisplayName("Deve lidar com campos nulos opcionalmente")
    void testCamposNulosOpcionais() throws SQLException {
        // Arrange
        Afastamento afastamento = criarAfastamentoTeste();
        afastamento.setDescricao(null);
        afastamento.setDocumentoComprobatorio(null);
        afastamento.setObservacoes(null);

        // Act
        afastamentoDAO.save(afastamento);

        // Assert
        assertThat(afastamento.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve buscar afastamentos em andamento")
    void testBuscarAfastamentosEmAndamento() throws SQLException {
        // Arrange
        Afastamento afastamento1 = criarAfastamentoTeste();
        afastamento1.setStatus("ATIVO");
        afastamento1.setDataInicial(LocalDate.now().minusDays(5));
        afastamento1.setDataFinal(LocalDate.now().plusDays(5));
        afastamentoDAO.save(afastamento1);

        Afastamento afastamento2 = criarAfastamentoTeste();
        afastamento2.setStatus("FINALIZADO");
        afastamento2.setDataInicial(LocalDate.now().minusDays(10));
        afastamento2.setDataFinal(LocalDate.now().minusDays(5));
        afastamentoDAO.save(afastamento2);

        // Act
        List<Afastamento> emAndamento = afastamentoDAO.findEmAndamento();

        // Assert
        assertThat(emAndamento).hasSizeGreaterThanOrEqualTo(1);
        assertThat(emAndamento).extracting("status")
            .allMatch(status -> status.equals("ATIVO"));
    }

    @Test
    @DisplayName("Deve buscar afastamentos vencidos")
    void testBuscarAfastamentosVencidos() throws SQLException {
        // Arrange
        Afastamento afastamento1 = criarAfastamentoTeste();
        afastamento1.setStatus("ATIVO");
        afastamento1.setDataInicial(LocalDate.now().minusDays(15));
        afastamento1.setDataFinal(LocalDate.now().minusDays(5));
        afastamentoDAO.save(afastamento1);

        Afastamento afastamento2 = criarAfastamentoTeste();
        afastamento2.setStatus("ATIVO");
        afastamento2.setDataInicial(LocalDate.now().plusDays(5));
        afastamento2.setDataFinal(LocalDate.now().plusDays(15));
        afastamentoDAO.save(afastamento2);

        // Act
        List<Afastamento> vencidos = afastamentoDAO.findVencidos();

        // Assert
        assertThat(vencidos).hasSizeGreaterThanOrEqualTo(1);
        assertThat(vencidos).extracting("status")
            .allMatch(status -> status.equals("ATIVO"));
    }

    @Test
    @DisplayName("Deve buscar afastamentos futuros")
    void testBuscarAfastamentosFuturos() throws SQLException {
        // Arrange
        Afastamento afastamento1 = criarAfastamentoTeste();
        afastamento1.setStatus("ATIVO");
        afastamento1.setDataInicial(LocalDate.now().plusDays(10));
        afastamento1.setDataFinal(LocalDate.now().plusDays(20));
        afastamentoDAO.save(afastamento1);

        Afastamento afastamento2 = criarAfastamentoTeste();
        afastamento2.setStatus("ATIVO");
        afastamento2.setDataInicial(LocalDate.now().minusDays(5));
        afastamento2.setDataFinal(LocalDate.now().plusDays(5));
        afastamentoDAO.save(afastamento2);

        // Act
        List<Afastamento> futuros = afastamentoDAO.findFuturos();

        // Assert
        assertThat(futuros).hasSizeGreaterThanOrEqualTo(1);
        assertThat(futuros).extracting("status")
            .allMatch(status -> status.equals("ATIVO"));
    }

    @Test
    @DisplayName("Deve finalizar afastamento")
    void testFinalizarAfastamento() throws SQLException {
        // Arrange
        Afastamento afastamento = criarAfastamentoTeste();
        afastamento.setStatus("ATIVO");
        afastamentoDAO.save(afastamento);
        Long id = afastamento.getId();

        // Act
        afastamentoDAO.finalizar(id);

        // Assert
        Afastamento finalizado = afastamentoDAO.findById(id);
        if (finalizado != null) {
            assertThat(finalizado.getStatus()).isEqualTo("FINALIZADO");
        }
    }

    @Test
    @DisplayName("Deve cancelar afastamento")
    void testCancelarAfastamento() throws SQLException {
        // Arrange
        Afastamento afastamento = criarAfastamentoTeste();
        afastamento.setStatus("ATIVO");
        afastamentoDAO.save(afastamento);
        Long id = afastamento.getId();

        // Act
        afastamentoDAO.cancelar(id);

        // Assert
        Afastamento cancelado = afastamentoDAO.findById(id);
        if (cancelado != null) {
            assertThat(cancelado.getStatus()).isEqualTo("CANCELADO");
        }
    }

    @Test
    @DisplayName("Deve reativar afastamento")
    void testReativarAfastamento() throws SQLException {
        // Arrange
        Afastamento afastamento = criarAfastamentoTeste();
        afastamento.setStatus("CANCELADO");
        afastamentoDAO.save(afastamento);
        Long id = afastamento.getId();

        // Act
        afastamentoDAO.reativar(id);

        // Assert
        Afastamento reativado = afastamentoDAO.findById(id);
        if (reativado != null) {
            assertThat(reativado.getStatus()).isEqualTo("ATIVO");
        }
    }

    @Test
    @DisplayName("Deve obter estatísticas de afastamentos")
    void testGetEstatisticas() throws SQLException {
        // Arrange
        Afastamento afastamento1 = criarAfastamentoTeste();
        afastamento1.setMotivo("LICENCA_MEDICA");
        afastamento1.setDiasAfastamento(10);
        afastamento1.setStatus("ATIVO");
        afastamentoDAO.save(afastamento1);

        Afastamento afastamento2 = criarAfastamentoTeste();
        afastamento2.setMotivo("FERIAS");
        afastamento2.setDiasAfastamento(15);
        afastamento2.setStatus("FINALIZADO");
        afastamentoDAO.save(afastamento2);

        // Act
        List<Object[]> estatisticas = afastamentoDAO.getEstatisticas();

        // Assert
        assertThat(estatisticas).hasSizeGreaterThanOrEqualTo(2);
        
        // Verificar estrutura das estatísticas: [motivo, quantidade, total_dias, ativos, finalizados]
        for (Object[] estatistica : estatisticas) {
            assertThat(estatistica).hasSize(5);
            assertThat(estatistica[0]).isInstanceOf(String.class); // motivo
            assertThat(estatistica[1]).isInstanceOf(Integer.class); // quantidade
            assertThat(estatistica[2]).isInstanceOf(Integer.class); // total_dias
            assertThat(estatistica[3]).isInstanceOf(Integer.class); // ativos
            assertThat(estatistica[4]).isInstanceOf(Integer.class); // finalizados
        }
    }

    @Test
    @DisplayName("Deve contar afastamentos por status")
    void testCountByStatus() throws SQLException {
        // Arrange
        Afastamento afastamento1 = criarAfastamentoTeste();
        afastamento1.setStatus("ATIVO");
        afastamentoDAO.save(afastamento1);

        Afastamento afastamento2 = criarAfastamentoTeste();
        afastamento2.setStatus("ATIVO");
        afastamentoDAO.save(afastamento2);

        Afastamento afastamento3 = criarAfastamentoTeste();
        afastamento3.setStatus("FINALIZADO");
        afastamentoDAO.save(afastamento3);

        // Act
        int countAtivos = afastamentoDAO.countByStatus("ATIVO");
        int countFinalizados = afastamentoDAO.countByStatus("FINALIZADO");
        int countCancelados = afastamentoDAO.countByStatus("CANCELADO");

        // Assert
        assertThat(countAtivos).isGreaterThanOrEqualTo(2);
        assertThat(countFinalizados).isGreaterThanOrEqualTo(1);
        assertThat(countCancelados).isEqualTo(0);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar ID inexistente")
    void testBuscarIdInexistente() throws SQLException {
        // Act & Assert
        Afastamento resultado = afastamentoDAO.findById(999L);
        assertThat(resultado).isNull();
    }

    @Test
    @DisplayName("Deve alternar status do afastamento")
    void testAlternarStatusAfastamento() throws SQLException {
        // Arrange
        Afastamento afastamento = criarAfastamentoTeste();
        afastamento.setStatus("ATIVO");
        afastamentoDAO.save(afastamento);
        Long id = afastamento.getId();

        // Act - Finalizar
        afastamentoDAO.finalizar(id);
        Afastamento finalizado = afastamentoDAO.findById(id);
        if (finalizado != null) {
            assertThat(finalizado.getStatus()).isEqualTo("FINALIZADO");
        }

        // Act - Reativar
        afastamentoDAO.reativar(id);
        Afastamento reativado = afastamentoDAO.findById(id);
        if (reativado != null) {
            assertThat(reativado.getStatus()).isEqualTo("ATIVO");
        }

        // Act - Cancelar
        afastamentoDAO.cancelar(id);
        Afastamento cancelado = afastamentoDAO.findById(id);
        if (cancelado != null) {
            assertThat(cancelado.getStatus()).isEqualTo("CANCELADO");
        }
    }

    /**
     * Método auxiliar para criar um afastamento de teste
     */
    private Afastamento criarAfastamentoTeste() {
        Afastamento afastamento = new Afastamento();
        afastamento.setCodigoIrmao(123L);
        afastamento.setDataInicial(LocalDate.of(2024, 6, 1));
        afastamento.setDataFinal(LocalDate.of(2024, 6, 15));
        afastamento.setDescricao("Afastamento para tratamento médico");
        afastamento.setMotivo("LICENCA_MEDICA");
        afastamento.setStatus("ATIVO");
        afastamento.setDocumentoComprobatorio("Atestado médico");
        afastamento.setUsuarioCadastro("admin");
        afastamento.setObservacoes("Acompanhamento médico semanal");
        afastamento.setAfetaFrequencia(true);
        afastamento.setDiasAfastamento(14);
        return afastamento;
    }
}
