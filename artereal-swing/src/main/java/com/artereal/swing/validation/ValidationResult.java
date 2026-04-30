package com.artereal.swing.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Resultado de uma validação contendo erros e status
 */
public class ValidationResult {
    
    private final boolean valid;
    private final List<ValidationError> errors;
    
    private ValidationResult(boolean valid, List<ValidationError> errors) {
        this.valid = valid;
        this.errors = new ArrayList<>(errors);
    }
    
    public static ValidationResult valid() {
        return new ValidationResult(true, new ArrayList<>());
    }
    
    public static ValidationResult invalid(ValidationError error) {
        List<ValidationError> errors = new ArrayList<>();
        errors.add(error);
        return new ValidationResult(false, errors);
    }
    
    public static ValidationResult invalid(List<ValidationError> errors) {
        return new ValidationResult(false, errors);
    }
    
    public static ValidationResult combine(ValidationResult... results) {
        List<ValidationError> allErrors = new ArrayList<>();
        boolean allValid = true;
        
        for (ValidationResult result : results) {
            if (!result.isValid()) {
                allValid = false;
                allErrors.addAll(result.getErrors());
            }
        }
        
        return allValid ? ValidationResult.valid() : ValidationResult.invalid(allErrors);
    }
    
    public boolean isValid() {
        return valid;
    }
    
    public boolean isInvalid() {
        return !valid;
    }
    
    public List<ValidationError> getErrors() {
        return new ArrayList<>(errors);
    }
    
    public String getErrorMessage() {
        if (valid) {
            return null;
        }
        
        if (errors.isEmpty()) {
            return "Erro de validação desconhecido";
        }
        
        if (errors.size() == 1) {
            return errors.get(0).getMessage();
        }
        
        StringBuilder sb = new StringBuilder("Múltiplos erros de validação:\n");
        for (int i = 0; i < errors.size(); i++) {
            sb.append(i + 1).append(". ").append(errors.get(i).getMessage());
            if (i < errors.size() - 1) {
                sb.append("\n");
            }
        }
        
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidationResult that = (ValidationResult) o;
        return valid == that.valid && Objects.equals(errors, that.errors);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(valid, errors);
    }
    
    @Override
    public String toString() {
        return "ValidationResult{" +
                "valid=" + valid +
                ", errors=" + errors +
                '}';
    }
}
