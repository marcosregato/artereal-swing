package com.artereal.swing.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes unitários para a classe Cheque
 */
@DisplayName("Testes Unitários - Cheque")
class ChequeTest {

    private Cheque cheque;

    @BeforeEach
    void setUp() {
        cheque = new Cheque();
    }

    @Test
    @DisplayName("Deve criar cheque com construtor padrão")
    void testConstrutorPadrao() {
        // Act
        Cheque novoCheque = new Cheque();

        // Assert
        assertThat(novoCheque).isNotNull();
        assertThat(novoCheque.getId()).isNull();
        assertThat(novoCheque.getFatura()).isNull();
        assertThat(novoCheque.getSacado()).isNull();
        assertThat(novoCheque.getSituacao()).isNull();
        assertThat(novoCheque.getDataEmissao()).isNull();
        assertThat(novoCheque.isAtivo()).isFalse(); // boolean default is false
    }

    @Test
    @DisplayName("Deve criar cheque com construtor parametrizado")
    void testConstrutorParametrizado() {
        // Arrange
        String sacado = "Empresa ABC";
        double valor = 1000.50;
        LocalDate dataVencimento = LocalDate.of(2024, 2, 15);

        // Act
        Cheque novoCheque = new Cheque(sacado, valor, dataVencimento);

        // Assert
        assertThat(novoCheque).isNotNull();
        assertThat(novoCheque.getSacado()).isEqualTo(sacado);
        assertThat(novoCheque.getValor()).isEqualTo(valor);
        assertThat(novoCheque.getDataVencimento()).isEqualTo(dataVencimento);
        assertThat(novoCheque.getDataEmissao()).isEqualTo(LocalDate.now());
        assertThat(novoCheque.getSituacao()).isEqualTo("ABERTO");
        assertThat(novoCheque.getValorPago()).isEqualTo(0.0);
        assertThat(novoCheque.isAtivo()).isTrue();
    }

