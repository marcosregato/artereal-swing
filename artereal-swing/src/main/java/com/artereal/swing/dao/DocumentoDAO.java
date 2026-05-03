package com.artereal.swing.dao;

import com.artereal.swing.database.DatabaseManager;
import com.artereal.swing.model.Documento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operações com Documentos
 */
public class DocumentoDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(DocumentoDAO.class);
    
    /**
     * Salva ou atualiza um documento
     */
    public void save(Documento documento) throws SQLException {
        String sql;
        if (documento.getId() == null) {
            sql = """
                INSERT INTO documento (codigo_irmao, nome_arquivo, caminho_arquivo, tipo, descricao, 
                    data_upload, data_expiracao, status, assinatura_digital, hash_arquivo, 
                    tamanho_arquivo, formato_arquivo, usuario_upload, ativo, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
                """;
        } else {
            sql = """
                UPDATE documento SET codigo_irmao = ?, nome_arquivo = ?, caminho_arquivo = ?, tipo = ?, 
                    descricao = ?, data_upload = ?, data_expiracao = ?, status = ?, assinatura_digital = ?, 
                    hash_arquivo = ?, tamanho_arquivo = ?, formato_arquivo = ?, usuario_upload = ?, 
                    ativo = ?, updated_at = CURRENT_TIMESTAMP
                WHERE id = ?
                """;
        }
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setObject(1, documento.getCodigoIrmao());
            stmt.setString(2, documento.getNomeArquivo());
            stmt.setString(3, documento.getCaminhoArquivo());
            stmt.setString(4, documento.getTipo());
            stmt.setString(5, documento.getDescricao());
            stmt.setString(6, documento.getDataUpload() != null ? documento.getDataUpload().toString() : null);
            stmt.setString(7, documento.getDataExpiracao() != null ? documento.getDataExpiracao().toString() : null);
            stmt.setString(8, documento.getStatus());
            stmt.setString(9, documento.getAssinaturaDigital());
            stmt.setString(10, documento.getHashArquivo());
            stmt.setDouble(11, documento.getTamanhoArquivo());
            stmt.setString(12, documento.getFormatoArquivo());
            stmt.setString(13, documento.getUsuarioUpload());
            stmt.setInt(14, documento.isAtivo() ? 1 : 0);
            
            if (documento.getId() != null) {
                stmt.setLong(15, documento.getId());
            }
            
            int rowsAffected = stmt.executeUpdate();
            
            logger.info("DocumentoDAO - Linhas afetadas: {}", rowsAffected);
            logger.info("DocumentoDAO - SQL executado: {}", documento.getId() == null ? "INSERT" : "UPDATE");
            
            if (documento.getId() == null && rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        documento.setId(generatedKeys.getLong(1));
                        logger.info("DocumentoDAO - ID gerado: {}", documento.getId());
                    } else {
                        logger.warn("DocumentoDAO - Nenhuma chave gerada retornada");
                    }
                }
            }
            
            // Auto-commit está ativado, não需要 commit manual
            logger.info("Documento salvo com auto-commit: {}", documento.getNomeArquivo());
            logger.info("Documento ID final: {}", documento.getId());
        }
    }
    
    /**
     * Busca documento por ID
     */
    public Documento findById(Long id) throws SQLException {
        String sql = "SELECT * FROM documento WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDocumento(rs);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Lista todos os documentos
     */
    public List<Documento> findAll() throws SQLException {
        List<Documento> documentos = new ArrayList<>();
        String sql = "SELECT * FROM documento WHERE ativo = 1 ORDER BY data_upload DESC";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                documentos.add(mapResultSetToDocumento(rs));
            }
        }
        
        return documentos;
    }
    
    /**
     * Lista documentos por tipo
     */
    public List<Documento> findByTipo(String tipo) throws SQLException {
        List<Documento> documentos = new ArrayList<>();
        String sql = "SELECT * FROM documento WHERE tipo = ? AND ativo = 1 ORDER BY data_upload DESC";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, tipo);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    documentos.add(mapResultSetToDocumento(rs));
                }
            }
        }
        
        return documentos;
    }
    
    /**
     * Lista documentos por irmão
     */
    public List<Documento> findByIrmao(Long codigoIrmao) throws SQLException {
        List<Documento> documentos = new ArrayList<>();
        String sql = "SELECT * FROM documento WHERE codigo_irmao = ? AND ativo = 1 ORDER BY data_upload DESC";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, codigoIrmao);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    documentos.add(mapResultSetToDocumento(rs));
                }
            }
        }
        
        return documentos;
    }
    
    /**
     * Lista documentos expirados
     */
    public List<Documento> findExpirados() throws SQLException {
        List<Documento> documentos = new ArrayList<>();
        String sql = """
            SELECT * FROM documento 
            WHERE CAST(data_expiracao AS DATE) < CURRENT_DATE AND ativo = 1 
            ORDER BY CAST(data_expiracao AS DATE)
            """;
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                documentos.add(mapResultSetToDocumento(rs));
            }
        }
        
        return documentos;
    }
    
    /**
     * Lista documentos próximos à expiração
     */
    public List<Documento> findProximosExpiracao(int dias) throws SQLException {
        List<Documento> documentos = new ArrayList<>();
        String sql = """
            SELECT * FROM documento 
            WHERE CAST(data_expiracao AS DATE) BETWEEN CURRENT_DATE AND CURRENT_DATE + INTERVAL '{}' day 
            AND ativo = 1 
            ORDER BY data_expiracao
            """.replace("{}", String.valueOf(dias));
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                documentos.add(mapResultSetToDocumento(rs));
            }
        }
        
        return documentos;
    }
    
    /**
     * Conta todos os documentos
     */
    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM documento WHERE ativo = 1";
        
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
     * Conta documentos ativos
     */
    public int countAtivos() throws SQLException {
        String sql = "SELECT COUNT(*) FROM documento WHERE ativo = 1 AND status = 'ATIVO'";
        
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
     * Conta documentos expirados
     */
    public int countExpirados() throws SQLException {
        String sql = "SELECT COUNT(*) FROM documento WHERE CAST(data_expiracao AS DATE) < CURRENT_DATE AND ativo = 1";
        
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
     * Conta documentos próximos à expiração
     */
    public int countProximosExpiracao() throws SQLException {
        String sql = """
            SELECT COUNT(*) FROM documento 
            WHERE CAST(data_expiracao AS DATE) BETWEEN CURRENT_DATE AND CURRENT_DATE + INTERVAL '30 day' 
            AND ativo = 1
            """;
        
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
     * Exclui (desativa) um documento
     */
    public void delete(Long id) throws SQLException {
        String sql = "UPDATE documento SET ativo = 0 WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            
            // Auto-commit está ativado, não precisa commit manual
            logger.info("Documento desativado com auto-commit: ID {}", id);
        }
    }
    
    /**
     * Mapeia ResultSet para objeto Documento
     */
    private Documento mapResultSetToDocumento(ResultSet rs) throws SQLException {
        Documento documento = new Documento();
        
        documento.setId(rs.getLong("id"));
        documento.setCodigoIrmao(rs.getObject("codigo_irmao") != null ? rs.getLong("codigo_irmao") : null);
        documento.setNomeArquivo(rs.getString("nome_arquivo"));
        documento.setCaminhoArquivo(rs.getString("caminho_arquivo"));
        documento.setTipo(rs.getString("tipo"));
        documento.setDescricao(rs.getString("descricao"));
        
        String dataUploadStr = rs.getString("data_upload");
        if (dataUploadStr != null && !dataUploadStr.isEmpty()) {
            // Converter formato "YYYY-MM-DD HH:mm:ss" para "YYYY-MM-DDTHH:mm:ss"
            String dataFormatada = dataUploadStr.replace(" ", "T");
            documento.setDataUpload(LocalDateTime.parse(dataFormatada));
        }
        
        String dataExpiracaoStr = rs.getString("data_expiracao");
        if (dataExpiracaoStr != null && !dataExpiracaoStr.isEmpty()) {
            // Converter formato "YYYY-MM-DD HH:mm:ss" para "YYYY-MM-DDTHH:mm:ss"
            String dataFormatada = dataExpiracaoStr.replace(" ", "T");
            documento.setDataExpiracao(LocalDateTime.parse(dataFormatada));
        }
        
        documento.setStatus(rs.getString("status"));
        documento.setAssinaturaDigital(rs.getString("assinatura_digital"));
        documento.setHashArquivo(rs.getString("hash_arquivo"));
        documento.setTamanhoArquivo(rs.getDouble("tamanho_arquivo"));
        documento.setFormatoArquivo(rs.getString("formato_arquivo"));
        documento.setUsuarioUpload(rs.getString("usuario_upload"));
        documento.setAtivo(rs.getBoolean("ativo"));
        
        return documento;
    }
}
