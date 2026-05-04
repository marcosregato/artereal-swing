package com.artereal.swing.security;

import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Gerenciador de segurança avançado para proteção contra dados maliciosos
 */
public class SecurityManager {
    
    private static final Logger logger = LoggerFactory.getLogger(SecurityManager.class);
    
    // Padrões maliciosos conhecidos
    
    private static final List<Pattern> XSS_PATTERNS = Arrays.asList(
        Pattern.compile("(?i)(<script[^>]*>.*?</script>)"),
        Pattern.compile("(?i)(<iframe[^>]*>.*?</iframe>)"),
        Pattern.compile("(?i)(<object[^>]*>.*?</object>)"),
        Pattern.compile("(?i)(<embed[^>]*>.*?</embed>)"),
        Pattern.compile("(?i)(javascript\\s*:)"),
        Pattern.compile("(?i)(vbscript\\s*:)"),
        Pattern.compile("(?i)(onload\\s*=)"),
        Pattern.compile("(?i)(onerror\\s*=)"),
        Pattern.compile("(?i)(onclick\\s*=)"),
        Pattern.compile("(?i)(onmouseover\\s*=)"),
        Pattern.compile("(?i)(onfocus\\s*=)"),
        Pattern.compile("(?i)(onblur\\s*=)"),
        Pattern.compile("(?i)(onchange\\s*=)"),
        Pattern.compile("(?i)(onsubmit\\s*=)"),
        Pattern.compile("(?i)(<img[^>]*src[^>]*javascript)"),
        Pattern.compile("(?i)(<link[^>]*href[^>]*javascript)"),
        Pattern.compile("(?i)(<meta[^>]*http-equiv[^>]*refresh)"),
        Pattern.compile("(?i)(<body[^>]*onload)"),
        Pattern.compile("(?i)(<div[^>]*onload)"),
        Pattern.compile("(?i)(<span[^>]*onload)"),
        Pattern.compile("(?i)(expression\\s*\\()")
    );
    
    private static final List<Pattern> PATH_TRAVERSAL_PATTERNS = Arrays.asList(
        Pattern.compile("(?i)(\\.\\./)"),
        Pattern.compile("(?i)(\\.\\.\\\\)"),
        Pattern.compile("(?i)(/etc/passwd)"),
        Pattern.compile("(?i)(/windows/system32)"),
        Pattern.compile("(?i)(%2e%2e%2f)"),
        Pattern.compile("(?i)(%2e%2e%5c)"),
        Pattern.compile("(?i)(\\.\\.%2f)"),
        Pattern.compile("(?i)(\\.\\.%5c)"),
        Pattern.compile("(?i)(%c0%af)"),
        Pattern.compile("(?i)(%c1%9c)"),
        Pattern.compile("(?i)(/proc/self/environ)"),
        Pattern.compile("(?i)(/proc/version)"),
        Pattern.compile("(?i)(/proc/cmdline)")
    );
    
    private static final List<Pattern> BUFFER_OVERFLOW_PATTERNS = Arrays.asList(
        Pattern.compile("A{1500,}"), // Muitos caracteres 'A' (ajustado para detectar 2000)
        Pattern.compile("\\x90{800,}"), // Muitos NOP sleds (ajustado)
        Pattern.compile("\\x41{1500,}"), // Muitos caracteres hexadecimais (ajustado)
        Pattern.compile("\\u9090{800,}"), // Unicode NOP sleds (ajustado)
        Pattern.compile("[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F]{150,}") // Caracteres de controle (ajustado)
    );
    
    private static final List<Pattern> COMMAND_INJECTION_PATTERNS = Arrays.asList(
        Pattern.compile("(?i)(\\s*;\\s*)"),
        Pattern.compile("(?i)(\\s*&&\\s*)"),
        Pattern.compile("(?i)(\\s*\\|\\|\\s*)"),
        Pattern.compile("(?i)(\\s*\\|\\s*\\w+)"),
        Pattern.compile("(?i)(\\s*&\\s*\\w+)"),
        Pattern.compile("(?i)(\\s*>\\s*)"),
        Pattern.compile("(?i)(\\s*>>\\s*)"),
        Pattern.compile("(?i)(\\s*<\\s*)"),
        Pattern.compile("(?i)(\\s*>>\\s*)"),
        Pattern.compile("(?i)(\\s*\\$\\s*\\()"),
        Pattern.compile("(?i)(\\s*`\\s*)"),
        Pattern.compile("(?i)(\\s*\\$\\{)"),
        Pattern.compile("(?i)(\\s*\\|\\s*grep)"),
        Pattern.compile("(?i)(\\s*\\|\\s*cat)"),
        Pattern.compile("(?i)(\\s*\\|\\s*ls)"),
        Pattern.compile("(?i)(\\s*\\|\\s*dir)"),
        Pattern.compile("(?i)(\\s*&&\\s*rm)"),
        Pattern.compile("(?i)(\\s*;\\s*rm)"),
        Pattern.compile("(?i)(\\s*&&\\s*del)"),
        Pattern.compile("(?i)(\\s*;\\s*del)")
    );
    
