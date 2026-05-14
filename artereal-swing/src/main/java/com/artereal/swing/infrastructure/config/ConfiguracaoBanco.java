package com.artereal.swing.infrastructure.config;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Configuração do banco de dados standalone
 * 
 * Esta classe fornece configuração para conexão com PostgreSQL
 * sem dependências do Spring, tornando o sistema independente.
 */
public class ConfiguracaoBanco {
    
    private static final String URL = "jdbc:postgresql://localhost:5432/artereal_db";
    private static final String USUARIO = "system";
    private static final String SENHA = "system";
    private static final String DRIVER = "org.postgresql.Driver";
    
    /**
     * Cria e retorna uma conexão com o banco PostgreSQL
     */
    public static Connection criarConexao() throws SQLException {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver PostgreSQL não encontrado", e);
        }
    }
    
    /**
     * Cria DataSource para uso com repositórios
     */
    public static DataSource criarDataSource() {
        try {
            // Usar implementação simples de DataSource
            return new SimpleDataSource(URL, USUARIO, SENHA, DRIVER);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar DataSource", e);
        }
    }
    
    /**
     * Implementação simples de DataSource
     */
    public static class SimpleDataSource implements DataSource {
        private final String url;
        private final String usuario;
        private final String senha;
        private final String driver;
        
        public SimpleDataSource(String url, String usuario, String senha, String driver) {
            this.url = url;
            this.usuario = usuario;
            this.senha = senha;
            this.driver = driver;
        }
        
        @Override
        public Connection getConnection() throws SQLException {
            try {
                Class.forName(driver);
                return DriverManager.getConnection(url, usuario, senha);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver não encontrado", e);
            }
        }
        
        @Override
        public Connection getConnection(String username, String password) throws SQLException {
            try {
                Class.forName(driver);
                return DriverManager.getConnection(url, username, password);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver não encontrado", e);
            }
        }
        
        // Outros métodos necessários da interface DataSource
        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            if (iface.isAssignableFrom(getClass())) {
                return iface.cast(this);
            }
            throw new SQLException("Não é um wrapper para " + iface.getName());
        }
        
        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return iface.isAssignableFrom(getClass());
        }
        
        @Override
        public java.io.PrintWriter getLogWriter() throws SQLException {
            return null;
        }
        
        @Override
        public void setLogWriter(java.io.PrintWriter out) throws SQLException {
            // Implementação vazia
        }
        
        @Override
        public void setLoginTimeout(int seconds) throws SQLException {
            // Implementação vazia
        }
        
        @Override
        public int getLoginTimeout() throws SQLException {
            return 0;
        }
        
        @Override
        public java.util.logging.Logger getParentLogger() throws java.sql.SQLFeatureNotSupportedException {
            return java.util.logging.Logger.getLogger(ConfiguracaoBanco.class.getName());
        }
    }
}
