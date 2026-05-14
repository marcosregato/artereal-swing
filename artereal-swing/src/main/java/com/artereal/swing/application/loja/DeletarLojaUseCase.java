package com.artereal.swing.application.loja;

import com.artereal.swing.domain.loja.Loja;
import com.artereal.swing.domain.loja.LojaRepository;
import com.artereal.swing.domain.loja.LojaException;
import java.util.Objects;

/**
 * Use Case para deletar uma loja
 */
public class DeletarLojaUseCase {
    
    private final LojaRepository lojaRepository;
    
    public DeletarLojaUseCase(LojaRepository lojaRepository) {
        this.lojaRepository = Objects.requireNonNull(lojaRepository, "LojaRepository é obrigatório");
    }
    
    /**
     * Executa o caso de uso para deletar uma loja
     */
    public void execute(Long id) {
        Objects.requireNonNull(id, "ID da loja é obrigatório");
        
        Loja loja = lojaRepository.findById(id)
            .orElseThrow(() -> new LojaException("Loja não encontrada com ID: " + id));
        
        // Verificar se há irmãos associados
        if (!loja.getIrmaos().isEmpty()) {
            throw new LojaException("Não é possível deletar loja com irmãos associados");
        }
        
        // Verificar se há caixas abertos
        if (loja.getCaixas().stream().anyMatch(c -> c.getStatus() == com.artereal.swing.domain.caixa.StatusCaixa.ABERTO)) {
            throw new LojaException("Não é possível deletar loja com caixas abertos");
        }
        
        lojaRepository.delete(id);
    }
}