    /**
     * Verifica se uma string contém conteúdo malicioso
     */
    public static SecurityScanResult scanForThreats(String input) {
        if (input == null || input.trim().isEmpty()) {
            return new SecurityScanResult(true, "Input vazio ou nulo", new ArrayList<>());
        }
        
        List<String> threats = new ArrayList<>();
        
        // Normaliza input para análise
        String normalizedInput = normalizeInput(input);
        
        // Valida SQL contra injection (apenas para inputs de usuário, não para queries SQL)
        if (!isValidSQLQuery(normalizedInput)) {
            threats.add("SQL Injection detectado");
        }
        
        // Verifica XSS
        for (Pattern pattern : XSS_PATTERNS) {
            if (pattern.matcher(normalizedInput).find()) {
                threats.add("XSS detectado: " + pattern.pattern());
            }
        }
        
        // Verifica Path Traversal
        for (Pattern pattern : PATH_TRAVERSAL_PATTERNS) {
            if (pattern.matcher(normalizedInput).find()) {
                threats.add("Path Traversal detectado: " + pattern.pattern());
            }
        }
        
        // Verifica buffer overflow
        for (Pattern pattern : BUFFER_OVERFLOW_PATTERNS) {
            if (pattern.matcher(input).find()) {
                threats.add("Buffer Overflow detectado: " + pattern.pattern());
            }
        }
        
        // Verifica buffer overflow baseado no comprimento
        if (input.length() > 1500) {
            threats.add("Buffer Overflow detectado: String muito longa (" + input.length() + " caracteres)");
        }
        
        // Verifica Command Injection
        for (Pattern pattern : COMMAND_INJECTION_PATTERNS) {
            if (pattern.matcher(normalizedInput).find()) {
                threats.add("Command Injection detectado: " + pattern.pattern());
            }
        }
        
        // Verifica comprimento excessivo (possível DoS)
        if (input.length() > 10000) {
            threats.add("Input excessivamente longo: " + input.length() + " caracteres");
        }
        
        // Verifica caracteres suspeitos
        if (containsSuspiciousCharacters(input)) {
            threats.add("Caracteres suspeitos detectados");
        }
        
        boolean isSafe = threats.isEmpty();
        String message = isSafe ? "Input seguro" : "Ameaças detectadas";
        
        // Log de tentativas suspeitas
        if (!isSafe) {
            logger.warn("Tentativa de ataque detectada: {}", String.join(", ", threats));
        }
        
        return new SecurityScanResult(isSafe, message, threats);
    }
    
    /**
     * Limpa e sanitiza uma string para uso seguro
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        
        // Remove caracteres de controle perigosos
        String sanitized = input.replaceAll("[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F]", "");
        
        // Remove tags HTML/JavaScript
        sanitized = sanitized.replaceAll("<script[^>]*>.*?</script>", "");
        sanitized = sanitized.replaceAll("<iframe[^>]*>.*?</iframe>", "");
        sanitized = sanitized.replaceAll("<object[^>]*>.*?</object>", "");
        sanitized = sanitized.replaceAll("<embed[^>]*>.*?</embed>", "");
        sanitized = sanitized.replaceAll("<img[^>]*>", "");
        sanitized = sanitized.replaceAll("<link[^>]*>", "");
        sanitized = sanitized.replaceAll("<meta[^>]*>", "");
        
        // Remove tags script perigosas
        sanitized = sanitized.replaceAll("(?i)<script[^>]*>.*?</script>", "");
        sanitized = sanitized.replaceAll("(?i)<script[^>]*>", "");
        sanitized = sanitized.replaceAll("(?i)</script>", "");
        
        // Remove eventos JavaScript
        sanitized = sanitized.replaceAll("on\\w+\\s*=\\s*[\"'][^\"']*[\"']", "");
        sanitized = sanitized.replaceAll("javascript\\s*:", "");
        sanitized = sanitized.replaceAll("vbscript\\s*:", "");
        
        // Remove tentativas de SQL Injection
        sanitized = sanitized.replaceAll("(?i)(union|select|insert|update|delete|drop|create|alter|exec|execute)", "");
        sanitized = sanitized.replaceAll("(?i)(or|and)\\s+\\d+\\s*=\\s*\\d+", "");
        sanitized = sanitized.replaceAll("(?i)(or|and)\\s+['\"]?\\w+['\"]?\\s*=\\s*['\"]?\\w+['\"]?", "");
        sanitized = sanitized.replaceAll("--|#|/\\*|\\*/|;", "");
        
