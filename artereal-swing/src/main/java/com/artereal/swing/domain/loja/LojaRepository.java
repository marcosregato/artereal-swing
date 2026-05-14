package com.artereal.swing.domain.loja;

import java.util.List;
import java.util.Optional;

/**
 * Interface do repositório para Lojas
 * 
 * Esta interface define os contratos para persistência e recuperação
 * de entidades Loja, seguindo o padrão Repository.
 */
public interface LojaRepository {
    
    /**
     * Busca uma loja pelo ID
     */
    Optional<Loja> findById(Long id);
    
    /**
     * Busca todas as lojas
     */
    List<Loja> findAll();
    
    /**
     * Busca lojas pelo status
     */
    List<Loja> findByStatus(StatusLoja status);
    
    /**
     * Busca lojas pela cidade
     */
    List<Loja> findByCidade(String cidade);
    
    /**
     * Busca lojas pelo estado
     */
    List<Loja> findByEstado(String estado);
    
    /**
     * Verifica se existe loja com o CNPJ informado
     */
    boolean existsByCnpj(String cnpj);
    
    /**
     * Salva uma loja (cria ou atualiza)
     */
    Loja save(Loja loja);
    
    /**
     * Remove uma loja pelo ID
     */
    void delete(Long id);
    
    /**
     * Conta o total de lojas
     */
    long count();
    
    /**
     * Conta lojas por status
     */
    long countByStatus(StatusLoja status);
    
    /**
     * Busca lojas com paginação
     */
    List<Loja> findAllWithPagination(int page, int size);
    
    /**
     * Busca lojas por nome (contém)
     */
    List<Loja> findByNomeContaining(String nome);
}
