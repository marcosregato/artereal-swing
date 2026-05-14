package com.artereal.swing.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes unitários para a classe Despesa
 */
@DisplayName("Testes Unitários - Despesa")
class DespesaTest {

    private Despesa despesa;

    @BeforeEach
    void setUp() {
        despesa = new Despesa();
    }

    @Test
    @DisplayName("Deve criar despesa com construtor padrão")
    void testConstrutorPadrao() {
        // Act
        Despesa novaDespesa = new Despesa();

        // Assert
        assertThat(novaDespesa).isNotNull();
        assertThat(novaDespesa.getId()).isNull();
        assertThat(novaDespesa.getDescricao()).isNull();
        assertThat(novaDespesa.getValor()).isEqualTo(0.0); // double default is 0.0
        assertThat(novaDespesa.getData()).isNotNull();
        assertThat(novaDespesa.getCategoria()).isEqualTo("Geral");
        assertThat(novaDespesa.getFornecedor()).isEqualTo("Não informado");
        assertThat(novaDespesa.getNumeroDocumento()).isNull();
    }

    @Test
    @DisplayName("Deve criar despesa com construtor parametrizado")
    void testConstrutorParametrizado() {
        // Arrange
        String descricao = "Material de escritório";
        double valor = 150.75;
        Date data = new Date();
        String categoria = "Material";
        String fornecedor = "Papelaria ABC";
        String numeroDocumento = "NF-123456";

        // Act
        Despesa novaDespesa = new Despesa(descricao, valor, data, categoria, fornecedor, numeroDocumento);

        // Assert
        assertThat(novaDespesa).isNotNull();
        assertThat(novaDespesa.getDescricao()).isEqualTo(descricao);
        assertThat(novaDespesa.getValor()).isEqualTo(valor);
        assertThat(novaDespesa.getData()).isEqualTo(data);
        assertThat(novaDespesa.getCategoria()).isEqualTo(categoria);
        assertThat(novaDespesa.getFornecedor()).isEqualTo(fornecedor);
        assertThat(novaDespesa.getNumeroDocumento()).isEqualTo(numeroDocumento);
    }

