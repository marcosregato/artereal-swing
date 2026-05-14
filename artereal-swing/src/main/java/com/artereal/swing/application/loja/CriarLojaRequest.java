package com.artereal.swing.application.loja;


/**
 * DTO para requisição de criação de loja
 * 
 * Este record representa os dados necessários para criar uma nova loja,
 * servindo como contrato de entrada para o use case CriarLojaUseCase.
 */
public record CriarLojaRequest(
    String nome,
    String cnpj,
    String endereco,
    String cidade,
    String estado
) {
    
    public CriarLojaRequest {
        validate();
    }
    
    /**
     * Validações básicas do request
     */
    private void validate() {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da loja é obrigatório");
        }
        if (cnpj == null || cnpj.trim().isEmpty()) {
            throw new IllegalArgumentException("CNPJ da loja é obrigatório");
        }
        if (endereco == null || endereco.trim().isEmpty()) {
            throw new IllegalArgumentException("Endereço da loja é obrigatório");
        }
        if (cidade == null || cidade.trim().isEmpty()) {
            throw new IllegalArgumentException("Cidade da loja é obrigatória");
        }
        if (estado == null || estado.trim().isEmpty()) {
            throw new IllegalArgumentException("Estado da loja é obrigatório");
        }
        
        // Validação de tamanho
        if (nome.length() > 200) {
            throw new IllegalArgumentException("Nome da loja não pode exceder 200 caracteres");
        }
        if (endereco.length() > 500) {
            throw new IllegalArgumentException("Endereço não pode exceder 500 caracteres");
        }
        if (cidade.length() > 100) {
            throw new IllegalArgumentException("Cidade não pode exceder 100 caracteres");
        }
        if (estado.length() > 50) {
            throw new IllegalArgumentException("Estado não pode exceder 50 caracteres");
        }
        
        // Validação de formato
        if (!isValidCnpj(cnpj)) {
            throw new IllegalArgumentException("CNPJ inválido");
        }
    }
    
    /**
     * Validação simples de CNPJ
     */
    private boolean isValidCnpj(String cnpj) {
        String cnpjLimpo = cnpj.replaceAll("[^0-9]", "");
        return cnpjLimpo.length() == 14;
    }
    
    /**
     * Formata o CNPJ para exibição
     */
    public String getCnpjFormatado() {
        String cnpjLimpo = cnpj.replaceAll("[^0-9]", "");
        if (cnpjLimpo.length() != 14) {
            return cnpj;
        }
        
        return String.format("%s.%s.%s/%s-%s",
            cnpjLimpo.substring(0, 2),
            cnpjLimpo.substring(2, 5),
            cnpjLimpo.substring(5, 8),
            cnpjLimpo.substring(8, 12),
            cnpjLimpo.substring(12, 14)
        );
    }
    
    /**
     * Retorna o endereço completo formatado
     */
    public String getEnderecoCompleto() {
        return String.format("%s, %s - %s/%s", endereco, cidade, estado, cnpj);
    }
}
