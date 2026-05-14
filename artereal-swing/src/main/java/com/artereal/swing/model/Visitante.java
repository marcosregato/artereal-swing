package com.artereal.swing.model;

import java.time.LocalDate;

/**
 * Modelo de dados para Controle de Visitantes
 */
public class Visitante extends SimpleModel {
    
    private Long id;
    private String nome;
    private LocalDate dataVisita;
    private String grauSecreto;
    private String historico;
    private String tipo; // VISITANTE, CONVIDADO, IRMAO_VISITANTE
    private String lojaOrigem;
    private String telefone;
    private String email;
    private String autorizadoPor;
    private boolean autorizado;
    private String observacoes;
    private String numeroCracha;
    private LocalDate dataCadastro;
    private boolean ativo;
    private String createdAt;
    private String updatedAt;
    
    public Visitante() {}
    
    public Visitante(String nome, String tipo, LocalDate dataVisita) {
        this.nome = nome;
        this.tipo = tipo;
        this.dataVisita = dataVisita;
        this.dataCadastro = LocalDate.now();
        this.autorizado = false;
        this.ativo = true;
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public LocalDate getDataVisita() { return dataVisita; }
    public void setDataVisita(LocalDate dataVisita) { this.dataVisita = dataVisita; }
    
    public String getGrauSecreto() { return grauSecreto; }
    public void setGrauSecreto(String grauSecreto) { this.grauSecreto = grauSecreto; }
    
    public String getHistorico() { return historico; }
    public void setHistorico(String historico) { this.historico = historico; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public String getLojaOrigem() { return lojaOrigem; }
    public void setLojaOrigem(String lojaOrigem) { this.lojaOrigem = lojaOrigem; }
    
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getAutorizadoPor() { return autorizadoPor; }
    public void setAutorizadoPor(String autorizadoPor) { this.autorizadoPor = autorizadoPor; }
    
    public boolean isAutorizado() { return autorizado; }
    public void setAutorizado(boolean autorizado) { this.autorizado = autorizado; }
    
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    
    public String getNumeroCracha() { return numeroCracha; }
    public void setNumeroCracha(String numeroCracha) { this.numeroCracha = numeroCracha; }
    
    public LocalDate getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDate dataCadastro) { this.dataCadastro = dataCadastro; }
    
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    
    // Métodos utilitários
    public boolean isVisitanteRegular() {
        return "VISITANTE".equals(tipo);
    }
    
    public boolean isConvidado() {
        return "CONVIDADO".equals(tipo);
    }
    
    public boolean isIrmaoVisitante() {
        return "IRMAO_VISITANTE".equals(tipo);
    }
    
    public boolean isHoje() {
        return dataVisita != null && dataVisita.equals(LocalDate.now());
    }
    
    public boolean necessitaAutorizacao() {
        return !isAutorizado() && (isIrmaoVisitante() || isConvidado());
    }
    
    public void autorizar(String autorizador) {
        this.autorizado = true;
        this.autorizadoPor = autorizador;
    }
    
    public void negarAutorizacao() {
        this.autorizado = false;
        this.autorizadoPor = null;
    }
    
    public String getStatusFormatado() {
        if (!isAtivo()) return "INATIVO";
        if (!isAutorizado() && necessitaAutorizacao()) return "PENDENTE";
        if (isAutorizado()) return "AUTORIZADO";
        return "REGULAR";
    }
}
