package com.artereal.swing.unit.layout;

import com.artereal.swing.ui.layout.PadraoLayout;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import javax.swing.*;
import java.awt.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes simplificados de conformidade visual para garantir que o PadraoLayout funciona corretamente
 */
@DisplayName("Testes de Conformidade do PadraoLayout (Simplificado)")
class PadraoLayoutConformidadeSimplificado {

    @Test
    @DisplayName("PadraoLayout deve ter cores constantes bem definidas")
    void testCoresConstantes() {
        // Assert - Verificar se as cores constantes estão definidas
        assertThat(PadraoLayout.COR_PRIMARIA).isNotNull();
        assertThat(PadraoLayout.COR_HEADER).isNotNull();
        assertThat(PadraoLayout.COR_FUNDO).isNotNull();
        assertThat(PadraoLayout.COR_PAINEL).isNotNull();
        assertThat(PadraoLayout.COR_BORDA).isNotNull();
        assertThat(PadraoLayout.COR_TEXTO_TITULO).isNotNull();
        assertThat(PadraoLayout.COR_TEXTO_BRANCO).isNotNull();
        
        // Verificar se as cores seguem o padrão esperado
        assertThat(PadraoLayout.COR_PRIMARIA.getRed()).isEqualTo(70);
        assertThat(PadraoLayout.COR_PRIMARIA.getGreen()).isEqualTo(130);
        assertThat(PadraoLayout.COR_PRIMARIA.getBlue()).isEqualTo(180);
        
        assertThat(PadraoLayout.COR_HEADER.getRed()).isEqualTo(25);
        assertThat(PadraoLayout.COR_HEADER.getGreen()).isEqualTo(25);
        assertThat(PadraoLayout.COR_HEADER.getBlue()).isEqualTo(112);
    }

    @Test
    @DisplayName("Botões criados devem seguir o padrão de cores")
    void testCoresBotoes() {
        // Act
        JButton botaoSalvar = PadraoLayout.criarBotaoSalvar();
        JButton botaoEditar = PadraoLayout.criarBotaoEditar();
        JButton botaoExcluir = PadraoLayout.criarBotaoExcluir();
        JButton botaoNovo = PadraoLayout.criarBotaoNovo();
        JButton botaoLimpar = PadraoLayout.criarBotaoLimpar();
        JButton botaoPesquisar = PadraoLayout.criarBotaoPesquisar();

        // Assert - Verificar cores dos botões
        assertThat(botaoSalvar.getBackground()).isEqualTo(PadraoLayout.COR_BOTAO_SALVAR);
        assertThat(botaoSalvar.getForeground()).isEqualTo(Color.BLACK);
        assertThat(botaoSalvar.getText()).isEqualTo("Salvar");
        
        assertThat(botaoEditar.getBackground()).isEqualTo(PadraoLayout.COR_BOTAO_EDITAR);
        assertThat(botaoEditar.getForeground()).isEqualTo(Color.BLACK);
        assertThat(botaoEditar.getText()).isEqualTo("Editar");
        
        assertThat(botaoExcluir.getBackground()).isEqualTo(PadraoLayout.COR_BOTAO_EXCLUIR);
        assertThat(botaoExcluir.getForeground()).isEqualTo(Color.BLACK);
        assertThat(botaoExcluir.getText()).isEqualTo("Excluir");
        
        assertThat(botaoNovo.getBackground()).isEqualTo(PadraoLayout.COR_BOTAO_NOVO);
        assertThat(botaoNovo.getForeground()).isEqualTo(Color.BLACK);
        assertThat(botaoNovo.getText()).isEqualTo("Novo");
        
        assertThat(botaoLimpar.getBackground()).isEqualTo(PadraoLayout.COR_BOTAO_LIMPAR);
        assertThat(botaoLimpar.getForeground()).isEqualTo(Color.BLACK);
        assertThat(botaoLimpar.getText()).isEqualTo("Limpar");
        
        assertThat(botaoPesquisar.getBackground()).isEqualTo(PadraoLayout.COR_BOTAO_PESQUISAR);
        assertThat(botaoPesquisar.getForeground()).isEqualTo(Color.BLACK);
        assertThat(botaoPesquisar.getText()).isEqualTo("Pesquisar");
    }

    @Test
    @DisplayName("Campos de texto devem ter estilização padrão")
    void testEstilizacaoCamposTexto() {
        // Arrange
        JTextField campo = new JTextField();
        JPasswordField senha = new JPasswordField();

        // Act
        PadraoLayout.estilizarCampoTexto(campo);
        PadraoLayout.estilizarCampoTexto(senha);

        // Assert - Verificar estilização
        assertThat(campo.getBorder()).isEqualTo(PadraoLayout.BORDA_CAMPO);
        assertThat(campo.getBackground()).isEqualTo(Color.WHITE);
        
        assertThat(senha.getBorder()).isEqualTo(PadraoLayout.BORDA_CAMPO);
        assertThat(senha.getBackground()).isEqualTo(Color.WHITE);
    }

