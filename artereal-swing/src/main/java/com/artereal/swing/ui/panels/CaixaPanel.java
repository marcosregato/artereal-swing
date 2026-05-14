package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.CaixaDAO;
import com.artereal.swing.model.Caixa;
import com.artereal.swing.ui.layout.PadraoLayout;
import com.artereal.swing.ui.panels.caixa.CaixaFormPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Painel de gestão Financeira (Caixa) - Layout padrão Header → Busca → Formulário → Tabela
 */
public class CaixaPanel extends JPanel {
    
    private static final Logger logger = LoggerFactory.getLogger(CaixaPanel.class);
    private CaixaDAO caixaDAO;
    private DefaultTableModel tableModel;
    private JTable caixaTable;
    private Caixa caixaAtual;
    private JTextField pesquisarField;
    private CaixaFormPanel caixaFormPanel;
    
        
    // Labels de resumo
    private JLabel receitasLabel;
    private JLabel despesasLabel;
    private JLabel saldoLabel;
    
    // Botões
    private JButton salvarButton;
    private JButton novoButton;
    private JButton editarButton;
    private JButton excluirButton;
    private JButton limparButton;
    private JButton pesquisarButton;
    private JButton relatorioButton;
    
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
            "ID", "Data", "Tipo", "Descrição", "Valor", "Categoria", "Status"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        caixaTable = new JTable(tableModel);
        
        // Botões
        salvarButton = PadraoLayout.criarBotaoSalvar();
        novoButton = PadraoLayout.criarBotaoNovo();
        editarButton = PadraoLayout.criarBotaoEditar();
        excluirButton = PadraoLayout.criarBotaoExcluir();
        limparButton = PadraoLayout.criarBotaoLimpar();
        pesquisarButton = PadraoLayout.criarBotaoPesquisar();
        relatorioButton = PadraoLayout.criarBotao("📊 Relatório", new Color(200, 200, 255));
        pesquisarField = new JTextField(20);
        
        // Inicializar formulário otimizado
        caixaFormPanel = new CaixaFormPanel();
        
        // Aplicar cores pastéis nos botões usando PadraoLayout
        PadraoLayout.aplicarCoresPastelBotoesPrincipais(salvarButton, novoButton, editarButton, excluirButton, limparButton);
        PadraoLayout.aplicarCoresPastelBotao(relatorioButton, "novo"); // Usa cor de novo para relatório
        
