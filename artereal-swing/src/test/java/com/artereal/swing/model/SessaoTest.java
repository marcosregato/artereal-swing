package com.artereal.swing.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes unitários para a classe Sessao
 */
@DisplayName("Testes Unitários - Sessao")
class SessaoTest {

    private Sessao sessao;

    @BeforeEach
    void setUp() {
        sessao = new Sessao();
    }

    @Test
    @DisplayName("Deve criar sessão com construtor padrão")
    void testConstrutorPadrao() {
        // Act
        Sessao novaSessao = new Sessao();

        // Assert
        assertThat(novaSessao).isNotNull();
        assertThat(novaSessao.getId()).isNull();
        assertThat(novaSessao.getTipo()).isNull();
        assertThat(novaSessao.getDataHora()).isNull();
        assertThat(novaSessao.getLocal()).isNull();
        assertThat(novaSessao.getPresidente()).isNull();
        assertThat(novaSessao.getSecretario()).isNull();
        assertThat(novaSessao.getTesoureiro()).isNull();
        assertThat(novaSessao.getOrador()).isNull();
        assertThat(novaSessao.getTema()).isNull();
        assertThat(novaSessao.getPauta()).isNull();
        assertThat(novaSessao.getObservacoes()).isNull();
        assertThat(novaSessao.getStatus()).isNull();
        assertThat(novaSessao.getQuantidadePresentes()).isEqualTo(0); // int default is 0
        assertThat(novaSessao.getQuantidadeVisitantes()).isEqualTo(0); // int default is 0
        assertThat(novaSessao.getCreatedAt()).isNull();
        assertThat(novaSessao.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("Deve criar sessão com construtor parametrizado")
    void testConstrutorParametrizado() {
        // Arrange
        String tipo = "MAGNA";
        LocalDateTime dataHora = LocalDateTime.of(2024, 1, 15, 20, 0);

        // Act
        Sessao novaSessao = new Sessao(tipo, dataHora);

        // Assert
        assertThat(novaSessao).isNotNull();
        assertThat(novaSessao.getTipo()).isEqualTo(tipo);
        assertThat(novaSessao.getDataHora()).isEqualTo(dataHora);
        assertThat(novaSessao.getStatus()).isEqualTo("PROGRAMADA");
    }

    @Test
    @DisplayName("Deve definir e obter ID")
    void testSetGetId() {
        // Arrange
        Long id = 123L;

        // Act
        sessao.setId(id);

        // Assert
        assertThat(sessao.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Deve definir e obter tipo")
    void testSetGetTipo() {
        // Arrange
        String tipo = "BRANCA";

        // Act
        sessao.setTipo(tipo);

        // Assert
        assertThat(sessao.getTipo()).isEqualTo(tipo);
    }

    @Test
    @DisplayName("Deve definir e obter data e hora")
    void testSetGetDataHora() {
        // Arrange
        LocalDateTime dataHora = LocalDateTime.of(2024, 1, 15, 20, 30);

        // Act
        sessao.setDataHora(dataHora);

        // Assert
        assertThat(sessao.getDataHora()).isEqualTo(dataHora);
    }

    @Test
    @DisplayName("Deve definir e obter local")
    void testSetGetLocal() {
        // Arrange
        String local = "Templo da Loja ArteReal";

        // Act
        sessao.setLocal(local);

        // Assert
        assertThat(sessao.getLocal()).isEqualTo(local);
    }

    @Test
    @DisplayName("Deve definir e obter presidente")
    void testSetGetPresidente() {
        // Arrange
        String presidente = "João da Silva";

        // Act
        sessao.setPresidente(presidente);

        // Assert
        assertThat(sessao.getPresidente()).isEqualTo(presidente);
    }

    @Test
    @DisplayName("Deve definir e obter secretário")
    void testSetGetSecretario() {
        // Arrange
        String secretario = "Pedro Santos";

        // Act
        sessao.setSecretario(secretario);

        // Assert
        assertThat(sessao.getSecretario()).isEqualTo(secretario);
    }

    @Test
    @DisplayName("Deve definir e obter tesoureiro")
    void testSetGetTesoureiro() {
        // Arrange
        String tesoureiro = "Carlos Oliveira";

        // Act
        sessao.setTesoureiro(tesoureiro);

        // Assert
        assertThat(sessao.getTesoureiro()).isEqualTo(tesoureiro);
    }

    @Test
    @DisplayName("Deve definir e obter orador")
    void testSetGetOrador() {
        // Arrange
        String orador = "Antonio Costa";

        // Act
        sessao.setOrador(orador);

        // Assert
        assertThat(sessao.getOrador()).isEqualTo(orador);
    }

    @Test
    @DisplayName("Deve definir e obter tema")
    void testSetGetTema() {
        // Arrange
        String tema = "A Importância da Fraternidade";

        // Act
        sessao.setTema(tema);

        // Assert
        assertThat(sessao.getTema()).isEqualTo(tema);
    }

    @Test
    @DisplayName("Deve definir e obter pauta")
    void testSetGetPauta() {
        // Arrange
        String pauta = "Iniciação de novos irmãos";

        // Act
        sessao.setPauta(pauta);

        // Assert
        assertThat(sessao.getPauta()).isEqualTo(pauta);
    }

    @Test
    @DisplayName("Deve definir e obter observações")
    void testSetGetObservacoes() {
        // Arrange
        String observacoes = "Sessão solene com presença de autoridades";

        // Act
        sessao.setObservacoes(observacoes);

        // Assert
        assertThat(sessao.getObservacoes()).isEqualTo(observacoes);
    }

    @Test
    @DisplayName("Deve definir e obter status")
    void testSetGetStatus() {
        // Arrange
        String status = "REALIZADA";

        // Act
        sessao.setStatus(status);

        // Assert
        assertThat(sessao.getStatus()).isEqualTo(status);
    }

    @Test
    @DisplayName("Deve definir e obter quantidade de presentes")
    void testSetGetQuantidadePresentes() {
        // Arrange
        int quantidadePresentes = 25;

        // Act
        sessao.setQuantidadePresentes(quantidadePresentes);

        // Assert
        assertThat(sessao.getQuantidadePresentes()).isEqualTo(quantidadePresentes);
    }

    @Test
    @DisplayName("Deve definir e obter quantidade de visitantes")
    void testSetGetQuantidadeVisitantes() {
        // Arrange
        int quantidadeVisitantes = 5;

        // Act
        sessao.setQuantidadeVisitantes(quantidadeVisitantes);

        // Assert
        assertThat(sessao.getQuantidadeVisitantes()).isEqualTo(quantidadeVisitantes);
    }

    @Test
    @DisplayName("Deve definir e obter created at")
    void testSetGetCreatedAt() {
        // Arrange
        String createdAt = "2024-01-15 20:30:00";

        // Act
        sessao.setCreatedAt(createdAt);

        // Assert
        assertThat(sessao.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Deve definir e obter updated at")
    void testSetGetUpdatedAt() {
        // Arrange
        String updatedAt = "2024-01-16 10:00:00";

        // Act
        sessao.setUpdatedAt(updatedAt);

        // Assert
        assertThat(sessao.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("Deve representar objeto como string")
    void testToString() {
        // Arrange
        sessao.setId(123L);
        sessao.setTipo("MAGNA");
        sessao.setDataHora(LocalDateTime.of(2024, 1, 15, 20, 0));
        sessao.setStatus("PROGRAMADA");

        // Act
        String resultado = sessao.toString();

        // Assert - Simplificado para usar SimpleModel
        assertThat(resultado).contains("Sessao");
        assertThat(resultado).contains("id=123");
    }

    @Test
    @DisplayName("Deve comparar objetos iguais")
    void testEquals() {
        // Arrange
        Sessao sessao1 = new Sessao();
        sessao1.setId(123L);

        Sessao sessao2 = new Sessao();
        sessao2.setId(123L);

        Sessao sessao3 = new Sessao();
        sessao3.setId(456L);

        // Act & Assert
        assertThat(sessao1).isEqualTo(sessao2);
        assertThat(sessao1).isNotEqualTo(sessao3);
        assertThat(sessao1).isNotEqualTo(null);
        assertThat(sessao1).isNotEqualTo("string");
    }

    @Test
    @DisplayName("Deve ter mesmo hashCode para IDs iguais")
    void testHashCode() {
        // Arrange
        Sessao sessao1 = new Sessao();
        sessao1.setId(123L);

        Sessao sessao2 = new Sessao();
        sessao2.setId(123L);

        Sessao sessao3 = new Sessao();
        sessao3.setId(456L);

        // Act & Assert
        assertThat(sessao1.hashCode()).isEqualTo(sessao2.hashCode());
        assertThat(sessao1.hashCode()).isNotEqualTo(sessao3.hashCode());
    }

    @Test
    @DisplayName("Deve lidar com diferentes tipos de sessão")
    void testDiferentesTipos() {
        // Arrange & Act
        sessao.setTipo("MAGNA");
        assertThat(sessao.getTipo()).isEqualTo("MAGNA");

        sessao.setTipo("BRANCA");
        assertThat(sessao.getTipo()).isEqualTo("BRANCA");

        sessao.setTipo("ELEICAO");
        assertThat(sessao.getTipo()).isEqualTo("ELEICAO");

        sessao.setTipo("INSTRUCAO");
        assertThat(sessao.getTipo()).isEqualTo("INSTRUCAO");

        sessao.setTipo("ADMINISTRATIVA");
        assertThat(sessao.getTipo()).isEqualTo("ADMINISTRATIVA");
    }

    @Test
    @DisplayName("Deve lidar com diferentes status")
    void testDiferentesStatus() {
        // Arrange & Act
        sessao.setStatus("PROGRAMADA");
        assertThat(sessao.getStatus()).isEqualTo("PROGRAMADA");

        sessao.setStatus("REALIZADA");
        assertThat(sessao.getStatus()).isEqualTo("REALIZADA");

        sessao.setStatus("CANCELADA");
        assertThat(sessao.getStatus()).isEqualTo("CANCELADA");

        sessao.setStatus("ADIADA");
        assertThat(sessao.getStatus()).isEqualTo("ADIADA");
    }

    @Test
    @DisplayName("Deve lidar com quantidade de presentes negativa")
    void testQuantidadePresentesNegativa() {
        // Act
        sessao.setQuantidadePresentes(-5);

        // Assert
        assertThat(sessao.getQuantidadePresentes()).isEqualTo(-5);
        assertThat(sessao.getQuantidadePresentes()).isNegative();
    }

    @Test
    @DisplayName("Deve lidar com quantidade de visitantes negativa")
    void testQuantidadeVisitantesNegativa() {
        // Act
        sessao.setQuantidadeVisitantes(-3);

        // Assert
        assertThat(sessao.getQuantidadeVisitantes()).isEqualTo(-3);
        assertThat(sessao.getQuantidadeVisitantes()).isNegative();
    }

    @Test
    @DisplayName("Deve lidar com valores nulos")
    void testValoresNulos() {
        // Act
        sessao.setTipo(null);
        sessao.setDataHora(null);
        sessao.setLocal(null);
        sessao.setPresidente(null);
        sessao.setSecretario(null);
        sessao.setTesoureiro(null);
        sessao.setOrador(null);
        sessao.setTema(null);
        sessao.setPauta(null);
        sessao.setObservacoes(null);
        sessao.setStatus(null);
        sessao.setCreatedAt(null);
        sessao.setUpdatedAt(null);

        // Assert
        assertThat(sessao.getTipo()).isNull();
        assertThat(sessao.getDataHora()).isNull();
        assertThat(sessao.getLocal()).isNull();
        assertThat(sessao.getPresidente()).isNull();
        assertThat(sessao.getSecretario()).isNull();
        assertThat(sessao.getTesoureiro()).isNull();
        assertThat(sessao.getOrador()).isNull();
        assertThat(sessao.getTema()).isNull();
        assertThat(sessao.getPauta()).isNull();
        assertThat(sessao.getObservacoes()).isNull();
        assertThat(sessao.getStatus()).isNull();
        assertThat(sessao.getCreatedAt()).isNull();
        assertThat(sessao.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("Deve lidar com valores vazios")
    void testValoresVazios() {
        // Act
        sessao.setTipo("");
        sessao.setLocal("");
        sessao.setPresidente("");
        sessao.setSecretario("");
        sessao.setTesoureiro("");
        sessao.setOrador("");
        sessao.setTema("");
        sessao.setPauta("");
        sessao.setObservacoes("");
        sessao.setStatus("");
        sessao.setCreatedAt("");
        sessao.setUpdatedAt("");

        // Assert
        assertThat(sessao.getTipo()).isEmpty();
        assertThat(sessao.getLocal()).isEmpty();
        assertThat(sessao.getPresidente()).isEmpty();
        assertThat(sessao.getSecretario()).isEmpty();
        assertThat(sessao.getTesoureiro()).isEmpty();
        assertThat(sessao.getOrador()).isEmpty();
        assertThat(sessao.getTema()).isEmpty();
        assertThat(sessao.getPauta()).isEmpty();
        assertThat(sessao.getObservacoes()).isEmpty();
        assertThat(sessao.getStatus()).isEmpty();
        assertThat(sessao.getCreatedAt()).isEmpty();
        assertThat(sessao.getUpdatedAt()).isEmpty();
    }

    @Test
    @DisplayName("Deve lidar com datas futuras")
    void testDataHoraFutura() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime dataFutura = agora.plusDays(30);

        // Act
        sessao.setDataHora(dataFutura);

        // Assert
        assertThat(sessao.getDataHora()).isEqualTo(dataFutura);
        assertThat(sessao.getDataHora()).isAfter(agora);
    }

    @Test
    @DisplayName("Deve lidar com datas passadas")
    void testDataHoraPassada() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime dataPassada = agora.minusDays(30);

        // Act
        sessao.setDataHora(dataPassada);

        // Assert
        assertThat(sessao.getDataHora()).isEqualTo(dataPassada);
        assertThat(sessao.getDataHora()).isBefore(agora);
    }

    @Test
    @DisplayName("Deve lidar com diferentes locais")
    void testDiferentesLocais() {
        // Arrange & Act
        sessao.setLocal("Templo da Loja ArteReal");
        assertThat(sessao.getLocal()).isEqualTo("Templo da Loja ArteReal");

        sessao.setLocal("Salão de Eventos Maçônicos");
        assertThat(sessao.getLocal()).isEqualTo("Salão de Eventos Maçônicos");

        sessao.setLocal("Sede da Grande Loja");
        assertThat(sessao.getLocal()).isEqualTo("Sede da Grande Loja");

        sessao.setLocal("Templo Loja Fraternidade");
        assertThat(sessao.getLocal()).isEqualTo("Templo Loja Fraternidade");
    }

    @Test
    @DisplayName("Deve lidar com diferentes temas")
    void testDiferentesTemas() {
        // Arrange & Act
        sessao.setTema("A Importância da Fraternidade");
        assertThat(sessao.getTema()).isEqualTo("A Importância da Fraternidade");

        sessao.setTema("Os Símbolos Maçônicos");
        assertThat(sessao.getTema()).isEqualTo("Os Símbolos Maçônicos");

        sessao.setTema("História da Maçonaria");
        assertThat(sessao.getTema()).isEqualTo("História da Maçonaria");

        sessao.setTema("Filosofia Maçônica");
        assertThat(sessao.getTema()).isEqualTo("Filosofia Maçônica");
    }

    @Test
    @DisplayName("Deve lidar com diferentes pautas")
    void testDiferentesPautas() {
        // Arrange & Act
        sessao.setPauta("Iniciação de novos irmãos");
        assertThat(sessao.getPauta()).isEqualTo("Iniciação de novos irmãos");

        sessao.setPauta("Elevação a Companheiro");
        assertThat(sessao.getPauta()).isEqualTo("Elevação a Companheiro");

        sessao.setPauta("Exaltação a Mestre");
        assertThat(sessao.getPauta()).isEqualTo("Exaltação a Mestre");

        sessao.setPauta("Eleição de Oficiais");
        assertThat(sessao.getPauta()).isEqualTo("Eleição de Oficiais");
    }

    @Test
    @DisplayName("Deve lidar com diferentes cargos")
    void testDiferentesCargos() {
        // Arrange & Act
        sessao.setPresidente("João da Silva");
        sessao.setSecretario("Pedro Santos");
        sessao.setTesoureiro("Carlos Oliveira");
        sessao.setOrador("Antonio Costa");

        // Assert
        assertThat(sessao.getPresidente()).isEqualTo("João da Silva");
        assertThat(sessao.getSecretario()).isEqualTo("Pedro Santos");
        assertThat(sessao.getTesoureiro()).isEqualTo("Carlos Oliveira");
        assertThat(sessao.getOrador()).isEqualTo("Antonio Costa");

        // Teste com outros nomes
        sessao.setPresidente("Francisco Lima");
        sessao.setSecretario("José Almeida");
        sessao.setTesoureiro("Manuel Costa");
        sessao.setOrador("Roberto Silva");

        assertThat(sessao.getPresidente()).isEqualTo("Francisco Lima");
        assertThat(sessao.getSecretario()).isEqualTo("José Almeida");
        assertThat(sessao.getTesoureiro()).isEqualTo("Manuel Costa");
        assertThat(sessao.getOrador()).isEqualTo("Roberto Silva");
    }

    @Test
    @DisplayName("Deve lidar com quantidades altas")
    void testQuantidadesAltas() {
        // Arrange & Act
        sessao.setQuantidadePresentes(100);
        sessao.setQuantidadeVisitantes(25);

        // Assert
        assertThat(sessao.getQuantidadePresentes()).isEqualTo(100);
        assertThat(sessao.getQuantidadePresentes()).isGreaterThan(50);
        assertThat(sessao.getQuantidadeVisitantes()).isEqualTo(25);
        assertThat(sessao.getQuantidadeVisitantes()).isGreaterThan(20);
    }

    @Test
    @DisplayName("Deve lidar com quantidades zero")
    void testQuantidadesZero() {
        // Act
        sessao.setQuantidadePresentes(0);
        sessao.setQuantidadeVisitantes(0);

        // Assert
        assertThat(sessao.getQuantidadePresentes()).isEqualTo(0);
        assertThat(sessao.getQuantidadePresentes()).isZero();
        assertThat(sessao.getQuantidadeVisitantes()).isEqualTo(0);
        assertThat(sessao.getQuantidadeVisitantes()).isZero();
    }

    @Test
    @DisplayName("Deve lidar com observações longas")
    void testObservacoesLongas() {
        // Arrange
        String observacoesLongas = "Sessão solene de iniciação com presença de autoridades maçônicas da Grande Loja. Cerimônia realizada com toda a dignidade e ritualística, contando com a participação de todos os irmãos presentes e visitantes convidados.";

        // Act
        sessao.setObservacoes(observacoesLongas);

        // Assert
        assertThat(sessao.getObservacoes()).isEqualTo(observacoesLongas);
        assertThat(sessao.getObservacoes()).hasSizeGreaterThan(50);
    }

    @Test
    @DisplayName("Deve lidar com ID zero")
    void testIdZero() {
        // Act
        sessao.setId(0L);

        // Assert
        assertThat(sessao.getId()).isEqualTo(0L);
    }

    @Test
    @DisplayName("Deve lidar com ID negativo")
    void testIdNegativo() {
        // Act
        sessao.setId(-1L);

        // Assert
        assertThat(sessao.getId()).isEqualTo(-1L);
        assertThat(sessao.getId()).isNegative();
    }

    @Test
    @DisplayName("Deve lidar com status default no construtor parametrizado")
    void testStatusDefaultConstrutorParametrizado() {
        // Act
        Sessao novaSessao = new Sessao("MAGNA", LocalDateTime.now());

        // Assert
        assertThat(novaSessao.getStatus()).isEqualTo("PROGRAMADA");
    }

    @Test
    @DisplayName("Deve lidar com diferentes horários")
    void testDiferentesHorarios() {
        // Arrange & Act
        LocalDateTime manha = LocalDateTime.of(2024, 1, 15, 9, 0);
        LocalDateTime tarde = LocalDateTime.of(2024, 1, 15, 15, 0);
        LocalDateTime noite = LocalDateTime.of(2024, 1, 15, 20, 0);

        sessao.setDataHora(manha);
        assertThat(sessao.getDataHora()).isEqualTo(manha);
        assertThat(sessao.getDataHora().getHour()).isEqualTo(9);

        sessao.setDataHora(tarde);
        assertThat(sessao.getDataHora()).isEqualTo(tarde);
        assertThat(sessao.getDataHora().getHour()).isEqualTo(15);

        sessao.setDataHora(noite);
        assertThat(sessao.getDataHora()).isEqualTo(noite);
        assertThat(sessao.getDataHora().getHour()).isEqualTo(20);
    }

    @Test
    @DisplayName("Deve lidar com caracteres especiais nos campos")
    void testCaracteresEspeciais() {
        // Arrange & Act
        sessao.setLocal("Sala Maçônica \"Luz do Oriente\"");
        sessao.setTema("A Importância da Fraternidade na Sociedade Atual");
        sessao.setObservacoes("Sessão especial com presença de autoridades da GLESP");

        // Assert
        assertThat(sessao.getLocal()).contains("Luz do Oriente");
        assertThat(sessao.getTema()).contains("Fraternidade");
        assertThat(sessao.getObservacoes()).contains("GLESP");
    }

    @Test
    @DisplayName("Deve lidar com total de participantes")
    void testTotalParticipantes() {
        // Arrange
        int presentes = 25;
        int visitantes = 5;

        // Act
        sessao.setQuantidadePresentes(presentes);
        sessao.setQuantidadeVisitantes(visitantes);

        // Assert
        int total = sessao.getQuantidadePresentes() + sessao.getQuantidadeVisitantes();
        assertThat(total).isEqualTo(30);
        assertThat(total).isGreaterThan(presentes);
        assertThat(total).isGreaterThan(visitantes);
    }

    @Test
    @DisplayName("Deve lidar com visitantes maiores que presentes")
    void testVisitantesMaioresQuePresentes() {
        // Arrange
        int presentes = 10;
        int visitantes = 15;

        // Act
        sessao.setQuantidadePresentes(presentes);
        sessao.setQuantidadeVisitantes(visitantes);

        // Assert
        assertThat(sessao.getQuantidadeVisitantes()).isGreaterThan(sessao.getQuantidadePresentes());
    }
}
