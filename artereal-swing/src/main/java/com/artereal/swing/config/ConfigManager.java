package com.artereal.swing.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gerenciador de configurações usando padrão Singleton.
 * Centraliza configurações da aplicação com persistência em arquivo.
 */
public class ConfigManager {
    
    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);
    private static volatile ConfigManager instance;
    
    // Cache de configurações em memória
    private final ConcurrentHashMap<String, String> configurations;
    
    // Arquivo de configurações
    private static final String CONFIG_FILE = "artereal.properties";
    private final Path configPath;
    
    // Configurações padrão
    private static final String DEFAULT_DB_URL = "jdbc:postgresql://localhost:5432/artereal_db";
    private static final String DEFAULT_APP_NAME = "ArteReal - Sistema de Gestão Maçônica";
    private static final String DEFAULT_APP_VERSION = "1.0.0";
    private static final String DEFAULT_THEME = "default";
    private static final String DEFAULT_LANGUAGE = "pt-BR";
    private static final boolean DEFAULT_DEBUG_MODE = false;
    private static final int DEFAULT_CACHE_TTL = 30;
    private static final int DEFAULT_SESSION_TIMEOUT = 30;
    
    /**
     * Construtor privado para implementar Singleton
     */
    private ConfigManager() {
        this.configurations = new ConcurrentHashMap<>();
        this.configPath = Paths.get(System.getProperty("user.home"), ".artereal", CONFIG_FILE);
        
        // Carrega configurações do arquivo
        loadConfigurations();
        
        // Define configurações padrão se não existirem
        setDefaultConfigurations();
        
        logger.debug("ConfigManager Singleton criado");
    }
    
    /**
     * Obtém a instância única do ConfigManager (Double-Checked Locking)
     */
    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * Carrega configurações do arquivo
     */
    private void loadConfigurations() {
        try {
            if (Files.exists(configPath)) {
                Properties props = new Properties();
                try (InputStream input = Files.newInputStream(configPath)) {
                    props.load(input);
                }
                
                // Carrega para o cache
                for (String key : props.stringPropertyNames()) {
                    configurations.put(key, props.getProperty(key));
                }
                
                logger.info("Configurações carregadas de: {}", configPath);
                logger.debug("Total de configurações carregadas: {}", configurations.size());
            } else {
                logger.info("Arquivo de configurações não encontrado. Criando novo.");
                createConfigDirectory();
                saveConfigurations();
            }
        } catch (IOException e) {
            logger.error("Erro ao carregar configurações: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Salva configurações no arquivo
     */
    public void saveConfigurations() {
        try {
            // Garante que o diretório existe
            createConfigDirectory();
            
            Properties props = new Properties();
            configurations.forEach(props::setProperty);
            
            try (OutputStream output = Files.newOutputStream(configPath)) {
                props.store(output, "ArteReal System Configuration - " + 
                           java.time.LocalDateTime.now());
            }
            
            logger.debug("Configurações salvas em: {}", configPath);
        } catch (IOException e) {
            logger.error("Erro ao salvar configurações: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Cria diretório de configurações se não existir
     */
    private void createConfigDirectory() throws IOException {
        Path parentDir = configPath.getParent();
        if (parentDir != null && !Files.exists(parentDir)) {
            Files.createDirectories(parentDir);
            logger.debug("Diretório de configurações criado: {}", parentDir);
        }
    }
    
    /**
     * Define configurações padrão
     */
    private void setDefaultConfigurations() {
        setDefaultIfAbsent("database.url", DEFAULT_DB_URL);
        setDefaultIfAbsent("app.name", DEFAULT_APP_NAME);
        setDefaultIfAbsent("app.version", DEFAULT_APP_VERSION);
        setDefaultIfAbsent("ui.theme", DEFAULT_THEME);
        setDefaultIfAbsent("ui.language", DEFAULT_LANGUAGE);
        setDefaultIfAbsent("debug.enabled", String.valueOf(DEFAULT_DEBUG_MODE));
        setDefaultIfAbsent("cache.ttl", String.valueOf(DEFAULT_CACHE_TTL));
        setDefaultIfAbsent("session.timeout", String.valueOf(DEFAULT_SESSION_TIMEOUT));
        setDefaultIfAbsent("ui.window.width", "1200");
        setDefaultIfAbsent("ui.window.height", "800");
        setDefaultIfAbsent("ui.window.maximized", "false");
        setDefaultIfAbsent("backup.auto.enabled", "true");
        setDefaultIfAbsent("backup.interval.hours", "24");
        setDefaultIfAbsent("logs.level", "INFO");
        setDefaultIfAbsent("logs.max.files", "10");
        setDefaultIfAbsent("reports.default.format", "PDF");
        
        logger.debug("Configurações padrão definidas");
    }
    
    /**
     * Define valor padrão se a chave não existir
     */
    private void setDefaultIfAbsent(String key, String defaultValue) {
        configurations.putIfAbsent(key, defaultValue);
    }
    
    /**
     * Obtém uma configuração como String
     */
    public String getString(String key) {
        if (key == null) {
            logger.warn("Tentativa de obter configuração com chave nula - retornando null");
            return null;
        }
        return configurations.get(key);
    }
    
    /**
     * Obtém uma configuração como String com valor padrão
     */
    public String getString(String key, String defaultValue) {
        if (key == null) {
            logger.warn("Tentativa de obter configuração com chave nula - retornando valor padrão");
            return defaultValue;
        }
        return configurations.getOrDefault(key, defaultValue);
    }
    
    /**
     * Obtém uma configuração como int
     */
    public int getInt(String key) {
        return getInt(key, 0);
    }
    
    /**
     * Obtém uma configuração como int com valor padrão
     */
    public int getInt(String key, int defaultValue) {
        try {
            String value = configurations.get(key);
            if (value != null) {
                return Integer.parseInt(value);
            }
        } catch (NumberFormatException e) {
            logger.warn("Valor inválido para configuração {}: {}", key, configurations.get(key));
        }
        return defaultValue;
    }
    
    /**
     * Obtém uma configuração como boolean
     */
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }
    
    /**
     * Obtém uma configuração como boolean com valor padrão
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        String value = configurations.get(key);
        if (value != null) {
            // Normalizar o valor para minúsculas
            String normalizedValue = value.trim().toLowerCase();
            
            // Aceitar múltiplos formatos booleanos
            if ("true".equals(normalizedValue) || "yes".equals(normalizedValue) || "1".equals(normalizedValue) || "sim".equals(normalizedValue)) {
                return true;
            } else if ("false".equals(normalizedValue) || "no".equals(normalizedValue) || "0".equals(normalizedValue) || "não".equals(normalizedValue) || "nao".equals(normalizedValue)) {
                return false;
            }
            
            // Se o valor não for reconhecido como boolean, retornar o padrão
            logger.warn("Valor boolean não reconhecido para configuração {}: {}, retornando padrão: {}", key, value, defaultValue);
            return defaultValue;
        }
        return defaultValue;
    }
    
    /**
     * Obtém uma configuração como long
     */
    public long getLong(String key) {
        return getLong(key, 0L);
    }
    
    /**
     * Obtém uma configuração como long com valor padrão
     */
    public long getLong(String key, long defaultValue) {
        try {
            String value = configurations.get(key);
            if (value != null) {
                return Long.parseLong(value);
            }
        } catch (NumberFormatException e) {
            logger.warn("Valor inválido para configuração {}: {}", key, configurations.get(key));
        }
        return defaultValue;
    }
    
    /**
     * Define uma configuração
     */
    public void set(String key, String value) {
        if (key == null) {
            logger.warn("Tentativa de definir configuração com chave nula - ignorando");
            return;
        }
        // Permitir valores nulos, mas tratá-los adequadamente
        if (value == null) {
            // Remover a chave se o valor for null
            configurations.remove(key);
            logger.debug("Configuração removida: {}", key);
        } else {
            configurations.put(key, value);
            logger.debug("Configuração definida: {} = {}", key, value);
        }
    }
    
    /**
     * Define uma configuração (int)
     */
    public void set(String key, int value) {
        set(key, String.valueOf(value));
    }
    
    /**
     * Define uma configuração (boolean)
     */
    public void set(String key, boolean value) {
        set(key, String.valueOf(value));
    }
    
    /**
     * Define uma configuração (long)
     */
    public void set(String key, long value) {
        set(key, String.valueOf(value));
    }
    
    /**
     * Remove uma configuração
     */
    public void remove(String key) {
        configurations.remove(key);
        logger.debug("Configuração removida: {}", key);
    }
    
    /**
     * Verifica se uma configuração existe
     */
    public boolean contains(String key) {
        return configurations.containsKey(key);
    }
    
    /**
     * Limpa todas as configurações
     */
    public void clear() {
        configurations.clear();
        logger.debug("Todas as configurações foram limpas");
    }
    
    /**
     * Obtém todas as configurações
     */
    public ConcurrentHashMap<String, String> getAll() {
        return new ConcurrentHashMap<>(configurations);
    }
    
    /**
     * Recarrega configurações do arquivo
     */
    public void reload() {
        configurations.clear();
        loadConfigurations();
        setDefaultConfigurations();
        logger.info("Configurações recarregadas");
    }
    
    /**
     * Exporta configurações para um arquivo específico
     */
    public void exportToFile(String filePath) {
        try {
            Properties props = new Properties();
            configurations.forEach(props::setProperty);
            
            try (OutputStream output = new FileOutputStream(filePath)) {
                props.store(output, "ArteReal Configuration Export - " + 
                           java.time.LocalDateTime.now());
            }
            
            logger.info("Configurações exportadas para: {}", filePath);
        } catch (IOException e) {
            logger.error("Erro ao exportar configurações: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao exportar configurações", e);
        }
    }
    
    /**
     * Importa configurações de um arquivo
     */
    public void importFromFile(String filePath) {
        try {
            Properties props = new Properties();
            try (InputStream input = new FileInputStream(filePath)) {
                props.load(input);
            }
            
            // Importa para o cache
            for (String key : props.stringPropertyNames()) {
                configurations.put(key, props.getProperty(key));
            }
            
            saveConfigurations();
            logger.info("Configurações importadas de: {}", filePath);
        } catch (IOException e) {
            logger.error("Erro ao importar configurações: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao importar configurações", e);
        }
    }
    
    // Métodos de conveniência para configurações específicas
    
    /**
     * Obtém URL do banco de dados
     */
    public String getDatabaseUrl() {
        return getString("database.url", DEFAULT_DB_URL);
    }
    
    /**
     * Obtém nome da aplicação
     */
    public String getApplicationName() {
        return getString("app.name", DEFAULT_APP_NAME);
    }
    
    /**
     * Obtém versão da aplicação
     */
    public String getApplicationVersion() {
        return getString("app.version", DEFAULT_APP_VERSION);
    }
    
    /**
     * Verifica se modo debug está ativado
     */
    public boolean isDebugEnabled() {
        return getBoolean("debug.enabled", DEFAULT_DEBUG_MODE);
    }
    
    /**
     * Obtém TTL do cache em minutos
     */
    public int getCacheTTL() {
        return getInt("cache.ttl", DEFAULT_CACHE_TTL);
    }
    
    /**
     * Obtém timeout da sessão em minutos
     */
    public int getSessionTimeout() {
        return getInt("session.timeout", DEFAULT_SESSION_TIMEOUT);
    }
    
    /**
     * Obtém tema da interface
     */
    public String getUITheme() {
        return getString("ui.theme", DEFAULT_THEME);
    }
    
    /**
     * Obtém idioma da interface
     */
    public String getUILanguage() {
        return getString("ui.language", DEFAULT_LANGUAGE);
    }
    
    /**
     * Obtém largura da janela
     */
    public int getWindowWidth() {
        return getInt("ui.window.width", 1200);
    }
    
    /**
     * Obtém altura da janela
     */
    public int getWindowHeight() {
        return getInt("ui.window.height", 800);
    }
    
    /**
     * Verifica se janela deve iniciar maximizada
     */
    public boolean isWindowMaximized() {
        return getBoolean("ui.window.maximized", false);
    }
    
    /**
     * Obtém caminho do arquivo de configurações
     */
    public Path getConfigPath() {
        return configPath;
    }
    
    /**
     * Obtém estatísticas das configurações
     */
    public String getStats() {
        return String.format("ConfigManager Stats - Total: %d, File: %s, Debug: %s", 
                           configurations.size(), configPath, isDebugEnabled());
    }
}