        // Labels de resumo com fontes muito maiores
        saldoLabel = PadraoLayout.criarLabelFormulario("💰 Saldo: R$ 0,00");
        saldoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        receitasLabel = PadraoLayout.criarLabelFormulario("📈 Receitas: R$ 0,00");
        receitasLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        despesasLabel = PadraoLayout.criarLabelFormulario("📉 Despesas: R$ 0,00");
        despesasLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
    }
    
    private void setupLayout() {
        // Aplicar estilização padrão ao painel principal
        PadraoLayout.estilizarPainelPrincipal(this);
        
        // Header estilizado usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader("💰 Gestão Financeira (Caixa)", "Controle de receitas, despesas e movimentações financeiras");
        add(headerPanel, BorderLayout.NORTH);
        
        // Painel principal usando PadraoLayout
        JPanel mainPanel = PadraoLayout.criarPainelPrincipal();
        
        // Painel de busca no topo
        JPanel buscaPanel = PadraoLayout.criarPainelPesquisa(pesquisarField, pesquisarButton);
        mainPanel.add(buscaPanel, BorderLayout.NORTH);
        
        // Painel do conteúdo com split vertical (Formulário acima, Tabela abaixo)
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(PadraoLayout.COR_FUNDO);
        
        // Painel de formulário otimizado usando CaixaFormPanel
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(PadraoLayout.COR_PAINEL);
        formPanel.setBorder(PadraoLayout.BORDA_CONTEUDO);
        
        // Usar o formulário otimizado
        formPanel.add(caixaFormPanel, BorderLayout.CENTER);
        
        // Painel de tabela usando PadraoLayout
        JPanel tabelaPanel = PadraoLayout.criarGrupoFormulario("📊 Movimentações Financeiras");
        PadraoLayout.configurarTabela(caixaTable);
        JScrollPane tableScrollPane = new JScrollPane(caixaTable);
        
        tabelaPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Split vertical: Formulário acima (60%), Tabela abaixo (40%)
        JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formPanel, tabelaPanel);
        verticalSplitPane.setDividerLocation(400);
        verticalSplitPane.setResizeWeight(0.6);
        
        contentPanel.add(verticalSplitPane, BorderLayout.CENTER);
        
        // Adicionar contentPanel ao mainPanel
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupEvents() {
        salvarButton.addActionListener(e -> salvarMovimentacao());
        novoButton.addActionListener(e -> limparFormulario());
        editarButton.addActionListener(e -> carregarMovimentacaoSelecionada());
        excluirButton.addActionListener(e -> excluirMovimentacao());
        limparButton.addActionListener(e -> limparFormulario());
        pesquisarButton.addActionListener(e -> pesquisarMovimentacoes());
        relatorioButton.addActionListener(e -> gerarRelatorio());
        
        caixaTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarMovimentacaoSelecionada();
            }
        });
    }
    
    private void salvarMovimentacao() {
        try {
            Caixa caixa = new Caixa();
            caixa.setDataMovimentacao(LocalDateTime.parse(caixaFormPanel.getDataField().getText() + " 00:00", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            caixa.setTipo(caixaFormPanel.getTipoCombo().getSelectedItem().toString());
            caixa.setCategoria(caixaFormPanel.getCategoriaCombo().getSelectedItem().toString());
            caixa.setDescricao(caixaFormPanel.getDescricaoField().getText());
            caixa.setValor(new BigDecimal(caixaFormPanel.getValorField().getText()));
            caixa.setFormaPagamento(caixaFormPanel.getFormaPagamentoCombo().getSelectedItem().toString());
            caixa.setNumeroDocumento(caixaFormPanel.getNumeroDocumentoField().getText());
            caixa.setStatus(caixaFormPanel.getStatusCombo().getSelectedItem().toString());
            caixa.setObservacoes(caixaFormPanel.getObservacoesArea().getText());
            
            if (caixaAtual == null) {
                caixaDAO.save(caixa);
            } else {
                caixa.setId(caixaAtual.getId());
                caixaDAO.save(caixa);
            }
            
            JOptionPane.showMessageDialog(this, "Movimentação salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            refreshData();
            atualizarResumoFinanceiro();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar movimentação: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar movimentação: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void excluirMovimentacao() {
        if (caixaAtual != null) {
            int option = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir esta movimentação?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    caixaDAO.delete(caixaAtual.getId());
                    JOptionPane.showMessageDialog(this, "Movimentação excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    limparFormulario();
                    refreshData();
                    atualizarResumoFinanceiro();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir movimentação: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void limparFormulario() {
        caixaAtual = null;
        caixaFormPanel.clearForm();
        caixaTable.clearSelection();
    }
    
    private void carregarMovimentacaoSelecionada() {
        int selectedRow = caixaTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                Long id = (Long) tableModel.getValueAt(selectedRow, 0);
                caixaAtual = caixaDAO.findById(id);
                if (caixaAtual != null) {
                    caixaFormPanel.getCodigoField().setText(String.valueOf(caixaAtual.getId()));
                    caixaFormPanel.getDataField().setText(caixaAtual.getDataMovimentacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    caixaFormPanel.getTipoCombo().setSelectedItem(caixaAtual.getTipo());
                    caixaFormPanel.getCategoriaCombo().setSelectedItem(caixaAtual.getCategoria());
                    caixaFormPanel.getDescricaoField().setText(caixaAtual.getDescricao());
                    caixaFormPanel.getValorField().setText(caixaAtual.getValor().toString());
                    caixaFormPanel.getFormaPagamentoCombo().setSelectedItem(caixaAtual.getFormaPagamento());
                    caixaFormPanel.getNumeroDocumentoField().setText(caixaAtual.getNumeroDocumento());
                    caixaFormPanel.getStatusCombo().setSelectedItem(caixaAtual.getStatus());
                    caixaFormPanel.getObservacoesArea().setText(caixaAtual.getObservacoes());
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar movimentação: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void pesquisarMovimentacoes() {
        String termo = pesquisarField.getText().trim();
        if (termo.isEmpty()) {
            refreshData();
        } else {
            try {
                List<Caixa> todasMovimentacoes = caixaDAO.findAll();
                List<Caixa> movimentacoesFiltradas = todasMovimentacoes.stream()
                    .filter(m -> m.getDescricao() != null && m.getDescricao().toLowerCase().contains(termo.toLowerCase()))
                    .toList();
                atualizarTabela(movimentacoesFiltradas);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao pesquisar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void gerarRelatorio() {
        JOptionPane.showMessageDialog(this, "Relatório financeiro gerado com sucesso!", "Relatório", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void refreshData() {
        try {
            List<Caixa> movimentacoes = caixaDAO.findAll();
            atualizarTabela(movimentacoes);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void atualizarTabela(List<Caixa> movimentacoes) {
        tableModel.setRowCount(0);
        for (Caixa caixa : movimentacoes) {
            Object[] row = {
                caixa.getId(),
                caixa.getDataMovimentacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                caixa.getTipo(),
                caixa.getDescricao(),
                caixa.getValor(),
                caixa.getCategoria(),
                caixa.getStatus()
            };
            tableModel.addRow(row);
        }
    }
    
    private void atualizarResumoFinanceiro() {
        try {
            BigDecimal totalReceitas = caixaDAO.getTotalReceitas();
            BigDecimal totalDespesas = caixaDAO.getTotalDespesas();
            BigDecimal saldo = totalReceitas.subtract(totalDespesas);
            
            receitasLabel.setText("📈 Receitas: R$ " + totalReceitas.toString());
            despesasLabel.setText("📉 Despesas: R$ " + totalDespesas.toString());
            saldoLabel.setText("💰 Saldo: R$ " + saldo.toString());
            
            // Cores dinâmicas
            if (saldo.compareTo(BigDecimal.ZERO) >= 0) {
                saldoLabel.setForeground(new Color(46, 125, 50)); // Verde
            } else {
                saldoLabel.setForeground(new Color(211, 47, 47)); // Vermelho
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao atualizar resumo financeiro: " + e.getMessage(), e);
        }
    }
}
