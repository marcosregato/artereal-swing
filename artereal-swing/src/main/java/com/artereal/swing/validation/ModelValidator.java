package com.artereal.swing.validation;

import com.artereal.swing.model.SimpleModel;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Validador automático para evitar erros comuns em modelos de dados
 */
public class ModelValidator {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$"
    );
    
    private static final Pattern TELEFONE_PATTERN = Pattern.compile(
        "^[0-9]{10,15}$"
    );
    
    private static final Pattern CEP_PATTERN = Pattern.compile(
        "^[0-9]{5}-[0-9]{3}$"
    );
    
    /**
     * Valida um modelo de dados genérico
     */
    public static ValidationResult validate(SimpleModel model) {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        // Validações básicas para todos os modelos
        if (model == null) {
            errors.add("Modelo não pode ser nulo");
            return new ValidationResult(errors, warnings);
        }
        
        // Validação de ID
        if (model.getId() != null && model.getId() <= 0) {
            errors.add("ID deve ser positivo ou nulo para novos registros");
        }
        
        return new ValidationResult(errors, warnings);
    }
    
    /**
     * Valida campo de email
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Valida campo de telefone
     */
    public static boolean isValidTelefone(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            return false;
        }
        // Remove caracteres não numéricos
        String telefoneLimpo = telefone.replaceAll("[^0-9]", "");
        return TELEFONE_PATTERN.matcher(telefoneLimpo).matches();
    }
    
    /**
     * Valida campo de CEP
     */
    public static boolean isValidCEP(String cep) {
        if (cep == null || cep.trim().isEmpty()) {
            return false;
        }
        return CEP_PATTERN.matcher(cep).matches();
    }
    
    /**
     * Valida string não vazia
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
    
    /**
     * Valida número positivo
     */
    public static boolean isPositive(Number number) {
        return number != null && number.doubleValue() > 0;
    }
    
    /**
     * Valida data no formato dd/MM/yyyy
     */
    public static boolean isValidDate(String data) {
        if (data == null || data.trim().isEmpty()) {
            return false;
        }
        
        try {
            String[] partes = data.split("/");
            if (partes.length != 3) return false;
            
            int dia = Integer.parseInt(partes[0]);
            int mes = Integer.parseInt(partes[1]);
            int ano = Integer.parseInt(partes[2]);
            
            if (mes < 1 || mes > 12) return false;
            if (dia < 1 || dia > 31) return false;
            if (ano < 1900 || ano > 2100) return false;
            
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Classe para armazenar resultado da validação
     */
    public static class ValidationResult {
        private final List<String> errors;
        private final List<String> warnings;
        
        public ValidationResult(List<String> errors, List<String> warnings) {
            this.errors = new ArrayList<>(errors);
            this.warnings = new ArrayList<>(warnings);
        }
        
        public boolean isValid() {
            return errors.isEmpty();
        }
        
        public boolean hasWarnings() {
            return !warnings.isEmpty();
        }
        
        public List<String> getErrors() {
            return new ArrayList<>(errors);
        }
        
        public List<String> getWarnings() {
            return new ArrayList<>(warnings);
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (!errors.isEmpty()) {
                sb.append("Errors: ").append(String.join(", ", errors));
            }
            if (!warnings.isEmpty()) {
                if (sb.length() > 0) sb.append(" | ");
                sb.append("Warnings: ").append(String.join(", ", warnings));
            }
            if (sb.length() == 0) {
                sb.append("Valid");
            }
            return sb.toString();
        }
    }
}
