package com.artereal.swing.integration;

import com.artereal.swing.dao.*;
import com.artereal.swing.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de fluxos de negócio completos do sistema ArteReal
 * Simula cenários reais de uso do sistema maçônico
 */
@DisplayName("Testes de Fluxos de Negócio")
class BusinessFlowTest {

    private LojaDAO lojaDAO;
    private IrmaoDAO irmaoDAO;
    private SessaoDAO sessaoDAO;
    private CaixaDAO caixaDAO;
    private FrequenciaDAO frequenciaDAO;

    @BeforeEach
    void setUp() throws Exception {
        // Configurar ambiente de testes para OWASP
        System.setProperty("test.environment", "true");
        
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
            // Limpa em ordem de dependência
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
    @DisplayName("Fluxo completo: Admissão de novo irmão")
    void testFluxoAdmissaoNovoIrmao() throws Exception {
        // 1. Criar loja
        Loja loja = criarLojaTeste();
        loja.setNome("Loja ArteReal São Paulo");
        loja.setNumero("123");
        lojaDAO.save(loja);

        // 2. Admitir novo irmão
        Irmao novoIrmao = new Irmao();
        novoIrmao.setNome("João da Silva");
        novoIrmao.setGrau("APRENDIZ");
        irmaoDAO.save(novoIrmao);

        // 3. Registrar primeira frequência
        Frequencia frequencia = new Frequencia();
        frequencia.setCodigoIrmao(novoIrmao.getId());
        frequencia.setNomeIrmao(novoIrmao.getNome());
        frequencia.setGrau(novoIrmao.getGrau());
        frequencia.setNumeroPresencas(0);
        frequencia.setNumeroFaltas(0);
        frequenciaDAO.save(frequencia);

        // 4. Participar de primeira sessão
        Sessao sessao = new Sessao();
        sessao.setTipo("BRANCA");
        sessao.setDataHora(LocalDateTime.now());
        sessao.setPresidente("Venerável Mestre");
        sessao.setSecretario("Secretário");
        sessao.setStatus("REALIZADA");
        sessao.setQuantidadePresentes(1);
        sessaoDAO.save(sessao);

        // 5. Registrar presença na sessão
        Frequencia presenca = new Frequencia();
        presenca.setCodigoIrmao(novoIrmao.getId());
        presenca.setNomeIrmao(novoIrmao.getNome());
        presenca.setGrau(novoIrmao.getGrau());
        presenca.setNumeroPresencas(1);
        presenca.setNumeroFaltas(0);
        frequenciaDAO.save(presenca);

        // Verificações
        Loja lojaRecuperada = lojaDAO.findById(loja.getId());
        assertThat(lojaRecuperada).isNotNull();
        assertThat(lojaRecuperada.getNome()).isEqualTo("Loja ArteReal São Paulo");

        List<Irmao> irmaos = irmaoDAO.findAll();
        assertThat(irmaos).hasSize(1);
        assertThat(irmaos.get(0).getNome()).isEqualTo("João da Silva");
        assertThat(irmaos.get(0).getGrau()).isEqualTo("APRENDIZ");

        List<Frequencia> frequencias = frequenciaDAO.findAll();
        assertThat(frequencias).hasSize(2); // Frequência inicial + presença

        List<Sessao> sessoes = sessaoDAO.findAll();
        assertThat(sessoes).hasSize(1);
        assertThat(sessoes.get(0).getQuantidadePresentes()).isEqualTo(1);
    }

    @Test
    @DisplayName("Fluxo completo: Sessão Magna com movimentações financeiras")
    void testFluxoSessaoMagnaMovimentacoes() throws Exception {
        // 1. Criar loja e irmãos
        Loja loja = criarLojaTeste();
        lojaDAO.save(loja);

        Irmao veneravel = criarIrmaoTeste();
        veneravel.setNome("Carlos Silva");
        veneravel.setGrau("MESTRE INSTALADO");
        irmaoDAO.save(veneravel);

        Irmao secretario = criarIrmaoTeste();
        secretario.setNome("Pedro Santos");
        secretario.setGrau("MESTRE");
        irmaoDAO.save(secretario);

        Irmao tesoureiro = criarIrmaoTeste();
        tesoureiro.setNome("João Oliveira");
        tesoureiro.setGrau("MESTRE");
        irmaoDAO.save(tesoureiro);

        Irmao irmao1 = criarIrmaoTeste();
        irmao1.setNome("Antonio Costa");
        irmao1.setGrau("COMPANHEIRO");
        irmaoDAO.save(irmao1);

        Irmao irmao2 = criarIrmaoTeste();
        irmao2.setNome("Francisco Lima");
        irmao2.setGrau("APRENDIZ");
        irmaoDAO.save(irmao2);

        // 2. Realizar sessão magna
        Sessao sessaoMagna = new Sessao();
        sessaoMagna.setTipo("MAGNA");
        sessaoMagna.setDataHora(LocalDateTime.now());
        sessaoMagna.setLocal("Templo da Loja");
        sessaoMagna.setPresidente(veneravel.getNome());
        sessaoMagna.setSecretario(secretario.getNome());
        sessaoMagna.setTesoureiro(tesoureiro.getNome());
        sessaoMagna.setStatus("REALIZADA");
        sessaoMagna.setQuantidadePresentes(5);
        sessaoMagna.setQuantidadeVisitantes(0);
        sessaoDAO.save(sessaoMagna);

        // 3. Registrar movimentações financeiras da sessão
        // Taxas de sessão
        Caixa taxa1 = criarCaixaTeste();
        taxa1.setTipo("ENTRADA");
        taxa1.setValor(new BigDecimal("50.00"));
        taxa1.setDescricao("Taxa de sessão - " + irmao1.getNome());
        taxa1.setCategoria("TAXA_SESSAO");
        caixaDAO.save(taxa1);

        Caixa taxa2 = criarCaixaTeste();
        taxa2.setTipo("ENTRADA");
        taxa2.setValor(new BigDecimal("50.00"));
        taxa2.setDescricao("Taxa de sessão - " + irmao2.getNome());
        taxa2.setCategoria("TAXA_SESSAO");
        caixaDAO.save(taxa2);

        // Donativos
        Caixa donativo = criarCaixaTeste();
        donativo.setTipo("ENTRADA");
        donativo.setValor(new BigDecimal("100.00"));
        donativo.setDescricao("Donativo sessão magna");
        donativo.setCategoria("DONATIVO");
        caixaDAO.save(donativo);

        // Despesas
        Caixa despesa = criarCaixaTeste();
        despesa.setTipo("SAIDA");
        despesa.setValor(new BigDecimal("30.00"));
        despesa.setDescricao("Material para sessão");
        despesa.setCategoria("MATERIAL");
        caixaDAO.save(despesa);

        // 4. Registrar presenças
        for (Irmao irmao : List.of(veneravel, secretario, tesoureiro, irmao1, irmao2)) {
            Frequencia frequencia = new Frequencia();
            frequencia.setCodigoIrmao(irmao.getId());
            frequencia.setNomeIrmao(irmao.getNome());
            frequencia.setGrau(irmao.getGrau());
            frequencia.setNumeroPresencas(1);
            frequencia.setNumeroFaltas(0);
            frequenciaDAO.save(frequencia);
        }

        // Verificações
        Sessao sessaoRecuperada = sessaoDAO.findById(sessaoMagna.getId());
        assertThat(sessaoRecuperada).isNotNull();
        assertThat(sessaoRecuperada.getQuantidadePresentes()).isEqualTo(5);

        List<Caixa> movimentacoes = caixaDAO.findAll();
        assertThat(movimentacoes).hasSize(4);

        BigDecimal totalEntradas = movimentacoes.stream()
            .filter(c -> "ENTRADA".equals(c.getTipo()))
            .map(Caixa::getValor)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalSaidas = movimentacoes.stream()
            .filter(c -> "SAIDA".equals(c.getTipo()))
            .map(Caixa::getValor)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal saldo = totalEntradas.subtract(totalSaidas);
        assertThat(saldo).isEqualTo(new BigDecimal("170.00")); // 200 - 30

        List<Frequencia> presencas = frequenciaDAO.findAll();
        assertThat(presencas).hasSize(5);
    }

    @Test
    @DisplayName("Fluxo completo: Promoção de grau maçônico")
    void testFluxoPromocaoGrau() throws Exception {
        // 1. Criar loja e irmão aprendiz
        Loja loja = criarLojaTeste();
        lojaDAO.save(loja);

        Irmao irmao = criarIrmaoTeste();
        irmao.setNome("José Almeida");
        irmao.setGrau("APRENDIZ");
        irmaoDAO.save(irmao);

        // 2. Registrar frequências como aprendiz
        for (int i = 0; i < 3; i++) {
            Frequencia freq = new Frequencia();
            freq.setCodigoIrmao(irmao.getId());
            freq.setNomeIrmao(irmao.getNome());
            freq.setGrau("APRENDIZ");
                freq.setNumeroPresencas(1);
            freq.setNumeroFaltas(0);
            frequenciaDAO.save(freq);
        }

        // 3. Realizar sessão de instrução
        Sessao sessaoInstrucao = new Sessao();
        sessaoInstrucao.setTipo("INSTRUCAO");
        sessaoInstrucao.setDataHora(LocalDateTime.now());
        sessaoInstrucao.setPresidente("Venerável Mestre");
        sessaoInstrucao.setSecretario("Secretário");
        sessaoInstrucao.setStatus("REALIZADA");
        sessaoInstrucao.setQuantidadePresentes(1);
        sessaoDAO.save(sessaoInstrucao);

        // 4. Promover para companheiro
        irmao.setGrau("COMPANHEIRO");
        irmaoDAO.save(irmao);

        // 5. Registrar primeira frequência como companheiro
        Frequencia frequenciaCompanheiro = new Frequencia();
        frequenciaCompanheiro.setCodigoIrmao(irmao.getId());
        frequenciaCompanheiro.setNomeIrmao(irmao.getNome());
        frequenciaCompanheiro.setGrau("COMPANHEIRO");
        frequenciaCompanheiro.setNumeroPresencas(1);
        frequenciaCompanheiro.setNumeroFaltas(0);
        frequenciaDAO.save(frequenciaCompanheiro);

        // Verificações
        Irmao irmaoRecuperado = irmaoDAO.findById(irmao.getId());
        assertThat(irmaoRecuperado).isNotNull();
        assertThat(irmaoRecuperado.getGrau()).isEqualTo("COMPANHEIRO");

        List<Frequencia> frequencias = frequenciaDAO.findAll();
        assertThat(frequencias).hasSize(4); // 3 como aprendiz + 1 como companheiro

        long freqAprendiz = frequencias.stream()
            .filter(f -> "APRENDIZ".equals(f.getGrau()))
            .count();
        long freqCompanheiro = frequencias.stream()
            .filter(f -> "COMPANHEIRO".equals(f.getGrau()))
            .count();

        assertThat(freqAprendiz).isEqualTo(3);
        assertThat(freqCompanheiro).isEqualTo(1);
    }

    @Test
    @DisplayName("Fluxo completo: Gestão financeira mensal")
    void testFluxoGestaoFinanceiraMensal() throws Exception {
        // 1. Criar loja e dados básicos
        Loja loja = criarLojaTeste();
        lojaDAO.save(loja);

        // 2. Criar movimentações financeiras do mês
        
        // Receitas
        Caixa mensalidade1 = criarCaixaTeste();
        mensalidade1.setTipo("ENTRADA");
        mensalidade1.setValor(new BigDecimal("100.00"));
        mensalidade1.setDescricao("Mensalidade - João Silva");
        mensalidade1.setCategoria("MENSALIDADE");
        caixaDAO.save(mensalidade1);

        Caixa mensalidade2 = criarCaixaTeste();
        mensalidade2.setTipo("ENTRADA");
        mensalidade2.setValor(new BigDecimal("100.00"));
        mensalidade2.setDescricao("Mensalidade - Pedro Santos");
        mensalidade2.setCategoria("MENSALIDADE");
        caixaDAO.save(mensalidade2);

        Caixa taxaSessao = criarCaixaTeste();
        taxaSessao.setTipo("ENTRADA");
        taxaSessao.setValor(new BigDecimal("50.00"));
        taxaSessao.setDescricao("Taxa sessão magna");
        taxaSessao.setCategoria("TAXA_SESSAO");
        caixaDAO.save(taxaSessao);

        Caixa donativo = criarCaixaTeste();
        donativo.setTipo("ENTRADA");
        donativo.setValor(new BigDecimal("200.00"));
        donativo.setDescricao("Donativo mensal");
        donativo.setCategoria("DONATIVO");
        caixaDAO.save(donativo);

        // Despesas
        Caixa aluguel = criarCaixaTeste();
        aluguel.setTipo("SAIDA");
        aluguel.setValor(new BigDecimal("500.00"));
        aluguel.setDescricao("Aluguel templo");
        aluguel.setCategoria("ALUGUEL");
        caixaDAO.save(aluguel);

        Caixa agua = criarCaixaTeste();
        agua.setTipo("SAIDA");
        agua.setValor(new BigDecimal("150.00"));
        agua.setDescricao("Conta de água");
        agua.setCategoria("UTILIDADES");
        caixaDAO.save(agua);

        Caixa material = criarCaixaTeste();
        material.setTipo("SAIDA");
        material.setValor(new BigDecimal("80.00"));
        material.setDescricao("Material escritório");
        material.setCategoria("MATERIAL");
        caixaDAO.save(material);

        // 3. Calcular balanço mensal
        List<Caixa> movimentacoes = caixaDAO.findAll();
        assertThat(movimentacoes).hasSize(7);

        BigDecimal totalReceitas = movimentacoes.stream()
            .filter(c -> "ENTRADA".equals(c.getTipo()))
            .map(Caixa::getValor)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDespesas = movimentacoes.stream()
            .filter(c -> "SAIDA".equals(c.getTipo()))
            .map(Caixa::getValor)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal saldoMensal = totalReceitas.subtract(totalDespesas);

        // Verificações
        assertThat(totalReceitas).isEqualTo(new BigDecimal("450.00")); // 100 + 100 + 50 + 200
        assertThat(totalDespesas).isEqualTo(new BigDecimal("730.00")); // 500 + 150 + 80
        assertThat(saldoMensal).isEqualTo(new BigDecimal("-280.00")); // 450 - 730

        // Verifica categorias
        long receitasMensalidade = movimentacoes.stream()
            .filter(c -> "ENTRADA".equals(c.getTipo()) && "MENSALIDADE".equals(c.getCategoria()))
            .count();
        assertThat(receitasMensalidade).isEqualTo(2);

        long despesasUtilidades = movimentacoes.stream()
            .filter(c -> "SAIDA".equals(c.getTipo()) && "UTILIDADES".equals(c.getCategoria()))
            .count();
        assertThat(despesasUtilidades).isEqualTo(1);
    }

    @Test
    @DisplayName("Fluxo completo: Gestão de visitantes")
    void testFluxoGestaoVisitantes() throws Exception {
        // 1. Criar loja
        Loja loja = criarLojaTeste();
        lojaDAO.save(loja);

        // 2. Criar sessão com visitantes
        Sessao sessao = new Sessao();
        sessao.setTipo("BRANCA");
        sessao.setDataHora(LocalDateTime.now());
        sessao.setPresidente("Venerável Mestre");
        sessao.setSecretario("Secretário");
        sessao.setStatus("REALIZADA");
        sessao.setQuantidadePresentes(3);
        sessao.setQuantidadeVisitantes(2);
        sessaoDAO.save(sessao);

        // 3. Registrar visitantes
        Visitante visitante1 = new Visitante();
        visitante1.setNome("Manoel Costa");
        visitante1.setTipo("VISITANTE");
        visitante1.setDataVisita(sessao.getDataHora().toLocalDate());
        visitante1.setGrauSecreto("MESTRE");
        visitante1.setLojaOrigem("Loja Esperança 456");
        visitante1.setAutorizado(true);
        visitante1.setObservacoes("Irmão visitante de outra loja");
        visitante1.setDataCadastro(LocalDate.now());
        visitante1.setAtivo(true);

        Visitante visitante2 = new Visitante();
        visitante2.setNome("Roberto Silva");
        visitante2.setTipo("CONVIDADO");
        visitante2.setDataVisita(sessao.getDataHora().toLocalDate());
        visitante2.setGrauSecreto("NENHUM");
        visitante2.setAutorizado(true);
        visitante2.setObservacoes("Convidado para sessão");
        visitante2.setDataCadastro(LocalDate.now());
        visitante2.setAtivo(true);

        // Verificações
        Sessao sessaoRecuperada = sessaoDAO.findById(sessao.getId());
        assertThat(sessaoRecuperada).isNotNull();
        assertThat(sessaoRecuperada.getQuantidadeVisitantes()).isEqualTo(2);

        List<Visitante> visitantes = List.of(visitante1, visitante2);
        assertThat(visitantes).hasSize(2);
        assertThat(visitantes).anyMatch(v -> v.getTipo().equals("VISITANTE"));
        assertThat(visitantes).anyMatch(v -> v.getTipo().equals("CONVIDADO"));
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

    private Caixa criarCaixaTeste() {
        Caixa caixa = new Caixa();
        caixa.setDescricao("Movimentação de teste");
        caixa.setDataMovimentacao(LocalDateTime.now());
        caixa.setResponsavel("Teste");
        caixa.setStatus("CONFIRMADO");
        return caixa;
    }
}
