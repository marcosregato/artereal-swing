package com.artereal.swing.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes unitários para a classe Loja
 */
@DisplayName("Testes Unitários - Loja")
class LojaTest {

    private Loja loja;

    @BeforeEach
    void setUp() {
        loja = new Loja();
    }

    @Test
    @DisplayName("Deve criar loja com construtor padrão")
    void testConstrutorPadrao() {
        // Act
        Loja novaLoja = new Loja();

        // Assert
        assertThat(novaLoja).isNotNull();
        assertThat(novaLoja.getId()).isNull();
        assertThat(novaLoja.getNome()).isNull();
        assertThat(novaLoja.getNumero()).isNull();
        assertThat(novaLoja.getEndereco()).isNull();
        assertThat(novaLoja.getBairro()).isNull();
        assertThat(novaLoja.getCidade()).isNull();
        assertThat(novaLoja.getEstado()).isNull();
        assertThat(novaLoja.getCep()).isNull();
        assertThat(novaLoja.getTelefone()).isNull();
        assertThat(novaLoja.getEmail()).isNull();
        assertThat(novaLoja.getPresidente()).isNull();
        assertThat(novaLoja.getSecretario()).isNull();
        assertThat(novaLoja.getTesoureiro()).isNull();
        assertThat(novaLoja.getDataFundacao()).isNull();
        assertThat(novaLoja.getRito()).isNull();
        assertThat(novaLoja.getPotencia()).isNull();
        assertThat(novaLoja.getObservacoes()).isNull();
        assertThat(novaLoja.getStatus()).isNull();
        assertThat(novaLoja.getCreatedAt()).isNull();
        assertThat(novaLoja.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("Deve criar loja com construtor parametrizado")
    void testConstrutorParametrizado() {
        // Arrange
        String nome = "ArteReal";
        String numero = "123";

        // Act
        Loja novaLoja = new Loja(nome, numero);

        // Assert
        assertThat(novaLoja).isNotNull();
        assertThat(novaLoja.getNome()).isEqualTo(nome);
        assertThat(novaLoja.getNumero()).isEqualTo(numero);
        assertThat(novaLoja.getStatus()).isEqualTo("ATIVA");
    }

    @Test
    @DisplayName("Deve definir e obter ID")
    void testSetGetId() {
        // Arrange
        Long id = 123L;

        // Act
        loja.setId(id);

        // Assert
        assertThat(loja.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Deve definir e obter nome")
    void testSetGetNome() {
        // Arrange
        String nome = "Loja Maçônica ArteReal";

        // Act
        loja.setNome(nome);

        // Assert
        assertThat(loja.getNome()).isEqualTo(nome);
    }

    @Test
    @DisplayName("Deve definir e obter número")
    void testSetGetNumero() {
        // Arrange
        String numero = "123";

        // Act
        loja.setNumero(numero);

        // Assert
        assertThat(loja.getNumero()).isEqualTo(numero);
    }

    @Test
    @DisplayName("Deve definir e obter endereço")
    void testSetGetEndereco() {
        // Arrange
        String endereco = "Rua das Acácias, 123";

        // Act
        loja.setEndereco(endereco);

        // Assert
        assertThat(loja.getEndereco()).isEqualTo(endereco);
    }

    @Test
    @DisplayName("Deve definir e obter bairro")
    void testSetGetBairro() {
        // Arrange
        String bairro = "Centro";

        // Act
        loja.setBairro(bairro);

        // Assert
        assertThat(loja.getBairro()).isEqualTo(bairro);
    }

    @Test
    @DisplayName("Deve definir e obter cidade")
    void testSetGetCidade() {
        // Arrange
        String cidade = "São Paulo";

        // Act
        loja.setCidade(cidade);

        // Assert
        assertThat(loja.getCidade()).isEqualTo(cidade);
    }

    @Test
    @DisplayName("Deve definir e obter estado")
    void testSetGetEstado() {
        // Arrange
        String estado = "SP";

        // Act
        loja.setEstado(estado);

        // Assert
        assertThat(loja.getEstado()).isEqualTo(estado);
    }

    @Test
    @DisplayName("Deve definir e obter CEP")
    void testSetGetCep() {
        // Arrange
        String cep = "01234-567";

        // Act
        loja.setCep(cep);

        // Assert
        assertThat(loja.getCep()).isEqualTo(cep);
    }

    @Test
    @DisplayName("Deve definir e obter telefone")
    void testSetGetTelefone() {
        // Arrange
        String telefone = "(11) 1234-5678";

        // Act
        loja.setTelefone(telefone);

        // Assert
        assertThat(loja.getTelefone()).isEqualTo(telefone);
    }

    @Test
    @DisplayName("Deve definir e obter email")
    void testSetGetEmail() {
        // Arrange
        String email = "artereal123@obsp.org.br";

        // Act
        loja.setEmail(email);

        // Assert
        assertThat(loja.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("Deve definir e obter presidente")
    void testSetGetPresidente() {
        // Arrange
        String presidente = "João da Silva";

        // Act
        loja.setPresidente(presidente);

        // Assert
        assertThat(loja.getPresidente()).isEqualTo(presidente);
    }

    @Test
    @DisplayName("Deve definir e obter secretário")
    void testSetGetSecretario() {
        // Arrange
        String secretario = "Pedro Santos";

        // Act
        loja.setSecretario(secretario);

        // Assert
        assertThat(loja.getSecretario()).isEqualTo(secretario);
    }

    @Test
    @DisplayName("Deve definir e obter tesoureiro")
    void testSetGetTesoureiro() {
        // Arrange
        String tesoureiro = "Carlos Oliveira";

        // Act
        loja.setTesoureiro(tesoureiro);

        // Assert
        assertThat(loja.getTesoureiro()).isEqualTo(tesoureiro);
    }

    @Test
    @DisplayName("Deve definir e obter data de fundação")
    void testSetGetDataFundacao() {
        // Arrange
        String dataFundacao = "15/01/1920";

        // Act
        loja.setDataFundacao(dataFundacao);

        // Assert
        assertThat(loja.getDataFundacao()).isEqualTo(dataFundacao);
    }

    @Test
    @DisplayName("Deve definir e obter rito")
    void testSetGetRito() {
        // Arrange
        String rito = "Escocês Antigo e Aceito";

        // Act
        loja.setRito(rito);

        // Assert
        assertThat(loja.getRito()).isEqualTo(rito);
    }

    @Test
    @DisplayName("Deve definir e obter potência")
    void testSetGetPotencia() {
        // Arrange
        String potencia = "Grande Oriente do Brasil";

        // Act
        loja.setPotencia(potencia);

        // Assert
        assertThat(loja.getPotencia()).isEqualTo(potencia);
    }

    @Test
    @DisplayName("Deve definir e obter observações")
    void testSetGetObservacoes() {
        // Arrange
        String observacoes = "Loja fundada em 1920, tradicional";

        // Act
        loja.setObservacoes(observacoes);

        // Assert
        assertThat(loja.getObservacoes()).isEqualTo(observacoes);
    }

    @Test
    @DisplayName("Deve definir e obter status")
    void testSetGetStatus() {
        // Arrange
        String status = "INATIVA";

        // Act
        loja.setStatus(status);

        // Assert
        assertThat(loja.getStatus()).isEqualTo(status);
    }

    @Test
    @DisplayName("Deve definir e obter created at")
    void testSetGetCreatedAt() {
        // Arrange
        String createdAt = "2024-01-15 10:30:00";

        // Act
        loja.setCreatedAt(createdAt);

        // Assert
        assertThat(loja.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Deve definir e obter updated at")
    void testSetGetUpdatedAt() {
        // Arrange
        String updatedAt = "2024-01-16 11:00:00";

        // Act
        loja.setUpdatedAt(updatedAt);

        // Assert
        assertThat(loja.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("Deve representar objeto como string")
    void testToString() {
        // Arrange
        loja.setId(123L);
        loja.setNome("ArteReal");
        loja.setNumero("123");
        loja.setCidade("São Paulo");

        // Act
        String resultado = loja.toString();

        // Assert - Simplificado para usar SimpleModel
        assertThat(resultado).contains("Loja");
        assertThat(resultado).contains("id=123");
    }

    @Test
    @DisplayName("Deve comparar objetos iguais")
    void testEquals() {
        // Arrange
        Loja loja1 = new Loja();
        loja1.setId(123L);

        Loja loja2 = new Loja();
        loja2.setId(123L);

        Loja loja3 = new Loja();
        loja3.setId(456L);

        // Act & Assert
        assertThat(loja1).isEqualTo(loja2);
        assertThat(loja1).isNotEqualTo(loja3);
        assertThat(loja1).isNotEqualTo(null);
        assertThat(loja1).isNotEqualTo("string");
    }

    @Test
    @DisplayName("Deve ter mesmo hashCode para IDs iguais")
    void testHashCode() {
        // Arrange
        Loja loja1 = new Loja();
        loja1.setId(123L);

        Loja loja2 = new Loja();
        loja2.setId(123L);

        Loja loja3 = new Loja();
        loja3.setId(456L);

        // Act & Assert
        assertThat(loja1.hashCode()).isEqualTo(loja2.hashCode());
        assertThat(loja1.hashCode()).isNotEqualTo(loja3.hashCode());
    }

    @Test
    @DisplayName("Deve lidar com diferentes status")
    void testDiferentesStatus() {
        // Arrange & Act
        loja.setStatus("ATIVA");
        assertThat(loja.getStatus()).isEqualTo("ATIVA");

        loja.setStatus("INATIVA");
        assertThat(loja.getStatus()).isEqualTo("INATIVA");

        loja.setStatus("SUSPENSA");
        assertThat(loja.getStatus()).isEqualTo("SUSPENSA");
    }

    @Test
    @DisplayName("Deve lidar com diferentes ritos")
    void testDiferentesRitos() {
        // Arrange & Act
        loja.setRito("Escocês Antigo e Aceito");
        assertThat(loja.getRito()).isEqualTo("Escocês Antigo e Aceito");

        loja.setRito("York");
        assertThat(loja.getRito()).isEqualTo("York");

        loja.setRito("Francês");
        assertThat(loja.getRito()).isEqualTo("Francês");

        loja.setRito("Adoniramita");
        assertThat(loja.getRito()).isEqualTo("Adoniramita");
    }

    @Test
    @DisplayName("Deve lidar com diferentes potências")
    void testDiferentesPotencias() {
        // Arrange & Act
        loja.setPotencia("Grande Oriente do Brasil");
        assertThat(loja.getPotencia()).isEqualTo("Grande Oriente do Brasil");

        loja.setPotencia("Grande Loja Maçônica do Estado de São Paulo");
        assertThat(loja.getPotencia()).isEqualTo("Grande Loja Maçônica do Estado de São Paulo");

        loja.setPotencia("Grande Oriente de Portugal");
        assertThat(loja.getPotencia()).isEqualTo("Grande Oriente de Portugal");
    }

    @Test
    @DisplayName("Deve lidar com valores nulos")
    void testValoresNulos() {
        // Act
        loja.setNome(null);
        loja.setNumero(null);
        loja.setEndereco(null);
        loja.setBairro(null);
        loja.setCidade(null);
        loja.setEstado(null);
        loja.setCep(null);
        loja.setTelefone(null);
        loja.setEmail(null);
        loja.setPresidente(null);
        loja.setSecretario(null);
        loja.setTesoureiro(null);
        loja.setDataFundacao(null);
        loja.setRito(null);
        loja.setPotencia(null);
        loja.setObservacoes(null);
        loja.setStatus(null);
        loja.setCreatedAt(null);
        loja.setUpdatedAt(null);

        // Assert
        assertThat(loja.getNome()).isNull();
        assertThat(loja.getNumero()).isNull();
        assertThat(loja.getEndereco()).isNull();
        assertThat(loja.getBairro()).isNull();
        assertThat(loja.getCidade()).isNull();
        assertThat(loja.getEstado()).isNull();
        assertThat(loja.getCep()).isNull();
        assertThat(loja.getTelefone()).isNull();
        assertThat(loja.getEmail()).isNull();
        assertThat(loja.getPresidente()).isNull();
        assertThat(loja.getSecretario()).isNull();
        assertThat(loja.getTesoureiro()).isNull();
        assertThat(loja.getDataFundacao()).isNull();
        assertThat(loja.getRito()).isNull();
        assertThat(loja.getPotencia()).isNull();
        assertThat(loja.getObservacoes()).isNull();
        assertThat(loja.getStatus()).isNull();
        assertThat(loja.getCreatedAt()).isNull();
        assertThat(loja.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("Deve lidar com valores vazios")
    void testValoresVazios() {
        // Act
        loja.setNome("");
        loja.setNumero("");
        loja.setEndereco("");
        loja.setBairro("");
        loja.setCidade("");
        loja.setEstado("");
        loja.setCep("");
        loja.setTelefone("");
        loja.setEmail("");
        loja.setPresidente("");
        loja.setSecretario("");
        loja.setTesoureiro("");
        loja.setDataFundacao("");
        loja.setRito("");
        loja.setPotencia("");
        loja.setObservacoes("");
        loja.setStatus("");
        loja.setCreatedAt("");
        loja.setUpdatedAt("");

        // Assert
        assertThat(loja.getNome()).isEmpty();
        assertThat(loja.getNumero()).isEmpty();
        assertThat(loja.getEndereco()).isEmpty();
        assertThat(loja.getBairro()).isEmpty();
        assertThat(loja.getCidade()).isEmpty();
        assertThat(loja.getEstado()).isEmpty();
        assertThat(loja.getCep()).isEmpty();
        assertThat(loja.getTelefone()).isEmpty();
        assertThat(loja.getEmail()).isEmpty();
        assertThat(loja.getPresidente()).isEmpty();
        assertThat(loja.getSecretario()).isEmpty();
        assertThat(loja.getTesoureiro()).isEmpty();
        assertThat(loja.getDataFundacao()).isEmpty();
        assertThat(loja.getRito()).isEmpty();
        assertThat(loja.getPotencia()).isEmpty();
        assertThat(loja.getObservacoes()).isEmpty();
        assertThat(loja.getStatus()).isEmpty();
        assertThat(loja.getCreatedAt()).isEmpty();
        assertThat(loja.getUpdatedAt()).isEmpty();
    }

    @Test
    @DisplayName("Deve lidar com diferentes formatos de telefone")
    void testDiferentesFormatosTelefone() {
        // Arrange & Act
        loja.setTelefone("(11) 1234-5678");
        assertThat(loja.getTelefone()).isEqualTo("(11) 1234-5678");

        loja.setTelefone("11912345678");
        assertThat(loja.getTelefone()).isEqualTo("11912345678");

        loja.setTelefone("+55 11 91234-5678");
        assertThat(loja.getTelefone()).isEqualTo("+55 11 91234-5678");

        loja.setTelefone("0800-123-4567");
        assertThat(loja.getTelefone()).isEqualTo("0800-123-4567");
    }

    @Test
    @DisplayName("Deve lidar com diferentes formatos de CEP")
    void testDiferentesFormatosCep() {
        // Arrange & Act
        loja.setCep("01234-567");
        assertThat(loja.getCep()).isEqualTo("01234-567");

        loja.setCep("01234567");
        assertThat(loja.getCep()).isEqualTo("01234567");

        loja.setCep("12345-678");
        assertThat(loja.getCep()).isEqualTo("12345-678");

        loja.setCep("12345678");
        assertThat(loja.getCep()).isEqualTo("12345678");
    }

    @Test
    @DisplayName("Deve lidar com diferentes formatos de email")
    void testDiferentesFormatosEmail() {
        // Arrange & Act
        loja.setEmail("artereal123@obsp.org.br");
        assertThat(loja.getEmail()).isEqualTo("artereal123@obsp.org.br");

        loja.setEmail("loja.artereal@gmail.com");
        assertThat(loja.getEmail()).isEqualTo("loja.artereal@gmail.com");

        loja.setEmail("contato@artereal.com.br");
        assertThat(loja.getEmail()).isEqualTo("contato@artereal.com.br");

        loja.setEmail("secretaria@loja123.org");
        assertThat(loja.getEmail()).isEqualTo("secretaria@loja123.org");
    }

    @Test
    @DisplayName("Deve lidar com diferentes formatos de data de fundação")
    void testDiferentesFormatosDataFundacao() {
        // Arrange & Act
        loja.setDataFundacao("15/01/1920");
        assertThat(loja.getDataFundacao()).isEqualTo("15/01/1920");

        loja.setDataFundacao("1920-01-15");
        assertThat(loja.getDataFundacao()).isEqualTo("1920-01-15");

        loja.setDataFundacao("15 de janeiro de 1920");
        assertThat(loja.getDataFundacao()).isEqualTo("15 de janeiro de 1920");

        loja.setDataFundacao("Janeiro/1920");
        assertThat(loja.getDataFundacao()).isEqualTo("Janeiro/1920");
    }

    @Test
    @DisplayName("Deve lidar com diferentes estados")
    void testDiferentesEstados() {
        // Arrange & Act
        loja.setEstado("SP");
        assertThat(loja.getEstado()).isEqualTo("SP");

        loja.setEstado("RJ");
        assertThat(loja.getEstado()).isEqualTo("RJ");

        loja.setEstado("MG");
        assertThat(loja.getEstado()).isEqualTo("MG");

        loja.setEstado("RS");
        assertThat(loja.getEstado()).isEqualTo("RS");

        loja.setEstado("PR");
        assertThat(loja.getEstado()).isEqualTo("PR");
    }

    @Test
    @DisplayName("Deve lidar com diferentes números de loja")
    void testDiferentesNumerosLoja() {
        // Arrange & Act
        loja.setNumero("123");
        assertThat(loja.getNumero()).isEqualTo("123");

        loja.setNumero("456");
        assertThat(loja.getNumero()).isEqualTo("456");

        loja.setNumero("789");
        assertThat(loja.getNumero()).isEqualTo("789");

        loja.setNumero("1000");
        assertThat(loja.getNumero()).isEqualTo("1000");
    }

    @Test
    @DisplayName("Deve lidar com endereços complexos")
    void testEnderecosComplexos() {
        // Arrange & Act
        loja.setEndereco("Rua das Acácias, 123, apto 45");
        assertThat(loja.getEndereco()).isEqualTo("Rua das Acácias, 123, apto 45");

        loja.setEndereco("Avenida Principal, 1000, sala 201");
        assertThat(loja.getEndereco()).isEqualTo("Avenida Principal, 1000, sala 201");

        loja.setEndereco("Praça da Maçonaria, s/nº");
        assertThat(loja.getEndereco()).isEqualTo("Praça da Maçonaria, s/nº");

        loja.setEndereco("Alameda dos Simbolos, 500, bloco B");
        assertThat(loja.getEndereco()).isEqualTo("Alameda dos Simbolos, 500, bloco B");
    }

    @Test
    @DisplayName("Deve lidar com diferentes bairros")
    void testDiferentesBairros() {
        // Arrange & Act
        loja.setBairro("Centro");
        assertThat(loja.getBairro()).isEqualTo("Centro");

        loja.setBairro("Liberdade");
        assertThat(loja.getBairro()).isEqualTo("Liberdade");

        loja.setBairro("Consolação");
        assertThat(loja.getBairro()).isEqualTo("Consolação");

        loja.setBairro("Santo Amaro");
        assertThat(loja.getBairro()).isEqualTo("Santo Amaro");
    }

    @Test
    @DisplayName("Deve lidar com observações longas")
    void testObservacoesLongas() {
        // Arrange
        String observacoesLongas = "Loja fundada em 1920 por um grupo de maçons dedicados, tradicional na cidade, com grande atuação comunitária e filantrópica. Sediada em prédio próprio, com templo construído em 1950, mantém atividades regulares de rituais, estudos e ações sociais.";

        // Act
        loja.setObservacoes(observacoesLongas);

        // Assert
        assertThat(loja.getObservacoes()).isEqualTo(observacoesLongas);
        assertThat(loja.getObservacoes()).hasSizeGreaterThan(50);
    }

    @Test
    @DisplayName("Deve lidar com ID zero")
    void testIdZero() {
        // Act
        loja.setId(0L);

        // Assert
        assertThat(loja.getId()).isEqualTo(0L);
    }

    @Test
    @DisplayName("Deve lidar com ID negativo")
    void testIdNegativo() {
        // Act
        loja.setId(-1L);

        // Assert
        assertThat(loja.getId()).isEqualTo(-1L);
        assertThat(loja.getId()).isNegative();
    }

    @Test
    @DisplayName("Deve lidar com nomes de loja com caracteres especiais")
    void testNomeLojaCaracteresEspeciais() {
        // Arrange
        String nomeComEspeciais = "Árvore da Vida - Nº 456";

        // Act
        loja.setNome(nomeComEspeciais);

        // Assert
        assertThat(loja.getNome()).isEqualTo(nomeComEspeciais);
        assertThat(loja.getNome()).contains("Árvore");
        assertThat(loja.getNome()).contains("456");
    }

    @Test
    @DisplayName("Deve lidar com diferentes cargos")
    void testDiferentesCargos() {
        // Arrange & Act
        loja.setPresidente("João da Silva");
        loja.setSecretario("Pedro Santos");
        loja.setTesoureiro("Carlos Oliveira");

        // Assert
        assertThat(loja.getPresidente()).isEqualTo("João da Silva");
        assertThat(loja.getSecretario()).isEqualTo("Pedro Santos");
        assertThat(loja.getTesoureiro()).isEqualTo("Carlos Oliveira");

        // Teste com outros nomes
        loja.setPresidente("Antonio Costa");
        loja.setSecretario("Francisco Lima");
        loja.setTesoureiro("José Almeida");

        assertThat(loja.getPresidente()).isEqualTo("Antonio Costa");
        assertThat(loja.getSecretario()).isEqualTo("Francisco Lima");
        assertThat(loja.getTesoureiro()).isEqualTo("José Almeida");
    }

    @Test
    @DisplayName("Deve lidar com status default no construtor parametrizado")
    void testStatusDefaultConstrutorParametrizado() {
        // Act
        Loja novaLoja = new Loja("Teste", "999");

        // Assert
        assertThat(novaLoja.getStatus()).isEqualTo("ATIVA");
    }
}
