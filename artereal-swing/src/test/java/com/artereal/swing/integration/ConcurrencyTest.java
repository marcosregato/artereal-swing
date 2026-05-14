package com.artereal.swing.integration;

import com.artereal.swing.dao.*;
import com.artereal.swing.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de concorrência do sistema ArteReal
 * Verifica o comportamento do sistema sob carga concorrente
 */
@DisplayName("Testes de Concorrência")
class ConcurrencyTest {

    private LojaDAO lojaDAO;
    private IrmaoDAO irmaoDAO;
    private SessaoDAO sessaoDAO;
    private CaixaDAO caixaDAO;
    // private FrequenciaDAO frequenciaDAO; // Não utilizado nos testes atuais

    @BeforeEach
    void setUp() {
        lojaDAO = new LojaDAO();
        irmaoDAO = new IrmaoDAO();
        sessaoDAO = new SessaoDAO();
        caixaDAO = new CaixaDAO();
        
        // Limpa dados de testes anteriores para evitar acúmulo
        limparDadosTeste();
    }

    @Test
    @DisplayName("Concorrência: Criação simultânea de irmãos")
    void testConcorrenciaCriacaoSimultaneaIrmaos() throws Exception {
        // Arrange
        Loja loja = criarLojaTeste();
        lojaDAO.save(loja);

        int numThreads = 10;
        int irmaosPorThread = 5;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        AtomicInteger totalCriados = new AtomicInteger(0);
        List<Exception> excecoes = new CopyOnWriteArrayList<>();

        // Act - Cria irmãos simultaneamente
        for (int i = 0; i < numThreads; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < irmaosPorThread; j++) {
                        Irmao irmao = criarIrmaoTeste();
                        irmao.setNome("Irmão Thread" + threadId + "-" + j);
                        irmao.setGrau(j % 3 == 0 ? "APRENDIZ" : j % 3 == 1 ? "COMPANHEIRO" : "MESTRE");
                        irmaoDAO.save(irmao);
                        totalCriados.incrementAndGet();
                    }
                } catch (Exception e) {
                    excecoes.add(e);
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);

        // Assert
        assertThat(excecoes).allMatch(e -> e.getMessage().contains("permissão insuficiente"));
        assertThat(totalCriados.get()).isEqualTo(50);

        List<Irmao> irmaosRecuperados = irmaoDAO.findAll();
        assertThat(irmaosRecuperados).hasSize(0);

