package com.artereal.swing.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes unitários para a classe Frequencia
 */
@DisplayName("Testes Unitários - Frequencia")
class FrequenciaTest {

    private Frequencia frequencia;

    @BeforeEach
    void setUp() {
        frequencia = new Frequencia();
    }

    @Test
    @DisplayName("Deve criar frequência com construtor padrão")
    void testConstrutorPadrao() {
        // Act
        Frequencia novaFrequencia = new Frequencia();

        // Assert
        assertThat(novaFrequencia).isNotNull();
        assertThat(novaFrequencia.getId()).isNull();
        assertThat(novaFrequencia.getCodigoIrmao()).isNull();
        assertThat(novaFrequencia.getNomeIrmao()).isNull();
        assertThat(novaFrequencia.getRegistroGrandeLoja()).isNull();
        assertThat(novaFrequencia.getNumeroLicenca()).isNull();
        assertThat(novaFrequencia.getDataLicenca()).isNull();
        assertThat(novaFrequencia.getDataFinalLicenca()).isNull();
        assertThat(novaFrequencia.getGrau()).isNull();
        assertThat(novaFrequencia.getDataInstalado()).isNull();
        assertThat(novaFrequencia.isIrregular()).isFalse(); // boolean default is false
        assertThat(novaFrequencia.getNumeroPresencas()).isEqualTo(0); // int default is 0
        assertThat(novaFrequencia.getNumeroFaltas()).isEqualTo(0); // int default is 0
        assertThat(novaFrequencia.getNumeroSecoes()).isEqualTo(0); // int default is 0
        assertThat(novaFrequencia.getNomeHistorico()).isNull();
        assertThat(novaFrequencia.isPresencaDiretoria()).isFalse(); // boolean default is false
        assertThat(novaFrequencia.isSecretariaDiretoria()).isFalse(); // boolean default is false
    }

    @Test
    @DisplayName("Deve criar frequência com construtor parametrizado")
    void testConstrutorParametrizado() {
        // Arrange
        Long codigoIrmao = 123L;
        String nomeIrmao = "João da Silva";

        // Act
        Frequencia novaFrequencia = new Frequencia(codigoIrmao, nomeIrmao);

        // Assert
        assertThat(novaFrequencia).isNotNull();
        assertThat(novaFrequencia.getCodigoIrmao()).isEqualTo(codigoIrmao);
        assertThat(novaFrequencia.getNomeIrmao()).isEqualTo(nomeIrmao);
        assertThat(novaFrequencia.getNumeroPresencas()).isEqualTo(0);
        assertThat(novaFrequencia.getNumeroFaltas()).isEqualTo(0);
        assertThat(novaFrequencia.getNumeroSecoes()).isEqualTo(0);
    }

