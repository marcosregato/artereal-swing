package com.artereal.swing.dao;

import com.artereal.swing.model.Loja;
import com.artereal.swing.database.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operações com Lojas
 */
public class LojaDAO {
    
    private final DatabaseManager dbManager;
    
    public LojaDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }
    
    public void save(Loja loja) throws SQLException {
        String sql;
        if (loja.getId() == null) {
            sql = """
                INSERT INTO loja (nome, numero_loja, endereco, bairro, cidade, estado, 
                    telefone, email, veneravel, rito, potencia, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
                """;
        } else {
            sql = """
                UPDATE loja SET nome = ?, numero_loja = ?, endereco = ?, bairro = ?, cidade = ?, 
                    estado = ?, telefone = ?, email = ?, veneravel = ?, rito = ?, potencia = ?, 
                    updated_at = CURRENT_TIMESTAMP
                WHERE id = ?
                """;
        }
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, loja.getNome());
            stmt.setString(2, loja.getNumero());
            stmt.setString(3, loja.getEndereco());
            stmt.setString(4, loja.getBairro());
            stmt.setString(5, loja.getCidade());
            stmt.setString(6, loja.getEstado());
            stmt.setString(7, loja.getTelefone());
            stmt.setString(8, loja.getEmail());
            stmt.setString(9, loja.getPresidente()); // Mapeando presidente para veneravel
            stmt.setString(10, loja.getRito());
            stmt.setString(11, loja.getPotencia());
            
            if (loja.getId() != null) {
                stmt.setLong(12, loja.getId());
            }
            
            int affectedRows = stmt.executeUpdate();
            
            if (loja.getId() == null && affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        loja.setId(generatedKeys.getLong(1));
                    }
                }
            }
        }
    }
    
    public Loja findById(Long id) throws SQLException {
        String sql = "SELECT * FROM loja WHERE id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLoja(rs);
                }
            }
        }
        return null;
    }
    
    public List<Loja> findAll() throws SQLException {
        List<Loja> lojas = new ArrayList<>();
        String sql = "SELECT * FROM loja ORDER BY nome";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                lojas.add(mapResultSetToLoja(rs));
            }
        }
        return lojas;
    }
    
    public List<Loja> findByStatus(String status) throws SQLException {
        List<Loja> lojas = new ArrayList<>();
        String sql = "SELECT * FROM loja WHERE status = ? ORDER BY nome";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lojas.add(mapResultSetToLoja(rs));
                }
            }
        }
        return lojas;
    }
    
    public List<Loja> findByNome(String nome) throws SQLException {
        List<Loja> lojas = new ArrayList<>();
        String sql = "SELECT * FROM loja WHERE nome LIKE ? ORDER BY nome";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nome + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lojas.add(mapResultSetToLoja(rs));
                }
            }
        }
        return lojas;
    }
    
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM loja WHERE id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
    
    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM loja";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    private Loja mapResultSetToLoja(ResultSet rs) throws SQLException {
        Loja loja = new Loja();
        loja.setId(rs.getLong("id"));
        loja.setNome(rs.getString("nome"));
        loja.setNumero(rs.getString("numero_loja")); // Mapeando numero_loja para numero
        loja.setEndereco(rs.getString("endereco"));
        loja.setBairro(rs.getString("bairro"));
        loja.setCidade(rs.getString("cidade"));
        loja.setEstado(rs.getString("estado"));
        loja.setTelefone(rs.getString("telefone"));
        loja.setEmail(rs.getString("email"));
        loja.setPresidente(rs.getString("veneravel")); // Mapeando veneravel para presidente
        loja.setSecretario(""); // Campo não existe na tabela
        loja.setTesoureiro(""); // Campo não existe na tabela
        loja.setDataFundacao(""); // Campo não existe na tabela
        loja.setRito(rs.getString("rito"));
        loja.setPotencia(rs.getString("potencia"));
        loja.setObservacoes(""); // Campo não existe na tabela
        loja.setStatus(rs.getInt("ativa") == 1 ? "ATIVA" : "INATIVA"); // Mapeando ativa para status
        loja.setCreatedAt(rs.getString("created_at"));
        loja.setUpdatedAt(rs.getString("updated_at"));
        
        return loja;
    }
}
