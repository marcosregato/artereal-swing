package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.BibliotecaDAO;
import com.artereal.swing.model.Biblioteca;
import com.artereal.swing.ui.layout.PadraoLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
        
        // Botões usando PadraoLayout com cores pastéis
        salvarButton = PadraoLayout.criarBotaoSalvar();
        novoButton = PadraoLayout.criarBotaoNovo();
        editarButton = PadraoLayout.criarBotaoEditar();
        excluirButton = PadraoLayout.criarBotaoExcluir();
        limparButton = PadraoLayout.criarBotaoLimpar();
        pesquisarButton = PadraoLayout.criarBotaoPesquisar();
        emprestarButton = PadraoLayout.criarBotao("Emprestar", new Color(255, 218, 185)); // Laranja pastel
        devolverButton = PadraoLayout.criarBotao("Devolver", new Color(152, 251, 152)); // Verde menta pastel
        pesquisarField = new JTextField(20);
        
        // Labels de estatísticas
        totalLivrosLabel = new JLabel("0");
        livrosDisponiveisLabel = new JLabel("0");
        emprestimosAtivosLabel = new JLabel("0");
        emprestimosAtrasadosLabel = new JLabel("0");
    }
    
    private void setupLayout() {
        // Aplicar layout padrão usando PadraoLayout
        PadraoLayout.aplicarLayoutPadrao(this);
        
        // Header usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader("📚 Biblioteca Maçônica", "Gestão de livros e empréstimos");
        add(headerPanel, BorderLayout.NORTH);
        
        // Painel principal com split
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(650);
        splitPane.setResizeWeight(0.6);
        
        // Painel esquerdo - Tabela
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
        leftPanel.setBackground(Color.WHITE);
        
        // Painel de pesquisa simplificado
        JPanel pesquisaPanel = PadraoLayout.criarGrupoFormulario("🔍 Pesquisa");
        JPanel pesquisaContent = new JPanel(new BorderLayout());
        pesquisaContent.setBackground(Color.WHITE);
        pesquisaContent.add(new JLabel("Pesquisar:"), BorderLayout.WEST);
        pesquisaContent.add(pesquisarField, BorderLayout.CENTER);
        pesquisaPanel.add(pesquisaContent);
        
        // Tabela estilizada usando PadraoLayout
        PadraoLayout.configurarTabela(bibliotecaTable);
        JScrollPane tableScrollPane = new JScrollPane(bibliotecaTable);
        
        leftPanel.add(pesquisaPanel, BorderLayout.NORTH);
        leftPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Painel direito - Formulário simplificado
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        
        // Header do formulário usando PadraoLayout
        JPanel formHeaderPanel = PadraoLayout.criarHeader("📝 Detalhes do Livro", "");
        rightPanel.add(formHeaderPanel, BorderLayout.NORTH);
        
        // Formulário usando PadraoLayout
        System.out.println("[BIBLIOTECA_PANEL] Iniciando setupLayout do formulário BibliotecaPanel");
        JPanel formPanel = PadraoLayout.criarGrupoFormulario("📚 Dados do Livro");
        System.out.println("[BIBLIOTECA_PANEL] Grupo formulário criado: Dados do Livro");
        JPanel formContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        formContent.setBackground(Color.WHITE);
        System.out.println("[BIBLIOTECA_PANEL] Layout de formulário aplicado com criarLayoutFormulario()");
        
        // Adicionar campos básicos com estilização PadraoLayout
        formContent.add(PadraoLayout.criarLabelFormulario("🔢 Código:"));
        codigoField.setEditable(false);
        codigoField.setBackground(new Color(240, 240, 245));
        PadraoLayout.estilizarCampoTexto(codigoField);
        formContent.add(codigoField);
        
        formContent.add(PadraoLayout.criarLabelFormulario("📂 Tipo:"));
        PadraoLayout.estilizarComboBox(tipoCombo);
        formContent.add(tipoCombo);
        
        formContent.add(PadraoLayout.criarLabelFormulario("📖 Título:"));
        PadraoLayout.estilizarCampoTexto(tituloField);
        formContent.add(tituloField);
        
        formContent.add(PadraoLayout.criarLabelFormulario("✍️ Autor:"));
        PadraoLayout.estilizarCampoTexto(autorField);
        formContent.add(autorField);
        
        // ISBN com estilização PadraoLayout
        formContent.add(PadraoLayout.criarLabelFormulario("📋 ISBN:"));
        PadraoLayout.estilizarCampoTexto(isbnField);
        formContent.add(isbnField);
        
        // Editora com estilização PadraoLayout
        formContent.add(PadraoLayout.criarLabelFormulario("🏢 Editora:"));
        PadraoLayout.estilizarCampoTexto(editoraField);
        formContent.add(editoraField);
        
        // Ano com estilização PadraoLayout
        formContent.add(PadraoLayout.criarLabelFormulario("📅 Ano:"));
        PadraoLayout.estilizarCampoTexto(anoPublicacaoField);
        formContent.add(anoPublicacaoField);
        
        // Categoria com estilização PadraoLayout
        formContent.add(PadraoLayout.criarLabelFormulario("🏷️ Categoria:"));
        PadraoLayout.estilizarCampoTexto(categoriaField);
        formContent.add(categoriaField);
        
        // Localização com estilização PadraoLayout
        formContent.add(PadraoLayout.criarLabelFormulario("📍 Localização:"));
        PadraoLayout.estilizarCampoTexto(localizacaoField);
        formContent.add(localizacaoField);
        
        // Status com estilização PadraoLayout
        formContent.add(PadraoLayout.criarLabelFormulario("✅ Status:"));
        PadraoLayout.estilizarComboBox(statusCombo);
        formContent.add(statusCombo);
        
        formPanel.add(formContent);
        
        // Adicionar formulário com scroll usando PadraoLayout
        JScrollPane formScroll = PadraoLayout.criarFormularioComScroll(formPanel);
        rightPanel.add(formScroll, BorderLayout.CENTER);
        
        // Painel de botões simplificado
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botoesPanel.setBackground(Color.WHITE);
        botoesPanel.add(salvarButton);
        botoesPanel.add(novoButton);
        botoesPanel.add(editarButton);
        botoesPanel.add(excluirButton);
        botoesPanel.add(limparButton);
        botoesPanel.add(emprestarButton);
        botoesPanel.add(devolverButton);
        
        rightPanel.add(botoesPanel, BorderLayout.SOUTH);
        
        // Montar painel principal
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        add(splitPane, BorderLayout.CENTER);
        
                
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
        JButton confirmarButton = PadraoLayout.criarBotao("Confirmar", new Color(152, 251, 152)); // Verde menta
        JButton cancelarButton = PadraoLayout.criarBotao("Cancelar", new Color(255, 182, 193)); // Rosa pastel
        
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
