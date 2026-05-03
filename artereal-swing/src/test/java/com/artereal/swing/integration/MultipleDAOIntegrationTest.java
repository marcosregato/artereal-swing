package com.artereal.swing.integration;

import com.artereal.swing.dao.*;
import com.artereal.swing.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de integração entre múltiplos DAOs
 * Verifica o funcionamento correto das relações e consistência entre diferentes entidades
 */
@DisplayName("Testes de Integração - Múltiplos DAOs")
class MultipleDAOIntegrationTest {

    private IrmaoDAO irmaoDAO;
    private SessaoDAO sessaoDAO;
    private FrequenciaDAO frequenciaDAO;
    private CaixaDAO caixaDAO;
    private LojaDAO lojaDAO;
    private UsuarioDAO usuarioDAO;

    @BeforeEach
    void setUp() throws Exception {
        // Inicializa DAOs
        irmaoDAO = DAOFactory.getInstance().getDAO(IrmaoDAO.class);
        sessaoDAO = DAOFactory.getInstance().getDAO(SessaoDAO.class);
        frequenciaDAO = DAOFactory.getInstance().getDAO(FrequenciaDAO.class);
        caixaDAO = DAOFactory.getInstance().getDAO(CaixaDAO.class);
        lojaDAO = DAOFactory.getInstance().getDAO(LojaDAO.class);
        usuarioDAO = DAOFactory.getInstance().getDAO(UsuarioDAO.class);

        // Limpa dados de teste
        limparDadosDeTeste();
    }

    @AfterEach
    void tearDown() throws Exception {
        limparDadosDeTeste();
    }

    private void limparDadosDeTeste() throws Exception {
        try {
            // Limpa em ordem de dependência
            List<Frequencia> frequencias = frequenciaDAO.findAll();
            for (Frequencia f : frequencias) {
                frequenciaDAO.delete(f.getId());
            }

            List<Caixa> caixas = caixaDAO.findAll();
            for (Caixa c : caixas) {
                caixaDAO.delete(c.getId());
            }

            List<Sessao> sessoes = sessaoDAO.findAll();
            for (Sessao s : sessoes) {
                sessaoDAO.delete(s.getId());
            }

            List<Irmao> irmaos = irmaoDAO.findAll();
            for (Irmao i : irmaos) {
                irmaoDAO.delete(i.getId());
            }

            List<Loja> lojas = lojaDAO.findAll();
            for (Loja l : lojas) {
                lojaDAO.delete(l.getId());
            }

            List<Usuario> usuarios = usuarioDAO.findAll();
            for (Usuario u : usuarios) {
                usuarioDAO.delete(u.getId());
            }
        } catch (Exception e) {
            // Ignora erros na limpeza
        }
    }

    @Test
    @DisplayName("Deve criar entidade relacionada e manter consistência")
    void testCriarEntidadeRelacionadaConsistencia() throws Exception {
        // Arrange
        Loja loja = criarLojaTeste();
        lojaDAO.save(loja);

        Irmao irmao = criarIrmaoTeste();
        irmaoDAO.save(irmao);

        // Act
        Sessao sessao = criarSessaoTeste();
        sessaoDAO.save(sessao);

        Frequencia frequencia = criarFrequenciaTeste(irmao);
        frequenciaDAO.save(frequencia);

        // Assert
        assertThat(loja.getId()).isNotNull();
        assertThat(irmao.getId()).isNotNull();
        assertThat(sessao.getId()).isNotNull();
        assertThat(frequencia.getId()).isNotNull();

        // Verifica consistência dos dados
        Loja lojaRecuperada = lojaDAO.findById(loja.getId());
        assertThat(lojaRecuperada).isNotNull();
        assertThat(lojaRecuperada.getNome()).isEqualTo(loja.getNome());

        Irmao irmaoRecuperado = irmaoDAO.findById(irmao.getId());
        assertThat(irmaoRecuperado).isNotNull();
        assertThat(irmaoRecuperado.getNome()).isEqualTo(irmao.getNome());

        List<Frequencia> frequencias = frequenciaDAO.findAll();
        assertThat(frequencias).hasSize(1);
        assertThat(frequencias.get(0).getCodigoIrmao()).isEqualTo(irmao.getId());
    }

