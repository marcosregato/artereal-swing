package com.artereal.swing.infrastructure.config;

import javax.sql.DataSource;

/**
 * Configuração standalone para a arquitetura hexagonal
 * 
 * Esta classe fornece beans essenciais sem dependências do Spring,
 * tornando o sistema independente e leve.
 */
public class StandaloneConfig {
    
    /**
     * Cria DataSource PostgreSQL standalone
     */
    public static DataSource createDataSource() {
        return ConfiguracaoBanco.criarDataSource();
    }
    
    /**
     * Factory para criar instâncias dos repositórios
     */
    public static com.artereal.swing.infrastructure.persistence.LojaJpaRepository createLojaRepository(DataSource dataSource) {
        return new com.artereal.swing.infrastructure.persistence.LojaJpaRepository(dataSource);
    }
    
    public static com.artereal.swing.infrastructure.persistence.IrmaoJpaRepository createIrmaoRepository(DataSource dataSource) {
        return new com.artereal.swing.infrastructure.persistence.IrmaoJpaRepository(dataSource);
    }
    
    public static com.artereal.swing.infrastructure.persistence.CaixaJpaRepository createCaixaRepository(DataSource dataSource) {
        return new com.artereal.swing.infrastructure.persistence.CaixaJpaRepository(dataSource);
    }
    
    /**
     * Factory para criar instâncias dos use cases
     */
    public static com.artereal.swing.application.loja.CriarLojaUseCase createCriarLojaUseCase(
            com.artereal.swing.domain.loja.LojaRepository lojaRepository) {
        return new com.artereal.swing.application.loja.CriarLojaUseCase(lojaRepository);
    }
}
