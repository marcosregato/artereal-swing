package com.artereal.swing.dao;

import com.artereal.swing.database.DatabaseManager;
import com.artereal.swing.model.Frequencia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operações com Frequência no banco PostgreSQL
 */
public class FrequenciaDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(FrequenciaDAO.class);
    
    /**
     * Salva ou atualiza um registro de frequência
     */
    public void save(Frequencia frequencia) throws SQLException {
        String sql;
        if (frequencia.getId() == null) {
            sql = """
                INSERT INTO frequencia (codigo_irmao, nome_irmao, registro_grande_loja, numero_licenca, 
                    data_licenca, data_final_licenca, grau, data_instalado, irregular, numero_presencas, 
                    numero_faltas, numero_secoes, nome_historico, presenca_diretoria, secretaria_diretoria, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
                """;
        } else {
            sql = """
                UPDATE frequencia SET codigo_irmao = ?, nome_irmao = ?, registro_grande_loja = ?, 
                    numero_licenca = ?, data_licenca = ?, data_final_licenca = ?, grau = ?, data_instalado = ?, 
                    irregular = ?, numero_presencas = ?, numero_faltas = ?, numero_secoes = ?, nome_historico = ?, 
                    presenca_diretoria = ?, secretaria_diretoria = ?, updated_at = CURRENT_TIMESTAMP
                WHERE id = ?
                """;
        }
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setLong(1, frequencia.getCodigoIrmao());
            stmt.setString(2, frequencia.getNomeIrmao());
            stmt.setString(3, frequencia.getRegistroGrandeLoja());
            stmt.setString(4, frequencia.getNumeroLicenca());
            stmt.setString(5, frequencia.getDataLicenca() != null ? frequencia.getDataLicenca().toString() : null);
            stmt.setString(6, frequencia.getDataFinalLicenca() != null ? frequencia.getDataFinalLicenca().toString() : null);
            stmt.setString(7, frequencia.getGrau());
            stmt.setString(8, frequencia.getDataInstalado() != null ? frequencia.getDataInstalado().toString() : null);
            stmt.setInt(9, frequencia.isIrregular() ? 1 : 0);
            stmt.setInt(10, frequencia.getNumeroPresencas());
            stmt.setInt(11, frequencia.getNumeroFaltas());
            stmt.setInt(12, frequencia.getNumeroSecoes());
            stmt.setString(13, frequencia.getNomeHistorico());
            stmt.setInt(14, frequencia.isPresencaDiretoria() ? 1 : 0);
            stmt.setInt(15, frequencia.isSecretariaDiretoria() ? 1 : 0);
            
            if (frequencia.getId() != null) {
                stmt.setLong(16, frequencia.getId());
            }
            
            int rowsAffected = stmt.executeUpdate();
            
            if (frequencia.getId() == null && rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        frequencia.setId(generatedKeys.getLong(1));
                    }
                }
            }
            
            logger.debug("Frequência salva: {}", frequencia.getNomeIrmao());
        }
    }
    
    /**
     * Busca frequência por ID
     */
    public Frequencia findById(Long id) throws SQLException {
        String sql = "SELECT * FROM frequencia WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFrequencia(rs);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Busca frequência por código do irmão
     */
    public Frequencia findByIrmao(Long codigoIrmao) throws SQLException {
        String sql = "SELECT * FROM frequencia WHERE codigo_irmao = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, codigoIrmao);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFrequencia(rs);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Lista todos os registros de frequência
     */
    public List<Frequencia> findAll() throws SQLException {
        List<Frequencia> frequencias = new ArrayList<>();
        String sql = "SELECT * FROM frequencia ORDER BY nome_irmao";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                frequencias.add(mapResultSetToFrequencia(rs));
            }
        }
        
        return frequencias;
    }
    
    /**
     * Lista frequências por grau
     */
    public List<Frequencia> findByGrau(String grau) throws SQLException {
        List<Frequencia> frequencias = new ArrayList<>();
        String sql = "SELECT * FROM frequencia WHERE grau = ? ORDER BY nome_irmao";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, grau);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    frequencias.add(mapResultSetToFrequencia(rs));
                }
            }
        }
        
        return frequencias;
    }
    
    /**
     * Lista frequências com baixa assiduidade
     */
    public List<Frequencia> findBaixaAssiduidade(double percentualMinimo) throws SQLException {
        List<Frequencia> frequencias = new ArrayList<>();
        String sql = "SELECT * FROM frequencia WHERE (numero_presencas * 100.0 / numero_secoes) < ? ORDER BY (numero_presencas * 100.0 / numero_secoes)";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, percentualMinimo);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    frequencias.add(mapResultSetToFrequencia(rs));
                }
            }
        }
        
        return frequencias;
    }
    
    /**
     * Incrementa presença de um irmão
     */
    public void incrementarPresenca(Long codigoIrmao) throws SQLException {
        String sql = "UPDATE frequencia SET numero_presencas = numero_presencas + 1, numero_secoes = numero_secoes + 1, updated_at = CURRENT_TIMESTAMP WHERE codigo_irmao = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, codigoIrmao);
            stmt.executeUpdate();
            
            logger.debug("Presença incrementada: Irmão {}", codigoIrmao);
        }
    }
    
    /**
     * Incrementa falta de um irmão
     */
    public void incrementarFalta(Long codigoIrmao) throws SQLException {
        String sql = "UPDATE frequencia SET numero_faltas = numero_faltas + 1, numero_secoes = numero_secoes + 1, updated_at = CURRENT_TIMESTAMP WHERE codigo_irmao = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, codigoIrmao);
            stmt.executeUpdate();
            
            logger.debug("Falta incrementada: Irmão {}", codigoIrmao);
        }
    }
    
    /**
     * Obtém estatísticas de frequência
     */
    public List<Object[]> getEstatisticasPorGrau() throws SQLException {
        List<Object[]> estatisticas = new ArrayList<>();
        String sql = """
            SELECT grau, COUNT(*) as total, AVG(numero_presencas * 100.0 / numero_secoes) as media_presenca
            FROM frequencia 
            WHERE numero_secoes > 0
            GROUP BY grau 
            ORDER BY grau
            """;
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Object[] estatistica = {
                    rs.getString("grau"),
                    rs.getInt("total"),
                    rs.getDouble("media_presenca")
                };
                estatisticas.add(estatistica);
            }
        }
        
        return estatisticas;
    }
    
    /**
     * Exclui um registro de frequência
     */
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM frequencia WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            
            logger.debug("Registro de frequência excluído: ID {}", id);
        }
    }
    
    /**
     * Mapeia ResultSet para objeto Frequencia
     */
    private Frequencia mapResultSetToFrequencia(ResultSet rs) throws SQLException {
        Frequencia frequencia = new Frequencia();
        
        frequencia.setId(rs.getLong("id"));
        frequencia.setCodigoIrmao(rs.getLong("codigo_irmao"));
        frequencia.setNomeIrmao(rs.getString("nome_irmao"));
        frequencia.setRegistroGrandeLoja(rs.getString("registro_grande_loja"));
        frequencia.setNumeroLicenca(rs.getString("numero_licenca"));
        
        String dataLicencaStr = rs.getString("data_licenca");
        if (dataLicencaStr != null && !dataLicencaStr.isEmpty()) {
            frequencia.setDataLicenca(LocalDate.parse(dataLicencaStr));
        }
        
        String dataFinalLicencaStr = rs.getString("data_final_licenca");
        if (dataFinalLicencaStr != null && !dataFinalLicencaStr.isEmpty()) {
            frequencia.setDataFinalLicenca(LocalDate.parse(dataFinalLicencaStr));
        }
        
        String dataInstaladoStr = rs.getString("data_instalado");
        if (dataInstaladoStr != null && !dataInstaladoStr.isEmpty()) {
            frequencia.setDataInstalado(LocalDate.parse(dataInstaladoStr));
        }
        
        frequencia.setGrau(rs.getString("grau"));
        frequencia.setIrregular(rs.getBoolean("irregular"));
        frequencia.setNumeroPresencas(rs.getInt("numero_presencas"));
        frequencia.setNumeroFaltas(rs.getInt("numero_faltas"));
        frequencia.setNumeroSecoes(rs.getInt("numero_secoes"));
        frequencia.setNomeHistorico(rs.getString("nome_historico"));
        frequencia.setPresencaDiretoria(rs.getBoolean("presenca_diretoria"));
        frequencia.setSecretariaDiretoria(rs.getBoolean("secretaria_diretoria"));
        
        return frequencia;
    }
}
