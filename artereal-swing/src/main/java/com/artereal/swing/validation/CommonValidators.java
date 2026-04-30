package com.artereal.swing.validation;

import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * Validadores comuns para uso em todo o sistema
 */
public class CommonValidators {
    
    // Padrões de validação
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    private static final Pattern TELEFONE_PATTERN = 
        Pattern.compile("^\\(?([0-9]{2})\\)? ?([0-9]{4,5})-?([0-9]{4})$");
    
    private static final Pattern CEP_PATTERN = 
        Pattern.compile("^\\d{5}-?\\d{3}$");
    
    private static final Pattern CPF_PATTERN = 
        Pattern.compile("^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$");
    
    private static final Pattern NOME_PATTERN = 
        Pattern.compile("^[A-Za-zÀ-ú\\s]{2,100}$");
    
    // Validadores de String
    public static Validator<String> notNull(String field) {
        return (String value) -> {
            if (value == null) {
                return ValidationResult.invalid(
                    new ValidationError(field, "Campo não pode ser nulo", "NULL_VALUE")
                );
            }
            return ValidationResult.valid();
        };
    }
    
    public static Validator<String> notBlank(String field) {
        return (String value) -> {
            if (value == null || value.trim().isEmpty()) {
                return ValidationResult.invalid(
                    new ValidationError(field, "Campo não pode ser vazio", "BLANK_VALUE", value)
                );
            }
            return ValidationResult.valid();
        };
    }
    
    public static Validator<String> minLength(String field, int min) {
        return (String value) -> {
            if (value != null && value.length() < min) {
                return ValidationResult.invalid(
                    new ValidationError(field, 
                        String.format("Campo deve ter pelo menos %d caracteres", min), 
                        "MIN_LENGTH", value)
                );
            }
            return ValidationResult.valid();
        };
    }
    
    public static Validator<String> maxLength(String field, int max) {
        return (String value) -> {
            if (value != null && value.length() > max) {
                return ValidationResult.invalid(
                    new ValidationError(field, 
                        String.format("Campo deve ter no máximo %d caracteres", max), 
                        "MAX_LENGTH", value)
                );
            }
            return ValidationResult.valid();
        };
    }
    
    public static Validator<String> length(String field, int min, int max) {
        return minLength(field, min).and(maxLength(field, max));
    }
    
    public static Validator<String> nomeValido(String field) {
        return (String value) -> {
            if (value != null && !NOME_PATTERN.matcher(value.trim()).matches()) {
                return ValidationResult.invalid(
                    new ValidationError(field, 
                        "Nome deve conter apenas letras e espaços", 
                        "INVALID_NAME", value)
                );
            }
            return ValidationResult.valid();
        };
    }
    
    public static Validator<String> emailValido(String field) {
        return (String value) -> {
            if (value != null && !EMAIL_PATTERN.matcher(value.trim()).matches()) {
                return ValidationResult.invalid(
                    new ValidationError(field, 
                        "Email inválido", 
                        "INVALID_EMAIL", value)
                );
            }
            return ValidationResult.valid();
        };
    }
    
    public static Validator<String> telefoneValido(String field) {
        return (String value) -> {
            if (value != null && !TELEFONE_PATTERN.matcher(value.trim()).matches()) {
                return ValidationResult.invalid(
                    new ValidationError(field, 
                        "Telefone inválido. Use o formato (XX) XXXX-XXXX ou XXXX-XXXX", 
                        "INVALID_PHONE", value)
                );
            }
            return ValidationResult.valid();
        };
    }
    
    public static Validator<String> cepValido(String field) {
        return (String value) -> {
            if (value != null && !CEP_PATTERN.matcher(value.trim()).matches()) {
                return ValidationResult.invalid(
                    new ValidationError(field, 
                        "CEP inválido. Use o formato XXXXX-XXX", 
                        "INVALID_CEP", value)
                );
            }
            return ValidationResult.valid();
        };
    }
    
    public static Validator<String> cpfValido(String field) {
        return (String value) -> {
            if (value != null && !CPF_PATTERN.matcher(value.trim()).matches()) {
                return ValidationResult.invalid(
                    new ValidationError(field, 
                        "CPF inválido. Use o formato XXX.XXX.XXX-XX", 
                        "INVALID_CPF", value)
                );
            }
            return ValidationResult.valid();
        };
    }
    
    // Validadores de Número
    public static Validator<Integer> range(String field, int min, int max) {
        return (Integer value) -> {
            if (value != null && (value < min || value > max)) {
                return ValidationResult.invalid(
                    new ValidationError(field, 
                        String.format("Valor deve estar entre %d e %d", min, max), 
                        "OUT_OF_RANGE", value)
                );
            }
            return ValidationResult.valid();
        };
    }
    
