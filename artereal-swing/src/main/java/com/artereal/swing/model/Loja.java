package com.artereal.swing.model;

import java.time.LocalDate;

/**
 * Modelo de dados para Loja Maçônica
 */
public class Loja {
    
    private Long id;
    private String nome;
    private String numero;
    private String endereco;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String telefone;
    private String email;
    private String presidente;
    private String secretario;
    private String tesoureiro;
    private String dataFundacao;
    private String rito;
    private String potencia;
    private String observacoes;
    private String status; // ATIVA, INATIVA, SUSPENSA
    private String createdAt;
    private String updatedAt;
    
    public Loja() {}
    
    public Loja(String nome, String numero) {
        this.nome = nome;
        this.numero = numero;
        this.status = "ATIVA";
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    
    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }
    
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPresidente() { return presidente; }
    public void setPresidente(String presidente) { this.presidente = presidente; }
    
    public String getSecretario() { return secretario; }
    public void setSecretario(String secretario) { this.secretario = secretario; }
    
    public String getTesoureiro() { return tesoureiro; }
    public void setTesoureiro(String tesoureiro) { this.tesoureiro = tesoureiro; }
    
    public String getDataFundacao() { return dataFundacao; }
    public void setDataFundacao(String dataFundacao) { this.dataFundacao = dataFundacao; }
    
    public String getRito() { return rito; }
    public void setRito(String rito) { this.rito = rito; }
    
    public String getPotencia() { return potencia; }
    public void setPotencia(String potencia) { this.potencia = potencia; }
    
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    
    @Override
    public String toString() {
        return nome + " - Loja " + numero + " (" + status + ")";
    }
}
