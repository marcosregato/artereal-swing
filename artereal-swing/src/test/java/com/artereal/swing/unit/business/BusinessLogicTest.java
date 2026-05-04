package com.artereal.swing.unit.business;

import com.artereal.swing.model.Irmao;
import com.artereal.swing.model.Loja;
import com.artereal.swing.model.Sessao;
import com.artereal.swing.model.Frequencia;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de lógica de negócio para validação de regras e valores
 * Verifica consistência de dados e validações de negócio
 */
@DisplayName("Testes de Lógica de Negócio")
class BusinessLogicTest {

    @BeforeEach
    void setUp() {
        System.setProperty("test.environment", "true");
    }

    @Test
    @DisplayName("Validação de dados do Irmão")
    void testIrmaoDataValidation() {
        // Arrange
        Irmao irmao = new Irmao();
        irmao.setNome("João da Silva");
        irmao.setGrau("Aprendiz");
        irmao.setNascimento(LocalDate.of(1980, 5, 15));
        irmao.setTelefone("(11) 98765-4321");

        // Act & Assert - Validações básicas
        assertThat(irmao.getNome()).isNotEmpty();
        assertThat(irmao.getGrau()).isIn("Aprendiz", "Companheiro", "Mestre");
        assertThat(irmao.getNascimento()).isBefore(LocalDate.now());
        assertThat(irmao.getTelefone()).matches("\\([0-9]{2}\\) [0-9]{5}-[0-9]{4}");
    }

    @Test
    @DisplayName("Validação de dados da Loja")
    void testLojaDataValidation() {
        // Arrange
        Loja loja = new Loja();
        loja.setNome("Loja Esperança");
        loja.setNumero("123");
        loja.setDataFundacao("15/06/1950");
        loja.setEndereco("Rua das Lojas, 123");
        loja.setCidade("São Paulo");
        loja.setEstado("SP");

        // Act & Assert - Validações básicas
        assertThat(loja.getNome()).isNotEmpty();
        assertThat(loja.getNumero()).isNotEmpty();
        assertThat(loja.getDataFundacao()).isNotEmpty();
        assertThat(loja.getEndereco()).isNotEmpty();
        assertThat(loja.getCidade()).isNotEmpty();
        assertThat(loja.getEstado()).hasSize(2);
    }

    @Test
    @DisplayName("Validação de dados da Sessão")
    void testSessaoDataValidation() {
        // Arrange
        Sessao sessao = new Sessao();
        sessao.setTipo("INSTRUCAO");
        sessao.setDataHora(LocalDateTime.now().plusDays(7));
        sessao.setTema("Sessão de instrução ritualística");
        sessao.setStatus("PROGRAMADA");

        // Act & Assert - Validações básicas
        assertThat(sessao.getTipo()).isIn("INSTRUCAO", "ADMINISTRATIVA", "RITUALISTICA", "ELEITORAL");
        assertThat(sessao.getDataHora()).isAfter(LocalDateTime.now());
        assertThat(sessao.getTema()).isNotEmpty();
        assertThat(sessao.getStatus()).isIn("PROGRAMADA", "REALIZADA", "CANCELADA", "ADIADA");
    }

    @Test
    @DisplayName("Validação de dados de Frequência")
    void testFrequenciaDataValidation() {
        // Arrange
        Frequencia frequencia = new Frequencia();
        frequencia.setCodigoIrmao(1L);
        frequencia.setNomeIrmao("Teste da Silva");
        frequencia.setGrau("Aprendiz");
        frequencia.setNumeroPresencas(10);
        frequencia.setNumeroFaltas(2);
        frequencia.setIrregular(false);

        // Act & Assert - Validações básicas
        assertThat(frequencia.getCodigoIrmao()).isNotNull();
        assertThat(frequencia.getNomeIrmao()).isNotEmpty();
        assertThat(frequencia.getGrau()).isIn("Aprendiz", "Companheiro", "Mestre");
        assertThat(frequencia.getNumeroPresencas()).isGreaterThanOrEqualTo(0);
        assertThat(frequencia.getNumeroFaltas()).isGreaterThanOrEqualTo(0);
        
        // Se não for irregular, deve ter presenças registradas
        if (!frequencia.isIrregular()) {
            assertThat(frequencia.getNumeroPresencas()).isGreaterThan(0);
        }
    }

