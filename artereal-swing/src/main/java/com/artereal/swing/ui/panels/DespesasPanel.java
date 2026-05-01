package com.artereal.swing.ui.panels;

import com.artereal.swing.ui.layout.PadraoLayout;
import com.artereal.swing.dao.DespesaDAO;
import com.artereal.swing.model.Despesa;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Date;

/**
 * Painel de gestão de despesas do sistema ArteReal
 */
public class DespesasPanel extends JPanel {
    
    // Componentes do formulário
    private JTextField descricaoField;
    private JTextField valorField;
    private JTextField dataField;
    private JTextField categoriaField;
    private JTextField fornecedorField;
    private JTextField numeroDocumentoField;
    
    // Componentes de busca
    private JTextField pesquisarField;
    private JButton pesquisarButton;
    
    // Componentes da tabela
    private JTable despesasTable;
    private DefaultTableModel tableModel;
    
    // Botões de ação
    private JButton salvarButton;
    private JButton novoButton;
    private JButton editarButton;
    private JButton excluirButton;
    private JButton limparButton;
    
    // DAO
    private DespesaDAO despesaDAO;
    
    public DespesasPanel() {
        despesaDAO = new DespesaDAO();
        initializeComponents();
        setupLayout();
        setupEvents();
        refreshData();
    }
    
    /**
     * Inicializa os componentes do painel
     */
    private void initializeComponents() {
        // Campos do formulário
        descricaoField = new JTextField(50);
        valorField = new JTextField(15);
        dataField = new JTextField(12);
        categoriaField = new JTextField(30);
        fornecedorField = new JTextField(40);
        numeroDocumentoField = new JTextField(20);
        
        // Componentes de busca
        pesquisarField = new JTextField(30);
        pesquisarButton = new JButton("🔍 Pesquisar");
        
        // Tabela
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Descrição");
        tableModel.addColumn("Valor");
        tableModel.addColumn("Data");
        tableModel.addColumn("Categoria");
        tableModel.addColumn("Fornecedor");
        tableModel.addColumn("Nº Documento");
        
        despesasTable = new JTable(tableModel);
        
        // Botões de ação
        salvarButton = PadraoLayout.criarBotaoSalvar();
        novoButton = PadraoLayout.criarBotaoNovo();
        editarButton = PadraoLayout.criarBotaoEditar();
        excluirButton = PadraoLayout.criarBotaoExcluir();
        limparButton = PadraoLayout.criarBotaoLimpar();
    }
    
