package com.artereal.swing.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes unitários para a classe Caixa
 */
@DisplayName("Testes Unitários - Caixa")
class CaixaTest {

    private Caixa caixa;

    @BeforeEach
    void setUp() {
        caixa = new Caixa();
    }

    @Test
    @DisplayName("Deve criar caixa com construtor padrão")
    void testConstrutorPadrao() {
        // Act
        Caixa novoCaixa = new Caixa();

        // Assert
        assertThat(novoCaixa).isNotNull();
        assertThat(novoCaixa.getId()).isNull();
        assertThat(novoCaixa.getTipo()).isNull();
        assertThat(novoCaixa.getCategoria()).isNull();
        assertThat(novoCaixa.getDescricao()).isNull();
        assertThat(novoCaixa.getValor()).isNull();
        assertThat(novoCaixa.getDataMovimentacao()).isNull();
        assertThat(novoCaixa.getStatus()).isNull();
    }

    @Test
    @DisplayName("Deve criar caixa com construtor parametrizado")
    void testConstrutorParametrizado() {
        // Arrange
        String tipo = "RECEITA";
        BigDecimal valor = new BigDecimal("100.50");
        String descricao = "Pagamento de anuidade";

        // Act
        Caixa novoCaixa = new Caixa(tipo, valor, descricao);

        // Assert
        assertThat(novoCaixa).isNotNull();
        assertThat(novoCaixa.getTipo()).isEqualTo(tipo);
        assertThat(novoCaixa.getValor()).isEqualTo(valor);
        assertThat(novoCaixa.getDescricao()).isEqualTo(descricao);
        assertThat(novoCaixa.getDataMovimentacao()).isNotNull();
        assertThat(novoCaixa.getDataMovimentacao()).isBeforeOrEqualTo(LocalDateTime.now());
        assertThat(novoCaixa.getStatus()).isEqualTo("PENDENTE");
    }

