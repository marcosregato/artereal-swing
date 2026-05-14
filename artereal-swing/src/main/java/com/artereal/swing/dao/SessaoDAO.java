package com.artereal.swing.dao;

import com.artereal.swing.database.DatabaseManager;
import com.artereal.swing.model.Sessao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

/**
 * DAO para gerenciamento de Sessões Maçônicas
 */
public class SessaoDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(SessaoDAO.class);
    private DatabaseManager dbManager;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public SessaoDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }
    
    public void save(Sessao sessao) throws SQLException {
        String sql;
        if (sessao.getId() == null) {
            sql = """
                INSERT INTO sessao (data, tipo, descricao, pauta, realizada, created_at, updated_at)
                VALUES (?, ?, ?, ?, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
                """;
        } else {
            sql = """
                UPDATE sessao SET data = ?, tipo = ?, descricao = ?, pauta = ?, 
                    realizada = ?, updated_at = CURRENT_TIMESTAMP
                WHERE id = ?
                """;
        }
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, sessao.getDataHora() != null ? sessao.getDataHora().format(formatter) : null);
            stmt.setString(2, sessao.getTipo());
            stmt.setString(3, sessao.getObservacoes());
            stmt.setString(4, sessao.getPauta());
            
            if (sessao.getId() != null) {
                stmt.setInt(5, sessao.getStatus() != null && sessao.getStatus().equals("REALIZADA") ? 1 : 0);
                stmt.setLong(6, sessao.getId());
            }
            
            int affectedRows = stmt.executeUpdate();
            
            if (sessao.getId() == null && affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        sessao.setId(generatedKeys.getLong(1));
                    }
                }
            }
        }
    }
    
    public Sessao findById(Long id) throws SQLException {
        String sql = "SELECT * FROM sessao WHERE id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSessao(rs);
                }
            }
        }
        return null;
    }
    
    public List<Sessao> findAll() throws SQLException {
        logger.info("Buscando todas as sessões no banco de dados");
        List<Sessao> sessoes = new ArrayList<>();
        String sql = "SELECT * FROM sessao ORDER BY data DESC";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                sessoes.add(mapResultSetToSessao(rs));
            }
            logger.info("Encontradas {} sessões no banco", sessoes.size());
        } catch (SQLException e) {
            logger.error("Erro ao buscar sessões: {}", e.getMessage(), e);
            throw e;
        }
        return sessoes;
    }
    
    public List<Sessao> findByStatus(String status) throws SQLException {
        List<Sessao> sessoes = new ArrayList<>();
        // Como a tabela não tem coluna 'status', vamos usar 'realizada'
        String sql;
        if ("REALIZADA".equals(status)) {
            sql = "SELECT * FROM sessao WHERE realizada = 1 ORDER BY data DESC";
        } else if ("PROGRAMADA".equals(status)) {
            sql = "SELECT * FROM sessao WHERE realizada = 0 ORDER BY data DESC";
        } else {
            sql = "SELECT * FROM sessao ORDER BY data DESC";
        }
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                sessoes.add(mapResultSetToSessao(rs));
            }
        }
        return sessoes;
    }
    
    public List<Sessao> findByTipo(String tipo) throws SQLException {
        List<Sessao> sessoes = new ArrayList<>();
        // A tabela tem coluna 'tipo', então podemos usar normalmente
        String sql = "SELECT * FROM sessao WHERE tipo = ? ORDER BY data DESC";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, tipo);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    sessoes.add(mapResultSetToSessao(rs));
                }
            }
        }
        return sessoes;
    }
    
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM sessao WHERE id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
    
    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM sessao";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    private Sessao mapResultSetToSessao(ResultSet rs) throws SQLException {
        logger.debug("Mapeando ResultSet para Sessao");
        Sessao sessao = new Sessao();
        sessao.setId(rs.getLong("id"));
        sessao.setTipo(rs.getString("tipo"));
        logger.debug("Sessao ID: {}, Tipo: {}", sessao.getId(), sessao.getTipo());
        
        // Parse data com tratamento de erro
        String dataStr = rs.getString("data");
        if (dataStr != null && !dataStr.isEmpty()) {
            try {
                LocalDateTime dataHora;
                if (dataStr.contains(" ")) {
                    dataHora = LocalDateTime.parse(dataStr, formatter);
                } else {
                    dataHora = LocalDateTime.parse(dataStr + " 00:00:00", formatter);
                }
                sessao.setDataHora(dataHora);
                logger.debug("Data parseada com sucesso: {} -> {}", dataStr, dataHora);
            } catch (Exception e) {
                logger.warn("Erro ao converter data '{}': {}", dataStr, e.getMessage());
                sessao.setDataHora(null);
            }
        } else {
            logger.debug("Data é nula ou vazia para sessao ID: {}", sessao.getId());
        }
        
        sessao.setPauta(rs.getString("descricao"));
        try {
            sessao.setQuantidadePresentes(rs.getInt("quantidade_presentes"));
        } catch (Exception e) {
            // Se coluna não existir, usar valor padrão
            sessao.setQuantidadePresentes(0);
        }
        // Campo 'realizada' não existe no modelo, vamos usar 'status'
        boolean realizada = rs.getBoolean("realizada");
        sessao.setStatus(realizada ? "REALIZADA" : "PROGRAMADA");
        // Usar coluna 'ata' em vez de 'observacoes' que não existe
        try {
            sessao.setObservacoes(rs.getString("ata"));
        } catch (Exception e) {
            // Se coluna não existir, usar valor padrão
            sessao.setObservacoes("");
        }
        
        logger.debug("Sessao mapeada: ID={}, Tipo={}, Data={}, Presenças={}, Status={}", 
                    sessao.getId(), sessao.getTipo(), sessao.getDataHora(), 
                    sessao.getQuantidadePresentes(), sessao.getStatus());
        
        return sessao;
    }
}
