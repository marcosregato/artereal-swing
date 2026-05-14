package com.artereal.swing.unit.util;

import com.artereal.swing.ui.layout.PadraoLayout;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes unitários para a classe PadraoLayout
 */
@DisplayName("Testes do PadraoLayout")
class PadraoLayoutTest {

    @Test
    @DisplayName("Deve criar botão salvar com cor e texto corretos")
    void testCriarBotaoSalvar() {
        // Act
        JButton botao = PadraoLayout.criarBotaoSalvar();

        // Assert
        assertThat(botao).isNotNull();
        assertThat(botao.getText()).isEqualTo("Salvar");
        assertThat(botao.getBackground()).isEqualTo(PadraoLayout.COR_BOTAO_SALVAR);
        assertThat(botao.getFont()).isEqualTo(PadraoLayout.FONTE_BOTAO);
    }

    @Test
    @DisplayName("Deve criar botão editar com cor e texto corretos")
    void testCriarBotaoEditar() {
        // Act
        JButton botao = PadraoLayout.criarBotaoEditar();

        // Assert
        assertThat(botao).isNotNull();
        assertThat(botao.getText()).isEqualTo("Editar");
        assertThat(botao.getBackground()).isEqualTo(PadraoLayout.COR_BOTAO_EDITAR);
        assertThat(botao.getFont()).isEqualTo(PadraoLayout.FONTE_BOTAO);
    }

    @Test
    @DisplayName("Deve criar botão excluir com cor e texto corretos")
    void testCriarBotaoExcluir() {
        // Act
        JButton botao = PadraoLayout.criarBotaoExcluir();

        // Assert
        assertThat(botao).isNotNull();
        assertThat(botao.getText()).isEqualTo("Excluir");
        assertThat(botao.getBackground()).isEqualTo(PadraoLayout.COR_BOTAO_EXCLUIR);
        assertThat(botao.getFont()).isEqualTo(PadraoLayout.FONTE_BOTAO);
    }

    @Test
    @DisplayName("Deve criar botão novo com cor e texto corretos")
    void testCriarBotaoNovo() {
        // Act
        JButton botao = PadraoLayout.criarBotaoNovo();

        // Assert
        assertThat(botao).isNotNull();
        assertThat(botao.getText()).isEqualTo("Novo");
        assertThat(botao.getBackground()).isEqualTo(PadraoLayout.COR_BOTAO_NOVO);
        assertThat(botao.getFont()).isEqualTo(PadraoLayout.FONTE_BOTAO);
    }

    @Test
    @DisplayName("Deve criar botão limpar com cor e texto corretos")
    void testCriarBotaoLimpar() {
        // Act
        JButton botao = PadraoLayout.criarBotaoLimpar();

        // Assert
        assertThat(botao).isNotNull();
        assertThat(botao.getText()).isEqualTo("Limpar");
        assertThat(botao.getBackground()).isEqualTo(PadraoLayout.COR_BOTAO_LIMPAR);
        assertThat(botao.getFont()).isEqualTo(PadraoLayout.FONTE_BOTAO);
    }

    @Test
    @DisplayName("Deve criar botão pesquisar com cor e texto corretos")
    void testCriarBotaoPesquisar() {
        // Act
        JButton botao = PadraoLayout.criarBotaoPesquisar();

        // Assert
        assertThat(botao).isNotNull();
        assertThat(botao.getText()).isEqualTo("Pesquisar");
        assertThat(botao.getBackground()).isEqualTo(PadraoLayout.COR_BOTAO_PESQUISAR);
        assertThat(botao.getFont()).isEqualTo(PadraoLayout.FONTE_BOTAO);
    }

    @Test
    @DisplayName("Deve criar botão personalizado com cor e texto específicos")
    void testCriarBotaoPersonalizado() {
        // Arrange
        String texto = "Botão Personalizado";
        Color cor = Color.ORANGE;

        // Act
        JButton botao = PadraoLayout.criarBotao(texto, cor);

        // Assert
        assertThat(botao).isNotNull();
        assertThat(botao.getText()).isEqualTo(texto);
        assertThat(botao.getBackground()).isEqualTo(cor);
        assertThat(botao.getFont()).isEqualTo(PadraoLayout.FONTE_BOTAO);
    }

    @Test
    @DisplayName("Deve configurar tabela com estilo padrão")
    void testConfigurarTabela() {
        // Arrange
        JTable tabela = new JTable();
        Font fonteOriginal = tabela.getFont();
        int alturaOriginal = tabela.getRowHeight();

        // Act
        PadraoLayout.configurarTabela(tabela);

        // Assert
        assertThat(tabela.getFont()).isEqualTo(PadraoLayout.FONTE_TABELA);
        assertThat(tabela.getRowHeight()).isEqualTo(PadraoLayout.ALTURA_LINHA_TABELA);
        assertThat(tabela.getFont()).isNotEqualTo(fonteOriginal);
        assertThat(tabela.getRowHeight()).isNotEqualTo(alturaOriginal);
    }

