package com.artereal.swing.dao;

import com.artereal.swing.database.DatabaseManager;
import com.artereal.swing.model.Biblioteca;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operações com Biblioteca
 */
public class BibliotecaDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(BibliotecaDAO.class);
    private final DatabaseManager dbManager;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public BibliotecaDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }
    
    public void save(Biblioteca biblioteca) throws SQLException {
        String sql;
        if (biblioteca.getId() == null) {
            sql = """
                INSERT INTO biblioteca (tipo, titulo, autor, isbn, editora, ano_publicacao, 
                    categoria, localizacao, status, nome_leitor, data_emprestimo, 
                    data_devolucao_prevista, data_devolucao_real, responsavel_emprestimo, 
                    multa, observacoes, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
                """;
        } else {
            sql = """
                UPDATE biblioteca SET tipo = ?, titulo = ?, autor = ?, isbn = ?, 
                    editora = ?, ano_publicacao = ?, categoria = ?, localizacao = ?, 
                    status = ?, nome_leitor = ?, data_emprestimo = ?, data_devolucao_prevista = ?, 
                    data_devolucao_real = ?, responsavel_emprestimo = ?, multa = ?, 
                    observacoes = ?, updated_at = CURRENT_TIMESTAMP
                WHERE id = ?
                """;
        }
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, biblioteca.getTipo());
            stmt.setString(2, biblioteca.getTitulo());
            stmt.setString(3, biblioteca.getAutor());
            stmt.setString(4, biblioteca.getIsbn());
            stmt.setString(5, biblioteca.getEditora());
            stmt.setString(6, biblioteca.getAnoPublicacao());
            stmt.setString(7, biblioteca.getCategoria());
            stmt.setString(8, biblioteca.getLocalizacao());
            stmt.setString(9, biblioteca.getStatus());
            stmt.setString(10, biblioteca.getNomeLeitor());
            stmt.setString(11, biblioteca.getDataEmprestimo() != null ? biblioteca.getDataEmprestimo().format(dateFormatter) : null);
            stmt.setString(12, biblioteca.getDataDevolucaoPrevista() != null ? biblioteca.getDataDevolucaoPrevista().format(dateFormatter) : null);
            stmt.setString(13, biblioteca.getDataDevolucaoReal() != null ? biblioteca.getDataDevolucaoReal().format(dateFormatter) : null);
            stmt.setString(14, biblioteca.getResponsavelEmprestimo());
            stmt.setBigDecimal(15, biblioteca.getMulta());
            stmt.setString(16, biblioteca.getObservacoes());
            
            if (biblioteca.getId() != null) {
                stmt.setLong(17, biblioteca.getId());
            }
            
            int affectedRows = stmt.executeUpdate();
            
            if (biblioteca.getId() == null && affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        biblioteca.setId(generatedKeys.getLong(1));
                    }
                }
            }
        }
    }
    
    public Biblioteca findById(Long id) throws SQLException {
        String sql = "SELECT * FROM biblioteca WHERE id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBiblioteca(rs);
                }
            }
        }
        return null;
    }
    
    public List<Biblioteca> findAll() throws SQLException {
        logger.info("Buscando todos os itens da biblioteca");
        List<Biblioteca> itens = new ArrayList<>();
        String sql = "SELECT * FROM biblioteca ORDER BY titulo";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                itens.add(mapResultSetToBiblioteca(rs));
            }
            logger.info("Encontrados {} itens na biblioteca", itens.size());
        } catch (SQLException e) {
            logger.error("Erro ao buscar itens da biblioteca: {}", e.getMessage(), e);
            throw e;
        }
        return itens;
    }
    
    public List<Biblioteca> findByTipo(String tipo) throws SQLException {
        List<Biblioteca> itens = new ArrayList<>();
        // Como a tabela não tem coluna 'tipo', vamos retornar todos os itens
        // e filtrar por status ou outro critério relevante
        String sql = "SELECT * FROM biblioteca ORDER BY titulo";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                itens.add(mapResultSetToBiblioteca(rs));
            }
        }
        return itens;
    }
    
    public List<Biblioteca> findLivrosDisponiveis() throws SQLException {
        List<Biblioteca> livros = new ArrayList<>();
        // Como a tabela não tem coluna 'tipo', vamos usar 'ativo' e 'estoque'
        String sql = "SELECT * FROM biblioteca WHERE ativo = 1 AND estoque > 0 ORDER BY titulo";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                livros.add(mapResultSetToBiblioteca(rs));
            }
        }
        return livros;
    }
    
    public List<Biblioteca> findEmprestimosAtivos() throws SQLException {
        List<Biblioteca> emprestimos = new ArrayList<>();
        // Como não há sistema de empréstimos na tabela, retornar lista vazia
        String sql = "SELECT * FROM biblioteca WHERE emprestados > 0 ORDER BY titulo";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                emprestimos.add(mapResultSetToBiblioteca(rs));
            }
        }
        return emprestimos;
    }
    
    public List<Biblioteca> findEmprestimosAtrasados() throws SQLException {
        List<Biblioteca> emprestimos = new ArrayList<>();
        // Como não há sistema de empréstimos com datas na tabela, retornar lista vazia
        String sql = "SELECT * FROM biblioteca WHERE emprestados > 0 ORDER BY titulo";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                emprestimos.add(mapResultSetToBiblioteca(rs));
            }
        }
        return emprestimos;
    }
    
    public List<Biblioteca> findByNomeLeitor(String nomeLeitor) throws SQLException {
        List<Biblioteca> emprestimos = new ArrayList<>();
        String sql = "SELECT * FROM biblioteca WHERE nome_leitor LIKE ? ORDER BY data_emprestimo DESC";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nomeLeitor + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    emprestimos.add(mapResultSetToBiblioteca(rs));
                }
            }
        }
        return emprestimos;
    }
    
    public int countLivros() throws SQLException {
        String sql = "SELECT COUNT(*) FROM biblioteca WHERE ativo = 1";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    public int countEmprestimosAtivos() throws SQLException {
        String sql = "SELECT COUNT(*) FROM biblioteca WHERE emprestados > 0";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM biblioteca WHERE id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
    
    private Biblioteca mapResultSetToBiblioteca(ResultSet rs) throws SQLException {
        Biblioteca biblioteca = new Biblioteca();
        biblioteca.setId(rs.getLong("id"));
        biblioteca.setTitulo(rs.getString("titulo"));
        biblioteca.setAutor(rs.getString("autor"));
        
        // Mapear 'assunto' para 'categoria'
        biblioteca.setCategoria(rs.getString("assunto"));
        
        // Determinar status baseado em 'ativo', 'estoque' e 'emprestados'
        boolean ativo = rs.getBoolean("ativo");
        int estoque = rs.getInt("estoque");
        int emprestados = rs.getInt("emprestados");
        
        if (!ativo) {
            biblioteca.setStatus("INATIVO");
            logger.debug("Item INATIVO: ID={}, Titulo={}", biblioteca.getId(), biblioteca.getTitulo());
        } else if (emprestados > 0 && emprestados >= estoque) {
            biblioteca.setStatus("INDISPONIVEL");
            logger.debug("Item INDISPONIVEL: ID={}, Titulo={}, Estoque={}, Emprestados={}", 
                        biblioteca.getId(), biblioteca.getTitulo(), estoque, emprestados);
        } else {
            biblioteca.setStatus("DISPONIVEL");
            logger.debug("Item DISPONIVEL: ID={}, Titulo={}, Estoque={}, Emprestados={}", 
                        biblioteca.getId(), biblioteca.getTitulo(), estoque, emprestados);
        }
        
        // Campos 'estoque' e 'emprestados' não existem no modelo Biblioteca
        // biblioteca.setEstoque(rs.getInt("estoque"));
        // biblioteca.setEmprestados(rs.getInt("emprestados"));
        
        logger.debug("Biblioteca mapeado: ID={}, Titulo={}, Autor={}, Status={}, Categoria={}", 
                    biblioteca.getId(), biblioteca.getTitulo(), biblioteca.getAutor(), 
                    biblioteca.getStatus(), biblioteca.getCategoria());
        
        return biblioteca;
    }
}
