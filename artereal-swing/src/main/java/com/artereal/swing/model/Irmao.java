package com.artereal.swing.model;

import java.time.LocalDate;

/**
 * Modelo de dados para Irmão (versão Swing sem JPA)
 */
public class Irmao extends SimpleModel {
    
    private Long id;
    private String nome;
    private LocalDate nascimento;
    private String estadoCivil;
    private String natural;
    private String identidade;
    private String tipoSanguineo;
    private String cargoLoja;
    private String grau;
    private String cargoGrandeLoja;
    private String endereco;
    private String bairro;
    private String cidade;
    private String estado;
    private String telefone;
    private String empresa;
    private String telefoneEmpresa;
    private String enderecoEmpresa;
    private String registroGrandeLoja;
    private boolean ativo;
    private String createdAt;
    private String updatedAt;
    
    public Irmao() {}
    
    public Irmao(String nome, LocalDate nascimento) {
        this.nome = nome;
        this.nascimento = nascimento;
        this.ativo = true;
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public LocalDate getNascimento() { return nascimento; }
    public void setNascimento(LocalDate nascimento) { this.nascimento = nascimento; }
    
    public String getEstadoCivil() { return estadoCivil; }
    public void setEstadoCivil(String estadoCivil) { this.estadoCivil = estadoCivil; }
    
    public String getNatural() { return natural; }
    public void setNatural(String natural) { this.natural = natural; }
    
    public String getIdentidade() { return identidade; }
    public void setIdentidade(String identidade) { this.identidade = identidade; }
    
    public String getTipoSanguineo() { return tipoSanguineo; }
    public void setTipoSanguineo(String tipoSanguineo) { this.tipoSanguineo = tipoSanguineo; }
    
    public String getCargoLoja() { return cargoLoja; }
    public void setCargoLoja(String cargoLoja) { this.cargoLoja = cargoLoja; }
    
    public String getGrau() { return grau; }
    public void setGrau(String grau) { this.grau = grau; }
    
    public String getCargoGrandeLoja() { return cargoGrandeLoja; }
    public void setCargoGrandeLoja(String cargoGrandeLoja) { this.cargoGrandeLoja = cargoGrandeLoja; }
    
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    
    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }
    
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    
    public String getEmpresa() { return empresa; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }
    
    public String getTelefoneEmpresa() { return telefoneEmpresa; }
    public void setTelefoneEmpresa(String telefoneEmpresa) { this.telefoneEmpresa = telefoneEmpresa; }
    
    public String getEnderecoEmpresa() { return enderecoEmpresa; }
    public void setEnderecoEmpresa(String enderecoEmpresa) { this.enderecoEmpresa = enderecoEmpresa; }
    
    public String getRegistroGrandeLoja() { return registroGrandeLoja; }
    public void setRegistroGrandeLoja(String registroGrandeLoja) { this.registroGrandeLoja = registroGrandeLoja; }
    
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
