package com.artereal.swing.patterns.strategy;

/**
 * Estratégia de validação para nomes de usuário
 * Implementa ValidationStrategy para validação específica de nomes
 */
public class UserNameValidationStrategy implements ValidationStrategy<String> {
    
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 50;
    private static final String ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567899._-";
    
    @Override
    public ValidationResult validate(String nome) {
        // Validação de nulo
        if (nome == null) {
            return ValidationResult.error("Nome não pode ser nulo", "nome");
        }
        
        String trimmedNome = nome.trim();
        
        // Validação de vazio
        if (trimmedNome.isEmpty()) {
            return ValidationResult.error("Nome não pode estar vazio", "nome");
        }
        
        // Validação de tamanho mínimo
        if (trimmedNome.length() < MIN_LENGTH) {
            return ValidationResult.error(String.format(
                "Nome deve ter pelo menos %d caracteres", MIN_LENGTH), "nome");
        }
        
        // Validação de tamanho máximo
        if (trimmedNome.length() > MAX_LENGTH) {
            return ValidationResult.error(String.format(
                "Nome não pode ter mais de %d caracteres", MAX_LENGTH), "nome");
        }
        
        // Validação de caracteres permitidos
        for (char c : trimmedNome.toCharArray()) {
            if (ALLOWED_CHARS.indexOf(c) == -1) {
                return ValidationResult.error(String.format(
                    "Caractere '%c' não é permitido no nome", c), "nome");
            }
        }
        
        // Validação de palavras reservadas
        String lowerNome = trimmedNome.toLowerCase();
        if (isReservedWord(lowerNome)) {
            return ValidationResult.error("Nome não pode ser uma palavra reservada", "nome");
        }
        
        // Validação de espaços consecutivos
        if (trimmedNome.contains("  ")) {
            return ValidationResult.error("Nome não pode ter espaços consecutivos", "nome");
        }
        
        return ValidationResult.success();
    }
    
    @Override
    public String getStrategyName() {
        return "Validação de Nome de Usuário";
    }
    
    /**
     * Verifica se é uma palavra reservada
     */
    private boolean isReservedWord(String nome) {
        String[] reservedWords = {
            "admin", "administrator", "root", "system", "guest",
            "user", "test", "demo", "null", "undefined",
            "anonymous", "public", "private", "protected"
        };
        
        for (String reserved : reservedWords) {
            if (nome.equals(reserved)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Obtém comprimento mínimo permitido
     */
    public static int getMinLength() {
        return MIN_LENGTH;
    }
    
    /**
     * Obtém comprimento máximo permitido
     */
    public static int getMaxLength() {
        return MAX_LENGTH;
    }
    
    /**
     * Obtém caracteres permitidos
     */
    public static String getAllowedChars() {
        return ALLOWED_CHARS;
    }
}
