package com.artereal.swing.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Modelo de dados para Movimentações do Caixa
 */
public class Caixa extends SimpleModel {
    
    private Long id;
    private String tipo; // RECEITA, DESPESA
    private String categoria; // ANUIDADE, DOACAO, MATERIAL, ALUGUEL, etc.
    private String descricao;
    private BigDecimal valor;
    private LocalDateTime dataMovimentacao;
    private String responsavel;
    private String formaPagamento; // DINHEIRO, CHEQUE, TRANSFERENCIA, PIX
    private String numeroDocumento; // número do cheque, boleto, etc.
    private String status; // PAGO, PENDENTE, CANCELADO
    private String observacoes;
    private String createdAt;
    private String updatedAt;
    
    public Caixa() {}
    
    public Caixa(String tipo, BigDecimal valor, String descricao) {
        this.tipo = tipo;
        this.valor = valor;
        this.descricao = descricao;
        this.dataMovimentacao = LocalDateTime.now();
        this.status = "PENDENTE";
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    
    public LocalDateTime getDataMovimentacao() { return dataMovimentacao; }
    public void setDataMovimentacao(LocalDateTime dataMovimentacao) { this.dataMovimentacao = dataMovimentacao; }
    
    public String getResponsavel() { return responsavel; }
    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }
    
    public String getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }
    
    public String getNumeroDocumento() { return numeroDocumento; }
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
