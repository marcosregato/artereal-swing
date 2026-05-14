package com.artereal.swing.application.loja;

/**
 * DTO para atualização de loja
 */
public record AtualizarLojaRequest(
    String nome,
    String cnpj,
    String endereco,
    String cidade,
    String estado
) {}
