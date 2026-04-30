package com.artereal.swing.unit.model;

import com.artereal.swing.model.Irmao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes unitários para a classe Irmao
 */
@DisplayName("Testes do Modelo Irmao")
class IrmaoTest {

    private Irmao irmao;

    @BeforeEach
    void setUp() {
        irmao = new Irmao();
    }

    @Test
    @DisplayName("Deve criar irmão com dados básicos")
    void testIrmaoCreation() {
        // Arrange
        String nomeEsperado = "João Silva";
        String telefoneEsperado = "123456789";
        boolean ativoEsperado = true;

        // Act
        irmao.setNome(nomeEsperado);
        irmao.setTelefone(telefoneEsperado);
        irmao.setAtivo(ativoEsperado);

        // Assert
        assertThat(irmao.getNome()).isEqualTo(nomeEsperado);
        assertThat(irmao.getTelefone()).isEqualTo(telefoneEsperado);
        assertThat(irmao.isAtivo()).isEqualTo(ativoEsperado);
    }

    @Test
    @DisplayName("Deve configurar data de nascimento")
    void testNascimentoConfiguration() {
        // Arrange
        LocalDate nascimentoEsperado = LocalDate.of(1980, 5, 15);

        // Act
        irmao.setNascimento(nascimentoEsperado);

        // Assert
        assertThat(irmao.getNascimento()).isEqualTo(nascimentoEsperado);
    }

    @Test
    @DisplayName("Deve configurar endereço completo")
    void testEnderecoConfiguration() {
        // Arrange
        String enderecoEsperado = "Rua das Flores, 123";
        String bairroEsperado = "Centro";
        String cidadeEsperada = "São Paulo";
        String estadoEsperado = "SP";

        // Act
        irmao.setEndereco(enderecoEsperado);
        irmao.setBairro(bairroEsperado);
        irmao.setCidade(cidadeEsperada);
        irmao.setEstado(estadoEsperado);

        // Assert
        assertThat(irmao.getEndereco()).isEqualTo(enderecoEsperado);
        assertThat(irmao.getBairro()).isEqualTo(bairroEsperado);
        assertThat(irmao.getCidade()).isEqualTo(cidadeEsperada);
        assertThat(irmao.getEstado()).isEqualTo(estadoEsperado);
    }

    @Test
    @DisplayName("Deve configurar dados maçônicos")
    void testDadosMasonicConfiguration() {
        // Arrange
        String cargoLojaEsperado = "Venerável";
        String grauEsperado = "Mestre";
        String cargoGrandeLojaEsperado = "Grande Secretário";
        String registroGrandeLojaEsperado = "12345/SP";

        // Act
        irmao.setCargoLoja(cargoLojaEsperado);
        irmao.setGrau(grauEsperado);
        irmao.setCargoGrandeLoja(cargoGrandeLojaEsperado);
        irmao.setRegistroGrandeLoja(registroGrandeLojaEsperado);

        // Assert
        assertThat(irmao.getCargoLoja()).isEqualTo(cargoLojaEsperado);
        assertThat(irmao.getGrau()).isEqualTo(grauEsperado);
        assertThat(irmao.getCargoGrandeLoja()).isEqualTo(cargoGrandeLojaEsperado);
        assertThat(irmao.getRegistroGrandeLoja()).isEqualTo(registroGrandeLojaEsperado);
    }

    @Test
    @DisplayName("Deve configurar dados profissionais")
    void testDadosProfissionaisConfiguration() {
        // Arrange
        String empresaEsperada = "Empresa ABC";
        String telefoneEmpresaEsperado = "987654321";
        String enderecoEmpresaEsperado = "Av. Principal, 456";

        // Act
        irmao.setEmpresa(empresaEsperada);
        irmao.setTelefoneEmpresa(telefoneEmpresaEsperado);
        irmao.setEnderecoEmpresa(enderecoEmpresaEsperado);

        // Assert
        assertThat(irmao.getEmpresa()).isEqualTo(empresaEsperada);
        assertThat(irmao.getTelefoneEmpresa()).isEqualTo(telefoneEmpresaEsperado);
        assertThat(irmao.getEnderecoEmpresa()).isEqualTo(enderecoEmpresaEsperado);
    }

