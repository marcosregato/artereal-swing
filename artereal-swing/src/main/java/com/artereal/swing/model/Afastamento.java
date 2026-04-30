package com.artereal.swing.model;

import java.time.LocalDate;

/**
 * Modelo de dados para Gestão de Afastamentos e Licenças
 */
public class Afastamento {
    
    private Long id;
    private Long codigoIrmao;
    private LocalDate dataInicial;
    private LocalDate dataFinal;
    private String descricao;
    private String motivo; // LICENCA_MEDICA, FERIAS, SUSPENSAO, AFASTAMENTO_TEMPORARIO, OUTRO
    private String status; // ATIVO, FINALIZADO, CANCELADO
    private String documentoComprobatorio;
    private LocalDate dataCadastro;
    private String usuarioCadastro;
    private String observacoes;
    private boolean afetaFrequencia;
    private int diasAfastamento;
    private String createdAt;
    private String updatedAt;
    
    public Afastamento() {}
    
    public Afastamento(Long codigoIrmao, LocalDate dataInicial, LocalDate dataFinal, String motivo) {
        this.codigoIrmao = codigoIrmao;
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        this.motivo = motivo;
        this.dataCadastro = LocalDate.now();
        this.status = "ATIVO";
        this.afetaFrequencia = true;
        this.diasAfastamento = calcularDias();
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getCodigoIrmao() { return codigoIrmao; }
    public void setCodigoIrmao(Long codigoIrmao) { this.codigoIrmao = codigoIrmao; }
    
    public LocalDate getDataInicial() { return dataInicial; }
    public void setDataInicial(LocalDate dataInicial) { 
        this.dataInicial = dataInicial;
        this.diasAfastamento = calcularDias();
    }
    
    public LocalDate getDataFinal() { return dataFinal; }
    public void setDataFinal(LocalDate dataFinal) { 
        this.dataFinal = dataFinal;
        this.diasAfastamento = calcularDias();
    }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getDocumentoComprobatorio() { return documentoComprobatorio; }
    public void setDocumentoComprobatorio(String documentoComprobatorio) { this.documentoComprobatorio = documentoComprobatorio; }
    
    public LocalDate getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDate dataCadastro) { this.dataCadastro = dataCadastro; }
    
    public String getUsuarioCadastro() { return usuarioCadastro; }
    public void setUsuarioCadastro(String usuarioCadastro) { this.usuarioCadastro = usuarioCadastro; }
    
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    
    public boolean isAfetaFrequencia() { return afetaFrequencia; }
    public void setAfetaFrequencia(boolean afetaFrequencia) { this.afetaFrequencia = afetaFrequencia; }
    
    public int getDiasAfastamento() { return diasAfastamento; }
    public void setDiasAfastamento(int diasAfastamento) { this.diasAfastamento = diasAfastamento; }
    
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    
    // Métodos utilitários
    public int calcularDias() {
        if (dataInicial == null || dataFinal == null) return 0;
        return (int) java.time.temporal.ChronoUnit.DAYS.between(dataInicial, dataFinal) + 1;
    }
    
    public boolean isAtivo() {
        return "ATIVO".equals(status);
    }
    
    public boolean isFinalizado() {
        return "FINALIZADO".equals(status);
    }
    
    public boolean isCancelado() {
        return "CANCELADO".equals(status);
    }
    
    public boolean estaEmAndamento() {
        LocalDate hoje = LocalDate.now();
        return isAtivo() && !hoje.isBefore(dataInicial) && !hoje.isAfter(dataFinal);
    }
    
    public boolean estaVencido() {
        LocalDate hoje = LocalDate.now();
        return isAtivo() && hoje.isAfter(dataFinal);
    }
    
    public boolean estaFuturo() {
        LocalDate hoje = LocalDate.now();
        return isAtivo() && hoje.isBefore(dataInicial);
    }
    
    public boolean isLicencaMedica() {
        return "LICENCA_MEDICA".equals(motivo);
    }
    
    public boolean isFerias() {
        return "FERIAS".equals(motivo);
    }
    
    public boolean isSuspenso() {
        return "SUSPENSAO".equals(motivo);
    }
    
    public void finalizar() {
        this.status = "FINALIZADO";
    }
    
    public void cancelar() {
        this.status = "CANCELADO";
    }
    
    public void reativar() {
        this.status = "ATIVO";
    }
    
    public String getMotivoFormatado() {
        switch (motivo) {
            case "LICENCA_MEDICA": return "Licença Médica";
            case "FERIAS": return "Férias";
            case "SUSPENSAO": return "Suspensão";
            case "AFASTAMENTO_TEMPORARIO": return "Afastamento Temporário";
            case "OUTRO": return "Outro";
            default: return motivo;
        }
    }
    
    public String getStatusFormatado() {
        switch (status) {
            case "ATIVO": return "Ativo";
            case "FINALIZADO": return "Finalizado";
            case "CANCELADO": return "Cancelado";
            default: return status;
        }
    }
    
    public String getPeriodoFormatado() {
        if (dataInicial == null || dataFinal == null) return "Não definido";
        return String.format("%s a %s", dataInicial, dataFinal);
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s (%s) - %s dias", 
                           descricao != null ? descricao : "Afastamento", 
                           getMotivoFormatado(), 
                           getStatusFormatado(), 
                           diasAfastamento);
    }
}
