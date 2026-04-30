package com.artereal.swing.dao;

import com.artereal.swing.model.Irmao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * DAO para operações com Irmãos no banco SQLite
 * Refatorado para usar BaseDAO eliminando código duplicado
 */
public class IrmaoDAO extends BaseDAO<Irmao> {
    
        
    // Métodos específicos para Irmãos
    
    /**
     * Busca irmãos por nome
     */
    public List<Irmao> findByNome(String nome) throws SQLException {
        String sql = "SELECT * FROM irmao WHERE nome LIKE ? ORDER BY nome";
        return findByFilter(sql, "%" + nome + "%");
    }
    
    /**
     * Busca irmãos por cidade
     */
    public List<Irmao> findByCidade(String cidade) throws SQLException {
        String sql = "SELECT * FROM irmao WHERE cidade LIKE ? ORDER BY nome";
        return findByFilter(sql, "%" + cidade + "%");
    }
    
    /**
     * Busca irmãos ativos
     */
    public List<Irmao> findAtivos() throws SQLException {
        String sql = "SELECT * FROM irmao WHERE ativo = true ORDER BY nome";
        return findByFilter(sql);
    }
    
    /**
     * Conta irmãos ativos
     */
    public int countAtivos() throws SQLException {
        return count("SELECT COUNT(*) FROM irmao WHERE ativo = true");
    }
    
    // Implementação dos métodos abstratos do BaseDAO
    
    @Override
    protected boolean isNew(Irmao irmao) {
        return irmao.getId() == null;
    }
    
    @Override
    protected String getInsertSQL() {
        return """
            INSERT INTO irmao (nome, nascimento, estado_civil, natural, identidade, tipo_sanguineo, 
                cargo_loja, grau, cargo_grande_loja, endereco, bairro, cidade, estado, telefone, 
                empresa, telefone_empresa, endereco_empresa, registro_grande_loja, ativo, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
            """;
    }
    
    @Override
    protected String getUpdateSQL() {
        return """
            UPDATE irmao SET nome = ?, nascimento = ?, estado_civil = ?, natural = ?, identidade = ?, 
                tipo_sanguineo = ?, cargo_loja = ?, grau = ?, cargo_grande_loja = ?, endereco = ?, 
                bairro = ?, cidade = ?, estado = ?, telefone = ?, empresa = ?, telefone_empresa = ?, 
                endereco_empresa = ?, registro_grande_loja = ?, ativo = ?, updated_at = CURRENT_TIMESTAMP
            WHERE id = ?
            """;
    }
    
    @Override
    protected String getSelectByIdSQL() {
        return "SELECT * FROM irmao WHERE id = ?";
    }
    
    @Override
    protected String getSelectAllSQL() {
        return "SELECT * FROM irmao ORDER BY nome";
    }
    
    @Override
    protected String getDeleteSQL() {
        return "DELETE FROM irmao WHERE id = ?";
    }
    
    @Override
    protected void setInsertParameters(PreparedStatement stmt, Irmao irmao) throws SQLException {
        stmt.setString(1, irmao.getNome());
        stmt.setString(2, formatDate(irmao.getNascimento()));
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
    }
    
    @Override
    protected void setUpdateParameters(PreparedStatement stmt, Irmao irmao) throws SQLException {
        setInsertParameters(stmt, irmao);
        stmt.setLong(20, irmao.getId());
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
        
        return irmao;
    }
    
    @Override
    protected void setGeneratedId(Irmao irmao, Long id) {
        irmao.setId(id);
    }
}
