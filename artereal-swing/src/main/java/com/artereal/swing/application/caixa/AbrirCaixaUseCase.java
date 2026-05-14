package com.artereal.swing.application.caixa;

import com.artereal.swing.domain.caixa.Caixa;
import com.artereal.swing.domain.caixa.CaixaException;
import com.artereal.swing.domain.caixa.CaixaRepository;
import com.artereal.swing.domain.loja.Loja;
import com.artereal.swing.domain.loja.LojaRepository;
import java.util.Objects;

/**
 * Use Case para abrir um novo caixa
 */
public class AbrirCaixaUseCase {
    
    private final CaixaRepository caixaRepository;
    private final LojaRepository lojaRepository;
    
    public AbrirCaixaUseCase(CaixaRepository caixaRepository, LojaRepository lojaRepository) {
        this.caixaRepository = Objects.requireNonNull(caixaRepository, "CaixaRepository é obrigatório");
        this.lojaRepository = Objects.requireNonNull(lojaRepository, "LojaRepository é obrigatório");
    }
    
    public CaixaResponse execute(AbrirCaixaRequest request) {
        validateRequest(request);
        
        // Verificar se a loja existe
        Loja loja = lojaRepository.findById(request.lojaId())
            .orElseThrow(() -> new CaixaException("Loja não encontrada"));
        
        // Verificar se a loja está ativa
        if (!loja.isAtiva()) {
            throw new CaixaException("Não é possível abrir caixa em loja inativa");
        }
        
        // Verificar se já existe caixa aberto na loja
        var caixasAbertos = caixaRepository.findAbertosByLojaId(request.lojaId());
        if (!caixasAbertos.isEmpty()) {
            throw new CaixaException("Já existe um caixa aberto nesta loja");
        }
        
        // Criar entidade de domínio
        Caixa caixa = Caixa.abrir(request.valor(), request.responsavel(), request.lojaId());
        
        // Salvar no repositório
        Caixa caixaSalvo = caixaRepository.save(caixa);
        
        // Retornar response
        return CaixaMapper.toResponse(caixaSalvo);
    }
    
    private void validateRequest(AbrirCaixaRequest request) {
        Objects.requireNonNull(request, "Request é obrigatório");
        
        if (request.lojaId() == null) {
            throw new CaixaException("ID da loja é obrigatório");
        }
        
        if (request.valor() == null || request.valor().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new CaixaException("Valor do caixa deve ser positivo");
        }
        
        if (request.responsavel() == null || request.responsavel().trim().isEmpty()) {
            throw new CaixaException("Responsável pelo caixa é obrigatório");
        }
    }
}
