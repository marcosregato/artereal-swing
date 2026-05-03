package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.DocumentoDAO;
import com.artereal.swing.database.DatabaseManager;
import com.artereal.swing.model.Documento;
import com.artereal.swing.ui.layout.PadraoLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Painel de Gestão Documental
 */
public class DocumentosPanel extends JPanel {
    
    private DocumentoDAO documentoDAO;
    private DefaultTableModel tableModel;
    private JTable documentosTable;
    private JTextField nomeArquivoField;
    private JTextField caminhoArquivoField;
    private JTextField descricaoField;
    private JTextField dataExpiracaoField;
    private JTextField usuarioUploadField;
    private JTextField tamanhoField;
    private JTextField formatoField;
    private JTextField pesquisarField;
    private JButton pesquisarButton;
    private JComboBox<String> tipoComboBox;
    private JTextArea hashArea;
    private JButton salvarButton;
    private JButton novoButton;
    private JButton excluirButton;
    private JButton selecionarArquivoButton;
    private JButton visualizarButton;
    private JButton uploadButton;
    private JButton relatorioButton;
    private JButton emailButton;
    private JButton agendarButton;
    private JButton whatsappButton;
    private Documento documentoAtual;
    
    public DocumentosPanel() {
        // DocumentoDAO já usa DatabaseManager.getInstance() internamente
        documentoDAO = new DocumentoDAO();
        initializeComponents();
        setupLayout();
        setupEvents();
        refreshData();
    }
    
    private void initializeComponents() {
        // Tabela
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome Arquivo", "Tipo", "Tamanho", "Upload", "Expiração", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        documentosTable = new JTable(tableModel);
        
        // Formulário
        nomeArquivoField = new JTextField(40);
        caminhoArquivoField = new JTextField(50);
        descricaoField = new JTextField(40);
        dataExpiracaoField = new JTextField(8);
        usuarioUploadField = new JTextField(20);
        tamanhoField = new JTextField(10);
        tamanhoField.setEditable(false);
        formatoField = new JTextField(6);
        formatoField.setEditable(false);
        tipoComboBox = new JComboBox<>(new String[]{"IDENTIDADE", "DIPLOMA", "CERTIFICADO", "FOTO", "OUTRO"});
        hashArea = new JTextArea(2, 40);
        hashArea.setEditable(false);
        hashArea.setFont(new Font("Monospaced", Font.PLAIN, 10));
        
        // Pesquisa
        pesquisarField = new JTextField(20);
        pesquisarButton = PadraoLayout.criarBotaoPesquisar();
        
        // Botões usando PadraoLayout com cores pastéis
        salvarButton = PadraoLayout.criarBotaoSalvar();
        novoButton = PadraoLayout.criarBotaoNovo();
        excluirButton = PadraoLayout.criarBotaoExcluir();
        selecionarArquivoButton = PadraoLayout.criarBotao("Selecionar Arquivo", new Color(173, 216, 230)); // Azul pastel
        visualizarButton = PadraoLayout.criarBotao("Visualizar", new Color(255, 250, 205)); // Amarelo pastel
        uploadButton = PadraoLayout.criarBotao("Upload", new Color(221, 160, 221)); // Lavanda pastel
        relatorioButton = PadraoLayout.criarBotao("Relatório", new Color(200, 200, 255)); // Azul claro
        emailButton = PadraoLayout.criarBotao("Enviar por Email", new Color(255, 182, 193)); // Rosa pastel
        agendarButton = PadraoLayout.criarBotao("Agendar", new Color(255, 218, 185)); // Pêssego pastel
        whatsappButton = PadraoLayout.criarBotao("📱 WhatsApp", new Color(129, 230, 217)); // Verde WhatsApp
        
        // Aplicar cores pastéis nos botões usando PadraoLayout
        PadraoLayout.aplicarCoresPastelBotoesUsuarios(salvarButton, novoButton, excluirButton); // Usa método de usuários (3 botões)
        PadraoLayout.aplicarCoresPastelBotao(selecionarArquivoButton, "novo"); // Usa cor de novo para selecionar
        PadraoLayout.aplicarCoresPastelBotao(visualizarButton, "limpar"); // Usa cor de limpar para visualizar
        PadraoLayout.aplicarCoresPastelBotao(uploadButton, "salvar"); // Usa cor de salvar para upload
        PadraoLayout.aplicarCoresPastelBotao(uploadButton, "editar"); // Usa cor de editar para upload
        PadraoLayout.aplicarCoresPastelBotao(relatorioButton, "novo"); // Usa cor de novo para relatório
        PadraoLayout.aplicarCoresPastelBotao(emailButton, "editar"); // Usa cor de editar para email
        PadraoLayout.aplicarCoresPastelBotao(agendarButton, "salvar"); // Usa cor de salvar para agendar
        PadraoLayout.aplicarCoresPastelBotao(whatsappButton, "novo"); // Usa cor de novo para WhatsApp
        
        documentoAtual = null;
    }
    
