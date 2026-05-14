package com.artereal.swing.domain.caixa;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidade Caixa - parte do bounded context de Lojas
 * 
 * Esta entidade representa um caixa financeiro da ArteReal
 * com controle de abertura e fechamento.
 */
public class Caixa {
    
    private Long id;
    private Long lojaId;
    private BigDecimal valor;
    private String responsavel;
    private StatusCaixa status;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataFechamento;
    private BigDecimal saldo;
    
    // Construtor privado para forçar uso de factory methods
    private Caixa(Long id, Long lojaId, BigDecimal valor, String responsavel) {
        this.id = Objects.requireNonNull(id, "ID do caixa é obrigatório");
        this.lojaId = Objects.requireNonNull(lojaId, "ID da loja é obrigatório");
        this.valor = Objects.requireNonNull(valor, "Valor do caixa é obrigatório");
        this.responsavel = Objects.requireNonNull(responsavel, "Responsável pelo caixa é obrigatório");
        this.status = StatusCaixa.ABERTO;
        this.dataAbertura = LocalDateTime.now();
        this.dataFechamento = null;
        this.saldo = valor;
        
        validate();
    }
    
    /**
     * Factory method para abrir um novo caixa
     */
    public static Caixa abrir(BigDecimal valor, String responsavel, Long lojaId) {
        // Gerar ID sequencial simples para demonstração
        Long novoId = System.currentTimeMillis();
        return new Caixa(novoId, lojaId, valor, responsavel);
    }
    
    /**
     * Fecha o caixa
     */
    public void fechar() {
        if (this.status == StatusCaixa.FECHADO) {
            throw new CaixaException("Caixa já está fechado");
        }
        
        this.status = StatusCaixa.FECHADO;
        this.dataFechamento = LocalDateTime.now();
    }
    
    /**
     * Adiciona uma movimentação ao caixa
     */
    public void adicionarMovimentacao(BigDecimal valor, String tipo, String descricao) {
        Objects.requireNonNull(valor, "Valor da movimentação é obrigatório");
        Objects.requireNonNull(tipo, "Tipo da movimentação é obrigatório");
        
        if (this.status != StatusCaixa.ABERTO) {
            throw new CaixaException("Não é possível adicionar movimentação em caixa fechado");
        }
        
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CaixaException("Valor da movimentação deve ser positivo");
        }
        
        // Lógica de movimentação
        if ("ENTRADA".equals(tipo)) {
            this.saldo = this.saldo.add(valor);
        } else if ("SAIDA".equals(tipo)) {
            if (valor.compareTo(this.saldo) > 0) {
                throw new CaixaException("Saldo insuficiente para esta saída");
            }
            this.saldo = this.saldo.subtract(valor);
        } else {
            throw new CaixaException("Tipo de movimentação inválido: " + tipo);
        }
    }
    
    /**
     * Validações de negócio
     */
    private void validate() {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CaixaException("Valor do caixa deve ser positivo");
        }
        if (responsavel == null || responsavel.trim().isEmpty()) {
            throw new CaixaException("Responsável pelo caixa é obrigatório");
        }
    }
    
    /**
     * Verifica se o caixa está aberto
     */
    public boolean isAberto() {
        return status == StatusCaixa.ABERTO;
    }
    
    /**
     * Verifica se o caixa está fechado
     */
    public boolean isFechado() {
        return status == StatusCaixa.FECHADO;
    }
    
    /**
     * Calcula o tempo em que o caixa ficou aberto
     */
    public long getTempoAbertoEmHoras() {
        if (dataFechamento == null) {
            return java.time.Duration.between(dataAbertura, LocalDateTime.now()).toHours();
        }
        return java.time.Duration.between(dataAbertura, dataFechamento).toHours();
    }
    
    // Getters
    public Long getId() { return id; }
    public Long getLojaId() { return lojaId; }
    public BigDecimal getValor() { return valor; }
    public String getResponsavel() { return responsavel; }
    public StatusCaixa getStatus() { return status; }
    public LocalDateTime getDataAbertura() { return dataAbertura; }
    public LocalDateTime getDataFechamento() { return dataFechamento; }
    public BigDecimal getSaldo() { return saldo; }
    
    @Override
    public String toString() {
        return String.format("Caixa{id=%d, lojaId=%d, valor=%s, status=%s, saldo=%s}", 
            id, lojaId, valor, status, saldo);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Caixa)) return false;
        Caixa outro = (Caixa) o;
        return Objects.equals(id, outro.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
