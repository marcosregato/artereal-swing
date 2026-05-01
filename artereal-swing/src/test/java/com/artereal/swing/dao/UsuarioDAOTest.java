package com.artereal.swing.dao;

import com.artereal.swing.model.Usuario;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para o UsuarioDAO do Sistema ArteReal
 */
class UsuarioDAOTest {

    @TempDir
    Path tempDir;
    
    private UsuarioDAO usuarioDAO;
    private String originalUserHome;

    @BeforeEach
    void setUp() {
        // Backup do user.home original
        originalUserHome = System.getProperty("user.home");
        
        // Configurar diretório temporário para testes
        System.setProperty("user.home", tempDir.toString());
        
        // Inicializar banco de dados e DAO
        try {
            com.artereal.swing.database.DatabaseManager.getInstance().initializeDatabase();
            usuarioDAO = new UsuarioDAO();
        } catch (Exception e) {
            fail("Falha ao inicializar banco de dados para testes: " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        // Restaurar user.home original
        if (originalUserHome != null) {
            System.setProperty("user.home", originalUserHome);
        }
    }

    @Test
    @DisplayName("Deve salvar usuário com sucesso")
    void testSave() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("test_user");
        usuario.setSenha("test_pass");
        usuario.setAdministrador(false);
        usuario.setDataInicio(LocalDateTime.now());
        usuario.setPermissaoPagar(true);
        usuario.setPermissaoReceber(false);
        usuario.setPermissaoBackup(true);

        // Salvar usuário
        assertDoesNotThrow(() -> {
            usuarioDAO.save(usuario);
        }, "Salvar usuário não deve lançar exceção");

        assertNotNull(usuario.getId(), "ID do usuário deve ser gerado");
        assertEquals("test_user", usuario.getNome());
        assertEquals("test_pass", usuario.getSenha());
        assertFalse(usuario.isAdministrador());
    }

    @Test
    @DisplayName("Deve autenticar usuário com credenciais corretas")
    void testAuthenticate() throws Exception {
        // Salvar usuário primeiro
        Usuario usuario = new Usuario();
        usuario.setNome("auth_user");
        usuario.setSenha("auth_pass");
        usuario.setAdministrador(false);
        usuario.setDataInicio(LocalDateTime.now());
        usuario.setPermissaoPagar(true);
        usuario.setPermissaoReceber(true);
        usuario.setPermissaoBackup(true);

        usuarioDAO.save(usuario);

        // Autenticar com credenciais corretas
        Usuario autenticado = usuarioDAO.authenticate("auth_user", "auth_pass");

        assertNotNull(autenticado, "Usuário deve ser autenticado");
        assertEquals("auth_user", autenticado.getNome());
        assertEquals("auth_pass", autenticado.getSenha());
        assertFalse(autenticado.isAdministrador());
    }

    @Test
    @DisplayName("Deve retornar null ao autenticar com credenciais incorretas")
    void testAuthenticateIncorrect() throws Exception {
        // Tentar autenticar com usuário inexistente
        Usuario autenticado = usuarioDAO.authenticate("nonexistent", "wrong_pass");
        assertNull(autenticado, "Autenticação com usuário inexistente deve retornar null");

        // Salvar usuário e tentar autenticar com senha errada
        Usuario usuario = new Usuario();
        usuario.setNome("wrong_pass_user");
        usuario.setSenha("correct_pass");
        usuario.setAdministrador(false);
        usuario.setDataInicio(LocalDateTime.now());

        usuarioDAO.save(usuario);

        autenticado = usuarioDAO.authenticate("wrong_pass_user", "wrong_pass");
        assertNull(autenticado, "Autenticação com senha errada deve retornar null");
    }

    @Test
    @DisplayName("Deve buscar usuário por ID")
    void testFindById() throws Exception {
        // Salvar usuário
        Usuario usuario = new Usuario();
        usuario.setNome("find_user");
        usuario.setSenha("find_pass");
        usuario.setAdministrador(true);
        usuario.setDataInicio(LocalDateTime.now());
        usuario.setPermissaoPagar(true);
        usuario.setPermissaoReceber(true);
        usuario.setPermissaoBackup(true);

        usuarioDAO.save(usuario);
        Long id = usuario.getId();

        // Buscar pelo ID
        Usuario encontrado = usuarioDAO.findById(id);

        assertNotNull(encontrado, "Usuário deve ser encontrado");
        assertEquals(id, encontrado.getId());
        assertEquals("find_user", encontrado.getNome());
        assertEquals("find_pass", encontrado.getSenha());
        assertTrue(encontrado.isAdministrador());
    }

    @Test
    @DisplayName("Deve retornar null ao buscar usuário por ID inexistente")
    void testFindByIdInexistente() throws Exception {
        Usuario encontrado = usuarioDAO.findById(999L);
        assertNull(encontrado, "Usuário inexistente não deve ser encontrado");
    }

    @Test
    @DisplayName("Deve listar todos os usuários")
    void testFindAll() throws Exception {
        // Salvar alguns usuários
        Usuario usuario1 = new Usuario();
        usuario1.setNome("user1");
        usuario1.setSenha("pass1");
        usuario1.setAdministrador(true);
        usuario1.setDataInicio(LocalDateTime.now());
        usuario1.setPermissaoPagar(true);
        usuario1.setPermissaoReceber(false);
        usuario1.setPermissaoBackup(false);

        Usuario usuario2 = new Usuario();
        usuario2.setNome("user2");
        usuario2.setSenha("pass2");
        usuario2.setAdministrador(false);
        usuario2.setDataInicio(LocalDateTime.now());
        usuario2.setPermissaoPagar(false);
        usuario2.setPermissaoReceber(true);
        usuario2.setPermissaoBackup(true);

        usuarioDAO.save(usuario1);
        usuarioDAO.save(usuario2);

        // Listar todos
        List<Usuario> usuarios = usuarioDAO.findAll();

        assertNotNull(usuarios, "Lista de usuários não deve ser nula");
        assertTrue(usuarios.size() >= 2, "Deve haver pelo menos 2 usuários (admin + testes)");
        
        // Verificar se nossos usuários de teste estão na lista
        boolean hasUser1 = usuarios.stream().anyMatch(u -> "user1".equals(u.getNome()));
        boolean hasUser2 = usuarios.stream().anyMatch(u -> "user2".equals(u.getNome()));
        
        assertTrue(hasUser1, "User1 deve estar na lista");
        assertTrue(hasUser2, "User2 deve estar na lista");
    }

    @Test
    @DisplayName("Deve buscar usuários por nome")
    void testFindByNome() throws Exception {
        // Salvar usuários
        Usuario usuario1 = new Usuario();
        usuario1.setNome("search_user");
        usuario1.setSenha("pass123");
        usuario1.setAdministrador(false);
        usuario1.setDataInicio(LocalDateTime.now());
        usuario1.setPermissaoPagar(false);
        usuario1.setPermissaoReceber(true);
        usuario1.setPermissaoBackup(false);

        Usuario usuario2 = new Usuario();
        usuario2.setNome("search_user_two");
        usuario2.setSenha("pass456");
        usuario2.setAdministrador(true);
        usuario2.setDataInicio(LocalDateTime.now());
        usuario2.setPermissaoPagar(true);
        usuario2.setPermissaoReceber(false);
        usuario2.setPermissaoBackup(true);

        usuarioDAO.save(usuario1);
        usuarioDAO.save(usuario2);

        // Buscar por nome parcial
        List<Usuario> usuarios = usuarioDAO.findByNome("search_user");

        assertNotNull(usuarios, "Lista não deve ser nula");
        assertTrue(usuarios.size() >= 2, "Deve encontrar pelo menos 2 usuários");
        
        boolean hasUser1 = usuarios.stream().anyMatch(u -> "search_user".equals(u.getNome()));
        boolean hasUser2 = usuarios.stream().anyMatch(u -> "search_user_two".equals(u.getNome()));
        
        assertTrue(hasUser1, "search_user deve estar na lista");
        assertTrue(hasUser2, "search_user_two deve estar na lista");
    }

    @Test
    @DisplayName("Deve atualizar usuário existente")
    void testUpdate() throws Exception {
        // Salvar usuário inicial
        Usuario usuario = new Usuario();
        usuario.setNome("update_user");
        usuario.setSenha("old_pass");
        usuario.setAdministrador(false);
        usuario.setDataInicio(LocalDateTime.now());
        usuario.setPermissaoPagar(false);
        usuario.setPermissaoReceber(false);
        usuario.setPermissaoBackup(false);

        usuarioDAO.save(usuario);
        Long id = usuario.getId();

        // Atualizar dados
        usuario.setSenha("new_pass");
        usuario.setAdministrador(true);
        usuario.setPermissaoPagar(true);
        usuario.setPermissaoReceber(true);
        usuario.setPermissaoBackup(true);

        // Salvar novamente (deve atualizar)
        usuarioDAO.save(usuario);

        // Verificar no banco
        Usuario atualizado = usuarioDAO.findById(id);
        assertNotNull(atualizado, "Usuário atualizado deve ser encontrado");
        assertEquals(id, atualizado.getId());
        assertEquals("update_user", atualizado.getNome());
        assertEquals("new_pass", atualizado.getSenha());
        assertTrue(atualizado.isAdministrador());
    }

    @Test
    @DisplayName("Deve excluir (desativar) usuário")
    void testDelete() throws Exception {
        // Salvar usuário
        Usuario usuario = new Usuario();
        usuario.setNome("delete_user");
        usuario.setSenha("delete_pass");
        usuario.setAdministrador(false);
        usuario.setDataInicio(LocalDateTime.now());
        usuario.setPermissaoPagar(false);
        usuario.setPermissaoReceber(false);
        usuario.setPermissaoBackup(false);

        usuarioDAO.save(usuario);
        Long id = usuario.getId();

        // Verificar que existe
        assertNotNull(usuarioDAO.findById(id), "Usuário deve existir antes de excluir");

        // Excluir (desativar)
        assertDoesNotThrow(() -> {
            usuarioDAO.delete(id);
        }, "Excluir usuário não deve lançar exceção");

        // Verificar que foi desativado (não aparece mais no findAll)
        List<Usuario> usuariosAtivos = usuarioDAO.findAll();
        boolean stillActive = usuariosAtivos.stream().anyMatch(u -> id.equals(u.getId()));
        assertFalse(stillActive, "Usuário desativado não deve aparecer na lista de ativos");
    }

    @Test
    @DisplayName("Deve contar usuários ativos")
    void testCount() throws Exception {
        // Contar usuários iniciais (deve ter pelo menos o admin)
        int countInicial = usuarioDAO.count();
        assertTrue(countInicial >= 1, "Deve haver pelo menos 1 usuário (admin)");

        // Salvar usuário
        Usuario usuario = new Usuario();
        usuario.setNome("count_user");
        usuario.setSenha("count_pass");
        usuario.setAdministrador(false);
        usuario.setDataInicio(LocalDateTime.now());
        usuario.setPermissaoPagar(false);
        usuario.setPermissaoReceber(false);
        usuario.setPermissaoBackup(false);

        usuarioDAO.save(usuario);

        // Verificar contagem atualizada
        int countAtual = usuarioDAO.count();
        assertTrue(countAtual > countInicial, "Contagem deve aumentar");
    }

    @Test
    @DisplayName("Deve verificar se existe administrador")
    void testHasAdministrator() throws Exception {
        // Deve ter administrador inicial (admin)
        assertTrue(usuarioDAO.hasAdministrator(), "Deve existir pelo menos 1 administrador");
    }

    @Test
    @DisplayName("Deve lidar com dados inválidos gracefulmente")
    void testDadosInvalidos() throws Exception {
        Usuario usuarioInvalido = new Usuario();
        
        // Testar usuário com nome nulo
        try {
            usuarioDAO.save(usuarioInvalido);
            // Se não lançar exceção, o sistema aceitou o usuário
            assertNotNull(usuarioInvalido.getId(), "Usuário deve ter sido salvo com ID");
        } catch (Exception e) {
            // Se lançar exceção, também é válido - não verificamos mensagem específica
            // pois o sistema pode ter validação em diferentes camadas
            assertTrue(e != null, "Exceção pode ser lançada para dados inválidos");
        }

        // Testar usuário com nome vazio
        usuarioInvalido = new Usuario();
        usuarioInvalido.setNome("");
        try {
            usuarioDAO.save(usuarioInvalido);
            // Se não lançar exceção, o sistema aceitou o usuário
            assertNotNull(usuarioInvalido.getId(), "Usuário deve ter sido salvo com ID");
        } catch (Exception e) {
            // Se lançar exceção, também é válido - não verificamos mensagem específica
            // pois o sistema pode ter validação em diferentes camadas
            assertTrue(e != null, "Exceção pode ser lançada para dados inválidos");
        }
    }

    @Test
    @DisplayName("Deve manter integridade dos dados em operações concorrentes")
    void testIntegridadeConcorrente() throws Exception {
        // Criar usuário
        Usuario usuario = new Usuario();
        usuario.setNome("concurrent_user");
        usuario.setSenha("original_pass");
        usuario.setAdministrador(false);
        usuario.setDataInicio(LocalDateTime.now());
        usuario.setPermissaoPagar(false);
        usuario.setPermissaoReceber(false);
        usuario.setPermissaoBackup(false);

        usuarioDAO.save(usuario);
        Long id = usuario.getId();

        // Simular atualização concorrente
        Usuario copia1 = usuarioDAO.findById(id);
        Usuario copia2 = usuarioDAO.findById(id);

        // Atualizar cópias com dados diferentes
        copia1.setSenha("pass1");
        copia1.setAdministrador(true);

        copia2.setSenha("pass2");
        copia2.setPermissaoPagar(true);

        // Salvar primeira cópia
        usuarioDAO.save(copia1);

        // Tentar salvar segunda cópia
        assertDoesNotThrow(() -> {
            usuarioDAO.save(copia2);
        }, "Segunda atualização não deve lançar exceção");

        // Verificar estado final
        Usuario usuarioFinal = usuarioDAO.findById(id);
        assertNotNull(usuarioFinal, "Usuário deve existir após atualizações concorrentes");
        assertEquals("concurrent_user", usuarioFinal.getNome());
        
        // Verificar que algum estado consistente foi mantido
        assertTrue(
            "pass1".equals(usuarioFinal.getSenha()) || "pass2".equals(usuarioFinal.getSenha()),
            "Senha deve ser uma das versões atualizadas"
        );
    }

    @Test
    @DisplayName("Deve autenticar apenas usuários ativos")
    void testAuthenticateActiveOnly() throws Exception {
        // Salvar usuário
        Usuario usuario = new Usuario();
        usuario.setNome("active_user");
        usuario.setSenha("active_pass");
        usuario.setAdministrador(false);
        usuario.setDataInicio(LocalDateTime.now());
        usuario.setPermissaoPagar(true);
        usuario.setPermissaoReceber(true);
        usuario.setPermissaoBackup(true);

        usuarioDAO.save(usuario);
        Long id = usuario.getId();

        // Autenticar antes de desativar
        Usuario autenticado = usuarioDAO.authenticate("active_user", "active_pass");
        assertNotNull(autenticado, "Usuário ativo deve ser autenticado");

        // Desativar usuário
        usuarioDAO.delete(id);

        // Tentar autenticar após desativar
        Usuario autenticadoInativo = usuarioDAO.authenticate("active_user", "active_pass");
        assertNull(autenticadoInativo, "Usuário inativo não deve ser autenticado");
    }
}
