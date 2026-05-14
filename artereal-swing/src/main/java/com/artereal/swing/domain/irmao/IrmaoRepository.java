package com.artereal.swing.domain.irmao;

import java.util.List;
import java.util.Optional;

/**
 * Interface do repositório para Irmãos
 * 
 * Esta interface define os contratos para persistência e recuperação
 * de entidades Irmao, seguindo o padrão Repository.
 */
public interface IrmaoRepository {
    
    /**
     * Busca um irmão pelo ID
     */
    Optional<Irmao> findById(Long id);
    
    /**
     * Busca todos os irmãos
     */
    List<Irmao> findAll();
    
    /**
     * Busca irmãos pelo status
     */
    List<Irmao> findByStatus(StatusIrmao status);
    
    /**
     * Busca irmãos pela loja
     */
    List<Irmao> findByLojaId(Long lojaId);
    
    /**
     * Busca irmãos ativos em uma loja
     */
    List<Irmao> findByLojaIdAndStatus(Long lojaId, StatusIrmao status);
    
    /**
     * Verifica se existe irmão com o CPF informado
     */
    boolean existsByCpf(String cpf);
    
    /**
     * Salva um irmão (cria ou atualiza)
     */
    Irmao save(Irmao irmao);
    
    /**
     * Remove um irmão pelo ID
     */
    void delete(Long id);
    
    /**
     * Conta o total de irmãos
     */
    long count();
    
    /**
     * Conta irmãos por status
     */
    long countByStatus(StatusIrmao status);
    
    /**
     * Busca irmãos com paginação
     */
    List<Irmao> findAllWithPagination(int page, int size);
    
    /**
     * Busca irmãos por nome (contém)
     */
    List<Irmao> findByNomeContaining(String nome);
    
    /**
     * Busca irmãos por CPF exato
     */
    Optional<Irmao> findByCpf(String cpf);
}
