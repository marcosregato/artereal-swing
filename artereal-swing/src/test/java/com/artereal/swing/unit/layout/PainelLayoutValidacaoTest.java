package com.artereal.swing.unit.layout;

import com.artereal.swing.ui.layout.PadraoLayout;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import javax.swing.*;
import java.awt.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes para validar se os componentes criados seguem o PadraoLayout
 */
@DisplayName("Testes de Validação de Layout com PadraoLayout")
class PainelLayoutValidacaoTest {

    @Test
    @DisplayName("Componentes criados com PadraoLayout devem ter conformidade visual")
    void testComponentesPadraoLayout() {
        // Testar botões
        JButton botaoSalvar = PadraoLayout.criarBotaoSalvar();
        assertThat(botaoSalvar.getBackground()).isEqualTo(PadraoLayout.COR_BOTAO_SALVAR);
        assertThat(botaoSalvar.getForeground()).isEqualTo(Color.BLACK);
        assertThat(botaoSalvar.getFont()).isEqualTo(PadraoLayout.FONTE_BOTAO);

        JButton botaoEditar = PadraoLayout.criarBotaoEditar();
        assertThat(botaoEditar.getBackground()).isEqualTo(PadraoLayout.COR_BOTAO_EDITAR);
        assertThat(botaoEditar.getForeground()).isEqualTo(Color.BLACK);
        assertThat(botaoEditar.getFont()).isEqualTo(PadraoLayout.FONTE_BOTAO);

        JButton botaoExcluir = PadraoLayout.criarBotaoExcluir();
        assertThat(botaoExcluir.getBackground()).isEqualTo(PadraoLayout.COR_BOTAO_EXCLUIR);
        assertThat(botaoExcluir.getForeground()).isEqualTo(Color.BLACK);
        assertThat(botaoExcluir.getFont()).isEqualTo(PadraoLayout.FONTE_BOTAO);

        JButton botaoNovo = PadraoLayout.criarBotaoNovo();
        assertThat(botaoNovo.getBackground()).isEqualTo(PadraoLayout.COR_BOTAO_NOVO);
        assertThat(botaoNovo.getForeground()).isEqualTo(Color.BLACK);
        assertThat(botaoNovo.getFont()).isEqualTo(PadraoLayout.FONTE_BOTAO);

        JButton botaoLimpar = PadraoLayout.criarBotaoLimpar();
        assertThat(botaoLimpar.getBackground()).isEqualTo(PadraoLayout.COR_BOTAO_LIMPAR);
        assertThat(botaoLimpar.getForeground()).isEqualTo(Color.BLACK);
        assertThat(botaoLimpar.getFont()).isEqualTo(PadraoLayout.FONTE_BOTAO);

        JButton botaoPesquisar = PadraoLayout.criarBotaoPesquisar();
        assertThat(botaoPesquisar.getBackground()).isEqualTo(PadraoLayout.COR_BOTAO_PESQUISAR);
        assertThat(botaoPesquisar.getForeground()).isEqualTo(Color.BLACK);
        assertThat(botaoPesquisar.getFont()).isEqualTo(PadraoLayout.FONTE_BOTAO);
    }

    @Test
    @DisplayName("Painéis criados com PadraoLayout devem ter cores e bordas corretas")
    void testPaineisPadraoLayout() {
        // Testar painel header
        JPanel header = PadraoLayout.criarHeader("Título Teste", "Subtítulo Teste");
        assertThat(header.getBackground()).isEqualTo(PadraoLayout.COR_HEADER);
        assertThat(header.getBorder()).isNotNull();

        // Testar painel de pesquisa
        JTextField campoPesquisa = new JTextField();
        JButton botaoPesquisa = PadraoLayout.criarBotaoPesquisar();
        JPanel painelPesquisa = PadraoLayout.criarPainelPesquisa(campoPesquisa, botaoPesquisa);
        assertThat(painelPesquisa.getBackground()).isEqualTo(PadraoLayout.COR_PAINEL);
        assertThat(painelPesquisa.getBorder()).isEqualTo(PadraoLayout.BORDA_PAINEL);

        // Testar grupo de formulário
        JPanel grupoForm = PadraoLayout.criarGrupoFormulario("Formulário Teste");
        assertThat(grupoForm.getBackground()).isEqualTo(Color.WHITE);
        assertThat(grupoForm.getBorder()).isEqualTo(PadraoLayout.BORDA_GRUPO);
    }

