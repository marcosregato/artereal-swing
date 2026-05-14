package com.artereal.swing.dao;

import com.artereal.swing.database.DatabaseManager;
import com.artereal.swing.model.Usuario;
import com.artereal.swing.validation.ValidationResult;
import com.artereal.swing.validation.ValidationService;
import com.artereal.swing.validation.ValidationService.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

/**
 * DAO para operações com Usuários no banco PostgreSQL com validação integrada
 */
public class UsuarioDAOValidated {
    
    private static final Logger logger = LoggerFactory.getLogger(UsuarioDAOValidated.class);
    
    /**
     * Salva ou atualiza um usuário com validação
     */
    public void save(Usuario usuario) throws SQLException, ValidationException {
        // Validar usuário antes de salvar
        if (usuario.getId() == null) {
            // Validação para criação
            ValidationResult result = ValidationService.validateUsuarioForCreation(usuario);
            if (result.isInvalid()) {
                throw new ValidationException("Erro de validação ao criar usuário: " + result.getErrorMessage());
            }
        } else {
            // Validação para atualização
            ValidationResult result = ValidationService.validateUsuarioForUpdate(usuario);
            if (result.isInvalid()) {
                throw new ValidationException("Erro de validação ao atualizar usuário: " + result.getErrorMessage());
            }
        }
        
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
            stmt.setBoolean(16, true); // Usuários são sempre ativos por padrão
            
            if (usuario.getId() != null) {
                stmt.setLong(17, usuario.getId());
            }
            
            int affectedRows = stmt.executeUpdate();
            
            if (usuario.getId() == null && affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        usuario.setId(generatedKeys.getLong(1));
                    }
                }
            }
            
            logger.debug("Usuário salvo: {}", usuario.getNome());
            
        } catch (SQLException e) {
            logger.error("Erro ao salvar usuário: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Autentica usuário pelo nome e senha com validação
     */
    public Usuario authenticate(String nome, String senha) throws SQLException, ValidationException {
        // Validar credenciais antes de autenticar
        Usuario credenciais = new Usuario(nome, senha);
        ValidationResult result = ValidationService.validateUsuarioForAuthentication(credenciais);
        if (result.isInvalid()) {
            throw new ValidationException("Credenciais inválidas: " + result.getErrorMessage());
        }
        
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
            
        } catch (SQLException e) {
            logger.error("Erro ao autenticar usuário: {}", e.getMessage(), e);
            throw e;
        }
        
        return null;
    }
    
    /**
     * Altera senha do usuário com validação
     */
    public void changePassword(Long usuarioId, String novaSenha) throws SQLException, ValidationException {
        // Validar nova senha
        ValidationResult result = ValidationService.validatePassword(novaSenha);
        if (result.isInvalid()) {
            throw new ValidationException("Nova senha inválida: " + result.getErrorMessage());
        }
        
        String sql = "UPDATE usuario SET senha = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, novaSenha);
            stmt.setLong(2, usuarioId);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Usuário não encontrado para alterar senha");
            }
            
            logger.debug("Senha alterada para usuário ID: {}", usuarioId);
            
        } catch (SQLException e) {
            logger.error("Erro ao alterar senha: {}", e.getMessage(), e);
            throw e;
        }
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
            
        } catch (SQLException e) {
            logger.error("Erro ao buscar usuário por ID: {}", e.getMessage(), e);
            throw e;
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
            
        } catch (SQLException e) {
            logger.error("Erro ao listar usuários: {}", e.getMessage(), e);
            throw e;
        }
        
        return usuarios;
    }
    
    /**
     * Busca usuários por nome
     */
    public List<Usuario> findByNome(String nome) throws SQLException, ValidationException {
        // Validar critério de busca
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidationException("Nome para busca não pode ser vazio");
        }
        
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuario WHERE nome LIKE ? AND ativo = 1 ORDER BY nome";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nome.trim() + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(mapResultSetToUsuario(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao buscar usuários por nome: {}", e.getMessage(), e);
            throw e;
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
            
        } catch (SQLException e) {
            logger.error("Erro ao desativar usuário: {}", e.getMessage(), e);
            throw e;
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
            
        } catch (SQLException e) {
            logger.error("Erro ao contar usuários: {}", e.getMessage(), e);
            throw e;
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
            
        } catch (SQLException e) {
            logger.error("Erro ao verificar administrador: {}", e.getMessage(), e);
            throw e;
        }
        
        return false;
    }
    
    /**
     * Mapeia ResultSet para Usuario
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
            try {
                usuario.setDataInicio(LocalDateTime.parse(dataInicioStr));
            } catch (Exception e) {
                logger.warn("Erro ao converter data_inicio: {}", dataInicioStr);
            }
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
        // Campo ativo não existe no modelo Usuario
        
        return usuario;
    }
}