    public static Validator<Long> positive(String field) {
        return (Long value) -> {
            if (value != null && value <= 0) {
                return ValidationResult.invalid(
                    new ValidationError(field, 
                        "Valor deve ser positivo", 
                        "NON_POSITIVE", value)
                );
            }
            return ValidationResult.valid();
        };
    }
    
    public static Validator<Double> positiveDouble(String field) {
        return (Double value) -> {
            if (value != null && value <= 0) {
                return ValidationResult.invalid(
                    new ValidationError(field, 
                        "Valor deve ser positivo", 
                        "NON_POSITIVE", value)
                );
            }
            return ValidationResult.valid();
        };
    }
    
    // Validadores de Data
    public static Validator<LocalDate> notFuture(String field) {
        return (LocalDate date) -> {
            if (date != null && date.isAfter(LocalDate.now())) {
                return ValidationResult.invalid(
                    new ValidationError(field, 
                        "Data não pode ser futura", 
                        "FUTURE_DATE", date)
                );
            }
            return ValidationResult.valid();
        };
    }
    
    public static Validator<LocalDate> notTooOld(String field, int maxAge) {
        return (LocalDate date) -> {
            if (date != null) {
                LocalDate minDate = LocalDate.now().minusYears(maxAge);
                if (date.isBefore(minDate)) {
                    return ValidationResult.invalid(
                        new ValidationError(field, 
                            String.format("Data não pode ser anterior a %d anos", maxAge), 
                            "TOO_OLD", date)
                    );
                }
            }
            return ValidationResult.valid();
        };
    }
    
    public static Validator<LocalDate> reasonableAge(String field) {
        return notFuture(field).and(notTooOld(field, 120));
    }
    
    // Validadores de Boolean
    public static Validator<Boolean> requireTrue(String field, String errorMessage) {
        return (Boolean value) -> {
            if (value == null || !value) {
                return ValidationResult.invalid(
                    new ValidationError(field, errorMessage, "REQUIRED_TRUE", value)
                );
            }
            return ValidationResult.valid();
        };
    }
    
    // Validadores Compostos
    public static Validator<String> nomeCompleto(String field) {
        return notNull(field)
            .and(notBlank(field))
            .and(minLength(field, 2))
            .and(maxLength(field, 100))
            .and(nomeValido(field));
    }
    
    public static Validator<String> emailObrigatorio(String field) {
        return notNull(field)
            .and(notBlank(field))
            .and(maxLength(field, 255))
            .and(emailValido(field));
    }
    
    public static Validator<String> telefoneObrigatorio(String field) {
        return notNull(field)
            .and(notBlank(field))
            .and(telefoneValido(field));
    }
    
    public static Validator<String> cepObrigatorio(String field) {
        return notNull(field)
            .and(notBlank(field))
            .and(cepValido(field));
    }
    
    public static Validator<String> cpfObrigatorio(String field) {
        return notNull(field)
            .and(notBlank(field))
            .and(cpfValido(field));
    }
    
    public static Validator<String> senhaForte(String field) {
        return notNull(field)
            .and(minLength(field, 8))
            .and(maxLength(field, 128))
            .and((String value) -> {
                if (value != null && !value.matches(".*[A-Z].*")) {
                    return ValidationResult.invalid(
                        new ValidationError(field, 
                            "Senha deve conter pelo menos uma letra maiúscula", 
                            "NO_UPPERCASE", value)
                    );
                }
                return ValidationResult.valid();
            })
            .and((String value) -> {
                if (value != null && !value.matches(".*[a-z].*")) {
                    return ValidationResult.invalid(
                        new ValidationError(field, 
                            "Senha deve conter pelo menos uma letra minúscula", 
                            "NO_LOWERCASE", value)
                    );
                }
                return ValidationResult.valid();
            })
            .and((String value) -> {
                if (value != null && !value.matches(".*\\d.*")) {
                    return ValidationResult.invalid(
                        new ValidationError(field, 
                            "Senha deve conter pelo menos um número", 
                            "NO_DIGIT", value)
                    );
                }
                return ValidationResult.valid();
            })
            .and((String value) -> {
                if (value != null && !value.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
                    return ValidationResult.invalid(
                        new ValidationError(field, 
                            "Senha deve conter pelo menos um caractere especial", 
                            "NO_SPECIAL_CHAR", value)
                    );
                }
                return ValidationResult.valid();
            });
    }
}
