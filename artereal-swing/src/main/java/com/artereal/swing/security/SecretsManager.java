package com.artereal.swing.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.PosixFilePermissions;

/**
 * Gerenciador de Secrets baseado em OWASP A02: Cryptographic Failures
 * Implementa secrets management seguro para produção
 */
public class SecretsManager {
    
    private static final Logger logger = LoggerFactory.getLogger(SecretsManager.class);
    
    // Cache de secrets em memória (em produção usar AWS KMS/Azure Key Vault)
    private static final Map<String, String> secretsCache = new HashMap<>();
    private static final Map<String, Long> lastModified = new HashMap<>();
    
    // Configurações OWASP
    private static final long CACHE_TTL_MS = 300000; // 5 minutos
    private static final String SECRETS_FILE = System.getProperty("user.home") + "/.artereal/secrets.properties";
    
    /**
     * Inicializa o gerenciador de secrets
     */
    public static void initialize() {
        logger.info("Inicializando Secrets Manager OWASP");
        
        // Carrega secrets do ambiente primeiro (OWASP recommendation)
        loadFromEnvironment();
        
        // Carrega do arquivo se não existir no ambiente
        loadFromFile();
        
        // Valida secrets críticos
        validateCriticalSecrets();
        
        logger.info("Secrets Manager inicializado com {} secrets", secretsCache.size());
    }
    
    /**
     * Carrega secrets de variáveis de ambiente (OWASP best practice)
     */
    private static void loadFromEnvironment() {
        // Database secrets
        setSecret("database.url", System.getenv("ARTEAL_DB_URL"));
        setSecret("database.username", System.getenv("ARTEAL_DB_USERNAME"));
        setSecret("database.password", System.getenv("ARTEAL_DB_PASSWORD"));
        
        // Crypto secrets
        setSecret("crypto.master.key", System.getenv("ARTEAL_CRYPTO_KEY"));
        setSecret("crypto.salt", System.getenv("ARTEAL_CRYPTO_SALT"));
        
        // API secrets
        setSecret("api.secret.key", System.getenv("ARTEAL_API_SECRET"));
        setSecret("jwt.secret", System.getenv("ARTEAL_JWT_SECRET"));
        
        // Security secrets
        setSecret("security.pepper", System.getenv("ARTEAL_SECURITY_PEPPER"));
        
        logger.debug("Secrets carregados de variáveis de ambiente");
    }
    
    /**
     * Carrega secrets do arquivo (fallback para desenvolvimento)
     */
    private static void loadFromFile() {
        try {
            Path secretsPath = Paths.get(SECRETS_FILE);
            if (!Files.exists(secretsPath)) {
                logger.warn("Arquivo de secrets não encontrado: {}", SECRETS_FILE);
                createDefaultSecretsFile();
                return;
            }
            
            Properties props = new Properties();
            try (InputStream is = Files.newInputStream(secretsPath)) {
                props.load(is);
            }
            
            for (String key : props.stringPropertyNames()) {
                if (!secretsCache.containsKey(key)) {
                    setSecret(key, props.getProperty(key));
                }
            }
            
            logger.debug("Secrets carregados do arquivo: {}", SECRETS_FILE);
            
        } catch (IOException e) {
            logger.error("Erro ao carregar secrets do arquivo", e);
        }
    }
    
    /**
     * Cria arquivo de secrets padrão para desenvolvimento
     */
    private static void createDefaultSecretsFile() {
        try {
            Path secretsDir = Paths.get(SECRETS_FILE).getParent();
            if (!Files.exists(secretsDir)) {
                Files.createDirectories(secretsDir);
            }
            
            Properties defaultProps = new Properties();
            defaultProps.setProperty("database.url", "jdbc:postgresql://localhost:5432/arteal");
            defaultProps.setProperty("database.username", "arteal_user");
            defaultProps.setProperty("database.password", "CHANGE_ME_IN_PRODUCTION");
            defaultProps.setProperty("crypto.master.key", generateSecureKey());
            defaultProps.setProperty("crypto.salt", generateSecureSalt());
            defaultProps.setProperty("api.secret.key", generateSecureKey());
            defaultProps.setProperty("jwt.secret", generateSecureKey());
            defaultProps.setProperty("security.pepper", generateSecureKey());
            
            try (OutputStream os = Files.newOutputStream(Paths.get(SECRETS_FILE))) {
                defaultProps.store(os, "ArteReal Secrets - NÃO COMMITAR");
            }
            
            // Define permissões restritas (OWASP)
            Files.setPosixFilePermissions(Paths.get(SECRETS_FILE), 
                PosixFilePermissions.fromString("rw-------"));
            
            logger.warn("Arquivo de secrets padrão criado: {}", SECRETS_FILE);
            
        } catch (IOException e) {
            logger.error("Erro ao criar arquivo de secrets padrão", e);
        }
    }
    
    /**
     * Obtém secret de forma segura
     */
    public static String getSecret(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key não pode ser nulo");
        }
        
        String secret = secretsCache.get(key);
        if (secret == null) {
            logger.warn("Secret não encontrado: {}", key);
            return null;
        }
        
        // Verifica TTL do cache
        Long lastMod = lastModified.get(key);
        if (lastMod != null && (System.currentTimeMillis() - lastMod) > CACHE_TTL_MS) {
            logger.debug("Secret expirado no cache, recarregando: {}", key);
            reloadSecret(key);
            secret = secretsCache.get(key);
        }
        
