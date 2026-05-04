package com.artereal.swing.security;

import java.util.regex.Pattern;
import java.util.List;
import java.util.Arrays;

/**
 * Validador de entrada com foco em dados específicos do sistema ArteReal
 */
public class InputValidator {
    
    
    // Padrões específicos para dados do ArteReal
    private static final Pattern NOME_PATTERN = Pattern.compile("^[a-zA-ZÀ-ÿ\\s]{2,100}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    private static final Pattern TELEFONE_PATTERN = Pattern.compile("^[0-9]{10,15}$");
    private static final Pattern CEP_PATTERN = Pattern.compile("^[0-9]{5}-[0-9]{3}$");
    private static final Pattern CPF_PATTERN = Pattern.compile("^[0-9]{3}\\.[0-9]{3}\\.[0-9]{3}-[0-9]{2}$");
    private static final Pattern RG_PATTERN = Pattern.compile("^[0-9]{6,15}$");
    private static final Pattern DATA_PATTERN = Pattern.compile("^[0-9]{2}/[0-9]{2}/[0-9]{4}$");
    private static final Pattern VALOR_PATTERN = Pattern.compile("^\\d+(\\.\\d{1,2})?$");
    private static final Pattern GRAU_PATTERN = Pattern.compile("^(APRENDIZ|COMPANHEIRO|MESTRE)$");
    private static final Pattern CARGO_PATTERN = Pattern.compile("^[a-zA-ZÀ-ÿ\\s]{2,50}$");
    private static final Pattern DESCRICAO_PATTERN = Pattern.compile("^[a-zA-Z0-9À-ÿ\\s.,!?-]{1,500}$");
    private static final Pattern TIPO_SANGUINEO_PATTERN = Pattern.compile("^(A|B|AB|O)[+-]?$");
    
    // Lista de palavras proibidas para nomes e descrições
    private static final List<String> PALAVRAS_PROIBIDAS = Arrays.asList(
        "delete", "drop", "truncate", "update", "insert", "select", "union",
        "script", "javascript", "vbscript", "iframe", "object", "embed",
        "alert", "confirm", "prompt", "eval", "exec", "system",
        "passwd", "password", "secret", "token", "key", "admin",
        "root", "administrator", "sudo", "su", "bash", "sh",
        "cmd", "powershell", "malware", "virus", "trojan", "backdoor"
    );
    
    /**
     * Valida nome de irmão
     */
    public static ValidationResult validateNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return new ValidationResult(false, "Nome não pode ser vazio");
        }
        
        nome = nome.trim();
        
        // Verifica padrão
        if (!NOME_PATTERN.matcher(nome).matches()) {
            return new ValidationResult(false, "Nome deve conter apenas letras e espaços (2-100 caracteres)");
        }
        
        // Verifica palavras proibidas
        if (containsPalavrasProibidas(nome)) {
            return new ValidationResult(false, "Nome contém palavras não permitidas");
        }
        
        // Verifica se é muito longo
        if (nome.length() > 100) {
            return new ValidationResult(false, "Nome muito longo");
        }
        
        // Verifica se é muito curto
        if (nome.length() < 2) {
            return new ValidationResult(false, "Nome muito curto");
        }
        
