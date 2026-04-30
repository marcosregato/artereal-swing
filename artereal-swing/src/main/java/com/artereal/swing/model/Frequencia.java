package com.artereal.swing.model;

import java.time.LocalDate;

/**
 * Modelo de dados para Controle de Frequência e Presença
 */
public class Frequencia {
    
    private Long id;
    private Long codigoIrmao;
    private String nomeIrmao;
    private String registroGrandeLoja;
    private String numeroLicenca;
    private LocalDate dataLicenca;
    private LocalDate dataFinalLicenca;
    private String grau;
    private LocalDate dataInstalado;
    private boolean irregular;
    private int numeroPresencas;
    private int numeroFaltas;
    private int numeroSecoes;
    private String nomeHistorico;
    private boolean presencaDiretoria;
    private boolean secretariaDiretoria;
    
    public Frequencia() {}
    
    public Frequencia(Long codigoIrmao, String nomeIrmao) {
        this.codigoIrmao = codigoIrmao;
        this.nomeIrmao = nomeIrmao;
        this.numeroPresencas = 0;
        this.numeroFaltas = 0;
        this.numeroSecoes = 0;
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getCodigoIrmao() { return codigoIrmao; }
    public void setCodigoIrmao(Long codigoIrmao) { this.codigoIrmao = codigoIrmao; }
    
    public String getNomeIrmao() { return nomeIrmao; }
    public void setNomeIrmao(String nomeIrmao) { this.nomeIrmao = nomeIrmao; }
    
    public String getRegistroGrandeLoja() { return registroGrandeLoja; }
    public void setRegistroGrandeLoja(String registroGrandeLoja) { this.registroGrandeLoja = registroGrandeLoja; }
    
    public String getNumeroLicenca() { return numeroLicenca; }
    public void setNumeroLicenca(String numeroLicenca) { this.numeroLicenca = numeroLicenca; }
    
    public LocalDate getDataLicenca() { return dataLicenca; }
    public void setDataLicenca(LocalDate dataLicenca) { this.dataLicenca = dataLicenca; }
    
    public LocalDate getDataFinalLicenca() { return dataFinalLicenca; }
    public void setDataFinalLicenca(LocalDate dataFinalLicenca) { this.dataFinalLicenca = dataFinalLicenca; }
    
    public String getGrau() { return grau; }
    public void setGrau(String grau) { this.grau = grau; }
    
    public LocalDate getDataInstalado() { return dataInstalado; }
    public void setDataInstalado(LocalDate dataInstalado) { this.dataInstalado = dataInstalado; }
    
    public boolean isIrregular() { return irregular; }
    public void setIrregular(boolean irregular) { this.irregular = irregular; }
    
    public int getNumeroPresencas() { return numeroPresencas; }
    public void setNumeroPresencas(int numeroPresencas) { this.numeroPresencas = numeroPresencas; }
    
    public int getNumeroFaltas() { return numeroFaltas; }
    public void setNumeroFaltas(int numeroFaltas) { this.numeroFaltas = numeroFaltas; }
    
    public int getNumeroSecoes() { return numeroSecoes; }
    public void setNumeroSecoes(int numeroSecoes) { this.numeroSecoes = numeroSecoes; }
    
    public String getNomeHistorico() { return nomeHistorico; }
    public void setNomeHistorico(String nomeHistorico) { this.nomeHistorico = nomeHistorico; }
    
    public boolean isPresencaDiretoria() { return presencaDiretoria; }
    public void setPresencaDiretoria(boolean presencaDiretoria) { this.presencaDiretoria = presencaDiretoria; }
    
    public boolean isSecretariaDiretoria() { return secretariaDiretoria; }
    public void setSecretariaDiretoria(boolean secretariaDiretoria) { this.secretariaDiretoria = secretariaDiretoria; }
    
    // Métodos utilitários
    public double getPercentualPresenca() {
        if (numeroSecoes == 0) return 0.0;
        return (double) numeroPresencas / numeroSecoes * 100;
    }
    
    public void incrementarPresenca() {
        this.numeroPresencas++;
        this.numeroSecoes++;
    }
    
    public void incrementarFalta() {
        this.numeroFaltas++;
        this.numeroSecoes++;
    }
    
    @Override
    public String toString() {
        return nomeIrmao + " - " + grau + " (" + numeroPresencas + "/" + numeroSecoes + " presenças)";
    }
}