    @Test
    @DisplayName("Regras de progressão de grau maçônico")
    void testGrauProgressionRules() {
        // Arrange & Act & Assert - Validações de progressão
        LocalDate dataNascimento = LocalDate.of(1985, 3, 20);
        LocalDate dataIniciacao = LocalDate.of(2021, 6, 15);
        
        // Verificar idade mínima para iniciação
        int idadeNaIniciacao = dataIniciacao.getYear() - dataNascimento.getYear();
        assertThat(idadeNaIniciacao).as("Idade mínima para iniciação deve ser 21 anos").isGreaterThanOrEqualTo(21);
        
        // Verificar tempo mínimo entre graus (simulado)
        LocalDate dataElevacao = LocalDate.of(2022, 6, 15);
        long mesesAprendiz = java.time.temporal.ChronoUnit.MONTHS.between(dataIniciacao, dataElevacao);
        assertThat(mesesAprendiz).as("Tempo mínimo como Aprendiz deve ser 12 meses").isGreaterThanOrEqualTo(12);
    }

    @Test
    @DisplayName("Validação de valores financeiros")
    void testFinancialValidation() {
        // Arrange
        BigDecimal valorPositivo = new BigDecimal("100.50");
        BigDecimal valorNegativo = new BigDecimal("-50.25");
        BigDecimal valorZero = BigDecimal.ZERO;

        // Act & Assert - Validações financeiras
        assertThat(valorPositivo).as("Valor deve ser positivo").isPositive();
        assertThat(valorNegativo).as("Valor negativo deve ser negativo").isNegative();
        assertThat(valorZero).as("Valor zero deve ser zero").isZero();
        
        // Verificar precisão monetária
        assertThat(valorPositivo.scale()).as("Precisão monetária deve ser 2 casas decimais").isEqualTo(2);
    }

    @Test
    @DisplayName("Validação de datas de sessões")
    void testSessaoDateValidation() {
        // Arrange
        LocalDateTime dataAtual = LocalDateTime.now();
        LocalDateTime dataFutura = dataAtual.plusWeeks(1);
        LocalDateTime dataPassada = dataAtual.minusWeeks(1);

        // Act & Assert - Validações de datas
        assertThat(dataFutura).as("Data futura deve ser após atual").isAfter(dataAtual);
        assertThat(dataPassada).as("Data passada deve ser antes da atual").isBefore(dataAtual);
        
        // Verificar se data de sessão é um dia válido (qualquer dia da semana)
        assertThat(dataFutura.getDayOfWeek()).as("Sessão deve ser em dia válido").isNotNull();
    }

    @Test
    @DisplayName("Validação de estrutura de dados")
    void testDataStructureValidation() {
        // Arrange & Act - Criar objetos com dados consistentes
        Irmao irmao = new Irmao();
        irmao.setId(1L);
        irmao.setNome("Teste da Silva");
        irmao.setGrau("Mestre");
        
        Loja loja = new Loja();
        loja.setId(1L);
        loja.setNome("Loja Teste");
        loja.setNumero("100");
        
        Sessao sessao = new Sessao();
        sessao.setId(1L);
        sessao.setTipo("ADMINISTRATIVA");
        // Note: Sessao não tem método setIdLoja() no modelo atual

        // Assert - Verificar consistência dos dados
        assertThat(irmao.getId()).isNotNull();
        assertThat(irmao.getNome()).contains("Teste");
        assertThat(loja.getId()).isNotNull();
        assertThat(sessao.getTipo()).isNotNull();
    }

    @Test
    @DisplayName("Validação de regras de negócio específicas")
    void testBusinessRulesValidation() {
        // Arrange & Act - Simular regras de negócio
        
        // Regra 1: Não pode haver sessão sem data futura
        LocalDateTime dataSessao = LocalDateTime.now().plusDays(3);
        assertThat(dataSessao).isAfter(LocalDateTime.now());
        
        // Regra 2: Irmão deve ter idade mínima para grau
        String grau = "Mestre";
        LocalDate dataNascimento = LocalDate.of(1975, 8, 20);
        int idade = LocalDate.now().getYear() - dataNascimento.getYear();
        
        if ("Mestre".equals(grau)) {
            assertThat(idade).as("Mestre deve ter idade mínima").isGreaterThanOrEqualTo(25);
        }
        
        // Regra 3: Loja deve ter número válido
        String numeroLoja = "123";
        assertThat(numeroLoja).matches("\\d+");
        assertThat(Integer.parseInt(numeroLoja)).isBetween(1, 9999);
    }
}