        return new ValidationResult(true, "Nome válido");
    }
    
    /**
     * Valida email
     */
    public static ValidationResult validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return new ValidationResult(false, "Email não pode ser vazio");
        }
        
        email = email.trim().toLowerCase();
        
        // Verifica padrão
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return new ValidationResult(false, "Email inválido");
        }
        
        // Verifica domínios suspeitos
        if (isDominioSuspeito(email)) {
            return new ValidationResult(false, "Email de domínio suspeito");
        }
        
        return new ValidationResult(true, "Email válido");
    }
    
    /**
     * Valida telefone
     */
    public static ValidationResult validateTelefone(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            return new ValidationResult(false, "Telefone não pode ser vazio");
        }
        
        // Remove caracteres não numéricos
        String telefoneLimpo = telefone.replaceAll("[^0-9]", "");
        
        if (!TELEFONE_PATTERN.matcher(telefoneLimpo).matches()) {
            return new ValidationResult(false, "Telefone deve conter 10-15 dígitos");
        }
        
        return new ValidationResult(true, "Telefone válido");
    }
    
    /**
     * Valida CEP
     */
    public static ValidationResult validateCEP(String cep) {
        if (cep == null || cep.trim().isEmpty()) {
            return new ValidationResult(false, "CEP não pode ser vazio");
        }
        
        if (!CEP_PATTERN.matcher(cep).matches()) {
            return new ValidationResult(false, "CEP inválido (formato: 00000-000)");
        }
        
        return new ValidationResult(true, "CEP válido");
    }
    
    /**
     * Valida CPF
     */
    public static ValidationResult validateCPF(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return new ValidationResult(false, "CPF não pode ser vazio");
        }
        
        if (!CPF_PATTERN.matcher(cpf).matches()) {
            return new ValidationResult(false, "CPF inválido (formato: 000.000.000-00)");
        }
        
        // Verifica se é um CPF válido (algoritmo)
        if (!isValidCPFAlgorithm(cpf)) {
            return new ValidationResult(false, "CPF inválido");
        }
        
        return new ValidationResult(true, "CPF válido");
    }
    
    /**
     * Valida RG
     */
    public static ValidationResult validateRG(String rg) {
        if (rg == null || rg.trim().isEmpty()) {
            return new ValidationResult(false, "RG não pode ser vazio");
        }
        
        String rgLimpo = rg.replaceAll("[^0-9]", "");
        
        if (!RG_PATTERN.matcher(rgLimpo).matches()) {
            return new ValidationResult(false, "RG deve conter 6-15 dígitos");
        }
        
        return new ValidationResult(true, "RG válido");
    }
    
    /**
     * Valida data
     */
    public static ValidationResult validateData(String data) {
        if (data == null || data.trim().isEmpty()) {
            return new ValidationResult(false, "Data não pode ser vazia");
        }
        
        if (!DATA_PATTERN.matcher(data).matches()) {
            return new ValidationResult(false, "Data inválida (formato: dd/MM/yyyy)");
        }
        
        // Verifica se a data é válida
        if (!isValidDate(data)) {
            return new ValidationResult(false, "Data inválida");
        }
        
        return new ValidationResult(true, "Data válida");
    }
    
    /**
     * Valida valor monetário
     */
    public static ValidationResult validateValor(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return new ValidationResult(false, "Valor não pode ser vazio");
        }
        
        if (!VALOR_PATTERN.matcher(valor).matches()) {
            return new ValidationResult(false, "Valor inválido");
        }
        
        double valorNum = Double.parseDouble(valor);
        if (valorNum < 0) {
            return new ValidationResult(false, "Valor não pode ser negativo");
        }
        
        if (valorNum > 999999.99) {
            return new ValidationResult(false, "Valor muito alto");
        }
        
        return new ValidationResult(true, "Valor válido");
    }
    
    /**
     * Valida grau maçônico
     */
    public static ValidationResult validateGrau(String grau) {
        if (grau == null || grau.trim().isEmpty()) {
            return new ValidationResult(false, "Grau não pode ser vazio");
        }
        
        if (!GRAU_PATTERN.matcher(grau).matches()) {
            return new ValidationResult(false, "Grau deve ser: APRENDIZ, COMPANHEIRO ou MESTRE");
        }
        
        return new ValidationResult(true, "Grau válido");
    }
    
    /**
     * Valida cargo
     */
    public static ValidationResult validateCargo(String cargo) {
        if (cargo == null || cargo.trim().isEmpty()) {
            return new ValidationResult(false, "Cargo não pode ser vazio");
        }
        
        cargo = cargo.trim();
        
        if (!CARGO_PATTERN.matcher(cargo).matches()) {
            return new ValidationResult(false, "Cargo deve conter apenas letras e espaços (2-50 caracteres)");
        }
        
        if (containsPalavrasProibidas(cargo)) {
            return new ValidationResult(false, "Cargo contém palavras não permitidas");
        }
        
        return new ValidationResult(true, "Cargo válido");
    }
    
    /**
     * Valida descrição
     */
    public static ValidationResult validateDescricao(String descricao) {
        if (descricao == null || descricao.trim().isEmpty()) {
            return new ValidationResult(false, "Descrição não pode ser vazia");
        }
        
        descricao = descricao.trim();
        
        if (!DESCRICAO_PATTERN.matcher(descricao).matches()) {
            return new ValidationResult(false, "Descrição contém caracteres inválidos");
        }
        
        if (containsPalavrasProibidas(descricao)) {
            return new ValidationResult(false, "Descrição contém palavras não permitidas");
        }
        
        return new ValidationResult(true, "Descrição válida");
    }
    
    /**
     * Valida tipo sanguíneo
     */
    public static ValidationResult validateTipoSanguineo(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            return new ValidationResult(false, "Tipo sanguíneo não pode ser vazio");
        }
        
        if (!TIPO_SANGUINEO_PATTERN.matcher(tipo).matches()) {
            return new ValidationResult(false, "Tipo sanguíneo inválido");
        }
        
        return new ValidationResult(true, "Tipo sanguíneo válido");
    }
    
    /**
     * Verifica se contém palavras proibidas
     */
    private static boolean containsPalavrasProibidas(String input) {
        String lowerInput = input.toLowerCase();
        for (String palavra : PALAVRAS_PROIBIDAS) {
            if (lowerInput.contains(palavra)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Verifica se domínio é suspeito
     */
    private static boolean isDominioSuspeito(String email) {
        String dominio = email.substring(email.indexOf("@") + 1);
        
        // Lista de domínios temporários/suspeitos
        List<String> dominiosSuspeitos = Arrays.asList(
            "10minutemail.com", "tempmail.org", "guerrillamail.com",
            "mailinator.com", "yopmail.com", "temp-mail.org"
        );
        
        return dominiosSuspeitos.contains(dominio);
    }
    
    /**
     * Verifica se CPF é válido usando algoritmo
     */
    private static boolean isValidCPFAlgorithm(String cpf) {
        // Remove caracteres não numéricos
        String cpfNumeros = cpf.replaceAll("[^0-9]", "");
        
        // Verifica se todos os dígitos são iguais
        if (cpfNumeros.chars().distinct().count() == 1) {
            return false;
        }
        
        // Calcula dígitos verificadores
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpfNumeros.charAt(i)) * (10 - i);
        }
        
        int resto = soma % 11;
        int digito1 = resto < 2 ? 0 : 11 - resto;
        
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(cpfNumeros.charAt(i)) * (11 - i);
        }
        
        resto = soma % 11;
        int digito2 = resto < 2 ? 0 : 11 - resto;
        
        return Character.getNumericValue(cpfNumeros.charAt(9)) == digito1 &&
               Character.getNumericValue(cpfNumeros.charAt(10)) == digito2;
    }
    
    /**
     * Verifica se data é válida
     */
    private static boolean isValidDate(String data) {
        try {
            String[] partes = data.split("/");
            int dia = Integer.parseInt(partes[0]);
            int mes = Integer.parseInt(partes[1]);
            int ano = Integer.parseInt(partes[2]);
            
            if (mes < 1 || mes > 12) return false;
            if (dia < 1 || dia > 31) return false;
            if (ano < 1900 || ano > 2100) return false;
            
            // Verifica meses com 30 dias
            if ((mes == 4 || mes == 6 || mes == 9 || mes == 11) && dia > 30) {
                return false;
            }
            
            // Verifica fevereiro
            if (mes == 2) {
                boolean bissexto = (ano % 4 == 0 && ano % 100 != 0) || (ano % 400 == 0);
                if (dia > (bissexto ? 29 : 28)) {
                    return false;
                }
            }
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Resultado da validação
     */
    public static class ValidationResult {
        private final boolean valid;
        private final String message;
        
        public ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }
        
        public boolean isValid() {
            return valid;
        }
        
        public String getMessage() {
            return message;
        }
        
        @Override
        public String toString() {
            return valid ? "VALID: " + message : "INVALID: " + message;
        }
    }
}