    @Test
    @DisplayName("Deve definir e obter ID")
    void testSetGetId() {
        // Arrange
        Long id = 123L;

        // Act
        despesa.setId(id);

        // Assert
        assertThat(despesa.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Deve definir e obter descrição")
    void testSetGetDescricao() {
        // Arrange
        String descricao = "Aluguel do templo";

        // Act
        despesa.setDescricao(descricao);

        // Assert
        assertThat(despesa.getDescricao()).isEqualTo(descricao);
    }

    @Test
    @DisplayName("Deve definir e obter valor")
    void testSetGetValor() {
        // Arrange
        double valor = 500.00;

        // Act
        despesa.setValor(valor);

        // Assert
        assertThat(despesa.getValor()).isEqualTo(valor);
    }

    @Test
    @DisplayName("Deve definir e obter data")
    void testSetGetData() {
        // Arrange
        Date data = new Date();

        // Act
        despesa.setData(data);

        // Assert
        assertThat(despesa.getData()).isEqualTo(data);
    }

    @Test
    @DisplayName("Deve definir e obter categoria")
    void testSetGetCategoria() {
        // Arrange
        String categoria = "Aluguel";

        // Act
        despesa.setCategoria(categoria);

        // Assert
        assertThat(despesa.getCategoria()).isEqualTo(categoria);
    }

    @Test
    @DisplayName("Deve definir e obter fornecedor")
    void testSetGetFornecedor() {
        // Arrange
        String fornecedor = "Imobiliária XYZ";

        // Act
        despesa.setFornecedor(fornecedor);

        // Assert
        assertThat(despesa.getFornecedor()).isEqualTo(fornecedor);
    }

    @Test
    @DisplayName("Deve definir e obter número do documento")
    void testSetGetNumeroDocumento() {
        // Arrange
        String numeroDocumento = "REC-789012";

        // Act
        despesa.setNumeroDocumento(numeroDocumento);

        // Assert
        assertThat(despesa.getNumeroDocumento()).isEqualTo(numeroDocumento);
    }

    @Test
    @DisplayName("Deve representar objeto como string")
    void testToString() {
        // Arrange
        despesa.setId(123L);
        despesa.setDescricao("Aluguel mensal");
        despesa.setValor(1000.00);
        despesa.setCategoria("Aluguel");
        despesa.setFornecedor("Imobiliária XYZ");

        // Act
        String resultado = despesa.toString();

        // Assert - Simplificado para usar SimpleModel
        assertThat(resultado).contains("Despesa");
        assertThat(resultado).contains("id=123");
    }

    @Test
    @DisplayName("Deve comparar objetos iguais")
    void testEquals() {
        // Arrange
        Despesa despesa1 = new Despesa();
        despesa1.setId(123L);

        Despesa despesa2 = new Despesa();
        despesa2.setId(123L);

        Despesa despesa3 = new Despesa();
        despesa3.setId(456L);

        // Act & Assert
        assertThat(despesa1).isEqualTo(despesa2);
        assertThat(despesa1).isNotEqualTo(despesa3);
        assertThat(despesa1).isNotEqualTo(null);
        assertThat(despesa1).isNotEqualTo("string");
    }

    @Test
    @DisplayName("Deve ter mesmo hashCode para IDs iguais")
    void testHashCode() {
        // Arrange
        Despesa despesa1 = new Despesa();
        despesa1.setId(123L);

        Despesa despesa2 = new Despesa();
        despesa2.setId(123L);

        Despesa despesa3 = new Despesa();
        despesa3.setId(456L);

        // Act & Assert
        assertThat(despesa1.hashCode()).isEqualTo(despesa2.hashCode());
        assertThat(despesa1.hashCode()).isNotEqualTo(despesa3.hashCode());
    }

    @Test
    @DisplayName("Deve lidar com valor zero")
    void testValorZero() {
        // Act
        despesa.setValor(0.0);

        // Assert
        assertThat(despesa.getValor()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Deve lidar com valor negativo")
    void testValorNegativo() {
        // Act
        despesa.setValor(-50.0);

        // Assert
        assertThat(despesa.getValor()).isEqualTo(-50.0);
        assertThat(despesa.getValor()).isNegative();
    }

    @Test
    @DisplayName("Deve lidar com valor decimal")
    void testValorDecimal() {
        // Act
        despesa.setValor(123.456);

        // Assert
        assertThat(despesa.getValor()).isEqualTo(123.456);
    }

    @Test
    @DisplayName("Deve lidar com diferentes categorias")
    void testDiferentesCategorias() {
        // Arrange & Act
        despesa.setCategoria("Aluguel");
        assertThat(despesa.getCategoria()).isEqualTo("Aluguel");

        despesa.setCategoria("Material");
        assertThat(despesa.getCategoria()).isEqualTo("Material");

        despesa.setCategoria("Evento");
        assertThat(despesa.getCategoria()).isEqualTo("Evento");

        despesa.setCategoria("Manutenção");
        assertThat(despesa.getCategoria()).isEqualTo("Manutenção");

        despesa.setCategoria("Doação");
        assertThat(despesa.getCategoria()).isEqualTo("Doação");
    }

    @Test
    @DisplayName("Deve lidar com valores nulos")
    void testValoresNulos() {
        // Act
        despesa.setDescricao(null);
        despesa.setData(null);
        despesa.setCategoria(null);
        despesa.setFornecedor(null);
        despesa.setNumeroDocumento(null);

        // Assert
        assertThat(despesa.getDescricao()).isNull();
        assertThat(despesa.getData()).isNull();
        assertThat(despesa.getCategoria()).isNull();
        assertThat(despesa.getFornecedor()).isNull();
        assertThat(despesa.getNumeroDocumento()).isNull();
    }

    @Test
    @DisplayName("Deve lidar com valores vazios")
    void testValoresVazios() {
        // Act
        despesa.setDescricao("");
        despesa.setCategoria("");
        despesa.setFornecedor("");
        despesa.setNumeroDocumento("");

        // Assert
        assertThat(despesa.getDescricao()).isEmpty();
        assertThat(despesa.getCategoria()).isEmpty();
        assertThat(despesa.getFornecedor()).isEmpty();
        assertThat(despesa.getNumeroDocumento()).isEmpty();
    }

    @Test
    @DisplayName("Deve lidar com data específica")
    void testDataEspecifica() {
        // Arrange
        long timestamp = 1640995200000L; // 2022-01-01 00:00:00
        Date dataEspecifica = new Date(timestamp);

        // Act
        despesa.setData(dataEspecifica);

        // Assert
        assertThat(despesa.getData()).isEqualTo(dataEspecifica);
        assertThat(despesa.getData().getTime()).isEqualTo(timestamp);
    }

    @Test
    @DisplayName("Deve lidar com descrições longas")
    void testDescricoesLongas() {
        // Arrange
        String descricaoLonga = "Despesa com material de escritório incluindo canetas, lápis, cadernos, pastas, clipes, grampos, e outros itens essenciais para o funcionamento administrativo da loja.";

        // Act
        despesa.setDescricao(descricaoLonga);

        // Assert
        assertThat(despesa.getDescricao()).isEqualTo(descricaoLonga);
        assertThat(despesa.getDescricao()).hasSizeGreaterThan(50);
    }

    @Test
    @DisplayName("Deve lidar com diferentes fornecedores")
    void testDiferentesFornecedores() {
        // Arrange & Act
        despesa.setFornecedor("Papelaria ABC");
        assertThat(despesa.getFornecedor()).isEqualTo("Papelaria ABC");

        despesa.setFornecedor("Supermercado XYZ");
        assertThat(despesa.getFornecedor()).isEqualTo("Supermercado XYZ");

        despesa.setFornecedor("Loja de Material LTDA");
        assertThat(despesa.getFornecedor()).isEqualTo("Loja de Material LTDA");

        despesa.setFornecedor("Serviços de Limpeza SA");
        assertThat(despesa.getFornecedor()).isEqualTo("Serviços de Limpeza SA");
    }

    @Test
    @DisplayName("Deve lidar com diferentes números de documento")
    void testDiferentesNumerosDocumento() {
        // Arrange & Act
        despesa.setNumeroDocumento("NF-123456");
        assertThat(despesa.getNumeroDocumento()).isEqualTo("NF-123456");

        despesa.setNumeroDocumento("REC-789012");
        assertThat(despesa.getNumeroDocumento()).isEqualTo("REC-789012");

        despesa.setNumeroDocumento("CUP-345678");
        assertThat(despesa.getNumeroDocumento()).isEqualTo("CUP-345678");

        despesa.setNumeroDocumento("BOLETO-901234");
        assertThat(despesa.getNumeroDocumento()).isEqualTo("BOLETO-901234");
    }

    @Test
    @DisplayName("Deve lidar com valor muito grande")
    void testValorMuitoGrande() {
        // Arrange
        double valorGrande = 999999.99;

        // Act
        despesa.setValor(valorGrande);

        // Assert
        assertThat(despesa.getValor()).isEqualTo(valorGrande);
        assertThat(despesa.getValor()).isGreaterThan(999999.0);
    }

    @Test
    @DisplayName("Deve lidar com valor muito pequeno")
    void testValorMuitoPequeno() {
        // Arrange
        double valorPequeno = 0.01;

        // Act
        despesa.setValor(valorPequeno);

        // Assert
        assertThat(despesa.getValor()).isEqualTo(valorPequeno);
        assertThat(despesa.getValor()).isGreaterThan(0.0);
        assertThat(despesa.getValor()).isLessThan(1.0);
    }

    @Test
    @DisplayName("Deve lidar com data atual no construtor padrão")
    void testDataAtualConstrutorPadrao() {
        // Act
        Despesa novaDespesa = new Despesa();
        Date agora = new Date();

        // Assert
        assertThat(novaDespesa.getData()).isNotNull();
        assertThat(novaDespesa.getData().getTime()).isCloseTo(agora.getTime(), org.assertj.core.data.Offset.offset(1000L)); // tolerância de 1 segundo
    }

    @Test
    @DisplayName("Deve lidar com categoria padrão no construtor padrão")
    void testCategoriaPadraoConstrutorPadrao() {
        // Act
        Despesa novaDespesa = new Despesa();

        // Assert
        assertThat(novaDespesa.getCategoria()).isEqualTo("Geral");
    }

    @Test
    @DisplayName("Deve lidar com fornecedor padrão no construtor padrão")
    void testFornecedorPadraoConstrutorPadrao() {
        // Act
        Despesa novaDespesa = new Despesa();

        // Assert
        assertThat(novaDespesa.getFornecedor()).isEqualTo("Não informado");
    }

    @Test
    @DisplayName("Deve lidar com caracteres especiais na descrição")
    void testCaracteresEspeciaisDescricao() {
        // Arrange
        String descricaoComEspeciais = "Despesa com ááá ççç ñññ e símbolos @#$%&*()";

        // Act
        despesa.setDescricao(descricaoComEspeciais);

        // Assert
        assertThat(despesa.getDescricao()).isEqualTo(descricaoComEspeciais);
        assertThat(despesa.getDescricao()).contains("ááá");
        assertThat(despesa.getDescricao()).contains("@#$");
    }

    @Test
    @DisplayName("Deve lidar com ID zero")
    void testIdZero() {
        // Act
        despesa.setId(0L);

        // Assert
        assertThat(despesa.getId()).isEqualTo(0L);
    }

    @Test
    @DisplayName("Deve lidar com ID negativo")
    void testIdNegativo() {
        // Act
        despesa.setId(-1L);

        // Assert
        assertThat(despesa.getId()).isEqualTo(-1L);
        assertThat(despesa.getId()).isNegative();
    }
}
