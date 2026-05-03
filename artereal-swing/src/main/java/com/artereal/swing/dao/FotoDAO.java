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
                INSERT INTO foto (titulo, descricao, caminho_arquivo, nome_arquivo, categoria, evento, 
                    data_foto, data_upload, usuario_upload, tags, tamanho_arquivo, formato_arquivo, ativo, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
                """;
        } else {
            sql = """
                UPDATE foto SET titulo = ?, descricao = ?, caminho_arquivo = ?, nome_arquivo = ?, 
                    categoria = ?, evento = ?, data_foto = ?, usuario_upload = ?, tags = ?, 
                    tamanho_arquivo = ?, formato_arquivo = ?, ativo = ?, updated_at = CURRENT_TIMESTAMP
                WHERE id = ?
                """;
        }
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            if (foto.getId() == null) {
                stmt.setString(1, foto.getTitulo() != null ? foto.getTitulo() : foto.getDescricao());
                stmt.setString(2, foto.getDescricao());
                stmt.setString(3, foto.getCaminhoArquivo());
                stmt.setString(4, foto.getNomeArquivo() != null ? foto.getNomeArquivo() : "foto.jpg");
                stmt.setString(5, foto.getCategoria() != null ? foto.getCategoria() : "OUTRA");
                stmt.setString(6, null); // evento
                stmt.setString(7, foto.getDataFoto() != null ? foto.getDataFoto().toString() : null);
                stmt.setString(8, foto.getUsuarioUpload() != null ? foto.getUsuarioUpload() : "admin");
                stmt.setString(9, null); // tags
                stmt.setDouble(10, foto.getTamanhoArquivo() != null ? foto.getTamanhoArquivo() : 0.0);
                stmt.setString(11, foto.getFormatoArquivo() != null ? foto.getFormatoArquivo() : "jpg");
            } else {
                stmt.setString(1, foto.getTitulo() != null ? foto.getTitulo() : foto.getDescricao());
                stmt.setString(2, foto.getDescricao());
                stmt.setString(3, foto.getCaminhoArquivo());
                stmt.setString(4, foto.getNomeArquivo() != null ? foto.getNomeArquivo() : "foto.jpg");
                stmt.setString(5, foto.getCategoria() != null ? foto.getCategoria() : "OUTRA");
                stmt.setString(6, null); // evento
                stmt.setString(7, foto.getDataFoto() != null ? foto.getDataFoto().toString() : null);
                stmt.setString(8, foto.getUsuarioUpload() != null ? foto.getUsuarioUpload() : "admin");
                stmt.setString(9, null); // tags
                stmt.setDouble(10, foto.getTamanhoArquivo() != null ? foto.getTamanhoArquivo() : 0.0);
                stmt.setString(11, foto.getFormatoArquivo() != null ? foto.getFormatoArquivo() : "jpg");
                stmt.setInt(12, foto.isAtivo() ? 1 : 0);
                stmt.setLong(13, foto.getId());
            }
            
            int rowsAffected = stmt.executeUpdate();
            
            if (foto.getId() == null && rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        foto.setId(generatedKeys.getLong(1));
                    }
                }
            }
            
            logger.debug("Foto salva: {}", foto.getTitulo());
        }
    }
    
    /**
     * Busca foto por código (ID)
     */
    public Foto findById(Long id) throws SQLException {
        String sql = "SELECT * FROM foto WHERE id = ? AND ativo = 1";
        
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
        String sql = "SELECT * FROM foto WHERE categoria = ? AND ativo = 1 ORDER BY created_at DESC";
        
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
            SELECT categoria, COUNT(*) as quantidade
            FROM foto 
            WHERE ativo = 1 
            GROUP BY categoria 
            ORDER BY categoria
            """;
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Object[] estatistica = {
                    rs.getString("categoria"),
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
        String sql = "UPDATE foto SET ativo = 0 WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            logger.debug("Foto desativada: ID {}", id);
        }
    }
    
    /**
     * Mapeia ResultSet para objeto Foto
     */
    private Foto mapResultSetToFoto(ResultSet rs) throws SQLException {
        Foto foto = new Foto();
        
        foto.setId(rs.getLong("id"));
        foto.setTitulo(rs.getString("titulo"));
        foto.setDescricao(rs.getString("descricao"));
        foto.setCaminhoArquivo(rs.getString("caminho_arquivo"));
        foto.setCategoria(rs.getString("categoria"));
        foto.setAtivo(rs.getInt("ativo") == 1);
        
        String dataFotoStr = rs.getString("data_foto");
        if (dataFotoStr != null && !dataFotoStr.isEmpty()) {
            // Converter formato "YYYY-MM-DD HH:mm:ss" para "YYYY-MM-DDTHH:mm:ss"
            String dataFormatada = dataFotoStr.replace(" ", "T");
            foto.setDataFoto(LocalDateTime.parse(dataFormatada));
        }
        
        return foto;
    }
}
