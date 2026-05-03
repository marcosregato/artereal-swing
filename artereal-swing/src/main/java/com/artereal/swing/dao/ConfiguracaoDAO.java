package com.artereal.swing.dao;

import com.artereal.swing.database.DatabaseManager;
import com.artereal.swing.model.Configuracao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operações com Configurações Globais
 */
public class ConfiguracaoDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(ConfiguracaoDAO.class);
    
    /**
     * Salva ou atualiza uma configuração
     */
    public void save(Configuracao configuracao) throws SQLException {
        String sql;
        if (configuracao.getId() == null) {
            sql = """
                INSERT INTO configuracao (chave, valor, descricao, tipo, categoria, editavel, visivel, 
                    data_atualizacao, usuario_atualizacao, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
                """;
        } else {
            sql = """
                UPDATE configuracao SET chave = ?, valor = ?, descricao = ?, tipo = ?, categoria = ?, 
                    editavel = ?, visivel = ?, data_atualizacao = CURRENT_TIMESTAMP, usuario_atualizacao = ?, 
                    updated_at = CURRENT_TIMESTAMP
                WHERE id = ?
                """;
        }
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, configuracao.getChave());
            stmt.setString(2, configuracao.getValor());
            stmt.setString(3, configuracao.getDescricao());
            stmt.setString(4, configuracao.getTipo());
            stmt.setString(5, configuracao.getCategoria());
            stmt.setInt(6, configuracao.isEditavel() ? 1 : 0);
            stmt.setInt(7, configuracao.isVisivel() ? 1 : 0);
            stmt.setString(8, configuracao.getUsuarioAtualizacao());
            
            if (configuracao.getId() != null) {
                stmt.setLong(9, configuracao.getId());
            }
            
            int rowsAffected = stmt.executeUpdate();
            
            if (configuracao.getId() == null && rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        configuracao.setId(generatedKeys.getLong(1));
                    }
                }
            }
            
            logger.debug("Configuração salva: {}", configuracao.getChave());
        }
    }
    
    /**
     * Busca configuração por chave
     */
    public Configuracao findByChave(String chave) throws SQLException {
        String sql = "SELECT * FROM configuracao WHERE chave = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, chave);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToConfiguracao(rs);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Busca configuração por chave com valor padrão
     */
    public Configuracao findByChaveOrDefault(String chave, String valorPadrao, String descricao, String tipo, String categoria) throws SQLException {
        Configuracao config = findByChave(chave);
        if (config == null) {
            config = new Configuracao(chave, valorPadrao, descricao, tipo, categoria);
            save(config);
        }
        return config;
    }
    
    /**
     * Lista todas as configurações
     */
    public List<Configuracao> findAll() throws SQLException {
        List<Configuracao> configuracoes = new ArrayList<>();
        String sql = "SELECT * FROM configuracao ORDER BY categoria, chave";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                configuracoes.add(mapResultSetToConfiguracao(rs));
            }
        }
        
        return configuracoes;
    }
    
    /**
     * Lista configurações por categoria
     */
    public List<Configuracao> findByCategoria(String categoria) throws SQLException {
        List<Configuracao> configuracoes = new ArrayList<>();
        String sql = "SELECT * FROM configuracao WHERE categoria = ? ORDER BY chave";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, categoria);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    configuracoes.add(mapResultSetToConfiguracao(rs));
                }
            }
        }
        
        return configuracoes;
    }
    
    /**
     * Lista configurações visíveis
     */
    public List<Configuracao> findVisiveis() throws SQLException {
        List<Configuracao> configuracoes = new ArrayList<>();
        String sql = "SELECT * FROM configuracao WHERE visivel = 1 ORDER BY categoria, chave";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                configuracoes.add(mapResultSetToConfiguracao(rs));
            }
        }
        
        return configuracoes;
    }
    
    /**
     * Lista configurações editáveis
     */
    public List<Configuracao> findEditaveis() throws SQLException {
        List<Configuracao> configuracoes = new ArrayList<>();
        String sql = "SELECT * FROM configuracao WHERE editavel = 1 AND visivel = 1 ORDER BY categoria, chave";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                configuracoes.add(mapResultSetToConfiguracao(rs));
            }
        }
        
        return configuracoes;
    }
    
    /**
     * Obtém valor de configuração como String
     */
    public String getValorString(String chave) throws SQLException {
        Configuracao config = findByChave(chave);
        return config != null ? config.getValor() : null;
    }
    
    /**
     * Obtém valor de configuração como String com padrão
     */
    public String getValorString(String chave, String valorPadrao) throws SQLException {
        Configuracao config = findByChave(chave);
        return config != null ? config.getValor() : valorPadrao;
    }
    
    /**
     * Obtém valor de configuração como boolean
     */
    public boolean getValorBoolean(String chave) throws SQLException {
        Configuracao config = findByChave(chave);
        return config != null ? config.getValorAsBoolean() : false;
    }
    
    /**
     * Obtém valor de configuração como boolean com padrão
     */
    public boolean getValorBoolean(String chave, boolean valorPadrao) throws SQLException {
        Configuracao config = findByChave(chave);
        return config != null ? config.getValorAsBoolean() : valorPadrao;
    }
    
    /**
     * Obtém valor de configuração como inteiro
     */
    public int getValorInt(String chave) throws SQLException {
        Configuracao config = findByChave(chave);
        return config != null ? config.getValorAsInt() : 0;
    }
    
    /**
     * Obtém valor de configuração como inteiro com padrão
     */
    public int getValorInt(String chave, int valorPadrao) throws SQLException {
        Configuracao config = findByChave(chave);
        return config != null ? config.getValorAsInt() : valorPadrao;
    }
    
    /**
     * Define valor de configuração
     */
    public void setValor(String chave, String valor, String usuario) throws SQLException {
        Configuracao config = findByChave(chave);
        if (config != null && config.isEditavel()) {
            config.setValor(valor);
            config.setUsuarioAtualizacao(usuario);
            save(config);
        }
    }
    
    /**
     * Define valor boolean de configuração
     */
    public void setValorBoolean(String chave, boolean valor, String usuario) throws SQLException {
        Configuracao config = findByChave(chave);
        if (config != null && config.isEditavel()) {
            config.setValorAsBoolean(valor);
            config.setUsuarioAtualizacao(usuario);
            save(config);
        }
    }
    
    /**
     * Define valor inteiro de configuração
     */
    public void setValorInt(String chave, int valor, String usuario) throws SQLException {
        Configuracao config = findByChave(chave);
        if (config != null && config.isEditavel()) {
            config.setValorAsInt(valor);
            config.setUsuarioAtualizacao(usuario);
            save(config);
        }
    }
    
    /**
     * Exclui uma configuração
     */
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM configuracao WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            logger.debug("Configuração excluída: ID {}", id);
        }
    }
    
    /**
     * Obtém categorias distintas
     */
    public List<String> getCategorias() throws SQLException {
        List<String> categorias = new ArrayList<>();
        String sql = "SELECT DISTINCT categoria FROM configuracao ORDER BY categoria";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                categorias.add(rs.getString("categoria"));
            }
        }
        
        return categorias;
    }
    
    /**
     * Mapeia ResultSet para objeto Configuracao
     */
    private Configuracao mapResultSetToConfiguracao(ResultSet rs) throws SQLException {
        Configuracao configuracao = new Configuracao();
        
        configuracao.setId(rs.getLong("id"));
        configuracao.setChave(rs.getString("chave"));
        configuracao.setValor(rs.getString("valor"));
        configuracao.setDescricao(rs.getString("descricao"));
        configuracao.setTipo(rs.getString("tipo"));
        configuracao.setCategoria(rs.getString("categoria"));
        configuracao.setEditavel(rs.getBoolean("editavel"));
        configuracao.setVisivel(rs.getBoolean("visivel"));
        
        String dataAtualizacaoStr = rs.getString("data_atualizacao");
        if (dataAtualizacaoStr != null && !dataAtualizacaoStr.isEmpty()) {
            try {
                // Tentar parsing direto primeiro
                configuracao.setDataAtualizacao(LocalDateTime.parse(dataAtualizacaoStr));
            } catch (Exception e) {
                try {
                    // Parsing manual para formatos complexos com timezone
                    if (dataAtualizacaoStr.contains(" ")) {
                        // Remover timezone e microssegundos se presentes
                        String dataLimpa = dataAtualizacaoStr;
                        
                        // Remover timezone se presente (ex: -03, -03:00)
                        if (dataLimpa.matches(".*-\\d{2}:?\\d{0,2}$")) {
                            dataLimpa = dataLimpa.replaceAll("-\\d{2}:?\\d{0,2}$", "");
                        }
                        
                        // Remover microssegundos se presentes
                        if (dataLimpa.contains(".")) {
                            dataLimpa = dataLimpa.replaceAll("\\.\\d+$", "");
                        }
                        
                        // Converter para formato ISO
                        dataLimpa = dataLimpa.replace(" ", "T");
                        configuracao.setDataAtualizacao(LocalDateTime.parse(dataLimpa));
                    } else {
                        // Usar parsing com formato específico como fallback
                        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        configuracao.setDataAtualizacao(LocalDateTime.parse(dataAtualizacaoStr, formatter));
                    }
                } catch (Exception e2) {
                    // Usar parsing com formato específico como fallback final
                    java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    configuracao.setDataAtualizacao(LocalDateTime.parse(dataAtualizacaoStr, formatter));
                }
            }
        }
        
        configuracao.setUsuarioAtualizacao(rs.getString("usuario_atualizacao"));
        
        return configuracao;
    }
}
