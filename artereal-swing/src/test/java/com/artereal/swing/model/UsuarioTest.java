package com.artereal.swing.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes unitários básicos para a classe Usuario
 */
@DisplayName("Testes Unitários - Usuario")
class UsuarioTest {

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
    }

    @Test
    @DisplayName("Deve criar usuário com construtor padrão")
    void testConstrutorPadrao() {
        // Act
        Usuario novoUsuario = new Usuario();

        // Assert
        assertThat(novoUsuario).isNotNull();
        assertThat(novoUsuario.getId()).isNull();
        assertThat(novoUsuario.getNome()).isNull();
        assertThat(novoUsuario.getSenha()).isNull();
        assertThat(novoUsuario.isAdministrador()).isFalse(); // boolean default is false
        assertThat(novoUsuario.getAcesso()).isNull();
        assertThat(novoUsuario.getDataInicio()).isNull();
        assertThat(novoUsuario.getContas()).isNull();
        assertThat(novoUsuario.getLancamentos()).isNull();
        assertThat(novoUsuario.getClasses()).isNull();
        assertThat(novoUsuario.isPortaria()).isFalse(); // boolean default is false
        assertThat(novoUsuario.getDadosUsuario()).isNull();
        assertThat(novoUsuario.isPermissaoBackup()).isFalse(); // boolean default is false
        assertThat(novoUsuario.isPermissaoRestaura()).isFalse(); // boolean default is false
        assertThat(novoUsuario.getDiretorioServico()).isNull();
        assertThat(novoUsuario.isPermissaoPagar()).isFalse(); // boolean default is false
        assertThat(novoUsuario.isPermissaoReceber()).isFalse(); // boolean default is false
    }

    @Test
    @DisplayName("Deve criar usuário com construtor parametrizado")
    void testConstrutorParametrizado() {
        // Arrange
        String nome = "João da Silva";
        String senha = "senha123";

        // Act
        Usuario novoUsuario = new Usuario(nome, senha);

        // Assert
        assertThat(novoUsuario).isNotNull();
        assertThat(novoUsuario.getNome()).isEqualTo(nome);
        assertThat(novoUsuario.getSenha()).isEqualTo(senha);
        assertThat(novoUsuario.getDataInicio()).isNotNull();
        assertThat(novoUsuario.getDataInicio()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    @DisplayName("Deve definir e obter ID")
    void testSetGetId() {
        // Arrange
        Long id = 123L;

        // Act
        usuario.setId(id);

        // Assert
        assertThat(usuario.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Deve definir e obter nome")
    void testSetGetNome() {
        // Arrange
        String nome = "Pedro Santos";

        // Act
        usuario.setNome(nome);

        // Assert
        assertThat(usuario.getNome()).isEqualTo(nome);
    }

    @Test
    @DisplayName("Deve definir e obter senha")
    void testSetGetSenha() {
        // Arrange
        String senha = "password456";

        // Act
        usuario.setSenha(senha);

        // Assert
        assertThat(usuario.getSenha()).isEqualTo(senha);
    }

    @Test
    @DisplayName("Deve definir e obter status administrador")
    void testSetGetAdministrador() {
        // Arrange
        boolean administrador = true;

        // Act
        usuario.setAdministrador(administrador);

        // Assert
        assertThat(usuario.isAdministrador()).isEqualTo(administrador);
    }

    @Test
    @DisplayName("Deve definir e obter acesso")
    void testSetGetAcesso() {
        // Arrange
        String acesso = "TOTAL";

        // Act
        usuario.setAcesso(acesso);

        // Assert
        assertThat(usuario.getAcesso()).isEqualTo(acesso);
    }

    @Test
    @DisplayName("Deve definir e obter data de início")
    void testSetGetDataInicio() {
        // Arrange
        LocalDateTime dataInicio = LocalDateTime.of(2024, 1, 15, 10, 30);

        // Act
        usuario.setDataInicio(dataInicio);

        // Assert
        assertThat(usuario.getDataInicio()).isEqualTo(dataInicio);
    }

    @Test
    @DisplayName("Deve definir e obter contas")
    void testSetGetContas() {
        // Arrange
        String contas = "LEITURA_ESCRITA";

        // Act
        usuario.setContas(contas);

        // Assert
        assertThat(usuario.getContas()).isEqualTo(contas);
    }

    @Test
    @DisplayName("Deve definir e obter lançamentos")
    void testSetGetLancamentos() {
        // Arrange
        String lancamentos = "TOTAL";

        // Act
        usuario.setLancamentos(lancamentos);

        // Assert
        assertThat(usuario.getLancamentos()).isEqualTo(lancamentos);
    }

    @Test
    @DisplayName("Deve definir e obter classes")
    void testSetGetClasses() {
        // Arrange
        String classes = "LEITURA_ESCRITA";

        // Act
        usuario.setClasses(classes);

        // Assert
        assertThat(usuario.getClasses()).isEqualTo(classes);
    }

    @Test
    @DisplayName("Deve definir e obter portaria")
    void testSetGetPortaria() {
        // Arrange
        boolean portaria = true;

        // Act
        usuario.setPortaria(portaria);

        // Assert
        assertThat(usuario.isPortaria()).isEqualTo(portaria);
    }

    @Test
    @DisplayName("Deve definir e obter dados do usuário")
    void testSetGetDadosUsuario() {
        // Arrange
        String dadosUsuario = "LEITURA_ESCRITA";

        // Act
        usuario.setDadosUsuario(dadosUsuario);

        // Assert
        assertThat(usuario.getDadosUsuario()).isEqualTo(dadosUsuario);
    }

    @Test
    @DisplayName("Deve definir e obter permissão de backup")
    void testSetGetPermissaoBackup() {
        // Arrange
        boolean permissaoBackup = true;

        // Act
        usuario.setPermissaoBackup(permissaoBackup);

        // Assert
        assertThat(usuario.isPermissaoBackup()).isEqualTo(permissaoBackup);
    }

    @Test
    @DisplayName("Deve definir e obter permissão de restauração")
    void testSetGetPermissaoRestaura() {
        // Arrange
        boolean permissaoRestaura = true;

        // Act
        usuario.setPermissaoRestaura(permissaoRestaura);

        // Assert
        assertThat(usuario.isPermissaoRestaura()).isEqualTo(permissaoRestaura);
    }

    @Test
    @DisplayName("Deve definir e obter diretório de serviço")
    void testSetGetDiretorioServico() {
        // Arrange
        String diretorioServico = "/tmp/backup";

        // Act
        usuario.setDiretorioServico(diretorioServico);

        // Assert
        assertThat(usuario.getDiretorioServico()).isEqualTo(diretorioServico);
    }

    @Test
    @DisplayName("Deve definir e obter permissão de pagar")
    void testSetGetPermissaoPagar() {
        // Arrange
        boolean permissaoPagar = true;

        // Act
        usuario.setPermissaoPagar(permissaoPagar);

        // Assert
        assertThat(usuario.isPermissaoPagar()).isEqualTo(permissaoPagar);
    }

    @Test
    @DisplayName("Deve definir e obter permissão de receber")
    void testSetGetPermissaoReceber() {
        // Arrange
        boolean permissaoReceber = true;

        // Act
        usuario.setPermissaoReceber(permissaoReceber);

        // Assert
        assertThat(usuario.isPermissaoReceber()).isEqualTo(permissaoReceber);
    }

    @Test
    @DisplayName("Deve representar objeto como string")
    void testToString() {
        // Arrange
        usuario.setId(123L);
        usuario.setNome("João da Silva");
        usuario.setAdministrador(true);

        // Act
        String resultado = usuario.toString();

        // Assert
        assertThat(resultado).isEqualTo("Usuario{id=123, nome='João da Silva', administrador=true}");
    }

    @Test
    @DisplayName("Deve comparar objetos iguais")
    void testEquals() {
        // Arrange
        Usuario usuario1 = new Usuario();
        usuario1.setId(123L);

        Usuario usuario2 = new Usuario();
        usuario2.setId(123L);

        Usuario usuario3 = new Usuario();
        usuario3.setId(456L);

        // Act & Assert
        assertThat(usuario1).isEqualTo(usuario2);
        assertThat(usuario1).isNotEqualTo(usuario3);
        assertThat(usuario1).isNotEqualTo(null);
        assertThat(usuario1).isNotEqualTo("string");
    }

    @Test
    @DisplayName("Deve ter mesmo hashCode para IDs iguais")
    void testHashCode() {
        // Arrange
        Usuario usuario1 = new Usuario();
        usuario1.setId(123L);

        Usuario usuario2 = new Usuario();
        usuario2.setId(123L);

        Usuario usuario3 = new Usuario();
        usuario3.setId(456L);

        // Act & Assert
        assertThat(usuario1.hashCode()).isEqualTo(usuario2.hashCode());
        assertThat(usuario1.hashCode()).isNotEqualTo(usuario3.hashCode());
    }

    @Test
    @DisplayName("Deve lidar com diferentes tipos de acesso")
    void testDiferentesTiposAcesso() {
        // Arrange & Act
        usuario.setAcesso("TOTAL");
        assertThat(usuario.getAcesso()).isEqualTo("TOTAL");

        usuario.setAcesso("PARCIAL");
        assertThat(usuario.getAcesso()).isEqualTo("PARCIAL");

        usuario.setAcesso("LEITURA");
        assertThat(usuario.getAcesso()).isEqualTo("LEITURA");

        usuario.setAcesso("RESTRITO");
        assertThat(usuario.getAcesso()).isEqualTo("RESTRITO");
    }

    @Test
    @DisplayName("Deve lidar com diferentes permissões")
    void testDiferentesPermissoes() {
        // Arrange & Act
        usuario.setContas("LEITURA_ESCRITA");
        usuario.setLancamentos("LEITURA_ESCRITA");
        usuario.setClasses("LEITURA_ESCRITA");
        usuario.setDadosUsuario("LEITURA_ESCRITA");

        // Assert
        assertThat(usuario.getContas()).isEqualTo("LEITURA_ESCRITA");
        assertThat(usuario.getLancamentos()).isEqualTo("LEITURA_ESCRITA");
        assertThat(usuario.getClasses()).isEqualTo("LEITURA_ESCRITA");
        assertThat(usuario.getDadosUsuario()).isEqualTo("LEITURA_ESCRITA");

        // Teste com outros valores
        usuario.setContas("TOTAL");
        usuario.setLancamentos("ESCRITA");
        usuario.setClasses("LEITURA");
        usuario.setDadosUsuario("NENHUMA");

        assertThat(usuario.getContas()).isEqualTo("TOTAL");
        assertThat(usuario.getLancamentos()).isEqualTo("ESCRITA");
        assertThat(usuario.getClasses()).isEqualTo("LEITURA");
        assertThat(usuario.getDadosUsuario()).isEqualTo("NENHUMA");
    }

    @Test
    @DisplayName("Deve lidar com valores nulos")
    void testValoresNulos() {
        // Act
        usuario.setNome(null);
        usuario.setSenha(null);
        usuario.setAcesso(null);
        usuario.setDataInicio(null);
        usuario.setContas(null);
        usuario.setLancamentos(null);
        usuario.setClasses(null);
        usuario.setDadosUsuario(null);
        usuario.setDiretorioServico(null);

        // Assert
        assertThat(usuario.getNome()).isNull();
        assertThat(usuario.getSenha()).isNull();
        assertThat(usuario.getAcesso()).isNull();
        assertThat(usuario.getDataInicio()).isNull();
        assertThat(usuario.getContas()).isNull();
        assertThat(usuario.getLancamentos()).isNull();
        assertThat(usuario.getClasses()).isNull();
        assertThat(usuario.getDadosUsuario()).isNull();
        assertThat(usuario.getDiretorioServico()).isNull();
    }

    @Test
    @DisplayName("Deve lidar com valores vazios")
    void testValoresVazios() {
        // Act
        usuario.setNome("");
        usuario.setSenha("");
        usuario.setAcesso("");
        usuario.setContas("");
        usuario.setLancamentos("");
        usuario.setClasses("");
        usuario.setDadosUsuario("");
        usuario.setDiretorioServico("");

        // Assert
        assertThat(usuario.getNome()).isEmpty();
        assertThat(usuario.getSenha()).isEmpty();
        assertThat(usuario.getAcesso()).isEmpty();
        assertThat(usuario.getContas()).isEmpty();
        assertThat(usuario.getLancamentos()).isEmpty();
        assertThat(usuario.getClasses()).isEmpty();
        assertThat(usuario.getDadosUsuario()).isEmpty();
        assertThat(usuario.getDiretorioServico()).isEmpty();
    }

    @Test
    @DisplayName("Deve lidar com diferentes status boolean")
    void testDiferentesStatusBoolean() {
        // Arrange & Act
        usuario.setAdministrador(true);
        usuario.setPortaria(true);
        usuario.setPermissaoBackup(true);
        usuario.setPermissaoRestaura(true);
        usuario.setPermissaoPagar(true);
        usuario.setPermissaoReceber(true);

        // Assert
        assertThat(usuario.isAdministrador()).isTrue();
        assertThat(usuario.isPortaria()).isTrue();
        assertThat(usuario.isPermissaoBackup()).isTrue();
        assertThat(usuario.isPermissaoRestaura()).isTrue();
        assertThat(usuario.isPermissaoPagar()).isTrue();
        assertThat(usuario.isPermissaoReceber()).isTrue();

        // Teste com valores falsos
        usuario.setAdministrador(false);
        usuario.setPortaria(false);
        usuario.setPermissaoBackup(false);
        usuario.setPermissaoRestaura(false);
        usuario.setPermissaoPagar(false);
        usuario.setPermissaoReceber(false);

        assertThat(usuario.isAdministrador()).isFalse();
        assertThat(usuario.isPortaria()).isFalse();
        assertThat(usuario.isPermissaoBackup()).isFalse();
        assertThat(usuario.isPermissaoRestaura()).isFalse();
        assertThat(usuario.isPermissaoPagar()).isFalse();
        assertThat(usuario.isPermissaoReceber()).isFalse();
    }

    @Test
    @DisplayName("Deve lidar com datas de início futuras")
    void testDataInicioFutura() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime dataFutura = agora.plusDays(30);

        // Act
        usuario.setDataInicio(dataFutura);

        // Assert
        assertThat(usuario.getDataInicio()).isEqualTo(dataFutura);
        assertThat(usuario.getDataInicio()).isAfter(agora);
    }

    @Test
    @DisplayName("Deve lidar com datas de início passadas")
    void testDataInicioPassada() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime dataPassada = agora.minusDays(30);

        // Act
        usuario.setDataInicio(dataPassada);

        // Assert
        assertThat(usuario.getDataInicio()).isEqualTo(dataPassada);
        assertThat(usuario.getDataInicio()).isBefore(agora);
    }

    @Test
    @DisplayName("Deve lidar com diferentes diretórios de serviço")
    void testDiferentesDiretoriosServico() {
        // Arrange & Act
        usuario.setDiretorioServico("/tmp/backup");
        assertThat(usuario.getDiretorioServico()).isEqualTo("/tmp/backup");

        usuario.setDiretorioServico("C:\\Backup\\ArteReal");
        assertThat(usuario.getDiretorioServico()).isEqualTo("C:\\Backup\\ArteReal");

        usuario.setDiretorioServico("./backups");
        assertThat(usuario.getDiretorioServico()).isEqualTo("./backups");

        usuario.setDiretorioServico("/var/lib/artereal/backup");
        assertThat(usuario.getDiretorioServico()).isEqualTo("/var/lib/artereal/backup");
    }

    @Test
    @DisplayName("Deve lidar com ID zero")
    void testIdZero() {
        // Act
        usuario.setId(0L);

        // Assert
        assertThat(usuario.getId()).isEqualTo(0L);
    }

    @Test
    @DisplayName("Deve lidar com ID negativo")
    void testIdNegativo() {
        // Act
        usuario.setId(-1L);

        // Assert
        assertThat(usuario.getId()).isEqualTo(-1L);
        assertThat(usuario.getId()).isNegative();
    }

    @Test
    @DisplayName("Deve lidar com nomes com caracteres especiais")
    void testNomesCaracteresEspeciais() {
        // Arrange
        String nomeComEspeciais = "João Álvares de Sá";

        // Act
        usuario.setNome(nomeComEspeciais);

        // Assert
        assertThat(usuario.getNome()).isEqualTo(nomeComEspeciais);
        assertThat(usuario.getNome()).contains("João");
        assertThat(usuario.getNome()).contains("Álvares");
    }

    @Test
    @DisplayName("Deve lidar com senhas complexas")
    void testSenhasComplexas() {
        // Arrange & Act
        usuario.setSenha("senha123@#");
        assertThat(usuario.getSenha()).isEqualTo("senha123@#");

        usuario.setSenha("P@ssw0rd!2024");
        assertThat(usuario.getSenha()).isEqualTo("P@ssw0rd!2024");

        usuario.setSenha("admin#123");
        assertThat(usuario.getSenha()).isEqualTo("admin#123");

        usuario.setSenha("user@2024");
        assertThat(usuario.getSenha()).isEqualTo("user@2024");
    }

    @Test
    @DisplayName("Deve lidar com data de início default no construtor parametrizado")
    void testDataInicioDefaultConstrutorParametrizado() {
        // Act
        Usuario novoUsuario = new Usuario("Teste", "senha");

        // Assert
        assertThat(novoUsuario.getDataInicio()).isNotNull();
        assertThat(novoUsuario.getDataInicio()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    @DisplayName("Deve lidar com combinações de permissões")
    void testCombinacoesPermissoes() {
        // Arrange & Act
        // Usuário administrador com todas as permissões
        usuario.setAdministrador(true);
        usuario.setPermissaoBackup(true);
        usuario.setPermissaoRestaura(true);
        usuario.setPermissaoPagar(true);
        usuario.setPermissaoReceber(true);

        // Assert
        assertThat(usuario.isAdministrador()).isTrue();
        assertThat(usuario.isPermissaoBackup()).isTrue();
        assertThat(usuario.isPermissaoRestaura()).isTrue();
        assertThat(usuario.isPermissaoPagar()).isTrue();
        assertThat(usuario.isPermissaoReceber()).isTrue();

        // Usuário com permissões limitadas
        usuario.setAdministrador(false);
        usuario.setPermissaoBackup(false);
        usuario.setPermissaoRestaura(false);
        usuario.setPermissaoPagar(true);
        usuario.setPermissaoReceber(false);

        assertThat(usuario.isAdministrador()).isFalse();
        assertThat(usuario.isPermissaoBackup()).isFalse();
        assertThat(usuario.isPermissaoRestaura()).isFalse();
        assertThat(usuario.isPermissaoPagar()).isTrue();
        assertThat(usuario.isPermissaoReceber()).isFalse();
    }
}
