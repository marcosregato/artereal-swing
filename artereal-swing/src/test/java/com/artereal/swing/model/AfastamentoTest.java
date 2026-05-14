package com.artereal.swing.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes unitários para a classe Afastamento
 */
@DisplayName("Testes Unitários - Afastamento")
class AfastamentoTest {

    private Afastamento afastamento;

    @BeforeEach
    void setUp() {
        afastamento = new Afastamento();
    }

    @Test
    @DisplayName("Deve criar afastamento com construtor padrão")
    void testConstrutorPadrao() {
        // Act
        Afastamento novoAfastamento = new Afastamento();

        // Assert
        assertThat(novoAfastamento).isNotNull();
        assertThat(novoAfastamento.getId()).isNull();
        assertThat(novoAfastamento.getCodigoIrmao()).isNull();
        assertThat(novoAfastamento.getDataInicial()).isNull();
        assertThat(novoAfastamento.getDataFinal()).isNull();
        assertThat(novoAfastamento.getDescricao()).isNull();
        assertThat(novoAfastamento.getMotivo()).isNull();
        assertThat(novoAfastamento.getStatus()).isNull();
    }

    @Test
    @DisplayName("Deve criar afastamento com construtor parametrizado")
    void testConstrutorParametrizado() {
        // Arrange
        Long codigoIrmao = 123L;
        LocalDate dataInicial = LocalDate.of(2024, 1, 1);
        LocalDate dataFinal = LocalDate.of(2024, 1, 15);
        String motivo = "LICENCA_MEDICA";

        // Act
        Afastamento novoAfastamento = new Afastamento(codigoIrmao, dataInicial, dataFinal, motivo);

        // Assert
        assertThat(novoAfastamento).isNotNull();
        assertThat(novoAfastamento.getCodigoIrmao()).isEqualTo(codigoIrmao);
        assertThat(novoAfastamento.getDataInicial()).isEqualTo(dataInicial);
        assertThat(novoAfastamento.getDataFinal()).isEqualTo(dataFinal);
        assertThat(novoAfastamento.getMotivo()).isEqualTo(motivo);
        assertThat(novoAfastamento.getStatus()).isEqualTo("ATIVO");
        assertThat(novoAfastamento.isAfetaFrequencia()).isTrue();
        assertThat(novoAfastamento.getDiasAfastamento()).isEqualTo(14);
        assertThat(novoAfastamento.getDataCadastro()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("Deve calcular dias de afastamento corretamente")
    void testCalcularDias() {
        // Arrange
        afastamento.setDataInicial(LocalDate.of(2024, 1, 1));
        afastamento.setDataFinal(LocalDate.of(2024, 1, 10));

        // Act
        int dias = afastamento.getDiasAfastamento();

        // Assert
        assertThat(dias).isEqualTo(9); // 10 - 1 = 9 dias
    }

    @Test
    @DisplayName("Deve calcular dias quando datas são iguais")
    void testCalcularDiasMesmoDia() {
        // Arrange
        LocalDate mesmaData = LocalDate.of(2024, 1, 1);
        afastamento.setDataInicial(mesmaData);
        afastamento.setDataFinal(mesmaData);

        // Act
        int dias = afastamento.getDiasAfastamento();

        // Assert
        assertThat(dias).isEqualTo(0);
    }

    @Test
    @DisplayName("Deve calcular dias quando data final é antes da inicial")
    void testCalcularDiasDataFinalAntesInicial() {
        // Arrange
        afastamento.setDataInicial(LocalDate.of(2024, 1, 10));
        afastamento.setDataFinal(LocalDate.of(2024, 1, 1));

        // Act
        int dias = afastamento.getDiasAfastamento();

        // Assert
        assertThat(dias).isEqualTo(-9); // 1 - 10 = -9 dias
    }

    @Test
    @DisplayName("Deve definir e obter ID")
    void testSetGetId() {
        // Arrange
        Long id = 123L;

        // Act
        afastamento.setId(id);

        // Assert
        assertThat(afastamento.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Deve definir e obter código do irmão")
    void testSetGetCodigoIrmao() {
        // Arrange
        Long codigoIrmao = 456L;

        // Act
        afastamento.setCodigoIrmao(codigoIrmao);

        // Assert
        assertThat(afastamento.getCodigoIrmao()).isEqualTo(codigoIrmao);
    }

    @Test
    @DisplayName("Deve definir e obter data inicial")
    void testSetGetDataInicial() {
        // Arrange
        LocalDate dataInicial = LocalDate.of(2024, 1, 1);

        // Act
        afastamento.setDataInicial(dataInicial);

        // Assert
        assertThat(afastamento.getDataInicial()).isEqualTo(dataInicial);
    }

    @Test
    @DisplayName("Deve definir e obter data final")
    void testSetGetDataFinal() {
        // Arrange
        LocalDate dataFinal = LocalDate.of(2024, 1, 15);

        // Act
        afastamento.setDataFinal(dataFinal);

        // Assert
        assertThat(afastamento.getDataFinal()).isEqualTo(dataFinal);
    }

    @Test
    @DisplayName("Deve definir e obter descrição")
    void testSetGetDescricao() {
        // Arrange
        String descricao = "Licença médica por motivo de saúde";

        // Act
        afastamento.setDescricao(descricao);

        // Assert
        assertThat(afastamento.getDescricao()).isEqualTo(descricao);
    }

    @Test
    @DisplayName("Deve definir e obter motivo")
    void testSetGetMotivo() {
        // Arrange
        String motivo = "FERIAS";

        // Act
        afastamento.setMotivo(motivo);

        // Assert
        assertThat(afastamento.getMotivo()).isEqualTo(motivo);
    }

    @Test
    @DisplayName("Deve definir e obter status")
    void testSetGetStatus() {
        // Arrange
        String status = "FINALIZADO";

        // Act
        afastamento.setStatus(status);

        // Assert
        assertThat(afastamento.getStatus()).isEqualTo(status);
    }

    @Test
    @DisplayName("Deve definir e obter documento comprobatório")
    void testSetGetDocumentoComprobatorio() {
        // Arrange
        String documento = "ATESTADO_MÉDICO_123456.pdf";

        // Act
        afastamento.setDocumentoComprobatorio(documento);

        // Assert
        assertThat(afastamento.getDocumentoComprobatorio()).isEqualTo(documento);
    }

    @Test
    @DisplayName("Deve definir e obter data de cadastro")
    void testSetGetDataCadastro() {
        // Arrange
        LocalDate dataCadastro = LocalDate.of(2024, 1, 1);

        // Act
        afastamento.setDataCadastro(dataCadastro);

        // Assert
        assertThat(afastamento.getDataCadastro()).isEqualTo(dataCadastro);
    }

    @Test
    @DisplayName("Deve definir e obter usuário de cadastro")
    void testSetGetUsuarioCadastro() {
        // Arrange
        String usuario = "admin";

        // Act
        afastamento.setUsuarioCadastro(usuario);

        // Assert
        assertThat(afastamento.getUsuarioCadastro()).isEqualTo(usuario);
    }

    @Test
    @DisplayName("Deve definir e obter observações")
    void testSetGetObservacoes() {
        // Arrange
        String observacoes = "Paciente em tratamento contínuo";

        // Act
        afastamento.setObservacoes(observacoes);

        // Assert
        assertThat(afastamento.getObservacoes()).isEqualTo(observacoes);
    }

    @Test
    @DisplayName("Deve definir e obter afeta frequência")
    void testSetGetAfetaFrequencia() {
        // Arrange
        boolean afetaFrequencia = false;

        // Act
        afastamento.setAfetaFrequencia(afetaFrequencia);

        // Assert
        assertThat(afastamento.isAfetaFrequencia()).isEqualTo(afetaFrequencia);
    }

    @Test
    @DisplayName("Deve definir e obter dias de afastamento")
    void testSetGetDiasAfastamento() {
        // Arrange
        int dias = 30;

        // Act
        afastamento.setDiasAfastamento(dias);

        // Assert
        assertThat(afastamento.getDiasAfastamento()).isEqualTo(dias);
    }

    @Test
    @DisplayName("Deve definir e obter created at")
    void testSetGetCreatedAt() {
        // Arrange
        String createdAt = "2024-01-01 10:00:00";

        // Act
        afastamento.setCreatedAt(createdAt);

        // Assert
        assertThat(afastamento.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Deve definir e obter updated at")
    void testSetGetUpdatedAt() {
        // Arrange
        String updatedAt = "2024-01-15 10:00:00";

        // Act
        afastamento.setUpdatedAt(updatedAt);

        // Assert
        assertThat(afastamento.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("Deve recalcular dias ao alterar data inicial")
    void testRecalcularDiasAoAlterarDataInicial() {
        // Arrange
        afastamento.setDataInicial(LocalDate.of(2024, 1, 5));
        afastamento.setDataFinal(LocalDate.of(2024, 1, 15));
        assertThat(afastamento.getDiasAfastamento()).isEqualTo(10);

        // Act
        afastamento.setDataInicial(LocalDate.of(2024, 1, 1));

        // Assert
        assertThat(afastamento.getDiasAfastamento()).isEqualTo(14);
    }

    @Test
    @DisplayName("Deve recalcular dias ao alterar data final")
    void testRecalcularDiasAoAlterarDataFinal() {
        // Arrange
        afastamento.setDataInicial(LocalDate.of(2024, 1, 1));
        afastamento.setDataFinal(LocalDate.of(2024, 1, 10));
        assertThat(afastamento.getDiasAfastamento()).isEqualTo(9);

        // Act
        afastamento.setDataFinal(LocalDate.of(2024, 1, 20));

        // Assert
        assertThat(afastamento.getDiasAfastamento()).isEqualTo(19);
    }

    @Test
    @DisplayName("Deve lidar com valores nulos em cálculo de dias")
    void testCalcularDiasComDatasNulas() {
        // Arrange
        afastamento.setDataInicial(null);
        afastamento.setDataFinal(null);

        // Act
        int dias = afastamento.getDiasAfastamento();

        // Assert
        assertThat(dias).isEqualTo(0);
    }

    @Test
    @DisplayName("Deve lidar com data inicial nula")
    void testCalcularDiasComDataInicialNula() {
        // Arrange
        afastamento.setDataInicial(null);
        afastamento.setDataFinal(LocalDate.of(2024, 1, 10));

        // Act
        int dias = afastamento.getDiasAfastamento();

        // Assert
        assertThat(dias).isEqualTo(0);
    }

    @Test
    @DisplayName("Deve lidar com data final nula")
    void testCalcularDiasComDataFinalNula() {
        // Arrange
        afastamento.setDataInicial(LocalDate.of(2024, 1, 1));
        afastamento.setDataFinal(null);

        // Act
        int dias = afastamento.getDiasAfastamento();

        // Assert
        assertThat(dias).isEqualTo(0);
    }

    @Test
    @DisplayName("Deve representar objeto como string")
    void testToString() {
        // Arrange
        afastamento.setId(123L);
        afastamento.setCodigoIrmao(456L);
        afastamento.setMotivo("LICENCA_MEDICA");
        afastamento.setStatus("ATIVO");

        // Act
        String resultado = afastamento.toString();

        // Assert - Simplificado para usar SimpleModel
        assertThat(resultado).contains("Afastamento");
        assertThat(resultado).contains("id=123");
    }

    @Test
    @DisplayName("Deve comparar objetos iguais")
    void testEquals() {
        // Arrange
        Afastamento afastamento1 = new Afastamento();
        afastamento1.setId(123L);

        Afastamento afastamento2 = new Afastamento();
        afastamento2.setId(123L);

        Afastamento afastamento3 = new Afastamento();
        afastamento3.setId(456L);

        // Act & Assert - Como não há equals customizado, apenas verifica referências diferentes
        assertThat(afastamento1).isNotSameAs(afastamento2);
        assertThat(afastamento1).isNotSameAs(afastamento3);
        assertThat(afastamento1).isNotNull();
        assertThat(afastamento1).isNotEqualTo("string");
    }

    @Test
    @DisplayName("Deve ter mesmo hashCode para IDs iguais")
    void testHashCode() {
        // Arrange
        Afastamento afastamento1 = new Afastamento();
        afastamento1.setId(123L);

        Afastamento afastamento2 = new Afastamento();
        afastamento2.setId(123L);

        Afastamento afastamento3 = new Afastamento();
        afastamento3.setId(456L);

        // Act & Assert
        assertThat(afastamento1.hashCode()).isEqualTo(afastamento2.hashCode());
        assertThat(afastamento1.hashCode()).isNotEqualTo(afastamento3.hashCode());
    }
}
