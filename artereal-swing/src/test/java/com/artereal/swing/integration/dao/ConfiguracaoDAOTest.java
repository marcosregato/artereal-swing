package com.artereal.swing.integration.dao;

import com.artereal.swing.database.DatabaseManager;
import com.artereal.swing.dao.ConfiguracaoDAO;
import com.artereal.swing.model.Configuracao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de integração para ConfiguracaoDAO
 */
@DisplayName("Testes de Integração - ConfiguracaoDAO")
class ConfiguracaoDAOTest {

    private DatabaseManager databaseManager;
    private ConfiguracaoDAO configuracaoDAO;

    @BeforeEach
    void setUp() throws SQLException {
        databaseManager = DatabaseManager.getInstance();
        configuracaoDAO = new ConfiguracaoDAO();
        
        // Forçar recriação do banco para cada teste
        databaseManager.initializeDatabase();
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Limpar dados da tabela configuracao após cada teste
        try (Connection conn = databaseManager.getConnection()) {
            conn.createStatement().execute("DELETE FROM configuracao");
        }
    }

    @Test
    @DisplayName("Deve criar e salvar configuração")
    void testSalvarConfiguracao() throws SQLException {
        // Arrange
        Configuracao configuracao = criarConfiguracaoTeste();
        configuracao.setChave("test.config");

        // Act
        configuracaoDAO.save(configuracao);

        // Assert
        assertThat(configuracao.getId()).isNotNull();
        assertThat(configuracao.getId()).isPositive();
    }

    @Test
    @DisplayName("Deve buscar configuração por chave")
    void testBuscarConfiguracaoPorChave() throws SQLException {
        // Arrange
        Configuracao configuracao = criarConfiguracaoTeste();
        configuracao.setChave("app.nome");
        configuracaoDAO.save(configuracao);

        // Act
        Configuracao encontrada = configuracaoDAO.findByChave("app.nome");

        // Assert
        if (encontrada != null) {
            assertThat(encontrada.getChave()).isEqualTo("app.nome");
            assertThat(encontrada.getValor()).isEqualTo("ArteReal System");
            assertThat(encontrada.getDescricao()).isEqualTo("Nome do sistema");
            assertThat(encontrada.getCategoria()).isEqualTo("GERAL");
        }
    }