    @Test
    @DisplayName("Deve criar múltiplas entidades relacionadas")
    void testCriarMultiplasEntidadesRelacionadas() throws Exception {
        // Arrange
        Loja loja = criarLojaTeste();
        lojaDAO.save(loja);

        Irmao irmao1 = criarIrmaoTeste();
        irmao1.setNome("João da Silva");
        irmaoDAO.save(irmao1);

        Irmao irmao2 = criarIrmaoTeste();
        irmao2.setNome("Pedro Santos");
        irmaoDAO.save(irmao2);

        // Act
        Sessao sessao = criarSessaoTeste();
        sessaoDAO.save(sessao);

        Frequencia freq1 = criarFrequenciaTeste(irmao1);
        frequenciaDAO.save(freq1);

        Frequencia freq2 = criarFrequenciaTeste(irmao2);
        frequenciaDAO.save(freq2);

        // Atualiza quantidade de presentes na sessão
        sessao.setQuantidadePresentes(2);
        sessaoDAO.save(sessao);

        // Assert
        Sessao sessaoRecuperada = sessaoDAO.findById(sessao.getId());
        assertThat(sessaoRecuperada.getQuantidadePresentes()).isEqualTo(2);

        List<Frequencia> presencas = frequenciaDAO.findAll();
        assertThat(presencas).hasSize(2);
        
        // Verifica se os irmãos corretos estão registrados
        assertThat(presencas).anyMatch(f -> f.getNomeIrmao().equals("João da Silva"));
        assertThat(presencas).anyMatch(f -> f.getNomeIrmao().equals("Pedro Santos"));
    }

    @Test
    @DisplayName("Deve manter integridade referencial ao excluir")
    void testManterIntegridadeReferencialExclusao() throws Exception {
        // Arrange
        Loja loja = criarLojaTeste();
        lojaDAO.save(loja);

        Irmao irmao = criarIrmaoTeste();
        irmaoDAO.save(irmao);

        Sessao sessao = criarSessaoTeste();
        sessaoDAO.save(sessao);

        Frequencia frequencia = criarFrequenciaTeste(irmao);
        frequenciaDAO.save(frequencia);

        Caixa caixa = criarCaixaTeste();
        caixaDAO.save(caixa);

        // Verifica que tudo foi criado
        assertThat(lojaDAO.findById(loja.getId())).isNotNull();
        assertThat(irmaoDAO.findById(irmao.getId())).isNotNull();
        assertThat(sessaoDAO.findById(sessao.getId())).isNotNull();
        assertThat(frequenciaDAO.findById(frequencia.getId())).isNotNull();
        assertThat(caixaDAO.findById(caixa.getId())).isNotNull();

        // Act - Excluir em ordem correta
        caixaDAO.delete(caixa.getId());
        frequenciaDAO.delete(frequencia.getId());
        sessaoDAO.delete(sessao.getId());
        irmaoDAO.delete(irmao.getId());
        lojaDAO.delete(loja.getId());

        // Assert
        assertThat(caixaDAO.findById(caixa.getId())).isNull();
        assertThat(frequenciaDAO.findById(frequencia.getId())).isNull();
        assertThat(sessaoDAO.findById(sessao.getId())).isNull();
        assertThat(irmaoDAO.findById(irmao.getId())).isNull();
        assertThat(lojaDAO.findById(loja.getId())).isNull();
    }

    @Test
    @DisplayName("Deve criar fluxo completo de negócio")
    void testFluxoCompletoNegocio() throws Exception {
        // Arrange & Act - Simula um fluxo completo de admissão
        
        // 1. Criar loja
        Loja loja = criarLojaTeste();
        loja.setNome("Loja ArteReal São Paulo");
        loja.setNumero("123");
        lojaDAO.save(loja);

        // 2. Admitir novo irmão
        Irmao novoIrmao = criarIrmaoTeste();
        novoIrmao.setNome("Carlos Alberto");
        novoIrmao.setGrau("APRENDIZ");
        irmaoDAO.save(novoIrmao);

        // 3. Criar sessão de iniciação
        Sessao sessaoIniciacao = criarSessaoTeste();
        sessaoIniciacao.setTipo("INICIACAO");
        sessaoIniciacao.setPauta("Sessão de iniciação do Irmão Carlos Alberto");
        sessaoDAO.save(sessaoIniciacao);

        // 4. Registrar presença
        Frequencia presenca = criarFrequenciaTeste(novoIrmao);
        presenca.setGrau("APRENDIZ");
        presenca.setNumeroPresencas(1);
        frequenciaDAO.save(presenca);

        // 5. Registrar pagamento de taxa
        Caixa taxaIniciacao = criarCaixaTeste();
        taxaIniciacao.setTipo("ENTRADA");
        taxaIniciacao.setDescricao("Taxa de iniciação - Carlos Alberto");
        taxaIniciacao.setValor(new BigDecimal("500.00"));
        taxaIniciacao.setCategoria("TAXA_INICIACAO");
        caixaDAO.save(taxaIniciacao);

        // 6. Atualizar quantidade de presentes
        sessaoIniciacao.setQuantidadePresentes(1);
        sessaoDAO.save(sessaoIniciacao);

        // Assert - Verifica consistência do fluxo
        assertThat(lojaDAO.findById(loja.getId())).isNotNull();
        assertThat(irmaoDAO.findById(novoIrmao.getId())).isNotNull();
        assertThat(sessaoDAO.findById(sessaoIniciacao.getId())).isNotNull();
        assertThat(frequenciaDAO.findById(presenca.getId())).isNotNull();
        assertThat(caixaDAO.findById(taxaIniciacao.getId())).isNotNull();

        // Verifica dados específicos
        Irmao irmaoRecuperado = irmaoDAO.findById(novoIrmao.getId());
        assertThat(irmaoRecuperado.getNome()).isEqualTo("Carlos Alberto");
        assertThat(irmaoRecuperado.getGrau()).isEqualTo("APRENDIZ");

        Sessao sessaoRecuperada = sessaoDAO.findById(sessaoIniciacao.getId());
        assertThat(sessaoRecuperada.getTipo()).isEqualTo("INICIACAO");
        assertThat(sessaoRecuperada.getQuantidadePresentes()).isEqualTo(1);

        List<Caixa> movimentacoes = caixaDAO.findAll();
        assertThat(movimentacoes).hasSize(1);
        assertThat(movimentacoes.get(0).getDescricao()).contains("Carlos Alberto");
        assertThat(movimentacoes.get(0).getValor()).isEqualTo(new BigDecimal("500.00"));
    }

