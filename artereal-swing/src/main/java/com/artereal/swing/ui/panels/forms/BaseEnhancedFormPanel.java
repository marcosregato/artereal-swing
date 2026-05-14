package com.artereal.swing.ui.panels.forms;

import com.artereal.swing.ui.layout.PadraoLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;


/**
 * Painel de formulário base com UX otimizada
 * Fornece estrutura consistente para todos os formulários do sistema
 */
public abstract class BaseEnhancedFormPanel extends JPanel {
    
    protected static final Color SECTION_TITLE_COLOR = new Color(70, 130, 180);
    protected static final Color FIELD_LABEL_COLOR = new Color(70, 130, 180);
    protected static final Color BORDER_COLOR = new Color(180, 180, 180);
    protected static final Font SECTION_TITLE_FONT = new Font("Segoe UI", Font.BOLD, 14);
    protected static final Font FIELD_LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    protected static final Font SECTION_SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 12);
    
    protected JPanel mainContent;
    protected JScrollPane scrollPane;
    
    public BaseEnhancedFormPanel() {
        initializeComponents();
        setupLayout();
    }
    
    /**
     * Inicializa componentes básicos
     */
    private void initializeComponents() {
        mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(Color.WHITE);
        mainContent.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        scrollPane = new JScrollPane(mainContent);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
    }
    
    /**
     * Configura layout principal
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Adiciona grupo de seções ao formulário
     */
    protected void addFormGroup(FormSection... sections) {
        for (int i = 0; i < sections.length; i++) {
            mainContent.add(createSectionPanel(sections[i]));
            
            if (i < sections.length - 1) {
                mainContent.add(Box.createVerticalStrut(20));
            }
        }
    }
    
    /**
     * Cria painel de seção com título e conteúdo
     */
    protected JPanel createSectionPanel(FormSection section) {
        JPanel sectionPanel = new JPanel();
        sectionPanel.setLayout(new BoxLayout(sectionPanel, BoxLayout.Y_AXIS));
        sectionPanel.setBackground(Color.WHITE);
        
        // Título da seção
        JLabel titleLabel = createSectionTitle(section.getTitle());
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sectionPanel.add(titleLabel);
        sectionPanel.add(Box.createVerticalStrut(12));
        
        // Subseções
        for (int i = 0; i < section.getSubsections().length; i++) {
            FormSubsection subsection = section.getSubsections()[i];
            JPanel subsectionPanel = createSubsectionPanel(subsection);
            sectionPanel.add(subsectionPanel);
            
            if (i < section.getSubsections().length - 1) {
                sectionPanel.add(Box.createVerticalStrut(15));
            }
        }
        
        return sectionPanel;
    }
    
    /**
     * Cria painel de subseção
     */
    protected JPanel createSubsectionPanel(FormSubsection subsection) {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        
        JPanel subsectionPanel = createModernSection(subsection.getTitle());
        subsectionPanel.add(createFieldGrid(subsection.getFields()), BorderLayout.CENTER);
        
        container.add(subsectionPanel, BorderLayout.CENTER);
        return container;
    }
    
    /**
     * Cria título de seção com estilo moderno
     */
    protected JLabel createSectionTitle(String title) {
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(SECTION_TITLE_FONT);
        titleLabel.setForeground(SECTION_TITLE_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        return titleLabel;
    }
    
    /**
     * Cria seção moderna com borda e espaçamento
     */
    protected JPanel createModernSection(String title) {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(Color.WHITE);
        
        // Borda moderna com sombra sutil
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                SECTION_SUBTITLE_FONT,
                SECTION_TITLE_COLOR
            ),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        return section;
    }
    
    /**
     * Cria grid de campos com GridBagLayout
     */
    protected JPanel createFieldGrid(FormField[] fields) {
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        for (FormField field : fields) {
            addFieldToGrid(grid, gbc, field);
        }
        
        return grid;
    }
    
    /**
     * Adiciona campo ao grid
     */
    private void addFieldToGrid(JPanel grid, GridBagConstraints gbc, FormField field) {
        gbc.gridx = field.getColumn();
        gbc.gridy = field.getRow();
        gbc.gridwidth = field.getColSpan();
        gbc.weightx = field.getWeight();
        
        JPanel fieldPanel = createModernFieldPanel(field.getLabel(), field.getComponent());
        grid.add(fieldPanel, gbc);
    }
    
    /**
     * Cria painel de campo moderno
     */
    protected JPanel createModernFieldPanel(String labelText, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        
        JLabel label = new JLabel(labelText);
        label.setFont(FIELD_LABEL_FONT);
        label.setForeground(FIELD_LABEL_COLOR);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 3, 0));
        
        panel.add(label, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Cria painel de campo de texto
     */
    protected JTextField createTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        return field;
    }
    
    /**
     * Cria área de texto
     */
    protected JTextArea createTextArea(int rows, int columns) {
        JTextArea area = new JTextArea(rows, columns);
        area.setBackground(Color.WHITE);
        area.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        return area;
    }
    
    /**
     * Cria painel de observações
     */
    protected JPanel createObservationsPanel(JTextArea textArea) {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        
        JLabel obsLabel = new JLabel("📝 Observações:");
        obsLabel.setFont(FIELD_LABEL_FONT);
        obsLabel.setForeground(FIELD_LABEL_COLOR);
        container.add(obsLabel, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        container.add(scrollPane, BorderLayout.CENTER);
        return container;
    }
    
    /**
     * Limpa todos os campos do formulário
     */
    public abstract void clearForm();
    
    /**
     * Aplica estilização padrão a um campo
     */
    protected void applyFieldStyling(JComponent field, String fieldType) {
        if (field instanceof JTextField) {
            JTextField textField = (JTextField) field;
            
            switch (fieldType.toLowerCase()) {
                case "codigo":
                    PadraoLayout.estilizarCampoCodigo(textField);
                    break;
                case "nome":
                    PadraoLayout.estilizarCampoNome(textField);
                    break;
                case "email":
                    PadraoLayout.estilizarCampoEmail(textField);
                    break;
                case "telefone":
                    PadraoLayout.estilizarCampoTelefone(textField);
                    break;
                case "endereco":
                    PadraoLayout.estilizarCampoEndereco(textField);
                    break;
                case "bairro":
                    PadraoLayout.estilizarCampoBairro(textField);
                    break;
                case "cidade":
                    PadraoLayout.estilizarCampoCidade(textField);
                    break;
                case "estado":
                    PadraoLayout.estilizarCampoEstado(textField);
                    break;
                case "cep":
                    PadraoLayout.estilizarCampoCEP(textField);
                    break;
                default:
                    // Estilização padrão
                    PadraoLayout.estilizarCampoCargoDiretoria(textField);
                    break;
            }
        }
    }
    
    /**
     * Classes de definição de estrutura do formulário
     */
    public static class FormSection {
        private final String title;
        private final FormSubsection[] subsections;
        
        public FormSection(String title, FormSubsection... subsections) {
            this.title = title;
            this.subsections = subsections;
        }
        
        public String getTitle() { return title; }
        public FormSubsection[] getSubsections() { return subsections; }
    }
    
    public static class FormSubsection {
        private final String title;
        private final FormField[] fields;
        
        public FormSubsection(String title, FormField... fields) {
            this.title = title;
            this.fields = fields;
        }
        
        public String getTitle() { return title; }
        public FormField[] getFields() { return fields; }
    }
    
    public static class FormField {
        private final String label;
        private final JComponent component;
        private final int row;
        private final int column;
        private final int colSpan;
        private final double weight;
        
        public FormField(String label, JComponent component, int row, int column, int colSpan, double weight) {
            this.label = label;
            this.component = component;
            this.row = row;
            this.column = column;
            this.colSpan = colSpan;
            this.weight = weight;
        }
        
        public FormField(String label, JComponent component, int row, int column) {
            this(label, component, row, column, 1, 0.5);
        }
        
        public String getLabel() { return label; }
        public JComponent getComponent() { return component; }
        public int getRow() { return row; }
        public int getColumn() { return column; }
        public int getColSpan() { return colSpan; }
        public double getWeight() { return weight; }
    }
}
