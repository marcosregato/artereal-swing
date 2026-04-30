package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.IrmaoDAO;
import com.artereal.swing.model.Irmao;
import com.artereal.swing.ui.components.MasonicLogo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Painel de gestão de Irmãos
 */
public class IrmaosPanel extends JPanel {
    
    private IrmaoDAO irmaoDAO;
    private DefaultTableModel tableModel;
    private JTable irmaosTable;
    private Irmao irmaoAtual;
    
    // Formulário
    private JTextField codigoField;
    private JTextField nomeField;
    private JTextField nascimentoField;
    private JTextField estadoCivilField;
    private JTextField naturalField;
    private JTextField identidadeField;
    private JTextField tipoSanguineoField;
    private JTextField cargoLojaField;
    private JTextField grauField;
    private JTextField cargoGrandeLojaField;
    private JTextField enderecoField;
    private JTextField bairroField;
    private JTextField cidadeField;
    private JTextField estadoField;
    private JTextField telefoneField;
    private JTextField empresaField;
    private JTextField telefoneEmpresaField;
    private JTextField enderecoEmpresaField;
    private JTextField registroGrandeLojaField;
    
    // Botões
    private JButton salvarButton;
    private JButton novoButton;
    private JButton editarButton;
    private JButton excluirButton;
    private JButton limparButton;
    private JButton pesquisarButton;
    private JTextField pesquisarField;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public IrmaosPanel() {
        irmaoDAO = new IrmaoDAO();
        initializeComponents();
        setupLayout();
        setupEvents();
        refreshData();
    }
    
