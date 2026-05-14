package com.artereal.swing.ui.panels.lojas;

import com.artereal.swing.ui.layout.PadraoLayout;

import javax.swing.*;

import java.awt.*;

/**
 * Painel de formulário para gestão de Lojas
 * Responsável por organizar e gerenciar todos os campos do formulário
 */
public class LojasFormPanel extends JPanel {
    
    // Campos de identificação
    private JTextField codigoField;
    private JTextField nomeField;
    private JTextField numeroField;
    private JTextField dataFundacaoField;
    
    // Campos de informações maçônicas
    private JTextField ritualField;
    private JTextField poderField;
    
    // Campos de endereço
    private JTextField enderecoField;
    private JTextField bairroField;
    private JTextField cidadeField;
    private JTextField estadoField;
    private JTextField cepField;
    
    // Campos de contato
    private JTextField telefoneField;
    private JTextField emailField;
    
    // Campos de diretoria
    private JTextField presidenteField;
    private JTextField secretarioField;
    private JTextField tesoureiroField;
    
    // Campo de observações
    private JTextArea observacoesArea;
    
    public LojasFormPanel() {
        initializeFields();
        setupLayout();
    }
    
    /**
     * Inicializa todos os campos do formulário
     */
    private void initializeFields() {
        // Campos de identificação
        codigoField = new JTextField();
        nomeField = new JTextField();
        numeroField = new JTextField(15);
        dataFundacaoField = new JTextField(15);
        
        // Campos de informações maçônicas
        ritualField = new JTextField(20);
        poderField = new JTextField(25);
        
        // Campos de endereço
        enderecoField = new JTextField();
        bairroField = new JTextField();
        cidadeField = new JTextField();
        estadoField = new JTextField();
        cepField = new JTextField();
        
        // Campos de contato
        telefoneField = new JTextField();
        emailField = new JTextField();
        
        // Campos de diretoria
        presidenteField = new JTextField(40);
        secretarioField = new JTextField(40);
        tesoureiroField = new JTextField(40);
        
        // Campo de observações
        observacoesArea = new JTextArea(3, 40);
        observacoesArea.setLineWrap(true);
        observacoesArea.setWrapStyleWord(true);
        
        // Aplicar estilização
        applyFieldStyling();
    }
    
    /**
     * Aplica estilização padrão aos campos
     */
    private void applyFieldStyling() {
        PadraoLayout.estilizarCampoCodigo(codigoField);
        PadraoLayout.estilizarCampoNome(nomeField);
        PadraoLayout.estilizarCampoNumeroLoja(numeroField);
        PadraoLayout.estilizarCampoData(dataFundacaoField);
        PadraoLayout.estilizarCampoRitalMasonico(ritualField);
        PadraoLayout.estilizarCampoPotenciaMasonica(poderField);
        PadraoLayout.estilizarCampoEndereco(enderecoField);
        PadraoLayout.estilizarCampoBairro(bairroField);
        PadraoLayout.estilizarCampoCidade(cidadeField);
        PadraoLayout.estilizarCampoEstado(estadoField);
        PadraoLayout.estilizarCampoCEP(cepField);
        PadraoLayout.estilizarCampoTelefone(telefoneField);
        PadraoLayout.estilizarCampoEmail(emailField);
        PadraoLayout.estilizarCampoCargoDiretoria(presidenteField);
        PadraoLayout.estilizarCampoCargoDiretoria(secretarioField);
        PadraoLayout.estilizarCampoCargoDiretoria(tesoureiroField);
    }
    
    /**
     * Configura o layout do formulário
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Criar conteúdo do formulário
        JPanel formContent = createFormContent();
        
        // Adicionar barra de rolagem
        JScrollPane scrollPane = new JScrollPane(formContent);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Cria conteúdo do formulário com layout otimizado para UX
     */
    private JPanel createFormContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        // GRUPO 1: Dados Principais (Identificação + Maçônicos)
        content.add(createMainDataSection());
        content.add(Box.createVerticalStrut(20));
        
        // GRUPO 2: Localização (Endereço + Contato)
        content.add(createLocationSection());
        content.add(Box.createVerticalStrut(20));
        
        // GRUPO 3: Administração (Diretoria + Observações)
        content.add(createAdministrationSection());
        
