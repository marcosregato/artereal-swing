package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.LojaDAO;
import com.artereal.swing.model.Loja;
import com.artereal.swing.ui.layout.PadraoLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Painel de gestão de Lojas - Layout padrão RelatoriosPanel
 */
public class LojasPanel extends JPanel {
    
    private LojaDAO lojaDAO;
    private DefaultTableModel tableModel;
    private JTable lojasTable;
    private Loja lojaAtual;
    private JTextField pesquisarField;
    
    // Formulário
    private JTextField codigoField;
    private JTextField nomeField;
    private JTextField numeroField;
    private JTextField enderecoField;
    private JTextField bairroField;
    private JTextField cidadeField;
    private JTextField estadoField;
    private JTextField cepField;
    private JTextField telefoneField;
    private JTextField emailField;
    private JTextField presidenteField;
    private JTextField secretarioField;
    private JTextField tesoureiroField;
    private JTextField dataFundacaoField;
    private JTextField ritualField;
    private JTextField poderField;
    private JTextArea observacoesArea;
    
    // Botões
    private JButton salvarButton;
    private JButton novoButton;
    private JButton editarButton;
    private JButton excluirButton;
    private JButton limparButton;
    private JButton pesquisarButton;
    
    public LojasPanel() {
        lojaDAO = new LojaDAO();
        initializeComponents();
        setupLayout();
        setupEvents();
        refreshData();
    }
    
