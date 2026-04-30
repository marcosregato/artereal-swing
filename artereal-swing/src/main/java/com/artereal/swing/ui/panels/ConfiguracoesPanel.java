package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.ConfiguracaoDAO;
import com.artereal.swing.model.Configuracao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

/**
 * Painel de Configurações Globais do Sistema
 */
public class ConfiguracoesPanel extends JPanel {
    
    private ConfiguracaoDAO configuracaoDAO;
    private DefaultTableModel tableModel;
    private JTable configuracoesTable;
    private JTextField chaveField;
    private JTextField valorField;
    private JTextField descricaoField;
    private JComboBox<String> tipoComboBox;
    private JComboBox<String> categoriaComboBox;
    private JCheckBox editavelCheckBox;
    private JCheckBox visivelCheckBox;
    private JButton salvarButton;
    private JButton novoButton;
    private JButton excluirButton;
    private JButton restaurarPadraoButton;
    private Configuracao configuracaoAtual;
    
    public ConfiguracoesPanel() {
        configuracaoDAO = new ConfiguracaoDAO();
        initializeComponents();
        setupLayout();
        setupEvents();
        refreshData();
    }
    
    private void initializeComponents() {
        // Tabela
        tableModel = new DefaultTableModel(new Object[]{"ID", "Chave", "Valor", "Descrição", "Tipo", "Categoria", "Editável"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        configuracoesTable = new JTable(tableModel);
        
        // Formulário
        chaveField = new JTextField(30);
        chaveField.setEditable(false);
        valorField = new JTextField(30);
        descricaoField = new JTextField(40);
        tipoComboBox = new JComboBox<>(new String[]{"STRING", "NUMBER", "BOOLEAN", "DATE"});
        categoriaComboBox = new JComboBox<>(new String[]{"SISTEMA", "FINANCEIRO", "USUARIO", "INTERFACE", "BACKUP"});
        editavelCheckBox = new JCheckBox("Editável");
        visivelCheckBox = new JCheckBox("Visível");
        
        // Botões
        salvarButton = new JButton("Salvar");
        novoButton = new JButton("Novo");
        excluirButton = new JButton("Excluir");
        restaurarPadraoButton = new JButton("Restaurar Padrão");
        
        configuracaoAtual = null;
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Título
        JLabel titleLabel = new JLabel("Configurações Globais do Sistema", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Painel de formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados da Configuração"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Chave
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Chave:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        formPanel.add(chaveField, gbc);
        
        // Valor
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Valor:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        formPanel.add(valorField, gbc);
        
        // Descrição
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        formPanel.add(descricaoField, gbc);
        
        // Tipo e Categoria
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        JPanel tipoCategoriaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tipoCategoriaPanel.add(tipoComboBox);
        tipoCategoriaPanel.add(new JLabel("Categoria:"));
        tipoCategoriaPanel.add(categoriaComboBox);
        formPanel.add(tipoCategoriaPanel, gbc);
        
        // Opções
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Opções:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        JPanel opcoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        opcoesPanel.add(editavelCheckBox);
        opcoesPanel.add(visivelCheckBox);
        formPanel.add(opcoesPanel, gbc);
        
        // Botões do formulário
        JPanel botoesFormPanel = new JPanel(new FlowLayout());
        botoesFormPanel.add(salvarButton);
        botoesFormPanel.add(novoButton);
        botoesFormPanel.add(excluirButton);
        botoesFormPanel.add(restaurarPadraoButton);
        
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(botoesFormPanel, gbc);
        
        // Painel da tabela
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBorder(BorderFactory.createTitledBorder("Configurações Cadastradas"));
        tabelaPanel.add(new JScrollPane(configuracoesTable), BorderLayout.CENTER);
        
        // Painel de informações
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder("Informações"));
        JTextArea infoArea = new JTextArea(3, 50);
        infoArea.setEditable(false);
        infoArea.setText("As configurações globais controlam o comportamento do sistema.\n" +
                       "Configurações não editáveis são internas do sistema.\n" +
                       "Alterações podem exigir reinicialização do sistema.");
        infoArea.setBackground(infoPanel.getBackground());
        infoPanel.add(infoArea, BorderLayout.CENTER);
        
        // Layout principal
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formPanel, tabelaPanel);
        splitPane.setDividerLocation(250);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(splitPane, BorderLayout.CENTER);
        centerPanel.add(infoPanel, BorderLayout.SOUTH);
        
        add(titleLabel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private void setupEvents() {
        salvarButton.addActionListener(e -> salvarConfiguracao());
        novoButton.addActionListener(e -> limparFormulario());
        excluirButton.addActionListener(e -> excluirConfiguracao());
        restaurarPadraoButton.addActionListener(e -> restaurarPadrao());
        
        // Seleção na tabela
        configuracoesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarConfiguracaoSelecionada();
            }
        });
        
        // Mudança de tipo para validação
        tipoComboBox.addActionListener(e -> validarTipo());
    }
    
