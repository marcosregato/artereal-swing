package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.BibliotecaDAO;
import com.artereal.swing.model.Biblioteca;
import com.artereal.swing.ui.components.MasonicLogo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Painel de gestão da Biblioteca
 */
public class BibliotecaPanel extends JPanel {
    
    private static final Logger logger = LoggerFactory.getLogger(BibliotecaPanel.class);
    private BibliotecaDAO bibliotecaDAO;
    private DefaultTableModel tableModel;
    private JTable bibliotecaTable;
    private Biblioteca bibliotecaAtual;
    
    // Formulário
    private JTextField codigoField;
    private JComboBox<String> tipoCombo;
    private JTextField tituloField;
    private JTextField autorField;
    private JTextField isbnField;
    private JTextField editoraField;
    private JTextField anoPublicacaoField;
    private JTextField categoriaField;
    private JTextField localizacaoField;
    private JComboBox<String> statusCombo;
    
    // Campos para empréstimo
    private JTextField nomeLeitorField;
    private JTextField dataEmprestimoField;
    private JTextField dataDevolucaoPrevistaField;
    private JTextField dataDevolucaoRealField;
    private JTextField responsavelEmprestimoField;
    private JTextField multaField;
    
    private JTextArea observacoesArea;
    
    // Botões
    private JButton salvarButton;
    private JButton novoButton;
    private JButton editarButton;
    private JButton excluirButton;
    private JButton limparButton;
    private JButton pesquisarButton;
    private JButton emprestarButton;
    private JButton devolverButton;
    private JTextField pesquisarField;
    
    // Labels de estatísticas
    private JLabel totalLivrosLabel;
    private JLabel livrosDisponiveisLabel;
    private JLabel emprestimosAtivosLabel;
    private JLabel emprestimosAtrasadosLabel;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public BibliotecaPanel() {
        bibliotecaDAO = new BibliotecaDAO();
        initializeComponents();
        setupLayout();
        setupEvents();
        refreshData();
        atualizarEstatisticas();
    }
    
    private void initializeComponents() {
        // Tabela
        tableModel = new DefaultTableModel(new Object[]{
            "Código", "Tipo", "Título", "Autor", "Status", "Leitor"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bibliotecaTable = new JTable(tableModel);
        
        // Formulário
        codigoField = new JTextField(10);
        codigoField.setEditable(false);
        
        tipoCombo = new JComboBox<>(new String[]{"LIVRO", "EMPRESTIMO"});
        tituloField = new JTextField(40);
        autorField = new JTextField(30);
        isbnField = new JTextField(15);
        editoraField = new JTextField(25);
        anoPublicacaoField = new JTextField(6);
        categoriaField = new JTextField(20);
        localizacaoField = new JTextField(15);
        
        statusCombo = new JComboBox<>(new String[]{
            "DISPONIVEL", "EMPRESTADO", "EM_MANUTENCAO", "BAIXADO"
        });
        
        // Campos para empréstimo
        nomeLeitorField = new JTextField(30);
        dataEmprestimoField = new JTextField(10);
        dataDevolucaoPrevistaField = new JTextField(10);
        dataDevolucaoRealField = new JTextField(10);
        responsavelEmprestimoField = new JTextField(25);
        multaField = new JTextField(10);
        
        observacoesArea = new JTextArea(3, 40);
        observacoesArea.setLineWrap(true);
        observacoesArea.setWrapStyleWord(true);
        
        // Botões
        salvarButton = new JButton("Salvar");
        novoButton = new JButton("Novo");
        editarButton = new JButton("Editar");
        excluirButton = new JButton("Excluir");
        limparButton = new JButton("Limpar");
        pesquisarButton = new JButton("Pesquisar");
        emprestarButton = new JButton("Emprestar");
        devolverButton = new JButton("Devolver");
        pesquisarField = new JTextField(20);
        
        // Labels de estatísticas
        totalLivrosLabel = new JLabel("0");
        livrosDisponiveisLabel = new JLabel("0");
        emprestimosAtivosLabel = new JLabel("0");
        emprestimosAtrasadosLabel = new JLabel("0");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 245));
        
        // Header com título
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(139, 69, 19)); // Marrom biblioteca
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("📚 Gestão da Biblioteca", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Catálogo de livros, empréstimos e controle de acervo", SwingConstants.LEFT);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(220, 220, 230));
        
        // Adicionar logo maçônico discreto
        JLabel logoLabel = MasonicLogo.createLogoLabel(32);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        JPanel titleContainer = new JPanel(new GridLayout(2, 1, 0, 5));
        titleContainer.setBackground(new Color(139, 69, 19));
        titleContainer.add(titleLabel);
        titleContainer.add(subtitleLabel);
        
        JPanel headerContent = new JPanel(new BorderLayout());
        headerContent.setBackground(new Color(139, 69, 19));
        headerContent.add(logoLabel, BorderLayout.WEST);
        headerContent.add(titleContainer, BorderLayout.CENTER);
        
        headerPanel.add(headerContent, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);
        
        // Painel principal com split
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(650);
        splitPane.setResizeWeight(0.6);
        
        // Painel esquerdo - Tabela e estatísticas
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Painel de pesquisa melhorado
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
        
        pesquisarField.setPreferredSize(new Dimension(200, 30));
        pesquisarField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        
        pesquisarButton.setBackground(new Color(221, 160, 221)); // Lila pastel suave
        pesquisarButton.setForeground(new Color(102, 51, 153));
        pesquisarButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pesquisarButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(221, 160, 221), 2),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        pesquisarButton.setFocusPainted(false);
        
