package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.CaixaDAO;
import com.artereal.swing.model.Caixa;
import com.artereal.swing.ui.components.MasonicLogo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Painel de gestão Financeira (Caixa)
 */
public class CaixaPanel extends JPanel {
    
    private static final Logger logger = LoggerFactory.getLogger(CaixaPanel.class);
    private CaixaDAO caixaDAO;
    private DefaultTableModel tableModel;
    private JTable caixaTable;
    private Caixa caixaAtual;
    
    // Formulário
    private JTextField codigoField;
    private JComboBox<String> tipoCombo;
    private JTextField categoriaField;
    private JTextField descricaoField;
    private JTextField valorField;
    private JTextField dataMovimentacaoField;
    private JTextField responsavelField;
    private JComboBox<String> formaPagamentoCombo;
    private JTextField numeroDocumentoField;
    private JComboBox<String> statusCombo;
    private JTextArea observacoesArea;
    
    // Botões
    private JButton salvarButton;
    private JButton novoButton;
    private JButton editarButton;
    private JButton excluirButton;
    private JButton limparButton;
    private JButton pesquisarButton;
    private JButton relatorioButton;
    private JTextField pesquisarField;
    
    // Labels de resumo
    private JLabel saldoLabel;
    private JLabel receitasLabel;
    private JLabel despesasLabel;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    public CaixaPanel() {
        caixaDAO = new CaixaDAO();
        initializeComponents();
        setupLayout();
        setupEvents();
        refreshData();
        atualizarResumoFinanceiro();
    }
    
    private void initializeComponents() {
        // Tabela
        tableModel = new DefaultTableModel(new Object[]{
            "Código", "Tipo", "Descrição", "Valor", "Data", "Status"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        caixaTable = new JTable(tableModel);
        
        // Formulário
        codigoField = new JTextField(10);
        codigoField.setEditable(false);
        
        tipoCombo = new JComboBox<>(new String[]{"RECEITA", "DESPESA"});
        categoriaField = new JTextField(20);
        descricaoField = new JTextField(40);
        valorField = new JTextField(15);
        dataMovimentacaoField = new JTextField(16);
        responsavelField = new JTextField(30);
        
        formaPagamentoCombo = new JComboBox<>(new String[]{
            "DINHEIRO", "CHEQUE", "TRANSFERENCIA", "PIX", "BOLETO", "CARTAO"
        });
        
        numeroDocumentoField = new JTextField(15);
        
        statusCombo = new JComboBox<>(new String[]{
            "PAGO", "PENDENTE", "CANCELADO"
        });
        
        observacoesArea = new JTextArea(3, 40);
        observacoesArea.setLineWrap(true);
        observacoesArea.setWrapStyleWord(true);
        
        // Botões
        salvarButton = new JButton("Salvar");
        novoButton = new JButton("Novo");
        editarButton = new JButton("Editar");
        excluirButton = new JButton("Excluir");
        limparButton = new JButton("Limpar");
        pesquisarButton = new JButton("Pesquisar");
        relatorioButton = new JButton("Relatório");
        pesquisarField = new JTextField(20);
        
        // Labels de resumo
        saldoLabel = new JLabel("R$ 0,00");
        receitasLabel = new JLabel("R$ 0,00");
        despesasLabel = new JLabel("R$ 0,00");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 245));
        
        // Header com título
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(46, 125, 50));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("💰 Gestão Financeira (Caixa)", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Controle de receitas, despesas e movimentações financeiras", SwingConstants.LEFT);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(220, 220, 230));
        
        // Adicionar logo maçônico discreto
        JLabel logoLabel = MasonicLogo.createLogoLabel(32);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        JPanel titleContainer = new JPanel(new GridLayout(2, 1, 0, 5));
        titleContainer.setBackground(new Color(46, 125, 50));
        titleContainer.add(titleLabel);
        titleContainer.add(subtitleLabel);
        
        JPanel headerContent = new JPanel(new BorderLayout());
        headerContent.setBackground(new Color(46, 125, 50));
        headerContent.add(logoLabel, BorderLayout.WEST);
        headerContent.add(titleContainer, BorderLayout.CENTER);
        
        headerPanel.add(headerContent, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);
        
        // Painel principal com split
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(650);
        splitPane.setResizeWeight(0.6);
        
        // Painel esquerdo - Tabela e resumo
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Painel de pesquisa melhorado
        JPanel pesquisaPanel = new JPanel(new BorderLayout());
        pesquisaPanel.setBackground(new Color(245, 245, 250));
        pesquisaPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 210)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        JPanel searchContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchContainer.setBackground(new Color(245, 245, 250));
        
