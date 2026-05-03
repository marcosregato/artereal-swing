package com.artereal.swing.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.util.Base64;

/**
 * Gerenciador Criptográfico baseado em recomendações OWASP
 * Implementa A02:2021 - Cryptographic Failures
 */
public class CryptoManager {
    
    private static final Logger logger = LoggerFactory.getLogger(CryptoManager.class);
    
    // Algoritmos recomendados OWASP
    private static final String AES_ALGORITHM = "AES";
    private static final String AES_TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int AES_KEY_LENGTH = 256;
    private static final int GCM_TAG_LENGTH = 128;
    private static final int PBKDF2_ITERATIONS = 100000;
    private static final int SALT_LENGTH = 32;
    
    // Chave mestre obtida do SecretsManager OWASP
    private static String getMasterKey() {
        String key = SecretsManager.getSecret("crypto.master.key");
        if (key == null) {
            throw new RuntimeException("Crypto master key não configurado no SecretsManager");
        }
        return key;
    }
    
    /**
     * Criptografa dados usando AES-GCM (OWASP recomendado)
     */
    public static String encrypt(String plaintext) throws CryptoException {
        try {
            // Gera salt aleatório
            byte[] salt = generateSalt();
            
            // Deriva chave usando PBKDF2
            SecretKey secretKey = deriveKey(getMasterKey().getBytes(), salt);
            
            // Configura AES-GCM
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            byte[] iv = new byte[12]; // IV de 96 bits para GCM
            new SecureRandom().nextBytes(iv);
            
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec);
            
            byte[] ciphertext = cipher.doFinal(plaintext.getBytes());
            
            // Combina salt + IV + ciphertext
            byte[] encrypted = new byte[salt.length + iv.length + ciphertext.length];
            System.arraycopy(salt, 0, encrypted, 0, salt.length);
            System.arraycopy(iv, 0, encrypted, salt.length, iv.length);
            System.arraycopy(ciphertext, 0, encrypted, salt.length + iv.length, ciphertext.length);
            
            return Base64.getEncoder().encodeToString(encrypted);
            
        } catch (Exception e) {
            logger.error("Erro na criptografia", e);
            throw new CryptoException("Falha na criptografia", e);
        }
    }
    
    /**
     * Descriptografa dados usando AES-GCM
     */
    public static String decrypt(String encrypted) throws CryptoException {
        try {
            byte[] encryptedBytes = Base64.getDecoder().decode(encrypted);
            
            // Extrai salt, IV e ciphertext
            byte[] salt = new byte[SALT_LENGTH];
            byte[] iv = new byte[12];
            byte[] ciphertext = new byte[encryptedBytes.length - SALT_LENGTH - 12];
            
            System.arraycopy(encryptedBytes, 0, salt, 0, SALT_LENGTH);
            System.arraycopy(encryptedBytes, SALT_LENGTH, iv, 0, 12);
            System.arraycopy(encryptedBytes, SALT_LENGTH + 12, ciphertext, 0, ciphertext.length);
            
            // Deriva chave
            SecretKey secretKey = deriveKey(getMasterKey().getBytes(), salt);
            
            // Configura AES-GCM
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSpec);
            
            byte[] plaintext = cipher.doFinal(ciphertext);
            return new String(plaintext);
            
        } catch (Exception e) {
            logger.error("Erro na descriptografia", e);
            throw new CryptoException("Falha na descriptografia", e);
        }
    }
    
    /**
     * Gera hash seguro com salt (OWASP recomendado)
     */
    public static String hashPassword(String password) throws CryptoException {
        try {
            byte[] salt = generateSalt();
            byte[] hash = hashWithPBKDF2(password, salt);
            
            // Combina salt + hash
            byte[] combined = new byte[salt.length + hash.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(hash, 0, combined, salt.length, hash.length);
            
            return Base64.getEncoder().encodeToString(combined);
            
        } catch (Exception e) {
            logger.error("Erro no hash de senha", e);
            throw new CryptoException("Falha no hash de senha", e);
        }
    }
    
    /**
     * Verifica hash de senha
     */
    public static boolean verifyPassword(String password, String hashedPassword) throws CryptoException {
        try {
            byte[] combined = Base64.getDecoder().decode(hashedPassword);
            
            byte[] salt = new byte[SALT_LENGTH];
            byte[] storedHash = new byte[combined.length - SALT_LENGTH];
            
            System.arraycopy(combined, 0, salt, 0, SALT_LENGTH);
            System.arraycopy(combined, SALT_LENGTH, storedHash, 0, storedHash.length);
            
            byte[] computedHash = hashWithPBKDF2(password, salt);
            
            return MessageDigest.isEqual(storedHash, computedHash);
            
        } catch (Exception e) {
            logger.error("Erro na verificação de senha", e);
            throw new CryptoException("Falha na verificação de senha", e);
        }
    }
    
    /**
     * Deriva chave usando PBKDF2 (OWASP recomendado)
     */
    private static SecretKey deriveKey(byte[] password, byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(
            new String(password).toCharArray(),
            salt,
            PBKDF2_ITERATIONS,
            AES_KEY_LENGTH / 8
        );
        
        SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        
        return new SecretKeySpec(keyBytes, AES_ALGORITHM);
    }
    
    /**
     * Gera hash com PBKDF2
     */
    private static byte[] hashWithPBKDF2(String password, byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(
            password.toCharArray(),
            salt,
            PBKDF2_ITERATIONS,
            32 // 256 bits
        );
        
        SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        return factory.generateSecret(spec).getEncoded();
    }
    
    /**
     * Gera salt aleatório
     */
    private static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return salt;
    }
    
    /**
     * Gera token seguro para CSRF
     */
    public static String generateCSRFToken() {
        byte[] token = new byte[32];
        new SecureRandom().nextBytes(token);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(token);
    }
    
    /**
     * Gera session ID seguro
     */
    public static String generateSessionId() {
        byte[] sessionId = new byte[64];
        new SecureRandom().nextBytes(sessionId);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(sessionId);
    }
    
    /**
     * Exceção customizada para operações criptográficas
     */
    public static class CryptoException extends Exception {
        public CryptoException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