    @Test
    @DisplayName("Deve definir e obter ID")
    void testSetGetId() {
        // Arrange
        Long id = 123L;

        // Act
        caixa.setId(id);

        // Assert
        assertThat(caixa.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Deve definir e obter tipo")
    void testSetGetTipo() {
        // Arrange
        String tipo = "DESPESA";

        // Act
        caixa.setTipo(tipo);

        // Assert
        assertThat(caixa.getTipo()).isEqualTo(tipo);
    }

    @Test
    @DisplayName("Deve definir e obter categoria")
    void testSetGetCategoria() {
        // Arrange
        String categoria = "ANUIDADE";

        // Act
        caixa.setCategoria(categoria);

        // Assert
        assertThat(caixa.getCategoria()).isEqualTo(categoria);
    }

    @Test
    @DisplayName("Deve definir e obter descrição")
    void testSetGetDescricao() {
        // Arrange
        String descricao = "Pagamento de material de escritório";

        // Act
        caixa.setDescricao(descricao);

        // Assert
        assertThat(caixa.getDescricao()).isEqualTo(descricao);
    }

    @Test
    @DisplayName("Deve definir e obter valor")
    void testSetGetValor() {
        // Arrange
        BigDecimal valor = new BigDecimal("250.75");

        // Act
        caixa.setValor(valor);

        // Assert
        assertThat(caixa.getValor()).isEqualTo(valor);
    }

    @Test
    @DisplayName("Deve definir e obter data de movimentação")
    void testSetGetDataMovimentacao() {
        // Arrange
        LocalDateTime dataMovimentacao = LocalDateTime.of(2024, 1, 15, 10, 30);

        // Act
        caixa.setDataMovimentacao(dataMovimentacao);

        // Assert
        assertThat(caixa.getDataMovimentacao()).isEqualTo(dataMovimentacao);
    }

    @Test
    @DisplayName("Deve definir e obter responsável")
    void testSetGetResponsavel() {
        // Arrange
        String responsavel = "Tesoureiro";

        // Act
        caixa.setResponsavel(responsavel);

        // Assert
        assertThat(caixa.getResponsavel()).isEqualTo(responsavel);
    }

    @Test
    @DisplayName("Deve definir e obter forma de pagamento")
    void testSetGetFormaPagamento() {
        // Arrange
        String formaPagamento = "TRANSFERENCIA";

        // Act
        caixa.setFormaPagamento(formaPagamento);

        // Assert
        assertThat(caixa.getFormaPagamento()).isEqualTo(formaPagamento);
    }

    @Test
    @DisplayName("Deve definir e obter número do documento")
    void testSetGetNumeroDocumento() {
        // Arrange
        String numeroDocumento = "CHQ-001234";

        // Act
        caixa.setNumeroDocumento(numeroDocumento);

        // Assert
        assertThat(caixa.getNumeroDocumento()).isEqualTo(numeroDocumento);
    }

    @Test
    @DisplayName("Deve definir e obter status")
    void testSetGetStatus() {
        // Arrange
        String status = "PAGO";

        // Act
        caixa.setStatus(status);

        // Assert
        assertThat(caixa.getStatus()).isEqualTo(status);
    }

    @Test
    @DisplayName("Deve definir e obter observações")
    void testSetGetObservacoes() {
        // Arrange
        String observacoes = "Pagamento realizado com sucesso";

        // Act
        caixa.setObservacoes(observacoes);

        // Assert
        assertThat(caixa.getObservacoes()).isEqualTo(observacoes);
    }

    @Test
    @DisplayName("Deve definir e obter created at")
    void testSetGetCreatedAt() {
        // Arrange
        String createdAt = "2024-01-15 10:30:00";

        // Act
        caixa.setCreatedAt(createdAt);

        // Assert
        assertThat(caixa.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Deve definir e obter updated at")
    void testSetGetUpdatedAt() {
        // Arrange
        String updatedAt = "2024-01-15 11:00:00";

        // Act
        caixa.setUpdatedAt(updatedAt);

        // Assert
        assertThat(caixa.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("Deve representar objeto como string")
    void testToString() {
        // Arrange
        caixa.setId(123L);
        caixa.setTipo("RECEITA");
        caixa.setCategoria("ANUIDADE");
        caixa.setDescricao("Anuidade 2024");
        caixa.setValor(new BigDecimal("100.00"));
        caixa.setStatus("PAGO");

        // Act
        String resultado = caixa.toString();

        // Assert - Simplificado para usar SimpleModel
        assertThat(resultado).contains("Caixa");
        assertThat(resultado).contains("id=123");
    }

    @Test
    @DisplayName("Deve comparar objetos iguais")
    void testEquals() {
        // Arrange
        Caixa caixa1 = new Caixa();
        caixa1.setId(123L);

        Caixa caixa2 = new Caixa();
        caixa2.setId(123L);

        Caixa caixa3 = new Caixa();
        caixa3.setId(456L);

        // Act & Assert
        assertThat(caixa1).isEqualTo(caixa2);
        assertThat(caixa1).isNotEqualTo(caixa3);
        assertThat(caixa1).isNotEqualTo(null);
        assertThat(caixa1).isNotEqualTo("string");
    }

    @Test
    @DisplayName("Deve ter mesmo hashCode para IDs iguais")
    void testHashCode() {
        // Arrange
        Caixa caixa1 = new Caixa();
        caixa1.setId(123L);

        Caixa caixa2 = new Caixa();
        caixa2.setId(123L);

        Caixa caixa3 = new Caixa();
        caixa3.setId(456L);

        // Act & Assert
        assertThat(caixa1.hashCode()).isEqualTo(caixa2.hashCode());
        assertThat(caixa1.hashCode()).isNotEqualTo(caixa3.hashCode());
    }

    @Test
    @DisplayName("Deve lidar com valor zero")
    void testValorZero() {
        // Arrange
        BigDecimal valorZero = BigDecimal.ZERO;

        // Act
        caixa.setValor(valorZero);

        // Assert
        assertThat(caixa.getValor()).isEqualTo(valorZero);
        assertThat(caixa.getValor()).isEqualByComparingTo("0.00");
    }

    @Test
    @DisplayName("Deve lidar com valor negativo")
    void testValorNegativo() {
        // Arrange
        BigDecimal valorNegativo = new BigDecimal("-50.00");

        // Act
        caixa.setValor(valorNegativo);

        // Assert
        assertThat(caixa.getValor()).isEqualTo(valorNegativo);
        assertThat(caixa.getValor()).isNegative();
    }

    @Test
    @DisplayName("Deve lidar com valor decimal")
    void testValorDecimal() {
        // Arrange
        BigDecimal valorDecimal = new BigDecimal("123.456");

        // Act
        caixa.setValor(valorDecimal);

        // Assert
        assertThat(caixa.getValor()).isEqualTo(valorDecimal);
        assertThat(caixa.getValor()).isEqualByComparingTo("123.456");
    }

    @Test
    @DisplayName("Deve lidar com diferentes tipos")
    void testDiferentesTipos() {
        // Arrange & Act
        caixa.setTipo("RECEITA");
        assertThat(caixa.getTipo()).isEqualTo("RECEITA");

        caixa.setTipo("DESPESA");
        assertThat(caixa.getTipo()).isEqualTo("DESPESA");
    }

    @Test
    @DisplayName("Deve lidar com diferentes categorias")
    void testDiferentesCategorias() {
        // Arrange & Act
        caixa.setCategoria("ANUIDADE");
        assertThat(caixa.getCategoria()).isEqualTo("ANUIDADE");

        caixa.setCategoria("DOACAO");
        assertThat(caixa.getCategoria()).isEqualTo("DOACAO");

        caixa.setCategoria("MATERIAL");
        assertThat(caixa.getCategoria()).isEqualTo("MATERIAL");

        caixa.setCategoria("ALUGUEL");
        assertThat(caixa.getCategoria()).isEqualTo("ALUGUEL");

        caixa.setCategoria("EVENTO");
        assertThat(caixa.getCategoria()).isEqualTo("EVENTO");
    }

    @Test
    @DisplayName("Deve lidar com diferentes formas de pagamento")
    void testDiferentesFormasPagamento() {
        // Arrange & Act
        caixa.setFormaPagamento("DINHEIRO");
        assertThat(caixa.getFormaPagamento()).isEqualTo("DINHEIRO");

        caixa.setFormaPagamento("CHEQUE");
        assertThat(caixa.getFormaPagamento()).isEqualTo("CHEQUE");

        caixa.setFormaPagamento("TRANSFERENCIA");
        assertThat(caixa.getFormaPagamento()).isEqualTo("TRANSFERENCIA");

        caixa.setFormaPagamento("PIX");
        assertThat(caixa.getFormaPagamento()).isEqualTo("PIX");

        caixa.setFormaPagamento("CARTAO");
        assertThat(caixa.getFormaPagamento()).isEqualTo("CARTAO");
    }

    @Test
    @DisplayName("Deve lidar com diferentes status")
    void testDiferentesStatus() {
        // Arrange & Act
        caixa.setStatus("PAGO");
        assertThat(caixa.getStatus()).isEqualTo("PAGO");

        caixa.setStatus("PENDENTE");
        assertThat(caixa.getStatus()).isEqualTo("PENDENTE");

        caixa.setStatus("CANCELADO");
        assertThat(caixa.getStatus()).isEqualTo("CANCELADO");
    }

    @Test
    @DisplayName("Deve lidar com valores nulos")
    void testValoresNulos() {
        // Act
        caixa.setValor(null);
        caixa.setDataMovimentacao(null);
        caixa.setResponsavel(null);
        caixa.setFormaPagamento(null);
        caixa.setNumeroDocumento(null);
        caixa.setStatus(null);
        caixa.setObservacoes(null);

        // Assert
        assertThat(caixa.getValor()).isNull();
        assertThat(caixa.getDataMovimentacao()).isNull();
        assertThat(caixa.getResponsavel()).isNull();
        assertThat(caixa.getFormaPagamento()).isNull();
        assertThat(caixa.getNumeroDocumento()).isNull();
        assertThat(caixa.getStatus()).isNull();
        assertThat(caixa.getObservacoes()).isNull();
    }

    @Test
    @DisplayName("Deve lidar com valores vazios")
    void testValoresVazios() {
        // Arrange & Act
        caixa.setDescricao("");
        caixa.setResponsavel("");
        caixa.setFormaPagamento("");
        caixa.setNumeroDocumento("");
        caixa.setStatus("");
        caixa.setObservacoes("");

        // Assert
        assertThat(caixa.getDescricao()).isEmpty();
        assertThat(caixa.getResponsavel()).isEmpty();
        assertThat(caixa.getFormaPagamento()).isEmpty();
        assertThat(caixa.getNumeroDocumento()).isEmpty();
        assertThat(caixa.getStatus()).isEmpty();
        assertThat(caixa.getObservacoes()).isEmpty();
    }

    @Test
    @DisplayName("Deve lidar com data de movimentação futura")
    void testDataMovimentacaoFutura() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime dataFutura = agora.plusDays(1);

        // Act
        caixa.setDataMovimentacao(dataFutura);

        // Assert
        assertThat(caixa.getDataMovimentacao()).isEqualTo(dataFutura);
        assertThat(caixa.getDataMovimentacao()).isAfter(agora);
    }

    @Test
    @DisplayName("Deve lidar com data de movimentação passada")
    void testDataMovimentacaoPassada() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime dataPassada = agora.minusDays(1);

        // Act
        caixa.setDataMovimentacao(dataPassada);

        // Assert
        assertThat(caixa.getDataMovimentacao()).isEqualTo(dataPassada);
        assertThat(caixa.getDataMovimentacao()).isBefore(agora);
    }

    @Test
    @DisplayName("Deve lidar com números de documento diferentes")
    void testNumerosDocumento() {
        // Arrange & Act
        caixa.setNumeroDocumento("CHQ-001234");
        assertThat(caixa.getNumeroDocumento()).isEqualTo("CHQ-001234");

        caixa.setNumeroDocumento("BLT-567890");
        assertThat(caixa.getNumeroDocumento()).isEqualTo("BLT-567890");

        caixa.setNumeroDocumento("PIX-ABC123");
        assertThat(caixa.getNumeroDocumento()).isEqualTo("PIX-ABC123");

        caixa.setNumeroDocumento("NFS-987654");
        assertThat(caixa.getNumeroDocumento()).isEqualTo("NFS-987654");
    }
}