    private void initializeComponents() {
        // Tabela
        tableModel = new DefaultTableModel(new Object[]{
            "Código", "Nome", "Número", "Cidade", "Estado", "Telefone"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        lojasTable = new JTable(tableModel);
        
        // Formulário
        codigoField = new JTextField(10);
        nomeField = new JTextField(30);
        numeroField = new JTextField(10);
        enderecoField = new JTextField(40);
        bairroField = new JTextField(25);
        cidadeField = new JTextField(25);
        estadoField = new JTextField(15);
        cepField = new JTextField(15);
        telefoneField = new JTextField(20);
        emailField = new JTextField(30);
        presidenteField = new JTextField(30);
        secretarioField = new JTextField(30);
        tesoureiroField = new JTextField(30);
        dataFundacaoField = new JTextField(15);
        ritualField = new JTextField(20);
        poderField = new JTextField(20);
        observacoesArea = new JTextArea(3, 30);
        
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
        // Aplicar layout padrão usando PadraoLayout - CORREÇÃO EM LOTE
        this.setBorder(PadraoLayout.BORDA_PAINEL);
        this.setBackground(PadraoLayout.COR_FUNDO);
        
        // Header estilizado usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader("🏛️ Gestão de Lojas", "Cadastro e administração de lojas maçônicas");
        add(headerPanel, BorderLayout.NORTH);
        
        // Painel principal usando PadraoLayout - CORREÇÃO EM LOTE
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(PadraoLayout.COR_PAINEL);
        mainPanel.setBorder(PadraoLayout.BORDA_CONTEUDO);
        
        // Painel de filtros/pesquisa usando PadraoLayout - CORREÇÃO EM LOTE
        JPanel filtroPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filtroPanel.setBackground(PadraoLayout.COR_PAINEL);
        filtroPanel.setBorder(PadraoLayout.BORDA_GRUPO);
        
        JLabel pesquisaLabel = PadraoLayout.criarLabelFormulario("🔍 Pesquisar:");
        
        pesquisarField.setPreferredSize(new Dimension(200, 30));
        PadraoLayout.estilizarCampoTexto(pesquisarField);
        
        // Botão de pesquisa usando PadraoLayout - CORREÇÃO EM LOTE
        pesquisarButton = PadraoLayout.criarBotaoPesquisar();
        pesquisarButton.setFocusPainted(false);
        pesquisarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        filtroPanel.add(pesquisaLabel);
        filtroPanel.add(pesquisarField);
        filtroPanel.add(pesquisarButton);
        
        // Painel de conteúdo com tabela e formulário usando PadraoLayout - CORREÇÃO EM LOTE
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(PadraoLayout.COR_PAINEL);
        contentPanel.setBorder(PadraoLayout.BORDA_CONTEUDO);
        // Borda já aplicada acima com PadraoLayout.BORDA_CONTEUDO
        
        // Tabela de lojas
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBackground(Color.WHITE);
        
        // Tabela estilizada usando PadraoLayout
        PadraoLayout.configurarTabela(lojasTable);
        
        tabelaPanel.add(new JScrollPane(lojasTable), BorderLayout.CENTER);
        
        // Painel de formulário usando PadraoLayout
        JPanel formularioPanel = new JPanel(new BorderLayout());
        formularioPanel.setBackground(Color.WHITE);
        formularioPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        JLabel formTitle = new JLabel("📝 Dados da Loja");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formTitle.setForeground(new Color(70, 130, 180));
        
        // Container principal para todos os grupos
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        
        // Grupo 1: Dados Básicos
        JPanel dadosBasicosPanel = createFormGroup("📅 Dados Básicos");
        JPanel dadosBasicosContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        dadosBasicosContent.setBackground(Color.WHITE);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Código:"));
        PadraoLayout.estilizarCampoTexto(codigoField);
        dadosBasicosContent.add(codigoField);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Nome:"));
        PadraoLayout.estilizarCampoTexto(nomeField);
        dadosBasicosContent.add(nomeField);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Número:"));
        PadraoLayout.estilizarCampoTexto(numeroField);
        dadosBasicosContent.add(numeroField);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Data Fundação:"));
        PadraoLayout.estilizarCampoTexto(dataFundacaoField);
        dadosBasicosContent.add(dataFundacaoField);
        
        dadosBasicosPanel.add(dadosBasicosContent);
        
        // Grupo 2: Informações Maçônicas
        JPanel infoMasonicasPanel = createFormGroup("🔱 Informações Maçônicas");
        JPanel infoMasonicasContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        infoMasonicasContent.setBackground(Color.WHITE);
        
        infoMasonicasContent.add(PadraoLayout.criarLabelFormulario("Ritual:"));
        PadraoLayout.estilizarCampoTexto(ritualField);
        infoMasonicasContent.add(ritualField);
        
        infoMasonicasContent.add(PadraoLayout.criarLabelFormulario("Potência:"));
        PadraoLayout.estilizarCampoTexto(poderField);
        infoMasonicasContent.add(poderField);
        
        infoMasonicasPanel.add(infoMasonicasContent);
        
        // Grupo 3: Endereço
        JPanel enderecoPanel = createFormGroup("📍 Endereço");
        JPanel enderecoContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        enderecoContent.setBackground(Color.WHITE);
        
        enderecoContent.add(PadraoLayout.criarLabelFormulario("Endereço:"));
        PadraoLayout.estilizarCampoTexto(enderecoField);
        enderecoContent.add(enderecoField);
        
        enderecoContent.add(PadraoLayout.criarLabelFormulario("Bairro:"));
        PadraoLayout.estilizarCampoTexto(bairroField);
        enderecoContent.add(bairroField);
        
        enderecoContent.add(PadraoLayout.criarLabelFormulario("Cidade:"));
        PadraoLayout.estilizarCampoTexto(cidadeField);
        enderecoContent.add(cidadeField);
        
        enderecoContent.add(PadraoLayout.criarLabelFormulario("Estado:"));
        PadraoLayout.estilizarCampoTexto(estadoField);
        enderecoContent.add(estadoField);
        
        enderecoContent.add(PadraoLayout.criarLabelFormulario("CEP:"));
        PadraoLayout.estilizarCampoTexto(cepField);
        enderecoContent.add(cepField);
        
        enderecoPanel.add(enderecoContent);
        
        // Grupo 4: Contato
        JPanel contatoPanel = createFormGroup("📞 Contato");
        JPanel contatoContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        contatoContent.setBackground(Color.WHITE);
        
        contatoContent.add(PadraoLayout.criarLabelFormulario("Telefone:"));
        PadraoLayout.estilizarCampoTexto(telefoneField);
        contatoContent.add(telefoneField);
        
        contatoContent.add(PadraoLayout.criarLabelFormulario("Email:"));
        PadraoLayout.estilizarCampoTexto(emailField);
        contatoContent.add(emailField);
        
        contatoPanel.add(contatoContent);
        
        // Grupo 5: Diretoria
        JPanel diretoriaPanel = createFormGroup("👥 Diretoria");
        JPanel diretoriaContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        diretoriaContent.setBackground(Color.WHITE);
        
        diretoriaContent.add(PadraoLayout.criarLabelFormulario("Presidente:"));
        PadraoLayout.estilizarCampoTexto(presidenteField);
        diretoriaContent.add(presidenteField);
        
        diretoriaContent.add(PadraoLayout.criarLabelFormulario("Secretário:"));
        PadraoLayout.estilizarCampoTexto(secretarioField);
        diretoriaContent.add(secretarioField);
        
        diretoriaContent.add(PadraoLayout.criarLabelFormulario("Tesoureiro:"));
        PadraoLayout.estilizarCampoTexto(tesoureiroField);
        diretoriaContent.add(tesoureiroField);
        
        diretoriaPanel.add(diretoriaContent);
        
        // Grupo 6: Conteúdo da Loja
        JPanel conteudoPanel = createFormGroup("📝 Conteúdo da Loja");
        JPanel conteudoContent = new JPanel(new BorderLayout());
        conteudoContent.setBackground(Color.WHITE);
        
        // Painel de TextAreas usando PadraoLayout
        JPanel obsPanel = PadraoLayout.criarPainelTextArea("📝 Observações:", observacoesArea);
        
        JPanel textAreasPanel = new JPanel(new BorderLayout());
        textAreasPanel.setBackground(Color.WHITE);
        textAreasPanel.add(obsPanel, BorderLayout.CENTER);
        conteudoContent.add(textAreasPanel, BorderLayout.CENTER);
        conteudoPanel.add(conteudoContent);
        
        // Organizar grupos verticalmente usando PadraoLayout
        JPanel allGroups = PadraoLayout.criarFormularioMultiplosGrupos(
            dadosBasicosPanel, infoMasonicasPanel, enderecoPanel, 
            contatoPanel, diretoriaPanel, conteudoPanel
        );
        
        // Adicionar scroll ao formulário usando PadraoLayout
        JScrollPane formScroll = PadraoLayout.criarFormularioComScroll(allGroups);
        
        formContainer.add(formScroll, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botoesPanel.setBackground(Color.WHITE);
        
        botoesPanel.add(createStyledButton("Salvar", new Color(144, 238, 144)));
        botoesPanel.add(createStyledButton("Novo", new Color(173, 216, 230)));
        botoesPanel.add(createStyledButton("Editar", new Color(255, 250, 205)));
        botoesPanel.add(createStyledButton("Excluir", new Color(255, 182, 193)));
        botoesPanel.add(createStyledButton("Limpar", new Color(240, 240, 240)));
        
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
        salvarButton.addActionListener(e -> salvarLoja());
        novoButton.addActionListener(e -> limparFormulario());
        editarButton.addActionListener(e -> carregarLojaSelecionada());
        excluirButton.addActionListener(e -> excluirLoja());
        limparButton.addActionListener(e -> limparFormulario());
        pesquisarButton.addActionListener(e -> pesquisarLojas());
        
        lojasTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarLojaSelecionada();
            }
        });
    }
    