    private void setupLayout() {
        // Aplicar layout padrão usando PadraoLayout
        // Aplicar estilização padrão ao painel principal
        PadraoLayout.estilizarPainelPrincipal(this);
        
        // Header estilizado usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader("📄 Gestão Documental", "Armazenamento e assinatura digital de documentos");
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
        JPanel formPanel = PadraoLayout.criarGrupoFormulario("📝 Dados do Documento");
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        
        // Painel direito - Formulário (padrão SessoesPanel)
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        
        // Painel de estatísticas usando PadraoLayout - CORREÇÃO EM LOTE
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        statsPanel.setBackground(PadraoLayout.COR_PAINEL);
        statsPanel.setBorder(PadraoLayout.BORDA_GRUPO);
        
        try {
            int total = documentoDAO.count();
            int ativos = documentoDAO.countAtivos();
            int expirados = documentoDAO.countExpirados();
            int proximosExpiracao = documentoDAO.countProximosExpiracao();
            
            JPanel[] statPanels = {
                createStatPanel("Total", String.valueOf(total), Color.BLUE),
                createStatPanel("Ativos", String.valueOf(ativos), Color.GREEN),
                createStatPanel("Expirados", String.valueOf(expirados), Color.RED),
                createStatPanel("Próximos à Expiração", String.valueOf(proximosExpiracao), Color.ORANGE)
            };
            
            for (JPanel panel : statPanels) {
                statsPanel.add(panel);
            }
        } catch (SQLException e) {
            statsPanel.add(new JLabel("Erro ao carregar estatísticas"));
        }
        
        // Painel de alertas
        JPanel alertasPanel = new JPanel(new BorderLayout());
        alertasPanel.setBorder(BorderFactory.createTitledBorder("Alertas de Documentos"));
        
        try {
            List<Documento> expirados = documentoDAO.findExpirados();
            List<Documento> proximos = documentoDAO.findProximosExpiracao(30);
            
            JPanel alertasContent = new JPanel(new GridLayout(2, 1));
            
            // Expirados
            JPanel expiradosPanel = new JPanel(new BorderLayout());
            expiradosPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            JLabel expiradosLabel = new JLabel(String.format("⚠ %d Documentos Expirados", expirados.size()), SwingConstants.CENTER);
            expiradosLabel.setForeground(Color.RED);
            expiradosPanel.add(expiradosLabel, BorderLayout.CENTER);
            alertasContent.add(expiradosPanel);
            
            // Próximos à expiração
            JPanel proximosPanel = new JPanel(new BorderLayout());
            proximosPanel.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 1));
            JLabel proximosLabel = new JLabel(String.format("⏰ %d Documentos Expiram em 30 dias", proximos.size()), SwingConstants.CENTER);
            proximosLabel.setForeground(Color.ORANGE);
            proximosPanel.add(proximosLabel, BorderLayout.CENTER);
            alertasContent.add(proximosPanel);
            