    @Test
    @DisplayName("Deve configurar identificação pessoal")
    void testIdentificacaoPessoalConfiguration() {
        // Arrange
        String estadoCivilEsperado = "Casado";
        String naturalEsperado = "Rio de Janeiro";
        String identidadeEsperada = "123456789";
        String tipoSanguineoEsperado = "O+";

        // Act
        irmao.setEstadoCivil(estadoCivilEsperado);
        irmao.setNatural(naturalEsperado);
        irmao.setIdentidade(identidadeEsperada);
        irmao.setTipoSanguineo(tipoSanguineoEsperado);

        // Assert
        assertThat(irmao.getEstadoCivil()).isEqualTo(estadoCivilEsperado);
        assertThat(irmao.getNatural()).isEqualTo(naturalEsperado);
        assertThat(irmao.getIdentidade()).isEqualTo(identidadeEsperada);
        assertThat(irmao.getTipoSanguineo()).isEqualTo(tipoSanguineoEsperado);
    }

    @Test
    @DisplayName("Deve configurar ID quando atribuído")
    void testIdConfiguration() {
        // Arrange
        Long idEsperado = 123L;

        // Act
        irmao.setId(idEsperado);

        // Assert
        assertThat(irmao.getId()).isEqualTo(idEsperado);
    }

    @Test
    @DisplayName("Deve configurar data de criação")
    void testCreatedAtConfiguration() {
        // Arrange
        String createdAtEsperado = "2024-01-15 10:30:00";

        // Act
        irmao.setCreatedAt(createdAtEsperado);

        // Assert
        assertThat(irmao.getCreatedAt()).isEqualTo(createdAtEsperado);
    }

    @Test
    @DisplayName("Deve permitir valores nulos em campos opcionais")
    void testCamposOpcionaisNulos() {
        // Act & Assert - Não deve lançar exceção
        irmao.setTelefone(null);
        irmao.setEmpresa(null);
        irmao.setEnderecoEmpresa(null);
        irmao.setTelefoneEmpresa(null);
        irmao.setCargoGrandeLoja(null);
        irmao.setRegistroGrandeLoja(null);

        assertThat(irmao.getTelefone()).isNull();
        assertThat(irmao.getEmpresa()).isNull();
        assertThat(irmao.getEnderecoEmpresa()).isNull();
        assertThat(irmao.getTelefoneEmpresa()).isNull();
        assertThat(irmao.getCargoGrandeLoja()).isNull();
        assertThat(irmao.getRegistroGrandeLoja()).isNull();
    }

    @Test
    @DisplayName("Deve manter consistência nos dados")
    void testConsistenciaDados() {
        // Arrange
        irmao.setId(1L);
        irmao.setNome("Teste Consistência");
        irmao.setTelefone("111111111");
        irmao.setAtivo(true);

        // Act & Assert
        assertThat(irmao.getId()).isPositive();
        assertThat(irmao.getNome()).isNotBlank();
        assertThat(irmao.getTelefone()).isNotBlank();
        assertThat(irmao.isAtivo()).isTrue();
    }

    @Test
    @DisplayName("Deve permitir atualização de dados")
    void testAtualizacaoDados() {
        // Arrange
        irmao.setNome("Nome Original");
        irmao.setTelefone("999999999");

        // Act
        irmao.setNome("Nome Atualizado");
        irmao.setTelefone("888888888");

        // Assert
        assertThat(irmao.getNome()).isEqualTo("Nome Atualizado");
        assertThat(irmao.getTelefone()).isEqualTo("888888888");
    }
}