    @Test
    @DisplayName("Labels devem seguir o padrão de fonte e cor")
    void testEstilizacaoLabels() {
        // Act
        JLabel labelFormulario = PadraoLayout.criarLabelFormulario("Campo Teste");

        // Assert - Verificar estilização
        assertThat(labelFormulario.getText()).isEqualTo("Campo Teste");
        assertThat(labelFormulario.getForeground()).isEqualTo(PadraoLayout.COR_TEXTO_TITULO);
        assertThat(labelFormulario.getFont().getName()).isEqualTo("Segoe UI");
    }

    @Test
    @DisplayName("Painéis devem seguir o padrão de cores e bordas")
    void testEstilizacaoPaineis() {
        // Act
        JPanel painelHeader = PadraoLayout.criarHeader("Título", "Subtítulo");
        JPanel painelPesquisa = PadraoLayout.criarPainelPesquisa(new JTextField(), PadraoLayout.criarBotaoPesquisar());
        JPanel painelGrupo = PadraoLayout.criarGrupoFormulario("Formulário Teste");

        // Assert - Verificar estilização
        assertThat(painelHeader.getBackground()).isEqualTo(PadraoLayout.COR_HEADER);
        assertThat(painelHeader.getBorder()).isNotNull();
        
        assertThat(painelPesquisa.getBackground()).isEqualTo(PadraoLayout.COR_PAINEL);
        assertThat(painelPesquisa.getBorder()).isEqualTo(PadraoLayout.BORDA_PAINEL);
        
        assertThat(painelGrupo.getBackground()).isEqualTo(Color.WHITE);
        assertThat(painelGrupo.getBorder()).isEqualTo(PadraoLayout.BORDA_GRUPO);
    }

    @Test
    @DisplayName("Tabelas devem ter estilização padrão")
    void testEstilizacaoTabelas() {
        // Arrange
        JTable tabela = new JTable(); // Simplificado - sem dados desnecessários

        // Act
        PadraoLayout.configurarTabela(tabela);

        // Assert - Verificar estilização
        // A cor do header pode variar dependendo do Look & Feel, então verificamos se é uma cor razoável
        Color headerBackground = tabela.getTableHeader().getBackground();
        assertThat(headerBackground).isNotNull();
        // Aceitamos tanto COR_HEADER quanto COR_PRIMARIA como cores válidas para o header
        assertThat(headerBackground).isIn(PadraoLayout.COR_HEADER, PadraoLayout.COR_PRIMARIA);
        
        assertThat(tabela.getTableHeader().getForeground()).isEqualTo(PadraoLayout.COR_TEXTO_BRANCO);
        assertThat(tabela.getSelectionBackground()).isEqualTo(PadraoLayout.COR_SELECAO_TABELA);
        assertThat(tabela.getSelectionForeground()).isEqualTo(PadraoLayout.COR_SELECAO_TEXTO);
        assertThat(tabela.getRowHeight()).isEqualTo(PadraoLayout.ALTURA_LINHA_TABELA);
    }

    @Test
    @DisplayName("ComboBox deve ter estilização padrão")
    void testEstilizacaoComboBox() {
        // Arrange
        JComboBox<String> combo = new JComboBox<>(new String[]{"Opção 1", "Opção 2"});

        // Act
        PadraoLayout.estilizarComboBox(combo);

        // Assert - Verificar estilização
        assertThat(combo.getBorder()).isEqualTo(PadraoLayout.BORDA_CAMPO);
        assertThat(combo.getBackground()).isEqualTo(Color.WHITE);
    }

    @Test
    @DisplayName("Layout de formulário deve ter configurações corretas")
    void testLayoutFormulario() {
        // Act
        GridLayout layout = PadraoLayout.criarLayoutFormulario();

        // Assert - Verificar configurações do layout
        assertThat(layout.getColumns()).isEqualTo(PadraoLayout.COLUNAS_FORMULARIO);
        assertThat(layout.getHgap()).isEqualTo(PadraoLayout.ESPACAMENTO_CAMPOS);
        assertThat(layout.getVgap()).isEqualTo(PadraoLayout.ESPACAMENTO_GRID);
    }

