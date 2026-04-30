package com.artereal.swing.dao;

import com.artereal.swing.database.DatabaseManager;
import com.artereal.swing.model.Irmao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operações com Irmãos no banco SQLite
 */
public class IrmaoDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(IrmaoDAO.class);
    
    /**
     * Salva ou atualiza um irmão
     */
    public void save(Irmao irmao) throws SQLException {
        String sql;
        if (irmao.getId() == null) {
            sql = """
                INSERT INTO irmao (nome, nascimento, estado_civil, natural, identidade, tipo_sanguineo, 
                    cargo_loja, grau, cargo_grande_loja, endereco, bairro, cidade, estado, telefone, 
                    empresa, telefone_empresa, endereco_empresa, registro_grande_loja, ativo, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
                """;
        } else {
            sql = """
                UPDATE irmao SET nome = ?, nascimento = ?, estado_civil = ?, natural = ?, identidade = ?, 
                    tipo_sanguineo = ?, cargo_loja = ?, grau = ?, cargo_grande_loja = ?, endereco = ?, 
                    bairro = ?, cidade = ?, estado = ?, telefone = ?, empresa = ?, telefone_empresa = ?, 
                    endereco_empresa = ?, registro_grande_loja = ?, ativo = ?, updated_at = CURRENT_TIMESTAMP
                WHERE id = ?
                """;
        }
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, irmao.getNome());
            stmt.setString(2, irmao.getNascimento() != null ? irmao.getNascimento().toString() : null);
            stmt.setString(3, irmao.getEstadoCivil());
            stmt.setString(4, irmao.getNatural());
            stmt.setString(5, irmao.getIdentidade());
            stmt.setString(6, irmao.getTipoSanguineo());
            stmt.setString(7, irmao.getCargoLoja());
            stmt.setString(8, irmao.getGrau());
            stmt.setString(9, irmao.getCargoGrandeLoja());
            stmt.setString(10, irmao.getEndereco());
            stmt.setString(11, irmao.getBairro());
            stmt.setString(12, irmao.getCidade());
            stmt.setString(13, irmao.getEstado());
            stmt.setString(14, irmao.getTelefone());
            stmt.setString(15, irmao.getEmpresa());
            stmt.setString(16, irmao.getTelefoneEmpresa());
            stmt.setString(17, irmao.getEnderecoEmpresa());
            stmt.setString(18, irmao.getRegistroGrandeLoja());
            stmt.setBoolean(19, irmao.isAtivo());
            
            if (irmao.getId() != null) {
                stmt.setLong(20, irmao.getId());
            }
            
            int rowsAffected = stmt.executeUpdate();
            
            if (irmao.getId() == null && rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        irmao.setId(generatedKeys.getLong(1));
                    }
                }
            }
            
            conn.commit();
            logger.debug("Irmão salvo: {}", irmao.getNome());
        }
    }
    
    /**
     * Busca um irmão por ID
     */
    public Irmao findById(Long id) throws SQLException {
        String sql = "SELECT * FROM irmao WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToIrmao(rs);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Lista todos os irmãos
     */
    public List<Irmao> findAll() throws SQLException {
        List<Irmao> irmaos = new ArrayList<>();
        String sql = "SELECT * FROM irmao ORDER BY nome";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                irmaos.add(mapResultSetToIrmao(rs));
            }
        }
        
        return irmaos;
    }
    
    /**
     * Lista apenas irmãos ativos
     */
    public List<Irmao> findActive() throws SQLException {
        List<Irmao> irmaos = new ArrayList<>();
        String sql = "SELECT * FROM irmao WHERE ativo = 1 ORDER BY nome";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                irmaos.add(mapResultSetToIrmao(rs));
            }
        }
        
        return irmaos;
    }
    
    /**
     * Busca irmãos por nome
     */
    public List<Irmao> findByNome(String nome) throws SQLException {
        List<Irmao> irmaos = new ArrayList<>();
        String sql = "SELECT * FROM irmao WHERE nome LIKE ? ORDER BY nome";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nome + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    irmaos.add(mapResultSetToIrmao(rs));
                }
            }
        }
        
        return irmaos;
    }
    
    /**
     * Exclui um irmão
     */
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM irmao WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            conn.commit();
            
            logger.debug("Irmão excluído: ID {}", id);
        }
    }
    
    /**
     * Conta o total de irmãos
     */
    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM irmao";
        
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
     * Conta irmãos ativos
     */
    public int countActive() throws SQLException {
        String sql = "SELECT COUNT(*) FROM irmao WHERE ativo = 1";
        
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
     * Mapeia ResultSet para objeto Irmao
     */
    private Irmao mapResultSetToIrmao(ResultSet rs) throws SQLException {
        Irmao irmao = new Irmao();
        
        irmao.setId(rs.getLong("id"));
        irmao.setNome(rs.getString("nome"));
        
        String nascimentoStr = rs.getString("nascimento");
        if (nascimentoStr != null && !nascimentoStr.isEmpty()) {
            irmao.setNascimento(LocalDate.parse(nascimentoStr));
        }
        
        irmao.setEstadoCivil(rs.getString("estado_civil"));
        irmao.setNatural(rs.getString("natural"));
        irmao.setIdentidade(rs.getString("identidade"));
        irmao.setTipoSanguineo(rs.getString("tipo_sanguineo"));
        irmao.setCargoLoja(rs.getString("cargo_loja"));
        irmao.setGrau(rs.getString("grau"));
        irmao.setCargoGrandeLoja(rs.getString("cargo_grande_loja"));
        irmao.setEndereco(rs.getString("endereco"));
        irmao.setBairro(rs.getString("bairro"));
        irmao.setCidade(rs.getString("cidade"));
        irmao.setEstado(rs.getString("estado"));
        irmao.setTelefone(rs.getString("telefone"));
        irmao.setEmpresa(rs.getString("empresa"));
        irmao.setTelefoneEmpresa(rs.getString("telefone_empresa"));
        irmao.setEnderecoEmpresa(rs.getString("endereco_empresa"));
        irmao.setRegistroGrandeLoja(rs.getString("registro_grande_loja"));
        irmao.setAtivo(rs.getBoolean("ativo"));
        irmao.setCreatedAt(rs.getString("created_at"));
        irmao.setUpdatedAt(rs.getString("updated_at"));
        
        return irmao;
    }
}
