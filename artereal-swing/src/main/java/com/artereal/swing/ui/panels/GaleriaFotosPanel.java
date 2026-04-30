package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.FotoDAO;
import com.artereal.swing.model.Foto;
import com.artereal.swing.ui.layout.PadraoLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
    private JTextField descricaoField;
    private JTextField caminhoField;
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
        
        // Botões usando PadraoLayout com cores pastéis
        salvarButton = PadraoLayout.criarBotaoSalvar();
        novoButton = PadraoLayout.criarBotaoNovo();
        excluirButton = PadraoLayout.criarBotaoExcluir();
        selecionarArquivoButton = PadraoLayout.criarBotao("Selecionar Arquivo", new Color(173, 216, 230)); // Azul pastel
        visualizarButton = PadraoLayout.criarBotao("Visualizar", new Color(255, 250, 205)); // Amarelo pastel
        relatorioButton = PadraoLayout.criarBotao("Relatório", new Color(200, 200, 255)); // Azul claro
        
        fotoAtual = null;
    }
    
    private void setupLayout() {
        // Aplicar layout padrão usando PadraoLayout
        PadraoLayout.aplicarLayoutPadrao(this);
        
        // Header estilizado usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader("�️ Galeria de Fotos", "Álbum de fotos e eventos maçônicos");
        add(headerPanel, BorderLayout.NORTH);
        
        // Painel de estatísticas
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        statsPanel.setBackground(new Color(245, 245, 250));
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 210)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
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
        
        // Painel de pesquisa
        JPanel pesquisaPanel = new JPanel(new BorderLayout());
        pesquisaPanel.setBackground(new Color(245, 245, 250));
        pesquisaPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 210)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        JPanel searchContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchContainer.setBackground(new Color(245, 245, 250));
        
        JLabel searchLabel = new JLabel("🔍 Pesquisar:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchLabel.setForeground(new Color(100, 100, 120));
        
        JTextField pesquisarField = new JTextField();
        pesquisarField.setPreferredSize(new Dimension(200, 30));
        pesquisarField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        
        JButton pesquisarButton = PadraoLayout.criarBotaoPesquisar();
        pesquisarButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        pesquisarButton.setFocusPainted(false);
        
        searchContainer.add(searchLabel);
        searchContainer.add(pesquisarField);
        searchContainer.add(pesquisarButton);
        pesquisaPanel.add(searchContainer, BorderLayout.CENTER);
        
        // Painel direito - Formulário usando PadraoLayout
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        
        // Header estilizado usando PadraoLayout
        JPanel formHeaderPanel = PadraoLayout.criarHeader("📸 Dados da Foto", "Gerencie as informações das fotos do sistema");
        rightPanel.add(formHeaderPanel, BorderLayout.NORTH);
        
        // Painel de formulário
        JPanel formularioPanel = new JPanel(new BorderLayout());
        formularioPanel.setBackground(Color.WHITE);
        formularioPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        JLabel formTitle = new JLabel("📸 Dados da Foto");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formTitle.setForeground(new Color(70, 130, 180));
        
        // Container principal para todos os grupos
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        
        // Grupo 1: Dados Básicos
        JPanel dadosBasicosPanel = createFormGroup("📅 Dados Básicos");
        JPanel dadosBasicosContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        dadosBasicosContent.setBackground(Color.WHITE);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Descrição:"));
        PadraoLayout.estilizarCampoTexto(descricaoField);
        dadosBasicosContent.add(descricaoField);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Categoria:"));
        PadraoLayout.estilizarComboBox(categoriaComboBox);
        dadosBasicosContent.add(categoriaComboBox);
        
        dadosBasicosPanel.add(dadosBasicosContent);
        
        // Grupo 2: Arquivo e Localização
        JPanel arquivoPanel = createFormGroup("📁 Arquivo e Localização");
        JPanel arquivoContent = new JPanel(new BorderLayout());
        arquivoContent.setBackground(Color.WHITE);
        
        // Painel de arquivo
        JPanel filePanel = new JPanel(PadraoLayout.criarLayoutFormulario());
        filePanel.setBackground(Color.WHITE);
        
        filePanel.add(PadraoLayout.criarLabelFormulario("Caminho:"));
        PadraoLayout.estilizarCampoTexto(caminhoField);
        JPanel pathPanel = new JPanel(new BorderLayout());
        pathPanel.add(caminhoField, BorderLayout.CENTER);
        pathPanel.add(selecionarArquivoButton, BorderLayout.EAST);
        filePanel.add(pathPanel);
        
        arquivoContent.add(filePanel, BorderLayout.CENTER);
        arquivoPanel.add(arquivoContent);
        
        // Organizar grupos verticalmente (padrão SessoesPanel)
        JPanel groupsPanel = new JPanel(new BorderLayout());
        groupsPanel.setBackground(Color.WHITE);
        
        JPanel topGroups = new JPanel(new BorderLayout());
        topGroups.setBackground(Color.WHITE);
        topGroups.add(dadosBasicosPanel, BorderLayout.NORTH);
        topGroups.add(arquivoPanel, BorderLayout.CENTER);
        
        JPanel allGroups = new JPanel(new BorderLayout());
        allGroups.setBackground(Color.WHITE);
        allGroups.add(topGroups, BorderLayout.NORTH);
        
        // Adicionar scroll ao formulário para garantir visibilidade
        JScrollPane formScroll = new JScrollPane(allGroups);
        formScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        formScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        formScroll.setBorder(BorderFactory.createEmptyBorder());
        
        formContainer.add(formScroll, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botoesPanel.setBackground(Color.WHITE);
        
        botoesPanel.add(createStyledButton("Salvar", new Color(144, 238, 144)));
        botoesPanel.add(createStyledButton("Novo", new Color(173, 216, 230)));
        botoesPanel.add(createStyledButton("Excluir", new Color(255, 182, 193)));
        botoesPanel.add(createStyledButton("Visualizar", new Color(255, 250, 205)));
        botoesPanel.add(createStyledButton("Relatório", new Color(255, 218, 185)));
        
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
        
        // Painel de pesquisa já foi definido anteriormente
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(pesquisaPanel, BorderLayout.NORTH);
        topPanel.add(statsPanel, BorderLayout.CENTER);
        
        // Tabela estilizada usando padrão PadraoLayout
        PadraoLayout.configurarTabela(fotosTable);
        JScrollPane tableScrollPane = new JScrollPane(fotosTable);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 210)));
        tableScrollPane.getViewport().setBackground(Color.WHITE);
        
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
