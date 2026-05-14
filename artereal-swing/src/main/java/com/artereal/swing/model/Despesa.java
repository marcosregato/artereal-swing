package com.artereal.swing.model;

import java.util.Date;

/**
 * Modelo de dados para Despesas
 */
public class Despesa extends SimpleModel {
    private Long id;
    private String descricao;
    private double valor;
    private Date data;
    private String categoria;
    private String fornecedor;
    private String numeroDocumento;
    
    public Despesa() {
        this.data = new Date();
        this.categoria = "Geral";
        this.fornecedor = "Não informado";
    }
    
    public Despesa(String descricao, double valor, Date data, String categoria, String fornecedor, String numeroDocumento) {
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
        this.categoria = categoria;
        this.fornecedor = fornecedor;
        this.numeroDocumento = numeroDocumento;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public double getValor() {
        return valor;
    }
    
    public void setValor(double valor) {
        this.valor = valor;
    }
    
    public Date getData() {
        return data;
    }
    
    public void setData(Date data) {
        this.data = data;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public String getFornecedor() {
        return fornecedor;
    }
    
    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }
    
    public String getNumeroDocumento() {
        return numeroDocumento;
    }
    
    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }
}
