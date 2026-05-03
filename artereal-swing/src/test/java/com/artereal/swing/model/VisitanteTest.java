package com.artereal.swing.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes unitários para a classe Visitante
 */
@DisplayName("Testes Unitários - Visitante")
class VisitanteTest {

    private Visitante visitante;

    @BeforeEach
    void setUp() {
        visitante = new Visitante();
    }

    @Test
    @DisplayName("Deve criar visitante com construtor padrão")
    void testConstrutorPadrao() {
        // Act
        Visitante novoVisitante = new Visitante();

        // Assert
        assertThat(novoVisitante).isNotNull();
        assertThat(novoVisitante.getId()).isNull();
        assertThat(novoVisitante.getNome()).isNull();
        assertThat(novoVisitante.getDataVisita()).isNull();
        assertThat(novoVisitante.getGrauSecreto()).isNull();
        assertThat(novoVisitante.getHistorico()).isNull();
        assertThat(novoVisitante.getTipo()).isNull();
        assertThat(novoVisitante.getLojaOrigem()).isNull();
        assertThat(novoVisitante.getTelefone()).isNull();
        assertThat(novoVisitante.getEmail()).isNull();
        assertThat(novoVisitante.getAutorizadoPor()).isNull();
        assertThat(novoVisitante.isAutorizado()).isFalse(); // boolean default is false
        assertThat(novoVisitante.getObservacoes()).isNull();
        assertThat(novoVisitante.getNumeroCracha()).isNull();
        assertThat(novoVisitante.getDataCadastro()).isNull();
        assertThat(novoVisitante.isAtivo()).isFalse(); // boolean default is false
        assertThat(novoVisitante.getCreatedAt()).isNull();
        assertThat(novoVisitante.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("Deve criar visitante com construtor parametrizado")
    void testConstrutorParametrizado() {
        // Arrange
        String nome = "João da Silva";
        String tipo = "VISITANTE";
        LocalDate dataVisita = LocalDate.of(2024, 1, 15);

        // Act
        Visitante novoVisitante = new Visitante(nome, tipo, dataVisita);

        // Assert
        assertThat(novoVisitante).isNotNull();
        assertThat(novoVisitante.getNome()).isEqualTo(nome);
        assertThat(novoVisitante.getTipo()).isEqualTo(tipo);
        assertThat(novoVisitante.getDataVisita()).isEqualTo(dataVisita);
        assertThat(novoVisitante.getDataCadastro()).isEqualTo(LocalDate.now());
        assertThat(novoVisitante.isAutorizado()).isFalse();
        assertThat(novoVisitante.isAtivo()).isTrue();
    }

    @Test
    @DisplayName("Deve definir e obter ID")
    void testSetGetId() {
        // Arrange
        Long id = 123L;

        // Act
        visitante.setId(id);

        // Assert
        assertThat(visitante.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Deve definir e obter nome")
    void testSetGetNome() {
        // Arrange
        String nome = "Pedro Santos";

        // Act
        visitante.setNome(nome);

        // Assert
        assertThat(visitante.getNome()).isEqualTo(nome);
    }

    @Test
    @DisplayName("Deve definir e obter data de visita")
    void testSetGetDataVisita() {
        // Arrange
        LocalDate dataVisita = LocalDate.of(2024, 1, 15);

        // Act
        visitante.setDataVisita(dataVisita);

        // Assert
        assertThat(visitante.getDataVisita()).isEqualTo(dataVisita);
    }

    @Test
    @DisplayName("Deve definir e obter grau secreto")
    void testSetGetGrauSecreto() {
        // Arrange
        String grauSecreto = "MESTRE";

        // Act
        visitante.setGrauSecreto(grauSecreto);

        // Assert
        assertThat(visitante.getGrauSecreto()).isEqualTo(grauSecreto);
    }

    @Test
    @DisplayName("Deve definir e obter histórico")
    void testSetGetHistorico() {
        // Arrange
        String historico = "Visitante regular da loja";

        // Act
        visitante.setHistorico(historico);

        // Assert
        assertThat(visitante.getHistorico()).isEqualTo(historico);
    }

    @Test
    @DisplayName("Deve definir e obter tipo")
    void testSetGetTipo() {
        // Arrange
        String tipo = "CONVIDADO";

        // Act
        visitante.setTipo(tipo);

        // Assert
        assertThat(visitante.getTipo()).isEqualTo(tipo);
    }

    @Test
    @DisplayName("Deve definir e obter loja de origem")
    void testSetGetLojaOrigem() {
        // Arrange
        String lojaOrigem = "Loja Fraternidade 456";

        // Act
        visitante.setLojaOrigem(lojaOrigem);

        // Assert
        assertThat(visitante.getLojaOrigem()).isEqualTo(lojaOrigem);
    }

    @Test
    @DisplayName("Deve definir e obter telefone")
    void testSetGetTelefone() {
        // Arrange
        String telefone = "(11) 1234-5678";

        // Act
        visitante.setTelefone(telefone);

        // Assert
        assertThat(visitante.getTelefone()).isEqualTo(telefone);
    }

    @Test
    @DisplayName("Deve definir e obter email")
    void testSetGetEmail() {
        // Arrange
        String email = "joao.silva@email.com";

        // Act
        visitante.setEmail(email);

        // Assert
        assertThat(visitante.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("Deve definir e obter autorizado por")
    void testSetGetAutorizadoPor() {
        // Arrange
        String autorizadoPor = "Venerável Mestre";

        // Act
        visitante.setAutorizadoPor(autorizadoPor);

        // Assert
        assertThat(visitante.getAutorizadoPor()).isEqualTo(autorizadoPor);
    }

    @Test
    @DisplayName("Deve definir e obter status autorizado")
    void testSetGetAutorizado() {
        // Arrange
        boolean autorizado = true;

        // Act
        visitante.setAutorizado(autorizado);

        // Assert
        assertThat(visitante.isAutorizado()).isEqualTo(autorizado);
    }

    @Test
    @DisplayName("Deve definir e obter observações")
    void testSetGetObservacoes() {
        // Arrange
        String observacoes = "Visitante interessado em ingressar na maçonaria";

        // Act
        visitante.setObservacoes(observacoes);

        // Assert
        assertThat(visitante.getObservacoes()).isEqualTo(observacoes);
    }

    @Test
    @DisplayName("Deve definir e obter número da crachá")
    void testSetGetNumeroCracha() {
        // Arrange
        String numeroCracha = "V-001";

        // Act
        visitante.setNumeroCracha(numeroCracha);

        // Assert
        assertThat(visitante.getNumeroCracha()).isEqualTo(numeroCracha);
    }

    @Test
    @DisplayName("Deve definir e obter data de cadastro")
    void testSetGetDataCadastro() {
        // Arrange
        LocalDate dataCadastro = LocalDate.of(2024, 1, 10);

        // Act
        visitante.setDataCadastro(dataCadastro);

        // Assert
        assertThat(visitante.getDataCadastro()).isEqualTo(dataCadastro);
    }

    @Test
    @DisplayName("Deve definir e obter status ativo")
    void testSetGetAtivo() {
        // Arrange
        boolean ativo = false;

        // Act
        visitante.setAtivo(ativo);

        // Assert
        assertThat(visitante.isAtivo()).isEqualTo(ativo);
    }

    @Test
    @DisplayName("Deve definir e obter created at")
    void testSetGetCreatedAt() {
        // Arrange
        String createdAt = "2024-01-15 10:30:00";

        // Act
        visitante.setCreatedAt(createdAt);

        // Assert
        assertThat(visitante.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Deve definir e obter updated at")
    void testSetGetUpdatedAt() {
        // Arrange
        String updatedAt = "2024-01-16 11:00:00";

        // Act
        visitante.setUpdatedAt(updatedAt);

        // Assert
        assertThat(visitante.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("Deve representar objeto como string")
    void testToString() {
        // Arrange
        visitante.setId(123L);
        visitante.setNome("João da Silva");
        visitante.setTipo("VISITANTE");
        visitante.setDataVisita(LocalDate.of(2024, 1, 15));

        // Act
        String resultado = visitante.toString();

        // Assert - Simplificado para usar SimpleModel
        assertThat(resultado).contains("Visitante");
        assertThat(resultado).contains("id=123");
    }

    @Test
    @DisplayName("Deve comparar objetos iguais")
    void testEquals() {
        // Arrange
        Visitante visitante1 = new Visitante();
        visitante1.setId(123L);

        Visitante visitante2 = new Visitante();
        visitante2.setId(123L);

        Visitante visitante3 = new Visitante();
        visitante3.setId(456L);

        // Act & Assert
        assertThat(visitante1).isEqualTo(visitante2);
        assertThat(visitante1).isNotEqualTo(visitante3);
        assertThat(visitante1).isNotEqualTo(null);
        assertThat(visitante1).isNotEqualTo("string");
    }

    @Test
    @DisplayName("Deve ter mesmo hashCode para IDs iguais")
    void testHashCode() {
        // Arrange
        Visitante visitante1 = new Visitante();
        visitante1.setId(123L);

        Visitante visitante2 = new Visitante();
        visitante2.setId(123L);

        Visitante visitante3 = new Visitante();
        visitante3.setId(456L);

        // Act & Assert
        assertThat(visitante1.hashCode()).isEqualTo(visitante2.hashCode());
        assertThat(visitante1.hashCode()).isNotEqualTo(visitante3.hashCode());
    }

    @Test
    @DisplayName("Deve lidar com diferentes tipos de visitante")
    void testDiferentesTipos() {
        // Arrange & Act
        visitante.setTipo("VISITANTE");
        assertThat(visitante.getTipo()).isEqualTo("VISITANTE");

        visitante.setTipo("CONVIDADO");
        assertThat(visitante.getTipo()).isEqualTo("CONVIDADO");

        visitante.setTipo("IRMAO_VISITANTE");
        assertThat(visitante.getTipo()).isEqualTo("IRMAO_VISITANTE");

        visitante.setTipo("CANDIDATO");
        assertThat(visitante.getTipo()).isEqualTo("CANDIDATO");
    }

    @Test
    @DisplayName("Deve lidar com diferentes graus secretos")
    void testDiferentesGrausSecretos() {
        // Arrange & Act
        visitante.setGrauSecreto("APRENDIZ");
        assertThat(visitante.getGrauSecreto()).isEqualTo("APRENDIZ");

        visitante.setGrauSecreto("COMPANHEIRO");
        assertThat(visitante.getGrauSecreto()).isEqualTo("COMPANHEIRO");

        visitante.setGrauSecreto("MESTRE");
        assertThat(visitante.getGrauSecreto()).isEqualTo("MESTRE");

        visitante.setGrauSecreto("INSTALADO");
        assertThat(visitante.getGrauSecreto()).isEqualTo("INSTALADO");
    }

    @Test
    @DisplayName("Deve lidar com valores nulos")
    void testValoresNulos() {
        // Act
        visitante.setNome(null);
        visitante.setDataVisita(null);
        visitante.setGrauSecreto(null);
        visitante.setHistorico(null);
        visitante.setTipo(null);
        visitante.setLojaOrigem(null);
        visitante.setTelefone(null);
        visitante.setEmail(null);
        visitante.setAutorizadoPor(null);
        visitante.setObservacoes(null);
        visitante.setNumeroCracha(null);
        visitante.setDataCadastro(null);
        visitante.setCreatedAt(null);
        visitante.setUpdatedAt(null);

        // Assert
        assertThat(visitante.getNome()).isNull();
        assertThat(visitante.getDataVisita()).isNull();
        assertThat(visitante.getGrauSecreto()).isNull();
        assertThat(visitante.getHistorico()).isNull();
        assertThat(visitante.getTipo()).isNull();
        assertThat(visitante.getLojaOrigem()).isNull();
        assertThat(visitante.getTelefone()).isNull();
        assertThat(visitante.getEmail()).isNull();
        assertThat(visitante.getAutorizadoPor()).isNull();
        assertThat(visitante.getObservacoes()).isNull();
        assertThat(visitante.getNumeroCracha()).isNull();
        assertThat(visitante.getDataCadastro()).isNull();
        assertThat(visitante.getCreatedAt()).isNull();
        assertThat(visitante.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("Deve lidar com valores vazios")
    void testValoresVazios() {
        // Act
        visitante.setNome("");
        visitante.setGrauSecreto("");
        visitante.setHistorico("");
        visitante.setTipo("");
        visitante.setLojaOrigem("");
        visitante.setTelefone("");
        visitante.setEmail("");
        visitante.setAutorizadoPor("");
        visitante.setObservacoes("");
        visitante.setNumeroCracha("");
        visitante.setCreatedAt("");
        visitante.setUpdatedAt("");

        // Assert
        assertThat(visitante.getNome()).isEmpty();
        assertThat(visitante.getGrauSecreto()).isEmpty();
        assertThat(visitante.getHistorico()).isEmpty();
        assertThat(visitante.getTipo()).isEmpty();
        assertThat(visitante.getLojaOrigem()).isEmpty();
        assertThat(visitante.getTelefone()).isEmpty();
        assertThat(visitante.getEmail()).isEmpty();
        assertThat(visitante.getAutorizadoPor()).isEmpty();
        assertThat(visitante.getObservacoes()).isEmpty();
        assertThat(visitante.getNumeroCracha()).isEmpty();
        assertThat(visitante.getCreatedAt()).isEmpty();
        assertThat(visitante.getUpdatedAt()).isEmpty();
    }

    @Test
    @DisplayName("Deve lidar com datas de visita futuras")
    void testDataVisitaFutura() {
        // Arrange
        LocalDate hoje = LocalDate.now();
        LocalDate dataFutura = hoje.plusDays(30);

        // Act
        visitante.setDataVisita(dataFutura);

        // Assert
        assertThat(visitante.getDataVisita()).isEqualTo(dataFutura);
        assertThat(visitante.getDataVisita()).isAfter(hoje);
    }

    @Test
    @DisplayName("Deve lidar com datas de visita passadas")
    void testDataVisitaPassada() {
        // Arrange
        LocalDate hoje = LocalDate.now();
        LocalDate dataPassada = hoje.minusDays(30);

        // Act
        visitante.setDataVisita(dataPassada);

        // Assert
        assertThat(visitante.getDataVisita()).isEqualTo(dataPassada);
        assertThat(visitante.getDataVisita()).isBefore(hoje);
    }

    @Test
    @DisplayName("Deve lidar com datas de cadastro futuras")
    void testDataCadastroFutura() {
        // Arrange
        LocalDate hoje = LocalDate.now();
        LocalDate dataFutura = hoje.plusDays(7);

        // Act
        visitante.setDataCadastro(dataFutura);

        // Assert
        assertThat(visitante.getDataCadastro()).isEqualTo(dataFutura);
        assertThat(visitante.getDataCadastro()).isAfter(hoje);
    }

    @Test
    @DisplayName("Deve lidar com datas de cadastro passadas")
    void testDataCadastroPassada() {
        // Arrange
        LocalDate hoje = LocalDate.now();
        LocalDate dataPassada = hoje.minusDays(7);

        // Act
        visitante.setDataCadastro(dataPassada);

        // Assert
        assertThat(visitante.getDataCadastro()).isEqualTo(dataPassada);
        assertThat(visitante.getDataCadastro()).isBefore(hoje);
    }

    @Test
    @DisplayName("Deve lidar com diferentes formatos de telefone")
    void testDiferentesFormatosTelefone() {
        // Arrange & Act
        visitante.setTelefone("(11) 1234-5678");
        assertThat(visitante.getTelefone()).isEqualTo("(11) 1234-5678");

        visitante.setTelefone("11912345678");
        assertThat(visitante.getTelefone()).isEqualTo("11912345678");

        visitante.setTelefone("+55 11 91234-5678");
        assertThat(visitante.getTelefone()).isEqualTo("+55 11 91234-5678");

        visitante.setTelefone("0800-123-4567");
        assertThat(visitante.getTelefone()).isEqualTo("0800-123-4567");
    }

    @Test
    @DisplayName("Deve lidar com diferentes formatos de email")
    void testDiferentesFormatosEmail() {
        // Arrange & Act
        visitante.setEmail("joao.silva@email.com");
        assertThat(visitante.getEmail()).isEqualTo("joao.silva@email.com");

        visitante.setEmail("pedro.santos@gmail.com");
        assertThat(visitante.getEmail()).isEqualTo("pedro.santos@gmail.com");

        visitante.setEmail("carlos@outlook.com");
        assertThat(visitante.getEmail()).isEqualTo("carlos@outlook.com");

        visitante.setEmail("antonio@yahoo.com.br");
        assertThat(visitante.getEmail()).isEqualTo("antonio@yahoo.com.br");
    }

    @Test
    @DisplayName("Deve lidar com diferentes números de crachá")
    void testDiferentesNumerosCracha() {
        // Arrange & Act
        visitante.setNumeroCracha("V-001");
        assertThat(visitante.getNumeroCracha()).isEqualTo("V-001");

        visitante.setNumeroCracha("C-045");
        assertThat(visitante.getNumeroCracha()).isEqualTo("C-045");

        visitante.setNumeroCracha("IV-789");
        assertThat(visitante.getNumeroCracha()).isEqualTo("IV-789");

        visitante.setNumeroCracha("CAN-123");
        assertThat(visitante.getNumeroCracha()).isEqualTo("CAN-123");
    }

    @Test
    @DisplayName("Deve lidar com diferentes lojas de origem")
    void testDiferentesLojasOrigem() {
        // Arrange & Act
        visitante.setLojaOrigem("Loja Fraternidade 456");
        assertThat(visitante.getLojaOrigem()).isEqualTo("Loja Fraternidade 456");

        visitante.setLojaOrigem("ArteReal 123");
        assertThat(visitante.getLojaOrigem()).isEqualTo("ArteReal 123");

        visitante.setLojaOrigem("Luz do Oriente 789");
        assertThat(visitante.getLojaOrigem()).isEqualTo("Luz do Oriente 789");

        visitante.setLojaOrigem("União e Fraternidade 321");
        assertThat(visitante.getLojaOrigem()).isEqualTo("União e Fraternidade 321");
    }

    @Test
    @DisplayName("Deve lidar com diferentes autorizados por")
    void testDiferentesAutorizadosPor() {
        // Arrange & Act
        visitante.setAutorizadoPor("Venerável Mestre");
        assertThat(visitante.getAutorizadoPor()).isEqualTo("Venerável Mestre");

        visitante.setAutorizadoPor("Secretário");
        assertThat(visitante.getAutorizadoPor()).isEqualTo("Secretário");

        visitante.setAutorizadoPor("Tesoureiro");
        assertThat(visitante.getAutorizadoPor()).isEqualTo("Tesoureiro");

        visitante.setAutorizadoPor("Mestre de Cerimônias");
        assertThat(visitante.getAutorizadoPor()).isEqualTo("Mestre de Cerimônias");
    }

    @Test
    @DisplayName("Deve lidar com nomes com caracteres especiais")
    void testNomesCaracteresEspeciais() {
        // Arrange
        String nomeComEspeciais = "João Álvares de Sá";

        // Act
        visitante.setNome(nomeComEspeciais);

        // Assert
        assertThat(visitante.getNome()).isEqualTo(nomeComEspeciais);
        assertThat(visitante.getNome()).contains("João");
        assertThat(visitante.getNome()).contains("Álvares");
    }

    @Test
    @DisplayName("Deve lidar com observações longas")
    void testObservacoesLongas() {
        // Arrange
        String observacoesLongas = "Visitante demonstrou grande interesse na maçonaria, fez várias perguntas sobre os rituais e a filosofia maçônica. Expressou desejo de se tornar membro e solicitou informações sobre o processo de admissão.";

        // Act
        visitante.setObservacoes(observacoesLongas);

        // Assert
        assertThat(visitante.getObservacoes()).isEqualTo(observacoesLongas);
        assertThat(visitante.getObservacoes()).hasSizeGreaterThan(50);
    }

    @Test
    @DisplayName("Deve lidar com históricos diferentes")
    void testHistoricosDiferentes() {
        // Arrange & Act
        visitante.setHistorico("Primeira visita à loja");
        assertThat(visitante.getHistorico()).isEqualTo("Primeira visita à loja");

        visitante.setHistorico("Visitante frequente desde 2023");
        assertThat(visitante.getHistorico()).isEqualTo("Visitante frequente desde 2023");

        visitante.setHistorico("Irmão visitante da Loja Fraternidade");
        assertThat(visitante.getHistorico()).isEqualTo("Irmão visitante da Loja Fraternidade");

        visitante.setHistorico("Convidado para sessão magna especial");
        assertThat(visitante.getHistorico()).isEqualTo("Convidado para sessão magna especial");
    }

    @Test
    @DisplayName("Deve lidar com status autorizado verdadeiro")
    void testAutorizadoVerdadeiro() {
        // Act
        visitante.setAutorizado(true);

        // Assert
        assertThat(visitante.isAutorizado()).isTrue();
    }

    @Test
    @DisplayName("Deve lidar com status autorizado falso")
    void testAutorizadoFalso() {
        // Act
        visitante.setAutorizado(false);

        // Assert
        assertThat(visitante.isAutorizado()).isFalse();
    }

    @Test
    @DisplayName("Deve lidar com status ativo verdadeiro")
    void testAtivoVerdadeiro() {
        // Act
        visitante.setAtivo(true);

        // Assert
        assertThat(visitante.isAtivo()).isTrue();
    }

    @Test
    @DisplayName("Deve lidar com status ativo falso")
    void testAtivoFalso() {
        // Act
        visitante.setAtivo(false);

        // Assert
        assertThat(visitante.isAtivo()).isFalse();
    }

    @Test
    @DisplayName("Deve lidar com ID zero")
    void testIdZero() {
        // Act
        visitante.setId(0L);

        // Assert
        assertThat(visitante.getId()).isEqualTo(0L);
    }

    @Test
    @DisplayName("Deve lidar com ID negativo")
    void testIdNegativo() {
        // Act
        visitante.setId(-1L);

        // Assert
        assertThat(visitante.getId()).isEqualTo(-1L);
        assertThat(visitante.getId()).isNegative();
    }

    @Test
    @DisplayName("Deve lidar com data de visita igual à data de cadastro")
    void testDataVisitaIgualDataCadastro() {
        // Arrange
        LocalDate mesmaData = LocalDate.of(2024, 1, 15);

        // Act
        visitante.setDataVisita(mesmaData);
        visitante.setDataCadastro(mesmaData);

        // Assert
        assertThat(visitante.getDataVisita()).isEqualTo(visitante.getDataCadastro());
    }

    @Test
    @DisplayName("Deve lidar com data de visita após data de cadastro")
    void testDataVisitaAposDataCadastro() {
        // Arrange
        LocalDate dataCadastro = LocalDate.of(2024, 1, 10);
        LocalDate dataVisita = LocalDate.of(2024, 1, 15);

        // Act
        visitante.setDataCadastro(dataCadastro);
        visitante.setDataVisita(dataVisita);

        // Assert
        assertThat(visitante.getDataVisita()).isAfter(visitante.getDataCadastro());
    }

    @Test
    @DisplayName("Deve lidar com data de visita antes da data de cadastro")
    void testDataVisitaAntesDataCadastro() {
        // Arrange
        LocalDate dataCadastro = LocalDate.of(2024, 1, 15);
        LocalDate dataVisita = LocalDate.of(2024, 1, 10);

        // Act
        visitante.setDataCadastro(dataCadastro);
        visitante.setDataVisita(dataVisita);

        // Assert
        assertThat(visitante.getDataVisita()).isBefore(visitante.getDataCadastro());
    }

    @Test
    @DisplayName("Deve lidar com status default no construtor parametrizado")
    void testStatusDefaultConstrutorParametrizado() {
        // Act
        Visitante novoVisitante = new Visitante("Teste", "VISITANTE", LocalDate.now());

        // Assert
        assertThat(novoVisitante.isAutorizado()).isFalse();
        assertThat(novoVisitante.isAtivo()).isTrue();
        assertThat(novoVisitante.getDataCadastro()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("Deve lidar com diferentes combinações de status")
    void testDiferentesCombinacoesStatus() {
        // Arrange & Act
        visitante.setAutorizado(true);
        visitante.setAtivo(true);
        assertThat(visitante.isAutorizado()).isTrue();
        assertThat(visitante.isAtivo()).isTrue();

        visitante.setAutorizado(false);
        visitante.setAtivo(true);
        assertThat(visitante.isAutorizado()).isFalse();
        assertThat(visitante.isAtivo()).isTrue();

        visitante.setAutorizado(true);
        visitante.setAtivo(false);
        assertThat(visitante.isAutorizado()).isTrue();
        assertThat(visitante.isAtivo()).isFalse();

        visitante.setAutorizado(false);
        visitante.setAtivo(false);
        assertThat(visitante.isAutorizado()).isFalse();
        assertThat(visitante.isAtivo()).isFalse();
    }
}