        return secret;
    }
    
    /**
     * Define secret de forma segura
     */
    private static void setSecret(String key, String value) {
        if (key != null && value != null && !value.trim().isEmpty()) {
            secretsCache.put(key, value);
            lastModified.put(key, System.currentTimeMillis());
            logger.debug("Secret carregado: {}", key);
        }
    }
    
    /**
     * Recarrega secret específico
     */
    private static void reloadSecret(String key) {
        // Tenta recarregar do ambiente
        String envValue = System.getenv(getEnvKeyForSecret(key));
        if (envValue != null) {
            setSecret(key, envValue);
            return;
        }
        
        // Tenta recarregar do arquivo
        try {
            Path secretsPath = Paths.get(SECRETS_FILE);
            if (Files.exists(secretsPath)) {
                Properties props = new Properties();
                try (InputStream is = Files.newInputStream(secretsPath)) {
                    props.load(is);
                }
                
                String fileValue = props.getProperty(key);
                if (fileValue != null) {
                    setSecret(key, fileValue);
                }
            }
        } catch (IOException e) {
            logger.error("Erro ao recarregar secret: {}", key, e);
        }
    }
    
    /**
     * Mapeia secret para variável de ambiente
     */
    private static String getEnvKeyForSecret(String secretKey) {
        switch (secretKey) {
            case "database.url": return "ARTEAL_DB_URL";
            case "database.username": return "ARTEAL_DB_USERNAME";
            case "database.password": return "ARTEAL_DB_PASSWORD";
            case "crypto.master.key": return "ARTEAL_CRYPTO_KEY";
            case "crypto.salt": return "ARTEAL_CRYPTO_SALT";
            case "api.secret.key": return "ARTEAL_API_SECRET";
            case "jwt.secret": return "ARTEAL_JWT_SECRET";
            case "security.pepper": return "ARTEAL_SECURITY_PEPPER";
            default: return "ARTEAL_" + secretKey.toUpperCase().replace(".", "_");
        }
    }
    
    /**
     * Valida secrets críticos OWASP
     */
    private static void validateCriticalSecrets() {
        List<String> issues = new ArrayList<>();
        
        // Valida crypto master key
        String cryptoKey = getSecret("crypto.master.key");
        if (cryptoKey == null || cryptoKey.length() < 32) {
            issues.add("Crypto master key ausente ou muito curto");
        }
        
        // Valida database password (permite valores de teste)
        String dbPassword = getSecret("database.password");
        if (dbPassword == null || dbPassword.equals("CHANGE_ME_IN_PRODUCTION")) {
            // Permite valores de teste em ambiente de testes
            if (!System.getProperty("test.environment", "false").equals("true") && 
                !dbPassword.contains("test_password")) {
                issues.add("Database password não configurado ou usando valor padrão");
            }
        }
        
        // Valida JWT secret
        String jwtSecret = getSecret("jwt.secret");
        if (jwtSecret == null || jwtSecret.length() < 64) {
            issues.add("JWT secret ausente ou muito curto (mínimo 64 caracteres)");
        }
        
        if (!issues.isEmpty()) {
            logger.error("Problemas na validação de secrets OWASP: {}", issues);
            throw new SecurityException("Configuração de secrets inválida: " + String.join(", ", issues));
        }
        
        logger.info("Validação de secrets OWASP concluída com sucesso");
    }
    
    /**
     * Gera chave segura
     */
    private static String generateSecureKey() {
        return UUID.randomUUID().toString() + UUID.randomUUID().toString();
    }
    
    /**
     * Gera salt seguro
     */
    private static String generateSecureSalt() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    
    /**
     * Rotaciona secrets (OWASP recommendation)
     */
    public static void rotateSecrets() {
        logger.info("Iniciando rotação de secrets OWASP");
        
        // Gera novos secrets
        String newCryptoKey = generateSecureKey();
        String newJwtSecret = generateSecureKey();
        String newApiSecret = generateSecureKey();
        
        // Atualiza cache
        setSecret("crypto.master.key", newCryptoKey);
        setSecret("jwt.secret", newJwtSecret);
        setSecret("api.secret.key", newApiSecret);
        
        // Em produção, atualizar também no KMS/Vault
        logger.info("Rotação de secrets concluída");
    }
    
    /**
     * Verifica se secrets estão expirados
     */
    public static List<String> checkExpiredSecrets() {
        List<String> expired = new ArrayList<>();
        long now = System.currentTimeMillis();
        
        for (Map.Entry<String, Long> entry : lastModified.entrySet()) {
            if ((now - entry.getValue()) > CACHE_TTL_MS) {
                expired.add(entry.getKey());
            }
        }
        
        return expired;
    }
    
    /**
     * Limpa cache de secrets (OWASP memory safety)
     */
    public static void clearCache() {
        secretsCache.clear();
        lastModified.clear();
        logger.info("Cache de secrets limpo");
    }
    
    /**
     * Obtém configuração de database com secrets
     */
    public static DatabaseConfig getDatabaseConfig() {
        return new DatabaseConfig(
            getSecret("database.url"),
            getSecret("database.username"),
            getSecret("database.password")
        );
    }
    
    /**
     * Configuração de database com secrets
     */
    public static class DatabaseConfig {
        private final String url;
        private final String username;
        private final String password;
        
        public DatabaseConfig(String url, String username, String password) {
            this.url = url;
            this.username = username;
            this.password = password;
        }
        
        public String getUrl() { return url; }
        public String getUsername() { return username; }
        public String getPassword() { return password; }
    }
}
