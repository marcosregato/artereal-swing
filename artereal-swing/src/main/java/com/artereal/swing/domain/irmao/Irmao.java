package com.artereal.swing.domain.irmao;

import com.artereal.swing.domain.loja.Loja;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Entidade Irmão - parte do bounded context de Lojas
 * 
 * Esta entidade representa um irmão da ArteReal com suas informações
 * básicas e relacionamentos com lojas e caixas.
 */
public class Irmao {
    
    private Long id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private StatusIrmao status;
    private Long lojaId;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    
    // Construtor privado para forçar uso de factory methods
    private Irmao(Long id, String nome, String cpf, String telefone, String email) {
        this.id = Objects.requireNonNull(id, "ID do irmão é obrigatório");
        this.nome = Objects.requireNonNull(nome, "Nome do irmão é obrigatório");
        this.cpf = Objects.requireNonNull(cpf, "CPF do irmão é obrigatório");
        this.telefone = telefone;
        this.email = email;
        this.status = StatusIrmao.ATIVO;
        this.lojaId = null;
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
        
        validate();
    }
    
    /**
     * Factory method para criar um novo irmão
     */
    public static Irmao criar(String nome, String cpf, String telefone, String email) {
        // Gerar ID sequencial simples para demonstração
        Long novoId = System.currentTimeMillis();
        return new Irmao(novoId, nome, cpf, telefone, email);
    }
    
    /**
     * Associa o irmão a uma loja
     */
    public void associarLoja(Loja loja) {
        Objects.requireNonNull(loja, "Loja é obrigatória");
        
        if (this.lojaId != null && !this.lojaId.equals(loja.getId())) {
            throw new IrmaoException("Irmão já está associado a outra loja");
        }
        
        this.lojaId = loja.getId();
        this.dataAtualizacao = LocalDateTime.now();
    }
    
    /**
     * Desassocia o irmão da loja atual
     */
    public void desassociarLoja() {
        this.lojaId = null;
        this.dataAtualizacao = LocalDateTime.now();
    }
    
    /**
     * Inativa o irmão
     */
    public void inativar() {
        if (this.status == StatusIrmao.INATIVO) {
            throw new IrmaoException("Irmão já está inativo");
        }
        
        this.status = StatusIrmao.INATIVO;
        this.dataAtualizacao = LocalDateTime.now();
    }
    
    /**
     * Reativa o irmão
     */
    public void reativar() {
        if (this.status == StatusIrmao.ATIVO) {
            throw new IrmaoException("Irmão já está ativo");
        }
        
        this.status = StatusIrmao.ATIVO;
        this.dataAtualizacao = LocalDateTime.now();
    }
    
    /**
     * Validações de negócio
     */
    private void validate() {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IrmaoException("Nome do irmão é obrigatório");
        }
        if (nome.length() > 100) {
            throw new IrmaoException("Nome do irmão não pode exceder 100 caracteres");
        }
        if (cpf == null || !isValidCpf(cpf)) {
            throw new IrmaoException("CPF do irmão é inválido");
        }
        if (email != null && !isValidEmail(email)) {
            throw new IrmaoException("Email do irmão é inválido");
        }
    }
    
    /**
     * Validação simples de CPF
     */
    private boolean isValidCpf(String cpf) {
        // Remover caracteres não numéricos
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        return cpfLimpo.length() == 11;
    }
    
    /**
     * Validação simples de email
     */
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
    
    /**
     * Verifica se o irmão está ativo
     */
    public boolean isAtivo() {
        return status == StatusIrmao.ATIVO;
    }
    
    /**
     * Verifica se o irmão está associado a alguma loja
     */
    public boolean isAssociadoLoja() {
        return lojaId != null;
    }
    
    // Getters
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getTelefone() { return telefone; }
    public String getEmail() { return email; }
    public StatusIrmao getStatus() { return status; }
    public Long getLojaId() { return lojaId; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    
    @Override
    public String toString() {
        return String.format("Irmao{id=%d, nome='%s', cpf='%s', status=%s}", 
            id, nome, cpf, status);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Irmao)) return false;
        Irmao outro = (Irmao) o;
        return Objects.equals(id, outro.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
