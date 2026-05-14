package com.artereal.swing.application.irmao;

import com.artereal.swing.domain.irmao.IrmaoException;
import java.util.ArrayList;
import java.util.List;

/**
 * Validator para CriarIrmaoRequest
 */
public class CriarIrmaoValidator {
    
    public static void validate(CriarIrmaoRequest request) {
        List<String> errors = new ArrayList<>();
        
        if (request == null) {
            throw new IrmaoException("Request não pode ser nulo");
        }
        
        if (request.nome() == null || request.nome().trim().isEmpty()) {
            errors.add("Nome do irmão é obrigatório");
        } else if (request.nome().length() > 100) {
            errors.add("Nome do irmão não pode exceder 100 caracteres");
        }
        
        if (request.cpf() == null || request.cpf().trim().isEmpty()) {
            errors.add("CPF do irmão é obrigatório");
        } else if (!isValidCpf(request.cpf())) {
            errors.add("CPF inválido");
        }
        
        if (request.telefone() != null && request.telefone().length() > 20) {
            errors.add("Telefone não pode exceder 20 caracteres");
        }
        
        if (request.email() != null && !request.email().trim().isEmpty()) {
            if (!isValidEmail(request.email())) {
                errors.add("Email inválido");
            }
        }
        
        if (!errors.isEmpty()) {
            throw new IrmaoException(String.join(", ", errors));
        }
    }
    
    private static boolean isValidCpf(String cpf) {
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        return cpfLimpo.length() == 11;
    }
    
    private static boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
}
