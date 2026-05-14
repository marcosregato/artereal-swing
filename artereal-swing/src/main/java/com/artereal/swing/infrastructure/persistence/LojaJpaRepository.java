package com.artereal.swing.infrastructure.persistence;

import com.artereal.swing.domain.loja.Loja;
import com.artereal.swing.domain.loja.LojaRepository;
import com.artereal.swing.domain.loja.StatusLoja;

import java.util.*;
import java.sql.*;
import javax.sql.DataSource;

/**
 * Implementação do repositório de Lojas usando JDBC
 * 
 * Esta classe implementa a interface LojaRepository fornecendo
 * persistência através de JDBC, seguindo o padrão Adapter.
 */
public class LojaJpaRepository implements LojaRepository {
    
    private final DataSource dataSource;
    
    public LojaJpaRepository(DataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource, "DataSource é obrigatório");
    }
    
    @Override
    public Optional<Loja> findById(Long id) {
        String sql = "SELECT * FROM lojas WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearParaEntidade(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar loja por ID", e);
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<Loja> findAll() {
        String sql = "SELECT * FROM lojas ORDER BY nome";
        List<Loja> lojas = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                lojas.add(mapearParaEntidade(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar todas as lojas", e);
        }
        
        return lojas;
    }
    
    @Override
    public List<Loja> findByStatus(StatusLoja status) {
        String sql = "SELECT * FROM lojas WHERE status = ? ORDER BY nome";
        List<Loja> lojas = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status.name());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lojas.add(mapearParaEntidade(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar lojas por status", e);
        }
        
        return lojas;
    }
    
    @Override
    public List<Loja> findByCidade(String cidade) {
        String sql = "SELECT * FROM lojas WHERE cidade ILIKE ? ORDER BY nome";
        List<Loja> lojas = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + cidade + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lojas.add(mapearParaEntidade(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar lojas por cidade", e);
        }
        
        return lojas;
    }
    
    @Override
    public List<Loja> findByEstado(String estado) {
        String sql = "SELECT * FROM lojas WHERE estado = ? ORDER BY nome";
        List<Loja> lojas = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, estado);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lojas.add(mapearParaEntidade(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar lojas por estado", e);
        }
        
        return lojas;
    }
    
    @Override
    public boolean existsByCnpj(String cnpj) {
        String sql = "SELECT COUNT(*) FROM lojas WHERE cnpj = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cnpj);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar existência de CNPJ", e);
        }
    }
    
    @Override
    public Loja save(Loja loja) {
        if (loja.getId() == null) {
            return inserir(loja);
        } else {
            return atualizar(loja);
        }
    }
    
    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM lojas WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar loja", e);
        }
    }
    
    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM lojas";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            return rs.next() ? rs.getLong(1) : 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar lojas", e);
        }
    }
    
    @Override
    public long countByStatus(StatusLoja status) {
        String sql = "SELECT COUNT(*) FROM lojas WHERE status = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status.name());
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getLong(1) : 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar lojas por status", e);
        }
    }
    
    @Override
    public List<Loja> findAllWithPagination(int page, int size) {
        String sql = "SELECT * FROM lojas ORDER BY nome LIMIT ? OFFSET ?";
        List<Loja> lojas = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, size);
            stmt.setInt(2, page * size);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lojas.add(mapearParaEntidade(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar lojas com paginação", e);
        }
        
        return lojas;
    }
    
    @Override
    public List<Loja> findByNomeContaining(String nome) {
        String sql = "SELECT * FROM lojas WHERE nome ILIKE ? ORDER BY nome";
        List<Loja> lojas = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nome + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lojas.add(mapearParaEntidade(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar lojas por nome", e);
        }
        
        return lojas;
    }
    
    /**
     * Mapeia ResultSet para entidade Loja
     */
    private Loja mapearParaEntidade(ResultSet rs) throws SQLException {
        try {
            // Reconstruir Value Objects a partir dos dados do banco
            com.artereal.swing.domain.loja.valueobjects.Cnpj cnpj = 
                new com.artereal.swing.domain.loja.valueobjects.Cnpj(rs.getString("cnpj"));
            
            com.artereal.swing.domain.loja.valueobjects.Endereco endereco = 
                new com.artereal.swing.domain.loja.valueobjects.Endereco(
                    rs.getString("endereco"), null, null, null, 
                    rs.getString("cidade"), rs.getString("estado"), null);
            
            // Usar reflexão para chamar o construtor privado
            java.lang.reflect.Constructor<Loja> constructor = 
                Loja.class.getDeclaredConstructor(Long.class, String.class, 
                    com.artereal.swing.domain.loja.valueobjects.Cnpj.class, 
                    com.artereal.swing.domain.loja.valueobjects.Endereco.class);
            constructor.setAccessible(true);
            
            Loja loja = constructor.newInstance(
                rs.getLong("id"),
                rs.getString("nome"),
                cnpj,
                endereco
            );
            
            // Setar status usando reflexão
            java.lang.reflect.Field statusField = Loja.class.getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(loja, StatusLoja.valueOf(rs.getString("status")));
            
            return loja;
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao mapear ResultSet para Loja", e);
        }
    }
    
    /**
     * Insere uma nova loja
     */
    private Loja inserir(Loja loja) {
        String sql = """
            INSERT INTO lojas (nome, cnpj, endereco, cidade, estado, status, data_criacao, data_atualizacao)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, loja.getNome());
            stmt.setString(2, loja.getCnpj().getNumeros());
            stmt.setString(3, loja.getEndereco().rua());
            stmt.setString(4, loja.getCidade());
            stmt.setString(5, loja.getEstado());
            stmt.setString(6, loja.getStatus().name());
            stmt.setTimestamp(7, Timestamp.valueOf(loja.getDataCriacao()));
            stmt.setTimestamp(8, Timestamp.valueOf(loja.getDataAtualizacao()));
            
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    java.lang.reflect.Field idField = Loja.class.getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(loja, generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir loja", e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao definir ID gerado", e);
        }
        
        return loja;
    }
    
    /**
     * Atualiza uma loja existente
     */
    private Loja atualizar(Loja loja) {
        String sql = """
            UPDATE lojas SET 
                nome = ?, cnpj = ?, endereco = ?, cidade = ?, estado = ?, 
                status = ?, data_atualizacao = ?
            WHERE id = ?
            """;
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, loja.getNome());
            stmt.setString(2, loja.getCnpj().getNumeros());
            stmt.setString(3, loja.getEndereco().rua());
            stmt.setString(4, loja.getCidade());
            stmt.setString(5, loja.getEstado());
            stmt.setString(6, loja.getStatus().name());
            stmt.setTimestamp(7, Timestamp.valueOf(loja.getDataAtualizacao()));
            stmt.setLong(8, loja.getId());
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar loja", e);
        }
        
        return loja;
    }
}
