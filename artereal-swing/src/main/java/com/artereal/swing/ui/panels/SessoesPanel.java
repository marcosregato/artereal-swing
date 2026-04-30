package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.SessaoDAO;
import com.artereal.swing.model.Sessao;
import com.artereal.swing.ui.layout.PadraoLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Painel de gestão de Sessões Maçônicas - Layout padrão RelatoriosPanel
 */
public class SessoesPanel extends JPanel {
    
    private SessaoDAO sessaoDAO;
    private DefaultTableModel tableModel;
    private JTable sessoesTable;
    private Sessao sessaoAtual;
    private JTextField pesquisarField;
    
    // Formulário
    private JTextField codigoField;
    private JTextField dataHoraField;
    private JTextField localField;
    private JTextField presidenteField;
    private JTextField secretarioField;
    private JTextField tesoureiroField;
    private JTextField oradorField;
    private JTextField temaField;
    private JTextArea pautaArea;
    private JTextArea observacoesArea;
    private JTextField presentesField;
    private JTextField visitantesField;
    
    // Botões
    private JButton salvarButton;
    private JButton novoButton;
    private JButton editarButton;
    private JButton excluirButton;
    private JButton limparButton;
    private JButton pesquisarButton;
    
    // Combos para seleção
    private JComboBox<String> tipoCombo;
    private JComboBox<String> statusCombo;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    public SessoesPanel() {
        sessaoDAO = new SessaoDAO();
        initializeComponents();
        setupLayout();
        setupEvents();
        refreshData();
    }
    