    // Métodos auxiliares para criação de entidades de teste
    
    private Loja criarLojaTeste() {
        Loja loja = new Loja();
        loja.setNome("Loja Teste");
        loja.setNumero("999");
        loja.setEndereco("Rua Teste, 123");
        loja.setBairro("Centro");
        loja.setCidade("São Paulo");
        loja.setEstado("SP");
        loja.setCep("01234-567");
        loja.setTelefone("(11) 1234-5678");
        loja.setEmail("teste@loja.com");
        loja.setPresidente("Venerável Teste");
        loja.setSecretario("Secretário Teste");
        loja.setTesoureiro("Tesoureiro Teste");
        loja.setDataFundacao(LocalDateTime.now().minusYears(50).toString());
        loja.setRito("Escocês Antigo e Aceito");
        loja.setPotencia("Grande Loja do Estado de São Paulo");
        loja.setStatus("ATIVA");
        return loja;
    }

    private Irmao criarIrmaoTeste() {
        Irmao irmao = new Irmao();
        irmao.setNome("Irmão Teste");
        irmao.setNascimento(LocalDateTime.now().toLocalDate().minusYears(30));
        irmao.setEstadoCivil("Casado");
        irmao.setNatural("São Paulo");
        irmao.setIdentidade("12.345.678-9");
        irmao.setTipoSanguineo("O+");
        irmao.setCargoLoja("Membro");
        irmao.setGrau("MESTRE");
        irmao.setCargoGrandeLoja("Nenhum");
        irmao.setEndereco("Rua do Irmão, 456");
        irmao.setBairro("Bairro Teste");
        irmao.setCidade("São Paulo");
        irmao.setEstado("SP");
        irmao.setTelefone("(11) 9876-5432");
        irmao.setEmpresa("Empresa Teste");
        irmao.setTelefoneEmpresa("(11) 2345-6789");
        irmao.setEnderecoEmpresa("Av. Empresa, 789");
        irmao.setRegistroGrandeLoja("REG-12345");
        irmao.setAtivo(true);
        return irmao;
    }

    private Sessao criarSessaoTeste() {
        Sessao sessao = new Sessao();
        sessao.setTipo("BRANCA");
        sessao.setDataHora(LocalDateTime.now());
        sessao.setLocal("Templo da Loja");
        sessao.setPresidente("Venerável Mestre");
        sessao.setSecretario("Secretário");
        sessao.setTesoureiro("Tesoureiro");
        sessao.setOrador("Orador Convidado");
        sessao.setPauta("Sessão ordinária");
        sessao.setObservacoes("Sessão de teste");
        sessao.setStatus("PROGRAMADA");
        sessao.setQuantidadePresentes(0);
        sessao.setQuantidadeVisitantes(0);
        return sessao;
    }

    private Frequencia criarFrequenciaTeste(Irmao irmao) {
        Frequencia frequencia = new Frequencia();
        frequencia.setCodigoIrmao(irmao.getId());
        frequencia.setNomeIrmao(irmao.getNome());
        frequencia.setRegistroGrandeLoja(irmao.getRegistroGrandeLoja());
        frequencia.setGrau(irmao.getGrau());
        frequencia.setDataInstalado(LocalDateTime.now().toLocalDate().minusYears(5));
        frequencia.setIrregular(false);
        frequencia.setNumeroPresencas(1);
        frequencia.setNumeroFaltas(0);
        frequencia.setNumeroSecoes(0);
        frequencia.setNomeHistorico("Regular");
        frequencia.setPresencaDiretoria(false);
        frequencia.setSecretariaDiretoria(false);
        return frequencia;
    }

    private Caixa criarCaixaTeste() {
        Caixa caixa = new Caixa();
        caixa.setTipo("ENTRADA");
        caixa.setValor(new BigDecimal("100.00"));
        caixa.setDescricao("Movimentação de teste");
        caixa.setCategoria("TESTE");
        caixa.setDataMovimentacao(LocalDateTime.now());
        caixa.setResponsavel("Responsável Teste");
        caixa.setFormaPagamento("DINHEIRO");
        caixa.setNumeroDocumento("N/A");
        caixa.setStatus("CONFIRMADO");
        caixa.setObservacoes("Observação de teste");
        return caixa;
    }

    }
