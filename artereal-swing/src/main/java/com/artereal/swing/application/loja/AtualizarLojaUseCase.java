package com.artereal.swing.application.loja;

import com.artereal.swing.domain.loja.Loja;
import com.artereal.swing.domain.loja.LojaRepository;
import com.artereal.swing.domain.loja.LojaException;
import java.util.Objects;

/**
 * Use Case para atualizar uma loja existente
 */
public class AtualizarLojaUseCase {
    
    private final LojaRepository lojaRepository;
    
    public AtualizarLojaUseCase(LojaRepository lojaRepository) {
        this.lojaRepository = Objects.requireNonNull(lojaRepository, "LojaRepository é obrigatório");
    }
    
    /**
     * Executa o caso de uso para atualizar uma loja
     */
    public LojaResponse execute(Long id, AtualizarLojaRequest request) {
        validateRequest(request);
        
        Loja loja = lojaRepository.findById(id)
            .orElseThrow(() -> new LojaException("Loja não encontrada com ID: " + id));
        
        // Verificar duplicidade de CNPJ (se foi alterado)
        if (!loja.getCnpj().equals(request.cnpj()) && lojaRepository.existsByCnpj(request.cnpj())) {
            throw new LojaException("Já existe uma loja com o CNPJ informado");
        }
        
        // Atualizar entidade
        loja.atualizarDados(
            request.nome(),
            request.cnpj(),
            request.endereco(),
            request.cidade(),
            request.estado()
        );
        
        // Salvar no repositório
        Loja lojaAtualizada = lojaRepository.save(loja);
        
        return LojaMapper.toResponse(lojaAtualizada);
    }
    
    private void validateRequest(AtualizarLojaRequest request) {
        Objects.requireNonNull(request, "Request é obrigatório");
        
        if (request.nome() == null || request.nome().trim().isEmpty()) {
            throw new LojaException("Nome da loja é obrigatório");
        }
        
        if (request.cnpj() == null || request.cnpj().trim().isEmpty()) {
            throw new LojaException("CNPJ da loja é obrigatório");
        }
        
        if (request.endereco() == null || request.endereco().trim().isEmpty()) {
            throw new LojaException("Endereço da loja é obrigatório");
        }
        
        if (request.cidade() == null || request.cidade().trim().isEmpty()) {
            throw new LojaException("Cidade da loja é obrigatória");
        }
        
        if (request.estado() == null || request.estado().trim().isEmpty()) {
            throw new LojaException("Estado da loja é obrigatório");
        }
    }
}
