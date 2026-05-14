package com.artereal.swing.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gerenciador de Validação de Integridade para Uploads de Arquivos
 * Implementa OWASP A08: Software and Data Integrity Failures
 * 
 * Valida integridade, segurança e conformidade de arquivos uploadados
 */
public class UploadIntegrityManager {
    
    private static final Logger logger = LoggerFactory.getLogger(UploadIntegrityManager.class);
    private static volatile UploadIntegrityManager instance;
    
    // Configurações de segurança
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final List<String> ALLOWED_MIME_TYPES = List.of(
        "image/jpeg", "image/png", "image/gif", "image/webp",
        "application/pdf", "text/plain", "text/csv",
        "application/json", "application/xml"
    );
    
    private static final List<String> DANGEROUS_EXTENSIONS = List.of(
        ".exe", ".bat", ".cmd", ".com", ".pif", ".scr", ".vbs", ".js", ".jar",
        ".php", ".asp", ".aspx", ".jsp", ".sh", ".ps1", ".py", ".rb", ".pl",
        ".msi", ".deb", ".rpm", ".dmg", ".app", ".apk", ".ipa"
    );
    
    private static final byte[] ZIP_SIGNATURE = {(byte) 0x50, (byte) 0x4B, (byte) 0x03, (byte) 0x04};
    private static final byte[] RAR_SIGNATURE = {(byte) 0x52, (byte) 0x61, (byte) 0x72, (byte) 0x21, (byte) 0x1A, (byte) 0x07};
    private static final byte[] SEVEN_Z_SIGNATURE = {(byte) 0x37, (byte) 0x7A, (byte) 0xBC, (byte) 0xAF, (byte) 0x27, (byte) 0x1C};
    
    private final ConcurrentHashMap<String, FileIntegrityRecord> integrityRecords;
    private final Map<String, UploadPolicy> uploadPolicies;
    
    private UploadIntegrityManager() {
        this.integrityRecords = new ConcurrentHashMap<>();
        this.uploadPolicies = new HashMap<>();
        initializeDefaultPolicies();
        logger.info("Upload Integrity Manager inicializado");
    }
    