        searchContainer.add(searchLabel);
        searchContainer.add(pesquisarField);
        searchContainer.add(pesquisarButton);
        pesquisaPanel.add(searchContainer, BorderLayout.CENTER);
        
        // Painel de estatísticas melhorado
        JPanel estatisticasPanel = new JPanel(new GridLayout(2, 2, 15, 10));
        estatisticasPanel.setBackground(new Color(245, 245, 250));
        estatisticasPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 210)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        // Card Total Livros
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBackground(new Color(230, 240, 255));
        totalPanel.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        JLabel totalTitleLabel = new JLabel("📖 Total Livros", SwingConstants.CENTER);
        totalTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        totalTitleLabel.setForeground(new Color(70, 130, 180));
        totalLivrosLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalLivrosLabel.setForeground(new Color(70, 130, 180));
        totalPanel.add(totalTitleLabel, BorderLayout.NORTH);
        totalPanel.add(totalLivrosLabel, BorderLayout.CENTER);
        
        // Card Disponíveis
        JPanel disponiveisPanel = new JPanel(new BorderLayout());
        disponiveisPanel.setBackground(new Color(220, 255, 220));
        disponiveisPanel.setBorder(BorderFactory.createLineBorder(new Color(46, 125, 50), 2));
        JLabel disponiveisTitleLabel = new JLabel("✅ Disponíveis", SwingConstants.CENTER);
        disponiveisTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        disponiveisTitleLabel.setForeground(new Color(46, 125, 50));
        livrosDisponiveisLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        livrosDisponiveisLabel.setForeground(new Color(46, 125, 50));
        disponiveisPanel.add(disponiveisTitleLabel, BorderLayout.NORTH);
        disponiveisPanel.add(livrosDisponiveisLabel, BorderLayout.CENTER);
        
        // Card Empréstimos Ativos
        JPanel ativosPanel = new JPanel(new BorderLayout());
        ativosPanel.setBackground(new Color(255, 248, 220));
        ativosPanel.setBorder(BorderFactory.createLineBorder(new Color(255, 193, 7), 2));
        JLabel ativosTitleLabel = new JLabel("📚 Empréstimos Ativos", SwingConstants.CENTER);
        ativosTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        ativosTitleLabel.setForeground(new Color(255, 193, 7));
        emprestimosAtivosLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        emprestimosAtivosLabel.setForeground(new Color(255, 193, 7));
        ativosPanel.add(ativosTitleLabel, BorderLayout.NORTH);
        ativosPanel.add(emprestimosAtivosLabel, BorderLayout.CENTER);
        
        // Card Empréstimos Atrasados
        JPanel atrasadosPanel = new JPanel(new BorderLayout());
        atrasadosPanel.setBackground(new Color(255, 220, 220));
        atrasadosPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 53, 69), 2));
        JLabel atrasadosTitleLabel = new JLabel("⚠️ Atrasados", SwingConstants.CENTER);
        atrasadosTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        atrasadosTitleLabel.setForeground(new Color(220, 53, 69));
        emprestimosAtrasadosLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        emprestimosAtrasadosLabel.setForeground(new Color(220, 53, 69));
        atrasadosPanel.add(atrasadosTitleLabel, BorderLayout.NORTH);
        atrasadosPanel.add(emprestimosAtrasadosLabel, BorderLayout.CENTER);
        
        estatisticasPanel.add(totalPanel);
        estatisticasPanel.add(disponiveisPanel);
        estatisticasPanel.add(ativosPanel);
        estatisticasPanel.add(atrasadosPanel);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(pesquisaPanel, BorderLayout.NORTH);
        topPanel.add(estatisticasPanel, BorderLayout.CENTER);
        
        // Tabela estilizada
        JScrollPane tableScrollPane = new JScrollPane(bibliotecaTable);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 210)));
        tableScrollPane.getViewport().setBackground(Color.WHITE);
        
        bibliotecaTable.setRowHeight(25);
        bibliotecaTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        bibliotecaTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        bibliotecaTable.getTableHeader().setBackground(new Color(139, 69, 19));
        bibliotecaTable.getTableHeader().setForeground(Color.WHITE);
        bibliotecaTable.setSelectionBackground(new Color(255, 218, 185));
        bibliotecaTable.setSelectionForeground(Color.BLACK);
        
        leftPanel.add(topPanel, BorderLayout.NORTH);
        leftPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Painel direito - Formulário
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        
        // Título do formulário
        JPanel formHeaderPanel = new JPanel(new BorderLayout());
        formHeaderPanel.setBackground(new Color(245, 245, 250));
        formHeaderPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel formTitleLabel = new JLabel("📝 Detalhes do Livro", SwingConstants.LEFT);
        formTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitleLabel.setForeground(new Color(139, 69, 19));
        
        formHeaderPanel.add(formTitleLabel, BorderLayout.CENTER);
        rightPanel.add(formHeaderPanel, BorderLayout.NORTH);
        
        // Formulário em grid
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Estilizar labels
        Font labelFont = new Font("Segoe UI", Font.BOLD, 12);
        Color labelColor = new Color(139, 69, 19);
        
        // Código
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        JLabel codigoLabel = new JLabel("🔢 Código:");
        codigoLabel.setFont(labelFont);
        codigoLabel.setForeground(labelColor);
        formPanel.add(codigoLabel, gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        codigoField.setEditable(false);
        codigoField.setBackground(new Color(240, 240, 245));
        codigoField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(codigoField, gbc);
        
        // Tipo
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel tipoLabel = new JLabel("📂 Tipo:");
        tipoLabel.setFont(labelFont);
        tipoLabel.setForeground(labelColor);
        formPanel.add(tipoLabel, gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        tipoCombo.setBackground(Color.WHITE);
        tipoCombo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(tipoCombo, gbc);
        
        // Título
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        JLabel tituloLabel = new JLabel("📖 Título:");
        tituloLabel.setFont(labelFont);
        tituloLabel.setForeground(labelColor);
        formPanel.add(tituloLabel, gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        tituloField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(tituloField, gbc);
        
        // Autor
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        JLabel autorLabel = new JLabel("✍️ Autor:");
        autorLabel.setFont(labelFont);
        autorLabel.setForeground(labelColor);
        formPanel.add(autorLabel, gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        autorField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(autorField, gbc);
        
        // ISBN
        gbc.gridx = 3; gbc.gridy = 3; gbc.gridwidth = 1;
        JLabel isbnLabel = new JLabel("📋 ISBN:");
        isbnLabel.setFont(labelFont);
        isbnLabel.setForeground(labelColor);
        formPanel.add(isbnLabel, gbc);
        gbc.gridx = 4;
        isbnField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(isbnField, gbc);
        
        // Editora
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1;
        JLabel editoraLabel = new JLabel("🏢 Editora:");
        editoraLabel.setFont(labelFont);
        editoraLabel.setForeground(labelColor);
        formPanel.add(editoraLabel, gbc);
        gbc.gridx = 1;
        editoraField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(editoraField, gbc);
        
        // Ano Publicação
        gbc.gridx = 2; gbc.gridy = 4; gbc.gridwidth = 1;
        JLabel anoLabel = new JLabel("📅 Ano:");
        anoLabel.setFont(labelFont);
        anoLabel.setForeground(labelColor);
        formPanel.add(anoLabel, gbc);
        gbc.gridx = 3;
        anoPublicacaoField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(anoPublicacaoField, gbc);
        
        // Categoria
        gbc.gridx = 4; gbc.gridy = 4; gbc.gridwidth = 1;
        JLabel categoriaLabel = new JLabel("🏷️ Categoria:");
        categoriaLabel.setFont(labelFont);
        categoriaLabel.setForeground(labelColor);
        formPanel.add(categoriaLabel, gbc);
        gbc.gridx = 5;
        categoriaField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(categoriaField, gbc);
        
        // Localização
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1;
        JLabel localizacaoLabel = new JLabel("📍 Localização:");
        localizacaoLabel.setFont(labelFont);
        localizacaoLabel.setForeground(labelColor);
        formPanel.add(localizacaoLabel, gbc);
        gbc.gridx = 1;
        localizacaoField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(localizacaoField, gbc);
        
        // Status
        gbc.gridx = 2; gbc.gridy = 5; gbc.gridwidth = 1;
        JLabel statusLabel = new JLabel("✅ Status:");
        statusLabel.setFont(labelFont);
        statusLabel.setForeground(labelColor);
        formPanel.add(statusLabel, gbc);
        gbc.gridx = 3;
        statusCombo.setBackground(Color.WHITE);
        statusCombo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(statusCombo, gbc);
        
        // Campos para empréstimo
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 6; gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel emprestimoTitleLabel = new JLabel("📚 DADOS DE EMPRÉSTIMO", SwingConstants.CENTER);
        emprestimoTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        emprestimoTitleLabel.setForeground(new Color(139, 69, 19));
        emprestimoTitleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        formPanel.add(emprestimoTitleLabel, gbc);
        
        gbc.gridy = 7; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel leitorLabel = new JLabel("👤 Nome Leitor:");
        leitorLabel.setFont(labelFont);
        leitorLabel.setForeground(labelColor);
        formPanel.add(leitorLabel, gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        nomeLeitorField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(nomeLeitorField, gbc);
        
        gbc.gridx = 4; gbc.gridy = 7; gbc.gridwidth = 1;
        JLabel dataEmprestimoLabel = new JLabel("📆 Data Empréstimo:");
        dataEmprestimoLabel.setFont(labelFont);
        dataEmprestimoLabel.setForeground(labelColor);
        formPanel.add(dataEmprestimoLabel, gbc);
        gbc.gridx = 5;
        dataEmprestimoField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(dataEmprestimoField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 8;
        JLabel devolucaoPrevistaLabel = new JLabel("📅 Devolução Prevista:");
        devolucaoPrevistaLabel.setFont(labelFont);
        devolucaoPrevistaLabel.setForeground(labelColor);
        formPanel.add(devolucaoPrevistaLabel, gbc);
        gbc.gridx = 1;
        dataDevolucaoPrevistaField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(dataDevolucaoPrevistaField, gbc);
        
        gbc.gridx = 2; gbc.gridy = 8;
        JLabel devolucaoRealLabel = new JLabel("✅ Devolução Real:");
        devolucaoRealLabel.setFont(labelFont);
        devolucaoRealLabel.setForeground(labelColor);
        formPanel.add(devolucaoRealLabel, gbc);
        gbc.gridx = 3;
        dataDevolucaoRealField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(dataDevolucaoRealField, gbc);
        
        gbc.gridx = 4; gbc.gridy = 8; gbc.gridwidth = 1;
        JLabel responsavelLabel = new JLabel("👨‍💼 Responsável:");
        responsavelLabel.setFont(labelFont);
        responsavelLabel.setForeground(labelColor);
        formPanel.add(responsavelLabel, gbc);
        gbc.gridx = 5;
        responsavelEmprestimoField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(responsavelEmprestimoField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 1;
        JLabel multaLabel = new JLabel("💰 Multa (R$):");
        multaLabel.setFont(labelFont);
        multaLabel.setForeground(labelColor);
        formPanel.add(multaLabel, gbc);
        gbc.gridx = 1;
        multaField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(multaField, gbc);
        
        // Observações
        gbc.gridx = 0; gbc.gridy = 10; gbc.gridwidth = 1;
        JLabel observacoesLabel = new JLabel("📝 Observações:");
        observacoesLabel.setFont(labelFont);
        observacoesLabel.setForeground(labelColor);
        formPanel.add(observacoesLabel, gbc);
        gbc.gridx = 1; gbc.gridwidth = 5; gbc.gridy = 11;
        gbc.fill = GridBagConstraints.BOTH;
        observacoesArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        JScrollPane observacoesScroll = new JScrollPane(observacoesArea);
        observacoesScroll.setPreferredSize(new Dimension(400, 60));
        formPanel.add(observacoesScroll, gbc);
        
        // Botões do formulário estilizados
        gbc.gridx = 0; gbc.gridy = 12; gbc.gridwidth = 6; gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel botoesFormPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 15));
        botoesFormPanel.setBackground(Color.WHITE);
        
        // Estilizar botões com cores pastéis
        salvarButton.setBackground(new Color(144, 238, 144)); // Verde pastel suave
        salvarButton.setForeground(new Color(34, 89, 34));
        salvarButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        salvarButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(144, 238, 144), 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        salvarButton.setFocusPainted(false);
        salvarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        novoButton.setBackground(new Color(173, 216, 230)); // Azul pastel suave
        novoButton.setForeground(new Color(25, 84, 123));
        novoButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        novoButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(173, 216, 230), 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        novoButton.setFocusPainted(false);
        novoButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        editarButton.setBackground(new Color(255, 239, 213)); // Amarelo pastel suave
        editarButton.setForeground(new Color(180, 140, 45));
        editarButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        editarButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 239, 213), 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        editarButton.setFocusPainted(false);
        editarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        excluirButton.setBackground(new Color(255, 182, 193)); // Rosa pastel suave
        excluirButton.setForeground(new Color(180, 82, 92));
        excluirButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        excluirButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 182, 193), 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        excluirButton.setFocusPainted(false);
        excluirButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        limparButton.setBackground(new Color(211, 211, 211)); // Cinza pastel suave
        limparButton.setForeground(new Color(84, 84, 84));
        limparButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        limparButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(211, 211, 211), 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        limparButton.setFocusPainted(false);
        limparButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        emprestarButton.setBackground(new Color(176, 224, 230)); // Azul-bebê pastel
        emprestarButton.setForeground(new Color(38, 84, 124));
        emprestarButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        emprestarButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(176, 224, 230), 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        emprestarButton.setFocusPainted(false);
        emprestarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        devolverButton.setBackground(new Color(152, 251, 152)); // Verde-claro pastel
        devolverButton.setForeground(new Color(34, 139, 34));
        devolverButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        devolverButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(152, 251, 152), 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        devolverButton.setFocusPainted(false);
        devolverButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        botoesFormPanel.add(salvarButton);
        botoesFormPanel.add(novoButton);
        botoesFormPanel.add(editarButton);
        botoesFormPanel.add(excluirButton);
        botoesFormPanel.add(limparButton);
        botoesFormPanel.add(emprestarButton);
        botoesFormPanel.add(devolverButton);
        
        formPanel.add(botoesFormPanel, gbc);
        
        rightPanel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        
        // Configurar split pane
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        splitPane.setDividerLocation(650);
        splitPane.setResizeWeight(0.6);
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private void setupEvents() {
        salvarButton.addActionListener(e -> salvarItem());
        novoButton.addActionListener(e -> limparFormulario());
        editarButton.addActionListener(e -> carregarItemSelecionado());
        excluirButton.addActionListener(e -> excluirItem());
        limparButton.addActionListener(e -> limparFormulario());
        pesquisarButton.addActionListener(e -> pesquisarItens());
        emprestarButton.addActionListener(e -> emprestarLivro());
        devolverButton.addActionListener(e -> devolverLivro());
        
        // Seleção na tabela
        bibliotecaTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && bibliotecaTable.getSelectedRow() >= 0) {
                carregarItemSelecionado();
            }
        });
        
        // Duplo clique para editar
        bibliotecaTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    carregarItemSelecionado();
                }
            }
        });
        
        // Mudança de tipo
        tipoCombo.addActionListener(e -> atualizarCamposPorTipo());
    }
    
    private void atualizarCamposPorTipo() {
        String tipo = (String) tipoCombo.getSelectedItem();
        boolean isLivro = "LIVRO".equals(tipo);
        
        // Habilitar/desabilitar campos
        autorField.setEnabled(isLivro);
        isbnField.setEnabled(isLivro);
        editoraField.setEnabled(isLivro);
        anoPublicacaoField.setEnabled(isLivro);
        categoriaField.setEnabled(isLivro);
        localizacaoField.setEnabled(isLivro);
        
        nomeLeitorField.setEnabled(!isLivro);
        dataEmprestimoField.setEnabled(!isLivro);
        dataDevolucaoPrevistaField.setEnabled(!isLivro);
        dataDevolucaoRealField.setEnabled(!isLivro);
        responsavelEmprestimoField.setEnabled(!isLivro);
        multaField.setEnabled(!isLivro);
        
        // Limpar campos não aplicáveis
        if (isLivro) {
            nomeLeitorField.setText("");
            dataEmprestimoField.setText("");
            dataDevolucaoPrevistaField.setText("");
            dataDevolucaoRealField.setText("");
            responsavelEmprestimoField.setText("");
            multaField.setText("0.00");
        } else {
            autorField.setText("");
            isbnField.setText("");
            editoraField.setText("");
            anoPublicacaoField.setText("");
            categoriaField.setText("");
            localizacaoField.setText("");
        }
    }
    
    private void salvarItem() {
        try {
            if (tituloField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Título é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Biblioteca biblioteca = bibliotecaAtual != null ? bibliotecaAtual : new Biblioteca();
            biblioteca.setTipo((String) tipoCombo.getSelectedItem());
            biblioteca.setTitulo(tituloField.getText().trim());
            biblioteca.setAutor(autorField.getText().trim());
            biblioteca.setIsbn(isbnField.getText().trim());
            biblioteca.setEditora(editoraField.getText().trim());
            biblioteca.setAnoPublicacao(anoPublicacaoField.getText().trim());
            biblioteca.setCategoria(categoriaField.getText().trim());
            biblioteca.setLocalizacao(localizacaoField.getText().trim());
            biblioteca.setStatus((String) statusCombo.getSelectedItem());
            biblioteca.setObservacoes(observacoesArea.getText().trim());
            
            // Dados de empréstimo
            biblioteca.setNomeLeitor(nomeLeitorField.getText().trim());
            
            if (!dataEmprestimoField.getText().trim().isEmpty()) {
                try {
                    biblioteca.setDataEmprestimo(LocalDate.parse(dataEmprestimoField.getText().trim(), DATE_FORMATTER));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Data de empréstimo inválida! Use formato dd/MM/yyyy", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            if (!dataDevolucaoPrevistaField.getText().trim().isEmpty()) {
                try {
                    biblioteca.setDataDevolucaoPrevista(LocalDate.parse(dataDevolucaoPrevistaField.getText().trim(), DATE_FORMATTER));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Data de devolução prevista inválida! Use formato dd/MM/yyyy", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            if (!dataDevolucaoRealField.getText().trim().isEmpty()) {
                try {
                    biblioteca.setDataDevolucaoReal(LocalDate.parse(dataDevolucaoRealField.getText().trim(), DATE_FORMATTER));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Data de devolução real inválida! Use formato dd/MM/yyyy", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            biblioteca.setResponsavelEmprestimo(responsavelEmprestimoField.getText().trim());
            
            try {
                String multaStr = multaField.getText().trim().replace("R$", "").replace(",", ".");
                biblioteca.setMulta(new BigDecimal(multaStr));
            } catch (NumberFormatException e) {
                biblioteca.setMulta(BigDecimal.ZERO);
            }
            
            bibliotecaDAO.save(biblioteca);
            
            JOptionPane.showMessageDialog(this, "Item salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            refreshData();
            atualizarEstatisticas();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar item: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limparFormulario() {
        bibliotecaAtual = null;
        codigoField.setText("");
        tipoCombo.setSelectedIndex(0);
        tituloField.setText("");
        autorField.setText("");
        isbnField.setText("");
        editoraField.setText("");
        anoPublicacaoField.setText("");
        categoriaField.setText("");
        localizacaoField.setText("");
        statusCombo.setSelectedIndex(0);
        nomeLeitorField.setText("");
        dataEmprestimoField.setText("");
        dataDevolucaoPrevistaField.setText("");
        dataDevolucaoRealField.setText("");
        responsavelEmprestimoField.setText("");
        multaField.setText("0.00");
        observacoesArea.setText("");
        tituloField.requestFocus();
        atualizarCamposPorTipo();
        atualizarBotoesAcao();
    }
    
    private void excluirItem() {
        if (bibliotecaAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um item para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int option = JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente excluir " + bibliotecaAtual.getTipo() + ": " + bibliotecaAtual.getTitulo() + "?",
            "Confirmar Exclusão",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (option == JOptionPane.YES_OPTION) {
            try {
                bibliotecaDAO.delete(bibliotecaAtual.getId());
                JOptionPane.showMessageDialog(this, "Item excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                refreshData();
                atualizarEstatisticas();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir item: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void pesquisarItens() {
        try {
            String termo = pesquisarField.getText().trim();
            if (termo.isEmpty()) {
                refreshData();
                return;
            }
            
            List<Biblioteca> itens = bibliotecaDAO.findAll();
            tableModel.setRowCount(0);
            
            for (Biblioteca biblioteca : itens) {
                if (biblioteca.getTitulo().toLowerCase().contains(termo.toLowerCase()) ||
                    biblioteca.getAutor().toLowerCase().contains(termo.toLowerCase()) ||
                    biblioteca.getNomeLeitor().toLowerCase().contains(termo.toLowerCase())) {
                    Object[] row = {
                        biblioteca.getId(),
                        biblioteca.getTipo(),
                        biblioteca.getTitulo(),
                        biblioteca.getAutor(),
                        biblioteca.getStatus(),
                        biblioteca.getNomeLeitor()
                    };
                    tableModel.addRow(row);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao pesquisar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void carregarItemSelecionado() {
        int selectedRow = bibliotecaTable.getSelectedRow();
        if (selectedRow >= 0) {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            try {
                bibliotecaAtual = bibliotecaDAO.findById(id);
                if (bibliotecaAtual != null) {
                    codigoField.setText(String.valueOf(bibliotecaAtual.getId()));
                    tipoCombo.setSelectedItem(bibliotecaAtual.getTipo());
                    tituloField.setText(bibliotecaAtual.getTitulo());
                    autorField.setText(bibliotecaAtual.getAutor());
                    isbnField.setText(bibliotecaAtual.getIsbn());
                    editoraField.setText(bibliotecaAtual.getEditora());
                    anoPublicacaoField.setText(bibliotecaAtual.getAnoPublicacao());
                    categoriaField.setText(bibliotecaAtual.getCategoria());
                    localizacaoField.setText(bibliotecaAtual.getLocalizacao());
                    statusCombo.setSelectedItem(bibliotecaAtual.getStatus());
                    nomeLeitorField.setText(bibliotecaAtual.getNomeLeitor());
                    dataEmprestimoField.setText(bibliotecaAtual.getDataEmprestimo() != null ? bibliotecaAtual.getDataEmprestimo().format(DATE_FORMATTER) : "");
                    dataDevolucaoPrevistaField.setText(bibliotecaAtual.getDataDevolucaoPrevista() != null ? bibliotecaAtual.getDataDevolucaoPrevista().format(DATE_FORMATTER) : "");
                    dataDevolucaoRealField.setText(bibliotecaAtual.getDataDevolucaoReal() != null ? bibliotecaAtual.getDataDevolucaoReal().format(DATE_FORMATTER) : "");
                    responsavelEmprestimoField.setText(bibliotecaAtual.getResponsavelEmprestimo());
                    multaField.setText(bibliotecaAtual.getMulta() != null ? bibliotecaAtual.getMulta().toString() : "0.00");
                    observacoesArea.setText(bibliotecaAtual.getObservacoes());
                    atualizarCamposPorTipo();
                    atualizarBotoesAcao();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar item: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void emprestarLivro() {
        if (bibliotecaAtual == null || !"LIVRO".equals(bibliotecaAtual.getTipo())) {
            JOptionPane.showMessageDialog(this, "Selecione um livro para emprestar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!"DISPONIVEL".equals(bibliotecaAtual.getStatus())) {
            JOptionPane.showMessageDialog(this, "Este livro não está disponível para empréstimo!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Criar diálogo para empréstimo
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Emprestar Livro", true);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        JTextField leitorField = new JTextField(30);
        JTextField dataPrevField = new JTextField(10);
        JTextField responsavelField = new JTextField(25);
        
        dataPrevField.setText(LocalDate.now().plusDays(15).format(DATE_FORMATTER));
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nome Leitor:"), gbc);
        gbc.gridx = 1;
        formPanel.add(leitorField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Devolução Prevista (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1;
        formPanel.add(dataPrevField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Responsável:"), gbc);
        gbc.gridx = 1;
        formPanel.add(responsavelField, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton confirmarButton = new JButton("Confirmar");
        JButton cancelarButton = new JButton("Cancelar");
        
        buttonPanel.add(confirmarButton);
        buttonPanel.add(cancelarButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        confirmarButton.addActionListener(e -> {
            if (leitorField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Nome do leitor é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                // Criar registro de empréstimo
                Biblioteca emprestimo = new Biblioteca("EMPRESTIMO", bibliotecaAtual.getTitulo());
                emprestimo.setNomeLeitor(leitorField.getText().trim());
                emprestimo.setDataEmprestimo(LocalDate.now());
                emprestimo.setDataDevolucaoPrevista(LocalDate.parse(dataPrevField.getText().trim(), DATE_FORMATTER));
                emprestimo.setResponsavelEmprestimo(responsavelField.getText().trim());
                
                bibliotecaDAO.save(emprestimo);
                
                // Atualizar status do livro
                bibliotecaAtual.setStatus("EMPRESTADO");
                bibliotecaDAO.save(bibliotecaAtual);
                
                JOptionPane.showMessageDialog(dialog, "Empréstimo realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                refreshData();
                atualizarEstatisticas();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro ao realizar empréstimo: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelarButton.addActionListener(e -> dialog.dispose());
        
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void devolverLivro() {
        if (bibliotecaAtual == null || !"EMPRESTIMO".equals(bibliotecaAtual.getTipo())) {
            JOptionPane.showMessageDialog(this, "Selecione um empréstimo para devolver!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Calcular multa se estiver atrasado
            BigDecimal multa = BigDecimal.ZERO;
            if (bibliotecaAtual.getDataDevolucaoPrevista() != null && 
                LocalDate.now().isAfter(bibliotecaAtual.getDataDevolucaoPrevista())) {
                
                long diasAtraso = ChronoUnit.DAYS.between(bibliotecaAtual.getDataDevolucaoPrevista(), LocalDate.now());
                multa = new BigDecimal(diasAtraso * 2.0); // R$ 2,00 por dia de atraso
            }
            
            bibliotecaAtual.setDataDevolucaoReal(LocalDate.now());
            bibliotecaAtual.setStatus("DEVOLVIDO");
            
            if (multa.compareTo(BigDecimal.ZERO) > 0) {
                bibliotecaAtual.setMulta(multa);
                JOptionPane.showMessageDialog(this, 
                    "Devolução registrada com multa de R$ " + String.format("%.2f", multa), 
                    "Multa Aplicada", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Devolução registrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
            
            bibliotecaDAO.save(bibliotecaAtual);
            
            // Encontrar e atualizar o livro correspondente
            List<Biblioteca> livros = bibliotecaDAO.findByTipo("LIVRO");
            for (Biblioteca livro : livros) {
                if (livro.getTitulo().equals(bibliotecaAtual.getTitulo()) && "EMPRESTADO".equals(livro.getStatus())) {
                    livro.setStatus("DISPONIVEL");
                    bibliotecaDAO.save(livro);
                    break;
                }
            }
            
            limparFormulario();
            refreshData();
            atualizarEstatisticas();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao registrar devolução: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void atualizarBotoesAcao() {
        boolean temItem = bibliotecaAtual != null;
        editarButton.setEnabled(temItem);
        excluirButton.setEnabled(temItem);
        
        boolean isLivro = temItem && "LIVRO".equals(bibliotecaAtual.getTipo());
        boolean isEmprestimo = temItem && "EMPRESTIMO".equals(bibliotecaAtual.getTipo());
        
        emprestarButton.setEnabled(isLivro && "DISPONIVEL".equals(bibliotecaAtual.getStatus()));
        devolverButton.setEnabled(isEmprestimo && !"DEVOLVIDO".equals(bibliotecaAtual.getStatus()));
    }
    
    private void atualizarEstatisticas() {
        try {
            int totalLivros = bibliotecaDAO.countLivros();
            int emprestimosAtivos = bibliotecaDAO.countEmprestimosAtivos();
            List<Biblioteca> livrosDisponiveis = bibliotecaDAO.findLivrosDisponiveis();
            List<Biblioteca> emprestimosAtrasados = bibliotecaDAO.findEmprestimosAtrasados();
            
            totalLivrosLabel.setText(String.valueOf(totalLivros));
            livrosDisponiveisLabel.setText(String.valueOf(livrosDisponiveis.size()));
            emprestimosAtivosLabel.setText(String.valueOf(emprestimosAtivos));
            emprestimosAtrasadosLabel.setText(String.valueOf(emprestimosAtrasados.size()));
            
            // Cores
            totalLivrosLabel.setForeground(Color.BLUE);
            livrosDisponiveisLabel.setForeground(new Color(0, 128, 0)); // Verde
            emprestimosAtivosLabel.setForeground(Color.ORANGE);
            emprestimosAtrasadosLabel.setForeground(Color.RED);
            
        } catch (Exception ex) {
            totalLivrosLabel.setText("Erro");
            livrosDisponiveisLabel.setText("Erro");
            emprestimosAtivosLabel.setText("Erro");
            emprestimosAtrasadosLabel.setText("Erro");
        }
    }
    
    public void refreshData() {
        logger.info("Iniciando refreshData() no BibliotecaPanel");
        try {
            logger.debug("Chamando bibliotecaDAO.findAll()");
            List<Biblioteca> itens = bibliotecaDAO.findAll();
            logger.info("Recebidos {} itens do DAO", itens.size());
            
            tableModel.setRowCount(0);
            logger.debug("TableModel limpo, começando a adicionar linhas");
            
            int linha = 0;
            for (Biblioteca biblioteca : itens) {
                linha++;
                Object[] row = {
                    biblioteca.getId(),
                    biblioteca.getTipo(),
                    biblioteca.getTitulo(),
                    biblioteca.getAutor(),
                    biblioteca.getStatus(),
                    biblioteca.getCategoria() != null ? biblioteca.getCategoria() : ""
                };
                tableModel.addRow(row);
                logger.debug("Linha {} adicionada: ID={}, Titulo={}, Status={}", 
                            linha, biblioteca.getId(), biblioteca.getTitulo(), biblioteca.getStatus());
            }
            logger.info("refreshData() concluído com sucesso: {} linhas adicionadas", linha);
        } catch (SQLException ex) {
            logger.error("Erro ao carregar itens no painel: {}", ex.getMessage(), ex);
            JOptionPane.showMessageDialog(this, "Erro ao carregar itens: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            logger.error("Erro inesperado ao carregar itens: {}", ex.getMessage(), ex);
            JOptionPane.showMessageDialog(this, "Erro inesperado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
