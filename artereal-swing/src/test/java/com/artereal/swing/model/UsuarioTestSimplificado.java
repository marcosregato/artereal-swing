package com.artereal.swing.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.time.LocalDateTime;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes simplificados para o modelo Usuario do Sistema ArteReal
 */
class UsuarioTestSimplificado {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("Deve criar usuário com valores padrão")
    void testUsuarioDefaultValues() {
        Usuario usuario = new Usuario();

        assertNull(usuario.getId(), "ID deve ser nulo por padrão");
        assertNull(usuario.getNome(), "Nome deve ser nulo por padrão");
        assertNull(usuario.getSenha(), "Senha deve ser nula por padrão");
        assertFalse(usuario.isAdministrador(), "Administrador deve ser false por padrão");
        assertNull(usuario.getDataInicio(), "Data início deve ser nula por padrão");
        assertNull(usuario.getAcesso(), "Acesso deve ser nulo por padrão");
        assertNull(usuario.getContas(), "Contas deve ser nulo por padrão");
        assertNull(usuario.getLancamentos(), "Lançamentos deve ser nulo por padrão");
        assertNull(usuario.getClasses(), "Classes deve ser nulo por padrão");
        assertFalse(usuario.isPortaria(), "Portaria deve ser false por padrão");
        assertNull(usuario.getDadosUsuario(), "Dados usuário deve ser nulo por padrão");
        assertFalse(usuario.isPermissaoBackup(), "Permissão backup deve ser false por padrão");
        assertFalse(usuario.isPermissaoRestaura(), "Permissão restaura deve ser false por padrão");
        assertNull(usuario.getDiretorioServico(), "Diretório serviço deve ser nulo por padrão");
        assertFalse(usuario.isPermissaoPagar(), "Permissão pagar deve ser false por padrão");
        assertFalse(usuario.isPermissaoReceber(), "Permissão receber deve ser false por padrão");
    }

    @Test
    @DisplayName("Deve definir e obter valores corretamente")
    void testSettersAndGetters() {
        Usuario usuario = new Usuario();
        LocalDateTime dataTeste = LocalDateTime.of(2024, 1, 15, 10, 30);

        usuario.setId(1L);
        usuario.setNome("Test User");
        usuario.setSenha("password123");
        usuario.setAdministrador(true);
        usuario.setDataInicio(dataTeste);
        usuario.setAcesso("FULL");
        usuario.setContas("ADMIN");
        usuario.setLancamentos("READ_WRITE");
        usuario.setClasses("READ");
        usuario.setPortaria(true);
        usuario.setDadosUsuario("ADMIN");
        usuario.setPermissaoBackup(true);
        usuario.setPermissaoRestaura(true);
        usuario.setDiretorioServico("/home/user");
        usuario.setPermissaoPagar(true);
        usuario.setPermissaoReceber(true);

        assertEquals(1L, usuario.getId());
        assertEquals("Test User", usuario.getNome());
        assertEquals("password123", usuario.getSenha());
        assertTrue(usuario.isAdministrador());
        assertEquals(dataTeste, usuario.getDataInicio());
        assertEquals("FULL", usuario.getAcesso());
        assertEquals("ADMIN", usuario.getContas());
        assertEquals("READ_WRITE", usuario.getLancamentos());
        assertEquals("READ", usuario.getClasses());
        assertTrue(usuario.isPortaria());
        assertEquals("ADMIN", usuario.getDadosUsuario());
        assertTrue(usuario.isPermissaoBackup());
        assertTrue(usuario.isPermissaoRestaura());
        assertEquals("/home/user", usuario.getDiretorioServico());
        assertTrue(usuario.isPermissaoPagar());
        assertTrue(usuario.isPermissaoReceber());
    }

    @Test
    @DisplayName("Deve implementar equals corretamente")
    void testEquals() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("Test User");

        Usuario usuario2 = new Usuario();
        usuario2.setId(1L);
        usuario2.setNome("Test User");

        Usuario usuario3 = new Usuario();
        usuario3.setId(2L);
        usuario3.setNome("Test User");