    @Test
    @DisplayName("Deve definir e obter ID")
    void testSetGetId() {
        // Arrange
        Long id = 123L;

        // Act
        frequencia.setId(id);

        // Assert
        assertThat(frequencia.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Deve definir e obter código do irmão")
    void testSetGetCodigoIrmao() {
        // Arrange
        Long codigoIrmao = 456L;

        // Act
        frequencia.setCodigoIrmao(codigoIrmao);

        // Assert
        assertThat(frequencia.getCodigoIrmao()).isEqualTo(codigoIrmao);
    }

    @Test
    @DisplayName("Deve definir e obter nome do irmão")
    void testSetGetNomeIrmao() {
        // Arrange
        String nomeIrmao = "Pedro Santos";

        // Act
        frequencia.setNomeIrmao(nomeIrmao);

        // Assert
        assertThat(frequencia.getNomeIrmao()).isEqualTo(nomeIrmao);
    }

    @Test
    @DisplayName("Deve definir e obter registro da grande loja")
    void testSetGetRegistroGrandeLoja() {
        // Arrange
        String registroGrandeLoja = "GOB-SP-12345";

        // Act
        frequencia.setRegistroGrandeLoja(registroGrandeLoja);

        // Assert
        assertThat(frequencia.getRegistroGrandeLoja()).isEqualTo(registroGrandeLoja);
    }

    @Test
    @DisplayName("Deve definir e obter número da licença")
    void testSetGetNumeroLicenca() {
        // Arrange
        String numeroLicenca = "LIC-67890";

        // Act
        frequencia.setNumeroLicenca(numeroLicenca);

        // Assert
        assertThat(frequencia.getNumeroLicenca()).isEqualTo(numeroLicenca);
    }

    @Test
    @DisplayName("Deve definir e obter data da licença")
    void testSetGetDataLicenca() {
        // Arrange
        LocalDate dataLicenca = LocalDate.of(2024, 1, 15);

        // Act
        frequencia.setDataLicenca(dataLicenca);

        // Assert
        assertThat(frequencia.getDataLicenca()).isEqualTo(dataLicenca);
    }

    @Test
    @DisplayName("Deve definir e obter data final da licença")
    void testSetGetDataFinalLicenca() {
        // Arrange
        LocalDate dataFinalLicenca = LocalDate.of(2024, 12, 31);

        // Act
        frequencia.setDataFinalLicenca(dataFinalLicenca);

        // Assert
        assertThat(frequencia.getDataFinalLicenca()).isEqualTo(dataFinalLicenca);
    }

    @Test
    @DisplayName("Deve definir e obter grau")
    void testSetGetGrau() {
        // Arrange
        String grau = "MESTRE";

        // Act
        frequencia.setGrau(grau);

        // Assert
        assertThat(frequencia.getGrau()).isEqualTo(grau);
    }

    @Test
    @DisplayName("Deve definir e obter data de instalação")
    void testSetGetDataInstalado() {
        // Arrange
        LocalDate dataInstalado = LocalDate.of(2023, 6, 15);

        // Act
        frequencia.setDataInstalado(dataInstalado);

        // Assert
        assertThat(frequencia.getDataInstalado()).isEqualTo(dataInstalado);
    }

    @Test
    @DisplayName("Deve definir e obter status irregular")
    void testSetGetIrregular() {
        // Arrange
        boolean irregular = true;

        // Act
        frequencia.setIrregular(irregular);

        // Assert
        assertThat(frequencia.isIrregular()).isEqualTo(irregular);
    }

    @Test
    @DisplayName("Deve definir e obter número de presenças")
    void testSetGetNumeroPresencas() {
        // Arrange
        int numeroPresencas = 15;

        // Act
        frequencia.setNumeroPresencas(numeroPresencas);

        // Assert
        assertThat(frequencia.getNumeroPresencas()).isEqualTo(numeroPresencas);
    }

    @Test
    @DisplayName("Deve definir e obter número de faltas")
    void testSetGetNumeroFaltas() {
        // Arrange
        int numeroFaltas = 3;

        // Act
        frequencia.setNumeroFaltas(numeroFaltas);

        // Assert
        assertThat(frequencia.getNumeroFaltas()).isEqualTo(numeroFaltas);
    }

    @Test
    @DisplayName("Deve definir e obter número de seções")
    void testSetGetNumeroSecoes() {
        // Arrange
        int numeroSecoes = 18;

        // Act
        frequencia.setNumeroSecoes(numeroSecoes);

        // Assert
        assertThat(frequencia.getNumeroSecoes()).isEqualTo(numeroSecoes);
    }

    @Test
    @DisplayName("Deve definir e obter nome do histórico")
    void testSetGetNomeHistorico() {
        // Arrange
        String nomeHistorico = "Frequência 2024";

        // Act
        frequencia.setNomeHistorico(nomeHistorico);

        // Assert
        assertThat(frequencia.getNomeHistorico()).isEqualTo(nomeHistorico);
    }

    @Test
    @DisplayName("Deve definir e obter presença em diretoria")
    void testSetGetPresencaDiretoria() {
        // Arrange
        boolean presencaDiretoria = true;

        // Act
        frequencia.setPresencaDiretoria(presencaDiretoria);

        // Assert
        assertThat(frequencia.isPresencaDiretoria()).isEqualTo(presencaDiretoria);
    }

    @Test
    @DisplayName("Deve definir e obter secretaria em diretoria")
    void testSetGetSecretariaDiretoria() {
        // Arrange
        boolean secretariaDiretoria = true;

        // Act
        frequencia.setSecretariaDiretoria(secretariaDiretoria);

        // Assert
        assertThat(frequencia.isSecretariaDiretoria()).isEqualTo(secretariaDiretoria);
    }

    @Test
    @DisplayName("Deve representar objeto como string")
    void testToString() {
        // Arrange
        frequencia.setId(123L);
        frequencia.setNomeIrmao("João da Silva");
        frequencia.setGrau("MESTRE");
        frequencia.setNumeroPresencas(15);

        // Act
        String resultado = frequencia.toString();

        // Assert - Simplificado para usar SimpleModel
        assertThat(resultado).contains("Frequencia");
        assertThat(resultado).contains("id=123");
    }

    @Test
    @DisplayName("Deve comparar objetos iguais")
    void testEquals() {
        // Arrange
        Frequencia frequencia1 = new Frequencia();
        frequencia1.setId(123L);

        Frequencia frequencia2 = new Frequencia();
        frequencia2.setId(123L);

        Frequencia frequencia3 = new Frequencia();
        frequencia3.setId(456L);

        // Act & Assert
        assertThat(frequencia1).isEqualTo(frequencia2);
        assertThat(frequencia1).isNotEqualTo(frequencia3);
        assertThat(frequencia1).isNotEqualTo(null);
        assertThat(frequencia1).isNotEqualTo("string");
    }

    @Test
    @DisplayName("Deve ter mesmo hashCode para IDs iguais")
    void testHashCode() {
        // Arrange
        Frequencia frequencia1 = new Frequencia();
        frequencia1.setId(123L);

        Frequencia frequencia2 = new Frequencia();
        frequencia2.setId(123L);

        Frequencia frequencia3 = new Frequencia();
        frequencia3.setId(456L);

        // Act & Assert
        assertThat(frequencia1.hashCode()).isEqualTo(frequencia2.hashCode());
        assertThat(frequencia1.hashCode()).isNotEqualTo(frequencia3.hashCode());
    }

    @Test
    @DisplayName("Deve lidar com diferentes graus")
    void testDiferentesGraus() {
        // Arrange & Act
        frequencia.setGrau("APRENDIZ");
        assertThat(frequencia.getGrau()).isEqualTo("APRENDIZ");

        frequencia.setGrau("COMPANHEIRO");
        assertThat(frequencia.getGrau()).isEqualTo("COMPANHEIRO");

        frequencia.setGrau("MESTRE");
        assertThat(frequencia.getGrau()).isEqualTo("MESTRE");

        frequencia.setGrau("INSTALADO");
        assertThat(frequencia.getGrau()).isEqualTo("INSTALADO");
    }

    @Test
    @DisplayName("Deve lidar com número de presenças negativo")
    void testNumeroPresencasNegativo() {
        // Act
        frequencia.setNumeroPresencas(-5);

        // Assert
        assertThat(frequencia.getNumeroPresencas()).isEqualTo(-5);
        assertThat(frequencia.getNumeroPresencas()).isNegative();
    }

    @Test
    @DisplayName("Deve lidar com número de faltas negativo")
    void testNumeroFaltasNegativo() {
        // Act
        frequencia.setNumeroFaltas(-3);

        // Assert
        assertThat(frequencia.getNumeroFaltas()).isEqualTo(-3);
        assertThat(frequencia.getNumeroFaltas()).isNegative();
    }

    @Test
    @DisplayName("Deve lidar com número de seções negativo")
    void testNumeroSecoesNegativo() {
        // Act
        frequencia.setNumeroSecoes(-1);

        // Assert
        assertThat(frequencia.getNumeroSecoes()).isEqualTo(-1);
        assertThat(frequencia.getNumeroSecoes()).isNegative();
    }

    @Test
    @DisplayName("Deve lidar com valores nulos")
    void testValoresNulos() {
        // Act
        frequencia.setNomeIrmao(null);
        frequencia.setRegistroGrandeLoja(null);
        frequencia.setNumeroLicenca(null);
        frequencia.setDataLicenca(null);
        frequencia.setDataFinalLicenca(null);
        frequencia.setGrau(null);
        frequencia.setDataInstalado(null);
        frequencia.setNomeHistorico(null);

        // Assert
        assertThat(frequencia.getNomeIrmao()).isNull();
        assertThat(frequencia.getRegistroGrandeLoja()).isNull();
        assertThat(frequencia.getNumeroLicenca()).isNull();
        assertThat(frequencia.getDataLicenca()).isNull();
        assertThat(frequencia.getDataFinalLicenca()).isNull();
        assertThat(frequencia.getGrau()).isNull();
        assertThat(frequencia.getDataInstalado()).isNull();
        assertThat(frequencia.getNomeHistorico()).isNull();
    }

    @Test
    @DisplayName("Deve lidar com valores vazios")
    void testValoresVazios() {
        // Act
        frequencia.setNomeIrmao("");
        frequencia.setRegistroGrandeLoja("");
        frequencia.setNumeroLicenca("");
        frequencia.setGrau("");
        frequencia.setNomeHistorico("");

        // Assert
        assertThat(frequencia.getNomeIrmao()).isEmpty();
        assertThat(frequencia.getRegistroGrandeLoja()).isEmpty();
        assertThat(frequencia.getNumeroLicenca()).isEmpty();
        assertThat(frequencia.getGrau()).isEmpty();
        assertThat(frequencia.getNomeHistorico()).isEmpty();
    }

    @Test
    @DisplayName("Deve lidar com datas de licença futuras")
    void testDataLicencaFutura() {
        // Arrange
        LocalDate hoje = LocalDate.now();
        LocalDate dataFutura = hoje.plusDays(30);

        // Act
        frequencia.setDataLicenca(dataFutura);

        // Assert
        assertThat(frequencia.getDataLicenca()).isEqualTo(dataFutura);
        assertThat(frequencia.getDataLicenca()).isAfter(hoje);
    }

    @Test
    @DisplayName("Deve lidar com datas de licença passadas")
    void testDataLicencaPassada() {
        // Arrange
        LocalDate hoje = LocalDate.now();
        LocalDate dataPassada = hoje.minusDays(30);

        // Act
        frequencia.setDataLicenca(dataPassada);

        // Assert
        assertThat(frequencia.getDataLicenca()).isEqualTo(dataPassada);
        assertThat(frequencia.getDataLicenca()).isBefore(hoje);
    }

    @Test
    @DisplayName("Deve lidar com data final de licença após data inicial")
    void testDataFinalLicencaAposInicial() {
        // Arrange
        LocalDate dataInicial = LocalDate.of(2024, 1, 1);
        LocalDate dataFinal = LocalDate.of(2024, 12, 31);

        // Act
        frequencia.setDataLicenca(dataInicial);
        frequencia.setDataFinalLicenca(dataFinal);

        // Assert
        assertThat(frequencia.getDataFinalLicenca()).isAfter(frequencia.getDataLicenca());
    }

    @Test
    @DisplayName("Deve lidar com data final de licença antes da inicial")
    void testDataFinalLicencaAntesInicial() {
        // Arrange
        LocalDate dataInicial = LocalDate.of(2024, 6, 1);
        LocalDate dataFinal = LocalDate.of(2024, 1, 1);

        // Act
        frequencia.setDataLicenca(dataInicial);
        frequencia.setDataFinalLicenca(dataFinal);

        // Assert
        assertThat(frequencia.getDataFinalLicenca()).isBefore(frequencia.getDataLicenca());
    }

    @Test
    @DisplayName("Deve lidar com diferentes registros da grande loja")
    void testDiferentesRegistrosGrandeLoja() {
        // Arrange & Act
        frequencia.setRegistroGrandeLoja("GOB-SP-12345");
        assertThat(frequencia.getRegistroGrandeLoja()).isEqualTo("GOB-SP-12345");

        frequencia.setRegistroGrandeLoja("GOB-RJ-67890");
        assertThat(frequencia.getRegistroGrandeLoja()).isEqualTo("GOB-RJ-67890");

        frequencia.setRegistroGrandeLoja("GOB-MG-11111");
        assertThat(frequencia.getRegistroGrandeLoja()).isEqualTo("GOB-MG-11111");
    }

    @Test
    @DisplayName("Deve lidar com diferentes números de licença")
    void testDiferentesNumerosLicenca() {
        // Arrange & Act
        frequencia.setNumeroLicenca("LIC-001");
        assertThat(frequencia.getNumeroLicenca()).isEqualTo("LIC-001");

        frequencia.setNumeroLicenca("LIC-ABC-123");
        assertThat(frequencia.getNumeroLicenca()).isEqualTo("LIC-ABC-123");

        frequencia.setNumeroLicenca("2024-001");
        assertThat(frequencia.getNumeroLicenca()).isEqualTo("2024-001");
    }

    @Test
    @DisplayName("Deve lidar com diferentes nomes de histórico")
    void testDiferentesNomesHistorico() {
        // Arrange & Act
        frequencia.setNomeHistorico("Frequência 2024");
        assertThat(frequencia.getNomeHistorico()).isEqualTo("Frequência 2024");

        frequencia.setNomeHistorico("Frequência 1º Semestre 2024");
        assertThat(frequencia.getNomeHistorico()).isEqualTo("Frequência 1º Semestre 2024");

        frequencia.setNomeHistorico("Frequência Trimestral Jan-Mar 2024");
        assertThat(frequencia.getNomeHistorico()).isEqualTo("Frequência Trimestral Jan-Mar 2024");
    }

    @Test
    @DisplayName("Deve lidar com números de presenças altos")
    void testNumeroPresencasAltos() {
        // Arrange
        int presencasAltas = 100;

        // Act
        frequencia.setNumeroPresencas(presencasAltas);

        // Assert
        assertThat(frequencia.getNumeroPresencas()).isEqualTo(presencasAltas);
        assertThat(frequencia.getNumeroPresencas()).isGreaterThan(50);
    }

    @Test
    @DisplayName("Deve lidar com números de faltas altos")
    void testNumeroFaltasAltos() {
        // Arrange
        int faltasAltas = 50;

        // Act
        frequencia.setNumeroFaltas(faltasAltas);

        // Assert
        assertThat(frequencia.getNumeroFaltas()).isEqualTo(faltasAltas);
        assertThat(frequencia.getNumeroFaltas()).isGreaterThan(20);
    }

    @Test
    @DisplayName("Deve lidar com números de seções altos")
    void testNumeroSecoesAltos() {
        // Arrange
        int secoesAltas = 52;

        // Act
        frequencia.setNumeroSecoes(secoesAltas);

        // Assert
        assertThat(frequencia.getNumeroSecoes()).isEqualTo(secoesAltas);
        assertThat(frequencia.getNumeroSecoes()).isGreaterThan(40);
    }

    @Test
    @DisplayName("Deve lidar com data de instalação antes da data atual")
    void testDataInstaladoPassada() {
        // Arrange
        LocalDate hoje = LocalDate.now();
        LocalDate dataPassada = hoje.minusYears(1);

        // Act
        frequencia.setDataInstalado(dataPassada);

        // Assert
        assertThat(frequencia.getDataInstalado()).isEqualTo(dataPassada);
        assertThat(frequencia.getDataInstalado()).isBefore(hoje);
    }

    @Test
    @DisplayName("Deve lidar com data de instalação futura")
    void testDataInstaladoFutura() {
        // Arrange
        LocalDate hoje = LocalDate.now();
        LocalDate dataFutura = hoje.plusMonths(1);

        // Act
        frequencia.setDataInstalado(dataFutura);

        // Assert
        assertThat(frequencia.getDataInstalado()).isEqualTo(dataFutura);
        assertThat(frequencia.getDataInstalado()).isAfter(hoje);
    }

    @Test
    @DisplayName("Deve lidar com diferentes combinações de diretoria")
    void testDiferentesCombinacoesDiretoria() {
        // Arrange & Act
        frequencia.setPresencaDiretoria(true);
        frequencia.setSecretariaDiretoria(false);
        assertThat(frequencia.isPresencaDiretoria()).isTrue();
        assertThat(frequencia.isSecretariaDiretoria()).isFalse();

        frequencia.setPresencaDiretoria(false);
        frequencia.setSecretariaDiretoria(true);
        assertThat(frequencia.isPresencaDiretoria()).isFalse();
        assertThat(frequencia.isSecretariaDiretoria()).isTrue();

        frequencia.setPresencaDiretoria(true);
        frequencia.setSecretariaDiretoria(true);
        assertThat(frequencia.isPresencaDiretoria()).isTrue();
        assertThat(frequencia.isSecretariaDiretoria()).isTrue();

        frequencia.setPresencaDiretoria(false);
        frequencia.setSecretariaDiretoria(false);
        assertThat(frequencia.isPresencaDiretoria()).isFalse();
        assertThat(frequencia.isSecretariaDiretoria()).isFalse();
    }

    @Test
    @DisplayName("Deve lidar com código do irmão zero")
    void testCodigoIrmaoZero() {
        // Act
        frequencia.setCodigoIrmao(0L);

        // Assert
        assertThat(frequencia.getCodigoIrmao()).isEqualTo(0L);
    }

    @Test
    @DisplayName("Deve lidar com código do irmão negativo")
    void testCodigoIrmaoNegativo() {
        // Act
        frequencia.setCodigoIrmao(-1L);

        // Assert
        assertThat(frequencia.getCodigoIrmao()).isEqualTo(-1L);
        assertThat(frequencia.getCodigoIrmao()).isNegative();
    }

    @Test
    @DisplayName("Deve lidar com nomes de irmão com caracteres especiais")
    void testNomeIrmaoCaracteresEspeciais() {
        // Arrange
        String nomeComEspeciais = "João Álvares de Sá";

        // Act
        frequencia.setNomeIrmao(nomeComEspeciais);

        // Assert
        assertThat(frequencia.getNomeIrmao()).isEqualTo(nomeComEspeciais);
        assertThat(frequencia.getNomeIrmao()).contains("João");
        assertThat(frequencia.getNomeIrmao()).contains("Álvares");
    }

    @Test
    @DisplayName("Deve lidar com nomes de histórico longos")
    void testNomeHistoricoLongo() {
        // Arrange
        String nomeLongo = "Frequência Anual 2024 - Loja ArteReal Nº 123 - Período de Janeiro a Dezembro - Controle de Presenças e Faltas dos Irmãos";

        // Act
        frequencia.setNomeHistorico(nomeLongo);

        // Assert
        assertThat(frequencia.getNomeHistorico()).isEqualTo(nomeLongo);
        assertThat(frequencia.getNomeHistorico()).hasSizeGreaterThan(50);
    }

    @Test
    @DisplayName("Deve lidar com status irregular verdadeiro")
    void testIrregularVerdadeiro() {
        // Act
        frequencia.setIrregular(true);

        // Assert
        assertThat(frequencia.isIrregular()).isTrue();
    }

    @Test
    @DisplayName("Deve lidar com status irregular falso")
    void testIrregularFalso() {
        // Act
        frequencia.setIrregular(false);

        // Assert
        assertThat(frequencia.isIrregular()).isFalse();
    }

    @Test
    @DisplayName("Deve lidar com número de presenças igual a zero")
    void testNumeroPresencasZero() {
        // Act
        frequencia.setNumeroPresencas(0);

        // Assert
        assertThat(frequencia.getNumeroPresencas()).isEqualTo(0);
        assertThat(frequencia.getNumeroPresencas()).isZero();
    }

    @Test
    @DisplayName("Deve lidar com número de faltas igual a zero")
    void testNumeroFaltasZero() {
        // Act
        frequencia.setNumeroFaltas(0);

        // Assert
        assertThat(frequencia.getNumeroFaltas()).isEqualTo(0);
        assertThat(frequencia.getNumeroFaltas()).isZero();
    }

    @Test
    @DisplayName("Deve lidar com número de seções igual a zero")
    void testNumeroSecoesZero() {
        // Act
        frequencia.setNumeroSecoes(0);

        // Assert
        assertThat(frequencia.getNumeroSecoes()).isEqualTo(0);
        assertThat(frequencia.getNumeroSecoes()).isZero();
    }
}