    @Test
    @DisplayName("Deve definir e obter ID")
    void testSetGetId() {
        // Arrange
        Long id = 123L;

        // Act
        cheque.setId(id);

        // Assert
        assertThat(cheque.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Deve definir e obter fatura")
    void testSetGetFatura() {
        // Arrange
        String fatura = "FAT-001234";

        // Act
        cheque.setFatura(fatura);

        // Assert
        assertThat(cheque.getFatura()).isEqualTo(fatura);
    }

    @Test
    @DisplayName("Deve definir e obter data de emissão")
    void testSetGetDataEmissao() {
        // Arrange
        LocalDate dataEmissao = LocalDate.of(2024, 1, 15);

        // Act
        cheque.setDataEmissao(dataEmissao);

        // Assert
        assertThat(cheque.getDataEmissao()).isEqualTo(dataEmissao);
    }

    @Test
    @DisplayName("Deve definir e obter sacado")
    void testSetGetSacado() {
        // Arrange
        String sacado = "João da Silva";

        // Act
        cheque.setSacado(sacado);

        // Assert
        assertThat(cheque.getSacado()).isEqualTo(sacado);
    }

    @Test
    @DisplayName("Deve definir e obter valor")
    void testSetGetValor() {
        // Arrange
        double valor = 2500.75;

        // Act
        cheque.setValor(valor);

        // Assert
        assertThat(cheque.getValor()).isEqualTo(valor);
    }

    @Test
    @DisplayName("Deve definir e obter data de vencimento")
    void testSetGetDataVencimento() {
        // Arrange
        LocalDate dataVencimento = LocalDate.of(2024, 3, 1);

        // Act
        cheque.setDataVencimento(dataVencimento);

        // Assert
        assertThat(cheque.getDataVencimento()).isEqualTo(dataVencimento);
    }

    @Test
    @DisplayName("Deve definir e obter modo de pagamento")
    void testSetGetModoPagamento() {
        // Arrange
        String modoPagamento = "DEPOSITO";

        // Act
        cheque.setModoPagamento(modoPagamento);

        // Assert
        assertThat(cheque.getModoPagamento()).isEqualTo(modoPagamento);
    }

    @Test
    @DisplayName("Deve definir e obter banco")
    void testSetGetBanco() {
        // Arrange
        String banco = "Banco do Brasil";

        // Act
        cheque.setBanco(banco);

        // Assert
        assertThat(cheque.getBanco()).isEqualTo(banco);
    }

    @Test
    @DisplayName("Deve definir e obter data de pagamento")
    void testSetGetDataPagamento() {
        // Arrange
        LocalDate dataPagamento = LocalDate.of(2024, 2, 28);

        // Act
        cheque.setDataPagamento(dataPagamento);

        // Assert
        assertThat(cheque.getDataPagamento()).isEqualTo(dataPagamento);
    }

    @Test
    @DisplayName("Deve definir e obter valor pago")
    void testSetGetValorPago() {
        // Arrange
        double valorPago = 1500.00;

        // Act
        cheque.setValorPago(valorPago);

        // Assert
        assertThat(cheque.getValorPago()).isEqualTo(valorPago);
    }

    @Test
    @DisplayName("Deve definir e obter código do cliente")
    void testSetGetCodigoCliente() {
        // Arrange
        Long codigoCliente = 456L;

        // Act
        cheque.setCodigoCliente(codigoCliente);

        // Assert
        assertThat(cheque.getCodigoCliente()).isEqualTo(codigoCliente);
    }

    @Test
    @DisplayName("Deve definir e obter situação")
    void testSetGetSituacao() {
        // Arrange
        String situacao = "PAGO";

        // Act
        cheque.setSituacao(situacao);

        // Assert
        assertThat(cheque.getSituacao()).isEqualTo(situacao);
    }

    @Test
    @DisplayName("Deve definir e obter grupo")
    void testSetGetGrupo() {
        // Arrange
        String grupo = "FORNECEDORES";

        // Act
        cheque.setGrupo(grupo);

        // Assert
        assertThat(cheque.getGrupo()).isEqualTo(grupo);
    }

    @Test
    @DisplayName("Deve definir e obter histórico")
    void testSetGetHistorico() {
        // Arrange
        String historico = "Pagamento de fornecedor";

        // Act
        cheque.setHistorico(historico);

        // Assert
        assertThat(cheque.getHistorico()).isEqualTo(historico);
    }

    @Test
    @DisplayName("Deve definir e obter lançamento crédito")
    void testSetGetLancamentoCredito() {
        // Arrange
        String lancamentoCredito = "CRED-001";

        // Act
        cheque.setLancamentoCredito(lancamentoCredito);

        // Assert
        assertThat(cheque.getLancamentoCredito()).isEqualTo(lancamentoCredito);
    }

    @Test
    @DisplayName("Deve definir e obter lançamento débito")
    void testSetGetLancamentoDebito() {
        // Arrange
        String lancamentoDebito = "DEB-001";

        // Act
        cheque.setLancamentoDebito(lancamentoDebito);

        // Assert
        assertThat(cheque.getLancamentoDebito()).isEqualTo(lancamentoDebito);
    }

    @Test
    @DisplayName("Deve definir e obter número da nota")
    void testSetGetNumeroNota() {
        // Arrange
        String numeroNota = "NF-123456";

        // Act
        cheque.setNumeroNota(numeroNota);

        // Assert
        assertThat(cheque.getNumeroNota()).isEqualTo(numeroNota);
    }

    @Test
    @DisplayName("Deve definir e obter código do vendedor")
    void testSetGetCodigoVendedor() {
        // Arrange
        Long codigoVendedor = 789L;

        // Act
        cheque.setCodigoVendedor(codigoVendedor);

        // Assert
        assertThat(cheque.getCodigoVendedor()).isEqualTo(codigoVendedor);
    }

    @Test
    @DisplayName("Deve definir e obter status ativo")
    void testSetGetAtivo() {
        // Arrange
        boolean ativo = false;

        // Act
        cheque.setAtivo(ativo);

        // Assert
        assertThat(cheque.isAtivo()).isEqualTo(ativo);
    }

    @Test
    @DisplayName("Deve definir e obter created at")
    void testSetGetCreatedAt() {
        // Arrange
        String createdAt = "2024-01-15 10:30:00";

        // Act
        cheque.setCreatedAt(createdAt);

        // Assert
        assertThat(cheque.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Deve definir e obter updated at")
    void testSetGetUpdatedAt() {
        // Arrange
        String updatedAt = "2024-02-15 11:00:00";

        // Act
        cheque.setUpdatedAt(updatedAt);

        // Assert
        assertThat(cheque.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("Deve representar objeto como string")
    void testToString() {
        // Arrange
        cheque.setId(123L);
        cheque.setSacado("Empresa ABC");
        cheque.setValor(1000.50);
        cheque.setSituacao("ABERTO");

        // Act
        String resultado = cheque.toString();

        // Assert - Simplificado para usar SimpleModel
        assertThat(resultado).contains("Cheque");
        assertThat(resultado).contains("id=123");
    }

    @Test
    @DisplayName("Deve comparar objetos iguais")
    void testEquals() {
        // Arrange
        Cheque cheque1 = new Cheque();
        cheque1.setId(123L);

        Cheque cheque2 = new Cheque();
        cheque2.setId(123L);

        Cheque cheque3 = new Cheque();
        cheque3.setId(456L);

        // Act & Assert
        assertThat(cheque1).isEqualTo(cheque2);
        assertThat(cheque1).isNotEqualTo(cheque3);
        assertThat(cheque1).isNotEqualTo(null);
        assertThat(cheque1).isNotEqualTo("string");
    }

    @Test
    @DisplayName("Deve ter mesmo hashCode para IDs iguais")
    void testHashCode() {
        // Arrange
        Cheque cheque1 = new Cheque();
        cheque1.setId(123L);

        Cheque cheque2 = new Cheque();
        cheque2.setId(123L);

        Cheque cheque3 = new Cheque();
        cheque3.setId(456L);

        // Act & Assert
        assertThat(cheque1.hashCode()).isEqualTo(cheque2.hashCode());
        assertThat(cheque1.hashCode()).isNotEqualTo(cheque3.hashCode());
    }

    @Test
    @DisplayName("Deve lidar com diferentes situações")
    void testDiferentesSituacoes() {
        // Arrange & Act
        cheque.setSituacao("ABERTO");
        assertThat(cheque.getSituacao()).isEqualTo("ABERTO");

        cheque.setSituacao("PAGO");
        assertThat(cheque.getSituacao()).isEqualTo("PAGO");

        cheque.setSituacao("CANCELADO");
        assertThat(cheque.getSituacao()).isEqualTo("CANCELADO");

        cheque.setSituacao("DEVOLVIDO");
        assertThat(cheque.getSituacao()).isEqualTo("DEVOLVIDO");
    }

    @Test
    @DisplayName("Deve lidar com diferentes modos de pagamento")
    void testDiferentesModosPagamento() {
        // Arrange & Act
        cheque.setModoPagamento("DEPOSITO");
        assertThat(cheque.getModoPagamento()).isEqualTo("DEPOSITO");

        cheque.setModoPagamento("TRANSFERENCIA");
        assertThat(cheque.getModoPagamento()).isEqualTo("TRANSFERENCIA");

        cheque.setModoPagamento("DINHEIRO");
        assertThat(cheque.getModoPagamento()).isEqualTo("DINHEIRO");

        cheque.setModoPagamento("PIX");
        assertThat(cheque.getModoPagamento()).isEqualTo("PIX");
    }

    @Test
    @DisplayName("Deve lidar com valor zero")
    void testValorZero() {
        // Act
        cheque.setValor(0.0);

        // Assert
        assertThat(cheque.getValor()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Deve lidar com valor negativo")
    void testValorNegativo() {
        // Act
        cheque.setValor(-100.0);

        // Assert
        assertThat(cheque.getValor()).isEqualTo(-100.0);
        assertThat(cheque.getValor()).isNegative();
    }

    @Test
    @DisplayName("Deve lidar com valor decimal")
    void testValorDecimal() {
        // Act
        cheque.setValor(1234.56);

        // Assert
        assertThat(cheque.getValor()).isEqualTo(1234.56);
    }

    @Test
    @DisplayName("Deve lidar com valor pago maior que valor")
    void testValorPagoMaiorQueValor() {
        // Arrange
        cheque.setValor(1000.0);
        cheque.setValorPago(1200.0);

        // Assert
        assertThat(cheque.getValorPago()).isGreaterThan(cheque.getValor());
    }

    @Test
    @DisplayName("Deve lidar com valor pago igual ao valor")
    void testValorPagoIgualValor() {
        // Arrange
        cheque.setValor(1000.0);
        cheque.setValorPago(1000.0);

        // Assert
        assertThat(cheque.getValorPago()).isEqualTo(cheque.getValor());
    }

    @Test
    @DisplayName("Deve lidar com datas de vencimento futuras e passadas")
    void testDatasVencimento() {
        // Arrange
        LocalDate hoje = LocalDate.now();
        LocalDate dataFutura = hoje.plusDays(30);
        LocalDate dataPassada = hoje.minusDays(30);

        // Act
        cheque.setDataVencimento(dataFutura);

        // Assert
        assertThat(cheque.getDataVencimento()).isEqualTo(dataFutura);
        assertThat(cheque.getDataVencimento()).isAfter(hoje);

        // Act
        cheque.setDataVencimento(dataPassada);

        // Assert
        assertThat(cheque.getDataVencimento()).isEqualTo(dataPassada);
        assertThat(cheque.getDataVencimento()).isBefore(hoje);
    }

    @Test
    @DisplayName("Deve lidar com valores nulos")
    void testValoresNulos() {
        // Act
        cheque.setFatura(null);
        cheque.setSacado(null);
        cheque.setModoPagamento(null);
        cheque.setBanco(null);
        cheque.setSituacao(null);
        cheque.setGrupo(null);
        cheque.setHistorico(null);
        cheque.setLancamentoCredito(null);
        cheque.setLancamentoDebito(null);
        cheque.setNumeroNota(null);

        // Assert
        assertThat(cheque.getFatura()).isNull();
        assertThat(cheque.getSacado()).isNull();
        assertThat(cheque.getModoPagamento()).isNull();
        assertThat(cheque.getBanco()).isNull();
        assertThat(cheque.getSituacao()).isNull();
        assertThat(cheque.getGrupo()).isNull();
        assertThat(cheque.getHistorico()).isNull();
        assertThat(cheque.getLancamentoCredito()).isNull();
        assertThat(cheque.getLancamentoDebito()).isNull();
        assertThat(cheque.getNumeroNota()).isNull();
    }

    @Test
    @DisplayName("Deve lidar com valores vazios")
    void testValoresVazios() {
        // Act
        cheque.setFatura("");
        cheque.setSacado("");
        cheque.setModoPagamento("");
        cheque.setBanco("");
        cheque.setSituacao("");
        cheque.setGrupo("");
        cheque.setHistorico("");
        cheque.setLancamentoCredito("");
        cheque.setLancamentoDebito("");
        cheque.setNumeroNota("");

        // Assert
        assertThat(cheque.getFatura()).isEmpty();
        assertThat(cheque.getSacado()).isEmpty();
        assertThat(cheque.getModoPagamento()).isEmpty();
        assertThat(cheque.getBanco()).isEmpty();
        assertThat(cheque.getSituacao()).isEmpty();
        assertThat(cheque.getGrupo()).isEmpty();
        assertThat(cheque.getHistorico()).isEmpty();
        assertThat(cheque.getLancamentoCredito()).isEmpty();
        assertThat(cheque.getLancamentoDebito()).isEmpty();
        assertThat(cheque.getNumeroNota()).isEmpty();
    }

    @Test
    @DisplayName("Deve lidar com diferentes bancos")
    void testDiferentesBancos() {
        // Arrange & Act
        cheque.setBanco("Banco do Brasil");
        assertThat(cheque.getBanco()).isEqualTo("Banco do Brasil");

        cheque.setBanco("Caixa Econômica Federal");
        assertThat(cheque.getBanco()).isEqualTo("Caixa Econômica Federal");

        cheque.setBanco("Banco Itaú");
        assertThat(cheque.getBanco()).isEqualTo("Banco Itaú");

        cheque.setBanco("Banco Santander");
        assertThat(cheque.getBanco()).isEqualTo("Banco Santander");
    }

    @Test
    @DisplayName("Deve lidar com data de pagamento após vencimento")
    void testDataPagamentoAposVencimento() {
        // Arrange
        LocalDate dataVencimento = LocalDate.of(2024, 1, 15);
        LocalDate dataPagamento = LocalDate.of(2024, 1, 20);

        // Act
        cheque.setDataVencimento(dataVencimento);
        cheque.setDataPagamento(dataPagamento);

        // Assert
        assertThat(cheque.getDataPagamento()).isAfter(cheque.getDataVencimento());
    }

    @Test
    @DisplayName("Deve lidar com data de pagamento antes do vencimento")
    void testDataPagamentoAntesVencimento() {
        // Arrange
        LocalDate dataVencimento = LocalDate.of(2024, 1, 15);
        LocalDate dataPagamento = LocalDate.of(2024, 1, 10);

        // Act
        cheque.setDataVencimento(dataVencimento);
        cheque.setDataPagamento(dataPagamento);

        // Assert
        assertThat(cheque.getDataPagamento()).isBefore(cheque.getDataVencimento());
    }

    @Test
    @DisplayName("Deve lidar com códigos nulos")
    void testCodigosNulos() {
        // Act
        cheque.setCodigoCliente(null);
        cheque.setCodigoVendedor(null);

        // Assert
        assertThat(cheque.getCodigoCliente()).isNull();
        assertThat(cheque.getCodigoVendedor()).isNull();
    }

    @Test
    @DisplayName("Deve lidar com códigos zero")
    void testCodigosZero() {
        // Act
        cheque.setCodigoCliente(0L);
        cheque.setCodigoVendedor(0L);

        // Assert
        assertThat(cheque.getCodigoCliente()).isEqualTo(0L);
        assertThat(cheque.getCodigoVendedor()).isEqualTo(0L);
    }
}
