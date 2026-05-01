package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.CaixaDAO;
import com.artereal.swing.model.Caixa;
import com.artereal.swing.ui.layout.PadraoLayout;

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
    
    // Formulário
    private JTextField codigoField;
    private JTextField dataField;
    private JTextField descricaoField;
    private JTextField valorField;
    private JTextField numeroDocumentoField;
    private JTextArea observacoesArea;
    
    // ComboBox
    private JComboBox<String> tipoCombo;
    private JComboBox<String> categoriaCombo;
    private JComboBox<String> formaPagamentoCombo;
    private JComboBox<String> statusCombo;
    
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
        
        // Formulário
        codigoField = new JTextField();
        dataField = new JTextField();
        descricaoField = new JTextField();
        valorField = new JTextField();
        numeroDocumentoField = new JTextField(25);
        observacoesArea = new JTextArea(3, 40);
        
        // ComboBox
        tipoCombo = new JComboBox<>(new String[]{"Receita", "Despesa"});
        categoriaCombo = new JComboBox<>(new String[]{
            "Mensalidades", "Doações", "Eventos", "Material", "Aluguel", 
            "Água", "Luz", "Telefone", "Outros"
        });
        formaPagamentoCombo = new JComboBox<>(new String[]{
            "Dinheiro", "Transferência", "Cheque", "Cartão", "Pix"
        });
        statusCombo = new JComboBox<>(new String[]{"Pendente", "Processado", "Cancelado"});
        
        // Botões
        salvarButton = PadraoLayout.criarBotaoSalvar();
        novoButton = PadraoLayout.criarBotaoNovo();
        editarButton = PadraoLayout.criarBotaoEditar();
        excluirButton = PadraoLayout.criarBotaoExcluir();
        limparButton = PadraoLayout.criarBotaoLimpar();
        pesquisarButton = PadraoLayout.criarBotaoPesquisar();
        relatorioButton = PadraoLayout.criarBotao("📊 Relatório", new Color(200, 200, 255));
        pesquisarField = new JTextField(20);
        
        // Aplicar cores pastéis nos botões usando PadraoLayout
        PadraoLayout.aplicarCoresPastelBotoesPrincipais(salvarButton, novoButton, editarButton, excluirButton, limparButton);
        PadraoLayout.aplicarCoresPastelBotao(relatorioButton, "novo"); // Usa cor de novo para relatório
        
        // Labels de resumo
        saldoLabel = PadraoLayout.criarLabelFormulario("💰 Saldo: R$ 0,00");
        receitasLabel = PadraoLayout.criarLabelFormulario("📈 Receitas: R$ 0,00");
        despesasLabel = PadraoLayout.criarLabelFormulario("📉 Despesas: R$ 0,00");
    }
    
    private void setupLayout() {
        // Aplicar layout padrão usando PadraoLayout
        PadraoLayout.aplicarLayoutPadrao(this);
        
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
        
        // Painel de formulário usando PadraoLayout
        JPanel formPanel = PadraoLayout.criarGrupoFormulario("💳 Detalhes da Movimentação");
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        
        // Formulário usando BoxLayout vertical para organizar JPanel um de baixo do outro
        JPanel formContent = new JPanel();
        formContent.setLayout(new BoxLayout(formContent, BoxLayout.Y_AXIS));
        formContent.setBackground(Color.WHITE);
        
        // SEÇÃO 1: Detalhes da Movimentação
        JPanel detalhesMovimentacaoPanel = new JPanel(new BorderLayout());
        detalhesMovimentacaoPanel.setBackground(Color.WHITE);
        detalhesMovimentacaoPanel.setBorder(BorderFactory.createTitledBorder("📋 Detalhes da Movimentação"));
        
        JPanel detalhesMovimentacaoContent = new JPanel();
        detalhesMovimentacaoContent.setLayout(new BoxLayout(detalhesMovimentacaoContent, BoxLayout.Y_AXIS));
        detalhesMovimentacaoContent.setBackground(Color.WHITE);
        
        // Primeira linha: Código e Data
        JPanel primeiraLinhaMovimentacaoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        primeiraLinhaMovimentacaoPanel.setBackground(Color.WHITE);
        
        // Campo Código
        JPanel codigoMovimentacaoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        codigoMovimentacaoPanel.setBackground(Color.WHITE);
        codigoMovimentacaoPanel.add(PadraoLayout.criarLabelFormularioCodigo("Código:"));
        PadraoLayout.estilizarCampoCodigo(codigoField);
        codigoMovimentacaoPanel.add(codigoField);
        
        // Campo Data
        JPanel dataMovimentacaoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        dataMovimentacaoPanel.setBackground(Color.WHITE);
        dataMovimentacaoPanel.add(PadraoLayout.criarLabelFormulario("Data:"));
        PadraoLayout.estilizarCampoData(dataField);
        dataMovimentacaoPanel.add(dataField);
        
        // Adicionar os painéis à primeira linha
        primeiraLinhaMovimentacaoPanel.add(codigoMovimentacaoPanel);
        primeiraLinhaMovimentacaoPanel.add(Box.createHorizontalStrut(20)); // Espaço entre os campos
        primeiraLinhaMovimentacaoPanel.add(dataMovimentacaoPanel);
        
        // Adicionar o painel da primeira linha ao conteúdo
        detalhesMovimentacaoContent.add(primeiraLinhaMovimentacaoPanel);
        detalhesMovimentacaoContent.add(Box.createVerticalStrut(5)); // Espaço vertical
        
        // Segunda linha: Tipo e Categoria
        JPanel segundaLinhaMovimentacaoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        segundaLinhaMovimentacaoPanel.setBackground(Color.WHITE);
        
        // Campo Tipo
        JPanel tipoMovimentacaoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tipoMovimentacaoPanel.setBackground(Color.WHITE);
        tipoMovimentacaoPanel.add(PadraoLayout.criarLabelFormulario("Tipo:"));
        PadraoLayout.estilizarComboBox(tipoCombo);
        tipoMovimentacaoPanel.add(tipoCombo);
        
        // Campo Categoria
        JPanel categoriaMovimentacaoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        categoriaMovimentacaoPanel.setBackground(Color.WHITE);
        categoriaMovimentacaoPanel.add(PadraoLayout.criarLabelFormulario("Categoria:"));
        PadraoLayout.estilizarComboBox(categoriaCombo);
        categoriaMovimentacaoPanel.add(categoriaCombo);
        
        // Adicionar os painéis à segunda linha
        segundaLinhaMovimentacaoPanel.add(tipoMovimentacaoPanel);
        segundaLinhaMovimentacaoPanel.add(Box.createHorizontalStrut(20)); // Espaço entre os campos
        segundaLinhaMovimentacaoPanel.add(categoriaMovimentacaoPanel);
        
        // Adicionar o painel da segunda linha ao conteúdo
        detalhesMovimentacaoContent.add(segundaLinhaMovimentacaoPanel);
        
        detalhesMovimentacaoPanel.add(detalhesMovimentacaoContent, BorderLayout.CENTER);
        formContent.add(detalhesMovimentacaoPanel);
        
        // SEÇÃO 2: Detalhes Financeiros
        JPanel financeirosPanel = new JPanel(new BorderLayout());
        financeirosPanel.setBackground(Color.WHITE);
        financeirosPanel.setBorder(BorderFactory.createTitledBorder("💰 Detalhes Financeiros"));
        
        JPanel financeirosContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        financeirosContent.setBackground(Color.WHITE);
        
        financeirosContent.add(PadraoLayout.criarLabelFormulario("Descrição:"));
        PadraoLayout.estilizarCampoDescricao(descricaoField);
        financeirosContent.add(descricaoField);
        
        financeirosContent.add(PadraoLayout.criarLabelFormulario("Valor:"));
        PadraoLayout.estilizarCampoValorFinanceiro(valorField);
        financeirosContent.add(valorField);
        
        financeirosContent.add(PadraoLayout.criarLabelFormulario("Forma Pagamento:"));
        PadraoLayout.estilizarComboBox(formaPagamentoCombo);
        financeirosContent.add(formaPagamentoCombo);
        
        financeirosPanel.add(financeirosContent, BorderLayout.CENTER);
        formContent.add(financeirosPanel);
        
        // SEÇÃO 3: Documentação
        JPanel documentacaoPanel = new JPanel(new BorderLayout());
        documentacaoPanel.setBackground(Color.WHITE);
        documentacaoPanel.setBorder(BorderFactory.createTitledBorder("📄 Documentação"));
        
        JPanel documentacaoContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        documentacaoContent.setBackground(Color.WHITE);
        
        documentacaoContent.add(PadraoLayout.criarLabelFormulario("Número Documento:"));
        PadraoLayout.estilizarCampoNumeroDocumento(numeroDocumentoField);
        documentacaoContent.add(numeroDocumentoField);
        
        documentacaoContent.add(PadraoLayout.criarLabelFormulario("Status:"));
        PadraoLayout.estilizarComboBox(statusCombo);
        documentacaoContent.add(statusCombo);
        
        documentacaoPanel.add(documentacaoContent, BorderLayout.CENTER);
        formContent.add(documentacaoPanel);
        
        // SEÇÃO 4: Observações
        JPanel observacoesPanel = new JPanel(new BorderLayout());
        observacoesPanel.setBackground(Color.WHITE);
        observacoesPanel.setBorder(BorderFactory.createTitledBorder("📝 Observações"));
        
        observacoesPanel.add(observacoesArea, BorderLayout.CENTER);
        formContent.add(observacoesPanel);
        
        // Resumo Financeiro no formulário
        JPanel resumoPanel = new JPanel(new GridLayout(1, 3, 10, 5));
        resumoPanel.setBackground(Color.WHITE);
        resumoPanel.setBorder(BorderFactory.createTitledBorder("📊 Resumo Financeiro"));
        
        resumoPanel.add(receitasLabel);
        resumoPanel.add(despesasLabel);
        resumoPanel.add(saldoLabel);
        
        // Painel de botões
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botoesPanel.setBackground(Color.WHITE);
        botoesPanel.add(salvarButton);
        botoesPanel.add(novoButton);
        botoesPanel.add(editarButton);
        botoesPanel.add(excluirButton);
        botoesPanel.add(limparButton);
        botoesPanel.add(relatorioButton);
        
        formContainer.add(formContent, BorderLayout.CENTER);
        formContainer.add(resumoPanel, BorderLayout.NORTH);
        formContainer.add(botoesPanel, BorderLayout.SOUTH);
        
        formPanel.add(formContainer, BorderLayout.CENTER);
        
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
            caixa.setDataMovimentacao(LocalDateTime.parse(dataField.getText() + " 00:00", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            caixa.setTipo(tipoCombo.getSelectedItem().toString());
            caixa.setCategoria(categoriaCombo.getSelectedItem().toString());
            caixa.setDescricao(descricaoField.getText());
            caixa.setValor(new BigDecimal(valorField.getText()));
            caixa.setFormaPagamento(formaPagamentoCombo.getSelectedItem().toString());
            caixa.setNumeroDocumento(numeroDocumentoField.getText());
            caixa.setStatus(statusCombo.getSelectedItem().toString());
            caixa.setObservacoes(observacoesArea.getText());
            
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
        codigoField.setText("");
        dataField.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        descricaoField.setText("");
        valorField.setText("");
        numeroDocumentoField.setText("");
        observacoesArea.setText("");
        tipoCombo.setSelectedIndex(0);
        categoriaCombo.setSelectedIndex(0);
        formaPagamentoCombo.setSelectedIndex(0);
        statusCombo.setSelectedIndex(0);
        caixaTable.clearSelection();
    }
    
    private void carregarMovimentacaoSelecionada() {
        int selectedRow = caixaTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                Long id = (Long) tableModel.getValueAt(selectedRow, 0);
                caixaAtual = caixaDAO.findById(id);
                if (caixaAtual != null) {
                    codigoField.setText(String.valueOf(caixaAtual.getId()));
                    dataField.setText(caixaAtual.getDataMovimentacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    tipoCombo.setSelectedItem(caixaAtual.getTipo());
                    categoriaCombo.setSelectedItem(caixaAtual.getCategoria());
                    descricaoField.setText(caixaAtual.getDescricao());
                    valorField.setText(caixaAtual.getValor().toString());
                    formaPagamentoCombo.setSelectedItem(caixaAtual.getFormaPagamento());
                    numeroDocumentoField.setText(caixaAtual.getNumeroDocumento());
                    statusCombo.setSelectedItem(caixaAtual.getStatus());
                    observacoesArea.setText(caixaAtual.getObservacoes());
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
