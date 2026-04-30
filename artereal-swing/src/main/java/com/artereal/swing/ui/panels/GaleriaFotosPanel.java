package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.FotoDAO;
import com.artereal.swing.model.Foto;

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
 * Painel de Galeria de Fotos
 */
public class GaleriaFotosPanel extends JPanel {
    
    private FotoDAO fotoDAO;
    private DefaultTableModel tableModel;
    private JTable fotosTable;
    private JTextField tituloField;
    private JTextField descricaoField;
    private JTextField caminhoField;
    private JTextField eventoField;
    private JTextField tagsField;
    private JComboBox<String> categoriaComboBox;
    private JButton salvarButton;
    private JButton novoButton;
    private JButton excluirButton;
    private JButton selecionarArquivoButton;
    private JButton visualizarButton;
    private JButton relatorioButton;
    private Foto fotoAtual;
    
    public GaleriaFotosPanel() {
        fotoDAO = new FotoDAO();
        initializeComponents();
        setupLayout();
        setupEvents();
        refreshData();
    }
    
    private void initializeComponents() {
        // Tabela
        tableModel = new DefaultTableModel(new Object[]{"ID", "Descrição", "Categoria", "Data Foto", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        fotosTable = new JTable(tableModel);
        
        // Formulário
        descricaoField = new JTextField(40);
        caminhoField = new JTextField(50);
        caminhoField.setEditable(false);
        categoriaComboBox = new JComboBox<>(new String[]{
            "IRMAO", "LOJA", "SESSAO", "EVENTO", "DOCUMENTO", "OUTRA"
        });
        
        // Botões
        salvarButton = new JButton("Salvar");
        novoButton = new JButton("Novo");
        excluirButton = new JButton("Excluir");
        selecionarArquivoButton = new JButton("Selecionar Arquivo");
        visualizarButton = new JButton("Visualizar");
        relatorioButton = new JButton("Relatório");
        
        fotoAtual = null;
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Título
        JLabel titleLabel = new JLabel("Galeria de Fotos", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Painel de estatísticas
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Estatísticas da Galeria"));
        
        try {
            List<Object[]> estatisticas = fotoDAO.getEstatisticas();
            int totalFotos = fotoDAO.count();
            
            // Total de fotos
            JPanel totalPanel = new JPanel(new BorderLayout());
            totalPanel.setBorder(BorderFactory.createEtchedBorder());
            JLabel totalLabel = new JLabel("Total de Fotos", SwingConstants.CENTER);
            totalLabel.setForeground(Color.BLUE);
            totalPanel.add(totalLabel, BorderLayout.NORTH);
            JLabel totalValorLabel = new JLabel(String.valueOf(totalFotos), SwingConstants.CENTER);
            totalValorLabel.setFont(new Font("Arial", Font.BOLD, 24));
            totalPanel.add(totalValorLabel, BorderLayout.CENTER);
            statsPanel.add(totalPanel);
            
            // Estatísticas por categoria
            for (Object[] est : estatisticas) {
                String categoria = (String) est[0];
                int quantidade = (Integer) est[1];
                double tamanho = (Double) est[2];
                
                JPanel catPanel = new JPanel(new BorderLayout());
                catPanel.setBorder(BorderFactory.createEtchedBorder());
                
                Color color = Color.BLACK;
                switch (categoria) {
                    case "SESSAO_MAGNA": color = Color.BLUE; break;
                    case "SESSAO_BRANCA": color = Color.GREEN; break;
                    case "CERIMONIA_INICIACAO": color = Color.MAGENTA; break;
                    default: color = Color.DARK_GRAY; break;
                }
                
                JLabel catLabel = new JLabel(categoria.replace("_", " "), SwingConstants.CENTER);
                catLabel.setForeground(color);
                catPanel.add(catLabel, BorderLayout.NORTH);
                
                JLabel detalhesLabel = new JLabel(String.format("%d fotos\n%.1f MB", quantidade, tamanho / (1024 * 1024)), SwingConstants.CENTER);
                detalhesLabel.setFont(new Font("Arial", Font.PLAIN, 10));
                catPanel.add(detalhesLabel, BorderLayout.CENTER);
                
                statsPanel.add(catPanel);
            }
        } catch (SQLException e) {
            statsPanel.add(new JLabel("Erro ao carregar estatísticas"));
        }
        
        // Painel de formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados da Foto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Descrição e Categoria
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        JPanel descCatPanel = new JPanel(new BorderLayout());
        descCatPanel.add(descricaoField, BorderLayout.CENTER);
        descCatPanel.add(new JLabel("Categoria:"), BorderLayout.WEST);
        descCatPanel.add(categoriaComboBox, BorderLayout.EAST);
        formPanel.add(descCatPanel, gbc);
        
        // Arquivo
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Arquivo:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        JPanel arquivoPanel = new JPanel(new BorderLayout());
        arquivoPanel.add(caminhoField, BorderLayout.CENTER);
        arquivoPanel.add(selecionarArquivoButton, BorderLayout.EAST);
        formPanel.add(arquivoPanel, gbc);
        
        // Botões do formulário
        JPanel botoesFormPanel = new JPanel(new FlowLayout());
        botoesFormPanel.add(salvarButton);
        botoesFormPanel.add(novoButton);
        botoesFormPanel.add(excluirButton);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(botoesFormPanel, gbc);
        
        // Botões de ações
        JPanel acoesPanel = new JPanel(new FlowLayout());
        acoesPanel.setBorder(BorderFactory.createTitledBorder("Ações da Foto"));
        acoesPanel.add(visualizarButton);
        acoesPanel.add(relatorioButton);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(acoesPanel, gbc);
        
        // Painel da tabela
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBorder(BorderFactory.createTitledBorder("Fotos Cadastradas"));
        tabelaPanel.add(new JScrollPane(fotosTable), BorderLayout.CENTER);
        
        // Layout principal
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formPanel, tabelaPanel);
        splitPane.setDividerLocation(350);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        add(titleLabel, BorderLayout.NORTH);
        add(statsPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.SOUTH);
    }
    
    private void setupEvents() {
        salvarButton.addActionListener(e -> salvarFoto());
        novoButton.addActionListener(e -> limparFormulario());
        excluirButton.addActionListener(e -> excluirFoto());
        selecionarArquivoButton.addActionListener(e -> selecionarArquivo());
        visualizarButton.addActionListener(e -> visualizarFoto());
        relatorioButton.addActionListener(e -> gerarRelatorio());
        
        // Seleção na tabela
        fotosTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarFotoSelecionada();
            }
        });
    }
    
    private void salvarFoto() {
        try {
            if (descricaoField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Descrição é obrigatória!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (caminhoField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecione um arquivo de imagem!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Foto foto = fotoAtual != null ? fotoAtual : new Foto();
            foto.setDescricao(descricaoField.getText().trim());
            foto.setCaminhoArquivo(caminhoField.getText().trim());
            foto.setCategoria((String) categoriaComboBox.getSelectedItem());
            foto.setDataFoto(LocalDateTime.now());
            
            fotoDAO.save(foto);
            
            JOptionPane.showMessageDialog(this, "Foto salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            refreshData();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar foto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limparFormulario() {
        fotoAtual = null;
        descricaoField.setText("");
        caminhoField.setText("");
        categoriaComboBox.setSelectedItem("IRMAO");
        descricaoField.requestFocus();
        atualizarBotoesAcao();
    }
    
    private void excluirFoto() {
        if (fotoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma foto para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente excluir esta foto?", 
            "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                fotoDAO.delete(fotoAtual.getId());
                JOptionPane.showMessageDialog(this, "Foto excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir foto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void selecionarArquivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Imagens", "jpg", "jpeg", "png", "gif", "bmp"));
        
        int resultado = fileChooser.showOpenDialog(this);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File arquivoSelecionado = fileChooser.getSelectedFile();
            caminhoField.setText(arquivoSelecionado.getAbsolutePath());
            
            // Preencher descrição com nome do arquivo se estiver vazia
            if (descricaoField.getText().trim().isEmpty()) {
                String nomeSemExtensao = arquivoSelecionado.getName();
                int pontoIndex = nomeSemExtensao.lastIndexOf('.');
                if (pontoIndex > 0) {
                    nomeSemExtensao = nomeSemExtensao.substring(0, pontoIndex);
                }
                descricaoField.setText(nomeSemExtensao);
            }
        }
    }
    
    private void visualizarFoto() {
        if (fotoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma foto para visualizar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            File arquivo = new File(fotoAtual.getCaminhoArquivo());
            if (arquivo.exists()) {
                // Abrir imagem em uma janela
                ImageIcon icon = new ImageIcon(arquivo.getAbsolutePath());
                JLabel label = new JLabel(icon);
                
                JScrollPane scrollPane = new JScrollPane(label);
                scrollPane.setPreferredSize(new Dimension(600, 400));
                
                JOptionPane.showMessageDialog(this, scrollPane, 
                    "Visualizando: " + fotoAtual.getTitulo(), JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Arquivo de imagem não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao visualizar foto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void gerarRelatorio() {
        try {
            List<Foto> fotos = fotoDAO.findAll();
            StringBuilder relatorio = new StringBuilder();
            relatorio.append("RELATÓRIO DA GALERIA DE FOTOS\n");
            relatorio.append("============================\n\n");
            
            int totalFotos = fotos.size();
            double tamanhoTotal = 0;
            
            for (Foto foto : fotos) {
                relatorio.append(String.format("Título: %s\n", foto.getTitulo()));
                relatorio.append(String.format("Categoria: %s\n", foto.getCategoriaFormatada()));
                relatorio.append(String.format("Evento: %s\n", foto.getEvento() != null ? foto.getEvento() : "N/A"));
                relatorio.append(String.format("Data: %s\n", foto.getDataFoto() != null ? foto.getDataFoto().toString() : "N/A"));
                relatorio.append(String.format("Tamanho: %s\n", foto.getTamanhoFormatado()));
                relatorio.append(String.format("Formato: %s\n", foto.getFormatoArquivo() != null ? foto.getFormatoArquivo() : "N/A"));
                relatorio.append(String.format("Tags: %s\n", foto.getTagsFormatadas()));
                relatorio.append(String.format("Arquivo: %s\n", foto.getNomeArquivo()));
                relatorio.append("------------------------\n");
                
                if (foto.getTamanhoArquivo() != null) {
                    tamanhoTotal += foto.getTamanhoArquivo();
                }
            }
            
            relatorio.append("\nRESUMO\n");
            relatorio.append("======\n");
            relatorio.append(String.format("Total de Fotos: %d\n", totalFotos));
            relatorio.append(String.format("Tamanho Total: %.1f MB\n", tamanhoTotal / (1024 * 1024)));
            relatorio.append(String.format("Tamanho Médio: %.1f KB\n", totalFotos > 0 ? (tamanhoTotal / totalFotos) / 1024 : 0));
            
            JTextArea textArea = new JTextArea(relatorio.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Relatório da Galeria", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar relatório: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void carregarFotoSelecionada() {
        int selectedRow = fotosTable.getSelectedRow();
        if (selectedRow >= 0) {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            try {
                fotoAtual = fotoDAO.findById(id);
                if (fotoAtual != null) {
                    descricaoField.setText(fotoAtual.getDescricao());
                    caminhoField.setText(fotoAtual.getCaminhoArquivo());
                    categoriaComboBox.setSelectedItem(fotoAtual.getCategoria());
                    atualizarBotoesAcao();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar foto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void atualizarBotoesAcao() {
        boolean temFoto = fotoAtual != null;
        
        visualizarButton.setEnabled(temFoto);
        excluirButton.setEnabled(temFoto);
    }
    
    private String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return (lastDot == -1) ? "" : fileName.substring(lastDot + 1).toLowerCase();
    }
    
    public void refreshData() {
        try {
            List<Foto> fotos = fotoDAO.findAll();
            tableModel.setRowCount(0);
            
            for (Foto foto : fotos) {
                Object[] row = {
                    foto.getId(),
                    foto.getDescricao() != null ? foto.getDescricao() : "Sem descrição",
                    foto.getCategoriaFormatada(),
                    foto.getDataFoto() != null ? foto.getDataFoto().toString() : "N/A",
                    foto.isAtivo() ? "Ativa" : "Inativa"
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar fotos: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
