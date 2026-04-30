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
 * Painel de gestão Financeira (Caixa)
 */
public class CaixaPanel extends JPanel {
    
    private static final Logger logger = LoggerFactory.getLogger(CaixaPanel.class);
    private CaixaDAO caixaDAO;
    private DefaultTableModel tableModel;
    private JTable caixaTable;
    private Caixa caixaAtual;
    
    // Formulário
    private JTextField codigoField;
    private JComboBox<String> tipoCombo;
    private JTextField categoriaField;
    private JTextField descricaoField;
    private JTextField valorField;
    private JTextField dataMovimentacaoField;
    private JTextField responsavelField;
    private JComboBox<String> formaPagamentoCombo;
    private JTextField numeroDocumentoField;
    private JComboBox<String> statusCombo;
    private JTextArea observacoesArea;
    
    // Botões
    private JButton salvarButton;
    private JButton novoButton;
    private JButton editarButton;
    private JButton excluirButton;
    private JButton limparButton;
    private JButton pesquisarButton;
    private JButton relatorioButton;
    private JTextField pesquisarField;
    
    // Labels de resumo
    private JLabel saldoLabel;
    private JLabel receitasLabel;
    private JLabel despesasLabel;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
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
            "Código", "Tipo", "Descrição", "Valor", "Data", "Status"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        caixaTable = new JTable(tableModel);
        
        // Formulário
        codigoField = new JTextField(10);
        codigoField.setEditable(false);
        
        tipoCombo = new JComboBox<>(new String[]{"RECEITA", "DESPESA"});
        categoriaField = new JTextField(20);
        descricaoField = new JTextField(40);
        valorField = new JTextField(15);
        dataMovimentacaoField = new JTextField(16);
        responsavelField = new JTextField(30);
        
        formaPagamentoCombo = new JComboBox<>(new String[]{
            "DINHEIRO", "CHEQUE", "TRANSFERENCIA", "PIX", "BOLETO", "CARTAO"
        });
        
        numeroDocumentoField = new JTextField(15);
        
        statusCombo = new JComboBox<>(new String[]{
            "PAGO", "PENDENTE", "CANCELADO"
        });
        
        observacoesArea = new JTextArea(3, 40);
        observacoesArea.setLineWrap(true);
        observacoesArea.setWrapStyleWord(true);
        
        // Botões usando PadraoLayout com cores pastéis
        salvarButton = PadraoLayout.criarBotaoSalvar();
        novoButton = PadraoLayout.criarBotaoNovo();
        editarButton = PadraoLayout.criarBotaoEditar();
        excluirButton = PadraoLayout.criarBotaoExcluir();
        limparButton = PadraoLayout.criarBotaoLimpar();
        pesquisarButton = PadraoLayout.criarBotaoPesquisar();
        relatorioButton = PadraoLayout.criarBotao("Relatório", new Color(200, 200, 255)); // Azul claro pastel
        pesquisarField = new JTextField(20);
        
