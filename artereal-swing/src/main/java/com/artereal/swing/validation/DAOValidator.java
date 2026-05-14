package com.artereal.swing.validation;

import com.artereal.swing.model.SimpleModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Validador automático para operações de DAO
 */
public class DAOValidator {
    
    /**
     * Valida antes de salvar um modelo
     */
    public static ValidationResult validateBeforeSave(SimpleModel model, Connection conn) throws SQLException {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        // Validação básica do modelo
        ModelValidator.ValidationResult basicValidation = ModelValidator.validate(model);
        errors.addAll(basicValidation.getErrors());
        warnings.addAll(basicValidation.getWarnings());
        
        // Validação de duplicidade (se tiver ID)
        if (model != null && model.getId() != null) {
            if (existsById(model, conn)) {
                warnings.add("Registro com ID " + model.getId() + " já existe. Será atualizado.");
            }
        }
        
        return new ValidationResult(errors, warnings);
    }
    
    /**
     * Valida antes de atualizar um modelo
     */
    public static ValidationResult validateBeforeUpdate(SimpleModel model, Connection conn) throws SQLException {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        // Validação básica do modelo
        ModelValidator.ValidationResult basicValidation = ModelValidator.validate(model);
        errors.addAll(basicValidation.getErrors());
        warnings.addAll(basicValidation.getWarnings());
        
        // Validação de existência
        if (model != null) {
            if (model.getId() == null) {
                errors.add("ID é obrigatório para atualização");
            } else {
                String tableName = getTableName(model.getClass().getSimpleName(), conn);
                if (!existsById(model.getId(), tableName, conn)) {
                    errors.add("Registro com ID " + model.getId() + " não encontrado");
                }
            }
        }
        
        return new ValidationResult(errors, warnings);
    }
    
    /**
     * Valida antes de deletar um modelo
     */
    public static ValidationResult validateBeforeDelete(Long id, Connection conn) throws SQLException {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        if (id == null) {
            errors.add("ID é obrigatório para exclusão");
        } else {
            // Para delete, precisamos de uma tabela padrão ou passar como parâmetro
            // Por enquanto, apenas verifica se o ID é válido
            if (id <= 0) {
                errors.add("ID deve ser positivo");
            }
        }
        
        return new ValidationResult(errors, warnings);
    }
    
    /**
     * Verifica se registro existe por ID
     */
    private static boolean existsById(SimpleModel model, Connection conn) throws SQLException {
        if (model.getId() == null) return false;
        
        String tableName = getTableName(model.getClass().getSimpleName(), conn);
        return existsById(model.getId(), tableName, conn);
    }
    
    /**
     * Verifica se registro existe por ID e nome da tabela
     */
    private static boolean existsById(Long id, String tableName, Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        
        return false;
    }
    
    /**
     * Obtém nome da tabela baseado no nome da classe
     */
    private static String getTableName(String className, Connection conn) throws SQLException {
        // Converte nome da classe para nome da tabela
        String tableName = className.toLowerCase();
        
        // Casos especiais
        switch (className) {
            case "Irmao":
                return "irmao";
            case "Usuario":
                return "usuario";
            case "Despesa":
                return "despesas";
            case "Configuracao":
                return "configuracao";
            case "Sessao":
                return "sessao";
            case "Loja":
                return "loja";
            case "Cheque":
                return "cheque";
            case "Documento":
                return "documento";
            case "Foto":
                return "foto";
            case "Frequencia":
                return "frequencia";
            case "Visitante":
                return "visitante";
            case "Caixa":
                return "caixa";
            case "Candidato":
                return "candidato";
            case "Afastamento":
                return "afastamento";
            case "Biblioteca":
                return "biblioteca";
            default:
                return tableName;
        }
    }
    
    /**
     * Valida SQL injection
     */
    public static boolean isSafeSQL(String sql) {
        if (sql == null) return false;
        
        // Verifica padrões básicos de SQL injection
        String[] dangerousPatterns = {
            "'", "\"", ";", "--", "/*", "*/", "xp_", "sp_",
            "drop ", "delete ", "truncate ", "insert ", "update ",
            "exec ", "execute ", "union ", "select "
        };
        
        String lowerSql = sql.toLowerCase();
        for (String pattern : dangerousPatterns) {
            if (lowerSql.contains(pattern)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Classe para armazenar resultado da validação
     */
    public static class ValidationResult {
        private final List<String> errors;
        private final List<String> warnings;
        
        public ValidationResult(List<String> errors, List<String> warnings) {
            this.errors = new ArrayList<>(errors);
            this.warnings = new ArrayList<>(warnings);
        }
        
        public boolean isValid() {
            return errors.isEmpty();
        }
        
        public boolean hasWarnings() {
            return !warnings.isEmpty();
        }
        
        public List<String> getErrors() {
            return new ArrayList<>(errors);
        }
        
        public List<String> getWarnings() {
            return new ArrayList<>(warnings);
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (!errors.isEmpty()) {
                sb.append("Errors: ").append(String.join(", ", errors));
            }
            if (!warnings.isEmpty()) {
                if (sb.length() > 0) sb.append(" | ");
                sb.append("Warnings: ").append(String.join(", ", warnings));
            }
            if (sb.length() == 0) {
                sb.append("Valid");
            }
            return sb.toString();
        }
    }
}
