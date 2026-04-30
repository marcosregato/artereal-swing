package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.DocumentoDAO;
import com.artereal.swing.model.Documento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        
        // Botões
        salvarButton = new JButton("Salvar");
        novoButton = new JButton("Novo");
        excluirButton = new JButton("Excluir");
        selecionarArquivoButton = new JButton("Selecionar Arquivo");
        visualizarButton = new JButton("Visualizar");
        assinarButton = new JButton("Assinar Digitalmente");
        uploadButton = new JButton("Upload");
        relatorioButton = new JButton("Relatório");
        
        documentoAtual = null;
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Título
        JLabel titleLabel = new JLabel("Gestão Documental", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Painel de estatísticas
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Estatísticas de Documentos"));
        
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
        
        // Painel de formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados do Documento"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Nome do Arquivo
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nome Arquivo:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        JPanel nomeArquivoPanel = new JPanel(new BorderLayout());
        nomeArquivoPanel.add(nomeArquivoField, BorderLayout.CENTER);
        nomeArquivoPanel.add(selecionarArquivoButton, BorderLayout.EAST);
        formPanel.add(nomeArquivoPanel, gbc);
        
        // Caminho do Arquivo
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Caminho:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        formPanel.add(caminhoArquivoField, gbc);
        
        // Tipo e Descrição
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        JPanel tipoDescricaoPanel = new JPanel(new BorderLayout());
        tipoDescricaoPanel.add(tipoComboBox, BorderLayout.WEST);
        tipoDescricaoPanel.add(new JLabel("Descrição:"), BorderLayout.CENTER);
        tipoDescricaoPanel.add(descricaoField, BorderLayout.EAST);
        formPanel.add(tipoDescricaoPanel, gbc);
        
        // Data de Expiração
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Data Expiração:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        JPanel expiracaoUsuarioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        expiracaoUsuarioPanel.add(dataExpiracaoField);
        expiracaoUsuarioPanel.add(new JLabel("Upload por:"));
        expiracaoUsuarioPanel.add(usuarioUploadField);
        formPanel.add(expiracaoUsuarioPanel, gbc);
        
        // Tamanho e Formato
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Informações:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        JPanel tamanhoFormatoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tamanhoFormatoPanel.add(new JLabel("Tamanho:"));
        tamanhoFormatoPanel.add(tamanhoField);
        tamanhoFormatoPanel.add(new JLabel("Formato:"));
        tamanhoFormatoPanel.add(formatoField);
        formPanel.add(tamanhoFormatoPanel, gbc);
        
        // Hash do Arquivo
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Hash SHA-256:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        formPanel.add(new JScrollPane(hashArea), gbc);
        
        // Assinatura Digital
        gbc.gridx = 0; gbc.gridy = 6; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Assinatura Digital:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        formPanel.add(new JScrollPane(assinaturaArea), gbc);
        
        // Botões do formulário
        JPanel botoesFormPanel = new JPanel(new FlowLayout());
        botoesFormPanel.add(salvarButton);
        botoesFormPanel.add(novoButton);
        botoesFormPanel.add(excluirButton);
        
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(botoesFormPanel, gbc);
        
        // Botões de ações
        JPanel acoesPanel = new JPanel(new FlowLayout());
        acoesPanel.setBorder(BorderFactory.createTitledBorder("Ações do Documento"));
        acoesPanel.add(uploadButton);
        acoesPanel.add(visualizarButton);
        acoesPanel.add(assinarButton);
        acoesPanel.add(relatorioButton);
        
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(acoesPanel, gbc);
        
        // Painel da tabela
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBorder(BorderFactory.createTitledBorder("Documentos Cadastrados"));
        tabelaPanel.add(new JScrollPane(documentosTable), BorderLayout.CENTER);
        
        // Layout principal
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formPanel, tabelaPanel);
        splitPane.setDividerLocation(450);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(splitPane, BorderLayout.CENTER);
        centerPanel.add(alertasPanel, BorderLayout.SOUTH);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(statsPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        add(titleLabel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
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