            alertasPanel.add(alertasContent, BorderLayout.CENTER);
        } catch (SQLException e) {
            alertasPanel.add(new JLabel("Erro ao carregar alertas"));
        }
        
        // Título do formulário
        JPanel formHeaderPanel = new JPanel(new BorderLayout());
        formHeaderPanel.setBackground(new Color(245, 245, 250));
        formHeaderPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel formTitleLabel = new JLabel("📝 Dados do Documento", SwingConstants.LEFT);
        formTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitleLabel.setForeground(new Color(70, 130, 180));
        
        formHeaderPanel.add(formTitleLabel, BorderLayout.CENTER);
        rightPanel.add(formHeaderPanel, BorderLayout.NORTH);
        
        // Painel de formulário
        JPanel formularioPanel = new JPanel(new BorderLayout());
        formularioPanel.setBackground(Color.WHITE);
        formularioPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        //JLabel formTitle = new JLabel("📝 Dados do Documento");
        //formTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        //formTitle.setForeground(new Color(70, 130, 180));
        
                
        // SEÇÃO 1: Dados do Documento
        JPanel dadosDocumentoPanel = new JPanel(new BorderLayout());
        dadosDocumentoPanel.setBackground(Color.WHITE);
        dadosDocumentoPanel.setBorder(BorderFactory.createTitledBorder("📄 Dados do Documento"));
        
        JPanel dadosDocumentoContent = new JPanel(PadraoLayout.criarLayoutFormularioAlinhado());
        dadosDocumentoContent.setBackground(Color.WHITE);
        
        // Primeira linha: Nome Arquivo (ocupa linha inteira)
        GridBagConstraints nomeLabelConstraints = new GridBagConstraints();
        nomeLabelConstraints.gridx = 0;
        nomeLabelConstraints.gridy = 0;
        nomeLabelConstraints.insets = new Insets(5, 8, 5, 8);
        nomeLabelConstraints.anchor = GridBagConstraints.WEST;
        nomeLabelConstraints.fill = GridBagConstraints.NONE;
        dadosDocumentoContent.add(PadraoLayout.criarLabelFormulario("Nome Arquivo:"), nomeLabelConstraints);
        
        JPanel nomeArquivoPanel = new JPanel(new BorderLayout());
        PadraoLayout.estilizarCampoNomeArquivo(nomeArquivoField);
        nomeArquivoPanel.add(nomeArquivoField, BorderLayout.CENTER);
        nomeArquivoPanel.add(selecionarArquivoButton, BorderLayout.EAST);
        
        GridBagConstraints nomeFieldConstraints = new GridBagConstraints();
        nomeFieldConstraints.gridx = 1;
        nomeFieldConstraints.gridy = 0;
        nomeFieldConstraints.gridwidth = 3;
        nomeFieldConstraints.weightx = 1.0;
        nomeFieldConstraints.insets = new Insets(5, 8, 5, 8);
        nomeFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        nomeFieldConstraints.anchor = GridBagConstraints.WEST;
        dadosDocumentoContent.add(nomeArquivoPanel, nomeFieldConstraints);
        
        // Segunda linha: Caminho e Tipo lado a lado
        GridBagConstraints caminhoLabelConstraints = new GridBagConstraints();
        caminhoLabelConstraints.gridx = 0;
        caminhoLabelConstraints.gridy = 1;
        caminhoLabelConstraints.insets = new Insets(5, 8, 5, 8);
        caminhoLabelConstraints.anchor = GridBagConstraints.WEST;
        caminhoLabelConstraints.fill = GridBagConstraints.NONE;
        dadosDocumentoContent.add(PadraoLayout.criarLabelFormulario("Caminho:"), caminhoLabelConstraints);
        
        GridBagConstraints caminhoFieldConstraints = new GridBagConstraints();
        caminhoFieldConstraints.gridx = 1;
        caminhoFieldConstraints.gridy = 1;
        caminhoFieldConstraints.gridwidth = 1;
        caminhoFieldConstraints.weightx = 0.6;
        caminhoFieldConstraints.insets = new Insets(5, 8, 5, 8);
        caminhoFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        caminhoFieldConstraints.anchor = GridBagConstraints.WEST;
        PadraoLayout.estilizarCampoCaminhoArquivo(caminhoArquivoField);
        dadosDocumentoContent.add(caminhoArquivoField, caminhoFieldConstraints);
        
        GridBagConstraints tipoLabelConstraints = new GridBagConstraints();
        tipoLabelConstraints.gridx = 2;
        tipoLabelConstraints.gridy = 1;
        tipoLabelConstraints.insets = new Insets(5, 8, 5, 8);
        tipoLabelConstraints.anchor = GridBagConstraints.WEST;
        tipoLabelConstraints.fill = GridBagConstraints.NONE;
        dadosDocumentoContent.add(PadraoLayout.criarLabelFormulario("Tipo:"), tipoLabelConstraints);
        
        GridBagConstraints tipoFieldConstraints = new GridBagConstraints();
        tipoFieldConstraints.gridx = 3;
        tipoFieldConstraints.gridy = 1;
        tipoFieldConstraints.gridwidth = 1;
        tipoFieldConstraints.weightx = 0.4;
        tipoFieldConstraints.insets = new Insets(5, 8, 5, 8);
        tipoFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        tipoFieldConstraints.anchor = GridBagConstraints.WEST;
        PadraoLayout.estilizarComboBox(tipoComboBox);
        dadosDocumentoContent.add(tipoComboBox, tipoFieldConstraints);
        
        // Terceira linha: Descrição (ocupa linha inteira)
        GridBagConstraints descricaoLabelConstraints = new GridBagConstraints();
        descricaoLabelConstraints.gridx = 0;
        descricaoLabelConstraints.gridy = 2;
        descricaoLabelConstraints.insets = new Insets(5, 8, 5, 8);
        descricaoLabelConstraints.anchor = GridBagConstraints.WEST;
        descricaoLabelConstraints.fill = GridBagConstraints.NONE;
        dadosDocumentoContent.add(PadraoLayout.criarLabelFormulario("Descrição:"), descricaoLabelConstraints);
        
        GridBagConstraints descricaoFieldConstraints = new GridBagConstraints();
        descricaoFieldConstraints.gridx = 1;
        descricaoFieldConstraints.gridy = 2;
        descricaoFieldConstraints.gridwidth = 3;
        descricaoFieldConstraints.weightx = 1.0;
        descricaoFieldConstraints.insets = new Insets(5, 8, 5, 8);
        descricaoFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        descricaoFieldConstraints.anchor = GridBagConstraints.WEST;
        PadraoLayout.estilizarCampoDescricao(descricaoField);
        dadosDocumentoContent.add(descricaoField, descricaoFieldConstraints);
        
        dadosDocumentoPanel.add(dadosDocumentoContent, BorderLayout.CENTER);
        formContainer.add(dadosDocumentoPanel, BorderLayout.NORTH);
        
        // SEÇÃO 2: Informações Adicionais
        JPanel infoAdicionaisPanel = new JPanel(new BorderLayout());
        infoAdicionaisPanel.setBackground(Color.WHITE);
        infoAdicionaisPanel.setBorder(BorderFactory.createTitledBorder("📝 Informações Adicionais"));
        
        JPanel infoAdicionaisContent = new JPanel(PadraoLayout.criarLayoutFormularioAlinhado());
        infoAdicionaisContent.setBackground(Color.WHITE);
        
        // Data Expiração e Usuário Upload na mesma linha
        GridBagConstraints dataLabelConstraints = new GridBagConstraints();
        dataLabelConstraints.gridx = 0;
        dataLabelConstraints.gridy = 0;
        dataLabelConstraints.insets = new Insets(5, 8, 5, 8);
        dataLabelConstraints.anchor = GridBagConstraints.WEST;
        dataLabelConstraints.fill = GridBagConstraints.NONE;
        infoAdicionaisContent.add(PadraoLayout.criarLabelFormulario("Data Expiração:"), dataLabelConstraints);
        
        GridBagConstraints dataFieldConstraints = new GridBagConstraints();
        dataFieldConstraints.gridx = 1;
        dataFieldConstraints.gridy = 0;
        dataFieldConstraints.weightx = 0.3;
        dataFieldConstraints.insets = new Insets(5, 8, 5, 8);
        dataFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        dataFieldConstraints.anchor = GridBagConstraints.WEST;
        PadraoLayout.estilizarCampoData(dataExpiracaoField);
        infoAdicionaisContent.add(dataExpiracaoField, dataFieldConstraints);
        
        GridBagConstraints usuarioLabelConstraints = new GridBagConstraints();
        usuarioLabelConstraints.gridx = 2;
        usuarioLabelConstraints.gridy = 0;
        usuarioLabelConstraints.insets = new Insets(5, 8, 5, 8);
        usuarioLabelConstraints.anchor = GridBagConstraints.WEST;
        usuarioLabelConstraints.fill = GridBagConstraints.NONE;
        infoAdicionaisContent.add(PadraoLayout.criarLabelFormulario("Usuário Upload:"), usuarioLabelConstraints);
        
        GridBagConstraints usuarioFieldConstraints = new GridBagConstraints();
        usuarioFieldConstraints.gridx = 3;
        usuarioFieldConstraints.gridy = 0;
        usuarioFieldConstraints.weightx = 0.7;
        usuarioFieldConstraints.insets = new Insets(5, 8, 5, 8);
        usuarioFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        usuarioFieldConstraints.anchor = GridBagConstraints.WEST;
        PadraoLayout.estilizarCampoUsuarioUpload(usuarioUploadField);
        infoAdicionaisContent.add(usuarioUploadField, usuarioFieldConstraints);
        
        // Tamanho e Formato na mesma linha
        GridBagConstraints tamanhoLabelConstraints = new GridBagConstraints();
        tamanhoLabelConstraints.gridx = 0;
        tamanhoLabelConstraints.gridy = 1;
        tamanhoLabelConstraints.insets = new Insets(5, 8, 5, 8);
        tamanhoLabelConstraints.anchor = GridBagConstraints.WEST;
        tamanhoLabelConstraints.fill = GridBagConstraints.NONE;
        infoAdicionaisContent.add(PadraoLayout.criarLabelFormulario("Tamanho:"), tamanhoLabelConstraints);
        
        GridBagConstraints tamanhoFieldConstraints = new GridBagConstraints();
        tamanhoFieldConstraints.gridx = 1;
        tamanhoFieldConstraints.gridy = 1;
        tamanhoFieldConstraints.weightx = 0.5;
        tamanhoFieldConstraints.insets = new Insets(5, 8, 5, 8);
        tamanhoFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        tamanhoFieldConstraints.anchor = GridBagConstraints.WEST;
        PadraoLayout.estilizarCampoTexto(tamanhoField);
        tamanhoField.setColumns(10); // Ajustar manualmente para tamanho ideal
        infoAdicionaisContent.add(tamanhoField, tamanhoFieldConstraints);
        
        GridBagConstraints formatoLabelConstraints = new GridBagConstraints();
        formatoLabelConstraints.gridx = 2;
        formatoLabelConstraints.gridy = 1;
        formatoLabelConstraints.insets = new Insets(5, 8, 5, 8);
        formatoLabelConstraints.anchor = GridBagConstraints.WEST;
        formatoLabelConstraints.fill = GridBagConstraints.NONE;
        infoAdicionaisContent.add(PadraoLayout.criarLabelFormulario("Formato:"), formatoLabelConstraints);
        
        GridBagConstraints formatoFieldConstraints = new GridBagConstraints();
        formatoFieldConstraints.gridx = 3;
        formatoFieldConstraints.gridy = 1;
        formatoFieldConstraints.weightx = 0.5;
        formatoFieldConstraints.insets = new Insets(5, 8, 5, 8);
        formatoFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        formatoFieldConstraints.anchor = GridBagConstraints.WEST;
        PadraoLayout.estilizarCampoFormatoArquivo(formatoField);
        infoAdicionaisContent.add(formatoField, formatoFieldConstraints);
        
        infoAdicionaisPanel.add(infoAdicionaisContent, BorderLayout.CENTER);
        formContainer.add(infoAdicionaisPanel, BorderLayout.CENTER);
        
        // Grupo 3: Segurança e Validação
        JPanel segurancaPanel = createFormGroup("🔒 Segurança e Validação");
        JPanel segurancaContent = new JPanel(new BorderLayout());
        segurancaContent.setBackground(Color.WHITE);
        
        // Painel de opções de segurança
        JPanel textAreasPanel = new JPanel(new GridLayout(1, 1, 10, 10));
        textAreasPanel.setBackground(Color.WHITE);
        
        JPanel hashPanel = new JPanel(new BorderLayout());
        hashPanel.setBackground(Color.WHITE);
        hashPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 210), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        hashPanel.add(new JLabel("🔐 Hash do Documento:"), BorderLayout.NORTH);
        hashPanel.add(new JScrollPane(hashArea), BorderLayout.CENTER);
        
        textAreasPanel.add(hashPanel);
        segurancaContent.add(textAreasPanel, BorderLayout.CENTER);
        segurancaPanel.add(segurancaContent);
        
        // Organizar grupos verticalmente usando PadraoLayout
        JPanel allGroups = PadraoLayout.criarFormularioMultiplosGrupos(
            dadosDocumentoPanel, infoAdicionaisPanel, segurancaPanel
        );
        
        // Adicionar scroll ao formulário usando PadraoLayout
        JScrollPane formScroll = PadraoLayout.criarFormularioComScroll(allGroups);
        
        formContainer.add(formScroll, BorderLayout.CENTER);
        
        // Painel de botões - apenas essenciais
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botoesPanel.setBackground(Color.WHITE);
        
        botoesPanel.add(salvarButton);
        botoesPanel.add(novoButton);
        botoesPanel.add(excluirButton);
        botoesPanel.add(selecionarArquivoButton);
        botoesPanel.add(visualizarButton);
        botoesPanel.add(emailButton);
        botoesPanel.add(whatsappButton);
        // Removidos: uploadButton, agendarButton, relatorioButton, assinarButton
        
        //formularioPanel.add(formTitle, BorderLayout.NORTH);
        formularioPanel.add(formContainer, BorderLayout.CENTER);
        formularioPanel.add(botoesPanel, BorderLayout.SOUTH);
        
        rightPanel.add(formularioPanel, BorderLayout.CENTER);
        
        formPanel.add(rightPanel, BorderLayout.CENTER);
        
        // Painel de tabela usando PadraoLayout
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBackground(PadraoLayout.COR_PAINEL);
        tabelaPanel.setBorder(PadraoLayout.BORDA_CONTEUDO);
        
        // Painel de estatísticas e alertas
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(statsPanel, BorderLayout.CENTER);
        topPanel.add(alertasPanel, BorderLayout.SOUTH);
        
        // Tabela estilizada usando PadraoLayout
        PadraoLayout.configurarTabela(documentosTable);
        JScrollPane tableScrollPane = new JScrollPane(documentosTable);
        
        tabelaPanel.add(topPanel, BorderLayout.NORTH);
        tabelaPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Split vertical: Formulário acima, Tabela abaixo
        JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formPanel, tabelaPanel);
        verticalSplitPane.setDividerLocation(350);
        verticalSplitPane.setResizeWeight(0.4);
        
        contentPanel.add(verticalSplitPane, BorderLayout.CENTER);
        
        // Adicionar contentPanel ao mainPanel
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
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
    
    private JPanel createStatPanel(String titulo, String valor, Color cor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEtchedBorder());
        
        JLabel tituloLabel = new JLabel(titulo, SwingConstants.CENTER);
        tituloLabel.setForeground(cor);
        panel.add(tituloLabel, BorderLayout.NORTH);
        
        JLabel valorLabel = new JLabel(valor, SwingConstants.CENTER);
        valorLabel.setFont(new Font("Arial", Font.BOLD, 16));
        valorLabel.setForeground(cor);
        panel.add(valorLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void setupEvents() {
        salvarButton.addActionListener(e -> salvarDocumento());
        novoButton.addActionListener(e -> limparFormulario());
        excluirButton.addActionListener(e -> excluirDocumento());
        selecionarArquivoButton.addActionListener(e -> selecionarArquivo());
        visualizarButton.addActionListener(e -> visualizarDocumento());
        emailButton.addActionListener(e -> enviarDocumentoPorEmail());
        whatsappButton.addActionListener(e -> enviarDocumentoPorWhatsApp());
        // Botões removidos: assinarButton, uploadButton, relatorioButton, agendarButton
        
        // Seleção na tabela
        documentosTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarDocumentoSelecionado();
            }
        });
    }
    
    private void selecionarArquivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File arquivoSelecionado = fileChooser.getSelectedFile();
            caminhoArquivoField.setText(arquivoSelecionado.getAbsolutePath());
            nomeArquivoField.setText(arquivoSelecionado.getName());
            
            // Extrair informações do arquivo
            tamanhoField.setText(formatarTamanho(arquivoSelecionado.length()));
            formatoField.setText(obterFormatoArquivo(arquivoSelecionado.getName()));
            
            // Gerar hash (simulação)
            String hash = gerarHashSimulado(arquivoSelecionado.getAbsolutePath());
            hashArea.setText(hash);
        }
    }
    
    private String formatarTamanho(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.1f KB", bytes / 1024.0);
        } else {
            return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
        }
    }
    
    private String obterFormatoArquivo(String nomeArquivo) {
        int ultimoPonto = nomeArquivo.lastIndexOf('.');
        if (ultimoPonto > 0) {
            return nomeArquivo.substring(ultimoPonto + 1).toUpperCase();
        }
        return "DESCONHECIDO";
    }
    
    private String gerarHashSimulado(String caminho) {
        // Simulação de hash SHA-256
        return "SHA256:" + Integer.toHexString(caminho.hashCode()).toUpperCase();
    }
    
    private void salvarDocumento() {
        try {
            if (nomeArquivoField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nome do arquivo é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (caminhoArquivoField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Caminho do arquivo é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Documento documento = documentoAtual != null ? documentoAtual : new Documento();
            documento.setNomeArquivo(nomeArquivoField.getText().trim());
            
            // Garantir que documento esteja ativo ao salvar
            documento.setAtivo(true);
            documento.setCaminhoArquivo(caminhoArquivoField.getText().trim());
            documento.setDescricao(descricaoField.getText().trim());
            documento.setTipo((String) tipoComboBox.getSelectedItem());
            documento.setTamanhoArquivo(parseTamanho(tamanhoField.getText()));
            documento.setFormatoArquivo(formatoField.getText());
            documento.setHashArquivo(hashArea.getText().trim());
            documento.setUsuarioUpload(usuarioUploadField.getText().trim());
            
            if (!dataExpiracaoField.getText().trim().isEmpty()) {
                String dataStr = dataExpiracaoField.getText().trim();
                // Converter formato xx/xx/xxxx para LocalDateTime
                if (dataStr.matches("\\d{2}/\\d{2}/\\d{4}")) {
                    String[] partes = dataStr.split("/");
                    int dia = Integer.parseInt(partes[0]);
                    int mes = Integer.parseInt(partes[1]);
                    int ano = Integer.parseInt(partes[2]);
                    documento.setDataExpiracao(LocalDateTime.of(ano, mes, dia, 0, 0, 0));
                } else {
                    // Tentar formato existente como fallback
                    documento.setDataExpiracao(LocalDateTime.parse(dataStr + "T00:00:00"));
                }
            }
            
            System.out.println("[DocumentosPanel] salvarDocumento() - Salvando documento: " + documento.getNomeArquivo());
            System.out.println("[DocumentosPanel] salvarDocumento() - Usuário: " + documento.getUsuarioUpload());
            System.out.println("[DocumentosPanel] salvarDocumento() - Data Expiração: " + documento.getDataExpiracao());
            
            try {
                System.out.println("[DocumentosPanel] Chamando documentoDAO.save()...");
                documentoDAO.save(documento);
                System.out.println("[DocumentosPanel] documentoDAO.save() executado com sucesso!");
                System.out.println("[DocumentosPanel] Documento salvo com ID: " + documento.getId());
            } catch (Exception daoEx) {
                System.out.println("[DocumentosPanel] ERRO no documentoDAO.save(): " + daoEx.getMessage());
                daoEx.printStackTrace();
                throw daoEx;
            }
            
            JOptionPane.showMessageDialog(this, "Documento salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            refreshData();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar documento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private double parseTamanho(String texto) {
        try {
            if (texto.endsWith("KB")) {
                return Double.parseDouble(texto.replace("KB", "").trim()) * 1024;
            } else if (texto.endsWith("MB")) {
                return Double.parseDouble(texto.replace("MB", "").trim()) * 1024 * 1024;
            } else if (texto.endsWith("B")) {
                return Double.parseDouble(texto.replace("B", "").trim());
            }
        } catch (NumberFormatException e) {
            // Ignora erros
        }
        return 0.0;
    }
    
    private void limparFormulario() {
        documentoAtual = null;
        nomeArquivoField.setText("");
        caminhoArquivoField.setText("");
        descricaoField.setText("");
        dataExpiracaoField.setText("");
        usuarioUploadField.setText("");
        tamanhoField.setText("");
        formatoField.setText("");
        hashArea.setText("");
        tipoComboBox.setSelectedItem("OUTRO");
        nomeArquivoField.requestFocus();
        atualizarBotoesAcao();
    }
    
    private void excluirDocumento() {
        if (documentoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um documento para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente excluir o documento " + documentoAtual.getNomeArquivo() + "?", 
            "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                documentoDAO.delete(documentoAtual.getId());
                JOptionPane.showMessageDialog(this, "Documento excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir documento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void visualizarDocumento() {
        if (documentoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um documento para visualizar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (documentoAtual.getCaminhoArquivo() == null || documentoAtual.getCaminhoArquivo().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Documento não possui caminho definido!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        File arquivo = new File(documentoAtual.getCaminhoArquivo());
        if (!arquivo.exists()) {
            JOptionPane.showMessageDialog(this, "Arquivo não encontrado: " + documentoAtual.getCaminhoArquivo(), "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            Desktop.getDesktop().open(arquivo);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao abrir arquivo: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    private void carregarDocumentoSelecionado() {
        int selectedRow = documentosTable.getSelectedRow();
        if (selectedRow >= 0) {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            try {
                documentoAtual = documentoDAO.findById(id);
                if (documentoAtual != null) {
                    nomeArquivoField.setText(documentoAtual.getNomeArquivo());
                    caminhoArquivoField.setText(documentoAtual.getCaminhoArquivo());
                    descricaoField.setText(documentoAtual.getDescricao());
                    tipoComboBox.setSelectedItem(documentoAtual.getTipo());
                    tamanhoField.setText(documentoAtual.getTamanhoFormatado());
                    formatoField.setText(documentoAtual.getFormatoArquivo());
                    hashArea.setText(documentoAtual.getHashArquivo());
                    usuarioUploadField.setText(documentoAtual.getUsuarioUpload());
                    
                    if (documentoAtual.getDataExpiracao() != null) {
                        LocalDateTime data = documentoAtual.getDataExpiracao();
                        String dataFormatada = String.format("%02d/%02d/%04d", 
                            data.getDayOfMonth(), data.getMonthValue(), data.getYear());
                        dataExpiracaoField.setText(dataFormatada);
                    }
                    
                    atualizarBotoesAcao();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar documento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void atualizarBotoesAcao() {
        boolean temDocumento = documentoAtual != null;
        boolean temArquivo = temDocumento && documentoAtual.getCaminhoArquivo() != null;
        
        visualizarButton.setEnabled(temArquivo);
    }
    
    private void enviarDocumentoPorEmail() {
        System.out.println("[DocumentosPanel] enviarDocumentoPorEmail() - Iniciando envio de documento por e-mail");
        
        if (documentoAtual == null) {
            JOptionPane.showMessageDialog(this, 
                "Nenhum documento selecionado!", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        System.out.println("[DocumentosPanel] enviarDocumentoPorEmail() - Documento selecionado: " + documentoAtual.getNomeArquivo());
        
        // Criar diálogo para envio de e-mail
        JTextField emailField = new JTextField("", 30);
        JTextField assuntoField = new JTextField("Documento: " + documentoAtual.getNomeArquivo(), 30);
        JTextArea mensagemField = new JTextArea(
            "Prezado(a),\n\nSegue em anexo o documento solicitado:\n" +
            "Nome: " + documentoAtual.getNomeArquivo() + "\n" +
            "Tipo: " + documentoAtual.getTipo() + "\n" +
            "Tamanho: " + documentoAtual.getTamanhoFormatado() + "\n" +
            "Data Upload: " + documentoAtual.getDataUpload() + "\n\n" +
            "Atenciosamente,\nSistema ArteReal", 5, 30);
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Para:"));
        panel.add(emailField);
        panel.add(new JLabel("Assunto:"));
        panel.add(assuntoField);
        panel.add(new JLabel("Mensagem:"));
        panel.add(new JScrollPane(mensagemField));
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
            "Enviar Documento por E-mail", 
            JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String email = emailField.getText().trim();
            String assunto = assuntoField.getText().trim();
            String mensagem = mensagemField.getText().trim();
            
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "O campo 'Para' é obrigatório!", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            System.out.println("[DocumentosPanel] enviarDocumentoPorEmail() - Enviando para: " + email);
            
            // Verificar configurações de e-mail
            com.artereal.swing.service.EmailService emailService = new com.artereal.swing.service.EmailService();
            if (!emailService.validarConfiguracoes()) {
                JOptionPane.showMessageDialog(this, 
                    "Configurações de e-mail inválidas!\n" +
                    "Verifique as configurações SMTP no EmailService.", 
                    "Erro de Configuração", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Construir mensagem completa
            String mensagemCompleta = mensagem + "\n\n" +
                "---\n" +
                "Documento enviado pelo Sistema ArteReal\n" +
                "ID: " + documentoAtual.getId() + "\n" +
                "Nome: " + documentoAtual.getNomeArquivo() + "\n" +
                "Tipo: " + documentoAtual.getTipo() + "\n" +
                "Data: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n" +
                "---";
            
            // Enviar e-mail
            boolean enviado = emailService.enviarEmail(email, assunto, mensagemCompleta);
            
            if (enviado) {
                JOptionPane.showMessageDialog(this, 
                    "Documento enviado com sucesso!\n" +
                    "Para: " + email + "\n" +
                    "Assunto: " + assunto + "\n" +
                    "Documento: " + documentoAtual.getNomeArquivo() + "\n" +
                    "Status: Enviado", 
                    "E-mail Enviado", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Falha ao enviar o documento por e-mail!\n" +
                    "Verifique:\n" +
                    "1. Conexão com a internet\n" +
                    "2. Configurações SMTP\n" +
                    "3. Endereço de e-mail", 
                    "Erro de Envio", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void enviarDocumentoPorWhatsApp() {
        System.out.println("[DocumentosPanel] enviarDocumentoPorWhatsApp() - Iniciando envio de documento por WhatsApp");
        
        if (documentoAtual == null) {
            JOptionPane.showMessageDialog(this, 
                "Nenhum documento selecionado!", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        System.out.println("[DocumentosPanel] enviarDocumentoPorWhatsApp() - Documento selecionado: " + documentoAtual.getNomeArquivo());
        
        // Criar diálogo para envio via WhatsApp
        JTextField telefoneField = new JTextField("", 30);
        JTextArea mensagemField = new JTextArea(
            "📄 *Documento ArteReal*\n\n" +
            "*Nome:* " + documentoAtual.getNomeArquivo() + "\n" +
            "*Tipo:* " + documentoAtual.getTipo() + "\n" +
            "*Tamanho:* " + documentoAtual.getTamanhoFormatado() + "\n" +
            "*Data Upload:* " + documentoAtual.getDataUpload() + "\n\n" +
            "📎 *Anexo:* O documento será anexado manualmente\n\n" +
            "_Enviado pelo Sistema ArteReal v2.0.0_", 5, 30);
        
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Telefone (com DDD):"));
        panel.add(telefoneField);
        panel.add(new JLabel("Mensagem:"));
        panel.add(new JScrollPane(mensagemField));
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
            "Enviar Documento por WhatsApp", 
            JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String telefone = telefoneField.getText().trim();
            
            if (telefone.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "O campo 'Telefone' é obrigatório!", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validar número de telefone
            if (!com.artereal.swing.service.WhatsAppService.validarNumeroTelefone(telefone)) {
                JOptionPane.showMessageDialog(this, 
                    "Número de telefone inválido!\n" +
                    "Formato esperado: DDD + número (ex: 11999998888)", 
                    "Erro de Validação", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            System.out.println("[DocumentosPanel] enviarDocumentoPorWhatsApp() - Enviando para: " + telefone);
            
            // Enviar via WhatsApp
            boolean enviado = com.artereal.swing.service.WhatsAppService.enviarDocumentoWhatsApp(
                telefone, 
                documentoAtual.getNomeArquivo(), 
                documentoAtual.getTipo(), 
                "Documento enviado pelo Sistema ArteReal"
            );
            
            if (enviado) {
                JOptionPane.showMessageDialog(this, 
                    "WhatsApp Web aberto com sucesso!\n" +
                    "Telefone: " + telefone + "\n" +
                    "Documento: " + documentoAtual.getNomeArquivo() + "\n" +
                    "Status: Anexe o documento manualmente", 
                    "WhatsApp Aberto", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Falha ao abrir WhatsApp Web!\n" +
                    "Verifique:\n" +
                    "1. Conexão com a internet\n" +
                    "2. Navegador padrão configurado\n" +
                    "3. Número de telefone válido", 
                    "Erro de Envio", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void refreshData() {
        try {
            System.out.println("[DocumentosPanel] refreshData() - Iniciando carregamento de documentos");
            System.out.println("[DocumentosPanel] refreshData() - DatabaseManager instance: " + DatabaseManager.getInstance().hashCode());
            
            List<Documento> documentos = documentoDAO.findAll();
            System.out.println("[DocumentosPanel] refreshData() - Encontrados " + documentos.size() + " documentos");
            
            if (documentos.isEmpty()) {
                System.out.println("[DocumentosPanel] refreshData() - Nenhum documento encontrado. Verificando se há dados no banco...");
                // Verificar se há algum registro no banco
                try (Connection conn = DatabaseManager.getInstance().getConnection();
                     PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM documento WHERE ativo = 1");
                     ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int count = rs.getInt(1);
                        System.out.println("[DocumentosPanel] refreshData() - Total de registros ativos no banco: " + count);
                    }
                }
            }
            
            tableModel.setRowCount(0);
            System.out.println("[DocumentosPanel] refreshData() - Linhas da tabela limpas");
            
            int linha = 0;
            for (Documento documento : documentos) {
                System.out.println("[DocumentosPanel] refreshData() - Processando documento " + (linha + 1) + ": " + documento.getNomeArquivo());
                System.out.println("[DocumentosPanel] refreshData() - ID: " + documento.getId() + ", Status: " + documento.getStatus());
                
                String status = "";
                if (documento.isExpirado()) {
                    status = "EXPIRADO";
                } else if (documento.isProximoExpiracao()) {
                    status = "PRÓXIMO";
                }
                
                Object[] row = {
                    documento.getId(),
                    documento.getNomeArquivo(),
                    documento.getTipo(),
                    documento.getTamanhoFormatado(),
                    documento.getDataUpload() != null ? documento.getDataUpload().toString() : "",
                    documento.getDataExpiracao() != null ? documento.getDataExpiracao().toString() : "",
                    documento.getStatus() + (status.isEmpty() ? "" : " (" + status + ")")
                };
                
                System.out.println("[DocumentosPanel] refreshData() - Adicionando linha: " + java.util.Arrays.toString(row));
                tableModel.addRow(row);
                linha++;
            }
            
            System.out.println("[DocumentosPanel] refreshData() - Total de linhas na tabela após carregamento: " + tableModel.getRowCount());
            
        } catch (SQLException ex) {
            System.err.println("[DocumentosPanel] refreshData() - Erro SQL: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar documentos: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
}
