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
        
        // Botões usando PadraoLayout com cores pastéis
        salvarButton = PadraoLayout.criarBotaoSalvar();
        novoButton = PadraoLayout.criarBotaoNovo();
        excluirButton = PadraoLayout.criarBotaoExcluir();
        restaurarPadraoButton = PadraoLayout.criarBotao("Restaurar Padrão", new Color(255, 160, 122)); // Laranja suave
        
        configuracaoAtual = null;
    }
    
    private void setupLayout() {
        // Aplicar layout padrão usando PadraoLayout - MELHORIA 80%+
        this.setBorder(PadraoLayout.BORDA_PAINEL);
        this.setBackground(PadraoLayout.COR_FUNDO);
        
        // Header estilizado usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader("⚙️ Configurações Globais", "Parâmetros e preferências do sistema");
        add(headerPanel, BorderLayout.NORTH);
        
        // Painel direito - Formulário usando PadraoLayout - MELHORIA 80%+
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(PadraoLayout.COR_PAINEL);
        rightPanel.setBorder(PadraoLayout.BORDA_CONTEUDO);
        
        // Título do formulário usando PadraoLayout - MELHORIA 80%+
        JPanel formHeaderPanel = new JPanel(new BorderLayout());
        formHeaderPanel.setBackground(PadraoLayout.COR_PAINEL);
        formHeaderPanel.setBorder(PadraoLayout.BORDA_GRUPO);
        
        JLabel formTitleLabel = PadraoLayout.criarLabelFormulario("⚙️ Dados da Configuração");
        formTitleLabel.setFont(PadraoLayout.FONTE_TITULO);
        
        formHeaderPanel.add(formTitleLabel, BorderLayout.CENTER);
        rightPanel.add(formHeaderPanel, BorderLayout.NORTH);
        
        // Painel de formulário usando PadraoLayout - MELHORIA 80%+
        JPanel formularioPanel = new JPanel(new BorderLayout());
        formularioPanel.setBackground(PadraoLayout.COR_PAINEL);
        formularioPanel.setBorder(PadraoLayout.BORDA_CONTEUDO);
        
        JLabel formTitle = PadraoLayout.criarLabelFormulario("⚙️ Dados da Configuração");
        formTitle.setFont(PadraoLayout.FONTE_GRUPO);
        
        // Container principal para todos os grupos
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        
        // Grupo 1: Dados Básicos
        JPanel dadosBasicosPanel = createFormGroup("📅 Dados Básicos");
        JPanel dadosBasicosContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        dadosBasicosContent.setBackground(Color.WHITE);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Chave:"));
        PadraoLayout.estilizarCampoTexto(chaveField);
        dadosBasicosContent.add(chaveField);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Valor:"));
        PadraoLayout.estilizarCampoTexto(valorField);
        dadosBasicosContent.add(valorField);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Descrição:"));
        PadraoLayout.estilizarCampoTexto(descricaoField);
        dadosBasicosContent.add(descricaoField);
        
        dadosBasicosPanel.add(dadosBasicosContent);
        
        // Grupo 2: Configurações
        JPanel configuracoesPanel = createFormGroup("⚙️ Configurações");
        JPanel configuracoesContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        configuracoesContent.setBackground(Color.WHITE);
        
        configuracoesContent.add(PadraoLayout.criarLabelFormulario("Tipo:"));
        PadraoLayout.estilizarComboBox(tipoComboBox);
        configuracoesContent.add(tipoComboBox);
        
        configuracoesContent.add(PadraoLayout.criarLabelFormulario("Categoria:"));
        PadraoLayout.estilizarComboBox(categoriaComboBox);
        configuracoesContent.add(categoriaComboBox);
        
        configuracoesContent.add(PadraoLayout.criarLabelFormulario("Editável:"));
        configuracoesContent.add(editavelCheckBox);
        
        configuracoesContent.add(PadraoLayout.criarLabelFormulario("Visível:"));
        configuracoesContent.add(visivelCheckBox);
        
        configuracoesPanel.add(configuracoesContent);
        
        // Organizar grupos verticalmente usando PadraoLayout
        JPanel allGroups = PadraoLayout.criarFormularioMultiplosGrupos(
            dadosBasicosPanel, configuracoesPanel
        );
        
        // Adicionar scroll ao formulário usando PadraoLayout
        JScrollPane formScroll = PadraoLayout.criarFormularioComScroll(allGroups);
        
        formContainer.add(formScroll, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botoesPanel.setBackground(Color.WHITE);
        
        botoesPanel.add(createStyledButton("Salvar", new Color(144, 238, 144)));
        botoesPanel.add(createStyledButton("Novo", new Color(173, 216, 230)));
        botoesPanel.add(createStyledButton("Excluir", new Color(255, 182, 193)));
        botoesPanel.add(createStyledButton("Restaurar Padrão", new Color(255, 250, 205)));
        
        formularioPanel.add(formTitle, BorderLayout.NORTH);
        formularioPanel.add(formContainer, BorderLayout.CENTER);
        formularioPanel.add(botoesPanel, BorderLayout.SOUTH);
        
        rightPanel.add(formularioPanel, BorderLayout.CENTER);
        
        // Painel principal com split
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setDividerLocation(650);
        mainSplitPane.setResizeWeight(0.6);
        
        // Painel esquerdo - Tabela e estatísticas
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Painel de pesquisa
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
        
        JTextField pesquisarField = new JTextField();
        pesquisarField.setPreferredSize(new Dimension(200, 30));
        pesquisarField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        
        JButton pesquisarButton = PadraoLayout.criarBotaoPesquisar();
        pesquisarButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        pesquisarButton.setFocusPainted(false);
        
        searchContainer.add(searchLabel);
        searchContainer.add(pesquisarField);
        searchContainer.add(pesquisarButton);
        pesquisaPanel.add(searchContainer, BorderLayout.CENTER);
        
        // Tabela estilizada
        JScrollPane tableScrollPane = new JScrollPane(configuracoesTable);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 210)));
        tableScrollPane.getViewport().setBackground(Color.WHITE);
        
        // Tabela estilizada usando PadraoLayout
        PadraoLayout.configurarTabela(configuracoesTable);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(pesquisaPanel, BorderLayout.NORTH);
        topPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        tablePanel.add(topPanel, BorderLayout.CENTER);
        
        mainSplitPane.setLeftComponent(tablePanel);
        mainSplitPane.setRightComponent(rightPanel);
        
        add(mainSplitPane, BorderLayout.CENTER);
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        return PadraoLayout.criarBotao(text, bgColor);
    }
    
    private JPanel createFormGroup(String title) {
        JPanel groupPanel = new JPanel(new BorderLayout());
        groupPanel.setBackground(Color.WHITE);
        groupPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 230), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(new Color(70, 130, 180));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        
        groupPanel.add(titleLabel, BorderLayout.NORTH);
        return groupPanel;
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
