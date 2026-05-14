package com.artereal.swing.application.loja;

import com.artereal.swing.domain.loja.Loja;
import com.artereal.swing.domain.loja.LojaRepository;
import java.util.List;
import java.util.Objects;

/**
 * Use Case para listar lojas
 */
public class ListarLojasUseCase {
    
    private final LojaRepository lojaRepository;
    
    public ListarLojasUseCase(LojaRepository lojaRepository) {
        this.lojaRepository = Objects.requireNonNull(lojaRepository, "LojaRepository é obrigatório");
    }
    
    /**
     * Lista todas as lojas
     */
    public List<LojaResponse> execute() {
        List<Loja> lojas = lojaRepository.findAll();
        return lojas.stream()
            .map(LojaMapper::toResponse)
            .toList();
    }
    
    /**
     * Lista lojas por status
     */
    public List<LojaResponse> executeByStatus(com.artereal.swing.domain.loja.StatusLoja status) {
        List<Loja> lojas = lojaRepository.findByStatus(status);
        return lojas.stream()
            .map(LojaMapper::toResponse)
            .toList();
    }
    
    /**
     * Lista lojas por cidade
     */
    public List<LojaResponse> executeByCidade(String cidade) {
        List<Loja> lojas = lojaRepository.findByCidade(cidade);
        return lojas.stream()
            .map(LojaMapper::toResponse)
            .toList();
    }
    
    /**
     * Busca lojas por nome (contém)
     */
    public List<LojaResponse> executeByNome(String nome) {
        List<Loja> lojas = lojaRepository.findByNomeContaining(nome);
        return lojas.stream()
            .map(LojaMapper::toResponse)
            .toList();
    }
}
