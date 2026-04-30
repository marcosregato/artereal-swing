package com.artereal.swing.dao;

import com.artereal.swing.database.DatabaseManager;
import com.artereal.swing.model.Candidato;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operações com Candidatos no banco SQLite
 */
public class CandidatoDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(CandidatoDAO.class);
    
    /**
     * Salva ou atualiza um candidato
     */
    public void save(Candidato candidato) throws SQLException {
        String sql;
        if (candidato.getId() == null) {
            sql = """
                INSERT INTO candidato (nome, endereco, numero, cidade, estado, bairro, fone_residencial, 
                    data_nascimento, idade, estado_civil, esposa, profissao, funcao, local_trabalho, onde_exerce, 
                    informacoes, chanceler, veneravel, secretario, linha_negra, status, data_cadastro, data_status, observacoes, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
                """;
        } else {
            sql = """
                UPDATE candidato SET nome = ?, endereco = ?, numero = ?, cidade = ?, estado = ?, bairro = ?, 
                    fone_residencial = ?, data_nascimento = ?, idade = ?, estado_civil = ?, esposa = ?, profissao = ?, 
                    funcao = ?, local_trabalho = ?, onde_exerce = ?, informacoes = ?, chanceler = ?, veneravel = ?, 
                    secretario = ?, linha_negra = ?, status = ?, data_cadastro = ?, data_status = ?, observacoes = ?, updated_at = CURRENT_TIMESTAMP
                WHERE id = ?
                """;
        }
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, candidato.getNome());
            stmt.setString(2, candidato.getEndereco());
            stmt.setString(3, candidato.getNumero());
            stmt.setString(4, candidato.getCidade());
            stmt.setString(5, candidato.getEstado());
            stmt.setString(6, candidato.getBairro());
            stmt.setString(7, candidato.getFoneResidencial());
            stmt.setString(8, candidato.getDataNascimento() != null ? candidato.getDataNascimento().toString() : null);
            stmt.setInt(9, candidato.getIdade());
            stmt.setString(10, candidato.getEstadoCivil());
            stmt.setString(11, candidato.getEsposa());
            stmt.setString(12, candidato.getProfissao());
            stmt.setString(13, candidato.getFuncao());
            stmt.setString(14, candidato.getLocalTrabalho());
            stmt.setString(15, candidato.getOndeExerce());
            stmt.setString(16, candidato.getInformacoes());
            stmt.setString(17, candidato.getChanceler());
            stmt.setString(18, candidato.getVeneravel());
            stmt.setString(19, candidato.getSecretario());
            stmt.setString(20, candidato.getLinhaNegra());
            stmt.setString(21, candidato.getStatus());
            stmt.setString(22, candidato.getDataCadastro() != null ? candidato.getDataCadastro().toString() : null);
            stmt.setString(23, candidato.getDataStatus() != null ? candidato.getDataStatus().toString() : null);
            stmt.setString(24, candidato.getObservacoes());
            
            if (candidato.getId() != null) {
                stmt.setLong(25, candidato.getId());
            }
            
            int rowsAffected = stmt.executeUpdate();
            
            if (candidato.getId() == null && rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        candidato.setId(generatedKeys.getLong(1));
                    }
                }
            }
            
            conn.commit();
            logger.debug("Candidato salvo: {}", candidato.getNome());
        }
    }
    
    /**
     * Busca candidato por ID
     */
    public Candidato findById(Long id) throws SQLException {
        String sql = "SELECT * FROM candidato WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCandidato(rs);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Lista todos os candidatos
     */
    public List<Candidato> findAll() throws SQLException {
        List<Candidato> candidatos = new ArrayList<>();
        String sql = "SELECT * FROM candidato ORDER BY nome";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                candidatos.add(mapResultSetToCandidato(rs));
            }
        }
        
        return candidatos;
    }
    
    /**
     * Lista candidatos por status
     */
    public List<Candidato> findByStatus(String status) throws SQLException {
        List<Candidato> candidatos = new ArrayList<>();
        String sql = "SELECT * FROM candidato WHERE status = ? ORDER BY nome";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    candidatos.add(mapResultSetToCandidato(rs));
                }
            }
        }
        
        return candidatos;
    }
    
    /**
     * Lista candidatos ativos (não iniciados nem rejeitados)
     */
    public List<Candidato> findAtivos() throws SQLException {
        List<Candidato> candidatos = new ArrayList<>();
        String sql = "SELECT * FROM candidato WHERE status = 'CANDIDATO' OR status = 'APROVADO' ORDER BY nome";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                candidatos.add(mapResultSetToCandidato(rs));
            }
        }
        
        return candidatos;
    }
    
    /**
     * Busca candidatos por nome
     */
    public List<Candidato> findByNome(String nome) throws SQLException {
        List<Candidato> candidatos = new ArrayList<>();
        String sql = "SELECT * FROM candidato WHERE nome LIKE ? ORDER BY nome";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nome + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    candidatos.add(mapResultSetToCandidato(rs));
                }
            }
        }
        
        return candidatos;
    }
    
    /**
     * Busca candidatos por cidade/estado
     */
    public List<Candidato> findByLocalidade(String cidade, String estado) throws SQLException {
        List<Candidato> candidatos = new ArrayList<>();
        String sql = "SELECT * FROM candidato WHERE cidade LIKE ? AND estado LIKE ? ORDER BY nome";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cidade != null ? "%" + cidade + "%" : "%");
            stmt.setString(2, estado != null ? "%" + estado + "%" : "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    candidatos.add(mapResultSetToCandidato(rs));
                }
            }
        }
        
        return candidatos;
    }
    
    /**
     * Aprova um candidato
     */
    public void aprovar(Long id) throws SQLException {
        String sql = "UPDATE candidato SET status = 'APROVADO', data_status = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            conn.commit();
            
            logger.debug("Candidato aprovado: ID {}", id);
        }
    }
    
    /**
     * Rejeita um candidato
     */
    public void rejeitar(Long id) throws SQLException {
        String sql = "UPDATE candidato SET status = 'REJEITADO', data_status = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            conn.commit();
            
            logger.debug("Candidato rejeitado: ID {}", id);
        }
    }
    
    /**
     * Inicia um candidato (torna-se irmão)
     */
    public void iniciar(Long id) throws SQLException {
        String sql = "UPDATE candidato SET status = 'INICIADO', data_status = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            conn.commit();
            
            logger.debug("Candidato iniciado: ID {}", id);
        }
    }
    
    /**
     * Conta candidatos por status
     */
    public int countByStatus(String status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM candidato WHERE status = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            stmt.setString(1, status);
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        
        return 0;
    }
    
    /**
     * Obtém estatísticas de candidatos
     */
    public List<Object[]> getEstatisticas() throws SQLException {
        List<Object[]> estatisticas = new ArrayList<>();
        String sql = """
            SELECT status, COUNT(*) as quantidade, 
                   (julianday(CURRENT_DATE) - julianday(data_cadastro)) as dias_medio
            FROM candidato 
            GROUP BY status 
            ORDER BY status
            """;
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Object[] estatistica = {
                    rs.getString("status"),
                    rs.getInt("quantidade"),
                    rs.getDouble("dias_medio")
                };
                estatisticas.add(estatistica);
            }
        }
        
        return estatisticas;
    }
    
    /**
     * Exclui um candidato
     */
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM candidato WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            conn.commit();
            
            logger.debug("Candidato excluído: ID {}", id);
        }
    }
    
    /**
     * Mapeia ResultSet para objeto Candidato
     */
    private Candidato mapResultSetToCandidato(ResultSet rs) throws SQLException {
        Candidato candidato = new Candidato();
        
        candidato.setId(rs.getLong("id"));
        candidato.setNome(rs.getString("nome"));
        candidato.setEndereco(rs.getString("endereco"));
        candidato.setNumero(rs.getString("numero"));
        candidato.setCidade(rs.getString("cidade"));
        candidato.setEstado(rs.getString("estado"));
        candidato.setBairro(rs.getString("bairro"));
        candidato.setFoneResidencial(rs.getString("fone_residencial"));
        
        String dataNascimentoStr = rs.getString("data_nascimento");
        if (dataNascimentoStr != null && !dataNascimentoStr.isEmpty()) {
            try {
                // Converter formato SQLite (YYYY-MM-DD HH:MM:SS) para LocalDate
                String dataFormatada = dataNascimentoStr.split(" ")[0];
                candidato.setDataNascimento(LocalDate.parse(dataFormatada));
            } catch (Exception e) {
                logger.warn("Erro ao converter data de nascimento: {}", dataNascimentoStr);
            }
        }
        
        candidato.setIdade(rs.getInt("idade"));
        candidato.setEstadoCivil(rs.getString("estado_civil"));
        candidato.setEsposa(rs.getString("esposa"));
        candidato.setProfissao(rs.getString("profissao"));
        candidato.setFuncao(rs.getString("funcao"));
        candidato.setLocalTrabalho(rs.getString("local_trabalho"));
        candidato.setOndeExerce(rs.getString("onde_exerce"));
        candidato.setInformacoes(rs.getString("informacoes"));
        candidato.setChanceler(rs.getString("chanceler"));
        candidato.setVeneravel(rs.getString("veneravel"));
        candidato.setSecretario(rs.getString("secretario"));
        candidato.setLinhaNegra(rs.getString("linha_negra"));
        candidato.setStatus(rs.getString("status"));
        
        String dataCadastroStr = rs.getString("data_cadastro");
        if (dataCadastroStr != null && !dataCadastroStr.isEmpty()) {
            try {
                // Converter formato SQLite (YYYY-MM-DD HH:MM:SS) para LocalDate
                String dataFormatada = dataCadastroStr.split(" ")[0];
                candidato.setDataCadastro(LocalDate.parse(dataFormatada));
            } catch (Exception e) {
                logger.warn("Erro ao converter data de cadastro: {}", dataCadastroStr);
            }
        }
        
        String dataStatusStr = rs.getString("data_status");
        if (dataStatusStr != null && !dataStatusStr.isEmpty()) {
            try {
                // Converter formato SQLite (YYYY-MM-DD HH:MM:SS) para LocalDate
                String dataFormatada = dataStatusStr.split(" ")[0];
                candidato.setDataStatus(LocalDate.parse(dataFormatada));
            } catch (Exception e) {
                logger.warn("Erro ao converter data de status: {}", dataStatusStr);
            }
        }
        
        candidato.setObservacoes(rs.getString("observacoes"));
        
        return candidato;
    }
}
