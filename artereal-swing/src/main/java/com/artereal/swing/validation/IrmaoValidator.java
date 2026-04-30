package com.artereal.swing.validation;

import com.artereal.swing.model.Irmao;
import java.time.LocalDate;

/**
 * Validador específico para o modelo Irmao
 */
public class IrmaoValidator {
    
    public static Validator<Irmao> forCreation() {
        return (Irmao irmao) -> {
            ValidationResult result = ValidationResult.valid();
            
            // Validação do nome (obrigatório)
            if (irmao.getNome() == null || irmao.getNome().trim().isEmpty()) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("nome", "Nome do irmão é obrigatório", "REQUIRED_NAME")
                ));
            } else {
                String nome = irmao.getNome().trim();
                if (nome.length() < 3) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("nome", "Nome deve ter pelo menos 3 caracteres", "MIN_LENGTH_NAME", nome)
                    ));
                } else if (nome.length() > 100) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("nome", "Nome deve ter no máximo 100 caracteres", "MAX_LENGTH_NAME", nome)
                    ));
                } else if (!nome.matches("^[A-Za-zÀ-ú\\s]{3,100}$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("nome", "Nome deve conter apenas letras e espaços", "INVALID_NAME", nome)
                    ));
                }
            }
            
            // Validação da data de nascimento (obrigatória)
            if (irmao.getNascimento() == null) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("nascimento", "Data de nascimento é obrigatória", "REQUIRED_BIRTH_DATE")
                ));
            } else {
                LocalDate nascimento = irmao.getNascimento();
                LocalDate hoje = LocalDate.now();
                
                if (nascimento.isAfter(hoje)) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("nascimento", "Data de nascimento não pode ser futura", "FUTURE_BIRTH_DATE", nascimento)
                    ));
                } else if (nascimento.isBefore(hoje.minusYears(120))) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("nascimento", "Data de nascimento muito antiga (mais de 120 anos)", "TOO_OLD_BIRTH_DATE", nascimento)
                    ));
                } else if (nascimento.isAfter(hoje.minusYears(18))) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("nascimento", "Irmão deve ter pelo menos 18 anos", "UNDERAGE", nascimento)
                    ));
                }
            }
            
            // Validação do estado civil (se fornecido)
            if (irmao.getEstadoCivil() != null && !irmao.getEstadoCivil().trim().isEmpty()) {
                String estadoCivil = irmao.getEstadoCivil().trim();
                if (estadoCivil.length() > 50) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("estadoCivil", "Estado civil deve ter no máximo 50 caracteres", "MAX_LENGTH_CIVIL_STATUS", estadoCivil)
                    ));
                } else if (!estadoCivil.matches("^[A-Za-zÀ-ú\\s]{2,50}$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("estadoCivil", "Estado civil deve conter apenas letras e espaços", "INVALID_CIVIL_STATUS", estadoCivil)
                    ));
                }
            }
            
            // Validação da naturalidade (se fornecida)
            if (irmao.getNatural() != null && !irmao.getNatural().trim().isEmpty()) {
                String natural = irmao.getNatural().trim();
                if (natural.length() > 100) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("natural", "Naturalidade deve ter no máximo 100 caracteres", "MAX_LENGTH_NATURAL", natural)
                    ));
                } else if (!natural.matches("^[A-Za-zÀ-ú\\s\\-]{2,100}$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("natural", "Naturalidade deve conter apenas letras, espaços e hífens", "INVALID_NATURAL", natural)
                    ));
                }
            }
            
            // Validação da identidade (se fornecida)
            if (irmao.getIdentidade() != null && !irmao.getIdentidade().trim().isEmpty()) {
                String identidade = irmao.getIdentidade().trim();
                if (identidade.length() > 30) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("identidade", "Identidade deve ter no máximo 30 caracteres", "MAX_LENGTH_IDENTITY", identidade)
                    ));
                } else if (!identidade.matches("^[A-Za-z0-9\\-\\.]{5,30}$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("identidade", "Identidade inválida", "INVALID_IDENTITY", identidade)
                    ));
                }
            }
            
            // Validação do tipo sanguíneo (se fornecido)
            if (irmao.getTipoSanguineo() != null && !irmao.getTipoSanguineo().trim().isEmpty()) {
                String tipoSanguineo = irmao.getTipoSanguineo().trim().toUpperCase();
                if (!tipoSanguineo.matches("^(A|B|AB|O)[\\+\\-]$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("tipoSanguineo", "Tipo sanguíneo inválido. Use formatos: A+, A-, B+, B-, AB+, AB-, O+, O-", "INVALID_BLOOD_TYPE", tipoSanguineo)
                    ));
                }
            }
            
            // Validação do cargo na loja (se fornecido)
            if (irmao.getCargoLoja() != null && !irmao.getCargoLoja().trim().isEmpty()) {
                String cargoLoja = irmao.getCargoLoja().trim();
                if (cargoLoja.length() > 50) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("cargoLoja", "Cargo na loja deve ter no máximo 50 caracteres", "MAX_LENGTH_CARGO_LOJA", cargoLoja)
                    ));
                } else if (!cargoLoja.matches("^[A-Za-zÀ-ú\\s]{2,50}$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("cargoLoja", "Cargo na loja deve conter apenas letras e espaços", "INVALID_CARGO_LOJA", cargoLoja)
                    ));
                }
            }
            
            // Validação do grau (se fornecido)
            if (irmao.getGrau() != null && !irmao.getGrau().trim().isEmpty()) {
                String grau = irmao.getGrau().trim();
                if (!grau.matches("^[A-Za-z0-9\\s]{1,20}$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("grau", "Grau maçônico inválido", "INVALID_DEGREE", grau)
                    ));
                }
            }
            
            // Validação do cargo na grande loja (se fornecido)
            if (irmao.getCargoGrandeLoja() != null && !irmao.getCargoGrandeLoja().trim().isEmpty()) {
                String cargoGrandeLoja = irmao.getCargoGrandeLoja().trim();
                if (cargoGrandeLoja.length() > 50) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("cargoGrandeLoja", "Cargo na grande loja deve ter no máximo 50 caracteres", "MAX_LENGTH_CARGO_GRANDE_LOJA", cargoGrandeLoja)
                    ));
                } else if (!cargoGrandeLoja.matches("^[A-Za-zÀ-ú\\s]{2,50}$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("cargoGrandeLoja", "Cargo na grande loja deve conter apenas letras e espaços", "INVALID_CARGO_GRANDE_LOJA", cargoGrandeLoja)
                    ));
                }
            }
            
            // Validação do endereço (se fornecido)
            if (irmao.getEndereco() != null && !irmao.getEndereco().trim().isEmpty()) {
                String endereco = irmao.getEndereco().trim();
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
            
            // Validação do bairro (se fornecido)
            if (irmao.getBairro() != null && !irmao.getBairro().trim().isEmpty()) {
                String bairro = irmao.getBairro().trim();
                if (bairro.length() > 100) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("bairro", "Bairro deve ter no máximo 100 caracteres", "MAX_LENGTH_BAIRRO", bairro)
                    ));
                } else if (!bairro.matches("^[A-Za-zÀ-ú0-9\\s]{2,100}$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("bairro", "Bairro deve conter apenas letras, números e espaços", "INVALID_BAIRRO", bairro)
                    ));
                }
            }
            
            // Validação da cidade (se fornecida)
            if (irmao.getCidade() != null && !irmao.getCidade().trim().isEmpty()) {
                String cidade = irmao.getCidade().trim();
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
            
            // Validação do estado (se fornecido)
            if (irmao.getEstado() != null && !irmao.getEstado().trim().isEmpty()) {
                String estado = irmao.getEstado().trim();
                if (!estado.matches("^[A-Z]{2}$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("estado", "Estado deve ser a sigla com 2 letras maiúsculas (ex: SP, RJ)", "INVALID_STATE", estado)
                    ));
                }
            }
            
            // Validação do telefone (se fornecido)
            if (irmao.getTelefone() != null && !irmao.getTelefone().trim().isEmpty()) {
                String telefone = irmao.getTelefone().trim();
                if (!telefone.matches("^\\(?([0-9]{2})\\)? ?([0-9]{4,5})-?([0-9]{4})$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("telefone", "Telefone inválido. Use o formato (XX) XXXX-XXXX ou XXXX-XXXX", "INVALID_PHONE", telefone)
                    ));
                }
            }
            
            // Validação da empresa (se fornecida)
            if (irmao.getEmpresa() != null && !irmao.getEmpresa().trim().isEmpty()) {
                String empresa = irmao.getEmpresa().trim();
                if (empresa.length() > 100) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("empresa", "Empresa deve ter no máximo 100 caracteres", "MAX_LENGTH_COMPANY", empresa)
                    ));
                }
            }
            
            // Validação do telefone da empresa (se fornecido)
            if (irmao.getTelefoneEmpresa() != null && !irmao.getTelefoneEmpresa().trim().isEmpty()) {
                String telefoneEmpresa = irmao.getTelefoneEmpresa().trim();
                if (!telefoneEmpresa.matches("^\\(?([0-9]{2})\\)? ?([0-9]{4,5})-?([0-9]{4})$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("telefoneEmpresa", "Telefone da empresa inválido. Use o formato (XX) XXXX-XXXX", "INVALID_COMPANY_PHONE", telefoneEmpresa)
                    ));
                }
            }
            
            // Validação do endereço da empresa (se fornecido)
            if (irmao.getEnderecoEmpresa() != null && !irmao.getEnderecoEmpresa().trim().isEmpty()) {
                String enderecoEmpresa = irmao.getEnderecoEmpresa().trim();
                if (enderecoEmpresa.length() > 200) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("enderecoEmpresa", "Endereço da empresa deve ter no máximo 200 caracteres", "MAX_LENGTH_COMPANY_ADDRESS", enderecoEmpresa)
                    ));
                }
            }
            
            // Validação do registro na grande loja (se fornecido)
            if (irmao.getRegistroGrandeLoja() != null && !irmao.getRegistroGrandeLoja().trim().isEmpty()) {
                String registroGrandeLoja = irmao.getRegistroGrandeLoja().trim();
                if (registroGrandeLoja.length() > 50) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("registroGrandeLoja", "Registro na grande loja deve ter no máximo 50 caracteres", "MAX_LENGTH_REGISTRATION", registroGrandeLoja)
                    ));
                } else if (!registroGrandeLoja.matches("^[A-Za-z0-9\\-\\/]{1,50}$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("registroGrandeLoja", "Registro na grande loja inválido", "INVALID_REGISTRATION", registroGrandeLoja)
                    ));
                }
            }
            
            return result;
        };
    }
    
    public static Validator<Irmao> forUpdate() {
        return (Irmao irmao) -> {
            ValidationResult result = ValidationResult.valid();
            
            // Para atualização, ID é obrigatório
            if (irmao.getId() == null) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("id", "ID é obrigatório para atualização", "REQUIRED_ID")
                ));
            } else if (irmao.getId() <= 0) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("id", "ID deve ser positivo", "INVALID_ID", irmao.getId())
                ));
            }
            
            // Aplica as mesmas validações de criação
            ValidationResult creationResult = forCreation().validate(irmao);
            result = ValidationResult.combine(result, creationResult);
            
            return result;
        };
    }
    
    public static Validator<Irmao> forSearch() {
        return (Irmao irmao) -> {
            ValidationResult result = ValidationResult.valid();
            
            // Para busca, apenas valida se há critérios de busca válidos
            boolean hasCriteria = false;
            
            if (irmao.getNome() != null && !irmao.getNome().trim().isEmpty()) {
                hasCriteria = true;
                String nome = irmao.getNome().trim();
                if (nome.length() < 2) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("nome", "Nome para busca deve ter pelo menos 2 caracteres", "MIN_LENGTH_SEARCH_NAME", nome)
                    ));
                }
            }
            
            if (irmao.getGrau() != null && !irmao.getGrau().trim().isEmpty()) {
                hasCriteria = true;
            }
            
            if (irmao.getCargoLoja() != null && !irmao.getCargoLoja().trim().isEmpty()) {
                hasCriteria = true;
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
