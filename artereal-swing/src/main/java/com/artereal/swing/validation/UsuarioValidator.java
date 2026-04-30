package com.artereal.swing.validation;

import com.artereal.swing.model.Usuario;

/**
 * Validador específico para o modelo Usuario
 */
public class UsuarioValidator {
    
    public static Validator<Usuario> forCreation() {
        return (Usuario usuario) -> {
            ValidationResult result = ValidationResult.valid();
            
            // Validação do nome (obrigatório)
            if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("nome", "Nome é obrigatório", "REQUIRED_NAME")
                ));
            } else {
                String nome = usuario.getNome().trim();
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
            
            // Validação da senha
            if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("senha", "Senha é obrigatória", "REQUIRED_PASSWORD")
                ));
            } else if (usuario.getSenha().length() < 6) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("senha", "Senha deve ter pelo menos 6 caracteres", "MIN_LENGTH_PASSWORD")
                ));
            } else if (usuario.getSenha().length() > 128) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("senha", "Senha deve ter no máximo 128 caracteres", "MAX_LENGTH_PASSWORD")
                ));
            } else if (usuario.getSenha().contains(usuario.getNome())) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("senha", "Senha não pode conter o nome do usuário", "PASSWORD_CONTAINS_NAME")
                ));
            }
            
            // Validação do acesso (se fornecido)
            if (usuario.getAcesso() != null && !usuario.getAcesso().trim().isEmpty()) {
                if (!usuario.getAcesso().matches("^[A-Z_]{3,20}$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("acesso", "Acesso deve conter apenas letras maiúsculas e underscores", "INVALID_ACCESS", usuario.getAcesso())
                    ));
                }
            }
            
            // Validação do diretório de serviço (se fornecido)
            if (usuario.getDiretorioServico() != null && !usuario.getDiretorioServico().trim().isEmpty()) {
                if (!usuario.getDiretorioServico().matches("^[a-zA-Z0-9/\\\\_\\-\\.]{1,255}$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("diretorioServico", "Diretório de serviço inválido", "INVALID_DIRECTORY", usuario.getDiretorioServico())
                    ));
                }
            }
            
            // Validação de permissões (se fornecidas)
            if (usuario.getContas() != null && !usuario.getContas().trim().isEmpty()) {
                if (!usuario.getContas().matches("^[A-Z_]{3,20}$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("contas", "Permissão de contas inválida", "INVALID_PERMISSION", usuario.getContas())
                    ));
                }
            }
            
            if (usuario.getLancamentos() != null && !usuario.getLancamentos().trim().isEmpty()) {
                if (!usuario.getLancamentos().matches("^[A-Z_]{3,20}$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("lancamentos", "Permissão de lançamentos inválida", "INVALID_PERMISSION", usuario.getLancamentos())
                    ));
                }
            }
            
            if (usuario.getClasses() != null && !usuario.getClasses().trim().isEmpty()) {
                if (!usuario.getClasses().matches("^[A-Z_]{3,20}$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("classes", "Permissão de classes inválida", "INVALID_PERMISSION", usuario.getClasses())
                    ));
                }
            }
            
            if (usuario.getDadosUsuario() != null && !usuario.getDadosUsuario().trim().isEmpty()) {
                if (!usuario.getDadosUsuario().matches("^[A-Z_]{3,20}$")) {
                    result = ValidationResult.combine(result, ValidationResult.invalid(
                        new ValidationError("dadosUsuario", "Permissão de dados de usuário inválida", "INVALID_PERMISSION", usuario.getDadosUsuario())
                    ));
                }
            }
            
            return result;
        };
    }
    
    public static Validator<Usuario> forUpdate() {
        return (Usuario usuario) -> {
            ValidationResult result = ValidationResult.valid();
            
            // Para atualização, ID é obrigatório
            if (usuario.getId() == null) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("id", "ID é obrigatório para atualização", "REQUIRED_ID")
                ));
            } else if (usuario.getId() <= 0) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("id", "ID deve ser positivo", "INVALID_ID", usuario.getId())
                ));
            }
            
            // Aplica as mesmas validações de criação, mas nome e senha podem ser opcionais para atualização parcial
            if (usuario.getNome() != null && !usuario.getNome().trim().isEmpty()) {
                ValidationResult nomeValidation = forCreation().validate(usuario);
                if (nomeValidation.isInvalid()) {
                    result = ValidationResult.combine(result, nomeValidation);
                }
            }
            
            if (usuario.getSenha() != null && !usuario.getSenha().trim().isEmpty()) {
                ValidationResult senhaValidation = forCreation().validate(usuario);
                if (senhaValidation.isInvalid()) {
                    result = ValidationResult.combine(result, senhaValidation);
                }
            }
            
            return result;
        };
    }
    
    public static Validator<Usuario> forAuthentication() {
        return (Usuario usuario) -> {
            ValidationResult result = ValidationResult.valid();
            
            // Validação básica para autenticação
            if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("nome", "Nome de usuário é obrigatório", "REQUIRED_USERNAME")
                ));
            }
            
            if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("senha", "Senha é obrigatória", "REQUIRED_PASSWORD")
                ));
            }
            
            return result;
        };
    }
    
    public static Validator<Usuario> forPasswordChange() {
        return (Usuario usuario) -> {
            ValidationResult result = ValidationResult.valid();
            
            // Validação para mudança de senha
            if (usuario.getId() == null) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("id", "ID é obrigatório para mudança de senha", "REQUIRED_ID")
                ));
            }
            
            if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("senha", "Nova senha é obrigatória", "REQUIRED_NEW_PASSWORD")
                ));
            } else if (usuario.getSenha().length() < 8) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("senha", "Nova senha deve ter pelo menos 8 caracteres", "MIN_LENGTH_NEW_PASSWORD")
                ));
            } else if (!usuario.getSenha().matches(".*[A-Z].*")) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("senha", "Nova senha deve conter pelo menos uma letra maiúscula", "NO_UPPERCASE")
                ));
            } else if (!usuario.getSenha().matches(".*[a-z].*")) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("senha", "Nova senha deve conter pelo menos uma letra minúscula", "NO_LOWERCASE")
                ));
            } else if (!usuario.getSenha().matches(".*\\d.*")) {
                result = ValidationResult.combine(result, ValidationResult.invalid(
                    new ValidationError("senha", "Nova senha deve conter pelo menos um número", "NO_DIGIT")
                ));
            }
            
            return result;
        };
    }
    
    public static Validator<String> usernameValidator() {
        return (String username) -> {
            if (username == null || username.trim().isEmpty()) {
                return ValidationResult.invalid(
                    new ValidationError("username", "Nome de usuário é obrigatório", "REQUIRED_USERNAME")
                );
            }
            
            if (username.trim().length() < 3) {
                return ValidationResult.invalid(
                    new ValidationError("username", "Nome de usuário deve ter pelo menos 3 caracteres", "MIN_LENGTH_USERNAME", username)
                );
            }
            
            if (username.trim().length() > 50) {
                return ValidationResult.invalid(
                    new ValidationError("username", "Nome de usuário deve ter no máximo 50 caracteres", "MAX_LENGTH_USERNAME", username)
                );
            }
            
            if (!username.matches("^[a-zA-Z0-9_]{3,50}$")) {
                return ValidationResult.invalid(
                    new ValidationError("username", "Nome de usuário deve conter apenas letras, números e underscores", "INVALID_USERNAME", username)
                );
            }
            
            return ValidationResult.valid();
        };
    }
    
    public static Validator<String> passwordValidator() {
        return (String password) -> {
            if (password == null || password.trim().isEmpty()) {
                return ValidationResult.invalid(
                    new ValidationError("password", "Senha é obrigatória", "REQUIRED_PASSWORD")
                );
            }
            
            if (password.length() < 6) {
                return ValidationResult.invalid(
                    new ValidationError("password", "Senha deve ter pelo menos 6 caracteres", "MIN_LENGTH_PASSWORD")
                );
            }
            
            if (password.length() > 128) {
                return ValidationResult.invalid(
                    new ValidationError("password", "Senha deve ter no máximo 128 caracteres", "MAX_LENGTH_PASSWORD")
                );
            }
            
            return ValidationResult.valid();
        };
    }
}
