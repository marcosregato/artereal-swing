package com.artereal.swing.domain.loja.valueobjects;

/**
 * Value Object para Endereço
 * 
 * Este Value Object representa um endereço imutável
 * seguindo os princípios de DDD.
 */
public record Endereco(
    String rua,
    String numero,
    String complemento,
    String bairro,
    String cidade,
    String estado,
    String cep
) {
    public Endereco {
        validate();
    }
    
    private void validate() {
        if (rua == null || rua.trim().isEmpty()) {
            throw new IllegalArgumentException("Rua é obrigatória");
        }
        if (cidade == null || cidade.trim().isEmpty()) {
            throw new IllegalArgumentException("Cidade é obrigatória");
        }
        if (estado == null || estado.trim().isEmpty()) {
            throw new IllegalArgumentException("Estado é obrigatório");
        }
        if (cep == null || cep.trim().isEmpty()) {
            throw new IllegalArgumentException("CEP é obrigatório");
        }
    }
    
    public String getEnderecoCompleto() {
        StringBuilder sb = new StringBuilder();
        sb.append(rua);
        if (numero != null && !numero.trim().isEmpty()) {
            sb.append(", ").append(numero);
        }
        if (complemento != null && !complemento.trim().isEmpty()) {
            sb.append(" - ").append(complemento);
        }
        if (bairro != null && !bairro.trim().isEmpty()) {
            sb.append(", ").append(bairro);
        }
        sb.append(", ").append(cidade);
        sb.append(" - ").append(estado);
        if (cep != null && !cep.trim().isEmpty()) {
            sb.append(", CEP: ").append(cep);
        }
        return sb.toString();
    }
}
