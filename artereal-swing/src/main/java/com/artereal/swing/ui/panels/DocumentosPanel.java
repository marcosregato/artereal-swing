package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.DocumentoDAO;
import com.artereal.swing.model.Documento;
import com.artereal.swing.ui.layout.PadraoLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
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
    private JTextArea assinaturaArea;
    private JButton salvarButton;
    private JButton novoButton;
    private JButton excluirButton;
    private JButton selecionarArquivoButton;
    private JButton visualizarButton;
    private JButton assinarButton;
    private JButton uploadButton;
    private JButton relatorioButton;
    private Documento documentoAtual;
    
    public DocumentosPanel() {
        documentoDAO = new DocumentoDAO();
        initializeComponents();
        setupLayout();
        setupEvents();
        refreshData();
    }
    
    private void initializeComponents() {
        // Tabela
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome Arquivo", "Tipo", "Tamanho", "Upload", "Expiração", "Status", "Assinatura"}, 0) {
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
        dataExpiracaoField = new JTextField(12);
        usuarioUploadField = new JTextField(20);
        tamanhoField = new JTextField(15);
        tamanhoField.setEditable(false);
        formatoField = new JTextField(10);
        formatoField.setEditable(false);
        tipoComboBox = new JComboBox<>(new String[]{"IDENTIDADE", "DIPLOMA", "CERTIFICADO", "FOTO", "OUTRO"});
        hashArea = new JTextArea(2, 40);
        hashArea.setEditable(false);
        hashArea.setFont(new Font("Monospaced", Font.PLAIN, 10));
        assinaturaArea = new JTextArea(2, 40);
        assinaturaArea.setEditable(false);
        assinaturaArea.setFont(new Font("Monospaced", Font.PLAIN, 10));
        
        // Pesquisa
        pesquisarField = new JTextField(20);
        pesquisarButton = PadraoLayout.criarBotaoPesquisar();
        
        // Botões usando PadraoLayout com cores pastéis
        salvarButton = PadraoLayout.criarBotaoSalvar();
        novoButton = PadraoLayout.criarBotaoNovo();
        excluirButton = PadraoLayout.criarBotaoExcluir();
        selecionarArquivoButton = PadraoLayout.criarBotao("Selecionar Arquivo", new Color(173, 216, 230)); // Azul pastel
        visualizarButton = PadraoLayout.criarBotao("Visualizar", new Color(255, 250, 205)); // Amarelo pastel
        assinarButton = PadraoLayout.criarBotao("Assinar Digitalmente", new Color(152, 251, 152)); // Verde menta
        uploadButton = PadraoLayout.criarBotao("Upload", new Color(221, 160, 221)); // Lavanda pastel
        relatorioButton = PadraoLayout.criarBotao("Relatório", new Color(200, 200, 255)); // Azul claro
        
        documentoAtual = null;
    }
    
    private void setupLayout() {
        // Aplicar layout padrão usando PadraoLayout - CORREÇÃO EM LOTE
        this.setBorder(PadraoLayout.BORDA_PAINEL);
        this.setBackground(PadraoLayout.COR_FUNDO);
        
        // Header estilizado usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader("📄 Gestão Documental", "Armazenamento e assinatura digital de documentos");
        add(headerPanel, BorderLayout.NORTH);
        
                
        // Painel esquerdo - Tabela e estatísticas usando PadraoLayout - CORREÇÃO EM LOTE
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(PadraoLayout.COR_FUNDO);
        leftPanel.setBorder(PadraoLayout.BORDA_CONTEUDO);
        
        // Painel de pesquisa usando PadraoLayout
        JPanel pesquisaPanel = PadraoLayout.criarPainelPesquisa(pesquisarField, pesquisarButton);
        
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
        
        // Painel direito - Formulário (padrão SessoesPanel)
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        
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
        
        JLabel formTitle = new JLabel("📝 Dados do Documento");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formTitle.setForeground(new Color(70, 130, 180));
        
        // Container principal para todos os grupos
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        
        // Grupo 1: Dados Básicos
        JPanel dadosBasicosPanel = createFormGroup("📅 Dados Básicos");
        JPanel dadosBasicosContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        dadosBasicosContent.setBackground(Color.WHITE);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Nome Arquivo:"));
        JPanel nomeArquivoPanel = new JPanel(new BorderLayout());
        PadraoLayout.estilizarCampoTexto(nomeArquivoField);
        nomeArquivoPanel.add(nomeArquivoField, BorderLayout.CENTER);
        nomeArquivoPanel.add(selecionarArquivoButton, BorderLayout.EAST);
        dadosBasicosContent.add(nomeArquivoPanel);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Caminho:"));
        PadraoLayout.estilizarCampoTexto(caminhoArquivoField);
        dadosBasicosContent.add(caminhoArquivoField);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Tipo:"));
        PadraoLayout.estilizarComboBox(tipoComboBox);
        dadosBasicosContent.add(tipoComboBox);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Descrição:"));
        PadraoLayout.estilizarCampoTexto(descricaoField);
        dadosBasicosContent.add(descricaoField);
        
        dadosBasicosPanel.add(dadosBasicosContent);
        
        // Grupo 2: Informações Adicionais
        JPanel infoAdicionaisPanel = createFormGroup("📝 Informações Adicionais");
        JPanel infoAdicionaisContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        infoAdicionaisContent.setBackground(Color.WHITE);
        
        infoAdicionaisContent.add(PadraoLayout.criarLabelFormulario("Data Expiração:"));
        PadraoLayout.estilizarCampoTexto(dataExpiracaoField);
        infoAdicionaisContent.add(dataExpiracaoField);
        
        infoAdicionaisContent.add(PadraoLayout.criarLabelFormulario("Usuário Upload:"));
        PadraoLayout.estilizarCampoTexto(usuarioUploadField);
        infoAdicionaisContent.add(usuarioUploadField);
        
        infoAdicionaisContent.add(PadraoLayout.criarLabelFormulario("Tamanho:"));
        PadraoLayout.estilizarCampoTexto(tamanhoField);
        infoAdicionaisContent.add(tamanhoField);
        
        infoAdicionaisContent.add(PadraoLayout.criarLabelFormulario("Formato:"));
        PadraoLayout.estilizarCampoTexto(formatoField);
        infoAdicionaisContent.add(formatoField);
        
        infoAdicionaisPanel.add(infoAdicionaisContent);
        
        // Grupo 3: Segurança e Validação
        JPanel segurancaPanel = createFormGroup("🔒 Segurança e Validação");
        JPanel segurancaContent = new JPanel(new BorderLayout());
        segurancaContent.setBackground(Color.WHITE);
        
        // Painel de opções de segurança
        JPanel textAreasPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        textAreasPanel.setBackground(Color.WHITE);
        
        JPanel hashPanel = new JPanel(new BorderLayout());
        hashPanel.setBackground(Color.WHITE);
        hashPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 210), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        hashPanel.add(new JLabel("🔐 Hash do Documento:"), BorderLayout.NORTH);
        hashPanel.add(new JScrollPane(hashArea), BorderLayout.CENTER);
        
        JPanel assinaturaPanel = new JPanel(new BorderLayout());
        assinaturaPanel.setBackground(Color.WHITE);
        assinaturaPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 210), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        assinaturaPanel.add(new JLabel("✍️ Assinatura Digital:"), BorderLayout.NORTH);
        assinaturaPanel.add(new JScrollPane(assinaturaArea), BorderLayout.CENTER);
        
        textAreasPanel.add(hashPanel);
        textAreasPanel.add(assinaturaPanel);
        segurancaContent.add(textAreasPanel, BorderLayout.CENTER);
        segurancaPanel.add(segurancaContent);
        
        // Organizar grupos verticalmente usando PadraoLayout
        JPanel allGroups = PadraoLayout.criarFormularioMultiplosGrupos(
            dadosBasicosPanel, infoAdicionaisPanel, segurancaPanel
        );
        
        // Adicionar scroll ao formulário usando PadraoLayout
        JScrollPane formScroll = PadraoLayout.criarFormularioComScroll(allGroups);
        
        formContainer.add(formScroll, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botoesPanel.setBackground(Color.WHITE);
        
        botoesPanel.add(createStyledButton("Salvar", new Color(144, 238, 144)));
        botoesPanel.add(createStyledButton("Novo", new Color(173, 216, 230)));
        botoesPanel.add(createStyledButton("Excluir", new Color(255, 182, 193)));
        botoesPanel.add(createStyledButton("Upload", new Color(255, 250, 205)));
        botoesPanel.add(createStyledButton("Assinar", new Color(255, 218, 185)));
        
        formularioPanel.add(formTitle, BorderLayout.NORTH);
        formularioPanel.add(formContainer, BorderLayout.CENTER);
        formularioPanel.add(botoesPanel, BorderLayout.SOUTH);
        
        rightPanel.add(formularioPanel, BorderLayout.CENTER);
        
        // Painel principal com split
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setDividerLocation(650);
        mainSplitPane.setResizeWeight(0.6);
        
        // Painel esquerdo - Tabela e estatísticas
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(pesquisaPanel, BorderLayout.NORTH);
        topPanel.add(statsPanel, BorderLayout.CENTER);
        topPanel.add(alertasPanel, BorderLayout.SOUTH);
        
        // Tabela estilizada
        JScrollPane tableScrollPane = new JScrollPane(documentosTable);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 210)));
        tableScrollPane.getViewport().setBackground(Color.WHITE);
        
        // Tabela estilizada usando PadraoLayout
        PadraoLayout.configurarTabela(documentosTable);
        
        tablePanel.add(topPanel, BorderLayout.NORTH);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);
        
        mainSplitPane.setLeftComponent(tablePanel);
        mainSplitPane.setRightComponent(rightPanel);
        
        add(mainSplitPane, BorderLayout.CENTER);
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
        assinarButton.addActionListener(e -> assinarDocumento());
        uploadButton.addActionListener(e -> uploadDocumento());
        relatorioButton.addActionListener(e -> gerarRelatorio());
        
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
            documento.setCaminhoArquivo(caminhoArquivoField.getText().trim());
            documento.setDescricao(descricaoField.getText().trim());
            documento.setTipo((String) tipoComboBox.getSelectedItem());
            documento.setTamanhoArquivo(parseTamanho(tamanhoField.getText()));
            documento.setFormatoArquivo(formatoField.getText());
            documento.setHashArquivo(hashArea.getText().trim());
            documento.setUsuarioUpload(usuarioUploadField.getText().trim());
            
            if (!dataExpiracaoField.getText().trim().isEmpty()) {
                documento.setDataExpiracao(LocalDateTime.parse(dataExpiracaoField.getText().trim() + "T00:00:00"));
            }
            
            if (!assinaturaArea.getText().trim().isEmpty()) {
                documento.setAssinaturaDigital(assinaturaArea.getText().trim());
            }
            
            documentoDAO.save(documento);
            
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
        assinaturaArea.setText("");
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
    
    private void assinarDocumento() {
        if (documentoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um documento para assinar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String assinatura = JOptionPane.showInputDialog(this, "Digite a assinatura digital:");
        if (assinatura != null && !assinatura.trim().isEmpty()) {
            documentoAtual.setAssinaturaDigital(assinatura.trim());
            assinaturaArea.setText(assinatura.trim());
            
            try {
                documentoDAO.save(documentoAtual);
                JOptionPane.showMessageDialog(this, "Documento assinado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao assinar documento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void uploadDocumento() {
        JOptionPane.showMessageDialog(this, "Funcionalidade de upload em desenvolvimento!\n" +
                                     "Use 'Selecionar Arquivo' para escolher o documento.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void gerarRelatorio() {
        try {
            List<Documento> documentos = documentoDAO.findAll();
            StringBuilder relatorio = new StringBuilder();
            relatorio.append("RELATÓRIO DE DOCUMENTOS\n");
            relatorio.append("=====================\n\n");
            
            double totalTamanho = 0;
            int totalAtivos = 0;
            int totalExpirados = 0;
            int totalAssinados = 0;
            
            for (Documento documento : documentos) {
                relatorio.append(String.format("Arquivo: %s\n", documento.getNomeArquivo()));
                relatorio.append(String.format("Tipo: %s\n", documento.getTipo()));
                relatorio.append(String.format("Tamanho: %s\n", documento.getTamanhoFormatado()));
                relatorio.append(String.format("Upload: %s\n", documento.getDataUpload()));
                relatorio.append(String.format("Usuário: %s\n", documento.getUsuarioUpload()));
                relatorio.append(String.format("Status: %s\n", documento.getStatus()));
                
                if (documento.getDataExpiracao() != null) {
                    relatorio.append(String.format("Expiração: %s\n", documento.getDataExpiracao()));
                }
                
                if (documento.temAssinaturaDigital()) {
                    relatorio.append("Assinatura: Sim\n");
                    totalAssinados++;
                } else {
                    relatorio.append("Assinatura: Não\n");
                }
                
                relatorio.append("------------------------\n");
                
                totalTamanho += documento.getTamanhoArquivo();
                if (documento.isAtivo()) totalAtivos++;
                if (documento.isExpirado()) totalExpirados++;
            }
            
            relatorio.append("\nRESUMO\n");
            relatorio.append("======\n");
            relatorio.append(String.format("Total de Documentos: %d\n", documentos.size()));
            relatorio.append(String.format("Total Ativos: %d\n", totalAtivos));
            relatorio.append(String.format("Total Expirados: %d\n", totalExpirados));
            relatorio.append(String.format("Total Assinados: %d\n", totalAssinados));
            relatorio.append(String.format("Espaço Total: %s\n", formatarTamanho((long) totalTamanho)));
            
            JTextArea textArea = new JTextArea(relatorio.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Relatório de Documentos", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar relatório: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
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
                    assinaturaArea.setText(documentoAtual.getAssinaturaDigital() != null ? documentoAtual.getAssinaturaDigital() : "");
                    usuarioUploadField.setText(documentoAtual.getUsuarioUpload());
                    
                    if (documentoAtual.getDataExpiracao() != null) {
                        dataExpiracaoField.setText(documentoAtual.getDataExpiracao().toString());
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
        assinarButton.setEnabled(temDocumento);
    }
    
    public void refreshData() {
        try {
            List<Documento> documentos = documentoDAO.findAll();
            tableModel.setRowCount(0);
            
            for (Documento documento : documentos) {
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
                    documento.getStatus() + (status.isEmpty() ? "" : " (" + status + ")"),
                    documento.temAssinaturaDigital() ? "Sim" : "Não"
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar documentos: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
