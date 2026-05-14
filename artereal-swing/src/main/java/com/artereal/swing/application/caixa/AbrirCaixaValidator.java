package com.artereal.swing.application.caixa;

import com.artereal.swing.domain.caixa.CaixaException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Validator para AbrirCaixaRequest
 */
public class AbrirCaixaValidator {
    
    public static void validate(AbrirCaixaRequest request) {
        List<String> errors = new ArrayList<>();
        
        if (request == null) {
            throw new CaixaException("Request não pode ser nulo");
        }
        
        if (request.lojaId() == null) {
            errors.add("ID da loja é obrigatório");
        }
        
        if (request.valor() == null) {
            errors.add("Valor do caixa é obrigatório");
        } else if (request.valor().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("Valor do caixa deve ser positivo");
        } else if (request.valor().scale() > 2) {
            errors.add("Valor do caixa não pode ter mais de 2 casas decimais");
        }
        
        if (request.responsavel() == null || request.responsavel().trim().isEmpty()) {
            errors.add("Responsável pelo caixa é obrigatório");
        } else if (request.responsavel().length() > 100) {
            errors.add("Responsável não pode exceder 100 caracteres");
        }
        
        if (!errors.isEmpty()) {
            throw new CaixaException(String.join(", ", errors));
        }
    }
}