    @Test
    @DisplayName("Deve criar painel de botões de ação")
    void testCriarPainelBotoesAcao() {
        // Act
        JPanel painel = PadraoLayout.criarPainelBotoesAcao();

        // Assert
        assertThat(painel).isNotNull();
        assertThat(painel.getBackground()).isEqualTo(Color.WHITE);
        assertThat(painel.getLayout()).isInstanceOf(FlowLayout.class);
        
        // Verificar que contém os botões principais
        Component[] componentes = painel.getComponents();
        assertThat(componentes.length).isGreaterThan(0);
        
        // Verificar que os componentes são botões
        for (Component componente : componentes) {
            assertThat(componente).isInstanceOf(JButton.class);
        }
    }

    @Test
    @DisplayName("Deve criar label de formulário estilizado")
    void testCriarLabelFormulario() {
        // Arrange
        String texto = "Teste Label";

        // Act
        JLabel label = PadraoLayout.criarLabelFormulario(texto);

        // Assert
        assertThat(label).isNotNull();
        assertThat(label.getText()).isEqualTo(texto);
        assertThat(label.getFont()).isEqualTo(PadraoLayout.FONTE_GRUPO);
        assertThat(label.getForeground()).isEqualTo(PadraoLayout.COR_TEXTO_TITULO);
    }

    @Test
    @DisplayName("Deve estilizar campo de texto")
    void testEstilizarCampoTexto() {
        // Arrange
        JTextField campo = new JTextField();
        Border bordaOriginal = campo.getBorder();

        // Act
        PadraoLayout.estilizarCampoTexto(campo);

        // Assert
        assertThat(campo.getBorder()).isEqualTo(PadraoLayout.BORDA_CAMPO);
        // Verificar se o background é branco (pode ser Color.WHITE ou ColorUIResource)
        assertThat(campo.getBackground()).isEqualTo(Color.WHITE);
        assertThat(campo.getBorder()).isNotEqualTo(bordaOriginal);
        // O background pode ser o mesmo dependendo do Look & Feel
    }

    @Test
    @DisplayName("Deve estilizar combo box")
    void testEstilizarComboBox() {
        // Arrange
        JComboBox<String> combo = new JComboBox<>();
        Border bordaOriginal = combo.getBorder();
        Color fundoOriginal = combo.getBackground();

        // Act
        PadraoLayout.estilizarComboBox(combo);

        // Assert
        assertThat(combo.getBorder()).isEqualTo(PadraoLayout.BORDA_CAMPO);
        assertThat(combo.getBackground()).isEqualTo(Color.WHITE);
        assertThat(combo.getBorder()).isNotEqualTo(bordaOriginal);
        assertThat(combo.getBackground()).isNotEqualTo(fundoOriginal);
    }

    @Test
    @DisplayName("Deve criar layout de formulário")
    void testCriarLayoutFormulario() {
        // Act
        GridLayout layout = PadraoLayout.criarLayoutFormulario();

        // Assert
        assertThat(layout).isNotNull();
        assertThat(layout.getColumns()).isEqualTo(PadraoLayout.COLUNAS_FORMULARIO);
        assertThat(layout.getHgap()).isEqualTo(PadraoLayout.ESPACAMENTO_CAMPOS);
        assertThat(layout.getVgap()).isEqualTo(PadraoLayout.ESPACAMENTO_GRID);
    }

    @Test
    @DisplayName("Deve criar grupo de formulário")
    void testCriarGrupoFormulario() {
        // Arrange
        String titulo = "Grupo de Teste";

        // Act
        JPanel painel = PadraoLayout.criarGrupoFormulario(titulo);

        // Assert
        assertThat(painel).isNotNull();
        assertThat(painel.getBackground()).isEqualTo(Color.WHITE);
        assertThat(painel.getBorder()).isEqualTo(PadraoLayout.BORDA_GRUPO);
    }

    @Test
    @DisplayName("Deve criar painel de pesquisa")
    void testCriarPainelPesquisa() {
        // Arrange
        JTextField campoPesquisa = new JTextField();
        JButton botaoPesquisa = new JButton();

        // Act
        JPanel painel = PadraoLayout.criarPainelPesquisa(campoPesquisa, botaoPesquisa);

        // Assert
        assertThat(painel).isNotNull();
        assertThat(painel.getBackground()).isEqualTo(PadraoLayout.COR_PAINEL);
        assertThat(painel.getBorder()).isEqualTo(PadraoLayout.BORDA_PAINEL);
    }

    @Test
    @DisplayName("Deve criar header estilizado")
    void testCriarHeader() {
        // Arrange
        String titulo = "Título de Teste";
        String subtitulo = "Subtítulo de Teste";

        // Act
        JPanel header = PadraoLayout.criarHeader(titulo, subtitulo);

        // Assert
        assertThat(header).isNotNull();
        assertThat(header.getBackground()).isEqualTo(PadraoLayout.COR_HEADER);
        assertThat(header.getBorder()).isNotNull();
    }

