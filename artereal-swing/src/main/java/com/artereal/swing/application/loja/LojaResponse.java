package com.artereal.swing.application.loja;

/**
 * DTO para resposta de dados da loja
 * 
 * Este record representa os dados de uma loja para serem
 * retornados pelos use cases, servindo como contrato de saída.
 */
public record LojaResponse(
    Long id,
    String nome,
    String cnpj,
    String endereco,
    String cidade,
    String estado,
    String status,
    String enderecoCompleto,
    int quantidadeIrmaos,
    int quantidadeCaixas,
    String dataCriacao,
    String dataAtualizacao
) {
    
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
     * Verifica se a loja está ativa
     */
    public boolean isAtiva() {
        return "ATIVA".equals(status);
    }
    
    /**
     * Retorna o endereço completo formatado
     */
    public String getEnderecoCompletoFormatado() {
        return String.format("%s, %s - %s/%s", endereco, cidade, estado, getCnpjFormatado());
    }
}
