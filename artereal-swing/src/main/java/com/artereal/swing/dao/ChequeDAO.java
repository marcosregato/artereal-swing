package com.artereal.swing.dao;

import com.artereal.swing.database.DatabaseManager;
import com.artereal.swing.model.Cheque;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operações com Cheques
 */
public class ChequeDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(ChequeDAO.class);
    
    /**
     * Salva ou atualiza um cheque
     */
    public void save(Cheque cheque) throws SQLException {
        String sql;
        if (cheque.getId() == null) {
            sql = """
                INSERT INTO cheque (fatura, data_emissao, sacado, valor, data_vencimento, modo_pagamento, 
                    banco, data_pagamento, valor_pago, codigo_cliente, situacao, grupo, historico, 
                    lancamento_credito, lancamento_debito, numero_nota, codigo_vendedor, ativo, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
                """;
        } else {
            sql = """
                UPDATE cheque SET fatura = ?, data_emissao = ?, sacado = ?, valor = ?, data_vencimento = ?, 
                    modo_pagamento = ?, banco = ?, data_pagamento = ?, valor_pago = ?, codigo_cliente = ?, situacao = ?, 
                    grupo = ?, historico = ?, lancamento_credito = ?, lancamento_debito = ?, numero_nota = ?, 
                    codigo_vendedor = ?, ativo = ?, updated_at = CURRENT_TIMESTAMP
                WHERE id = ?
                """;
        }
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, cheque.getFatura());
            stmt.setString(2, cheque.getDataEmissao() != null ? cheque.getDataEmissao().toString() : null);
            stmt.setString(3, cheque.getSacado());
            stmt.setDouble(4, cheque.getValor());
            stmt.setString(5, cheque.getDataVencimento() != null ? cheque.getDataVencimento().toString() : null);
            stmt.setString(6, cheque.getModoPagamento());
            stmt.setString(7, cheque.getBanco());
            stmt.setString(8, cheque.getDataPagamento() != null ? cheque.getDataPagamento().toString() : null);
            stmt.setDouble(9, cheque.getValorPago());
            stmt.setObject(10, cheque.getCodigoCliente());
            stmt.setString(11, cheque.getSituacao());
            stmt.setString(12, cheque.getGrupo());
            stmt.setString(13, cheque.getHistorico());
            stmt.setString(14, cheque.getLancamentoCredito());
            stmt.setString(15, cheque.getLancamentoDebito());
            stmt.setString(16, cheque.getNumeroNota());
            stmt.setObject(17, cheque.getCodigoVendedor());
            stmt.setInt(18, cheque.isAtivo() ? 1 : 0);
            
            if (cheque.getId() != null) {
                stmt.setLong(19, cheque.getId());
            }
            
            int rowsAffected = stmt.executeUpdate();
            
            if (cheque.getId() == null && rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        cheque.setId(generatedKeys.getLong(1));
                    }
                }
            }
            
            logger.debug("Cheque salvo: {}", cheque.getSacado());
        }
    }
    
    /**
     * Busca cheque por ID
     */
    public Cheque findById(Long id) throws SQLException {
        String sql = "SELECT * FROM cheque WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCheque(rs);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Lista todos os cheques
     */
    public List<Cheque> findAll() throws SQLException {
        List<Cheque> cheques = new ArrayList<>();
        String sql = "SELECT * FROM cheque WHERE ativo = 1 ORDER BY data_vencimento";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                cheques.add(mapResultSetToCheque(rs));
            }
        }
        
        return cheques;
    }
    
    /**
     * Lista cheques por situação
     */
    public List<Cheque> findBySituacao(String situacao) throws SQLException {
        List<Cheque> cheques = new ArrayList<>();
        String sql = "SELECT * FROM cheque WHERE situacao = ? AND ativo = 1 ORDER BY data_vencimento";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, situacao);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cheques.add(mapResultSetToCheque(rs));
                }
            }
        }
        
        return cheques;
    }
    
    /**
     * Lista cheques vencidos
     */
    public List<Cheque> findVencidos() throws SQLException {
        List<Cheque> cheques = new ArrayList<>();
        String sql = """
            SELECT * FROM cheque 
            WHERE CAST(data_vencimento AS DATE) < CURRENT_DATE AND situacao = 'ABERTO' AND ativo = 1 
            ORDER BY CAST(data_vencimento AS DATE)
            """;
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                cheques.add(mapResultSetToCheque(rs));
            }
        }
        
        return cheques;
    }
    
    /**
     * Lista cheques próximos ao vencimento
     */
    public List<Cheque> findProximosVencimento(int dias) throws SQLException {
        List<Cheque> cheques = new ArrayList<>();
        String sql = """
            SELECT * FROM cheque 
            WHERE CAST(data_vencimento AS DATE) BETWEEN CURRENT_DATE AND CURRENT_DATE + INTERVAL '{} days' 
            AND situacao = 'ABERTO' AND ativo = 1 
            ORDER BY CAST(data_vencimento AS DATE)
            """.replace("{}", String.valueOf(dias));
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                cheques.add(mapResultSetToCheque(rs));
            }
        }
        
        return cheques;
    }
    
    /**
     * Lista cheques por sacado
     */
    public List<Cheque> findBySacado(String sacado) throws SQLException {
        List<Cheque> cheques = new ArrayList<>();
        String sql = "SELECT * FROM cheque WHERE sacado LIKE ? AND ativo = 1 ORDER BY data_vencimento";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + sacado + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cheques.add(mapResultSetToCheque(rs));
                }
            }
        }
        
        return cheques;
    }
    
    /**
     * Lista cheques por período
     */
    public List<Cheque> findByPeriodo(LocalDate dataInicio, LocalDate dataFim) throws SQLException {
        List<Cheque> cheques = new ArrayList<>();
        String sql = """
            SELECT * FROM cheque 
            WHERE data_vencimento BETWEEN ? AND ? AND ativo = 1 
            ORDER BY data_vencimento
            """;
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, dataInicio.toString());
            stmt.setString(2, dataFim.toString());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cheques.add(mapResultSetToCheque(rs));
                }
            }
        }
        
        return cheques;
    }
    
    /**
     * Compensa um cheque
     */
    public void compensar(Long id, double valorPago, LocalDate dataPagamento) throws SQLException {
        String sql = """
            UPDATE cheque SET valor_pago = ?, data_pagamento = ?, situacao = 'PAGO', updated_at = CURRENT_TIMESTAMP 
            WHERE id = ?
            """;
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, valorPago);
            stmt.setString(2, dataPagamento.toString());
            stmt.setLong(3, id);
            
            stmt.executeUpdate();
            logger.debug("Cheque compensado: ID {}, Valor: {}", id, valorPago);
        }
    }
    
    /**
     * Cancela um cheque
     */
    public void cancelar(Long id) throws SQLException {
        String sql = """
            UPDATE cheque SET situacao = 'CANCELADO', updated_at = CURRENT_TIMESTAMP 
            WHERE id = ?
            """;
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            logger.debug("Cheque cancelado: ID {}", id);
        }
    }
    
    /**
     * Devolve um cheque
     */
    public void devolver(Long id) throws SQLException {
        String sql = """
            UPDATE cheque SET situacao = 'DEVOLVIDO', updated_at = CURRENT_TIMESTAMP 
            WHERE id = ?
            """;
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            logger.debug("Cheque devolvido: ID {}", id);
        }
    }
    
    /**
     * Obtém estatísticas de cheques
     */
    public List<Object[]> getEstatisticas() throws SQLException {
        List<Object[]> estatisticas = new ArrayList<>();
        String sql = """
            SELECT situacao, COUNT(*) as quantidade, SUM(valor) as valor_total, 
                   AVG(valor) as valor_medio, SUM(valor_pago) as valor_pago_total
            FROM cheque 
            WHERE ativo = 1 
            GROUP BY situacao 
            ORDER BY situacao
            """;
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Object[] estatistica = {
                    rs.getString("situacao"),
                    rs.getInt("quantidade"),
                    rs.getDouble("valor_total"),
                    rs.getDouble("valor_medio"),
                    rs.getDouble("valor_pago_total")
                };
                estatisticas.add(estatistica);
            }
        }
        
        return estatisticas;
    }
    
    /**
     * Obtém valor total de cheques por situação
     */
    public double getValorTotalPorSituacao(String situacao) throws SQLException {
        String sql = "SELECT COALESCE(SUM(valor), 0) as total FROM cheque WHERE situacao = ? AND ativo = 1";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, situacao);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        }
        
        return 0.0;
    }
    
    /**
     * Exclui (desativa) um cheque
     */
    public void delete(Long id) throws SQLException {
        String sql = "UPDATE cheque SET ativo = 0 WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            logger.debug("Cheque desativado: ID {}", id);
        }
    }
    
    /**
     * Conta cheques por situação
     */
    public int countBySituacao(String situacao) throws SQLException {
        String sql = "SELECT COUNT(*) FROM cheque WHERE situacao = ? AND ativo = 1";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, situacao);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        
        return 0;
    }
    
    /**
     * Mapeia ResultSet para objeto Cheque
     */
    private Cheque mapResultSetToCheque(ResultSet rs) throws SQLException {
        Cheque cheque = new Cheque();
        
        cheque.setId(rs.getLong("id"));
        cheque.setFatura(rs.getString("fatura"));
        
        String dataEmissaoStr = rs.getString("data_emissao");
        if (dataEmissaoStr != null && !dataEmissaoStr.isEmpty()) {
            cheque.setDataEmissao(LocalDate.parse(dataEmissaoStr));
        }
        
        cheque.setSacado(rs.getString("sacado"));
        cheque.setValor(rs.getDouble("valor"));
        
        String dataVencimentoStr = rs.getString("data_vencimento");
        if (dataVencimentoStr != null && !dataVencimentoStr.isEmpty()) {
            cheque.setDataVencimento(LocalDate.parse(dataVencimentoStr));
        }
        
        cheque.setModoPagamento(rs.getString("modo_pagamento"));
        cheque.setBanco(rs.getString("banco"));
        
        String dataPagamentoStr = rs.getString("data_pagamento");
        if (dataPagamentoStr != null && !dataPagamentoStr.isEmpty()) {
            cheque.setDataPagamento(LocalDate.parse(dataPagamentoStr));
        }
        
        cheque.setValorPago(rs.getDouble("valor_pago"));
        cheque.setCodigoCliente(rs.getObject("codigo_cliente") != null ? rs.getLong("codigo_cliente") : null);
        cheque.setSituacao(rs.getString("situacao"));
        cheque.setGrupo(rs.getString("grupo"));
        cheque.setHistorico(rs.getString("historico"));
        cheque.setLancamentoCredito(rs.getString("lancamento_credito"));
        cheque.setLancamentoDebito(rs.getString("lancamento_debito"));
        cheque.setNumeroNota(rs.getString("numero_nota"));
        cheque.setCodigoVendedor(rs.getObject("codigo_vendedor") != null ? rs.getLong("codigo_vendedor") : null);
        cheque.setAtivo(rs.getBoolean("ativo"));
        
        return cheque;
    }
}
