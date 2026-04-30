package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.IrmaoDAO;
import com.artereal.swing.model.Irmao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Painel de cadastro de Irmãos Maçons
 */
public class CadastroIrmaosPanel extends JPanel {
    
    private IrmaoDAO irmaoDAO;
    private DefaultTableModel tableModel;
    private JTable irmaosTable;
    
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
    
    private Irmao irmaoAtual;
    
    public CadastroIrmaosPanel() {
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
        
        // Título
        JLabel titleLabel = new JLabel("Cadastro de Irmãos Maçons", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Painel principal
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        // Painel do formulário
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados do Irmão"));
        
        // Formulário em grid
        JPanel gridFormPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Código e Nome
        gbc.gridx = 0; gbc.gridy = 0;
        gridFormPanel.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1;
        gridFormPanel.add(codigoField, gbc);
        
        gbc.gridx = 2; gbc.gridy = 0;
        gridFormPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 3; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        gridFormPanel.add(nomeField, gbc);
        
        // Endereço
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        gridFormPanel.add(new JLabel("Endereço:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 5; gbc.fill = GridBagConstraints.HORIZONTAL;
        gridFormPanel.add(enderecoField, gbc);
        
        // Bairro e Cidade
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        gridFormPanel.add(new JLabel("Bairro:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 1;
        gridFormPanel.add(bairroField, gbc);
        
        gbc.gridx = 2; gbc.gridy = 2;
        gridFormPanel.add(new JLabel("Cidade:"), gbc);
        gbc.gridx = 3; gbc.gridwidth = 1;
        gridFormPanel.add(cidadeField, gbc);
        
        gbc.gridx = 4; gbc.gridy = 2;
        gridFormPanel.add(new JLabel("UF:"), gbc);
        gbc.gridx = 5; gbc.gridwidth = 1;
        gridFormPanel.add(estadoField, gbc);
        
        // Telefone
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        gridFormPanel.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 1;
        gridFormPanel.add(telefoneField, gbc);
        
        // Grau e Cargo Loja
        gbc.gridx = 0; gbc.gridy = 4;
        gridFormPanel.add(new JLabel("Grau:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 1;
        gridFormPanel.add(grauField, gbc);
        
        gbc.gridx = 2; gbc.gridy = 4;
        gridFormPanel.add(new JLabel("Cargo Loja:"), gbc);
        gbc.gridx = 3; gbc.gridwidth = 1;
        gridFormPanel.add(cargoLojaField, gbc);
        
        // Botões do formulário
        JPanel botoesFormPanel = new JPanel(new FlowLayout());
        botoesFormPanel.add(salvarButton);
        botoesFormPanel.add(novoButton);
        botoesFormPanel.add(editarButton);
        botoesFormPanel.add(excluirButton);
        botoesFormPanel.add(limparButton);
        
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 6; gbc.fill = GridBagConstraints.HORIZONTAL;
        gridFormPanel.add(botoesFormPanel, gbc);
        
        formPanel.add(gridFormPanel, BorderLayout.CENTER);
        
        // Painel da tabela
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBorder(BorderFactory.createTitledBorder("Lista de Irmãos"));
        
        // Painel de pesquisa
        JPanel pesquisaPanel = new JPanel(new FlowLayout());
        pesquisaPanel.add(new JLabel("Pesquisar:"));
        pesquisaPanel.add(pesquisarField);
        pesquisaPanel.add(pesquisarButton);
        
        tabelaPanel.add(pesquisaPanel, BorderLayout.NORTH);
        tabelaPanel.add(new JScrollPane(irmaosTable), BorderLayout.CENTER);
        
        splitPane.setLeftComponent(formPanel);
        splitPane.setRightComponent(tabelaPanel);
        splitPane.setDividerLocation(500);
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private void setupEvents() {
        salvarButton.addActionListener(e -> salvarIrmao());
        novoButton.addActionListener(e -> limparFormulario());
        editarButton.addActionListener(e -> editarIrmao());
        excluirButton.addActionListener(e -> excluirIrmao());
        limparButton.addActionListener(e -> limparFormulario());
        pesquisarButton.addActionListener(e -> pesquisarIrmaos());
        
        // Seleção na tabela
        irmaosTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarIrmaoSelecionado();
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
                    irmao.setNascimento(LocalDate.parse(nascimentoStr));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Data de nascimento inválida! Use formato YYYY-MM-DD", "Erro", JOptionPane.ERROR_MESSAGE);
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
    
    private void editarIrmao() {
        if (irmaoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um irmão para editar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // O formulário já está preenchido com os dados do irmão selecionado
        nomeField.requestFocus();
    }
    
    private void excluirIrmao() {
        if (irmaoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um irmão para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente excluir o irmão " + irmaoAtual.getNome() + "?",
            "Confirmar Exclusão",
            JOptionPane.YES_NO_OPTION);
            
        if (opcao == JOptionPane.YES_OPTION) {
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
    
    private void pesquisarIrmaos() {
        try {
            String termo = pesquisarField.getText().trim();
            if (termo.isEmpty()) {
                refreshData();
                return;
            }
            
            List<Irmao> irmaos = irmaoDAO.findAll(); // Simplificado - poderia ter método específico
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
                        nascimentoField.setText(irmaoAtual.getNascimento().toString());
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
