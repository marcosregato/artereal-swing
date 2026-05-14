package com.artereal.swing.model;

import java.util.Date;

/**
 * Record otimizado para Despesas
 * Substitui a classe Despesa com design moderno e imutável
 */
public record DespesaRecord(
    Long id,
    String descricao,
    double valor,
    Date data,
    String categoria,
    String fornecedor,
    String numeroDocumento,
    String createdAt,
    String updatedAt
) {
    
    // Construtor padrão para novas despesas
    public DespesaRecord(String descricao, double valor, Date data, String categoria, String fornecedor, String numeroDocumento) {
        this(
            null,
            descricao,
            valor,
            data,
            categoria,
            fornecedor,
            numeroDocumento,
            null,
            null
        );
    }
    
    // Construtor padrão simplificado
    public DespesaRecord(String descricao, double valor, Date data, String categoria) {
        this(
            null,
            descricao,
            valor,
            data,
            categoria,
            "Não informado",
            null,
            null,
            null
        );
    }
    
    // Métodos de validação
    public boolean isDespesaAlta() {
        return valor > 1000.0;
    }
    
    public boolean isDespesaUrgente() {
        return categoria != null && categoria.equalsIgnoreCase("Urgente");
    }
    
    // Métodos de formatação
    public String getValorFormatado() {
        return String.format("R$ %.2f", valor);
    }
    
    public String getDataFormatada() {
        if (data == null) return "Não definida";
        return new java.text.SimpleDateFormat("dd/MM/yyyy").format(data);
    }
}
