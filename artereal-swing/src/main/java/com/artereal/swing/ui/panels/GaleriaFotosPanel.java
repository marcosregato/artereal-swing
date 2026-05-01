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
        // Aplicar estilização padrão ao painel principal
        PadraoLayout.estilizarPainelPrincipal(this);
        
        // Header estilizado usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader("🖼️ Galeria de Fotos", "Álbum de fotos e eventos maçônicos");
        add(headerPanel, BorderLayout.NORTH);
        
        // Painel principal usando PadraoLayout
        JPanel mainPanel = PadraoLayout.criarPainelPrincipal();
        
        // Painel de busca no topo
        JTextField pesquisarField = new JTextField(20);
        JButton pesquisarButton = PadraoLayout.criarBotaoPesquisar();
        JPanel buscaPanel = PadraoLayout.criarPainelPesquisa(pesquisarField, pesquisarButton);
        mainPanel.add(buscaPanel, BorderLayout.NORTH);
        
        // Painel do conteúdo com split vertical (Formulário acima, Tabela abaixo)
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(PadraoLayout.COR_FUNDO);
        
        // Painel de formulário usando PadraoLayout
        JPanel formPanel = PadraoLayout.criarGrupoFormulario("📸 Dados da Foto");
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        
        // Formulário usando PadraoLayout
        JPanel formContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        formContent.setBackground(Color.WHITE);
        
        // SEÇÃO 1: Dados da Foto
        JPanel dadosFotoPanel = new JPanel(new BorderLayout());
        dadosFotoPanel.setBackground(Color.WHITE);
        dadosFotoPanel.setBorder(BorderFactory.createTitledBorder("📸 Dados da Foto"));
        
        JPanel dadosFotoContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        dadosFotoContent.setBackground(Color.WHITE);
        
        dadosFotoContent.add(PadraoLayout.criarLabelFormulario("Descrição:"));
        PadraoLayout.estilizarCampoDescricaoFoto(descricaoField);
        dadosFotoContent.add(descricaoField);
        
        dadosFotoContent.add(PadraoLayout.criarLabelFormulario("Categoria:"));
        PadraoLayout.estilizarComboBox(categoriaComboBox);
        dadosFotoContent.add(categoriaComboBox);
        
        dadosFotoPanel.add(dadosFotoContent, BorderLayout.CENTER);
        formContainer.add(dadosFotoPanel, BorderLayout.NORTH);
        
        // SEÇÃO 2: Arquivo da Foto
        JPanel arquivoFotoPanel = new JPanel(new BorderLayout());
        arquivoFotoPanel.setBackground(Color.WHITE);
        arquivoFotoPanel.setBorder(BorderFactory.createTitledBorder("📁 Arquivo da Foto"));
        
        JPanel arquivoFotoContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        arquivoFotoContent.setBackground(Color.WHITE);
        
        arquivoFotoContent.add(PadraoLayout.criarLabelFormulario("Caminho do Arquivo:"));
        PadraoLayout.estilizarCampoCaminhoFoto(caminhoField);
        caminhoField.setEditable(false);
        arquivoFotoContent.add(caminhoField);
        
        arquivoFotoContent.add(PadraoLayout.criarLabelFormulario("Selecionar Arquivo:"));
        arquivoFotoContent.add(selecionarArquivoButton);
        
        arquivoFotoPanel.add(arquivoFotoContent, BorderLayout.CENTER);
        formContainer.add(arquivoFotoPanel, BorderLayout.CENTER);
        
        // Painel de estatísticas no formulário
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 5));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(BorderFactory.createTitledBorder("📊 Estatísticas de Fotos"));
        
        try {
            List<Object[]> estatisticas = fotoDAO.getEstatisticas();
            int totalFotos = fotoDAO.count();
            
            // Total de fotos
            JPanel totalPanel = new JPanel(new BorderLayout());
            totalPanel.setBorder(BorderFactory.createEtchedBorder());
            JLabel totalLabel = new JLabel("Total", SwingConstants.CENTER);
            totalLabel.setForeground(Color.BLUE);
            totalPanel.add(totalLabel, BorderLayout.NORTH);
            JLabel totalValorLabel = new JLabel(String.valueOf(totalFotos), SwingConstants.CENTER);
            totalValorLabel.setFont(new Font("Arial", Font.BOLD, 16));
            totalPanel.add(totalValorLabel, BorderLayout.CENTER);
            statsPanel.add(totalPanel);
            
            // Estatísticas por categoria
            for (Object[] est : estatisticas) {
                String categoria = (String) est[0];
                int quantidade = (Integer) est[1];
                
                JPanel catPanel = new JPanel(new BorderLayout());
                catPanel.setBorder(BorderFactory.createEtchedBorder());
                
                Color color = Color.BLACK;
                switch (categoria) {
                    case "IRMAO": color = Color.BLUE; break;
                    case "LOJA": color = Color.GREEN; break;
                    case "SESSAO": color = Color.MAGENTA; break;
                    case "EVENTO": color = Color.RED; break;
                    case "DOCUMENTO": color = Color.ORANGE; break;
                    case "OUTRA": color = Color.DARK_GRAY; break;
                }
                
                JLabel catLabel = new JLabel(categoria.replace("_", " "), SwingConstants.CENTER);
                catLabel.setForeground(color);
                catLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
                catPanel.add(catLabel, BorderLayout.NORTH);
                
                JLabel quantLabel = new JLabel(String.valueOf(quantidade), SwingConstants.CENTER);
                quantLabel.setFont(new Font("Arial", Font.BOLD, 14));
                quantLabel.setForeground(color);
                catPanel.add(quantLabel, BorderLayout.CENTER);
                
                statsPanel.add(catPanel);
            }
        } catch (SQLException e) {
            statsPanel.add(new JLabel("Erro ao carregar estatísticas"));
        }
        
        // Painel de botões
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botoesPanel.setBackground(Color.WHITE);
        botoesPanel.add(salvarButton);
        botoesPanel.add(novoButton);
        botoesPanel.add(excluirButton);
        botoesPanel.add(visualizarButton);
        botoesPanel.add(selecionarArquivoButton);
        
        formContainer.add(formContent, BorderLayout.CENTER);
        formContainer.add(statsPanel, BorderLayout.NORTH);
        formContainer.add(botoesPanel, BorderLayout.SOUTH);
        
        formPanel.add(formContainer, BorderLayout.CENTER);
        
        // Painel de tabela usando PadraoLayout
        JPanel tabelaPanel = PadraoLayout.criarGrupoFormulario("🖼️ Fotos Cadastradas");
        PadraoLayout.configurarTabela(fotosTable);
        JScrollPane tableScrollPane = new JScrollPane(fotosTable);
        
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
