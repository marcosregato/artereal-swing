package com.artereal.swing.model;

import java.time.LocalDate;

/**
 * Record otimizado para Irmãos
 * Substitui a classe Irmao com design moderno e imutável
 */
public record IrmaoRecord(
    Long id,
    String nome,
    String apelido,
    String cpf,
    String rg,
    String dataNascimento,
    String endereco,
    String bairro,
    String cidade,
    String estado,
    String cep,
    String telefone,
    String celular,
    String email,
    String cargo,
    String loja,
    String dataIniciacao,
    String dataRegularizacao,
    String dataUltimoPagamento,
    double valorMensalidade,
    String status,
    String observacoes,
    String createdAt,
    String updatedAt
) {
    
    // Construtor padrão para novos irmãos
    public IrmaoRecord(String nome, String cpf, String dataNascimento, String loja) {
        this(
            null,
            nome,
            null, // apelido será definido posteriormente
            cpf,
            null, // rg será definido posteriormente
            dataNascimento,
            null, // endereço será definido posteriormente
            null, // bairro será definido posteriormente
            null, // cidade será definida posteriormente
            null, // estado será definido posteriormente
            null, // cep será definido posteriormente
            null, // telefone será definido posteriormente
            null, // celular será definido posteriormente
            null, // email será definido posteriormente
            null, // cargo será definido posteriormente
            loja,
            null, // dataIniciacao será definida posteriormente
            null, // dataRegularizacao será definida posteriormente
            null, // dataUltimoPagamento será definida posteriormente
            0.0, // valorMensalidade será definido posteriormente
            "ATIVO",
            null, // observações serão definidas posteriormente
            null,
            null
        );
    }
    
    // Métodos de validação
    public boolean isAtivo() {
        return status != null && status.equals("ATIVO");
    }
    
    public boolean isInadimplente() {
        return dataUltimoPagamento != null && 
               LocalDate.parse(dataUltimoPagamento).isBefore(LocalDate.now().minusMonths(2));
    }
    
    public int getIdade() {
        if (dataNascimento == null) return 0;
        return LocalDate.now().getYear() - LocalDate.parse(dataNascimento).getYear();
    }
    
    public String getNomeCompleto() {
        return apelido != null && !apelido.trim().isEmpty() 
               ? nome + " (" + apelido + ")" 
               : nome;
    }
    
    // Métodos de formatação
    public String getValorMensalidadeFormatado() {
        return String.format("R$ %.2f", valorMensalidade);
    }
    
    public String getDataNascimentoFormatada() {
        if (dataNascimento == null) return "Não definida";
        return dataNascimento; // Já está no formato dd/MM/yyyy
    }
    
    public String getLojaFormatada() {
        return loja != null ? loja : "Não definida";
    }
}
