package com.artereal.swing.dao;

import com.artereal.swing.database.DatabaseManager;
import com.artereal.swing.model.Visitante;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operações com Visitantes
 */
public class VisitanteDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(VisitanteDAO.class);
    
    /**
     * Salva ou atualiza um visitante
     */
    public void save(Visitante visitante) throws SQLException {
        String sql;
        if (visitante.getId() == null) {
            sql = """
                INSERT INTO visitante (nome, data_visita, grau_secreto, historico, tipo, loja_origem, 
                    telefone, email, autorizado_por, autorizado, observacoes, numero_cracha, 
                    data_cadastro, ativo, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_DATE, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
                """;
        } else {
            sql = """
                UPDATE visitante SET nome = ?, data_visita = ?, grau_secreto = ?, historico = ?, tipo = ?, 
                    loja_origem = ?, telefone = ?, email = ?, autorizado_por = ?, autorizado = ?, 
                    observacoes = ?, numero_cracha = ?, ativo = ?, updated_at = CURRENT_TIMESTAMP
                WHERE id = ?
                """;
        }
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, visitante.getNome());
            stmt.setString(2, visitante.getDataVisita() != null ? visitante.getDataVisita().toString() : null);
            stmt.setString(3, visitante.getGrauSecreto());
            stmt.setString(4, visitante.getHistorico());
            stmt.setString(5, visitante.getTipo());
            stmt.setString(6, visitante.getLojaOrigem());
            stmt.setString(7, visitante.getTelefone());
            stmt.setString(8, visitante.getEmail());
            stmt.setString(9, visitante.getAutorizadoPor());
            stmt.setBoolean(10, visitante.isAutorizado());
            stmt.setString(11, visitante.getObservacoes());
            stmt.setString(12, visitante.getNumeroCracha());
            stmt.setBoolean(13, visitante.isAtivo());
            
            if (visitante.getId() != null) {
                stmt.setLong(14, visitante.getId());
            }
            
            int rowsAffected = stmt.executeUpdate();
            
            if (visitante.getId() == null && rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        visitante.setId(generatedKeys.getLong(1));
                    }
                }
            }
            
            conn.commit();
            logger.debug("Visitante salvo: {}", visitante.getNome());
        }
    }
    
    /**
     * Busca visitante por ID
     */
    public Visitante findById(Long id) throws SQLException {
        String sql = "SELECT * FROM visitante WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToVisitante(rs);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Lista todos os visitantes
     */
    public List<Visitante> findAll() throws SQLException {
        List<Visitante> visitantes = new ArrayList<>();
        String sql = "SELECT * FROM visitante WHERE ativo = 1 ORDER BY data_visita DESC, nome";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                visitantes.add(mapResultSetToVisitante(rs));
            }
        }
        
        return visitantes;
    }
    
    /**
     * Lista visitantes por data
     */
    public List<Visitante> findByData(LocalDate data) throws SQLException {
        List<Visitante> visitantes = new ArrayList<>();
        String sql = "SELECT * FROM visitante WHERE data_visita = ? AND ativo = 1 ORDER BY nome";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, data.toString());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    visitantes.add(mapResultSetToVisitante(rs));
                }
            }
        }
        
        return visitantes;
    }
    
    /**
     * Lista visitantes por período
     */
    public List<Visitante> findByPeriodo(LocalDate dataInicio, LocalDate dataFim) throws SQLException {
        List<Visitante> visitantes = new ArrayList<>();
        String sql = """
            SELECT * FROM visitante 
            WHERE data_visita BETWEEN ? AND ? AND ativo = 1 
            ORDER BY data_visita DESC, nome
            """;
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, dataInicio.toString());
            stmt.setString(2, dataFim.toString());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    visitantes.add(mapResultSetToVisitante(rs));
                }
            }
        }
        
        return visitantes;
    }
    
    /**
     * Lista visitantes por tipo
     */
    public List<Visitante> findByTipo(String tipo) throws SQLException {
        List<Visitante> visitantes = new ArrayList<>();
        String sql = "SELECT * FROM visitante WHERE tipo = ? AND ativo = 1 ORDER BY data_visita DESC, nome";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, tipo);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    visitantes.add(mapResultSetToVisitante(rs));
                }
            }
        }
        
        return visitantes;
    }
    
    /**
     * Lista visitantes pendentes de autorização
     */
    public List<Visitante> findPendentes() throws SQLException {
        List<Visitante> visitantes = new ArrayList<>();
        String sql = """
            SELECT * FROM visitante 
            WHERE autorizado = 0 AND ativo = 1 
            AND (tipo = 'IRMAO_VISITANTE' OR tipo = 'CONVIDADO')
            ORDER BY data_visita DESC, nome
            """;
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                visitantes.add(mapResultSetToVisitante(rs));
            }
        }
        
        return visitantes;
    }
    
    /**
     * Lista visitantes de hoje
     */
    public List<Visitante> findHoje() throws SQLException {
        return findByData(LocalDate.now());
    }
    
    /**
     * Busca visitantes por nome
     */
    public List<Visitante> findByNome(String nome) throws SQLException {
        List<Visitante> visitantes = new ArrayList<>();
        String sql = "SELECT * FROM visitante WHERE nome LIKE ? AND ativo = 1 ORDER BY data_visita DESC, nome";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nome + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    visitantes.add(mapResultSetToVisitante(rs));
                }
            }
        }
        
        return visitantes;
    }
    
    /**
     * Autoriza um visitante
     */
    public void autorizar(Long id, String autorizador) throws SQLException {
        String sql = """
            UPDATE visitante SET autorizado = 1, autorizado_por = ?, updated_at = CURRENT_TIMESTAMP 
            WHERE id = ?
            """;
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, autorizador);
            stmt.setLong(2, id);
            
            stmt.executeUpdate();
            conn.commit();
            
            logger.debug("Visitante autorizado: ID {}, por: {}", id, autorizador);
        }
    }
    
    /**
     * Nega autorização a um visitante
     */
    public void negarAutorizacao(Long id) throws SQLException {
        String sql = """
            UPDATE visitante SET autorizado = 0, autorizado_por = NULL, updated_at = CURRENT_TIMESTAMP 
            WHERE id = ?
            """;
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            stmt.executeUpdate();
            conn.commit();
            
            logger.debug("Autorização negada ao visitante: ID {}", id);
        }
    }
    
    /**
     * Gera número de cracha único
     */
    public String gerarNumeroCracha() throws SQLException {
        String sql = "SELECT COUNT(*) FROM visitante WHERE data_cadastro = CURRENT_DATE";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                int count = rs.getInt(1) + 1;
                return "V" + LocalDate.now().getYear() + String.format("%03d", count);
            }
        }
        
        return "V" + LocalDate.now().getYear() + "001";
    }
    
    /**
     * Obtém estatísticas de visitantes
     */
    public List<Object[]> getEstatisticas() throws SQLException {
        List<Object[]> estatisticas = new ArrayList<>();
        String sql = """
            SELECT tipo, COUNT(*) as quantidade, 
                   COUNT(CASE WHEN autorizado = 1 THEN 1 END) as autorizados,
                   COUNT(CASE WHEN autorizado = 0 THEN 1 END) as pendentes
            FROM visitante 
            WHERE ativo = 1 
            GROUP BY tipo 
            ORDER BY tipo
            """;
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Object[] estatistica = {
                    rs.getString("tipo"),
                    rs.getInt("quantidade"),
                    rs.getInt("autorizados"),
                    rs.getInt("pendentes")
                };
                estatisticas.add(estatistica);
            }
        }
        
        return estatisticas;
    }
    
    /**
     * Conta visitantes por tipo
     */
    public int countByTipo(String tipo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM visitante WHERE tipo = ? AND ativo = 1";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            stmt.setString(1, tipo);
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        
        return 0;
    }
    
    /**
     * Conta visitantes pendentes
     */
    public int countPendentes() throws SQLException {
        String sql = """
            SELECT COUNT(*) FROM visitante 
            WHERE autorizado = 0 AND ativo = 1 
            AND (tipo = 'IRMAO_VISITANTE' OR tipo = 'CONVIDADO')
            """;
        
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
     * Exclui (desativa) um visitante
     */
    public void delete(Long id) throws SQLException {
        String sql = "UPDATE visitante SET ativo = 0 WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            conn.commit();
            
            logger.debug("Visitante desativado: ID {}", id);
        }
    }
    
    /**
     * Mapeia ResultSet para objeto Visitante
     */
    private Visitante mapResultSetToVisitante(ResultSet rs) throws SQLException {
        Visitante visitante = new Visitante();
        
        visitante.setId(rs.getLong("id"));
        visitante.setNome(rs.getString("nome"));
        
        String dataVisitaStr = rs.getString("data_visita");
        if (dataVisitaStr != null && !dataVisitaStr.isEmpty()) {
            try {
                // Converter formato SQLite (YYYY-MM-DD HH:MM:SS) para LocalDate
                String dataFormatada = dataVisitaStr.split(" ")[0];
                visitante.setDataVisita(LocalDate.parse(dataFormatada));
            } catch (Exception e) {
                logger.warn("Erro ao converter data de visita: {}", dataVisitaStr);
            }
        }
        
        visitante.setGrauSecreto(rs.getString("grau_secreto"));
        visitante.setHistorico(rs.getString("historico"));
        visitante.setTipo(rs.getString("tipo"));
        visitante.setLojaOrigem(rs.getString("loja_origem"));
        visitante.setTelefone(rs.getString("telefone"));
        visitante.setEmail(rs.getString("email"));
        visitante.setAutorizadoPor(rs.getString("autorizado_por"));
        visitante.setAutorizado(rs.getBoolean("autorizado"));
        visitante.setObservacoes(rs.getString("observacoes"));
        visitante.setNumeroCracha(rs.getString("numero_cracha"));
        
        String dataCadastroStr = rs.getString("data_cadastro");
        if (dataCadastroStr != null && !dataCadastroStr.isEmpty()) {
            try {
                // Converter formato SQLite (YYYY-MM-DD HH:MM:SS) para LocalDate
                String dataFormatada = dataCadastroStr.split(" ")[0];
                visitante.setDataCadastro(LocalDate.parse(dataFormatada));
            } catch (Exception e) {
                logger.warn("Erro ao converter data de cadastro: {}", dataCadastroStr);
            }
        }
        
        visitante.setAtivo(rs.getBoolean("ativo"));
        
        return visitante;
    }
}
