package com.artereal.swing.application.loja;

import com.artereal.swing.domain.loja.Loja;
import com.artereal.swing.domain.loja.LojaRepository;
import com.artereal.swing.domain.loja.LojaException;
import java.util.Objects;

/**
 * Use Case para criar uma nova loja
 * 
 * Esta classe orquestra a lógica de negócio para criação de lojas,
 * validando regras e coordenando as entidades de domínio.
 */
public class CriarLojaUseCase {
    
    private final LojaRepository lojaRepository;
    
    public CriarLojaUseCase(LojaRepository lojaRepository) {
        this.lojaRepository = Objects.requireNonNull(lojaRepository, "LojaRepository é obrigatório");
    }
    
    /**
     * Executa o caso de uso para criar uma nova loja
     * 
     * @param request DTO com dados da nova loja
     * @return LojaResponse com dados da loja criada
     * @throws LojaException em caso de violação de regras de negócio
     */
    public LojaResponse execute(CriarLojaRequest request) {
        // Validações básicas do request
        validateRequest(request);
        
        // Verificar duplicidade de CNPJ
        if (lojaRepository.existsByCnpj(request.cnpj())) {
            throw new LojaException("Já existe uma loja com o CNPJ informado");
        }
        
        // Criar entidade de domínio
        Loja loja = Loja.criar(
            request.nome(),
            request.cnpj(),
            request.endereco(),
            request.cidade(),
            request.estado()
        );
        
        // Salvar no repositório
        Loja lojaSalva = lojaRepository.save(loja);
        
        // Retornar response
        return LojaMapper.toResponse(lojaSalva);
    }
    
    /**
     * Validações básicas do request
     */
    private void validateRequest(CriarLojaRequest request) {
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
        
        // Validação de tamanho dos campos
        if (request.nome().length() > 200) {
            throw new LojaException("Nome da loja não pode exceder 200 caracteres");
        }
        
        if (request.endereco().length() > 500) {
            throw new LojaException("Endereço não pode exceder 500 caracteres");
        }
    }
}
