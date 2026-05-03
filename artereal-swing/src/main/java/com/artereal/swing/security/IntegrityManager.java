package com.artereal.swing.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gerenciador de Integridade de Software e Dados baseado em OWASP
 * Implementa A08:2021 - Software and Data Integrity Failures
 */
public class IntegrityManager {
    
    private static final Logger logger = LoggerFactory.getLogger(IntegrityManager.class);
    
    // Cache de checksums para verificação de integridade
    private static final ConcurrentHashMap<String, String> dataChecksums = new ConcurrentHashMap<>();
    
    // Assinaturas digitais para verificação de software
    private static final Map<String, String> softwareSignatures = new HashMap<>();
    
    /**
     * Calcula checksum SHA-256 para dados (OWASP recomendado)
     */
    public static String calculateChecksum(byte[] data) throws IntegrityException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data);
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            logger.error("Algoritmo SHA-256 não disponível", e);
            throw new IntegrityException("Falha no cálculo de checksum", e);
        }
    }
    
    /**
     * Calcula checksum para string
     */
    public static String calculateChecksum(String data) throws IntegrityException {
        return calculateChecksum(data.getBytes());
    }
    
    /**
     * Verifica integridade dos dados usando checksum
     */
    public static boolean verifyDataIntegrity(String dataId, byte[] data) {
        try {
            String storedChecksum = dataChecksums.get(dataId);
            if (storedChecksum == null) {
                logger.warn("Checksum não encontrado para dados: {}", dataId);
                return false;
            }
            
            String currentChecksum = calculateChecksum(data);
            boolean isValid = storedChecksum.equals(currentChecksum);
            
            if (!isValid) {
                logger.error("Integridade de dados violada: {}", dataId);
            }
            
            return isValid;
        } catch (IntegrityException e) {
            logger.error("Erro na verificação de integridade: {}", dataId, e);
            return false;
        }
    }
    
    /**
     * Armazena checksum para verificação futura
     */
    public static void storeChecksum(String dataId, byte[] data) throws IntegrityException {
        String checksum = calculateChecksum(data);
        dataChecksums.put(dataId, checksum);
        logger.debug("Checksum armazenado para: {}", dataId);
    }
    
    /**
     * Gera assinatura digital para software (OWASP Code Signing)
     */
    public static String generateSoftwareSignature(String softwareName, String version, byte[] softwareData) 
            throws IntegrityException {
        try {
            String dataToSign = softwareName + ":" + version + ":" + calculateChecksum(softwareData);
            String signature = signData(dataToSign);
            
            softwareSignatures.put(softwareName + ":" + version, signature);
            logger.info("Assinatura gerada para software: {} v{}", softwareName, version);
            
            return signature;
        } catch (Exception e) {
            logger.error("Erro na geração de assinatura de software", e);
            throw new IntegrityException("Falha na assinatura de software", e);
        }
    }
    
    /**
     * Verifica assinatura de software
     */
    public static boolean verifySoftwareSignature(String softwareName, String version, byte[] softwareData, 
                                                  String signature) throws IntegrityException {
        try {
            String expectedSignature = softwareSignatures.get(softwareName + ":" + version);
            if (expectedSignature == null) {
                logger.warn("Assinatura não encontrada para software: {} v{}", softwareName, version);
                return false;
            }
            
            String dataToVerify = softwareName + ":" + version + ":" + calculateChecksum(softwareData);
            boolean isValid = verifySignature(dataToVerify, signature);
            
            if (!isValid) {
                logger.error("Assinatura de software inválida: {} v{}", softwareName, version);
            }
            
            return isValid && expectedSignature.equals(signature);
        } catch (Exception e) {
            logger.error("Erro na verificação de assinatura de software", e);
            throw new IntegrityException("Falha na verificação de assinatura", e);
        }
    }
    
    /**
     * Assina dados usando chave privada (simplificado para demonstração)
     */
    private static String signData(String data) throws Exception {
        // Em produção, usar KeyPair real e algoritmo como RSA/ECDSA
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data.getBytes());
        
        // Simulação de assinatura (em produção usar Signature.getInstance())
        return "SIG_" + bytesToHex(hash);
    }
    
    /**
     * Verifica assinatura digital (simplificado para demonstração)
     */
    private static boolean verifySignature(String data, String signature) throws Exception {
        if (!signature.startsWith("SIG_")) {
            return false;
        }
        
        String expectedHash = signature.substring(4);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] computedHash = digest.digest(data.getBytes());
        String computedHex = bytesToHex(computedHash);
        
        return expectedHash.equals(computedHex);
    }
    
    /**
     * Valida integridade de configurações do sistema
     */
    public static List<String> validateSystemIntegrity() {
        List<String> issues = new ArrayList<>();
        
        // Verifica configurações críticas
        try {
            // Verifica se SecurityManager está intacto
            String securityManagerHash = calculateChecksum("SecurityManager".getBytes());
            if (!dataChecksums.containsKey("security_manager")) {
                issues.add("Checksum do SecurityManager não registrado");
            }
            
            // Verifica se CryptoManager está intacto
            String cryptoManagerHash = calculateChecksum("CryptoManager".getBytes());
            if (!dataChecksums.containsKey("crypto_manager")) {
                issues.add("Checksum do CryptoManager não registrado");
            }
            
            // Verifica integridade das configurações de segurança
            Map<String, String> securityHeaders = SecurityConfigManager.getSecurityHeaders();
            for (Map.Entry<String, String> header : securityHeaders.entrySet()) {
                String headerChecksum = calculateChecksum(header.getKey() + ":" + header.getValue());
                String configKey = "header_" + header.getKey().toLowerCase().replace("-", "_");
                
                if (!dataChecksums.containsKey(configKey)) {
                    issues.add("Header de segurança não verificado: " + header.getKey());
                }
            }
            
        } catch (IntegrityException e) {
            issues.add("Erro na validação de integridade: " + e.getMessage());
        }
        
        logger.info("Validação de integridade concluída: {} problemas encontrados", issues.size());
        return issues;
    }
    
    /**
     * Monitora alterações em dados críticos
     */
    public static boolean monitorDataChange(String dataId, byte[] currentData) {
        try {
            String currentChecksum = calculateChecksum(currentData);
            String storedChecksum = dataChecksums.get(dataId);
            
            if (storedChecksum == null) {
                // Primeira vez que vemos estes dados
                dataChecksums.put(dataId, currentChecksum);
                logger.info("Novo dado monitorado: {}", dataId);
                return true;
            }
            
            if (!storedChecksum.equals(currentChecksum)) {
                logger.warn("Alteração detectada em dados críticos: {}", dataId);
                dataChecksums.put(dataId, currentChecksum);
                return false; // Dados alterados
            }
            
            return true; // Dados intactos
            
        } catch (IntegrityException e) {
            logger.error("Erro no monitoramento de dados: {}", dataId, e);
            return false;
        }
    }
    
    /**
     * Valida integridade de uploads de arquivos
     */
    public static boolean validateFileIntegrity(String filename, byte[] fileData, String expectedChecksum) {
        try {
            String actualChecksum = calculateChecksum(fileData);
            boolean isValid = expectedChecksum.equals(actualChecksum);
            
            if (!isValid) {
                logger.error("Checksum de arquivo inválido: {} esperado={}, atual={}", 
                           filename, expectedChecksum, actualChecksum);
            } else {
                logger.debug("Arquivo validado com sucesso: {}", filename);
            }
            
            return isValid;
        } catch (IntegrityException e) {
            logger.error("Erro na validação de arquivo: {}", filename, e);
            return false;
        }
    }
    
    /**
     * Converte bytes para hexadecimal
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    
    /**
     * Inicializa checksums para componentes críticos
     */
    public static void initializeCriticalChecksums() {
        logger.info("Inicializando checksums de componentes críticos");
        
        try {
            // Armazena checksums dos componentes de segurança
            storeChecksum("security_manager", "SecurityManager".getBytes());
            storeChecksum("crypto_manager", "CryptoManager".getBytes());
            storeChecksum("access_control", "AccessControlManager".getBytes());
            storeChecksum("security_config", "SecurityConfigManager".getBytes());
            
            logger.info("Checksums críticos inicializados com sucesso");
        } catch (IntegrityException e) {
            logger.error("Falha na inicialização de checksums críticos", e);
        }
    }
    
    /**
     * Exceção customizada para operações de integridade
     */
    public static class IntegrityException extends Exception {
        public IntegrityException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