    @Test
    @DisplayName("Fontes padrão devem estar definidas")
    void testFontesPadrao() {
        // Assert - Verificar se as fontes constantes estão definidas
        assertThat(PadraoLayout.FONTE_TITULO).isNotNull();
        assertThat(PadraoLayout.FONTE_SUBTITULO).isNotNull();
        assertThat(PadraoLayout.FONTE_GRUPO).isNotNull();
        assertThat(PadraoLayout.FONTE_PESQUISA).isNotNull();
        assertThat(PadraoLayout.FONTE_BOTAO).isNotNull();
        assertThat(PadraoLayout.FONTE_TABELA).isNotNull();
        assertThat(PadraoLayout.FONTE_HEADER_TABELA).isNotNull();
        
        // Verificar se as fontes têm os tamanhos corretos
        assertThat(PadraoLayout.FONTE_TITULO.getSize()).isEqualTo(18);
        assertThat(PadraoLayout.FONTE_SUBTITULO.getSize()).isEqualTo(12);
        assertThat(PadraoLayout.FONTE_GRUPO.getSize()).isEqualTo(12);
        assertThat(PadraoLayout.FONTE_BOTAO.getSize()).isEqualTo(12);
        assertThat(PadraoLayout.FONTE_TABELA.getSize()).isEqualTo(12);
        assertThat(PadraoLayout.FONTE_HEADER_TABELA.getSize()).isEqualTo(12);
        
        // Verificar se as fontes têm os estilos corretos
        assertThat(PadraoLayout.FONTE_TITULO.isBold()).isTrue();
        assertThat(PadraoLayout.FONTE_SUBTITULO.isBold()).isFalse();
        assertThat(PadraoLayout.FONTE_GRUPO.isBold()).isTrue();
        assertThat(PadraoLayout.FONTE_PESQUISA.isBold()).isTrue();
        assertThat(PadraoLayout.FONTE_BOTAO.isBold()).isTrue();
        assertThat(PadraoLayout.FONTE_TABELA.isBold()).isFalse();
        assertThat(PadraoLayout.FONTE_HEADER_TABELA.isBold()).isTrue();
    }

    @Test
    @DisplayName("Bordas padrão devem estar definidas")
    void testBordasPadrao() {
        // Assert - Verificar se as bordas constantes estão definidas
        assertThat(PadraoLayout.BORDA_PADRAO).isNotNull();
        assertThat(PadraoLayout.BORDA_GRUPO).isNotNull();
        assertThat(PadraoLayout.BORDA_CAMPO).isNotNull();
        assertThat(PadraoLayout.BORDA_PAINEL).isNotNull();
        assertThat(PadraoLayout.BORDA_CONTEUDO).isNotNull();
        
        // Verificar se as bordas são do tipo esperado
        assertThat(PadraoLayout.BORDA_CAMPO).isInstanceOf(javax.swing.border.Border.class);
        assertThat(PadraoLayout.BORDA_PAINEL).isInstanceOf(javax.swing.border.Border.class);
        assertThat(PadraoLayout.BORDA_GRUPO).isInstanceOf(javax.swing.border.Border.class);
    }

    @Test
    @DisplayName("Dimensões padrão devem estar definidas")
    void testDimensoesPadrao() {
        // Assert - Verificar se as dimensões constantes estão definidas
        assertThat(PadraoLayout.DIVISOR_SPLIT_VERTICAL).isGreaterThan(0);
        assertThat(PadraoLayout.PESO_SPLIT_VERTICAL).isGreaterThan(0);
        assertThat(PadraoLayout.ALTURA_LINHA_TABELA).isGreaterThan(0);
        assertThat(PadraoLayout.MARGEM_PADRAO).isGreaterThanOrEqualTo(0);
        assertThat(PadraoLayout.MARGEM_GRANDE).isGreaterThanOrEqualTo(0);
        assertThat(PadraoLayout.ESPACAMENTO_CAMPOS).isGreaterThanOrEqualTo(0);
        assertThat(PadraoLayout.ESPACAMENTO_BOTOES).isGreaterThanOrEqualTo(0);
        assertThat(PadraoLayout.ESPACAMENTO_GRUPO).isGreaterThanOrEqualTo(0);
        assertThat(PadraoLayout.ESPACAMENTO_GRID).isGreaterThanOrEqualTo(0);
        assertThat(PadraoLayout.LARGURA_CAMPO_PESQUISA).isGreaterThan(0);
        assertThat(PadraoLayout.ALTURA_CAMPO).isGreaterThan(0);
        assertThat(PadraoLayout.MARGEM_PAINEL).isGreaterThanOrEqualTo(0);
        assertThat(PadraoLayout.MARGEM_CONTEUDO).isGreaterThanOrEqualTo(0);
        
        // Verificar valores específicos
        assertThat(PadraoLayout.COLUNAS_FORMULARIO).isEqualTo(2);
        assertThat(PadraoLayout.LINHAS_TEXTO_AREA).isEqualTo(3);
    }
}
