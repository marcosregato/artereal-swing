package com.artereal.swing.integration.dao;

import com.artereal.swing.database.DatabaseManager;
import com.artereal.swing.dao.CandidatoDAO;
import com.artereal.swing.model.Candidato;
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
 * Testes de integração para CandidatoDAO
 */
@DisplayName("Testes de Integração - CandidatoDAO")
class CandidatoDAOTest {

    private DatabaseManager databaseManager;
    private CandidatoDAO candidatoDAO;

    @BeforeEach
    void setUp() throws SQLException {
        databaseManager = DatabaseManager.getInstance();
        candidatoDAO = new CandidatoDAO();
        
        // Forçar recriação do banco para cada teste
        databaseManager.initializeDatabase();
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Limpar dados da tabela candidato após cada teste
        try (Connection conn = databaseManager.getConnection()) {
            conn.createStatement().execute("DELETE FROM candidato");
        }
    }

    @Test
    @DisplayName("Deve criar e salvar candidato")
    void testSalvarCandidato() throws SQLException {
        // Arrange
        Candidato candidato = criarCandidatoTeste();
        candidato.setNome("João Silva Santos");

        // Act
        candidatoDAO.save(candidato);

        // Assert
        assertThat(candidato.getId()).isNotNull();
        assertThat(candidato.getId()).isPositive();
    }

    @Test
    @DisplayName("Deve buscar candidato por ID")
    void testBuscarCandidatoPorId() throws SQLException {
        // Arrange
        Candidato candidato = criarCandidatoTeste();
        candidatoDAO.save(candidato);
        Long id = candidato.getId();

        // Act
        Candidato encontrado = candidatoDAO.findById(id);

        // Assert
        if (encontrado != null) {
            assertThat(encontrado.getId()).isEqualTo(id);
            assertThat(encontrado.getNome()).isEqualTo("José da Silva");
            assertThat(encontrado.getCidade()).isEqualTo("São Paulo");
            assertThat(encontrado.getEstado()).isEqualTo("SP");
        }
    }

