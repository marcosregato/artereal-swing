package com.artereal.swing.model;

import java.time.LocalDate;

/**
 * Modelo de dados para Candidatos e Profanos
 */
public class Candidato extends SimpleModel {
    
    private Long id;
    private String nome;
    private String endereco;
    private String numero;
    private String cidade;
    private String estado;
    private String bairro;
    private String foneResidencial;
    private LocalDate dataNascimento;
    private int idade;
    private String estadoCivil;
    private String esposa;
    private String profissao;
    private String funcao;
    private String localTrabalho;
    private String ondeExerce;
    private String informacoes;
    private String chanceler;
    private String veneravel;
    private String secretario;
    private String linhaNegra;
    private String status; // CANDIDATO, INICIADO, REJEITADO, APROVADO
    private LocalDate dataCadastro;
    private LocalDate dataStatus;
    private String observacoes;
    
    public Candidato() {}
    
    public Candidato(String nome, String endereco) {
        this.nome = nome;
        this.endereco = endereco;
        this.dataCadastro = LocalDate.now();
        this.status = "CANDIDATO";
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }
    
    public String getFoneResidencial() { return foneResidencial; }
    public void setFoneResidencial(String foneResidencial) { this.foneResidencial = foneResidencial; }
    
    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
    
    public int getIdade() { return idade; }
    public void setIdade(int idade) { this.idade = idade; }
    
    public String getEstadoCivil() { return estadoCivil; }
    public void setEstadoCivil(String estadoCivil) { this.estadoCivil = estadoCivil; }
    
    public String getEsposa() { return esposa; }
    public void setEsposa(String esposa) { this.esposa = esposa; }
    
    public String getProfissao() { return profissao; }
    public void setProfissao(String profissao) { this.profissao = profissao; }
    
    public String getFuncao() { return funcao; }
    public void setFuncao(String funcao) { this.funcao = funcao; }
    
    public String getLocalTrabalho() { return localTrabalho; }
    public void setLocalTrabalho(String localTrabalho) { this.localTrabalho = localTrabalho; }
    
    public String getOndeExerce() { return ondeExerce; }
    public void setOndeExerce(String ondeExerce) { this.ondeExerce = ondeExerce; }
    
    public String getInformacoes() { return informacoes; }
    public void setInformacoes(String informacoes) { this.informacoes = informacoes; }
    
    public String getChanceler() { return chanceler; }
    public void setChanceler(String chanceler) { this.chanceler = chanceler; }
    
    public String getVeneravel() { return veneravel; }
    public void setVeneravel(String veneravel) { this.veneravel = veneravel; }
    
    public String getSecretario() { return secretario; }
    public void setSecretario(String secretario) { this.secretario = secretario; }
    
    public String getLinhaNegra() { return linhaNegra; }
    public void setLinhaNegra(String linhaNegra) { this.linhaNegra = linhaNegra; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDate getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDate dataCadastro) { this.dataCadastro = dataCadastro; }
    
    public LocalDate getDataStatus() { return dataStatus; }
    public void setDataStatus(LocalDate dataStatus) { this.dataStatus = dataStatus; }
    
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    
    // Métodos utilitários
    public void aprovar() {
        this.status = "APROVADO";
        this.dataStatus = LocalDate.now();
    }
    
    public void rejeitar() {
        this.status = "REJEITADO";
        this.dataStatus = LocalDate.now();
    }
    
    public void iniciar() {
        this.status = "INICIADO";
        this.dataStatus = LocalDate.now();
    }
    
    public boolean isAprovado() {
        return "APROVADO".equals(status);
    }
    
    public boolean isIniciado() {
        return "INICIADO".equals(status);
    }
    
    public boolean isRejeitado() {
        return "REJEITADO".equals(status);
    }
}