        // Testar igualdade
        assertEquals(usuario1, usuario2, "Usuários com mesmo ID devem ser iguais");
        assertNotEquals(usuario1, usuario3, "Usuários com IDs diferentes devem ser diferentes");

        // Testar reflexividade
        assertEquals(usuario1, usuario1, "Usuário deve ser igual a si mesmo");

        // Testar nulo
        assertNotEquals(usuario1, null, "Usuário não deve ser igual a nulo");

        // Testar classe diferente
        assertNotEquals(usuario1, "string", "Usuário não deve ser igual a objeto de outra classe");
    }

    @Test
    @DisplayName("Deve implementar hashCode corretamente")
    void testHashCode() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);

        Usuario usuario2 = new Usuario();
        usuario2.setId(1L);

        Usuario usuario3 = new Usuario();
        usuario3.setId(2L);

        // Testar consistência
        int hashCode1 = usuario1.hashCode();
        int hashCode2 = usuario1.hashCode();
        assertEquals(hashCode1, hashCode2, "HashCode deve ser consistente");

        // Testar igualdade
        assertEquals(usuario1.hashCode(), usuario2.hashCode(), "Usuários iguais devem ter hashCode iguais");
    }

    @Test
    @DisplayName("Deve implementar toString corretamente")
    void testToString() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Test User");

        String toString = usuario.toString();

        assertNotNull(toString, "ToString não deve ser nulo");
        assertTrue(toString.contains("Test User"), "ToString deve conter o nome do usuário");
        assertTrue(toString.contains("1"), "ToString deve conter o ID do usuário");
    }

    @Test
    @DisplayName("Deve lidar com permissões corretamente")
    void testPermissoes() {
        Usuario usuario = new Usuario();

        // Testar permissões individuais
        assertFalse(usuario.isPermissaoPagar(), "Permissão pagar deve ser false inicialmente");
        usuario.setPermissaoPagar(true);
        assertTrue(usuario.isPermissaoPagar(), "Permissão pagar deve ser true após definição");

        assertFalse(usuario.isPermissaoReceber(), "Permissão receber deve ser false inicialmente");
        usuario.setPermissaoReceber(true);
        assertTrue(usuario.isPermissaoReceber(), "Permissão receber deve ser true após definição");

        assertFalse(usuario.isPermissaoBackup(), "Permissão backup deve ser false inicialmente");
        usuario.setPermissaoBackup(true);
        assertTrue(usuario.isPermissaoBackup(), "Permissão backup deve ser true após definição");

        assertFalse(usuario.isPermissaoRestaura(), "Permissão restaura deve ser false inicialmente");
        usuario.setPermissaoRestaura(true);
        assertTrue(usuario.isPermissaoRestaura(), "Permissão restaura deve ser true após definição");
    }

    @Test
    @DisplayName("Deve lidar com dados temporais corretamente")
    void testDadosTemporais() {
        Usuario usuario = new Usuario();
        LocalDateTime dataTeste = LocalDateTime.of(2024, 6, 15, 14, 30);

        // Data inicial nula
        assertNull(usuario.getDataInicio(), "Data início deve ser nula inicialmente");

        // Definir data
        usuario.setDataInicio(dataTeste);
        assertEquals(dataTeste, usuario.getDataInicio(), "Data início deve ser definida corretamente");

        // Definir data nula
        usuario.setDataInicio(null);
        assertNull(usuario.getDataInicio(), "Data início deve ser nula após definição como null");
    }

    @Test
    @DisplayName("Deve lidar com campos de texto corretamente")
    void testCamposTexto() {
        Usuario usuario = new Usuario();

        // Testar campos de acesso
        assertDoesNotThrow(() -> {
            usuario.setAcesso("FULL");
            usuario.setContas("ADMIN");
            usuario.setLancamentos("READ_WRITE");
            usuario.setClasses("READ");
            usuario.setDadosUsuario("ADMIN");
            usuario.setDiretorioServico("/home/user");
        }, "Definir campos de texto não deve lançar exceção");

        assertEquals("FULL", usuario.getAcesso());
        assertEquals("ADMIN", usuario.getContas());
        assertEquals("READ_WRITE", usuario.getLancamentos());
        assertEquals("READ", usuario.getClasses());
        assertEquals("ADMIN", usuario.getDadosUsuario());
        assertEquals("/home/user", usuario.getDiretorioServico());

        // Testar valores nulos
        assertDoesNotThrow(() -> {
            usuario.setAcesso(null);
            usuario.setContas(null);
            usuario.setLancamentos(null);
            usuario.setClasses(null);
            usuario.setDadosUsuario(null);
            usuario.setDiretorioServico(null);
        }, "Definir campos como nulo não deve lançar exceção");
    }

    @Test
    @DisplayName("Deve criar usuário com construtor de nome e senha")
    void testConstrutorNomeSenha() {
        LocalDateTime antes = LocalDateTime.now();
        
        Usuario usuario = new Usuario("Test User", "password123");
        LocalDateTime depois = LocalDateTime.now();

        assertEquals("Test User", usuario.getNome());
        assertEquals("password123", usuario.getSenha());
        assertNotNull(usuario.getDataInicio(), "Data início deve ser definida automaticamente");
        assertTrue(usuario.getDataInicio().isAfter(antes) || usuario.getDataInicio().isEqual(antes), 
            "Data início deve ser após o momento da criação");
        assertTrue(usuario.getDataInicio().isBefore(depois) || usuario.getDataInicio().isEqual(depois), 
            "Data início deve ser antes do momento final");
    }

    @Test
    @DisplayName("Deve ser serializável se implementar Serializable")
    void testSerializable() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Test User");

        // Verificar se implementa Serializable
        assertTrue(usuario instanceof java.io.Serializable, "Usuario deve implementar Serializable");
    }

    @Test
    @DisplayName("Deve manter consistência entre equals e hashCode")
    void testConsistenciaEqualsHashCode() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("Test User");

        Usuario usuario2 = new Usuario();
        usuario2.setId(1L);
        usuario2.setNome("Different Name"); // Nome diferente, mesmo ID

        // Se equals for baseado apenas no ID, devem ser iguais
        if (usuario1.equals(usuario2)) {
            assertEquals(usuario1.hashCode(), usuario2.hashCode(), 
                "Se equals retornar true, hashCode deve ser igual");
        }

        Usuario usuario3 = new Usuario();
        usuario3.setId(2L);
        usuario3.setNome("Test User"); // Mesmo nome, ID diferente

        // Se equals for baseado apenas no ID, devem ser diferentes
        if (!usuario1.equals(usuario3)) {
            // hashCode pode ser igual ou diferente, mas objetos diferentes idealmente devem ter hashCodes diferentes
            // Não há garantia que hashCodes sejam diferentes para objetos diferentes
        }
    }

    @Test
    @DisplayName("Deve lidar com valores extremos")
    void testValoresExtremos() {
        Usuario usuario = new Usuario();

        // Testar valores extremos para ID
        assertDoesNotThrow(() -> {
            usuario.setId(Long.MAX_VALUE);
            usuario.setId(Long.MIN_VALUE);
            usuario.setId(0L);
            usuario.setId(-1L);
        }, "Definir ID com valores extremos não deve lançar exceção");

        // Testar strings longas
        assertDoesNotThrow(() -> {
            String longString = "a".repeat(1000);
            usuario.setNome(longString);
            usuario.setSenha(longString);
            usuario.setAcesso(longString);
            usuario.setContas(longString);
            usuario.setLancamentos(longString);
            usuario.setClasses(longString);
            usuario.setDadosUsuario(longString);
            usuario.setDiretorioServico(longString);
        }, "Definir strings longas não deve lançar exceção");

        // Testar datas extremas
        assertDoesNotThrow(() -> {
            usuario.setDataInicio(LocalDateTime.MAX);
            usuario.setDataInicio(LocalDateTime.MIN);
            usuario.setDataInicio(LocalDateTime.of(1900, 1, 1, 0, 0));
            usuario.setDataInicio(LocalDateTime.of(2100, 12, 31, 23, 59));
        }, "Definir datas extremas não deve lançar exceção");
    }
}
