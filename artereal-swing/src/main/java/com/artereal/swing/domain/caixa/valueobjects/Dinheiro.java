package com.artereal.swing.domain.caixa.valueobjects;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Value Object para Dinheiro
 * 
 * Este Value Object representa um valor monetário imutável
 * seguindo os princípios de DDD.
 */
public record Dinheiro(BigDecimal valor) {
    public Dinheiro {
        validate();
    }
    
    private void validate() {
        if (valor == null) {
            throw new IllegalArgumentException("Valor não pode ser nulo");
        }
        if (valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor não pode ser negativo");
        }
    }
    
    public static Dinheiro zero() {
        return new Dinheiro(BigDecimal.ZERO);
    }
    
    public static Dinheiro de(double valor) {
        return new Dinheiro(BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP));
    }
    
    public static Dinheiro de(String valor) {
        return new Dinheiro(new BigDecimal(valor).setScale(2, RoundingMode.HALF_UP));
    }
    
    public Dinheiro somar(Dinheiro outro) {
        return new Dinheiro(this.valor.add(outro.valor).setScale(2, RoundingMode.HALF_UP));
    }
    
    public Dinheiro subtrair(Dinheiro outro) {
        BigDecimal resultado = this.valor.subtract(outro.valor).setScale(2, RoundingMode.HALF_UP);
        if (resultado.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Resultado não pode ser negativo");
        }
        return new Dinheiro(resultado);
    }
    
    public Dinheiro multiplicar(BigDecimal fator) {
        if (fator == null || fator.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Fator inválido");
        }
        return new Dinheiro(this.valor.multiply(fator).setScale(2, RoundingMode.HALF_UP));
    }
    
    public boolean isZero() {
        return valor.compareTo(BigDecimal.ZERO) == 0;
    }
    
    public boolean isPositivo() {
        return valor.compareTo(BigDecimal.ZERO) > 0;
    }
    
    public String formatado() {
        return String.format("R$ %,.2f", valor);
    }
    
    @Override
    public String toString() {
        return formatado();
    }
}
