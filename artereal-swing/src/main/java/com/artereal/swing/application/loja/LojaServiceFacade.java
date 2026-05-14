package com.artereal.swing.application.loja;

import com.artereal.swing.domain.loja.Loja;
import com.artereal.swing.domain.loja.LojaRepository;
import com.artereal.swing.domain.loja.StatusLoja;
import java.util.List;
import java.util.Optional;

/**
 * Facade para coordenar os use cases de Loja
 * 
 * Esta classe simplifica a interação da UI com a camada de application,
 * coordenando os diferentes use cases necessários para as operações CRUD.
 */
public class LojaServiceFacade {
    
    private final CriarLojaUseCase criarLojaUseCase;
    private final AtualizarLojaUseCase atualizarLojaUseCase;
    private final DeletarLojaUseCase deletarLojaUseCase;
    private final ListarLojasUseCase listarLojasUseCase;
    private final LojaRepository lojaRepository;
    
    public LojaServiceFacade(LojaRepository lojaRepository) {
        this.lojaRepository = lojaRepository;
        this.criarLojaUseCase = new CriarLojaUseCase(lojaRepository);
        this.atualizarLojaUseCase = new AtualizarLojaUseCase(lojaRepository);
        this.deletarLojaUseCase = new DeletarLojaUseCase(lojaRepository);
        this.listarLojasUseCase = new ListarLojasUseCase(lojaRepository);
    }
    
    /**
     * Cria uma nova loja
     */
    public LojaResponse criar(String nome, String cnpj, String endereco, String cidade, String estado) {
        CriarLojaRequest request = new CriarLojaRequest(nome, cnpj, endereco, cidade, estado);
        return criarLojaUseCase.execute(request);
    }
    
    /**
     * Atualiza uma loja existente
     */
    public LojaResponse atualizar(Long id, String nome, String cnpj, String endereco, String cidade, String estado) {
        AtualizarLojaRequest request = new AtualizarLojaRequest(nome, cnpj, endereco, cidade, estado);
        return atualizarLojaUseCase.execute(id, request);
    }
    
    /**
     * Deleta uma loja
     */
    public void deletar(Long id) {
        deletarLojaUseCase.execute(id);
    }
    
    /**
     * Lista todas as lojas
     */
    public List<LojaResponse> listarTodas() {
        return listarLojasUseCase.execute();
    }
    
    /**
     * Busca lojas por nome
     */
    public List<LojaResponse> buscarPorNome(String nome) {
        return listarLojasUseCase.executeByNome(nome);
    }
    
    /**
     * Busca uma loja por ID (retorna entidade de domínio para uso interno)
     */
    public Optional<Loja> buscarPorId(Long id) {
        return lojaRepository.findById(id);
    }
    
    /**
     * Lista lojas por status
     */
    public List<LojaResponse> listarPorStatus(StatusLoja status) {
        return listarLojasUseCase.executeByStatus(status);
    }
    
    /**
     * Lista lojas por cidade
     */
    public List<LojaResponse> listarPorCidade(String cidade) {
        return listarLojasUseCase.executeByCidade(cidade);
    }
    
    /**
     * Conta total de lojas
     */
    public long contar() {
        return lojaRepository.count();
    }
}
