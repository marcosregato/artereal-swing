package com.artereal.swing.dao;

import com.artereal.swing.database.DatabaseManager;
import com.artereal.swing.model.Foto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operações com Fotos
 */
public class FotoDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(FotoDAO.class);
    
    /**
     * Salva ou atualiza uma foto
     */
    public void save(Foto foto) throws SQLException {
        String sql;
        if (foto.getId() == null) {
            sql = """
                INSERT INTO foto (codigo, foto, descricao, data_foto, tipo_foto, ativo, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
                """;
        } else {
            sql = """
                UPDATE foto SET foto = ?, descricao = ?, data_foto = ?, tipo_foto = ?, 
                    ativo = ?, updated_at = CURRENT_TIMESTAMP
                WHERE codigo = ?
                """;
        }
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            if (foto.getId() == null) {
                stmt.setInt(1, (int)(System.currentTimeMillis() % 1000000)); // Gerar código único
                stmt.setString(2, foto.getCaminhoArquivo());
                stmt.setString(3, foto.getDescricao());
                stmt.setString(4, foto.getDataFoto() != null ? foto.getDataFoto().toString() : null);
                stmt.setString(5, foto.getCategoria() != null ? foto.getCategoria() : "IRMAO");
                stmt.setBoolean(6, foto.isAtivo());
            } else {
                stmt.setString(1, foto.getCaminhoArquivo());
                stmt.setString(2, foto.getDescricao());
                stmt.setString(3, foto.getDataFoto() != null ? foto.getDataFoto().toString() : null);
                stmt.setString(4, foto.getCategoria() != null ? foto.getCategoria() : "IRMAO");
                stmt.setBoolean(5, foto.isAtivo());
                stmt.setInt(6, foto.getId().intValue());
            }
            
            int rowsAffected = stmt.executeUpdate();
            
            if (foto.getId() == null && rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        foto.setId(generatedKeys.getLong(1));
                    }
                }
            }
            
            conn.commit();
            logger.debug("Foto salva: {}", foto.getTitulo());
        }
    }
    
    /**
     * Busca foto por código (ID)
     */
    public Foto findById(Long id) throws SQLException {
        String sql = "SELECT * FROM foto WHERE codigo = ? AND ativo = 1";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFoto(rs);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Lista todas as fotos ativas
     */
    public List<Foto> findAll() throws SQLException {
        List<Foto> fotos = new ArrayList<>();
        String sql = "SELECT * FROM foto WHERE ativo = 1 ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                fotos.add(mapResultSetToFoto(rs));
            }
        }
        
        return fotos;
    }
    
    /**
     * Lista fotos por tipo (categoria)
     */
    public List<Foto> findByCategoria(String categoria) throws SQLException {
        List<Foto> fotos = new ArrayList<>();
        String sql = "SELECT * FROM foto WHERE tipo_foto = ? AND ativo = 1 ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, categoria);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    fotos.add(mapResultSetToFoto(rs));
                }
            }
        }
        
        return fotos;
    }
    
    /**
     * Conta todas as fotos ativas
     */
    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM foto WHERE ativo = 1";
        
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
     * Obtém estatísticas das fotos
     */
    public List<Object[]> getEstatisticas() throws SQLException {
        List<Object[]> estatisticas = new ArrayList<>();
        String sql = """
            SELECT tipo_foto, COUNT(*) as quantidade
            FROM foto 
            WHERE ativo = 1 
            GROUP BY tipo_foto 
            ORDER BY tipo_foto
            """;
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Object[] estatistica = {
                    rs.getString("tipo_foto"),
                    rs.getInt("quantidade")
                };
                estatisticas.add(estatistica);
            }
        }
        
        return estatisticas;
    }
    
    /**
     * Exclui (desativa) uma foto
     */
    public void delete(Long id) throws SQLException {
        String sql = "UPDATE foto SET ativo = 0 WHERE codigo = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            conn.commit();
            
            logger.debug("Foto desativada: ID {}", id);
        }
    }
    
    /**
     * Mapeia ResultSet para objeto Foto
     */
    private Foto mapResultSetToFoto(ResultSet rs) throws SQLException {
        Foto foto = new Foto();
        
        foto.setId(rs.getLong("codigo"));
        foto.setCaminhoArquivo(rs.getString("foto"));
        foto.setDescricao(rs.getString("descricao"));
        foto.setCategoria(rs.getString("tipo_foto"));
        foto.setAtivo(rs.getBoolean("ativo"));
        
        String dataFotoStr = rs.getString("data_foto");
        if (dataFotoStr != null && !dataFotoStr.isEmpty()) {
            // Converter formato "YYYY-MM-DD HH:mm:ss" para "YYYY-MM-DDTHH:mm:ss"
            String dataFormatada = dataFotoStr.replace(" ", "T");
            foto.setDataFoto(LocalDateTime.parse(dataFormatada));
        }
        
        // Para compatibilidade, usar a descrição como título
        foto.setTitulo(rs.getString("descricao") != null ? rs.getString("descricao") : "Foto sem título");
        
        return foto;
    }
}
