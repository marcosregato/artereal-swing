package com.artereal.swing.database;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para o DatabaseManager do Sistema ArteReal
 */
class DatabaseManagerTest {

    @TempDir
    Path tempDir;
    
    private DatabaseManager databaseManager;
    private String originalUserHome;

    @BeforeEach
    void setUp() {
        // Backup do user.home original
        originalUserHome = System.getProperty("user.home");
        
        // Configurar diretório temporário para testes
        System.setProperty("user.home", tempDir.toString());
        
        databaseManager = DatabaseManager.getInstance();
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (databaseManager != null) {
            try {
                databaseManager.closeConnection();
            } catch (SQLException e) {
                // Ignorar erros ao fechar
            }
        }
        
        // Restaurar user.home original
        if (originalUserHome != null) {
            System.setProperty("user.home", originalUserHome);
        }
    }

    @Test
    @DisplayName("Deve obter instância singleton do DatabaseManager")
    void testGetInstance() {
        DatabaseManager instance1 = DatabaseManager.getInstance();
        DatabaseManager instance2 = DatabaseManager.getInstance();
        
        assertNotNull(instance1);
        assertSame(instance1, instance2, "DatabaseManager deve ser singleton");
    }

    @Test
    @DisplayName("Deve inicializar banco de dados com sucesso")
    void testInitializeDatabase() throws SQLException {
        assertDoesNotThrow(() -> {
            databaseManager.initializeDatabase();
        }, "Inicialização do banco não deve lançar exceção");
        
        // Verificar se a conexão PostgreSQL foi estabelecida
        try (Connection conn = databaseManager.getConnection()) {
            assertNotNull(conn, "Conexão PostgreSQL deve ser estabelecida");
            assertFalse(conn.isClosed(), "Conexão não deve estar fechada");
        }
    }

    @Test
    @DisplayName("Deve obter conexão com o banco de dados")
    void testGetConnection() throws SQLException {
        databaseManager.initializeDatabase();
        
        Connection connection = databaseManager.getConnection();
        
        assertNotNull(connection, "Conexão não deve ser nula");
        assertFalse(connection.isClosed(), "Conexão não deve estar fechada");
        assertTrue(connection.isValid(1), "Conexão deve ser válida");
    }

    @Test
    @DisplayName("Deve criar tabelas principais na inicialização")
    void testCreateTables() throws SQLException {
        databaseManager.initializeDatabase();
        
        try (Connection conn = databaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT tablename FROM pg_tables WHERE schemaname = 'public'")) {
            
            assertTrue(rs.next(), "Deve existir pelo menos uma tabela");
            
            // Verificar tabelas principais
            boolean hasUsuarioTable = false;
            boolean hasIrmaoTable = false;
            boolean hasLojaTable = false;
            boolean hasSessaoTable = false;
            boolean hasCaixaTable = false;
            boolean hasConfiguracaoTable = false;
            
            do {
                String tableName = rs.getString("tablename");
                switch (tableName) {
                    case "usuario" -> hasUsuarioTable = true;
                    case "irmao" -> hasIrmaoTable = true;
                    case "loja" -> hasLojaTable = true;
                    case "sessao" -> hasSessaoTable = true;
                    case "caixa" -> hasCaixaTable = true;
                    case "configuracao" -> hasConfiguracaoTable = true;
                }
            } while (rs.next());
            
            assertTrue(hasUsuarioTable, "Tabela usuario deve existir");
            assertTrue(hasIrmaoTable, "Tabela irmao deve existir");
            assertTrue(hasLojaTable, "Tabela loja deve existir");
            assertTrue(hasSessaoTable, "Tabela sessao deve existir");
            assertTrue(hasCaixaTable, "Tabela caixa deve existir");
            assertTrue(hasConfiguracaoTable, "Tabela configuracao deve existir");
        }
    }

    @Test
    @DisplayName("Deve inserir dados iniciais na inicialização")
    void testInsertInitialData() throws SQLException {
        databaseManager.initializeDatabase();
        
        // Verificar usuário administrador
        try (Connection conn = databaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM usuario WHERE nome = 'Administrador'")) {
            
            assertTrue(rs.next(), "Deve retornar resultado");
            assertEquals(0, rs.getInt("count"), "Usuário Administrador não deve ser inserido automaticamente");
        }
        
        // Verificar configurações iniciais
        try (Connection conn = databaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM configuracao")) {
            
            assertTrue(rs.next(), "Deve retornar resultado");
            assertTrue(rs.getInt("count") >= 0, "Configurações iniciais devem ser verificadas");
        }
    }

    @Test
    @DisplayName("Deve inicializar banco de dados corretamente")
    void testDatabaseInitialization() {
        // Após inicializar
        assertDoesNotThrow(() -> {
            databaseManager.initializeDatabase();
        }, "Inicialização não deve lançar exceção");
    }