    @Test
    @DisplayName("Campos de texto estilizados devem seguir o padrão")
    void testCamposTextoEstilizados() {
        JTextField campo = new JTextField();
        JPasswordField senha = new JPasswordField();
        JComboBox<String> combo = new JComboBox<>(new String[]{"Opção 1", "Opção 2"});

        // Estilizar componentes
        PadraoLayout.estilizarCampoTexto(campo);
        PadraoLayout.estilizarCampoTexto(senha);
        PadraoLayout.estilizarComboBox(combo);

        // Verificar estilização
        assertThat(campo.getBorder()).isEqualTo(PadraoLayout.BORDA_CAMPO);
        assertThat(campo.getBackground()).isEqualTo(Color.WHITE);

        assertThat(senha.getBorder()).isEqualTo(PadraoLayout.BORDA_CAMPO);
        assertThat(senha.getBackground()).isEqualTo(Color.WHITE);

        assertThat(combo.getBorder()).isEqualTo(PadraoLayout.BORDA_CAMPO);
        assertThat(combo.getBackground()).isEqualTo(Color.WHITE);
    }

    @Test
    @DisplayName("Labels criados devem seguir o padrão de cores e fontes")
    void testLabelsPadraoLayout() {
        JLabel labelForm = PadraoLayout.criarLabelFormulario("Campo Teste");
        assertThat(labelForm.getText()).isEqualTo("Campo Teste");
        assertThat(labelForm.getForeground()).isEqualTo(PadraoLayout.COR_TEXTO_TITULO);
        assertThat(labelForm.getFont().getName()).isEqualTo("Segoe UI");
    }

    @Test
    @DisplayName("Tabelas configuradas devem seguir o padrão visual")
    void testTabelasConfiguradas() {
        JTable tabela = new JTable();
        
        // Configurar tabela
        PadraoLayout.configurarTabela(tabela);

        // Verificar configurações - aceitamos tanto COR_HEADER quanto COR_PRIMARIA
        assertThat(tabela.getTableHeader().getBackground()).isIn(PadraoLayout.COR_HEADER, PadraoLayout.COR_PRIMARIA);
        assertThat(tabela.getTableHeader().getForeground()).isEqualTo(PadraoLayout.COR_TEXTO_BRANCO);
        assertThat(tabela.getTableHeader().getFont()).isEqualTo(PadraoLayout.FONTE_HEADER_TABELA);
        
        assertThat(tabela.getSelectionBackground()).isEqualTo(PadraoLayout.COR_SELECAO_TABELA);
        assertThat(tabela.getSelectionForeground()).isEqualTo(PadraoLayout.COR_SELECAO_TEXTO);
        assertThat(tabela.getFont()).isEqualTo(PadraoLayout.FONTE_TABELA);
        assertThat(tabela.getRowHeight()).isEqualTo(PadraoLayout.ALTURA_LINHA_TABELA);
    }

    @Test
    @DisplayName("Layout de formulário deve ter configurações corretas")
    void testLayoutFormulario() {
        GridLayout layout = PadraoLayout.criarLayoutFormulario();

        assertThat(layout.getColumns()).isEqualTo(PadraoLayout.COLUNAS_FORMULARIO);
        assertThat(layout.getHgap()).isEqualTo(PadraoLayout.ESPACAMENTO_CAMPOS);
        assertThat(layout.getVgap()).isEqualTo(PadraoLayout.ESPACAMENTO_GRID);
    }

    @Test
    @DisplayName("Botões criados devem ter tamanho padrão")
    void testTamanhoBotoes() {
        JButton botaoSalvar = PadraoLayout.criarBotaoSalvar();
        JButton botaoEditar = PadraoLayout.criarBotaoEditar();
        JButton botaoExcluir = PadraoLayout.criarBotaoExcluir();

        // Verificar se os botões têm dimensões razoáveis
        assertThat(botaoSalvar.getPreferredSize().width).isGreaterThan(50);
        assertThat(botaoSalvar.getPreferredSize().height).isGreaterThan(20);
        
        assertThat(botaoEditar.getPreferredSize().width).isGreaterThan(50);
        assertThat(botaoEditar.getPreferredSize().height).isGreaterThan(20);
        
        assertThat(botaoExcluir.getPreferredSize().width).isGreaterThan(50);
        assertThat(botaoExcluir.getPreferredSize().height).isGreaterThan(20);
    }