    private void initializeComponents() {
        // Tabela
        tableModel = new DefaultTableModel(new Object[]{
            "ID", "Tipo", "Data/Hora", "Local", "Presidente", "Status"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        sessoesTable = new JTable(tableModel);
        
        // Formulário
        codigoField = new JTextField(10);
        dataHoraField = new JTextField(20);
        localField = new JTextField(30);
        presidenteField = new JTextField(30);
        secretarioField = new JTextField(30);
        tesoureiroField = new JTextField(30);
        oradorField = new JTextField(30);
        temaField = new JTextField(40);
        pautaArea = new JTextArea(3, 30);
        observacoesArea = new JTextArea(3, 30);
        presentesField = new JTextField(10);
        visitantesField = new JTextField(10);
        
        // Combos
        tipoCombo = new JComboBox<>(new String[]{"Sessão Magna", "Sessão Ordinária", "Sessão Administrativa", "Sessão de Iniciação", "Sessão de Elevação", "Sessão Fúnebre"});
        statusCombo = new JComboBox<>(new String[]{"Planejada", "Realizada", "Cancelada", "Adiada"});
        
        // Botões usando PadraoLayout com cores pastéis
        salvarButton = PadraoLayout.criarBotaoSalvar();
        novoButton = PadraoLayout.criarBotaoNovo();
        editarButton = PadraoLayout.criarBotaoEditar();
        excluirButton = PadraoLayout.criarBotaoExcluir();
        limparButton = PadraoLayout.criarBotaoLimpar();
        pesquisarButton = PadraoLayout.criarBotaoPesquisar();
        pesquisarField = new JTextField(20);
    }
    
    private void setupLayout() {
        // Aplicar layout padrão usando PadraoLayout - CORREÇÃO CRÍTICA
        this.setBorder(PadraoLayout.BORDA_PAINEL);
        this.setBackground(PadraoLayout.COR_FUNDO);
        
        // Header estilizado usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader("📝 Gestão de Sessões", "Cadastro e administração de sessões maçônicas");
        add(headerPanel, BorderLayout.NORTH);
        
        // Painel principal (padrão RelatoriosPanel)
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Painel de pesquisa usando PadraoLayout
        JPanel filtroPanel = PadraoLayout.criarPainelPesquisa(pesquisarField, pesquisarButton);
        
        // Painel de conteúdo com tabela e formulário
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 210)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Tabela de sessões usando PadraoLayout
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBackground(Color.WHITE);
        
        PadraoLayout.configurarTabela(sessoesTable);
        tabelaPanel.add(new JScrollPane(sessoesTable), BorderLayout.CENTER);
        
        // Painel de formulário
        JPanel formularioPanel = new JPanel(new BorderLayout());
        formularioPanel.setBackground(Color.WHITE);
        formularioPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        JLabel formTitle = new JLabel("📝 Dados da Sessão");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formTitle.setForeground(new Color(70, 130, 180));
        
        // Container principal para todos os grupos
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        
        // Grupo 1: Dados Básicos usando PadraoLayout
        JPanel dadosBasicosPanel = PadraoLayout.criarGrupoFormulario(" Dados Básicos");
        JPanel dadosBasicosContent = new JPanel(PadraoLayout.criarLayoutGrupo());
        dadosBasicosContent.setBackground(Color.WHITE);
        
        dadosBasicosContent.add(new JLabel("Código:"));
        dadosBasicosContent.add(codigoField);
        dadosBasicosContent.add(new JLabel("Data e Hora:"));
        dadosBasicosContent.add(dataHoraField);
        dadosBasicosContent.add(new JLabel("Local:"));
        dadosBasicosContent.add(localField);
        dadosBasicosContent.add(new JLabel("Tipo:"));
        dadosBasicosContent.add(tipoCombo);
        
        dadosBasicosPanel.add(dadosBasicosContent);
        
        // Grupo 2: Diretoria usando PadraoLayout
        JPanel diretoriaPanel = PadraoLayout.criarGrupoFormulario(" Diretoria da Sessão");
        JPanel diretoriaContent = new JPanel(PadraoLayout.criarLayoutGrupo());
        diretoriaContent.setBackground(Color.WHITE);
        
        diretoriaContent.add(new JLabel("Presidente:"));
        diretoriaContent.add(presidenteField);
        diretoriaContent.add(new JLabel("Secretário:"));
        diretoriaContent.add(secretarioField);
        diretoriaContent.add(new JLabel("Tesoureiro:"));
        diretoriaContent.add(tesoureiroField);
        diretoriaContent.add(new JLabel("Orador:"));
        diretoriaContent.add(oradorField);
        
        diretoriaPanel.add(diretoriaContent);
        
        // Grupo 3: Participantes usando PadraoLayout
        JPanel participantesPanel = PadraoLayout.criarGrupoFormulario(" Participantes");
        JPanel participantesContent = new JPanel(PadraoLayout.criarLayoutGrupo());
        participantesContent.setBackground(Color.WHITE);
        
        participantesContent.add(new JLabel("Presentes:"));
        participantesContent.add(presentesField);
        participantesContent.add(new JLabel("Visitantes:"));
        participantesContent.add(visitantesField);
        
        participantesPanel.add(participantesContent);
        
        // Grupo 4: Conteúdo da Sessão usando PadraoLayout
        JPanel conteudoPanel = PadraoLayout.criarGrupoFormulario(" Conteúdo da Sessão");
        JPanel conteudoContent = new JPanel(new BorderLayout());
        conteudoContent.setBackground(Color.WHITE);
        
        JPanel pautaContainer = new JPanel(new BorderLayout());
        pautaContainer.setBackground(Color.WHITE);
        pautaContainer.add(new JLabel("Pauta:"), BorderLayout.NORTH);
        pautaArea.setLineWrap(true);
        pautaArea.setWrapStyleWord(true);
        pautaArea.setBorder(PadraoLayout.BORDA_CAMPO);
        pautaContainer.add(new JScrollPane(pautaArea), BorderLayout.CENTER);
        
        JPanel obsContainer = new JPanel(new BorderLayout());
        obsContainer.setBackground(Color.WHITE);
        obsContainer.add(new JLabel("Observações:"), BorderLayout.NORTH);
        observacoesArea.setLineWrap(true);
        observacoesArea.setWrapStyleWord(true);
        observacoesArea.setBorder(PadraoLayout.BORDA_CAMPO);
        obsContainer.add(new JScrollPane(observacoesArea), BorderLayout.CENTER);
        
        conteudoContent.add(pautaContainer, BorderLayout.NORTH);
        conteudoContent.add(obsContainer, BorderLayout.CENTER);
        
        conteudoPanel.add(conteudoContent);
        
        // Organizar grupos verticalmente usando PadraoLayout
        JPanel allGroups = PadraoLayout.criarFormularioMultiplosGrupos(
            dadosBasicosPanel, diretoriaPanel, participantesPanel, conteudoPanel
        );
        
        // Adicionar scroll ao formulário usando PadraoLayout
        JScrollPane formScroll = PadraoLayout.criarFormularioComScroll(allGroups);
        
        formContainer.add(formScroll, BorderLayout.CENTER);
        
        // Painel de botões usando PadraoLayout
        JPanel botoesPanel = PadraoLayout.criarPainelBotoes(
            PadraoLayout.criarBotao("Salvar", PadraoLayout.COR_BOTAO_SALVAR),
            PadraoLayout.criarBotao("Novo", PadraoLayout.COR_BOTAO_NOVO),
            PadraoLayout.criarBotao("Editar", PadraoLayout.COR_BOTAO_EDITAR),
            PadraoLayout.criarBotao("Excluir", PadraoLayout.COR_BOTAO_EXCLUIR),
            PadraoLayout.criarBotao("Limpar", PadraoLayout.COR_BOTAO_LIMPAR)
        );
        
        formularioPanel.add(formTitle, BorderLayout.NORTH);
        formularioPanel.add(formContainer, BorderLayout.CENTER);
        formularioPanel.add(botoesPanel, BorderLayout.SOUTH);
        
        // Split pane para tabela e formulário
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tabelaPanel, formularioPanel);
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.6);
        
        contentPanel.add(splitPane, BorderLayout.CENTER);
        
