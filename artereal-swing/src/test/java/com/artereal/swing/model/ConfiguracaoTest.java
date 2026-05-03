package com.artereal.swing.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes unitários para a classe Configuracao
 */
@DisplayName("Testes Unitários - Configuracao")
class ConfiguracaoTest {

    private Configuracao configuracao;

    @BeforeEach
    void setUp() {
        configuracao = new Configuracao();
    }

    @Test
    @DisplayName("Deve criar configuração com construtor padrão")
    void testConstrutorPadrao() {
        // Act
        Configuracao novaConfiguracao = new Configuracao();

        // Assert
        assertThat(novaConfiguracao).isNotNull();
        assertThat(novaConfiguracao.getId()).isNull();
        assertThat(novaConfiguracao.getChave()).isNull();
        assertThat(novaConfiguracao.getValor()).isNull();
        assertThat(novaConfiguracao.getDescricao()).isNull();
        assertThat(novaConfiguracao.getTipo()).isNull();
        assertThat(novaConfiguracao.getCategoria()).isNull();
        assertThat(novaConfiguracao.isEditavel()).isFalse(); // boolean default is false
        assertThat(novaConfiguracao.isVisivel()).isFalse(); // boolean default is false
        assertThat(novaConfiguracao.getDataAtualizacao()).isNull();
        assertThat(novaConfiguracao.getUsuarioAtualizacao()).isNull();
    }

