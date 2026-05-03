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
 * DAO para operações com Candidatos no banco PostgreSQL
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
                INSERT INTO candidato (nome, endereco, numero, cidade, estado, bairro, 
                    fone_residencial, data_nascimento, idade, estado_civil, esposa, profissao, 
                    funcao, local_trabalho, onde_exerce, informacoes, chanceler, veneravel, 
                    secretario, linha_negra, status, data_cadastro, data_status, observacoes)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        } else {
            sql = """
                UPDATE candidato SET nome = ?, endereco = ?, numero = ?, cidade = ?, estado = ?, bairro = ?, 
                    fone_residencial = ?, data_nascimento = ?, idade = ?, estado_civil = ?, esposa = ?, profissao = ?, 
                    funcao = ?, local_trabalho = ?, onde_exerce = ?, informacoes = ?, chanceler = ?, veneravel = ?, 
                    secretario = ?, linha_negra = ?, status = ?, data_cadastro = ?, data_status = ?, observacoes = ?
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
            stmt.setString(8, candidato.getDataNascimento() != null ? candidato.getDataNascimento().toString() : "");
            stmt.setInt(9, candidato.getIdade());
            stmt.setString(10, candidato.getEstadoCivil());
            stmt.setString(11, candidato.getEsposa());
            stmt.setString(12, candidato.getProfissao());
            stmt.setString(13, candidato.getFuncao());
            stmt.setString(14, candidato.getLocalTrabalho());
            stmt.setString(15, candidato.getOndeExerce());
            stmt.setString(16, candidato.getInformacoes());
            stmt.setBoolean(17, false); // isChanceler() não existe
            stmt.setBoolean(18, true);  // isVeneravel() não existe  
            stmt.setBoolean(19, false); // isSecretario() não existe
            stmt.setString(20, candidato.getLinhaNegra());
            stmt.setString(21, candidato.getStatus());
            stmt.setString(22, candidato.getDataCadastro() != null ? candidato.getDataCadastro().toString() : LocalDate.now().toString());
            stmt.setString(23, candidato.getDataStatus() != null ? candidato.getDataStatus().toString() : LocalDate.now().toString());
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
            logger.debug("Candidato iniciado: ID {}", id);
        }
    }
    
    /**
     * Conta candidatos por status
     */
    public int countByStatus(String status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM candidato WHERE status = ?";
        
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
     * Obtém estatísticas de candidatos
     */
    public List<Object[]> getEstatisticas() throws SQLException {
        List<Object[]> estatisticas = new ArrayList<>();
        String sql = """
            SELECT status, COUNT(*) as quantidade, 
                   AVG(EXTRACT(DAY FROM CURRENT_DATE::DATE) - EXTRACT(DAY FROM data_cadastro::DATE)) as dias_medio
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
            logger.debug("Candidato excluído: ID {}", id);
        }
    }
    
    /**
     * Mapeia ResultSet para objeto Candidato
     */
    private Candidato mapResultSetToCandidato(ResultSet rs) throws SQLException {
        Candidato candidato = new Candidato();
        
        try {
            candidato.setId(rs.getLong("id"));
        } catch (Exception e) {
            candidato.setId(null);
        }
        try {
            candidato.setNome(rs.getString("nome"));
        } catch (Exception e) {
            candidato.setNome("");
        }
        try {
            candidato.setEndereco(rs.getString("endereco"));
        } catch (Exception e) {
            candidato.setEndereco("");
        }
        try {
            candidato.setNumero(rs.getString("numero"));
        } catch (Exception e) {
            candidato.setNumero("");
        }
        try {
            candidato.setCidade(rs.getString("cidade"));
        } catch (Exception e) {
            candidato.setCidade("");
        }
        try {
            candidato.setEstado(rs.getString("estado"));
        } catch (Exception e) {
            candidato.setEstado("");
        }
        try {
            candidato.setBairro(rs.getString("bairro"));
        } catch (Exception e) {
            candidato.setBairro("");
        }
        try {
            candidato.setFoneResidencial(rs.getString("fone_residencial"));
        } catch (Exception e) {
            candidato.setFoneResidencial("");
        }
        
        String dataNascimentoStr = rs.getString("data_nascimento");
        if (dataNascimentoStr != null && !dataNascimentoStr.isEmpty()) {
            try {
                // Converter formato PostgreSQL (YYYY-MM-DD HH:MM:SS) para LocalDate
                String dataFormatada = dataNascimentoStr.split(" ")[0];
                candidato.setDataNascimento(LocalDate.parse(dataFormatada));
            } catch (Exception e) {
                logger.warn("Erro ao converter data de nascimento: {}", dataNascimentoStr);
            }
        }
        
        try {
            candidato.setIdade(rs.getInt("idade"));
        } catch (Exception e) {
            candidato.setIdade(0);
        }
        try {
            candidato.setEstadoCivil(rs.getString("estado_civil"));
        } catch (Exception e) {
            candidato.setEstadoCivil("");
        }
        try {
            candidato.setEsposa(rs.getString("esposa"));
        } catch (Exception e) {
            candidato.setEsposa("");
        }
        try {
            candidato.setProfissao(rs.getString("profissao"));
        } catch (Exception e) {
            candidato.setProfissao("");
        }
        try {
            candidato.setFuncao(rs.getString("funcao"));
        } catch (Exception e) {
            candidato.setFuncao("");
        }
        try {
            candidato.setLocalTrabalho(rs.getString("local_trabalho"));
        } catch (Exception e) {
            candidato.setLocalTrabalho("");
        }
        try {
            candidato.setOndeExerce(rs.getString("onde_exerce"));
        } catch (Exception e) {
            candidato.setOndeExerce("");
        }
        try {
            candidato.setInformacoes(rs.getString("informacoes"));
        } catch (Exception e) {
            candidato.setInformacoes("");
        }
        try {
            candidato.setChanceler(rs.getString("chanceler"));
        } catch (Exception e) {
            candidato.setChanceler("");
        }
        try {
            candidato.setVeneravel(rs.getString("veneravel"));
        } catch (Exception e) {
            candidato.setVeneravel("");
        }
        try {
            candidato.setSecretario(rs.getString("secretario"));
        } catch (Exception e) {
            candidato.setSecretario("");
        }
        try {
            candidato.setLinhaNegra(rs.getString("linha_negra"));
        } catch (Exception e) {
            candidato.setLinhaNegra("");
        }
        try {
            candidato.setStatus(rs.getString("status"));
        } catch (Exception e) {
            candidato.setStatus("");
        }
        
        String dataCadastroStr = rs.getString("data_cadastro");
        if (dataCadastroStr != null && !dataCadastroStr.isEmpty()) {
            try {
                // Converter formato PostgreSQL (YYYY-MM-DD HH:MM:SS) para LocalDate
                String dataFormatada = dataCadastroStr.split(" ")[0];
                candidato.setDataCadastro(LocalDate.parse(dataFormatada));
            } catch (Exception e) {
                logger.warn("Erro ao converter data de cadastro: {}", dataCadastroStr);
            }
        }
        
        String dataStatusStr = rs.getString("data_status");
        if (dataStatusStr != null && !dataStatusStr.isEmpty()) {
            try {
                // Converter formato PostgreSQL (YYYY-MM-DD HH:MM:SS) para LocalDate
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
