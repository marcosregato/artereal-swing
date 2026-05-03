package com.artereal.swing.dao;

import com.artereal.swing.model.Irmao;
import com.artereal.swing.database.DatabaseManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

/**
 * DAO para operações com Irmãos no banco PostgreSQL
 * Refatorado para usar BaseDAO eliminando código duplicado
 */
public class IrmaoDAO extends BaseDAO<Irmao> {
    
        
    // Métodos específicos para Irmãos
    
    /**
     * Busca irmãos por nome
     */
    public List<Irmao> findByNome(String nome) throws SQLException {
        String sql = "SELECT * FROM irmao WHERE nome LIKE ? ORDER BY nome";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();
            List<Irmao> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapResultSetToEntity(rs));
            }
            return result;
        }
    }
    
    /**
     * Busca irmãos por cidade
     */
    public List<Irmao> findByCidade(String cidade) throws SQLException {
        String sql = "SELECT * FROM irmao WHERE cidade LIKE ? ORDER BY nome";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + cidade + "%");
            ResultSet rs = stmt.executeQuery();
            List<Irmao> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapResultSetToEntity(rs));
            }
            return result;
        }
    }
    
    /**
     * Busca irmãos ativos
     */
    public List<Irmao> findAtivos() throws SQLException {
        String sql = "SELECT * FROM irmao WHERE CAST(ativo AS INTEGER) = 1 ORDER BY nome";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            List<Irmao> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapResultSetToEntity(rs));
            }
            return result;
        }
    }
    
    /**
     * Conta irmãos ativos
     */
    public int countAtivos() throws SQLException {
        String sql = "SELECT COUNT(*) FROM irmao WHERE CAST(ativo AS INTEGER) = 1";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }
    
    // Implementação dos métodos abstratos do BaseDAO
    
    @Override
    protected boolean isNew(Irmao irmao) {
        return irmao.getId() == null;
    }
    
    @Override
    protected String getInsertSQL() {
        return """
            INSERT INTO irmao (nome, nascimento, estado_civil, naturalidade, identidade, tipo_sanguineo, 
                cargo_loja, grau, endereco, bairro, cidade, estado, telefone, 
                empresa, telefone_empresa, endereco_empresa, registro_grande_loja, ativo, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
    }
    
    @Override
    protected String getUpdateSQL() {
        return """
            UPDATE irmao SET nome = ?, nascimento = ?, estado_civil = ?, naturalidade = ?, identidade = ?, 
                tipo_sanguineo = ?, cargo_loja = ?, grau = ?, endereco = ?, 
                bairro = ?, cidade = ?, estado = ?, telefone = ?, empresa = ?, telefone_empresa = ?, 
                endereco_empresa = ?, registro_grande_loja = ?, ativo = ?, updated_at = CURRENT_TIMESTAMP
            WHERE id = ?
            """;
    }
    
    @Override
    protected String getFindByIdSQL() {
        return "SELECT * FROM irmao WHERE id = ?";
    }
    
    @Override
    protected String getFindAllSQL() {
        return "SELECT * FROM irmao ORDER BY nome";
    }
    
    @Override
    protected String getDeleteSQL() {
        return "DELETE FROM irmao WHERE id = ?";
    }
    
    @Override
    protected void setInsertParameters(PreparedStatement stmt, Irmao irmao) throws SQLException {
        stmt.setString(1, irmao.getNome());
        stmt.setString(2, irmao.getNascimento() != null ? irmao.getNascimento().toString() : null);
        stmt.setString(3, irmao.getEstadoCivil());
        stmt.setString(4, irmao.getNatural());
        stmt.setString(5, irmao.getIdentidade());
        stmt.setString(6, irmao.getTipoSanguineo());
        stmt.setString(7, irmao.getCargoLoja());
        stmt.setString(8, irmao.getGrau());
        stmt.setString(9, irmao.getEndereco());
        stmt.setString(10, irmao.getBairro());
        stmt.setString(11, irmao.getCidade());
        stmt.setString(12, irmao.getEstado());
        stmt.setString(13, irmao.getTelefone());
        stmt.setString(14, irmao.getEmpresa());
        stmt.setString(15, irmao.getTelefoneEmpresa());
        stmt.setString(16, irmao.getEnderecoEmpresa());
        stmt.setString(17, irmao.getRegistroGrandeLoja());
        stmt.setInt(18, irmao.isAtivo() ? 1 : 0);
        stmt.setString(19, java.time.LocalDateTime.now().toString());
        stmt.setString(20, java.time.LocalDateTime.now().toString());
    }
    
    @Override
    protected void setUpdateParameters(PreparedStatement stmt, Irmao irmao) throws SQLException {
        stmt.setString(1, irmao.getNome());
        stmt.setString(2, irmao.getNascimento() != null ? irmao.getNascimento().toString() : null);
        stmt.setString(3, irmao.getEstadoCivil());
        stmt.setString(4, irmao.getNatural());
        stmt.setString(5, irmao.getIdentidade());
        stmt.setString(6, irmao.getTipoSanguineo());
        stmt.setString(7, irmao.getCargoLoja());
        stmt.setString(8, irmao.getGrau());
        stmt.setString(9, irmao.getEndereco());
        stmt.setString(10, irmao.getBairro());
        stmt.setString(11, irmao.getCidade());
        stmt.setString(12, irmao.getEstado());
        stmt.setString(13, irmao.getTelefone());
        stmt.setString(14, irmao.getEmpresa());
        stmt.setString(15, irmao.getTelefoneEmpresa());
        stmt.setString(16, irmao.getEnderecoEmpresa());
        stmt.setString(17, irmao.getRegistroGrandeLoja());
        stmt.setInt(18, irmao.isAtivo() ? 1 : 0);
        stmt.setLong(19, irmao.getId());
    }
    
    @Override
    protected Irmao mapResultSetToEntity(ResultSet rs) throws SQLException {
        Irmao irmao = new Irmao();
        irmao.setId(rs.getLong("id"));
        irmao.setNome(rs.getString("nome"));
        
        String nascimentoStr = rs.getString("nascimento");
        if (nascimentoStr != null && !nascimentoStr.isEmpty()) {
            irmao.setNascimento(LocalDate.parse(nascimentoStr));
        }
        
        irmao.setEstadoCivil(rs.getString("estado_civil"));
        irmao.setNatural(rs.getString("naturalidade"));
        irmao.setIdentidade(rs.getString("identidade"));
        irmao.setTipoSanguineo(rs.getString("tipo_sanguineo"));
        irmao.setCargoLoja(rs.getString("cargo_loja"));
        irmao.setGrau(rs.getString("grau"));
        irmao.setEndereco(rs.getString("endereco"));
        irmao.setBairro(rs.getString("bairro"));
        irmao.setCidade(rs.getString("cidade"));
        irmao.setEstado(rs.getString("estado"));
        irmao.setTelefone(rs.getString("telefone"));
        irmao.setEmpresa(rs.getString("empresa"));
        irmao.setTelefoneEmpresa(rs.getString("telefone_empresa"));
        irmao.setEnderecoEmpresa(rs.getString("endereco_empresa"));
        irmao.setRegistroGrandeLoja(rs.getString("registro_grande_loja"));
        irmao.setAtivo(rs.getInt("ativo") == 1);
        
        return irmao;
    }
    
    @Override
    protected void setGeneratedId(Irmao irmao, long id) {
        irmao.setId(id);
    }
}