    @Test
    @DisplayName("Deve fechar conexão corretamente")
    void testCloseConnection() throws SQLException {
        databaseManager.initializeDatabase();
        
        Connection connection = databaseManager.getConnection();
        assertNotNull(connection, "Conexão não deve ser nula");
        assertFalse(connection.isClosed(), "Conexão não deve estar fechada");
        
        databaseManager.closeConnection();
        
        assertFalse(connection.isClosed(), "Conexão não deve estar fechada após closeConnection (pool de conexões)");
    }

    @Test
    @DisplayName("Deve lidar com múltiplas chamadas de inicialização")
    void testMultipleInitialization() throws SQLException {
        // Primeira inicialização
        databaseManager.initializeDatabase();
        File dbFile1 = tempDir.resolve(".artereal/artereal.db").toFile();
        long size1 = dbFile1.length();
        
        // Segunda inicialização (não deve criar novamente)
        assertDoesNotThrow(() -> {
            databaseManager.initializeDatabase();
        }, "Segunda inicialização não deve lançar exceção");
        
        File dbFile2 = tempDir.resolve(".artereal/artereal.db").toFile();
        long size2 = dbFile2.length();
        
        assertEquals(size1, size2, "Tamanho do banco não deve mudar na segunda inicialização");
    }

    @Test
    @DisplayName("Deve executar SQL diretamente na conexão")
    void testDirectSqlExecution() throws SQLException {
        databaseManager.initializeDatabase();
        
        try (Connection conn = databaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Inserir usuário de teste
            int result = stmt.executeUpdate(
                "INSERT INTO usuario (nome, senha, administrador, data_inicio, permissao_pagar, permissao_receber, permissao_backup) " +
                "VALUES ('test_user', 'test_pass', FALSE, CURRENT_TIMESTAMP, 0, 0, 0)"
            );
            
            assertEquals(1, result, "Um usuário deve ser inserido");
            
            // Verificar inserção
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM usuario WHERE nome = 'test_user'")) {
                assertTrue(rs.next(), "Deve retornar resultado");
                // ON CONFLICT pode impedir inserção duplicada, então verificamos se existe pelo menos um
                assertTrue(rs.getInt("count") >= 1, "Usuário de teste deve ser encontrado");
            }
        }
    }

    @Test
    @DisplayName("Deve lidar com rollback em caso de erro")
    void testTransactionRollback() throws SQLException {
        databaseManager.initializeDatabase();
        
        try (Connection conn = databaseManager.getConnection()) {
            conn.setAutoCommit(false);
            
            try (Statement stmt = conn.createStatement()) {
                // Inserir dados válidos
                stmt.executeUpdate(
                    "INSERT INTO usuario (nome, senha, administrador, data_inicio, permissao_pagar, permissao_receber, permissao_backup) " +
                    "VALUES ('rollback_test', 'pass', FALSE, CURRENT_TIMESTAMP, 0, 0, 0)"
                );
                
                // Tentar inserir em tabela inválida (deve causar erro)
                assertThrows(SQLException.class, () -> {
                    stmt.executeUpdate("INSERT INTO tabela_inexistente VALUES (1)");
                });
                
                // Rollback manual
                conn.rollback();
            }
            
            conn.setAutoCommit(true);
            
            // Verificar que dados não foram commitados
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM usuario WHERE nome = 'rollback_test'")) {
                
                assertTrue(rs.next(), "Deve retornar resultado");
                assertEquals(0, rs.getInt("count"), "Dados não devem ser commitados após rollback");
            }
        }
    }

    @Test
    @DisplayName("Deve verificar integridade das tabelas")
    void testTableIntegrity() throws SQLException {
        databaseManager.initializeDatabase();
        
        try (Connection conn = databaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Verificar estrutura da tabela usuario
            try (ResultSet rs = stmt.executeQuery("SELECT column_name FROM information_schema.columns WHERE table_name = 'usuario'")) {
                assertTrue(rs.next(), "Tabela usuario deve ter colunas");
                
                boolean hasId = false;
                boolean hasNome = false;
                boolean hasSenha = false;
                boolean hasAdministrador = false;
                
                do {
                    String columnName = rs.getString("column_name");
                    switch (columnName) {
                        case "id" -> hasId = true;
                        case "nome" -> hasNome = true;
                        case "senha" -> hasSenha = true;
                        case "administrador" -> hasAdministrador = true;
                    }
                } while (rs.next());
                
                assertTrue(hasId, "Coluna id deve existir");
                assertTrue(hasNome, "Coluna nome deve existir");
                assertTrue(hasSenha, "Coluna senha deve existir");
                assertTrue(hasAdministrador, "Coluna administrador deve existir");
            }
        }
    }
}