    @Test
    @DisplayName("Cores do sistema devem ser consistentes")
    void testCoresSistema() {
        // Verificar cores primárias
        assertThat(PadraoLayout.COR_PRIMARIA).isEqualTo(new Color(70, 130, 180));
        assertThat(PadraoLayout.COR_HEADER).isEqualTo(new Color(25, 25, 112));
        assertThat(PadraoLayout.COR_FUNDO).isEqualTo(new Color(245, 245, 250));
        assertThat(PadraoLayout.COR_PAINEL).isEqualTo(new Color(240, 240, 245));

        // Verificar cores de botões
        assertThat(PadraoLayout.COR_BOTAO_SALVAR).isEqualTo(new Color(144, 238, 144));
        assertThat(PadraoLayout.COR_BOTAO_NOVO).isEqualTo(new Color(173, 216, 230));
        assertThat(PadraoLayout.COR_BOTAO_EDITAR).isEqualTo(new Color(255, 250, 205));
        assertThat(PadraoLayout.COR_BOTAO_EXCLUIR).isEqualTo(new Color(255, 182, 193));
        assertThat(PadraoLayout.COR_BOTAO_LIMPAR).isEqualTo(new Color(240, 240, 240));
        assertThat(PadraoLayout.COR_BOTAO_PESQUISAR).isEqualTo(new Color(221, 160, 221));

        // Verificar cores de texto
        assertThat(PadraoLayout.COR_TEXTO_TITULO).isEqualTo(new Color(70, 130, 180));
        assertThat(PadraoLayout.COR_TEXTO_SUBTITULO).isEqualTo(new Color(173, 216, 230));
        assertThat(PadraoLayout.COR_TEXTO_BRANCO).isEqualTo(Color.WHITE);
        assertThat(PadraoLayout.COR_SELECAO_TABELA).isEqualTo(new Color(173, 216, 230));
        assertThat(PadraoLayout.COR_SELECAO_TEXTO).isEqualTo(new Color(25, 84, 123));
    }

    @Test
    @DisplayName("Fontes do sistema devem ser consistentes")
    void testFontesSistema() {
        // Verificar fontes
        assertThat(PadraoLayout.FONTE_TITULO).isEqualTo(new Font("Segoe UI", Font.BOLD, 18));
        assertThat(PadraoLayout.FONTE_SUBTITULO).isEqualTo(new Font("Segoe UI", Font.PLAIN, 12));
        assertThat(PadraoLayout.FONTE_GRUPO).isEqualTo(new Font("Segoe UI", Font.BOLD, 12));
        assertThat(PadraoLayout.FONTE_PESQUISA).isEqualTo(new Font("Segoe UI", Font.BOLD, 12));
        assertThat(PadraoLayout.FONTE_BOTAO).isEqualTo(new Font("Segoe UI", Font.BOLD, 12));
        assertThat(PadraoLayout.FONTE_TABELA).isEqualTo(new Font("Segoe UI", Font.PLAIN, 12));
        assertThat(PadraoLayout.FONTE_HEADER_TABELA).isEqualTo(new Font("Segoe UI", Font.BOLD, 12));
    }

    @Test
    @DisplayName("Dimensões do sistema devem ser consistentes")
    void testDimensoesSistema() {
        // Verificar dimensões principais
        assertThat(PadraoLayout.DIVISOR_SPLIT_VERTICAL).isEqualTo(300);
        assertThat(PadraoLayout.PESO_SPLIT_VERTICAL).isEqualTo(0.6);
        assertThat(PadraoLayout.ALTURA_LINHA_TABELA).isEqualTo(25);
        assertThat(PadraoLayout.MARGEM_PADRAO).isEqualTo(10);
        assertThat(PadraoLayout.MARGEM_GRANDE).isEqualTo(20);
        assertThat(PadraoLayout.ESPACAMENTO_CAMPOS).isEqualTo(10);
        assertThat(PadraoLayout.ESPACAMENTO_BOTOES).isEqualTo(10);
        assertThat(PadraoLayout.ESPACAMENTO_GRUPO).isEqualTo(10);
        assertThat(PadraoLayout.ESPACAMENTO_GRID).isEqualTo(8);
        assertThat(PadraoLayout.LARGURA_CAMPO_PESQUISA).isEqualTo(200);
        assertThat(PadraoLayout.ALTURA_CAMPO).isEqualTo(30);
        assertThat(PadraoLayout.MARGEM_PAINEL).isEqualTo(20);
        assertThat(PadraoLayout.MARGEM_CONTEUDO).isEqualTo(30);
        assertThat(PadraoLayout.COLUNAS_FORMULARIO).isEqualTo(2);
        assertThat(PadraoLayout.LINHAS_TEXTO_AREA).isEqualTo(3);
    }
}