        mainPanel.add(filtroPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
        
    private void setupEvents() {
        salvarButton.addActionListener(e -> salvarSessao());
        novoButton.addActionListener(e -> limparFormulario());
        editarButton.addActionListener(e -> carregarSessaoSelecionada());
        excluirButton.addActionListener(e -> excluirSessao());
        limparButton.addActionListener(e -> limparFormulario());
        pesquisarButton.addActionListener(e -> pesquisarSessoes());
        
        sessoesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarSessaoSelecionada();
            }
        });
    }
    
    private void salvarSessao() {
        try {
            Sessao sessao = new Sessao();
            sessao.setTipo((String) tipoCombo.getSelectedItem());
            sessao.setDataHora(LocalDateTime.parse(dataHoraField.getText(), DATE_FORMATTER));
            sessao.setLocal(localField.getText());
            sessao.setPresidente(presidenteField.getText());
            sessao.setSecretario(secretarioField.getText());
            sessao.setTesoureiro(tesoureiroField.getText());
            sessao.setOrador(oradorField.getText());
            sessao.setTema(temaField.getText());
            sessao.setPauta(pautaArea.getText());
            sessao.setObservacoes(observacoesArea.getText());
            sessao.setQuantidadePresentes(Integer.parseInt(presentesField.getText()));
            sessao.setQuantidadeVisitantes(Integer.parseInt(visitantesField.getText()));
            sessao.setStatus((String) statusCombo.getSelectedItem());
            
            if (sessaoAtual == null) {
                sessaoDAO.save(sessao);
            } else {
                sessao.setId(sessaoAtual.getId());
                sessaoDAO.save(sessao);
            }
            
            JOptionPane.showMessageDialog(this, "Sessão salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            refreshData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar sessão: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void carregarSessaoSelecionada() {
        int selectedRow = sessoesTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                Long id = (Long) tableModel.getValueAt(selectedRow, 0);
                sessaoAtual = sessaoDAO.findById(id);
                if (sessaoAtual != null) {
                    preencherFormulario(sessaoAtual);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar sessão: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void preencherFormulario(Sessao sessao) {
        codigoField.setText(sessao.getId() != null ? sessao.getId().toString() : "");
        tipoCombo.setSelectedItem(sessao.getTipo());
        dataHoraField.setText(sessao.getDataHora().format(DATE_FORMATTER));
        localField.setText(sessao.getLocal());
        presidenteField.setText(sessao.getPresidente());
        secretarioField.setText(sessao.getSecretario());
        tesoureiroField.setText(sessao.getTesoureiro());
        oradorField.setText(sessao.getOrador());
        temaField.setText(sessao.getTema());
        pautaArea.setText(sessao.getPauta());
        observacoesArea.setText(sessao.getObservacoes());
        presentesField.setText(String.valueOf(sessao.getQuantidadePresentes()));
        visitantesField.setText(String.valueOf(sessao.getQuantidadeVisitantes()));
        statusCombo.setSelectedItem(sessao.getStatus());
    }
    
    private void excluirSessao() {
        if (sessaoAtual != null) {
            int option = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir esta sessão?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    sessaoDAO.delete(sessaoAtual.getId());
                    JOptionPane.showMessageDialog(this, "Sessão excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    limparFormulario();
                    refreshData();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir sessão: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void limparFormulario() {
        sessaoAtual = null;
        codigoField.setText("");
        tipoCombo.setSelectedIndex(0);
        dataHoraField.setText("");
        localField.setText("");
        presidenteField.setText("");
        secretarioField.setText("");
        tesoureiroField.setText("");
        oradorField.setText("");
        temaField.setText("");
        pautaArea.setText("");
        observacoesArea.setText("");
        presentesField.setText("");
        visitantesField.setText("");
        statusCombo.setSelectedIndex(0);
        sessoesTable.clearSelection();
    }
    
    private void pesquisarSessoes() {
        String termo = pesquisarField.getText().trim();
        if (termo.isEmpty()) {
            refreshData();
        } else {
            try {
                List<Sessao> sessoes = sessaoDAO.findAll();
                // Filtrar localmente por tema
                List<Sessao> filtradas = sessoes.stream()
                    .filter(s -> s.getTema() != null && s.getTema().toLowerCase().contains(termo.toLowerCase()))
                    .toList();
                atualizarTabela(filtradas);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao pesquisar sessões: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void atualizarTabela(List<Sessao> sessoes) {
        tableModel.setRowCount(0);
        for (Sessao sessao : sessoes) {
            Object[] row = {
                sessao.getId(),
                sessao.getTipo(),
                sessao.getDataHora().format(DATE_FORMATTER),
                sessao.getLocal(),
                sessao.getPresidente(),
                sessao.getStatus()
            };
            tableModel.addRow(row);
        }
    }
    
    public void refreshData() {
        try {
            List<Sessao> sessoes = sessaoDAO.findAll();
            atualizarTabela(sessoes);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
