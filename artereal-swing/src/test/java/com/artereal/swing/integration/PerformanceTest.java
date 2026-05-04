package com.artereal.swing.integration;

import com.artereal.swing.dao.*;
import com.artereal.swing.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de performance do sistema ArteReal
 * Verifica o desempenho em cenários de alto volume
 */
@DisplayName("Testes de Performance")
class PerformanceTest {

    private LojaDAO lojaDAO;
    private IrmaoDAO irmaoDAO;
    private SessaoDAO sessaoDAO;
    private CaixaDAO caixaDAO;
    private FrequenciaDAO frequenciaDAO;

    @BeforeEach
    void setUp() throws Exception {
        // Inicializa DAOs
        lojaDAO = DAOFactory.getInstance().getDAO(LojaDAO.class);
        irmaoDAO = DAOFactory.getInstance().getDAO(IrmaoDAO.class);
        sessaoDAO = DAOFactory.getInstance().getDAO(SessaoDAO.class);
        caixaDAO = DAOFactory.getInstance().getDAO(CaixaDAO.class);
        frequenciaDAO = DAOFactory.getInstance().getDAO(FrequenciaDAO.class);

        // Limpa dados de teste
        limparDadosDeTeste();
    }

    @AfterEach
    void tearDown() throws Exception {
        limparDadosDeTeste();
    }

    private void limparDadosDeTeste() throws Exception {
        try {
            List<Frequencia> frequencias = frequenciaDAO.findAll();
            for (Frequencia f : frequencias) {
                frequenciaDAO.delete(f.getId());
            }

            List<Sessao> sessoes = sessaoDAO.findAll();
            for (Sessao s : sessoes) {
                sessaoDAO.delete(s.getId());
            }

            List<Caixa> caixas = caixaDAO.findAll();
            for (Caixa c : caixas) {
                caixaDAO.delete(c.getId());
            }

            List<Irmao> irmaos = irmaoDAO.findAll();
            for (Irmao i : irmaos) {
                irmaoDAO.delete(i.getId());
            }

            List<Loja> lojas = lojaDAO.findAll();
            for (Loja l : lojas) {
                lojaDAO.delete(l.getId());
            }
        } catch (Exception e) {
            // Ignora erros na limpeza
        }
    }

    @Test
    @DisplayName("Performance: Criação em massa de irmãos")
    void testPerformanceCriacaoMassaIrmaos() throws Exception {
        // Arrange
        Loja loja = criarLojaTeste();
        lojaDAO.save(loja);

        int quantidadeIrmaos = 100;
        List<Irmao> irmaosCriados = new ArrayList<>();

        // Act
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < quantidadeIrmaos; i++) {
            Irmao irmao = criarIrmaoTeste();
            irmao.setNome("Irmão " + i);
            irmao.setGrau(i % 3 == 0 ? "APRENDIZ" : i % 3 == 1 ? "COMPANHEIRO" : "MESTRE");
            irmaoDAO.save(irmao);
            irmaosCriados.add(irmao);
        }

        long endTime = System.currentTimeMillis();
        long duracao = endTime - startTime;

        // Assert
        assertThat(irmaosCriados).hasSize(quantidadeIrmaos);
        
        List<Irmao> irmaosRecuperados = irmaoDAO.findAll();
        assertThat(irmaosRecuperados).hasSize(quantidadeIrmaos);

        // Verificação de performance (deve criar 100 irmãos em menos de 10 segundos)
        assertThat(duracao).isLessThan(10000);
        
