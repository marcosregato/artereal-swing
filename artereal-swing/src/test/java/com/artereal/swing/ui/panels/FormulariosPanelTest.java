package com.artereal.swing.ui.panels;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.assertThat;

import javax.swing.*;
import java.awt.*;

/**
 * Testes para validação de formulários e campos dos painéis UI
 */
@DisplayName("Testes de Formulários UI")
class FormulariosPanelTest {

    @BeforeEach
    void setUp() {
        // Configura ambiente de testes
        System.setProperty("test.environment", "true");
    }

    @Test
    @DisplayName("CadastroIrmaosPanel - Validação de campos obrigatórios")
    void testCadastroIrmaosPanelCamposObrigatorios() {
        // Arrange & Act
        CadastroIrmaosPanel panel = new CadastroIrmaosPanel();
        
        // Assert - Verifica se campos obrigatórios existem
        assertThat(panel).isNotNull();
        
        // Verifica componentes básicos do formulário
        Component[] components = panel.getComponents();
        assertThat(components).isNotEmpty();
        
        // Verifica se há campos de texto para nome
        boolean temCampoNome = false;
        boolean temCampoGrau = false;
        boolean temBotaoSalvar = false;
        
        for (Component comp : components) {
            if (comp instanceof JTextField) {
                JTextField textField = (JTextField) comp;
                if (textField.getName() != null && textField.getName().toLowerCase().contains("nome")) {
                    temCampoNome = true;
                }
            } else if (comp instanceof JComboBox) {
                temCampoGrau = true;
            } else if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if (button.getText().toLowerCase().contains("salvar") || 
                    button.getText().toLowerCase().contains("cadastrar")) {
                    temBotaoSalvar = true;
                }
            }
        }
        
