package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.SessaoDAO;
import com.artereal.swing.model.Sessao;
import com.artereal.swing.ui.components.MasonicLogo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Painel de gestão de Sessões Maçônicas
 */
public class SessoesPanel extends JPanel {
    
    private static final Logger logger = LoggerFactory.getLogger(SessoesPanel.class);
    private SessaoDAO sessaoDAO;
    private DefaultTableModel tableModel;
    private JTable sessoesTable;
    private Sessao sessaoAtual;
    
    // Formulário
    private JTextField codigoField;
    private JTextField tipoField;
    private JTextField dataHoraField;
    private JTextField localField;
    private JTextField presidenteField;
    private JTextField secretarioField;
    private JTextField tesoureiroField;
    private JTextField oradorField;
    private JTextField temaField;
    private JTextArea pautaArea;
    private JTextArea observacoesArea;
    private JTextField statusField;
    private JTextField presentesField;
    private JTextField visitantesField;
    
    // Botões
    private JButton salvarButton;
    private JButton novoButton;
    private JButton editarButton;
    private JButton excluirButton;
    private JButton limparButton;
    private JButton pesquisarButton;
    private JTextField pesquisarField;
    
    // Combos para seleção
    private JComboBox<String> tipoCombo;
    private JComboBox<String> statusCombo;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    public SessoesPanel() {
        sessaoDAO = new SessaoDAO();
        initializeComponents();
        setupLayout();
        setupEvents();
        refreshData();
    }
    
