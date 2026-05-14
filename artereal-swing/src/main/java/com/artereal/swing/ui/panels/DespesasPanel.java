package com.artereal.swing.ui.panels;

import com.artereal.swing.ui.layout.PadraoLayout;
import com.artereal.swing.dao.DespesaDAO;
import com.artereal.swing.model.Despesa;
import com.artereal.swing.ui.panels.despesas.DespesasFormPanel;

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
    private DespesasFormPanel despesasFormPanel;
    private JTable despesasTable;
    
    // Componentes de busca
    private JTextField pesquisarField;
    private JButton pesquisarButton;
    
    // Componentes da tabela
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
        // Inicializar formulário otimizado
        despesasFormPanel = new DespesasFormPanel();
        
        // Componentes de busca
        pesquisarField = new JTextField(30);
        PadraoLayout.estilizarCampoTexto(pesquisarField);
        pesquisarField.setEditable(true);
        pesquisarField.setEnabled(true);
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
        
        // Painel de formulário otimizado usando DespesasFormPanel
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(PadraoLayout.COR_PAINEL);
        formPanel.setBorder(PadraoLayout.BORDA_CONTEUDO);
        
        // Usar o formulário otimizado
        formPanel.add(despesasFormPanel, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botoesPanel.setBackground(Color.WHITE);
        botoesPanel.add(salvarButton);
        botoesPanel.add(novoButton);
        botoesPanel.add(editarButton);
        botoesPanel.add(excluirButton);
        botoesPanel.add(limparButton);
        
        formPanel.add(despesasFormPanel, BorderLayout.CENTER);
        
        // Painel de tabela usando PadraoLayout
        JPanel tabelaPanel = PadraoLayout.criarGrupoFormulario("📊 Registros de Despesas");
        PadraoLayout.configurarTabela(despesasTable);
        JScrollPane tableScrollPane = new JScrollPane(despesasTable);
        
        tabelaPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Split vertical: Formulário acima (40%), Tabela abaixo (60%)
        JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formPanel, tabelaPanel);
        verticalSplitPane.setDividerLocation(300);
        verticalSplitPane.setResizeWeight(0.4);
        
        contentPanel.add(verticalSplitPane, BorderLayout.CENTER);
        
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
            String descricao = despesasFormPanel.getDescricaoField().getText().trim();
            String valorText = despesasFormPanel.getValorField().getText().trim();
            String data = despesasFormPanel.getDataField().getText().trim();
            String categoria = despesasFormPanel.getCategoriaField().getText().trim();
            String fornecedor = despesasFormPanel.getFornecedorField().getText().trim();
            String numeroDocumento = despesasFormPanel.getNumeroDocumentoField().getText().trim();
            
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
        if (selectedRow >= 0) {
            despesasFormPanel.getDescricaoField().setText(String.valueOf(tableModel.getValueAt(selectedRow, 1)));
            despesasFormPanel.getValorField().setText(String.valueOf(tableModel.getValueAt(selectedRow, 2)));
            despesasFormPanel.getDataField().setText(String.valueOf(tableModel.getValueAt(selectedRow, 3)));
            despesasFormPanel.getCategoriaField().setText((String) tableModel.getValueAt(selectedRow, 4));
            despesasFormPanel.getFornecedorField().setText((String) tableModel.getValueAt(selectedRow, 5));
            despesasFormPanel.getNumeroDocumentoField().setText((String) tableModel.getValueAt(selectedRow, 6));
        }
    }
    
    /**
     * Limpa o formulário
     */
    private void limparFormulario() {
        despesasFormPanel.clearForm();
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
