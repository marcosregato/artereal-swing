package com.artereal.swing.validation;

import com.artereal.swing.model.Caixa;
import com.artereal.swing.model.Irmao;
import com.artereal.swing.model.Loja;
import com.artereal.swing.model.Usuario;

/**
 * Serviço centralizado de validação para todo o sistema
 */
public class ValidationService {
    
    // Validadores de Usuario
    public static ValidationResult validateUsuarioForCreation(Usuario usuario) {
        return UsuarioValidator.forCreation().validate(usuario);
    }
    
    public static ValidationResult validateUsuarioForUpdate(Usuario usuario) {
        return UsuarioValidator.forUpdate().validate(usuario);
    }
    
    public static ValidationResult validateUsuarioForAuthentication(Usuario usuario) {
        return UsuarioValidator.forAuthentication().validate(usuario);
    }
    
    public static ValidationResult validateUsuarioForPasswordChange(Usuario usuario) {
        return UsuarioValidator.forPasswordChange().validate(usuario);
    }
    
    public static ValidationResult validateUsername(String username) {
        return UsuarioValidator.usernameValidator().validate(username);
    }
    
    public static ValidationResult validatePassword(String password) {
        return UsuarioValidator.passwordValidator().validate(password);
    }
    
    // Validadores de Irmao
    public static ValidationResult validateIrmaoForCreation(Irmao irmao) {
        return IrmaoValidator.forCreation().validate(irmao);
    }
    
    public static ValidationResult validateIrmaoForUpdate(Irmao irmao) {
        return IrmaoValidator.forUpdate().validate(irmao);
    }
    
    public static ValidationResult validateIrmaoForSearch(Irmao irmao) {
        return IrmaoValidator.forSearch().validate(irmao);
    }
    
    // Validadores de Loja
    public static ValidationResult validateLojaForCreation(Loja loja) {
        return LojaValidator.forCreation().validate(loja);
    }
    
    public static ValidationResult validateLojaForUpdate(Loja loja) {
        return LojaValidator.forUpdate().validate(loja);
    }
    
    public static ValidationResult validateLojaForStatusChange(Loja loja) {
        return LojaValidator.forStatusChange().validate(loja);
    }
    
    public static ValidationResult validateLojaForSearch(Loja loja) {
        return LojaValidator.forSearch().validate(loja);
    }
    
    // Validadores de Caixa
    public static ValidationResult validateCaixaForCreation(Caixa caixa) {
        return CaixaValidator.forCreation().validate(caixa);
    }
    
    public static ValidationResult validateCaixaForUpdate(Caixa caixa) {
        return CaixaValidator.forUpdate().validate(caixa);
    }
    
    public static ValidationResult validateCaixaForStatusChange(Caixa caixa) {
        return CaixaValidator.forStatusChange().validate(caixa);
    }
    
    public static ValidationResult validateCaixaForSearch(Caixa caixa) {
        return CaixaValidator.forSearch().validate(caixa);
    }
    
    public static ValidationResult validateCaixaValor(java.math.BigDecimal valor) {
        return CaixaValidator.valorValidator().validate(valor);
    }
    
    // Validadores comuns
    public static ValidationResult validateEmail(String email) {
        return CommonValidators.emailObrigatorio("email").validate(email);
    }
    
    public static ValidationResult validateTelefone(String telefone) {
        return CommonValidators.telefoneObrigatorio("telefone").validate(telefone);
    }
    
    public static ValidationResult validateCEP(String cep) {
        return CommonValidators.cepObrigatorio("cep").validate(cep);
    }
    
    public static ValidationResult validateCPF(String cpf) {
        return CommonValidators.cpfObrigatorio("cpf").validate(cpf);
    }
    
    public static ValidationResult validateNomeCompleto(String nome) {
        return CommonValidators.nomeCompleto("nome").validate(nome);
    }
    
    public static ValidationResult validateSenhaForte(String senha) {
        return CommonValidators.senhaForte("senha").validate(senha);
    }
    
    // Métodos utilitários para validação rápida
    
    /**
     * Valida se uma string não é nula nem vazia
     */
    public static boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }
    
    /**
     * Valida se uma string é nula ou vazia
     */
    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
    
    /**
     * Valida se um número é positivo
     */
    public static boolean isPositive(Number number) {
        return number != null && number.doubleValue() > 0;
    }
    
    /**
     * Valida se um email tem formato válido
     */
    public static boolean isValidEmail(String email) {
        return CommonValidators.emailValido("email").validate(email).isValid();
    }
    
    /**
     * Valida se um telefone tem formato válido
     */
    public static boolean isValidTelefone(String telefone) {
        return CommonValidators.telefoneValido("telefone").validate(telefone).isValid();
    }
    
    /**
     * Valida se um CEP tem formato válido
     */
    public static boolean isValidCEP(String cep) {
        return CommonValidators.cepValido("cep").validate(cep).isValid();
    }
    
    /**
     * Valida se um CPF tem formato válido
     */
    public static boolean isValidCPF(String cpf) {
        return CommonValidators.cpfValido("cpf").validate(cpf).isValid();
    }
    
    /**
     * Formata mensagem de erro para exibição na UI
     */
    public static String formatErrorMessage(ValidationResult result) {
        if (result.isValid()) {
            return null;
        }
        
        String message = result.getErrorMessage();
        if (message == null) {
            return "Erro de validação desconhecido";
        }
        
        // Se tiver múltiplos erros, retorna apenas o primeiro para UI
        if (message.contains("\n")) {
            return message.split("\n")[0];
        }
        
        return message;
    }
    
    /**
     * Formata todas as mensagens de erro para exibição detalhada
     */
    public static String formatAllErrorMessages(ValidationResult result) {
        if (result.isValid()) {
            return null;
        }
        
        return result.getErrorMessage();
    }
    
    /**
     * Validação rápida que retorna apenas true/false
     */
    public static <T> boolean quickValidate(Validator<T> validator, T object) {
        try {
            return validator.validate(object).isValid();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Lança exceção se validação falhar
     */
    public static <T> void validateOrThrow(Validator<T> validator, T object) throws ValidationException {
        ValidationResult result = validator.validate(object);
        if (result.isInvalid()) {
            throw new ValidationException(result.getErrorMessage());
        }
    }
    
    /**
     * Lança exceção se validação falhar (com resultado detalhado)
     */
    public static <T> void validateOrThrow(Validator<T> validator, T object, String errorMessage) throws ValidationException {
        ValidationResult result = validator.validate(object);
        if (result.isInvalid()) {
            throw new ValidationException(errorMessage + ": " + result.getErrorMessage());
        }
    }
    
    /**
     * Exceção customizada para validação
     */
    public static class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
        
        public ValidationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
