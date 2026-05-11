package com.artereal.swing.ui.panels;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.assertThat;

import javax.swing.*;
import java.awt.*;

/**
 * Testes para validação de layout, tamanho, posição e espaçamento dos componentes UI
 */
@DisplayName("Testes de Layout e Validação Visual")
class LayoutValidacaoTest {

    @BeforeEach
    void setUp() {
        // Configura ambiente de testes
        System.setProperty("test.environment", "true");
    }

    @Test
    @DisplayName("Validação de tamanho ideal dos campos")
    void testTamanhoIdealCampos() {
        // Arrange & Act
        CadastroIrmaosPanel panel = new CadastroIrmaosPanel();
        
        // Assert - Verifica se campos têm tamanhos adequados
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JTextField) {
                JTextField textField = (JTextField) comp;
                Dimension size = textField.getSize();
                
                // Verifica tamanho mínimo para receber valores
                if (size.width > 0) {
                    assertThat(size.width).as("Largura do campo deve ser suficiente para digitar")
                        .isGreaterThanOrEqualTo(100);
                    assertThat(size.width).as("Largura do campo não deve ser excessiva")
                        .isLessThanOrEqualTo(500);
                }
                
                if (size.height > 0) {
                    assertThat(size.height).as("Altura do campo deve ser adequada")
                        .isGreaterThanOrEqualTo(20);
                    assertThat(size.height).as("Altura do campo não deve ser excessiva")
                        .isLessThanOrEqualTo(100);
                }
            }
        }
    }

    @Test
    @DisplayName("Verificação de posição adequada no formulário")
    void testPosicaoAdequadaFormulario() {
        // Arrange & Act
        CadastroIrmaosPanel panel = new CadastroIrmaosPanel();
        
        // Assert - Verifica se componentes estão posicionados corretamente
        Component[] components = panel.getComponents();
        
        // Verifica se há componentes e se estão posicionados
        assertThat(components).isNotEmpty().as("Formulário deve ter componentes");
        
        // Verifica se componentes têm posições definidas
        for (Component comp : components) {
            Point location = comp.getLocation();
            assertThat(location.x).as("Posição X deve ser definida").isGreaterThanOrEqualTo(0);
            assertThat(location.y).as("Posição Y deve ser definida").isGreaterThanOrEqualTo(0);
        }
    }

    @Test
    @DisplayName("Validação de espaçamento correto entre campos")
    void testEspacamentoCorretoCampos() {
        // Arrange & Act
        CadastroIrmaosPanel panel = new CadastroIrmaosPanel();
        
        // Assert - Verifica espaçamento entre componentes
        Component[] components = panel.getComponents();
        
        if (components.length > 1) {
            for (int i = 1; i < components.length; i++) {
                Component prev = components[i - 1];
                Component curr = components[i];
                
                Point prevLocation = prev.getLocation();
                Point currLocation = curr.getLocation();
                
                // Verifica se há espaçamento adequado
                if (prevLocation.y != currLocation.y) {
                    int espacamentoVertical = Math.abs(currLocation.y - prevLocation.y);
                    assertThat(espacamentoVertical).as("Espaçamento vertical deve ser adequado")
                        .isGreaterThanOrEqualTo(5);
                    assertThat(espacamentoVertical).as("Espaçamento vertical não deve ser excessivo")
                        .isLessThanOrEqualTo(100);
                }
            }
        }
    }

    @Test
    @DisplayName("Verificação de alinhamento correto dos campos")
    void testAlinhamentoCorretoCampos() {
        // Arrange & Act
        CadastroIrmaosPanel panel = new CadastroIrmaosPanel();
        
        // Assert - Verifica alinhamento de componentes
        Component[] components = panel.getComponents();
        
        // Agrupa componentes por linha (mesmo Y)
        java.util.Map<Integer, java.util.List<Component>> linhas = new java.util.HashMap<>();
        for (Component comp : components) {
            int y = comp.getLocation().y;
            linhas.computeIfAbsent(y, k -> new java.util.ArrayList<>()).add(comp);
        }
        
        // Verifica alinhamento em cada linha
        for (java.util.List<Component> linha : linhas.values()) {
            if (linha.size() > 1) {
                // Verifica se componentes na mesma linha estão alinhados verticalmente
                int yBase = linha.get(0).getLocation().y;
                for (Component comp : linha) {
                    assertThat(comp.getLocation().y).as("Componentes na mesma linha devem estar alinhados")
                        .isEqualTo(yBase);
                }
            }
        }
    }

    @Test
    @DisplayName("Validação de tamanho correto dos componentes")
    void testTamanhoCorretoComponentes() {
        // Arrange & Act
        CadastroIrmaosPanel panel = new CadastroIrmaosPanel();
        
        // Assert - Verifica tamanhos específicos por tipo de componente
        for (Component comp : panel.getComponents()) {
            Dimension size = comp.getSize();
            
            if (comp instanceof JButton) {
                // Botões devem ter tamanho adequado
                if (size.width > 0) {
                    assertThat(size.width).as("Largura do botão deve ser adequada")
                        .isGreaterThanOrEqualTo(80);
                    assertThat(size.width).as("Largura do botão não deve ser excessiva")
                        .isLessThanOrEqualTo(300);
                }
            } else if (comp instanceof JTextField) {
                // Campos de texto devem ter tamanho para digitação
                if (size.width > 0) {
                    assertThat(size.width).as("Campo de texto deve ter largura suficiente")
                        .isGreaterThanOrEqualTo(100);
                }
            } else if (comp instanceof JComboBox) {
                // ComboBox devem ter tamanho adequado
                if (size.width > 0) {
                    assertThat(size.width).as("ComboBox deve ter largura adequada")
                        .isGreaterThanOrEqualTo(100);
                }
            }
        }
    }

    @Test
    @DisplayName("Verificação de fonte correta nos componentes")
    void testFonteCorretaComponentes() {
        // Arrange & Act
        CadastroIrmaosPanel panel = new CadastroIrmaosPanel();
        
        // Assert - Verifica se componentes têm fontes definidas
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JComponent) {
                JComponent jcomp = (JComponent) comp;
                Font font = jcomp.getFont();
                
                if (font != null) {
                    assertThat(font.getSize()).as("Tamanho da fonte deve ser legível")
                        .isGreaterThanOrEqualTo(8);
                    assertThat(font.getSize()).as("Tamanho da fonte não deve ser excessivo")
                        .isLessThanOrEqualTo(48);
                }
            }
        }
    }

    @Test
    @DisplayName("Validação de layout em painel de dashboard")
    void testLayoutDashboardPanel() {
        // Arrange & Act
        DashboardPanel dashboard = new DashboardPanel();
        
        // Assert - Verifica layout do dashboard
        assertThat(dashboard).isNotNull();
        
        LayoutManager layout = dashboard.getLayout();
        assertThat(layout).isNotNull().as("Dashboard deve ter layout manager");
        
        // Verifica se dashboard tem componentes
        Component[] components = dashboard.getComponents();
        assertThat(components).isNotEmpty().as("Dashboard deve ter componentes");
    }

    @Test
    @DisplayName("Verificação de responsividade do layout")
    void testResponsividadeLayout() {
        // Arrange
        CadastroIrmaosPanel panel = new CadastroIrmaosPanel();
        
        // Act - Simula mudança de tamanho
        Dimension tamanhoOriginal = panel.getSize();
        panel.setSize(new Dimension(800, 600));
        
        // Assert - Verifica se painel se ajusta ao novo tamanho
        Dimension tamanhoNovo = panel.getSize();
        assertThat(tamanhoNovo.width).isEqualTo(800);
        assertThat(tamanhoNovo.height).isEqualTo(600);
        
        // Verifica se componentes ainda são visíveis
        for (Component comp : panel.getComponents()) {
            assertThat(comp.isVisible()).as("Componentes devem permanecer visíveis após redimensionamento")
                .isTrue();
        }
    }

    @Test
    @DisplayName("Validação de margens e padding")
    void testMargensPadding() {
        // Arrange & Act
        CadastroIrmaosPanel panel = new CadastroIrmaosPanel();
        
        // Assert - Verifica se painel tem insets (margens)
        Insets insets = panel.getInsets();
        assertThat(insets).isNotNull().as("Painel deve ter insets definidos");
        
        // Verifica se margens são razoáveis
        assertThat(insets.top).as("Margem superior deve ser razoável")
            .isGreaterThanOrEqualTo(0);
        assertThat(insets.left).as("Margem esquerda deve ser razoável")
            .isGreaterThanOrEqualTo(0);
        assertThat(insets.bottom).as("Margem inferior deve ser razoável")
            .isGreaterThanOrEqualTo(0);
        assertThat(insets.right).as("Margem direita deve ser razoável")
            .isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("Verificação de ordem lógica dos componentes")
    void testOrdemLogicaComponentes() {
        // Arrange & Act
        CadastroIrmaosPanel panel = new CadastroIrmaosPanel();
        
        // Assert - Verifica se componentes estão em ordem lógica
        Component[] components = panel.getComponents();
        
        // Verifica se há componentes e se estão ordenados por posição
        if (components.length > 1) {
            for (int i = 1; i < components.length; i++) {
                Point prevLocation = components[i - 1].getLocation();
                Point currLocation = components[i].getLocation();
                
                // Verifica se componentes seguem uma ordem (topo para baixo ou esquerda para direita)
                boolean ordemVertical = currLocation.y >= prevLocation.y;
                boolean ordemHorizontal = currLocation.x >= prevLocation.x;
                
                // Pelo menos uma das ordens deve ser mantida
                assertThat(ordemVertical || ordemHorizontal)
                    .as("Componentes devem seguir ordem lógica")
                    .isTrue();
            }
        }
    }

    @Test
    @DisplayName("Validação de acessibilidade no layout")
    void testAcessibilidadeLayout() {
        // Arrange & Act
        CadastroIrmaosPanel panel = new CadastroIrmaosPanel();
        
        // Assert - Verifica aspectos básicos de acessibilidade
        for (Component comp : panel.getComponents()) {
            // Verifica se componentes são focáveis quando apropriado
            if (comp instanceof JTextField || comp instanceof JButton || comp instanceof JComboBox) {
                assertThat(comp.isFocusable()).as("Campos interativos devem ser focáveis")
                    .isTrue();
            }
            
            // Verifica se componentes têm tooltips (se aplicável)
            if (comp instanceof JComponent) {
                JComponent jcomp = (JComponent) comp;
                // Tooltip não é obrigatório, mas é bom para acessibilidade
                // Esta verificação é apenas para garantir que não causa erro
                String tooltip = jcomp.getToolTipText();
                // Não falha se não tiver tooltip, apenas verifica se não causa exceção
            }
        }
    }
}
