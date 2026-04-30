package com.artereal.swing.validation;

import com.artereal.swing.model.Caixa;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Validador específico para o modelo Caixa
 */
public class CaixaValidator {
    
    public static Validator<Caixa> forCreation() {
        return (Caixa caixa) -> {
            ValidationResult result = ValidationResult.valid();
            
            // Validação do tipo (obrigatório)
            if (caixa.getTipo() == null || caixa.getTipo().trim().isEmpty()) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("tipo", "Tipo de movimentação é obrigatório", "REQUIRED_TYPE")
                ));
            } else {
                String tipo = caixa.getTipo().trim().toUpperCase();
                if (!tipo.matches("^(RECEITA|DESPESA)$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("tipo", "Tipo deve ser RECEITA ou DESPESA", "INVALID_TYPE", tipo)
                    ));
                }
            }
            
            // Validação do valor (obrigatório)
            if (caixa.getValor() == null) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("valor", "Valor é obrigatório", "REQUIRED_VALUE")
                ));
            } else {
                BigDecimal valor = caixa.getValor();
                if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("valor", "Valor deve ser positivo", "NON_POSITIVE_VALUE", valor)
                    ));
                } else if (valor.compareTo(new BigDecimal("999999.99")) > 0) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("valor", "Valor não pode ser maior que 999.999,99", "MAX_VALUE_EXCEEDED", valor)
                    ));
                }
                
                // Verificar se tem mais de 2 casas decimais
                if (valor.scale() > 2) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("valor", "Valor não pode ter mais de 2 casas decimais", "INVALID_SCALE", valor)
                    ));
                }
            }
            
            // Validação da descrição (obrigatória)
            if (caixa.getDescricao() == null || caixa.getDescricao().trim().isEmpty()) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("descricao", "Descrição é obrigatória", "REQUIRED_DESCRIPTION")
                ));
            } else {
                String descricao = caixa.getDescricao().trim();
                if (descricao.length() < 3) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("descricao", "Descrição deve ter pelo menos 3 caracteres", "MIN_LENGTH_DESCRIPTION", descricao)
                    ));
                } else if (descricao.length() > 500) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("descricao", "Descrição deve ter no máximo 500 caracteres", "MAX_LENGTH_DESCRIPTION", descricao)
                    ));
                }
            }
            
            // Validação da categoria (obrigatória)
            if (caixa.getCategoria() == null || caixa.getCategoria().trim().isEmpty()) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("categoria", "Categoria é obrigatória", "REQUIRED_CATEGORY")
                ));
            } else {
                String categoria = caixa.getCategoria().trim().toUpperCase();
                if (categoria.length() < 2) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("categoria", "Categoria deve ter pelo menos 2 caracteres", "MIN_LENGTH_CATEGORY", categoria)
                    ));
                } else if (categoria.length() > 50) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("categoria", "Categoria deve ter no máximo 50 caracteres", "MAX_LENGTH_CATEGORY", categoria)
                    ));
                } else if (!categoria.matches("^[A-Z_]{2,50}$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("categoria", "Categoria deve conter apenas letras maiúsculas e underscores", "INVALID_CATEGORY", categoria)
                    ));
                }
            }
            
            // Validação da data de movimentação (se fornecida)
            if (caixa.getDataMovimentacao() != null) {
                LocalDateTime dataMovimentacao = caixa.getDataMovimentacao();
                LocalDateTime agora = LocalDateTime.now();
                
                // Não permitir datas muito futuras (mais de 1 dia)
                if (dataMovimentacao.isAfter(agora.plusDays(1))) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("dataMovimentacao", "Data de movimentação não pode ser muito futura", "FUTURE_MOVEMENT_DATE", dataMovimentacao)
                    ));
                }
                
                // Não permitir datas muito antigas (mais de 5 anos)
                if (dataMovimentacao.isBefore(agora.minusYears(5))) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("dataMovimentacao", "Data de movimentação muito antiga (mais de 5 anos)", "TOO_OLD_MOVEMENT_DATE", dataMovimentacao)
                    ));
                }
            }
            
            // Validação do responsável (se fornecido)
            if (caixa.getResponsavel() != null && !caixa.getResponsavel().trim().isEmpty()) {
                String responsavel = caixa.getResponsavel().trim();
                if (responsavel.length() < 3) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("responsavel", "Nome do responsável deve ter pelo menos 3 caracteres", "MIN_LENGTH_RESPONSAVEL", responsavel)
                    ));
                } else if (responsavel.length() > 100) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("responsavel", "Nome do responsável deve ter no máximo 100 caracteres", "MAX_LENGTH_RESPONSAVEL", responsavel)
                    ));
                } else if (!responsavel.matches("^[A-Za-zÀ-ú\\s]{3,100}$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("responsavel", "Nome do responsável deve conter apenas letras e espaços", "INVALID_RESPONSAVEL", responsavel)
                    ));
                }
            }
            
            // Validação da forma de pagamento (se fornecida)
            if (caixa.getFormaPagamento() != null && !caixa.getFormaPagamento().trim().isEmpty()) {
                String formaPagamento = caixa.getFormaPagamento().trim().toUpperCase();
                if (!formaPagamento.matches("^(DINHEIRO|CHEQUE|TRANSFERENCIA|PIX|CARTAO|DEBITO)$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("formaPagamento", "Forma de pagamento inválida. Use: DINHEIRO, CHEQUE, TRANSFERENCIA, PIX, CARTAO ou DEBITO", "INVALID_PAYMENT_FORM", formaPagamento)
                    ));
                }
            }
            
            // Validação do número do documento (se fornecido)
            if (caixa.getNumeroDocumento() != null && !caixa.getNumeroDocumento().trim().isEmpty()) {
                String numeroDocumento = caixa.getNumeroDocumento().trim();
                if (numeroDocumento.length() > 50) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("numeroDocumento", "Número do documento deve ter no máximo 50 caracteres", "MAX_LENGTH_DOCUMENT_NUMBER", numeroDocumento)
                    ));
                } else if (!numeroDocumento.matches("^[A-Za-z0-9\\-\\/\\.]{1,50}$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("numeroDocumento", "Número do documento inválido", "INVALID_DOCUMENT_NUMBER", numeroDocumento)
                    ));
                }
            }
            
            // Validação do status (se fornecido)
            if (caixa.getStatus() != null && !caixa.getStatus().trim().isEmpty()) {
                String status = caixa.getStatus().trim().toUpperCase();
                if (!status.matches("^(PAGO|PENDENTE|CANCELADO)$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("status", "Status deve ser PAGO, PENDENTE ou CANCELADO", "INVALID_STATUS", status)
                    ));
                }
            } else {
                // Se não fornecido, assume PENDENTE como padrão
                caixa.setStatus("PENDENTE");
            }
            
            // Validação das observações (se fornecidas)
            if (caixa.getObservacoes() != null && !caixa.getObservacoes().trim().isEmpty()) {
                String observacoes = caixa.getObservacoes().trim();
                if (observacoes.length() > 1000) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("observacoes", "Observações devem ter no máximo 1000 caracteres", "MAX_LENGTH_OBSERVACOES", observacoes)
                    ));
                }
            }
            
            // Validação de regras de negócio específicas
            
            // Se for CHEQUE, número do documento é obrigatório
            if ("CHEQUE".equals(caixa.getFormaPagamento()) && 
                (caixa.getNumeroDocumento() == null || caixa.getNumeroDocumento().trim().isEmpty())) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("numeroDocumento", "Número do documento é obrigatório para pagamentos com cheque", "REQUIRED_DOCUMENT_FOR_CHEQUE")
                ));
            }
            
            // Se for TRANSFERENCIA ou PIX, deve haver observações com dados da transação
            if (("TRANSFERENCIA".equals(caixa.getFormaPagamento()) || "PIX".equals(caixa.getFormaPagamento())) && 
                (caixa.getObservacoes() == null || caixa.getObservacoes().trim().isEmpty())) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("observacoes", "Observações são obrigatórias para transferências e PIX (informar dados da transação)", "REQUIRED_OBSERVATIONS_FOR_TRANSFER")
                ));
            }
            
            return result;
        };
    }
    
    public static Validator<Caixa> forUpdate() {
        return (Caixa caixa) -> {
            ValidationResult result = ValidationResult.valid();
            
            // Para atualização, ID é obrigatório
            if (caixa.getId() == null) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("id", "ID é obrigatório para atualização", "REQUIRED_ID")
                ));
            } else if (caixa.getId() <= 0) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("id", "ID deve ser positivo", "INVALID_ID", caixa.getId())
                ));
            }
            
            // Aplica as mesmas validações de criação
            ValidationResult creationResult = forCreation().validate(caixa);
            result = ValidationResult.combine(result, creationResult);
            
            return result;
        };
    }
    
    public static Validator<Caixa> forStatusChange() {
        return (Caixa caixa) -> {
            ValidationResult result = ValidationResult.valid();
            
            // Para mudança de status, ID e status são obrigatórios
            if (caixa.getId() == null) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("id", "ID é obrigatório para mudança de status", "REQUIRED_ID")
                ));
            }
            
            if (caixa.getStatus() == null || caixa.getStatus().trim().isEmpty()) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("status", "Status é obrigatório", "REQUIRED_STATUS")
                ));
            } else {
                String status = caixa.getStatus().trim().toUpperCase();
                if (!status.matches("^(PAGO|PENDENTE|CANCELADO)$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("status", "Status deve ser PAGO, PENDENTE ou CANCELADO", "INVALID_STATUS", status)
                    ));
                }
            }
            
            return result;
        };
    }
    
    public static Validator<Caixa> forSearch() {
        return (Caixa caixa) -> {
            ValidationResult result = ValidationResult.valid();
            
            // Para busca, apenas valida se há critérios de busca válidos
            boolean hasCriteria = false;
            
            if (caixa.getTipo() != null && !caixa.getTipo().trim().isEmpty()) {
                hasCriteria = true;
                String tipo = caixa.getTipo().trim().toUpperCase();
                if (!tipo.matches("^(RECEITA|DESPESA)$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("tipo", "Tipo para busca deve ser RECEITA ou DESPESA", "INVALID_SEARCH_TYPE", tipo)
                    ));
                }
            }
            
            if (caixa.getCategoria() != null && !caixa.getCategoria().trim().isEmpty()) {
                hasCriteria = true;
            }
            
            if (caixa.getDescricao() != null && !caixa.getDescricao().trim().isEmpty()) {
                hasCriteria = true;
                String descricao = caixa.getDescricao().trim();
                if (descricao.length() < 2) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("descricao", "Descrição para busca deve ter pelo menos 2 caracteres", "MIN_LENGTH_SEARCH_DESCRIPTION", descricao)
                    ));
                }
            }
            
            if (caixa.getStatus() != null && !caixa.getStatus().trim().isEmpty()) {
                hasCriteria = true;
                String status = caixa.getStatus().trim().toUpperCase();
                if (!status.matches("^(PAGO|PENDENTE|CANCELADO)$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("status", "Status para busca deve ser PAGO, PENDENTE ou CANCELADO", "INVALID_SEARCH_STATUS", status)
                    ));
                }
            }
            
            if (caixa.getFormaPagamento() != null && !caixa.getFormaPagamento().trim().isEmpty()) {
                hasCriteria = true;
                String formaPagamento = caixa.getFormaPagamento().trim().toUpperCase();
                if (!formaPagamento.matches("^(DINHEIRO|CHEQUE|TRANSFERENCIA|PIX|CARTAO|DEBITO)$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("formaPagamento", "Forma de pagamento para busca inválida", "INVALID_SEARCH_PAYMENT_FORM", formaPagamento)
                    ));
                }
            }
            
            if (caixa.getResponsavel() != null && !caixa.getResponsavel().trim().isEmpty()) {
                hasCriteria = true;
            }
            
            // Validação de valor para busca (se fornecido)
            if (caixa.getValor() != null) {
                hasCriteria = true;
                BigDecimal valor = caixa.getValor();
                if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("valor", "Valor para busca deve ser positivo", "NON_POSITIVE_SEARCH_VALUE", valor)
                    ));
                }
            }
            
            if (!hasCriteria) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("search", "Pelo menos um critério de busca deve ser informado", "NO_SEARCH_CRITERIA")
                ));
            }
            
            return result;
        };
    }
    
    public static Validator<BigDecimal> valorValidator() {
        return (BigDecimal valor) -> {
            if (valor == null) {
                return ValidationResult.invalid(
                    new ValidationError("valor", "Valor é obrigatório", "REQUIRED_VALUE")
                );
            }
            
            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                return ValidationResult.invalid(
                    new ValidationError("valor", "Valor deve ser positivo", "NON_POSITIVE_VALUE", valor)
                );
            }
            
            if (valor.compareTo(new BigDecimal("999999.99")) > 0) {
                return ValidationResult.invalid(
                    new ValidationError("valor", "Valor não pode ser maior que 999.999,99", "MAX_VALUE_EXCEEDED", valor)
                );
            }
            
            if (valor.scale() > 2) {
                return ValidationResult.invalid(
                    new ValidationError("valor", "Valor não pode ter mais de 2 casas decimais", "INVALID_SCALE", valor)
                );
            }
            
            return ValidationResult.valid();
        };
    }
}
