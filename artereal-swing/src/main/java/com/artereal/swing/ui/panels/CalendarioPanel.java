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
        descricaoField = new JTextField();
        dataField = new JTextField();
        horarioField = new JTextField();
        localField = new JTextField();
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
        // Aplicar estilização padrão ao painel principal
        PadraoLayout.estilizarPainelPrincipal(this);
        
        // Header estilizado usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader(" Calendário Maçônico", "Eventos e reuniões maçônicas");
        add(headerPanel, BorderLayout.NORTH);
        
        // Painel principal usando PadraoLayout
        JPanel mainPanel = PadraoLayout.criarPainelPrincipal();
        
        // Painel de busca no topo
        JPanel buscaPanel = PadraoLayout.criarPainelPesquisa(new JTextField(20), new JButton(" Buscar"));
        mainPanel.add(buscaPanel, BorderLayout.NORTH);
        
        // Painel do conteúdo com split vertical (Formulário acima, Tabela abaixo)
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(PadraoLayout.COR_FUNDO);
        
        // Painel de formulário usando PadraoLayout
        JPanel formPanel = PadraoLayout.criarGrupoFormulario(" Dados do Evento");
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        
        // SEÇÃO 1: Dados do Evento
        JPanel dadosEventoPanel = new JPanel(new BorderLayout());
        dadosEventoPanel.setBackground(Color.WHITE);
        dadosEventoPanel.setBorder(BorderFactory.createTitledBorder("📅 Dados do Evento"));
        
        JPanel dadosEventoContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        dadosEventoContent.setBackground(Color.WHITE);
        
        dadosEventoContent.add(PadraoLayout.criarLabelFormulario("Descrição:"));
        PadraoLayout.estilizarCampoDescricaoEvento(descricaoField);
        dadosEventoContent.add(descricaoField);
        
        dadosEventoContent.add(PadraoLayout.criarLabelFormulario("Data:"));
        PadraoLayout.estilizarCampoDataEvento(dataField);
        dadosEventoContent.add(dataField);
        
        dadosEventoContent.add(PadraoLayout.criarLabelFormulario("Horário:"));
        PadraoLayout.estilizarCampoHora(horarioField);
        dadosEventoContent.add(horarioField);
        
        dadosEventoPanel.add(dadosEventoContent, BorderLayout.CENTER);
        formContainer.add(dadosEventoPanel, BorderLayout.NORTH);
        
        // SEÇÃO 2: Localização e Tipo
        JPanel localizacaoPanel = new JPanel(new BorderLayout());
        localizacaoPanel.setBackground(Color.WHITE);
        localizacaoPanel.setBorder(BorderFactory.createTitledBorder("📍 Localização e Tipo"));
        
        JPanel localizacaoContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        localizacaoContent.setBackground(Color.WHITE);
        
        localizacaoContent.add(PadraoLayout.criarLabelFormulario("Local:"));
        PadraoLayout.estilizarCampoLocalEvento(localField);
        localizacaoContent.add(localField);
        
        localizacaoContent.add(PadraoLayout.criarLabelFormulario("Tipo:"));
        PadraoLayout.estilizarComboBox(tipoComboBox);
        localizacaoContent.add(tipoComboBox);
        
        localizacaoPanel.add(localizacaoContent, BorderLayout.CENTER);
        formContainer.add(localizacaoPanel, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botoesPanel.setBackground(Color.WHITE);
        botoesPanel.add(salvarButton);
        botoesPanel.add(novoButton);
        botoesPanel.add(excluirButton);
        
        // Painel de ações específicas do calendário
        JPanel acoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        acoesPanel.setBackground(Color.WHITE);
        acoesPanel.setBorder(BorderFactory.createTitledBorder("Ações do Evento"));
        acoesPanel.add(realizarButton);
        acoesPanel.add(cancelarButton);
        acoesPanel.add(proximosButton);
        acoesPanel.add(hojeButton);
        
        formContainer.add(botoesPanel, BorderLayout.SOUTH);
        formContainer.add(acoesPanel, BorderLayout.NORTH);
        
        formPanel.add(formContainer, BorderLayout.CENTER);
        
        // Painel de tabela usando PadraoLayout
        JPanel tabelaPanel = PadraoLayout.criarGrupoFormulario(" Eventos Programados");
        PadraoLayout.configurarTabela(calendarioTable);
        JScrollPane tableScrollPane = new JScrollPane(calendarioTable);
        
        tabelaPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Split vertical: Formulário acima (60%), Tabela abaixo (40%)
        JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formPanel, tabelaPanel);
        verticalSplitPane.setDividerLocation(350);
        verticalSplitPane.setResizeWeight(0.6);
        
        contentPanel.add(verticalSplitPane, BorderLayout.CENTER);
        
        // Adicionar contentPanel ao mainPanel
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
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
