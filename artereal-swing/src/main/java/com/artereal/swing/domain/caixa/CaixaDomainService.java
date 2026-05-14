package com.artereal.swing.domain.caixa;

import com.artereal.swing.domain.loja.Loja;
import com.artereal.swing.domain.loja.LojaRepository;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Domain Service para operações complexas de Caixa
 * 
 * Este service encapsula lógica de negócio que envolve
 * múltiplas entidades ou regras complexas.
 */
public class CaixaDomainService {
    
    private final CaixaRepository caixaRepository;
    private final LojaRepository lojaRepository;
    
    public CaixaDomainService(CaixaRepository caixaRepository, LojaRepository lojaRepository) {
        this.caixaRepository = Objects.requireNonNull(caixaRepository, "CaixaRepository é obrigatório");
        this.lojaRepository = Objects.requireNonNull(lojaRepository, "LojaRepository é obrigatório");
    }
    
    /**
     * Fecha um caixa com validações adicionais
     */
    public Caixa fecharCaixa(Long caixaId) {
        Caixa caixa = caixaRepository.findById(caixaId)
            .orElseThrow(() -> new CaixaException("Caixa não encontrado"));
        
        if (!caixa.isAberto()) {
            throw new CaixaException("Caixa já está fechado");
        }
        
        // Validar se há movimentações pendentes
        if (caixa.getSaldo().compareTo(BigDecimal.ZERO) > 0) {
            // Em uma implementação real, verificaríamos movimentações
            // Por enquanto, apenas fechamos
        }
        
        caixa.fechar();
        caixaRepository.save(caixa);
        
        return caixa;
    }
    
    /**
     * Verifica se uma loja pode abrir um novo caixa
     */
    public boolean podeAbrirCaixa(Long lojaId) {
        Loja loja = lojaRepository.findById(lojaId)
            .orElseThrow(() -> new CaixaException("Loja não encontrada"));
        
        if (!loja.isAtiva()) {
            return false;
        }
        
        // Verificar se já há caixa aberto
        var caixasAbertos = caixaRepository.findAbertosByLojaId(lojaId);
        return caixasAbertos.isEmpty();
    }
    
    /**
     * Calcula o saldo total de caixas abertos de uma loja
     */
    public BigDecimal calcularSaldoTotalCaixasAbertos(Long lojaId) {
        var caixasAbertos = caixaRepository.findAbertosByLojaId(lojaId);
        
        return caixasAbertos.stream()
            .map(Caixa::getSaldo)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * Obtém o caixa aberto de uma loja
     */
    public Caixa getCaixaAberto(Long lojaId) {
        var caixasAbertos = caixaRepository.findAbertosByLojaId(lojaId);
        
        if (caixasAbertos.isEmpty()) {
            throw new CaixaException("Não há caixa aberto nesta loja");
        }
        
        if (caixasAbertos.size() > 1) {
            throw new CaixaException("Há múltiplos caixas abertos (erro de consistência)");
        }
        
        return caixasAbertos.get(0);
    }
}
