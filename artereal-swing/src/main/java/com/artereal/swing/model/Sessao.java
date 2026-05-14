package com.artereal.swing.model;

import java.time.LocalDateTime;

/**
 * Modelo de dados para Sessão Maçônica
 */
public class Sessao extends SimpleModel {
    
    private Long id;
    private String tipo; // MAGNA, BRANCA, ELEICAO, INSTRUCAO, etc.
    private LocalDateTime dataHora;
    private String local;
    private String presidente;
    private String secretario;
    private String tesoureiro;
    private String orador;
    private String tema;
    private String pauta;
    private String observacoes;
    private String status; // PROGRAMADA, REALIZADA, CANCELADA, ADIADA
    private int quantidadePresentes;
    private int quantidadeVisitantes;
    private String createdAt;
    private String updatedAt;
    
    public Sessao() {}
    
    public Sessao(String tipo, LocalDateTime dataHora) {
        this.tipo = tipo;
        this.dataHora = dataHora;
        this.status = "PROGRAMADA";
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    
    public String getLocal() { return local; }
    public void setLocal(String local) { this.local = local; }
    
    public String getPresidente() { return presidente; }
    public void setPresidente(String presidente) { this.presidente = presidente; }
    
    public String getSecretario() { return secretario; }
    public void setSecretario(String secretario) { this.secretario = secretario; }
    
    public String getTesoureiro() { return tesoureiro; }
    public void setTesoureiro(String tesoureiro) { this.tesoureiro = tesoureiro; }
    
    public String getOrador() { return orador; }
    public void setOrador(String orador) { this.orador = orador; }
    
    public String getTema() { return tema; }
    public void setTema(String tema) { this.tema = tema; }
    
    public String getPauta() { return pauta; }
    public void setPauta(String pauta) { this.pauta = pauta; }
    
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public int getQuantidadePresentes() { return quantidadePresentes; }
    public void setQuantidadePresentes(int quantidadePresentes) { this.quantidadePresentes = quantidadePresentes; }
    
    public int getQuantidadeVisitantes() { return quantidadeVisitantes; }
    public void setQuantidadeVisitantes(int quantidadeVisitantes) { this.quantidadeVisitantes = quantidadeVisitantes; }
    
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
