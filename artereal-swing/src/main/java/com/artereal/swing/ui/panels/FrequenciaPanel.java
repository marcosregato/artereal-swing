package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.FrequenciaDAO;
import com.artereal.swing.model.Frequencia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

/**
 * Painel de Controle de Frequência e Presença
 */
public class FrequenciaPanel extends JPanel {
    
    private FrequenciaDAO frequenciaDAO;
    private DefaultTableModel tableModel;
    private JTable frequenciaTable;
    private JTextField nomeField;
    private JTextField grauField;
    private JTextField presencasField;
    private JTextField faltasField;
    private JTextField totalField;
    private JButton registrarPresencaButton;
    private JButton registrarFaltaButton;
    private JButton novoButton;
    private JButton salvarButton;
    private JButton excluirButton;
    private JButton atualizarButton;
    private JLabel percentualLabel;
    private Frequencia frequenciaAtual;
    
    public FrequenciaPanel() {
        frequenciaDAO = new FrequenciaDAO();
        initializeComponents();
        setupLayout();
        setupEvents();
        refreshData();
    }
    
    private void initializeComponents() {
        // Tabela
        tableModel = new DefaultTableModel(new Object[]{"ID", "Irmão", "Grau", "Presenças", "Faltas", "Total", "% Presença"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        frequenciaTable = new JTable(tableModel);
        
        // Formulário
        nomeField = new JTextField(25);
        grauField = new JTextField(15);
        presencasField = new JTextField(10);
        presencasField.setEditable(false);
        faltasField = new JTextField(10);
        faltasField.setEditable(false);
        totalField = new JTextField(10);
        totalField.setEditable(false);
        percentualLabel = new JLabel("0.00%");
        percentualLabel.setFont(new Font("Arial", Font.BOLD, 16));
        percentualLabel.setForeground(Color.BLUE);
        
        // Botões
        registrarPresencaButton = new JButton("Registrar Presença");
        registrarFaltaButton = new JButton("Registrar Falta");
        novoButton = new JButton("Novo");
        salvarButton = new JButton("Salvar");
        excluirButton = new JButton("Excluir");
        atualizarButton = new JButton("Atualizar");
        
        frequenciaAtual = null;
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Título
        JLabel titleLabel = new JLabel("Controle de Frequência", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Painel de estatísticas
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Estatísticas Gerais"));
        
        try {
            List<Object[]> estatisticas = frequenciaDAO.getEstatisticasPorGrau();
            for (Object[] est : estatisticas) {
                String grau = (String) est[0];
                int total = (Integer) est[1];
                double media = (Double) est[2];
                
                JPanel grauPanel = new JPanel(new BorderLayout());
                grauPanel.setBorder(BorderFactory.createEtchedBorder());
                grauPanel.add(new JLabel(grau + " (" + total + ")", SwingConstants.CENTER), BorderLayout.NORTH);
                grauPanel.add(new JLabel(String.format("%.1f%%", media), SwingConstants.CENTER), BorderLayout.CENTER);
                statsPanel.add(grauPanel);
            }
        } catch (SQLException e) {
            statsPanel.add(new JLabel("Erro ao carregar estatísticas"));
        }
        
        // Painel de formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados da Frequência"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Nome
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Irmão:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        formPanel.add(nomeField, gbc);
        
        // Grau
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Grau:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        formPanel.add(grauField, gbc);
        
        // Contadores
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Presenças:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        formPanel.add(presencasField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Faltas:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        formPanel.add(faltasField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Total Sessões:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        formPanel.add(totalField, gbc);
        
        // Percentual
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(new JLabel("Percentual de Presença:"), gbc);
        gbc.gridy = 6;
        formPanel.add(percentualLabel, gbc);
        
        // Botões do formulário
        JPanel botoesFormPanel = new JPanel(new FlowLayout());
        botoesFormPanel.add(registrarPresencaButton);
        botoesFormPanel.add(registrarFaltaButton);
        botoesFormPanel.add(novoButton);
        botoesFormPanel.add(salvarButton);
        botoesFormPanel.add(excluirButton);
        botoesFormPanel.add(atualizarButton);
        
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(botoesFormPanel, gbc);
        
        // Painel da tabela
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBorder(BorderFactory.createTitledBorder("Registros de Frequência"));
        tabelaPanel.add(new JScrollPane(frequenciaTable), BorderLayout.CENTER);
        
        // Painel de baixa frequência
        JPanel baixaFrequenciaPanel = new JPanel(new BorderLayout());
        baixaFrequenciaPanel.setBorder(BorderFactory.createTitledBorder("Alerta - Baixa Frequência (< 75%)"));
        
        try {
            List<Frequencia> baixaFreq = frequenciaDAO.findBaixaAssiduidade(75.0);
            DefaultTableModel baixaModel = new DefaultTableModel(new Object[]{"Irmão", "Grau", "% Presença"}, 0);
            
            for (Frequencia freq : baixaFreq) {
                Object[] row = {
                    freq.getNomeIrmao(),
                    freq.getGrau(),
                    String.format("%.1f%%", freq.getPercentualPresenca())
                };
                baixaModel.addRow(row);
            }
            
            JTable baixaTable = new JTable(baixaModel);
            baixaFrequenciaPanel.add(new JScrollPane(baixaTable), BorderLayout.CENTER);
        } catch (SQLException e) {
            baixaFrequenciaPanel.add(new JLabel("Erro ao carregar alertas"), BorderLayout.CENTER);
        }
        
        // Layout principal
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formPanel, tabelaPanel);
        splitPane.setDividerLocation(300);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(splitPane, BorderLayout.CENTER);
        centerPanel.add(baixaFrequenciaPanel, BorderLayout.SOUTH);
        
        add(titleLabel, BorderLayout.NORTH);
        add(statsPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private void setupEvents() {
        registrarPresencaButton.addActionListener(e -> registrarPresenca());
        registrarFaltaButton.addActionListener(e -> registrarFalta());
        novoButton.addActionListener(e -> limparFormulario());
        salvarButton.addActionListener(e -> salvarFrequencia());
        excluirButton.addActionListener(e -> excluirFrequencia());
        atualizarButton.addActionListener(e -> refreshData());
        
        // Seleção na tabela
        frequenciaTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarFrequenciaSelecionada();
            }
        });
    }
    
    private void registrarPresenca() {
        if (frequenciaAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um irmão para registrar presença!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            frequenciaDAO.incrementarPresenca(frequenciaAtual.getCodigoIrmao());
            JOptionPane.showMessageDialog(this, "Presença registrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarFrequenciaSelecionada();
            refreshData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao registrar presença: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void registrarFalta() {
        if (frequenciaAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um irmão para registrar falta!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            frequenciaDAO.incrementarFalta(frequenciaAtual.getCodigoIrmao());
            JOptionPane.showMessageDialog(this, "Falta registrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarFrequenciaSelecionada();
            refreshData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao registrar falta: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limparFormulario() {
        frequenciaAtual = null;
        nomeField.setText("");
        grauField.setText("");
        presencasField.setText("");
        faltasField.setText("");
        totalField.setText("");
        percentualLabel.setText("0.00%");
        nomeField.requestFocus();
        atualizarBotoesAcao();
    }
    
    private void salvarFrequencia() {
        try {
            if (nomeField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nome do irmão é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (grauField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Grau do irmão é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Frequencia frequencia = frequenciaAtual != null ? frequenciaAtual : new Frequencia();
            frequencia.setNomeIrmao(nomeField.getText().trim());
            frequencia.setGrau(grauField.getText().trim());
            
            // Se é novo registro, inicializa com zero presenças e faltas
            if (frequenciaAtual == null) {
                frequencia.setNumeroPresencas(0);
                frequencia.setNumeroFaltas(0);
                frequencia.setNumeroSecoes(0);
            }
            
            frequenciaDAO.save(frequencia);
            
            JOptionPane.showMessageDialog(this, "Frequência salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            refreshData();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar frequência: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void excluirFrequencia() {
        if (frequenciaAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um registro para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente excluir o registro de frequência do irmão " + frequenciaAtual.getNomeIrmao() + "?", 
            "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                frequenciaDAO.delete(frequenciaAtual.getId());
                JOptionPane.showMessageDialog(this, "Registro excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir registro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void atualizarBotoesAcao() {
        boolean temSelecao = frequenciaAtual != null;
        
        // Habilitar/desabilitar botões baseado na seleção
        salvarButton.setEnabled(!temSelecao); // Habilitar para novo registro
        excluirButton.setEnabled(temSelecao);  // Habilitar se tem seleção
        registrarPresencaButton.setEnabled(temSelecao);
        registrarFaltaButton.setEnabled(temSelecao);
    }
    
    private void carregarFrequenciaSelecionada() {
        int selectedRow = frequenciaTable.getSelectedRow();
        if (selectedRow >= 0) {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            try {
                frequenciaAtual = frequenciaDAO.findById(id);
                if (frequenciaAtual != null) {
                    nomeField.setText(frequenciaAtual.getNomeIrmao());
                    grauField.setText(frequenciaAtual.getGrau());
                    presencasField.setText(String.valueOf(frequenciaAtual.getNumeroPresencas()));
                    faltasField.setText(String.valueOf(frequenciaAtual.getNumeroFaltas()));
                    totalField.setText(String.valueOf(frequenciaAtual.getNumeroSecoes()));
                    percentualLabel.setText(String.format("%.2f%%", frequenciaAtual.getPercentualPresenca()));
                    
                    // Alterar cor do percentual baseado no valor
                    double percentual = frequenciaAtual.getPercentualPresenca();
                    if (percentual < 75) {
                        percentualLabel.setForeground(Color.RED);
                    } else if (percentual < 85) {
                        percentualLabel.setForeground(Color.ORANGE);
                    } else {
                        percentualLabel.setForeground(Color.BLUE);
                    }
                    atualizarBotoesAcao();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar frequência: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Se não há seleção, limpa o formulário
            limparFormulario();
        }
    }
    
    public void refreshData() {
        try {
            List<Frequencia> frequencias = frequenciaDAO.findAll();
            tableModel.setRowCount(0);
            
            for (Frequencia frequencia : frequencias) {
                Object[] row = {
                    frequencia.getId(),
                    frequencia.getNomeIrmao(),
                    frequencia.getGrau(),
                    frequencia.getNumeroPresencas(),
                    frequencia.getNumeroFaltas(),
                    frequencia.getNumeroSecoes(),
                    String.format("%.1f%%", frequencia.getPercentualPresenca())
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar frequências: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
