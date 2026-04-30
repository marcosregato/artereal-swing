package com.artereal.swing.dao;

import com.artereal.swing.database.DatabaseManager;
import com.artereal.swing.model.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operações com Usuários no banco SQLite
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
            stmt.setBoolean(9, usuario.isPortaria());
            stmt.setString(10, usuario.getDadosUsuario());
            stmt.setBoolean(11, usuario.isPermissaoBackup());
            stmt.setBoolean(12, usuario.isPermissaoRestaura());
            stmt.setString(13, usuario.getDiretorioServico());
            stmt.setBoolean(14, usuario.isPermissaoPagar());
            stmt.setBoolean(15, usuario.isPermissaoReceber());
            stmt.setBoolean(16, true); // ativo
            
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
            
            conn.commit();
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
            conn.commit();
            
            logger.debug("Usuário desativado: ID {}", id);
        }
    }
    
    /**
     * Conta usuários ativos
     */
    public int count() throws SQLException {
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
        String sql = "SELECT COUNT(*) FROM usuario WHERE administrador = 1 AND ativo = 1";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
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
        usuario.setAdministrador(rs.getBoolean("administrador"));
        usuario.setAcesso(rs.getString("acesso"));
        
        String dataInicioStr = rs.getString("data_inicio");
        if (dataInicioStr != null && !dataInicioStr.isEmpty()) {
            // Converter formato "YYYY-MM-DD HH:mm:ss" para "YYYY-MM-DDTHH:mm:ss"
            String dataFormatada = dataInicioStr.replace(" ", "T");
            usuario.setDataInicio(LocalDateTime.parse(dataFormatada));
        }
        
        usuario.setContas(rs.getString("contas"));
        usuario.setLancamentos(rs.getString("lancamentos"));
        usuario.setClasses(rs.getString("classes"));
        usuario.setPortaria(rs.getBoolean("portaria"));
        usuario.setDadosUsuario(rs.getString("dados_usuario"));
        usuario.setPermissaoBackup(rs.getBoolean("permissao_backup"));
        usuario.setPermissaoRestaura(rs.getBoolean("permissao_restaura"));
        usuario.setDiretorioServico(rs.getString("diretorio_servico"));
        usuario.setPermissaoPagar(rs.getBoolean("permissao_pagar"));
        usuario.setPermissaoReceber(rs.getBoolean("permissao_receber"));
        
        return usuario;
    }
}
