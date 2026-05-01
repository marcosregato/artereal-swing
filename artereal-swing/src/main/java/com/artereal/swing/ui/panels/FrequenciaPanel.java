package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.FrequenciaDAO;
import com.artereal.swing.model.Frequencia;
import com.artereal.swing.ui.layout.PadraoLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Painel de Controle de Frequência - Layout padrão Header → Busca → Formulário → Tabela
 */
public class FrequenciaPanel extends JPanel {
    
    private FrequenciaDAO frequenciaDAO;
    private DefaultTableModel tableModel;
    private JTable frequenciaTable;
    private Frequencia frequenciaAtual;
    private JTextField pesquisarField;
    
    // Formulário
    private JTextField nomeField;
    private JTextField grauField;
    private JTextField presencasField;
    private JTextField faltasField;
    private JTextField totalField;
    
    // Botões
    private JButton salvarButton;
    private JButton novoButton;
    private JButton editarButton;
    private JButton excluirButton;
    private JButton limparButton;
    private JButton pesquisarButton;
    private JButton registrarPresencaButton;
    private JButton registrarFaltaButton;
    
    public FrequenciaPanel() {
        frequenciaDAO = new FrequenciaDAO();
        initializeComponents();
        setupLayout();
        setupEvents();
        refreshData();
    }
    
