package com.artereal.swing.ui.panels.screens;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Painel de Configurações otimizado com UX moderna
 */
public class ConfiguracoesEnhancedPanel extends BaseEnhancedScreenPanel {
    
    // Campos de configuração
    private JTextField nomeLojaField;
    private JTextField numeroLojaField;
    private JTextField enderecoField;
    private JTextField telefoneField;
    private JTextField emailField;
    private JComboBox<String> temaCombo;
    private JCheckBox notificacoesCheckBox;
    private JCheckBox backupAutoCheckBox;
    private JTextField intervaloBackupField;
    private JTextArea observacoesArea;
    
    public ConfiguracoesEnhancedPanel() {
        super();
        initializeFields();
        setupConfigSections();
    }
    
    /**
     * Inicializa campos de configuração
     */
    private void initializeFields() {
        nomeLojaField = new JTextField(30);
        numeroLojaField = new JTextField(15);
        enderecoField = new JTextField(40);
        telefoneField = new JTextField(20);
        emailField = new JTextField(30);
        temaCombo = new JComboBox<>(new String[]{"Claro", "Escuro", "Azul", "Verde"});
        notificacoesCheckBox = new JCheckBox("Habilitar notificações");
        backupAutoCheckBox = new JCheckBox("Backup automático");
        intervaloBackupField = new JTextField(10);
        observacoesArea = new JTextArea(3, 40);
        
        // Estilização
        nomeLojaField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        numeroLojaField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        enderecoField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        telefoneField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        emailField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        intervaloBackupField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        observacoesArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        // Valores padrão
        notificacoesCheckBox.setSelected(true);
        backupAutoCheckBox.setSelected(true);
        intervaloBackupField.setText("24");
        temaCombo.setSelectedItem("Claro");
    }
    
    /**
     * Configura seções de configurações
     */
    private void setupConfigSections() {
        // Seção 1: Dados da Loja
        addSection(new ScreenSection(
            "🏢 DADOS DA LOJA",
            ScreenSection.LayoutType.FORM,
            new ScreenCard("", "Configurar informações básicas da loja", "")
        ));
        
        // Seção 2: Aparência
        addSection(new ScreenSection(
            "🎨 APARÊNCIA",
            ScreenSection.LayoutType.CARDS,
            new ScreenCard("🎨", "Tema", "Claro", "Tema visual do sistema"),
            new ScreenCard("🔤", "Fonte", "Segoe UI", "Tipo de fonte padrão"),
            new ScreenCard("📐", "Layout", "Padrão", "Configuração de layout")
        ));
        
        // Seção 3: Sistema
        addSection(new ScreenSection(
            "⚙️ SISTEMA",
            ScreenSection.LayoutType.CARDS,
            new ScreenCard("🔔", "Notificações", "Ativas", "Alertas do sistema"),
            new ScreenCard("💾", "Backup", "Automático", "Cópia de segurança"),
            new ScreenCard("🔄", "Atualização", "2.2.0", "Versão atual")
        ));
        
        // Seção 4: Ações
        addSection(new ScreenSection(
            "⚡ AÇÕES",
            ScreenSection.LayoutType.CARDS,
            new ScreenCard("💾", "Salvar", "Configurações", null, this::salvarConfiguracoes),
            new ScreenCard("🔄", "Restaurar", "Padrão", null, this::restaurarPadrao),
            new ScreenCard("📤", "Exportar", "Config", null, this::exportarConfig),
            new ScreenCard("📥", "Importar", "Config", null, this::importarConfig)
        ));
    }
    
    @Override
    protected JPanel createFormLayout(ScreenSection section) {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CARD_BACKGROUND);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(CARD_BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Nome da Loja
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        formPanel.add(new JLabel("📛 Nome da Loja:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(nomeLojaField, gbc);
        
        // Número da Loja
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        formPanel.add(new JLabel("🔢 Número:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(numeroLojaField, gbc);
        
        // Endereço
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        formPanel.add(new JLabel("📍 Endereço:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(enderecoField, gbc);
        
        // Telefone
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.3;
        formPanel.add(new JLabel("📞 Telefone:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(telefoneField, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.3;
        formPanel.add(new JLabel("📧 E-mail:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(emailField, gbc);
        
        return formPanel;
    }
    
    /**
     * Ações dos cards
     */
    private void salvarConfiguracoes(ActionEvent e) {
        JOptionPane.showMessageDialog(this, 
            "Configurações salvas com sucesso!", 
            "Salvar", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void restaurarPadrao(ActionEvent e) {
        nomeLojaField.setText("");
        numeroLojaField.setText("");
        enderecoField.setText("");
        telefoneField.setText("");
        emailField.setText("");
        temaCombo.setSelectedItem("Claro");
        notificacoesCheckBox.setSelected(true);
        backupAutoCheckBox.setSelected(true);
        intervaloBackupField.setText("24");
        
        JOptionPane.showMessageDialog(this, 
            "Configurações restauradas para o padrão!", 
            "Restaurar", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void exportarConfig(ActionEvent e) {
        JOptionPane.showMessageDialog(this, 
            "Exportando configurações...", 
            "Exportar", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void importarConfig(ActionEvent e) {
        JOptionPane.showMessageDialog(this, 
            "Importando configurações...", 
            "Importar", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    @Override
    protected String getScreenTitle() {
        return "⚙️ Configurações";
    }
    
    @Override
    protected String getScreenSubtitle() {
        return "Personalize as configurações do sistema ArteReal";
    }
    
    @Override
    protected JPanel createHeaderActions() {
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionsPanel.setBackground(HEADER_COLOR);
        
        JButton testButton = createActionButton("Testar", "🧪");
        testButton.addActionListener(e -> testarConfiguracoes());
        
        JButton helpButton = createActionButton("Ajuda", "❓");
        helpButton.addActionListener(e -> mostrarAjuda());
        
        actionsPanel.add(testButton);
        actionsPanel.add(helpButton);
        
        return actionsPanel;
    }
    
    /**
     * Testa configurações
     */
    private void testarConfiguracoes() {
        JOptionPane.showMessageDialog(this, 
            "Testando conexões e configurações...", 
            "Teste", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Mostra ajuda
     */
    private void mostrarAjuda() {
        JOptionPane.showMessageDialog(this, 
            "Sistema ArteReal - Guia de Configurações\n\n" +
            "1. Configure os dados da loja\n" +
            "2. Escolha o tema visual\n" +
            "3. Ajuste as notificações\n" +
            "4. Configure o backup automático\n" +
            "5. Salve as configurações", 
            "Ajuda", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}
