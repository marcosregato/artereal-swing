package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.LojaDAO;
import com.artereal.swing.model.Loja;
import com.artereal.swing.ui.components.MasonicLogo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;

/**
 * Painel de gestão de Lojas
 */
public class LojasPanel extends JPanel {
    
    private LojaDAO lojaDAO;
    private DefaultTableModel tableModel;
    private JTable lojasTable;
    private Loja lojaAtual;
    
    // Formulário
    private JTextField codigoField;
    private JTextField nomeField;
    private JTextField numeroField;
    private JTextField enderecoField;
    private JTextField bairroField;
    private JTextField cidadeField;
    private JTextField estadoField;
    private JTextField cepField;
    private JTextField telefoneField;
    private JTextField emailField;
    private JTextField presidenteField;
    private JTextField secretarioField;
    private JTextField tesoureiroField;
    private JTextField dataFundacaoField;
    private JTextField ritoField;
    private JTextField potenciaField;
    private JTextArea observacoesArea;
    private JComboBox<String> statusCombo;
    
    // Botões
    private JButton salvarButton;
    private JButton novoButton;
    private JButton editarButton;
    private JButton excluirButton;
    private JButton limparButton;
    private JButton pesquisarButton;
    private JTextField pesquisarField;
    
    public LojasPanel() {
        lojaDAO = new LojaDAO();
        initializeComponents();
        setupLayout();
        setupEvents();
        refreshData();
    }
    
