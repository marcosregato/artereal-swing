package com.artereal.swing.dao;

import com.artereal.swing.database.DatabaseManager;
import com.artereal.swing.model.Caixa;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operações com Caixa
 */
public class CaixaDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(CaixaDAO.class);
    private final DatabaseManager dbManager;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public CaixaDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }
    
    public void save(Caixa caixa) throws SQLException {
        String sql;
        if (caixa.getId() == null) {
            sql = """
                INSERT INTO caixa (data, historico, entrada, saida, saldo, grupo, 
                    lancamento, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
                """;
        } else {
            sql = """
                UPDATE caixa SET data = ?, historico = ?, entrada = ?, saida = ?, 
                    saldo = ?, grupo = ?, lancamento = ?, updated_at = CURRENT_TIMESTAMP
                WHERE id = ?
                """;
        }
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Determinar se é entrada ou saída baseado no tipo
            BigDecimal entrada = BigDecimal.ZERO;
            BigDecimal saida = BigDecimal.ZERO;
            
            if ("RECEITA".equals(caixa.getTipo())) {
                entrada = caixa.getValor();
            } else if ("DESPESA".equals(caixa.getTipo())) {
                saida = caixa.getValor();
            }
            
            stmt.setString(1, caixa.getDataMovimentacao() != null ? caixa.getDataMovimentacao().format(formatter) : LocalDateTime.now().format(formatter));
            stmt.setString(2, caixa.getDescricao());
            stmt.setBigDecimal(3, entrada);
            stmt.setBigDecimal(4, saida);
            stmt.setBigDecimal(5, entrada.subtract(saida)); // saldo = entrada - saida
            stmt.setString(6, caixa.getCategoria());
            stmt.setString(7, caixa.getResponsavel());
            
            if (caixa.getId() != null) {
                stmt.setLong(8, caixa.getId());
            }
            
            int affectedRows = stmt.executeUpdate();
            
            if (caixa.getId() == null && affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        caixa.setId(generatedKeys.getLong(1));
                    }
                }
            }
        }
    }
    
    public Caixa findById(Long id) throws SQLException {
        String sql = "SELECT * FROM caixa WHERE id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCaixa(rs);
                }
            }
        }
        return null;
    }
    
    public List<Caixa> findAll() throws SQLException {
        logger.info("Buscando todas as movimentações do caixa");
        List<Caixa> movimentacoes = new ArrayList<>();
        String sql = "SELECT * FROM caixa ORDER BY data DESC";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                movimentacoes.add(mapResultSetToCaixa(rs));
            }
            logger.info("Encontradas {} movimentações no caixa", movimentacoes.size());
        } catch (SQLException e) {
            logger.error("Erro ao buscar movimentações do caixa: {}", e.getMessage(), e);
            throw e;
        }
        return movimentacoes;
    }
    
    public List<Caixa> findByTipo(String tipo) throws SQLException {
        List<Caixa> movimentacoes = new ArrayList<>();
        // Como a tabela não tem coluna 'tipo', vamos filtrar por entrada/saida
        String sql;
        if ("RECEITA".equals(tipo)) {
            sql = "SELECT * FROM caixa WHERE entrada > 0 ORDER BY data DESC";
        } else if ("DESPESA".equals(tipo)) {
            sql = "SELECT * FROM caixa WHERE saida > 0 ORDER BY data DESC";
        } else {
            sql = "SELECT * FROM caixa ORDER BY data DESC";
        }
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                movimentacoes.add(mapResultSetToCaixa(rs));
            }
        }
        return movimentacoes;
    }
    
    public List<Caixa> findByStatus(String status) throws SQLException {
        List<Caixa> movimentacoes = new ArrayList<>();
        // Como a tabela não tem coluna 'status', vamos retornar todos
        String sql = "SELECT * FROM caixa ORDER BY data DESC";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                movimentacoes.add(mapResultSetToCaixa(rs));
            }
        }
        return movimentacoes;
    }
    
    public BigDecimal getSaldo() throws SQLException {
        String sql = """
            SELECT COALESCE(SUM(entrada) - SUM(saida), 0) as saldo
            FROM caixa
            """;
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getBigDecimal("saldo");
            }
        }
        return BigDecimal.ZERO;
    }
    
    public BigDecimal getTotalReceitas() throws SQLException {
        String sql = "SELECT COALESCE(SUM(entrada), 0) as total FROM caixa";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getBigDecimal("total");
            }
        }
        return BigDecimal.ZERO;
    }
    
    public BigDecimal getTotalDespesas() throws SQLException {
        String sql = "SELECT COALESCE(SUM(saida), 0) as total FROM caixa";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getBigDecimal("total");
            }
        }
        return BigDecimal.ZERO;
    }
    
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM caixa WHERE id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
    
    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM caixa";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    private Caixa mapResultSetToCaixa(ResultSet rs) throws SQLException {
        logger.debug("Mapeando ResultSet para Caixa");
        Caixa caixa = new Caixa();
        caixa.setId(rs.getLong("id"));
        
        // Parse data com tratamento de erro
        String dataStr = rs.getString("data");
        if (dataStr != null && !dataStr.isEmpty()) {
            try {
                LocalDateTime dataMov;
                if (dataStr.contains(" ")) {
                    dataMov = LocalDateTime.parse(dataStr, formatter);
                } else {
                    dataMov = LocalDateTime.parse(dataStr + " 00:00:00", formatter);
                }
                caixa.setDataMovimentacao(dataMov);
                logger.debug("Data parseada com sucesso: {} -> {}", dataStr, dataMov);
            } catch (Exception e) {
                logger.warn("Erro ao converter data '{}': {}", dataStr, e.getMessage());
                caixa.setDataMovimentacao(null);
            }
        } else {
            logger.debug("Data é nula ou vazia para caixa ID: {}", caixa.getId());
        }
        
        caixa.setDescricao(rs.getString("historico"));
        
        // Determinar tipo e valor baseado em entrada/saida
        BigDecimal entrada = rs.getBigDecimal("entrada");
        BigDecimal saida = rs.getBigDecimal("saida");
        
        if (entrada != null && entrada.compareTo(BigDecimal.ZERO) > 0) {
            caixa.setTipo("RECEITA");
            caixa.setValor(entrada);
            logger.debug("Movimentação do tipo RECEITA: valor={}", entrada);
        } else if (saida != null && saida.compareTo(BigDecimal.ZERO) > 0) {
            caixa.setTipo("DESPESA");
            caixa.setValor(saida);
            logger.debug("Movimentação do tipo DESPESA: valor={}", saida);
        } else {
            caixa.setTipo("OUTRO");
            caixa.setValor(BigDecimal.ZERO);
            logger.debug("Movimentação do tipo OUTRO: valor=0");
        }
        
        // Campo 'observacoes' não existe na tabela caixa, vamos usar null ou string vazia
        caixa.setObservacoes(null);
        
        logger.debug("Caixa mapeado: ID={}, Tipo={}, Valor={}, Data={}", 
                    caixa.getId(), caixa.getTipo(), caixa.getValor(), caixa.getDataMovimentacao());
        
        return caixa;
    }
}