    private void initializeComponents() {
        // Tabela
        tableModel = new DefaultTableModel(new Object[]{
            "ID", "Irmão", "Grau", "Presenças", "Faltas", "Total", "Percentual"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        frequenciaTable = new JTable(tableModel);
        
        // Formulário
        nomeField = new JTextField(40);
        grauField = new JTextField(20);
        presencasField = new JTextField(10);
        faltasField = new JTextField(10);
        totalField = new JTextField(10);
        
        // Botões
        salvarButton = PadraoLayout.criarBotaoSalvar();
        novoButton = PadraoLayout.criarBotaoNovo();
        editarButton = PadraoLayout.criarBotaoEditar();
        excluirButton = PadraoLayout.criarBotaoExcluir();
        limparButton = PadraoLayout.criarBotaoLimpar();
        pesquisarButton = PadraoLayout.criarBotaoPesquisar();
        registrarPresencaButton = PadraoLayout.criarBotao("✅ Registrar Presença", new Color(144, 238, 144));
        registrarFaltaButton = PadraoLayout.criarBotao("❌ Registrar Falta", new Color(255, 182, 193));
        pesquisarField = new JTextField(20);
    }
    
    private void setupLayout() {
        // Aplicar layout padrão usando PadraoLayout
        PadraoLayout.aplicarLayoutPadrao(this);
        
        // Header estilizado usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader("📊 Controle de Frequência", "Gestão de presenças e estatísticas");
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
        JPanel formPanel = PadraoLayout.criarGrupoFormulario("📝 Dados da Frequência");
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        
        // Container principal com layout vertical para colocar painéis um debaixo do outro
        JPanel formContent = new JPanel();
        formContent.setLayout(new BoxLayout(formContent, BoxLayout.Y_AXIS));
        formContent.setBackground(Color.WHITE);
        formContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // SEÇÃO 1: Dados do Irmão
        JPanel dadosIrmaoPanel = new JPanel(new BorderLayout());
        dadosIrmaoPanel.setBackground(Color.WHITE);
        dadosIrmaoPanel.setBorder(BorderFactory.createTitledBorder("👤 Dados do Irmão"));
        dadosIrmaoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel dadosIrmaoContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        dadosIrmaoContent.setBackground(Color.WHITE);
        
        dadosIrmaoContent.add(PadraoLayout.criarLabelFormulario("Irmão:"));
        PadraoLayout.estilizarCampoNomeIrmao(nomeField);
        dadosIrmaoContent.add(nomeField);
        
        dadosIrmaoContent.add(PadraoLayout.criarLabelFormulario("Grau:"));
        PadraoLayout.estilizarCampoGrauMasonicoFrequencia(grauField);
        dadosIrmaoContent.add(grauField);
        
        dadosIrmaoPanel.add(dadosIrmaoContent, BorderLayout.CENTER);
        formContent.add(dadosIrmaoPanel);
        formContent.add(Box.createVerticalStrut(15)); // Espaçamento entre painéis
        
        // SEÇÃO 2: Controle de Frequência
        JPanel frequenciaPanel = new JPanel(new BorderLayout());
        frequenciaPanel.setBackground(Color.WHITE);
        frequenciaPanel.setBorder(BorderFactory.createTitledBorder("📊 Controle de Frequência"));
        frequenciaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Layout personalizado com 3 colunas para Presenças, Faltas e Total na mesma linha
        JPanel frequenciaContent = new JPanel(new GridBagLayout());
        frequenciaContent.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Presenças - coluna 0
        gbc.gridx = 0;
        gbc.gridy = 0;
        frequenciaContent.add(PadraoLayout.criarLabelFormulario("Presenças:"), gbc);
        gbc.gridx = 1;
        PadraoLayout.estilizarCampoContagemFrequencia(presencasField);
        frequenciaContent.add(presencasField, gbc);
        
        // Faltas - coluna 2
        gbc.gridx = 2;
        gbc.gridy = 0;
        frequenciaContent.add(PadraoLayout.criarLabelFormulario("Faltas:"), gbc);
        gbc.gridx = 3;
        PadraoLayout.estilizarCampoContagemFrequencia(faltasField);
        frequenciaContent.add(faltasField, gbc);
        
        // Total - coluna 4
        gbc.gridx = 4;
        gbc.gridy = 0;
        frequenciaContent.add(PadraoLayout.criarLabelFormulario("Total:"), gbc);
        gbc.gridx = 5;
        PadraoLayout.estilizarCampoContagemFrequencia(totalField);
        frequenciaContent.add(totalField, gbc);
        
        frequenciaPanel.add(frequenciaContent, BorderLayout.CENTER);
        formContent.add(frequenciaPanel);
        formContent.add(Box.createVerticalStrut(15)); // Espaçamento após o segundo painel
        
        // Painel de botões
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botoesPanel.setBackground(Color.WHITE);
        botoesPanel.add(salvarButton);
        botoesPanel.add(novoButton);
        botoesPanel.add(editarButton);
        botoesPanel.add(excluirButton);
        botoesPanel.add(limparButton);
        botoesPanel.add(registrarPresencaButton);
        botoesPanel.add(registrarFaltaButton);
        
        formContainer.add(formContent, BorderLayout.CENTER);
        formContainer.add(botoesPanel, BorderLayout.SOUTH);
        
        formPanel.add(formContainer, BorderLayout.CENTER);
        
        // Painel de estatísticas separado do formulário
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 10, 5));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(BorderFactory.createTitledBorder("📊 Estatísticas por Grau"));
        
        try {
            List<Object[]> estatisticas = frequenciaDAO.getEstatisticasPorGrau();
            for (Object[] est : estatisticas) {
                String grau = (String) est[0];
                int total = (Integer) est[1];
                double media = (Double) est[2];
                
                JPanel grauPanel = new JPanel(new BorderLayout());
                grauPanel.setBackground(new Color(240, 240, 245));
                grauPanel.setBorder(BorderFactory.createEtchedBorder());
                
                JLabel grauLabel = new JLabel(grau + " (" + total + ")", SwingConstants.CENTER);
                grauLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
                grauLabel.setForeground(new Color(70, 130, 180));
                grauPanel.add(grauLabel, BorderLayout.NORTH);
                
                JLabel mediaLabel = new JLabel(String.format("%.1f%%", media), SwingConstants.CENTER);
                mediaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
                mediaLabel.setForeground(new Color(46, 125, 50));
                grauPanel.add(mediaLabel, BorderLayout.CENTER);
                
                statsPanel.add(grauPanel);
            }
        } catch (SQLException e) {
            statsPanel.add(new JLabel("Erro ao carregar estatísticas"));
        }
        
        // Painel de tabela usando PadraoLayout
        JPanel tabelaPanel = PadraoLayout.criarGrupoFormulario("📊 Registros de Frequência");
        PadraoLayout.configurarTabela(frequenciaTable);
        JScrollPane tableScrollPane = new JScrollPane(frequenciaTable);
        
        tabelaPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Adicionar painel de estatísticas entre formulário e tabela
        JPanel statsContainer = new JPanel(new BorderLayout());
        statsContainer.setBackground(Color.WHITE);
        statsContainer.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        statsContainer.add(statsPanel, BorderLayout.CENTER);
        
        // Estrutura vertical: Formulário → Tabela → Estatísticas
        JPanel verticalPanel = new JPanel(new BorderLayout());
        verticalPanel.setBackground(PadraoLayout.COR_FUNDO);
        verticalPanel.add(formPanel, BorderLayout.NORTH);
        verticalPanel.add(tabelaPanel, BorderLayout.CENTER);
        verticalPanel.add(statsContainer, BorderLayout.SOUTH);
        
        contentPanel.add(verticalPanel, BorderLayout.CENTER);
        
        // Adicionar contentPanel ao mainPanel
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupEvents() {
        salvarButton.addActionListener(e -> salvarFrequencia());
        novoButton.addActionListener(e -> limparFormulario());
        editarButton.addActionListener(e -> carregarFrequenciaSelecionada());
        excluirButton.addActionListener(e -> excluirFrequencia());
        limparButton.addActionListener(e -> limparFormulario());
        pesquisarButton.addActionListener(e -> pesquisarFrequencias());
        registrarPresencaButton.addActionListener(e -> registrarPresenca());
        registrarFaltaButton.addActionListener(e -> registrarFalta());
        
        frequenciaTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarFrequenciaSelecionada();
            }
        });
    }
    
    private void salvarFrequencia() {
        try {
            Frequencia frequencia = new Frequencia();
            frequencia.setNomeIrmao(nomeField.getText());
            frequencia.setGrau(grauField.getText());
            frequencia.setNumeroPresencas(Integer.parseInt(presencasField.getText()));
            frequencia.setNumeroFaltas(Integer.parseInt(faltasField.getText()));
            frequencia.setNumeroSecoes(Integer.parseInt(totalField.getText()));
            
            if (frequenciaAtual == null) {
                frequenciaDAO.save(frequencia);
            } else {
                frequencia.setId(frequenciaAtual.getId());
                frequenciaDAO.save(frequencia);
            }
            
            JOptionPane.showMessageDialog(this, "Frequência salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            refreshData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar frequência: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro: Preencha os campos numéricos corretamente", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void excluirFrequencia() {
        if (frequenciaAtual != null) {
            int option = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este registro?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    frequenciaDAO.delete(frequenciaAtual.getId());
                    JOptionPane.showMessageDialog(this, "Registro excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    limparFormulario();
                    refreshData();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir registro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void limparFormulario() {
        frequenciaAtual = null;
        nomeField.setText("");
        grauField.setText("");
        presencasField.setText("");
        faltasField.setText("");
        totalField.setText("");
        frequenciaTable.clearSelection();
    }
    
    private void carregarFrequenciaSelecionada() {
        int selectedRow = frequenciaTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                Long id = (Long) tableModel.getValueAt(selectedRow, 0);
                frequenciaAtual = frequenciaDAO.findById(id);
                if (frequenciaAtual != null) {
                    nomeField.setText(frequenciaAtual.getNomeIrmao());
                    grauField.setText(frequenciaAtual.getGrau());
                    presencasField.setText(String.valueOf(frequenciaAtual.getNumeroPresencas()));
                    faltasField.setText(String.valueOf(frequenciaAtual.getNumeroFaltas()));
                    totalField.setText(String.valueOf(frequenciaAtual.getNumeroSecoes()));
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar registro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void pesquisarFrequencias() {
        String termo = pesquisarField.getText().trim();
        if (termo.isEmpty()) {
            refreshData();
        } else {
            try {
                List<Frequencia> frequencias = frequenciaDAO.findAll();
                List<Frequencia> frequenciasFiltradas = frequencias.stream()
                    .filter(f -> f.getNomeIrmao() != null && f.getNomeIrmao().toLowerCase().contains(termo.toLowerCase()))
                    .toList();
                atualizarTabela(frequenciasFiltradas);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao pesquisar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void registrarPresenca() {
        if (frequenciaAtual != null) {
            try {
                frequenciaAtual.setNumeroPresencas(frequenciaAtual.getNumeroPresencas() + 1);
                frequenciaAtual.setNumeroSecoes(frequenciaAtual.getNumeroSecoes() + 1);
                frequenciaDAO.save(frequenciaAtual);
                JOptionPane.showMessageDialog(this, "Presença registrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarFrequenciaSelecionada();
                refreshData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao registrar presença: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um registro para registrar presença", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void registrarFalta() {
        if (frequenciaAtual != null) {
            try {
                frequenciaAtual.setNumeroFaltas(frequenciaAtual.getNumeroFaltas() + 1);
                frequenciaAtual.setNumeroSecoes(frequenciaAtual.getNumeroSecoes() + 1);
                frequenciaDAO.save(frequenciaAtual);
                JOptionPane.showMessageDialog(this, "Falta registrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarFrequenciaSelecionada();
                refreshData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao registrar falta: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um registro para registrar falta", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public void refreshData() {
        try {
            List<Frequencia> frequencias = frequenciaDAO.findAll();
            atualizarTabela(frequencias);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void atualizarTabela(List<Frequencia> frequencias) {
        tableModel.setRowCount(0);
        for (Frequencia frequencia : frequencias) {
            double percentual = 0.0;
            if (frequencia.getNumeroSecoes() > 0) {
                percentual = (double) frequencia.getNumeroPresencas() / frequencia.getNumeroSecoes() * 100;
            }
            
            Object[] row = {
                frequencia.getId(),
                frequencia.getNomeIrmao(),
                frequencia.getGrau(),
                frequencia.getNumeroPresencas(),
                frequencia.getNumeroFaltas(),
                frequencia.getNumeroSecoes(),
                String.format("%.1f%%", percentual)
            };
            tableModel.addRow(row);
        }
    }
}
