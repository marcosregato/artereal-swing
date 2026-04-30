package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.AfastamentoDAO;
import com.artereal.swing.model.Afastamento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Painel de Gestão de Afastamentos e Licenças
 */
public class AfastamentosPanel extends JPanel {
    
    private AfastamentoDAO afastamentoDAO;
    private DefaultTableModel tableModel;
    private JTable afastamentosTable;
    private JTextField codigoIrmaoField;
    private JTextField dataInicialField;
    private JTextField dataFinalField;
    private JTextField descricaoField;
    private JTextField documentoField;
    private JTextField usuarioCadastroField;
    private JTextArea observacoesArea;
    private JComboBox<String> motivoComboBox;
    private JCheckBox afetaFrequenciaCheckBox;
    private JButton salvarButton;
    private JButton novoButton;
    private JButton excluirButton;
    private JButton finalizarButton;
    private JButton cancelarButton;
    private JButton reativarButton;
    private JButton relatorioButton;
    private Afastamento afastamentoAtual;
    
    public AfastamentosPanel() {
        afastamentoDAO = new AfastamentoDAO();
        initializeComponents();
        setupLayout();
        setupEvents();
        refreshData();
    }
    
    private void initializeComponents() {
        // Tabela
        tableModel = new DefaultTableModel(new Object[]{"ID", "Cód. Irmão", "Descrição", "Motivo", "Período", "Dias", "Status", "Afeta Frequência"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        afastamentosTable = new JTable(tableModel);
        
        // Formulário
        codigoIrmaoField = new JTextField(10);
        dataInicialField = new JTextField(12);
        dataFinalField = new JTextField(12);
        descricaoField = new JTextField(40);
        documentoField = new JTextField(30);
        usuarioCadastroField = new JTextField(20);
        observacoesArea = new JTextArea(3, 40);
        observacoesArea.setLineWrap(true);
        observacoesArea.setWrapStyleWord(true);
        motivoComboBox = new JComboBox<>(new String[]{
            "LICENCA_MEDICA", "FERIAS", "SUSPENSAO", 
            "AFASTAMENTO_TEMPORARIO", "LICENCA_MATERNIDADE", "OUTRO"
        });
        afetaFrequenciaCheckBox = new JCheckBox("Afeta Frequência");
        afetaFrequenciaCheckBox.setSelected(true);
        
        // Botões
        salvarButton = new JButton("Salvar");
        novoButton = new JButton("Novo");
        excluirButton = new JButton("Excluir");
        finalizarButton = new JButton("Finalizar");
        cancelarButton = new JButton("Cancelar");
        reativarButton = new JButton("Reativar");
        relatorioButton = new JButton("Relatório");
        
        // Data atual como padrão
        dataInicialField.setText(LocalDate.now().toString());
        
        afastamentoAtual = null;
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Título
        JLabel titleLabel = new JLabel("Gestão de Afastamentos e Licenças", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Painel de estatísticas
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Estatísticas de Afastamentos"));
        
        try {
            List<Object[]> estatisticas = afastamentoDAO.getEstatisticas();
            for (Object[] est : estatisticas) {
                String motivo = (String) est[0];
                int quantidade = (Integer) est[1];
                int totalDias = (Integer) est[2];
                int ativos = (Integer) est[3];
                int finalizados = (Integer) est[4];
                
                JPanel statPanel = new JPanel(new BorderLayout());
                statPanel.setBorder(BorderFactory.createEtchedBorder());
                
                Color color = Color.BLACK;
                switch (motivo) {
                    case "LICENCA_MEDICA": color = Color.RED; break;
                    case "FERIAS": color = Color.BLUE; break;
                    case "SUSPENSAO": color = Color.ORANGE; break;
                    default: color = Color.DARK_GRAY; break;
                }
                
                JLabel motivoLabel = new JLabel(motivo.replace("_", " "), SwingConstants.CENTER);
                motivoLabel.setForeground(color);
                statPanel.add(motivoLabel, BorderLayout.NORTH);
                
                JLabel detalhesLabel = new JLabel(String.format("%d afastamentos\n%d dias totais\n%d ativos\n%d finalizados", 
                    quantidade, totalDias, ativos, finalizados), SwingConstants.CENTER);
                detalhesLabel.setFont(new Font("Arial", Font.PLAIN, 10));
                statPanel.add(detalhesLabel, BorderLayout.CENTER);
                
                statsPanel.add(statPanel);
            }
        } catch (SQLException e) {
            statsPanel.add(new JLabel("Erro ao carregar estatísticas"));
        }
        
        // Painel de alertas
        JPanel alertasPanel = new JPanel(new BorderLayout());
        alertasPanel.setBorder(BorderFactory.createTitledBorder("Alertas de Afastamentos"));
        
        try {
            List<Afastamento> vencidos = afastamentoDAO.findVencidos();
            List<Afastamento> emAndamento = afastamentoDAO.findEmAndamento();
            
            JPanel alertasContent = new JPanel(new GridLayout(2, 1));
            
            // Vencidos
            JPanel vencidosPanel = new JPanel(new BorderLayout());
            vencidosPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            JLabel vencidosLabel = new JLabel(String.format("⚠ %d Afastamentos Vencidos", vencidos.size()), SwingConstants.CENTER);
            vencidosLabel.setForeground(Color.RED);
            vencidosPanel.add(vencidosLabel, BorderLayout.CENTER);
            alertasContent.add(vencidosPanel);
            
            // Em andamento
            JPanel andamentoPanel = new JPanel(new BorderLayout());
            andamentoPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
            JLabel andamentoLabel = new JLabel(String.format("📋 %d Afastamentos em Andamento", emAndamento.size()), SwingConstants.CENTER);
            andamentoLabel.setForeground(Color.BLUE);
            andamentoPanel.add(andamentoLabel, BorderLayout.CENTER);
            alertasContent.add(andamentoPanel);
            
            alertasPanel.add(alertasContent, BorderLayout.CENTER);
        } catch (SQLException e) {
            alertasPanel.add(new JLabel("Erro ao carregar alertas"));
        }
        
        // Painel de formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados do Afastamento"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Código do Irmão e Motivo
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Cód. Irmão:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        JPanel irmaoMotivoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        irmaoMotivoPanel.add(codigoIrmaoField);
        irmaoMotivoPanel.add(new JLabel("Motivo:"));
        irmaoMotivoPanel.add(motivoComboBox);
        formPanel.add(irmaoMotivoPanel, gbc);
        
        // Descrição
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        formPanel.add(descricaoField, gbc);
        
        // Período
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Período:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        JPanel periodoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        periodoPanel.add(new JLabel("De:"));
        periodoPanel.add(dataInicialField);
        periodoPanel.add(new JLabel("Até:"));
        periodoPanel.add(dataFinalField);
        formPanel.add(periodoPanel, gbc);
        
        // Documento e Usuário
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Documento:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        JPanel documentoUsuarioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        documentoUsuarioPanel.add(documentoField);
        documentoUsuarioPanel.add(new JLabel("Cadastro por:"));
        documentoUsuarioPanel.add(usuarioCadastroField);
        formPanel.add(documentoUsuarioPanel, gbc);
        
        // Opções
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Opções:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        formPanel.add(afetaFrequenciaCheckBox, gbc);
        
        // Observações
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Observações:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        formPanel.add(new JScrollPane(observacoesArea), gbc);
        
        // Botões do formulário
        JPanel botoesFormPanel = new JPanel(new FlowLayout());
        botoesFormPanel.add(salvarButton);
        botoesFormPanel.add(novoButton);
        botoesFormPanel.add(excluirButton);
        
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(botoesFormPanel, gbc);
        
        // Botões de ações
        JPanel acoesPanel = new JPanel(new FlowLayout());
        acoesPanel.setBorder(BorderFactory.createTitledBorder("Ações do Afastamento"));
        acoesPanel.add(finalizarButton);
        acoesPanel.add(cancelarButton);
        acoesPanel.add(reativarButton);
        acoesPanel.add(relatorioButton);
        
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(acoesPanel, gbc);
        
        // Painel da tabela
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBorder(BorderFactory.createTitledBorder("Afastamentos Cadastrados"));
        tabelaPanel.add(new JScrollPane(afastamentosTable), BorderLayout.CENTER);
        
        // Layout principal
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formPanel, tabelaPanel);
        splitPane.setDividerLocation(350);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(splitPane, BorderLayout.CENTER);
        centerPanel.add(alertasPanel, BorderLayout.SOUTH);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(statsPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        add(titleLabel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupEvents() {
        salvarButton.addActionListener(e -> salvarAfastamento());
        novoButton.addActionListener(e -> limparFormulario());
        excluirButton.addActionListener(e -> excluirAfastamento());
        finalizarButton.addActionListener(e -> finalizarAfastamento());
        cancelarButton.addActionListener(e -> cancelarAfastamento());
        reativarButton.addActionListener(e -> reativarAfastamento());
        relatorioButton.addActionListener(e -> gerarRelatorio());
        
        // Seleção na tabela
        afastamentosTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarAfastamentoSelecionado();
            }
        });
        
        // Mudança de datas para calcular dias
        dataInicialField.addActionListener(e -> calcularDias());
        dataFinalField.addActionListener(e -> calcularDias());
    }
    
    private void salvarAfastamento() {
        try {
            if (codigoIrmaoField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Código do irmão é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (descricaoField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Descrição é obrigatória!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (dataInicialField.getText().trim().isEmpty() || dataFinalField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Período (data inicial e final) é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Afastamento afastamento = afastamentoAtual != null ? afastamentoAtual : new Afastamento();
            afastamento.setCodigoIrmao(Long.parseLong(codigoIrmaoField.getText().trim()));
            afastamento.setDataInicial(LocalDate.parse(dataInicialField.getText().trim()));
            afastamento.setDataFinal(LocalDate.parse(dataFinalField.getText().trim()));
            afastamento.setDescricao(descricaoField.getText().trim());
            afastamento.setMotivo((String) motivoComboBox.getSelectedItem());
            afastamento.setDocumentoComprobatorio(documentoField.getText().trim());
            afastamento.setUsuarioCadastro(usuarioCadastroField.getText().trim());
            afastamento.setObservacoes(observacoesArea.getText().trim());
            afastamento.setAfetaFrequencia(afetaFrequenciaCheckBox.isSelected());
            
            // Calcular dias automaticamente
            afastamento.setDiasAfastamento(afastamento.calcularDias());
            
            afastamentoDAO.save(afastamento);
            
            JOptionPane.showMessageDialog(this, "Afastamento salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            refreshData();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar afastamento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limparFormulario() {
        afastamentoAtual = null;
        codigoIrmaoField.setText("");
        dataInicialField.setText(LocalDate.now().toString());
        dataFinalField.setText("");
        descricaoField.setText("");
        documentoField.setText("");
        usuarioCadastroField.setText("");
        observacoesArea.setText("");
        motivoComboBox.setSelectedItem("OUTRO");
        afetaFrequenciaCheckBox.setSelected(true);
        codigoIrmaoField.requestFocus();
        atualizarBotoesAcao();
    }
    
    private void excluirAfastamento() {
        if (afastamentoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um afastamento para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente excluir este afastamento?", 
            "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                afastamentoDAO.delete(afastamentoAtual.getId());
                JOptionPane.showMessageDialog(this, "Afastamento excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir afastamento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void finalizarAfastamento() {
        if (afastamentoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um afastamento para finalizar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!afastamentoAtual.isAtivo()) {
            JOptionPane.showMessageDialog(this, "Apenas afastamentos ativos podem ser finalizados!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente finalizar este afastamento?", 
            "Confirmar Finalização", JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                afastamentoDAO.finalizar(afastamentoAtual.getId());
                JOptionPane.showMessageDialog(this, "Afastamento finalizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarAfastamentoSelecionado();
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao finalizar afastamento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void cancelarAfastamento() {
        if (afastamentoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um afastamento para cancelar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!afastamentoAtual.isAtivo()) {
            JOptionPane.showMessageDialog(this, "Apenas afastamentos ativos podem ser cancelados!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente cancelar este afastamento?", 
            "Confirmar Cancelamento", JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                afastamentoDAO.cancelar(afastamentoAtual.getId());
                JOptionPane.showMessageDialog(this, "Afastamento cancelado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarAfastamentoSelecionado();
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao cancelar afastamento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void reativarAfastamento() {
        if (afastamentoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um afastamento para reativar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (afastamentoAtual.isAtivo()) {
            JOptionPane.showMessageDialog(this, "Afastamento já está ativo!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente reativar este afastamento?", 
            "Confirmar Reativação", JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                afastamentoDAO.reativar(afastamentoAtual.getId());
                JOptionPane.showMessageDialog(this, "Afastamento reativado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarAfastamentoSelecionado();
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao reativar afastamento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void gerarRelatorio() {
        try {
            List<Afastamento> afastamentos = afastamentoDAO.findAll();
            StringBuilder relatorio = new StringBuilder();
            relatorio.append("RELATÓRIO DE AFASTAMENTOS\n");
            relatorio.append("========================\n\n");
            
            int totalAfastamentos = afastamentos.size();
            int totalDias = 0;
            int ativos = 0;
            int finalizados = 0;
            int cancelados = 0;
            
            for (Afastamento afastamento : afastamentos) {
                relatorio.append(String.format("Irmão: %d\n", afastamento.getCodigoIrmao()));
                relatorio.append(String.format("Descrição: %s\n", afastamento.getDescricao()));
                relatorio.append(String.format("Motivo: %s\n", afastamento.getMotivoFormatado()));
                relatorio.append(String.format("Período: %s\n", afastamento.getPeriodoFormatado()));
                relatorio.append(String.format("Dias: %d\n", afastamento.getDiasAfastamento()));
                relatorio.append(String.format("Status: %s\n", afastamento.getStatusFormatado()));
                relatorio.append(String.format("Afeta Frequência: %s\n", afastamento.isAfetaFrequencia() ? "Sim" : "Não"));
                
                if (afastamento.getUsuarioCadastro() != null) {
                    relatorio.append(String.format("Cadastro por: %s\n", afastamento.getUsuarioCadastro()));
                }
                
                relatorio.append("------------------------\n");
                
                totalDias += afastamento.getDiasAfastamento();
                if (afastamento.isAtivo()) ativos++;
                else if (afastamento.isFinalizado()) finalizados++;
                else if (afastamento.isCancelado()) cancelados++;
            }
            
            relatorio.append("\nRESUMO\n");
            relatorio.append("======\n");
            relatorio.append(String.format("Total de Afastamentos: %d\n", totalAfastamentos));
            relatorio.append(String.format("Total de Dias: %d\n", totalDias));
            relatorio.append(String.format("Ativos: %d\n", ativos));
            relatorio.append(String.format("Finalizados: %d\n", finalizados));
            relatorio.append(String.format("Cancelados: %d\n", cancelados));
            relatorio.append(String.format("Média de Dias: %.1f\n", totalAfastamentos > 0 ? (double) totalDias / totalAfastamentos : 0));
            
            JTextArea textArea = new JTextArea(relatorio.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Relatório de Afastamentos", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar relatório: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void carregarAfastamentoSelecionado() {
        int selectedRow = afastamentosTable.getSelectedRow();
        if (selectedRow >= 0) {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            try {
                afastamentoAtual = afastamentoDAO.findById(id);
                if (afastamentoAtual != null) {
                    codigoIrmaoField.setText(String.valueOf(afastamentoAtual.getCodigoIrmao()));
                    dataInicialField.setText(afastamentoAtual.getDataInicial().toString());
                    dataFinalField.setText(afastamentoAtual.getDataFinal().toString());
                    descricaoField.setText(afastamentoAtual.getDescricao());
                    motivoComboBox.setSelectedItem(afastamentoAtual.getMotivo());
                    documentoField.setText(afastamentoAtual.getDocumentoComprobatorio());
                    usuarioCadastroField.setText(afastamentoAtual.getUsuarioCadastro());
                    observacoesArea.setText(afastamentoAtual.getObservacoes());
                    afetaFrequenciaCheckBox.setSelected(afastamentoAtual.isAfetaFrequencia());
                    atualizarBotoesAcao();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar afastamento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void calcularDias() {
        try {
            String dataInicialStr = dataInicialField.getText().trim();
            String dataFinalStr = dataFinalField.getText().trim();
            
            if (!dataInicialStr.isEmpty() && !dataFinalStr.isEmpty()) {
                LocalDate dataInicial = LocalDate.parse(dataInicialStr);
                LocalDate dataFinal = LocalDate.parse(dataFinalStr);
                
                if (dataFinal.isBefore(dataInicial)) {
                    JOptionPane.showMessageDialog(this, "Data final deve ser posterior à data inicial!", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                int dias = (int) java.time.temporal.ChronoUnit.DAYS.between(dataInicial, dataFinal) + 1;
                JOptionPane.showMessageDialog(this, String.format("Período de %d dias", dias), "Cálculo de Dias", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            // Ignora erros de parsing
        }
    }
    
    private void atualizarBotoesAcao() {
        boolean temAfastamento = afastamentoAtual != null;
        boolean ativo = temAfastamento && afastamentoAtual.isAtivo();
        
        finalizarButton.setEnabled(ativo);
        cancelarButton.setEnabled(ativo);
        reativarButton.setEnabled(temAfastamento && !ativo);
    }
    
    public void refreshData() {
        try {
            List<Afastamento> afastamentos = afastamentoDAO.findAll();
            tableModel.setRowCount(0);
            
            for (Afastamento afastamento : afastamentos) {
                Object[] row = {
                    afastamento.getId(),
                    afastamento.getCodigoIrmao(),
                    afastamento.getDescricao(),
                    afastamento.getMotivoFormatado(),
                    afastamento.getPeriodoFormatado(),
                    afastamento.getDiasAfastamento(),
                    afastamento.getStatusFormatado(),
                    afastamento.isAfetaFrequencia() ? "Sim" : "Não"
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar afastamentos: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
