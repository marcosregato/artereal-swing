package com.artereal.swing.application.irmao;

/**
 * DTO para resposta de operações com irmãos
 */
public record IrmaoResponse(
    Long id,
    String nome,
    String cpf,
    String telefone,
    String email,
    String status,
    Long lojaId,
    String dataCriacao,
    String dataAtualizacao
) {
    public boolean isAtivo() {
        return "ATIVO".equals(status);
    }
    
    public boolean isAssociado() {
        return lojaId != null;
    }
}