        // Labels de resumo usando PadraoLayout - META 100% CONFORMIDADE
        saldoLabel = PadraoLayout.criarLabelFormulario("💰 Saldo: R$ 0,00");
        receitasLabel = PadraoLayout.criarLabelFormulario("📈 Receitas: R$ 0,00");
        despesasLabel = PadraoLayout.criarLabelFormulario("📉 Despesas: R$ 0,00");
    }
    
    private void setupLayout() {
        // Aplicar layout padrão usando PadraoLayout - META 100% CONFORMIDADE
        this.setBorder(PadraoLayout.BORDA_PAINEL);
        this.setBackground(PadraoLayout.COR_FUNDO);
        
        // Header estilizado usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader("💰 Gestão Financeira (Caixa)", "Controle de receitas, despesas e movimentações financeiras");
        add(headerPanel, BorderLayout.NORTH);
        
                
        // Painel esquerdo - Tabela e resumo usando PadraoLayout - META 100% CONFORMIDADE
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(PadraoLayout.COR_PAINEL);
        leftPanel.setBorder(PadraoLayout.BORDA_CONTEUDO);
        
        // Painel de pesquisa usando PadraoLayout
        JPanel pesquisaPanel = PadraoLayout.criarPainelPesquisa(pesquisarField, pesquisarButton);
        
        // Painel de resumo financeiro usando PadraoLayout - META 100% CONFORMIDADE
        JPanel resumoPanel = new JPanel(new GridLayout(1, 3, 15, 10));
        resumoPanel.setBackground(PadraoLayout.COR_PAINEL);
        resumoPanel.setBorder(PadraoLayout.BORDA_GRUPO);
        
        // Painel de receitas usando PadraoLayout - META 100% CONFORMIDADE
        JPanel receitasPanel = new JPanel(new BorderLayout());
        receitasPanel.setBackground(PadraoLayout.COR_PAINEL);
        receitasPanel.setBorder(PadraoLayout.BORDA_CONTEUDO);
        JLabel receitasTitleLabel = PadraoLayout.criarLabelFormulario("📈 RECEITAS");
        receitasTitleLabel.setFont(PadraoLayout.FONTE_GRUPO);
        receitasLabel = PadraoLayout.criarLabelFormulario("📈 RECEITAS");
        receitasLabel.setFont(PadraoLayout.FONTE_GRUPO);
        receitasPanel.add(receitasTitleLabel, BorderLayout.NORTH);
        receitasPanel.add(receitasLabel, BorderLayout.CENTER);
        
        // Painel de despesas usando PadraoLayout - META 100% CONFORMIDADE
        JPanel despesasPanel = new JPanel(new BorderLayout());
        despesasPanel.setBackground(PadraoLayout.COR_PAINEL);
        despesasPanel.setBorder(PadraoLayout.BORDA_CONTEUDO);
        JLabel despesasTitleLabel = PadraoLayout.criarLabelFormulario("📉 DESPESAS");
        despesasTitleLabel.setFont(PadraoLayout.FONTE_GRUPO);
        despesasLabel = PadraoLayout.criarLabelFormulario("📉 DESPESAS");
        despesasLabel.setFont(PadraoLayout.FONTE_GRUPO);
        despesasPanel.add(despesasTitleLabel, BorderLayout.NORTH);
        despesasPanel.add(despesasLabel, BorderLayout.CENTER);
        
        // Painel de saldo usando PadraoLayout - META 100% CONFORMIDADE
        JPanel saldoPanel = new JPanel(new BorderLayout());
        saldoPanel.setBackground(PadraoLayout.COR_PAINEL);
        saldoPanel.setBorder(PadraoLayout.BORDA_CONTEUDO);
        JLabel saldoTitleLabel = PadraoLayout.criarLabelFormulario("💼 SALDO");
        saldoTitleLabel.setFont(PadraoLayout.FONTE_GRUPO);
        saldoLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        saldoLabel.setForeground(new Color(70, 130, 180));
        saldoPanel.add(saldoTitleLabel, BorderLayout.NORTH);
        saldoPanel.add(saldoLabel, BorderLayout.CENTER);
        
        resumoPanel.add(receitasPanel);
        resumoPanel.add(despesasPanel);
        resumoPanel.add(saldoPanel);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(pesquisaPanel, BorderLayout.NORTH);
        topPanel.add(resumoPanel, BorderLayout.CENTER);
        
        leftPanel.add(topPanel, BorderLayout.NORTH);
        
        // Tabela estilizada
        JScrollPane tableScrollPane = new JScrollPane(caixaTable);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 210)));
        tableScrollPane.getViewport().setBackground(Color.WHITE);
        
        // Tabela estilizada usando PadraoLayout
        PadraoLayout.configurarTabela(caixaTable);
        
        leftPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Painel direito - Formulário
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        
        // Título do formulário
        JPanel formHeaderPanel = new JPanel(new BorderLayout());
        formHeaderPanel.setBackground(new Color(245, 245, 250));
        formHeaderPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel formTitleLabel = new JLabel("💳 Detalhes da Movimentação", SwingConstants.LEFT);
        formTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitleLabel.setForeground(new Color(46, 125, 50));
        
        formHeaderPanel.add(formTitleLabel, BorderLayout.CENTER);
        rightPanel.add(formHeaderPanel, BorderLayout.NORTH);
        
        // Painel de formulário
        JPanel formularioPanel = new JPanel(new BorderLayout());
        formularioPanel.setBackground(Color.WHITE);
        formularioPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        JLabel formTitle = new JLabel("💳 Detalhes da Movimentação");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formTitle.setForeground(new Color(70, 130, 180));
        // Container principal para todos os grupos
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        
        // Grupo 1: Dados Básicos
        System.out.println("[CAIXA_PANEL] Iniciando setupLayout do formulário CaixaPanel");
        JPanel dadosBasicosPanel = createFormGroup("📅 Dados Básicos");
        System.out.println("[CAIXA_PANEL] Grupo formulário criado: Dados Básicos");
        JPanel dadosBasicosContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        dadosBasicosContent.setBackground(Color.WHITE);
        System.out.println("[CAIXA_PANEL] Layout de formulário aplicado com criarLayoutFormulario()");
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Código:"));
        PadraoLayout.estilizarCampoTexto(codigoField);
        dadosBasicosContent.add(codigoField);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Tipo:"));
        PadraoLayout.estilizarComboBox(tipoCombo);
        dadosBasicosContent.add(tipoCombo);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Categoria:"));
        PadraoLayout.estilizarCampoTexto(categoriaField);
        dadosBasicosContent.add(categoriaField);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Descrição:"));
        PadraoLayout.estilizarCampoTexto(descricaoField);
        dadosBasicosContent.add(descricaoField);
        
        dadosBasicosPanel.add(dadosBasicosContent);
        
        // Grupo 2: Valores e Datas
        JPanel valoresPanel = createFormGroup("💰 Valores e Datas");
        JPanel valoresContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        valoresContent.setBackground(Color.WHITE);
        
        valoresContent.add(PadraoLayout.criarLabelFormulario("Valor:"));
        PadraoLayout.estilizarCampoTexto(valorField);
        valoresContent.add(valorField);
        
        valoresContent.add(PadraoLayout.criarLabelFormulario("Data Movimentação:"));
        PadraoLayout.estilizarCampoTexto(dataMovimentacaoField);
        valoresContent.add(dataMovimentacaoField);
        
        valoresContent.add(PadraoLayout.criarLabelFormulario("Responsável:"));
        PadraoLayout.estilizarCampoTexto(responsavelField);
        valoresContent.add(responsavelField);
        
        valoresContent.add(PadraoLayout.criarLabelFormulario("Forma Pagamento:"));
        PadraoLayout.estilizarComboBox(formaPagamentoCombo);
        valoresContent.add(formaPagamentoCombo);
        
        valoresPanel.add(valoresContent);
        
        // Grupo 3: Documentação
        JPanel documentacaoPanel = createFormGroup("📄 Documentação");
        JPanel documentacaoContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        documentacaoContent.setBackground(Color.WHITE);
        
        documentacaoContent.add(PadraoLayout.criarLabelFormulario("Número Documento:"));
        PadraoLayout.estilizarCampoTexto(numeroDocumentoField);
        documentacaoContent.add(numeroDocumentoField);
        
        documentacaoContent.add(PadraoLayout.criarLabelFormulario("Status:"));
        PadraoLayout.estilizarComboBox(statusCombo);
        documentacaoContent.add(statusCombo);
        
        documentacaoPanel.add(documentacaoContent);
        
        // Grupo 4: Observações usando PadraoLayout
        JPanel observacoesPanel = PadraoLayout.criarGrupoFormulario("📝 Observações");
        JPanel observacoesContent = PadraoLayout.criarPainelTextArea("Observações:", observacoesArea);
        observacoesPanel.add(observacoesContent);
        
        // Organizar grupos verticalmente usando PadraoLayout
        JPanel allGroups = PadraoLayout.criarFormularioMultiplosGrupos(
            dadosBasicosPanel, valoresPanel, documentacaoPanel, observacoesPanel
        );
        
        // Adicionar scroll ao formulário usando PadraoLayout
        JScrollPane formScroll = PadraoLayout.criarFormularioComScroll(allGroups);
        
        formContainer.add(formScroll, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botoesPanel.setBackground(Color.WHITE);
        
        botoesPanel.add(createStyledButton("Salvar", new Color(144, 238, 144)));
        botoesPanel.add(createStyledButton("Novo", new Color(173, 216, 230)));
        botoesPanel.add(createStyledButton("Editar", new Color(255, 250, 205)));
        botoesPanel.add(createStyledButton("Excluir", new Color(255, 182, 193)));
        botoesPanel.add(createStyledButton("Limpar", new Color(240, 240, 240)));
        
        formularioPanel.add(formTitle, BorderLayout.NORTH);
        formularioPanel.add(formContainer, BorderLayout.CENTER);
        formularioPanel.add(botoesPanel, BorderLayout.SOUTH);
        
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.add(formularioPanel, BorderLayout.CENTER);
        
        rightPanel.add(formPanel, BorderLayout.CENTER);
        
        // Painel principal com split (padrão SessoesPanel)
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setDividerLocation(650);
        mainSplitPane.setResizeWeight(0.6);
        
        mainSplitPane.setLeftComponent(leftPanel);
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
        salvarButton.addActionListener(e -> salvarMovimentacao());
        novoButton.addActionListener(e -> limparFormulario());
        editarButton.addActionListener(e -> carregarMovimentacaoSelecionada());
        excluirButton.addActionListener(e -> excluirMovimentacao());
        limparButton.addActionListener(e -> limparFormulario());
        pesquisarButton.addActionListener(e -> pesquisarMovimentacoes());
        relatorioButton.addActionListener(e -> gerarRelatorio());
        
        // Seleção na tabela
        caixaTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && caixaTable.getSelectedRow() >= 0) {
                carregarMovimentacaoSelecionada();
            }
        });
        
        // Duplo clique para editar
        caixaTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    carregarMovimentacaoSelecionada();
                }
            }
        });
        
        // Mudança de tipo para atualizar categoria
        tipoCombo.addActionListener(e -> sugerirCategoria());
    }
    
    private void sugerirCategoria() {
        String tipo = (String) tipoCombo.getSelectedItem();
        if ("RECEITA".equals(tipo)) {
            categoriaField.setText("ANUIDADE");
        } else if ("DESPESA".equals(tipo)) {
            categoriaField.setText("MATERIAL");
        }
    }
    
    private void salvarMovimentacao() {
        try {
            if (descricaoField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Descrição é obrigatória!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (valorField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Valor é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Caixa caixa = caixaAtual != null ? caixaAtual : new Caixa();
            caixa.setTipo((String) tipoCombo.getSelectedItem());
            caixa.setCategoria(categoriaField.getText().trim());
            caixa.setDescricao(descricaoField.getText().trim());
            
            // Parse valor
            try {
                String valorStr = valorField.getText().trim().replace("R$", "").replace(",", ".");
                caixa.setValor(new BigDecimal(valorStr));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Parse data
            if (!dataMovimentacaoField.getText().trim().isEmpty()) {
                try {
                    String dataStr = dataMovimentacaoField.getText().trim();
                    caixa.setDataMovimentacao(LocalDateTime.parse(dataStr, DATE_FORMATTER));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Data inválida! Use formato dd/MM/yyyy HH:mm", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                caixa.setDataMovimentacao(LocalDateTime.now());
            }
            
            caixa.setResponsavel(responsavelField.getText().trim());
            caixa.setFormaPagamento((String) formaPagamentoCombo.getSelectedItem());
            caixa.setNumeroDocumento(numeroDocumentoField.getText().trim());
            caixa.setStatus((String) statusCombo.getSelectedItem());
            caixa.setObservacoes(observacoesArea.getText().trim());
            
            caixaDAO.save(caixa);
            
            JOptionPane.showMessageDialog(this, "Movimentação salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            refreshData();
            atualizarResumoFinanceiro();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar movimentação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limparFormulario() {
        caixaAtual = null;
        codigoField.setText("");
        tipoCombo.setSelectedIndex(0);
        sugerirCategoria();
        descricaoField.setText("");
        valorField.setText("");
        dataMovimentacaoField.setText("");
        responsavelField.setText("");
        formaPagamentoCombo.setSelectedIndex(0);
        numeroDocumentoField.setText("");
        statusCombo.setSelectedIndex(0);
        observacoesArea.setText("");
        descricaoField.requestFocus();
        atualizarBotoesAcao();
    }
    
    private void excluirMovimentacao() {
        if (caixaAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma movimentação para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int option = JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente excluir a movimentação " + caixaAtual.getDescricao() + " (R$ " + caixaAtual.getValor() + ")?",
            "Confirmar Exclusão",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (option == JOptionPane.YES_OPTION) {
            try {
                caixaDAO.delete(caixaAtual.getId());
                JOptionPane.showMessageDialog(this, "Movimentação excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                refreshData();
                atualizarResumoFinanceiro();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir movimentação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void pesquisarMovimentacoes() {
        try {
            String termo = pesquisarField.getText().trim();
            if (termo.isEmpty()) {
                refreshData();
                return;
            }
            
            List<Caixa> movimentacoes = caixaDAO.findAll();
            tableModel.setRowCount(0);
            
            for (Caixa caixa : movimentacoes) {
                if (caixa.getDescricao().toLowerCase().contains(termo.toLowerCase()) ||
                    caixa.getCategoria().toLowerCase().contains(termo.toLowerCase()) ||
                    caixa.getResponsavel().toLowerCase().contains(termo.toLowerCase())) {
                    Object[] row = {
                        caixa.getId(),
                        caixa.getTipo(),
                        caixa.getDescricao(),
                        "R$ " + caixa.getValor(),
                        caixa.getDataMovimentacao() != null ? caixa.getDataMovimentacao().format(DATE_FORMATTER) : "",
                        caixa.getStatus()
                    };
                    tableModel.addRow(row);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao pesquisar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void carregarMovimentacaoSelecionada() {
        int selectedRow = caixaTable.getSelectedRow();
        if (selectedRow >= 0) {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            try {
                caixaAtual = caixaDAO.findById(id);
                if (caixaAtual != null) {
                    codigoField.setText(String.valueOf(caixaAtual.getId()));
                    tipoCombo.setSelectedItem(caixaAtual.getTipo());
                    categoriaField.setText(caixaAtual.getCategoria());
                    descricaoField.setText(caixaAtual.getDescricao());
                    valorField.setText(caixaAtual.getValor().toString());
                    dataMovimentacaoField.setText(caixaAtual.getDataMovimentacao() != null ? caixaAtual.getDataMovimentacao().format(DATE_FORMATTER) : "");
                    responsavelField.setText(caixaAtual.getResponsavel());
                    formaPagamentoCombo.setSelectedItem(caixaAtual.getFormaPagamento());
                    numeroDocumentoField.setText(caixaAtual.getNumeroDocumento());
                    statusCombo.setSelectedItem(caixaAtual.getStatus());
                    observacoesArea.setText(caixaAtual.getObservacoes());
                    atualizarBotoesAcao();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar movimentação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void atualizarBotoesAcao() {
        boolean temCaixa = caixaAtual != null;
        editarButton.setEnabled(temCaixa);
        excluirButton.setEnabled(temCaixa);
    }
    
    private void atualizarResumoFinanceiro() {
        try {
            BigDecimal saldo = caixaDAO.getSaldo();
            BigDecimal receitas = caixaDAO.getTotalReceitas();
            BigDecimal despesas = caixaDAO.getTotalDespesas();
            
            saldoLabel.setText("R$ " + String.format("%,.2f", saldo));
            receitasLabel.setText("R$ " + String.format("%,.2f", receitas));
            despesasLabel.setText("R$ " + String.format("%,.2f", despesas));
            
            // Cores
            receitasLabel.setForeground(new Color(0, 128, 0)); // Verde
            despesasLabel.setForeground(new Color(255, 0, 0)); // Vermelho
            saldoLabel.setForeground(saldo.compareTo(BigDecimal.ZERO) >= 0 ? new Color(0, 128, 0) : new Color(255, 0, 0));
            
        } catch (Exception ex) {
            saldoLabel.setText("Erro");
            receitasLabel.setText("Erro");
            despesasLabel.setText("Erro");
        }
    }
    
    private void gerarRelatorio() {
        try {
            StringBuilder relatorio = new StringBuilder();
            relatorio.append("=== RELATÓRIO FINANCEIRO ===\n\n");
            
            BigDecimal receitas = caixaDAO.getTotalReceitas();
            BigDecimal despesas = caixaDAO.getTotalDespesas();
            BigDecimal saldo = caixaDAO.getSaldo();
            
            relatorio.append("RESUMO:\n");
            relatorio.append("Receitas: R$ ").append(String.format("%,.2f", receitas)).append("\n");
            relatorio.append("Despesas: R$ ").append(String.format("%,.2f", despesas)).append("\n");
            relatorio.append("Saldo: R$ ").append(String.format("%,.2f", saldo)).append("\n\n");
            
            relatorio.append("MOVIMENTAÇÕES:\n");
            relatorio.append("--------------------------------------------------\n");
            
            List<Caixa> movimentacoes = caixaDAO.findAll();
            for (Caixa caixa : movimentacoes) {
                relatorio.append(caixa.getTipo()).append(" - ");
                relatorio.append(caixa.getDescricao()).append("\n");
                relatorio.append("Valor: R$ ").append(String.format("%,.2f", caixa.getValor())).append("\n");
                relatorio.append("Data: ").append(caixa.getDataMovimentacao() != null ? caixa.getDataMovimentacao().format(DATE_FORMATTER) : "").append("\n");
                relatorio.append("Status: ").append(caixa.getStatus()).append("\n");
                relatorio.append("--------------------------------------------------\n");
            }
            
            JTextArea textArea = new JTextArea(relatorio.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Relatório Financeiro", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar relatório: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void refreshData() {
        logger.info("Iniciando refreshData() no CaixaPanel");
        try {
            logger.debug("Chamando caixaDAO.findAll()");
            List<Caixa> movimentacoes = caixaDAO.findAll();
            logger.info("Recebidas {} movimentações do DAO", movimentacoes.size());
            
            tableModel.setRowCount(0);
            logger.debug("TableModel limpo, começando a adicionar linhas");
            
            int linha = 0;
            for (Caixa caixa : movimentacoes) {
                linha++;
                Object[] row = {
                    caixa.getId(),
                    caixa.getTipo(),
                    caixa.getDescricao(),
                    caixa.getValor() != null ? "R$ " + caixa.getValor() : "R$ 0,00",
                    caixa.getDataMovimentacao() != null ? caixa.getDataMovimentacao().format(DATE_FORMATTER) : "",
                    caixa.getObservacoes() != null ? caixa.getObservacoes() : ""
                };
                tableModel.addRow(row);
                logger.debug("Linha {} adicionada: ID={}, Tipo={}, Valor={}", 
                            linha, caixa.getId(), caixa.getTipo(), caixa.getValor());
            }
            logger.info("refreshData() concluído com sucesso: {} linhas adicionadas", linha);
        } catch (SQLException ex) {
            logger.error("Erro ao carregar movimentações no painel: {}", ex.getMessage(), ex);
            JOptionPane.showMessageDialog(this, "Erro ao carregar movimentações: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            logger.error("Erro inesperado ao carregar movimentações: {}", ex.getMessage(), ex);
            JOptionPane.showMessageDialog(this, "Erro inesperado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
