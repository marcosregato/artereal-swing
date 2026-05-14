package com.artereal.swing.domain.loja.valueobjects;

/**
 * Value Object para CNPJ
 * 
 * Este Value Object representa um CNPJ imutável
 * seguindo os princípios de DDD.
 */
public record Cnpj(String valor) {
    public Cnpj {
        validate();
    }
    
    private void validate() {
        if (valor == null || !isValid(valor)) {
            throw new IllegalArgumentException("CNPJ inválido");
        }
    }
    
    public static boolean isValid(String cnpj) {
        if (cnpj == null) {
            return false;
        }
        // Remover caracteres não numéricos
        String cnpjLimpo = cnpj.replaceAll("[^0-9]", "");
        // CNPJ deve ter 14 dígitos
        return cnpjLimpo.length() == 14;
    }
    
    public String formatado() {
        String cnpjLimpo = valor.replaceAll("[^0-9]", "");
        if (cnpjLimpo.length() != 14) {
            return valor;
        }
        return String.format("%s.%s.%s/%s-%s",
            cnpjLimpo.substring(0, 2),
            cnpjLimpo.substring(2, 5),
            cnpjLimpo.substring(5, 8),
            cnpjLimpo.substring(8, 12),
            cnpjLimpo.substring(12, 14)
        );
    }
    
    public String getNumeros() {
        return valor.replaceAll("[^0-9]", "");
    }
}