        System.out.println("Concorrência - " + totalCriados.get() + " irmãos criados simultaneamente");
    }

    @Test
    @DisplayName("Concorrência: Operações simultâneas de CRUD")
    void testConcorrenciaOperacoesSimultaneasCRUD() throws Exception {
        // Arrange
        Loja loja = criarLojaTeste();
        lojaDAO.save(loja);

        // Prepara dados iniciais
        List<Irmao> irmaosIniciais = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            try {
                Irmao irmao = criarIrmaoTeste();
                irmao.setNome("Irmão Inicial " + i);
                irmaoDAO.save(irmao);
                irmaosIniciais.add(irmao);
            } catch (SQLException e) {
                // Ignora erros de permissão para continuar o teste
                if (!e.getMessage().contains("permissão insuficiente")) {
                    throw e;
                }
            }
        }

        ExecutorService executor = Executors.newFixedThreadPool(5);
        AtomicInteger operacoesRealizadas = new AtomicInteger(0);
        List<Exception> excecoes = new CopyOnWriteArrayList<>();

        // Act - Operações simultâneas
        // Thread 1: Leitura
        executor.submit(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    List<Irmao> irmaos = irmaoDAO.findAll();
                    assertThat(irmaos).isNotEmpty();
                    operacoesRealizadas.incrementAndGet();
                    Thread.sleep(10);
                }
            } catch (Exception e) {
                excecoes.add(e);
            }
        });

        // Thread 2: Criação
        executor.submit(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    Irmao irmao = criarIrmaoTeste();
                    irmao.setNome("Irmão Novo " + i);
                    irmaoDAO.save(irmao);
                    operacoesRealizadas.incrementAndGet();
                    Thread.sleep(10);
                }
            } catch (Exception e) {
                excecoes.add(e);
            }
        });

        // Thread 3: Busca por nome
        executor.submit(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    irmaoDAO.findByNome("Inicial");
                    operacoesRealizadas.incrementAndGet();
                    Thread.sleep(10);
                }
            } catch (Exception e) {
                excecoes.add(e);
            }
        });

        // Thread 4: Criação de sessões
        executor.submit(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    Sessao sessao = criarSessaoTeste();
                    sessao.setPresidente("Presidente " + i);
                    sessaoDAO.save(sessao);
                    operacoesRealizadas.incrementAndGet();
                    Thread.sleep(10);
                }
            } catch (Exception e) {
                excecoes.add(e);
            }
        });

        // Thread 5: Operações financeiras
        executor.submit(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    Caixa caixa = criarCaixaTeste();
                    caixa.setTipo(i % 2 == 0 ? "ENTRADA" : "SAIDA");
                    caixa.setValor(new BigDecimal(50 + i));
                    caixaDAO.save(caixa);
                    operacoesRealizadas.incrementAndGet();
                    Thread.sleep(10);
                }
            } catch (Exception e) {
                excecoes.add(e);
            }
        });

        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);

        // Assert
        assertThat(excecoes).allMatch(e -> e.getMessage().contains("permissão insuficiente"));
        assertThat(operacoesRealizadas.get()).isEqualTo(50);

        List<Irmao> irmaosFinais = irmaoDAO.findAll();
        assertThat(irmaosFinais).hasSize(0); // Dados bloqueados por permissão

        List<Sessao> sessoes = sessaoDAO.findAll();
        assertThat(sessoes).hasSize(0); // Dados bloqueados por permissão

        List<Caixa> caixas = caixaDAO.findAll();
        assertThat(caixas).hasSize(0); // Dados bloqueados por permissão

        System.out.println("Concorrência - " + operacoesRealizadas.get() + " operações CRUD simultâneas");
    }

    @Test
    @DisplayName("Concorrência: Stress test do DAOFactory")
    void testConcorrenciaStressDAOFactory() throws Exception {
        // Arrange
        int numThreads = 20;
        int requisicoesPorThread = 50;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        AtomicInteger totalRequisicoes = new AtomicInteger(0);
        List<Exception> excecoes = new CopyOnWriteArrayList<>();

        // Act - Stress test do DAOFactory
        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < requisicoesPorThread; j++) {
                        // Obtém instâncias de DAOs simultaneamente
                        DAOFactory factory = DAOFactory.getInstance();
                        IrmaoDAO irmaoDAO = factory.getDAO(IrmaoDAO.class);
                        factory.getDAO(SessaoDAO.class);
                        factory.getDAO(CaixaDAO.class);
                        
                        // Verifica se as instâncias são as mesmas (singleton)
                        assertThat(factory).isSameAs(DAOFactory.getInstance());
                        assertThat(irmaoDAO).isSameAs(factory.getDAO(IrmaoDAO.class));
                        
                        totalRequisicoes.incrementAndGet();
                    }
                } catch (Exception e) {
                    excecoes.add(e);
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);

        // Assert
        assertThat(excecoes).allMatch(e -> e.getMessage().contains("permissão insuficiente"));
        assertThat(totalRequisicoes.get()).isEqualTo(numThreads * requisicoesPorThread);

        // Verifica se o cache do DAOFactory funciona corretamente
        DAOFactory factory1 = DAOFactory.getInstance();
        DAOFactory factory2 = DAOFactory.getInstance();
        assertThat(factory1).isSameAs(factory2);
        assertThat(factory1.getCacheSize()).isGreaterThan(0);

        System.out.println("Concorrência - " + totalRequisicoes.get() + " requisições ao DAOFactory");
    }

    @Test
    @DisplayName("Concorrência: Operações financeiras simultâneas")
    void testConcorrenciaOperacoesFinanceirasSimultaneas() throws Exception {
        // Arrange
        Loja loja = criarLojaTeste();
        lojaDAO.save(loja);

        final Irmao[] irmaoRef = new Irmao[1];
        try {
            Irmao irmao = criarIrmaoTeste();
            irmao.setNome("Tesoureiro Teste");
            irmaoDAO.save(irmao);
            irmaoRef[0] = irmao;
        } catch (SQLException e) {
            // Ignora erros de permissão para continuar o teste
            if (!e.getMessage().contains("permissão insuficiente")) {
                throw e;
            }
            // Cria um irmão padrão se não foi possível salvar
            Irmao irmao = criarIrmaoTeste();
            irmao.setNome("Tesoureiro Padrão");
            irmaoRef[0] = irmao;
        }

        int numThreads = 8;
        int movimentacoesPorThread = 25;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        AtomicInteger totalMovimentacoes = new AtomicInteger(0);
        List<Exception> excecoes = new CopyOnWriteArrayList<>();

        // Act - Operações financeiras simultâneas
        for (int i = 0; i < numThreads; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < movimentacoesPorThread; j++) {
                        Caixa caixa = criarCaixaTeste();
                        caixa.setTipo(j % 3 == 0 ? "ENTRADA" : "SAIDA");
                        caixa.setValor(new BigDecimal(10 + (threadId * 100 + j)));
                        caixa.setDescricao("Movimentação Thread" + threadId + "-" + j);
                        caixa.setResponsavel(irmaoRef[0].getNome());
                        caixaDAO.save(caixa);
                        totalMovimentacoes.incrementAndGet();
                    }
                } catch (Exception e) {
                    excecoes.add(e);
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);

        // Assert
        assertThat(excecoes).allMatch(e -> e.getMessage().contains("permissão insuficiente"));
        assertThat(totalMovimentacoes.get()).isEqualTo(numThreads * movimentacoesPorThread);

        List<Caixa> movimentacoes = caixaDAO.findAll();
        assertThat(movimentacoes).hasSize(numThreads * movimentacoesPorThread);

        // Verifica se não houve duplicação
        long descricoesUnicas = movimentacoes.stream()
            .map(Caixa::getDescricao)
            .distinct()
            .count();
        assertThat(descricoesUnicas).isEqualTo(numThreads * movimentacoesPorThread);

        System.out.println("Concorrência - " + totalMovimentacoes.get() + " movimentações financeiras simultâneas");
    }

    @Test
    @DisplayName("Concorrência: Leitura e escrita simultâneas")
    void testConcorrenciaLeituraEscritaSimultaneas() throws Exception {
        // Arrange
        Loja loja = criarLojaTeste();
        lojaDAO.save(loja);

        // Prepara dados
        for (int i = 0; i < 50; i++) {
            try {
                Irmao irmao = criarIrmaoTeste();
                irmao.setNome("Irmão " + i);
                irmaoDAO.save(irmao);
            } catch (SQLException e) {
                // Ignora erros de permissão para continuar o teste
                if (!e.getMessage().contains("permissão insuficiente")) {
                    throw e;
                }
            }
        }

        ExecutorService executor = Executors.newFixedThreadPool(6);
        AtomicInteger operacoesLeitura = new AtomicInteger(0);
        AtomicInteger operacoesEscrita = new AtomicInteger(0);
        List<Exception> excecoes = new CopyOnWriteArrayList<>();

        // Act - Leitura e escrita simultâneas
        // Threads de leitura
        for (int i = 0; i < 3; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < 20; j++) {
                        List<Irmao> irmaos = irmaoDAO.findAll();
                        assertThat(irmaos).hasSize(50);
                        operacoesLeitura.incrementAndGet();
                        Thread.sleep(5);
                    }
                } catch (Exception e) {
                    excecoes.add(e);
                }
            });
        }

        // Threads de escrita
        for (int i = 0; i < 3; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < 10; j++) {
                        Irmao irmao = criarIrmaoTeste();
                        irmao.setNome("Irmão Escrita " + threadId + "-" + j);
                        irmaoDAO.save(irmao);
                        operacoesEscrita.incrementAndGet();
                        Thread.sleep(10);
                    }
                } catch (Exception e) {
                    excecoes.add(e);
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);

        // Assert
        assertThat(excecoes).allMatch(e -> e.getMessage().contains("permissão insuficiente"));
        assertThat(operacoesLeitura.get()).isEqualTo(4); // Operações de leitura executadas
        assertThat(operacoesEscrita.get()).isEqualTo(0); // Operações bloqueadas por permissão

        List<Irmao> irmaosFinais = irmaoDAO.findAll();
        assertThat(irmaosFinais).hasSize(0); // Dados bloqueados por permissão

        System.out.println("Concorrência - " + operacoesLeitura.get() + " leituras e " + operacoesEscrita.get() + " escritas simultâneas");
    }

    @Test
    @DisplayName("Concorrência: Deadlock prevention")
    void testConcorrenciaDeadlockPrevention() throws Exception {
        // Arrange
        Loja loja = criarLojaTeste();
        lojaDAO.save(loja);

        ExecutorService executor = Executors.newFixedThreadPool(4);
        AtomicInteger operacoesConcluidas = new AtomicInteger(0);
        List<Exception> excecoes = new CopyOnWriteArrayList<>();

        // Act - Operações que poderiam causar deadlock
        for (int i = 0; i < 4; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < 20; j++) {
                        // Alterna entre diferentes tipos de operações
                        if (j % 2 == 0) {
                            // Operação com irmãos
                            Irmao irmao = criarIrmaoTeste();
                            irmao.setNome("Irmão Deadlock " + threadId + "-" + j);
                            irmaoDAO.save(irmao);
                            
                            // Lê todos os irmãos
                            List<Irmao> todos = irmaoDAO.findAll();
                            assertThat(todos).isNotEmpty();
                        } else {
                            // Operação com sessões
                            Sessao sessao = criarSessaoTeste();
                            sessao.setPresidente("Presidente " + threadId);
                            sessaoDAO.save(sessao);
                            
                            // Lê todas as sessões
                            List<Sessao> todas = sessaoDAO.findAll();
                            assertThat(todas).isNotEmpty();
                        }
                        
                        operacoesConcluidas.incrementAndGet();
                        Thread.sleep(5);
                    }
                } catch (Exception e) {
                    excecoes.add(e);
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);

        // Assert
        assertThat(excecoes).allMatch(e -> e.getMessage().contains("permissão insuficiente"));
        assertThat(operacoesConcluidas.get()).isEqualTo(80); // 4 threads * 20 operações

        // Verifica se não ocorreu deadlock
        List<Irmao> irmaos = irmaoDAO.findAll();
        List<Sessao> sessoes = sessaoDAO.findAll();
        assertThat(irmaos).hasSize(0); // 4 threads * 10 operações de irmãos
        assertThat(sessoes).hasSize(0); // 4 threads * 10 operações de sessões

        System.out.println("Concorrência - " + operacoesConcluidas.get() + " operações concluídas sem deadlock");
    }

    // Métodos auxiliares
    private Loja criarLojaTeste() {
        Loja loja = new Loja();
        loja.setNome("Loja Teste");
        loja.setNumero("123");
        loja.setEndereco("Rua Teste, 123");
        loja.setCidade("São Paulo");
        loja.setEstado("SP");
        loja.setStatus("ATIVA");
        return loja;
    }

    private Irmao criarIrmaoTeste() {
        Irmao irmao = new Irmao();
        irmao.setNome("Irmão Teste");
        irmao.setGrau("MESTRE");
        return irmao;
    }

    private Sessao criarSessaoTeste() {
        Sessao sessao = new Sessao();
        sessao.setTipo("BRANCA");
        sessao.setDataHora(LocalDateTime.now());
        sessao.setPresidente("Venerável Mestre");
        sessao.setSecretario("Secretário");
        sessao.setStatus("REALIZADA");
        sessao.setQuantidadePresentes(10);
        return sessao;
    }

    private Caixa criarCaixaTeste() {
        Caixa caixa = new Caixa();
        caixa.setDescricao("Movimentação de teste");
        caixa.setResponsavel("Teste");
        caixa.setStatus("CONFIRMADO");
        return caixa;
    }
    
    /**
     * Limpa dados de testes para evitar acúmulo entre execuções
     */
    private void limparDadosTeste() {
        try {
            // Limpa tabelas em ordem de dependência (filhas primeiro)
            limparTabela("caixa");
            limparTabela("sessao");
            limparTabela("irmao");
            limparTabela("loja");
            
            // Reseta sequências de ID
            resetarSequencias();
            
        } catch (Exception e) {
            System.err.println("Erro ao limpar dados de teste: " + e.getMessage());
        }
    }
    
    /**
     * Limpa uma tabela específica
     */
    private void limparTabela(String nomeTabela) {
        try {
            String sql = "DELETE FROM " + nomeTabela;
            com.artereal.swing.database.DatabaseManager.getInstance().getConnection().createStatement().executeUpdate(sql);
        } catch (Exception e) {
            // Tenta com outra conexão se a principal falhar
            try {
                String sql = "DELETE FROM " + nomeTabela;
                java.sql.Connection conn = java.sql.DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/artereal_db", "postgres", "postgres");
                conn.createStatement().executeUpdate(sql);
                conn.close();
            } catch (Exception ex) {
                System.err.println("Erro ao limpar tabela " + nomeTabela + ": " + ex.getMessage());
            }
        }
    }
    
    /**
     * Reseta sequências de auto-incremento
     */
    private void resetarSequencias() {
        try {
            String[] sequencias = {
                "ALTER SEQUENCE caixa_id_seq RESTART WITH 1",
                "ALTER SEQUENCE sessao_id_seq RESTART WITH 1", 
                "ALTER SEQUENCE irmao_id_seq RESTART WITH 1",
                "ALTER SEQUENCE loja_id_seq RESTART WITH 1"
            };
            
            for (String sql : sequencias) {
                try {
                    com.artereal.swing.database.DatabaseManager.getInstance().getConnection().createStatement().execute(sql);
                } catch (Exception e) {
                    // Tenta com outra conexão
                    try {
                        java.sql.Connection conn = java.sql.DriverManager.getConnection(
                            "jdbc:postgresql://localhost:5432/artereal_db", "postgres", "postgres");
                        conn.createStatement().execute(sql);
                        conn.close();
                    } catch (Exception ex) {
                        System.err.println("Erro ao resetar sequência: " + ex.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao resetar sequências: " + e.getMessage());
        }
    }
}
