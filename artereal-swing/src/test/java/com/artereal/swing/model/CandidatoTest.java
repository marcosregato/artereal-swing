package com.artereal.swing.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes unitários para a classe Candidato
 */
@DisplayName("Testes Unitários - Candidato")
class CandidatoTest {

    private Candidato candidato;

    @BeforeEach
    void setUp() {
        candidato = new Candidato();
    }

    @Test
    @DisplayName("Deve criar candidato com construtor padrão")
    void testConstrutorPadrao() {
        // Act
        Candidato novoCandidato = new Candidato();

        // Assert
        assertThat(novoCandidato).isNotNull();
        assertThat(novoCandidato.getId()).isNull();
        assertThat(novoCandidato.getNome()).isNull();
        assertThat(novoCandidato.getEndereco()).isNull();
        assertThat(novoCandidato.getStatus()).isNull();
        assertThat(novoCandidato.getDataCadastro()).isNull();
    }

    @Test
    @DisplayName("Deve criar candidato com construtor parametrizado")
    void testConstrutorParametrizado() {
        // Arrange
        String nome = "João da Silva";
        String endereco = "Rua Principal, 123";

        // Act
        Candidato novoCandidato = new Candidato(nome, endereco);

        // Assert
        assertThat(novoCandidato).isNotNull();
        assertThat(novoCandidato.getNome()).isEqualTo(nome);
        assertThat(novoCandidato.getEndereco()).isEqualTo(endereco);
        assertThat(novoCandidato.getDataCadastro()).isEqualTo(LocalDate.now());
        assertThat(novoCandidato.getStatus()).isEqualTo("CANDIDATO");
    }

