package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.CalendarioDAO;
import com.artereal.swing.ui.layout.PadraoLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Painel de Calendário Maçônico
 */
public class CalendarioPanel extends JPanel {
    
    private CalendarioDAO calendarioDAO;
    private DefaultTableModel tableModel;
    private JTable calendarioTable;
    private JTextField descricaoField;
    private JTextField dataField;
    private JTextField horarioField;
    private JTextField localField;
    private JComboBox<String> tipoComboBox;
    private JButton salvarButton;
    private JButton novoButton;
    private JButton excluirButton;
    private JButton realizarButton;
    private JButton cancelarButton;
    private JButton proximosButton;
    private JButton hojeButton;
    private Long eventoAtualId;
    
    public CalendarioPanel() {
        calendarioDAO = new CalendarioDAO();
        initializeComponents();
        setupLayout();
        setupEvents();
        refreshData();
    }
    
    private void initializeComponents() {
        // Tabela
        tableModel = new DefaultTableModel(new Object[]{"ID", "Descrição", "Data", "Horário", "Local", "Tipo", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        calendarioTable = new JTable(tableModel);
        
        // Formulário
        descricaoField = new JTextField(40);
        dataField = new JTextField(12);
        horarioField = new JTextField(8);
        localField = new JTextField(30);
        tipoComboBox = new JComboBox<>(new String[]{
            "SESSAO_MAGNA", "SESSAO_BRANCA", "SESSAO_ELEICAO", 
            "CERIMONIA_INICIACAO", "CERIMONIA_ELEVACAO", "CERIMONIA_EXALTACAO",
            "INSTALACAO", "REUNIAO_ADMINISTRATIVA", "CONFRATERNIZACAO", "EVENTO_SOCIAL"
        });
        
        // Botões usando PadraoLayout com cores pastéis
        salvarButton = PadraoLayout.criarBotaoSalvar();
        novoButton = PadraoLayout.criarBotaoNovo();
        excluirButton = PadraoLayout.criarBotaoExcluir();
        realizarButton = PadraoLayout.criarBotao("Realizar", new Color(152, 251, 152)); // Verde menta
        cancelarButton = PadraoLayout.criarBotao("Cancelar", new Color(255, 182, 193)); // Rosa pastel
        proximosButton = PadraoLayout.criarBotao("Próximos 30 dias", new Color(173, 216, 230)); // Azul pastel
        hojeButton = PadraoLayout.criarBotao("Hoje", new Color(255, 250, 205)); // Amarelo pastel
        
        // Data atual como padrão
        dataField.setText(LocalDate.now().toString());
        horarioField.setText("20:00");
        localField.setText("Templo ArteReal");
        
        eventoAtualId = null;
    }
    
    private void setupLayout() {
        // Aplicar layout padrão usando PadraoLayout
        PadraoLayout.aplicarLayoutPadrao(this);
        
        // Header estilizado usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader("📅 Calendário Maçônico", "Eventos e reuniões maçônicas");
        add(headerPanel, BorderLayout.NORTH);
        
        // Painel de estatísticas
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Estatísticas de Eventos"));
        
        try {
            List<Object[]> estatisticas = calendarioDAO.getEstatisticas();
            for (Object[] est : estatisticas) {
                String tipo = (String) est[0];
                int quantidade = (Integer) est[1];
                int realizados = (Integer) est[2];
                int programados = (Integer) est[3];
                
                JPanel statPanel = new JPanel(new BorderLayout());
                statPanel.setBorder(BorderFactory.createEtchedBorder());
                
                Color color = Color.BLACK;
                switch (tipo) {
                    case "SESSAO_MAGNA": color = Color.BLUE; break;
                    case "SESSAO_BRANCA": color = Color.GREEN; break;
                    case "CERIMONIA_INICIACAO": color = Color.MAGENTA; break;
                    default: color = Color.DARK_GRAY; break;
                }
                
                JLabel tipoLabel = new JLabel(tipo.replace("_", " "), SwingConstants.CENTER);
                tipoLabel.setForeground(color);
                statPanel.add(tipoLabel, BorderLayout.NORTH);
                
                JLabel detalhesLabel = new JLabel(String.format("%d total\n%d realizados\n%d programados", quantidade, realizados, programados), SwingConstants.CENTER);
                detalhesLabel.setFont(new Font("Arial", Font.PLAIN, 10));
                statPanel.add(detalhesLabel, BorderLayout.CENTER);
                
                statsPanel.add(statPanel);
            }
        } catch (SQLException e) {
            statsPanel.add(new JLabel("Erro ao carregar estatísticas"));
        }
        
        // Painel de eventos próximos
        JPanel proximosPanel = new JPanel(new BorderLayout());
        proximosPanel.setBorder(BorderFactory.createTitledBorder("Próximos Eventos"));
        
        try {
            List<Object[]> proximos = calendarioDAO.findProximos(7);
            StringBuilder proximosText = new StringBuilder();
            
            if (proximos.isEmpty()) {
                proximosText.append("Nenhum evento nos próximos 7 dias");
            } else {
                for (Object[] evento : proximos) {
                    String data = (String) evento[2];
                    String descricao = (String) evento[1];
                    String horario = (String) evento[5];
                    proximosText.append(String.format("• %s - %s (%s)\n", data, descricao, horario));
                }
            }
            
            JTextArea proximosArea = new JTextArea(proximosText.toString());
            proximosArea.setEditable(false);
            proximosArea.setBackground(proximosPanel.getBackground());
            proximosPanel.add(proximosArea, BorderLayout.CENTER);
        } catch (SQLException e) {
            proximosPanel.add(new JLabel("Erro ao carregar próximos eventos"));
        }
        
        // Painel de formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados do Evento"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Descrição usando PadraoLayout
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(PadraoLayout.criarLabelFormulario("Descrição:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        PadraoLayout.estilizarCampoTexto(descricaoField);
        formPanel.add(descricaoField, gbc);
        
        // Data e Horário usando PadraoLayout
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(PadraoLayout.criarLabelFormulario("Data e Horário:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        JPanel dataHorarioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        PadraoLayout.estilizarCampoTexto(dataField);
        dataHorarioPanel.add(dataField);
        PadraoLayout.estilizarCampoTexto(horarioField);
        dataHorarioPanel.add(horarioField);
        formPanel.add(dataHorarioPanel, gbc);
        
        // Local usando PadraoLayout
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(PadraoLayout.criarLabelFormulario("Local:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        JPanel localTipoPanel = new JPanel(new BorderLayout());
        PadraoLayout.estilizarCampoTexto(localField);
        localTipoPanel.add(localField, BorderLayout.NORTH);
        localTipoPanel.add(PadraoLayout.criarLabelFormulario("Tipo:"), BorderLayout.CENTER);
        PadraoLayout.estilizarComboBox(tipoComboBox);
        localTipoPanel.add(tipoComboBox, BorderLayout.SOUTH);
        formPanel.add(localTipoPanel, gbc);
        
        // Botões do formulário
        JPanel botoesFormPanel = new JPanel(new FlowLayout());
        botoesFormPanel.add(salvarButton);
        botoesFormPanel.add(novoButton);
        botoesFormPanel.add(excluirButton);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(botoesFormPanel, gbc);
        
        // Botões de ações
        JPanel acoesPanel = new JPanel(new FlowLayout());
        acoesPanel.setBorder(BorderFactory.createTitledBorder("Ações do Evento"));
        acoesPanel.add(realizarButton);
        acoesPanel.add(cancelarButton);
        acoesPanel.add(proximosButton);
        acoesPanel.add(hojeButton);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(acoesPanel, gbc);
        
        // Painel da tabela
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBorder(BorderFactory.createTitledBorder("Eventos Programados"));
        tabelaPanel.add(new JScrollPane(calendarioTable), BorderLayout.CENTER);
        
        // Layout principal
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formPanel, tabelaPanel);
        splitPane.setDividerLocation(300);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(splitPane, BorderLayout.CENTER);
        centerPanel.add(proximosPanel, BorderLayout.SOUTH);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(statsPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Tabela estilizada
        calendarioTable.setRowHeight(25);
        calendarioTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        calendarioTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        calendarioTable.getTableHeader().setBackground(new Color(70, 130, 180));
        calendarioTable.getTableHeader().setForeground(Color.WHITE);
        calendarioTable.setSelectionBackground(new Color(173, 216, 230));
        calendarioTable.setSelectionForeground(new Color(25, 84, 123));
        
        // Layout principal com split
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setLeftComponent(formPanel);
        mainSplitPane.setRightComponent(tabelaPanel);
        mainSplitPane.setDividerLocation(500);
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainSplitPane, BorderLayout.CENTER);
    }
    
    private void setupEvents() {
        salvarButton.addActionListener(e -> salvarEvento());
        novoButton.addActionListener(e -> limparFormulario());
        excluirButton.addActionListener(e -> excluirEvento());
        realizarButton.addActionListener(e -> realizarEvento());
        cancelarButton.addActionListener(e -> cancelarEvento());
        proximosButton.addActionListener(e -> mostrarProximos());
        hojeButton.addActionListener(e -> mostrarHoje());
        
        // Seleção na tabela
        calendarioTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarEventoSelecionado();
            }
        });
    }
    
    private void salvarEvento() {
        try {
            if (descricaoField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Descrição é obrigatória!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (dataField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Data é obrigatória!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String descricao = descricaoField.getText().trim();
            String data = dataField.getText().trim();
            String horario = horarioField.getText().trim();
            String local = localField.getText().trim();
            String tipo = (String) tipoComboBox.getSelectedItem();
            
            calendarioDAO.save(descricao, data, tipo, local, horario, null);
            
            JOptionPane.showMessageDialog(this, "Evento salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            refreshData();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar evento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limparFormulario() {
        eventoAtualId = null;
        descricaoField.setText("");
        dataField.setText(LocalDate.now().toString());
        horarioField.setText("20:00");
        localField.setText("Templo ArteReal");
        tipoComboBox.setSelectedItem("SESSAO_MAGNA");
        descricaoField.requestFocus();
        atualizarBotoesAcao();
    }
    
    private void excluirEvento() {
        if (eventoAtualId == null) {
            JOptionPane.showMessageDialog(this, "Selecione um evento para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente excluir este evento?", 
            "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                calendarioDAO.delete(eventoAtualId);
                JOptionPane.showMessageDialog(this, "Evento excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir evento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void realizarEvento() {
        if (eventoAtualId == null) {
            JOptionPane.showMessageDialog(this, "Selecione um evento para marcar como realizado!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja marcar este evento como realizado?", 
            "Confirmar Realização", JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                calendarioDAO.realizar(eventoAtualId);
                JOptionPane.showMessageDialog(this, "Evento marcado como realizado!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarEventoSelecionado();
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao realizar evento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void cancelarEvento() {
        if (eventoAtualId == null) {
            JOptionPane.showMessageDialog(this, "Selecione um evento para cancelar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja cancelar este evento?", 
            "Confirmar Cancelamento", JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                calendarioDAO.cancelar(eventoAtualId);
                JOptionPane.showMessageDialog(this, "Evento cancelado!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarEventoSelecionado();
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao cancelar evento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void mostrarProximos() {
        try {
            List<Object[]> proximos = calendarioDAO.findProximos(30);
            StringBuilder texto = new StringBuilder();
            texto.append("PRÓXIMOS EVENTOS (30 dias)\n");
            texto.append("========================\n\n");
            
            if (proximos.isEmpty()) {
                texto.append("Nenhum evento programado para os próximos 30 dias.");
            } else {
                for (Object[] evento : proximos) {
                    String data = (String) evento[2];
                    String descricao = (String) evento[1];
                    String horario = (String) evento[5];
                    String local = (String) evento[4];
                    String status = (String) evento[3];
                    
                    texto.append(String.format("• %s - %s\n", data, descricao));
                    texto.append(String.format("  Horário: %s\n", horario));
                    texto.append(String.format("  Local: %s\n", local));
                    texto.append(String.format("  Status: %s\n", status));
                    texto.append("------------------------\n");
                }
            }
            
            JTextArea textArea = new JTextArea(texto.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Próximos Eventos", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar próximos eventos: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void mostrarHoje() {
        try {
            List<Object[]> hoje = calendarioDAO.findHoje();
            StringBuilder texto = new StringBuilder();
            texto.append("EVENTOS DE HOJE\n");
            texto.append("==============\n\n");
            
            if (hoje.isEmpty()) {
                texto.append("Nenhum evento programado para hoje.");
            } else {
                for (Object[] evento : hoje) {
                    String descricao = (String) evento[1];
                    String horario = (String) evento[5];
                    String local = (String) evento[4];
                    String tipo = (String) evento[3];
                    String status = (String) evento[6];
                    
                    texto.append(String.format("• %s\n", descricao));
                    texto.append(String.format("  Horário: %s\n", horario));
                    texto.append(String.format("  Local: %s\n", local));
                    texto.append(String.format("  Tipo: %s\n", tipo.replace("_", " ")));
                    texto.append(String.format("  Status: %s\n", status));
                    texto.append("------------------------\n");
                }
            }
            
            JTextArea textArea = new JTextArea(texto.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 300));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Eventos de Hoje", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar eventos de hoje: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void carregarEventoSelecionado() {
        int selectedRow = calendarioTable.getSelectedRow();
        if (selectedRow >= 0) {
            eventoAtualId = (Long) tableModel.getValueAt(selectedRow, 0);
            descricaoField.setText((String) tableModel.getValueAt(selectedRow, 1));
            dataField.setText((String) tableModel.getValueAt(selectedRow, 2));
            horarioField.setText((String) tableModel.getValueAt(selectedRow, 3));
            localField.setText((String) tableModel.getValueAt(selectedRow, 4));
            tipoComboBox.setSelectedItem((String) tableModel.getValueAt(selectedRow, 5));
            atualizarBotoesAcao();
        }
    }
    
    private void atualizarBotoesAcao() {
        boolean temEvento = eventoAtualId != null;
        
        realizarButton.setEnabled(temEvento);
        cancelarButton.setEnabled(temEvento);
        excluirButton.setEnabled(temEvento);
    }
    
    public void refreshData() {
        try {
            List<Object[]> eventos = calendarioDAO.findAll();
            tableModel.setRowCount(0);
            
            for (Object[] evento : eventos) {
                Object[] row = {
                    evento[0], // ID
                    evento[1], // Descrição
                    evento[2], // Data
                    evento[5], // Horário
                    evento[4], // Local
                    ((String) evento[3]).replace("_", " "), // Tipo
                    evento[6]  // Status
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar eventos: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