    private void initializeComponents() {
        // Tabela
        tableModel = new DefaultTableModel(new Object[]{
            "Código", "Nome", "Telefone", "Grau", "Cargo Loja", "Cidade"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        irmaosTable = new JTable(tableModel);
        
        // Formulário
        codigoField = new JTextField(10);
        codigoField.setEditable(false);
        nomeField = new JTextField(40);
        nascimentoField = new JTextField(10);
        estadoCivilField = new JTextField(15);
        naturalField = new JTextField(20);
        identidadeField = new JTextField(15);
        tipoSanguineoField = new JTextField(10);
        cargoLojaField = new JTextField(20);
        grauField = new JTextField(15);
        cargoGrandeLojaField = new JTextField(20);
        enderecoField = new JTextField(40);
        bairroField = new JTextField(20);
        cidadeField = new JTextField(20);
        estadoField = new JTextField(10);
        telefoneField = new JTextField(15);
        empresaField = new JTextField(30);
        telefoneEmpresaField = new JTextField(15);
        enderecoEmpresaField = new JTextField(40);
        registroGrandeLojaField = new JTextField(20);
        
        // Botões
        salvarButton = new JButton("Salvar");
        novoButton = new JButton("Novo");
        editarButton = new JButton("Editar");
        excluirButton = new JButton("Excluir");
        limparButton = new JButton("Limpar");
        pesquisarButton = new JButton("Pesquisar");
        pesquisarField = new JTextField(20);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 245));
        
        // Header com título
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(25, 25, 112)); // Azul marinho maçônico
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("👥 Gestão de Irmãos Maçons", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Cadastro e administração dos membros da loja", SwingConstants.LEFT);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(220, 220, 230));
        
        // Adicionar logo maçônico discreto
        JLabel logoLabel = MasonicLogo.createLogoLabel(32);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        JPanel titleContainer = new JPanel(new GridLayout(2, 1, 0, 5));
        titleContainer.setBackground(new Color(25, 25, 112));
        titleContainer.add(titleLabel);
        titleContainer.add(subtitleLabel);
        
        JPanel headerContent = new JPanel(new BorderLayout());
        headerContent.setBackground(new Color(25, 25, 112));
        headerContent.add(logoLabel, BorderLayout.WEST);
        headerContent.add(titleContainer, BorderLayout.CENTER);
        
        headerPanel.add(headerContent, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);
        
        // Painel principal com split
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        // Painel esquerdo - Tabela
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(new Color(245, 245, 250));
        
        // Painel de pesquisa melhorado
        JPanel pesquisaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pesquisaPanel.setBackground(new Color(240, 240, 245));
        pesquisaPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel searchLabel = new JLabel("🔍 Pesquisar:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchLabel.setForeground(new Color(100, 100, 120));
        
        pesquisarField.setPreferredSize(new Dimension(200, 30));
        pesquisarField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        
        pesquisarButton.setBackground(new Color(221, 160, 221)); // Lila pastel suave
        pesquisarButton.setForeground(new Color(102, 51, 153));
        pesquisarButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pesquisarButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(221, 160, 221), 2),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        pesquisarButton.setFocusPainted(false);
        pesquisarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        pesquisaPanel.add(searchLabel);
        pesquisaPanel.add(pesquisarField);
        pesquisaPanel.add(pesquisarButton);
        
        // Tabela estilizada
        irmaosTable.setRowHeight(25);
        irmaosTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        irmaosTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        irmaosTable.getTableHeader().setBackground(new Color(70, 130, 180));
        irmaosTable.getTableHeader().setForeground(Color.WHITE);
        irmaosTable.setSelectionBackground(new Color(173, 216, 230));
        irmaosTable.setSelectionForeground(new Color(25, 84, 123));
        
        leftPanel.add(pesquisaPanel, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(irmaosTable), BorderLayout.CENTER);
        
        // Painel direito - Formulário
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(new Color(245, 245, 250));
        
        // Título do formulário
        JPanel formTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formTitlePanel.setBackground(new Color(70, 130, 180));
        formTitlePanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel formTitle = new JLabel("📝 Dados do Irmão");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formTitle.setForeground(Color.WHITE);
        formTitlePanel.add(formTitle);
        rightPanel.add(formTitlePanel, BorderLayout.NORTH);
        
        // Formulário em grid
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Código
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel codigoLabel = new JLabel("Código:");
        codigoLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        codigoLabel.setForeground(new Color(70, 130, 180));
        formPanel.add(codigoLabel, gbc);
        gbc.gridx = 1;
        codigoField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(codigoField, gbc);
        
        // Nome
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel nomeLabel = new JLabel("Nome:");
        nomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nomeLabel.setForeground(new Color(70, 130, 180));
        formPanel.add(nomeLabel, gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        nomeField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(nomeField, gbc);
        
        // Data de Nascimento
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        JLabel nascimentoLabel = new JLabel("Nascimento:");
        nascimentoLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nascimentoLabel.setForeground(new Color(70, 130, 180));
        formPanel.add(nascimentoLabel, gbc);
        gbc.gridx = 1;
        nascimentoField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(nascimentoField, gbc);
        
        // Estado Civil
        gbc.gridx = 2; gbc.gridy = 2;
        JLabel estadoCivilLabel = new JLabel("Estado Civil:");
        estadoCivilLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        estadoCivilLabel.setForeground(new Color(70, 130, 180));
        formPanel.add(estadoCivilLabel, gbc);
        gbc.gridx = 3;
        formPanel.add(estadoCivilField, gbc);
        
        // Naturalidade
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel naturalLabel = new JLabel("Naturalidade:");
        naturalLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        naturalLabel.setForeground(new Color(70, 130, 180));
        formPanel.add(naturalLabel, gbc);
        gbc.gridx = 1;
        naturalField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(naturalField, gbc);
        
        // Identidade
        gbc.gridx = 2; gbc.gridy = 3;
        JLabel identidadeLabel = new JLabel("Identidade:");
        identidadeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        identidadeLabel.setForeground(new Color(70, 130, 180));
        formPanel.add(identidadeLabel, gbc);
        gbc.gridx = 3;
        identidadeField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(identidadeField, gbc);
        
        // Tipo Sanguíneo
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel tipoSanguineoLabel = new JLabel("Tipo Sanguíneo:");
        tipoSanguineoLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tipoSanguineoLabel.setForeground(new Color(70, 130, 180));
        formPanel.add(tipoSanguineoLabel, gbc);
        gbc.gridx = 1;
        tipoSanguineoField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(tipoSanguineoField, gbc);
        
        // Cargo na Loja
        gbc.gridx = 2; gbc.gridy = 4;
        JLabel cargoLojaLabel = new JLabel("Cargo na Loja:");
        cargoLojaLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cargoLojaLabel.setForeground(new Color(70, 130, 180));
        formPanel.add(cargoLojaLabel, gbc);
        gbc.gridx = 3;
        cargoLojaField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(cargoLojaField, gbc);
        
        // Grau
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel grauLabel = new JLabel("Grau:");
        grauLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        grauLabel.setForeground(new Color(70, 130, 180));
        formPanel.add(grauLabel, gbc);
        gbc.gridx = 1;
        grauField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(grauField, gbc);
        
        // Cargo na Grande Loja
        gbc.gridx = 2; gbc.gridy = 5;
        JLabel cargoGrandeLojaLabel = new JLabel("Cargo na Grande Loja:");
        cargoGrandeLojaLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cargoGrandeLojaLabel.setForeground(new Color(70, 130, 180));
        formPanel.add(cargoGrandeLojaLabel, gbc);
        gbc.gridx = 3;
        cargoGrandeLojaField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(cargoGrandeLojaField, gbc);
        
        // Dados Pessoais
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 6;
        JLabel dadosPessoaisLabel = new JLabel("=== DADOS PESSOAIS ===");
        dadosPessoaisLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        dadosPessoaisLabel.setForeground(new Color(70, 130, 180));
        formPanel.add(dadosPessoaisLabel, gbc);
        
        // Endereço
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 1;
        JLabel enderecoLabel = new JLabel("Endereço:");
        enderecoLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        enderecoLabel.setForeground(new Color(70, 130, 180));
        formPanel.add(enderecoLabel, gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        enderecoField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(enderecoField, gbc);
        
        // Bairro e Cidade
        gbc.gridx = 3; gbc.gridy = 8; gbc.gridwidth = 1;
        JLabel bairroLabel = new JLabel("Bairro:");
        bairroLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        bairroLabel.setForeground(new Color(70, 130, 180));
        formPanel.add(bairroLabel, gbc);
        gbc.gridx = 4;
        bairroField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(bairroField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 9;
        JLabel cidadeLabel = new JLabel("Cidade:");
        cidadeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cidadeLabel.setForeground(new Color(70, 130, 180));
        formPanel.add(cidadeLabel, gbc);
        gbc.gridx = 1;
        cidadeField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(cidadeField, gbc);
        
        // Estado e Telefone
        gbc.gridx = 2; gbc.gridy = 9;
        JLabel estadoLabel = new JLabel("Estado:");
        estadoLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        estadoLabel.setForeground(new Color(70, 130, 180));
        formPanel.add(estadoLabel, gbc);
        gbc.gridx = 3;
        estadoField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(estadoField, gbc);
        
        gbc.gridx = 4; gbc.gridy = 9;
        JLabel telefoneLabel = new JLabel("Telefone:");
        telefoneLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        telefoneLabel.setForeground(new Color(70, 130, 180));
        formPanel.add(telefoneLabel, gbc);
        gbc.gridx = 5;
        telefoneField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(telefoneField, gbc);
        
        // Dados Empresariais
        gbc.gridx = 0; gbc.gridy = 10; gbc.gridwidth = 6;
        JLabel dadosEmpresariaisLabel = new JLabel("=== DADOS EMPRESARIAIS ===");
        dadosEmpresariaisLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        dadosEmpresariaisLabel.setForeground(new Color(70, 130, 180));
        formPanel.add(dadosEmpresariaisLabel, gbc);
        
        gbc.gridy = 11; gbc.gridwidth = 1;
        JLabel empresaLabel = new JLabel("Empresa:");
        empresaLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        empresaLabel.setForeground(new Color(70, 130, 180));
        formPanel.add(empresaLabel, gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        empresaField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(empresaField, gbc);
        
        gbc.gridx = 3; gbc.gridy = 11; gbc.gridwidth = 1;
        JLabel telefoneEmpresaLabel = new JLabel("Telefone Empresa:");
        telefoneEmpresaLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        telefoneEmpresaLabel.setForeground(new Color(70, 130, 180));
        formPanel.add(telefoneEmpresaLabel, gbc);
        gbc.gridx = 4;
        telefoneEmpresaField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(telefoneEmpresaField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 12;
        JLabel enderecoEmpresaLabel = new JLabel("Endereço Empresa:");
        enderecoEmpresaLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        enderecoEmpresaLabel.setForeground(new Color(70, 130, 180));
        formPanel.add(enderecoEmpresaLabel, gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        enderecoEmpresaField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(enderecoEmpresaField, gbc);
        
        gbc.gridx = 4; gbc.gridy = 12;
        JLabel registroGrandeLojaLabel = new JLabel("Reg. Grande Loja:");
        registroGrandeLojaLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        registroGrandeLojaLabel.setForeground(new Color(70, 130, 180));
        formPanel.add(registroGrandeLojaLabel, gbc);
        gbc.gridx = 5;
        registroGrandeLojaField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(registroGrandeLojaField, gbc);
        
        // Botões do formulário estilizados com cores pastéis
        JPanel botoesFormPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        botoesFormPanel.setBackground(Color.WHITE);
        
        // Estilizar botões com cores pastéis
        salvarButton.setBackground(new Color(144, 238, 144)); // Verde pastel suave
        salvarButton.setForeground(new Color(34, 89, 34));
        salvarButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        salvarButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(144, 238, 144), 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        salvarButton.setFocusPainted(false);
        salvarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        novoButton.setBackground(new Color(173, 216, 230)); // Azul pastel suave
        novoButton.setForeground(new Color(25, 84, 123));
        novoButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        novoButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(173, 216, 230), 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        novoButton.setFocusPainted(false);
        novoButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        editarButton.setBackground(new Color(255, 239, 213)); // Amarelo pastel suave
        editarButton.setForeground(new Color(180, 140, 45));
        editarButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        editarButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 239, 213), 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        editarButton.setFocusPainted(false);
        editarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        excluirButton.setBackground(new Color(255, 182, 193)); // Rosa pastel suave
        excluirButton.setForeground(new Color(180, 82, 92));
        excluirButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        excluirButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 182, 193), 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        excluirButton.setFocusPainted(false);
        excluirButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        limparButton.setBackground(new Color(211, 211, 211)); // Cinza pastel suave
        limparButton.setForeground(new Color(84, 84, 84));
        limparButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        limparButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(211, 211, 211), 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        limparButton.setFocusPainted(false);
        limparButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        botoesFormPanel.add(salvarButton);
        botoesFormPanel.add(novoButton);
        botoesFormPanel.add(editarButton);
        botoesFormPanel.add(excluirButton);
        botoesFormPanel.add(limparButton);
        
        gbc.gridy = 12; gbc.gridx = 0; gbc.gridwidth = 6;
        formPanel.add(botoesFormPanel, gbc);
        
        rightPanel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        
        // Configurar split pane
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        splitPane.setDividerLocation(600);
        splitPane.setResizeWeight(0.6);
        
        add(titleLabel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }
    
    private void setupEvents() {
        salvarButton.addActionListener(e -> salvarIrmao());
        novoButton.addActionListener(e -> limparFormulario());
        editarButton.addActionListener(e -> carregarIrmaoSelecionado());
        excluirButton.addActionListener(e -> excluirIrmao());
        limparButton.addActionListener(e -> limparFormulario());
        pesquisarButton.addActionListener(e -> pesquisarIrmaos());
        
        // Seleção na tabela
        irmaosTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && irmaosTable.getSelectedRow() >= 0) {
                carregarIrmaoSelecionado();
            }
        });
        
        // Duplo clique para editar
        irmaosTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    carregarIrmaoSelecionado();
                }
            }
        });
    }
    
    private void salvarIrmao() {
        try {
            if (nomeField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nome é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Irmao irmao = irmaoAtual != null ? irmaoAtual : new Irmao();
            irmao.setNome(nomeField.getText().trim());
            
            // Data de nascimento
            String nascimentoStr = nascimentoField.getText().trim();
            if (!nascimentoStr.isEmpty()) {
                try {
                    irmao.setNascimento(LocalDate.parse(nascimentoStr, DATE_FORMATTER));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Data de nascimento inválida! Use formato dd/MM/yyyy", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            irmao.setEstadoCivil(estadoCivilField.getText().trim());
            irmao.setNatural(naturalField.getText().trim());
            irmao.setIdentidade(identidadeField.getText().trim());
            irmao.setTipoSanguineo(tipoSanguineoField.getText().trim());
            irmao.setCargoLoja(cargoLojaField.getText().trim());
            irmao.setGrau(grauField.getText().trim());
            irmao.setCargoGrandeLoja(cargoGrandeLojaField.getText().trim());
            irmao.setEndereco(enderecoField.getText().trim());
            irmao.setBairro(bairroField.getText().trim());
            irmao.setCidade(cidadeField.getText().trim());
            irmao.setEstado(estadoField.getText().trim());
            irmao.setTelefone(telefoneField.getText().trim());
            irmao.setEmpresa(empresaField.getText().trim());
            irmao.setTelefoneEmpresa(telefoneEmpresaField.getText().trim());
            irmao.setEnderecoEmpresa(enderecoEmpresaField.getText().trim());
            irmao.setRegistroGrandeLoja(registroGrandeLojaField.getText().trim());
            
            irmaoDAO.save(irmao);
            
            JOptionPane.showMessageDialog(this, "Irmão salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            refreshData();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar irmão: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limparFormulario() {
        irmaoAtual = null;
        codigoField.setText("");
        nomeField.setText("");
        nascimentoField.setText("");
        estadoCivilField.setText("");
        naturalField.setText("");
        identidadeField.setText("");
        tipoSanguineoField.setText("");
        cargoLojaField.setText("");
        grauField.setText("");
        cargoGrandeLojaField.setText("");
        enderecoField.setText("");
        bairroField.setText("");
        cidadeField.setText("");
        estadoField.setText("");
        telefoneField.setText("");
        empresaField.setText("");
        telefoneEmpresaField.setText("");
        enderecoEmpresaField.setText("");
        registroGrandeLojaField.setText("");
        nomeField.requestFocus();
        atualizarBotoesAcao();
    }
    
    private void excluirIrmao() {
        if (irmaoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um irmão para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int option = JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente excluir o irmão " + irmaoAtual.getNome() + "?",
            "Confirmar Exclusão",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (option == JOptionPane.YES_OPTION) {
            try {
                irmaoDAO.delete(irmaoAtual.getId());
                JOptionPane.showMessageDialog(this, "Irmão excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                refreshData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir irmão: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void pesquisarIrmaos() {
        try {
            String termo = pesquisarField.getText().trim();
            if (termo.isEmpty()) {
                refreshData();
                return;
            }
            
            List<Irmao> irmaos = irmaoDAO.findAll();
            tableModel.setRowCount(0);
            
            for (Irmao irmao : irmaos) {
                if (irmao.getNome().toLowerCase().contains(termo.toLowerCase()) ||
                    irmao.getEndereco().toLowerCase().contains(termo.toLowerCase())) {
                    Object[] row = {
                        irmao.getId(),
                        irmao.getNome(),
                        irmao.getTelefone(),
                        irmao.getGrau(),
                        irmao.getCargoLoja(),
                        irmao.getCidade()
                    };
                    tableModel.addRow(row);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao pesquisar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void carregarIrmaoSelecionado() {
        int selectedRow = irmaosTable.getSelectedRow();
        if (selectedRow >= 0) {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            try {
                irmaoAtual = irmaoDAO.findById(id);
                if (irmaoAtual != null) {
                    codigoField.setText(String.valueOf(irmaoAtual.getId()));
                    nomeField.setText(irmaoAtual.getNome());
                    
                    // Data de nascimento
                    if (irmaoAtual.getNascimento() != null) {
                        nascimentoField.setText(irmaoAtual.getNascimento().format(DATE_FORMATTER));
                    }
                    
                    estadoCivilField.setText(irmaoAtual.getEstadoCivil());
                    naturalField.setText(irmaoAtual.getNatural());
                    identidadeField.setText(irmaoAtual.getIdentidade());
                    tipoSanguineoField.setText(irmaoAtual.getTipoSanguineo());
                    cargoLojaField.setText(irmaoAtual.getCargoLoja());
                    grauField.setText(irmaoAtual.getGrau());
                    cargoGrandeLojaField.setText(irmaoAtual.getCargoGrandeLoja());
                    enderecoField.setText(irmaoAtual.getEndereco());
                    bairroField.setText(irmaoAtual.getBairro());
                    cidadeField.setText(irmaoAtual.getCidade());
                    estadoField.setText(irmaoAtual.getEstado());
                    telefoneField.setText(irmaoAtual.getTelefone());
                    empresaField.setText(irmaoAtual.getEmpresa());
                    telefoneEmpresaField.setText(irmaoAtual.getTelefoneEmpresa());
                    enderecoEmpresaField.setText(irmaoAtual.getEnderecoEmpresa());
                    registroGrandeLojaField.setText(irmaoAtual.getRegistroGrandeLoja());
                    atualizarBotoesAcao();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar irmão: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void atualizarBotoesAcao() {
        boolean temIrmao = irmaoAtual != null;
        editarButton.setEnabled(temIrmao);
        excluirButton.setEnabled(temIrmao);
    }
    
    public void refreshData() {
        try {
            List<Irmao> irmaos = irmaoDAO.findAll();
            tableModel.setRowCount(0);
            
            for (Irmao irmao : irmaos) {
                Object[] row = {
                    irmao.getId(),
                    irmao.getNome(),
                    irmao.getTelefone(),
                    irmao.getGrau(),
                    irmao.getCargoLoja(),
                    irmao.getCidade()
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar irmãos: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
