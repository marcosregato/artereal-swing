package com.artereal.swing.presentation;

import com.artereal.swing.application.loja.CriarLojaUseCase;
import com.artereal.swing.application.loja.CriarLojaRequest;
import com.artereal.swing.application.loja.LojaResponse;
import com.artereal.swing.application.loja.LojaMapper;
import com.artereal.swing.domain.loja.Loja;
import com.artereal.swing.domain.loja.LojaRepository;
import com.artereal.swing.domain.loja.StatusLoja;

import java.util.List;
import java.util.Optional;

/**
 * Service para gestão de Lojas em Swing UI
 * 
 * Esta classe orquestra as operações de lojas para a interface Swing,
 * seguindo os princípios da arquitetura hexagonal.
 */
public class LojaController {
    
    private final CriarLojaUseCase criarLojaUseCase;
    private final LojaRepository lojaRepository;
    
    public LojaController(CriarLojaUseCase criarLojaUseCase, 
                          LojaRepository lojaRepository) {
        this.criarLojaUseCase = criarLojaUseCase;
        this.lojaRepository = lojaRepository;
    }
    
    /**
     * Cria uma nova loja
     */
    public LojaResponse criarLoja(CriarLojaRequest request) {
        return criarLojaUseCase.execute(request);
    }
    
    /**
     * Busca uma loja pelo ID
     */
    public Optional<LojaResponse> buscarLoja(Long id) {
        return lojaRepository.findById(id)
            .map(LojaMapper::toResponse);
    }
    
    /**
     * Lista todas as lojas
     */
    public List<LojaResponse> listarLojas() {
        List<Loja> lojas = lojaRepository.findAll();
        return LojaMapper.toResponseList(lojas);
    }
    
    /**
     * Lista lojas por status
     */
    public List<LojaResponse> listarLojasPorStatus(StatusLoja status) {
        List<Loja> lojas = lojaRepository.findByStatus(status);
        return LojaMapper.toResponseList(lojas);
    }
    
    /**
     * Lista lojas por cidade
     */
    public List<LojaResponse> listarLojasPorCidade(String cidade) {
        List<Loja> lojas = lojaRepository.findByCidade(cidade);
        return LojaMapper.toResponseList(lojas);
    }
    
    /**
     * Lista lojas com paginação
     */
    public LojaMapper.PaginatedLojaResponse listarLojasPaginado(int pagina, int tamanho) {
        List<Loja> lojas = lojaRepository.findAllWithPagination(pagina, tamanho);
        long total = lojaRepository.count();
        
        return LojaMapper.toPaginatedResponse(lojas, pagina, tamanho, total);
    }
    
    /**
     * Busca lojas por nome
     */
    public List<LojaResponse> buscarLojasPorNome(String nome) {
        List<Loja> lojas = lojaRepository.findByNomeContaining(nome);
        return LojaMapper.toResponseList(lojas);
    }
    
    /**
     * Inativa uma loja
     */
    public boolean inativarLoja(Long id) {
        Optional<Loja> loja = lojaRepository.findById(id);
        if (loja.isPresent()) {
            loja.get().inativar();
            lojaRepository.save(loja.get());
            return true;
        }
        return false;
    }
    
    /**
     * Reativa uma loja
     */
    public boolean reativarLoja(Long id) {
        Optional<Loja> loja = lojaRepository.findById(id);
        if (loja.isPresent()) {
            loja.get().reativar();
            lojaRepository.save(loja.get());
            return true;
        }
        return false;
    }
    
    /**
     * Remove uma loja
     */
    public boolean removerLoja(Long id) {
        Optional<Loja> loja = lojaRepository.findById(id);
        if (loja.isPresent()) {
            lojaRepository.delete(id);
            return true;
        }
        return false;
    }
    
    /**
     * Estatísticas das lojas
     */
    public EstatisticasResponse getEstatisticas() {
        long totalLojas = lojaRepository.count();
        long lojasAtivas = lojaRepository.countByStatus(StatusLoja.ATIVA);
        long lojasInativas = lojaRepository.countByStatus(StatusLoja.INATIVA);
        long lojasBloqueadas = lojaRepository.countByStatus(StatusLoja.BLOQUEADA);
        
        return new EstatisticasResponse(
            totalLojas, lojasAtivas, lojasInativas, lojasBloqueadas
        );
    }
    
    /**
     * DTO para estatísticas de lojas
     */
    public record EstatisticasResponse(
        long totalLojas,
        long lojasAtivas,
        long lojasInativas,
        long lojasBloqueadas
    ) {
        public double getPercentualAtivas() {
            return totalLojas > 0 ? (lojasAtivas * 100.0 / totalLojas) : 0.0;
        }
        
        public double getPercentualInativas() {
            return totalLojas > 0 ? (lojasInativas * 100.0 / totalLojas) : 0.0;
        }
    }
}
