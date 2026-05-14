package com.artereal.swing.validation;

import java.util.Objects;

/**
 * Representa um erro de validação com campo, mensagem e código
 */
public class ValidationError {
    
    private final String field;
    private final String message;
    private final String code;
    private final Object rejectedValue;
    
    public ValidationError(String field, String message) {
        this(field, message, null, null);
    }
    
    public ValidationError(String field, String message, String code) {
        this(field, message, code, null);
    }
    
    public ValidationError(String field, String message, String code, Object rejectedValue) {
        this.field = field;
        this.message = message;
        this.code = code;
        this.rejectedValue = rejectedValue;
    }
    
    public String getField() {
        return field;
    }
    
    public String getMessage() {
        return message;
    }
    
    public String getCode() {
        return code;
    }
    
    public Object getRejectedValue() {
        return rejectedValue;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidationError that = (ValidationError) o;
        return Objects.equals(field, that.field) && 
               Objects.equals(message, that.message) && 
               Objects.equals(code, that.code);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(field, message, code);
    }
    
    @Override
    public String toString() {
        return "ValidationError{" +
                "field='" + field + '\'' +
                ", message='" + message + '\'' +
                ", code='" + code + '\'' +
                ", rejectedValue=" + rejectedValue +
                '}';
    }
}