    private void salvarConfiguracao() {
        try {
            if (chaveField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Chave da configuração é obrigatória!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Configuracao configuracao = configuracaoAtual != null ? configuracaoAtual : new Configuracao();
            configuracao.setChave(chaveField.getText().trim());
            configuracao.setValor(valorField.getText().trim());
            configuracao.setDescricao(descricaoField.getText().trim());
            configuracao.setTipo((String) tipoComboBox.getSelectedItem());
            configuracao.setCategoria((String) categoriaComboBox.getSelectedItem());
            configuracao.setEditavel(editavelCheckBox.isSelected());
            configuracao.setVisivel(visivelCheckBox.isSelected());
            
            // Validar valor baseado no tipo
            if (!validarValor(configuracao)) {
                return;
            }
            
            configuracaoDAO.save(configuracao);
            
            JOptionPane.showMessageDialog(this, "Configuração salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            refreshData();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar configuração: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean validarValor(Configuracao configuracao) {
        String tipo = configuracao.getTipo();
        String valor = configuracao.getValor();
        
        try {
            switch (tipo) {
                case "NUMBER":
                    Double.parseDouble(valor);
                    break;
                case "BOOLEAN":
                    if (!"true".equalsIgnoreCase(valor) && !"false".equalsIgnoreCase(valor) && 
                        !"1".equals(valor) && !"0".equals(valor) && 
                        !"sim".equalsIgnoreCase(valor) && !"nao".equalsIgnoreCase(valor)) {
                        JOptionPane.showMessageDialog(this, 
                            "Valor booleano deve ser: true/false, 1/0, sim/nao", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    break;
                case "DATE":
                    // Validação básica de data (pode ser melhorada)
                    if (!valor.matches("\\d{4}-\\d{2}-\\d{2}")) {
                        JOptionPane.showMessageDialog(this, 
                            "Data deve estar no formato YYYY-MM-DD", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    break;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Valor numérico inválido para o tipo selecionado", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void validarTipo() {
        String tipo = (String) tipoComboBox.getSelectedItem();
        if ("BOOLEAN".equals(tipo)) {
            valorField.setText("false");
        } else if ("NUMBER".equals(tipo)) {
            valorField.setText("0");
        } else if ("DATE".equals(tipo)) {
            valorField.setText(java.time.LocalDate.now().toString());
        }
    }
    
    private void limparFormulario() {
        configuracaoAtual = null;
        chaveField.setText("");
        chaveField.setEditable(true);
        valorField.setText("");
        descricaoField.setText("");
        tipoComboBox.setSelectedItem("STRING");
        categoriaComboBox.setSelectedItem("SISTEMA");
        editavelCheckBox.setSelected(true);
        visivelCheckBox.setSelected(true);
        valorField.requestFocus();
    }
    
    private void excluirConfiguracao() {
        if (configuracaoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma configuração para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!configuracaoAtual.isEditavel()) {
            JOptionPane.showMessageDialog(this, "Configurações não editáveis não podem ser excluídas!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente excluir a configuração " + configuracaoAtual.getChave() + "?", 
            "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                configuracaoDAO.delete(configuracaoAtual.getId());
                JOptionPane.showMessageDialog(this, "Configuração excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir configuração: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void restaurarPadrao() {
        if (configuracaoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma configuração para restaurar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja restaurar o valor padrão da configuração " + configuracaoAtual.getChave() + "?", 
            "Confirmar Restauração", JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                // Implementar lógica para restaurar valor padrão baseado na chave
                String valorPadrao = getValorPadrao(configuracaoAtual.getChave());
                valorField.setText(valorPadrao);
                
                JOptionPane.showMessageDialog(this, "Valor padrão restaurado! Salve para aplicar as alterações.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao restaurar valor padrão: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private String getValorPadrao(String chave) {
        // Valores padrão para configurações conhecidas
        switch (chave) {
            case "NOME_LOJA": return "Loja Simbólica ArteReal";
            case "NUMERO_LOJA": return "123";
            case "RITO": return "Escocês Antigo e Aceito";
            case "POTENCIA": return "Grande Oriente do Brasil";
            case "MOEDA_PADRAO": return "BRL";
            case "DIAS_AVISO_VENCIMENTO": return "7";
            case "PERCENTUAL_MINIMO_FREQUENCIA": return "75";
            case "BACKUP_AUTOMATICO": return "true";
            default: return "";
        }
    }
    
    private void carregarConfiguracaoSelecionada() {
        int selectedRow = configuracoesTable.getSelectedRow();
        if (selectedRow >= 0) {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            try {
                configuracaoAtual = configuracaoDAO.findByChave(tableModel.getValueAt(selectedRow, 1).toString());
                if (configuracaoAtual != null) {
                    chaveField.setText(configuracaoAtual.getChave());
                    chaveField.setEditable(false);
                    valorField.setText(configuracaoAtual.getValor());
                    descricaoField.setText(configuracaoAtual.getDescricao());
                    tipoComboBox.setSelectedItem(configuracaoAtual.getTipo());
                    categoriaComboBox.setSelectedItem(configuracaoAtual.getCategoria());
                    editavelCheckBox.setSelected(configuracaoAtual.isEditavel());
                    visivelCheckBox.setSelected(configuracaoAtual.isVisivel());
                    
                    // Habilitar/desabilitar campos baseado na editabilidade
                    boolean editavel = configuracaoAtual.isEditavel();
                    valorField.setEditable(editavel);
                    descricaoField.setEditable(editavel);
                    tipoComboBox.setEnabled(editavel);
                    categoriaComboBox.setEnabled(editavel);
                    editavelCheckBox.setEnabled(editavel);
                    visivelCheckBox.setEnabled(editavel);
                    salvarButton.setEnabled(editavel);
                    excluirButton.setEnabled(editavel);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar configuração: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void refreshData() {
        try {
            List<Configuracao> configuracoes = configuracaoDAO.findVisiveis();
            tableModel.setRowCount(0);
            
            for (Configuracao configuracao : configuracoes) {
                Object[] row = {
                    configuracao.getId(),
                    configuracao.getChave(),
                    configuracao.getValor(),
                    configuracao.getDescricao(),
                    configuracao.getTipo(),
                    configuracao.getCategoria(),
                    configuracao.isEditavel() ? "Sim" : "Não"
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar configurações: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