    public static UploadIntegrityManager getInstance() {
        if (instance == null) {
            synchronized (UploadIntegrityManager.class) {
                if (instance == null) {
                    instance = new UploadIntegrityManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * Política de upload para diferentes tipos de conteúdo
     */
    public static class UploadPolicy {
        private final long maxFileSize;
        private final List<String> allowedMimeTypes;
        private final List<String> allowedExtensions;
        private final boolean scanForMalware;
        private final boolean validateIntegrity;
        
        public UploadPolicy(long maxFileSize, List<String> allowedMimeTypes, 
                          List<String> allowedExtensions, boolean scanMalware, boolean validateIntegrity) {
            this.maxFileSize = maxFileSize;
            this.allowedMimeTypes = allowedMimeTypes;
            this.allowedExtensions = allowedExtensions;
            this.scanForMalware = scanMalware;
            this.validateIntegrity = validateIntegrity;
        }
        
        // Getters
        public long getMaxFileSize() { return maxFileSize; }
        public List<String> getAllowedMimeTypes() { return allowedMimeTypes; }
        public List<String> getAllowedExtensions() { return allowedExtensions; }
        public boolean shouldScanForMalware() { return scanForMalware; }
        public boolean shouldValidateIntegrity() { return validateIntegrity; }
    }
    
    /**
     * Registro de integridade de arquivo
     */
    private static class FileIntegrityRecord {
        private final String fileName;
        private final String originalName;
        private final String mimeType;
        private final long fileSize;
        private final String checksumSHA256;
        private final String checksumMD5;
        private final long uploadTime;
        private final String uploadedBy;
        private final String ipAddress;
        
        public FileIntegrityRecord(String fileName, String originalName, String mimeType, 
                                 long fileSize, String sha256, String md5, String uploadedBy, String ipAddress) {
            this.fileName = fileName;
            this.originalName = originalName;
            this.mimeType = mimeType;
            this.fileSize = fileSize;
            this.checksumSHA256 = sha256;
            this.checksumMD5 = md5;
            this.uploadTime = System.currentTimeMillis();
            this.uploadedBy = uploadedBy;
            this.ipAddress = ipAddress;
        }
        
        // Getters
        @SuppressWarnings("unused")
        public String getFileName() { return fileName; }
        @SuppressWarnings("unused")
        public String getOriginalName() { return originalName; }
        @SuppressWarnings("unused")
        public String getMimeType() { return mimeType; }
        public long getFileSize() { return fileSize; }
        public String getChecksumSHA256() { return checksumSHA256; }
        public String getChecksumMD5() { return checksumMD5; }
        @SuppressWarnings("unused")
        public long getUploadTime() { return uploadTime; }
        public String getUploadedBy() { return uploadedBy; }
        @SuppressWarnings("unused")
        public String getIpAddress() { return ipAddress; }
    }
    
    /**
     * Inicializa políticas padrão
     */
    private void initializeDefaultPolicies() {
        // Política para imagens
        uploadPolicies.put("images", new UploadPolicy(
            5 * 1024 * 1024, // 5MB
            List.of("image/jpeg", "image/png", "image/gif", "image/webp"),
            List.of(".jpg", ".jpeg", ".png", ".gif", ".webp"),
            true, true
        ));
        
        // Política para documentos
        uploadPolicies.put("documents", new UploadPolicy(
            10 * 1024 * 1024, // 10MB
            List.of("application/pdf", "text/plain", "text/csv", "application/json"),
            List.of(".pdf", ".txt", ".csv", ".json"),
            true, true
        ));
        
        // Política padrão
        uploadPolicies.put("default", new UploadPolicy(
            MAX_FILE_SIZE,
            ALLOWED_MIME_TYPES,
            List.of(".jpg", ".jpeg", ".png", ".gif", ".pdf", ".txt", ".csv", ".json"),
            true, true
        ));
    }
    
    /**
     * Resultado da validação de upload
     */
    public static class UploadValidationResult {
        private final boolean valid;
        private final String errorMessage;
        private final String warningMessage;
        private final Map<String, Object> metadata;
        
        private UploadValidationResult(boolean valid, String error, String warning, Map<String, Object> metadata) {
            this.valid = valid;
            this.errorMessage = error;
            this.warningMessage = warning;
            this.metadata = metadata;
        }
        
        public static UploadValidationResult valid(Map<String, Object> metadata) {
            return new UploadValidationResult(true, null, null, metadata);
        }
        
        public static UploadValidationResult invalid(String error) {
            return new UploadValidationResult(false, error, null, null);
        }
        
        public static UploadValidationResult warning(String warning, Map<String, Object> metadata) {
            return new UploadValidationResult(true, null, warning, metadata);
        }
        
        // Getters
        public boolean isValid() { return valid; }
        public String getErrorMessage() { return errorMessage; }
        public String getWarningMessage() { return warningMessage; }
        public Map<String, Object> getMetadata() { return metadata; }
    }
    
    /**
     * Valida arquivo uploadado
     */
    public UploadValidationResult validateUpload(String originalName, byte[] fileContent, 
                                              String mimeType, String uploadedBy, String ipAddress) {
        
        // 1. Validação básica de tamanho
        if (fileContent.length > MAX_FILE_SIZE) {
            return UploadValidationResult.invalid("Arquivo excede tamanho máximo permitido");
        }
        
        // 2. Validação de nome de arquivo
        String fileNameValidation = validateFileName(originalName);
        if (fileNameValidation != null) {
            return UploadValidationResult.invalid(fileNameValidation);
        }
        
        // 3. Validação de extensão
        String extensionValidation = validateFileExtension(originalName);
        if (extensionValidation != null) {
            return UploadValidationResult.invalid(extensionValidation);
        }
        
        // 4. Validação de MIME type
        String mimeValidation = validateMimeType(mimeType);
        if (mimeValidation != null) {
            return UploadValidationResult.invalid(mimeValidation);
        }
        
        // 5. Validação de conteúdo (header magic)
        String contentValidation = validateFileContent(fileContent, mimeType);
        if (contentValidation != null) {
            return UploadValidationResult.invalid(contentValidation);
        }
        
        // 6. Cálculo de checksums
        Map<String, Object> checksums = calculateChecksums(fileContent);
        
        // 7. Verificação de malware (simulada)
        String malwareCheck = scanForMalware(fileContent);
        if (malwareCheck != null) {
            return UploadValidationResult.invalid(malwareCheck);
        }
        
        // 8. Validação de integridade estrutural
        String integrityCheck = validateStructuralIntegrity(fileContent, mimeType);
        if (integrityCheck != null) {
            return UploadValidationResult.warning(integrityCheck, checksums);
        }
        
        // 9. Metadados adicionais
        Map<String, Object> metadata = new HashMap<>(checksums);
        metadata.put("file_size", fileContent.length);
        metadata.put("mime_type", mimeType);
        metadata.put("original_name", originalName);
        metadata.put("upload_time", System.currentTimeMillis());
        
        return UploadValidationResult.valid(metadata);
    }
    
    /**
     * Valida nome do arquivo
     */
    private String validateFileName(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return "Nome do arquivo não pode ser vazio";
        }
        
        // Verifica caracteres perigosos
        if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\") ||
            fileName.contains(":") || fileName.contains("*") || fileName.contains("?") ||
            fileName.contains("\"") || fileName.contains("<") || fileName.contains(">") ||
            fileName.contains("|")) {
            return "Nome do arquivo contém caracteres inválidos";
        }
        
        // Verifica tamanho do nome
        if (fileName.length() > 255) {
            return "Nome do arquivo muito longo";
        }
        
        return null;
    }
    
    /**
     * Valida extensão do arquivo
     */
    private String validateFileExtension(String fileName) {
        String lowerName = fileName.toLowerCase();
        
        for (String dangerousExt : DANGEROUS_EXTENSIONS) {
            if (lowerName.endsWith(dangerousExt)) {
                return "Extensão de arquivo não permitida: " + dangerousExt;
            }
        }
        
        return null;
    }
    
    /**
     * Valida MIME type
     */
    private String validateMimeType(String mimeType) {
        if (mimeType == null || mimeType.trim().isEmpty()) {
            return "MIME type não pode ser vazio";
        }
        
        if (!ALLOWED_MIME_TYPES.contains(mimeType.toLowerCase())) {
            return "MIME type não permitido: " + mimeType;
        }
        
        return null;
    }
    
    /**
     * Valida conteúdo do arquivo (magic bytes)
     */
    private String validateFileContent(byte[] content, String expectedMimeType) {
        if (content.length < 4) {
            return "Arquivo muito pequeno para ser válido";
        }
        
        // Verifica se é um arquivo compactado disfarçado
        if (isCompressedFile(content)) {
            return "Arquivos compactados não são permitidos";
        }
        
        // Validação específica por MIME type
        switch (expectedMimeType.toLowerCase()) {
            case "image/jpeg":
                if (!isJPEG(content)) return "Conteúdo não corresponde ao MIME type JPEG";
                break;
            case "image/png":
                if (!isPNG(content)) return "Conteúdo não corresponde ao MIME type PNG";
                break;
            case "image/gif":
                if (!isGIF(content)) return "Conteúdo não corresponde ao MIME type GIF";
                break;
            case "application/pdf":
                if (!isPDF(content)) return "Conteúdo não corresponde ao MIME type PDF";
                break;
        }
        
        return null;
    }
    
    /**
     * Verifica se arquivo é compactado
     */
    private boolean isCompressedFile(byte[] content) {
        if (content.length < 4) return false;
        
        // ZIP
        if (content[0] == ZIP_SIGNATURE[0] && content[1] == ZIP_SIGNATURE[1] && 
            content[2] == ZIP_SIGNATURE[2] && content[3] == ZIP_SIGNATURE[3]) {
            return true;
        }
        
        // RAR
        if (content.length >= 6 && content[0] == RAR_SIGNATURE[0] && content[1] == RAR_SIGNATURE[1] && 
            content[2] == RAR_SIGNATURE[2] && content[3] == RAR_SIGNATURE[3] && 
            content[4] == RAR_SIGNATURE[4] && content[5] == RAR_SIGNATURE[5]) {
            return true;
        }
        
        // 7-Zip
        if (content.length >= 6 && content[0] == SEVEN_Z_SIGNATURE[0] && content[1] == SEVEN_Z_SIGNATURE[1] && 
            content[2] == SEVEN_Z_SIGNATURE[2] && content[3] == SEVEN_Z_SIGNATURE[3] && 
            content[4] == SEVEN_Z_SIGNATURE[4] && content[5] == SEVEN_Z_SIGNATURE[5]) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Validações específicas de formato
     */
    private boolean isJPEG(byte[] content) {
        return content.length >= 2 && content[0] == (byte) 0xFF && content[1] == (byte) 0xD8;
    }
    
    private boolean isPNG(byte[] content) {
        return content.length >= 8 && 
               content[0] == (byte) 0x89 && content[1] == 0x50 && content[2] == 0x4E && content[3] == 0x47 &&
               content[4] == 0x0D && content[5] == 0x0A && content[6] == 0x1A && content[7] == 0x0A;
    }
    
    private boolean isGIF(byte[] content) {
        return content.length >= 6 && 
               content[0] == 0x47 && content[1] == 0x49 && content[2] == 0x46 &&
               (content[3] == 0x38 && (content[4] == 0x37 || content[4] == 0x39));
    }
    
    private boolean isPDF(byte[] content) {
        return content.length >= 4 && 
               content[0] == 0x25 && content[1] == 0x50 && content[2] == 0x44 && content[3] == 0x46;
    }
    
    /**
     * Calcula checksums do arquivo
     */
    private Map<String, Object> calculateChecksums(byte[] content) {
        Map<String, Object> checksums = new HashMap<>();
        
        try {
            // SHA-256
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] sha256Hash = sha256.digest(content);
            checksums.put("sha256", bytesToHex(sha256Hash));
            
            // MD5
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] md5Hash = md5.digest(content);
            checksums.put("md5", bytesToHex(md5Hash));
            
        } catch (NoSuchAlgorithmException e) {
            logger.error("Erro ao calcular checksums", e);
        }
        
        return checksums;
    }
    
    /**
     * Converte bytes para hex
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
    
    /**
     * Scan para malware (simulação)
     */
    private String scanForMalware(byte[] content) {
        // Em produção, integrar com antivirus real
        // Por enquanto, verificação básica de padrões suspeitos
        
        String contentStr = new String(content, java.nio.charset.StandardCharsets.UTF_8);
        
        // Padrões suspeitos
        String[] suspiciousPatterns = {
            "eval(", "base64_decode(", "shell_exec(", "system(", "passthru(",
            "<?php", "<%", "<script", "javascript:",
            "cmd.exe", "powershell", "/bin/sh", "/bin/bash"
        };
        
        for (String pattern : suspiciousPatterns) {
            if (contentStr.toLowerCase().contains(pattern.toLowerCase())) {
                logger.warn("Padrão suspeito detectado no upload: {}", pattern);
                return "Conteúdo potencialmente malicioso detectado";
            }
        }
        
        return null;
    }
    
    /**
     * Valida integridade estrutural
     */
    private String validateStructuralIntegrity(byte[] content, String mimeType) {
        try {
            // Validação básica de integridade
            if (content.length == 0) {
                return "Arquivo vazio";
            }
            
            // Verifica se há bytes nulos no meio (possível corrupção)
            boolean hasNullBytes = false;
            for (int i = 1; i < content.length - 1; i++) {
                if (content[i] == 0) {
                    hasNullBytes = true;
                    break;
                }
            }
            
            if (hasNullBytes) {
                return "Arquivo pode estar corrompido (bytes nulos detectados)";
            }
            
            // Validações específicas por tipo
            switch (mimeType.toLowerCase()) {
                case "image/jpeg":
                    return validateJPEGIntegrity(content);
                case "image/png":
                    return validatePNGIntegrity(content);
                case "application/pdf":
                    return validatePDFIntegrity(content);
            }
            
        } catch (Exception e) {
            logger.warn("Erro na validação de integridade", e);
            return "Erro na validação de integridade";
        }
        
        return null;
    }
    
    private String validateJPEGIntegrity(byte[] content) {
        // Verifica se tem assinatura JPEG no final
        if (content.length < 4) return "Arquivo JPEG muito pequeno";
        
        // Procura por EOI marker (FF D9)
        for (int i = content.length - 2; i >= 0; i--) {
            if (content[i] == (byte) 0xFF && i + 1 < content.length && content[i + 1] == (byte) 0xD9) {
                return null; // Encontrou fim válido
            }
        }
        
        return "Arquivo JPEG não termina corretamente";
    }
    
    private String validatePNGIntegrity(byte[] content) {
        if (content.length < 12) return "Arquivo PNG muito pequeno";
        
        // Verifica CRC do chunk IHDR
        // Implementação simplificada
        return null;
    }
    
    private String validatePDFIntegrity(byte[] content) {
        // Verifica se termina com %%EOF
        String contentStr = new String(content, java.nio.charset.StandardCharsets.UTF_8);
        if (!contentStr.trim().endsWith("%%EOF")) {
            return "Arquivo PDF não termina corretamente";
        }
        return null;
    }
    
    /**
     * Registra arquivo uploadado
     */
    public void recordUpload(String fileName, String originalName, String mimeType, 
                           long fileSize, String sha256, String md5, String uploadedBy, String ipAddress) {
        FileIntegrityRecord record = new FileIntegrityRecord(fileName, originalName, mimeType, 
                                                            fileSize, sha256, md5, uploadedBy, ipAddress);
        
        integrityRecords.put(fileName, record);
        logger.info("Upload registrado: {} por {} desde {}", originalName, uploadedBy, ipAddress);
    }
    
    /**
     * Verifica integridade de arquivo existente
     */
    public boolean verifyFileIntegrity(String fileName, byte[] currentContent) {
        FileIntegrityRecord record = integrityRecords.get(fileName);
        if (record == null) {
            return false;
        }
        
        try {
            // Recalcula checksums
            Map<String, Object> currentChecksums = calculateChecksums(currentContent);
            String currentSHA256 = (String) currentChecksums.get("sha256");
            String currentMD5 = (String) currentChecksums.get("md5");
            
            // Compara com registros
            return record.getChecksumSHA256().equals(currentSHA256) && 
                   record.getChecksumMD5().equals(currentMD5);
                   
        } catch (Exception e) {
            logger.error("Erro ao verificar integridade do arquivo: {}", fileName, e);
            return false;
        }
    }
    
    /**
     * Obtém estatísticas de uploads
     */
    public Map<String, Object> getUploadStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total_uploads", integrityRecords.size());
        stats.put("total_size_mb", integrityRecords.values().stream()
            .mapToLong(FileIntegrityRecord::getFileSize)
            .sum() / (1024 * 1024));
        stats.put("unique_uploaders", integrityRecords.values().stream()
            .map(FileIntegrityRecord::getUploadedBy)
            .distinct()
            .count());
        return stats;
    }
}