    private void initializeComponents() {
        // Tabela
        tableModel = new DefaultTableModel(new Object[]{
            "Código", "Tipo", "Data/Hora", "Local", "Status", "Presentes"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        sessoesTable = new JTable(tableModel);
        
        // Formulário
        codigoField = new JTextField(10);
        codigoField.setEditable(false);
        
        tipoCombo = new JComboBox<>(new String[]{
            "MAGNA", "BRANCA", "ELEICAO", "INSTRUCAO", "ADMINISTRATIVA", "FESTIVA"
        });
        
        dataHoraField = new JTextField(16);
        localField = new JTextField(30);
        presidenteField = new JTextField(25);
        secretarioField = new JTextField(25);
        tesoureiroField = new JTextField(25);
        oradorField = new JTextField(25);
        temaField = new JTextField(40);
        
        pautaArea = new JTextArea(4, 40);
        pautaArea.setLineWrap(true);
        pautaArea.setWrapStyleWord(true);
        
        observacoesArea = new JTextArea(3, 40);
        observacoesArea.setLineWrap(true);
        observacoesArea.setWrapStyleWord(true);
        
        statusCombo = new JComboBox<>(new String[]{
            "PROGRAMADA", "REALIZADA", "CANCELADA", "ADIADA"
        });
        
        presentesField = new JTextField(5);
        visitantesField = new JTextField(5);
        
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
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("🏛️ Gestão de Sessões Maçônicas", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Controle de sessões, reuniões e eventos maçônicos", SwingConstants.LEFT);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(220, 220, 230));
        
        // Adicionar logo maçônico discreto
        JLabel logoLabel = MasonicLogo.createLogoLabel(32);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        JPanel titleContainer = new JPanel(new GridLayout(2, 1, 0, 5));
        titleContainer.setBackground(new Color(70, 130, 180));
        titleContainer.add(titleLabel);
        titleContainer.add(subtitleLabel);
        
        JPanel headerContent = new JPanel(new BorderLayout());
        headerContent.setBackground(new Color(70, 130, 180));
        headerContent.add(logoLabel, BorderLayout.WEST);
        headerContent.add(titleContainer, BorderLayout.CENTER);
        
        headerPanel.add(headerContent, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);
        
        // Painel principal com split
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(600);
        splitPane.setResizeWeight(0.6);
        
        // Painel esquerdo - Tabela
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
        
        pesquisarButton.setBackground(new Color(70, 130, 180));
        pesquisarButton.setForeground(Color.WHITE);
        pesquisarButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pesquisarButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        pesquisarButton.setFocusPainted(false);
        
        searchContainer.add(searchLabel);
        searchContainer.add(pesquisarField);
        searchContainer.add(pesquisarButton);
        pesquisaPanel.add(searchContainer, BorderLayout.CENTER);
        
        leftPanel.add(pesquisaPanel, BorderLayout.NORTH);
        
        // Tabela estilizada
        JScrollPane tableScrollPane = new JScrollPane(sessoesTable);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 210)));
        tableScrollPane.getViewport().setBackground(Color.WHITE);
        
        sessoesTable.setRowHeight(25);
        sessoesTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sessoesTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        sessoesTable.getTableHeader().setBackground(new Color(70, 130, 180));
        sessoesTable.getTableHeader().setForeground(Color.WHITE);
        sessoesTable.setSelectionBackground(new Color(135, 206, 250));
        sessoesTable.setSelectionForeground(Color.WHITE);
        
        leftPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Painel direito - Formulário
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        
        // Título do formulário
        JPanel formHeaderPanel = new JPanel(new BorderLayout());
        formHeaderPanel.setBackground(new Color(245, 245, 250));
        formHeaderPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel formTitleLabel = new JLabel("📝 Detalhes da Sessão", SwingConstants.LEFT);
        formTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitleLabel.setForeground(new Color(70, 130, 180));
        
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
        Color labelColor = new Color(70, 130, 180);
        
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
        JLabel tipoLabel = new JLabel("🏛️ Tipo:");
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
        
        // Data/Hora
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        JLabel dataLabel = new JLabel("📅 Data/Hora:");
        dataLabel.setFont(labelFont);
        dataLabel.setForeground(labelColor);
        formPanel.add(dataLabel, gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        dataHoraField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(dataHoraField, gbc);
        
        // Local
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        JLabel localLabel = new JLabel("📍 Local:");
        localLabel.setFont(labelFont);
        localLabel.setForeground(labelColor);
        formPanel.add(localLabel, gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        localField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(localField, gbc);
        
        // Presidente
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1;
        JLabel presidenteLabel = new JLabel("👑 Presidente:");
        presidenteLabel.setFont(labelFont);
        presidenteLabel.setForeground(labelColor);
        formPanel.add(presidenteLabel, gbc);
        gbc.gridx = 1; gbc.gridwidth = 1;
        presidenteField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(presidenteField, gbc);
        
        // Secretário
        gbc.gridx = 2; gbc.gridy = 4; gbc.gridwidth = 1;
        JLabel secretarioLabel = new JLabel("📝 Secretário:");
        secretarioLabel.setFont(labelFont);
        secretarioLabel.setForeground(labelColor);
        formPanel.add(secretarioLabel, gbc);
        gbc.gridx = 3;
        secretarioField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(secretarioField, gbc);
        
        // Tesoureiro
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1;
        JLabel tesoureiroLabel = new JLabel("💰 Tesoureiro:");
        tesoureiroLabel.setFont(labelFont);
        tesoureiroLabel.setForeground(labelColor);
        formPanel.add(tesoureiroLabel, gbc);
        gbc.gridx = 1;
        tesoureiroField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(tesoureiroField, gbc);
        
        // Orador
        gbc.gridx = 2; gbc.gridy = 5; gbc.gridwidth = 1;
        JLabel oradorLabel = new JLabel("🎤 Orador:");
        oradorLabel.setFont(labelFont);
        oradorLabel.setForeground(labelColor);
        formPanel.add(oradorLabel, gbc);
        gbc.gridx = 3;
        oradorField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(oradorField, gbc);
        
        // Tema
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 1;
        JLabel temaLabel = new JLabel("🎯 Tema:");
        temaLabel.setFont(labelFont);
        temaLabel.setForeground(labelColor);
        formPanel.add(temaLabel, gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        temaField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(temaField, gbc);
        
        // Pauta
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 1;
        JLabel pautaLabel = new JLabel("📋 Pauta:");
        pautaLabel.setFont(labelFont);
        pautaLabel.setForeground(labelColor);
        formPanel.add(pautaLabel, gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.gridy = 8;
        gbc.fill = GridBagConstraints.BOTH;
        pautaArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        JScrollPane pautaScroll = new JScrollPane(pautaArea);
        pautaScroll.setPreferredSize(new Dimension(300, 80));
        formPanel.add(pautaScroll, gbc);
        
        // Status
        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
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
        
        // Presentes
        gbc.gridx = 2;
        JLabel presentesLabel = new JLabel("👥 Presentes:");
        presentesLabel.setFont(labelFont);
        presentesLabel.setForeground(labelColor);
        formPanel.add(presentesLabel, gbc);
        gbc.gridx = 3;
        presentesField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(presentesField, gbc);
        
        // Visitantes
        gbc.gridx = 0; gbc.gridy = 10;
        JLabel visitantesLabel = new JLabel("👤 Visitantes:");
        visitantesLabel.setFont(labelFont);
        visitantesLabel.setForeground(labelColor);
        formPanel.add(visitantesLabel, gbc);
        gbc.gridx = 1;
        visitantesField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(visitantesField, gbc);
        
        // Observações
        gbc.gridx = 0; gbc.gridy = 11; gbc.gridwidth = 1;
        JLabel observacoesLabel = new JLabel("📝 Observações:");
        observacoesLabel.setFont(labelFont);
        observacoesLabel.setForeground(labelColor);
        formPanel.add(observacoesLabel, gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.gridy = 12;
        gbc.fill = GridBagConstraints.BOTH;
        observacoesArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        JScrollPane observacoesScroll = new JScrollPane(observacoesArea);
        observacoesScroll.setPreferredSize(new Dimension(300, 60));
        formPanel.add(observacoesScroll, gbc);
        
        // Botões do formulário estilizados
        gbc.gridx = 0; gbc.gridy = 13; gbc.gridwidth = 4; gbc.fill = GridBagConstraints.HORIZONTAL;
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
        salvarButton.addActionListener(e -> salvarSessao());
        novoButton.addActionListener(e -> limparFormulario());
        editarButton.addActionListener(e -> carregarSessaoSelecionada());
        excluirButton.addActionListener(e -> excluirSessao());
        limparButton.addActionListener(e -> limparFormulario());
        pesquisarButton.addActionListener(e -> pesquisarSessoes());
        
        // Seleção na tabela
        sessoesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && sessoesTable.getSelectedRow() >= 0) {
                carregarSessaoSelecionada();
            }
        });
        
        // Duplo clique para editar
        sessoesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    carregarSessaoSelecionada();
                }
            }
        });
    }
    
    private void salvarSessao() {
        try {
            if (dataHoraField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Data/Hora é obrigatória!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Sessao sessao = sessaoAtual != null ? sessaoAtual : new Sessao();
            sessao.setTipo((String) tipoCombo.getSelectedItem());
            
            // Parse data/hora
            try {
                String dataHoraStr = dataHoraField.getText().trim();
                sessao.setDataHora(LocalDateTime.parse(dataHoraStr, DATE_FORMATTER));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Data/Hora inválida! Use formato dd/MM/yyyy HH:mm", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            sessao.setLocal(localField.getText().trim());
            sessao.setPresidente(presidenteField.getText().trim());
            sessao.setSecretario(secretarioField.getText().trim());
            sessao.setTesoureiro(tesoureiroField.getText().trim());
            sessao.setOrador(oradorField.getText().trim());
            sessao.setTema(temaField.getText().trim());
            sessao.setPauta(pautaArea.getText().trim());
            sessao.setObservacoes(observacoesArea.getText().trim());
            sessao.setStatus((String) statusCombo.getSelectedItem());
            
            try {
                sessao.setQuantidadePresentes(Integer.parseInt(presentesField.getText().trim()));
            } catch (NumberFormatException e) {
                sessao.setQuantidadePresentes(0);
            }
            
            try {
                sessao.setQuantidadeVisitantes(Integer.parseInt(visitantesField.getText().trim()));
            } catch (NumberFormatException e) {
                sessao.setQuantidadeVisitantes(0);
            }
            
            sessaoDAO.save(sessao);
            
            JOptionPane.showMessageDialog(this, "Sessão salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            refreshData();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar sessão: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limparFormulario() {
        sessaoAtual = null;
        codigoField.setText("");
        tipoCombo.setSelectedIndex(0);
        dataHoraField.setText("");
        localField.setText("");
        presidenteField.setText("");
        secretarioField.setText("");
        tesoureiroField.setText("");
        oradorField.setText("");
        temaField.setText("");
        pautaArea.setText("");
        observacoesArea.setText("");
        statusCombo.setSelectedIndex(0);
        presentesField.setText("0");
        visitantesField.setText("0");
        dataHoraField.requestFocus();
        atualizarBotoesAcao();
    }
    
    private void excluirSessao() {
        if (sessaoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma sessão para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int option = JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente excluir a sessão " + sessaoAtual.getTipo() + " de " + sessaoAtual.getDataHora() + "?",
            "Confirmar Exclusão",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (option == JOptionPane.YES_OPTION) {
            try {
                sessaoDAO.delete(sessaoAtual.getId());
                JOptionPane.showMessageDialog(this, "Sessão excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                refreshData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir sessão: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void pesquisarSessoes() {
        try {
            String termo = pesquisarField.getText().trim();
            if (termo.isEmpty()) {
                refreshData();
                return;
            }
            
            List<Sessao> sessoes = sessaoDAO.findAll();
            tableModel.setRowCount(0);
            
            for (Sessao sessao : sessoes) {
                if (sessao.getTipo().toLowerCase().contains(termo.toLowerCase()) ||
                    sessao.getLocal().toLowerCase().contains(termo.toLowerCase()) ||
                    sessao.getTema().toLowerCase().contains(termo.toLowerCase())) {
                    Object[] row = {
                        sessao.getId(),
                        sessao.getTipo(),
                        sessao.getDataHora() != null ? sessao.getDataHora().format(DATE_FORMATTER) : "",
                        sessao.getLocal(),
                        sessao.getStatus(),
                        sessao.getQuantidadePresentes()
                    };
                    tableModel.addRow(row);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao pesquisar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void carregarSessaoSelecionada() {
        int selectedRow = sessoesTable.getSelectedRow();
        if (selectedRow >= 0) {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            try {
                sessaoAtual = sessaoDAO.findById(id);
                if (sessaoAtual != null) {
                    codigoField.setText(String.valueOf(sessaoAtual.getId()));
                    tipoCombo.setSelectedItem(sessaoAtual.getTipo());
                    dataHoraField.setText(sessaoAtual.getDataHora() != null ? sessaoAtual.getDataHora().format(DATE_FORMATTER) : "");
                    localField.setText(sessaoAtual.getLocal());
                    presidenteField.setText(sessaoAtual.getPresidente());
                    secretarioField.setText(sessaoAtual.getSecretario());
                    tesoureiroField.setText(sessaoAtual.getTesoureiro());
                    oradorField.setText(sessaoAtual.getOrador());
                    temaField.setText(sessaoAtual.getTema());
                    pautaArea.setText(sessaoAtual.getPauta());
                    observacoesArea.setText(sessaoAtual.getObservacoes());
                    statusCombo.setSelectedItem(sessaoAtual.getStatus());
                    presentesField.setText(String.valueOf(sessaoAtual.getQuantidadePresentes()));
                    visitantesField.setText(String.valueOf(sessaoAtual.getQuantidadeVisitantes()));
                    atualizarBotoesAcao();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar sessão: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void atualizarBotoesAcao() {
        boolean temSessao = sessaoAtual != null;
        editarButton.setEnabled(temSessao);
        excluirButton.setEnabled(temSessao);
    }
    
    public void refreshData() {
        logger.info("Iniciando refreshData() no SessoesPanel");
        try {
            logger.debug("Chamando sessaoDAO.findAll()");
            List<Sessao> sessoes = sessaoDAO.findAll();
            logger.info("Recebidas {} sessões do DAO", sessoes.size());
            
            tableModel.setRowCount(0);
            logger.debug("TableModel limpo, começando a adicionar linhas");
            
            int linha = 0;
            for (Sessao sessao : sessoes) {
                linha++;
                Object[] row = {
                    sessao.getId(),
                    sessao.getTipo(),
                    sessao.getDataHora() != null ? sessao.getDataHora().format(DATE_FORMATTER) : "",
                    sessao.getPauta() != null ? sessao.getPauta() : "",
                    sessao.getQuantidadePresentes(),
                    sessao.getObservacoes() != null ? sessao.getObservacoes() : ""
                };
                tableModel.addRow(row);
                logger.debug("Linha {} adicionada: ID={}, Tipo={}", linha, sessao.getId(), sessao.getTipo());
            }
            logger.info("refreshData() concluído com sucesso: {} linhas adicionadas", linha);
        } catch (SQLException ex) {
            logger.error("Erro ao carregar sessões no painel: {}", ex.getMessage(), ex);
            JOptionPane.showMessageDialog(this, "Erro ao carregar sessões: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            logger.error("Erro inesperado ao carregar sessões: {}", ex.getMessage(), ex);
            JOptionPane.showMessageDialog(this, "Erro inesperado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
