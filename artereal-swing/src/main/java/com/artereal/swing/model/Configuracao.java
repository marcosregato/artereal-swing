package com.artereal.swing.model;

import java.time.LocalDateTime;

/**
 * Modelo de dados para Configurações Globais do Sistema
 */
public class Configuracao {
    
    private Long id;
    private String chave;
    private String valor;
    private String descricao;
    private String tipo; // STRING, NUMBER, BOOLEAN, DATE
    private String categoria; // SISTEMA, FINANCEIRO, USUARIO, etc.
    private boolean editavel;
    private boolean visivel;
    private LocalDateTime dataAtualizacao;
    private String usuarioAtualizacao;
    
    public Configuracao() {}
    
    public Configuracao(String chave, String valor, String descricao, String tipo, String categoria) {
        this.chave = chave;
        this.valor = valor;
        this.descricao = descricao;
        this.tipo = tipo;
        this.categoria = categoria;
        this.editavel = true;
        this.visivel = true;
        this.dataAtualizacao = LocalDateTime.now();
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getChave() { return chave; }
    public void setChave(String chave) { this.chave = chave; }
    
    public String getValor() { return valor; }
    public void setValor(String valor) { this.valor = valor; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    
    public boolean isEditavel() { return editavel; }
    public void setEditavel(boolean editavel) { this.editavel = editavel; }
    
    public boolean isVisivel() { return visivel; }
    public void setVisivel(boolean visivel) { this.visivel = visivel; }
    
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
    
    public String getUsuarioAtualizacao() { return usuarioAtualizacao; }
    public void setUsuarioAtualizacao(String usuarioAtualizacao) { this.usuarioAtualizacao = usuarioAtualizacao; }
    
    // Métodos utilitários
    public boolean getValorAsBoolean() {
        return "true".equalsIgnoreCase(valor) || "1".equals(valor) || "sim".equalsIgnoreCase(valor);
    }
    
    public int getValorAsInt() {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    public double getValorAsDouble() {
        try {
            return Double.parseDouble(valor);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    public void setValorAsBoolean(boolean valor) {
        this.valor = valor ? "true" : "false";
    }
    
    public void setValorAsInt(int valor) {
        this.valor = String.valueOf(valor);
    }
    
    public void setValorAsDouble(double valor) {
        this.valor = String.valueOf(valor);
    }
    
    @Override
    public String toString() {
        return descricao + " (" + chave + "): " + valor;
    }
}
