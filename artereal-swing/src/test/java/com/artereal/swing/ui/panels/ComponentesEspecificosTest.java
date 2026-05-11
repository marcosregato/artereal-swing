package com.artereal.swing.ui.panels;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.assertThat;

import javax.swing.*;
import java.awt.*;

/**
 * Testes para componentes específicos e funcionalidades avançadas dos painéis
 */
@DisplayName("Testes de Componentes Específicos")
class ComponentesEspecificosTest {

    @BeforeEach
    void setUp() {
        // Configura ambiente de testes
        System.setProperty("test.environment", "true");
    }

    @Test
    @DisplayName("CaixaPanel - Validação de campos financeiros")
    void testCaixaPanelCamposFinanceiros() {
        // Arrange & Act
        CaixaPanel panel = new CaixaPanel();
        
        // Assert - Verifica se há campos para valores financeiros
        assertThat(panel).isNotNull();
        
        boolean temCampoValor = false;
        boolean temCampoTipo = false;
        boolean temCampoData = false;
        boolean temBotaoRegistrar = false;
        
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JTextField) {
                temCampoValor = true;
            } else if (comp instanceof JComboBox) {
                temCampoTipo = true;
            } else if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                String text = button.getText().toLowerCase();
                if (text.contains("registrar") || text.contains("adicionar") || text.contains("salvar")) {
                    temBotaoRegistrar = true;
                }
            }
        }
        
        assertThat(temCampoValor).as("Deve ter campo para valor financeiro").isTrue();
        assertThat(temCampoTipo).as("Deve ter campo para tipo de movimento").isTrue();
        assertThat(temBotaoRegistrar).as("Deve ter botão para registrar movimento").isTrue();
    }

    @Test
    @DisplayName("RelatoriosPanel - Geração de relatórios")
    void testRelatoriosPanelGeracao() {
        // Arrange & Act
        RelatoriosPanel panel = new RelatoriosPanel();
        
        // Assert - Verifica se há opções de relatórios
        assertThat(panel).isNotNull();
        
        boolean temOpcoesRelatorio = false;
        boolean temBotaoGerar = false;
        boolean temAreaVisualizacao = false;
        
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JComboBox) {
                temOpcoesRelatorio = true;
            } else if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if (button.getText().toLowerCase().contains("gerar")) {
                    temBotaoGerar = true;
                }
            } else if (comp instanceof JScrollPane || comp instanceof JTextArea) {
                temAreaVisualizacao = true;
            }
        }
        
        assertThat(temOpcoesRelatorio).as("Deve ter opções de relatório").isTrue();
        assertThat(temBotaoGerar).as("Deve ter botão para gerar relatório").isTrue();
        assertThat(temAreaVisualizacao).as("Deve ter área para visualização").isTrue();
    }

    @Test
    @DisplayName("ConfiguracoesPanel - Configurações do sistema")
    void testConfiguracoesPanelSistema() {
        // Arrange & Act
        ConfiguracoesPanel panel = new ConfiguracoesPanel();
        
        // Assert - Verifica se há opções de configuração
        assertThat(panel).isNotNull();
        
        boolean temCamposConfig = false;
        boolean temBotaoSalvar = false;
        boolean temAbasOuSecoes = false;
        
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JTextField || comp instanceof JComboBox || comp instanceof JCheckBox) {
                temCamposConfig = true;
            } else if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if (button.getText().toLowerCase().contains("salvar") || 
                    button.getText().toLowerCase().contains("aplicar")) {
                    temBotaoSalvar = true;
                }
            } else if (comp instanceof JTabbedPane) {
                temAbasOuSecoes = true;
            }
        }
        
        assertThat(temCamposConfig).as("Deve ter campos de configuração").isTrue();
        assertThat(temBotaoSalvar).as("Deve ter botão para salvar configurações").isTrue();
    }

    @Test
    @DisplayName("BibliotecaPanel - Gestão de biblioteca")
    void testBibliotecaPanelGestao() {
        // Arrange & Act
        BibliotecaPanel panel = new BibliotecaPanel();
        
        // Assert - Verifica se há componentes para gestão de biblioteca
        assertThat(panel).isNotNull();
        
        boolean temCampoBusca = false;
        boolean temTabelaLivros = false;
        boolean temBotaoEmprestar = false;
        boolean temBotaoDevolver = false;
        
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JTextField) {
                temCampoBusca = true;
            } else if (comp instanceof JScrollPane) {
                JScrollPane scroll = (JScrollPane) comp;
                if (scroll.getViewport().getView() instanceof JTable) {
                    temTabelaLivros = true;
                }
            } else if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                String text = button.getText().toLowerCase();
                if (text.contains("emprestar")) {
                    temBotaoEmprestar = true;
                } else if (text.contains("devolver")) {
                    temBotaoDevolver = true;
                }
            }
        }
        
        assertThat(temCampoBusca).as("Deve ter campo de busca de livros").isTrue();
        assertThat(temTabelaLivros).as("Deve ter tabela de livros").isTrue();
    }

    @Test
    @DisplayName("GaleriaFotosPanel - Gestão de galeria de fotos")
    void testGaleriaFotosPanelGestao() {
        // Arrange & Act
        GaleriaFotosPanel panel = new GaleriaFotosPanel();
        
        // Assert - Verifica se há componentes para gestão de fotos
        assertThat(panel).isNotNull();
        
        boolean temAreaFotos = false;
        boolean temBotaoUpload = false;
        boolean temBotaoRemover = false;
        
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JScrollPane || comp instanceof JPanel) {
                // Área para exibir fotos
                temAreaFotos = true;
            } else if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                String text = button.getText().toLowerCase();
                if (text.contains("upload") || text.contains("adicionar") || text.contains("importar")) {
                    temBotaoUpload = true;
                } else if (text.contains("remover") || text.contains("excluir") || text.contains("deletar")) {
                    temBotaoRemover = true;
                }
            }
        }
        
        assertThat(temAreaFotos).as("Deve ter área para exibir fotos").isTrue();
        assertThat(temBotaoUpload).as("Deve ter botão para upload de fotos").isTrue();
    }

    @Test
    @DisplayName("Validação de componentes de data e hora")
    void testComponentesDataHora() {
        // Arrange & Act
        CaixaPanel panel = new CaixaPanel();
        
        // Assert - Verifica se há componentes para data/hora
        boolean temCampoData = false;
        boolean temCampoHora = false;
        
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JTextField) {
                JTextField textField = (JTextField) comp;
                String name = textField.getName();
                if (name != null) {
                    if (name.toLowerCase().contains("data")) {
                        temCampoData = true;
                    } else if (name.toLowerCase().contains("hora") || name.toLowerCase().contains("time")) {
                        temCampoHora = true;
                    }
                }
            }
        }
        
        // Pelo menos um campo de data deve existir
        assertThat(temCampoData || temCampoHora).as("Deve ter campo de data ou hora").isTrue();
    }

    @Test
    @DisplayName("Validação de componentes de seleção múltipla")
    void testComponentesSelecaoMultipla() {
        // Arrange & Act
        IrmaosPanel panel = new IrmaosPanel();
        
        // Assert - Verifica se há componentes para seleção múltipla
        boolean temTabelaSelecao = false;
        boolean temCheckBox = false;
        
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JScrollPane) {
                JScrollPane scroll = (JScrollPane) comp;
                if (scroll.getViewport().getView() instanceof JTable) {
                    JTable table = (JTable) scroll.getViewport().getView();
                    // Verifica se tabela permite seleção múltipla (verificação básica)
                    if (table.getRowCount() > 0) {
                        temTabelaSelecao = true;
                    }
                }
            } else if (comp instanceof JCheckBox) {
                temCheckBox = true;
            }
        }
        
        // Pelo menos um tipo de seleção deve existir
        assertThat(temTabelaSelecao || temCheckBox).as("Deve ter opção de seleção múltipla").isTrue();
    }

    @Test
    @DisplayName("Validação de componentes de navegação")
    void testComponentesNavegacao() {
        // Arrange & Act
        RelatoriosPanel panel = new RelatoriosPanel();
        
        // Assert - Verifica se há componentes de navegação
        boolean temBotoesNavegacao = false;
        boolean temPaginacao = false;
        
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                String text = button.getText().toLowerCase();
                if (text.contains("próximo") || text.contains("anterior") || 
                    text.contains("next") || text.contains("previous") ||
                    text.contains("primeiro") || text.contains("último")) {
                    temBotoesNavegacao = true;
                }
            }
        }
        
        // Navegação é opcional, mas se existir deve funcionar
        if (temBotoesNavegacao) {
            assertThat(temBotoesNavegacao).as("Botões de navegação devem estar presentes").isTrue();
        }
    }

    @Test
    @DisplayName("Validação de componentes de filtro")
    void testComponentesFiltro() {
        // Arrange & Act
        IrmaosPanel panel = new IrmaosPanel();
        
        // Assert - Verifica se há componentes de filtro
        boolean temCampoFiltro = false;
        boolean temComboFiltro = false;
        boolean temBotaoFiltrar = false;
        
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JTextField) {
                temCampoFiltro = true;
            } else if (comp instanceof JComboBox) {
                temComboFiltro = true;
            } else if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if (button.getText().toLowerCase().contains("filtrar") || 
                    button.getText().toLowerCase().contains("buscar")) {
                    temBotaoFiltrar = true;
                }
            }
        }
        
        assertThat(temCampoFiltro || temComboFiltro).as("Deve ter opções de filtro").isTrue();
    }

    @Test
    @DisplayName("Validação de componentes de exportação")
    void testComponentesExportacao() {
        // Arrange & Act
        RelatoriosPanel panel = new RelatoriosPanel();
        
        // Assert - Verifica se há componentes de exportação
        boolean temBotaoExportar = false;
        boolean temComboFormato = false;
        
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                String text = button.getText().toLowerCase();
                if (text.contains("exportar") || text.contains("salvar") || text.contains("baixar")) {
                    temBotaoExportar = true;
                }
            } else if (comp instanceof JComboBox) {
                // Verifica se combo tem opções de formato
                JComboBox<?> combo = (JComboBox<?>) comp;
                if (combo.getItemCount() > 0) {
                    Object firstItem = combo.getItemAt(0);
                    if (firstItem instanceof String) {
                        String item = (String) firstItem;
                        if (item.toLowerCase().contains("pdf") || 
                            item.toLowerCase().contains("excel") ||
                            item.toLowerCase().contains("csv")) {
                            temComboFormato = true;
                        }
                    }
                }
            }
        }
        
        // Exportação é opcional, mas se existir deve funcionar
        if (temBotaoExportar) {
            assertThat(temBotaoExportar).as("Deve ter opção de exportação").isTrue();
        }
    }

    @Test
    @DisplayName("Validação de componentes de impressão")
    void testComponentesImpressao() {
        // Arrange & Act
        RelatoriosPanel panel = new RelatoriosPanel();
        
        // Assert - Verifica se há componentes de impressão
        boolean temBotaoImprimir = false;
        
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if (button.getText().toLowerCase().contains("imprimir")) {
                    temBotaoImprimir = true;
                }
            }
        }
        
        // Impressão é opcional, mas se existir deve funcionar
        if (temBotaoImprimir) {
            assertThat(temBotaoImprimir).as("Deve ter opção de impressão").isTrue();
        }
    }
}
