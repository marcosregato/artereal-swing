package com.artereal.swing.application.loja;

import com.artereal.swing.domain.loja.LojaException;
import java.util.ArrayList;
import java.util.List;

/**
 * Validator para CriarLojaRequest
 */
public class CriarLojaValidator {
    
    public static void validate(CriarLojaRequest request) {
        List<String> errors = new ArrayList<>();
        
        if (request == null) {
            throw new LojaException("Request não pode ser nulo");
        }
        
        if (request.nome() == null || request.nome().trim().isEmpty()) {
            errors.add("Nome da loja é obrigatório");
        } else if (request.nome().length() > 200) {
            errors.add("Nome da loja não pode exceder 200 caracteres");
        }
        
        if (request.cnpj() == null || request.cnpj().trim().isEmpty()) {
            errors.add("CNPJ da loja é obrigatório");
        } else if (!isValidCnpj(request.cnpj())) {
            errors.add("CNPJ inválido");
        }
        
        if (request.endereco() == null || request.endereco().trim().isEmpty()) {
            errors.add("Endereço da loja é obrigatório");
        } else if (request.endereco().length() > 500) {
            errors.add("Endereço não pode exceder 500 caracteres");
        }
        
        if (request.cidade() == null || request.cidade().trim().isEmpty()) {
            errors.add("Cidade da loja é obrigatória");
        }
        
        if (request.estado() == null || request.estado().trim().isEmpty()) {
            errors.add("Estado da loja é obrigatório");
        } else if (request.estado().length() != 2) {
            errors.add("Estado deve ter 2 caracteres (UF)");
        }
        
        if (!errors.isEmpty()) {
            throw new LojaException(String.join(", ", errors));
        }
    }
    
    private static boolean isValidCnpj(String cnpj) {
        String cnpjLimpo = cnpj.replaceAll("[^0-9]", "");
        return cnpjLimpo.length() == 14;
    }
}
