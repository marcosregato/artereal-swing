package com.artereal.swing.ui.factory;

import com.artereal.swing.ui.layout.PadraoLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Factory para criação de componentes UI padronizados.
 * Implementa o padrão Factory para centralizar criação de componentes.
 */
public class UIComponentFactory {
    
    // Cores padrão
    public static final Color PRIMARY_COLOR = new Color(70, 130, 180);
    public static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    public static final Color WARNING_COLOR = new Color(255, 193, 7);
    public static final Color DANGER_COLOR = new Color(220, 53, 69);
    public static final Color INFO_COLOR = new Color(23, 162, 184);
    public static final Color SECONDARY_COLOR = new Color(108, 117, 125);
    public static final Color LIGHT_COLOR = new Color(248, 249, 250);
    public static final Color DARK_COLOR = new Color(52, 58, 64);
    
    // Fontes
    public static final Font DEFAULT_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font BOLD_FONT = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    
    /**
     * Cria um botão com estilo padrão
     */
    public static JButton createButton(String text, Color color) {
        return PadraoLayout.criarBotao(text, color);
    }
    
    /**
     * Cria um botão primário
     */
    public static JButton createPrimaryButton(String text) {
        return createButton(text, PRIMARY_COLOR);
    }
    
    /**
     * Cria um botão de sucesso
     */
    public static JButton createSuccessButton(String text) {
        return createButton(text, SUCCESS_COLOR);
    }
    
    /**
     * Cria um botão de aviso
     */
    public static JButton createWarningButton(String text) {
        return createButton(text, WARNING_COLOR);
    }
    
    /**
     * Cria um botão de perigo
     */
    public static JButton createDangerButton(String text) {
        return createButton(text, DANGER_COLOR);
    }
    
    /**
     * Cria um botão secundário
     */
    public static JButton createSecondaryButton(String text) {
        return createButton(text, SECONDARY_COLOR);
    }
    
    /**
     * Cria um campo de texto estilizado
     */
    public static JTextField createTextField(int columns) {
        JTextField field = new JTextField(columns);
        PadraoLayout.estilizarCampoTexto(field);
        return field;
    }
    
    /**
     * Cria um campo de texto estilizado
     */
    public static JTextField createTextField() {
        return createTextField(20);
    }
    
    /**
     * Cria uma área de texto estilizada
     */
    public static JTextArea createTextArea(int rows, int columns) {
        JTextArea area = new JTextArea(rows, columns);
        area.setFont(DEFAULT_FONT);
        area.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        return area;
    }
    
    /**
     * Cria um combo box estilizado
     */
    public static <T> JComboBox<T> createComboBox(T[] items) {
        JComboBox<T> comboBox = new JComboBox<>(items);
        PadraoLayout.estilizarComboBox(comboBox);
        return comboBox;
    }
    
    /**
     * Cria um combo box estilizado vazio
     */
    public static <T> JComboBox<T> createComboBox() {
        JComboBox<T> comboBox = new JComboBox<>();
        PadraoLayout.estilizarComboBox(comboBox);
        return comboBox;
    }
    
    /**
     * Cria um label de formulário
     */
    public static JLabel createFormLabel(String text) {
        return PadraoLayout.criarLabelFormulario(text);
    }
    
    /**
     * Cria um label normal
     */
    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(DEFAULT_FONT);
        label.setForeground(DARK_COLOR);
        return label;
    }
    
    /**
     * Cria um label de título
     */
    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(TITLE_FONT);
        label.setForeground(PRIMARY_COLOR);
        return label;
    }
    
    /**
     * Cria um label de header
     */
    public static JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(HEADER_FONT);
        label.setForeground(PRIMARY_COLOR);
        return label;
    }
    
    /**
     * Cria um checkbox estilizado
     */
    public static JCheckBox createCheckBox(String text) {
        JCheckBox checkBox = new JCheckBox(text);
        checkBox.setFont(DEFAULT_FONT);
        checkBox.setForeground(DARK_COLOR);
        checkBox.setBackground(Color.WHITE);
        return checkBox;
    }
    
    /**
     * Cria um radio button estilizado
     */
    public static JRadioButton createRadioButton(String text) {
        JRadioButton radioButton = new JRadioButton(text);
        radioButton.setFont(DEFAULT_FONT);
        radioButton.setForeground(DARK_COLOR);
        radioButton.setBackground(Color.WHITE);
        return radioButton;
    }
    
    /**
     * Cria um painel com layout de formulário
     */
    public static JPanel createFormPanel() {
        JPanel panel = new JPanel(PadraoLayout.criarLayoutFormulario());
        panel.setBackground(Color.WHITE);
        return panel;
    }
    
    /**
     * Cria um grupo de formulário
     */
    public static JPanel createFormGroup(String title) {
        return PadraoLayout.criarGrupoFormulario(title);
    }
    
    /**
     * Cria um painel de botões
     */
    public static JPanel createButtonPanel(JButton... buttons) {
        return PadraoLayout.criarPainelBotoes(buttons);
    }
    
    /**
     * Cria um painel com scroll
     */
    public static JScrollPane createScrollPane(Component component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 210), 1));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }
    
    /**
     * Cria um separador
     */
    public static JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(200, 200, 210));
        return separator;
    }
    
    /**
     * Cria um painel de header
     */
    public static JPanel createHeaderPanel(String title, String description) {
        return PadraoLayout.criarHeader(title, description);
    }
    
    /**
     * Cria um spinner
     */
    public static JSpinner createSpinner() {
        JSpinner spinner = new JSpinner();
        spinner.setFont(DEFAULT_FONT);
        PadraoLayout.estilizarCampoTexto((JTextField) spinner.getEditor().getComponent(0));
        return spinner;
    }
    
    /**
     * Cria um campo de senha
     */
    public static JPasswordField createPasswordField(int columns) {
        JPasswordField field = new JPasswordField(columns);
        PadraoLayout.estilizarCampoTexto(field);
        return field;
    }
    
    /**
     * Cria um campo de data
     */
    public static JTextField createDateField() {
        JTextField field = createTextField(12);
        field.setToolTipText("Formato: dd/MM/yyyy");
        return field;
    }
    
    /**
     * Cria um painel com grid layout
     */
    public static JPanel createGridPanel(int rows, int cols, int hgap, int vgap) {
        JPanel panel = new JPanel(new GridLayout(rows, cols, hgap, vgap));
        panel.setBackground(Color.WHITE);
        return panel;
    }
    
    /**
     * Cria um painel com border layout
     */
    public static JPanel createBorderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        return panel;
    }
    
    /**
     * Cria um painel com flow layout
     */
    public static JPanel createFlowPanel(int alignment) {
        JPanel panel = new JPanel(new FlowLayout(alignment));
        panel.setBackground(Color.WHITE);
        return panel;
    }
    
    /**
     * Cria um painel com flow layout (alinhamento à esquerda)
     */
    public static JPanel createFlowPanel() {
        return createFlowPanel(FlowLayout.LEFT);
    }
    
    /**
     * Aplica espaçamento padrão a um componente
     */
    public static void applyStandardSpacing(JComponent component) {
        component.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
    
    /**
     * Aplica borda padrão a um componente
     */
    public static void applyStandardBorder(JComponent component) {
        component.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 210), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
    }
    
    /**
     * Cria um painel com margem
     */
    public static JPanel createPanelWithMargin(JPanel innerPanel) {
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(Color.WHITE);
        outerPanel.add(innerPanel, BorderLayout.CENTER);
        outerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        return outerPanel;
    }
}
