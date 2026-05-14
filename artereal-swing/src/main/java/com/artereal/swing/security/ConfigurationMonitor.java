package com.artereal.swing.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Monitor de Alterações em Configurações de Segurança
 * Implementa OWASP A05: Security Misconfiguration
 * 
 * Monitora alterações não autorizadas em arquivos de configuração críticos
 */
public class ConfigurationMonitor {
    
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationMonitor.class);
    private static volatile ConfigurationMonitor instance;
    
    // Arquivos críticos para monitorar
    private static final List<String> CRITICAL_FILES = List.of(
        "application.properties",
        "security.properties", 
        "database.properties",
        "logging.properties",
        ".artereal/secrets.properties"
    );
    
    private final Map<String, FileMetadata> monitoredFiles;
    private final Map<String, ConfigurationChange> changeHistory;
    private final ScheduledExecutorService scheduler;
    private final StructuredLogger structuredLogger;
    private final AtomicLong changeIdCounter;
    
    private ConfigurationMonitor() {
        this.monitoredFiles = new ConcurrentHashMap<>();
        this.changeHistory = new ConcurrentHashMap<>();
        this.scheduler = Executors.newScheduledThreadPool(2);
        this.structuredLogger = StructuredLogger.getInstance();
        this.changeIdCounter = new AtomicLong(1);
        
        initializeMonitoring();
        logger.info("Configuration Monitor inicializado");
    }
    
    public static ConfigurationMonitor getInstance() {
        if (instance == null) {
            synchronized (ConfigurationMonitor.class) {
                if (instance == null) {
                    instance = new ConfigurationMonitor();
                }
            }
        }
        return instance;
    }
    
    /**
     * Metadados de arquivo para comparação
     */
    private static class FileMetadata {
        private final String filePath;
        private final long lastModified;
        private final long size;
        private final String checksumSHA256;
        private final LocalDateTime lastChecked;
        
        public FileMetadata(String filePath, long lastModified, long size, String checksum) {
            this.filePath = filePath;
            this.lastModified = lastModified;
            this.size = size;
            this.checksumSHA256 = checksum;
            this.lastChecked = LocalDateTime.now();
        }
        
        // Getters
        public String getFilePath() { return filePath; }
        public long getLastModified() { return lastModified; }
        public long getSize() { return size; }
        public String getChecksumSHA256() { return checksumSHA256; }
        public LocalDateTime getLastChecked() { return lastChecked; }
        
        public boolean hasChanged(long newLastModified, long newSize, String newChecksum) {
            return lastModified != newLastModified || size != newSize || !checksumSHA256.equals(newChecksum);
        }
    }
    
    /**
     * Registro de alteração de configuração
     */
    public static class ConfigurationChange {
        private final long changeId;
        private final String filePath;
        private final LocalDateTime changeTime;
        private final String changeType;
        private final String oldValue;
        private final String newValue;
        private final String changedBy;
        private final String ipAddress;
        private final String description;
        private final boolean suspicious;
        
        public ConfigurationChange(long changeId, String filePath, String changeType, 
                                String oldValue, String newValue, String changedBy, String ipAddress, 
                                String description, boolean suspicious) {
            this.changeId = changeId;
            this.filePath = filePath;
            this.changeTime = LocalDateTime.now();
            this.changeType = changeType;
            this.oldValue = oldValue;
            this.newValue = newValue;
            this.changedBy = changedBy;
            this.ipAddress = ipAddress;
            this.description = description;
            this.suspicious = suspicious;
        }
        
        // Getters
        public long getChangeId() { return changeId; }
        public String getFilePath() { return filePath; }
        public LocalDateTime getChangeTime() { return changeTime; }
        public String getChangeType() { return changeType; }
        public String getOldValue() { return oldValue; }
        public String getNewValue() { return newValue; }
        public String getChangedBy() { return changedBy; }
        public String getIpAddress() { return ipAddress; }
        public String getDescription() { return description; }
        public boolean isSuspicious() { return suspicious; }
    }
    
    /**
     * Inicializa monitoramento dos arquivos críticos
     */
    private void initializeMonitoring() {
        // Inicializa metadados dos arquivos
        for (String fileName : CRITICAL_FILES) {
            try {
                Path filePath = getConfigFilePath(fileName);
                if (Files.exists(filePath)) {
                    FileMetadata metadata = createFileMetadata(filePath);
                    if (metadata != null) {
                        monitoredFiles.put(fileName, metadata);
                        logger.debug("Arquivo configurado para monitoramento: {}", fileName);
                    }
                }
            } catch (Exception e) {
                logger.warn("Erro ao inicializar monitoramento do arquivo: {}", fileName, e);
            }
        }
        
        // Agenda verificação periódica (a cada 30 segundos)
        scheduler.scheduleAtFixedRate(this::performPeriodicCheck, 30, 30, TimeUnit.SECONDS);
        
        // Agenda limpeza de histórico (a cada 24 horas)
        scheduler.scheduleAtFixedRate(this::cleanupHistory, 24, 24, TimeUnit.HOURS);
    }
    
    /**
     * Obtém caminho do arquivo de configuração
     */
    private Path getConfigFilePath(String fileName) {
        // Tenta diferentes localizações
        List<String> possiblePaths = List.of(
            System.getProperty("user.home") + "/.artereal/" + fileName,
            System.getProperty("user.dir") + "/config/" + fileName,
            System.getProperty("user.dir") + "/" + fileName
        );
        
        for (String pathStr : possiblePaths) {
            Path path = Paths.get(pathStr);
            if (Files.exists(path)) {
                return path;
            }
        }
        
        return Paths.get(fileName);
    }
    
    /**
     * Cria metadados do arquivo
     */
    private FileMetadata createFileMetadata(Path filePath) {
        try {
            long lastModified = Files.getLastModifiedTime(filePath).toMillis();
            long size = Files.size(filePath);
            String checksum = calculateFileChecksum(filePath);
            
            return new FileMetadata(filePath.toString(), lastModified, size, checksum);
        } catch (Exception e) {
            logger.error("Erro ao criar metadados do arquivo: {}", filePath, e);
            return null;
        }
    }
    
    /**
     * Calcula checksum SHA256 do arquivo
     */
    private String calculateFileChecksum(Path filePath) {
        try {
            byte[] fileContent = Files.readAllBytes(filePath);
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(fileContent);
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (Exception e) {
            logger.error("Erro ao calcular checksum do arquivo: {}", filePath, e);
            return null;
        }
    }
    
    /**
     * Verificação periódica de alterações
     */
    private void performPeriodicCheck() {
        for (String fileName : CRITICAL_FILES) {
            try {
                Path filePath = getConfigFilePath(fileName);
                if (Files.exists(filePath)) {
                    checkFileChanges(fileName, filePath);
                }
            } catch (Exception e) {
                logger.warn("Erro na verificação periódica do arquivo: {}", fileName, e);
            }
        }
    }
    
    /**
     * Verifica alterações em arquivo específico
     */
    private void checkFileChanges(String fileName, Path filePath) {
        FileMetadata currentMetadata = monitoredFiles.get(fileName);
        FileMetadata newMetadata = createFileMetadata(filePath);
        
        if (newMetadata == null) return;
        
        if (currentMetadata == null) {
            // Novo arquivo detectado
            monitoredFiles.put(fileName, newMetadata);
            recordConfigurationChange(fileName, "FILE_CREATED", null, null, "SYSTEM", "localhost", 
                                     "Novo arquivo de configuração detectado", false);
        } else if (currentMetadata.hasChanged(newMetadata.getLastModified(), newMetadata.getSize(), newMetadata.getChecksumSHA256())) {
            // Alteração detectada
            boolean suspicious = isSuspiciousChange(fileName, currentMetadata, newMetadata);
            
            // Tenta obter diff das alterações
            String oldValue = suspicious ? extractFileContent(filePath.toString()) : null;
            String newValue = extractFileContent(filePath.toString());
            
            recordConfigurationChange(fileName, "FILE_MODIFIED", oldValue, newValue, "UNKNOWN", "UNKNOWN", 
                                     "Arquivo de configuração alterado", suspicious);
            
            // Atualiza metadados
            monitoredFiles.put(fileName, newMetadata);
            
            // Se for suspeito, toma ações adicionais
            if (suspicious) {
                handleSuspiciousChange(fileName, newMetadata);
            }
        }
    }
    
    /**
     * Verifica se alteração é suspeita
     */
    private boolean isSuspiciousChange(String fileName, FileMetadata oldMetadata, FileMetadata newMetadata) {
        // Critérios para identificar alterações suspeitas
        
        // 1. Alteração muito rápida (menos de 1 minuto da última)
        long timeDiff = System.currentTimeMillis() - oldMetadata.getLastChecked().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
        if (timeDiff < 60000) {
            return true;
        }
        
        // 2. Alteração em arquivo de secrets fora do horário de trabalho
        if (fileName.contains("secrets") || fileName.contains("security")) {
            int hour = LocalDateTime.now().getHour();
            if (hour < 8 || hour > 18) {
                return true;
            }
        }
        
        // 3. Mudança drástica no tamanho do arquivo
        double sizeRatio = (double) newMetadata.getSize() / oldMetadata.getSize();
        if (sizeRatio > 2.0 || sizeRatio < 0.5) {
            return true;
        }
        
        // 4. Alteração em múltiplos arquivos ao mesmo tempo
        long recentChanges = changeHistory.values().stream()
            .filter(change -> change.getChangeTime().isAfter(LocalDateTime.now().minusMinutes(5)))
            .count();
        if (recentChanges > 3) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Extrai conteúdo do arquivo para análise
     */
    private String extractFileContent(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (Files.exists(path) && Files.size(path) < 10000) { // Limitado a 10KB
                return Files.readString(path);
            }
        } catch (Exception e) {
            logger.warn("Erro ao extrair conteúdo do arquivo: {}", filePath, e);
        }
        return null;
    }
    
    /**
     * Registra alteração de configuração
     */
    private void recordConfigurationChange(String filePath, String changeType, String oldValue, 
                                        String newValue, String changedBy, String ipAddress, 
                                        String description, boolean suspicious) {
        
        long changeId = changeIdCounter.getAndIncrement();
        ConfigurationChange change = new ConfigurationChange(changeId, filePath, changeType, 
                                                            oldValue, newValue, changedBy, ipAddress, 
                                                            description, suspicious);
        
        changeHistory.put(filePath + ":" + changeId, change);
        
        // Log estruturado
        StructuredLogger.SecurityEvent event = new StructuredLogger.SecurityEvent(
            "CONFIGURATION_CHANGE", suspicious ? "HIGH" : "MEDIUM", changedBy, changeType, 
            suspicious ? "SUSPICIOUS" : "NORMAL", description)
            .withResource(filePath)
            .withIpAddress(ipAddress)
            .withDetail("change_id", changeId)
            .withDetail("file_size", newValue != null ? newValue.length() : 0)
            .withDetail("suspicious", suspicious);
        
        structuredLogger.logSecurityEvent(event);
        
        logger.info("Alteração de configuração registrada: {} - {} - Suspicious: {}", 
                   filePath, changeType, suspicious);
    }
    
    /**
     * Trata alterações suspeitas
     */
    private void handleSuspiciousChange(String fileName, FileMetadata metadata) {
        logger.error("ALERTA DE SEGURANÇA: Alteração suspeita detectada em: {}", fileName);
        
        // Em produção, poderia:
        // 1. Enviar alerta para equipe de segurança
        // 2. Bloquear alterações subsequentes
        // 3. Reverter para backup
        // 4. Notificar administradores
        
        // Por enquanto, apenas log detalhado
        structuredLogger.logSecurityEvent(
            new StructuredLogger.SecurityEvent("SUSPICIOUS_CONFIG_CHANGE", "CRITICAL", "UNKNOWN", 
                                             "DETECT", "ALERT", "Alteração suspeita em configuração crítica")
                .withResource(fileName)
                .withDetail("file_path", metadata.getFilePath())
                .withDetail("file_size", metadata.getSize())
                .withDetail("last_modified", metadata.getLastModified())
                .withDetail("checksum", metadata.getChecksumSHA256())
        );
    }
    
    /**
     * Verificação manual de arquivo
     */
    public boolean verifyFileIntegrity(String fileName) {
        FileMetadata metadata = monitoredFiles.get(fileName);
        if (metadata == null) {
            logger.warn("Arquivo não está sendo monitorado: {}", fileName);
            return false;
        }
        
        try {
            Path filePath = getConfigFilePath(fileName);
            if (!Files.exists(filePath)) {
                logger.warn("Arquivo não encontrado: {}", fileName);
                return false;
            }
            
            FileMetadata currentMetadata = createFileMetadata(filePath);
            return currentMetadata != null && 
                   !metadata.hasChanged(currentMetadata.getLastModified(), currentMetadata.getSize(), currentMetadata.getChecksumSHA256());
                   
        } catch (Exception e) {
            logger.error("Erro ao verificar integridade do arquivo: {}", fileName, e);
            return false;
        }
    }
    
    /**
     * Adiciona arquivo para monitoramento
     */
    public void addFileToMonitoring(String fileName) {
        if (!CRITICAL_FILES.contains(fileName)) {
            Path filePath = getConfigFilePath(fileName);
            if (Files.exists(filePath)) {
                FileMetadata metadata = createFileMetadata(filePath);
                if (metadata != null) {
                    monitoredFiles.put(fileName, metadata);
                    logger.info("Arquivo adicionado ao monitoramento: {}", fileName);
                }
            }
        }
    }
    
    /**
     * Remove arquivo do monitoramento
     */
    public void removeFileFromMonitoring(String fileName) {
        FileMetadata removed = monitoredFiles.remove(fileName);
        if (removed != null) {
            logger.info("Arquivo removido do monitoramento: {}", fileName);
        }
    }
    
    /**
     * Obtém histórico de alterações
     */
    public List<ConfigurationChange> getChangeHistory(String fileName, int limit) {
        return changeHistory.values().stream()
            .filter(change -> fileName == null || change.getFilePath().contains(fileName))
            .sorted((a, b) -> b.getChangeTime().compareTo(a.getChangeTime()))
            .limit(limit)
            .toList();
    }
    
    /**
     * Obtém estatísticas de monitoramento
     */
    public Map<String, Object> getMonitoringStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("monitored_files", monitoredFiles.size());
        stats.put("total_changes", changeHistory.size());
        stats.put("suspicious_changes", changeHistory.values().stream().mapToLong(c -> c.isSuspicious() ? 1 : 0).sum());
        stats.put("last_check", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        // Alterações por arquivo
        Map<String, Long> changesByFile = new HashMap<>();
        changeHistory.values().forEach(change -> {
            changesByFile.merge(change.getFilePath(), 1L, Long::sum);
        });
        stats.put("changes_by_file", changesByFile);
        
        return stats;
    }
    
    /**
     * Limpa histórico antigo
     */
    private void cleanupHistory() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(30);
        final int[] removedCount = {0};
        
        changeHistory.entrySet().removeIf(entry -> {
            boolean shouldRemove = entry.getValue().getChangeTime().isBefore(cutoff);
            if (shouldRemove) removedCount[0]++;
            return shouldRemove;
        });
        
        if (removedCount[0] > 0) {
            logger.info("Cleanup: {} registros de alterações removidos", removedCount[0]);
        }
    }
    
    /**
     * Para o monitoramento
     */
    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
        logger.info("Configuration Monitor desligado");
    }
}