        JLabel searchLabel = new JLabel("🔍 Pesquisar:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchLabel.setForeground(new Color(100, 100, 120));
        
        pesquisarField.setPreferredSize(new Dimension(200, 30));
        pesquisarField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        
        pesquisarButton.setBackground(new Color(46, 125, 50));
        pesquisarButton.setForeground(Color.WHITE);
        pesquisarButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pesquisarButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        pesquisarButton.setFocusPainted(false);
        
        searchContainer.add(searchLabel);
        searchContainer.add(pesquisarField);
        searchContainer.add(pesquisarButton);
        pesquisaPanel.add(searchContainer, BorderLayout.CENTER);
        
        // Painel de resumo financeiro melhorado
        JPanel resumoPanel = new JPanel(new GridLayout(1, 3, 15, 10));
        resumoPanel.setBackground(new Color(245, 245, 250));
        resumoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 210)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        // Painel de receitas
        JPanel receitasPanel = new JPanel(new BorderLayout());
        receitasPanel.setBackground(new Color(220, 255, 220));
        receitasPanel.setBorder(BorderFactory.createLineBorder(new Color(46, 125, 50), 2));
        JLabel receitasTitleLabel = new JLabel("📈 RECEITAS", SwingConstants.CENTER);
        receitasTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        receitasTitleLabel.setForeground(new Color(46, 125, 50));
        receitasLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        receitasLabel.setForeground(new Color(46, 125, 50));
        receitasPanel.add(receitasTitleLabel, BorderLayout.NORTH);
        receitasPanel.add(receitasLabel, BorderLayout.CENTER);
        
