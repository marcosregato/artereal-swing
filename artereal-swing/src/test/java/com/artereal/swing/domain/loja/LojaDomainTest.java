package com.artereal.swing.domain.loja;

import com.artereal.swing.domain.irmao.Irmao;
import com.artereal.swing.domain.caixa.Caixa;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

/**
 * Testes de domínio para a entidade Loja
 * 
 * Esta classe contém testes unitários para validar as regras
 * de negócio da entidade Loja, seguindo os princípios DDD.
 */
class LojaDomainTest {
    
    private Loja loja;
    
    @BeforeEach
    void setUp() {
        loja = Loja.criar(
            "Loja Teste",
            "12.345.678/0001-95",
            "Rua Teste, 123",
            "São Paulo",
            "SP"
        );
    }
    
    @Test
    @DisplayName("Deve criar loja com dados válidos")
    void deveCriarLojaComDadosValidos() {
        // Assert
        assertThat(loja).isNotNull();
        assertThat(loja.getId()).isNotNull();
        assertThat(loja.getNome()).isEqualTo("Loja Teste");
        assertThat(loja.getCnpj()).isEqualTo("12.345.678/0001-95");
        assertThat(loja.getEndereco()).isEqualTo("Rua Teste, 123");
        assertThat(loja.getCidade()).isEqualTo("São Paulo");
        assertThat(loja.getEstado()).isEqualTo("SP");
        assertThat(loja.getStatus()).isEqualTo(StatusLoja.ATIVA);
        assertThat(loja.getDataCriacao()).isNotNull();
        assertThat(loja.getDataAtualizacao()).isNotNull();
        assertThat(loja.isAtiva()).isTrue();
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao criar loja com nome nulo")
    void deveLancarExcecaoAoCriarLojaComNomeNulo() {
        assertThatThrownBy(() -> Loja.criar(null, "12.345.678/0001-95", "Rua Teste", "São Paulo", "SP"))
            .isInstanceOf(LojaException.class)
            .hasMessage("Nome da loja é obrigatório");
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao criar loja com CNPJ inválido")
    void deveLancarExcecaoAoCriarLojaComCnpjInvalido() {
        assertThatThrownBy(() -> Loja.criar("Loja Teste", "123", "Rua Teste", "São Paulo", "SP"))
            .isInstanceOf(LojaException.class)
            .hasMessage("CNPJ da loja é inválido");
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao criar loja com nome muito longo")
    void deveLancarExcecaoAoCriarLojaComNomeMuitoLongo() {
        String nomeLongo = "a".repeat(201); // 201 caracteres
        
        assertThatThrownBy(() -> Loja.criar(nomeLongo, "12.345.678/0001-95", "Rua Teste", "São Paulo", "SP"))
            .isInstanceOf(LojaException.class)
            .hasMessage("Nome da loja não pode exceder 200 caracteres");
    }
    
    @Test
    @DisplayName("Deve adicionar irmão com sucesso")
    void deveAdicionarIrmaoComSucesso() {
        Irmao irmao = Irmao.criar("Irmão Teste", "123.456.789-00", "11999999999", "irmao@teste.com");
        
        loja.adicionarIrmao(irmao);
        
        assertThat(loja.getIrmaos()).hasSize(1);
        assertThat(irmao.getLojaId()).isEqualTo(loja.getId());
        assertThat(irmao.isAssociadoLoja()).isTrue();
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao adicionar irmão duplicado")
    void deveLancarExcecaoAoAdicionarIrmaoDuplicado() {
        Irmao irmao = Irmao.criar("Irmão Teste", "123.456.789-00", "11999999999", "irmao@teste.com");
        loja.adicionarIrmao(irmao);
        
        Irmao irmaoDuplicado = Irmao.criar("Irmão Duplicado", "987.654.321-00", "11999999998", "irmao2@teste.com");
        
        assertThatThrownBy(() -> loja.adicionarIrmao(irmaoDuplicado))
            .isInstanceOf(LojaException.class)
            .hasMessage("Irmão já está associado à loja");
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao adicionar irmão além do limite")
    void deveLancarExcecaoAoAdicionarIrmaoAlemDoLimite() {
        // Adicionar 50 irmãos (limite)
        for (int i = 0; i < 50; i++) {
            Irmao irmao = Irmao.criar("Irmão " + i, "123.456.789-0" + i, "1199999999" + i, "irmao" + i + "@teste.com");
            loja.adicionarIrmao(irmao);
        }
        
        // Tentar adicionar o 51º irmão
        Irmao irmaoExtra = Irmao.criar("Irmão Extra", "123.456.789-99", "11999999999", "irmaoextra@teste.com");
        
        assertThatThrownBy(() -> loja.adicionarIrmao(irmaoExtra))
            .isInstanceOf(LojaException.class)
            .hasMessage("Loja atingiu o limite máximo de 50 irmãos");
    }
    
    @Test
    @DisplayName("Deve remover irmão com sucesso")
    void deveRemoverIrmaoComSucesso() {
        Irmao irmao = Irmao.criar("Irmão Teste", "123.456.789-00", "11999999999", "irmao@teste.com");
        loja.adicionarIrmao(irmao);
        
        loja.removerIrmao(irmao.getId());
        
        assertThat(loja.getIrmaos()).isEmpty();
        assertThat(irmao.isAssociadoLoja()).isFalse();
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao remover irmão inexistente")
    void deveLancarExcecaoAoRemoverIrmaoInexistente() {
        assertThatThrownBy(() -> loja.removerIrmao(999L))
            .isInstanceOf(LojaException.class)
            .hasMessage("Irmão não encontrado na loja");
    }
    
    @Test
    @DisplayName("Deve abrir caixa com sucesso")
    void deveAbrirCaixaComSucesso() {
        loja.abrirCaixa(new BigDecimal("1000.00"), "Responsável Teste");
        
        assertThat(loja.getCaixas()).hasSize(1);
        assertThat(loja.temCaixasAbertos()).isTrue();
        
        Caixa caixa = loja.getCaixas().get(0);
        assertThat(caixa.getValor()).isEqualTo(new BigDecimal("1000.00"));
        assertThat(caixa.getResponsavel()).isEqualTo("Responsável Teste");
        assertThat(caixa.getLojaId()).isEqualTo(loja.getId());
        assertThat(caixa.isAberto()).isTrue();
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao abrir caixa com valor negativo")
    void deveLancarExcecaoAoAbrirCaixaComValorNegativo() {
        assertThatThrownBy(() -> loja.abrirCaixa(new BigDecimal("-100.00"), "Responsável Teste"))
            .isInstanceOf(LojaException.class)
            .hasMessage("Valor do caixa deve ser positivo");
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao abrir caixa com responsável vazio")
    void deveLancarExcecaoAoAbrirCaixaComResponsavelVazio() {
        assertThatThrownBy(() -> loja.abrirCaixa(new BigDecimal("1000.00"), ""))
            .isInstanceOf(LojaException.class)
            .hasMessage("Responsável pelo caixa é obrigatório");
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao abrir caixa quando já existe caixa aberto")
    void deveLancarExcecaoAoAbrirCaixaQuandoJaExisteCaixaAberto() {
        loja.abrirCaixa(new BigDecimal("1000.00"), "Responsável 1");
        
        assertThatThrownBy(() -> loja.abrirCaixa(new BigDecimal("2000.00"), "Responsável 2"))
            .isInstanceOf(LojaException.class)
            .hasMessage("Já existe um caixa aberto na loja");
    }
    
    @Test
    @DisplayName("Deve fechar caixa com sucesso")
    void deveFecharCaixaComSucesso() {
        loja.abrirCaixa(new BigDecimal("1000.00"), "Responsável Teste");
        Caixa caixa = loja.getCaixas().get(0);
        
        loja.fecharCaixa(caixa.getId());
        
        assertThat(caixa.isFechado()).isTrue();
        assertThat(loja.temCaixasAbertos()).isFalse();
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao fechar caixa inexistente")
    void deveLancarExcecaoAoFecharCaixaInexistente() {
        assertThatThrownBy(() -> loja.fecharCaixa(999L))
            .isInstanceOf(LojaException.class)
            .hasMessage("Caixa não encontrado na loja");
    }
    
    @Test
    @DisplayName("Deve inativar loja com sucesso")
    void deveInativarLojaComSucesso() {
        loja.inativar();
        
        assertThat(loja.getStatus()).isEqualTo(StatusLoja.INATIVA);
        assertThat(loja.isAtiva()).isFalse();
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao inativar loja já inativa")
    void deveLancarExcecaoAoInativarLojaJaInativa() {
        loja.inativar();
        
        assertThatThrownBy(() -> loja.inativar())
            .isInstanceOf(LojaException.class)
            .hasMessage("Loja já está inativa");
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao inativar loja com caixa aberto")
    void deveLancarExcecaoAoInativarLojaComCaixaAberto() {
        loja.abrirCaixa(new BigDecimal("1000.00"), "Responsável Teste");
        
        assertThatThrownBy(() -> loja.inativar())
            .isInstanceOf(LojaException.class)
            .hasMessage("Não é possível inativar loja com caixas abertos");
    }
    
    @Test
    @DisplayName("Deve reativar loja com sucesso")
    void deveReativarLojaComSucesso() {
        loja.inativar();
        loja.reativar();
        
        assertThat(loja.getStatus()).isEqualTo(StatusLoja.ATIVA);
        assertThat(loja.isAtiva()).isTrue();
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao reativar loja já ativa")
    void deveLancarExcecaoAoReativarLojaJaAtiva() {
        assertThatThrownBy(() -> loja.reativar())
            .isInstanceOf(LojaException.class)
            .hasMessage("Loja já está ativa");
    }
    
    @Test
    @DisplayName("Deve calcular quantidade de irmãos ativos corretamente")
    void deveCalcularQuantidadeIrmaosAtivosCorretamente() {
        // Adicionar irmãos com diferentes status
        Irmao irmao1 = Irmao.criar("Irmão Ativo 1", "123.456.789-01", "11999999991", "irmao1@teste.com");
        Irmao irmao2 = Irmao.criar("Irmão Ativo 2", "123.456.789-02", "11999999992", "irmao2@teste.com");
        Irmao irmao3 = Irmao.criar("Irmão Inativo", "123.456.789-03", "11999999993", "irmao3@teste.com");
        
        loja.adicionarIrmao(irmao1);
        loja.adicionarIrmao(irmao2);
        loja.adicionarIrmao(irmao3);
        
        // Inativar um irmão
        irmao3.inativar();
        
        assertThat(loja.getQuantidadeIrmaosAtivos()).isEqualTo(2);
    }
    
    @Test
    @DisplayName("Deve calcular valor total em caixas abertos corretamente")
    void deveCalcularValorTotalCaixasAbertosCorretamente() {
        loja.abrirCaixa(new BigDecimal("1000.00"), "Responsável 1");
        loja.abrirCaixa(new BigDecimal("2000.00"), "Responsável 2");
        
        // Fechar um caixa
        Caixa caixa1 = loja.getCaixas().get(0);
        loja.fecharCaixa(caixa1.getId());
        
        assertThat(loja.getValorTotalCaixasAbertos()).isEqualTo(new BigDecimal("2000.00"));
    }
    
    @Test
    @DisplayName("Deve retornar endereço completo formatado")
    void deveRetornarEnderecoCompletoFormatado() {
        String enderecoCompleto = loja.getEnderecoCompleto();
        
        assertThat(enderecoCompleto).isEqualTo("Rua Teste, 123, São Paulo - SP, 12.345.678/0001-95");
    }
    
    @Test
    @DisplayName("Deve verificar se tem caixas abertos")
    void deveVerificarSeTemCaixasAbertos() {
        assertThat(loja.temCaixasAbertos()).isFalse();
        
        loja.abrirCaixa(new BigDecimal("1000.00"), "Responsável Teste");
        assertThat(loja.temCaixasAbertos()).isTrue();
        
        Caixa caixa = loja.getCaixas().get(0);
        loja.fecharCaixa(caixa.getId());
        assertThat(loja.temCaixasAbertos()).isFalse();
    }
}