        System.out.println("Performance - Criação de " + quantidadeIrmaos + " irmãos: " + duracao + "ms");
    }

    @Test
    @DisplayName("Performance: Consulta de grande volume de dados")
    void testPerformanceConsultaGrandeVolume() throws Exception {
        // Arrange - Prepara dados
        Loja loja = criarLojaTeste();
        lojaDAO.save(loja);

        // Cria 200 irmãos
        for (int i = 0; i < 200; i++) {
            Irmao irmao = criarIrmaoTeste();
            irmao.setNome("Irmão " + i);
            irmao.setGrau(i % 3 == 0 ? "APRENDIZ" : i % 3 == 1 ? "COMPANHEIRO" : "MESTRE");
            irmaoDAO.save(irmao);
        }

        // Cria 50 sessões
        for (int i = 0; i < 50; i++) {
            Sessao sessao = criarSessaoTeste();
            sessao.setTipo(i % 3 == 0 ? "BRANCA" : i % 3 == 1 ? "MAGNA" : "INSTRUCAO");
            sessao.setQuantidadePresentes(10 + (i % 20));
            sessaoDAO.save(sessao);
        }

        // Act - Testa performance das consultas
        long startTime = System.currentTimeMillis();

        List<Irmao> irmaos = irmaoDAO.findAll();
        List<Sessao> sessoes = sessaoDAO.findAll();

        long endTime = System.currentTimeMillis();
        long duracao = endTime - startTime;

        // Assert
        assertThat(irmaos).hasSize(200);
        assertThat(sessoes).hasSize(50);
        
        // Verificação de performance (consulta deve ser rápida)
        assertThat(duracao).isLessThan(1000);
        
        System.out.println("Performance - Consulta de 250 registros: " + duracao + "ms");
    }

    @Test
    @DisplayName("Performance: Operações financeiras em lote")
    void testPerformanceOperacoesFinanceirasLote() throws Exception {
        // Arrange
        Loja loja = criarLojaTeste();
        lojaDAO.save(loja);

        Irmao irmao = criarIrmaoTeste();
        irmaoDAO.save(irmao);

        int quantidadeMovimentacoes = 500;
        List<Caixa> movimentacoes = new ArrayList<>();

        // Act
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < quantidadeMovimentacoes; i++) {
            Caixa caixa = criarCaixaTeste();
            caixa.setTipo(i % 2 == 0 ? "ENTRADA" : "SAIDA");
            caixa.setValor(new BigDecimal(10 + (i % 100)));
            caixa.setDescricao("Movimentação " + i);
            caixa.setResponsavel(irmao.getNome());
            caixaDAO.save(caixa);
            movimentacoes.add(caixa);
        }

        long endTime = System.currentTimeMillis();
        long duracao = endTime - startTime;

        // Assert
        assertThat(movimentacoes).hasSize(quantidadeMovimentacoes);
        
        List<Caixa> movimentacoesRecuperadas = caixaDAO.findAll();
        assertThat(movimentacoesRecuperadas).hasSize(quantidadeMovimentacoes);

        // Verificação de performance (500 movimentações em menos de 30 segundos)
        assertThat(duracao).isLessThan(30000);
        
        System.out.println("Performance - Criação de " + quantidadeMovimentacoes + " movimentações: " + duracao + "ms");
    }

    @Test
    @DisplayName("Performance: Busca e filtragem")
    void testPerformanceBuscaFiltragem() throws Exception {
        // Arrange - Prepara dados
        Loja loja = criarLojaTeste();
        lojaDAO.save(loja);

        // Cria 1000 irmãos com diferentes nomes
        for (int i = 0; i < 1000; i++) {
            Irmao irmao = criarIrmaoTeste();
            irmao.setNome("Irmão " + (i % 100)); // Cria nomes repetidos para teste de busca
            irmao.setGrau(i % 3 == 0 ? "APRENDIZ" : i % 3 == 1 ? "COMPANHEIRO" : "MESTRE");
            irmaoDAO.save(irmao);
        }

        // Act - Testa performance de busca
        long startTime = System.currentTimeMillis();

        // Busca por nome
        List<Irmao> resultadoBusca = irmaoDAO.findByNome("Irmão 42");

        long endTime = System.currentTimeMillis();
        long duracao = endTime - startTime;

        // Assert
        // Deve encontrar 10 irmãos com o nome "Irmão 42" (1000/100)
        assertThat(resultadoBusca).hasSize(10);
        
        // Verificação de performance (busca deve ser rápida)
        assertThat(duracao).isLessThan(500);
        
        System.out.println("Performance - Busca em 1000 registros: " + duracao + "ms");
    }

    @Test
    @DisplayName("Performance: Operações de agregação")
    void testPerformanceOperacoesAgregacao() throws Exception {
        // Arrange - Prepara dados
        Loja loja = criarLojaTeste();
        lojaDAO.save(loja);

        Irmao irmao = criarIrmaoTeste();
        irmaoDAO.save(irmao);

        // Cria 1000 movimentações financeiras
        for (int i = 0; i < 1000; i++) {
            Caixa caixa = criarCaixaTeste();
            caixa.setTipo(i % 3 == 0 ? "ENTRADA" : i % 3 == 1 ? "SAIDA" : "ENTRADA"); // 2/3 de entradas
            caixa.setValor(new BigDecimal(10 + (i % 1000)));
            caixa.setResponsavel(irmao.getNome());
            caixaDAO.save(caixa);
        }

        // Act - Testa performance de agregação
        long startTime = System.currentTimeMillis();

        List<Caixa> todasMovimentacoes = caixaDAO.findAll();
        
        BigDecimal totalEntradas = todasMovimentacoes.stream()
                .filter(c -> "ENTRADA".equals(c.getTipo()))
                .map(Caixa::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalSaidas = todasMovimentacoes.stream()
                .filter(c -> "SAIDA".equals(c.getTipo()))
                .map(Caixa::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal saldo = totalEntradas.subtract(totalSaidas);

        long endTime = System.currentTimeMillis();
        long duracao = endTime - startTime;

        // Assert
        assertThat(todasMovimentacoes).hasSize(1000);
        assertThat(totalEntradas).isNotNull();
        assertThat(totalSaidas).isNotNull();
        assertThat(saldo).isNotNull();
        
        // Verificação de performance (agregação em menos de 5 segundos)
        assertThat(duracao).isLessThan(5000);
        
        System.out.println("Performance - Agregação de 1000 registros: " + duracao + "ms");
        System.out.println("Total Entradas: " + totalEntradas);
        System.out.println("Total Saídas: " + totalSaidas);
        System.out.println("Saldo: " + saldo);
    }

    @Test
    @DisplayName("Performance: Operações simultâneas")
    void testPerformanceOperacoesSimultaneas() throws Exception {
        // Arrange
        Loja loja = criarLojaTeste();
        lojaDAO.save(loja);

        int numThreads = 10;
        int operacoesPorThread = 20;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        // Act - Executa operações simultâneas
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numThreads; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < operacoesPorThread; j++) {
                        // Cada thread cria irmãos e sessões
                        Irmao irmao = criarIrmaoTeste();
                        irmao.setNome("Irmão Thread" + threadId + "-" + j);
                        irmaoDAO.save(irmao);

                        Sessao sessao = criarSessaoTeste();
                        sessao.setPresidente("Thread" + threadId);
                        sessaoDAO.save(sessao);
                    }
                } catch (Exception e) {
                    System.err.println("Erro na thread " + threadId + ": " + e.getMessage());
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);

        long endTime = System.currentTimeMillis();
        long duracao = endTime - startTime;

        // Assert
        List<Irmao> irmaos = irmaoDAO.findAll();
        List<Sessao> sessoes = sessaoDAO.findAll();

        assertThat(irmaos).hasSize(numThreads * operacoesPorThread);
        assertThat(sessoes).hasSize(numThreads * operacoesPorThread);

        // Verificação de performance (200 operações simultâneas em menos de 10 segundos)
        assertThat(duracao).isLessThan(10000);
        
        System.out.println("Performance - " + (numThreads * operacoesPorThread * 2) + " operações simultâneas: " + duracao + "ms");
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
}
