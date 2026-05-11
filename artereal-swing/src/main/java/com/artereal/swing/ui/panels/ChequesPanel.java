package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.ChequeDAO;
import com.artereal.swing.model.Cheque;
import com.artereal.swing.ui.layout.PadraoLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Painel de Gestão de Cheques
 */
public class ChequesPanel extends JPanel {
    
    private ChequeDAO chequeDAO;
    private DefaultTableModel tableModel;
    private JTable chequesTable;
    private JTextField faturaField;
    private JTextField sacadoField;
    private JTextField valorField;
    private JTextField dataEmissaoField;
    private JTextField dataVencimentoField;
    private JTextField bancoField;
    private JTextField grupoField;
    private JTextField historicoField;
    private JComboBox<String> situacaoComboBox;
    private JButton salvarButton;
    private JButton novoButton;
    private JButton excluirButton;
    private JButton compensarButton;
    private JButton cancelarButton;
    private JButton devolverButton;
    private JButton relatorioButton;
    private JTextField pesquisarField;
    private JButton pesquisarButton;
    private Cheque chequeAtual;
    
    public ChequesPanel() {
        chequeDAO = new ChequeDAO();
        initializeComponents();
        setupLayout();
        setupEvents();
        refreshData();
    }
    
    private void initializeComponents() {
        // Tabela
        tableModel = new DefaultTableModel(new Object[]{"ID", "Fatura", "Sacado", "Valor", "Vencimento", "Banco", "Situação", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        chequesTable = new JTable(tableModel);
        
        // Formulário
        faturaField = new JTextField();
        sacadoField = new JTextField();
        valorField = new JTextField();
        dataEmissaoField = new JTextField();
        dataVencimentoField = new JTextField();
        bancoField = new JTextField();
        grupoField = new JTextField();
        historicoField = new JTextField();
        situacaoComboBox = new JComboBox<>(new String[]{"ABERTO", "PAGO", "CANCELADO", "DEVOLVIDO"});
        
        // Botões usando PadraoLayout com cores pastéis
        salvarButton = PadraoLayout.criarBotaoSalvar();
        novoButton = PadraoLayout.criarBotaoNovo();
        excluirButton = PadraoLayout.criarBotaoExcluir();
        compensarButton = PadraoLayout.criarBotao("Compensar", new Color(152, 251, 152)); // Verde menta
        cancelarButton = PadraoLayout.criarBotao("Cancelar", new Color(255, 182, 193)); // Rosa pastel
        devolverButton = PadraoLayout.criarBotao("Devolver", new Color(255, 218, 185)); // Laranja pastel
        relatorioButton = PadraoLayout.criarBotao("Relatório", new Color(200, 200, 255)); // Azul claro
        
        // Pesquisa
        pesquisarField = new JTextField(20);
        PadraoLayout.estilizarCampoTexto(pesquisarField);
        pesquisarField.setEditable(true);
        pesquisarField.setEnabled(true);
        pesquisarButton = PadraoLayout.criarBotaoPesquisar();
        
        // Data atual como padrão
        dataEmissaoField.setText(LocalDate.now().toString());
        
        chequeAtual = null;
    }
    
    private void setupLayout() {
        // Aplicar layout padrão usando PadraoLayout
        // Aplicar estilização padrão ao painel principal
        PadraoLayout.estilizarPainelPrincipal(this);
        
        // Header estilizado usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader("💰 Gestão de Cheques", "Controle de cheques e compensação bancária");
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
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(PadraoLayout.COR_PAINEL);
        formPanel.setBorder(PadraoLayout.BORDA_CONTEUDO);
        
        // Painel de estatísticas
        JPanel statsPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Estatísticas Financeiras"));
        
        try {
            List<Object[]> estatisticas = chequeDAO.getEstatisticas();
            for (Object[] est : estatisticas) {
                String situacao = (String) est[0];
                int quantidade = (Integer) est[1];
                double valorTotal = (Double) est[2];
                
                JPanel statPanel = new JPanel(new BorderLayout());
                statPanel.setBorder(BorderFactory.createEtchedBorder());
                
                Color color = Color.BLACK;
                switch (situacao) {
                    case "ABERTO": color = Color.BLUE; break;
                    case "PAGO": color = Color.GREEN; break;
                    case "CANCELADO": color = Color.RED; break;
                    case "DEVOLVIDO": color = Color.ORANGE; break;
                }
                
                JLabel situacaoLabel = new JLabel(situacao, SwingConstants.CENTER);
                situacaoLabel.setForeground(color);
                statPanel.add(situacaoLabel, BorderLayout.NORTH);
                
                JLabel valorLabel = new JLabel(String.format("%d cheques\nR$ %.2f", quantidade, valorTotal), SwingConstants.CENTER);
                valorLabel.setFont(new Font("Arial", Font.BOLD, 12));
                statPanel.add(valorLabel, BorderLayout.CENTER);
                
                statsPanel.add(statPanel);
            }
        } catch (SQLException e) {
            statsPanel.add(new JLabel("Erro ao carregar estatísticas"));
        }
        
        // Painel de alertas
        JPanel alertasPanel = new JPanel(new BorderLayout());
        alertasPanel.setBorder(BorderFactory.createTitledBorder("Alertas"));
        
        try {
            List<Cheque> vencidos = chequeDAO.findVencidos();
            List<Cheque> proximos = chequeDAO.findProximosVencimento(7);
            
            JPanel alertasContent = new JPanel(new GridLayout(2, 1));
            
            // Vencidos
            JPanel vencidosPanel = new JPanel(new BorderLayout());
            vencidosPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            JLabel vencidosLabel = new JLabel(String.format("⚠ %d Cheques Vencidos", vencidos.size()), SwingConstants.CENTER);
            vencidosLabel.setForeground(Color.RED);
            vencidosPanel.add(vencidosLabel, BorderLayout.CENTER);
            alertasContent.add(vencidosPanel);
            
            // Próximos ao vencimento
            JPanel proximosPanel = new JPanel(new BorderLayout());
            proximosPanel.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 1));
            JLabel proximosLabel = new JLabel(String.format("⏰ %d Cheques Vencem em 7 dias", proximos.size()), SwingConstants.CENTER);
            proximosLabel.setForeground(Color.ORANGE);
            proximosPanel.add(proximosLabel, BorderLayout.CENTER);
            alertasContent.add(proximosPanel);
            
            alertasPanel.add(alertasContent, BorderLayout.CENTER);
        } catch (SQLException e) {
            alertasPanel.add(new JLabel("Erro ao carregar alertas"));
        }
        
        // Painel de formulário completo usando PadraoLayout
        JPanel formCompletoPanel = new JPanel(new BorderLayout());
        formCompletoPanel.setBackground(Color.WHITE);
        
        JPanel formContent = new JPanel(PadraoLayout.criarLayoutFormularioAlinhado());
        formContent.setBackground(Color.WHITE);
        
        // Fatura (ocupa duas colunas)
        GridBagConstraints faturaLabelConstraints = PadraoLayout.criarConstraintsFormulario(0, 0);
        faturaLabelConstraints.gridwidth = 2;
        faturaLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
        formContent.add(PadraoLayout.criarLabelFormulario("Fatura:"), faturaLabelConstraints);
        
        GridBagConstraints faturaFieldConstraints = PadraoLayout.criarConstraintsFormulario(1, 0);
        faturaFieldConstraints.gridwidth = 2;
        faturaFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        PadraoLayout.estilizarCampoTexto(faturaField);
        formContent.add(faturaField, faturaFieldConstraints);
        
        // Sacado (ocupa duas colunas)
        GridBagConstraints sacadoLabelConstraints = PadraoLayout.criarConstraintsFormulario(2, 0);
        sacadoLabelConstraints.gridwidth = 2;
        sacadoLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
        formContent.add(PadraoLayout.criarLabelFormulario("Sacado:"), sacadoLabelConstraints);
        
        GridBagConstraints sacadoFieldConstraints = PadraoLayout.criarConstraintsFormulario(3, 0);
        sacadoFieldConstraints.gridwidth = 2;
        sacadoFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        PadraoLayout.estilizarCampoTexto(sacadoField);
        formContent.add(sacadoField, sacadoFieldConstraints);
        
        // Valor
        formContent.add(PadraoLayout.criarLabelFormulario("Valor:"), PadraoLayout.criarConstraintsFormulario(4, 0));
        PadraoLayout.estilizarCampoTexto(valorField);
        formContent.add(valorField, PadraoLayout.criarConstraintsFormulario(4, 1));
        
        // Emissão
        formContent.add(PadraoLayout.criarLabelFormulario("Emissão:"), PadraoLayout.criarConstraintsFormulario(5, 0));
        PadraoLayout.estilizarCampoTexto(dataEmissaoField);
        formContent.add(dataEmissaoField, PadraoLayout.criarConstraintsFormulario(5, 1));
        
        // Vencimento
        formContent.add(PadraoLayout.criarLabelFormulario("Vencimento:"), PadraoLayout.criarConstraintsFormulario(6, 0));
        PadraoLayout.estilizarCampoTexto(dataVencimentoField);
        formContent.add(dataVencimentoField, PadraoLayout.criarConstraintsFormulario(6, 1));
        
        // Situação
        formContent.add(PadraoLayout.criarLabelFormulario("Situação:"), PadraoLayout.criarConstraintsFormulario(7, 0));
        PadraoLayout.estilizarComboBox(situacaoComboBox);
        formContent.add(situacaoComboBox, PadraoLayout.criarConstraintsFormulario(7, 1));
        
        // Banco (ocupa duas colunas)
        GridBagConstraints bancoLabelConstraints = PadraoLayout.criarConstraintsFormulario(8, 0);
        bancoLabelConstraints.gridwidth = 2;
        bancoLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
        formContent.add(PadraoLayout.criarLabelFormulario("Banco:"), bancoLabelConstraints);
        
        GridBagConstraints bancoFieldConstraints = PadraoLayout.criarConstraintsFormulario(9, 0);
        bancoFieldConstraints.gridwidth = 2;
        bancoFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        PadraoLayout.estilizarCampoTexto(bancoField);
        formContent.add(bancoField, bancoFieldConstraints);
        
        // Grupo (ocupa duas colunas)
        GridBagConstraints grupoLabelConstraints = PadraoLayout.criarConstraintsFormulario(10, 0);
        grupoLabelConstraints.gridwidth = 2;
        grupoLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
        formContent.add(PadraoLayout.criarLabelFormulario("Grupo:"), grupoLabelConstraints);
        
        GridBagConstraints grupoFieldConstraints = PadraoLayout.criarConstraintsFormulario(11, 0);
        grupoFieldConstraints.gridwidth = 2;
        grupoFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        PadraoLayout.estilizarCampoTexto(grupoField);
        formContent.add(grupoField, grupoFieldConstraints);
        
        // Histórico (ocupa duas colunas)
        GridBagConstraints historicoLabelConstraints = PadraoLayout.criarConstraintsFormulario(12, 0);
        historicoLabelConstraints.gridwidth = 2;
        historicoLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
        formContent.add(PadraoLayout.criarLabelFormulario("Histórico:"), historicoLabelConstraints);
        
        GridBagConstraints historicoFieldConstraints = PadraoLayout.criarConstraintsFormulario(13, 0);
        historicoFieldConstraints.gridwidth = 2;
        historicoFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        PadraoLayout.estilizarCampoTexto(historicoField);
        formContent.add(historicoField, historicoFieldConstraints);
        
        formCompletoPanel.add(formContent, BorderLayout.CENTER);
        
        // Botões usando PadraoLayout
        JPanel botoesPanel = PadraoLayout.criarPainelBotoes(
            PadraoLayout.criarBotao("Salvar", PadraoLayout.COR_BOTAO_SALVAR),
            PadraoLayout.criarBotao("Novo", PadraoLayout.COR_BOTAO_NOVO),
            PadraoLayout.criarBotao("Excluir", PadraoLayout.COR_BOTAO_EXCLUIR),
            PadraoLayout.criarBotao("Compensar", PadraoLayout.COR_BOTAO_EDITAR)
        );
        formCompletoPanel.add(botoesPanel, BorderLayout.SOUTH);
        
        formPanel.add(formCompletoPanel, BorderLayout.CENTER);
        
        // Painel de tabela usando PadraoLayout
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBackground(PadraoLayout.COR_PAINEL);
        tabelaPanel.setBorder(PadraoLayout.BORDA_CONTEUDO);
        
        // Painel de estatísticas e alertas
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(statsPanel, BorderLayout.CENTER);
        topPanel.add(alertasPanel, BorderLayout.SOUTH);
        
        // Tabela estilizada usando PadraoLayout
        PadraoLayout.configurarTabela(chequesTable);
        JScrollPane tableScrollPane = new JScrollPane(chequesTable);
        
        tabelaPanel.add(topPanel, BorderLayout.NORTH);
        tabelaPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Split vertical: Formulário acima, Tabela abaixo
        JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formPanel, tabelaPanel);
        verticalSplitPane.setDividerLocation(350);
        verticalSplitPane.setResizeWeight(0.4);
        
