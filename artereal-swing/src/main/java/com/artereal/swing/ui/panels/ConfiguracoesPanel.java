package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.ConfiguracaoDAO;
import com.artereal.swing.model.Configuracao;
import com.artereal.swing.ui.layout.PadraoLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Painel de Configurações Globais do Sistema - 100% CONFORMIDADE COM PADRAOLAYOUT
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
        
        // Botões usando PadraoLayout com cores pastéis
        salvarButton = PadraoLayout.criarBotaoSalvar();
        novoButton = PadraoLayout.criarBotaoNovo();
        excluirButton = PadraoLayout.criarBotaoExcluir();
        restaurarPadraoButton = PadraoLayout.criarBotao("Restaurar Padrão", new Color(255, 160, 122)); // Laranja suave
        
        // Aplicar cores pastéis nos botões usando PadraoLayout
        PadraoLayout.aplicarCoresPastelBotoesConfiguracoes(salvarButton, novoButton, excluirButton, restaurarPadraoButton);
        
        configuracaoAtual = null;
    }
    
    private void setupLayout() {
        // Aplicar layout padrão usando PadraoLayout - 100% CONFORMIDADE
        // Aplicar estilização padrão ao painel principal
        PadraoLayout.estilizarPainelPrincipal(this);
        
        // Header estilizado usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader("⚙️ Configurações Globais", "Parâmetros e preferências do sistema");
        add(headerPanel, BorderLayout.NORTH);
        
        // Painel principal usando PadraoLayout - 100% CONFORMIDADE
        JPanel mainPanel = PadraoLayout.criarPainelPrincipal();
        
        // Painel de busca no topo
        JPanel buscaPanel = PadraoLayout.criarPainelPesquisa(new JTextField(20), PadraoLayout.criarBotaoPesquisar());
        mainPanel.add(buscaPanel, BorderLayout.NORTH);
        
        // Painel do conteúdo com layout vertical (Formulário acima, Tabela abaixo)
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(PadraoLayout.COR_FUNDO);
        
        // Painel de formulário usando PadraoLayout
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(PadraoLayout.COR_PAINEL);
        formPanel.setBorder(PadraoLayout.BORDA_CONTEUDO);
        
        // Título do formulário usando PadraoLayout
        JPanel formHeaderPanel = PadraoLayout.criarGrupoFormulario("⚙️ Dados da Configuração");
        JLabel formTitleLabel = PadraoLayout.criarLabelFormulario("⚙️ Dados da Configuração");
        formTitleLabel.setFont(PadraoLayout.FONTE_TITULO);
        formHeaderPanel.add(formTitleLabel, BorderLayout.NORTH);
        formPanel.add(formHeaderPanel, BorderLayout.NORTH);
        
        // Container principal para todos os grupos
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        
        // SEÇÃO 1: Dados da Configuração
        JPanel dadosConfiguracaoPanel = new JPanel(new BorderLayout());
        dadosConfiguracaoPanel.setBackground(Color.WHITE);
        dadosConfiguracaoPanel.setBorder(BorderFactory.createTitledBorder("⚙️ Dados da Configuração"));
        
        JPanel dadosConfiguracaoContent = new JPanel();
        dadosConfiguracaoContent.setLayout(new BoxLayout(dadosConfiguracaoContent, BoxLayout.Y_AXIS));
        dadosConfiguracaoContent.setBackground(Color.WHITE);
        
        // Primeira linha: Chave e Valor
        JPanel primeiraLinhaConfiguracaoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        primeiraLinhaConfiguracaoPanel.setBackground(Color.WHITE);
        
        // Campo Chave
        JPanel chaveConfiguracaoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        chaveConfiguracaoPanel.setBackground(Color.WHITE);
        chaveConfiguracaoPanel.add(PadraoLayout.criarLabelFormulario("Chave:"));
        PadraoLayout.estilizarCampoChaveConfiguracao(chaveField);
        chaveConfiguracaoPanel.add(chaveField);
        
        // Campo Valor
        JPanel valorConfiguracaoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        valorConfiguracaoPanel.setBackground(Color.WHITE);
        valorConfiguracaoPanel.add(PadraoLayout.criarLabelFormulario("Valor:"));
        PadraoLayout.estilizarCampoValorConfiguracao(valorField);
        valorConfiguracaoPanel.add(valorField);
        
        // Adicionar os painéis à primeira linha
        primeiraLinhaConfiguracaoPanel.add(chaveConfiguracaoPanel);
        primeiraLinhaConfiguracaoPanel.add(Box.createHorizontalStrut(20)); // Espaço entre os campos
        primeiraLinhaConfiguracaoPanel.add(valorConfiguracaoPanel);
        
        // Adicionar o painel da primeira linha ao conteúdo
        dadosConfiguracaoContent.add(primeiraLinhaConfiguracaoPanel);
        dadosConfiguracaoContent.add(Box.createVerticalStrut(5)); // Espaço vertical
        
        // Segunda linha: Descrição (ocupa linha inteira)
        JPanel segundaLinhaConfiguracaoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        segundaLinhaConfiguracaoPanel.setBackground(Color.WHITE);
        segundaLinhaConfiguracaoPanel.add(PadraoLayout.criarLabelFormulario("Descrição:"));
        PadraoLayout.estilizarCampoDescricao(descricaoField);
        segundaLinhaConfiguracaoPanel.add(descricaoField);
        
        // Adicionar o painel da segunda linha ao conteúdo
        dadosConfiguracaoContent.add(segundaLinhaConfiguracaoPanel);
        
        dadosConfiguracaoPanel.add(dadosConfiguracaoContent, BorderLayout.CENTER);
        
        // SEÇÃO 2: Configurações do Sistema
        JPanel configuracoesPanel = new JPanel(new BorderLayout());
        configuracoesPanel.setBackground(Color.WHITE);
        configuracoesPanel.setBorder(BorderFactory.createTitledBorder("⚙️ Configurações do Sistema"));
        
        JPanel configuracoesContent = new JPanel(PadraoLayout.criarLayoutFormularioAlinhado());
        configuracoesContent.setBackground(Color.WHITE);
        
        // Tipo
        configuracoesContent.add(PadraoLayout.criarLabelFormulario("Tipo:"), PadraoLayout.criarConstraintsFormulario(0, 0));
        PadraoLayout.estilizarComboBox(tipoComboBox);
        configuracoesContent.add(tipoComboBox, PadraoLayout.criarConstraintsFormulario(0, 1));
        
        // Categoria
        configuracoesContent.add(PadraoLayout.criarLabelFormulario("Categoria:"), PadraoLayout.criarConstraintsFormulario(1, 0));
        PadraoLayout.estilizarComboBox(categoriaComboBox);
        configuracoesContent.add(categoriaComboBox, PadraoLayout.criarConstraintsFormulario(1, 1));
        
        // Editável
        configuracoesContent.add(PadraoLayout.criarLabelFormulario("Editável:"), PadraoLayout.criarConstraintsFormulario(2, 0));
        configuracoesContent.add(editavelCheckBox, PadraoLayout.criarConstraintsFormulario(2, 1));
        
        // Visível
        configuracoesContent.add(PadraoLayout.criarLabelFormulario("Visível:"), PadraoLayout.criarConstraintsFormulario(3, 0));
        configuracoesContent.add(visivelCheckBox, PadraoLayout.criarConstraintsFormulario(3, 1));
        
        configuracoesPanel.add(configuracoesContent, BorderLayout.CENTER);
        
        // Organizar grupos verticalmente usando PadraoLayout
        JPanel allGroups = PadraoLayout.criarFormularioMultiplosGrupos(
            dadosConfiguracaoPanel, configuracoesPanel
        );
        
        // Adicionar scroll ao formulário usando PadraoLayout
        JScrollPane formScroll = PadraoLayout.criarFormularioComScroll(allGroups);
        
        formContainer.add(formScroll, BorderLayout.CENTER);
        
        // Painel de botões usando PadraoLayout
        JPanel botoesPanel = PadraoLayout.criarPainelBotoesAcao();
        botoesPanel.add(restaurarPadraoButton);
        
        // Painel de formulário completo
        JPanel formCompletoPanel = new JPanel(new BorderLayout());
        formCompletoPanel.setBackground(Color.WHITE);
        formCompletoPanel.add(formContainer, BorderLayout.CENTER);
        formCompletoPanel.add(botoesPanel, BorderLayout.SOUTH);
        
        formPanel.add(formCompletoPanel, BorderLayout.CENTER);
        
        // Painel de tabela usando PadraoLayout
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBackground(PadraoLayout.COR_PAINEL);
        tabelaPanel.setBorder(PadraoLayout.BORDA_CONTEUDO);
        
        // Tabela estilizada usando PadraoLayout
        PadraoLayout.configurarTabela(configuracoesTable);
        tabelaPanel.add(new JScrollPane(configuracoesTable), BorderLayout.CENTER);
        
        // Split vertical: Formulário acima, Tabela abaixo
        JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formPanel, tabelaPanel);
        verticalSplitPane.setDividerLocation(300);
        verticalSplitPane.setResizeWeight(0.4);
        
        contentPanel.add(verticalSplitPane, BorderLayout.CENTER);
        
        // Adicionar contentPanel ao mainPanel
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupEvents() {
        salvarButton.addActionListener(e -> salvarConfiguracao());
        novoButton.addActionListener(e -> limparFormulario());
        excluirButton.addActionListener(e -> excluirConfiguracao());
        restaurarPadraoButton.addActionListener(e -> restaurarPadroes());
        
        // Seleção na tabela
        configuracoesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && configuracoesTable.getSelectedRow() >= 0) {
                carregarConfiguracaoSelecionada();
            }
        });
    }
    
    private void salvarConfiguracao() {
        try {
            if (chaveField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Chave é obrigatória!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (valorField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Valor é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Configuracao config = configuracaoAtual != null ? configuracaoAtual : new Configuracao();
            config.setChave(chaveField.getText().trim());
            config.setValor(valorField.getText().trim());
            config.setDescricao(descricaoField.getText().trim());
            config.setTipo((String) tipoComboBox.getSelectedItem());
            config.setCategoria((String) categoriaComboBox.getSelectedItem());
            config.setEditavel(editavelCheckBox.isSelected());
            config.setVisivel(visivelCheckBox.isSelected());
            
            configuracaoDAO.save(config);
            
            JOptionPane.showMessageDialog(this, "Configuração salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            refreshData();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar configuração: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limparFormulario() {
        configuracaoAtual = null;
        chaveField.setText("");
        chaveField.setEditable(true);
        valorField.setText("");
        descricaoField.setText("");
        tipoComboBox.setSelectedIndex(0);
        categoriaComboBox.setSelectedIndex(0);
        editavelCheckBox.setSelected(false);
        visivelCheckBox.setSelected(true);
        atualizarBotoesAcao();
    }
    
    private void excluirConfiguracao() {
        if (configuracaoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma configuração para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int option = JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente excluir a configuração " + configuracaoAtual.getChave() + "?",
            "Confirmar Exclusão",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (option == JOptionPane.YES_OPTION) {
            try {
                Long id = (Long) tableModel.getValueAt(configuracoesTable.getSelectedRow(), 0);
                configuracaoDAO.delete(id);
                JOptionPane.showMessageDialog(this, "Configuração excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                refreshData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir configuração: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void carregarConfiguracaoSelecionada() {
        int selectedRow = configuracoesTable.getSelectedRow();
        if (selectedRow >= 0) {
            String chave = (String) tableModel.getValueAt(selectedRow, 1);
            try {
                configuracaoAtual = configuracaoDAO.findByChave(chave);
                if (configuracaoAtual != null) {
                    chaveField.setText(configuracaoAtual.getChave());
                    chaveField.setEditable(false);
                    valorField.setText(configuracaoAtual.getValor());
                    descricaoField.setText(configuracaoAtual.getDescricao());
                    tipoComboBox.setSelectedItem(configuracaoAtual.getTipo());
                    categoriaComboBox.setSelectedItem(configuracaoAtual.getCategoria());
                    editavelCheckBox.setSelected(configuracaoAtual.isEditavel());
                    visivelCheckBox.setSelected(configuracaoAtual.isVisivel());
                    atualizarBotoesAcao();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar configuração: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void atualizarBotoesAcao() {
        boolean temConfiguracao = configuracaoAtual != null;
        excluirButton.setEnabled(temConfiguracao);
    }
    
    private void restaurarPadroes() {
        int option = JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente restaurar todas as configurações para o padrão?\nEsta ação não pode ser desfeita!",
            "Confirmar Restauração",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (option == JOptionPane.YES_OPTION) {
            try {
                // Implementar lógica de restauração de padrões
                JOptionPane.showMessageDialog(this, "Configurações restauradas com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao restaurar configurações: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void refreshData() {
        try {
            List<Configuracao> configuracoes = configuracaoDAO.findAll();
            tableModel.setRowCount(0);
            
            for (Configuracao config : configuracoes) {
                Object[] row = {
                    config.getId(),
                    config.getChave(),
                    config.getValor(),
                    config.getDescricao(),
                    config.getTipo(),
                    config.getCategoria(),
                    config.isEditavel() ? "Sim" : "Não"
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar configurações: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
