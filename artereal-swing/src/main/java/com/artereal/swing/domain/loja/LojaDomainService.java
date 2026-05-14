package com.artereal.swing.domain.loja;

import com.artereal.swing.domain.irmao.Irmao;
import com.artereal.swing.domain.irmao.IrmaoRepository;
import java.util.Objects;

/**
 * Domain Service para operações complexas de Loja
 * 
 * Este service encapsula lógica de negócio que envolve
 * múltiplas entidades ou regras complexas.
 */
public class LojaDomainService {
    
    private final LojaRepository lojaRepository;
    private final IrmaoRepository irmaoRepository;
    
    private static final int MAX_IRMAOS_POR_LOJA = 50;
    
    public LojaDomainService(LojaRepository lojaRepository, IrmaoRepository irmaoRepository) {
        this.lojaRepository = Objects.requireNonNull(lojaRepository, "LojaRepository é obrigatório");
        this.irmaoRepository = Objects.requireNonNull(irmaoRepository, "IrmaoRepository é obrigatório");
    }
    
    /**
     * Transfere um irmão de uma loja para outra
     */
    public Loja transferirIrmao(Long lojaOrigemId, Long lojaDestinoId, Long irmaoId) {
        // Buscar lojas
        Loja lojaOrigem = lojaRepository.findById(lojaOrigemId)
            .orElseThrow(() -> new LojaException("Loja de origem não encontrada"));
        
        Loja lojaDestino = lojaRepository.findById(lojaDestinoId)
            .orElseThrow(() -> new LojaException("Loja de destino não encontrada"));
        
        // Buscar irmão
        Irmao irmao = irmaoRepository.findById(irmaoId)
            .orElseThrow(() -> new LojaException("Irmão não encontrado"));
        
        // Validar transferência
        validarTransferencia(lojaOrigem, lojaDestino, irmao);
        
        // Executar transferência
        lojaOrigem.removerIrmao(irmaoId);
        lojaDestino.adicionarIrmao(irmao);
        
        // Salvar alterações
        lojaRepository.save(lojaOrigem);
        lojaRepository.save(lojaDestino);
        
        return lojaDestino;
    }
    
    /**
     * Valida as regras para transferência de irmão
     */
    private void validarTransferencia(Loja origem, Loja destino, Irmao irmao) {
        if (!origem.isAtiva()) {
            throw new LojaException("Loja de origem está inativa");
        }
        
        if (!destino.isAtiva()) {
            throw new LojaException("Loja de destino está inativa");
        }
        
        if (destino.getIrmaos().size() >= MAX_IRMAOS_POR_LOJA) {
            throw new LojaException("Loja de destino atingiu o limite máximo de irmãos");
        }
        
        if (!origem.getIrmaos().contains(irmao)) {
            throw new LojaException("Irmão não pertence à loja de origem");
        }
    }
    
    /**
     * Verifica se uma loja pode ser inativada
     */
    public boolean podeInativarLoja(Long lojaId) {
        Loja loja = lojaRepository.findById(lojaId)
            .orElseThrow(() -> new LojaException("Loja não encontrada"));
        
        // Não pode inativar se tiver caixas abertos
        if (loja.temCaixasAbertos()) {
            return false;
        }
        
        // Não pode inativar se tiver irmãos ativos
        if (loja.getQuantidadeIrmaosAtivos() > 0) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Calcula estatísticas de uma loja
     */
    public LojaEstatisticas calcularEstatisticas(Long lojaId) {
        Loja loja = lojaRepository.findById(lojaId)
            .orElseThrow(() -> new LojaException("Loja não encontrada"));
        
        return new LojaEstatisticas(
            loja.getIrmaos().size(),
            loja.getQuantidadeIrmaosAtivos(),
            loja.getCaixas().size(),
            loja.getValorTotalCaixasAbertos(),
            loja.isAtiva()
        );
    }
    
    /**
     * Record para estatísticas de loja
     */
    public record LojaEstatisticas(
        int totalIrmaos,
        int irmaosAtivos,
        int totalCaixas,
        java.math.BigDecimal valorTotalCaixasAbertos,
        boolean lojaAtiva
    ) {}
}