        contentPanel.add(verticalSplitPane, BorderLayout.CENTER);
        
        // Adicionar contentPanel ao mainPanel
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
    }

private void setupEvents() {
    salvarButton.addActionListener(e -> salvarCheque());
    novoButton.addActionListener(e -> limparFormulario());
    excluirButton.addActionListener(e -> excluirCheque());
    compensarButton.addActionListener(e -> compensarCheque());
    cancelarButton.addActionListener(e -> cancelarCheque());
    devolverButton.addActionListener(e -> devolverCheque());
    relatorioButton.addActionListener(e -> gerarRelatorio());
        excluirButton.addActionListener(e -> excluirCheque());
        compensarButton.addActionListener(e -> compensarCheque());
        cancelarButton.addActionListener(e -> cancelarCheque());
        devolverButton.addActionListener(e -> devolverCheque());
        relatorioButton.addActionListener(e -> gerarRelatorio());
        
        // Seleção na tabela
        chequesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarChequeSelecionado();
            }
        });
        
        // Formato de valor
        valorField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                formatarValor();
            }
        });
    }
    
    private void salvarCheque() {
        try {
            if (sacadoField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Sacado é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (valorField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Valor é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Cheque cheque = chequeAtual != null ? chequeAtual : new Cheque();
            cheque.setFatura(faturaField.getText().trim());
            cheque.setSacado(sacadoField.getText().trim());
            cheque.setValor(parseValor(valorField.getText()));
            cheque.setDataEmissao(LocalDate.parse(dataEmissaoField.getText().trim()));
            cheque.setDataVencimento(LocalDate.parse(dataVencimentoField.getText().trim()));
            cheque.setBanco(bancoField.getText().trim());
            cheque.setGrupo(grupoField.getText().trim());
            cheque.setHistorico(historicoField.getText().trim());
            cheque.setSituacao((String) situacaoComboBox.getSelectedItem());
            
            chequeDAO.save(cheque);
            
            JOptionPane.showMessageDialog(this, "Cheque salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            refreshData();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar cheque: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limparFormulario() {
        chequeAtual = null;
        faturaField.setText("");
        sacadoField.setText("");
        valorField.setText("");
        dataEmissaoField.setText(LocalDate.now().toString());
        dataVencimentoField.setText("");
        bancoField.setText("");
        grupoField.setText("");
        historicoField.setText("");
        situacaoComboBox.setSelectedItem("ABERTO");
        sacadoField.requestFocus();
        atualizarBotoesAcao();
    }
    
    private void excluirCheque() {
        if (chequeAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um cheque para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente excluir o cheque de " + chequeAtual.getSacado() + "?", 
            "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                chequeDAO.delete(chequeAtual.getId());
                JOptionPane.showMessageDialog(this, "Cheque excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir cheque: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void compensarCheque() {
        if (chequeAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um cheque para compensar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!chequeAtual.isAberto()) {
            JOptionPane.showMessageDialog(this, "Apenas cheques em aberto podem ser compensados!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String valorPagoStr = JOptionPane.showInputDialog(this, "Valor pago:", String.valueOf(chequeAtual.getValor()));
        if (valorPagoStr != null && !valorPagoStr.trim().isEmpty()) {
            try {
                double valorPago = Double.parseDouble(valorPagoStr.replace(",", "."));
                chequeDAO.compensar(chequeAtual.getId(), valorPago, LocalDate.now());
                JOptionPane.showMessageDialog(this, "Cheque compensado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarChequeSelecionado();
                refreshData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao compensar cheque: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void cancelarCheque() {
        if (chequeAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um cheque para cancelar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!chequeAtual.isAberto()) {
            JOptionPane.showMessageDialog(this, "Apenas cheques em aberto podem ser cancelados!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente cancelar o cheque de " + chequeAtual.getSacado() + "?", 
            "Confirmar Cancelamento", JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                chequeDAO.cancelar(chequeAtual.getId());
                JOptionPane.showMessageDialog(this, "Cheque cancelado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarChequeSelecionado();
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao cancelar cheque: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void devolverCheque() {
        if (chequeAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um cheque para devolver!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente devolver o cheque de " + chequeAtual.getSacado() + "?", 
            "Confirmar Devolução", JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                chequeDAO.devolver(chequeAtual.getId());
                JOptionPane.showMessageDialog(this, "Cheque devolvido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarChequeSelecionado();
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao devolver cheque: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void gerarRelatorio() {
        try {
            List<Cheque> cheques = chequeDAO.findAll();
            StringBuilder relatorio = new StringBuilder();
            relatorio.append("RELATÓRIO DE CHEQUES\n");
            relatorio.append("================\n\n");
            
            double totalAberto = 0;
            double totalPago = 0;
            double totalCancelado = 0;
            double totalDevolvido = 0;
            
            for (Cheque cheque : cheques) {
                relatorio.append(String.format("Cheque: %s\n", cheque.getFatura() != null ? cheque.getFatura() : "N/A"));
                relatorio.append(String.format("Sacado: %s\n", cheque.getSacado()));
                relatorio.append(String.format("Valor: R$ %.2f\n", cheque.getValor()));
                relatorio.append(String.format("Vencimento: %s\n", cheque.getDataVencimento()));
                relatorio.append(String.format("Banco: %s\n", cheque.getBanco()));
                relatorio.append(String.format("Situação: %s\n", cheque.getSituacao()));
                relatorio.append("------------------------\n");
                
                switch (cheque.getSituacao()) {
                    case "ABERTO": totalAberto += cheque.getValor(); break;
                    case "PAGO": totalPago += cheque.getValor(); break;
                    case "CANCELADO": totalCancelado += cheque.getValor(); break;
                    case "DEVOLVIDO": totalDevolvido += cheque.getValor(); break;
                }
            }
            
            relatorio.append("\nRESUMO FINANCEIRO\n");
            relatorio.append("================\n");
            relatorio.append(String.format("Total em Aberto: R$ %.2f\n", totalAberto));
            relatorio.append(String.format("Total Pago: R$ %.2f\n", totalPago));
            relatorio.append(String.format("Total Cancelado: R$ %.2f\n", totalCancelado));
            relatorio.append(String.format("Total Devolvido: R$ %.2f\n", totalDevolvido));
            relatorio.append(String.format("Total Geral: R$ %.2f\n", totalAberto + totalPago + totalCancelado + totalDevolvido));
            
            JTextArea textArea = new JTextArea(relatorio.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Relatório de Cheques", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar relatório: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void carregarChequeSelecionado() {
        int selectedRow = chequesTable.getSelectedRow();
        if (selectedRow >= 0) {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            try {
                chequeAtual = chequeDAO.findById(id);
                if (chequeAtual != null) {
                    faturaField.setText(chequeAtual.getFatura());
                    sacadoField.setText(chequeAtual.getSacado());
                    valorField.setText(String.format("%.2f", chequeAtual.getValor()));
                    dataEmissaoField.setText(chequeAtual.getDataEmissao().toString());
                    dataVencimentoField.setText(chequeAtual.getDataVencimento().toString());
                    bancoField.setText(chequeAtual.getBanco());
                    grupoField.setText(chequeAtual.getGrupo());
                    historicoField.setText(chequeAtual.getHistorico());
                    situacaoComboBox.setSelectedItem(chequeAtual.getSituacao());
                    atualizarBotoesAcao();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar cheque: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void atualizarBotoesAcao() {
        boolean temCheque = chequeAtual != null;
        boolean aberto = temCheque && chequeAtual.isAberto();
        
        compensarButton.setEnabled(aberto);
        cancelarButton.setEnabled(aberto);
        devolverButton.setEnabled(temCheque);
    }
    
    private void formatarValor() {
        String texto = valorField.getText().trim();
        if (!texto.isEmpty()) {
            try {
                double valor = Double.parseDouble(texto.replace(",", "."));
                valorField.setText(String.format("%.2f", valor));
            } catch (NumberFormatException e) {
                // Não faz nada se não for número
            }
        }
    }
    
    private double parseValor(String texto) {
        return Double.parseDouble(texto.replace(",", "."));
    }
    
    public void refreshData() {
        try {
            List<Cheque> cheques = chequeDAO.findAll();
            tableModel.setRowCount(0);
            
            for (Cheque cheque : cheques) {
                String status = "";
                if (cheque.isVencido()) {
                    status = "VENCIDO";
                } else if (cheque.isProximoVencimento()) {
                    status = "PRÓXIMO";
                }
                
                Object[] row = {
                    cheque.getId(),
                    cheque.getFatura(),
                    cheque.getSacado(),
                    String.format("R$ %.2f", cheque.getValor()),
                    cheque.getDataVencimento(),
                    cheque.getBanco(),
                    cheque.getSituacao(),
                    status
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar cheques: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
