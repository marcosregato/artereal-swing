package com.artereal.swing.model;

import java.time.LocalDateTime;

/**
 * Modelo de dados para Foto
 */
public class Foto extends SimpleModel {
    
    private Long id;
    private String titulo;
    private String descricao;
    private String caminhoArquivo;
    private String nomeArquivo;
    private String categoria;
    private String evento;
    private LocalDateTime dataFoto;
    private LocalDateTime dataUpload;
    private String usuarioUpload;
    private String tags;
    private Double tamanhoArquivo;
    private String formatoArquivo;
    private Boolean ativo;
    private String created_at;
    private String updated_at;
    
    public Foto() {
        this.ativo = true;
        this.dataUpload = LocalDateTime.now();
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public String getCaminhoArquivo() { return caminhoArquivo; }
    public void setCaminhoArquivo(String caminhoArquivo) { this.caminhoArquivo = caminhoArquivo; }
    
    public String getNomeArquivo() { return nomeArquivo; }
    public void setNomeArquivo(String nomeArquivo) { this.nomeArquivo = nomeArquivo; }
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    
    public String getEvento() { return evento; }
    public void setEvento(String evento) { this.evento = evento; }
    
    public LocalDateTime getDataFoto() { return dataFoto; }
    public void setDataFoto(LocalDateTime dataFoto) { this.dataFoto = dataFoto; }
    
    public LocalDateTime getDataUpload() { return dataUpload; }
    public void setDataUpload(LocalDateTime dataUpload) { this.dataUpload = dataUpload; }
    
    public String getUsuarioUpload() { return usuarioUpload; }
    public void setUsuarioUpload(String usuarioUpload) { this.usuarioUpload = usuarioUpload; }
    
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    
    public Double getTamanhoArquivo() { return tamanhoArquivo; }
    public void setTamanhoArquivo(Double tamanhoArquivo) { this.tamanhoArquivo = tamanhoArquivo; }
    
    public String getFormatoArquivo() { return formatoArquivo; }
    public void setFormatoArquivo(String formatoArquivo) { this.formatoArquivo = formatoArquivo; }
    
    public Boolean isAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    
    public String getCreatedAt() { return created_at; }
    public void setCreatedAt(String created_at) { this.created_at = created_at; }
    
    public String getUpdatedAt() { return updated_at; }
    public void setUpdatedAt(String updated_at) { this.updated_at = updated_at; }
    
    // Métodos utilitários
    public String getCategoriaFormatada() {
        if (categoria == null) return "Outra";
        return categoria.replace("_", " ");
    }
    
    public String getTagsFormatadas() {
        if (tags == null || tags.trim().isEmpty()) return "Nenhuma";
        return tags.replace(",", ", ");
    }
    
    public String getTamanhoFormatado() {
        if (tamanhoArquivo == null) return "N/A";
        
        if (tamanhoArquivo < 1024) {
            return String.format("%.0f B", tamanhoArquivo);
        } else if (tamanhoArquivo < 1024 * 1024) {
            return String.format("%.1f KB", tamanhoArquivo / 1024);
        } else {
            return String.format("%.1f MB", tamanhoArquivo / (1024 * 1024));
        }
    }
}
