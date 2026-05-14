package com.artereal.swing.application.irmao;

/**
 * DTO para criação de um novo irmão
 */
public record CriarIrmaoRequest(
    String nome,
    String cpf,
    String telefone,
    String email,
    Long lojaId
) {
    public CriarIrmaoRequest {
        validate();
    }
    
    private void validate() {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do irmão é obrigatório");
        }
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF do irmão é obrigatório");
        }
        if (nome.length() > 100) {
            throw new IllegalArgumentException("Nome do irmão não pode exceder 100 caracteres");
        }
    }
}