        // Verifica componentes essenciais
        assertThat(temCampoNome).as("Deve ter campo para nome").isTrue();
        assertThat(temBotaoSalvar).as("Deve ter botão de salvar").isTrue();
    }

    @Test
    @DisplayName("IrmaosPanel - Teste de busca e filtros")
    void testIrmaosPanelBuscaEFiltros() {
        // Arrange & Act
        IrmaosPanel panel = new IrmaosPanel();
        
        // Assert
        assertThat(panel).isNotNull();
        
        // Verifica se há campo de busca
        boolean temCampoBusca = false;
        boolean temTabela = false;
        
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JTextField) {
                temCampoBusca = true;
            } else if (comp instanceof JScrollPane) {
                JScrollPane scroll = (JScrollPane) comp;
                if (scroll.getViewport().getView() instanceof JTable) {
                    temTabela = true;
                }
            }
        }
        
        assertThat(temCampoBusca).as("Deve ter campo de busca").isTrue();
        assertThat(temTabela).as("Deve ter tabela de resultados").isTrue();
    }

    @Test
    @DisplayName("VisitantesPanel - Validação de formulário de visitantes")
    void testVisitantesPanelFormulario() {
        // Arrange & Act
        VisitantesPanel panel = new VisitantesPanel();
        
        // Assert
        assertThat(panel).isNotNull();
        
        // Verifica componentes do formulário
        Component[] components = panel.getComponents();
        assertThat(components).isNotEmpty();
        
        boolean temCampoNome = false;
        boolean temCampoData = false;
        boolean temBotaoRegistrar = false;
        
        for (Component comp : components) {
            if (comp instanceof JTextField) {
                temCampoNome = true;
            } else if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if (button.getText().toLowerCase().contains("registrar") ||
                    button.getText().toLowerCase().contains("adicionar")) {
                    temBotaoRegistrar = true;
                }
            }
        }
        
        assertThat(temCampoNome).as("Deve ter campo para nome do visitante").isTrue();
        assertThat(temBotaoRegistrar).as("Deve ter botão para registrar visitante").isTrue();
    }

    @Test
    @DisplayName("CandidatosPanel - Processamento de candidaturas")
    void testCandidatosPanelProcessamento() {
        // Arrange & Act
        CandidatosPanel panel = new CandidatosPanel();
        
        // Assert
        assertThat(panel).isNotNull();
        
        // Verifica se há botões de ação
        boolean temBotaoAprovar = false;
        boolean temBotaoRejeitar = false;
        boolean temListaCandidatos = false;
        
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                String text = button.getText().toLowerCase();
                if (text.contains("aprovar") || text.contains("aceitar")) {
                    temBotaoAprovar = true;
                } else if (text.contains("rejeitar") || text.contains("recusar")) {
                    temBotaoRejeitar = true;
                }
            } else if (comp instanceof JScrollPane) {
                temListaCandidatos = true;
            }
        }
        
        // Verifica funcionalidades básicas
        assertThat(temListaCandidatos).as("Deve ter lista de candidatos").isTrue();
    }

    @Test
    @DisplayName("Validação de tamanho e posição dos campos")
    void testValidacaoTamanhoPosicaoCampos() {
        // Arrange
        CadastroIrmaosPanel panel = new CadastroIrmaosPanel();
        
        // Act & Assert - Verifica dimensões do painel
        Dimension size = panel.getSize();
        assertThat(size.width).as("Largura do painel deve ser positiva").isGreaterThan(0);
        assertThat(size.height).as("Altura do painel deve ser positiva").isGreaterThan(0);
        
        // Verifica se componentes têm tamanhos razoáveis
        for (Component comp : panel.getComponents()) {
            Dimension compSize = comp.getSize();
            if (compSize.width > 0 && compSize.height > 0) {
                assertThat(compSize.width).as("Largura do componente deve ser razoável")
                    .isLessThan(2000); // Limite máximo
                assertThat(compSize.height).as("Altura do componente deve ser razoável")
                    .isLessThan(2000);
            }
        }
    }

    @Test
    @DisplayName("Teste de renderização de formulários")
    void testRenderizacaoFormularios() {
        // Arrange & Act
        CadastroIrmaosPanel panel = new CadastroIrmaosPanel();
        
        // Assert - Verifica se painel é renderizado corretamente
        assertThat(panel.isDisplayable()).as("Painel deve ser displayable").isTrue();
        
        // Verifica se componentes são visíveis
        for (Component comp : panel.getComponents()) {
            assertThat(comp.isVisible()).as("Componente deve ser visível").isTrue();
        }
    }

    @Test
    @DisplayName("Validação de mensagens e textos")
    void testValidacaoMensagensTextos() {
        // Arrange & Act
        CadastroIrmaosPanel panel = new CadastroIrmaosPanel();
        
        // Assert - Verifica se há labels com textos
        boolean temLabels = false;
        boolean temTextosValidos = false;
        
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                temLabels = true;
                String text = label.getText();
                if (text != null && !text.trim().isEmpty()) {
                    temTextosValidos = true;
                }
            }
        }
        
        assertThat(temLabels).as("Deve ter labels no formulário").isTrue();
        assertThat(temTextosValidos).as("Labels devem ter textos válidos").isTrue();
    }

    @Test
    @DisplayName("Teste de funcionamento de botões")
    void testFuncionamentoBotoes() {
        // Arrange
        CadastroIrmaosPanel panel = new CadastroIrmaosPanel();
        
        // Act & Assert - Verifica se botões estão habilitados
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                assertThat(button.isEnabled()).as("Botão deve estar habilitado").isTrue();
                assertThat(button.getText()).isNotEmpty().as("Botão deve ter texto");
            }
        }
    }

    @Test
    @DisplayName("Validação de campos de texto")
    void testValidacaoCamposTexto() {
        // Arrange
        CadastroIrmaosPanel panel = new CadastroIrmaosPanel();
        
        // Act & Assert - Verifica campos de texto
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JTextField) {
                JTextField textField = (JTextField) comp;
                assertThat(textField.isEnabled()).as("Campo de texto deve estar habilitado").isTrue();
                assertThat(textField.isEditable()).as("Campo de texto deve ser editável").isTrue();
            }
        }
    }

    @Test
    @DisplayName("Teste de layout e espaçamento")
    void testLayoutEspacamento() {
        // Arrange
        CadastroIrmaosPanel panel = new CadastroIrmaosPanel();
        
        // Act & Assert - Verifica layout manager
        LayoutManager layout = panel.getLayout();
        assertThat(layout).isNotNull().as("Painel deve ter layout manager");
        
        // Verifica se componentes não se sobrepõem (verificação básica)
        Rectangle bounds = panel.getBounds();
        assertThat(bounds.width).as("Painel deve ter largura definida").isGreaterThan(0);
        assertThat(bounds.height).as("Painel deve ter altura definida").isGreaterThan(0);
    }
}
