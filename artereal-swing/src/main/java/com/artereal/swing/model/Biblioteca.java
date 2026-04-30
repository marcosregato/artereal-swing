package com.artereal.swing.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Modelo de dados para Biblioteca (Livros e Empréstimos)
 */
public class Biblioteca {
    
    private Long id;
    private String tipo; // LIVRO, EMPRESTIMO
    private String titulo;
    private String autor;
    private String isbn;
    private String editora;
    private String anoPublicacao;
    private String categoria; // RITUAL, HISTORIA, FILOSOFIA, etc.
    private String localizacao; // Estante, Prateleira
    private String status; // DISPONIVEL, EMPRESTADO, EM_MANUTENCAO, BAIXADO
    
    // Campos para empréstimo
    private String nomeLeitor;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucaoReal;
    private String responsavelEmprestimo;
    private BigDecimal multa;
    private String observacoes;
    
    private String createdAt;
    private String updatedAt;
    
    public Biblioteca() {}
    
    public Biblioteca(String tipo, String titulo) {
        this.tipo = tipo;
        this.titulo = titulo;
        if ("LIVRO".equals(tipo)) {
            this.status = "DISPONIVEL";
        } else if ("EMPRESTIMO".equals(tipo)) {
            this.status = "EMPRESTADO";
        }
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    
    public String getEditora() { return editora; }
    public void setEditora(String editora) { this.editora = editora; }
    
    public String getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(String anoPublicacao) { this.anoPublicacao = anoPublicacao; }
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    
    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getNomeLeitor() { return nomeLeitor; }
    public void setNomeLeitor(String nomeLeitor) { this.nomeLeitor = nomeLeitor; }
    
    public LocalDate getDataEmprestimo() { return dataEmprestimo; }
    public void setDataEmprestimo(LocalDate dataEmprestimo) { this.dataEmprestimo = dataEmprestimo; }
    
    public LocalDate getDataDevolucaoPrevista() { return dataDevolucaoPrevista; }
    public void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) { this.dataDevolucaoPrevista = dataDevolucaoPrevista; }
    
    public LocalDate getDataDevolucaoReal() { return dataDevolucaoReal; }
    public void setDataDevolucaoReal(LocalDate dataDevolucaoReal) { this.dataDevolucaoReal = dataDevolucaoReal; }
    
    public String getResponsavelEmprestimo() { return responsavelEmprestimo; }
    public void setResponsavelEmprestimo(String responsavelEmprestimo) { this.responsavelEmprestimo = responsavelEmprestimo; }
    
    public BigDecimal getMulta() { return multa; }
    public void setMulta(BigDecimal multa) { this.multa = multa; }
    
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    
    @Override
    public String toString() {
        if ("LIVRO".equals(tipo)) {
            return titulo + " - " + autor + " (" + status + ")";
        } else {
            return "Empréstimo: " + nomeLeitor + " - " + titulo;
        }
    }
}
