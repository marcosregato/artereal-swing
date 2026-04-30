package com.artereal.swing.validation;

import com.artereal.swing.model.Loja;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Validador específico para o modelo Loja
 */
public class LojaValidator {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public static Validator<Loja> forCreation() {
        return (Loja loja) -> {
            ValidationResult result = ValidationResult.valid();
            
            // Validação do nome (obrigatório)
            if (loja.getNome() == null || loja.getNome().trim().isEmpty()) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("nome", "Nome da loja é obrigatório", "REQUIRED_NAME")
                ));
            } else {
                String nome = loja.getNome().trim();
                if (nome.length() < 3) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("nome", "Nome da loja deve ter pelo menos 3 caracteres", "MIN_LENGTH_NAME", nome)
                    ));
                } else if (nome.length() > 100) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("nome", "Nome da loja deve ter no máximo 100 caracteres", "MAX_LENGTH_NAME", nome)
                    ));
                } else if (!nome.matches("^[A-Za-zÀ-ú0-9\\s\\.\\-]{3,100}$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("nome", "Nome da loja deve conter apenas letras, números, espaços, pontos e hífens", "INVALID_NAME", nome)
                    ));
                }
            }
            
            // Validação do número (obrigatório)
            if (loja.getNumero() == null || loja.getNumero().trim().isEmpty()) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("numero", "Número da loja é obrigatório", "REQUIRED_NUMBER")
                ));
            } else {
                String numero = loja.getNumero().trim();
                if (!numero.matches("^[0-9]{1,6}$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("numero", "Número da loja deve conter apenas números (máximo 6 dígitos)", "INVALID_NUMBER", numero)
                    ));
                }
            }
            
            // Validação do endereço (obrigatório)
            if (loja.getEndereco() == null || loja.getEndereco().trim().isEmpty()) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("endereco", "Endereço é obrigatório", "REQUIRED_ADDRESS")
                ));
            } else {
                String endereco = loja.getEndereco().trim();
                if (endereco.length() < 5) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("endereco", "Endereço deve ter pelo menos 5 caracteres", "MIN_LENGTH_ADDRESS", endereco)
                    ));
                } else if (endereco.length() > 200) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("endereco", "Endereço deve ter no máximo 200 caracteres", "MAX_LENGTH_ADDRESS", endereco)
                    ));
                }
            }
            
            // Validação do bairro (obrigatório)
            if (loja.getBairro() == null || loja.getBairro().trim().isEmpty()) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("bairro", "Bairro é obrigatório", "REQUIRED_BAIRRO")
                ));
            } else {
                String bairro = loja.getBairro().trim();
                if (bairro.length() < 2) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("bairro", "Bairro deve ter pelo menos 2 caracteres", "MIN_LENGTH_BAIRRO", bairro)
                    ));
                } else if (bairro.length() > 100) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("bairro", "Bairro deve ter no máximo 100 caracteres", "MAX_LENGTH_BAIRRO", bairro)
                    ));
                } else if (!bairro.matches("^[A-Za-zÀ-ú0-9\\s]{2,100}$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("bairro", "Bairro deve conter apenas letras, números e espaços", "INVALID_BAIRRO", bairro)
                    ));
                }
            }
            
            // Validação da cidade (obrigatória)
            if (loja.getCidade() == null || loja.getCidade().trim().isEmpty()) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("cidade", "Cidade é obrigatória", "REQUIRED_CITY")
                ));
            } else {
                String cidade = loja.getCidade().trim();
                if (cidade.length() < 2) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("cidade", "Cidade deve ter pelo menos 2 caracteres", "MIN_LENGTH_CITY", cidade)
                    ));
                } else if (cidade.length() > 100) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("cidade", "Cidade deve ter no máximo 100 caracteres", "MAX_LENGTH_CITY", cidade)
                    ));
                } else if (!cidade.matches("^[A-Za-zÀ-ú\\s]{2,100}$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("cidade", "Cidade deve conter apenas letras e espaços", "INVALID_CITY", cidade)
                    ));
                }
            }
            
            // Validação do estado (obrigatório)
            if (loja.getEstado() == null || loja.getEstado().trim().isEmpty()) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("estado", "Estado é obrigatório", "REQUIRED_STATE")
                ));
            } else {
                String estado = loja.getEstado().trim();
                if (!estado.matches("^[A-Z]{2}$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("estado", "Estado deve ser a sigla com 2 letras maiúsculas (ex: SP, RJ)", "INVALID_STATE", estado)
                    ));
                }
            }
            
            // Validação do CEP (obrigatório)
            if (loja.getCep() == null || loja.getCep().trim().isEmpty()) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("cep", "CEP é obrigatório", "REQUIRED_CEP")
                ));
            } else {
                String cep = loja.getCep().trim();
                if (!cep.matches("^\\d{5}-?\\d{3}$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("cep", "CEP inválido. Use o formato XXXXX-XXX", "INVALID_CEP", cep)
                    ));
                }
            }
            
            // Validação do telefone (obrigatório)
            if (loja.getTelefone() == null || loja.getTelefone().trim().isEmpty()) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("telefone", "Telefone é obrigatório", "REQUIRED_PHONE")
                ));
            } else {
                String telefone = loja.getTelefone().trim();
                if (!telefone.matches("^\\(?([0-9]{2})\\)? ?([0-9]{4,5})-?([0-9]{4})$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("telefone", "Telefone inválido. Use o formato (XX) XXXX-XXXX ou XXXX-XXXX", "INVALID_PHONE", telefone)
                    ));
                }
            }
            
            // Validação do email (se fornecido)
            if (loja.getEmail() != null && !loja.getEmail().trim().isEmpty()) {
                String email = loja.getEmail().trim();
                if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("email", "Email inválido", "INVALID_EMAIL", email)
                    ));
                } else if (email.length() > 255) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("email", "Email deve ter no máximo 255 caracteres", "MAX_LENGTH_EMAIL", email)
                    ));
                }
            }
            
            // Validação do presidente (se fornecido)
            if (loja.getPresidente() != null && !loja.getPresidente().trim().isEmpty()) {
                String presidente = loja.getPresidente().trim();
                if (presidente.length() < 3) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("presidente", "Nome do presidente deve ter pelo menos 3 caracteres", "MIN_LENGTH_PRESIDENTE", presidente)
                    ));
                } else if (presidente.length() > 100) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("presidente", "Nome do presidente deve ter no máximo 100 caracteres", "MAX_LENGTH_PRESIDENTE", presidente)
                    ));
                } else if (!presidente.matches("^[A-Za-zÀ-ú\\s]{3,100}$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("presidente", "Nome do presidente deve conter apenas letras e espaços", "INVALID_PRESIDENTE", presidente)
                    ));
                }
            }
            
            // Validação do secretário (se fornecido)
            if (loja.getSecretario() != null && !loja.getSecretario().trim().isEmpty()) {
                String secretario = loja.getSecretario().trim();
                if (secretario.length() < 3) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("secretario", "Nome do secretário deve ter pelo menos 3 caracteres", "MIN_LENGTH_SECRETARIO", secretario)
                    ));
                } else if (secretario.length() > 100) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("secretario", "Nome do secretário deve ter no máximo 100 caracteres", "MAX_LENGTH_SECRETARIO", secretario)
                    ));
                } else if (!secretario.matches("^[A-Za-zÀ-ú\\s]{3,100}$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("secretario", "Nome do secretário deve conter apenas letras e espaços", "INVALID_SECRETARIO", secretario)
                    ));
                }
            }
            
            // Validação do tesoureiro (se fornecido)
            if (loja.getTesoureiro() != null && !loja.getTesoureiro().trim().isEmpty()) {
                String tesoureiro = loja.getTesoureiro().trim();
                if (tesoureiro.length() < 3) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("tesoureiro", "Nome do tesoureiro deve ter pelo menos 3 caracteres", "MIN_LENGTH_TESOUREIRO", tesoureiro)
                    ));
                } else if (tesoureiro.length() > 100) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("tesoureiro", "Nome do tesoureiro deve ter no máximo 100 caracteres", "MAX_LENGTH_TESOUREIRO", tesoureiro)
                    ));
                } else if (!tesoureiro.matches("^[A-Za-zÀ-ú\\s]{3,100}$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("tesoureiro", "Nome do tesoureiro deve conter apenas letras e espaços", "INVALID_TESOUREIRO", tesoureiro)
                    ));
                }
            }
            
            // Validação da data de fundação (se fornecida)
            if (loja.getDataFundacao() != null && !loja.getDataFundacao().trim().isEmpty()) {
                String dataFundacao = loja.getDataFundacao().trim();
                try {
                    LocalDate data = LocalDate.parse(dataFundacao, DATE_FORMATTER);
                    LocalDate hoje = LocalDate.now();
                    
                    if (data.isAfter(hoje)) {
                        result = ValidationResult.combine(result, ValidationResult.invalid(
                            new ValidationError("dataFundacao", "Data de fundação não pode ser futura", "FUTURE_FOUNDATION_DATE", dataFundacao)
                        ));
                    } else if (data.isBefore(hoje.minusYears(300))) {
                        result = ValidationResult.combine(result, ValidationResult.invalid(
                            new ValidationError("dataFundacao", "Data de fundação muito antiga (mais de 300 anos)", "TOO_OLD_FOUNDATION_DATE", dataFundacao)
                        ));
                    }
                } catch (DateTimeParseException e) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("dataFundacao", "Data de fundação inválida. Use o formato dd/MM/yyyy", "INVALID_DATE_FORMAT", dataFundacao)
                    ));
                }
            }
            
            // Validação do rito (se fornecido)
            if (loja.getRito() != null && !loja.getRito().trim().isEmpty()) {
                String rito = loja.getRito().trim();
                if (rito.length() > 50) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("rito", "Rito deve ter no máximo 50 caracteres", "MAX_LENGTH_RITO", rito)
                    ));
                } else if (!rito.matches("^[A-Za-zÀ-ú\\s]{2,50}$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("rito", "Rito deve conter apenas letras e espaços", "INVALID_RITO", rito)
                    ));
                }
            }
            
            // Validação da potência (se fornecida)
            if (loja.getPotencia() != null && !loja.getPotencia().trim().isEmpty()) {
                String potencia = loja.getPotencia().trim();
                if (potencia.length() > 100) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("potencia", "Potência deve ter no máximo 100 caracteres", "MAX_LENGTH_POTENCIA", potencia)
                    ));
                } else if (!potencia.matches("^[A-Za-zÀ-ú\\s\\-\\.]{2,100}$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("potencia", "Potência deve conter apenas letras, espaços, hífens e pontos", "INVALID_POTENCIA", potencia)
                    ));
                }
            }
            
            // Validação das observações (se fornecidas)
            if (loja.getObservacoes() != null && !loja.getObservacoes().trim().isEmpty()) {
                String observacoes = loja.getObservacoes().trim();
                if (observacoes.length() > 1000) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("observacoes", "Observações devem ter no máximo 1000 caracteres", "MAX_LENGTH_OBSERVACOES", observacoes)
                    ));
                }
            }
            
            // Validação do status (se fornecido)
            if (loja.getStatus() != null && !loja.getStatus().trim().isEmpty()) {
                String status = loja.getStatus().trim().toUpperCase();
                if (!status.matches("^(ATIVA|INATIVA|SUSPENSA)$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("status", "Status deve ser ATIVA, INATIVA ou SUSPENSA", "INVALID_STATUS", status)
                    ));
                }
            } else {
                // Se não fornecido, assume ATIVA como padrão
                loja.setStatus("ATIVA");
            }
            
            return result;
        };
    }
    
    public static Validator<Loja> forUpdate() {
        return (Loja loja) -> {
            ValidationResult result = ValidationResult.valid();
            
            // Para atualização, ID é obrigatório
            if (loja.getId() == null) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("id", "ID é obrigatório para atualização", "REQUIRED_ID")
                ));
            } else if (loja.getId() <= 0) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("id", "ID deve ser positivo", "INVALID_ID", loja.getId())
                ));
            }
            
            // Aplica as mesmas validações de criação
            ValidationResult creationResult = forCreation().validate(loja);
            result = ValidationResult.combine(result, creationResult);
            
            return result;
        };
    }
    
    public static Validator<Loja> forStatusChange() {
        return (Loja loja) -> {
            ValidationResult result = ValidationResult.valid();
            
            // Para mudança de status, ID e status são obrigatórios
            if (loja.getId() == null) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("id", "ID é obrigatório para mudança de status", "REQUIRED_ID")
                ));
            }
            
            if (loja.getStatus() == null || loja.getStatus().trim().isEmpty()) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("status", "Status é obrigatório", "REQUIRED_STATUS")
                ));
            } else {
                String status = loja.getStatus().trim().toUpperCase();
                if (!status.matches("^(ATIVA|INATIVA|SUSPENSA)$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("status", "Status deve ser ATIVA, INATIVA ou SUSPENSA", "INVALID_STATUS", status)
                    ));
                }
            }
            
            return result;
        };
    }
    
    public static Validator<Loja> forSearch() {
        return (Loja loja) -> {
            ValidationResult result = ValidationResult.valid();
            
            // Para busca, apenas valida se há critérios de busca válidos
            boolean hasCriteria = false;
            
            if (loja.getNome() != null && !loja.getNome().trim().isEmpty()) {
                hasCriteria = true;
                String nome = loja.getNome().trim();
                if (nome.length() < 2) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("nome", "Nome para busca deve ter pelo menos 2 caracteres", "MIN_LENGTH_SEARCH_NAME", nome)
                    ));
                }
            }
            
            if (loja.getNumero() != null && !loja.getNumero().trim().isEmpty()) {
                hasCriteria = true;
            }
            
            if (loja.getCidade() != null && !loja.getCidade().trim().isEmpty()) {
                hasCriteria = true;
            }
            
            if (loja.getEstado() != null && !loja.getEstado().trim().isEmpty()) {
                hasCriteria = true;
                String estado = loja.getEstado().trim();
                if (!estado.matches("^[A-Z]{2}$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("estado", "Estado para busca deve ser a sigla com 2 letras maiúsculas", "INVALID_SEARCH_STATE", estado)
                    ));
                }
            }
            
            if (loja.getStatus() != null && !loja.getStatus().trim().isEmpty()) {
                hasCriteria = true;
                String status = loja.getStatus().trim().toUpperCase();
                if (!status.matches("^(ATIVA|INATIVA|SUSPENSA)$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("status", "Status para busca deve ser ATIVA, INATIVA ou SUSPENSA", "INVALID_SEARCH_STATUS", status)
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
}
