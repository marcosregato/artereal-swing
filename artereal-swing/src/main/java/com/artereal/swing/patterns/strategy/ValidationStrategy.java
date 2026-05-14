package com.artereal.swing.patterns.strategy;

/**
 * Interface Strategy para validações
 * Implementa o padrão Strategy para diferentes tipos de validação
 */
public interface ValidationStrategy<T> {
    
    /**
     * Executa validação específica
     * 
     * @param data Dados a serem validados
     * @return Resultado da validação
     */
    ValidationResult validate(T data);
    
    /**
     * Obtém nome da estratégia de validação
     * 
     * @return Nome descritivo da validação
     */
    String getStrategyName();
    
    /**
     * Resultado de validação
     */
    class ValidationResult {
        private final boolean valid;
        private final String message;
        private final String field;
        
        public ValidationResult(boolean valid, String message, String field) {
            this.valid = valid;
            this.message = message;
            this.field = field;
        }
        
        public static ValidationResult success() {
            return new ValidationResult(true, null, null);
        }
        
        public static ValidationResult error(String message, String field) {
            return new ValidationResult(false, message, field);
        }
        
        public boolean isValid() {
            return valid;
        }
        
        public String getMessage() {
            return message;
        }
        
        public String getField() {
            return field;
        }
        
        @Override
        public String toString() {
            if (valid) {
                return "Validação bem-sucedida";
            } else {
                return String.format("Erro no campo '%s': %s", field, message);
            }
        }
    }
}
