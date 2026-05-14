package com.artereal.swing.model;

import java.time.LocalDateTime;

/**
 * Record otimizado para Gestão Documental
 * Substitui a classe Documento com design moderno e imutável
 */
public record DocumentoRecord(
    Long id,
    Long codigoIrmao,
    String nomeArquivo,
    String caminhoArquivo,
    String tipo, // IDENTIDADE, DIPLOMA, CERTIFICADO, OUTRO
    String descricao,
    LocalDateTime dataUpload,
    LocalDateTime dataExpiracao,
    String status, // ATIVO, EXPIRADO, CANCELADO
    String assinaturaDigital,
    String hashArquivo,
    double tamanhoArquivo,
    String formatoArquivo,
    String usuarioUpload,
    boolean ativo,
    String createdAt,
    String updatedAt
) {
    
    // Construtor padrão para novos documentos
    public DocumentoRecord(Long codigoIrmao, String nomeArquivo, String tipo) {
        this(
            null,
            codigoIrmao,
            nomeArquivo,
            null, // caminhoArquivo será definido posteriormente
            tipo,
            null, // descrição será definida posteriormente
            LocalDateTime.now(),
            null, // dataExpiracao será definida posteriormente
            "ATIVO",
            null, // assinaturaDigital será definida posteriormente
            null, // hashArquivo será definido posteriormente
            0.0, // tamanhoArquivo será definido posteriormente
            null, // formatoArquivo será definido posteriormente
            null, // usuarioUpload será definido posteriormente
            true,
            null,
            null
        );
    }
    
    // Métodos utilitários
    public boolean isExpirado() {
        return dataExpiracao != null && dataExpiracao.isBefore(LocalDateTime.now());
    }
    
    public boolean isProximoExpiracao() {
        return dataExpiracao != null && 
               dataExpiracao.isBefore(LocalDateTime.now().plusDays(30)) && 
               dataExpiracao.isAfter(LocalDateTime.now());
    }
    
    public boolean temAssinaturaDigital() {
        return assinaturaDigital != null && !assinaturaDigital.trim().isEmpty();
    }
    
    public String getTamanhoFormatado() {
        if (tamanhoArquivo < 1024) {
            return String.format("%.0f B", tamanhoArquivo);
        } else if (tamanhoArquivo < 1024 * 1024) {
            return String.format("%.1f KB", tamanhoArquivo / 1024);
        } else {
            return String.format("%.1f MB", tamanhoArquivo / (1024 * 1024));
        }
    }
    
    // Métodos de transição de estado
    public DocumentoRecord assinarDigitalmente(String assinatura) {
        return new DocumentoRecord(
            id, codigoIrmao, nomeArquivo, caminhoArquivo,
            tipo, descricao, dataUpload, dataExpiracao,
            status, assinatura, hashArquivo, tamanhoArquivo,
            formatoArquivo, usuarioUpload, ativo, createdAt, updatedAt
        );
    }
    
    public DocumentoRecord cancelar() {
        return new DocumentoRecord(
            id, codigoIrmao, nomeArquivo, caminhoArquivo,
            tipo, descricao, dataUpload, dataExpiracao,
            "CANCELADO", assinaturaDigital, hashArquivo,
            tamanhoArquivo, formatoArquivo, usuarioUpload,
            false, createdAt, updatedAt
        );
    }
}
