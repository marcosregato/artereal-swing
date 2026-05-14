package com.artereal.swing.dao;

import com.artereal.swing.database.DatabaseManager;
import com.artereal.swing.model.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

/**
 * DAO para operações com Usuários no banco PostgreSQL
 */
public class UsuarioDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(UsuarioDAO.class);
    
    /**
     * Salva ou atualiza um usuário
     */
    public void save(Usuario usuario) throws SQLException {
        String sql;
        if (usuario.getId() == null) {
            sql = """
                INSERT INTO usuario (nome, senha, administrador, acesso, data_inicio, contas, 
                    lancamentos, classes, portaria, dados_usuario, permissao_backup, 
                    permissao_restaura, diretorio_servico, permissao_pagar, permissao_receber, ativo, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
                """;
        } else {
            sql = """
                UPDATE usuario SET nome = ?, senha = ?, administrador = ?, acesso = ?, data_inicio = ?, 
                    contas = ?, lancamentos = ?, classes = ?, portaria = ?, dados_usuario = ?, 
                    permissao_backup = ?, permissao_restaura = ?, diretorio_servico = ?, permissao_pagar = ?, 
                    permissao_receber = ?, ativo = ?, updated_at = CURRENT_TIMESTAMP
                WHERE id = ?
                """;
        }
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getSenha());
            stmt.setBoolean(3, usuario.isAdministrador());
            stmt.setString(4, usuario.getAcesso());
            stmt.setString(5, usuario.getDataInicio() != null ? usuario.getDataInicio().toString() : null);
            stmt.setString(6, usuario.getContas());
            stmt.setString(7, usuario.getLancamentos());
            stmt.setString(8, usuario.getClasses());
            stmt.setInt(9, usuario.isPortaria() ? 1 : 0);
            stmt.setString(10, usuario.getDadosUsuario());
            stmt.setInt(11, usuario.isPermissaoBackup() ? 1 : 0);
            stmt.setInt(12, usuario.isPermissaoRestaura() ? 1 : 0);
            stmt.setString(13, usuario.getDiretorioServico());
            stmt.setInt(14, usuario.isPermissaoPagar() ? 1 : 0);
            stmt.setInt(15, usuario.isPermissaoReceber() ? 1 : 0);
            stmt.setInt(16, 1); // ativo
            
            if (usuario.getId() != null) {
                stmt.setLong(17, usuario.getId());
            }
            
            int rowsAffected = stmt.executeUpdate();
            
            if (usuario.getId() == null && rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        usuario.setId(generatedKeys.getLong(1));
                    }
                }
            }
            
            logger.debug("Usuário salvo: {}", usuario.getNome());
        }
    }
    
    /**
     * Autentica usuário pelo nome e senha
     */
    public Usuario authenticate(String nome, String senha) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE nome = ? AND senha = ? AND ativo = 1";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nome);
            stmt.setString(2, senha);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUsuario(rs);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Busca usuário por ID
     */
    public Usuario findById(Long id) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUsuario(rs);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Lista todos os usuários ativos
     */
    public List<Usuario> findAll() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuario WHERE ativo = 1 ORDER BY nome";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                usuarios.add(mapResultSetToUsuario(rs));
            }
        }
        
        return usuarios;
    }
    
    /**
     * Busca usuários por nome
     */
    public List<Usuario> findByNome(String nome) throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuario WHERE nome LIKE ? AND ativo = 1 ORDER BY nome";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nome + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(mapResultSetToUsuario(rs));
                }
            }
        }
        
        return usuarios;
    }
    
    /**
     * Exclui (desativa) um usuário
     */
    public void delete(Long id) throws SQLException {
        String sql = "UPDATE usuario SET ativo = 0 WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            
            logger.debug("Usuário desativado: ID {}", id);
        }
    }
    
    /**
     * Conta usuários ativos
     */
    public int countActive() throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE ativo = 1";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        
        return 0;
    }
    
    /**
     * Verifica se existe administrador
     */
    public boolean hasAdministrator() throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM usuario WHERE administrador = TRUE AND ativo = 1";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        }
        
        return false;
    }
    
    /**
     * Mapeia ResultSet para objeto Usuario
     */
    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        
        usuario.setId(rs.getLong("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setSenha(rs.getString("senha"));
        usuario.setClasses(rs.getString("classes"));
        usuario.setPortaria(rs.getBoolean("portaria"));
        usuario.setDadosUsuario(rs.getString("dados_usuario"));
        usuario.setPermissaoBackup(rs.getBoolean("permissao_backup"));
        usuario.setPermissaoRestaura(rs.getBoolean("permissao_restaura"));
        usuario.setDiretorioServico(rs.getString("diretorio_servico"));
        usuario.setPermissaoPagar(rs.getBoolean("permissao_pagar"));
        usuario.setPermissaoReceber(rs.getBoolean("permissao_receber"));
        usuario.setAdministrador(rs.getBoolean("administrador"));
        
        return usuario;
    }
    
    /**
     * Conta usuários ativos (método de compatibilidade)
     */
    public int count() throws SQLException {
        return countActive();
    }
}