        // Painel de despesas
        JPanel despesasPanel = new JPanel(new BorderLayout());
        despesasPanel.setBackground(new Color(255, 220, 220));
        despesasPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 53, 69), 2));
        JLabel despesasTitleLabel = new JLabel("📉 DESPESAS", SwingConstants.CENTER);
        despesasTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        despesasTitleLabel.setForeground(new Color(220, 53, 69));
        despesasLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        despesasLabel.setForeground(new Color(220, 53, 69));
        despesasPanel.add(despesasTitleLabel, BorderLayout.NORTH);
        despesasPanel.add(despesasLabel, BorderLayout.CENTER);
        
        // Painel de saldo
        JPanel saldoPanel = new JPanel(new BorderLayout());
        saldoPanel.setBackground(new Color(230, 240, 255));
        saldoPanel.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        JLabel saldoTitleLabel = new JLabel("💼 SALDO", SwingConstants.CENTER);
        saldoTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        saldoTitleLabel.setForeground(new Color(70, 130, 180));
        saldoLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        saldoLabel.setForeground(new Color(70, 130, 180));
        saldoPanel.add(saldoTitleLabel, BorderLayout.NORTH);
        saldoPanel.add(saldoLabel, BorderLayout.CENTER);
        
        resumoPanel.add(receitasPanel);
        resumoPanel.add(despesasPanel);
        resumoPanel.add(saldoPanel);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(pesquisaPanel, BorderLayout.NORTH);
        topPanel.add(resumoPanel, BorderLayout.CENTER);
        
        leftPanel.add(topPanel, BorderLayout.NORTH);
        
        // Tabela estilizada
        JScrollPane tableScrollPane = new JScrollPane(caixaTable);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 210)));
        tableScrollPane.getViewport().setBackground(Color.WHITE);
        
        caixaTable.setRowHeight(25);
        caixaTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        caixaTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        caixaTable.getTableHeader().setBackground(new Color(46, 125, 50));
        caixaTable.getTableHeader().setForeground(Color.WHITE);
        caixaTable.setSelectionBackground(new Color(144, 238, 144));
        caixaTable.setSelectionForeground(Color.BLACK);
        
        leftPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Painel direito - Formulário
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        
        // Título do formulário
        JPanel formHeaderPanel = new JPanel(new BorderLayout());
        formHeaderPanel.setBackground(new Color(245, 245, 250));
        formHeaderPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel formTitleLabel = new JLabel("💳 Detalhes da Movimentação", SwingConstants.LEFT);
        formTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitleLabel.setForeground(new Color(46, 125, 50));
        
        formHeaderPanel.add(formTitleLabel, BorderLayout.CENTER);
        rightPanel.add(formHeaderPanel, BorderLayout.NORTH);
        
        // Formulário em grid
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Estilizar labels
        Font labelFont = new Font("Segoe UI", Font.BOLD, 12);
        Color labelColor = new Color(46, 125, 50);
        
        // Código
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        JLabel codigoLabel = new JLabel("🔢 Código:");
        codigoLabel.setFont(labelFont);
        codigoLabel.setForeground(labelColor);
        formPanel.add(codigoLabel, gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        codigoField.setEditable(false);
        codigoField.setBackground(new Color(240, 240, 245));
        codigoField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(codigoField, gbc);
        
        // Tipo
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel tipoLabel = new JLabel("💳 Tipo:");
        tipoLabel.setFont(labelFont);
        tipoLabel.setForeground(labelColor);
        formPanel.add(tipoLabel, gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        tipoCombo.setBackground(Color.WHITE);
        tipoCombo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(tipoCombo, gbc);
        
        // Categoria
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        JLabel categoriaLabel = new JLabel("📂 Categoria:");
        categoriaLabel.setFont(labelFont);
        categoriaLabel.setForeground(labelColor);
        formPanel.add(categoriaLabel, gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        categoriaField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(categoriaField, gbc);
        
        // Descrição
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        JLabel descricaoLabel = new JLabel("📝 Descrição:");
        descricaoLabel.setFont(labelFont);
        descricaoLabel.setForeground(labelColor);
        formPanel.add(descricaoLabel, gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        descricaoField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(descricaoField, gbc);
        
        // Valor
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1;
        JLabel valorLabel = new JLabel("💰 Valor (R$):");
        valorLabel.setFont(labelFont);
        valorLabel.setForeground(labelColor);
        formPanel.add(valorLabel, gbc);
        gbc.gridx = 1;
        valorField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(valorField, gbc);
        
        // Data Movimentação
        gbc.gridx = 2; gbc.gridy = 4;
        JLabel dataLabel = new JLabel("📅 Data:");
        dataLabel.setFont(labelFont);
        dataLabel.setForeground(labelColor);
        formPanel.add(dataLabel, gbc);
        gbc.gridx = 3;
        dataMovimentacaoField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(dataMovimentacaoField, gbc);
        
        // Responsável
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1;
        JLabel responsavelLabel = new JLabel("👤 Responsável:");
        responsavelLabel.setFont(labelFont);
        responsavelLabel.setForeground(labelColor);
        formPanel.add(responsavelLabel, gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        responsavelField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(responsavelField, gbc);
        
        // Forma de Pagamento
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 1;
        JLabel formaPagamentoLabel = new JLabel("💳 Forma Pagto:");
        formaPagamentoLabel.setFont(labelFont);
        formaPagamentoLabel.setForeground(labelColor);
        formPanel.add(formaPagamentoLabel, gbc);
        gbc.gridx = 1;
        formaPagamentoCombo.setBackground(Color.WHITE);
        formaPagamentoCombo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(formaPagamentoCombo, gbc);
        
        // Número Documento
        gbc.gridx = 2; gbc.gridy = 6; gbc.gridwidth = 1;
        JLabel documentoLabel = new JLabel("📄 Nº Doc:");
        documentoLabel.setFont(labelFont);
        documentoLabel.setForeground(labelColor);
        formPanel.add(documentoLabel, gbc);
        gbc.gridx = 3;
        numeroDocumentoField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(numeroDocumentoField, gbc);
        
        // Status
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 1;
        JLabel statusLabel = new JLabel("✅ Status:");
        statusLabel.setFont(labelFont);
        statusLabel.setForeground(labelColor);
        formPanel.add(statusLabel, gbc);
        gbc.gridx = 1;
        statusCombo.setBackground(Color.WHITE);
        statusCombo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(statusCombo, gbc);
        
        // Observações
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 1;
        JLabel observacoesLabel = new JLabel("📝 Observações:");
        observacoesLabel.setFont(labelFont);
        observacoesLabel.setForeground(labelColor);
        formPanel.add(observacoesLabel, gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.gridy = 9;
        gbc.fill = GridBagConstraints.BOTH;
        observacoesArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        JScrollPane observacoesScroll = new JScrollPane(observacoesArea);
        observacoesScroll.setPreferredSize(new Dimension(300, 60));
        formPanel.add(observacoesScroll, gbc);
        
        // Botões do formulário estilizados
        gbc.gridx = 0; gbc.gridy = 10; gbc.gridwidth = 4; gbc.fill = GridBagConstraints.HORIZONTAL;
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
        
        relatorioButton.setBackground(new Color(230, 230, 250)); // Lavanda pastel suave
        relatorioButton.setForeground(new Color(72, 61, 139));
        relatorioButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        relatorioButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 250), 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        relatorioButton.setFocusPainted(false);
        relatorioButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        botoesFormPanel.add(salvarButton);
        botoesFormPanel.add(novoButton);
        botoesFormPanel.add(editarButton);
        botoesFormPanel.add(excluirButton);
        botoesFormPanel.add(limparButton);
        botoesFormPanel.add(relatorioButton);
        
        formPanel.add(botoesFormPanel, gbc);
        
        rightPanel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        
        // Configurar split pane
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        splitPane.setDividerLocation(650);
        splitPane.setResizeWeight(0.6);
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private void setupEvents() {
        salvarButton.addActionListener(e -> salvarMovimentacao());
        novoButton.addActionListener(e -> limparFormulario());
        editarButton.addActionListener(e -> carregarMovimentacaoSelecionada());
        excluirButton.addActionListener(e -> excluirMovimentacao());
        limparButton.addActionListener(e -> limparFormulario());
        pesquisarButton.addActionListener(e -> pesquisarMovimentacoes());
        relatorioButton.addActionListener(e -> gerarRelatorio());
        
        // Seleção na tabela
        caixaTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && caixaTable.getSelectedRow() >= 0) {
                carregarMovimentacaoSelecionada();
            }
        });
        
        // Duplo clique para editar
        caixaTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    carregarMovimentacaoSelecionada();
                }
            }
        });
        
        // Mudança de tipo para atualizar categoria
        tipoCombo.addActionListener(e -> sugerirCategoria());
    }
    
    private void sugerirCategoria() {
        String tipo = (String) tipoCombo.getSelectedItem();
        if ("RECEITA".equals(tipo)) {
            categoriaField.setText("ANUIDADE");
        } else if ("DESPESA".equals(tipo)) {
            categoriaField.setText("MATERIAL");
        }
    }
    
    private void salvarMovimentacao() {
        try {
            if (descricaoField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Descrição é obrigatória!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (valorField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Valor é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Caixa caixa = caixaAtual != null ? caixaAtual : new Caixa();
            caixa.setTipo((String) tipoCombo.getSelectedItem());
            caixa.setCategoria(categoriaField.getText().trim());
            caixa.setDescricao(descricaoField.getText().trim());
            
            // Parse valor
            try {
                String valorStr = valorField.getText().trim().replace("R$", "").replace(",", ".");
                caixa.setValor(new BigDecimal(valorStr));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Parse data
            if (!dataMovimentacaoField.getText().trim().isEmpty()) {
                try {
                    String dataStr = dataMovimentacaoField.getText().trim();
                    caixa.setDataMovimentacao(LocalDateTime.parse(dataStr, DATE_FORMATTER));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Data inválida! Use formato dd/MM/yyyy HH:mm", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                caixa.setDataMovimentacao(LocalDateTime.now());
            }
            
            caixa.setResponsavel(responsavelField.getText().trim());
            caixa.setFormaPagamento((String) formaPagamentoCombo.getSelectedItem());
            caixa.setNumeroDocumento(numeroDocumentoField.getText().trim());
            caixa.setStatus((String) statusCombo.getSelectedItem());
            caixa.setObservacoes(observacoesArea.getText().trim());
            
            caixaDAO.save(caixa);
            
            JOptionPane.showMessageDialog(this, "Movimentação salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            refreshData();
            atualizarResumoFinanceiro();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar movimentação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limparFormulario() {
        caixaAtual = null;
        codigoField.setText("");
        tipoCombo.setSelectedIndex(0);
        sugerirCategoria();
        descricaoField.setText("");
        valorField.setText("");
        dataMovimentacaoField.setText("");
        responsavelField.setText("");
        formaPagamentoCombo.setSelectedIndex(0);
        numeroDocumentoField.setText("");
        statusCombo.setSelectedIndex(0);
        observacoesArea.setText("");
        descricaoField.requestFocus();
        atualizarBotoesAcao();
    }
    
    private void excluirMovimentacao() {
        if (caixaAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma movimentação para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int option = JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente excluir a movimentação " + caixaAtual.getDescricao() + " (R$ " + caixaAtual.getValor() + ")?",
            "Confirmar Exclusão",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (option == JOptionPane.YES_OPTION) {
            try {
                caixaDAO.delete(caixaAtual.getId());
                JOptionPane.showMessageDialog(this, "Movimentação excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                refreshData();
                atualizarResumoFinanceiro();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir movimentação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void pesquisarMovimentacoes() {
        try {
            String termo = pesquisarField.getText().trim();
            if (termo.isEmpty()) {
                refreshData();
                return;
            }
            
            List<Caixa> movimentacoes = caixaDAO.findAll();
            tableModel.setRowCount(0);
            
            for (Caixa caixa : movimentacoes) {
                if (caixa.getDescricao().toLowerCase().contains(termo.toLowerCase()) ||
                    caixa.getCategoria().toLowerCase().contains(termo.toLowerCase()) ||
                    caixa.getResponsavel().toLowerCase().contains(termo.toLowerCase())) {
                    Object[] row = {
                        caixa.getId(),
                        caixa.getTipo(),
                        caixa.getDescricao(),
                        "R$ " + caixa.getValor(),
                        caixa.getDataMovimentacao() != null ? caixa.getDataMovimentacao().format(DATE_FORMATTER) : "",
                        caixa.getStatus()
                    };
                    tableModel.addRow(row);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao pesquisar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void carregarMovimentacaoSelecionada() {
        int selectedRow = caixaTable.getSelectedRow();
        if (selectedRow >= 0) {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            try {
                caixaAtual = caixaDAO.findById(id);
                if (caixaAtual != null) {
                    codigoField.setText(String.valueOf(caixaAtual.getId()));
                    tipoCombo.setSelectedItem(caixaAtual.getTipo());
                    categoriaField.setText(caixaAtual.getCategoria());
                    descricaoField.setText(caixaAtual.getDescricao());
                    valorField.setText(caixaAtual.getValor().toString());
                    dataMovimentacaoField.setText(caixaAtual.getDataMovimentacao() != null ? caixaAtual.getDataMovimentacao().format(DATE_FORMATTER) : "");
                    responsavelField.setText(caixaAtual.getResponsavel());
                    formaPagamentoCombo.setSelectedItem(caixaAtual.getFormaPagamento());
                    numeroDocumentoField.setText(caixaAtual.getNumeroDocumento());
                    statusCombo.setSelectedItem(caixaAtual.getStatus());
                    observacoesArea.setText(caixaAtual.getObservacoes());
                    atualizarBotoesAcao();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar movimentação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void atualizarBotoesAcao() {
        boolean temCaixa = caixaAtual != null;
        editarButton.setEnabled(temCaixa);
        excluirButton.setEnabled(temCaixa);
    }
    
    private void atualizarResumoFinanceiro() {
        try {
            BigDecimal saldo = caixaDAO.getSaldo();
            BigDecimal receitas = caixaDAO.getTotalReceitas();
            BigDecimal despesas = caixaDAO.getTotalDespesas();
            
            saldoLabel.setText("R$ " + String.format("%,.2f", saldo));
            receitasLabel.setText("R$ " + String.format("%,.2f", receitas));
            despesasLabel.setText("R$ " + String.format("%,.2f", despesas));
            
            // Cores
            receitasLabel.setForeground(new Color(0, 128, 0)); // Verde
            despesasLabel.setForeground(new Color(255, 0, 0)); // Vermelho
            saldoLabel.setForeground(saldo.compareTo(BigDecimal.ZERO) >= 0 ? new Color(0, 128, 0) : new Color(255, 0, 0));
            
        } catch (Exception ex) {
            saldoLabel.setText("Erro");
            receitasLabel.setText("Erro");
            despesasLabel.setText("Erro");
        }
    }
    
    private void gerarRelatorio() {
        try {
            StringBuilder relatorio = new StringBuilder();
            relatorio.append("=== RELATÓRIO FINANCEIRO ===\n\n");
            
            BigDecimal receitas = caixaDAO.getTotalReceitas();
            BigDecimal despesas = caixaDAO.getTotalDespesas();
            BigDecimal saldo = caixaDAO.getSaldo();
            
            relatorio.append("RESUMO:\n");
            relatorio.append("Receitas: R$ ").append(String.format("%,.2f", receitas)).append("\n");
            relatorio.append("Despesas: R$ ").append(String.format("%,.2f", despesas)).append("\n");
            relatorio.append("Saldo: R$ ").append(String.format("%,.2f", saldo)).append("\n\n");
            
            relatorio.append("MOVIMENTAÇÕES:\n");
            relatorio.append("--------------------------------------------------\n");
            
            List<Caixa> movimentacoes = caixaDAO.findAll();
            for (Caixa caixa : movimentacoes) {
                relatorio.append(caixa.getTipo()).append(" - ");
                relatorio.append(caixa.getDescricao()).append("\n");
                relatorio.append("Valor: R$ ").append(String.format("%,.2f", caixa.getValor())).append("\n");
                relatorio.append("Data: ").append(caixa.getDataMovimentacao() != null ? caixa.getDataMovimentacao().format(DATE_FORMATTER) : "").append("\n");
                relatorio.append("Status: ").append(caixa.getStatus()).append("\n");
                relatorio.append("--------------------------------------------------\n");
            }
            
            JTextArea textArea = new JTextArea(relatorio.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Relatório Financeiro", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar relatório: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void refreshData() {
        logger.info("Iniciando refreshData() no CaixaPanel");
        try {
            logger.debug("Chamando caixaDAO.findAll()");
            List<Caixa> movimentacoes = caixaDAO.findAll();
            logger.info("Recebidas {} movimentações do DAO", movimentacoes.size());
            
            tableModel.setRowCount(0);
            logger.debug("TableModel limpo, começando a adicionar linhas");
            
            int linha = 0;
            for (Caixa caixa : movimentacoes) {
                linha++;
                Object[] row = {
                    caixa.getId(),
                    caixa.getTipo(),
                    caixa.getDescricao(),
                    caixa.getValor() != null ? "R$ " + caixa.getValor() : "R$ 0,00",
                    caixa.getDataMovimentacao() != null ? caixa.getDataMovimentacao().format(DATE_FORMATTER) : "",
                    caixa.getObservacoes() != null ? caixa.getObservacoes() : ""
                };
                tableModel.addRow(row);
                logger.debug("Linha {} adicionada: ID={}, Tipo={}, Valor={}", 
                            linha, caixa.getId(), caixa.getTipo(), caixa.getValor());
            }
            logger.info("refreshData() concluído com sucesso: {} linhas adicionadas", linha);
        } catch (SQLException ex) {
            logger.error("Erro ao carregar movimentações no painel: {}", ex.getMessage(), ex);
            JOptionPane.showMessageDialog(this, "Erro ao carregar movimentações: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            logger.error("Erro inesperado ao carregar movimentações: {}", ex.getMessage(), ex);
            JOptionPane.showMessageDialog(this, "Erro inesperado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
