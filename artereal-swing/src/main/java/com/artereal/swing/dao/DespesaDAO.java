package com.artereal.swing.dao;

import com.artereal.swing.model.Despesa;
import com.artereal.swing.database.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operações com Despesas
 */
public class DespesaDAO extends BaseDAO<Despesa> {
    
    @Override
    public String getInsertSQL() {
        return "INSERT INTO despesas (descricao, valor, data, categoria, fornecedor, numero_documento) VALUES (?, ?, ?, ?, ?, ?)";
    }
    
    @Override
    public String getUpdateSQL() {
        return "UPDATE despesas SET descricao = ?, valor = ?, data = ?, categoria = ?, fornecedor = ?, numero_documento = ? WHERE id = ?";
    }
    
    @Override
    public String getDeleteSQL() {
        return "DELETE FROM despesas WHERE id = ?";
    }
    
    @Override
    public String getFindByIdSQL() {
        return "SELECT * FROM despesas WHERE id = ?";
    }
    
    @Override
    public String getFindAllSQL() {
        return "SELECT * FROM despesas ORDER BY data DESC, id DESC";
    }
    
    @Override
    public void setInsertParameters(PreparedStatement stmt, Despesa despesa) throws SQLException {
        stmt.setString(1, despesa.getDescricao());
        stmt.setDouble(2, despesa.getValor());
        stmt.setDate(3, new java.sql.Date(despesa.getData().getTime()));
        stmt.setString(4, despesa.getCategoria());
        stmt.setString(5, despesa.getFornecedor());
        stmt.setString(6, despesa.getNumeroDocumento());
    }
    
    @Override
    public void setUpdateParameters(PreparedStatement stmt, Despesa despesa) throws SQLException {
        stmt.setString(1, despesa.getDescricao());
        stmt.setDouble(2, despesa.getValor());
        stmt.setDate(3, new java.sql.Date(despesa.getData().getTime()));
        stmt.setString(4, despesa.getCategoria());
        stmt.setString(5, despesa.getFornecedor());
        stmt.setString(6, despesa.getNumeroDocumento());
        stmt.setLong(7, despesa.getId());
    }
    
        
    @Override
    public Despesa mapResultSetToEntity(ResultSet rs) throws SQLException {
        Despesa despesa = new Despesa();
        despesa.setId(rs.getLong("id"));
        despesa.setDescricao(rs.getString("descricao"));
        despesa.setValor(rs.getDouble("valor"));
        despesa.setData(rs.getDate("data"));
        despesa.setCategoria(rs.getString("categoria"));
        despesa.setFornecedor(rs.getString("fornecedor"));
        despesa.setNumeroDocumento(rs.getString("numero_documento"));
        return despesa;
    }
    
    @Override
    public boolean isNew(Despesa despesa) {
        return despesa.getId() == null;
    }
    
    @Override
    public void setGeneratedId(Despesa despesa, long id) {
        despesa.setId(id);
    }
    
    /**
     * Busca despesas por descrição
     */
    public List<Despesa> findByDescricao(String descricao) throws SQLException {
        String sql = "SELECT * FROM despesas WHERE descricao LIKE ? ORDER BY data DESC, id DESC";
        List<Despesa> despesas = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + descricao + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    despesas.add(mapResultSetToEntity(rs));
                }
            }
        }
        
        return despesas;
    }
    
    /**
     * Busca despesas por categoria
     */
    public List<Despesa> findByCategoria(String categoria) throws SQLException {
        String sql = "SELECT * FROM despesas WHERE categoria = ? ORDER BY data DESC, id DESC";
        List<Despesa> despesas = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, categoria);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    despesas.add(mapResultSetToEntity(rs));
                }
            }
        }
        
        return despesas;
    }
    
    /**
     * Busca despesas por período
     */
    public List<Despesa> findByPeriodo(Date dataInicio, Date dataFim) throws SQLException {
        String sql = "SELECT * FROM despesas WHERE data BETWEEN ? AND ? ORDER BY data DESC, id DESC";
        List<Despesa> despesas = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, new java.sql.Date(dataInicio.getTime()));
            stmt.setDate(2, new java.sql.Date(dataFim.getTime()));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    despesas.add(mapResultSetToEntity(rs));
                }
            }
        }
        
        return despesas;
    }
    
    /**
     * Calcula o total de despesas por período
     */
    public double getTotalPorPeriodo(Date dataInicio, Date dataFim) throws SQLException {
        String sql = "SELECT COALESCE(SUM(valor), 0) as total FROM despesas WHERE data BETWEEN ? AND ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, new java.sql.Date(dataInicio.getTime()));
            stmt.setDate(2, new java.sql.Date(dataFim.getTime()));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        }
        
        return 0.0;
    }
}
