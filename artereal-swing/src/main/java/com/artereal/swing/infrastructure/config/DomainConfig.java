package com.artereal.swing.infrastructure.config;

import com.artereal.swing.application.caixa.AbrirCaixaUseCase;
import com.artereal.swing.application.irmao.CriarIrmaoUseCase;
import com.artereal.swing.application.loja.CriarLojaUseCase;
import com.artereal.swing.domain.caixa.CaixaRepository;
import com.artereal.swing.domain.irmao.IrmaoRepository;
import com.artereal.swing.domain.loja.LojaRepository;
import com.artereal.swing.infrastructure.persistence.CaixaJpaRepository;
import com.artereal.swing.infrastructure.persistence.IrmaoJpaRepository;
import com.artereal.swing.infrastructure.persistence.LojaJpaRepository;
import javax.sql.DataSource;

/**
 * Configuração standalone para a camada de domínio
 * 
 * Esta classe fornece beans essenciais para a arquitetura hexagonal
 * sem dependências do Spring, tornando o sistema mais leve e independente.
 */
public class DomainConfig {
    
    private static DataSource dataSource;
    private static LojaRepository lojaRepository;
    private static IrmaoRepository irmaoRepository;
    private static CaixaRepository caixaRepository;
    
    private static CriarLojaUseCase criarLojaUseCase;
    private static CriarIrmaoUseCase criarIrmaoUseCase;
    private static AbrirCaixaUseCase abrirCaixaUseCase;
    
    /**
     * Inicializa todas as dependências
     */
    public static void initialize() {
        dataSource = ConfiguracaoBanco.criarDataSource();
        
        // Inicializar repositories
        lojaRepository = new LojaJpaRepository(dataSource);
        irmaoRepository = new IrmaoJpaRepository(dataSource);
        caixaRepository = new CaixaJpaRepository(dataSource);
        
        // Inicializar use cases
        criarLojaUseCase = new CriarLojaUseCase(lojaRepository);
        criarIrmaoUseCase = new CriarIrmaoUseCase(irmaoRepository, lojaRepository);
        abrirCaixaUseCase = new AbrirCaixaUseCase(caixaRepository, lojaRepository);
    }
    
    /**
     * Factory para criar DataSource
     */
    public static DataSource createDataSource() {
        if (dataSource == null) {
            dataSource = ConfiguracaoBanco.criarDataSource();
        }
        return dataSource;
    }
    
    // Getters para repositories
    public static LojaRepository getLojaRepository() {
        if (lojaRepository == null) {
            initialize();
        }
        return lojaRepository;
    }
    
    public static IrmaoRepository getIrmaoRepository() {
        if (irmaoRepository == null) {
            initialize();
        }
        return irmaoRepository;
    }
    
    public static CaixaRepository getCaixaRepository() {
        if (caixaRepository == null) {
            initialize();
        }
        return caixaRepository;
    }
    
    // Getters para use cases
    public static CriarLojaUseCase getCriarLojaUseCase() {
        if (criarLojaUseCase == null) {
            initialize();
        }
        return criarLojaUseCase;
    }
    
    public static CriarIrmaoUseCase getCriarIrmaoUseCase() {
        if (criarIrmaoUseCase == null) {
            initialize();
        }
        return criarIrmaoUseCase;
    }
    
    public static AbrirCaixaUseCase getAbrirCaixaUseCase() {
        if (abrirCaixaUseCase == null) {
            initialize();
        }
        return abrirCaixaUseCase;
    }
}
