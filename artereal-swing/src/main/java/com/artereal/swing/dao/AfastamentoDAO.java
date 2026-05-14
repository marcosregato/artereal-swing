package com.artereal.swing.dao;

import com.artereal.swing.database.DatabaseManager;
import com.artereal.swing.model.Afastamento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

/**
 * DAO para operações com Afastamentos e Licenças
 */
public class AfastamentoDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(AfastamentoDAO.class);
    
    /**
     * Salva ou atualiza um afastamento
     */
    public void save(Afastamento afastamento) throws SQLException {
        String sql;
        if (afastamento.getId() == null) {
            sql = """
                INSERT INTO afastamento (codigo_irmao, data_inicial, data_final, descricao, motivo, status, 
                    documento_comprobatorio, data_cadastro, usuario_cadastro, observacoes, 
                    afeta_frequencia, dias_afastamento, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_DATE, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
                """;
        } else {
            sql = """
                UPDATE afastamento SET codigo_irmao = ?, data_inicial = ?, data_final = ?, descricao = ?, 
                    motivo = ?, status = ?, documento_comprobatorio = ?, usuario_cadastro = ?, 
                    observacoes = ?, afeta_frequencia = ?, dias_afastamento = ?, updated_at = CURRENT_TIMESTAMP
                WHERE id = ?
                """;
        }
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setLong(1, afastamento.getCodigoIrmao());
            stmt.setString(2, afastamento.getDataInicial().toString());
            stmt.setString(3, afastamento.getDataFinal().toString());
            stmt.setString(4, afastamento.getDescricao());
            stmt.setString(5, afastamento.getMotivo());
            stmt.setString(6, afastamento.getStatus());
            stmt.setString(7, afastamento.getDocumentoComprobatorio());
            stmt.setString(8, afastamento.getUsuarioCadastro());
            stmt.setString(9, afastamento.getObservacoes());
            stmt.setInt(10, afastamento.isAfetaFrequencia() ? 1 : 0);
            stmt.setInt(11, afastamento.getDiasAfastamento());
            
            if (afastamento.getId() != null) {
                stmt.setLong(12, afastamento.getId());
            }
            
            int rowsAffected = stmt.executeUpdate();
            
            if (afastamento.getId() == null && rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        afastamento.setId(generatedKeys.getLong(1));
                    }
                }
            }
            
            logger.debug("Afastamento salvo: {}", afastamento.getDescricao());
        }
    }
    
    /**
     * Busca afastamento por ID
     */
    public Afastamento findById(Long id) throws SQLException {
        String sql = "SELECT * FROM afastamento WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAfastamento(rs);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Lista todos os afastamentos
     */
    public List<Afastamento> findAll() throws SQLException {
        List<Afastamento> afastamentos = new ArrayList<>();
        String sql = "SELECT * FROM afastamento ORDER BY data_cadastro DESC";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                afastamentos.add(mapResultSetToAfastamento(rs));
            }
        }
        
        return afastamentos;
    }
    
    /**
     * Lista afastamentos por irmão
     */
    public List<Afastamento> findByIrmao(Long codigoIrmao) throws SQLException {
        List<Afastamento> afastamentos = new ArrayList<>();
        String sql = "SELECT * FROM afastamento WHERE codigo_irmao = ? ORDER BY data_cadastro DESC";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, codigoIrmao);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    afastamentos.add(mapResultSetToAfastamento(rs));
                }
            }
        }
        
        return afastamentos;
    }
    
    /**
     * Lista afastamentos por status
     */
    public List<Afastamento> findByStatus(String status) throws SQLException {
        List<Afastamento> afastamentos = new ArrayList<>();
        String sql = "SELECT * FROM afastamento WHERE status = ? ORDER BY data_cadastro DESC";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    afastamentos.add(mapResultSetToAfastamento(rs));
                }
            }
        }
        
        return afastamentos;
    }
    
    /**
     * Lista afastamentos em andamento
     */
    public List<Afastamento> findEmAndamento() throws SQLException {
        List<Afastamento> afastamentos = new ArrayList<>();
        String sql = """
            SELECT * FROM afastamento 
            WHERE status = 'ATIVO' AND CURRENT_DATE BETWEEN CAST(data_inicial AS DATE) AND CAST(data_final AS DATE) 
            ORDER BY CAST(data_final AS DATE)
            """;
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                afastamentos.add(mapResultSetToAfastamento(rs));
            }
        }
        
        return afastamentos;
    }
    
    /**
     * Lista afastamentos vencidos
     */
    public List<Afastamento> findVencidos() throws SQLException {
        List<Afastamento> afastamentos = new ArrayList<>();
        String sql = """
            SELECT * FROM afastamento 
            WHERE status = 'ATIVO' AND CURRENT_DATE > CAST(data_final AS DATE) 
            ORDER BY CAST(data_final AS DATE)
            """;
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                afastamentos.add(mapResultSetToAfastamento(rs));
            }
        }
        
        return afastamentos;
    }
    
    /**
     * Lista afastamentos futuros
     */
    public List<Afastamento> findFuturos() throws SQLException {
        List<Afastamento> afastamentos = new ArrayList<>();
        String sql = """
            SELECT * FROM afastamento 
            WHERE status = 'ATIVO' AND CURRENT_DATE < CAST(data_inicial AS DATE) 
            ORDER BY CAST(data_inicial AS DATE)
            """;
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                afastamentos.add(mapResultSetToAfastamento(rs));
            }
        }
        
        return afastamentos;
    }
    
    /**
     * Lista afastamentos por período
     */
    public List<Afastamento> findByPeriodo(LocalDate dataInicio, LocalDate dataFim) throws SQLException {
        List<Afastamento> afastamentos = new ArrayList<>();
        String sql = """
            SELECT * FROM afastamento 
            WHERE (data_inicial BETWEEN ? AND ? OR data_final BETWEEN ? AND ?)
            ORDER BY data_inicial
            """;
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, dataInicio.toString());
            stmt.setString(2, dataFim.toString());
            stmt.setString(3, dataInicio.toString());
            stmt.setString(4, dataFim.toString());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    afastamentos.add(mapResultSetToAfastamento(rs));
                }
            }
        }
        
        return afastamentos;
    }
    
    /**
     * Finaliza um afastamento
     */
    public void finalizar(Long id) throws SQLException {
        String sql = "UPDATE afastamento SET status = 'FINALIZADO', updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            logger.debug("Afastamento finalizado: ID {}", id);
        }
    }
    
    /**
     * Cancela um afastamento
     */
    public void cancelar(Long id) throws SQLException {
        String sql = "UPDATE afastamento SET status = 'CANCELADO', updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            logger.debug("Afastamento cancelado: ID {}", id);
        }
    }
    
    /**
     * Reativa um afastamento
     */
    public void reativar(Long id) throws SQLException {
        String sql = "UPDATE afastamento SET status = 'ATIVO', updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            logger.debug("Afastamento reativado: ID {}", id);
        }
    }
    
    /**
     * Exclui um afastamento
     */
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM afastamento WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            logger.debug("Afastamento excluído: ID {}", id);
        }
    }
    
    /**
     * Obtém estatísticas de afastamentos
     */
    public List<Object[]> getEstatisticas() throws SQLException {
        List<Object[]> estatisticas = new ArrayList<>();
        String sql = """
            SELECT motivo, COUNT(*) as quantidade,
                   SUM(dias_afastamento) as total_dias,
                   COUNT(CASE WHEN status = 'ATIVO' THEN 1 END) as ativos,
                   COUNT(CASE WHEN status = 'FINALIZADO' THEN 1 END) as finalizados
            FROM afastamento 
            GROUP BY motivo 
            ORDER BY motivo
            """;
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Object[] estatistica = {
                    rs.getString("motivo"),
                    rs.getInt("quantidade"),
                    rs.getInt("total_dias"),
                    rs.getInt("ativos"),
                    rs.getInt("finalizados")
                };
                estatisticas.add(estatistica);
            }
        }
        
        return estatisticas;
    }
    
    /**
     * Conta afastamentos por status
     */
    public int countByStatus(String status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM afastamento WHERE status = ?";
        
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
    
    /**
     * Mapeia ResultSet para objeto Afastamento
     */
    private Afastamento mapResultSetToAfastamento(ResultSet rs) throws SQLException {
        Afastamento afastamento = new Afastamento();
        
        afastamento.setId(rs.getLong("id"));
        afastamento.setCodigoIrmao(rs.getLong("codigo_irmao"));
        
        String dataInicialStr = rs.getString("data_inicial");
        if (dataInicialStr != null && !dataInicialStr.isEmpty()) {
            afastamento.setDataInicial(LocalDate.parse(dataInicialStr));
        }
        
        String dataFinalStr = rs.getString("data_final");
        if (dataFinalStr != null && !dataFinalStr.isEmpty()) {
            afastamento.setDataFinal(LocalDate.parse(dataFinalStr));
        }
        
        afastamento.setDescricao(rs.getString("descricao"));
        afastamento.setMotivo(rs.getString("motivo"));
        afastamento.setStatus(rs.getString("status"));
        afastamento.setDocumentoComprobatorio(rs.getString("documento_comprobatorio"));
        
        String dataCadastroStr = rs.getString("data_cadastro");
        if (dataCadastroStr != null && !dataCadastroStr.isEmpty()) {
            afastamento.setDataCadastro(LocalDate.parse(dataCadastroStr));
        }
        
        afastamento.setUsuarioCadastro(rs.getString("usuario_cadastro"));
        afastamento.setObservacoes(rs.getString("observacoes"));
        afastamento.setAfetaFrequencia(rs.getBoolean("afeta_frequencia"));
        afastamento.setDiasAfastamento(rs.getInt("dias_afastamento"));
        
        return afastamento;
    }
}