        return content;
    }
    
    /**
     * Cria grupo de dados principais com layout GridBag otimizado
     */
    private JPanel createMainDataSection() {
        JPanel mainGroup = new JPanel();
        mainGroup.setLayout(new BoxLayout(mainGroup, BoxLayout.Y_AXIS));
        mainGroup.setBackground(Color.WHITE);
        
        // Título do grupo com estilo aprimorado
        JLabel mainTitle = createSectionTitle("📋 DADOS PRINCIPAIS");
        mainTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainGroup.add(mainTitle);
        mainGroup.add(Box.createVerticalStrut(12));
        
        // Subgrupo: Identificação com GridBagLayout
        JPanel identSubGroup = createModernSection("🏛️ Identificação da Loja");
        identSubGroup.add(createIdentificationGrid(), BorderLayout.CENTER);
        mainGroup.add(identSubGroup);
        mainGroup.add(Box.createVerticalStrut(15));
        
        // Subgrupo: Informações Maçônicas
        JPanel masonicSubGroup = createModernSection("🔷 Informações Maçônicas");
        masonicSubGroup.add(createMasonicGrid(), BorderLayout.CENTER);
        mainGroup.add(masonicSubGroup);
        
        return mainGroup;
    }
    
    /**
     * Cria grupo de localização com layout otimizado
     */
    private JPanel createLocationSection() {
        JPanel locationGroup = new JPanel();
        locationGroup.setLayout(new BoxLayout(locationGroup, BoxLayout.Y_AXIS));
        locationGroup.setBackground(Color.WHITE);
        
        // Título do grupo
        JLabel locationTitle = createSectionTitle("📍 LOCALIZAÇÃO");
        locationTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        locationGroup.add(locationTitle);
        locationGroup.add(Box.createVerticalStrut(12));
        
        // Subgrupo: Endereço com GridBagLayout
        JPanel addressSubGroup = createModernSection("🏠 Endereço");
        addressSubGroup.add(createAddressGrid(), BorderLayout.CENTER);
        locationGroup.add(addressSubGroup);
        locationGroup.add(Box.createVerticalStrut(15));
        
        // Subgrupo: Contato
        JPanel contactSubGroup = createModernSection("📞 Contato");
        contactSubGroup.add(createContactGrid(), BorderLayout.CENTER);
        locationGroup.add(contactSubGroup);
        
        return locationGroup;
    }
    
    /**
     * Cria grupo de administração com layout otimizado
     */
    private JPanel createAdministrationSection() {
        JPanel adminGroup = new JPanel();
        adminGroup.setLayout(new BoxLayout(adminGroup, BoxLayout.Y_AXIS));
        adminGroup.setBackground(Color.WHITE);
        
        // Título do grupo
        JLabel adminTitle = createSectionTitle("👥 ADMINISTRAÇÃO");
        adminTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        adminGroup.add(adminTitle);
        adminGroup.add(Box.createVerticalStrut(12));
        
        // Subgrupo: Diretoria com GridBagLayout
        JPanel directorshipSubGroup = createModernSection("🏛️ Diretoria Atual");
        directorshipSubGroup.add(createDirectorshipGrid(), BorderLayout.CENTER);
        adminGroup.add(directorshipSubGroup);
        adminGroup.add(Box.createVerticalStrut(15));
        
        // Subgrupo: Observações
        JPanel obsSubGroup = createModernSection("📝 Observações Adicionais");
        obsSubGroup.add(createObservationsGrid(), BorderLayout.CENTER);
        adminGroup.add(obsSubGroup);
        
        return adminGroup;
    }
    
    /**
     * Cria título de seção com estilo moderno
     */
    private JLabel createSectionTitle(String title) {
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(70, 130, 180));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        return titleLabel;
    }
    
    /**
     * Cria seção moderna com borda e espaçamento
     */
    private JPanel createModernSection(String title) {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(Color.WHITE);
        
        // Borda moderna com sombra sutil
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                title,
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(70, 130, 180)
            ),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        return section;
    }
    
    /**
     * Cria grid de identificação com GridBagLayout
     */
    private JPanel createIdentificationGrid() {
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Linha 1: Código e Nome
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        grid.add(createModernFieldPanel("🔢 Código:", codigoField), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.7;
        grid.add(createModernFieldPanel("📛 Nome:", nomeField), gbc);
        
        // Linha 2: Número e Data Fundação
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.5;
        grid.add(createModernFieldPanel("🏢 Número:", numeroField), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.5;
        grid.add(createModernFieldPanel("📅 Data Fundação:", dataFundacaoField), gbc);
        
        return grid;
    }
    
    /**
     * Cria grid de informações maçônicas
     */
    private JPanel createMasonicGrid() {
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.5;
        grid.add(createModernFieldPanel("🔷 Ritual:", ritualField), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.5;
        grid.add(createModernFieldPanel("⭐ Potência:", poderField), gbc);
        
        return grid;
    }
    
    /**
     * Cria grid de endereço
     */
    private JPanel createAddressGrid() {
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Endereço completo (linha inteira)
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.weightx = 1.0;
        grid.add(createModernFieldPanel("🏠 Endereço:", enderecoField), gbc);
        
        // Bairro e Cidade
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.weightx = 0.4;
        grid.add(createModernFieldPanel("🏘️ Bairro:", bairroField), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.6;
        grid.add(createModernFieldPanel("🏙️ Cidade:", cidadeField), gbc);
        
        // Estado e CEP
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        grid.add(createModernFieldPanel("🗺️ Estado:", estadoField), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.7;
        grid.add(createModernFieldPanel("📮 CEP:", cepField), gbc);
        
        return grid;
    }
    
    /**
     * Cria grid de contato
     */
    private JPanel createContactGrid() {
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.5;
        grid.add(createModernFieldPanel("📞 Telefone:", telefoneField), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.5;
        grid.add(createModernFieldPanel("📧 E-mail:", emailField), gbc);
        
        return grid;
    }
    
    /**
     * Cria grid de diretoria
     */
    private JPanel createDirectorshipGrid() {
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Presidente (linha inteira)
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.weightx = 1.0;
        grid.add(createModernFieldPanel("👑 Presidente:", presidenteField), gbc);
        
        // Secretário e Tesoureiro
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.weightx = 0.5;
        grid.add(createModernFieldPanel("📝 Secretário:", secretarioField), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.5;
        grid.add(createModernFieldPanel("💰 Tesoureiro:", tesoureiroField), gbc);
        
        return grid;
    }
    
    /**
     * Cria grid de observações
     */
    private JPanel createObservationsGrid() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        
        // Label
        JLabel obsLabel = new JLabel("📝 Observações:");
        obsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        obsLabel.setForeground(new Color(70, 130, 180));
        container.add(obsLabel, BorderLayout.NORTH);
        
        // Área de texto
        observacoesArea.setBackground(Color.WHITE);
        observacoesArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        
        JScrollPane scrollPane = new JScrollPane(observacoesArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        container.add(scrollPane, BorderLayout.CENTER);
        return container;
    }
    
    /**
     * Cria painel de campo moderno
     */
    private JPanel createModernFieldPanel(String labelText, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(new Color(70, 130, 180));
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 3, 0));
        
        panel.add(label, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        
        return panel;
    }
    
    
    /**
     * Cria painel de campo em largura completa
     */
    @SuppressWarnings("unused")
    private JPanel createFullWidthFieldPanel(String labelText, JTextField field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setBackground(Color.WHITE);
        
        panel.add(PadraoLayout.criarLabelFormulario(labelText));
        panel.add(field);
        
        return panel;
    }
    
    // Métodos getters para acesso aos campos
    public JTextField getCodigoField() { return codigoField; }
    public JTextField getNomeField() { return nomeField; }
    public JTextField getNumeroField() { return numeroField; }
    public JTextField getDataFundacaoField() { return dataFundacaoField; }
    public JTextField getRitualField() { return ritualField; }
    public JTextField getPoderField() { return poderField; }
    public JTextField getEnderecoField() { return enderecoField; }
    public JTextField getBairroField() { return bairroField; }
    public JTextField getCidadeField() { return cidadeField; }
    public JTextField getEstadoField() { return estadoField; }
    public JTextField getCepField() { return cepField; }
    public JTextField getTelefoneField() { return telefoneField; }
    public JTextField getEmailField() { return emailField; }
    public JTextField getPresidenteField() { return presidenteField; }
    public JTextField getSecretarioField() { return secretarioField; }
    public JTextField getTesoureiroField() { return tesoureiroField; }
    public JTextArea getObservacoesArea() { return observacoesArea; }
    
    /**
     * Limpa todos os campos do formulário
     */
    public void clearForm() {
        codigoField.setText("");
        nomeField.setText("");
        numeroField.setText("");
        dataFundacaoField.setText("");
        ritualField.setText("");
        poderField.setText("");
        enderecoField.setText("");
        bairroField.setText("");
        cidadeField.setText("");
        estadoField.setText("");
        cepField.setText("");
        telefoneField.setText("");
        emailField.setText("");
        presidenteField.setText("");
        secretarioField.setText("");
        tesoureiroField.setText("");
        observacoesArea.setText("");
    }
}