    @Test
    @DisplayName("Deve criar configuração com construtor parametrizado")
    void testConstrutorParametrizado() {
        // Arrange
        String chave = "system.name";
        String valor = "ArteReal System";
        String descricao = "Nome do sistema";
        String tipo = "STRING";
        String categoria = "SISTEMA";

        // Act
        Configuracao novaConfiguracao = new Configuracao(chave, valor, descricao, tipo, categoria);

        // Assert
        assertThat(novaConfiguracao).isNotNull();
        assertThat(novaConfiguracao.getChave()).isEqualTo(chave);
        assertThat(novaConfiguracao.getValor()).isEqualTo(valor);
        assertThat(novaConfiguracao.getDescricao()).isEqualTo(descricao);
        assertThat(novaConfiguracao.getTipo()).isEqualTo(tipo);
        assertThat(novaConfiguracao.getCategoria()).isEqualTo(categoria);
        assertThat(novaConfiguracao.isEditavel()).isTrue();
        assertThat(novaConfiguracao.isVisivel()).isTrue();
        assertThat(novaConfiguracao.getDataAtualizacao()).isNotNull();
        assertThat(novaConfiguracao.getDataAtualizacao()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    @DisplayName("Deve definir e obter ID")
    void testSetGetId() {
        // Arrange
        Long id = 123L;

        // Act
        configuracao.setId(id);

        // Assert
        assertThat(configuracao.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Deve definir e obter chave")
    void testSetGetChave() {
        // Arrange
        String chave = "database.url";

        // Act
        configuracao.setChave(chave);

        // Assert
        assertThat(configuracao.getChave()).isEqualTo(chave);
    }

    @Test
    @DisplayName("Deve definir e obter valor")
    void testSetGetValor() {
        // Arrange
        String valor = "localhost:5432/artereal_db";

        // Act
        configuracao.setValor(valor);

        // Assert
        assertThat(configuracao.getValor()).isEqualTo(valor);
    }

    @Test
    @DisplayName("Deve definir e obter descrição")
    void testSetGetDescricao() {
        // Arrange
        String descricao = "URL do banco de dados PostgreSQL";

        // Act
        configuracao.setDescricao(descricao);

        // Assert
        assertThat(configuracao.getDescricao()).isEqualTo(descricao);
    }

    @Test
    @DisplayName("Deve definir e obter tipo")
    void testSetGetTipo() {
        // Arrange
        String tipo = "STRING";

        // Act
        configuracao.setTipo(tipo);

        // Assert
        assertThat(configuracao.getTipo()).isEqualTo(tipo);
    }

    @Test
    @DisplayName("Deve definir e obter categoria")
    void testSetGetCategoria() {
        // Arrange
        String categoria = "SISTEMA";

        // Act
        configuracao.setCategoria(categoria);

        // Assert
        assertThat(configuracao.getCategoria()).isEqualTo(categoria);
    }

    @Test
    @DisplayName("Deve definir e obter editável")
    void testSetGetEditavel() {
        // Arrange
        boolean editavel = false;

        // Act
        configuracao.setEditavel(editavel);

        // Assert
        assertThat(configuracao.isEditavel()).isEqualTo(editavel);
    }

    @Test
    @DisplayName("Deve definir e obter visível")
    void testSetGetVisivel() {
        // Arrange
        boolean visivel = false;

        // Act
        configuracao.setVisivel(visivel);

        // Assert
        assertThat(configuracao.isVisivel()).isEqualTo(visivel);
    }

    @Test
    @DisplayName("Deve definir e obter data de atualização")
    void testSetGetDataAtualizacao() {
        // Arrange
        LocalDateTime dataAtualizacao = LocalDateTime.of(2024, 1, 15, 10, 30);

        // Act
        configuracao.setDataAtualizacao(dataAtualizacao);

        // Assert
        assertThat(configuracao.getDataAtualizacao()).isEqualTo(dataAtualizacao);
    }

    @Test
    @DisplayName("Deve definir e obter usuário de atualização")
    void testSetGetUsuarioAtualizacao() {
        // Arrange
        String usuarioAtualizacao = "admin";

        // Act
        configuracao.setUsuarioAtualizacao(usuarioAtualizacao);

        // Assert
        assertThat(configuracao.getUsuarioAtualizacao()).isEqualTo(usuarioAtualizacao);
    }

    @Test
    @DisplayName("Deve representar objeto como string")
    void testToString() {
        // Arrange
        configuracao.setId(123L);
        configuracao.setChave("system.name");
        configuracao.setValor("ArteReal System");
        configuracao.setTipo("STRING");
        configuracao.setCategoria("SISTEMA");

        // Act
        String resultado = configuracao.toString();

        // Assert - Simplificado para usar SimpleModel
        assertThat(resultado).contains("Configuracao");
        assertThat(resultado).contains("id=123");
    }

    @Test
    @DisplayName("Deve comparar objetos iguais")
    void testEquals() {
        // Arrange
        Configuracao configuracao1 = new Configuracao();
        configuracao1.setId(123L);

        Configuracao configuracao2 = new Configuracao();
        configuracao2.setId(123L);

        Configuracao configuracao3 = new Configuracao();
        configuracao3.setId(456L);

        // Act & Assert
        assertThat(configuracao1).isEqualTo(configuracao2);
        assertThat(configuracao1).isNotEqualTo(configuracao3);
        assertThat(configuracao1).isNotEqualTo(null);
        assertThat(configuracao1).isNotEqualTo("string");
    }

    @Test
    @DisplayName("Deve ter mesmo hashCode para IDs iguais")
    void testHashCode() {
        // Arrange
        Configuracao configuracao1 = new Configuracao();
        configuracao1.setId(123L);

        Configuracao configuracao2 = new Configuracao();
        configuracao2.setId(123L);

        Configuracao configuracao3 = new Configuracao();
        configuracao3.setId(456L);

        // Act & Assert
        assertThat(configuracao1.hashCode()).isEqualTo(configuracao2.hashCode());
        assertThat(configuracao1.hashCode()).isNotEqualTo(configuracao3.hashCode());
    }

    @Test
    @DisplayName("Deve lidar com diferentes tipos")
    void testDiferentesTipos() {
        // Arrange & Act
        configuracao.setTipo("STRING");
        assertThat(configuracao.getTipo()).isEqualTo("STRING");

        configuracao.setTipo("NUMBER");
        assertThat(configuracao.getTipo()).isEqualTo("NUMBER");

        configuracao.setTipo("BOOLEAN");
        assertThat(configuracao.getTipo()).isEqualTo("BOOLEAN");

        configuracao.setTipo("DATE");
        assertThat(configuracao.getTipo()).isEqualTo("DATE");
    }

    @Test
    @DisplayName("Deve lidar com diferentes categorias")
    void testDiferentesCategorias() {
        // Arrange & Act
        configuracao.setCategoria("SISTEMA");
        assertThat(configuracao.getCategoria()).isEqualTo("SISTEMA");

        configuracao.setCategoria("FINANCEIRO");
        assertThat(configuracao.getCategoria()).isEqualTo("FINANCEIRO");

        configuracao.setCategoria("USUARIO");
        assertThat(configuracao.getCategoria()).isEqualTo("USUARIO");

        configuracao.setCategoria("RELATORIO");
        assertThat(configuracao.getCategoria()).isEqualTo("RELATORIO");

        configuracao.setCategoria("INTERFACE");
        assertThat(configuracao.getCategoria()).isEqualTo("INTERFACE");
    }

    @Test
    @DisplayName("Deve lidar com valores nulos")
    void testValoresNulos() {
        // Act
        configuracao.setChave(null);
        configuracao.setValor(null);
        configuracao.setDescricao(null);
        configuracao.setTipo(null);
        configuracao.setCategoria(null);
        configuracao.setDataAtualizacao(null);
        configuracao.setUsuarioAtualizacao(null);

        // Assert
        assertThat(configuracao.getChave()).isNull();
        assertThat(configuracao.getValor()).isNull();
        assertThat(configuracao.getDescricao()).isNull();
        assertThat(configuracao.getTipo()).isNull();
        assertThat(configuracao.getCategoria()).isNull();
        assertThat(configuracao.getDataAtualizacao()).isNull();
        assertThat(configuracao.getUsuarioAtualizacao()).isNull();
    }

    @Test
    @DisplayName("Deve lidar com valores vazios")
    void testValoresVazios() {
        // Act
        configuracao.setChave("");
        configuracao.setValor("");
        configuracao.setDescricao("");
        configuracao.setTipo("");
        configuracao.setCategoria("");
        configuracao.setUsuarioAtualizacao("");

        // Assert
        assertThat(configuracao.getChave()).isEmpty();
        assertThat(configuracao.getValor()).isEmpty();
        assertThat(configuracao.getDescricao()).isEmpty();
        assertThat(configuracao.getTipo()).isEmpty();
        assertThat(configuracao.getCategoria()).isEmpty();
        assertThat(configuracao.getUsuarioAtualizacao()).isEmpty();
    }

    @Test
    @DisplayName("Deve lidar com configuração não editável")
    void testConfiguracaoNaoEditavel() {
        // Arrange
        Configuracao configNaoEditavel = new Configuracao("readonly.key", "value", "desc", "STRING", "SISTEMA");
        configNaoEditavel.setEditavel(false);

        // Assert
        assertThat(configNaoEditavel.isEditavel()).isFalse();
    }

    @Test
    @DisplayName("Deve lidar com configuração não visível")
    void testConfiguracaoNaoVisivel() {
        // Arrange
        Configuracao configNaoVisivel = new Configuracao("hidden.key", "value", "desc", "STRING", "SISTEMA");
        configNaoVisivel.setVisivel(false);

        // Assert
        assertThat(configNaoVisivel.isVisivel()).isFalse();
    }

    @Test
    @DisplayName("Deve lidar com data de atualização futura")
    void testDataAtualizacaoFutura() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime dataFutura = agora.plusDays(1);

        // Act
        configuracao.setDataAtualizacao(dataFutura);

        // Assert
        assertThat(configuracao.getDataAtualizacao()).isEqualTo(dataFutura);
        assertThat(configuracao.getDataAtualizacao()).isAfter(agora);
    }

    @Test
    @DisplayName("Deve lidar com data de atualização passada")
    void testDataAtualizacaoPassada() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime dataPassada = agora.minusDays(1);

        // Act
        configuracao.setDataAtualizacao(dataPassada);

        // Assert
        assertThat(configuracao.getDataAtualizacao()).isEqualTo(dataPassada);
        assertThat(configuracao.getDataAtualizacao()).isBefore(agora);
    }

    @Test
    @DisplayName("Deve lidar com chaves complexas")
    void testChavesComplexas() {
        // Arrange & Act
        configuracao.setChave("database.connection.pool.size");
        assertThat(configuracao.getChave()).isEqualTo("database.connection.pool.size");

        configuracao.setChave("mail.smtp.auth.enabled");
        assertThat(configuracao.getChave()).isEqualTo("mail.smtp.auth.enabled");

        configuracao.setChave("ui.theme.primary.color");
        assertThat(configuracao.getChave()).isEqualTo("ui.theme.primary.color");

        configuracao.setChave("security.jwt.expiration.hours");
        assertThat(configuracao.getChave()).isEqualTo("security.jwt.expiration.hours");
    }

    @Test
    @DisplayName("Deve lidar com diferentes tipos de valores")
    void testDiferentesTiposValores() {
        // Arrange & Act
        configuracao.setTipo("STRING");
        configuracao.setValor("texto simples");
        assertThat(configuracao.getValor()).isEqualTo("texto simples");

        configuracao.setTipo("NUMBER");
        configuracao.setValor("123.45");
        assertThat(configuracao.getValor()).isEqualTo("123.45");

        configuracao.setTipo("BOOLEAN");
        configuracao.setValor("true");
        assertThat(configuracao.getValor()).isEqualTo("true");

        configuracao.setTipo("DATE");
        configuracao.setValor("2024-01-15");
        assertThat(configuracao.getValor()).isEqualTo("2024-01-15");
    }

    @Test
    @DisplayName("Deve lidar com descrições longas")
    void testDescricoesLongas() {
        // Arrange
        String descricaoLonga = "Esta é uma configuração muito importante que controla o comportamento do sistema em diversas situações e deve ser utilizada com cuidado.";

        // Act
        configuracao.setDescricao(descricaoLonga);

        // Assert
        assertThat(configuracao.getDescricao()).isEqualTo(descricaoLonga);
        assertThat(configuracao.getDescricao()).hasSizeGreaterThan(50);
    }

    @Test
    @DisplayName("Deve lidar com valores especiais")
    void testValoresEspeciais() {
        // Arrange & Act
        configuracao.setValor("localhost:5432/artereal_db?ssl=false");
        assertThat(configuracao.getValor()).contains("localhost");
        assertThat(configuracao.getValor()).contains("5432");

        configuracao.setValor("user@domain.com.br");
        assertThat(configuracao.getValor()).contains("@");

        configuracao.setValor("/path/to/directory/file.txt");
        assertThat(configuracao.getValor()).startsWith("/");

        configuracao.setValor("https://example.com/api/v1/resource");
        assertThat(configuracao.getValor()).startsWith("https://");
    }

    @Test
    @DisplayName("Deve lidar com usuários de atualização diferentes")
    void testUsuariosAtualizacao() {
        // Arrange & Act
        configuracao.setUsuarioAtualizacao("admin");
        assertThat(configuracao.getUsuarioAtualizacao()).isEqualTo("admin");

        configuracao.setUsuarioAtualizacao("joao.silva");
        assertThat(configuracao.getUsuarioAtualizacao()).isEqualTo("joao.silva");

        configuracao.setUsuarioAtualizacao("system");
        assertThat(configuracao.getUsuarioAtualizacao()).isEqualTo("system");

        configuracao.setUsuarioAtualizacao("root");
        assertThat(configuracao.getUsuarioAtualizacao()).isEqualTo("root");
    }

    @Test
    @DisplayName("Deve lidar com configuração booleana")
    void testConfiguracaoBooleana() {
        // Arrange
        Configuracao configBoolean = new Configuracao("feature.enabled", "true", "Habilita funcionalidade", "BOOLEAN", "SISTEMA");

        // Assert
        assertThat(configBoolean.getTipo()).isEqualTo("BOOLEAN");
        assertThat(configBoolean.getValor()).isEqualTo("true");
        assertThat(configBoolean.isEditavel()).isTrue();
        assertThat(configBoolean.isVisivel()).isTrue();
    }

    @Test
    @DisplayName("Deve lidar com configuração numérica")
    void testConfiguracaoNumerica() {
        // Arrange
        Configuracao configNumber = new Configuracao("max.connections", "100", "Número máximo de conexões", "NUMBER", "SISTEMA");

        // Assert
        assertThat(configNumber.getTipo()).isEqualTo("NUMBER");
        assertThat(configNumber.getValor()).isEqualTo("100");
        assertThat(configNumber.isEditavel()).isTrue();
        assertThat(configNumber.isVisivel()).isTrue();
    }

    @Test
    @DisplayName("Deve lidar com configuração de data")
    void testConfiguracaoData() {
        // Arrange
        Configuracao configDate = new Configuracao("system.startup.date", "2024-01-01", "Data de inicialização", "DATE", "SISTEMA");

        // Assert
        assertThat(configDate.getTipo()).isEqualTo("DATE");
        assertThat(configDate.getValor()).isEqualTo("2024-01-01");
        assertThat(configDate.isEditavel()).isTrue();
        assertThat(configDate.isVisivel()).isTrue();
    }
}
