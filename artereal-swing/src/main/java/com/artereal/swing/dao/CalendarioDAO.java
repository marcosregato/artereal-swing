package com.artereal.swing.dao;

import com.artereal.swing.database.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operações com Calendário Maçônico
 */
public class CalendarioDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(CalendarioDAO.class);
    
    /**
     * Salva ou atualiza um evento no calendário
     */
    public void save(String descricao, String dataInforme, String tipoEvento, String local, String horario, Long codigoIrmao) throws SQLException {
        String sql = """
            INSERT INTO calendario (descricao, data_informe, tipo_evento, status, local, horario, codigo_irmao, updated_at)
            VALUES (?, ?, ?, 'PROGRAMADO', ?, ?, ?, CURRENT_TIMESTAMP)
            ON CONFLICT (id) DO UPDATE SET
                descricao = EXCLUDED.descricao,
                data_informe = EXCLUDED.data_informe,
                tipo_evento = EXCLUDED.tipo_evento,
                status = EXCLUDED.status,
                local = EXCLUDED.local,
                horario = EXCLUDED.horario,
                codigo_irmao = EXCLUDED.codigo_irmao,
                updated_at = CURRENT_TIMESTAMP
            """;
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, descricao);
            stmt.setString(2, dataInforme);
            stmt.setString(3, tipoEvento);
            stmt.setString(4, local);
            stmt.setString(5, horario);
            stmt.setObject(6, codigoIrmao);
            
            stmt.executeUpdate();
            logger.debug("Evento salvo no calendário: {}", descricao);
        }
    }
    
    /**
     * Lista todos os eventos
     */
    public List<Object[]> findAll() throws SQLException {
        List<Object[]> eventos = new ArrayList<>();
        String sql = """
            SELECT id, descricao, data_informe, tipo_evento, status, local, horario, codigo_irmao
            FROM calendario 
            ORDER BY data_informe
            """;
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Object[] evento = {
                    rs.getLong("id"),
                    rs.getString("descricao"),
                    rs.getString("data_informe"),
                    rs.getString("tipo_evento"),
                    rs.getString("status"),
                    rs.getString("local"),
                    rs.getString("horario"),
                    rs.getObject("codigo_irmao")
                };
                eventos.add(evento);
            }
        }
        
        return eventos;
    }
    
    /**
     * Lista eventos por período
     */
    public List<Object[]> findByPeriodo(LocalDate dataInicio, LocalDate dataFim) throws SQLException {
        List<Object[]> eventos = new ArrayList<>();
        String sql = """
            SELECT id, descricao, data_informe, tipo_evento, status, local, horario, codigo_irmao
            FROM calendario 
            WHERE data_informe BETWEEN ? AND ?
            ORDER BY data_informe
            """;
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, dataInicio.toString());
            stmt.setString(2, dataFim.toString());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object[] evento = {
                        rs.getLong("id"),
                        rs.getString("descricao"),
                        rs.getString("data_informe"),
                        rs.getString("tipo_evento"),
                        rs.getString("status"),
                        rs.getString("local"),
                        rs.getString("horario"),
                        rs.getObject("codigo_irmao")
                    };
                    eventos.add(evento);
                }
            }
        }
        
        return eventos;
    }
    
    /**
     * Lista eventos por tipo
     */
    public List<Object[]> findByTipo(String tipoEvento) throws SQLException {
        List<Object[]> eventos = new ArrayList<>();
        String sql = """
            SELECT id, descricao, data_informe, tipo_evento, status, local, horario, codigo_irmao
            FROM calendario 
            WHERE tipo_evento = ?
            ORDER BY data_informe
            """;
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, tipoEvento);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object[] evento = {
                        rs.getLong("id"),
                        rs.getString("descricao"),
                        rs.getString("data_informe"),
                        rs.getString("tipo_evento"),
                        rs.getString("status"),
                        rs.getString("local"),
                        rs.getString("horario"),
                        rs.getObject("codigo_irmao")
                    };
                    eventos.add(evento);
                }
            }
        }
        
        return eventos;
    }
    
    /**
     * Lista eventos próximos
     */
    public List<Object[]> findProximos(int dias) throws SQLException {
        List<Object[]> eventos = new ArrayList<>();
        String sql = """
            SELECT id, descricao, data_informe, tipo_evento, status, local, horario, codigo_irmao
            FROM calendario 
            WHERE CAST(data_informe AS DATE) BETWEEN CURRENT_DATE AND CURRENT_DATE + INTERVAL '{} days'
            ORDER BY CAST(data_informe AS DATE)
            """.replace("{}", String.valueOf(dias));
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Object[] evento = {
                    rs.getLong("id"),
                    rs.getString("descricao"),
                    rs.getString("data_informe"),
                    rs.getString("tipo_evento"),
                    rs.getString("status"),
                    rs.getString("local"),
                    rs.getString("horario"),
                    rs.getObject("codigo_irmao")
                };
                eventos.add(evento);
            }
        }
        
        return eventos;
    }
    
    /**
     * Lista eventos de hoje
     */
    public List<Object[]> findHoje() throws SQLException {
        List<Object[]> eventos = new ArrayList<>();
        String sql = """
            SELECT id, descricao, data_informe, tipo_evento, status, local, horario, codigo_irmao
            FROM calendario 
            WHERE CAST(data_informe AS DATE) = CURRENT_DATE
            ORDER BY horario
            """;
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Object[] evento = {
                    rs.getLong("id"),
                    rs.getString("descricao"),
                    rs.getString("data_informe"),
                    rs.getString("tipo_evento"),
                    rs.getString("status"),
                    rs.getString("local"),
                    rs.getString("horario"),
                    rs.getObject("codigo_irmao")
                };
                eventos.add(evento);
            }
        }
        
        return eventos;
    }
    
    /**
     * Marca evento como realizado
     */
    public void realizar(Long id) throws SQLException {
        String sql = "UPDATE calendario SET status = 'REALIZADO', updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            logger.debug("Evento realizado: ID {}", id);
        }
    }
    
    /**
     * Cancela um evento
     */
    public void cancelar(Long id) throws SQLException {
        String sql = "UPDATE calendario SET status = 'CANCELADO', updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            logger.debug("Evento cancelado: ID {}", id);
        }
    }
    
    /**
     * Exclui um evento
     */
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM calendario WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            logger.debug("Evento excluído: ID {}", id);
        }
    }
    
    /**
     * Obtém estatísticas de eventos
     */
    public List<Object[]> getEstatisticas() throws SQLException {
        List<Object[]> estatisticas = new ArrayList<>();
        String sql = """
            SELECT tipo_evento, COUNT(*) as quantidade,
                   COUNT(CASE WHEN status = 'REALIZADO' THEN 1 END) as realizados,
                   COUNT(CASE WHEN status = 'PROGRAMADO' THEN 1 END) as programados
            FROM calendario 
            GROUP BY tipo_evento 
            ORDER BY tipo_evento
            """;
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Object[] estatistica = {
                    rs.getString("tipo_evento"),
                    rs.getInt("quantidade"),
                    rs.getInt("realizados"),
                    rs.getInt("programados")
                };
                estatisticas.add(estatistica);
            }
        }
        
        return estatisticas;
    }
    
    /**
     * Conta eventos por status
     */
    public int countByStatus(String status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM calendario WHERE status = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        
        return 0;
    }
}
