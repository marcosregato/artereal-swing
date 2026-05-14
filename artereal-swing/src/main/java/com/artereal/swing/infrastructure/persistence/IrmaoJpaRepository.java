package com.artereal.swing.infrastructure.persistence;

import com.artereal.swing.domain.irmao.Irmao;
import com.artereal.swing.domain.irmao.IrmaoRepository;
import com.artereal.swing.domain.irmao.StatusIrmao;


import java.util.*;
import java.sql.*;
import javax.sql.DataSource;

/**
 * Implementação do repositório de Irmãos usando JDBC
 * 
 * Esta classe implementa a interface IrmaoRepository fornecendo
 * persistência através de JDBC, seguindo o padrão Adapter.
 */

public class IrmaoJpaRepository implements IrmaoRepository {
    
    private final DataSource dataSource;
    
    public IrmaoJpaRepository(DataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource, "DataSource é obrigatório");
    }
    
    @Override
    public Optional<Irmao> findById(Long id) {
        String sql = "SELECT * FROM irmaos WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearParaEntidade(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar irmão por ID", e);
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<Irmao> findAll() {
        String sql = "SELECT * FROM irmaos ORDER BY nome";
        List<Irmao> irmaos = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                irmaos.add(mapearParaEntidade(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar todos os irmãos", e);
        }
        
        return irmaos;
    }
    
    @Override
    public List<Irmao> findByStatus(StatusIrmao status) {
        String sql = "SELECT * FROM irmaos WHERE status = ? ORDER BY nome";
        List<Irmao> irmaos = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status.name());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    irmaos.add(mapearParaEntidade(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar irmãos por status", e);
        }
        
        return irmaos;
    }
    
    @Override
    public List<Irmao> findByLojaId(Long lojaId) {
        String sql = "SELECT * FROM irmaos WHERE loja_id = ? ORDER BY nome";
        List<Irmao> irmaos = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, lojaId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    irmaos.add(mapearParaEntidade(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar irmãos por loja", e);
        }
        
        return irmaos;
    }
    
    @Override
    public List<Irmao> findByLojaIdAndStatus(Long lojaId, StatusIrmao status) {
        String sql = "SELECT * FROM irmaos WHERE loja_id = ? AND status = ? ORDER BY nome";
        List<Irmao> irmaos = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, lojaId);
            stmt.setString(2, status.name());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    irmaos.add(mapearParaEntidade(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar irmãos por loja e status", e);
        }
        
        return irmaos;
    }
    
    @Override
    public boolean existsByCpf(String cpf) {
        String sql = "SELECT COUNT(*) FROM irmaos WHERE cpf = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cpf);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar existência de CPF", e);
        }
    }
    
    @Override
    public Irmao save(Irmao irmao) {
        if (irmao.getId() == null) {
            return inserir(irmao);
        } else {
            return atualizar(irmao);
        }
    }
    
    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM irmaos WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar irmão", e);
        }
    }
    
    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM irmaos";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            return rs.next() ? rs.getLong(1) : 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar irmãos", e);
        }
    }
    
    @Override
    public long countByStatus(StatusIrmao status) {
        String sql = "SELECT COUNT(*) FROM irmaos WHERE status = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status.name());
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getLong(1) : 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar irmãos por status", e);
        }
    }
    
    @Override
    public List<Irmao> findAllWithPagination(int page, int size) {
        String sql = "SELECT * FROM irmaos ORDER BY nome LIMIT ? OFFSET ?";
        List<Irmao> irmaos = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, size);
            stmt.setInt(2, page * size);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    irmaos.add(mapearParaEntidade(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar irmãos com paginação", e);
        }
        
        return irmaos;
    }
    
    @Override
    public List<Irmao> findByNomeContaining(String nome) {
        String sql = "SELECT * FROM irmaos WHERE nome ILIKE ? ORDER BY nome";
        List<Irmao> irmaos = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nome + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    irmaos.add(mapearParaEntidade(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar irmãos por nome", e);
        }
        
        return irmaos;
    }
    
    @Override
    public Optional<Irmao> findByCpf(String cpf) {
        String sql = "SELECT * FROM irmaos WHERE cpf = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cpf);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearParaEntidade(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar irmão por CPF", e);
        }
        
        return Optional.empty();
    }
    
    /**
     * Mapeia ResultSet para entidade Irmao
     */
    private Irmao mapearParaEntidade(ResultSet rs) throws SQLException {
        try {
            // Extrair valores do ResultSet
            Long id = rs.getLong("id");
            String nome = rs.getString("nome");
            String cpf = rs.getString("cpf");
            String telefone = rs.getString("telefone");
            String email = rs.getString("email");
            
            // Usar reflexão para chamar o construtor privado
            java.lang.reflect.Constructor<Irmao> constructor = 
                Irmao.class.getDeclaredConstructor(Long.class, String.class, String.class, String.class, String.class);
            constructor.setAccessible(true);
            
            Irmao irmao = constructor.newInstance(id, nome, cpf, telefone, email);
            
            // Setar campos adicionais usando reflexão
            java.lang.reflect.Field statusField = Irmao.class.getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(irmao, StatusIrmao.valueOf(rs.getString("status")));
            
            java.lang.reflect.Field lojaIdField = Irmao.class.getDeclaredField("lojaId");
            lojaIdField.setAccessible(true);
            
            Long lojaId = rs.getObject("loja_id") != null ? rs.getLong("loja_id") : null;
            lojaIdField.set(irmao, lojaId);
            
            return irmao;
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao mapear ResultSet para Irmao", e);
        }
    }
    
    /**
     * Insere um novo irmão
     */
    private Irmao inserir(Irmao irmao) {
        String sql = """
            INSERT INTO irmaos (nome, cpf, telefone, email, status, loja_id, data_criacao, data_atualizacao)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, irmao.getNome());
            stmt.setString(2, irmao.getCpf());
            stmt.setString(3, irmao.getTelefone());
            stmt.setString(4, irmao.getEmail());
            stmt.setString(5, irmao.getStatus().name());
            
            Long lojaId = irmao.getLojaId();
            if (lojaId != null) {
                stmt.setLong(6, lojaId);
            } else {
                stmt.setNull(6, Types.BIGINT);
            }
            
            stmt.setTimestamp(7, Timestamp.valueOf(irmao.getDataCriacao()));
            stmt.setTimestamp(8, Timestamp.valueOf(irmao.getDataAtualizacao()));
            
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    java.lang.reflect.Field idField = Irmao.class.getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(irmao, generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir irmão", e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao definir ID gerado", e);
        }
        
        return irmao;
    }
    
    /**
     * Atualiza um irmão existente
     */
    private Irmao atualizar(Irmao irmao) {
        String sql = """
            UPDATE irmaos SET 
                nome = ?, cpf = ?, telefone = ?, email = ?, status = ?, loja_id = ?, data_atualizacao = ?
            WHERE id = ?
            """;
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, irmao.getNome());
            stmt.setString(2, irmao.getCpf());
            stmt.setString(3, irmao.getTelefone());
            stmt.setString(4, irmao.getEmail());
            stmt.setString(5, irmao.getStatus().name());
            
            Long lojaId = irmao.getLojaId();
            if (lojaId != null) {
                stmt.setLong(6, lojaId);
            } else {
                stmt.setNull(6, Types.BIGINT);
            }
            
            stmt.setTimestamp(7, Timestamp.valueOf(irmao.getDataAtualizacao()));
            stmt.setLong(8, irmao.getId());
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar irmão", e);
        }
        
        return irmao;
    }
}
