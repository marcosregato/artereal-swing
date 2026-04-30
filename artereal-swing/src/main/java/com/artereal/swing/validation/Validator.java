package com.artereal.swing.validation;

/**
 * Interface genérica para validação de objetos
 */
@FunctionalInterface
public interface Validator<T> {
    
    ValidationResult validate(T object);
    
    default Validator<T> and(Validator<T> other) {
        return (T object) -> {
            ValidationResult firstResult = this.validate(object);
            if (firstResult.isInvalid()) {
                return firstResult;
            }
            return other.validate(object);
        };
    }
    
    default Validator<T> or(Validator<T> other) {
        return (T object) -> {
            ValidationResult firstResult = this.validate(object);
            if (firstResult.isValid()) {
                return firstResult;
            }
            ValidationResult secondResult = other.validate(object);
            if (secondResult.isValid()) {
                return secondResult;
            }
            // Combina os erros de ambas as validações
            return ValidationResult.combine(firstResult, secondResult);
        };
    }
}
