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
        // Aplicar layout padrão usando PadraoLayout - 100% CONFORMIDADE
        // Aplicar estilização padrão ao painel principal
        PadraoLayout.estilizarPainelPrincipal(this);
        
        // Header estilizado usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader("📝 Gestão de Sessões", "Cadastro e administração de sessões maçônicas");
        add(headerPanel, BorderLayout.NORTH);
        
        // Painel principal usando PadraoLayout - 100% CONFORMIDADE
        JPanel mainPanel = PadraoLayout.criarPainelPrincipal();
        
        // Painel de busca no topo
        JPanel buscaPanel = PadraoLayout.criarPainelPesquisa(pesquisarField, pesquisarButton);
        mainPanel.add(buscaPanel, BorderLayout.NORTH);
        
        // Painel do conteúdo com split horizontal
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(PadraoLayout.COR_FUNDO);
        
        // Tabela de sessões usando PadraoLayout
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBackground(Color.WHITE);
        
        PadraoLayout.configurarTabela(sessoesTable);
        tabelaPanel.add(new JScrollPane(sessoesTable), BorderLayout.CENTER);
        
        // Painel de formulário usando PadraoLayout
        JPanel formularioPanel = new JPanel(new BorderLayout());
        formularioPanel.setBackground(PadraoLayout.COR_PAINEL);
        formularioPanel.setBorder(PadraoLayout.BORDA_GRUPO);
        
        JLabel formTitle = PadraoLayout.criarLabelFormulario("📝 Dados da Sessão");
        formTitle.setFont(PadraoLayout.FONTE_GRUPO);
        
        // Container principal para todos os grupos
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        
        // Grupo 1: Dados da Sessão usando PadraoLayout com alinhamento correto
        JPanel dadosSessaoPanel = PadraoLayout.criarGrupoFormulario("📅 Dados da Sessão");
        JPanel dadosSessaoContent = new JPanel(PadraoLayout.criarLayoutFormularioAlinhado());
        dadosSessaoContent.setBackground(Color.WHITE);
        
        // Código
        dadosSessaoContent.add(PadraoLayout.criarLabelFormularioCodigo("Código:"), PadraoLayout.criarConstraintsFormulario(0, 0));
        PadraoLayout.estilizarCampoCodigo(codigoField); // Usando método específico
        dadosSessaoContent.add(codigoField, PadraoLayout.criarConstraintsFormulario(0, 1));
        
        // Data e Hora
        dadosSessaoContent.add(PadraoLayout.criarLabelFormulario("Data e Hora:"), PadraoLayout.criarConstraintsFormulario(1, 0));
        PadraoLayout.estilizarCampoDataHora(dataHoraField); // Usando método específico
        dadosSessaoContent.add(dataHoraField, PadraoLayout.criarConstraintsFormulario(1, 1));
        
        // Local (ocupa duas colunas)
        GridBagConstraints localLabelConstraints = PadraoLayout.criarConstraintsFormulario(2, 0);
        localLabelConstraints.gridwidth = 2;
        localLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
        dadosSessaoContent.add(PadraoLayout.criarLabelFormulario("Local:"), localLabelConstraints);
        
        GridBagConstraints localFieldConstraints = PadraoLayout.criarConstraintsFormulario(3, 0);
        localFieldConstraints.gridwidth = 2;
        localFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        PadraoLayout.estilizarCampoLocalSessao(localField); // Usando método específico
        dadosSessaoContent.add(localField, localFieldConstraints);
        
        // Tipo
        dadosSessaoContent.add(PadraoLayout.criarLabelFormulario("Tipo:"), PadraoLayout.criarConstraintsFormulario(4, 0));
        PadraoLayout.estilizarComboBox(tipoCombo);
        dadosSessaoContent.add(tipoCombo, PadraoLayout.criarConstraintsFormulario(4, 1));
        
        dadosSessaoPanel.add(dadosSessaoContent);
        
        // Grupo 2: Diretoria usando PadraoLayout com alinhamento correto
        JPanel diretoriaPanel = PadraoLayout.criarGrupoFormulario("👥 Diretoria da Sessão");
        JPanel diretoriaContent = new JPanel(PadraoLayout.criarLayoutFormularioAlinhado());
        diretoriaContent.setBackground(Color.WHITE);
        
        // Presidente
        diretoriaContent.add(PadraoLayout.criarLabelFormulario("Presidente:"), PadraoLayout.criarConstraintsFormulario(0, 0));
        PadraoLayout.estilizarCampoCargoSessao(presidenteField); // Usando método específico
        diretoriaContent.add(presidenteField, PadraoLayout.criarConstraintsFormulario(0, 1));
        
        // Secretário
        diretoriaContent.add(PadraoLayout.criarLabelFormulario("Secretário:"), PadraoLayout.criarConstraintsFormulario(1, 0));
        PadraoLayout.estilizarCampoCargoSessao(secretarioField); // Usando método específico
        diretoriaContent.add(secretarioField, PadraoLayout.criarConstraintsFormulario(1, 1));
        
        // Tesoureiro
        diretoriaContent.add(PadraoLayout.criarLabelFormulario("Tesoureiro:"), PadraoLayout.criarConstraintsFormulario(2, 0));
        PadraoLayout.estilizarCampoCargoSessao(tesoureiroField); // Usando método específico
        diretoriaContent.add(tesoureiroField, PadraoLayout.criarConstraintsFormulario(2, 1));
        
        // Orador
        diretoriaContent.add(PadraoLayout.criarLabelFormulario("Orador:"), PadraoLayout.criarConstraintsFormulario(3, 0));
        PadraoLayout.estilizarCampoCargoSessao(oradorField); // Usando método específico
        diretoriaContent.add(oradorField, PadraoLayout.criarConstraintsFormulario(3, 1));
        
        diretoriaPanel.add(diretoriaContent);
        
        // Grupo 3: Participantes usando PadraoLayout com alinhamento correto
        JPanel participantesPanel = PadraoLayout.criarGrupoFormulario("👥 Participantes");
        JPanel participantesContent = new JPanel(PadraoLayout.criarLayoutFormularioAlinhado());
        participantesContent.setBackground(Color.WHITE);
        
        // Presentes
        participantesContent.add(PadraoLayout.criarLabelFormulario("Presentes:"), PadraoLayout.criarConstraintsFormulario(0, 0));
        PadraoLayout.estilizarCampoNumeroParticipantes(presentesField); // Usando método específico
        participantesContent.add(presentesField, PadraoLayout.criarConstraintsFormulario(0, 1));
        
        // Visitantes
        participantesContent.add(PadraoLayout.criarLabelFormulario("Visitantes:"), PadraoLayout.criarConstraintsFormulario(1, 0));
        PadraoLayout.estilizarCampoNumeroParticipantes(visitantesField); // Usando método específico
        participantesContent.add(visitantesField, PadraoLayout.criarConstraintsFormulario(1, 1));
        
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
            dadosSessaoPanel, diretoriaPanel, participantesPanel, conteudoPanel
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
        
        // Split vertical: Formulário acima, Tabela abaixo
        JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formularioPanel, tabelaPanel);
        verticalSplitPane.setDividerLocation(350);
        verticalSplitPane.setResizeWeight(0.4);
        
        contentPanel.add(verticalSplitPane, BorderLayout.CENTER);
        
        mainPanel.add(buscaPanel, BorderLayout.NORTH);
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
