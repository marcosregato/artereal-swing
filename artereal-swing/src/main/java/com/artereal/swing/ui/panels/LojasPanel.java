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
 * Painel de gestão de Lojas - Layout padrão Header → Busca → Formulário → Tabela
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
    private JTextField dataFundacaoField;
    private JTextField ritualField;
    private JTextField poderField;
    private JTextField presidenteField;
    private JTextField secretarioField;
    private JTextField tesoureiroField;
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
            "Código", "Nome", "Número", "Telefone", "Cidade", "Estado"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        lojasTable = new JTable(tableModel);
        
        // Formulário
        codigoField = new JTextField();
        nomeField = new JTextField();
        numeroField = new JTextField(15);
        enderecoField = new JTextField();
        bairroField = new JTextField();
        cidadeField = new JTextField();
        estadoField = new JTextField();
        cepField = new JTextField();
        telefoneField = new JTextField();
        emailField = new JTextField();
        dataFundacaoField = new JTextField(15);
        ritualField = new JTextField(20);
        poderField = new JTextField(25);
        presidenteField = new JTextField(40);
        secretarioField = new JTextField(40);
        tesoureiroField = new JTextField(40);
        observacoesArea = new JTextArea(3, 40);
        
        // Botões
        salvarButton = PadraoLayout.criarBotaoSalvar();
        novoButton = PadraoLayout.criarBotaoNovo();
        editarButton = PadraoLayout.criarBotaoEditar();
        excluirButton = PadraoLayout.criarBotaoExcluir();
        limparButton = PadraoLayout.criarBotaoLimpar();
        pesquisarButton = PadraoLayout.criarBotaoPesquisar();
        
        // Aplicar cores pastéis nos botões usando PadraoLayout
        PadraoLayout.aplicarCoresPastelBotoesPrincipais(salvarButton, novoButton, editarButton, excluirButton, limparButton);
        
        pesquisarField = new JTextField(20);
    }
    
    private void setupLayout() {
        // Aplicar estilização padrão ao painel principal
        PadraoLayout.estilizarPainelPrincipal(this);
        
        // Header estilizado usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader("🏛️ Gestão de Lojas", "Cadastro e administração de lojas maçônicas");
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
        JPanel formPanel = PadraoLayout.criarGrupoFormulario("📝 Dados da Loja");
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        
        // Formulário usando BoxLayout vertical para organizar JPanel um de baixo do outro
        JPanel formContent = new JPanel();
        formContent.setLayout(new BoxLayout(formContent, BoxLayout.Y_AXIS));
        formContent.setBackground(Color.WHITE);
        
        // Adicionar barra de rolagem ao formulário
        JScrollPane formScroll = new JScrollPane(formContent);
        formScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        formScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        formScroll.setBorder(BorderFactory.createEmptyBorder());
        formScroll.getVerticalScrollBar().setUnitIncrement(16);
        formScroll.getVerticalScrollBar().setBlockIncrement(64);
        
        // SEÇÃO 1: Dados Básicos da Loja
        JPanel dadosBasicosPanel = new JPanel(new BorderLayout());
        dadosBasicosPanel.setBackground(Color.WHITE);
        dadosBasicosPanel.setBorder(BorderFactory.createTitledBorder("🏛️ Dados Básicos da Loja"));
        
        JPanel dadosBasicosContent = new JPanel();
        dadosBasicosContent.setLayout(new BoxLayout(dadosBasicosContent, BoxLayout.Y_AXIS));
        dadosBasicosContent.setBackground(Color.WHITE);
        
        // Primeira linha: Código e Nome
        JPanel primeiraLinhaLojaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        primeiraLinhaLojaPanel.setBackground(Color.WHITE);
        
        // Campo Código
        JPanel codigoLojaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        codigoLojaPanel.setBackground(Color.WHITE);
        codigoLojaPanel.add(PadraoLayout.criarLabelFormularioCodigo("Código:"));
        PadraoLayout.estilizarCampoCodigo(codigoField);
        codigoLojaPanel.add(codigoField);
        
        // Campo Nome
        JPanel nomeLojaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        nomeLojaPanel.setBackground(Color.WHITE);
        nomeLojaPanel.add(PadraoLayout.criarLabelFormulario("Nome:"));
        PadraoLayout.estilizarCampoNome(nomeField);
        nomeLojaPanel.add(nomeField);
        
        // Adicionar os painéis à primeira linha
        primeiraLinhaLojaPanel.add(codigoLojaPanel);
        primeiraLinhaLojaPanel.add(Box.createHorizontalStrut(20)); // Espaço entre os campos
        primeiraLinhaLojaPanel.add(nomeLojaPanel);
        
        // Adicionar o painel da primeira linha ao conteúdo
        dadosBasicosContent.add(primeiraLinhaLojaPanel);
        dadosBasicosContent.add(Box.createVerticalStrut(5)); // Espaço vertical
        
        // Segunda linha: Número e Data Fundação
        JPanel segundaLinhaLojaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        segundaLinhaLojaPanel.setBackground(Color.WHITE);
        
        // Campo Número
        JPanel numeroLojaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        numeroLojaPanel.setBackground(Color.WHITE);
        numeroLojaPanel.add(PadraoLayout.criarLabelFormulario("Número:"));
        PadraoLayout.estilizarCampoNumeroLoja(numeroField);
        numeroLojaPanel.add(numeroField);
        
        // Campo Data Fundação
        JPanel dataFundacaoLojaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        dataFundacaoLojaPanel.setBackground(Color.WHITE);
        dataFundacaoLojaPanel.add(PadraoLayout.criarLabelFormulario("Data Fundação:"));
        PadraoLayout.estilizarCampoData(dataFundacaoField);
        dataFundacaoLojaPanel.add(dataFundacaoField);
        
        // Adicionar os painéis à segunda linha
        segundaLinhaLojaPanel.add(numeroLojaPanel);
        segundaLinhaLojaPanel.add(Box.createHorizontalStrut(20)); // Espaço entre os campos
        segundaLinhaLojaPanel.add(dataFundacaoLojaPanel);
        
        // Adicionar o painel da segunda linha ao conteúdo
        dadosBasicosContent.add(segundaLinhaLojaPanel);
        
        dadosBasicosPanel.add(dadosBasicosContent, BorderLayout.CENTER);
        formContent.add(dadosBasicosPanel);
        
        // SEÇÃO 2: Informações Maçônicas
        JPanel masonicosPanel = new JPanel(new BorderLayout());
        masonicosPanel.setBackground(Color.WHITE);
        masonicosPanel.setBorder(BorderFactory.createTitledBorder("🔷 Informações Maçônicas"));
        
        JPanel masonicosContent = new JPanel();
        masonicosContent.setLayout(new BoxLayout(masonicosContent, BoxLayout.Y_AXIS));
        masonicosContent.setBackground(Color.WHITE);
        
        // Primeira linha: Ritual e Potência
        JPanel primeiraLinhaMasonicosLojaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        primeiraLinhaMasonicosLojaPanel.setBackground(Color.WHITE);
        
        // Campo Ritual
        JPanel ritualLojaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        ritualLojaPanel.setBackground(Color.WHITE);
        ritualLojaPanel.add(PadraoLayout.criarLabelFormulario("Ritual:"));
        PadraoLayout.estilizarCampoRitalMasonico(ritualField);
        ritualLojaPanel.add(ritualField);
        
        // Campo Potência
        JPanel potenciaLojaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        potenciaLojaPanel.setBackground(Color.WHITE);
        potenciaLojaPanel.add(PadraoLayout.criarLabelFormulario("Potência:"));
        PadraoLayout.estilizarCampoPotenciaMasonica(poderField);
        potenciaLojaPanel.add(poderField);
        
        // Adicionar os painéis à primeira linha
        primeiraLinhaMasonicosLojaPanel.add(ritualLojaPanel);
        primeiraLinhaMasonicosLojaPanel.add(Box.createHorizontalStrut(20)); // Espaço entre os campos
        primeiraLinhaMasonicosLojaPanel.add(potenciaLojaPanel);
        
        // Adicionar o painel da primeira linha ao conteúdo
        masonicosContent.add(primeiraLinhaMasonicosLojaPanel);
        
        masonicosPanel.add(masonicosContent, BorderLayout.CENTER);
        formContent.add(masonicosPanel);
        
        // SEÇÃO 3: Endereço
        JPanel enderecoPanel = new JPanel(new BorderLayout());
        enderecoPanel.setBackground(Color.WHITE);
        enderecoPanel.setBorder(BorderFactory.createTitledBorder("📍 Endereço da Loja"));
        
        JPanel enderecoContent = new JPanel();
        enderecoContent.setLayout(new BoxLayout(enderecoContent, BoxLayout.Y_AXIS));
        enderecoContent.setBackground(Color.WHITE);
        
        // Primeira linha: Endereço (ocupa linha inteira)
        JPanel enderecoLojaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        enderecoLojaPanel.setBackground(Color.WHITE);
        enderecoLojaPanel.add(PadraoLayout.criarLabelFormulario("Endereço:"));
        PadraoLayout.estilizarCampoEndereco(enderecoField);
        enderecoLojaPanel.add(enderecoField);
        
        // Adicionar o painel do Endereço ao conteúdo
        enderecoContent.add(enderecoLojaPanel);
        enderecoContent.add(Box.createVerticalStrut(5)); // Espaço vertical
        
        // Segunda linha: Bairro e Cidade
        JPanel segundaLinhaEnderecoLojaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        segundaLinhaEnderecoLojaPanel.setBackground(Color.WHITE);
        
        // Campo Bairro
        JPanel bairroLojaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        bairroLojaPanel.setBackground(Color.WHITE);
        bairroLojaPanel.add(PadraoLayout.criarLabelFormulario("Bairro:"));
        PadraoLayout.estilizarCampoBairro(bairroField);
        bairroLojaPanel.add(bairroField);
        
        // Campo Cidade
        JPanel cidadeLojaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        cidadeLojaPanel.setBackground(Color.WHITE);
        cidadeLojaPanel.add(PadraoLayout.criarLabelFormulario("Cidade:"));
        PadraoLayout.estilizarCampoCidade(cidadeField);
        cidadeLojaPanel.add(cidadeField);
        
        // Adicionar os painéis à segunda linha
        segundaLinhaEnderecoLojaPanel.add(bairroLojaPanel);
        segundaLinhaEnderecoLojaPanel.add(Box.createHorizontalStrut(20)); // Espaço entre os campos
        segundaLinhaEnderecoLojaPanel.add(cidadeLojaPanel);
        
        // Adicionar o painel da segunda linha ao conteúdo
        enderecoContent.add(segundaLinhaEnderecoLojaPanel);
        enderecoContent.add(Box.createVerticalStrut(5)); // Espaço vertical
        
        // Terceira linha: Estado e CEP
        JPanel terceiraLinhaEnderecoLojaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        terceiraLinhaEnderecoLojaPanel.setBackground(Color.WHITE);
        
        // Campo Estado
        JPanel estadoLojaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        estadoLojaPanel.setBackground(Color.WHITE);
        estadoLojaPanel.add(PadraoLayout.criarLabelFormulario("Estado:"));
        PadraoLayout.estilizarCampoEstado(estadoField);
        estadoLojaPanel.add(estadoField);
        
        // Campo CEP
        JPanel cepLojaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        cepLojaPanel.setBackground(Color.WHITE);
        cepLojaPanel.add(PadraoLayout.criarLabelFormulario("CEP:"));
        PadraoLayout.estilizarCampoCEP(cepField);
        cepLojaPanel.add(cepField);
        
        // Adicionar os painéis à terceira linha
        terceiraLinhaEnderecoLojaPanel.add(estadoLojaPanel);
        terceiraLinhaEnderecoLojaPanel.add(Box.createHorizontalStrut(20)); // Espaço entre os campos
        terceiraLinhaEnderecoLojaPanel.add(cepLojaPanel);
        
        // Adicionar o painel da terceira linha ao conteúdo
        enderecoContent.add(terceiraLinhaEnderecoLojaPanel);
        
        enderecoPanel.add(enderecoContent, BorderLayout.CENTER);
        formContent.add(enderecoPanel);
        
        // SEÇÃO 4: Contato
        JPanel contatoPanel = new JPanel(new BorderLayout());
        contatoPanel.setBackground(Color.WHITE);
        contatoPanel.setBorder(BorderFactory.createTitledBorder("📞 Contato"));
        
        JPanel contatoContent = new JPanel();
        contatoContent.setLayout(new BoxLayout(contatoContent, BoxLayout.Y_AXIS));
        contatoContent.setBackground(Color.WHITE);
        
        // Primeira linha: Telefone e E-mail
        JPanel primeiraLinhaContatoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        primeiraLinhaContatoPanel.setBackground(Color.WHITE);
        
        // Campo Telefone
        JPanel telefonePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        telefonePanel.setBackground(Color.WHITE);
        telefonePanel.add(PadraoLayout.criarLabelFormulario("Telefone:"));
        PadraoLayout.estilizarCampoTelefone(telefoneField);
        telefonePanel.add(telefoneField);
        
        // Campo E-mail
        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        emailPanel.setBackground(Color.WHITE);
        emailPanel.add(PadraoLayout.criarLabelFormulario("E-mail:"));
        PadraoLayout.estilizarCampoEmail(emailField);
        emailPanel.add(emailField);
        
        // Adicionar os painéis à primeira linha
        primeiraLinhaContatoPanel.add(telefonePanel);
        primeiraLinhaContatoPanel.add(Box.createHorizontalStrut(20)); // Espaço entre os campos
        primeiraLinhaContatoPanel.add(emailPanel);
        
        // Adicionar o painel da primeira linha ao conteúdo
        contatoContent.add(primeiraLinhaContatoPanel);
        
        contatoPanel.add(contatoContent, BorderLayout.CENTER);
        formContent.add(contatoPanel);
        
        // SEÇÃO 5: Diretoria
        JPanel diretoriaPanel = new JPanel(new BorderLayout());
        diretoriaPanel.setBackground(Color.WHITE);
        diretoriaPanel.setBorder(BorderFactory.createTitledBorder("👥 Diretoria Atual"));
        
        JPanel diretoriaContent = new JPanel();
        diretoriaContent.setLayout(new BoxLayout(diretoriaContent, BoxLayout.Y_AXIS));
        diretoriaContent.setBackground(Color.WHITE);
        
        // Primeira linha: Presidente (ocupa linha inteira)
        JPanel presidentePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        presidentePanel.setBackground(Color.WHITE);
        presidentePanel.add(PadraoLayout.criarLabelFormulario("Presidente:"));
        PadraoLayout.estilizarCampoCargoDiretoria(presidenteField);
        presidentePanel.add(presidenteField);
        
        // Adicionar o painel do Presidente ao conteúdo
        diretoriaContent.add(presidentePanel);
        diretoriaContent.add(Box.createVerticalStrut(5)); // Espaço vertical
        
        // Segunda linha: Secretário e Tesoureiro
        JPanel segundaLinhaDiretoriaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        segundaLinhaDiretoriaPanel.setBackground(Color.WHITE);
        
        // Campo Secretário
        JPanel secretarioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        secretarioPanel.setBackground(Color.WHITE);
        secretarioPanel.add(PadraoLayout.criarLabelFormulario("Secretário:"));
        PadraoLayout.estilizarCampoCargoDiretoria(secretarioField);
        secretarioPanel.add(secretarioField);
        
        // Campo Tesoureiro
        JPanel tesoureiroPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tesoureiroPanel.setBackground(Color.WHITE);
        tesoureiroPanel.add(PadraoLayout.criarLabelFormulario("Tesoureiro:"));
        PadraoLayout.estilizarCampoCargoDiretoria(tesoureiroField);
        tesoureiroPanel.add(tesoureiroField);
        
        // Adicionar os painéis à segunda linha
        segundaLinhaDiretoriaPanel.add(secretarioPanel);
        segundaLinhaDiretoriaPanel.add(Box.createHorizontalStrut(20)); // Espaço entre os campos
        segundaLinhaDiretoriaPanel.add(tesoureiroPanel);
        
        // Adicionar o painel da segunda linha ao conteúdo
        diretoriaContent.add(segundaLinhaDiretoriaPanel);
        
        diretoriaPanel.add(diretoriaContent, BorderLayout.CENTER);
        formContent.add(diretoriaPanel);
        
        // SEÇÃO 6: Observações
        JPanel observacoesPanel = new JPanel(new BorderLayout());
        observacoesPanel.setBackground(Color.WHITE);
        observacoesPanel.setBorder(BorderFactory.createTitledBorder("📝 Observações"));
        
        observacoesPanel.add(observacoesArea, BorderLayout.CENTER);
        formContent.add(observacoesPanel);
        
        // Painel de botões
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botoesPanel.setBackground(Color.WHITE);
        botoesPanel.add(salvarButton);
        botoesPanel.add(novoButton);
        botoesPanel.add(editarButton);
        botoesPanel.add(excluirButton);
        botoesPanel.add(limparButton);
        
        formContainer.add(formScroll, BorderLayout.CENTER);
        formContainer.add(botoesPanel, BorderLayout.SOUTH);
        
        formPanel.add(formContainer, BorderLayout.CENTER);
        
        // Painel de tabela usando PadraoLayout
        JPanel tabelaPanel = PadraoLayout.criarGrupoFormulario("🏛️ Lojas Cadastradas");
        PadraoLayout.configurarTabela(lojasTable);
        JScrollPane tableScrollPane = new JScrollPane(lojasTable);
        
        tabelaPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Split vertical: Formulário acima (60%), Tabela abaixo (40%)
        JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formPanel, tabelaPanel);
        verticalSplitPane.setDividerLocation(400);
        verticalSplitPane.setResizeWeight(0.6);
        
        contentPanel.add(verticalSplitPane, BorderLayout.CENTER);
        
        // Adicionar contentPanel ao mainPanel
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
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
            
            if (lojaAtual == null) {
                lojaDAO.save(loja);
            } else {
                loja.setId(lojaAtual.getId());
                lojaDAO.save(loja);
            }
            
            JOptionPane.showMessageDialog(this, "Loja salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            refreshData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar loja: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
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
        dataFundacaoField.setText("");
        ritualField.setText("");
        poderField.setText("");
        presidenteField.setText("");
        secretarioField.setText("");
        tesoureiroField.setText("");
        observacoesArea.setText("");
        lojasTable.clearSelection();
    }
    
    private void carregarLojaSelecionada() {
        int selectedRow = lojasTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                int id = (Integer) tableModel.getValueAt(selectedRow, 0);
                Loja loja = lojaDAO.findById((long) id);
                if (loja != null) {
                    lojaAtual = loja;
                    codigoField.setText(String.valueOf(loja.getId()));
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
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar loja: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
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
                JOptionPane.showMessageDialog(this, "Erro ao pesquisar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
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
    
    private void atualizarTabela(List<Loja> lojas) {
        tableModel.setRowCount(0);
        for (Loja loja : lojas) {
            Object[] row = {
                loja.getId(),
                loja.getNome(),
                loja.getNumero(),
                loja.getTelefone(),
                loja.getCidade(),
                loja.getEstado()
            };
            tableModel.addRow(row);
        }
    }
}
