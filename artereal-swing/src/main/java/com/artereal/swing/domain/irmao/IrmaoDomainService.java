package com.artereal.swing.domain.irmao;

import com.artereal.swing.domain.loja.Loja;
import com.artereal.swing.domain.loja.LojaRepository;
import java.util.Objects;

/**
 * Domain Service para operações complexas de Irmão
 * 
 * Este service encapsula lógica de negócio que envolve
 * múltiplas entidades ou regras complexas.
 */
public class IrmaoDomainService {
    
    private final IrmaoRepository irmaoRepository;
    private final LojaRepository lojaRepository;
    
    public IrmaoDomainService(IrmaoRepository irmaoRepository, LojaRepository lojaRepository) {
        this.irmaoRepository = Objects.requireNonNull(irmaoRepository, "IrmaoRepository é obrigatório");
        this.lojaRepository = Objects.requireNonNull(lojaRepository, "LojaRepository é obrigatório");
    }
    
    /**
     * Associa um irmão a uma loja com validações adicionais
     */
    public Irmao associarLoja(Long irmaoId, Long lojaId) {
        Irmao irmao = irmaoRepository.findById(irmaoId)
            .orElseThrow(() -> new IrmaoException("Irmão não encontrado"));
        
        Loja loja = lojaRepository.findById(lojaId)
            .orElseThrow(() -> new IrmaoException("Loja não encontrada"));
        
        // Validar se o irmão já está associado a esta loja
        if (irmao.getLojaId() != null && irmao.getLojaId().equals(lojaId)) {
            throw new IrmaoException("Irmão já está associado a esta loja");
        }
        
        // Validar se o irmão está associado a outra loja
        if (irmao.getLojaId() != null && !irmao.getLojaId().equals(lojaId)) {
            throw new IrmaoException("Irmão já está associado a outra loja");
        }
        
        // Validar se a loja está ativa
        if (!loja.isAtiva()) {
            throw new IrmaoException("Não é possível associar irmão a loja inativa");
        }
        
        // Validar capacidade da loja
        if (loja.getIrmaos().size() >= 50) {
            throw new IrmaoException("Loja atingiu o limite máximo de irmãos");
        }
        
        // Associar
        irmao.associarLoja(loja);
        irmaoRepository.save(irmao);
        
        return irmao;
    }
    
    /**
     * Desassocia um irmão da loja atual
     */
    public Irmao desassociarLoja(Long irmaoId) {
        Irmao irmao = irmaoRepository.findById(irmaoId)
            .orElseThrow(() -> new IrmaoException("Irmão não encontrado"));
        
        if (irmao.getLojaId() == null) {
            throw new IrmaoException("Irmão não está associado a nenhuma loja");
        }
        
        irmao.desassociarLoja();
        irmaoRepository.save(irmao);
        
        return irmao;
    }
    
    /**
     * Inativa um irmão com validações
     */
    public Irmao inativarIrmao(Long irmaoId) {
        Irmao irmao = irmaoRepository.findById(irmaoId)
            .orElseThrow(() -> new IrmaoException("Irmão não encontrado"));
        
        if (!irmao.isAtivo()) {
            throw new IrmaoException("Irmão já está inativo");
        }
        
        // Validar se o irmão tem responsabilidades ativas
        if (irmao.getLojaId() != null) {
            Loja loja = lojaRepository.findById(irmao.getLojaId())
                .orElseThrow(() -> new IrmaoException("Loja não encontrada"));
            
            // Verificar se o irmão é responsável por algum caixa aberto
            if (loja.temCaixasAbertos()) {
                throw new IrmaoException("Irmão responsável por caixas abertos não pode ser inativado");
            }
        }
        
        irmao.inativar();
        irmaoRepository.save(irmao);
        
        return irmao;
    }
}
