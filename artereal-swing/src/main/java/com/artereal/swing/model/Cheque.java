package com.artereal.swing.model;

import java.time.LocalDate;

/**
 * Modelo de dados para Gestão de Cheques
 */
public class Cheque extends SimpleModel {
    
    private Long id;
    private String fatura;
    private LocalDate dataEmissao;
    private String sacado;
    private double valor;
    private LocalDate dataVencimento;
    private String modoPagamento;
    private String banco;
    private LocalDate dataPagamento;
    private double valorPago;
    private Long codigoCliente;
    private String situacao; // ABERTO, PAGO, CANCELADO, DEVOLVIDO
    private String grupo;
    private String historico;
    private String lancamentoCredito;
    private String lancamentoDebito;
    private String numeroNota;
    private Long codigoVendedor;
    private boolean ativo;
    private String createdAt;
    private String updatedAt;
    
    public Cheque() {}
    
    public Cheque(String sacado, double valor, LocalDate dataVencimento) {
        this.sacado = sacado;
        this.valor = valor;
        this.dataVencimento = dataVencimento;
        this.dataEmissao = LocalDate.now();
        this.situacao = "ABERTO";
        this.valorPago = 0.0;
        this.ativo = true;
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getFatura() { return fatura; }
    public void setFatura(String fatura) { this.fatura = fatura; }
    
    public LocalDate getDataEmissao() { return dataEmissao; }
    public void setDataEmissao(LocalDate dataEmissao) { this.dataEmissao = dataEmissao; }
    
    public String getSacado() { return sacado; }
    public void setSacado(String sacado) { this.sacado = sacado; }
    
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    
    public LocalDate getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }
    
    public String getModoPagamento() { return modoPagamento; }
    public void setModoPagamento(String modoPagamento) { this.modoPagamento = modoPagamento; }
    
    public String getBanco() { return banco; }
    public void setBanco(String banco) { this.banco = banco; }
    
    public LocalDate getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDate dataPagamento) { this.dataPagamento = dataPagamento; }
    
    public double getValorPago() { return valorPago; }
    public void setValorPago(double valorPago) { this.valorPago = valorPago; }
    
    public Long getCodigoCliente() { return codigoCliente; }
    public void setCodigoCliente(Long codigoCliente) { this.codigoCliente = codigoCliente; }
    
    public String getSituacao() { return situacao; }
    public void setSituacao(String situacao) { this.situacao = situacao; }
    
    public String getGrupo() { return grupo; }
    public void setGrupo(String grupo) { this.grupo = grupo; }
    
    public String getHistorico() { return historico; }
    public void setHistorico(String historico) { this.historico = historico; }
    
    public String getLancamentoCredito() { return lancamentoCredito; }
    public void setLancamentoCredito(String lancamentoCredito) { this.lancamentoCredito = lancamentoCredito; }
    
    public String getLancamentoDebito() { return lancamentoDebito; }
    public void setLancamentoDebito(String lancamentoDebito) { this.lancamentoDebito = lancamentoDebito; }
    
    public String getNumeroNota() { return numeroNota; }
    public void setNumeroNota(String numeroNota) { this.numeroNota = numeroNota; }
    
    public Long getCodigoVendedor() { return codigoVendedor; }
    public void setCodigoVendedor(Long codigoVendedor) { this.codigoVendedor = codigoVendedor; }
    
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    
    // Métodos utilitários
    public boolean isPago() {
        return "PAGO".equals(situacao);
    }
    
    public boolean isAberto() {
        return "ABERTO".equals(situacao);
    }
    
    public boolean isVencido() {
        return dataVencimento != null && dataVencimento.isBefore(LocalDate.now()) && isAberto();
    }
    
    public boolean isProximoVencimento() {
        return dataVencimento != null && 
               dataVencimento.isBefore(LocalDate.now().plusDays(7)) && 
               dataVencimento.isAfter(LocalDate.now()) && 
               isAberto();
    }
    
    public double getSaldo() {
        return valor - valorPago;
    }
    
    public void pagar(double valorPagamento) {
        this.valorPago = valorPagamento;
        this.dataPagamento = LocalDate.now();
        this.situacao = "PAGO";
    }
    
    public void cancelar() {
        this.situacao = "CANCELADO";
    }
    
    public void devolver() {
        this.situacao = "DEVOLVIDO";
    }
}
