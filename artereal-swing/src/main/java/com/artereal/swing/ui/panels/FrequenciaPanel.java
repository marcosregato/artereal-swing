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
        
        // Botões usando PadraoLayout com cores pastéis
        registrarPresencaButton = PadraoLayout.criarBotao("Registrar Presença", new Color(152, 251, 152)); // Verde menta
        registrarFaltaButton = PadraoLayout.criarBotao("Registrar Falta", new Color(255, 182, 193)); // Rosa pastel
        novoButton = PadraoLayout.criarBotaoNovo();
        salvarButton = PadraoLayout.criarBotaoSalvar();
        excluirButton = PadraoLayout.criarBotaoExcluir();
        atualizarButton = PadraoLayout.criarBotao("Atualizar", new Color(173, 216, 230)); // Azul pastel
        
        frequenciaAtual = null;
    }
    
    private void setupLayout() {
        // Aplicar layout padrão usando PadraoLayout
        PadraoLayout.aplicarLayoutPadrao(this);
        
        // Header estilizado usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader("📊 Controle de Frequência", "Gestão de presenças e estatísticas");
        add(headerPanel, BorderLayout.NORTH);
        
        // Painel principal usando PadraoLayout
        JSplitPane splitPane = PadraoLayout.criarSplitPaneVertical(null, null);
        
        // Painel esquerdo - Tabela e estatísticas
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
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
        
        // Painel de estatísticas
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        statsPanel.setBackground(new Color(245, 245, 250));
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 210)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
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
        
        // Tabela estilizada
        JScrollPane tableScrollPane = new JScrollPane(frequenciaTable);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 210)));
        tableScrollPane.getViewport().setBackground(Color.WHITE);
        
        // Tabela estilizada usando PadraoLayout
        PadraoLayout.configurarTabela(frequenciaTable);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(pesquisaPanel, BorderLayout.NORTH);
        topPanel.add(statsPanel, BorderLayout.CENTER);
        topPanel.add(tableScrollPane, BorderLayout.SOUTH);
        
        leftPanel.add(topPanel, BorderLayout.CENTER);
        
        // Painel direito - Formulário (padrão SessoesPanel)
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        
        // Título do formulário
        JPanel formHeaderPanel = new JPanel(new BorderLayout());
        formHeaderPanel.setBackground(new Color(245, 245, 250));
        formHeaderPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel formTitleLabel = new JLabel("📝 Dados da Frequência", SwingConstants.LEFT);
        formTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitleLabel.setForeground(new Color(70, 130, 180));
        
        formHeaderPanel.add(formTitleLabel, BorderLayout.CENTER);
        rightPanel.add(formHeaderPanel, BorderLayout.NORTH);
        
        // Painel de formulário
        JPanel formularioPanel = new JPanel(new BorderLayout());
        formularioPanel.setBackground(Color.WHITE);
        formularioPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        JLabel formTitle = new JLabel("📝 Dados da Frequência");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formTitle.setForeground(new Color(70, 130, 180));
        
        // Container principal para todos os grupos
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        
        // Grupo 1: Dados Básicos
        JPanel dadosBasicosPanel = createFormGroup("📅 Dados Básicos");
        JPanel dadosBasicosContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        dadosBasicosContent.setBackground(Color.WHITE);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Irmão:"));
        PadraoLayout.estilizarCampoTexto(nomeField);
        dadosBasicosContent.add(nomeField);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Grau:"));
        PadraoLayout.estilizarCampoTexto(grauField);
        dadosBasicosContent.add(grauField);
        
        dadosBasicosPanel.add(dadosBasicosContent);
        
        // Grupo 2: Controle de Frequência
        JPanel controlePanel = createFormGroup("📊 Controle de Frequência");
        JPanel controleContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        controleContent.setBackground(Color.WHITE);
        
        controleContent.add(PadraoLayout.criarLabelFormulario("Presenças:"));
        PadraoLayout.estilizarCampoTexto(presencasField);
        controleContent.add(presencasField);
        
        controleContent.add(PadraoLayout.criarLabelFormulario("Faltas:"));
        PadraoLayout.estilizarCampoTexto(faltasField);
        controleContent.add(faltasField);
        
        controleContent.add(PadraoLayout.criarLabelFormulario("Total:"));
        PadraoLayout.estilizarCampoTexto(totalField);
        controleContent.add(totalField);
        
        controlePanel.add(controleContent);
        
        // Grupo 3: Estatísticas
        JPanel estatisticasPanel = createFormGroup("📈 Estatísticas");
        JPanel estatisticasContent = new JPanel(new BorderLayout());
        estatisticasContent.setBackground(Color.WHITE);
        
        JPanel percentualPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        percentualPanel.setBackground(Color.WHITE);
        percentualPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 210), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        percentualPanel.add(new JLabel("Percentual de Presença:"));
        percentualPanel.add(percentualLabel);
        
        estatisticasContent.add(percentualPanel, BorderLayout.CENTER);
        estatisticasPanel.add(estatisticasContent);
        
        // Organizar grupos verticalmente usando PadraoLayout
        JPanel allGroups = PadraoLayout.criarFormularioMultiplosGrupos(
            dadosBasicosPanel, controlePanel, estatisticasPanel
        );
        
        // Adicionar scroll ao formulário usando PadraoLayout
        JScrollPane formScroll = PadraoLayout.criarFormularioComScroll(allGroups);
        
        formContainer.add(formScroll, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botoesPanel.setBackground(Color.WHITE);
        
        botoesPanel.add(createStyledButton("Registrar Presença", new Color(144, 238, 144)));
        botoesPanel.add(createStyledButton("Registrar Falta", new Color(255, 182, 193)));
        botoesPanel.add(createStyledButton("Novo", new Color(173, 216, 230)));
        botoesPanel.add(createStyledButton("Salvar", new Color(255, 250, 205)));
        botoesPanel.add(createStyledButton("Excluir", new Color(255, 218, 185)));
        botoesPanel.add(createStyledButton("Atualizar", new Color(240, 240, 240)));
        
        formularioPanel.add(formTitle, BorderLayout.NORTH);
        formularioPanel.add(formContainer, BorderLayout.CENTER);
        formularioPanel.add(botoesPanel, BorderLayout.SOUTH);
        
        rightPanel.add(formularioPanel, BorderLayout.CENTER);
        
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        
        add(splitPane, BorderLayout.CENTER);
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