    @Test
    @DisplayName("Deve criar painel de filtros")
    void testCriarPainelFiltros() {
        // Act
        JPanel painel = PadraoLayout.criarPainelFiltros();

        // Assert
        assertThat(painel).isNotNull();
        assertThat(painel.getBackground()).isEqualTo(PadraoLayout.COR_PAINEL);
        assertThat(painel.getBorder()).isNotNull();
        
        // Verificar que contém componentes de pesquisa
        Component[] componentes = painel.getComponents();
        assertThat(componentes.length).isGreaterThan(0);
    }

    @Test
    @DisplayName("Deve criar painel de conteúdo")
    void testCriarPainelConteudo() {
        // Arrange
        JPanel conteudo = new JPanel();

        // Act
        JPanel painel = PadraoLayout.criarPainelConteudo(conteudo);

        // Assert
        assertThat(painel).isNotNull();
        assertThat(painel.getBackground()).isEqualTo(PadraoLayout.COR_FUNDO);
        assertThat(painel.getLayout()).isInstanceOf(BorderLayout.class);
    }

    @Test
    @DisplayName("Deve criar painel completo")
    void testCriarPainelCompleto() {
        // Arrange
        String titulo = "Título";
        String subtitulo = "Subtítulo";
        String icone = "🔍";
        JPanel conteudo = new JPanel();

        // Act
        JPanel painel = PadraoLayout.criarPainelCompleto(titulo, subtitulo, icone, conteudo);

        // Assert
        assertThat(painel).isNotNull();
        assertThat(painel.getBackground()).isEqualTo(PadraoLayout.COR_FUNDO);
        assertThat(painel.getLayout()).isInstanceOf(BorderLayout.class);
    }

    @Test
    @DisplayName("Deve aplicar layout padrão a painel")
    void testAplicarLayoutPadrao() {
        // Arrange
        JPanel painel = new JPanel();

        // Act
        PadraoLayout.aplicarLayoutPadrao(painel);

        // Assert
        assertThat(painel.getLayout()).isInstanceOf(BorderLayout.class);
        assertThat(painel.getBackground()).isEqualTo(PadraoLayout.COR_FUNDO);
    }

    @Test
    @DisplayName("Deve criar botão com efeito hover")
    void testCriarBotaoComHover() {
        // Arrange
        String texto = "Botão Hover";
        Color cor = Color.BLUE;

        // Act
        JButton botao = PadraoLayout.criarBotaoComHover(texto, cor);

        // Assert
        assertThat(botao).isNotNull();
        assertThat(botao.getText()).isEqualTo(texto);
        assertThat(botao.getBackground()).isEqualTo(cor);
        assertThat(botao.getFont()).isEqualTo(PadraoLayout.FONTE_BOTAO);
    }

    @Test
    @DisplayName("Deve ter constantes de cores definidas")
    void testConstantesCores() {
        // Assert
        assertThat(PadraoLayout.COR_PRIMARIA).isNotNull();
        assertThat(PadraoLayout.COR_HEADER).isNotNull();
        assertThat(PadraoLayout.COR_FUNDO).isNotNull();
        assertThat(PadraoLayout.COR_BOTAO_SALVAR).isNotNull();
        assertThat(PadraoLayout.COR_BOTAO_EDITAR).isNotNull();
        assertThat(PadraoLayout.COR_BOTAO_EXCLUIR).isNotNull();
    }

    @Test
    @DisplayName("Deve ter constantes de fontes definidas")
    void testConstantesFontes() {
        // Assert
        assertThat(PadraoLayout.FONTE_TITULO).isNotNull();
        assertThat(PadraoLayout.FONTE_SUBTITULO).isNotNull();
        assertThat(PadraoLayout.FONTE_BOTAO).isNotNull();
        assertThat(PadraoLayout.FONTE_TABELA).isNotNull();
    }

    @Test
    @DisplayName("Deve ter constantes de bordas definidas")
    void testConstantesBordas() {
        // Assert
        assertThat(PadraoLayout.BORDA_PADRAO).isNotNull();
        assertThat(PadraoLayout.BORDA_GRUPO).isNotNull();
        assertThat(PadraoLayout.BORDA_CAMPO).isNotNull();
        assertThat(PadraoLayout.BORDA_PAINEL).isNotNull();
    }

    @Test
    @DisplayName("Deve ter constantes de dimensões definidas")
    void testConstantesDimensoes() {
        // Assert
        assertThat(PadraoLayout.ALTURA_LINHA_TABELA).isGreaterThan(0);
        assertThat(PadraoLayout.MARGEM_PADRAO).isGreaterThanOrEqualTo(0);
        assertThat(PadraoLayout.ESPACAMENTO_CAMPOS).isGreaterThanOrEqualTo(0);
        assertThat(PadraoLayout.LARGURA_CAMPO_PESQUISA).isGreaterThan(0);
    }
}