    private void initializeComponents() {
        // Tabela
        tableModel = new DefaultTableModel(new Object[]{
            "Código", "Nome", "Número", "Cidade", "Estado", "Presidente", "Status"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        lojasTable = new JTable(tableModel);
        
        // Formulário
        codigoField = new JTextField(10);
        codigoField.setEditable(false);
        nomeField = new JTextField(40);
        numeroField = new JTextField(10);
        enderecoField = new JTextField(40);
        bairroField = new JTextField(20);
        cidadeField = new JTextField(20);
        estadoField = new JTextField(10);
        cepField = new JTextField(10);
        telefoneField = new JTextField(15);
        emailField = new JTextField(30);
        presidenteField = new JTextField(30);
        secretarioField = new JTextField(30);
        tesoureiroField = new JTextField(30);
        dataFundacaoField = new JTextField(10);
        ritoField = new JTextField(20);
        potenciaField = new JTextField(20);
        observacoesArea = new JTextArea(3, 40);
        observacoesArea.setLineWrap(true);
        observacoesArea.setWrapStyleWord(true);
        
        statusCombo = new JComboBox<>(new String[]{
            "ATIVA", "INATIVA", "SUSPENSA"
        });
        
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
        headerPanel.setBackground(new Color(128, 0, 32)); // Bordô maçônico
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("🏰 Gestão de Lojas Maçônicas", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Administração das lojas e potências maçônicas", SwingConstants.LEFT);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(220, 220, 230));
        
        // Adicionar logo maçônico discreto
        JLabel logoLabel = MasonicLogo.createLogoLabel(32);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        JPanel titleContainer = new JPanel(new GridLayout(2, 1, 0, 5));
        titleContainer.setBackground(new Color(128, 0, 32));
        titleContainer.add(titleLabel);
        titleContainer.add(subtitleLabel);
        
        JPanel headerContent = new JPanel(new BorderLayout());
        headerContent.setBackground(new Color(128, 0, 32));
        headerContent.add(logoLabel, BorderLayout.WEST);
        headerContent.add(titleContainer, BorderLayout.CENTER);
        
        headerPanel.add(headerContent, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);
        
        // Painel principal com split
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        // Painel esquerdo - Tabela
        JPanel leftPanel = new JPanel(new BorderLayout());
        
        // Painel de pesquisa
        JPanel pesquisaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pesquisaPanel.add(new JLabel("Pesquisar:"));
        pesquisaPanel.add(pesquisarField);
        pesquisaPanel.add(pesquisarButton);
        
        leftPanel.add(pesquisaPanel, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(lojasTable), BorderLayout.CENTER);
        
        // Painel direito - Formulário
        JPanel rightPanel = new JPanel(new BorderLayout());
        
        // Formulário em grid
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Código
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1;
        formPanel.add(codigoField, gbc);
        
        // Nome
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        formPanel.add(nomeField, gbc);
        
        // Número
        gbc.gridx = 3; gbc.gridy = 1; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Número:"), gbc);
        gbc.gridx = 4;
        formPanel.add(numeroField, gbc);
        
        // Endereço
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Endereço:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        formPanel.add(enderecoField, gbc);
        
        // Bairro e Cidade
        gbc.gridx = 4; gbc.gridy = 2; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Bairro:"), gbc);
        gbc.gridx = 5;
        formPanel.add(bairroField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Cidade:"), gbc);
        gbc.gridx = 1;
        formPanel.add(cidadeField, gbc);
        
        // Estado e CEP
        gbc.gridx = 2; gbc.gridy = 3;
        formPanel.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 3;
        formPanel.add(estadoField, gbc);
        
        gbc.gridx = 4; gbc.gridy = 3;
        formPanel.add(new JLabel("CEP:"), gbc);
        gbc.gridx = 5;
        formPanel.add(cepField, gbc);
        
        // Contatos
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1;
        formPanel.add(telefoneField, gbc);
        
        gbc.gridx = 2; gbc.gridy = 4;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 3; gbc.gridwidth = 3;
        formPanel.add(emailField, gbc);
        
        // Administração
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 6;
        formPanel.add(new JLabel("=== ADMINISTRAÇÃO ==="), gbc);
        
        gbc.gridy = 6; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Presidente:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        formPanel.add(presidenteField, gbc);
        
        gbc.gridx = 3; gbc.gridy = 6; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Secretário:"), gbc);
        gbc.gridx = 4; gbc.gridwidth = 2;
        formPanel.add(secretarioField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("Tesoureiro:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        formPanel.add(tesoureiroField, gbc);
        
        // Informações Maçônicas
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 6;
        formPanel.add(new JLabel("=== INFORMAÇÕES MAÇÔNICAS ==="), gbc);
        
        gbc.gridy = 9; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Data Fundação:"), gbc);
        gbc.gridx = 1;
        formPanel.add(dataFundacaoField, gbc);
        
        gbc.gridx = 2; gbc.gridy = 9;
        formPanel.add(new JLabel("Rito:"), gbc);
        gbc.gridx = 3;
        formPanel.add(ritoField, gbc);
        
        gbc.gridx = 4; gbc.gridy = 9;
        formPanel.add(new JLabel("Potência:"), gbc);
        gbc.gridx = 5;
        formPanel.add(potenciaField, gbc);
        
        // Status
        gbc.gridx = 0; gbc.gridy = 10;
        formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        formPanel.add(statusCombo, gbc);
        
        // Observações
        gbc.gridx = 0; gbc.gridy = 11; gbc.gridwidth = 6;
        formPanel.add(new JLabel("Observações:"), gbc);
        gbc.gridy = 12;
        JScrollPane observacoesScroll = new JScrollPane(observacoesArea);
        formPanel.add(observacoesScroll, gbc);
        
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
        
        gbc.gridy = 13; gbc.gridx = 0; gbc.gridwidth = 6;
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
        salvarButton.addActionListener(e -> salvarLoja());
        novoButton.addActionListener(e -> limparFormulario());
        editarButton.addActionListener(e -> carregarLojaSelecionada());
        excluirButton.addActionListener(e -> excluirLoja());
        limparButton.addActionListener(e -> limparFormulario());
        pesquisarButton.addActionListener(e -> pesquisarLojas());
        
        // Seleção na tabela
        lojasTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && lojasTable.getSelectedRow() >= 0) {
                carregarLojaSelecionada();
            }
        });
        
        // Duplo clique para editar
        lojasTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    carregarLojaSelecionada();
                }
            }
        });
    }
    
    private void salvarLoja() {
        try {
            if (nomeField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nome é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (numeroField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Número é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Loja loja = lojaAtual != null ? lojaAtual : new Loja();
            loja.setNome(nomeField.getText().trim());
            loja.setNumero(numeroField.getText().trim());
            loja.setEndereco(enderecoField.getText().trim());
            loja.setBairro(bairroField.getText().trim());
            loja.setCidade(cidadeField.getText().trim());
            loja.setEstado(estadoField.getText().trim());
            loja.setCep(cepField.getText().trim());
            loja.setTelefone(telefoneField.getText().trim());
            loja.setEmail(emailField.getText().trim());
            loja.setPresidente(presidenteField.getText().trim());
            loja.setSecretario(secretarioField.getText().trim());
            loja.setTesoureiro(tesoureiroField.getText().trim());
            loja.setDataFundacao(dataFundacaoField.getText().trim());
            loja.setRito(ritoField.getText().trim());
            loja.setPotencia(potenciaField.getText().trim());
            loja.setObservacoes(observacoesArea.getText().trim());
            loja.setStatus((String) statusCombo.getSelectedItem());
            
            lojaDAO.save(loja);
            
            JOptionPane.showMessageDialog(this, "Loja salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            refreshData();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar loja: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limparFormulario() {
        lojaAtual = null;
        codigoField.setText("");
        nomeField.setText("");
        numeroField.setText("");
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
        dataFundacaoField.setText("");
        ritoField.setText("");
        potenciaField.setText("");
        observacoesArea.setText("");
        statusCombo.setSelectedIndex(0);
        nomeField.requestFocus();
        atualizarBotoesAcao();
    }
    
    private void excluirLoja() {
        if (lojaAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma loja para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int option = JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente excluir a loja " + lojaAtual.getNome() + " (" + lojaAtual.getNumero() + ")?",
            "Confirmar Exclusão",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (option == JOptionPane.YES_OPTION) {
            try {
                lojaDAO.delete(lojaAtual.getId());
                JOptionPane.showMessageDialog(this, "Loja excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                refreshData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir loja: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void pesquisarLojas() {
        try {
            String termo = pesquisarField.getText().trim();
            if (termo.isEmpty()) {
                refreshData();
                return;
            }
            
            List<Loja> lojas = lojaDAO.findAll();
            tableModel.setRowCount(0);
            
            for (Loja loja : lojas) {
                if (loja.getNome().toLowerCase().contains(termo.toLowerCase()) ||
                    loja.getNumero().toLowerCase().contains(termo.toLowerCase()) ||
                    loja.getCidade().toLowerCase().contains(termo.toLowerCase())) {
                    Object[] row = {
                        loja.getId(),
                        loja.getNome(),
                        loja.getNumero(),
                        loja.getCidade(),
                        loja.getEstado(),
                        loja.getPresidente(),
                        loja.getStatus()
                    };
                    tableModel.addRow(row);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao pesquisar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void carregarLojaSelecionada() {
        int selectedRow = lojasTable.getSelectedRow();
        if (selectedRow >= 0) {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            try {
                lojaAtual = lojaDAO.findById(id);
                if (lojaAtual != null) {
                    codigoField.setText(String.valueOf(lojaAtual.getId()));
                    nomeField.setText(lojaAtual.getNome());
                    numeroField.setText(lojaAtual.getNumero());
                    enderecoField.setText(lojaAtual.getEndereco());
                    bairroField.setText(lojaAtual.getBairro());
                    cidadeField.setText(lojaAtual.getCidade());
                    estadoField.setText(lojaAtual.getEstado());
                    cepField.setText(lojaAtual.getCep());
                    telefoneField.setText(lojaAtual.getTelefone());
                    emailField.setText(lojaAtual.getEmail());
                    presidenteField.setText(lojaAtual.getPresidente());
                    secretarioField.setText(lojaAtual.getSecretario());
                    tesoureiroField.setText(lojaAtual.getTesoureiro());
                    dataFundacaoField.setText(lojaAtual.getDataFundacao());
                    ritoField.setText(lojaAtual.getRito());
                    potenciaField.setText(lojaAtual.getPotencia());
                    observacoesArea.setText(lojaAtual.getObservacoes());
                    statusCombo.setSelectedItem(lojaAtual.getStatus());
                    atualizarBotoesAcao();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar loja: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void atualizarBotoesAcao() {
        boolean temLoja = lojaAtual != null;
        editarButton.setEnabled(temLoja);
        excluirButton.setEnabled(temLoja);
    }
    
    public void refreshData() {
        try {
            List<Loja> lojas = lojaDAO.findAll();
            tableModel.setRowCount(0);
            
            for (Loja loja : lojas) {
                Object[] row = {
                    loja.getId(),
                    loja.getNome(),
                    loja.getNumero(),
                    loja.getCidade(),
                    loja.getEstado(),
                    loja.getPresidente(),
                    loja.getStatus()
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar lojas: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
