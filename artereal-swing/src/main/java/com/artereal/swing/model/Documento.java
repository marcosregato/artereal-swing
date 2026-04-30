package com.artereal.swing.model;

import java.time.LocalDateTime;

/**
 * Modelo de dados para Gestão Documental
 */
public class Documento {
    
    private Long id;
    private Long codigoIrmao;
    private String nomeArquivo;
    private String caminhoArquivo;
    private String tipo; // IDENTIDADE, DIPLOMA, CERTIFICADO, OUTRO
    private String descricao;
    private LocalDateTime dataUpload;
    private LocalDateTime dataExpiracao;
    private String status; // ATIVO, EXPIRADO, CANCELADO
    private String assinaturaDigital;
    private String hashArquivo;
    private double tamanhoArquivo;
    private String formatoArquivo;
    private String usuarioUpload;
    private boolean ativo;
    private String createdAt;
    private String updatedAt;
    
    public Documento() {}
    
    public Documento(Long codigoIrmao, String nomeArquivo, String tipo) {
        this.codigoIrmao = codigoIrmao;
        this.nomeArquivo = nomeArquivo;
        this.tipo = tipo;
        this.dataUpload = LocalDateTime.now();
        this.status = "ATIVO";
        this.ativo = true;
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getCodigoIrmao() { return codigoIrmao; }
    public void setCodigoIrmao(Long codigoIrmao) { this.codigoIrmao = codigoIrmao; }
    
    public String getNomeArquivo() { return nomeArquivo; }
    public void setNomeArquivo(String nomeArquivo) { this.nomeArquivo = nomeArquivo; }
    
    public String getCaminhoArquivo() { return caminhoArquivo; }
    public void setCaminhoArquivo(String caminhoArquivo) { this.caminhoArquivo = caminhoArquivo; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public LocalDateTime getDataUpload() { return dataUpload; }
    public void setDataUpload(LocalDateTime dataUpload) { this.dataUpload = dataUpload; }
    
    public LocalDateTime getDataExpiracao() { return dataExpiracao; }
    public void setDataExpiracao(LocalDateTime dataExpiracao) { this.dataExpiracao = dataExpiracao; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getAssinaturaDigital() { return assinaturaDigital; }
    public void setAssinaturaDigital(String assinaturaDigital) { this.assinaturaDigital = assinaturaDigital; }
    
    public String getHashArquivo() { return hashArquivo; }
    public void setHashArquivo(String hashArquivo) { this.hashArquivo = hashArquivo; }
    
    public double getTamanhoArquivo() { return tamanhoArquivo; }
    public void setTamanhoArquivo(double tamanhoArquivo) { this.tamanhoArquivo = tamanhoArquivo; }
    
    public String getFormatoArquivo() { return formatoArquivo; }
    public void setFormatoArquivo(String formatoArquivo) { this.formatoArquivo = formatoArquivo; }
    
    public String getUsuarioUpload() { return usuarioUpload; }
    public void setUsuarioUpload(String usuarioUpload) { this.usuarioUpload = usuarioUpload; }
    
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    
    // Métodos utilitários
    public boolean isExpirado() {
        if (dataExpiracao == null) return false;
        return dataExpiracao.isBefore(LocalDateTime.now());
    }
    
    public boolean isProximoExpiracao() {
        if (dataExpiracao == null) return false;
        return dataExpiracao.isBefore(LocalDateTime.now().plusDays(30)) && 
               dataExpiracao.isAfter(LocalDateTime.now());
    }
    
    public boolean temAssinaturaDigital() {
        return assinaturaDigital != null && !assinaturaDigital.trim().isEmpty();
    }
    
    public String getTamanhoFormatado() {
        if (tamanhoArquivo < 1024) {
            return String.format("%.0f B", tamanhoArquivo);
        } else if (tamanhoArquivo < 1024 * 1024) {
            return String.format("%.1f KB", tamanhoArquivo / 1024);
        } else {
            return String.format("%.1f MB", tamanhoArquivo / (1024 * 1024));
        }
    }
    
    public void assinarDigitalmente(String assinatura) {
        this.assinaturaDigital = assinatura;
    }
    
    public void cancelar() {
        this.status = "CANCELADO";
        this.ativo = false;
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s (%s) - %s", 
                           nomeArquivo, tipo, status, codigoIrmao);
    }
}
