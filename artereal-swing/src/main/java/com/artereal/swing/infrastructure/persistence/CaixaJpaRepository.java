package com.artereal.swing.infrastructure.persistence;

import com.artereal.swing.domain.caixa.Caixa;
import com.artereal.swing.domain.caixa.CaixaRepository;
import com.artereal.swing.domain.caixa.StatusCaixa;

import java.math.BigDecimal;
import java.util.*;
import java.sql.*;
import javax.sql.DataSource;

/**
 * Implementação do repositório de Caixas usando JDBC
 * 
 * Esta classe implementa a interface CaixaRepository fornecendo
 * persistência através de JDBC, seguindo o padrão Adapter.
 */

public class CaixaJpaRepository implements CaixaRepository {
    
    private final DataSource dataSource;
    
    public CaixaJpaRepository(DataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource, "DataSource é obrigatório");
    }
    
    @Override
    public Optional<Caixa> findById(Long id) {
        String sql = "SELECT * FROM caixas WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearParaEntidade(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar caixa por ID", e);
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<Caixa> findAll() {
        String sql = "SELECT * FROM caixas ORDER BY data_abertura DESC";
        List<Caixa> caixas = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                caixas.add(mapearParaEntidade(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar todas as caixas", e);
        }
        
        return caixas;
    }
    
    @Override
    public List<Caixa> findByStatus(StatusCaixa status) {
        String sql = "SELECT * FROM caixas WHERE status = ? ORDER BY data_abertura DESC";
        List<Caixa> caixas = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status.name());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    caixas.add(mapearParaEntidade(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar caixas por status", e);
        }
        
        return caixas;
    }
    
    @Override
    public List<Caixa> findByLojaId(Long lojaId) {
        String sql = "SELECT * FROM caixas WHERE loja_id = ? ORDER BY data_abertura DESC";
        List<Caixa> caixas = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, lojaId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    caixas.add(mapearParaEntidade(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar caixas por loja", e);
        }
        
        return caixas;
    }
    
    @Override
    public List<Caixa> findAbertosByLojaId(Long lojaId) {
        String sql = "SELECT * FROM caixas WHERE loja_id = ? AND status = 'ABERTO' ORDER BY data_abertura DESC";
        List<Caixa> caixas = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, lojaId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    caixas.add(mapearParaEntidade(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar caixas abertos por loja", e);
        }
        
        return caixas;
    }
    
    @Override
    public Caixa save(Caixa caixa) {
        if (caixa.getId() == null) {
            return inserir(caixa);
        } else {
            return atualizar(caixa);
        }
    }
    
    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM caixas WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar caixa", e);
        }
    }
    
    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM caixas";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            return rs.next() ? rs.getLong(1) : 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar caixas", e);
        }
    }
    
    @Override
    public long countByStatus(StatusCaixa status) {
        String sql = "SELECT COUNT(*) FROM caixas WHERE status = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status.name());
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getLong(1) : 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar caixas por status", e);
        }
    }
    
    @Override
    public List<Caixa> findAllWithPagination(int page, int size) {
        String sql = "SELECT * FROM caixas ORDER BY data_abertura DESC LIMIT ? OFFSET ?";
        List<Caixa> caixas = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, size);
            stmt.setInt(2, page * size);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    caixas.add(mapearParaEntidade(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar caixas com paginação", e);
        }
        
        return caixas;
    }
    
    @Override
    public List<Caixa> findByResponsavel(String responsavel) {
        String sql = "SELECT * FROM caixas WHERE responsavel ILIKE ? ORDER BY data_abertura DESC";
        List<Caixa> caixas = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + responsavel + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    caixas.add(mapearParaEntidade(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar caixas por responsável", e);
        }
        
        return caixas;
    }
    
    @Override
    public List<Caixa> findByDataAberturaBetween(java.time.LocalDateTime inicio, 
                                              java.time.LocalDateTime fim) {
        String sql = "SELECT * FROM caixas WHERE data_abertura BETWEEN ? AND ? ORDER BY data_abertura DESC";
        List<Caixa> caixas = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(inicio));
            stmt.setTimestamp(2, Timestamp.valueOf(fim));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    caixas.add(mapearParaEntidade(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar caixas por período", e);
        }
        
        return caixas;
    }
    
    /**
     * Mapeia ResultSet para entidade Caixa
     */
    private Caixa mapearParaEntidade(ResultSet rs) throws SQLException {
        try {
            // Extrair valores do ResultSet
            Long id = rs.getLong("id");
            Long lojaId = rs.getObject("loja_id") != null ? rs.getLong("loja_id") : null;
            BigDecimal valor = rs.getBigDecimal("valor");
            String responsavel = rs.getString("responsavel");
            
            // Usar reflexão para chamar o construtor privado
            java.lang.reflect.Constructor<Caixa> constructor = 
                Caixa.class.getDeclaredConstructor(Long.class, Long.class, BigDecimal.class, String.class);
            constructor.setAccessible(true);
            
            Caixa caixa = constructor.newInstance(id, lojaId, valor, responsavel);
            
            // Setar campos adicionais usando reflexão
            java.lang.reflect.Field statusField = Caixa.class.getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(caixa, StatusCaixa.valueOf(rs.getString("status")));
            
            java.lang.reflect.Field dataAberturaField = Caixa.class.getDeclaredField("dataAbertura");
            dataAberturaField.setAccessible(true);
            dataAberturaField.set(caixa, rs.getTimestamp("data_abertura").toLocalDateTime());
            
            java.lang.reflect.Field dataFechamentoField = Caixa.class.getDeclaredField("dataFechamento");
            dataFechamentoField.setAccessible(true);
            
            Timestamp dataFechamentoTs = rs.getTimestamp("data_fechamento");
            if (dataFechamentoTs != null) {
                dataFechamentoField.set(caixa, dataFechamentoTs.toLocalDateTime());
            }
            
            java.lang.reflect.Field saldoField = Caixa.class.getDeclaredField("saldo");
            saldoField.setAccessible(true);
            saldoField.set(caixa, rs.getBigDecimal("saldo"));
            
            return caixa;
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao mapear ResultSet para Caixa", e);
        }
    }
    
    /**
     * Insere um novo caixa
     */
    private Caixa inserir(Caixa caixa) {
        String sql = """
            INSERT INTO caixas (loja_id, valor, responsavel, status, data_abertura, data_fechamento, saldo)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            Long lojaId = caixa.getLojaId();
            if (lojaId != null) {
                stmt.setLong(1, lojaId);
            } else {
                stmt.setNull(1, Types.BIGINT);
            }
            
            stmt.setBigDecimal(2, caixa.getValor());
            stmt.setString(3, caixa.getResponsavel());
            stmt.setString(4, caixa.getStatus().name());
            stmt.setTimestamp(5, Timestamp.valueOf(caixa.getDataAbertura()));
            
            Timestamp dataFechamento = caixa.getDataFechamento() != null ? 
                Timestamp.valueOf(caixa.getDataFechamento()) : null;
            if (dataFechamento != null) {
                stmt.setTimestamp(6, dataFechamento);
            } else {
                stmt.setNull(6, Types.TIMESTAMP);
            }
            
            stmt.setBigDecimal(7, caixa.getSaldo());
            
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    java.lang.reflect.Field idField = Caixa.class.getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(caixa, generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir caixa", e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao definir ID gerado", e);
        }
        
        return caixa;
    }
    
    /**
     * Atualiza um caixa existente
     */
    private Caixa atualizar(Caixa caixa) {
        String sql = """
            UPDATE caixas SET 
                loja_id = ?, valor = ?, responsavel = ?, status = ?, 
                data_abertura = ?, data_fechamento = ?, saldo = ?
            WHERE id = ?
            """;
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            Long lojaId = caixa.getLojaId();
            if (lojaId != null) {
                stmt.setLong(1, lojaId);
            } else {
                stmt.setNull(1, Types.BIGINT);
            }
            
            stmt.setBigDecimal(2, caixa.getValor());
            stmt.setString(3, caixa.getResponsavel());
            stmt.setString(4, caixa.getStatus().name());
            stmt.setTimestamp(5, Timestamp.valueOf(caixa.getDataAbertura()));
            
            Timestamp dataFechamento = caixa.getDataFechamento() != null ? 
                Timestamp.valueOf(caixa.getDataFechamento()) : null;
            if (dataFechamento != null) {
                stmt.setTimestamp(6, dataFechamento);
            } else {
                stmt.setNull(6, Types.TIMESTAMP);
            }
            
            stmt.setBigDecimal(7, caixa.getSaldo());
            stmt.setLong(8, caixa.getId());
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar caixa", e);
        }
        
        return caixa;
    }
}
