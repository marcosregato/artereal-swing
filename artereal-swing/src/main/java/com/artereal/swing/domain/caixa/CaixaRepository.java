package com.artereal.swing.domain.caixa;

import java.util.List;
import java.util.Optional;

/**
 * Interface do repositório para Caixas
 * 
 * Esta interface define os contratos para persistência e recuperação
 * de entidades Caixa, seguindo o padrão Repository.
 */
public interface CaixaRepository {
    
    /**
     * Busca um caixa pelo ID
     */
    Optional<Caixa> findById(Long id);
    
    /**
     * Busca todos os caixas
     */
    List<Caixa> findAll();
    
    /**
     * Busca caixas pelo status
     */
    List<Caixa> findByStatus(StatusCaixa status);
    
    /**
     * Busca caixas pela loja
     */
    List<Caixa> findByLojaId(Long lojaId);
    
    /**
     * Busca caixas abertos de uma loja
     */
    List<Caixa> findAbertosByLojaId(Long lojaId);
    
    /**
     * Salva um caixa (cria ou atualiza)
     */
    Caixa save(Caixa caixa);
    
    /**
     * Remove um caixa pelo ID
     */
    void delete(Long id);
    
    /**
     * Conta o total de caixas
     */
    long count();
    
    /**
     * Conta caixas por status
     */
    long countByStatus(StatusCaixa status);
    
    /**
     * Busca caixas com paginação
     */
    List<Caixa> findAllWithPagination(int page, int size);
    
    /**
     * Busca caixas por responsável
     */
    List<Caixa> findByResponsavel(String responsavel);
    
    /**
     * Busca caixas por período
     */
    List<Caixa> findByDataAberturaBetween(java.time.LocalDateTime inicio, 
                                      java.time.LocalDateTime fim);
}