    @Test
    @DisplayName("Deve definir e obter ID")
    void testSetGetId() {
        // Arrange
        Long id = 123L;

        // Act
        candidato.setId(id);

        // Assert
        assertThat(candidato.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Deve definir e obter nome")
    void testSetGetNome() {
        // Arrange
        String nome = "Pedro Santos";

        // Act
        candidato.setNome(nome);

        // Assert
        assertThat(candidato.getNome()).isEqualTo(nome);
    }

    @Test
    @DisplayName("Deve definir e obter endereço")
    void testSetGetEndereco() {
        // Arrange
        String endereco = "Avenida Central, 456";

        // Act
        candidato.setEndereco(endereco);

        // Assert
        assertThat(candidato.getEndereco()).isEqualTo(endereco);
    }

    @Test
    @DisplayName("Deve definir e obter número")
    void testSetGetNumero() {
        // Arrange
        String numero = "789";

        // Act
        candidato.setNumero(numero);

        // Assert
        assertThat(candidato.getNumero()).isEqualTo(numero);
    }

    @Test
    @DisplayName("Deve definir e obter cidade")
    void testSetGetCidade() {
        // Arrange
        String cidade = "São Paulo";

        // Act
        candidato.setCidade(cidade);

        // Assert
        assertThat(candidato.getCidade()).isEqualTo(cidade);
    }

    @Test
    @DisplayName("Deve definir e obter estado")
    void testSetGetEstado() {
        // Arrange
        String estado = "SP";

        // Act
        candidato.setEstado(estado);

        // Assert
        assertThat(candidato.getEstado()).isEqualTo(estado);
    }

    @Test
    @DisplayName("Deve definir e obter bairro")
    void testSetGetBairro() {
        // Arrange
        String bairro = "Centro";

        // Act
        candidato.setBairro(bairro);

        // Assert
        assertThat(candidato.getBairro()).isEqualTo(bairro);
    }

    @Test
    @DisplayName("Deve definir e obter telefone residencial")
    void testSetGetFoneResidencial() {
        // Arrange
        String foneResidencial = "11 1234-5678";

        // Act
        candidato.setFoneResidencial(foneResidencial);

        // Assert
        assertThat(candidato.getFoneResidencial()).isEqualTo(foneResidencial);
    }

    @Test
    @DisplayName("Deve definir e obter data de nascimento")
    void testSetGetDataNascimento() {
        // Arrange
        LocalDate dataNascimento = LocalDate.of(1980, 5, 15);

        // Act
        candidato.setDataNascimento(dataNascimento);

        // Assert
        assertThat(candidato.getDataNascimento()).isEqualTo(dataNascimento);
    }

    @Test
    @DisplayName("Deve definir e obter idade")
    void testSetGetIdade() {
        // Arrange
        int idade = 43;

        // Act
        candidato.setIdade(idade);

        // Assert
        assertThat(candidato.getIdade()).isEqualTo(idade);
    }

    @Test
    @DisplayName("Deve definir e obter estado civil")
    void testSetGetEstadoCivil() {
        // Arrange
        String estadoCivil = "Casado";

        // Act
        candidato.setEstadoCivil(estadoCivil);

        // Assert
        assertThat(candidato.getEstadoCivil()).isEqualTo(estadoCivil);
    }

    @Test
    @DisplayName("Deve definir e obter esposa")
    void testSetGetEsposa() {
        // Arrange
        String esposa = "Maria Silva";

        // Act
        candidato.setEsposa(esposa);

        // Assert
        assertThat(candidato.getEsposa()).isEqualTo(esposa);
    }

    @Test
    @DisplayName("Deve definir e obter profissão")
    void testSetGetProfissao() {
        // Arrange
        String profissao = "Engenheiro";

        // Act
        candidato.setProfissao(profissao);

        // Assert
        assertThat(candidato.getProfissao()).isEqualTo(profissao);
    }

    @Test
    @DisplayName("Deve definir e obter função")
    void testSetGetFuncao() {
        // Arrange
        String funcao = "Gerente de Projetos";

        // Act
        candidato.setFuncao(funcao);

        // Assert
        assertThat(candidato.getFuncao()).isEqualTo(funcao);
    }

    @Test
    @DisplayName("Deve definir e obter local de trabalho")
    void testSetGetLocalTrabalho() {
        // Arrange
        String localTrabalho = "Empresa ABC Ltda";

        // Act
        candidato.setLocalTrabalho(localTrabalho);

        // Assert
        assertThat(candidato.getLocalTrabalho()).isEqualTo(localTrabalho);
    }

    @Test
    @DisplayName("Deve definir e obter onde exerce")
    void testSetGetOndeExerce() {
        // Arrange
        String ondeExerce = "Sede da empresa";

        // Act
        candidato.setOndeExerce(ondeExerce);

        // Assert
        assertThat(candidato.getOndeExerce()).isEqualTo(ondeExerce);
    }

    @Test
    @DisplayName("Deve definir e obter informações")
    void testSetGetInformacoes() {
        // Arrange
        String informacoes = "Candidato recomendado por irmão";

        // Act
        candidato.setInformacoes(informacoes);

        // Assert
        assertThat(candidato.getInformacoes()).isEqualTo(informacoes);
    }

    @Test
    @DisplayName("Deve definir e obter chanceler")
    void testSetGetChanceler() {
        // Arrange
        String chanceler = "João Costa";

        // Act
        candidato.setChanceler(chanceler);

        // Assert
        assertThat(candidato.getChanceler()).isEqualTo(chanceler);
    }

    @Test
    @DisplayName("Deve definir e obter venerável")
    void testSetGetVeneravel() {
        // Arrange
        String veneravel = "Pedro Silva";

        // Act
        candidato.setVeneravel(veneravel);

        // Assert
        assertThat(candidato.getVeneravel()).isEqualTo(veneravel);
    }

    @Test
    @DisplayName("Deve definir e obter secretário")
    void testSetGetSecretario() {
        // Arrange
        String secretario = "Carlos Santos";

        // Act
        candidato.setSecretario(secretario);

        // Assert
        assertThat(candidato.getSecretario()).isEqualTo(secretario);
    }

    @Test
    @DisplayName("Deve definir e obter linha negra")
    void testSetGetLinhaNegra() {
        // Arrange
        String linhaNegra = "Nenhuma informação relevante";

        // Act
        candidato.setLinhaNegra(linhaNegra);

        // Assert
        assertThat(candidato.getLinhaNegra()).isEqualTo(linhaNegra);
    }

    @Test
    @DisplayName("Deve definir e obter status")
    void testSetGetStatus() {
        // Arrange
        String status = "APROVADO";

        // Act
        candidato.setStatus(status);

        // Assert
        assertThat(candidato.getStatus()).isEqualTo(status);
    }

    @Test
    @DisplayName("Deve definir e obter data de cadastro")
    void testSetGetDataCadastro() {
        // Arrange
        LocalDate dataCadastro = LocalDate.of(2024, 1, 15);

        // Act
        candidato.setDataCadastro(dataCadastro);

        // Assert
        assertThat(candidato.getDataCadastro()).isEqualTo(dataCadastro);
    }

    @Test
    @DisplayName("Deve definir e obter data de status")
    void testSetGetDataStatus() {
        // Arrange
        LocalDate dataStatus = LocalDate.of(2024, 2, 1);

        // Act
        candidato.setDataStatus(dataStatus);

        // Assert
        assertThat(candidato.getDataStatus()).isEqualTo(dataStatus);
    }

    @Test
    @DisplayName("Deve definir e obter observações")
    void testSetGetObservacoes() {
        // Arrange
        String observacoes = "Candidato com bom perfil";

        // Act
        candidato.setObservacoes(observacoes);

        // Assert
        assertThat(candidato.getObservacoes()).isEqualTo(observacoes);
    }

    @Test
    @DisplayName("Deve representar objeto como string")
    void testToString() {
        // Arrange
        candidato.setId(123L);
        candidato.setNome("João da Silva");
        candidato.setStatus("CANDIDATO");

        // Act
        String resultado = candidato.toString();

        // Assert - Simplificado para usar SimpleModel
        assertThat(resultado).contains("Candidato");
        assertThat(resultado).contains("id=123");
    }

    @Test
    @DisplayName("Deve comparar objetos iguais")
    void testEquals() {
        // Arrange
        Candidato candidato1 = new Candidato();
        candidato1.setId(123L);

        Candidato candidato2 = new Candidato();
        candidato2.setId(123L);

        Candidato candidato3 = new Candidato();
        candidato3.setId(456L);

        // Act & Assert
        assertThat(candidato1).isEqualTo(candidato2);
        assertThat(candidato1).isNotEqualTo(candidato3);
        assertThat(candidato1).isNotEqualTo(null);
        assertThat(candidato1).isNotEqualTo("string");
    }

    @Test
    @DisplayName("Deve ter mesmo hashCode para IDs iguais")
    void testHashCode() {
        // Arrange
        Candidato candidato1 = new Candidato();
        candidato1.setId(123L);

        Candidato candidato2 = new Candidato();
        candidato2.setId(123L);

        Candidato candidato3 = new Candidato();
        candidato3.setId(456L);

        // Act & Assert
        assertThat(candidato1.hashCode()).isEqualTo(candidato2.hashCode());
        assertThat(candidato1.hashCode()).isNotEqualTo(candidato3.hashCode());
    }

    @Test
    @DisplayName("Deve lidar com diferentes status")
    void testDiferentesStatus() {
        // Arrange & Act
        candidato.setStatus("CANDIDATO");
        assertThat(candidato.getStatus()).isEqualTo("CANDIDATO");

        candidato.setStatus("INICIADO");
        assertThat(candidato.getStatus()).isEqualTo("INICIADO");

        candidato.setStatus("REJEITADO");
        assertThat(candidato.getStatus()).isEqualTo("REJEITADO");

        candidato.setStatus("APROVADO");
        assertThat(candidato.getStatus()).isEqualTo("APROVADO");
    }

    @Test
    @DisplayName("Deve lidar com diferentes estados civis")
    void testDiferentesEstadosCivis() {
        // Arrange & Act
        candidato.setEstadoCivil("Solteiro");
        assertThat(candidato.getEstadoCivil()).isEqualTo("Solteiro");

        candidato.setEstadoCivil("Casado");
        assertThat(candidato.getEstadoCivil()).isEqualTo("Casado");

        candidato.setEstadoCivil("Divorciado");
        assertThat(candidato.getEstadoCivil()).isEqualTo("Divorciado");

        candidato.setEstadoCivil("Viúvo");
        assertThat(candidato.getEstadoCivil()).isEqualTo("Viúvo");
    }

    @Test
    @DisplayName("Deve lidar com idade válida")
    void testIdadeValida() {
        // Arrange & Act
        candidato.setIdade(18);
        assertThat(candidato.getIdade()).isEqualTo(18);

        candidato.setIdade(25);
        assertThat(candidato.getIdade()).isEqualTo(25);

        candidato.setIdade(50);
        assertThat(candidato.getIdade()).isEqualTo(50);

        candidato.setIdade(70);
        assertThat(candidato.getIdade()).isEqualTo(70);
    }

    @Test
    @DisplayName("Deve lidar com data de nascimento e idade")
    void testDataNascimentoIdade() {
        // Arrange
        LocalDate dataNascimento = LocalDate.of(1980, 1, 1);
        int idadeEsperada = LocalDate.now().getYear() - 1980;

        // Act
        candidato.setDataNascimento(dataNascimento);
        candidato.setIdade(idadeEsperada);

        // Assert
        assertThat(candidato.getDataNascimento()).isEqualTo(dataNascimento);
        assertThat(candidato.getIdade()).isEqualTo(idadeEsperada);
    }

    @Test
    @DisplayName("Deve lidar com valores nulos")
    void testValoresNulos() {
        // Act
        candidato.setNome(null);
        candidato.setEndereco(null);
        candidato.setFoneResidencial(null);
        candidato.setEsposa(null);
        candidato.setProfissao(null);
        candidato.setFuncao(null);
        candidato.setLocalTrabalho(null);
        candidato.setOndeExerce(null);
        candidato.setInformacoes(null);
        candidato.setChanceler(null);
        candidato.setVeneravel(null);
        candidato.setSecretario(null);
        candidato.setLinhaNegra(null);
        candidato.setStatus(null);
        candidato.setDataCadastro(null);
        candidato.setDataStatus(null);
        candidato.setObservacoes(null);

        // Assert
        assertThat(candidato.getNome()).isNull();
        assertThat(candidato.getEndereco()).isNull();
        assertThat(candidato.getFoneResidencial()).isNull();
        assertThat(candidato.getEsposa()).isNull();
        assertThat(candidato.getProfissao()).isNull();
        assertThat(candidato.getFuncao()).isNull();
        assertThat(candidato.getLocalTrabalho()).isNull();
        assertThat(candidato.getOndeExerce()).isNull();
        assertThat(candidato.getInformacoes()).isNull();
        assertThat(candidato.getChanceler()).isNull();
        assertThat(candidato.getVeneravel()).isNull();
        assertThat(candidato.getSecretario()).isNull();
        assertThat(candidato.getLinhaNegra()).isNull();
        assertThat(candidato.getStatus()).isNull();
        assertThat(candidato.getDataCadastro()).isNull();
        assertThat(candidato.getDataStatus()).isNull();
        assertThat(candidato.getObservacoes()).isNull();
    }

    @Test
    @DisplayName("Deve lidar com valores vazios")
    void testValoresVazios() {
        // Act
        candidato.setNome("");
        candidato.setEndereco("");
        candidato.setNumero("");
        candidato.setCidade("");
        candidato.setEstado("");
        candidato.setBairro("");
        candidato.setFoneResidencial("");
        candidato.setEstadoCivil("");
        candidato.setEsposa("");
        candidato.setProfissao("");
        candidato.setFuncao("");
        candidato.setLocalTrabalho("");
        candidato.setOndeExerce("");
        candidato.setInformacoes("");
        candidato.setChanceler("");
        candidato.setVeneravel("");
        candidato.setSecretario("");
        candidato.setLinhaNegra("");
        candidato.setStatus("");
        candidato.setObservacoes("");

        // Assert
        assertThat(candidato.getNome()).isEmpty();
        assertThat(candidato.getEndereco()).isEmpty();
        assertThat(candidato.getNumero()).isEmpty();
        assertThat(candidato.getCidade()).isEmpty();
        assertThat(candidato.getEstado()).isEmpty();
        assertThat(candidato.getBairro()).isEmpty();
        assertThat(candidato.getFoneResidencial()).isEmpty();
        assertThat(candidato.getEstadoCivil()).isEmpty();
        assertThat(candidato.getEsposa()).isEmpty();
        assertThat(candidato.getProfissao()).isEmpty();
        assertThat(candidato.getFuncao()).isEmpty();
        assertThat(candidato.getLocalTrabalho()).isEmpty();
        assertThat(candidato.getOndeExerce()).isEmpty();
        assertThat(candidato.getInformacoes()).isEmpty();
        assertThat(candidato.getChanceler()).isEmpty();
        assertThat(candidato.getVeneravel()).isEmpty();
        assertThat(candidato.getSecretario()).isEmpty();
        assertThat(candidato.getLinhaNegra()).isEmpty();
        assertThat(candidato.getStatus()).isEmpty();
        assertThat(candidato.getObservacoes()).isEmpty();
    }

    @Test
    @DisplayName("Deve lidar com datas futuras e passadas")
    void testDatasFuturasPassadas() {
        // Arrange
        LocalDate hoje = LocalDate.now();
        LocalDate dataFutura = hoje.plusDays(30);
        LocalDate dataPassada = hoje.minusDays(30);

        // Act
        candidato.setDataCadastro(dataFutura);
        candidato.setDataStatus(dataPassada);

        // Assert
        assertThat(candidato.getDataCadastro()).isEqualTo(dataFutura);
        assertThat(candidato.getDataCadastro()).isAfter(hoje);
        assertThat(candidato.getDataStatus()).isEqualTo(dataPassada);
        assertThat(candidato.getDataStatus()).isBefore(hoje);
    }

    @Test
    @DisplayName("Deve lidar com idade zero")
    void testIdadeZero() {
        // Act
        candidato.setIdade(0);

        // Assert
        assertThat(candidato.getIdade()).isEqualTo(0);
    }

    @Test
    @DisplayName("Deve lidar com idade negativa")
    void testIdadeNegativa() {
        // Act
        candidato.setIdade(-1);

        // Assert
        assertThat(candidato.getIdade()).isEqualTo(-1);
    }

    @Test
    @DisplayName("Deve lidar com idade avançada")
    void testIdadeAvancada() {
        // Act
        candidato.setIdade(100);

        // Assert
        assertThat(candidato.getIdade()).isEqualTo(100);
    }
}
