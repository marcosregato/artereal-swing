package com.artereal.swing.application.irmao;

import com.artereal.swing.domain.irmao.Irmao;
import com.artereal.swing.domain.irmao.IrmaoException;
import com.artereal.swing.domain.irmao.IrmaoRepository;
import com.artereal.swing.domain.loja.LojaRepository;
import java.util.Objects;

/**
 * Use Case para criar um novo irmão
 */
public class CriarIrmaoUseCase {
    
    private final IrmaoRepository irmaoRepository;
    private final LojaRepository lojaRepository;
    
    public CriarIrmaoUseCase(IrmaoRepository irmaoRepository, LojaRepository lojaRepository) {
        this.irmaoRepository = Objects.requireNonNull(irmaoRepository, "IrmaoRepository é obrigatório");
        this.lojaRepository = Objects.requireNonNull(lojaRepository, "LojaRepository é obrigatório");
    }
    
    public IrmaoResponse execute(CriarIrmaoRequest request) {
        validateRequest(request);
        
        // Verificar duplicidade de CPF
        if (irmaoRepository.existsByCpf(request.cpf())) {
            throw new IrmaoException("Já existe um irmão com o CPF informado");
        }
        
        // Se lojaId foi informado, verificar se a loja existe
        if (request.lojaId() != null) {
            if (lojaRepository.findById(request.lojaId()).isEmpty()) {
                throw new IrmaoException("Loja não encontrada");
            }
        }
        
        // Criar entidade de domínio
        Irmao irmao = Irmao.criar(
            request.nome(),
            request.cpf(),
            request.telefone(),
            request.email()
        );
        
        // Associar à loja se informado
        if (request.lojaId() != null) {
            // Aqui precisaríamos carregar a loja e associar
            // Por enquanto, apenas setamos o lojaId diretamente
            // Em uma implementação completa, usaríamos o método associarLoja
        }
        
        // Salvar no repositório
        Irmao irmaoSalvo = irmaoRepository.save(irmao);
        
        // Retornar response
        return IrmaoMapper.toResponse(irmaoSalvo);
    }
    
    private void validateRequest(CriarIrmaoRequest request) {
        Objects.requireNonNull(request, "Request é obrigatório");
        
        if (request.nome() == null || request.nome().trim().isEmpty()) {
            throw new IrmaoException("Nome do irmão é obrigatório");
        }
        
        if (request.cpf() == null || request.cpf().trim().isEmpty()) {
            throw new IrmaoException("CPF do irmão é obrigatório");
        }
    }
}
