package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.CandidatoDAO;
import com.artereal.swing.model.Candidato;
import com.artereal.swing.ui.components.LogoMaconaria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

/**
 * Painel de Gestão de Candidatos e Profanos
 */
public class CandidatosPanel extends JPanel {
    
    private CandidatoDAO candidatoDAO;
    private DefaultTableModel tableModel;
    private JTable candidatosTable;
    private JTextField nomeField;
    private JTextField enderecoField;
    private JTextField cidadeField;
    private JTextField estadoField;
    private JTextField profissaoField;
    private JTextField foneField;
    private JTextArea informacoesArea;
    private JComboBox<String> statusComboBox;
    private JButton salvarButton;
    private JButton novoButton;
    private JButton excluirButton;
    private JButton aprovarButton;
    private JButton rejeitarButton;
    private JButton iniciarButton;
    private Candidato candidatoAtual;
    
    public CandidatosPanel() {
        candidatoDAO = new CandidatoDAO();
        initializeComponents();
        setupLayout();
        setupEvents();
        refreshData();
    }
    
    private void initializeComponents() {
        // Tabela
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "Cidade/Estado", "Profissão", "Status", "Data Cadastro"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        candidatosTable = new JTable(tableModel);
        
        // Formulário
        nomeField = new JTextField(30);
        enderecoField = new JTextField(40);
        cidadeField = new JTextField(20);
        estadoField = new JTextField(10);
        profissaoField = new JTextField(25);
        foneField = new JTextField(15);
        informacoesArea = new JTextArea(4, 30);
        informacoesArea.setLineWrap(true);
        informacoesArea.setWrapStyleWord(true);
        
        statusComboBox = new JComboBox<>(new String[]{"CANDIDATO", "APROVADO", "REJEITADO", "INICIADO"});
        
        // Botões
        salvarButton = new JButton("Salvar");
        novoButton = new JButton("Novo");
        excluirButton = new JButton("Excluir");
        aprovarButton = new JButton("Aprovar");
        rejeitarButton = new JButton("Rejeitar");
        iniciarButton = new JButton("Iniciar");
        
        candidatoAtual = null;
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 250));
        
        // Header estilizado com logo
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(25, 25, 112)); // Azul marinho maçônico
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("👥 Gestão de Candidatos e Profanos", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Controle de admissões e iniciações maçônicas", SwingConstants.LEFT);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(173, 216, 230));
        
        // Logo maçônico
        JLabel logoLabel = LogoMaconaria.createLogoLabel(32);
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
        
        // Painel esquerdo - Tabela e estatísticas
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(new Color(245, 245, 250));
        
        // Painel de pesquisa
        JPanel pesquisaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pesquisaPanel.setBackground(new Color(240, 240, 245));
        pesquisaPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel searchLabel = new JLabel("🔍 Pesquisar:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchLabel.setForeground(new Color(100, 100, 120));
        
        // Campo de pesquisa (adicionar se não existir)
        JTextField pesquisarField = new JTextField();
        pesquisarField.setPreferredSize(new Dimension(200, 30));
        pesquisarField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        
        JButton pesquisarButton = new JButton("Buscar");
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
        
        // Painel de estatísticas melhorado
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        statsPanel.setBackground(new Color(240, 240, 245));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        try {
            List<Object[]> estatisticas = candidatoDAO.getEstatisticas();
            for (Object[] est : estatisticas) {
                String status = (String) est[0];
                int quantidade = (Integer) est[1];
                
                JPanel statusPanel = new JPanel(new BorderLayout());
                statusPanel.setBorder(BorderFactory.createEtchedBorder());
                statusPanel.add(new JLabel(status, SwingConstants.CENTER), BorderLayout.NORTH);
                statusPanel.add(new JLabel(String.valueOf(quantidade), SwingConstants.CENTER), BorderLayout.CENTER);
                
                // Colorir baseado no status
                Color color = Color.BLACK;
                switch (status) {
                    case "CANDIDATO": color = Color.BLUE; break;
                    case "APROVADO": color = Color.GREEN; break;
                    case "REJEITADO": color = Color.RED; break;
                    case "INICIADO": color = new Color(128, 0, 128); break;
                }
                
                JLabel statusLabel = new JLabel(status, SwingConstants.CENTER);
                statusLabel.setForeground(color);
                statusPanel.add(statusLabel, BorderLayout.NORTH);
                
                statsPanel.add(statusPanel);
            }
        } catch (SQLException e) {
            statsPanel.add(new JLabel("Erro ao carregar estatísticas"));
        }
        
        // Painel de formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados do Candidato"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Nome
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        formPanel.add(nomeField, gbc);
        
        // Endereço
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Endereço:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        formPanel.add(enderecoField, gbc);
        
        // Cidade e Estado
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Cidade:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        JPanel cidadeEstadoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cidadeEstadoPanel.add(cidadeField);
        cidadeEstadoPanel.add(new JLabel("Estado:"));
        cidadeEstadoPanel.add(estadoField);
        formPanel.add(cidadeEstadoPanel, gbc);
        
        // Profissão
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Profissão:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        formPanel.add(profissaoField, gbc);
        
        // Telefone
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        formPanel.add(foneField, gbc);
        
        // Status
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        formPanel.add(statusComboBox, gbc);
        
        // Informações
        gbc.gridx = 0; gbc.gridy = 6; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Informações:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1; gbc.weighty = 1;
        formPanel.add(new JScrollPane(informacoesArea), gbc);
        
        // Botões do formulário
        JPanel botoesFormPanel = new JPanel(new FlowLayout());
        botoesFormPanel.add(salvarButton);
        botoesFormPanel.add(novoButton);
        botoesFormPanel.add(excluirButton);
        
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weighty = 0;
        formPanel.add(botoesFormPanel, gbc);
        
        // Botões de ações
        JPanel acoesPanel = new JPanel(new FlowLayout());
        acoesPanel.setBorder(BorderFactory.createTitledBorder("Ações do Processo"));
        acoesPanel.add(aprovarButton);
        acoesPanel.add(rejeitarButton);
        acoesPanel.add(iniciarButton);
        
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(acoesPanel, gbc);
        
        // Tabela estilizada
        candidatosTable.setRowHeight(25);
        candidatosTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        candidatosTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        candidatosTable.getTableHeader().setBackground(new Color(70, 130, 180));
        candidatosTable.getTableHeader().setForeground(Color.WHITE);
        candidatosTable.setSelectionBackground(new Color(173, 216, 230));
        candidatosTable.setSelectionForeground(new Color(25, 84, 123));
        
        // Painel da tabela
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBackground(Color.WHITE);
        tabelaPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        tabelaPanel.add(new JScrollPane(candidatosTable), BorderLayout.CENTER);
        
        // Painel direito - Formulário
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(new Color(245, 245, 250));
        
        // Título do formulário
        JPanel formTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formTitlePanel.setBackground(new Color(70, 130, 180));
        formTitlePanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel formTitle = new JLabel("📝 Dados do Candidato");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formTitle.setForeground(Color.WHITE);
        formTitlePanel.add(formTitle);
        rightPanel.add(formTitlePanel, BorderLayout.NORTH);
        
        // Formulário com design melhorado
        JPanel rightFormPanel = new JPanel(new GridBagLayout());
        rightFormPanel.setBackground(Color.WHITE);
        rightFormPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints formGbc = new GridBagConstraints();
        formGbc.insets = new Insets(8, 8, 8, 8);
        formGbc.anchor = GridBagConstraints.WEST;
        
        // Nome
        formGbc.gridx = 0; formGbc.gridy = 0;
        JLabel nomeLabel = new JLabel("Nome:");
        nomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nomeLabel.setForeground(new Color(70, 130, 180));
        rightFormPanel.add(nomeLabel, formGbc);
        formGbc.gridx = 1; formGbc.fill = GridBagConstraints.HORIZONTAL; formGbc.weightx = 1;
        nomeField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        rightFormPanel.add(nomeField, formGbc);
        
        // Endereço
        formGbc.gridx = 0; formGbc.gridy = 1; formGbc.fill = GridBagConstraints.NONE; formGbc.weightx = 0;
        JLabel enderecoLabel = new JLabel("Endereço:");
        enderecoLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        enderecoLabel.setForeground(new Color(70, 130, 180));
        rightFormPanel.add(enderecoLabel, formGbc);
        formGbc.gridx = 1; formGbc.fill = GridBagConstraints.HORIZONTAL; formGbc.weightx = 1;
        enderecoField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        rightFormPanel.add(enderecoField, formGbc);
        
        // Cidade e Estado
        formGbc.gridx = 0; formGbc.gridy = 2; formGbc.fill = GridBagConstraints.NONE; formGbc.weightx = 0;
        JLabel cidadeLabel = new JLabel("Cidade:");
        cidadeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cidadeLabel.setForeground(new Color(70, 130, 180));
        rightFormPanel.add(cidadeLabel, formGbc);
        formGbc.gridx = 1; formGbc.fill = GridBagConstraints.HORIZONTAL; formGbc.weightx = 1;
        JPanel cidadeEstadoRightPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cidadeEstadoRightPanel.setOpaque(false);
        cidadeField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        cidadeEstadoRightPanel.add(cidadeField);
        cidadeEstadoRightPanel.add(new JLabel("Estado:"));
        estadoField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        cidadeEstadoRightPanel.add(estadoField);
        rightFormPanel.add(cidadeEstadoRightPanel, formGbc);
        
        // Adicionar outros campos...
        formGbc.gridx = 0; formGbc.gridy = 3; formGbc.fill = GridBagConstraints.NONE; formGbc.weightx = 0;
        JLabel profissaoLabel = new JLabel("Profissão:");
        profissaoLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        profissaoLabel.setForeground(new Color(70, 130, 180));
        rightFormPanel.add(profissaoLabel, formGbc);
        formGbc.gridx = 1; formGbc.fill = GridBagConstraints.HORIZONTAL; formGbc.weightx = 1;
        profissaoField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        rightFormPanel.add(profissaoField, formGbc);
        
        // Botões estilizados
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        botoesPanel.setBackground(Color.WHITE);
        
        // Aplicar cores pastéis aos botões
        salvarButton.setBackground(new Color(144, 238, 144));
        salvarButton.setForeground(new Color(34, 89, 34));
        salvarButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        salvarButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(144, 238, 144), 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        salvarButton.setFocusPainted(false);
        salvarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        novoButton.setBackground(new Color(173, 216, 230));
        novoButton.setForeground(new Color(25, 84, 123));
        novoButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        novoButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(173, 216, 230), 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        novoButton.setFocusPainted(false);
        novoButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        excluirButton.setBackground(new Color(255, 182, 193));
        excluirButton.setForeground(new Color(139, 0, 0));
        excluirButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        excluirButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 182, 193), 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        excluirButton.setFocusPainted(false);
        excluirButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        botoesPanel.add(salvarButton);
        botoesPanel.add(novoButton);
        botoesPanel.add(excluirButton);
        
        rightPanel.add(rightFormPanel, BorderLayout.CENTER);
        rightPanel.add(botoesPanel, BorderLayout.SOUTH);
        
        // Montar layout principal
        leftPanel.add(pesquisaPanel, BorderLayout.NORTH);
        leftPanel.add(tabelaPanel, BorderLayout.CENTER);
        
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        splitPane.setDividerLocation(600);
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private void setupEvents() {
        salvarButton.addActionListener(e -> salvarCandidato());
        novoButton.addActionListener(e -> limparFormulario());
        excluirButton.addActionListener(e -> excluirCandidato());
        aprovarButton.addActionListener(e -> aprovarCandidato());
        rejeitarButton.addActionListener(e -> rejeitarCandidato());
        iniciarButton.addActionListener(e -> iniciarCandidato());
        
        // Seleção na tabela
        candidatosTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarCandidatoSelecionado();
            }
        });
        
        // Mudança de status
        statusComboBox.addActionListener(e -> atualizarBotoesAcao());
    }
    
    private void salvarCandidato() {
        try {
            if (nomeField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nome do candidato é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Candidato candidato = candidatoAtual != null ? candidatoAtual : new Candidato();
            candidato.setNome(nomeField.getText().trim());
            candidato.setEndereco(enderecoField.getText().trim());
            candidato.setCidade(cidadeField.getText().trim());
            candidato.setEstado(estadoField.getText().trim());
            candidato.setProfissao(profissaoField.getText().trim());
            candidato.setFoneResidencial(foneField.getText().trim());
            candidato.setInformacoes(informacoesArea.getText().trim());
            candidato.setStatus((String) statusComboBox.getSelectedItem());
            
            candidatoDAO.save(candidato);
            
            JOptionPane.showMessageDialog(this, "Candidato salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            refreshData();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar candidato: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limparFormulario() {
        candidatoAtual = null;
        nomeField.setText("");
        enderecoField.setText("");
        cidadeField.setText("");
        estadoField.setText("");
        profissaoField.setText("");
        foneField.setText("");
        informacoesArea.setText("");
        statusComboBox.setSelectedItem("CANDIDATO");
        nomeField.requestFocus();
        atualizarBotoesAcao();
    }
    
    private void excluirCandidato() {
        if (candidatoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um candidato para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente excluir o candidato " + candidatoAtual.getNome() + "?", 
            "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                candidatoDAO.delete(candidatoAtual.getId());
                JOptionPane.showMessageDialog(this, "Candidato excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir candidato: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void aprovarCandidato() {
        if (candidatoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um candidato para aprovar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            candidatoDAO.aprovar(candidatoAtual.getId());
            JOptionPane.showMessageDialog(this, "Candidato aprovado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarCandidatoSelecionado();
            refreshData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao aprovar candidato: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void rejeitarCandidato() {
        if (candidatoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um candidato para rejeitar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente rejeitar o candidato " + candidatoAtual.getNome() + "?", 
            "Confirmar Rejeição", JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                candidatoDAO.rejeitar(candidatoAtual.getId());
                JOptionPane.showMessageDialog(this, "Candidato rejeitado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarCandidatoSelecionado();
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao rejeitar candidato: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void iniciarCandidato() {
        if (candidatoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um candidato para iniciar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente iniciar o candidato " + candidatoAtual.getNome() + " como irmão?", 
            "Confirmar Iniciação", JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                candidatoDAO.iniciar(candidatoAtual.getId());
                JOptionPane.showMessageDialog(this, "Candidato iniciado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarCandidatoSelecionado();
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao iniciar candidato: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void carregarCandidatoSelecionado() {
        int selectedRow = candidatosTable.getSelectedRow();
        if (selectedRow >= 0) {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            try {
                candidatoAtual = candidatoDAO.findById(id);
                if (candidatoAtual != null) {
                    nomeField.setText(candidatoAtual.getNome());
                    enderecoField.setText(candidatoAtual.getEndereco());
                    cidadeField.setText(candidatoAtual.getCidade());
                    estadoField.setText(candidatoAtual.getEstado());
                    profissaoField.setText(candidatoAtual.getProfissao());
                    foneField.setText(candidatoAtual.getFoneResidencial());
                    informacoesArea.setText(candidatoAtual.getInformacoes());
                    statusComboBox.setSelectedItem(candidatoAtual.getStatus());
                    atualizarBotoesAcao();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar candidato: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void atualizarBotoesAcao() {
        String status = (String) statusComboBox.getSelectedItem();
        
        // Habilitar/desabilitar botões baseado no status
        aprovarButton.setEnabled("CANDIDATO".equals(status));
        rejeitarButton.setEnabled("CANDIDATO".equals(status) || "APROVADO".equals(status));
        iniciarButton.setEnabled("APROVADO".equals(status));
    }
    
    public void refreshData() {
        try {
            List<Candidato> candidatos = candidatoDAO.findAll();
            tableModel.setRowCount(0);
            
            for (Candidato candidato : candidatos) {
                Object[] row = {
                    candidato.getId(),
                    candidato.getNome(),
                    candidato.getCidade() + "/" + candidato.getEstado(),
                    candidato.getProfissao(),
                    candidato.getStatus(),
                    candidato.getDataCadastro() != null ? candidato.getDataCadastro().toString() : ""
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar candidatos: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