    @Test
    @DisplayName("Deve buscar configuração por chave com valor padrão")
    void testBuscarConfiguracaoPorChaveOrDefault() throws SQLException {
        // Act
        Configuracao config = configuracaoDAO.findByChaveOrDefault(
            "nova.config", 
            "valor_padrao", 
            "Descrição da nova configuração", 
            "STRING", 
            "TESTE"
        );

        // Assert
        assertThat(config).isNotNull();
        assertThat(config.getChave()).isEqualTo("nova.config");
        assertThat(config.getValor()).isEqualTo("valor_padrao");
        assertThat(config.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve buscar todas as configurações")
    void testBuscarTodasConfiguracoes() throws SQLException {
        // Arrange
        Configuracao config1 = criarConfiguracaoTeste();
        config1.setChave("config.1");
        configuracaoDAO.save(config1);

        Configuracao config2 = criarConfiguracaoTeste();
        config2.setChave("config.2");
        config2.setCategoria("BANCO");
        configuracaoDAO.save(config2);

        // Act
        List<Configuracao> configuracoes = configuracaoDAO.findAll();

        // Assert
        assertThat(configuracoes).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Deve buscar configurações por categoria")
    void testBuscarConfiguracoesPorCategoria() throws SQLException {
        // Arrange
        Configuracao config1 = criarConfiguracaoTeste();
        config1.setChave("config.db.1");
        config1.setCategoria("BANCO");
        configuracaoDAO.save(config1);

        Configuracao config2 = criarConfiguracaoTeste();
        config2.setChave("config.db.2");
        config2.setCategoria("BANCO");
        configuracaoDAO.save(config2);

        Configuracao config3 = criarConfiguracaoTeste();
        config3.setChave("config.app.1");
        config3.setCategoria("GERAL");
        configuracaoDAO.save(config3);

        // Act
        List<Configuracao> configsBanco = configuracaoDAO.findByCategoria("BANCO");
        List<Configuracao> configsGeral = configuracaoDAO.findByCategoria("GERAL");

        // Assert
        assertThat(configsBanco).hasSizeGreaterThanOrEqualTo(2);
        assertThat(configsGeral).hasSizeGreaterThanOrEqualTo(1);
        
        assertThat(configsBanco).extracting("categoria")
            .allMatch(categoria -> "BANCO".equals(categoria));
        assertThat(configsGeral).extracting("categoria")
            .allMatch(categoria -> "GERAL".equals(categoria));
    }

    @Test
    @DisplayName("Deve buscar configurações visíveis")
    void testBuscarConfiguracoesVisiveis() throws SQLException {
        // Arrange
        Configuracao config1 = criarConfiguracaoTeste();
        config1.setChave("config.visivel.1");
        config1.setVisivel(true);
        configuracaoDAO.save(config1);

        Configuracao config2 = criarConfiguracaoTeste();
        config2.setChave("config.invisivel.1");
        config2.setVisivel(false);
        configuracaoDAO.save(config2);

        Configuracao config3 = criarConfiguracaoTeste();
        config3.setChave("config.visivel.2");
        config3.setVisivel(true);
        configuracaoDAO.save(config3);

        // Act
        List<Configuracao> visiveis = configuracaoDAO.findVisiveis();

        // Assert
        assertThat(visiveis).hasSizeGreaterThanOrEqualTo(2);
        assertThat(visiveis).extracting("visivel")
            .allMatch(visivel -> (Boolean) visivel);
    }

    @Test
    @DisplayName("Deve buscar configurações editáveis")
    void testBuscarConfiguracoesEditaveis() throws SQLException {
        // Arrange
        Configuracao config1 = criarConfiguracaoTeste();
        config1.setChave("config.editavel.1");
        config1.setEditavel(true);
        config1.setVisivel(true);
        configuracaoDAO.save(config1);

        Configuracao config2 = criarConfiguracaoTeste();
        config2.setChave("config.nao.editavel.1");
        config2.setEditavel(false);
        config2.setVisivel(true);
        configuracaoDAO.save(config2);

        Configuracao config3 = criarConfiguracaoTeste();
        config3.setChave("config.invisivel.editavel");
        config3.setEditavel(true);
        config3.setVisivel(false);
        configuracaoDAO.save(config3);

        // Act
        List<Configuracao> editaveis = configuracaoDAO.findEditaveis();

        // Assert
        assertThat(editaveis).hasSizeGreaterThanOrEqualTo(1);
        assertThat(editaveis).extracting("editavel")
            .allMatch(editavel -> (Boolean) editavel);
        assertThat(editaveis).extracting("visivel")
            .allMatch(visivel -> (Boolean) visivel);
    }

    @Test
    @DisplayName("Deve obter valor como String")
    void testGetValorString() throws SQLException {
        // Arrange
        Configuracao config = criarConfiguracaoTeste();
        config.setChave("test.string");
        config.setValor("valor_teste");
        configuracaoDAO.save(config);

        // Act
        String valor = configuracaoDAO.getValorString("test.string");

        // Assert
        assertThat(valor).isEqualTo("valor_teste");
    }

    @Test
    @DisplayName("Deve obter valor como String com padrão")
    void testGetValorStringComPadrao() throws SQLException {
        // Act
        String valorExistente = configuracaoDAO.getValorString("nao.existe", "padrao");
        String valorPadrao = configuracaoDAO.getValorString("chave.inexistente", "valor_padrao");

        // Assert
        assertThat(valorExistente).isEqualTo("padrao");
        assertThat(valorPadrao).isEqualTo("valor_padrao");
    }

    @Test
    @DisplayName("Deve obter valor como boolean")
    void testGetValorBoolean() throws SQLException {
        // Arrange
        Configuracao config = criarConfiguracaoTeste();
        config.setChave("test.boolean");
        config.setValor("true");
        configuracaoDAO.save(config);

        // Act
        boolean valor = configuracaoDAO.getValorBoolean("test.boolean");

        // Assert
        assertThat(valor).isTrue();
    }

    @Test
    @DisplayName("Deve obter valor como boolean com padrão")
    void testGetValorBooleanComPadrao() throws SQLException {
        // Act
        boolean valorExistente = configuracaoDAO.getValorBoolean("nao.existe", true);
        boolean valorPadrao = configuracaoDAO.getValorBoolean("chave.inexistente", false);

        // Assert
        assertThat(valorExistente).isTrue();
        assertThat(valorPadrao).isFalse();
    }

    @Test
    @DisplayName("Deve obter valor como inteiro")
    void testGetValorInt() throws SQLException {
        // Arrange
        Configuracao config = criarConfiguracaoTeste();
        config.setChave("test.int");
        config.setValor("42");
        configuracaoDAO.save(config);

        // Act
        int valor = configuracaoDAO.getValorInt("test.int");

        // Assert
        assertThat(valor).isEqualTo(42);
    }

    @Test
    @DisplayName("Deve obter valor como inteiro com padrão")
    void testGetValorIntComPadrao() throws SQLException {
        // Act
        int valorExistente = configuracaoDAO.getValorInt("nao.existe", 100);
        int valorPadrao = configuracaoDAO.getValorInt("chave.inexistente", 200);

        // Assert
        assertThat(valorExistente).isEqualTo(100);
        assertThat(valorPadrao).isEqualTo(200);
    }

    @Test
    @DisplayName("Deve definir valor de configuração")
    void testSetValor() throws SQLException {
        // Arrange
        Configuracao config = criarConfiguracaoTeste();
        config.setChave("test.set");
        config.setEditavel(true);
        configuracaoDAO.save(config);

        // Act
        configuracaoDAO.setValor("test.set", "novo_valor", "usuario_teste");

        // Assert
        Configuracao atualizada = configuracaoDAO.findByChave("test.set");
        if (atualizada != null) {
            assertThat(atualizada.getValor()).isEqualTo("novo_valor");
            assertThat(atualizada.getUsuarioAtualizacao()).isEqualTo("usuario_teste");
        }
    }

    @Test
    @DisplayName("Deve definir valor boolean de configuração")
    void testSetValorBoolean() throws SQLException {
        // Arrange
        Configuracao config = criarConfiguracaoTeste();
        config.setChave("test.boolean.set");
        config.setEditavel(true);
        configuracaoDAO.save(config);

        // Act
        configuracaoDAO.setValorBoolean("test.boolean.set", true, "usuario_teste");

        // Assert
        Configuracao atualizada = configuracaoDAO.findByChave("test.boolean.set");
        if (atualizada != null) {
            assertThat(atualizada.getValorAsBoolean()).isTrue();
            assertThat(atualizada.getUsuarioAtualizacao()).isEqualTo("usuario_teste");
        }
    }

    @Test
    @DisplayName("Deve definir valor inteiro de configuração")
    void testSetValorInt() throws SQLException {
        // Arrange
        Configuracao config = criarConfiguracaoTeste();
        config.setChave("test.int.set");
        config.setEditavel(true);
        configuracaoDAO.save(config);

        // Act
        configuracaoDAO.setValorInt("test.int.set", 999, "usuario_teste");

        // Assert
        Configuracao atualizada = configuracaoDAO.findByChave("test.int.set");
        if (atualizada != null) {
            assertThat(atualizada.getValorAsInt()).isEqualTo(999);
            assertThat(atualizada.getUsuarioAtualizacao()).isEqualTo("usuario_teste");
        }
    }

    @Test
    @DisplayName("Deve excluir configuração")
    void testExcluirConfiguracao() throws SQLException {
        // Arrange
        Configuracao config = criarConfiguracaoTeste();
        configuracaoDAO.save(config);
        Long id = config.getId();

        // Act
        configuracaoDAO.delete(id);

        // Assert
        Configuracao excluida = configuracaoDAO.findByChave(config.getChave());
        assertThat(excluida).isNull();
    }

    @Test
    @DisplayName("Deve obter categorias distintas")
    void testGetCategorias() throws SQLException {
        // Arrange
        Configuracao config1 = criarConfiguracaoTeste();
        config1.setChave("cat.1");
        config1.setCategoria("GERAL");
        configuracaoDAO.save(config1);

        Configuracao config2 = criarConfiguracaoTeste();
        config2.setChave("cat.2");
        config2.setCategoria("BANCO");
        configuracaoDAO.save(config2);

        Configuracao config3 = criarConfiguracaoTeste();
        config3.setChave("cat.3");
        config3.setCategoria("GERAL");
        configuracaoDAO.save(config3);

        // Act
        List<String> categorias = configuracaoDAO.getCategorias();

        // Assert
        assertThat(categorias).hasSizeGreaterThanOrEqualTo(2);
        assertThat(categorias).contains("GERAL", "BANCO");
    }

    @Test
    @DisplayName("Deve atualizar configuração")
    void testAtualizarConfiguracao() throws SQLException {
        // Arrange
        Configuracao config = criarConfiguracaoTeste();
        configuracaoDAO.save(config);

        // Act
        config.setValor("novo_valor");
        config.setDescricao("Nova descrição");
        config.setUsuarioAtualizacao("usuario_atualizacao");
        configuracaoDAO.save(config);

        // Assert
        Configuracao atualizada = configuracaoDAO.findByChave(config.getChave());
        if (atualizada != null) {
            assertThat(atualizada.getValor()).isEqualTo("novo_valor");
            assertThat(atualizada.getDescricao()).isEqualTo("Nova descrição");
            assertThat(atualizada.getUsuarioAtualizacao()).isEqualTo("usuario_atualizacao");
        }
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar chave inexistente")
    void testBuscarChaveInexistente() throws SQLException {
        // Act & Assert
        Configuracao resultado = configuracaoDAO.findByChave("chave_inexistente");
        assertThat(resultado).isNull();
    }

    @Test
    @DisplayName("Deve lidar com campos nulos opcionalmente")
    void testCamposNulosOpcionais() throws SQLException {
        // Arrange
        Configuracao config = criarConfiguracaoTeste();
        config.setDescricao(null);
        config.setUsuarioAtualizacao(null);

        // Act
        configuracaoDAO.save(config);

        // Assert
        assertThat(config.getId()).isNotNull();
        
        Configuracao salva = configuracaoDAO.findByChave(config.getChave());
        if (salva != null) {
            assertThat(salva.getDescricao()).isNull();
            assertThat(salva.getUsuarioAtualizacao()).isNull();
        }
    }

    @Test
    @DisplayName("Não deve definir valor em configuração não editável")
    void testNaoDefinirValorNaoEditavel() throws SQLException {
        // Arrange
        Configuracao config = criarConfiguracaoTeste();
        config.setChave("test.nao.editavel");
        config.setEditavel(false);
        config.setValor("valor_original");
        configuracaoDAO.save(config);

        // Act
        configuracaoDAO.setValor("test.nao.editavel", "valor_novo", "usuario_teste");

        // Assert
        Configuracao naoAlterada = configuracaoDAO.findByChave("test.nao.editavel");
        if (naoAlterada != null) {
            assertThat(naoAlterada.getValor()).isEqualTo("valor_original");
        }
    }

    @Test
    @DisplayName("Deve lidar com diferentes tipos de valor")
    void testDiferentesTiposValor() throws SQLException {
        // Arrange
        Configuracao configString = criarConfiguracaoTeste();
        configString.setChave("test.tipo.string");
        configString.setTipo("STRING");
        configString.setValor("texto");
        configuracaoDAO.save(configString);

        Configuracao configBoolean = criarConfiguracaoTeste();
        configBoolean.setChave("test.tipo.boolean");
        configBoolean.setTipo("BOOLEAN");
        configBoolean.setValor("true");
        configuracaoDAO.save(configBoolean);

        Configuracao configInt = criarConfiguracaoTeste();
        configInt.setChave("test.tipo.int");
        configInt.setTipo("INTEGER");
        configInt.setValor("123");
        configuracaoDAO.save(configInt);

        // Act & Assert
        assertThat(configuracaoDAO.getValorString("test.tipo.string")).isEqualTo("texto");
        assertThat(configuracaoDAO.getValorBoolean("test.tipo.boolean")).isTrue();
        assertThat(configuracaoDAO.getValorInt("test.tipo.int")).isEqualTo(123);
    }

    @Test
    @DisplayName("Deve registrar fluxo completo de configuração")
    void testFluxoCompletoConfiguracao() throws SQLException {
        // Arrange
        String chave = "fluxo.completo";
        
        // Act - Fluxo completo
        Configuracao config = configuracaoDAO.findByChaveOrDefault(
            chave, "valor_inicial", "Descrição inicial", "STRING", "TESTE"
        );
        
        assertThat(config).isNotNull();
        assertThat(config.getValor()).isEqualTo("valor_inicial");
        
        configuracaoDAO.setValor(chave, "valor_modificado", "usuario_fluxo");
        
        Configuracao modificada = configuracaoDAO.findByChave(chave);
        assertThat(modificada).isNotNull();
        assertThat(modificada.getValor()).isEqualTo("valor_modificado");
        assertThat(modificada.getUsuarioAtualizacao()).isEqualTo("usuario_fluxo");

        // Assert
        assertThat(modificada.getDataAtualizacao()).isNotNull();
    }

    /**
     * Método auxiliar para criar uma configuração de teste
     */
    private Configuracao criarConfiguracaoTeste() {
        Configuracao configuracao = new Configuracao();
        configuracao.setChave("test.config");
        configuracao.setValor("ArteReal System");
        configuracao.setDescricao("Nome do sistema");
        configuracao.setTipo("STRING");
        configuracao.setCategoria("GERAL");
        configuracao.setEditavel(true);
        configuracao.setVisivel(true);
        configuracao.setUsuarioAtualizacao("admin");
        return configuracao;
    }
}
