package com.artereal.swing.model;

import java.time.LocalDateTime;

/**
 * Modelo de dados para Usuário do sistema
 */
public class Usuario extends SimpleModel {
    
    private Long id;
    private String nome;
    private String senha;
    private boolean administrador;
    private String acesso;
    private LocalDateTime dataInicio;
    private String contas;
    private String lancamentos;
    private String classes;
    private boolean portaria;
    private String dadosUsuario;
    private boolean permissaoBackup;
    private boolean permissaoRestaura;
    private String diretorioServico;
    private boolean permissaoPagar;
    private boolean permissaoReceber;
    
    public Usuario() {}
    
    public Usuario(String nome, String senha) {
        this.nome = nome;
        this.senha = senha;
        this.dataInicio = LocalDateTime.now();
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    
    public boolean isAdministrador() { return administrador; }
    public void setAdministrador(boolean administrador) { this.administrador = administrador; }
    
    public String getAcesso() { return acesso; }
    public void setAcesso(String acesso) { this.acesso = acesso; }
    
    public LocalDateTime getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio; }
    
    public String getContas() { return contas; }
    public void setContas(String contas) { this.contas = contas; }
    
    public String getLancamentos() { return lancamentos; }
    public void setLancamentos(String lancamentos) { this.lancamentos = lancamentos; }
    
    public String getClasses() { return classes; }
    public void setClasses(String classes) { this.classes = classes; }
    
    public boolean isPortaria() { return portaria; }
    public void setPortaria(boolean portaria) { this.portaria = portaria; }
    
    public String getDadosUsuario() { return dadosUsuario; }
    public void setDadosUsuario(String dadosUsuario) { this.dadosUsuario = dadosUsuario; }
    
    public boolean isPermissaoBackup() { return permissaoBackup; }
    public void setPermissaoBackup(boolean permissaoBackup) { this.permissaoBackup = permissaoBackup; }
    
    public boolean isPermissaoRestaura() { return permissaoRestaura; }
    public void setPermissaoRestaura(boolean permissaoRestaura) { this.permissaoRestaura = permissaoRestaura; }
    
    public String getDiretorioServico() { return diretorioServico; }
    public void setDiretorioServico(String diretorioServico) { this.diretorioServico = diretorioServico; }
    
    public boolean isPermissaoPagar() { return permissaoPagar; }
    public void setPermissaoPagar(boolean permissaoPagar) { this.permissaoPagar = permissaoPagar; }
    
    public boolean isPermissaoReceber() { return permissaoReceber; }
    public void setPermissaoReceber(boolean permissaoReceber) { this.permissaoReceber = permissaoReceber; }
    
    @Override
    public String toString() {
        return "Usuario{id=" + id + ", nome='" + nome + "', administrador=" + administrador + "}";
    }
}
