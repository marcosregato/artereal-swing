package com.artereal.swing.dao;

import com.artereal.swing.model.Loja;
import com.artereal.swing.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                INSERT INTO loja (nome, endereco, bairro, cidade, estado, cep, 
                    telefone, email, presidente, secretario, tesoureiro, data_fundacao, 
                    rito, potencia, observacoes, status, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
                """;
        } else {
            sql = """
                UPDATE loja SET nome = ?, endereco = ?, bairro = ?, cidade = ?, estado = ?, 
                    cep = ?, telefone = ?, email = ?, presidente = ?, secretario = ?, 
                    tesoureiro = ?, data_fundacao = ?, rito = ?, potencia = ?, observacoes = ?, 
                    status = ?, updated_at = CURRENT_TIMESTAMP
                WHERE id = ?
                """;
        }
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, loja.getNome());
            stmt.setString(2, loja.getEndereco());
            stmt.setString(3, loja.getBairro());
            stmt.setString(4, loja.getCidade());
            stmt.setString(5, loja.getEstado());
            stmt.setString(6, loja.getCep() != null ? loja.getCep() : "");
            stmt.setString(7, loja.getTelefone());
            stmt.setString(8, loja.getEmail());
            stmt.setString(9, loja.getPresidente());
            stmt.setString(10, loja.getSecretario());
            stmt.setString(11, loja.getTesoureiro());
            stmt.setString(12, loja.getDataFundacao());
            stmt.setString(13, loja.getRito());
            stmt.setString(14, loja.getPotencia());
            stmt.setString(15, loja.getObservacoes());
            stmt.setString(16, loja.getStatus() != null ? loja.getStatus() : "ATIVA");
            
            if (loja.getId() != null) {
                stmt.setLong(17, loja.getId());
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
        loja.setNumero(""); // Campo não existe na tabela
        loja.setEndereco(rs.getString("endereco"));
        loja.setBairro(rs.getString("bairro"));
        loja.setCidade(rs.getString("cidade"));
        loja.setEstado(rs.getString("estado"));
        loja.setCep(rs.getString("cep"));
        loja.setTelefone(rs.getString("telefone"));
        loja.setEmail(rs.getString("email"));
        loja.setPresidente(rs.getString("presidente"));
        loja.setSecretario(rs.getString("secretario"));
        loja.setTesoureiro(rs.getString("tesoureiro"));
        loja.setDataFundacao(rs.getString("data_fundacao"));
        loja.setRito(rs.getString("rito"));
        loja.setPotencia(rs.getString("potencia"));
        loja.setObservacoes(rs.getString("observacoes"));
        loja.setStatus(rs.getString("status"));
        loja.setCreatedAt(rs.getString("created_at"));
        loja.setUpdatedAt(rs.getString("updated_at"));
        
        return loja;
    }
}