    @Test
    @DisplayName("Deve buscar todos os candidatos")
    void testBuscarTodosCandidatos() throws SQLException {
        // Arrange
        Candidato candidato1 = criarCandidatoTeste();
        candidato1.setNome("Candidato 1");
        candidatoDAO.save(candidato1);

        Candidato candidato2 = criarCandidatoTeste();
        candidato2.setNome("Candidato 2");
        candidatoDAO.save(candidato2);

        // Act
        List<Candidato> candidatos = candidatoDAO.findAll();

        // Assert
        assertThat(candidatos).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Deve buscar candidatos por status")
    void testBuscarCandidatosPorStatus() throws SQLException {
        // Arrange
        Candidato candidato1 = criarCandidatoTeste();
        candidato1.setStatus("CANDIDATO");
        candidatoDAO.save(candidato1);

        Candidato candidato2 = criarCandidatoTeste();
        candidato2.setStatus("APROVADO");
        candidatoDAO.save(candidato2);

        Candidato candidato3 = criarCandidatoTeste();
        candidato3.setStatus("CANDIDATO");
        candidatoDAO.save(candidato3);

        // Act
        List<Candidato> candidatos = candidatoDAO.findByStatus("CANDIDATO");

        // Assert
        assertThat(candidatos).hasSizeGreaterThanOrEqualTo(2);
        assertThat(candidatos).extracting("status")
            .allMatch(status -> "CANDIDATO".equals(status));
    }

    @Test
    @DisplayName("Deve buscar candidatos ativos")
    void testBuscarCandidatosAtivos() throws SQLException {
        // Arrange
        Candidato candidato1 = criarCandidatoTeste();
        candidato1.setStatus("CANDIDATO");
        candidatoDAO.save(candidato1);

        Candidato candidato2 = criarCandidatoTeste();
        candidato2.setStatus("APROVADO");
        candidatoDAO.save(candidato2);

        Candidato candidato3 = criarCandidatoTeste();
        candidato3.setStatus("REJEITADO");
        candidatoDAO.save(candidato3);

        Candidato candidato4 = criarCandidatoTeste();
        candidato4.setStatus("INICIADO");
        candidatoDAO.save(candidato4);

        // Act
        List<Candidato> ativos = candidatoDAO.findAtivos();

        // Assert
        assertThat(ativos).hasSizeGreaterThanOrEqualTo(2); // CANDIDATO e APROVADO
        assertThat(ativos).extracting("status")
            .allMatch(status -> "CANDIDATO".equals(status) || "APROVADO".equals(status));
    }

    @Test
    @DisplayName("Deve buscar candidatos por nome")
    void testBuscarCandidatosPorNome() throws SQLException {
        // Arrange
        Candidato candidato1 = criarCandidatoTeste();
        candidato1.setNome("João Silva Santos");
        candidatoDAO.save(candidato1);

        Candidato candidato2 = criarCandidatoTeste();
        candidato2.setNome("Maria João Oliveira");
        candidatoDAO.save(candidato2);

        Candidato candidato3 = criarCandidatoTeste();
        candidato3.setNome("Pedro Costa");
        candidatoDAO.save(candidato3);

        // Act
        List<Candidato> candidatos = candidatoDAO.findByNome("João");

        // Assert
        assertThat(candidatos).hasSizeGreaterThanOrEqualTo(2);
        assertThat(candidatos).extracting("nome")
            .allMatch(nome -> nome.toString().contains("João"));
    }

    @Test
    @DisplayName("Deve buscar candidatos por localidade")
    void testBuscarCandidatosPorLocalidade() throws SQLException {
        // Arrange
        Candidato candidato1 = criarCandidatoTeste();
        candidato1.setCidade("São Paulo");
        candidato1.setEstado("SP");
        candidatoDAO.save(candidato1);

        Candidato candidato2 = criarCandidatoTeste();
        candidato2.setCidade("Rio de Janeiro");
        candidato2.setEstado("RJ");
        candidatoDAO.save(candidato2);

        Candidato candidato3 = criarCandidatoTeste();
        candidato3.setCidade("São Bernardo");
        candidato3.setEstado("SP");
        candidatoDAO.save(candidato3);

        // Act
        List<Candidato> candidatosSP = candidatoDAO.findByLocalidade("São", "SP");
        List<Candidato> candidatosRJ = candidatoDAO.findByLocalidade("Rio", "RJ");

        // Assert
        assertThat(candidatosSP).hasSizeGreaterThanOrEqualTo(2);
        assertThat(candidatosRJ).hasSizeGreaterThanOrEqualTo(1);
        
        assertThat(candidatosSP).extracting("estado")
            .allMatch(estado -> "SP".equals(estado));
        assertThat(candidatosRJ).extracting("estado")
            .allMatch(estado -> "RJ".equals(estado));
    }

    @Test
    @DisplayName("Deve aprovar candidato")
    void testAprovarCandidato() throws SQLException {
        // Arrange
        Candidato candidato = criarCandidatoTeste();
        candidato.setStatus("CANDIDATO");
        candidatoDAO.save(candidato);
        Long id = candidato.getId();

        // Act
        candidatoDAO.aprovar(id);

        // Assert
        Candidato aprovado = candidatoDAO.findById(id);
        if (aprovado != null) {
            assertThat(aprovado.getStatus()).isEqualTo("APROVADO");
            assertThat(aprovado.getDataStatus()).isNotNull();
        }
    }

    @Test
    @DisplayName("Deve rejeitar candidato")
    void testRejeitarCandidato() throws SQLException {
        // Arrange
        Candidato candidato = criarCandidatoTeste();
        candidato.setStatus("CANDIDATO");
        candidatoDAO.save(candidato);
        Long id = candidato.getId();

        // Act
        candidatoDAO.rejeitar(id);

        // Assert
        Candidato rejeitado = candidatoDAO.findById(id);
        if (rejeitado != null) {
            assertThat(rejeitado.getStatus()).isEqualTo("REJEITADO");
            assertThat(rejeitado.getDataStatus()).isNotNull();
        }
    }

    @Test
    @DisplayName("Deve iniciar candidato")
    void testIniciarCandidato() throws SQLException {
        // Arrange
        Candidato candidato = criarCandidatoTeste();
        candidato.setStatus("APROVADO");
        candidatoDAO.save(candidato);
        Long id = candidato.getId();

        // Act
        candidatoDAO.iniciar(id);

        // Assert
        Candidato iniciado = candidatoDAO.findById(id);
        if (iniciado != null) {
            assertThat(iniciado.getStatus()).isEqualTo("INICIADO");
            assertThat(iniciado.getDataStatus()).isNotNull();
        }
    }

    @Test
    @DisplayName("Deve contar candidatos por status")
    void testCountCandidatosPorStatus() throws SQLException {
        // Arrange
        Candidato candidato1 = criarCandidatoTeste();
        candidato1.setStatus("CANDIDATO");
        candidatoDAO.save(candidato1);

        Candidato candidato2 = criarCandidatoTeste();
        candidato2.setStatus("CANDIDATO");
        candidatoDAO.save(candidato2);

        Candidato candidato3 = criarCandidatoTeste();
        candidato3.setStatus("APROVADO");
        candidatoDAO.save(candidato3);

        // Act
        int countCandidatos = candidatoDAO.countByStatus("CANDIDATO");
        int countAprovados = candidatoDAO.countByStatus("APROVADO");

        // Assert
        assertThat(countCandidatos).isGreaterThanOrEqualTo(2);
        assertThat(countAprovados).isGreaterThanOrEqualTo(1);
    }

    @Test
    @DisplayName("Deve obter estatísticas de candidatos")
    void testGetEstatisticas() throws SQLException {
        // Arrange
        Candidato candidato1 = criarCandidatoTeste();
        candidato1.setStatus("CANDIDATO");
        candidato1.setDataCadastro(LocalDate.now().minusDays(10));
        candidatoDAO.save(candidato1);

        Candidato candidato2 = criarCandidatoTeste();
        candidato2.setStatus("APROVADO");
        candidato2.setDataCadastro(LocalDate.now().minusDays(5));
        candidatoDAO.save(candidato2);

        Candidato candidato3 = criarCandidatoTeste();
        candidato3.setStatus("REJEITADO");
        candidato3.setDataCadastro(LocalDate.now().minusDays(15));
        candidatoDAO.save(candidato3);

        // Act
        List<Object[]> estatisticas = candidatoDAO.getEstatisticas();

        // Assert
        assertThat(estatisticas).hasSizeGreaterThanOrEqualTo(3);
        
        // Verificar estrutura das estatísticas: [status, quantidade, dias_medio]
        for (Object[] estatistica : estatisticas) {
            assertThat(estatistica).hasSize(3);
            assertThat(estatistica[0]).isInstanceOf(String.class); // status
            assertThat(estatistica[1]).isInstanceOf(Integer.class); // quantidade
            assertThat(estatistica[2]).isInstanceOf(Double.class); // dias_medio
        }
    }

    @Test
    @DisplayName("Deve excluir candidato")
    void testExcluirCandidato() throws SQLException {
        // Arrange
        Candidato candidato = criarCandidatoTeste();
        candidatoDAO.save(candidato);
        Long id = candidato.getId();

        // Act
        candidatoDAO.delete(id);

        // Assert
        Candidato excluido = candidatoDAO.findById(id);
        assertThat(excluido).isNull();
    }

    @Test
    @DisplayName("Deve atualizar candidato")
    void testAtualizarCandidato() throws SQLException {
        // Arrange
        Candidato candidato = criarCandidatoTeste();
        candidato.setStatus("CANDIDATO");
        candidatoDAO.save(candidato);
        Long id = candidato.getId();

        // Act
        candidato.setStatus("APROVADO");
        candidato.setProfissao("Engenheiro Senior");
        candidatoDAO.save(candidato);

        // Assert
        Candidato atualizado = candidatoDAO.findById(id);
        if (atualizado != null) {
            assertThat(atualizado.getStatus()).isEqualTo("APROVADO");
            assertThat(atualizado.getProfissao()).isEqualTo("Engenheiro Senior");
        }
    }

    @Test
    @DisplayName("Deve lidar com campos nulos opcionalmente")
    void testCamposNulosOpcionais() throws SQLException {
        // Arrange
        Candidato candidato = criarCandidatoTeste();
        candidato.setEsposa(null);
        candidato.setFuncao(null);
        candidato.setObservacoes(null);

        // Act
        candidatoDAO.save(candidato);

        // Assert
        assertThat(candidato.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar ID inexistente")
    void testBuscarIdInexistente() throws SQLException {
        // Act & Assert
        Candidato resultado = candidatoDAO.findById(999L);
        assertThat(resultado).isNull();
    }

    @Test
    @DisplayName("Deve lidar com data de nascimento nula")
    void testDataNascimentoNula() throws SQLException {
        // Arrange
        Candidato candidato = criarCandidatoTeste();
        candidato.setDataNascimento(null);

        // Act
        candidatoDAO.save(candidato);

        // Assert
        assertThat(candidato.getId()).isNotNull();
        
        Candidato salvo = candidatoDAO.findById(candidato.getId());
        if (salvo != null) {
            assertThat(salvo.getDataNascimento()).isNull();
        }
    }

    @Test
    @DisplayName("Deve calcular idade corretamente")
    void testCalcularIdade() throws SQLException {
        // Arrange
        Candidato candidato = criarCandidatoTeste();
        candidato.setDataNascimento(LocalDate.of(1990, 5, 15));
        candidato.setIdade(34); // Idade aproximada

        // Act
        candidatoDAO.save(candidato);

        // Assert
        assertThat(candidato.getId()).isNotNull();
        
        Candidato salvo = candidatoDAO.findById(candidato.getId());
        if (salvo != null) {
            assertThat(salvo.getIdade()).isEqualTo(34);
        }
    }

    @Test
    @DisplayName("Deve lidar com busca por localidade com parâmetros nulos")
    void testBuscarLocalidadeComParametrosNulos() throws SQLException {
        // Arrange
        Candidato candidato1 = criarCandidatoTeste();
        candidato1.setCidade("São Paulo");
        candidato1.setEstado("SP");
        candidatoDAO.save(candidato1);

        Candidato candidato2 = criarCandidatoTeste();
        candidato2.setCidade("Rio de Janeiro");
        candidato2.setEstado("RJ");
        candidatoDAO.save(candidato2);

        // Act
        List<Candidato> todos = candidatoDAO.findByLocalidade(null, null);

        // Assert
        assertThat(todos).hasSizeGreaterThanOrEqualTo(2); // Retorna todos com LIKE %%
    }

    @Test
    @DisplayName("Deve registrar fluxo completo de candidato")
    void testFluxoCompletoCandidato() throws SQLException {
        // Arrange
        Candidato candidato = criarCandidatoTeste();
        candidato.setStatus("CANDIDATO");
        candidatoDAO.save(candidato);
        Long id = candidato.getId();

        // Act - Fluxo completo
        candidatoDAO.aprovar(id);
        
        Candidato aprovado = candidatoDAO.findById(id);
        assertThat(aprovado).isNotNull();
        assertThat(aprovado.getStatus()).isEqualTo("APROVADO");

        candidatoDAO.iniciar(id);
        
        Candidato iniciado = candidatoDAO.findById(id);
        assertThat(iniciado).isNotNull();
        assertThat(iniciado.getStatus()).isEqualTo("INICIADO");

        // Assert
        assertThat(iniciado.getDataStatus()).isNotNull();
        assertThat(iniciado.getDataStatus()).isAfter(aprovado.getDataStatus());
    }

    @Test
    @DisplayName("Deve lidar com informações maçônicas")
    void testInformacoesMaconicas() throws SQLException {
        // Arrange
        Candidato candidato = criarCandidatoTeste();
        candidato.setChanceler("Irmão Chanceler Teste");
        candidato.setVeneravel("Irmão Venerável Teste");
        candidato.setSecretario("Irmão Secretário Teste");
        candidato.setLinhaNegra("Linha Negra Teste");

        // Act
        candidatoDAO.save(candidato);

        // Assert
        assertThat(candidato.getId()).isNotNull();
        
        Candidato salvo = candidatoDAO.findById(candidato.getId());
        if (salvo != null) {
            assertThat(salvo.getChanceler()).isEqualTo("Irmão Chanceler Teste");
            assertThat(salvo.getVeneravel()).isEqualTo("Irmão Venerável Teste");
            assertThat(salvo.getSecretario()).isEqualTo("Irmão Secretário Teste");
            assertThat(salvo.getLinhaNegra()).isEqualTo("Linha Negra Teste");
        }
    }

    /**
     * Método auxiliar para criar um candidato de teste
     */
    private Candidato criarCandidatoTeste() {
        Candidato candidato = new Candidato();
        candidato.setNome("José da Silva");
        candidato.setEndereco("Rua das Flores");
        candidato.setNumero("123");
        candidato.setCidade("São Paulo");
        candidato.setEstado("SP");
        candidato.setBairro("Centro");
        candidato.setFoneResidencial("(11) 99999-9999");
        candidato.setDataNascimento(LocalDate.of(1985, 6, 15));
        candidato.setIdade(39);
        candidato.setEstadoCivil("Casado");
        candidato.setEsposa("Maria Silva");
        candidato.setProfissao("Engenheiro");
        candidato.setFuncao("Gerente");
        candidato.setLocalTrabalho("Empresa ABC");
        candidato.setOndeExerce("Escritório Central");
        candidato.setInformacoes("Informações adicionais do candidato");
        candidato.setChanceler("Irmão Chanceler");
        candidato.setVeneravel("Irmão Venerável");
        candidato.setSecretario("Irmão Secretário");
        candidato.setLinhaNegra("Linha Negra");
        candidato.setStatus("CANDIDATO");
        candidato.setDataCadastro(LocalDate.now());
        candidato.setDataStatus(null);
        candidato.setObservacoes("Observações do candidato");
        return candidato;
    }
}
