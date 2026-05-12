package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.FrequenciaDAO;
import com.artereal.swing.model.Frequencia;
import com.artereal.swing.ui.layout.PadraoLayout;
import com.artereal.swing.ui.panels.frequencia.FrequenciaFormPanel;

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
    private FrequenciaFormPanel frequenciaFormPanel;
    
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
        
        // Inicializar formulário otimizado
        frequenciaFormPanel = new FrequenciaFormPanel();
        
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
        
        // Painel de formulário otimizado usando FrequenciaFormPanel
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(PadraoLayout.COR_PAINEL);
        formPanel.setBorder(PadraoLayout.BORDA_CONTEUDO);
        
        // Usar o formulário otimizado
        formPanel.add(frequenciaFormPanel, BorderLayout.CENTER);
        
        // Painel de tabela usando PadraoLayout
        JPanel tabelaPanel = PadraoLayout.criarGrupoFormulario("📊 Registros de Frequência");
        PadraoLayout.configurarTabela(frequenciaTable);
        JScrollPane tableScrollPane = new JScrollPane(frequenciaTable);
        
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
            frequencia.setNomeIrmao(frequenciaFormPanel.getNomeField().getText());
            frequencia.setGrau(frequenciaFormPanel.getGrauField().getText());
            frequencia.setNumeroPresencas(Integer.parseInt(frequenciaFormPanel.getPresencasField().getText()));
            frequencia.setNumeroFaltas(Integer.parseInt(frequenciaFormPanel.getFaltasField().getText()));
            frequencia.setNumeroSecoes(Integer.parseInt(frequenciaFormPanel.getTotalField().getText()));
            
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
        frequenciaFormPanel.clearForm();
        frequenciaTable.clearSelection();
    }
    
    private void carregarFrequenciaSelecionada() {
        int selectedRow = frequenciaTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                Long id = (Long) tableModel.getValueAt(selectedRow, 0);
                frequenciaAtual = frequenciaDAO.findById(id);
                if (frequenciaAtual != null) {
                    frequenciaFormPanel.getNomeField().setText(frequenciaAtual.getNomeIrmao());
                    frequenciaFormPanel.getGrauField().setText(frequenciaAtual.getGrau());
                    frequenciaFormPanel.getPresencasField().setText(String.valueOf(frequenciaAtual.getNumeroPresencas()));
                    frequenciaFormPanel.getFaltasField().setText(String.valueOf(frequenciaAtual.getNumeroFaltas()));
                    frequenciaFormPanel.getTotalField().setText(String.valueOf(frequenciaAtual.getNumeroSecoes()));
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