        // Remove apóstrofos e aspas simples
        sanitized = sanitized.replaceAll("'", "");
        sanitized = sanitized.replaceAll("\"", "");
        
        // Remove path traversal
        sanitized = sanitized.replaceAll("\\.\\./", "");
        sanitized = sanitized.replaceAll("\\.\\.\\\\", "");
        
        // Limita comprimento
        if (sanitized.length() > 1000) {
            sanitized = sanitized.substring(0, 1000);
        }
        
        return sanitized.trim();
    }
    
    /**
     * Verifica se uma string é segura para uso em HTML
     */
    public static boolean isSafeForHTML(String input) {
        SecurityScanResult result = scanForThreats(input);
        return result.isSafe();
    }
    
    /**
     * Verifica se uma string é segura para uso em paths de arquivo
     */
    public static boolean isSafeForFilePath(String input) {
        SecurityScanResult result = scanForThreats(input);
        return result.isSafe() && !containsPathTraversalPatterns(input);
    }
    
    /**
     * Verifica se uma string é uma query SQL legítima (não injection)
     */
    private static boolean isValidSQLQuery(String input) {
        // Se é uma query SQL legítima (começa com SELECT, INSERT, UPDATE, DELETE, etc.)
        // e não contém padrões de injection, consideramos segura
        String trimmed = input.trim().toLowerCase();
        
        // Queries SQL legítimas começam com palavras-chave SQL
        if (trimmed.startsWith("select ") || trimmed.startsWith("insert ") || 
            trimmed.startsWith("update ") || trimmed.startsWith("delete ") ||
            trimmed.startsWith("create ") || trimmed.startsWith("drop ") ||
            trimmed.startsWith("alter ")) {
            
            // Verifica se contém padrões de injection reais
            // (não apenas palavras SQL legítimas)
            return !containsRealSQLInjection(input);
        }
        
        // Se não começa com palavra-chave SQL, verifica se tem injection
        return !containsRealSQLInjection(input);
    }
    
    /**
     * Verifica se contém SQL injection real (não apenas palavras SQL)
     */
    private static boolean containsRealSQLInjection(String input) {
        String lowerInput = input.toLowerCase();
        
        // Padrões que realmente indicam injection
        return lowerInput.contains(" or 1=1") ||
               lowerInput.contains(" and 1=1") ||
               lowerInput.contains(" or '1'='1") ||
               lowerInput.contains(" and '1'='1") ||
               lowerInput.contains(" union select") ||
               lowerInput.contains("--") ||
               lowerInput.contains("/*") ||
               lowerInput.contains("*/") ||
               lowerInput.contains(";") ||
               lowerInput.contains("xp_") ||
               lowerInput.contains("sp_") ||
               lowerInput.contains("waitfor delay") ||
               lowerInput.contains("sleep(") ||
               lowerInput.contains("benchmark(");
    }
    
    /**
     * Normaliza input para análise
     */
    private static String normalizeInput(String input) {
        return input.toLowerCase().trim();
    }
    
    /**
     * Verifica se contém caracteres suspeitos
     */
    private static boolean containsSuspiciousCharacters(String input) {
        // Verifica caracteres de controle
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isISOControl(c) && c != '\t' && c != '\n' && c != '\r') {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Verifica se contém padrões de path traversal
     */
    private static boolean containsPathTraversalPatterns(String input) {
        return input.contains("../") || input.contains("..\\") || 
               input.contains("/etc/") || input.contains("\\windows\\");
    }
    
    /**
     * Resultado da verificação de segurança
     */
    public static class SecurityScanResult {
        private final boolean safe;
        private final String message;
        private final List<String> threats;
        
        public SecurityScanResult(boolean safe, String message, List<String> threats) {
            this.safe = safe;
            this.message = message;
            this.threats = new ArrayList<>(threats);
        }
        
        public boolean isSafe() {
            return safe;
        }
        
        public String getMessage() {
            return message;
        }
        
        public List<String> getThreats() {
            return new ArrayList<>(threats);
        }
        
        @Override
        public String toString() {
            if (safe) {
                return "SAFE: " + message;
            } else {
                return "THREAT: " + message + " | Threats: " + String.join(", ", threats);
            }
        }
    }
}