    private void salvarLoja() {
        try {
            Loja loja = new Loja();
            loja.setNome(nomeField.getText());
            loja.setNumero(numeroField.getText());
            loja.setEndereco(enderecoField.getText());
            loja.setBairro(bairroField.getText());
            loja.setCidade(cidadeField.getText());
            loja.setEstado(estadoField.getText());
            loja.setCep(cepField.getText());
            loja.setTelefone(telefoneField.getText());
            loja.setEmail(emailField.getText());
            loja.setPresidente(presidenteField.getText());
            loja.setSecretario(secretarioField.getText());
            loja.setTesoureiro(tesoureiroField.getText());
            loja.setDataFundacao(dataFundacaoField.getText());
            loja.setRito(ritualField.getText());
            loja.setPotencia(poderField.getText());
            loja.setObservacoes(observacoesArea.getText());
            
            if (lojaAtual == null) {
                lojaDAO.save(loja);
            } else {
                loja.setId(lojaAtual.getId());
                lojaDAO.save(loja);
            }
            
            JOptionPane.showMessageDialog(this, "Loja salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            refreshData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar loja: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void carregarLojaSelecionada() {
        int selectedRow = lojasTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                Long id = (Long) tableModel.getValueAt(selectedRow, 0);
                lojaAtual = lojaDAO.findById(id);
                if (lojaAtual != null) {
                    preencherFormulario(lojaAtual);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar loja: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void preencherFormulario(Loja loja) {
        codigoField.setText(loja.getId() != null ? loja.getId().toString() : "");
        nomeField.setText(loja.getNome());
        numeroField.setText(loja.getNumero());
        enderecoField.setText(loja.getEndereco());
        bairroField.setText(loja.getBairro());
        cidadeField.setText(loja.getCidade());
        estadoField.setText(loja.getEstado());
        cepField.setText(loja.getCep());
        telefoneField.setText(loja.getTelefone());
        emailField.setText(loja.getEmail());
        presidenteField.setText(loja.getPresidente());
        secretarioField.setText(loja.getSecretario());
        tesoureiroField.setText(loja.getTesoureiro());
        dataFundacaoField.setText(loja.getDataFundacao());
        ritualField.setText(loja.getRito());
        poderField.setText(loja.getPotencia());
        observacoesArea.setText(loja.getObservacoes());
    }
    
    private void excluirLoja() {
        if (lojaAtual != null) {
            int option = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir esta loja?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    lojaDAO.delete(lojaAtual.getId());
                    JOptionPane.showMessageDialog(this, "Loja excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    limparFormulario();
                    refreshData();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir loja: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void limparFormulario() {
        lojaAtual = null;
        codigoField.setText("");
        nomeField.setText("");
        numeroField.setText("");
        enderecoField.setText("");
        bairroField.setText("");
        cidadeField.setText("");
        estadoField.setText("");
        cepField.setText("");
        telefoneField.setText("");
        emailField.setText("");
        presidenteField.setText("");
        secretarioField.setText("");
        tesoureiroField.setText("");
        dataFundacaoField.setText("");
        ritualField.setText("");
        poderField.setText("");
        observacoesArea.setText("");
        lojasTable.clearSelection();
    }
    
    private void pesquisarLojas() {
        String termo = pesquisarField.getText().trim();
        if (termo.isEmpty()) {
            refreshData();
        } else {
            try {
                List<Loja> lojas = lojaDAO.findByNome(termo);
                atualizarTabela(lojas);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao pesquisar lojas: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void atualizarTabela(List<Loja> lojas) {
        tableModel.setRowCount(0);
        for (Loja loja : lojas) {
            Object[] row = {
                loja.getId(),
                loja.getNome(),
                loja.getNumero(),
                loja.getCidade(),
                loja.getEstado(),
                loja.getTelefone()
            };
            tableModel.addRow(row);
        }
    }
    
    public void refreshData() {
        try {
            List<Loja> lojas = lojaDAO.findAll();
            atualizarTabela(lojas);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