    /**
     * Configura o layout do painel
     */
    private void setupLayout() {
        // Aplicar layout padrão usando PadraoLayout
        PadraoLayout.aplicarLayoutPadrao(this);
        
        // Header estilizado usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader("💰 Despesas", "Gestão de despesas e controle financeiro");
        add(headerPanel, BorderLayout.NORTH);
        
        // Painel principal usando PadraoLayout
        JPanel mainPanel = PadraoLayout.criarPainelPrincipal();
        
        // Painel de busca no topo
        JPanel buscaPanel = PadraoLayout.criarPainelPesquisa(pesquisarField, pesquisarButton);
        mainPanel.add(buscaPanel, BorderLayout.NORTH);
        
        // Painel do conteúdo com split vertical (Formulário acima, Tabela abaixo)
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(PadraoLayout.COR_FUNDO);
        
        // Painel de formulário usando PadraoLayout
        JPanel formPanel = PadraoLayout.criarGrupoFormulario("📝 Dados da Despesa");
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        
        // Container principal com layout vertical para colocar painéis um debaixo do outro
        JPanel formContent = new JPanel();
        formContent.setLayout(new BoxLayout(formContent, BoxLayout.Y_AXIS));
        formContent.setBackground(Color.WHITE);
        formContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // SEÇÃO 1: Dados básicos da despesa
        JPanel dadosBasicosPanel = new JPanel(new BorderLayout());
        dadosBasicosPanel.setBackground(Color.WHITE);
        dadosBasicosPanel.setBorder(BorderFactory.createTitledBorder("💵 Informações da Despesa"));
        dadosBasicosPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel dadosBasicosContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        dadosBasicosContent.setBackground(Color.WHITE);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Descrição:"));
        dadosBasicosContent.add(descricaoField);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Valor:"));
        dadosBasicosContent.add(valorField);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Data:"));
        dadosBasicosContent.add(dataField);
        
        dadosBasicosPanel.add(dadosBasicosContent, BorderLayout.CENTER);
        formContent.add(dadosBasicosPanel);
        formContent.add(Box.createVerticalStrut(15)); // Espaçamento entre painéis
        
        // SEÇÃO 2: Detalhes adicionais
        JPanel detalhesPanel = new JPanel(new BorderLayout());
        detalhesPanel.setBackground(Color.WHITE);
        detalhesPanel.setBorder(BorderFactory.createTitledBorder("📋 Detalhes Adicionais"));
        detalhesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel detalhesContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        detalhesContent.setBackground(Color.WHITE);
        
        detalhesContent.add(PadraoLayout.criarLabelFormulario("Categoria:"));
        detalhesContent.add(categoriaField);
        
        detalhesContent.add(PadraoLayout.criarLabelFormulario("Fornecedor:"));
        detalhesContent.add(fornecedorField);
        
        detalhesContent.add(PadraoLayout.criarLabelFormulario("Nº Documento:"));
        detalhesContent.add(numeroDocumentoField);
        
        detalhesPanel.add(detalhesContent, BorderLayout.CENTER);
        formContent.add(detalhesPanel);
        formContent.add(Box.createVerticalStrut(15)); // Espaçamento após o segundo painel
        
        // Painel de botões
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botoesPanel.setBackground(Color.WHITE);
        botoesPanel.add(salvarButton);
        botoesPanel.add(novoButton);
        botoesPanel.add(editarButton);
        botoesPanel.add(excluirButton);
        botoesPanel.add(limparButton);
        
        formContainer.add(formContent, BorderLayout.CENTER);
        formContainer.add(botoesPanel, BorderLayout.SOUTH);
        
        formPanel.add(formContainer, BorderLayout.CENTER);
        
        // Painel de tabela usando PadraoLayout
        JPanel tabelaPanel = PadraoLayout.criarGrupoFormulario("📊 Registros de Despesas");
        PadraoLayout.configurarTabela(despesasTable);
        JScrollPane tableScrollPane = new JScrollPane(despesasTable);
        
        tabelaPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Estrutura vertical: Formulário → Tabela
        JPanel verticalPanel = new JPanel(new BorderLayout());
        verticalPanel.setBackground(PadraoLayout.COR_FUNDO);
        verticalPanel.add(formPanel, BorderLayout.NORTH);
        verticalPanel.add(tabelaPanel, BorderLayout.CENTER);
        
        contentPanel.add(verticalPanel, BorderLayout.CENTER);
        
        // Adicionar contentPanel ao mainPanel
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    /**
     * Configura os eventos dos componentes
     */
    private void setupEvents() {
        salvarButton.addActionListener(e -> salvarDespesa());
        novoButton.addActionListener(e -> limparFormulario());
        editarButton.addActionListener(e -> carregarDespesaSelecionada());
        excluirButton.addActionListener(e -> excluirDespesa());
        limparButton.addActionListener(e -> limparFormulario());
        pesquisarButton.addActionListener(e -> pesquisarDespesas());
        
        despesasTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarDespesaSelecionada();
            }
        });
    }
    
    /**
     * Salva uma despesa
     */
    private void salvarDespesa() {
        try {
            String descricao = descricaoField.getText().trim();
            String valorText = valorField.getText().trim();
            String data = dataField.getText().trim();
            String categoria = categoriaField.getText().trim();
            String fornecedor = fornecedorField.getText().trim();
            String numeroDocumento = numeroDocumentoField.getText().trim();
            
            if (descricao.isEmpty()) {
                JOptionPane.showMessageDialog(this, "A descrição é obrigatória!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (valorText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "O valor é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double valor;
            try {
                valor = Double.parseDouble(valorText.replace(",", "."));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Despesa despesa = new Despesa();
            despesa.setDescricao(descricao);
            despesa.setValor(valor);
            despesa.setData(data.isEmpty() ? new Date() : java.sql.Date.valueOf(data));
            despesa.setCategoria(categoria.isEmpty() ? "Geral" : categoria);
            despesa.setFornecedor(fornecedor.isEmpty() ? "Não informado" : fornecedor);
            despesa.setNumeroDocumento(numeroDocumento);
            
            despesaDAO.save(despesa);
            
            JOptionPane.showMessageDialog(this, "Despesa salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            refreshData();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar despesa: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Exclui uma despesa
     */
    private void excluirDespesa() {
        int selectedRow = despesasTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma despesa para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir esta despesa?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Long id = (Long) tableModel.getValueAt(selectedRow, 0);
                despesaDAO.delete(id);
                JOptionPane.showMessageDialog(this, "Despesa excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                refreshData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir despesa: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Carrega os dados da despesa selecionada no formulário
     */
    private void carregarDespesaSelecionada() {
        int selectedRow = despesasTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        descricaoField.setText((String) tableModel.getValueAt(selectedRow, 1));
        valorField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 2)));
        dataField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 3)));
        categoriaField.setText((String) tableModel.getValueAt(selectedRow, 4));
        fornecedorField.setText((String) tableModel.getValueAt(selectedRow, 5));
        numeroDocumentoField.setText((String) tableModel.getValueAt(selectedRow, 6));
    }
    
    /**
     * Limpa o formulário
     */
    private void limparFormulario() {
        descricaoField.setText("");
        valorField.setText("");
        dataField.setText("");
        categoriaField.setText("");
        fornecedorField.setText("");
        numeroDocumentoField.setText("");
        despesasTable.clearSelection();
    }
    
    /**
     * Pesquisa despesas
     */
    private void pesquisarDespesas() {
        String termo = pesquisarField.getText().trim();
        try {
            List<Despesa> despesas;
            if (termo.isEmpty()) {
                despesas = despesaDAO.findAll();
            } else {
                despesas = despesaDAO.findByDescricao(termo);
            }
            atualizarTabela(despesas);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao pesquisar despesas: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Atualiza os dados da tabela
     */
    public void refreshData() {
        try {
            List<Despesa> despesas = despesaDAO.findAll();
            atualizarTabela(despesas);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar despesas: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Atualiza a tabela com a lista de despesas
     */
    private void atualizarTabela(List<Despesa> despesas) {
        tableModel.setRowCount(0);
        for (Despesa despesa : despesas) {
            Object[] row = {
                despesa.getId(),
                despesa.getDescricao(),
                String.format("R$ %.2f", despesa.getValor()),
                despesa.getData(),
                despesa.getCategoria(),
                despesa.getFornecedor(),
                despesa.getNumeroDocumento()
            };
            tableModel.addRow(row);
        }
    }
}
